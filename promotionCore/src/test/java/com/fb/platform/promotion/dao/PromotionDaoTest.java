/**
 * 
 */
package com.fb.platform.promotion.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.PromotionLimitsConfig;
import com.fb.platform.promotion.model.UserPromotionUses;

/**
 * @author vinayak
 *
 */
public class PromotionDaoTest extends BaseTestCase {

	@Autowired
	private PromotionDao promotionDao;

	@Test
	public void get() {
		Promotion promotion = promotionDao.load(-1);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertEquals(new DateTime(2012, 1, 1, 0, 0), promotion.getDates().getValidFrom());
		assertEquals(new DateTime(2012, 5, 31, 0, 0), promotion.getDates().getValidTill());
		assertEquals("Test Promotion 1", promotion.getName());
		assertEquals("try1_desc", promotion.getDescription());
		assertTrue(promotion.isActive());

		PromotionLimitsConfig limitsConfig = promotion.getLimitsConfig();

		assertNotNull(limitsConfig);
		assertEquals(300, limitsConfig.getMaxUses());
		assertEquals(20, limitsConfig.getMaxUsesPerUser());
		assertTrue(limitsConfig.getMaxAmount().eq(new Money(new BigDecimal(10000))));
		assertTrue(limitsConfig.getMaxAmountPerUser().eq(new Money(new BigDecimal(1000))));
	}

	@Test
	public void getValidInFuture() {
		Promotion promotion = promotionDao.load(-2);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertEquals(new DateTime(2013, 2, 2, 0, 0), promotion.getDates().getValidFrom());
		assertEquals(new DateTime(2013, 6, 11, 0, 0), promotion.getDates().getValidTill());
		assertEquals("Not yet Valid", promotion.getName());
		assertEquals("This promotion is valid in future", promotion.getDescription());
		assertTrue(promotion.isActive());

		PromotionLimitsConfig limitsConfig = promotion.getLimitsConfig();

		assertNotNull(limitsConfig);
		assertEquals(2, limitsConfig.getMaxUses());
		assertEquals(1, limitsConfig.getMaxUsesPerUser());
		assertTrue(limitsConfig.getMaxAmount().eq(new Money(new BigDecimal(-1))));
		assertTrue(limitsConfig.getMaxAmountPerUser().eq(new Money(new BigDecimal(2000))));
	}

	@Test
	public void getExpired() {
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
	public void nullValidFrom() {
		Promotion promotion = promotionDao.load(-4);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertNull(promotion.getDates().getValidFrom());
		assertEquals(new DateTime(2012, 6, 30, 0, 0), promotion.getDates().getValidTill());
		assertEquals("No Valid From Promotion", promotion.getName());
		assertEquals("This promotion has no valid from date", promotion.getDescription());
		assertTrue(promotion.isActive());
	}

	@Test
	public void nullValidTill() {
		Promotion promotion = promotionDao.load(-5);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertEquals(new DateTime(2012, 3, 2, 0, 0), promotion.getDates().getValidFrom());
		assertNull(promotion.getDates().getValidTill());
		assertEquals("No Valid Till Promotion", promotion.getName());
		assertEquals("This promotion has no valid till date", promotion.getDescription());
		assertTrue(promotion.isActive());
	}

	@Test
	public void getInactive() {
		Promotion promotion = promotionDao.load(-6);

		assertNotNull(promotion);
		assertNotNull(promotion.getDates());
		assertFalse(promotion.isActive());
	}

	@Test
	public void getNonExistent() {
		Promotion promotion = promotionDao.load(100000);
		assertNull(promotion);
	}

	@Test
	public void loadGlobalUses() {
		GlobalPromotionUses globalUses = promotionDao.loadGlobalUses(-1);

		assertNotNull(globalUses);
		assertEquals(1, globalUses.getCurrentCount());
		assertTrue(globalUses.getCurrentAmount().eq(new Money(new BigDecimal(2000))));
	}

	@Test
	public void loadNonExistantGlobalUses() {
		GlobalPromotionUses globalUses = promotionDao.loadGlobalUses(-12);

		assertNotNull(globalUses);
		assertEquals(0, globalUses.getCurrentCount());
		assertEquals(globalUses.getCurrentAmount().getAmount().compareTo(BigDecimal.ZERO), 0);
	}

	@Test
	public void loadUserUses() {
		UserPromotionUses userUses = promotionDao.loadUserUses(-1, 1);

		assertNotNull(userUses);
		assertEquals(1, userUses.getCurrentCount());
		assertTrue(userUses.getCurrentAmount().eq(new Money(new BigDecimal(2000))));
	}

	@Test
	public void loadEmpyUserUses() {
		UserPromotionUses userUses = promotionDao.loadUserUses(-2, 2);

		assertNotNull(userUses);
		assertEquals(1, userUses.getCurrentCount());
		assertTrue(userUses.getCurrentAmount().eq(new Money(BigDecimal.ZERO)));
	}
	
	@Test
	public void updateUserUsesCreateNew() {
		boolean isCreatededSuccessfully = promotionDao.updateUserUses(-3, 3, new BigDecimal(222),42);

		UserPromotionUses userPromotionUses = promotionDao.loadUserUses(-3, 3);
		
		assertTrue(isCreatededSuccessfully);
		assertNotNull(userPromotionUses);
		assertEquals(2, userPromotionUses.getCurrentCount());
		assertTrue(new Money(new BigDecimal(422)).eq(userPromotionUses.getCurrentAmount()));
	}
	
	@Test(expected=PlatformException.class)
	public void updateUserUsesFailed() {
		boolean isUpdatedSuccessfully = promotionDao.updateUserUses(-3, 3, new BigDecimal(100), 40);

		UserPromotionUses userPromotionUses = promotionDao.loadUserUses(-3, 3);
		
		assertFalse(isUpdatedSuccessfully);
		assertNotNull(userPromotionUses);
		assertEquals(3, userPromotionUses.getUserId());
		assertEquals(-3, userPromotionUses.getPromotionId());
	}
	
	/*@Test
	public void cancelUserUses(){
		boolean isCancelled = promotionDao.cancelUserUses(-4, -4, -5);
		
		assertTrue(isCancelled);
	}*/
}
