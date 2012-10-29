/**
 * 
 */
package com.fb.platform.mom.receiver.bigBazaar.delivery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.bigBazaar.to.DeliveryTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class DeliveryMessageListener extends AbstractPlatformListener implements MessageListener {
	
	private static Log infoLog = LogFactory.getLog(DeliveryMessageListener.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.DELIVERY_BB_AUDIT_LOG);
	
	@Override
	public void onMessage(Message message) {
		infoLog.info("Received the message for the delivery destination.");

		try {
			long uid = message.getLongProperty(LoggerConstants.UID);
			String idocNumber = message.getStringProperty(LoggerConstants.IDOC_NO);
			String timestamp = message.getStringProperty(LoggerConstants.TIMESTAMP);

			auditLog.info(uid + "," + idocNumber + "," + timestamp + ",false");

			ObjectMessage objectMessage = (ObjectMessage) message;
			DeliveryTO delivery = (DeliveryTO) objectMessage.getObject();

			infoLog.info("Received the delivery Message from SAP. \n" + delivery.toString());

			super.notify(delivery , PlatformDestinationEnum.DELIVERY_BB);
			
			SapMomTO sapIdoc = delivery.getSapIdoc();
			auditLog.info(sapIdoc.getAckUID() + "," + sapIdoc.getIdocNumber() + "," + sapIdoc.getTimestamp() + ",true");
		} catch (JMSException e) {
			infoLog.error("Error in processing hornetQ delivery message.", e);
			throw JmsUtils.convertJmsAccessException(e);
		} catch (Exception e) {
			infoLog.error("Error in processing hornetQ delivery message.", e);
			throw new PlatformException(e);
			
		}
	}
}
