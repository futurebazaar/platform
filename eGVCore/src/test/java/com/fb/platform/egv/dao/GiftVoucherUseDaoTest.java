/**
 * 
 */
package com.fb.platform.egv.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.egv.model.GiftVoucherUse;

/**
 * @author keith
 *
 */
public class GiftVoucherUseDaoTest extends BaseTestCase {

	@Autowired
	private GiftVoucherDao giftVoucherDao;

	@Test
	public void getUsage() {
		GiftVoucherUse gvUse = giftVoucherDao.loadUse(Long.parseLong("-12345678901"));

		assertNotNull(gvUse);
//		assertEquals(new DateTime(2012, 5, 28, 0, 0), gvUse.getUsedOn());
		assertEquals("-12345678901", gvUse.getGiftVoucherNumber());
		assertEquals(-5, gvUse.getOrderId());
		assertEquals(-1,gvUse.getUsedBy());
		assertEquals(0,new Money(new BigDecimal(126.0)).compareTo(gvUse.getAmountRedeemed()));
	}

}
