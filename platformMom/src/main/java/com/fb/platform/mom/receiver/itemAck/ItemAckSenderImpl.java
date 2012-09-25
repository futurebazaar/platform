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

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class ItemAckSenderImpl implements ItemAckSender {

	private static Log infoLog = LogFactory.getLog(LoggerConstants.ITEM_ACK_LOG);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.ITEM_ACK_AUDIT_LOG);

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
				infoLog.info("Creating the Item Ack Message for sending.");
				ObjectMessage jmsMessage = session.createObjectMessage();
				jmsMessage.setObject(message);
				SapMomTO sapIdoc = ((ItemTO) message).getSapIdoc();
				
				//set the properties on jms message as well
				jmsMessage.setLongProperty(LoggerConstants.UID, sapIdoc.getAckUID());
				jmsMessage.setStringProperty(LoggerConstants.IDOC_NO, sapIdoc.getIdocNumber());
				jmsMessage.setStringProperty(LoggerConstants.TIMESTAMP, sapIdoc.getTimestamp().toString());
				
				//log for audit
				auditLog.info(sapIdoc.getAckUID() + "," + sapIdoc.getIdocNumber() + "," + sapIdoc.getTimestamp());
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
