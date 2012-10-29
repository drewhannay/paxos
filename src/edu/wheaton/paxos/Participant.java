package edu.wheaton.paxos;

import java.util.List;
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
		case PAUSE:
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
			while (true)
			{
				// things a participant does:
				// join
				// resign
				// enter
				// leave (amnesia?)
				// delay (interval)
				// initiate proposal
				initiateProposal();
				// receive (interval)
			}
		}

		private void initiateProposal()
		{
//			int messageId = Math.max(m_highestSeenNumber, m_promisedNumber) + 1;
//			PaxosMessage message = new PaxosMessage(messageId, m_leaderId, quorum, new Decree(DecreeType.OPAQUE_DECREE, "Woo!"), new Bag<Integer>());
//			m_highestSeenNumber = messageId;
		}
	});

	private final RunnableOfT<PaxosMessage> m_sendMessageRunnable;
	// housekeeping
	private final int m_id;
	private final Clock m_clock;

	// volatile state
	private final Queue<PaxosMessage> m_inbox;
//	private final List<Integer> m_participants;

	// Paxos state (persistent)
//	private File m_eventLog;
	private int m_promisedNumber;
	private int m_highestSeenNumber;

	// Paxos-maintained state
	private int m_leaderId;
}
