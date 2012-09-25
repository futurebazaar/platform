/**
 * 
 */
package com.fb.platform.mom.receiver.inventory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.InventoryTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author vinayak
 *
 */
public class InventoryMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log infoLog = LogFactory.getLog(LoggerConstants.INVENTORY_LOG);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.INVENTORY_AUDIT_LOG);

	@Override
	public void onMessage(Message message) {
		infoLog.info("Received the message for the Inventor destination.");

		try {
			long uid = message.getLongProperty(LoggerConstants.UID);
			String idocNumber = message.getStringProperty(LoggerConstants.IDOC_NO);
			String timestamp = message.getStringProperty(LoggerConstants.TIMESTAMP);

			auditLog.info(uid + "," + idocNumber + "," + timestamp + ",false");

			ObjectMessage objectMessage = (ObjectMessage) message;
			InventoryTO inventory = (InventoryTO) objectMessage.getObject();

			infoLog.info("Received the Inventory Message from SAP. \n" + inventory.toString());

			super.notify(inventory , PlatformDestinationEnum.INVENTORY);
			
			SapMomTO sapIdoc = inventory.getSapIdoc();
			auditLog.info(sapIdoc.getAckUID() + "," + sapIdoc.getIdocNumber() + "," + sapIdoc.getTimestamp() + ",true");
		} catch (JMSException e) {
			infoLog.error("Error in processing hornetQ inventory message.", e);
			throw JmsUtils.convertJmsAccessException(e);
		} catch (Exception e) {
			infoLog.error("Error in processing hornetQ inventory message.", e);
			throw new PlatformException(e);
			
		}
	}
}
