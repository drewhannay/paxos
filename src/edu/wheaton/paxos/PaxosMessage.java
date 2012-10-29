package edu.wheaton.paxos;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class PaxosMessage
{
	public PaxosMessage(int messageId, int recipientId, List<Integer> quorum, Decree decree, Bag<Integer> ballot)
	{
		m_messageId = messageId;
		m_quorum = ImmutableList.copyOf(quorum);
		m_decree = decree;
		m_ballot = ballot;
		m_recipientId = recipientId;
	}

	public int getRecipientId()
	{
		return m_recipientId;
	}

	private final int m_messageId;
	private final ImmutableList<Integer> m_quorum;
	private final Decree m_decree;
	private final Bag<Integer> m_ballot;
	private final int m_recipientId;
}
