/**
 * 
 */
package com.fb.platform.promotion.rule;

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
	 * 
	 * @return
	 */
	public boolean isApplicable();
	public void execute();
}
