/**
 * 
 */
package com.fb.platform.mom.receiver.inventory;

import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @author vinayak
 *
 */
public class InventorySenderImpl implements InventorySender {

	private static Log logger = LogFactory.getLog(InventorySenderImpl.class);

	private JmsTemplate jmsTemplate;

	private Destination inventoryDestination;

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.receiver.inventory.InventorySender#sendMessage(java.lang.Object)
	 */
	@Override
	public void sendMessage(final Serializable message) {
		jmsTemplate.send(inventoryDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				logger.info("Creating the Inventory Message for sending.");
				System.out.println("Creating the Inventory Message for sending.");
				ObjectMessage jmsMessage = session.createObjectMessage();
				jmsMessage.setObject(message);
				return jmsMessage;
			}
		});
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setInventoryDestination(Destination inventoryDestination) {
		this.inventoryDestination = inventoryDestination;
	}

}