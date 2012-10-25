package edu.wheaton.paxos;

public class Decree
{
	public Decree(DecreeType decreeType, String value)
	{
		m_decreeType = decreeType;
		m_value = value;
	}

	private final DecreeType m_decreeType;
	private final String m_value;
}
