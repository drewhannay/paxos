package edu.wheaton.paxos;

import java.util.List;
import java.util.Queue;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

public final class PostOffice
{
	public static void main(String[] args)
	{
		PostOffice postOffice = new PostOffice();
		postOffice.addParticipant();
		postOffice.addParticipant();
		postOffice.addParticipant();
	}

	public PostOffice()
	{
		m_eventQueue = Queues.newPriorityQueue();
		m_participants = Lists.newArrayList();
//		m_queues = Lists.newArrayList();

		m_time = 0;
		m_mainThread.start();
	}

	private void addParticipant()
	{
		Participant participant = new Participant(m_sendMessageRunnable);
		m_participants.add(participant);
//		Queue<PaxosMessage> queue = Queues.newPriorityQueue();
//		m_queues.add(queue);
	}

	private void sendCommand(ImmutableSet<Participant> participants, CommandMessage command)
	{
		for (Participant participant : participants)
			participant.executeCommand(command);
	}

	private final Thread m_mainThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (true)
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
//	private final List<Queue<PaxosMessage>> m_queues;

	private long m_time;
}
