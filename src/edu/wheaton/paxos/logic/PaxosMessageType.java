package edu.wheaton.paxos.logic;

public enum PaxosMessageType
{
	PREPARE, // sent only by the leader to prepare for a decree
	DECREE_COMMIT, // sent only by the leader after a decree has passed voting
	DECREE_REQUEST, // sent only to the leader
	ACCEPT, // sent by participants to vote on decrees
	REJECT, // sent by participants to vote on decrees
	REQUEST_LOG,
	SEND_LOG;
}
