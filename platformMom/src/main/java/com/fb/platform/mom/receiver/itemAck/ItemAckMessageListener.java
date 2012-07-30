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

import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;

/**
 * @author nehaga
 *
 */
public class ItemAckMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log logger = LogFactory.getLog(ItemAckMessageListener.class);

	@Override
	public void onMessage(Message message) {
		logger.info("Received the message for the itemAck destination.");
		System.out.println("Received the message for the itemAck destination.");
		ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			ItemTO itemAck = (ItemTO) objectMessage.getObject();

			logger.info("Received the item ack Message from SAP. \n" + itemAck.toString());
			System.out.println("Received the item ack Message from SAP. \n" + itemAck.toString());

			super.notify(itemAck);
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
		}
	}
}
