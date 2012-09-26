/**
 * 
 */
package com.fb.platform.sap.client.idoc.sap.deliveryInventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fb.commons.mom.to.DeliveryDeleteTO;
import com.fb.commons.mom.to.InventoryTO;
import com.fb.commons.mom.to.MailTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.deliveryDelete.impl.DeliveryDeleteIDocHandler;
import com.fb.platform.sap.client.idoc.platform.inventory.impl.DeliveryInventoryIDocHandler;

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

public class DeliveryInventoryIdocHandlerTest extends BaseTestCase {
	
	@Autowired
	private MomManager momManager = null;
	
	@Autowired
	private PlatformIDocHandlerFactory platformIDocHandlerFactory = null;
	
	private TestDeliveryInventoryReceiver testReceiver = null;
	
	@Before
	public void init() {
		testReceiver  = new TestDeliveryInventoryReceiver();
	}
	
	@Test
	public void processDeliveryDeleteIdoc() throws IOException ,Exception {
		momManager.registerReceiver(PlatformDestinationEnum.INVENTORY, testReceiver);
		
		InputStream deliveryInventoryStream = DeliveryInventoryIdocHandlerTest.class.getClassLoader().getResourceAsStream("ztinla_dlvry.xml");
		DeliveryInventoryIDocHandler deliveryInventoryIDocHandler = (DeliveryInventoryIDocHandler) platformIDocHandlerFactory.getHandler(DeliveryInventoryIDocHandler.DELIVERY_INVENTORY_IDOC_TYPE);
		StringWriter sw = new StringWriter();
		IOUtils.copy(deliveryInventoryStream, sw);
		deliveryInventoryIDocHandler.handle(sw.toString());
	}
	
	private static class TestDeliveryInventoryReceiver implements PlatformMessageReceiver {

		private static int count = 1;

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestDeliveryInventoryReceiver, received the delivery inventory message, count is : " + count + ", message is : " + message);
			InventoryTO inventoryTO = (InventoryTO) message;

			if (count == 1) {
				assertEquals("2786", inventoryTO.getIssuingSite());
				assertEquals("000000000100315457", inventoryTO.getArticleId());
				assertEquals("10", inventoryTO.getIssuingStorageLoc());
				assertEquals("311", inventoryTO.getMovementType());
				assertEquals("2.000", inventoryTO.getQuantity());
				assertEquals("VNA07", inventoryTO.getTransactionCode());
				assertEquals("2786", inventoryTO.getReceivingSite());
				assertEquals("90", inventoryTO.getReceivingStorageLoc());
				assertEquals("EA", inventoryTO.getSellingUnit());
			} else if (count > 1) {
				throw new IllegalArgumentException("Invalid message");
			}
			
			count++;
			System.out.println("TestDeliveryInventoryReceiver Incremented count to : " + count);
		}
	}
}
