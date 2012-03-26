/**
 * 
 */
package com.fb.platform.promotion.dao;

import com.fb.platform.promotion.model.GlobalPromotioUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.rule.RuleConfiguration;

/**
 * @author vinayak
 *
 */
public interface PromotionDao {

	public Promotion load(int promotionId);

	public GlobalPromotioUses loadGlobalUses(int promotionId);

	public UserPromotionUses loadUserUses(int promotionId, int userId);

	public RuleConfiguration loadRuleConfiguration(int promotionId);
}
