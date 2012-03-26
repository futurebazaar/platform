/**
 * 
 */
package com.fb.platform.promotion.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUses;

/**
 * @author vinayak
 *
 */
public class CouponDaoTest extends BaseTestCase {

	@Autowired
	private CouponDao couponDao;

	@Test
	public void loadGlobal() {
		Coupon coupon = couponDao.load("global_coupon_1");

		assertNotNull(coupon);

		assertEquals(-1, coupon.getPromotionId());
		assertEquals("global_coupon_1", coupon.getCode());
		assertEquals(CouponType.GLOBAL, coupon.getType());
		assertEquals(-1, coupon.getId());

		CouponLimitsConfig limitsConfig = coupon.getLimitsConfig();
		assertNotNull(limitsConfig);
		assertEquals(10, limitsConfig.getMaxUses());
		assertEquals(2, limitsConfig.getMaxUsesPerUser());
		assertTrue(limitsConfig.getMaxAmount().eq(new Money(new BigDecimal(10000))));
		assertTrue(limitsConfig.getMaxAmountPerUser().eq(new Money(new BigDecimal(1000))));
	}

	@Test
	public void loadPreIssue() {
		Coupon coupon = couponDao.load("pre_issued_1");

		assertNotNull(coupon);

		assertEquals(-4, coupon.getPromotionId());
		assertEquals("pre_issued_1", coupon.getCode());
		assertEquals(CouponType.PRE_ISSUE, coupon.getType());
		assertEquals(-2, coupon.getId());

		CouponLimitsConfig limitsConfig = coupon.getLimitsConfig();
		assertNotNull(limitsConfig);
		assertEquals(0, limitsConfig.getMaxUses());
		assertEquals(1, limitsConfig.getMaxUsesPerUser());
		assertNull(limitsConfig.getMaxAmount());
		assertTrue(limitsConfig.getMaxAmountPerUser().eq(new Money(new BigDecimal(2000))));
	}

	@Test
	public void loadPostIssueFirst() {
		Coupon coupon = couponDao.load("post_issued_1");

		assertNotNull(coupon);

		assertEquals(-5, coupon.getPromotionId());
		assertEquals("post_issued_1", coupon.getCode());
		assertEquals(CouponType.POST_ISSUE, coupon.getType());
		assertEquals(-3, coupon.getId());

		CouponLimitsConfig limitsConfig = coupon.getLimitsConfig();
		assertNotNull(limitsConfig);
		assertEquals(10, limitsConfig.getMaxUses());
		assertEquals(0, limitsConfig.getMaxUsesPerUser());
		assertTrue(limitsConfig.getMaxAmount().eq(new Money(new BigDecimal(10000))));
		assertNull(limitsConfig.getMaxAmountPerUser());
	}

	@Test
	public void loadPostIssueSecond() {
		Coupon coupon = couponDao.load("post_issued_2");

		assertNotNull(coupon);

		assertEquals(-5, coupon.getPromotionId());
		assertEquals("post_issued_2", coupon.getCode());
		assertEquals(CouponType.POST_ISSUE, coupon.getType());
		assertEquals(-4, coupon.getId());

		CouponLimitsConfig limitsConfig = coupon.getLimitsConfig();
		assertNotNull(limitsConfig);
		assertEquals(10, limitsConfig.getMaxUses());
		assertEquals(2, limitsConfig.getMaxUsesPerUser());
		assertTrue(limitsConfig.getMaxAmount().eq(new Money(new BigDecimal(10000))));
		assertTrue(limitsConfig.getMaxAmountPerUser().eq(new Money(new BigDecimal(1000))));
	}

	@Test
	public void loadNoxExistent() {
		Coupon coupon = couponDao.load("i_dont_exists");

		assertNull(coupon);
	}

	@Test
	public void loadGlobalUses() {
		GlobalCouponUses globalUses = couponDao.loadGlobalUses(-1);

		assertNotNull(globalUses);
		assertEquals(10, globalUses.getCurrentCount());
		assertTrue(globalUses.getCurrentAmount().eq(new Money(new BigDecimal(10000))));
	}

	@Test
	public void loadNonExistantGlobalUses() {
		GlobalCouponUses globalUses = couponDao.loadGlobalUses(-2);

		assertNull(globalUses);
	}

	@Test
	public void loadUserUses() {
		UserCouponUses userUses = couponDao.loadUserUses(-1, 1);

		assertNotNull(userUses);
		assertEquals(50, userUses.getCurrentCount());
		assertTrue(userUses.getCurrentAmount().eq(new Money(new BigDecimal(2000))));
	}

	@Test
	public void loadEmpyUserUses() {
		UserCouponUses userUses = couponDao.loadUserUses(-2, 2);

		assertNotNull(userUses);
		assertEquals(0, userUses.getCurrentCount());
		assertTrue(userUses.getCurrentAmount().eq(new Money(BigDecimal.ZERO)));
	}
}
