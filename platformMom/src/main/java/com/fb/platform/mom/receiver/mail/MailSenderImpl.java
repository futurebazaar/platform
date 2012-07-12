/**
 * 
 */
package com.fb.platform.mom.receiver.mail;

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
public class MailSenderImpl implements MailMsgSender {

	private static Log logger = LogFactory.getLog(MailSenderImpl.class);

	private JmsTemplate jmsTemplate;

	private Destination mailDestination;

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.receiver.inventory.InventorySender#sendMessage(java.lang.Object)
	 */
	@Override
	public void sendMessage(final Serializable message) {
		jmsTemplate.send(mailDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				logger.info("Creating the Mail Message for sending.");
				System.out.println("Creating the Mail Message for sending.");
				ObjectMessage jmsMessage = session.createObjectMessage();
				jmsMessage.setObject(message);
				return jmsMessage;
			}
		});
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setMailDestination(Destination mailDestination) {
		this.mailDestination = mailDestination;
	}

}
