package edu.wheaton.paxos;

import java.util.Queue;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import edu.wheaton.utility.Bag;
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
			// TODO Auto-generated method stub
		}

		private void resign()
		{
			// TODO Auto-generated method stub
		}

		private void enter()
		{
			// TODO Auto-generated method stub
		}

		private void leave(boolean withAmnesia)
		{
			// TODO Auto-generated method stub
		}

		private void delay(int interval)
		{
			// TODO Auto-generated method stub
		}

		private void initiateProposal()
		{
			int messageId = Math.max(m_highestSeenNumber, m_promisedNumber) + 1;
			m_highestSeenNumber = messageId;

			if (m_id == m_leaderId)
			{
				// TODO If we're the leader, we should know about all the participants and use the appropriate quorum
			}
			else
			{
				// TODO What should the decree message be? Does it matter?
				// TODO Should the quorum be null in this case?
				PaxosMessage message = new PaxosMessage(messageId, m_leaderId,
						Lists.asList(Integer.valueOf(m_leaderId), null),
						new Decree(DecreeType.OPAQUE_DECREE, "Woo!"),
						new Bag<Integer>());
				m_sendMessageRunnable.run(message);
			}
		}

		private void receive(int interval)
		{
			// TODO Auto-generated method stub
		}
	});

	private final RunnableOfT<PaxosMessage> m_sendMessageRunnable;
	// housekeeping
	private final int m_id;
	private final Clock m_clock;

	// volatile state
	private final Queue<PaxosMessage> m_inbox;
//	private final List<Integer> m_participants;
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
