/**
 * 
 */
package com.fb.platform.egv.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.egv.exception.GiftVoucherException;
import com.fb.platform.egv.model.GiftVoucherUse;

/**
 * @author keith
 *
 */
public class GiftVoucherUseDaoTest extends BaseTestCase {

	@Autowired
	private GiftVoucherDao giftVoucherDao;

	@Test
	public void getUsageValid() {
		String gvNumber = "-12345678921";
		GiftVoucherUse gvUse = giftVoucherDao.loadUse(Long.parseLong(gvNumber));

		assertNotNull(gvUse);
		assertEquals(gvNumber, gvUse.getGiftVoucherNumber());
		assertEquals(-5, gvUse.getOrderId());
		assertEquals(-1,gvUse.getUsedBy());
		assertEquals(0,new Money(new BigDecimal(126.0)).compareTo(gvUse.getAmountRedeemed()));
	}
	
	@Test (expected = GiftVoucherException.class)
	public void getUsageInvalidEgvNumber() {
		String gvNumber = "-12345678957";
		giftVoucherDao.loadUse(Long.parseLong(gvNumber));
	}
	
	@Test (expected = GiftVoucherException.class)
	public void deleteUsageValid() {
		String gvNumber = "-12345678922";
		giftVoucherDao.deleteUse(Long.parseLong(gvNumber),-1,-6);

		//Check if Usage is deleted : should throw exception on load
		giftVoucherDao.loadUse(Long.parseLong(gvNumber));
	}

	@Test (expected = PlatformException.class)
	public void deleteUsageInvalidEgvNumber() {
		String gvNumber = "-12345678940";
		giftVoucherDao.deleteUse(Long.parseLong(gvNumber),-1,-6);
	}

}
