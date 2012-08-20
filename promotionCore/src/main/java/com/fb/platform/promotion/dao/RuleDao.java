/**
 * 
 */
package com.fb.platform.promotion.dao;

import java.util.List;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfiguration;

/**
 * @author vinayak
 * 
 */
public interface RuleDao {

	public PromotionRule load(int promotionId, int ruleId);

	public RuleConfiguration loadRuleConfiguration(int promotionId, int ruleId);

	/**
	 * This function returns a list of Promotion rules.
	 * 
	 * @param clearCouponCacheRequest
	 * @return
	 */
	public List<RulesEnum> getAllPromotionRules();

	/**
	 * This function will return the rule id of a rule.
	 * 
	 * @param rulesEnum
	 * @return
	 */
	public int getRuleId(String ruleName);

	/**
	 * This function return the Payment Options String associated with the
	 * Promotion in the Rules Config Data
	 * 
	 * @param promotionId
	 * @param ruleId
	 * @return
	 */
	public String getPaymentOptionsString(int promotionId, RuleConfigDescriptorEnum configDesc);

}
