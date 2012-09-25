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

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.DeliveryDeleteTO;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log infoLog = LogFactory.getLog("DELIVERY_DELETE_LOG");
	
	private static Log errorLog = LogFactory.getLog("DELIVERY_DELETE_ERROR");

	@Override
	public void onMessage(Message message) {
		infoLog.info("Received the message for the delivery delete destination.");
		ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			DeliveryDeleteTO deliveryDelete = (DeliveryDeleteTO) objectMessage.getObject();

			infoLog.info("Received the delivery delete Message from SAP. \n" + deliveryDelete.toString());

			super.notify(deliveryDelete , PlatformDestinationEnum.DELIVERY_DELETE);
		} catch (JMSException e) {
			errorLog.error("Error in delivery delete on message : ", e);
			throw JmsUtils.convertJmsAccessException(e);
		} catch (Exception e) {
			errorLog.error("Error in delivery delete on message : ", e);
			throw new PlatformException(e);
		}
	}
}
