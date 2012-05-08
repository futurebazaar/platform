package com.fb.platform.promotion.to;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.RulesEnum;

public class PromotionTO {
	
	private String name;
	private String description;
	private boolean isActive;
	private DateTime validFrom;
	private DateTime validTill;
	private int maxUses = 0;
	private Money maxAmount = null;
	private int maxUsesPerUser = 0;
	private Money maxAmountPerUser = null;
	private RulesEnum rulesEnum = null;
	private List<RuleConfigItemTO> configItems = new ArrayList<RuleConfigItemTO>();
	
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
	public DateTime getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(DateTime validFrom) {
		this.validFrom = validFrom;
	}
	public DateTime getValidTill() {
		return validTill;
	}
	public void setValidTill(DateTime validTill) {
		this.validTill = validTill;
	}
	public int getMaxUses() {
		return maxUses;
	}
	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}
	public Money getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Money maxAmount) {
		this.maxAmount = maxAmount;
	}
	public int getMaxUsesPerUser() {
		return maxUsesPerUser;
	}
	public void setMaxUsesPerUser(int maxUsesPerUser) {
		this.maxUsesPerUser = maxUsesPerUser;
	}
	public Money getMaxAmountPerUser() {
		return maxAmountPerUser;
	}
	public void setMaxAmountPerUser(Money maxAmountPerUser) {
		this.maxAmountPerUser = maxAmountPerUser;
	}
	public RulesEnum getRulesEnum() {
		return rulesEnum;
	}
	public void setRulesEnum(RulesEnum rulesEnum) {
		this.rulesEnum = rulesEnum;
	}
	public List<RuleConfigItemTO> getConfigItems() {
		return configItems;
	}
	public void setConfigItems(List<RuleConfigItemTO> configItems) {
		this.configItems = configItems;
	}
	
}
