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
        imageNamePanel = new JPanel();
        imagePanel = new JPanel();
        jButton2 = new JButton();
        nameLabel = new JLabel();
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

        homeButton.setIcon(new ImageIcon(getClass().getResource("/images/image/home1.png")));
        homeButton.setMinimumSize(new Dimension(10, 10));
        homeButton.setPreferredSize(new Dimension(10, 10));

        PlayPauseButton.setIcon(new ImageIcon(getClass().getResource("/images/image/pause2.png")));

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
        EnterButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                EnterButtonActionPerformed(event);
            }
        });

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

        imageNamePanel.setBorder(BorderFactory.createEtchedBorder());

        imagePanel.setBorder(BorderFactory.createEtchedBorder());

        jButton2.setIcon(new ImageIcon(getClass().getResource("/images/image/profile1.png"))); // NOI18N

        GroupLayout imagePanelLayout = new GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        nameLabel.setText("Name here");

        GroupLayout imageNamePanelLayout = new GroupLayout(imageNamePanel);
        imageNamePanel.setLayout(imageNamePanelLayout);
        imageNamePanelLayout.setHorizontalGroup(
            imageNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(imageNamePanelLayout.createSequentialGroup()
                .addGroup(imageNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(imageNamePanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(nameLabel))
                    .addGroup(imageNamePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(imagePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        imageNamePanelLayout.setVerticalGroup(
            imageNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(imageNamePanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(nameLabel)
                .addGap(18, 18, 18)
                .addComponent(imagePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        PlusMinusPanel.setBorder(BorderFactory.createEtchedBorder());
        PlusButton.setText("+");
        PlusButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				PlusButtonActionPerformed(event);
			}
		});
        
        MinusButton.setText("-");
        MinusButton.addActionListener(new ActionListener()
        {
        	@Override
            public void actionPerformed(ActionEvent event)
            {
                MinusButtonActionPerformed(event);
            }
        });


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
                        .addComponent(imageNamePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(imageNamePanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void EnterButtonActionPerformed(ActionEvent event)
    {
        // TODO add your handling code here:
    }

    private void PlusButtonActionPerformed(ActionEvent event)
    {
    	m_listModel.addElement("Participant " + m_participantIdGenerator);
    	m_postOffice.addParticipant(m_participantIdGenerator++);
    }

    private void MinusButtonActionPerformed(ActionEvent event)
    {
    	for (Object value : ParticipantList.getSelectedValues())
    	{
    		// TODO actually remove the participant from the PostOffice
    		m_listModel.removeElement(value);
    	}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private JPanel imageNamePanel;
    private JPanel imagePanel;
    private JButton jButton2;
    private JLabel nameLabel;
    private JButton promoteButton;
    private JLabel timeLabel;
    // End of variables declaration//GEN-END:variables

	private static final long serialVersionUID = -7049383055209558563L;

	private static int m_participantIdGenerator = 1;
//	private static int m_time = 1;

	private final PostOffice m_postOffice;

	private DefaultListModel m_listModel;
}
