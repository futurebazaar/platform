/**
 * 
 */
package com.fb.platform.sap.client.idoc.sap.inventory;

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

import com.fb.commons.mom.to.InventoryTO;
import com.fb.commons.mom.to.MailTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.inventory.impl.InventoryIDocHandler;

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

public class InventoryIdocHandlerTest extends BaseTestCase {
	
	@Autowired
	private MomManager momManager = null;
	
	@Autowired
	private PlatformIDocHandlerFactory platformIDocHandlerFactory = null;
	
	private TestInventoryReceiver testReceiver = null;
	
	private TestInventoryCorruptReceiver corruptTestReceiver = null;
	
	
	@Before
	public void init() {
		testReceiver  = new TestInventoryReceiver();
		corruptTestReceiver = new TestInventoryCorruptReceiver();
	}
	
	@Test
	public void processInventoryIdoc() throws IOException ,Exception {
		momManager.registerReceiver(PlatformDestinationEnum.INVENTORY, testReceiver);
		
		InputStream inventoryStream = InventoryIdocHandlerTest.class.getClassLoader().getResourceAsStream("ztinla_idoctype.xml");
		InventoryIDocHandler inventoryIDocHandler = (InventoryIDocHandler) platformIDocHandlerFactory.getHandler(InventoryIDocHandler.INVENTORY_IDOC_TYPE);
		StringWriter sw = new StringWriter();
		IOUtils.copy(inventoryStream, sw);
		inventoryIDocHandler.handle(sw.toString());
	}
	
	@Test
	public void processCorruptInventoryIdoc() throws IOException ,Exception {
		momManager.registerReceiver(PlatformDestinationEnum.CORRUPT_IDOCS, corruptTestReceiver);
		
		InputStream inventoryStream = InventoryIdocHandlerTest.class.getClassLoader().getResourceAsStream("zatgdeld.xml");
		InventoryIDocHandler inventoryIDocHandler = (InventoryIDocHandler) platformIDocHandlerFactory.getHandler(InventoryIDocHandler.INVENTORY_IDOC_TYPE);
		StringWriter sw = new StringWriter();
		IOUtils.copy(inventoryStream, sw);
		inventoryIDocHandler.handle(sw.toString());
	}
	
	private static class TestInventoryReceiver implements PlatformMessageReceiver {

		private static int count = 1;

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestInventoryReceiver, received the inventory message, count is : " + count + ", message is : " + message);
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
			} else if (count > 2) {
				throw new IllegalArgumentException("Invalid message");
			}
			
			count++;
			System.out.println("TestInventoryReceiver Incremented count to : " + count);
		}
	}
	
	private static class TestInventoryCorruptReceiver implements PlatformMessageReceiver {

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestInventoryCorruptReceiver, received the corrupt inventory message, message is : " + message);
			MailTO mailTo = (MailTO) message;
			assertNotNull(mailTo);
		}
	}
}
