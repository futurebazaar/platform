/**
 * 
 */
package com.fb.platform.mom.receiver.inventory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.mom.to.InventoryTO;
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
		ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			InventoryTO inventory = (InventoryTO) objectMessage.getObject();

			logger.info("Received the Inventory Message from SAP. \n" + inventory.toString());
			System.out.println("Received the Inventory Message from SAP. \n" + inventory.toString());

			super.notify(inventory);
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
		}
	}
}
