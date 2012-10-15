package com.fb.platform.scheduler.mbean;

/**
 * @author anubhav
 *
 */

public interface PlatformSchedulerMbean {
	
	/**
	 * Method to start the scheduler.
	 * Implementation depends upon the scheduling process
	 * 
	 */
	public boolean start();
	
	/**
	 * Stops the scheduler.
	 */
	public boolean stop();

}
