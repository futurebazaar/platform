package com.fb.platform.scheduler.mbean.impl;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.Notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;

import com.fb.platform.scheduler.mbean.PlatformSchedulerMbean;
import com.fb.platform.scheduler.service.OrderSchedulerService;

/**
 * @author anubhav
 *
 */

@ManagedResource(description="Schedules sending order xml to SAP")
public class OrderSchedulerMbeanImpl implements PlatformSchedulerMbean, NotificationPublisherAware {

	private static Log logger = LogFactory.getLog(OrderSchedulerMbeanImpl.class);
	
	private OrderSchedulerService orderSchedulerService;
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	
	private static final long ORDER_SCHEDULER_DELAY = 30000;
	private long sequenceNumber = 1;

	public void setOrderSchedulerService(OrderSchedulerService orderSchedulerService) {
		this.orderSchedulerService = orderSchedulerService;
	}
	
	private NotificationPublisher notificationPublisher;
	@Override
	public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
		this.notificationPublisher = notificationPublisher;
	}
	
	@Override
	@ManagedOperation(description="Starts order scheduler")
	public boolean start() {
		logger.info("Starting Order Scheduler.... ");
		if (scheduledThreadPoolExecutor != null) {
			logger.info(" Aleadry Started with COUNT :  " + scheduledThreadPoolExecutor.getTaskCount());
			return false;
		}
		scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		scheduledThreadPoolExecutor.scheduleWithFixedDelay(new Schedule(), ORDER_SCHEDULER_DELAY, ORDER_SCHEDULER_DELAY, TimeUnit.MILLISECONDS);
		logger.info(" Order Scheduler started with properties: " + 
				"\nActive Count: " + scheduledThreadPoolExecutor.getActiveCount() + 
				"\nTask Count: " + scheduledThreadPoolExecutor.getTaskCount());
		
		sendChangedNotification("Operation", "Start", "Order Scheduler Started");
		return true;
	}
	
	@Override
	@ManagedOperation(description="Stops Order scheduler")
	public boolean stop() {
		logger.info("Shutting Down Order Scheduler .... ");
		if (scheduledThreadPoolExecutor != null) {
			scheduledThreadPoolExecutor.shutdownNow();
			scheduledThreadPoolExecutor = null;
			logger.info(" Order Scheduler Shut down Complete." );
			sendChangedNotification("Operation", "Stop", "Order Scheduler Stopped");
			return true;
		}
		logger.info(" Order Scheduler already terminated" );
		return false;
	}	
	
	private void sendChangedNotification(String type, String source, String message) {
		try {
			Notification notification = new Notification(type, source, sequenceNumber++, System.currentTimeMillis(), message);
			notificationPublisher.sendNotification(notification);
		} catch (Exception e) {
			logger.error("Unable to send Notification", e);
		}
	}
	
	private class Schedule implements Runnable {
		@Override
		public void run() {
			orderSchedulerService.postXmlsTOBapi();
		}
		
	}
	
}
