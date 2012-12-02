package edu.wheaton.paxos.logic;

import java.io.Closeable;
import java.util.List;

import com.google.common.collect.Lists;

import edu.wheaton.paxos.utility.RunnableOfT;

public final class Participant implements Closeable
{
	public Participant(int id, RunnableOfT<PaxosMessage> sendMessageRunnable)
	{
		m_sendMessageRunnable = sendMessageRunnable;
		m_id = id;
		
		m_clock = new Clock();
		m_log = PaxosLogManager.createPaxosLog(m_id);

		m_lock = new Object();
		m_stopped = false;
		m_paused = false;

		m_inbox = PaxosMessageQueueManager.createPaxosMessageQueue(m_id);
		m_participants = Lists.newArrayList();
		m_mainThread.start();
	}

	@Override
	public void close()
	{
		PaxosLogManager.closeLog(m_id);
		PaxosMessageQueueManager.closeQueue(m_id);
	}

	public int getId()
	{
		return m_id;
	}

	public void receiveMessage(PaxosMessage message)
	{
		m_inbox.add(message);
	}

	public void executeCommand(CommandMessage command)
	{
		switch (command)
		{
		case TOGGLE_PAUSE_STATE:
			m_paused = !m_paused;
			if (!m_paused)
			{
				synchronized (m_lock)
				{
					m_lock.notifyAll();
				}
			}
			break;
		case ENTER:
		case LEAVE:
			// TODO: call leave()
		case SHOW:
		case HIDE:
		}
	}

	private final Thread m_mainThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (!m_stopped)
			{
				while (!m_paused)
				{
					// TODO: pick one of these
					// Note: if you have "left", your only options should be enter() or delay()

//					// join
//					join();
//					// resign
//					resign();
//					// enter
//					enter();
//					// leave (amnesia?)
//					leave(true);
//					// delay
//					delay(10);
//					// initiate proposal
//					initiateProposal();
//					// receive (interval)
					receive(10);
				}

				synchronized (m_lock)
				{
                    try
                    {
                    	m_lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                    	Thread.currentThread().interrupt();
                    	return;
                    }
                }
			}
		}

		private void join()
		{
			Decree decree = Decree.createRequestLogDecree(m_log.getLatestLogId());
			for (Integer recipientId : m_participants)
				m_sendMessageRunnable.run(new PaxosMessage(m_id, recipientId.intValue(), decree));			
		}

		private void resign()
		{
			// TODO
			Decree decree = null;// = new Decree(DecreeType.REMOVE_PARTICIPANT);
			if (m_id == m_leaderId)
			{
				// TODO messageId needs to go in the Decree
				int messageId = Math.max(m_highestSeenNumber, m_promisedNumber) + 1;
				m_highestSeenNumber = messageId;

				for (Integer recipientId : m_participants)
					m_sendMessageRunnable.run(new PaxosMessage(m_id, recipientId.intValue(), decree));
			}
			else
			{
				m_sendMessageRunnable.run(new PaxosMessage(m_id, m_leaderId, decree));
			}
		}

		private void enter()
		{
			Decree decree = Decree.createRequestLogDecree(m_log.getLatestLogId());
			for (Integer recipientId : m_participants)
				m_sendMessageRunnable.run(new PaxosMessage(m_id, recipientId.intValue(), decree));
		}

		private void leave(boolean withAmnesia)
		{
			m_isPresent = false;

			if (withAmnesia)
			{
				m_inbox.clear();
				m_participants.clear();
			}
		}

		private void delay(int interval)
		{
			try
			{
				Thread.sleep(interval);
			}
			catch (InterruptedException e)
			{
				System.out.println("delay()");
				e.printStackTrace();
			}
		}

		private void initiateProposal()
		{
			if (m_id == m_leaderId)
			{
				int messageId = Math.max(m_highestSeenNumber, m_promisedNumber) + 1;
				m_highestSeenNumber = messageId;

				Decree decree = Decree.createOpaqueDecree(messageId, "Leader Decree");
				for (Integer recipientId : m_participants)
					m_sendMessageRunnable.run(new PaxosMessage(m_id, recipientId.intValue(), decree));
			}
			else
			{
				PaxosMessage message = new PaxosMessage(m_id, m_leaderId, 
						Decree.createOpaqueDecree(Decree.NO_ID, "Woo!"));
				m_sendMessageRunnable.run(message);
			}
		}

		private void receive(int interval)
		{
			if (!m_inbox.isEmpty())
			{
				PaxosMessage message = m_inbox.poll();
				Decree decree = message.getDecree();
				Decree responseDecree;
				switch (decree.getDecreeType())
				{
				case OPAQUE_DECREE:
					m_log.recordDecree(decree);
					System.out.println(decree.toString());
					break;
				case ADD_PARTICIPANT:
					break;
				case REMOVE_PARTICIPANT:
//					int removeId = Integer.parseInt(decree.getDecreeValue());
//					m_participants.remove(removeId);
					break;
				case SET_LEADER:
					m_leaderId = Integer.parseInt(decree.getDecreeValue());
					m_leaderInterval = decree.getInterval();
					m_log.recordDecree(decree);
					// TODO maintain interval
					break;
				case REQUEST_LOG:
					if (m_log.getLatestLogId() > decree.getLogId())
					{
						responseDecree = Decree.createSendLogDecree(m_log.getLogSinceId(decree.getLogId()), m_log.getLatestLogId());
						m_sendMessageRunnable.run(new PaxosMessage(m_id, message.getSenderId(), responseDecree));
					}
					break;
				case SEND_LOG:
					m_log.update(decree.getDecreeValue());
					if (m_log.getLatestLogId() < decree.getLogId())
					{
						responseDecree = Decree.createRequestLogDecree(m_log.getLatestLogId());
						m_sendMessageRunnable.run(new PaxosMessage(m_id, message.getSenderId(), responseDecree));
					}
					break;
				}
			}
			else
			{
				if (interval > 0)
				{
					try
					{
						Thread.sleep(1);
						receive(interval - 1);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					return;
				}
			}
		}
	});

	private final RunnableOfT<PaxosMessage> m_sendMessageRunnable;
	// housekeeping
	private final int m_id;
	private final Clock m_clock;
	private boolean m_hasJoined;
	private boolean m_isPresent;

	// volatile state
	private final PaxosQueue<PaxosMessage> m_inbox;
	private final List<Integer> m_participants;

	private final Object m_lock;
	private volatile boolean m_stopped;
	private volatile boolean m_paused;

	// Paxos state (persistent)
	private final PaxosLog m_log;
	private int m_promisedNumber;
	private int m_highestSeenNumber;

	// Paxos-maintained state
	private int m_leaderId;
	private int m_leaderInterval;
}
