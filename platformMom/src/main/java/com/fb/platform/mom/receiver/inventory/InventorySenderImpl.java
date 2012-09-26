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

import com.fb.commons.mom.to.InventoryTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author vinayak
 *
 */
public class InventorySenderImpl implements InventorySender {

	private static Log infoLog = LogFactory.getLog(InventorySenderImpl.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.INVENTORY_AUDIT_LOG);

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
				infoLog.info("Creating the Inventory Message for sending.");
				ObjectMessage jmsMessage = session.createObjectMessage();

				SapMomTO sapIdoc = ((InventoryTO) message).getSapIdoc();

				//set the properties on jms message as well
				jmsMessage.setLongProperty(LoggerConstants.UID, sapIdoc.getAckUID());
				jmsMessage.setStringProperty(LoggerConstants.IDOC_NO, sapIdoc.getIdocNumber());
				jmsMessage.setStringProperty(LoggerConstants.TIMESTAMP, sapIdoc.getTimestamp().toString());

				jmsMessage.setObject(message);

				//log for audit
				auditLog.info(sapIdoc.getAckUID() + "," + sapIdoc.getIdocNumber() + "," + sapIdoc.getTimestamp());
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
