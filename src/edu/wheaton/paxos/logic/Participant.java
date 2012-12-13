package edu.wheaton.paxos.logic;

import java.io.Closeable;
import java.util.List;
import java.util.Scanner;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import edu.wheaton.paxos.logic.PaxosListeners.ParticipantDetailsListener;
import edu.wheaton.paxos.logic.PaxosLog.LogState;
import edu.wheaton.paxos.utility.Bag;
import edu.wheaton.paxos.utility.RunnableOfT;

/**
 * Process of adding a Participant:
 * enter() // set m_isPresent to true, start learning
 * start looping main thread
 * try requestJoin() periodically until receiving an addParticipant Decree 
 * 		with m_id, set m_hasJoined to true
 * if leave()
 * 		set m_isPresent to false
 * 		eventually enter() and start catching up
 * 		if you get a removeParticipant decree for yourself while catching up,
 * 			someone must have resigned you while you were gone
 * if resign()
 * 		set m_isPresent = m_hasJoined = false
 * 		stop m_mainThread and die
 * 
 */
public final class Participant implements Closeable
{
	public Participant(int id, RunnableOfT<PaxosMessage> sendMessageRunnable, RunnableOfT<Integer> onResignationRunnable, List<Integer> potentialParticipantIds)
	{
		m_sendMessageRunnable = sendMessageRunnable;
		m_onResignationRunnable = onResignationRunnable;
		m_listeners = Lists.newArrayList();
		m_potentialParticipantIds = potentialParticipantIds;
		m_id = id;
		m_leaderId = 1;

//		m_clock = new Clock();
		m_log = PaxosLogManager.createPaxosLog(m_id);

		m_lock = new Object();
		m_stopped = false;
		m_paused = false;

		if (m_potentialParticipantIds.size() == 0)
		{
			m_highestSeenNumber = 1;
			Decree decree = Decree.createAddParticipantDecree(m_highestSeenNumber, m_id);
			m_log.recordDecree(LogState.COMMIT, decree);
			m_highestSeenNumber++;
			decree = Decree.createSetLeaderDecree(m_highestSeenNumber, m_id, LEADER_INTERVAL);
			m_leaderId = Integer.parseInt(decree.getDecreeValue());
//			m_leaderExpiry = decree.getLeaderExpiry();
			m_log.recordDecree(LogState.COMMIT, decree);
			m_hasJoined = true;
			m_isPresent = true;
		}

		m_inbox = PaxosMessageQueueManager.createPaxosMessageQueue(m_id);
		m_participantIds = Lists.newCopyOnWriteArrayList();
		m_mainThread.start();
	}

	@Override
	public void close()
	{
		PaxosLogManager.closeLog(m_id);
		PaxosMessageQueueManager.closeQueue(m_id);
	}

	public void addParticipantDetailsListener(ParticipantDetailsListener listener)
	{
		m_listeners.add(listener);
		updateDetails();
	}

	public void removeParticipantDetailsListener(ParticipantDetailsListener listener)
	{
		m_listeners.remove(listener);
	}

	public int getId()
	{
		return m_id;
	}

	public void receiveMessage(PaxosMessage message)
	{
		m_inbox.add(message);
	}

	public void executeCommand(CommandMessage command)
	{
		switch (command)
		{
		case TOGGLE_PAUSE_STATE:
			m_paused = !m_paused;
			if (!m_paused)
			{
				synchronized (m_lock)
				{
					m_lock.notifyAll();
				}
			}
			break;
		case ENTER:
			if (!m_isPresent)
				m_shouldEnter = true;
			break;
		case LEAVE:
			if (m_isPresent)
			{
				m_shouldLeave = true;
				m_withAmnesia = false;
			}
			break;
		case LEAVE_WITH_AMNESIA:
			if (m_isPresent)
			{
				m_shouldLeave = true;
				m_withAmnesia = true;
			}
			break;
		case RESIGN:
			if (m_isPresent && m_hasJoined)
				m_shouldResign = true;
			break;
		}
	}

	private final Thread m_mainThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			// when a new participant starts, it needs to enter immediately
			m_isPresent = true;
			for (Integer participantId : m_potentialParticipantIds)
				m_sendMessageRunnable.run(PaxosMessage.createRequestLogMessage(m_id, participantId, m_log.getFirstUnknownId()));

