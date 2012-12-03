package edu.wheaton.paxos.gui;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wheaton.paxos.logic.Decree;
import edu.wheaton.paxos.logic.PaxosEvent;
import edu.wheaton.paxos.logic.PaxosListeners.LogUpdateListener;
import edu.wheaton.paxos.logic.PaxosListeners.QueueUpdateListener;
import edu.wheaton.paxos.logic.PaxosLogManager;
import edu.wheaton.paxos.logic.PaxosMessage;
import edu.wheaton.paxos.logic.PaxosMessageQueueManager;
import edu.wheaton.paxos.logic.PaxosQueue;
import edu.wheaton.paxos.logic.PostOffice;
import edu.wheaton.paxos.utility.RunnableOfT;

public class PostOfficeGUI extends JFrame
{
	private static int m_time = 1;

    public PostOfficeGUI()
    {
    	m_postOffice = new PostOffice(m_updateTimeDisplayRunnable);
        initComponents();

        m_plusButtonListener.actionPerformed(null);
        m_plusButtonListener.actionPerformed(null);
        m_plusButtonListener.actionPerformed(null);
//		m_postOffice.addEvent(new PaxosEvent(m_time++,
//				new PaxosMessage(0, 1, Decree.createOpaqueDecree(0, "test"))));
//		m_postOffice.addEvent(new PaxosEvent(m_time++, 
//				new PaxosMessage(1, 2, Decree.createOpaqueDecree(0, "ignore me"))));
//		m_postOffice.addEvent(new PaxosEvent(m_time++, 
//				new PaxosMessage(2, 3, Decree.createOpaqueDecree(1, "test2"))));
    }

