package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.commons.to.PlatformMessage;
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
	public List<PlatformMessage> isValid() {
		List<PlatformMessage> invalidationList = new ArrayList<PlatformMessage>();
		invalidationList.addAll(isNameValid());
		invalidationList.addAll(isRuleValid());
		invalidationList.addAll(isDateConfigValid());
		invalidationList.addAll(isLimitsConfigValid());
		for(RuleConfigItemTO ruleConfigItem : configItems) {
			invalidationList.addAll(ruleConfigItem.isValid());
		}
		return invalidationList;
	}
	
	private List<PlatformMessage> isNameValid() {
		List<PlatformMessage> nameInvalidationList = new ArrayList<PlatformMessage>();
		if(StringUtils.isBlank(promotionName)) {
			nameInvalidationList.add(new PlatformMessage("EPA2", null));
		} else if(promotionName.length() > 50) {
			nameInvalidationList.add(new PlatformMessage("EPA3", null));
		}
		return nameInvalidationList;
	}
	
	private List<PlatformMessage> isLimitsConfigValid() {
		List<PlatformMessage> limitsConfigInvalidationList = new ArrayList<PlatformMessage>();
		if(maxAmount == null) {
			limitsConfigInvalidationList.add(new PlatformMessage("EPA4", null));
		}
		if(maxAmountPerUser == null) {
			limitsConfigInvalidationList.add(new PlatformMessage("EPA5", null));
		}
		return limitsConfigInvalidationList;
	}
	
	private List<PlatformMessage> isDateConfigValid() {
		List<PlatformMessage> dateInvalidationList = new ArrayList<PlatformMessage>();
		if(getValidFrom() != null && getValidTill() != null && getValidTill().isBefore(getValidFrom())) {
			dateInvalidationList.add(new PlatformMessage("EPA6", null));
		}
		return dateInvalidationList;
	}
	
	private List<PlatformMessage> isRuleValid() {
		List<PlatformMessage> ruleInvalidationList = new ArrayList<PlatformMessage>();
		if(StringUtils.isBlank(ruleName)) {
			ruleInvalidationList.add(new PlatformMessage("EPA7", null));
		}
		if(!RulesEnum.isRuleValid(ruleName)) {
			ruleInvalidationList.add(new PlatformMessage("EPA8", new Object[]{ruleName}));
		}
		return ruleInvalidationList;
	}
}
