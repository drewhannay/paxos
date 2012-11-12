package edu.wheaton.paxos;

public class PaxosLog
{
	public int getFirstLogId()
	{
		// TODO
		return 0;
	}

	public int getFirstUnknownId()
	{
		return getLatestLogId() + 1;
	}

	public void recordDecree(int id, Decree decree)
	{
		// TODO
	}

	public Decree readDecree(int id)
	{
		// TODO
		return null;
	}
	
	public int getLatestLogId()
	{
		// TODO
		return 0;
	}

	public String getLogSinceId(int logId)
	{
		// TODO
		return "";
	}

	public void update(String log)
	{
		
	}
}
