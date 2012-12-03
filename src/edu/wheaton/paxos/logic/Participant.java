package edu.wheaton.paxos.logic;

import java.io.Closeable;
import java.util.List;

import com.google.common.collect.Lists;

import edu.wheaton.paxos.logic.PaxosListeners.ParticipantDetailsListener;
import edu.wheaton.paxos.utility.RunnableOfT;

public final class Participant implements Closeable
{
	public Participant(int id, RunnableOfT<PaxosMessage> sendMessageRunnable)
	{
		m_sendMessageRunnable = sendMessageRunnable;
		m_listeners = Lists.newArrayList();
		m_id = id;
		m_leaderId = 1;
		
		m_clock = new Clock();
		m_log = PaxosLogManager.createPaxosLog(m_id);

		m_lock = new Object();
		m_stopped = false;
		m_paused = false;

		m_inbox = PaxosMessageQueueManager.createPaxosMessageQueue(m_id);
		m_participants = Lists.newCopyOnWriteArrayList();
		m_mainThread.start();
	}

	@Override
	public void close()
	{
		PaxosLogManager.closeLog(m_id);
		PaxosMessageQueueManager.closeQueue(m_id);
	}

	// TODO: we shouldn't need this method...make it go away
	public void addParticipant(int participantId)
	{
		m_participants.add(participantId);
	}

	public void addParticipantDetailsListener(ParticipantDetailsListener listener)
	{
		m_listeners.add(listener);
	}

	public void removeParticipantDetailsListener(ParticipantDetailsListener listener)
	{
		m_listeners.remove(listener);
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

					double choice = Math.random();
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
					if (choice < 0.1 && m_leaderId == m_id)
						initiateProposal();
//					// receive (interval)
					else
						receive(100);

					updateDetails();
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

				Decree decree = Decree.createOpaqueDecree(messageId, "Leader-Initiated Decree");
				m_log.recordDecree(decree);
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
					// TODO if you're the leader and you're receiving a request to send a decree, what do we do here?
					m_log.recordDecree(decree);
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
			}
		}
	});

	private void updateDetails()
	{
		for (int i = 0; i < m_listeners.size(); i++)
		{
			StringBuilder builder = new StringBuilder();
			builder.append("Leader ID: ")
			.append(m_leaderId)
			.append('\n')
			.append("Leader Interval: ")
			.append(m_leaderInterval)
			.append('\n')
			.append("Participant List: ")
			.append(m_participants.toString())
			.append('\n')
			.append("Promised Number: ")
			.append(m_promisedNumber)
			.append('\n')
			.append("Highest Seen Number: ")
			.append(m_highestSeenNumber)
			.append('\n')
			.append("Joined: ")
			.append(m_hasJoined)
			.append('\n')
			.append("Present: ")
			.append(m_isPresent)
			.append('\n');
			

			String participantDetails = builder.toString();
			m_listeners.get(i).onParticipantDetailsUpdate(participantDetails);
		}
	}

	private final RunnableOfT<PaxosMessage> m_sendMessageRunnable;
	private final List<ParticipantDetailsListener> m_listeners;
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
