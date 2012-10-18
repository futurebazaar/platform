/**
 * 
 */
package com.fb.platform.mom.receiver.delivery;

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
public class DeliveryDeleteSenderImpl implements DeliveryDeleteSender {

	private static Log logger = LogFactory.getLog(DeliveryDeleteSenderImpl.class);

	private JmsTemplate jmsTemplate;

	private Destination deliveryDeleteDestination;

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.receiver.inventory.DeliveryDeleteSender#sendMessage(java.lang.Object)
	 */
	@Override
	public void sendMessage(final Serializable message) {
		jmsTemplate.send(deliveryDeleteDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				logger.info("Creating the Delivery Delete Message for sending.");
				ObjectMessage jmsMessage = session.createObjectMessage();
				jmsMessage.setObject(message);
				return jmsMessage;
			}
		});
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setDeliveryDeleteDestination(Destination deliveryDeleteDestination) {
		this.deliveryDeleteDestination = deliveryDeleteDestination;
	}

}
