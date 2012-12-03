package edu.wheaton.paxos.logic;

import edu.wheaton.paxos.utility.Bag;

public class PaxosMessage
{
	private PaxosMessage(PaxosMessageType messageType, int senderId, int recipientId, int logId, String logData, Decree decree)
	{
		m_messageType = messageType;
		m_senderId = senderId;
		m_recipientId = recipientId;
		m_logId = logId;
		m_logData = logData;
		m_quorum = new Bag<Integer>();
		m_decree = decree;
		m_ballot = new Bag<Integer>();
	}

	public static PaxosMessage createDecreeCommitMessage(int senderId, int recipientId, Decree decree)
	{
		return new PaxosMessage(PaxosMessageType.DECREE_COMMIT, senderId, recipientId, NO_LOG_ID, null, decree);
	}

	public static PaxosMessage createDecreeRequestMessage(int senderId, int recipientId, Decree decree)
	{
		return new PaxosMessage(PaxosMessageType.DECREE_REQUEST, senderId, recipientId, NO_LOG_ID, null, decree);
	}

	public static PaxosMessage createRequestLogMessage(int senderId, int recipientId, int logId)
	{
		return new PaxosMessage(PaxosMessageType.REQUEST_LOG, senderId, recipientId, logId, null, null);
	}

	public static PaxosMessage createSendLogMessage(int senderId, int recipientId, String logData, int logId)
	{
		return new PaxosMessage(PaxosMessageType.SEND_LOG, senderId, recipientId, logId, logData, null);
	}

	public PaxosMessageType getMessageType()
	{
		return m_messageType;
	}

	public int getSenderId()
	{
		return m_senderId;
	}

	public int getRecipientId()
	{
		return m_recipientId;
	}

	public int getLogId()
	{
		return m_logId;
	}

	public String getLogData()
	{
		return m_logData;
	}

	public Bag<Integer> getQuorum()
	{
		return m_quorum;
	}

	public Decree getDecree()
	{
		return m_decree;
	}

	public Bag<Integer> getBallot()
	{
		return m_ballot;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(Integer.toString(m_senderId));
		builder.append(Decree.DELIMITER);
		builder.append(Integer.toString(m_recipientId));
		builder.append(Decree.DELIMITER);
		builder.append(m_quorum.toString());
		builder.append(Decree.DELIMITER);
		builder.append(m_decree.toString());
		builder.append(Decree.DELIMITER);
		builder.append(m_ballot.toString());

		return builder.toString();
	}

	private static final int NO_LOG_ID = -1;

	private final PaxosMessageType m_messageType;
	private final int m_senderId;
	private final int m_recipientId;
	private final int m_logId;
	private final String m_logData;
	private final Bag<Integer> m_quorum;
	private final Decree m_decree;
	private final Bag<Integer> m_ballot;
}