			while (!m_stopped)
			{
				while (!m_paused)
				{
					// Note: if you have "left", your only options should be enter() or delay()

					if (m_shouldEnter)
					{
						m_shouldEnter = false;
						enter();
						continue;
					}
					if (m_shouldLeave)
					{
						m_shouldLeave = false;
						leave(m_withAmnesia);
						continue;
					}
					if (m_shouldResign)
					{
						// TODO: if we set m_shouldResign to false, that means we only make one attempt to get our decree passed
						m_shouldResign = false;
						resign();
					}

					// cannot perform any of the following actions if you have "left"
					if (!m_isPresent)
					{
						updateDetails();
						continue;
					}

					double choice = Math.random();
					if (!m_hasJoined && choice < 0.05)
					{
						requestToJoin();
					}
					else if (m_hasJoined && !m_voteInProgress && choice < 0.1)
					{
						initiateProposal();
					}
					else if (choice < 0.5)
					{
						delay(DELAY_INTERVAL);
					}
					else
					{
						receive(DELAY_INTERVAL);
					}

					if (!m_stopped)
						updateDetails();
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
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

		private void requestToJoin()
		{
			Preconditions.checkArgument(!m_hasJoined);

			m_sendMessageRunnable.run(PaxosMessage.createDecreeRequestMessage(m_id, m_leaderId, Decree.createAddParticipantDecree(Decree.NO_ID, m_id)));
		}

		private void resign()
		{
			Preconditions.checkArgument(m_hasJoined);

			if (m_id == m_leaderId)
				return;

			Decree decree = Decree.createRemoveParticipantDecree(Decree.NO_ID, m_id);
			PaxosMessage message = PaxosMessage.createDecreeRequestMessage(m_id, m_leaderId, decree);
			m_sendMessageRunnable.run(message);
		}

		private void enter()
		{
			m_isPresent = true;
			learn();
		}

		private void learn()
		{
			for (Integer recipientId : m_participantIds)
				m_sendMessageRunnable.run(PaxosMessage.createRequestLogMessage(m_id, recipientId, m_log.getFirstUnknownId()));
		}

		private void leave(boolean withAmnesia)
		{
			m_isPresent = false;

			if (withAmnesia)
			{
				m_inbox.clear();
				m_participantIds.clear();
			}
		}

		private void delay(int interval)
		{
			try
			{
				Thread.sleep(interval);
			}
			catch (InterruptedException e)
			{
				System.out.println("delay()");
				e.printStackTrace();
			}
		}

		private void initiateProposal()
		{
			Decree decree = Decree.createOpaqueDecree(Decree.NO_ID, "Opaque Decree from " + Integer.toString(m_id));
			PaxosMessage message = PaxosMessage.createDecreeRequestMessage(m_id, m_leaderId, decree);
			m_sendMessageRunnable.run(message);
		}

		private void receive(int interval)
		{
			if (!m_inbox.isEmpty())
			{
				PaxosMessage message = m_inbox.poll();
				PaxosMessage responseMessage = null;
				Decree decree;
				switch (message.getMessageType())
				{
				case PREPARE:
					m_voteInProgress = true;

					decree = message.getDecree();
					// 1) ACCEPT/REJECT check if this is already in the log, if so reply as before
					if (decree.getDecreeId() <= m_log.getLastWrittenLogId())
					{
						LogState logState = m_log.findResponseForDecreeId(decree.getDecreeId());
						if (logState == LogState.ACCEPT)
							responseMessage = PaxosMessage.createAcceptMessage(m_id, m_leaderId, decree);
						else
							responseMessage = PaxosMessage.createRejectMessage(m_id, m_leaderId, decree);
					}
					// 2) Decree number is too high for your log. Go learn.
					else if (decree.getDecreeId() > m_log.getFirstUnknownId())
					{
						learn();
					}
					// 3) ACCEPT "completely okay", from the leader, etc...
					// and leader is not expired
					else if (message.getSenderId() == m_leaderId && decree.getDecreeId() > m_highestSeenNumber)
					{
						m_log.recordDecree(LogState.ACCEPT, decree);
						responseMessage = PaxosMessage.createAcceptMessage(m_id, m_leaderId, decree);
					}
					// 4) REJECT Definitely not acceptable
					else
					{
						m_log.recordDecree(LogState.REJECT, decree);
						responseMessage = PaxosMessage.createRejectMessage(m_id, m_leaderId, decree);
					}

					if (responseMessage != null)
						m_sendMessageRunnable.run(responseMessage);

					break;
				case DECREE_COMMIT:
					if (!processDecree(message.getDecree()))
						learn();
					break;
				case DECREE_REQUEST:
					if (m_id != m_leaderId || m_ballot != null)
					{
						if (m_id == m_leaderId)
						{
							if (!m_ballot.contains(message.getSenderId()))
								m_sendMessageRunnable.run(PaxosMessage.createPrepareMessage(m_id, message.getSenderId(), m_currentDecree));
						}
					}
					else
					{
						processDecreeRequest(message.getDecree());
					}
					break;
				case ACCEPT:
					if (m_id != m_leaderId || m_ballot == null)
						return;

					decree = message.getDecree();
					if (m_currentDecreeId == decree.getDecreeId() && m_participantIds.contains(message.getSenderId()))
					{
						m_ballot.add(message.getSenderId());
						checkForCompletedBallot(decree);
					}
					break;
				case REJECT:
					if (m_id != m_leaderId)
						return;

					m_ballot = null;
					decree = message.getDecree();
					if (m_currentDecreeId == decree.getDecreeId() && m_participantIds.contains(message.getSenderId()))
					{
						// this cannot happen while we have a fixed leader
						Preconditions.checkArgument(false);
					}
					break;
				case REQUEST_LOG:
					if (m_log.getLatestLogId() >= message.getFirstUnknownLogId())
					{
						responseMessage = PaxosMessage.createSendLogMessage(m_id, message.getSenderId(), 
								m_log.getLogSinceId(message.getFirstUnknownLogId())); 
						m_sendMessageRunnable.run(responseMessage);
					}
					break;
				case SEND_LOG:
					String logData = message.getLogData();
					Scanner scanner = new Scanner(logData);
					while (scanner.hasNextLine())
					{
						if (!processDecree(Decree.fromString(scanner.nextLine())))
							continue;
					}
					learn();
					break;
				}
			}
			else
			{
				if (interval > 0)
				{
					try
					{
						Thread.sleep(1);
						receive(interval - 1);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	});

	private boolean processDecree(Decree decree)
	{
		if (!m_log.recordDecree(LogState.COMMIT, decree))
			return false;

		m_voteInProgress = false;

		int participantId;
		m_highestSeenNumber = Math.max(m_highestSeenNumber, decree.getDecreeId());
		switch (decree.getDecreeType())
		{
		case OPAQUE_DECREE:
			// decree has already been recorded; nothing to do
			break;
		case ADD_PARTICIPANT:
			participantId = Integer.parseInt(decree.getDecreeValue());
			if (participantId != m_id)
				m_participantIds.add(participantId);
			else
				m_hasJoined = true;
			break;
		case REMOVE_PARTICIPANT:
			participantId = Integer.parseInt(decree.getDecreeValue());
			if (participantId != m_id)
			{
				m_participantIds.remove(Integer.valueOf(participantId));
				if (m_id == m_leaderId)
				{
					// make sure to inform the participant that he has been removed
					m_sendMessageRunnable.run(PaxosMessage.createDecreeCommitMessage(m_id, participantId, decree));
				}
			}
			else
			{
				m_hasJoined = false;
				// kill the main thread
				m_paused = true;
				m_stopped = true;
				m_onResignationRunnable.run(Integer.valueOf(m_id));
			}
			break;
		case SET_LEADER:
			m_leaderId = Integer.parseInt(decree.getDecreeValue());
//			m_leaderExpiry = decree.getLeaderExpiry();
			// TODO maintain interval
			break;
		}

		return true;
	}

	private boolean processDecreeRequest(Decree decree)
	{
		m_ballot = new Bag<Integer>();
		m_highestSeenNumber++;
		m_currentDecreeId = m_highestSeenNumber;
		int participantId;

		Decree proposedDecree = null;
		switch (decree.getDecreeType())
		{
		case OPAQUE_DECREE:
			proposedDecree = Decree.createOpaqueDecree(m_highestSeenNumber, decree.getDecreeValue());
			break;
		case ADD_PARTICIPANT:
			participantId = Integer.parseInt(decree.getDecreeValue());
			if (m_participantIds.contains(participantId))
			{
				m_ballot = null;
				m_highestSeenNumber--;
				return false;
			}
			proposedDecree = Decree.createAddParticipantDecree(m_highestSeenNumber, participantId);
			break;
		case REMOVE_PARTICIPANT:
			participantId = Integer.parseInt(decree.getDecreeValue());
			if (!m_participantIds.contains(participantId))
			{
				m_ballot = null;
				m_highestSeenNumber--;
				return false;
			}
			proposedDecree = Decree.createRemoveParticipantDecree(m_highestSeenNumber, participantId);
			break;
		case SET_LEADER:
			proposedDecree = Decree.createSetLeaderDecree(m_highestSeenNumber, Integer.parseInt(decree.getDecreeValue()), decree.getLeaderExpiry());
			break;
		}

		m_log.recordDecree(LogState.PREPARE, proposedDecree);
		m_voteInProgress = true;
		m_ballot.add(m_id);
		m_currentDecree = proposedDecree;
		if (!checkForCompletedBallot(proposedDecree))
		{
			for (Integer pId : m_participantIds)
				m_sendMessageRunnable.run(PaxosMessage.createPrepareMessage(m_id, pId, proposedDecree));
		}

		return true;
	}

	private boolean checkForCompletedBallot(Decree decree)
	{
		if (m_ballot.size() > Math.ceil((double) m_participantIds.size() / 2.0))
		{
			// this should always pass because only the leader calls checkForCompletedBallot()
			Preconditions.checkArgument(processDecree(decree));
			m_ballot = null;
			for (Integer participantId : m_participantIds)
			{
				m_sendMessageRunnable.run(PaxosMessage.createDecreeCommitMessage(m_id, participantId, decree));
			}
			return true;
		}
		return false;
	}

	private void updateDetails()
	{
		if (m_listeners.isEmpty())
			return;

		StringBuilder builder = new StringBuilder();
		builder.append("Leader ID: ")
		.append(m_leaderId)
		.append('\n')
//		.append("Leader Interval: ")
//		.append(m_leaderExpiry)
//		.append('\n')
		.append("Participant List: ")
		.append(m_participantIds.toString())
		.append('\n');
		if (m_ballot != null)
		{
			builder.append("Ballot: ")
			.append(m_ballot.toString())
			.append('\n');
		}
//		.append("Promised Number: ")
//		.append(m_promisedNumber)
//		.append('\n')
		builder.append("Highest Seen Number: ")
		.append(m_highestSeenNumber)
		.append('\n')
		.append("Joined: ")
		.append(m_hasJoined)
		.append('\n')
		.append("Present: ")
		.append(m_isPresent)
		.append('\n');

		String participantDetails = builder.toString();
		for (int i = 0; i < m_listeners.size(); i++)
			m_listeners.get(i).onParticipantDetailsUpdate(participantDetails);
	}

	private static final int LEADER_INTERVAL = 30;
	private static final int DELAY_INTERVAL = 100;

	private final RunnableOfT<PaxosMessage> m_sendMessageRunnable;
	private final RunnableOfT<Integer> m_onResignationRunnable;
	private final List<ParticipantDetailsListener> m_listeners;
	private final List<Integer> m_potentialParticipantIds;
	private Decree m_currentDecree;

	// housekeeping
	private final int m_id;
	//	private final Clock m_clock;
	private boolean m_hasJoined;
	private boolean m_isPresent;

	// volatile state
	private final PaxosQueue<PaxosMessage> m_inbox;
	private final List<Integer> m_participantIds;

	private Bag<Integer> m_ballot;
	private int m_currentDecreeId;
	private boolean m_voteInProgress;

	private final Object m_lock;
	private volatile boolean m_stopped;
	private volatile boolean m_paused;
	private volatile boolean m_shouldEnter;
	private volatile boolean m_shouldLeave;
	private volatile boolean m_withAmnesia;
	private volatile boolean m_shouldResign;

	// Paxos state (persistent)
	private final PaxosLog m_log;
	//	private int m_promisedNumber;
	private int m_highestSeenNumber;

	// Paxos-maintained state
	private int m_leaderId;
//	private int m_leaderExpiry;
}
