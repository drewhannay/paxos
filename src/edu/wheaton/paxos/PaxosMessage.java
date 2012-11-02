package edu.wheaton.paxos;

import edu.wheaton.utility.Bag;

public class PaxosMessage
{
	public static final int NO_ID = -1;

	public PaxosMessage(int messageId, int senderId, int recipientId, Decree decree)
	{
		m_messageId = messageId;
		m_senderId = senderId;
		m_recipientId = recipientId;
		m_quorum = new Bag<Integer>();
		m_decree = decree;
		m_ballot = new Bag<Integer>();
	}

	public int getMessageId()
	{
		return m_messageId;
	}

	public int getSenderId()
	{
		return m_senderId;
	}

	public int getRecipientId()
	{
		return m_recipientId;
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

	private final int m_messageId;
	private final int m_senderId;
	private final int m_recipientId;
	private final Bag<Integer> m_quorum;
	private final Decree m_decree;
	private final Bag<Integer> m_ballot;
}
