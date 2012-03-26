/**
 * 
 */
package com.fb.platform.promotion.dao;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.GlobalPromotioUses;
import com.fb.platform.promotion.model.Promotion;

/**
 * @author vinayak
 *
 */
public class PromotionDaoTest extends BaseTestCase {

	@Autowired
	private PromotionDao promotionDao;

	@Test
	public void testGet() {
		Promotion promotion = promotionDao.load(-1);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertEquals(new DateTime(2012, 1, 1, 0, 0), promotion.getDates().getValidFrom());
		assertEquals(new DateTime(2012, 5, 31, 0, 0), promotion.getDates().getValidTill());
		assertEquals("Test Promotion 1", promotion.getName());
		assertEquals("try1_desc", promotion.getDescription());
		assertTrue(promotion.isActive());
	}

	@Test
	public void testGetValidInFuture() {
		Promotion promotion = promotionDao.load(-2);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertEquals(new DateTime(2013, 2, 2, 0, 0), promotion.getDates().getValidFrom());
		assertEquals(new DateTime(2013, 6, 11, 0, 0), promotion.getDates().getValidTill());
		assertEquals("Not yet Valid", promotion.getName());
		assertEquals("This promotion is valid in future", promotion.getDescription());
		assertTrue(promotion.isActive());
	}

	@Test
	public void testGetExpired() {
		Promotion promotion = promotionDao.load(-3);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertEquals(new DateTime(2012, 1, 1, 0, 0), promotion.getDates().getValidFrom());
		assertEquals(new DateTime(2012, 1, 31, 0, 0), promotion.getDates().getValidTill());
		assertEquals("Expired Promotion", promotion.getName());
		assertEquals("This promotion is expired", promotion.getDescription());
		assertTrue(promotion.isActive());
	}

	@Test
	public void testNullValidFrom() {
		Promotion promotion = promotionDao.load(-4);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertNull(promotion.getDates().getValidFrom());
		assertEquals(new DateTime(2012, 6, 30, 0, 0), promotion.getDates().getValidTill());
		assertEquals("No Valid From Promotion", promotion.getName());
		assertEquals("This promotion has not valid from date", promotion.getDescription());
		assertTrue(promotion.isActive());
	}

	@Test
	public void testNullValidTill() {
		Promotion promotion = promotionDao.load(-5);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertEquals(new DateTime(2012, 3, 2, 0, 0), promotion.getDates().getValidFrom());
		assertNull(promotion.getDates().getValidTill());
		assertEquals("No Valid Till Promotion", promotion.getName());
		assertEquals("This promotion has not valid till date", promotion.getDescription());
		assertTrue(promotion.isActive());
	}

	@Test
	public void testGetInactive() {
		Promotion promotion = promotionDao.load(-6);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertFalse(promotion.isActive());
	}

	@Test
	public void loadGlobalUses() {
		GlobalPromotioUses globalUses = promotionDao.loadGlobalUses(-1);

		assertNotNull(globalUses);
		assertEquals(10, globalUses.getCurrentCount());
		assertEquals(true, globalUses.getCurrentAmount().eq(new Money(new BigDecimal(10000))));
	}

	@Test
	public void loadNonExistantGlobalUses() {
		GlobalPromotioUses globalUses = promotionDao.loadGlobalUses(-2);

		assertNull(globalUses);
	}
}
