/**
 * 
 */
package com.fb.platform.dao.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.account.Client;

/**
 * @author vinayak
 *
 */
public class ClientDaoTest extends BaseTestCase {

	@Autowired
	private ClientDao clientDao;

	@Test
	public void testGetClient() {
		Client client = clientDao.get(-1);

		assertNotNull(client);
		assertEquals("Test Client 1", client.getName());
		assertEquals("test confirmed_order_email 1", client.getConfirmedOrderEmail());
		assertEquals("test pending_order_email 1", client.getPendingOrderEmail());
		assertEquals("test share_product_email 1", client.getShareProductEmail());
		assertEquals("test signature 1", client.getSignature());
		assertEquals("1111111111", client.getPendingOrderHelpline());
		assertEquals("12121212", client.getConfirmedOrderHelpline());
		assertEquals("test sms_mask 1", client.getSmsMask());
		assertEquals("test noreply_email 1", client.getNoReplyEmail());
		assertEquals("test feedback_email 1", client.getFeedbackEmail());
		assertEquals("test promotions_email 1", client.getPromotionsEmail());
		assertEquals("12345678", client.getSalePriceList());
		assertEquals("123456789", client.getListPriceList());
		assertEquals("test clientdomain_name 1", client.getClientDomainName());
		assertEquals("test slug 1", client.getSlug());
	}
}
