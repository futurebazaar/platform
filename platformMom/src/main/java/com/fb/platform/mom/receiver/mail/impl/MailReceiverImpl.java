/**
 * 
 */
package com.fb.platform.mom.receiver.mail.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsUtils;

import com.fb.platform.mom.receiver.mail.MailReceiver;

/**
 * @author vinayak
 *
 */
public class MailReceiverImpl implements MailReceiver {

	private static Log logger = LogFactory.getLog(MailReceiverImpl.class);

	private JmsTemplate jmsTemplate;

	private Destination mailDestination;

	@Override
	public TextMessage receiveMail() {
		TextMessage message = (TextMessage)jmsTemplate.receive(mailDestination);
		try {
			String text = message.getText();
			logger.info("Received the text message : " + text);
			System.out.println("Received the text message : " + text);
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
		}
		return message;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setMailDestination(Destination mailDestination) {
		this.mailDestination = mailDestination;
	}

}
