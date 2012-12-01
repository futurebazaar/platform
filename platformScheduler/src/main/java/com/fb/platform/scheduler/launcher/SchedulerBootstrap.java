package com.fb.platform.scheduler.launcher;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author anubhav
 *
 */
public class SchedulerBootstrap {

	public static void main(String[] args) throws Exception {
		new ClassPathXmlApplicationContext("scheduler-applicationContext-service.xml", "scheduler-applicationContext-dao.xml", "scheduler-applicationContext-resources.xml", "scheduler-applicationContext.xml");
		System.out.println("Waiting Forever..... : + Press CTRL+C to abort all the schedulers");
		Thread.sleep(Long.MAX_VALUE);
	}
	
}
