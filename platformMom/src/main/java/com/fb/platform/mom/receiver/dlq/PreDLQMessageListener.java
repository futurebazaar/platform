/**
 * 
 */
package com.fb.platform.mom.receiver.dlq;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.MailTO;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;

/**
 * @author vinayak
 *
 */
public class PreDLQMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log logger = LogFactory.getLog(PreDLQMessageListener.class);
	
	private static Properties prop = initProperties();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = PreDLQMessageListener.class.getClassLoader().getResourceAsStream("receivers.properties");
		try {
			properties.load(propertiesStream);
		} catch (IOException e) {
			logger.error("Error loading properties file.", e);
			throw new PlatformException("Error loading properties file.", e);
		}
		return properties;
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			MailTO dlqMail = new MailTO();
			String to = StringUtils.trim(prop.getProperty("receiver.mail.dlq.to"));
			String cc = StringUtils.trim(prop.getProperty("receiver.mail.dlq.cc"));
			String bcc = StringUtils.trim(prop.getProperty("receiver.mail.dlq.bcc"));
			
			if(StringUtils.isNotBlank(to)) {
				dlqMail.setTo(to.split(","));
			}
			if(StringUtils.isNotBlank(cc)) {
				dlqMail.setCc(cc.split(","));
			}
			if(StringUtils.isNotBlank(bcc)) {
				dlqMail.setBcc(bcc.split(","));
			}
			
			dlqMail.setFrom(prop.getProperty("receiver.mail.dlq.from"));
			Date date = new Date();
			dlqMail.setSubject("MOM message delivery failed : " + date.toString());
			dlqMail.setMessage("Search string _HQ_ORIG_MESSAGE_ID=" + objectMessage.getObjectProperty("_HQ_ORIG_MESSAGE_ID") 
					+ "\n\n MOM message delivery failed : " + date.toString() 
					+ "\n\n destination : " + objectMessage.getObjectProperty("_HQ_ORIG_ADDRESS") 
					+ "\n\n original message id : " + objectMessage.getObjectProperty("_HQ_ORIG_MESSAGE_ID") 
					+ "\n\n message timestamp : " + message.getJMSTimestamp() 
					+ "\n\n message id : " + message.getJMSMessageID()
					+ "\n\n priority : " + message.getJMSPriority()
					+ "\n\n object : " + objectMessage.getObject().toString() );

			logger.info("Search string _HQ_ORIG_MESSAGE_ID=" + objectMessage.getObjectProperty("_HQ_ORIG_MESSAGE_ID") 
					+ "\n\n MOM message delivery failed : " + date.toString() 
					+ "\n\n destination : " + objectMessage.getObjectProperty("_HQ_ORIG_ADDRESS") 
					+ "\n\n original message id : " + objectMessage.getObjectProperty("_HQ_ORIG_MESSAGE_ID") 
					+ "\n\n message timestamp : " + message.getJMSTimestamp() 
					+ "\n\n message id : " + message.getJMSMessageID()
					+ "\n\n priority : " + message.getJMSPriority()
					+ "\n\n object : " + objectMessage.getObject().toString() );

			super.notify(dlqMail);
		} catch (JMSException e) {
			logger.error("JMSException in preDLQ", e);
			throw JmsUtils.convertJmsAccessException(e);
		}
	}
}
