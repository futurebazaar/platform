/**
 * 
 */
package com.fb.platform.mom.receiver.mail;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.mom.to.MailTO;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;

/**
 * @author nehaga
 *
 */
public class MailMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log logger = LogFactory.getLog(MailMessageListener.class);

	@Override
	public void onMessage(Message message) {
		logger.info("Received the message for the Mail destination.");
		System.out.println("Received the message for the Mail destination.");
		ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			MailTO mail = (MailTO) objectMessage.getObject();

			logger.info("Received the Mail Message. \n" + mail.toString());
			System.out.println("Received the Mail Message. \n" + mail.toString());

			super.notify(mail);
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
		}
	}
}
