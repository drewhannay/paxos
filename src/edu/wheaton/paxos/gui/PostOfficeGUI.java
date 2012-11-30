package edu.wheaton.paxos.gui;
import java.awt.Dimension;
import java.awt.ScrollPane;
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

import edu.wheaton.paxos.logic.PostOffice;


public class PostOfficeGUI extends JFrame
{
    public PostOfficeGUI()
    {
    	m_postOffice = new PostOffice();
        initComponents();
    }

    private void initComponents()
    {
        ParticipantScrollPane = new JScrollPane();
        ParticipantList = new JList();
        ParticipantLabel = new JLabel();
        QueueScrollPane = new ScrollPane();
        QueueJScrollPane = new JScrollPane();
        QueueText = new JTextPane();
        LogScrollPane = new ScrollPane();
        LogJScrollPane = new JScrollPane();
        LogText = new JTextPane();
        TopPanel = new JPanel();
        homeButton = new JButton();
        PlayPauseButton = new JButton();
        timeLabel = new JLabel();
        DetailsScrollPane = new ScrollPane();
        DetailsJScrollPane = new JScrollPane();
        ParticipantDetailsPanel = new JTextPane();
        OperationsPanel = new JPanel();
        promoteButton = new JButton();
        MessagesButton = new JButton();
        ResignButton = new JButton();
        EnterButton = new JButton();
        DelayButton = new JButton();
        LeaveButton = new JButton();
        m_participantNamePanel = new JPanel();
        m_participantNameLabel = new JLabel();
        PlusMinusPanel = new JPanel();
        PlusButton = new JButton();
        MinusButton = new JButton();

        setResizable(true);
        setTitle("Paxos Simulator");

        m_listModel = new DefaultListModel();
		ParticipantList.setModel(m_listModel);
        ParticipantScrollPane.setViewportView(ParticipantList);

        ParticipantLabel.setText("Participants");

        QueueText.setText("Queue/Events");
        QueueJScrollPane.setViewportView(QueueText);

        QueueScrollPane.add(QueueJScrollPane);

        LogText.setText("Log");
        LogJScrollPane.setViewportView(LogText);

        LogScrollPane.add(LogJScrollPane);

        TopPanel.setBorder(BorderFactory.createEtchedBorder());

        homeButton.setIcon(new ImageIcon(getClass().getResource("/images/home1.png")));
        homeButton.setMinimumSize(new Dimension(10, 10));
        homeButton.setPreferredSize(new Dimension(10, 10));

        PlayPauseButton.setIcon(PAUSE_ICON);
        PlayPauseButton.addActionListener(m_playPauseButtonListener);

        timeLabel.setText("currentTime");

        GroupLayout TopPanelLayout = new GroupLayout(TopPanel);
        TopPanel.setLayout(TopPanelLayout);
        TopPanelLayout.setHorizontalGroup(
            TopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, TopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(homeButton, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                .addComponent(PlayPauseButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addGap(155, 155, 155)
                .addComponent(timeLabel)
                .addContainerGap())
        );
        TopPanelLayout.setVerticalGroup(
            TopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addGroup(TopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(timeLabel, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(PlayPauseButton, GroupLayout.PREFERRED_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(homeButton, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addContainerGap())
        );

        ParticipantDetailsPanel.setText("Participant Details");
        DetailsJScrollPane.setViewportView(ParticipantDetailsPanel);

        DetailsScrollPane.add(DetailsJScrollPane);

        OperationsPanel.setBorder(BorderFactory.createEtchedBorder());

        promoteButton.setText("Promote");

        MessagesButton.setText("Messages");

        ResignButton.setText("Resign");

        EnterButton.setText("Enter");
        EnterButton.addActionListener(m_enterButtonListener);

        DelayButton.setText("Delay");

        LeaveButton.setText("Leave");

        GroupLayout OperationsPanelLayout = new GroupLayout(OperationsPanel);
        OperationsPanel.setLayout(OperationsPanelLayout);
        OperationsPanelLayout.setHorizontalGroup(
            OperationsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(OperationsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(OperationsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(LeaveButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(DelayButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(EnterButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(ResignButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(MessagesButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(promoteButton, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addContainerGap())
        );
        OperationsPanelLayout.setVerticalGroup(
            OperationsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, OperationsPanelLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(promoteButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MessagesButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ResignButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(EnterButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DelayButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LeaveButton)
                .addContainerGap())
        );

        m_participantNamePanel.setBorder(BorderFactory.createEtchedBorder());

        m_participantNameLabel.setText("Participant Name");

        GroupLayout imageNamePanelLayout = new GroupLayout(m_participantNamePanel);
        m_participantNamePanel.setLayout(imageNamePanelLayout);
        imageNamePanelLayout.setHorizontalGroup(
            imageNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(imageNamePanelLayout.createSequentialGroup()
                .addGroup(imageNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(imageNamePanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(m_participantNameLabel)))
                .addContainerGap())
        );
        imageNamePanelLayout.setVerticalGroup(
            imageNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(imageNamePanelLayout.createSequentialGroup()
                .addComponent(m_participantNameLabel))
        );

        PlusMinusPanel.setBorder(BorderFactory.createEtchedBorder());
        PlusButton.setText("+");
        PlusButton.addActionListener(m_plusButtonListener);
        
        MinusButton.setText("-");
        MinusButton.addActionListener(m_minusButtonListener);


        GroupLayout PlusMinusPanelLayout = new GroupLayout(PlusMinusPanel);
        PlusMinusPanel.setLayout(PlusMinusPanelLayout);
        PlusMinusPanelLayout.setHorizontalGroup(
            PlusMinusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(PlusMinusPanelLayout.createSequentialGroup()
                .addComponent(PlusButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MinusButton, GroupLayout.PREFERRED_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );
        PlusMinusPanelLayout.setVerticalGroup(
            PlusMinusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(PlusMinusPanelLayout.createSequentialGroup()
                .addGroup(PlusMinusPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(MinusButton, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                    .addComponent(PlusButton, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(ParticipantLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PlusMinusPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ParticipantScrollPane, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(QueueScrollPane, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LogScrollPane, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(m_participantNamePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DetailsScrollPane, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OperationsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(TopPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(TopPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(ParticipantLabel)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(DetailsScrollPane, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(OperationsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(m_participantNamePanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(LogScrollPane, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .addComponent(QueueScrollPane, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(ParticipantScrollPane, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PlusMinusPanel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        pack();

        // Determine the new location of the window
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;

        // Move the window
        setLocation(x, y);

        setVisible(true);
    }

    private final ActionListener m_enterButtonListener = new ActionListener()
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
		public void actionPerformed(ActionEvent e)
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
			for (Object value : ParticipantList.getSelectedValues())
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
			PlayPauseButton.setIcon(m_postOffice.togglePauseState() ? PLAY_ICON : PAUSE_ICON);
		}
	};

    private static final long serialVersionUID = -7049383055209558563L;
    private static final ImageIcon PAUSE_ICON = new ImageIcon(PostOfficeGUI.class.getResource("/images/pause2.png"));
    private static final ImageIcon PLAY_ICON = new ImageIcon(PostOfficeGUI.class.getResource("/images/play2.png"));
    
    private static int m_participantIdGenerator = 1;
//	private static int m_time = 1;

    private final PostOffice m_postOffice;

    private JButton DelayButton;
    private JScrollPane DetailsJScrollPane;
    private ScrollPane DetailsScrollPane;
    private JButton EnterButton;
    private JButton LeaveButton;
    private JScrollPane LogJScrollPane;
    private ScrollPane LogScrollPane;
    private JTextPane LogText;
    private JButton MessagesButton;
    private JButton MinusButton;
    private JPanel OperationsPanel;
    private JTextPane ParticipantDetailsPanel;
    private JLabel ParticipantLabel;
    private JList ParticipantList;
    private JScrollPane ParticipantScrollPane;
    private JButton PlayPauseButton;
    private JButton PlusButton;
    private JPanel PlusMinusPanel;
    private JScrollPane QueueJScrollPane;
    private ScrollPane QueueScrollPane;
    private JTextPane QueueText;
    private JButton ResignButton;
    private JPanel TopPanel;
    private JButton homeButton;
    private JPanel m_participantNamePanel;
    private JLabel m_participantNameLabel;
    private JButton promoteButton;
    private JLabel timeLabel;
    private DefaultListModel m_listModel;
}
