package edu.wheaton.paxos.logic;

public final class PaxosEvent implements Comparable<PaxosEvent>
{
	public PaxosEvent(long time, PaxosMessage message)
	{
		m_time = time;
		m_message = message;
	}

	public long getTime()
	{
		return m_time;
	}

	public PaxosMessage getMessage()
	{
		return m_message;
	}

	@Override
	public int compareTo(PaxosEvent o)
	{
		return (int) (m_time - o.getTime());
	}

	private final long m_time;
	private final PaxosMessage m_message;
}
