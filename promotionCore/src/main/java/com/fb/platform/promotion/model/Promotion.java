/**
 * 
 */

package com.fb.platform.promotion.model;

import java.io.Serializable;

import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;

/**
 * @author vinayak
 *
 */
public abstract class Promotion implements Serializable {

	protected int id;
	protected String name;
	protected String description;
	protected boolean isActive;
	protected PromotionDates dates;
	protected PromotionLimitsConfig limitsConfig;

	public abstract OrderDiscount apply(OrderRequest request);

	public PromotionStatusEnum isWithinLimits(GlobalPromotionUses globalUses, UserPromotionUses userUses) {
		return limitsConfig.isWithinLimit(globalUses, userUses);
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDates(PromotionDates dates) {
		this.dates = dates;
	}
	public void setLimitsConfig(PromotionLimitsConfig limitsConfig) {
		this.limitsConfig = limitsConfig;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public boolean isActive() {
		return isActive;
	}
	public PromotionDates getDates() {
		return dates;
	}
	public PromotionLimitsConfig getLimitsConfig() {
		return limitsConfig;
	}
	public boolean isWithinDates() {
		return dates.isWithinDates();
	}
}
