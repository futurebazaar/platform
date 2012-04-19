/**
 * 
 */
package com.fb.platform.promotion.dao.admin;

import java.util.List;

import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.rule.RuleConfiguration;

/**
 * @author vinayak
 *
 */
public interface PromotionAdminDao {

	public void createPromotion(Promotion promotion, RuleConfiguration ruleConfig, List<Coupon> couponsList);
}
