package com.fb.commons.mom.receiver;

/**
 * @author nehaga
 *
 * This interface should be implemented by MoM receiver module managers.
 */
public interface ReceiverManager {
	/*
	 * This function acts as an entry point for the receiver module.
	 * The launcher manager will invoke this function on all the receiver managers.
	 * The receiver managers should invoke registerReceiver() and list interest in
	 * the destination queues.
	 */
	public void start();
}
