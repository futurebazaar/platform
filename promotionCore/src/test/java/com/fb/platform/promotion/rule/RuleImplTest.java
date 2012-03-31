package com.fb.platform.promotion.rule;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.rule.impl.BuyXGetYFreeRuleImpl;

public class RuleImplTest extends BaseTestCase {
	
	@Autowired
	private RuleDao ruleDao = null;

	@Test
	public void load() {
		PromotionRule rule = ruleDao.load(-1, -1);

		assertNotNull(rule);
		assertTrue(rule instanceof BuyXGetYFreeRuleImpl);
	}

	@Test
	public void loadRuleConfig() {
		RuleConfiguration ruleConfiguration = ruleDao.loadRuleConfiguration(-1, -1);

		assertNotNull(ruleConfiguration);
		assertEquals(2, ruleConfiguration.getConfigItems().size());
	}

}
