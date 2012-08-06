/**
 * 
 */
package com.fb.platform.egv.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;

/**
 * @author keith
 *
 */
public class GiftVoucherDaoTest extends BaseTestCase {

	@Autowired
	private GiftVoucherDao giftVoucherDao;

	@Test
	public void get() {
		GiftVoucher eGV = giftVoucherDao.load(-1000);

		assertNotNull(eGV);
		assertNotNull(eGV.getDates());
		assertEquals(new DateTime(2012, 5, 28, 0, 0), eGV.getDates().getValidFrom());
		assertEquals(new DateTime(2015, 7, 31, 0, 0), eGV.getDates().getValidTill());
		assertEquals("-12345678901", eGV.getNumber());
//		assertEquals(true, eGV.isValidPin("12345");
		assertEquals(GiftVoucherStatusEnum.CONFIRMED,eGV.getStatus());
	}

}
