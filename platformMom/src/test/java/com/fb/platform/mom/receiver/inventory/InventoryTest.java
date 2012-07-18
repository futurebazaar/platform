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
	private InventorySender inventorySender = null;

	@Autowired
	private MomManager momManager = null;

	private TestInventoryReceiver testReceiver = null;

	@Before
	public void init() {
		testReceiver = new TestInventoryReceiver();
	}

	@Test
	public void sendTwoMessages() {

		//inventoryListener.addReceiver(testReceiver);
		momManager.registerReceiver(PlatformDestinationEnum.INVENTORY, testReceiver);

		InventoryTO inventory1 = new InventoryTO();
		inventory1.setArticleId("article1");
		inventory1.setIssuingSite("issuingsite1");
		inventory1.setIssuingStorageLoc("issuingstoragelocation1");
		inventorySender.sendMessage(inventory1);

		InventoryTO inventory2 = new InventoryTO();
		inventory2.setArticleId("article2");
		inventory2.setIssuingSite("issuingsite2");
		inventory2.setIssuingStorageLoc("issuingstoragelocation2");
		inventorySender.sendMessage(inventory2);
		
		InventoryTO inventory3 = new InventoryTO();
		inventory1.setArticleId("neha1");
		inventory1.setIssuingSite("neha1");
		inventory1.setIssuingStorageLoc("neha1");
		inventorySender.sendMessage(inventory3);

		InventoryTO inventory4 = new InventoryTO();
		inventory2.setArticleId("neha2");
		inventory2.setIssuingSite("neha2");
		inventory2.setIssuingStorageLoc("neha2");
		inventorySender.sendMessage(inventory4);
		
		InventoryTO inventory5 = new InventoryTO();
		inventory1.setArticleId("neha3");
		inventory1.setIssuingSite("neha3");
		inventory1.setIssuingStorageLoc("neha3");
		inventorySender.sendMessage(inventory5);
	}

	@After
	public void verifyResult() {
		//System.out.println("InventoryTest, verifying the count.");
		//assertEquals(2, testReceiver.getCount());
	}

	private static class TestInventoryReceiver implements PlatformMessageReceiver {

		private int count = 0;

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestInventoryReceiver, received the inventory message, count is : " + count + ", message is : " + message);
			InventoryTO inventory = (InventoryTO) message;

			if (count == 0) {
				assertEquals("article1", inventory.getArticleId());
			} else if (count == 1) {
				assertEquals("article2", inventory.getArticleId());
			} else {
				throw new IllegalArgumentException("Invalid message");
			}
			count ++;
			System.out.println("TestInventoryReceiver Incremented count to : " + count);
		}

		public int getCount() {
			return count;
		}
	}
}
