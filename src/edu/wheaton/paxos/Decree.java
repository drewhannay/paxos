package edu.wheaton.paxos;

import com.google.common.base.Preconditions;

public class Decree
{
	public Decree(DecreeType decreeType)
	{
		Preconditions.checkArgument(decreeType != DecreeType.OPAQUE_DECREE);

		m_decreeType = decreeType;
		m_value = null; 
	}

	public Decree(DecreeType decreeType, String value)
	{
		Preconditions.checkArgument(decreeType == DecreeType.OPAQUE_DECREE);

		m_decreeType = decreeType;
		m_value = value;
	}

	public DecreeType getDecreeType()
	{
		return m_decreeType;
	}

	public String getDecreeValue()
	{
		return m_value;
	}

	private final DecreeType m_decreeType;
	private final String m_value;
}
