package edu.wheaton.paxos;

public class Decree
{
	public Decree(DecreeType decreeType, String value)
	{
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
