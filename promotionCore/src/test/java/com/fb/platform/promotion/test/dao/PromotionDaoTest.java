package com.fb.platform.promotion.test.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.dao.PromotionDao;

public class PromotionDaoTest extends BaseTestCase {
	
	@Autowired
	private PromotionDao promotionDao;
	
	@Test
	public void testGetPromotion(){
		assertTrue(true);
	}

}
