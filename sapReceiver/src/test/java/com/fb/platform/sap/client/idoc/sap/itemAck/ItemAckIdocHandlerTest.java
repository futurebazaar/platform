/**
 * 
 */
package com.fb.platform.sap.client.idoc.sap.itemAck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fb.commons.mom.to.CancelItemTO;
import com.fb.commons.mom.to.ItemInvoiceTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.PgiCreationItemTO;
import com.fb.commons.mom.to.PgrCreationItemTO;
import com.fb.commons.mom.to.ReturnDeliveryTO;
import com.fb.commons.mom.to.ReturnInvoiceTO;
import com.fb.commons.mom.to.ReturnOrderTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.itemAck.impl.ItemAckIDocHandler;

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

public class ItemAckIdocHandlerTest extends BaseTestCase {
	
	@Autowired
	private MomManager momManager = null;
	
	@Autowired
	private PlatformIDocHandlerFactory platformIDocHandlerFactory = null;
	
	private TestItemAckReceiver testReceiver = null;
	
	@Before
	public void init() {
		testReceiver  = new TestItemAckReceiver();
	}
	
	@Test
	public void processDeliveryDeleteIdoc() throws IOException ,Exception {
		momManager.registerReceiver(PlatformDestinationEnum.ITEM_ACK, testReceiver);
		
		InputStream itemAckStream = ItemAckIdocHandlerTest.class.getClassLoader().getResourceAsStream("zatgflow.xml");
		ItemAckIDocHandler itemAckIDocHandler = (ItemAckIDocHandler) platformIDocHandlerFactory.getHandler(ItemAckIDocHandler.ITEM_ACK_IDOC_TYPE);
		StringWriter sw = new StringWriter();
		IOUtils.copy(itemAckStream, sw);
		itemAckIDocHandler.handle(sw.toString());
	}
	
	private static class TestItemAckReceiver implements PlatformMessageReceiver {

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestItemAckReceiver, received the item ack, message is : " + message);
			ItemTO itemAck = (ItemTO) message;
			
			assertTrue(checkItemParam(itemAck));
			
			if (ItemInvoiceTO.class.isInstance(itemAck)) {
				ItemInvoiceTO itemInvoiceTO = (ItemInvoiceTO) itemAck;
				assertEquals(10, itemInvoiceTO.getSapDocumentId());
				assertEquals("M", itemInvoiceTO.getOrderState());
			} else if (ReturnDeliveryTO.class.isInstance(itemAck)) {
				ReturnDeliveryTO returnDeliveryTO = (ReturnDeliveryTO) itemAck;
				assertEquals(20, returnDeliveryTO.getSapDocumentId());
				assertEquals("T", returnDeliveryTO.getOrderState());
			} else if (itemAck.getOrderState().equalsIgnoreCase("J")){
				assertEquals(30, itemAck.getSapDocumentId());
			} else {
				assertEquals("C", itemAck.getOrderState());
				assertEquals(40, itemAck.getSapDocumentId());
			}
		}
		
		private boolean checkItemParam(ItemTO itemTo) {
			try {
				assertEquals("8100140827", itemTo.getDeliveryNumber());
				assertEquals("5057287309", itemTo.getOrderId());
			} catch (AssertionError e) {
				return false;
			}
			
			return true;
		}
	}
}
