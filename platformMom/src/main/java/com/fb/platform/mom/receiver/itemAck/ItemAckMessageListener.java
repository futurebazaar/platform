/**
 * 
 */
package com.fb.platform.mom.receiver.itemAck;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

<<<<<<< HEAD
import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;
import com.fb.platform.mom.util.LoggerConstants;
=======
import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;
>>>>>>> sapConnector

/**
 * @author nehaga
 *
 */
public class ItemAckMessageListener extends AbstractPlatformListener implements MessageListener {

<<<<<<< HEAD
	private static Log infoLog = LogFactory.getLog(ItemAckMessageListener.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.ITEM_ACK_AUDIT_LOG);

	@Override
	public void onMessage(Message message) {
		infoLog.info("Received the message for the itemAck destination.");
		ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			long uid = message.getLongProperty(LoggerConstants.UID);
			String idocNumber = message.getStringProperty(LoggerConstants.IDOC_NO);
			String timestamp = message.getStringProperty(LoggerConstants.TIMESTAMP);

			auditLog.info(uid + "," + idocNumber + "," + timestamp + ",false");
			
			ItemTO itemAck = (ItemTO) objectMessage.getObject();

			infoLog.info("Received the item ack Message from SAP. \n" + itemAck.toString());

			super.notify(itemAck , PlatformDestinationEnum.ITEM_ACK);
			
			SapMomTO sapIdoc = itemAck.getSapIdoc();
			auditLog.info(sapIdoc.getAckUID() + "," + sapIdoc.getIdocNumber() + "," + sapIdoc.getTimestamp() + ",true");
		} catch (JMSException e) {
			infoLog.error("Error in processing hornetQ item ack message.", e);
			throw JmsUtils.convertJmsAccessException(e);
		} catch (Exception e) {
			infoLog.error("Error in processing hornetQ item ack message.", e);
			throw new PlatformException(e);
=======
	private static Log logger = LogFactory.getLog(ItemAckMessageListener.class);

	@Override
	public void onMessage(Message message) {
		logger.info("Received the message for the itemAck destination.");
		System.out.println("Received the message for the itemAck destination.");
		ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			ItemTO itemAck = (ItemTO) objectMessage.getObject();

			logger.info("Received the item ack Message from SAP. \n" + itemAck.toString());

			super.notify(itemAck);
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
>>>>>>> sapConnector
		}
	}
}
