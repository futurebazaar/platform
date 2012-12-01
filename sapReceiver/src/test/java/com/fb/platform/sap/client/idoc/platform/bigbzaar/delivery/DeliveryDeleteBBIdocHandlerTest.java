/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.bigbzaar.delivery;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fb.commons.mom.bigBazaar.to.DeliveryDeleteBBHeaderTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryDeleteBBTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryDeleteItemBBTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.bigBazaar.deliveryDelete.DeliveryDeleteBBIdocHandler;

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

public class DeliveryDeleteBBIdocHandlerTest extends BaseTestCase {
	
	@Autowired
	private MomManager momManager = null;
	
	@Autowired
	private PlatformIDocHandlerFactory platformIDocHandlerFactory = null;
	
	private TestDeliveryDeleteReceiver testReceiver = null;
	
	@Before
	public void init() {
		testReceiver  = new TestDeliveryDeleteReceiver();
	}
	
	@Test
	public void processDeliveryDeleteIdoc() throws IOException ,Exception {
		momManager.registerReceiver(PlatformDestinationEnum.DELIVERY_DELETE_BB, testReceiver);
		
		InputStream deliveryDeleteStream = DeliveryDeleteBBIdocHandlerTest.class.getClassLoader().getResourceAsStream("DelvDeleteBB.xml");
		DeliveryDeleteBBIdocHandler deliveryDeleteIDocHandler = (DeliveryDeleteBBIdocHandler) platformIDocHandlerFactory.getHandler(DeliveryDeleteBBIdocHandler.DELIVERY_DELETE_BB_IDOC_TYPE);
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
			System.out.println("TestDeliveryDeleteReceiver, received the DeliveryDelete message, count is : " + count + ", message is : " + message);
			DeliveryDeleteBBTO deliveryDeleteTO = (DeliveryDeleteBBTO) message;
			if (count == 1) {
				assertEquals("0000040000687160", deliveryDeleteTO.getSapIdoc().getIdocNumber());
				
				DeliveryDeleteBBHeaderTO deliveryDeleteBBHeader = deliveryDeleteTO.getDeliveryDeleteHeader();
				assertEquals("I000000636", deliveryDeleteBBHeader.getOrderNumber());
				assertEquals(145000409, deliveryDeleteBBHeader.getDeliveryNumber());
				assertEquals("VL02N", deliveryDeleteBBHeader.getDeletedCode());
				assertEquals(2, deliveryDeleteBBHeader.getDeletedItems().size());
				
				DeliveryDeleteItemBBTO item1 = deliveryDeleteBBHeader.getDeletedItems().get(0);
				
				assertEquals(10, item1.getItemNumber());
				assertEquals("ABHISHEKG", item1.getUser());
				
				DeliveryDeleteItemBBTO item2 = deliveryDeleteBBHeader.getDeletedItems().get(1);
				
				assertEquals(20, item2.getItemNumber());
				assertEquals("ABHISHEKG", item2.getUser());
			} else if (count > 1) {
				throw new IllegalArgumentException("Invalid message");
			}
			
			count++;
			System.out.println("TestDeliveryDeleteReceiver Incremented count to : " + count);
		}
	}
}