    private void initComponents()
    {
    	m_topPanel = new JPanel();
    	m_operationsPanel = new JPanel();
    	m_plusMinusPanel = new JPanel();

    	m_participantDetailsTextPane = new JTextPane();
    	m_detailsJScrollPane = new JScrollPane();

    	m_logTextPane = new JTextPane();
    	m_logJScrollPane = new JScrollPane();

    	m_queueTextPane = new JTextPane();
    	m_queueJScrollPane = new JScrollPane();

    	m_timeDisplay = new JLabel();

    	m_homeButton = new JButton();
        m_playPauseButton = new JButton();
        m_plusButton = new JButton();
        m_minusButton = new JButton();
        m_promoteButton = new JButton();
        m_messagesButton = new JButton();
        m_resignButton = new JButton();
        m_enterButton = new JButton();
        m_delayButton = new JButton();
        m_leaveButton = new JButton();

        m_participantList = new JList();
        m_participantListScrollPane = new JScrollPane();

        setResizable(true);
        setTitle("Paxos Simulator");

        m_listModel = new DefaultListModel();
		m_participantList.setModel(m_listModel);
		m_participantList.getSelectionModel().addListSelectionListener(m_listSelectionListener);
		m_participantListScrollPane.setBorder(BorderFactory.createTitledBorder("Participants"));
        m_participantListScrollPane.setViewportView(m_participantList);

        m_queueTextPane.setEditable(false);
        m_queueJScrollPane.setViewportView(m_queueTextPane);
        m_queueJScrollPane.setBorder(BorderFactory.createTitledBorder("Message Queue"));

        m_logTextPane.setEditable(false);
        m_logJScrollPane.setViewportView(m_logTextPane);
        m_logJScrollPane.setBorder(BorderFactory.createTitledBorder("Log"));

        m_topPanel.setBorder(BorderFactory.createEtchedBorder());

        m_homeButton.setIcon(new ImageIcon(getClass().getResource("/images/home1.png")));
        Dimension homeButtonDimension = new Dimension(10, 10);
		m_homeButton.setMinimumSize(homeButtonDimension);
        m_homeButton.setPreferredSize(homeButtonDimension);
        m_homeButton.addActionListener(m_homeButtonListener);

        m_playPauseButton.setIcon(PAUSE_ICON);
        m_playPauseButton.addActionListener(m_playPauseButtonListener);

        GroupLayout TopPanelLayout = new GroupLayout(m_topPanel);
        m_topPanel.setLayout(TopPanelLayout);
        TopPanelLayout.setHorizontalGroup(
            TopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, TopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(m_homeButton, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, Short.MAX_VALUE)
                .addComponent(m_playPauseButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, Short.MAX_VALUE)
                .addComponent(m_timeDisplay, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.DEFAULT_SIZE)
                .addContainerGap())
        );
        TopPanelLayout.setVerticalGroup(
            TopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addGroup(TopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(m_timeDisplay, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(m_playPauseButton, GroupLayout.PREFERRED_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(m_homeButton, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addContainerGap())
        );

        m_participantDetailsTextPane.setEditable(false);
        m_detailsJScrollPane.setViewportView(m_participantDetailsTextPane);
        m_detailsJScrollPane.setBorder(BorderFactory.createTitledBorder("Participant Details"));

        m_operationsPanel.setBorder(BorderFactory.createEtchedBorder());

        m_promoteButton.setText("Promote");
        m_promoteButton.addActionListener(m_promoteButtonListener);

        m_messagesButton.setText("Messages");
        m_messagesButton.addActionListener(m_messageButtonListener);

        m_resignButton.setText("Resign");
        m_resignButton.addActionListener(m_resignButtonListener);

        m_enterButton.setText("Enter");
        m_enterButton.addActionListener(m_enterButtonListener);

        m_delayButton.setText("Delay");
        m_delayButton.addActionListener(m_delayButtonListener);

        m_leaveButton.setText("Leave");
        m_leaveButton.addActionListener(m_leaveButtonListener);

        GroupLayout operationsPanelLayout = new GroupLayout(m_operationsPanel);
        m_operationsPanel.setLayout(operationsPanelLayout);
        operationsPanelLayout.setHorizontalGroup(
            operationsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(operationsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(operationsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(m_leaveButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(m_delayButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(m_enterButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(m_resignButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(m_messagesButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_promoteButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addContainerGap())
        );
        operationsPanelLayout.setVerticalGroup(
            operationsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, operationsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(m_promoteButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_messagesButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_resignButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_enterButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_delayButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_leaveButton)
                .addContainerGap())
        );

        m_plusMinusPanel.setBorder(BorderFactory.createEtchedBorder());
        m_plusButton.setText("+");
        m_plusButton.addActionListener(m_plusButtonListener);
        
        m_minusButton.setText("-");
        m_minusButton.addActionListener(m_minusButtonListener);


        GroupLayout PlusMinusPanelLayout = new GroupLayout(m_plusMinusPanel);
        m_plusMinusPanel.setLayout(PlusMinusPanelLayout);
        PlusMinusPanelLayout.setHorizontalGroup(
            PlusMinusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(PlusMinusPanelLayout.createSequentialGroup()
                .addComponent(m_plusButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_minusButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PlusMinusPanelLayout.setVerticalGroup(
            PlusMinusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(PlusMinusPanelLayout.createSequentialGroup()
            		.addContainerGap()
                .addGroup(PlusMinusPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(m_minusButton, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_plusButton, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.LEADING, layout.createParallelGroup()
            		.addGroup(layout.createSequentialGroup()
            				.addContainerGap()
            				.addComponent(m_topPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
            				.addContainerGap()))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                	.addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(m_plusMinusPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(m_participantListScrollPane, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(m_queueJScrollPane, 200, 400, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_logJScrollPane, 200, 400, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(m_detailsJScrollPane, 200, 230, GroupLayout.DEFAULT_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_operationsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        )
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(m_topPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(m_detailsJScrollPane, GroupLayout.DEFAULT_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_operationsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                            )
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(m_logJScrollPane, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .addComponent(m_queueJScrollPane, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(m_participantListScrollPane, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(m_plusMinusPanel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        
        pack();

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;

        // Move the window
        setLocation(x, y);

        setVisible(true);
    }

    private final ActionListener m_homeButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
		}
	};

    private final ActionListener m_promoteButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			// TODO Auto-generated method stub
		}
	};

    private final ActionListener m_messageButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			m_postOffice.addEvent(new PaxosEvent(m_time++,
					new PaxosMessage(1, 2, Decree.createOpaqueDecree(0, "ignore me"))));
			m_postOffice.addEvent(new PaxosEvent(m_time++,
					new PaxosMessage(1, 2, Decree.createOpaqueDecree(1, "ignore me again"))));
		}
	};

	private final ActionListener m_resignButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
		}
	};

    private final ActionListener m_enterButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			// TODO Auto-generated method stub
		}
	};

	private final ActionListener m_delayButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
		}
	};

	private final ActionListener m_leaveButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
		}
	};

    private final ActionListener m_plusButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			m_listModel.addElement("Participant " + m_participantIdGenerator);
			m_postOffice.addParticipant(m_participantIdGenerator++);
		}
	};

    private final ActionListener m_minusButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			for (Object value : m_participantList.getSelectedValues())
			{
				// TODO actually remove the participant from the PostOffice
				m_listModel.removeElement(value);
			}
		}
	};

    private final ActionListener m_playPauseButtonListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			m_playPauseButton.setIcon(m_postOffice.togglePauseState() ? PLAY_ICON : PAUSE_ICON);
		}
	};

	private final RunnableOfT<String> m_updateTimeDisplayRunnable = new RunnableOfT<String>()
	{
		@Override
		public void run(String time)
		{
			m_timeDisplay.setText("Time: " + time);
		}
	};

	private final ListSelectionListener m_listSelectionListener = new ListSelectionListener()
	{
		@Override
		public void valueChanged(ListSelectionEvent event)
		{
			if (m_selectedParticipantId > 0)
			{
				PaxosLogManager.removeLogUpdateListener(m_selectedParticipantId, m_logUpdateListener);
				PaxosMessageQueueManager.removeQueueUpdateListener(m_selectedParticipantId, m_queueUpdateListener);
			}

			ListSelectionModel model = (ListSelectionModel) event.getSource();
			if (model.isSelectionEmpty() || model.getMaxSelectionIndex() - model.getMinSelectionIndex() != 0)
			{
		        m_detailsJScrollPane.setBorder(BorderFactory.createTitledBorder("Participant Details"));
				m_logTextPane.setText("");
				m_queueTextPane.setText("");
				m_participantDetailsTextPane.setText("");
			}
			else
			{
				String participantNameString = m_listModel.get(model.getMaxSelectionIndex()).toString();
				m_selectedParticipantId = Integer.parseInt(participantNameString.substring(participantNameString.lastIndexOf(' ') + 1));
		        m_detailsJScrollPane.setBorder(BorderFactory.createTitledBorder(participantNameString + " Details"));
				m_logUpdateListener = new LogUpdateListener()
				{
					@Override
					public void onLogUpdate(String updatedLog)
					{
						m_logTextPane.setText(updatedLog);
					}
				};
		        PaxosLogManager.addLogUpdateListener(m_selectedParticipantId, m_logUpdateListener);
		        m_queueUpdateListener = new QueueUpdateListener()
				{
					@Override
					public void onQueueUpdate(String queueContents)
					{
						m_queueTextPane.setText(queueContents);
					}
				};
				PaxosMessageQueueManager.addQueueUpdateListener(m_selectedParticipantId, m_queueUpdateListener);
			}
		}

	    private int m_selectedParticipantId;
	    private LogUpdateListener m_logUpdateListener;
	    private QueueUpdateListener m_queueUpdateListener;
	};

    private static final long serialVersionUID = -7049383055209558563L;
    private static final ImageIcon PAUSE_ICON = new ImageIcon(PostOfficeGUI.class.getResource("/images/pause2.png"));
    private static final ImageIcon PLAY_ICON = new ImageIcon(PostOfficeGUI.class.getResource("/images/play2.png"));
    
    private static int m_participantIdGenerator = 1;

    private final PostOffice m_postOffice;

    private JPanel m_topPanel;
    private JPanel m_operationsPanel;
    private JPanel m_plusMinusPanel;

    private JTextPane m_participantDetailsTextPane;
    private JScrollPane m_detailsJScrollPane;

    private JTextPane m_logTextPane;
    private JScrollPane m_logJScrollPane;

    private JTextPane m_queueTextPane;
    private JScrollPane m_queueJScrollPane;

    private JLabel m_timeDisplay;

    private JButton m_homeButton;
    private JButton m_playPauseButton;
    private JButton m_plusButton;
    private JButton m_minusButton;
    private JButton m_promoteButton;
    private JButton m_messagesButton;
    private JButton m_resignButton;
    private JButton m_enterButton;
    private JButton m_delayButton;
    private JButton m_leaveButton;

    private JList m_participantList;
    private JScrollPane m_participantListScrollPane;
    private DefaultListModel m_listModel;
}
