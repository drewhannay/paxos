package edu.wheaton.paxos;

import java.util.List;
import java.util.Queue;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import edu.wheaton.utility.RunnableOfT;

public final class Participant
{
	public Participant(RunnableOfT<PaxosMessage> sendMessageRunnable)
	{
		m_sendMessageRunnable = sendMessageRunnable;
		m_id = 0;
		
		m_clock = new Clock();

		m_lock = new Object();
		m_stopped = false;
		m_paused = false;

		m_inbox = Queues.newPriorityQueue();
		m_participants = Lists.newArrayList();
		m_mainThread.start();
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
		case START:
			if (!m_paused)
				break;
			m_paused = false;
			synchronized (m_lock)
			{
				m_lock.notifyAll();
			}
			break;
		case PAUSE:
			m_paused = true;
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

					// join
					join();
					// resign
					resign();
					// enter
					enter();
					// leave (amnesia?)
					leave(true);
					// delay
					delay(10);
					// initiate proposal
					initiateProposal();
					// receive (interval)
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
			Decree decree = new Decree(DecreeType.REQUEST_LOG);
			for (Integer recipientId : m_participants)
				m_sendMessageRunnable.run(new PaxosMessage(PaxosMessage.NO_ID, m_id, recipientId.intValue(), decree));			
		}

		private void resign()
		{
			Decree decree = new Decree(DecreeType.REMOVE_PARTICIPANT);
			if (m_id == m_leaderId)
			{
				int messageId = Math.max(m_highestSeenNumber, m_promisedNumber) + 1;
				m_highestSeenNumber = messageId;

				for (Integer recipientId : m_participants)
					m_sendMessageRunnable.run(new PaxosMessage(messageId, m_id, recipientId.intValue(), decree));
			}
			else
			{
				m_sendMessageRunnable.run(new PaxosMessage(PaxosMessage.NO_ID, m_id, m_leaderId, decree));
			}
		}

		private void enter()
		{
			Decree decree = new Decree(DecreeType.REQUEST_LOG);
			for (Integer recipientId : m_participants)
				m_sendMessageRunnable.run(new PaxosMessage(PaxosMessage.NO_ID, m_id, recipientId.intValue(), decree));
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

				Decree decree = new Decree(DecreeType.OPAQUE_DECREE, "Leader Decree");
				for (Integer recipientId : m_participants)
					m_sendMessageRunnable.run(new PaxosMessage(messageId, m_id, recipientId.intValue(), decree));
			}
			else
			{
				PaxosMessage message = new PaxosMessage(PaxosMessage.NO_ID, m_id, m_leaderId, 
						new Decree(DecreeType.OPAQUE_DECREE, "Woo!"));
				m_sendMessageRunnable.run(message);
			}
		}

		private void receive(int interval)
		{
			if (!m_inbox.isEmpty())
			{
				//TODO
			}
			else
			{
				//TODO
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
	private final Queue<PaxosMessage> m_inbox;
	private final List<Integer> m_participants;

	private final Object m_lock;
	private volatile boolean m_stopped;
	private volatile boolean m_paused;

	// Paxos state (persistent)
//	private File m_eventLog;
	private int m_promisedNumber;
	private int m_highestSeenNumber;

	// Paxos-maintained state
	private int m_leaderId;
}
