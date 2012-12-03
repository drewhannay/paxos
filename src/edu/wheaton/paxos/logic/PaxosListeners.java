package edu.wheaton.paxos.logic;

public final class PaxosListeners
{
	private PaxosListeners() { }

	public interface LogUpdateListener
	{
		public void onLogUpdate(String updatedLog);
	}

	public interface QueueUpdateListener
	{
		public void onQueueUpdate(String queueContents);
	}

	public interface ParticipantDetailsListener
	{
		public void onParticipantDetailsUpdate(String participantDetails);
	}

}
