/**
 * 
 */
package com.fb.platform.mom.receiver.corrupt;

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
import com.fb.commons.mom.to.CorruptMessageCause;
import com.fb.commons.mom.to.CorruptMessageTO;
import com.fb.commons.mom.to.MailTO;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;
import com.fb.platform.mom.receiver.dlq.PreDLQMessageListener;

/**
 * @author nehaga
 *
 */
public class CorruptMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log logger = LogFactory.getLog(CorruptMessageListener.class);
	
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
			CorruptMessageTO corruptMessage = (CorruptMessageTO) objectMessage.getObject();
			
			MailTO corruptMail = new MailTO();
			String to = StringUtils.trim(prop.getProperty("receiver.mail.corrupt.to"));
			String cc = StringUtils.trim(prop.getProperty("receiver.mail.corrupt.cc"));
			String bcc = StringUtils.trim(prop.getProperty("receiver.mail.corrupt.bcc"));
			
			if(StringUtils.isNotBlank(to)) {
				corruptMail.setTo(to.split(","));
			}
			if(StringUtils.isNotBlank(cc)) {
				corruptMail.setCc(cc.split(","));
			}
			if(StringUtils.isNotBlank(bcc)) {
				corruptMail.setBcc(bcc.split(","));
			}
			
			corruptMail.setFrom(prop.getProperty("receiver.mail.corrupt.from"));
			
			Date date = new Date();
			corruptMail.setSubject("MOM corrupt message received : " + date.toString());
			corruptMail.setMessage("MOM corrupt message received : " + date.toString() 
					+ "\n\n message timestamp : " + message.getJMSTimestamp() 
					+ "\n\n message id : " + message.getJMSMessageID()
					+ "\n\n priority : " + message.getJMSPriority()
					+ "\n\n error cause : " + corruptMessage.toString());

			logger.info("MOM corrupt message received : " + date.toString() 
					+ "\n\n message timestamp : " + message.getJMSTimestamp() 
					+ "\n\n message id : " + message.getJMSMessageID()
					+ "\n\n priority : " + message.getJMSPriority()
					+ "\n\n error cause : " + corruptMessage.toString());
			
			System.out.println("MOM corrupt message received : " + date.toString() 
					+ "\n\n message timestamp : " + message.getJMSTimestamp() 
					+ "\n\n message id : " + message.getJMSMessageID()
					+ "\n\n priority : " + message.getJMSPriority()
					+ "\n\n error cause : " + corruptMessage.toString());
			
			if(corruptMessage.getCause().equals(CorruptMessageCause.CORRUPT_IDOC)) {
				super.notify(corruptMail);
			}
		} catch (JMSException e) {
			logger.error("JMSException in corruption queue", e);
			throw JmsUtils.convertJmsAccessException(e);
		}
	}
}
