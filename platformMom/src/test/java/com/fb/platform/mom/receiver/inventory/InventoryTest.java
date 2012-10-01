/**
 * 
 */
package com.fb.platform.mom.receiver.inventory;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.to.InventoryTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author vinayak
 *
 */
public class InventoryTest extends BaseTestCase {

	@Autowired
	private MomManager momManager = null;

	private TestInventoryReceiver testReceiver = null;

	@Before
	public void init() {
		testReceiver = new TestInventoryReceiver();
	}

	@Test
	public void sendInventoryMessages() {

		//inventoryListener.addReceiver(testReceiver);
		momManager.registerReceiver(PlatformDestinationEnum.INVENTORY, testReceiver);

		InventoryTO inventory1 = new InventoryTO();
		inventory1.setArticleId("article1");
		inventory1.setIssuingSite("issuingsite1");
		inventory1.setIssuingStorageLoc("issuingstoragelocation1");

		SapMomTO sapIdoc1 = new SapMomTO(100);
		sapIdoc1.setIdocNumber("x100");

		inventory1.setSapIdoc(sapIdoc1);

		momManager.send(PlatformDestinationEnum.INVENTORY, inventory1);
		//inventorySender.sendMessage(inventory1);

		InventoryTO inventory2 = new InventoryTO();
		inventory2.setArticleId("article2");
		inventory2.setIssuingSite("issuingsite2");
		inventory2.setIssuingStorageLoc("issuingstoragelocation2");
		SapMomTO sapIdoc2 = new SapMomTO(100);
		sapIdoc2.setIdocNumber("x100");

		inventory2.setSapIdoc(sapIdoc2);
		momManager.send(PlatformDestinationEnum.INVENTORY, inventory2);
		
		InventoryTO inventory3 = new InventoryTO();
		inventory3.setArticleId("article3");
		inventory3.setIssuingSite("issuingsite3");
		inventory3.setIssuingStorageLoc("issuingstoragelocation3");
		SapMomTO sapIdoc3 = new SapMomTO(100);
		sapIdoc3.setIdocNumber("x100");

		inventory3.setSapIdoc(sapIdoc3);
		momManager.send(PlatformDestinationEnum.INVENTORY, inventory3);

		InventoryTO inventory4 = new InventoryTO();
		inventory4.setArticleId("article4");
		inventory4.setIssuingSite("issuingsite4");
		inventory4.setIssuingStorageLoc("issuingstoragelocation4");
		SapMomTO sapIdoc4 = new SapMomTO(100);
		sapIdoc4.setIdocNumber("x100");

		inventory4.setSapIdoc(sapIdoc4);
		momManager.send(PlatformDestinationEnum.INVENTORY, inventory4);
		
		InventoryTO inventory5 = new InventoryTO();
		inventory5.setArticleId("article5");
		inventory5.setIssuingSite("issuingsite5");
		inventory5.setIssuingStorageLoc("issuingstoragelocation5");
		SapMomTO sapIdoc5 = new SapMomTO(100);
		sapIdoc5.setIdocNumber("x100");

		inventory5.setSapIdoc(sapIdoc5);
		momManager.send(PlatformDestinationEnum.INVENTORY, inventory5);
	}

	@After
	public void verifyResult() {
		//System.out.println("InventoryTest, verifying the count.");
		//assertEquals(2, testReceiver.getCount());
	}

	private static class TestInventoryReceiver implements PlatformMessageReceiver {

		private static int count = 1;

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestInventoryReceiver, received the inventory message, count is : " + count + ", message is : " + message);
			InventoryTO inventory = (InventoryTO) message;

			if (count == 1) {
				assertEquals("article1", inventory.getArticleId());
			} else if (count == 2) {
				assertEquals("article2", inventory.getArticleId());
			} else if (count == 3) {
				assertEquals("article3", inventory.getArticleId());
			} else if (count == 4) {
				assertEquals("article4", inventory.getArticleId());
			} else if (count == 5) {
				assertEquals("article5", inventory.getArticleId());
			} else if (count > 5){
				throw new IllegalArgumentException("Invalid message");
			}
			count++;
			System.out.println("TestInventoryReceiver Incremented count to : " + count);
		}
	}
}
