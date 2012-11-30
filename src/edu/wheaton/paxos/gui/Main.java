package edu.wheaton.paxos.gui;

import edu.wheaton.paxos.logic.Decree;
import edu.wheaton.paxos.logic.PaxosEvent;
import edu.wheaton.paxos.logic.PaxosMessage;
import edu.wheaton.paxos.logic.PostOffice;

public class Main
{
    public static void main(String[] args)
    {
//    	new PostOfficeGUI();
    	PostOffice postOffice = new PostOffice();
		postOffice.addParticipant(m_participantIdGenerator++);
		postOffice.addParticipant(m_participantIdGenerator++);
		postOffice.addParticipant(m_participantIdGenerator++);
		postOffice.addEvent(new PaxosEvent(m_time++,
				new PaxosMessage(0, 1, Decree.createOpaqueDecree(0, "test"))));
		postOffice.addEvent(new PaxosEvent(m_time++, 
				new PaxosMessage(1, 2, Decree.createOpaqueDecree(0, "ignore me"))));
		postOffice.addEvent(new PaxosEvent(m_time++, 
				new PaxosMessage(1, 2, Decree.createOpaqueDecree(1, "test2"))));
    }

	private static int m_participantIdGenerator = 0;
	private static int m_time = 1;
}
