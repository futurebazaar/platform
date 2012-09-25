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

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;

/**
 * @author nehaga
 *
 */
public class ItemAckMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log infoLog = LogFactory.getLog("ITEM_ACK_LOG");

	private static Log errorLog = LogFactory.getLog("ITEM_ACK_ERROR");

	@Override
	public void onMessage(Message message) {
		infoLog.info("Received the message for the itemAck destination.");
		ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			ItemTO itemAck = (ItemTO) objectMessage.getObject();

			infoLog.info("Received the item ack Message from SAP. \n" + itemAck.toString());

			super.notify(itemAck , PlatformDestinationEnum.ITEM_ACK);
		} catch (JMSException e) {
			errorLog.error("Error in processing hornetQ item ack message.", e);
			throw JmsUtils.convertJmsAccessException(e);
		} catch (Exception e) {
			errorLog.error("Error in processing hornetQ item ack message.", e);
			throw new PlatformException(e);
		}
	}
}
