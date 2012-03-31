/**
 * 
 */

package com.fb.platform.promotion.model;

import java.io.Serializable;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.to.CouponRequest;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class Promotion implements Serializable {

	private int id;
	private String name;
	private String description;
	private boolean isActive;
	private PromotionDates dates;
	private PromotionLimitsConfig limitsConfig;

	private PromotionRule rule;

	public boolean isApplicable(OrderRequest request) {
		if (!isActive) {
			return false;
		}
		boolean withinDates = dates.isWithinDates();
		if (!withinDates) {
			return false;
		}
		boolean ruleApplicable = rule.isApplicable(request);
		if (!ruleApplicable) {
			return false;
		}
		return true;
	}

	public boolean isWithinLimits(GlobalPromotionUses globalUses, UserPromotionUses userUses) {
		return limitsConfig.isWithinLimit(globalUses, userUses);
	}

	public Money apply(OrderRequest request) {
		return rule.execute(request);
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
	public void setRule(PromotionRule rule) {
		this.rule = rule;
	}
}
