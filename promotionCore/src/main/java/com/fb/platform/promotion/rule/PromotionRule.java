/**
 * 
 */
package com.fb.platform.promotion.rule;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.impl.ApplicableResponse;
import com.fb.platform.promotion.to.OrderRequest;


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
	public ApplicableResponse isApplicable(OrderRequest request);

	/**
	 * Applies the rule on the request.
	 * @param request
	 */
	public Money execute(OrderRequest request);
}
