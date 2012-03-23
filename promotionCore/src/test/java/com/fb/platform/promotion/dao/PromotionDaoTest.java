/**
 * 
 */
package com.fb.platform.promotion.dao;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.model.Promotion;

/**
 * @author vinayak
 *
 */
public class PromotionDaoTest extends BaseTestCase {

	@Autowired
	private PromotionDao promotionDao;

	@Test
	public void testGetPromotion() {
		Promotion promotion = promotionDao.load(-1);

		assertNotNull(promotion);
		assertEquals(new DateTime(2012, 1, 1, 0, 0), promotion.getDates().getValidFrom());
	}
}
