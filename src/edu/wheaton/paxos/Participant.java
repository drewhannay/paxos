package edu.wheaton.paxos;

import java.util.Queue;

import com.google.common.collect.Queues;

public final class Participant
{
	public Participant(RunnableOfT<PaxosMessage> sendMessageRunnable)
	{
		m_sendMessageRunnable = sendMessageRunnable;
		m_id = 0;
		m_clock = new Clock();

		m_inbox = Queues.newPriorityQueue();
		m_thread.start();
	}

	public void commandNotify(CommandMessage message)
	{
		switch (message)
		{
		//process
		}
	}

	private final Thread m_thread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (true)
			{
				while (!m_inbox.isEmpty())
				{
					//process message
				}

				// randomly choose whether or not to propose a message
				// if you're the leader, make the proposal, otherwise
				// ask the leader
			}
		}
	});
	


	private final RunnableOfT<PaxosMessage> m_sendMessageRunnable;
	// housekeeping
	private final int m_id;
	private final Clock m_clock;

	// volatile state
	private final Queue<PaxosMessage> m_inbox;

	// Paxos state (persistent)
//	private File m_eventLog;
	private int m_promisedNumber;
	private int m_highestSeenNumber;

	// Paxos-maintained state
	private int m_leaderId;
}
