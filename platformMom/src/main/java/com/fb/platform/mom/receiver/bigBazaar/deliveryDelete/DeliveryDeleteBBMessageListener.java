/**
 * 
 */
package com.fb.platform.mom.receiver.bigBazaar.deliveryDelete;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.bigBazaar.to.DeliveryDeleteBBTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteBBMessageListener extends AbstractPlatformListener implements MessageListener {
	
	private static Log infoLog = LogFactory.getLog(DeliveryDeleteBBMessageListener.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.DELIVERY_DELETE_BB_AUDIT_LOG);
	
	@Override
	public void onMessage(Message message) {
		infoLog.info("Received the message for the delivery delete big bazaar destination.");

		try {
			long uid = message.getLongProperty(LoggerConstants.UID);
			String idocNumber = message.getStringProperty(LoggerConstants.IDOC_NO);
			String timestamp = message.getStringProperty(LoggerConstants.TIMESTAMP);

			auditLog.info(uid + "," + idocNumber + "," + timestamp + ",false");

			ObjectMessage objectMessage = (ObjectMessage) message;
			DeliveryDeleteBBTO deliveryDelete = (DeliveryDeleteBBTO) objectMessage.getObject();

			infoLog.info("Received the delivery delete big bazaar Message from SAP. \n" + deliveryDelete.toString());

			super.notify(deliveryDelete , PlatformDestinationEnum.DELIVERY_DELETE_BB);
			
			SapMomTO sapIdoc = deliveryDelete.getSapIdoc();
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
