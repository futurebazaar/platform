package com.fb.platform.promotion.product.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.exception.PromotionNotFoundException;
import com.fb.platform.promotion.product.model.Condition;
import com.fb.platform.promotion.product.model.Conditions;
import com.fb.platform.promotion.product.model.ConfigModule;
import com.fb.platform.promotion.product.model.ModuleJoin;
import com.fb.platform.promotion.product.model.PromotionConfig;
import com.fb.platform.promotion.product.model.Result;
import com.fb.platform.promotion.product.model.Results;
import com.fb.platform.promotion.product.model.condition.ProductCondition;
import com.fb.platform.promotion.product.model.result.ProductResult;

/**
 * @author nehaga
 *
 */
public class PromotionConfigDaoTest extends BaseTestCase {
	
	@Autowired
	PromotionConfigDao promotionConfigDao = null;

	@Test
	public void loadPromotion() {
		PromotionConfig promotionConfig = promotionConfigDao.load(100);
		
		assertNotNull(promotionConfig);
		assertEquals(1, promotionConfig.getModules().size());
		
		ConfigModule configModule = promotionConfig.getModules().get(0);
		
		assertNotNull(configModule);
		assertNotNull(configModule.getConditions());
		assertNotNull(configModule.getResults());
		
		Conditions conditions = configModule.getConditions();
		
		assertNotNull(conditions.getConditions());
		assertNotNull(conditions.getJoins());
		
		assertEquals(2, conditions.getConditions().size());
		assertEquals(1, conditions.getJoins().size());
		
		assertEquals(ModuleJoin.AND, conditions.getJoins().get(0));
		
		Condition condition1 = conditions.getConditions().get(0);
		Condition condition2 = conditions.getConditions().get(1);
		
		assertTrue(ProductCondition.class.isInstance(condition1));
		assertTrue(ProductCondition.class.isInstance(condition2));
		
		Results results = configModule.getResults();
		
		assertNotNull(results.getResults());
		assertNotNull(results.getJoins());
		
		assertEquals(2, results.getResults().size());
		assertEquals(1, results.getJoins().size());
		
		assertEquals(ModuleJoin.AND, results.getJoins().get(0));

		Result result1 = results.getResults().get(0);
		Result result2 = results.getResults().get(1);
		
		assertTrue(ProductResult.class.isInstance(result1));
		assertTrue(ProductResult.class.isInstance(result2));
		
	}
	
	@Test(expected=PromotionNotFoundException.class)
	public void promotionNotFound() {
		promotionConfigDao.load(-100);
	}
	
	
	public void setPromotionConfigDao(PromotionConfigDao promotionConfigDao) {
		this.promotionConfigDao = promotionConfigDao;
	}
}
