package com.fb.platform.promotion.test.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.interfaces.PromotionManager;

public class PromotionManagerTest extends BaseTestCase {
	
	@Autowired
	private PromotionManager promotionManager;
	
	@Test
	public void testGetPromotion(){
		assertTrue(true);
	}
	
	@Test
	public void testGetPromotionByCouponCode() {
		assertTrue(true);
	}


}
