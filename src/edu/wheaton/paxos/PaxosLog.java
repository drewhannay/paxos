package edu.wheaton.paxos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PaxosLog
{
	public PaxosLog(int partipantId)
	{
		m_file = new File(partipantId + ".log");
		if (m_file.exists())
		{
			if (!m_file.delete())
			{
				System.err.println("Log file " + partipantId + ".log could not be deleted");
				System.exit(-1);
			}
		}
		try
		{
			m_file.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public int getFirstLogId()
	{
		Scanner scanner;
		int firstLogId = 0;
		try
		{
			scanner = new Scanner(m_file);
			String line = scanner.nextLine();
			firstLogId = Integer.parseInt(line.substring(0, line.indexOf(Decree.DELIMITER)));
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return firstLogId;
	}

	public int getFirstUnknownId()
	{
		return getLatestLogId() + 1;
	}

	public void recordDecree(Decree decree)
	{
		String lineToAppend = decree.toString();
		if (decree.getDecreeId() != getFirstUnknownId())
			return;

		try
		{
			FileWriter writer = new FileWriter(m_file);
			writer.append(lineToAppend);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Decree readDecree(int id)
	{
		try
		{
			Scanner scanner = new Scanner(m_file);
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if (Integer.parseInt(line.substring(0, line.indexOf(Decree.DELIMITER))) == id)
					return Decree.fromString(line);
			}
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public int getLatestLogId()
	{
		try
		{
			Scanner scanner = new Scanner(m_file);
			String line = "";

			while (scanner.hasNextLine())
				line = scanner.nextLine();
			scanner.close();

			if (line.isEmpty())
				return -1;

			return Integer.parseInt(line.substring(0, line.indexOf(Decree.DELIMITER)));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	public String getLogSinceId(int logId)
	{
		StringBuilder logBuilder = new StringBuilder();
		try
		{
			boolean isPastGivenId = false;
			Scanner scanner = new Scanner(m_file);
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if (!isPastGivenId && Integer.parseInt(line.substring(0, line.indexOf(Decree.DELIMITER))) == logId)
					isPastGivenId = true;
				if (isPastGivenId)
					logBuilder.append(line);
			}
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		return logBuilder.toString();
	}

	public void update(String log)
	{
		int firstUnknownId = getFirstUnknownId();
		while (Integer.parseInt(log.substring(0, log.indexOf(Decree.DELIMITER))) != firstUnknownId
			&& log.contains("\n"))
		{
			log = log.substring(log.indexOf('\n') + 1);
		}
		if (!log.isEmpty())
		{
			try
			{
				FileWriter writer = new FileWriter(m_file);
				writer.append(log);
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private final File m_file;
}
