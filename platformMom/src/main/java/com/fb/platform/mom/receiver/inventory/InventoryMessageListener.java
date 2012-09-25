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
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;

/**
 * @author vinayak
 *
 */
public class InventoryMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log infoLog = LogFactory.getLog("INVENTORY_LOG");
	
	private static Log errorLog = LogFactory.getLog("INVENTORY_ERROR");

	@Override
	public void onMessage(Message message) {
		infoLog.info("Received the message for the Inventor destination.");
		System.out.println("Received the message for the Inventor destination.");

		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			InventoryTO inventory = (InventoryTO) objectMessage.getObject();

			infoLog.info("Received the Inventory Message from SAP. \n" + inventory.toString());

			super.notify(inventory , PlatformDestinationEnum.INVENTORY);
		} catch (JMSException e) {
			errorLog.error("Error in processing hornetQ inventory message.", e);
			throw JmsUtils.convertJmsAccessException(e);
		} catch (Exception e) {
			errorLog.error("Error in processing hornetQ inventory message.", e);
			throw new PlatformException(e);
			
		}
	}
}
