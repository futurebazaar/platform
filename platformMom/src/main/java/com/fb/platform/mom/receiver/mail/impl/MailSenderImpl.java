/**
 * 
 */
package com.fb.platform.mom.receiver.mail.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.fb.platform.mom.receiver.mail.MailSender;

/**
 * @author vinayak
 *
 */
public class MailSenderImpl implements MailSender {

	private JmsTemplate jmsTemplate;

	private Destination mailDestination;

	@Override
	public void sendMail() {
		jmsTemplate.send(mailDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage();
				message.setText("Hello world!");
				return message;
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
