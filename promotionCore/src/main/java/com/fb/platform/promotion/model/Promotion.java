/**
 * 
 */

package com.fb.platform.promotion.model;

import java.io.Serializable;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.to.PromotionRequest;
import com.fb.platform.promotion.util.PromotionRuleMapper;

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

	public boolean isApplicable(PromotionRequest request) {
		boolean withinDates = dates.isWithinDates();
		if (!withinDates) {
			return false;
		}
		if (!isActive) {
			return false;
		}
		boolean ruleApplicable = rule.isApplicable(PromotionRuleMapper.promotionToRuleRequest(request));
		if (!ruleApplicable) {
			return false;
		}
		return true;
	}

	public boolean isWithinLimits(GlobalPromotionUses globalUses, UserPromotionUses userUses) {
		return limitsConfig.isWithinLimit(globalUses, userUses);
	}

	public Object apply(PromotionRequest request) {
		return rule.execute(PromotionRuleMapper.promotionToRuleRequest(request));
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
