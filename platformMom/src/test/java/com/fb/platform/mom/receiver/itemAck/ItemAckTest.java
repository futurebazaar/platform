/**
 * 
 */
package com.fb.platform.mom.receiver.itemAck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.to.CancelItemTO;
import com.fb.commons.mom.to.ItemInvoiceTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.PgiCreationItemTO;
import com.fb.commons.mom.to.PgrCreationItemTO;
import com.fb.commons.mom.to.ReturnDeliveryTO;
import com.fb.commons.mom.to.ReturnInvoiceTO;
import com.fb.commons.mom.to.ReturnOrderTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class ItemAckTest extends BaseTestCase {
	
	@Autowired
	private MomManager momManager = null;

	private TestItemAckReceiver testReceiver = null;
	
	@Before
	public void init() {
		testReceiver = new TestItemAckReceiver();
	}
	
	@Test
	public void sendItemAckMessages() {
		
		momManager.registerReceiver(PlatformDestinationEnum.ITEM_ACK, testReceiver);
		
		ItemTO itemCState = new ItemTO();
		itemCState.setSapDocumentId(10);
		itemCState.setAtgDocumentId(10);
		itemCState.setDeliveryNumber("8100140302");
		itemCState.setOrderId("5057271857");

		SapMomTO sapIdoc1 = new SapMomTO(100);
		sapIdoc1.setIdocNumber("x100");

		itemCState.setSapIdoc(sapIdoc1);

		momManager.send(PlatformDestinationEnum.ITEM_ACK, itemCState);
		
		PgiCreationItemTO pgiItemTO = new PgiCreationItemTO();
		pgiItemTO.setItemTO(itemCState);
		pgiItemTO.setPgiCreationDate(new DateTime(2009, 02, 22, 7, 0));

		SapMomTO sapIdoc2 = new SapMomTO(100);
		sapIdoc2.setIdocNumber("x100");

		pgiItemTO.setSapIdoc(sapIdoc2);

		momManager.send(PlatformDestinationEnum.ITEM_ACK, pgiItemTO);
		
		CancelItemTO cancelItemTO = new CancelItemTO();
		cancelItemTO.setItemTO(itemCState);
		cancelItemTO.setCancelInvoiceNumber("1234567");

		SapMomTO sapIdoc3 = new SapMomTO(100);
		sapIdoc3.setIdocNumber("x100");

		cancelItemTO.setSapIdoc(sapIdoc3);

		momManager.send(PlatformDestinationEnum.ITEM_ACK, cancelItemTO);
		
		ItemInvoiceTO itemInvoiceTO = new ItemInvoiceTO();
		itemInvoiceTO.setItemTO(itemCState);
		itemInvoiceTO.setInvoiceDate(new DateTime(2012, 02, 22, 7, 0));
		itemInvoiceTO.setInvoiceNumber("1234567");

		SapMomTO sapIdoc4 = new SapMomTO(100);
		sapIdoc4.setIdocNumber("x100");

		itemInvoiceTO.setSapIdoc(sapIdoc4);

		momManager.send(PlatformDestinationEnum.ITEM_ACK, itemInvoiceTO);
		
		PgrCreationItemTO pgrItemTO = new PgrCreationItemTO();
		pgrItemTO.setItemTO(itemCState);
		pgrItemTO.setPgrCreationDate(new DateTime(2009, 02, 22, 7, 0));

		SapMomTO sapIdoc5 = new SapMomTO(100);
		sapIdoc5.setIdocNumber("x100");

		pgrItemTO.setSapIdoc(sapIdoc5);

		momManager.send(PlatformDestinationEnum.ITEM_ACK, pgrItemTO);
		
		ReturnDeliveryTO returnDeliveryTO = new ReturnDeliveryTO();
		returnDeliveryTO.setItemTO(itemCState);
		returnDeliveryTO.setReturnCreatedBy("XYZ");
		returnDeliveryTO.setReturnCreatedDate(new DateTime(2012, 02, 22, 7, 0));
		returnDeliveryTO.setNumber("7654321");
		returnDeliveryTO.setType("defected");

		SapMomTO sapIdoc6 = new SapMomTO(100);
		sapIdoc6.setIdocNumber("x100");

		returnDeliveryTO.setSapIdoc(sapIdoc6);

		momManager.send(PlatformDestinationEnum.ITEM_ACK, returnDeliveryTO);
		
		ReturnInvoiceTO returnInvoiceTO = new ReturnInvoiceTO();
		returnInvoiceTO.setItemTO(itemCState);
		returnInvoiceTO.setInvoiceNet("NET");
		returnInvoiceTO.setInvoiceDate(new DateTime(2012, 02, 22, 7, 0));
		returnInvoiceTO.setNumber("7654321");
		returnInvoiceTO.setType("defected");

		SapMomTO sapIdoc7 = new SapMomTO(100);
		sapIdoc7.setIdocNumber("x100");

		returnInvoiceTO.setSapIdoc(sapIdoc7);

		momManager.send(PlatformDestinationEnum.ITEM_ACK, returnInvoiceTO);
		
		ReturnOrderTO returnOrderTO = new ReturnOrderTO();
		returnOrderTO.setItemTO(itemCState);
		returnOrderTO.setStorageLocation("NYC");
		returnOrderTO.setQuantity(new BigDecimal(1));
		returnOrderTO.setOrderId("7654321");
		returnOrderTO.setCategory("defected");

		SapMomTO sapIdoc8 = new SapMomTO(100);
		sapIdoc8.setIdocNumber("x100");

		returnOrderTO.setSapIdoc(sapIdoc8);

		momManager.send(PlatformDestinationEnum.ITEM_ACK, returnOrderTO);
		
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
			
			if (PgrCreationItemTO.class.isInstance(itemAck)) {
				PgrCreationItemTO pgrItemTO = (PgrCreationItemTO) itemAck;
				assertTrue(new DateTime(2009, 02, 22, 7, 0).equals(pgrItemTO.getPgrCreationDate()));
			} else if (PgiCreationItemTO.class.isInstance(itemAck)) {
				PgiCreationItemTO pgiItemTO = (PgiCreationItemTO) itemAck;
				assertTrue(new DateTime(2009, 02, 22, 7, 0).equals(pgiItemTO.getPgiCreationDate()));
			} else if (ItemInvoiceTO.class.isInstance(itemAck)) {
				ItemInvoiceTO itemInvoiceTO = (ItemInvoiceTO) itemAck;
				assertTrue(new DateTime(2012, 02, 22, 7, 0).equals(itemInvoiceTO.getInvoiceDate()));
				assertEquals("1234567", itemInvoiceTO.getInvoiceNumber());
			} else if (CancelItemTO.class.isInstance(itemAck)) {
				CancelItemTO cancelItemTO = (CancelItemTO) itemAck;
				assertEquals("1234567", cancelItemTO.getCancelInvoiceNumber());
			} else if (ReturnDeliveryTO.class.isInstance(itemAck)) {
				ReturnDeliveryTO returnDeliveryTO = (ReturnDeliveryTO) itemAck;
				assertEquals("7654321", returnDeliveryTO.getNumber());
				assertEquals("XYZ", returnDeliveryTO.getReturnCreatedBy());
				assertTrue(new DateTime(2012, 02, 22, 7, 0).equals(returnDeliveryTO.getReturnCreatedDate()));
				assertEquals("defected", returnDeliveryTO.getType());
			} else if (ReturnInvoiceTO.class.isInstance(itemAck)) {
				ReturnInvoiceTO returnInvoiceTO = (ReturnInvoiceTO) itemAck;
				assertEquals("NET", returnInvoiceTO.getInvoiceNet());
				assertTrue(new DateTime(2012, 02, 22, 7, 0).equals(returnInvoiceTO.getInvoiceDate()));
				assertEquals("7654321", returnInvoiceTO.getNumber());
				assertEquals("defected", returnInvoiceTO.getType());
			} else if (ReturnOrderTO.class.isInstance(itemAck)) {
				ReturnOrderTO returnOrderTO = (ReturnOrderTO) itemAck;
				assertEquals("NYC", returnOrderTO.getStorageLocation());
				assertEquals(new BigDecimal(1).intValue(), returnOrderTO.getQuantity().intValue());
				assertEquals("7654321", returnOrderTO.getOrderId());
				assertEquals("defected", returnOrderTO.getCategory());
			}
		}
		
		private boolean checkItemParam(ItemTO itemTo) {
			try {
				assertEquals(10, itemTo.getSapDocumentId());
				assertEquals(10, itemTo.getAtgDocumentId());
				assertEquals("8100140302", itemTo.getDeliveryNumber());
				assertEquals("5057271857", itemTo.getOrderId());
			} catch (AssertionError e) {
				return false;
			}
			
			return true;
		}
	}
}
