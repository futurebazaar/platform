/**
 * 
 */
package com.fb.platform.mom.receiver.deliveryDelete;

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

import com.fb.commons.mom.to.DeliveryDeleteTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteSenderImpl implements DeliveryDeleteSender {

	private static Log infoLog = LogFactory.getLog(DeliveryDeleteSenderImpl.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.DELIVERY_DELETE_AUDIT_LOG);

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
				infoLog.info("Creating the Delivery Delete Message for sending.");
				ObjectMessage jmsMessage = session.createObjectMessage();
				SapMomTO sapIdoc = ((DeliveryDeleteTO)message).getSapIdoc();
				
				//set the properties on jms message as well
				jmsMessage.setLongProperty(LoggerConstants.UID, sapIdoc.getAckUID());
				jmsMessage.setStringProperty(LoggerConstants.IDOC_NO, sapIdoc.getIdocNumber());
				jmsMessage.setStringProperty(LoggerConstants.TIMESTAMP, sapIdoc.getTimestamp().toString());
				
				//log for audit
				auditLog.info(sapIdoc.getAckUID() + "," + sapIdoc.getIdocNumber() + "," + sapIdoc.getTimestamp());
				
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
