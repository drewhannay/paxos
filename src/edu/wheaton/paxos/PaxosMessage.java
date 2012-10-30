package edu.wheaton.paxos;

import java.util.List;

import com.google.common.collect.ImmutableList;

import edu.wheaton.utility.Bag;

public class PaxosMessage
{
	public PaxosMessage(int messageId, int recipientId, List<Integer> quorum, Decree decree, Bag<Integer> ballot)
	{
		m_messageId = messageId;
		m_recipientId = recipientId;
		m_quorum = ImmutableList.copyOf(quorum);
		m_decree = decree;
		m_ballot = ballot;
	}

	public int getMessageId()
	{
		return m_messageId;
	}

	public int getRecipientId()
	{
		return m_recipientId;
	}

	public ImmutableList<Integer> getQuorum()
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
	private final int m_recipientId;
	private final ImmutableList<Integer> m_quorum;
	private final Decree m_decree;
	private final Bag<Integer> m_ballot;
}
