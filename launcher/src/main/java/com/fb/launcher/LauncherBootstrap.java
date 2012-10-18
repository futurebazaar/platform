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
		ApplicationContext appContext = new ClassPathXmlApplicationContext("launcher-applicationContext-service.xml", "platformMom-applicationContext-resources.xml", "platformMom-applicationContext-service.xml");
		System.out.println("Start");
		ReceiverManager inventoryManager = (ReceiverManager) appContext.getBean("inventoryManager");
		inventoryManager.start();
		
		ReceiverManager mailManager = (ReceiverManager) appContext.getBean("mailManager");
		mailManager.start();
		
		ReceiverManager deliveryDeleteManager = (ReceiverManager) appContext.getBean("deliveryDeleteManager");
		deliveryDeleteManager.start();
		
		ReceiverManager itemAckManager = (ReceiverManager) appContext.getBean("itemAckManager");
		itemAckManager.start();
		
		ReceiverManager invoiceManager = (ReceiverManager) appContext.getBean("invoiceManager");
		invoiceManager.start();
		
	}

}
