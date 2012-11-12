package edu.wheaton.paxos;

import com.google.common.base.Preconditions;

public class Decree
{
	public static final int NO_ID = -1;

	private Decree(int id, DecreeType decreeType, String value, int logId)
	{
		m_id = id;
		m_decreeType = decreeType;
		m_value = value;
		m_logId = logId;
	}

	public static Decree createRequestLogDecree(int logId)
	{
		return new Decree(NO_ID, DecreeType.REQUEST_LOG, null, logId);
	}

	public static Decree createSendLogDecree(String log, int logId)
	{
		return new Decree(NO_ID, DecreeType.SEND_LOG, log, logId);
	}

	public static Decree createOpaqueDecree(int decreeId, String value)
	{
		return new Decree(decreeId, DecreeType.OPAQUE_DECREE, value, NO_LOG_ID);
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

	private static final int NO_LOG_ID = -1;

	private final int m_id;
	private final DecreeType m_decreeType;
	private final String m_value;
	private final int m_logId;
}
