/**
 * 
 */
package com.fb.platform.promotion.admin.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;
import com.fb.platform.promotion.service.CouponNotFoundException;
import com.fb.platform.promotion.service.InvalidCouponTypeException;
import com.fb.platform.promotion.service.PromotionService;

/**
 * @author vinayak
 *
 */
public class PromotionAdminServiceTest extends BaseTestCase {

	@Autowired
	private PromotionAdminService promotionAdminService;

	@Autowired
	private PromotionService promotionService;

	@Test
	public void assignCouponToUser() {
		promotionAdminService.assignCouponToUser("pre_issued_1", -4, 20);

		//try to load the assigned coupon. if it was assigned properly it will load fine.
		Coupon coupon = promotionService.getCoupon("pre_issued_1", -4);

		assertNotNull(coupon);
		assertEquals("pre_issued_1", coupon.getCode());
		assertEquals(CouponType.PRE_ISSUE, coupon.getType());

		assertNotNull(coupon.getLimitsConfig());
		assertEquals(coupon.getLimitsConfig().getMaxUsesPerUser(), 20);
	}

	@Test(expected = CouponAlreadyAssignedToUserException.class)
	public void reassign() {
		promotionAdminService.assignCouponToUser("pre_issued_1", -2, 0);
	}

	@Test(expected = InvalidCouponTypeException.class)
	public void assignNonPreIssue() {
		promotionAdminService.assignCouponToUser("global_coupon_1", -2, 0);
	}

	@Test(expected = CouponNotFoundException.class)
	public void assignInvalidCouponCode() {
		promotionAdminService.assignCouponToUser("i_am_not_there_in_db", -2, 0);
	}
	
}
