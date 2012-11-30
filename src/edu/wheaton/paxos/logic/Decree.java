package edu.wheaton.paxos.logic;

import com.google.common.base.Preconditions;

public class Decree
{
	public static final String DELIMITER = " ### ";
	public static final int NO_ID = -1;

	private Decree(int id, DecreeType decreeType, String value, int logId, int interval)
	{
		m_id = id;
		m_decreeType = decreeType;
		m_value = value;
		m_logId = logId;
		m_interval = interval;
	}

	public static Decree createSetLeaderDecree(int decreeId, int leaderId, int interval)
	{
		return new Decree(decreeId, DecreeType.SET_LEADER, Integer.toString(leaderId), NO_LOG_ID, interval);
	}

	public static Decree createRequestLogDecree(int logId)
	{
		return new Decree(NO_ID, DecreeType.REQUEST_LOG, null, logId, NO_INTERVAL);
	}

	public static Decree createSendLogDecree(String log, int logId)
	{
		return new Decree(NO_ID, DecreeType.SEND_LOG, log, logId, NO_INTERVAL);
	}

	public static Decree createOpaqueDecree(int decreeId, String value)
	{
		return new Decree(decreeId, DecreeType.OPAQUE_DECREE, value, NO_LOG_ID, NO_INTERVAL);
	}

	public int getDecreeId()
	{
		return m_id;
	}

	public DecreeType getDecreeType()
	{
		return m_decreeType;
	}

	public String getDecreeValue()
	{
		return m_value;
	}

	public int getLogId()
	{
		Preconditions.checkState(m_logId != NO_LOG_ID);

		return m_logId;
	}

	public int getInterval()
	{
		Preconditions.checkState(m_interval != NO_INTERVAL);

		return m_interval;
	}

	public static Decree fromString(String decreeString)
	{
		String[] fields = decreeString.split(DELIMITER);
		
		Preconditions.checkState(fields.length == 5);
		
		return new Decree(Integer.parseInt(fields[0]), DecreeType.valueOf(fields[1]), fields[2],
				Integer.parseInt(fields[3]), Integer.parseInt(fields[4]));
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(Integer.toString(m_id));
		builder.append(DELIMITER);
		builder.append(m_decreeType.toString());
		builder.append(DELIMITER);
		builder.append(m_value);
		builder.append(DELIMITER);
		builder.append(m_logId);
		builder.append(DELIMITER);
		builder.append(m_interval);
		
		return builder.toString();
	}

	private static final int NO_LOG_ID = -1;
	private static final int NO_INTERVAL = -1;

	private final int m_id;
	private final DecreeType m_decreeType;
	private final String m_value;
	private final int m_logId;
	private final int m_interval;
}
