package edu.wheaton.paxos.logic;

import java.util.List;
import java.util.Queue;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import edu.wheaton.paxos.logic.PaxosListeners.ParticipantDetailsListener;
import edu.wheaton.paxos.utility.RunnableOfT;

public final class PostOffice
{
	public PostOffice(RunnableOfT<String> updateTimeDisplayRunnable, RunnableOfT<Integer> removeParticipantRunnable)
	{
		m_eventQueue = Queues.newPriorityBlockingQueue();
		m_participants = Lists.newArrayList();
		m_updateTimeDisplayRunnable = updateTimeDisplayRunnable;
		m_removeParticipantRunnable = removeParticipantRunnable;

		m_lock = new Object();
		m_time = 0;
		m_mainThread.start();
	}

	public void addParticipant(int participantId)
	{
		List<Integer> participantIds = Lists.newArrayList();
		for (Participant participant : m_participants)
			participantIds.add(participant.getId());
		m_participants.add(new Participant(participantId, m_sendMessageRunnable, m_onResignationRunnable, participantIds));
	}

	public void addParticipantDetailsListener(int participantId, ParticipantDetailsListener listener)
	{
		for (Participant participant : m_participants)
		{
			if (participant.getId() == participantId)
			{
				participant.addParticipantDetailsListener(listener);
				return;
			}
		}
	}

	public void removeParticipantDetailsListener(int participantId, ParticipantDetailsListener listener)
	{
		for (Participant participant : m_participants)
		{
			if (participant.getId() == participantId)
			{
				participant.removeParticipantDetailsListener(listener);
				return;
			}
		}
	}

	public void addEvent(PaxosEvent event)
	{
		m_eventQueue.add(event);
	}

	/**
	 * Toggle the pause state of the simulation
	 * @return The new pause state of the simulation
	 */
	public boolean togglePauseState()
	{
		sendCommand(m_participants, CommandMessage.TOGGLE_PAUSE_STATE);
		m_paused = !m_paused;
		if (!m_paused)
		{
			synchronized (m_lock)
			{
				m_lock.notifyAll();
			}
		}
		return m_paused;
	}

	public void sendEnterMessage(int participantId)
	{
		sendCommand(participantId, CommandMessage.ENTER);
	}

	public void sendLeaveMessage(int participantId, boolean withAmnesia)
	{
		if (withAmnesia)
			sendCommand(participantId, CommandMessage.LEAVE_WITH_AMNESIA);
		else
			sendCommand(participantId, CommandMessage.LEAVE);
	}

	public void sendResignMessage(int participantId)
	{
		sendCommand(participantId, CommandMessage.RESIGN);
	}

	private void sendCommand(int participantId, CommandMessage command)
	{
		for (Participant participant : m_participants)
		{
			if (participant.getId() == participantId)
			{
				participant.executeCommand(command);
				return;
			}
		}
	}

	private void sendCommand(List<Participant> participants, CommandMessage command)
	{
		for (Participant participant : participants)
			participant.executeCommand(command);
	}

	private final Thread m_mainThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (!m_stopped)
			{
				while (!m_paused)
				{
					if (m_eventQueue.isEmpty())
					{
						try
						{
							Thread.sleep(100);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						PaxosEvent event = m_eventQueue.poll();
						m_time = event.getTime();  //TODO: NPE here, sometimes
						m_updateTimeDisplayRunnable.run(Long.toString(m_time));
						PaxosMessage message = event.getMessage();
						int id = message.getRecipientId();
						for (Participant participant : m_participants)
						{
							if (participant.getId() == id)
								participant.receiveMessage(message);
						}
					}
				}

				synchronized (m_lock)
				{
                    try
                    {
                    	m_lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                    	Thread.currentThread().interrupt();
                    	return;
                    }
                }
			}
		}
	});

	private final RunnableOfT<PaxosMessage> m_sendMessageRunnable = new RunnableOfT<PaxosMessage>()
	{
		@Override
		public void run(PaxosMessage message)
		{
			addEvent(new PaxosEvent(m_time + DELAY, message));
		}
	};

	private final RunnableOfT<Integer> m_onResignationRunnable = new RunnableOfT<Integer>()
	{
		@Override
		public void run(Integer participantId)
		{
			for (int i = 0; i < m_participants.size(); i++)
			{
				if (m_participants.get(i).getId() == participantId)
				{
					m_participants.remove(i);
					break;
				}
			}
			m_removeParticipantRunnable.run(participantId);
		};
	};

	private static final int DELAY = 1;

	private final Queue<PaxosEvent> m_eventQueue;
	private final List<Participant> m_participants;
	private final Object m_lock;
	private final RunnableOfT<String> m_updateTimeDisplayRunnable;
	private final RunnableOfT<Integer> m_removeParticipantRunnable;

	private volatile boolean m_stopped;
	private volatile boolean m_paused;

	private long m_time;
}
