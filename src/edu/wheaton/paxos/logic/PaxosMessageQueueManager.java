package edu.wheaton.paxos.logic;

import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import edu.wheaton.paxos.logic.PaxosListeners.QueueUpdateListener;

public final class PaxosMessageQueueManager
{
	public static PaxosQueue<PaxosMessage> createPaxosMessageQueue(int participantId)
	{
		Queue<PaxosMessage> innerQueue = Queues.newArrayDeque();
		PaxosQueue<PaxosMessage> queue = new PaxosQueue<PaxosMessage>(innerQueue);
		m_queues.put(Integer.valueOf(participantId), queue);

		return queue;
	}

	public static void addQueueUpdateListener(int participantId, QueueUpdateListener listener)
	{
		m_queues.get(Integer.valueOf(participantId)).addQueueUpdateListener(listener);
	}

	public static void removeQueueUpdateListener(int participantId, QueueUpdateListener listener)
	{
		m_queues.get(Integer.valueOf(participantId)).removeQueueUpdateListener(listener);
	}

	private PaxosMessageQueueManager() { }

	private static final Map<Integer, PaxosQueue<PaxosMessage>> m_queues = Maps.newHashMap();
}
