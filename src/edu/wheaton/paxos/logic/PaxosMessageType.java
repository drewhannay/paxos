package edu.wheaton.paxos.logic;

public enum PaxosMessageType
{
	DECREE_COMMIT, // sent only by the leader after a decree has passed voting
	DECREE_REQUEST, // sent only to the leader
	BALLOT, // TODO
	REQUEST_LOG,
	SEND_LOG;
}
