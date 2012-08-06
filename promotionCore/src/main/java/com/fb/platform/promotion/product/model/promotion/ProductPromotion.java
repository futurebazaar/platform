/**
 * 
 */
package com.fb.platform.promotion.product.model.promotion;

import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.product.model.PromotionConfig;
import com.fb.platform.promotion.rule.product.Rule;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class ProductPromotion extends Promotion {

	private PromotionConfig promotionConfig;
	private Rule rule;
	private int priority;

	public boolean isApplicableOn(int productId, int userId) {
		if (promotionConfig.isApplicableOn(productId)) {
			return true;
		}
		if (rule != null) {
			if (rule.isApplicableOn(productId, userId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if this promotion is applicable on the given productId. 
	 * If it is applicable, then returns the possible results.
	 * 
	 * @param productId
	 * @return The result if promotion is applicable on the productId. null if it is not applicable.
	 */
	public PromotionConfig getApplicableConfig(int productId) {
		if (promotionConfig.isApplicableOn(productId)) {
			return promotionConfig;
		}
		return null;
	}

	public OrderDiscount apply(OrderRequest orderRequest) {
		return null;
	}

	public PromotionConfig getPromotionConfig() {
		return promotionConfig;
	}
	public void setPromotionConfig(PromotionConfig promotionConfig) {
		this.promotionConfig = promotionConfig;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
