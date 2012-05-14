/**
 * 
 */
package com.fb.platform.promotion.admin.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;

/**
 * @author vinayak
 *
 */
public class CouponAdminDaoTest extends BaseTestCase {

	@Autowired
	private CouponAdminDao couponAdminDao;

	@Autowired
	private CouponDao couponDao;

	@Test
	public void loadCouponWithoutConfig() {
		Coupon coupon = couponAdminDao.loadCouponWithoutConfig("global_coupon_1");

		assertNotNull(coupon);
		assertEquals("global_coupon_1", coupon.getCode());
		assertEquals(CouponType.GLOBAL, coupon.getType());
	}

	@Test
	public void loadPreIssueCoupon() {
		Coupon coupon = couponAdminDao.loadCouponWithoutConfig("pre_issued_1");

		assertNotNull(coupon);
		assertEquals("pre_issued_1", coupon.getCode());
		assertEquals(CouponType.PRE_ISSUE, coupon.getType());
	}

	@Test
	public void loadInvalidCouponCode() {
		Coupon coupon = couponAdminDao.loadCouponWithoutConfig("i_dont_exists_sorry");

		assertNull(coupon);
	}

	@Test
	public void assignToUser() {
		couponAdminDao.assignToUser(-4, "pre_issued_1", 0);

		//try to load the newly assigned coupon for this user, should load fine
		Coupon coupon = couponDao.load("pre_issued_1", -4);

		assertNotNull(coupon);
	}

	@Test(expected = CouponAlreadyAssignedToUserException.class)
	public void reassign() {
		couponAdminDao.assignToUser(-2, "pre_issued_1", 0);
	}

	@Test(expected = CouponAlreadyAssignedToUserException.class)
	public void assignToInvalidUser() {
		couponAdminDao.assignToUser(-2800, "pre_issued_1", 0);
	}

	@Test(expected = DataAccessException.class)
	public void assignInvalidCoupon() {
		couponAdminDao.assignToUser(-2, "i_am_not_there_in_db", 0);
	}

}
