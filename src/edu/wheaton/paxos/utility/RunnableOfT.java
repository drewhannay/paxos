package edu.wheaton.paxos.utility;

public interface RunnableOfT<T>
{
	void run(T t);
}
