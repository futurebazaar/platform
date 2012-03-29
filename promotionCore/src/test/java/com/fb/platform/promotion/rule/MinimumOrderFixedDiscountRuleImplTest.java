/**
 * 
 */
package com.fb.platform.promotion.rule;

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
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffRuleImpl;

/**
 * @author vinayak
 *
 */
public class MinimumOrderFixedDiscountRuleImplTest extends BaseTestCase {

	@Test
	public void isCouponApplicable() {
		assertTrue(true);
	}
}
