/**
 * 
 */
package com.fb.platform.sap.client.idoc.sap.deliveryDelete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fb.commons.mom.to.DeliveryDeleteTO;
import com.fb.commons.mom.to.MailTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.deliveryDelete.impl.DeliveryDeleteIDocHandler;

/**
 * @author nehaga
 *
 */

@ContextConfiguration(
		locations={"classpath:/test-applicationContext-resources.xml",
				"classpath:/platformMom-applicationContext-resources.xml",
				"classpath:/platformMom-applicationContext-service.xml",
				"classpath:/*platformMom-applicationContext-resources.xml",
				"classpath:/*platformMom-applicationContext-service.xml",
				"classpath:/test-sapReceiver-applicationContext.xml",
				"classpath:/sapReceiver-applicationContext-service.xml",
				"classpath:/*applicationContext.xml",
				"classpath:/*applicationContext-service.xml",
				"classpath*:/*applicationContext.xml",
				"classpath*:/*applicationContext-service.xml",
				"classpath*:/*platformMom-applicationContext-resources.xml",
				"classpath*:/*platformMom-applicationContext-service.xml",
				"classpath:**/test-applicationContext*.xml"})

public class DeliveryDeleteIdocHandlerTest extends BaseTestCase {
	
	@Autowired
	private MomManager momManager = null;
	
	@Autowired
	private PlatformIDocHandlerFactory platformIDocHandlerFactory = null;
	
	private TestDeliveryDeleteReceiver testReceiver = null;
	
	private TestDeliveryDeleteCorruptReceiver corruptTestReceiver = null;
	
	
	@Before
	public void init() {
		testReceiver  = new TestDeliveryDeleteReceiver();
		corruptTestReceiver = new TestDeliveryDeleteCorruptReceiver();
	}
	
	@Test
	public void processDeliveryDeleteIdoc() throws IOException, Exception {
		momManager.registerReceiver(PlatformDestinationEnum.DELIVERY_DELETE, testReceiver);
		
		InputStream deliveryDeleteStream = DeliveryDeleteIdocHandlerTest.class.getClassLoader().getResourceAsStream("zatgdeld.xml");
		DeliveryDeleteIDocHandler deliveryDeleteIDocHandler = (DeliveryDeleteIDocHandler) platformIDocHandlerFactory.getHandler(DeliveryDeleteIDocHandler.DELIVERY_DELETE);
		StringWriter sw = new StringWriter();
		IOUtils.copy(deliveryDeleteStream, sw);
		deliveryDeleteIDocHandler.handle(sw.toString());
	}
	
	@Test
	public void processCorruptDeliveryDeleteIdoc() throws IOException, Exception {
		momManager.registerReceiver(PlatformDestinationEnum.CORRUPT_IDOCS, corruptTestReceiver);
		
		InputStream deliveryDeleteStream = DeliveryDeleteIdocHandlerTest.class.getClassLoader().getResourceAsStream("ztinla_dlvry.xml");
		DeliveryDeleteIDocHandler deliveryDeleteIDocHandler = (DeliveryDeleteIDocHandler) platformIDocHandlerFactory.getHandler(DeliveryDeleteIDocHandler.DELIVERY_DELETE);
		StringWriter sw = new StringWriter();
		IOUtils.copy(deliveryDeleteStream, sw);
		deliveryDeleteIDocHandler.handle(sw.toString());
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
				assertEquals(20, deliveryDelete.getItemNo());
				assertEquals("5057272903", deliveryDelete.getOrderNo());
				assertEquals("8100140628", deliveryDelete.getDeliveryNo());
				assertEquals("VL03N", deliveryDelete.getTransactionCode());
			} else if (count == 2) {
				assertEquals(10, deliveryDelete.getItemNo());
				assertEquals("5057272903", deliveryDelete.getOrderNo());
				assertEquals("8100140628", deliveryDelete.getDeliveryNo());
				assertEquals("VL02N", deliveryDelete.getTransactionCode());
			} else if (count > 2) {
				throw new IllegalArgumentException("Invalid message");
			}
			
			count++;
			System.out.println("TestDeliveryDeleteReceiver Incremented count to : " + count);
		}
	}
	
	private static class TestDeliveryDeleteCorruptReceiver implements PlatformMessageReceiver {

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestDeliveryDeleteCorruptReceiver, received the corrupt delivery delete message, message is : " + message);
			MailTO mailTo = (MailTO) message;
			assertNotNull(mailTo);
		}
	}
}
