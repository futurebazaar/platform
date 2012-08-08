/**
 * 
 */
package com.fb.platform.promotion.product.model.promotion;

import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.product.model.PromotionConfig;
import com.fb.platform.promotion.rule.product.Rule;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class AutoPromotion extends Promotion {

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

	public boolean isApplicableOn(OrderRequest orderRequest, int userId) {
		for (OrderItem orderItem : orderRequest.getOrderItems()) {
			int productId = orderItem.getProduct().getProductId();
			if (isApplicableOn(productId, userId)) {
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

	public boolean apply(OrderRequest orderRequest, OrderDiscount orderResponse) {
		boolean applied = promotionConfig.apply(orderRequest);
		if (applied) {
			orderResponse.promotionApplied(super.id);
		}
		return applied;
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
