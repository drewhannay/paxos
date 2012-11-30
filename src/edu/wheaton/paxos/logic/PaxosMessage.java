package edu.wheaton.paxos.logic;

import edu.wheaton.paxos.utility.Bag;

public class PaxosMessage
{
	public PaxosMessage(int senderId, int recipientId, Decree decree)
	{
		m_senderId = senderId;
		m_recipientId = recipientId;
		m_quorum = new Bag<Integer>();
		m_decree = decree;
		m_ballot = new Bag<Integer>();
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

	private final int m_senderId;
	private final int m_recipientId;
	private final Bag<Integer> m_quorum;
	private final Decree m_decree;
	private final Bag<Integer> m_ballot;
}
