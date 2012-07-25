/**
 * 
 */
package com.fb.platform.mom.receiver.order;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.mom.to.OrderTO;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;

/**
 * @author nehaga
 *
 */
public class OrderMessageListener extends AbstractPlatformListener implements MessageListener {

	private static Log logger = LogFactory.getLog(OrderMessageListener.class);

	@Override
	public void onMessage(Message message) {
		logger.info("Received the message for the order destination.");
		System.out.println("Received the message for the order destination.");
		ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			OrderTO order = (OrderTO) objectMessage.getObject();

			logger.info("Received the order Message from SAP. \n" + order.toString());
			System.out.println("Received the order Message from SAP. \n" + order.toString());

			super.notify(order);
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
		}
	}
}
