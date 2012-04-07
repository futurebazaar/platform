/**
 * 
 */
package com.fb.platform.promotion.dao;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.RulesEnum;

/**
 * @author vinayak
 *
 */
public interface PromotionAdminDao {

	public int create(String name, String description, DateTime validFrom, DateTime validTill, int maxUses, Money maxAmount, int maxUsesPerUser, Money maxAmountPerUser, RulesEnum rule);

	public int createRuleConfiguration(int promotionId, int ruleId, RuleConfiguration ruleConfiguration);
}
