package edu.wheaton.paxos.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Main
{
    public static void main(String[] args)
    {
    	SwingUtilities.invokeLater(new Thread(new Runnable()
    	{
    		public void run()
    		{
    			try
    			{
    				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    			new PostOfficeGUI();
    		}
    	}));
    }
}
