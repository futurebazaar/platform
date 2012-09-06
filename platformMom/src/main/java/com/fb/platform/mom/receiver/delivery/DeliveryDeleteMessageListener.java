/**
 * 
 */
package com.fb.platform.mom.receiver.delivery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.mom.to.DeliveryDeleteTO;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log logger = LogFactory.getLog(DeliveryDeleteMessageListener.class);

	@Override
	public void onMessage(Message message) {
		logger.info("Received the message for the delivery delete destination.");
		System.out.println("Received the message for the delivery delete destination.");
		ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			DeliveryDeleteTO deliveryDelete = (DeliveryDeleteTO) objectMessage.getObject();

			logger.info("Received the delivery delete Message from SAP. \n" + deliveryDelete.toString());

			super.notify(deliveryDelete);
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
		}
	}
}
