package edu.wheaton.paxos.logic;

import java.util.List;
import java.util.Queue;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import edu.wheaton.paxos.utility.RunnableOfT;

public final class PostOffice
{
	public PostOffice()
	{
		m_eventQueue = Queues.newPriorityQueue();
		m_participants = Lists.newArrayList();

		m_lock = new Object();
		m_time = 0;
		m_mainThread.start();
	}

	public void addParticipant(int participantId)
	{
		Participant participant = new Participant(participantId, m_sendMessageRunnable);
		m_participants.add(participant);
	}

	public void addEvent(PaxosEvent event)
	{
		m_eventQueue.add(event);
	}

//	private void sendCommand(ImmutableSet<Participant> participants, CommandMessage command)
//	{
//		for (Participant participant : participants)
//			participant.executeCommand(command);
//	}

	private final Thread m_mainThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (!m_stopped)
			{
				while (!m_paused)
				{
					if (m_eventQueue.isEmpty())
					{
						try
						{
							Thread.sleep(100);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						PaxosEvent event = m_eventQueue.poll();
						m_time = event.getTime();
						PaxosMessage message = event.getMessage();
						int id = message.getRecipientId();
						for (Participant participant : m_participants)
						{
							if (participant.getId() == id)
								participant.receiveMessage(message);
						}
					}
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
	});

	private final RunnableOfT<PaxosMessage> m_sendMessageRunnable = new RunnableOfT<PaxosMessage>()
	{
		@Override
		public void run(PaxosMessage message)
		{
			// TODO: Randomize success
			m_eventQueue.add(new PaxosEvent(m_time + DELAY, message));
		}
	};

	private static final int DELAY = 10;

	private final Queue<PaxosEvent> m_eventQueue;
	private final List<Participant> m_participants;
	private final Object m_lock;

	private volatile boolean m_stopped;
	private volatile boolean m_paused;

	private long m_time;
}
