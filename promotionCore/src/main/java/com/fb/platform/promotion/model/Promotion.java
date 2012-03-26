/**
 * 
 */
package com.fb.platform.promotion.model;

import java.io.Serializable;

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

	private int ruleId;

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

}
