/**
 * 
 */
package com.fb.platform.promotion.rule;

import com.fb.platform.promotion.to.PromotionRequest;

/**
 * @author vinayak
 *
 */
public interface PromotionRule {

	/**
	 * Called at the time of rule objects creation.
	 * @param ruleConfig The configuration items from DB.
	 */
	public void init(RuleConfiguration ruleConfig);

	/**
	 * @return true if the rule is applicable on the request
	 */
	public boolean isApplicable(RuleRequest request);

	/**
	 * Applies the rule on the request.
	 * @param request
	 */
	public Object execute(RuleRequest request);
}
