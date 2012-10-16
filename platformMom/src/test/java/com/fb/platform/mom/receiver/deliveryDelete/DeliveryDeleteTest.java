package com.fb.platform.mom.receiver.deliveryDelete;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.to.DeliveryDeleteTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteTest extends BaseTestCase {
	@Autowired
	private MomManager momManager = null;

	private TestDeliveryDeleteReceiver testReceiver = null;
	
	@Before
	public void init() {
		testReceiver = new TestDeliveryDeleteReceiver();
	}

	@Test
	public void sendDeliveyDeleteMessages() {
		
		momManager.registerReceiver(PlatformDestinationEnum.DELIVERY_DELETE, testReceiver);
		
		DeliveryDeleteTO deliveryDeleteTO1 = new DeliveryDeleteTO();
		deliveryDeleteTO1.setOrderNo("5100140309");
		deliveryDeleteTO1.setItemNo(0);
		deliveryDeleteTO1.setDeliveryNo("8100140309");
		deliveryDeleteTO1.setTransactionCode("VL03N");
		deliveryDeleteTO1.setUser("YOGENDRAB");
		deliveryDeleteTO1.setDate("20120625");
		deliveryDeleteTO1.setTime("172208");

		SapMomTO sapIdoc1 = new SapMomTO(100);
		sapIdoc1.setIdocNumber("x100");

		deliveryDeleteTO1.setSapIdoc(sapIdoc1);
		momManager.send(PlatformDestinationEnum.DELIVERY_DELETE, deliveryDeleteTO1);
		
		DeliveryDeleteTO deliveryDeleteTO2 = new DeliveryDeleteTO();
		deliveryDeleteTO2.setOrderNo("5100140309");
		deliveryDeleteTO2.setItemNo(10);
		deliveryDeleteTO2.setDeliveryNo("8100140309");
		deliveryDeleteTO2.setTransactionCode("VL03G");
		deliveryDeleteTO2.setUser("YOGENDRAB");
		deliveryDeleteTO2.setDate("20120625");
		deliveryDeleteTO2.setTime("172208");

		SapMomTO sapIdoc2 = new SapMomTO(100);
		sapIdoc2.setIdocNumber("x100");

		deliveryDeleteTO2.setSapIdoc(sapIdoc2);
		momManager.send(PlatformDestinationEnum.DELIVERY_DELETE, deliveryDeleteTO2);
	}
	
	private static class TestDeliveryDeleteReceiver implements PlatformMessageReceiver {

		private static int count = 1;

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestDeliveryDeleteReceiver, received the delivery delete message, count is : " + count + ", message is : " + message);
			DeliveryDeleteTO deliveryDelete = (DeliveryDeleteTO) message;

			if (count == 1) {
				assertEquals(0, deliveryDelete.getItemNo());
				assertEquals("VL03N", deliveryDelete.getTransactionCode());
			} else if (count == 2) {
				assertEquals(10, deliveryDelete.getItemNo());
				assertEquals("VL03G", deliveryDelete.getTransactionCode());
			} else if (count > 2) {
				throw new IllegalArgumentException("Invalid message");
			}
			
			count++;
			System.out.println("TestDeliveryDeleteReceiver Incremented count to : " + count);
		}
	}
}
