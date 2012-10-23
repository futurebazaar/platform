package com.fb.platform.scheduler.mbean.impl;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.Notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;

import com.fb.platform.scheduler.mbean.PlatformSchedulerMbean;
import com.fb.platform.scheduler.service.FillWalletSchedulerService;

/**
 * @author kislay
 *
 */

@ManagedResource(description="Schedules sending fill wallet xml to SAP")
public class FillWalletSchedulerMbeanImpl implements PlatformSchedulerMbean, NotificationPublisherAware {

	private static Log logger = LogFactory.getLog(FillWalletSchedulerMbeanImpl.class);
	
	private FillWalletSchedulerService fillWalletSchedulerService;
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	
	private static final long FILL_WALLET_SCHEDULER_DELAY = (24*60*60*1000);
	private long sequenceNumber = 1;

	public void setFillWalletSchedulerService(FillWalletSchedulerService fillWalletSchedulerService) {
		this.fillWalletSchedulerService = fillWalletSchedulerService;
	}
	
	private NotificationPublisher notificationPublisher;
	@Override
	public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
		this.notificationPublisher = notificationPublisher;
	}
	
	@Override
	@ManagedOperation(description="Starts fill wallet scheduler")
	public boolean start() {
		logger.info("Starting fill wallet Scheduler.... ");
		if (scheduledThreadPoolExecutor != null) {
			logger.info(" Aleadry Started with COUNT :  " + scheduledThreadPoolExecutor.getTaskCount());
			return false;
		}
		DateTime dateTime = new DateTime().toDateMidnight().toDateTime().plusDays(1);
		long fillWalletStartDelay = dateTime.getMillis() - DateTime.now().getMillis();
		scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		scheduledThreadPoolExecutor.scheduleWithFixedDelay(new Schedule(), fillWalletStartDelay, FILL_WALLET_SCHEDULER_DELAY, TimeUnit.MILLISECONDS);
		logger.info(" Fill Wallet Scheduler started with properties: " + 
				"\nActive Count: " + scheduledThreadPoolExecutor.getActiveCount() + 
				"\nTask Count: " + scheduledThreadPoolExecutor.getTaskCount());
		
		sendChangedNotification("Operation", "Start", "Fill Wallet Scheduler Started");
		return true;
	}
	
	@Override
	@ManagedOperation(description="Stops Fill Wallet scheduler")
	public boolean stop() {
		logger.info("Shutting Down Fill Wallet Scheduler .... ");
		if (scheduledThreadPoolExecutor != null) {
			scheduledThreadPoolExecutor.shutdownNow();
			scheduledThreadPoolExecutor = null;
			logger.info(" Fill Wallet Scheduler Shut down Complete." );
			sendChangedNotification("Operation", "Stop", "Fill Wallet Scheduler Stopped");
			return true;
		}
		logger.info(" Fill Wallet Scheduler already terminated" );
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
			fillWalletSchedulerService.postXmlsTOBapi();
		}
		
	}
	
}
