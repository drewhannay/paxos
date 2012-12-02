package edu.wheaton.paxos.logic;

import java.util.Map;

import com.google.common.collect.Maps;

import edu.wheaton.paxos.logic.PaxosListeners.LogUpdateListener;

public final class PaxosLogManager
{
	public static PaxosLog createPaxosLog(int participantId)
	{
		PaxosLog log = new PaxosLog(participantId);
		m_logs.put(Integer.valueOf(participantId), log);

		return log;
	}

	public static void addLogUpdateListener(int participantId, LogUpdateListener listener)
	{
		m_logs.get(Integer.valueOf(participantId)).addLogUpdateListener(listener);
	}

	public static void removeLogUpdateListener(int participantId, LogUpdateListener listener)
	{
		m_logs.get(Integer.valueOf(participantId)).removeLogUpdateListener(listener);
	}

	public static void closeLog(int participantId)
	{
		m_logs.remove(Integer.valueOf(participantId));
	}

	private PaxosLogManager() { }

	private static final Map<Integer, PaxosLog> m_logs = Maps.newHashMap();
}
