/**
 * 
 */
package com.fb.platform.mom.receiver.inventory;

import static org.junit.Assert.assertEquals;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author vinayak
 *
 */
public class InventoryTest extends BaseTestCase {

	@Autowired
	private InventorySender inventorySender = null;

	@Autowired
	private InventoryMessageListener inventoryListener = null;

	private TestInventoryReceiver testReceiver = null;

	@Before
	public void init() {
		testReceiver = new TestInventoryReceiver();
	}

	@Test
	public void sendTwoMessages() {

		inventoryListener.addReceiver(testReceiver);

		String message1 = "First inventory ack.";
		inventorySender.sendMessage(message1);

		String message2 = "Second inventory ack";
		inventorySender.sendMessage(message2);
	}

	@After
	public void verifyResult() {
		assertEquals(1, testReceiver.getCount());
	}

	private static class TestInventoryReceiver implements PlatformMessageReceiver {

		private int count = 0;

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			ObjectMessage jmsMessage = (ObjectMessage) message;
			String messageStr = null;
			try {
				messageStr = (String)jmsMessage.getObject();
			} catch (JMSException e) {
				throw JmsUtils.convertJmsAccessException(e);
			}

			if (count == 0) {
				assertEquals("First inventory ack.", messageStr);
			} else if (count == 1) {
				assertEquals("Second inventory ack", messageStr);
			} else {
				throw new IllegalArgumentException("Invalid message");
			}
			count ++;
		}

		public int getCount() {
			return count;
		}
	}
}
