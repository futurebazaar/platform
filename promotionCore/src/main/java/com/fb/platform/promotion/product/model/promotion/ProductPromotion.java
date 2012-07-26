/**
 * 
 */
package com.fb.platform.promotion.product.model.promotion;

import com.fb.platform.promotion.model.PromotionDates;
import com.fb.platform.promotion.model.PromotionLimitsConfig;
import com.fb.platform.promotion.product.model.PromotionConfig;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class ProductPromotion {

	private int id;
	private String name;
	private String description;
	private boolean isActive;
	private PromotionDates dates;
	private PromotionLimitsConfig limitsConfig;
	private PromotionConfig promotionConfig;

	/**
	 * Checks if this promotion is applicable on the given productId. 
	 * If it is applicable, then returns the possible results.
	 * 
	 * @param productId
	 * @return The result if promotion is applicable on the productId. null if it is not applicable.
	 */
	public PromotionConfig getApplicableResults(int productId) {
		
		return null;
	}

	public Object apply(OrderRequest orderRequest) {
		return null;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public PromotionDates getDates() {
		return dates;
	}
	public void setDates(PromotionDates dates) {
		this.dates = dates;
	}
	public PromotionLimitsConfig getLimitsConfig() {
		return limitsConfig;
	}
	public void setLimitsConfig(PromotionLimitsConfig limitsConfig) {
		this.limitsConfig = limitsConfig;
	}
	public PromotionConfig getPromotionConfig() {
		return promotionConfig;
	}
	public void setPromotionConfig(PromotionConfig promotionConfig) {
		this.promotionConfig = promotionConfig;
	}

}
