/**
 * 
 */
package com.fb.launcher;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.commons.mom.receiver.ReceiverManager;

/**
 * @author nehaga
 *
 */
public class LauncherBootstrap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("launcher-applicationContext-dao.xml",
				"launcher-applicationContext-resources.xml",
				"launcher-applicationContext-service.xml");
		System.out.println("Start");
		ReceiverManager inventoryManager = (ReceiverManager) appContext.getBean("inventoryManager");
		inventoryManager.start();
		
	}

}
