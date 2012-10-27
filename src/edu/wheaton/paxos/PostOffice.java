package edu.wheaton.paxos;

import java.util.List;
import java.util.Queue;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

public final class PostOffice
{
	public static void main(String[] args)
	{
		PostOffice postOffice = new PostOffice();
		postOffice.addParticipant();
	}

	public PostOffice()
	{
		m_eventQueue = Queues.newPriorityQueue();
		m_participants = Lists.newArrayList();
		m_queues = Lists.newArrayList();

		m_time = 0;
		m_thread.start();
	}

	private void addParticipant()
	{
		Participant participant = new Participant(m_sendMessageRunnable);
		m_participants.add(participant);
		Queue<PaxosMessage> queue = Queues.newPriorityQueue();
		m_queues.add(queue);
	}

	private final Thread m_thread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (true)
			{
				// TODO Fill out this loop
			}
		}
	});

	private final RunnableOfT<PaxosMessage> m_sendMessageRunnable = new RunnableOfT<PaxosMessage>()
	{
		@Override
		public void run(PaxosMessage t)
		{
			// TODO Auto-generated method stub
		}
	};

	private final Queue<PaxosEvent> m_eventQueue;
	private final List<Participant> m_participants;
	private final List<Queue<PaxosMessage>> m_queues;

	private long m_time;
}
