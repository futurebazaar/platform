/**
 * 
 */
package com.fb.platform.mom.receiver.inventory;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.mom.manager.impl.AbstractPlatformListener;

/**
 * @author vinayak
 *
 */
public class InventoryMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log logger = LogFactory.getLog(InventoryMessageListener.class);

	@Override
	public void onMessage(Message message) {
		logger.info("Received the message for the Inventor destination.");
		System.out.println("Received the message for the Inventor destination.");
		super.notify(message);
	}
}
