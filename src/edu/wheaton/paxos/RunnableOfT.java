package edu.wheaton.paxos;

public interface RunnableOfT<T>
{
	void run(T t);
}
