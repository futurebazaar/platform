/**
 * 
 */
package com.fb.platform.promotion.dao;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;

/**
 * @author vinayak
 *
 */
public interface RuleDao {

	public PromotionRule load(int promotionId, int ruleId);

	public RuleConfiguration loadRuleConfiguration(int promotionId, int ruleId);

}
