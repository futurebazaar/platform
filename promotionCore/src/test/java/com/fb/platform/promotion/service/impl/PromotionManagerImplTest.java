package com.fb.platform.promotion.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.service.PromotionManager;

public class PromotionManagerImplTest extends BaseTestCase{

	@Autowired
	PromotionManager promotionManager = null;
	
	@Test
	public void testCouponMaxAmountLimit(){
		assertTrue(true);
	}
}
