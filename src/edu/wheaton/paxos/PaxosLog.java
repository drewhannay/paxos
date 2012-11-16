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
	}

	public int getFirstLogId()
	{
		Scanner scanner;
		int firstLogId = 0;
		try
		{
			scanner = new Scanner(m_file);
			firstLogId = scanner.nextInt();
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
		try
		{
			Scanner scanner = new Scanner(m_file);
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if (line.equals(lineToAppend))
					return;
			}
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}

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
				if (line.startsWith(Integer.toString(id)))
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

			return Integer.parseInt(line.substring(0, line.indexOf(' ')));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return 0;
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
				if (line.startsWith(Integer.toString(logId)))
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
		while (!log.startsWith(Integer.toString(firstUnknownId)) && log.contains("\n"))
		{
			log = log.substring(log.indexOf('\n'));
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
