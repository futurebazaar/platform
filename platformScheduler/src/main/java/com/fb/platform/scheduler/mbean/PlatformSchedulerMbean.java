package com.fb.platform.scheduler.mbean;

import javax.management.ObjectName;

import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;

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
