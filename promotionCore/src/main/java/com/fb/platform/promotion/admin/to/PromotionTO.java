package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.RulesEnum;

/**
 * @author nehaga
 *
 */
public class PromotionTO {
	
	private int id;
	private int ruleId;
	private String promotionName;
	private String description;
	private boolean isActive;
	private DateTime validFrom;
	private DateTime validTill;
	private int maxUses = 0;
	private Money maxAmount = null;
	private int maxUsesPerUser = 0;
	private Money maxAmountPerUser = null;
	private String ruleName = null;
	private int couponCount = 0;
	private List<RuleConfigItemTO> configItems = new ArrayList<RuleConfigItemTO>();
	
	public String getPromotionName() {
		return promotionName;
	}
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
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
	public List<RuleConfigItemTO> getConfigItems() {
		return configItems;
	}
	public void setConfigItems(List<RuleConfigItemTO> configItems) {
		this.configItems = configItems;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRuleId() {
		return ruleId;
	}
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
	public int getCouponCount() {
		return couponCount;
	}
	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}
	public String isValid() {
		List<String> invalidationList = new ArrayList<String>();
		invalidationList.addAll(isNameValid());
		invalidationList.addAll(isRuleValid());
		invalidationList.addAll(isDateConfigValid());
		invalidationList.addAll(isLimitsConfigValid());
		for(RuleConfigItemTO ruleConfigItem : configItems) {
			if(!StringUtils.isEmpty(ruleConfigItem.isValid())) {
				invalidationList.add(ruleConfigItem.isValid());
			}
		}
		return StringUtils.join(invalidationList.toArray(), ",");
	}
	
	private List<String> isNameValid() {
		List<String> nameInvalidationList = new ArrayList<String>();
		if(StringUtils.isBlank(promotionName)) {
			nameInvalidationList.add("Promotion Name cannot be blank");
		}
		return nameInvalidationList;
	}
	
	private List<String> isLimitsConfigValid() {
		List<String> limitsConfigInvalidationList = new ArrayList<String>();
		if(maxAmount == null) {
			limitsConfigInvalidationList.add("Max amount cannot be empty");
		}
		if(maxAmountPerUser == null) {
			limitsConfigInvalidationList.add("Max amount per user cannot be empty");
		}
		return limitsConfigInvalidationList;
	}
	
	private List<String> isDateConfigValid() {
		List<String> dateInvalidationList = new ArrayList<String>();
		if(getValidFrom() != null && getValidTill() != null && getValidTill().isBefore(getValidFrom())) {
			dateInvalidationList.add("Valid Till date before Valid From");
		}
		return dateInvalidationList;
	}
	
	private List<String> isRuleValid() {
		List<String> ruleInvalidationList = new ArrayList<String>();
		if(StringUtils.isEmpty(ruleName)) {
			ruleInvalidationList.add("Rule name empty");
		}
		if(!RulesEnum.isRuleValid(ruleName)) {
			ruleInvalidationList.add("Invalid rule name " + ruleName);
		}
		return ruleInvalidationList;
	}
}
