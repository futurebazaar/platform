/**
 * 
 */
package com.fb.platform.promotion.dao;

import java.util.List;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.RulesEnum;

/**
 * @author vinayak
 *
 */
public interface RuleDao {

	public PromotionRule load(int promotionId, int ruleId);

	public RuleConfiguration loadRuleConfiguration(int promotionId, int ruleId);
	
	/**
	 * This function returns a list of Promotion rules.
	 * @param clearCouponCacheRequest
	 * @return
	 */
	public List<RulesEnum> getAllPromotionRules();

}
