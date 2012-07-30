/**
 * 
 */
package com.fb.platform.mom.receiver.itemAck;

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
 * @author nehaga
 *
 */
public class ItemAckSenderImpl implements ItemAckSender {

	private static Log logger = LogFactory.getLog(ItemAckSenderImpl.class);

	private JmsTemplate jmsTemplate;

	private Destination itemAckDestination;

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.receiver.itemAck.ItemAckSender#sendMessage(java.lang.Object)
	 */
	@Override
	public void sendMessage(final Serializable message) {
		jmsTemplate.send(itemAckDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				logger.info("Creating the Item Ack Message for sending.");
				System.out.println("Creating the Item Ack Message for sending.");
				ObjectMessage jmsMessage = session.createObjectMessage();
				jmsMessage.setObject(message);
				return jmsMessage;
			}
		});
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setItemAckDestination(Destination itemAckDestination) {
		this.itemAckDestination = itemAckDestination;
	}

}
