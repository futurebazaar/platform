/**
 * 
 */
package com.fb.platform.promotion.rule;

import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.RuleData;
import com.fb.platform.promotion.rule.config.data.convertor.BuyWorthXGetYPercentOffRuleDataConvertor;
import com.fb.platform.promotion.rule.config.data.convertor.BuyWorthXGetYRsOffRuleDataConvertor;
import com.fb.platform.promotion.rule.config.data.convertor.BuyXBrandGetYRsOffOnZProductRuleDataConvertor;
import com.fb.platform.promotion.rule.config.data.convertor.BuyXGetYFreeRuleDataConvertor;
import com.fb.platform.promotion.rule.config.data.convertor.BuyXQuantityGetVariablePercentOffRuleDataConvertor;
import com.fb.platform.promotion.rule.config.data.convertor.FirstPurchaseBuyWorthXGetYRsOffRuleDataConvertor;
import com.fb.platform.promotion.rule.config.data.convertor.MonthlyDiscountRsOffDataConverter;
import com.fb.platform.promotion.rule.config.data.convertor.RuleDataConvertor;
import com.fb.platform.promotion.rule.metadata.BuyWorthXGetYPercentOffRuleMetadata;
import com.fb.platform.promotion.rule.metadata.BuyWorthXGetYRsOffRuleMetadata;
import com.fb.platform.promotion.rule.metadata.BuyXBrandGetYRsOffOnZProductRuleMatadata;
import com.fb.platform.promotion.rule.metadata.BuyXGetYFreeRuleMetadata;
import com.fb.platform.promotion.rule.metadata.BuyXQuantityGetVariablePercentOffRuleMetadata;
import com.fb.platform.promotion.rule.metadata.FirstPurchaseBuyWorthXGetYRsOffRuleMetadata;
import com.fb.platform.promotion.rule.metadata.MonthlyDiscountRsOffRuleMetadata;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;

/**
 * This Enum identifies the Java Class associated with a Rule
 * @author vinayak
 *
 */
public enum RulesEnum {

	BUY_X_GET_Y_FREE("BUY_X_GET_Y_FREE", new BuyXGetYFreeRuleMetadata(), new BuyXGetYFreeRuleDataConvertor()),
	BUY_WORTH_X_GET_Y_RS_OFF("BUY_WORTH_X_GET_Y_RS_OFF", new BuyWorthXGetYRsOffRuleMetadata(), new BuyWorthXGetYRsOffRuleDataConvertor()),
	BUY_WORTH_X_GET_Y_PERCENT_OFF("BUY_WORTH_X_GET_Y_PERCENT_OFF", new BuyWorthXGetYPercentOffRuleMetadata(), new BuyWorthXGetYPercentOffRuleDataConvertor()),
	BUY_X_BRAND_GET_Y_RS_OFF_ON_Z_PRODUCT("BUY_X_BRAND_GET_Y_RS_OFF_ON_Z_PRODUCT", new BuyXBrandGetYRsOffOnZProductRuleMatadata(), new BuyXBrandGetYRsOffOnZProductRuleDataConvertor()),
	FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF", new FirstPurchaseBuyWorthXGetYRsOffRuleMetadata(), new FirstPurchaseBuyWorthXGetYRsOffRuleDataConvertor()),
	BUY_X_QUANTITY_GET_VARIABLE_PERCENT_OFF("BUY_X_QUANTITY_GET_VARIABLE_PERCENT_OFF", new BuyXQuantityGetVariablePercentOffRuleMetadata(), new BuyXQuantityGetVariablePercentOffRuleDataConvertor()),
	MONTHLY_DISCOUNT_RS_OFF("MONTHLDY_DISCOUNT_RS_OFF", new MonthlyDiscountRsOffRuleMetadata(), new MonthlyDiscountRsOffDataConverter() );
	
	public static boolean isRuleValid(String ruleName) {
		RulesEnum[] rules = RulesEnum.values();
		for(RulesEnum ruleEnum : rules) {
			if(ruleEnum.name().equals(ruleName)) {
				return true;
			}
		}
		return false;
	}
	
	private String description = null;
	
	private RuleConfigMetadata metadata = null;
	
	private RuleDataConvertor dbToRuleData = null;
	
	private RulesEnum(String description, RuleConfigMetadata metadata, RuleDataConvertor dbToRuleDataConvertor) {
		this.setDescription(description);
		this.metadata = metadata;
		this.dbToRuleData = dbToRuleDataConvertor;
	}
	
	public RuleConfigMetadata getMetaData() {
		return this.metadata;
	}

	public String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}
	
	public RuleData getRuleData(RuleConfiguration ruleConfig) {
		return this.dbToRuleData.convert(ruleConfig,this.metadata);
	}
	
	public static RulesEnum getRule(String ruleName) {
	
		for(RulesEnum rule : RulesEnum.values()) {
			if(ruleName.equalsIgnoreCase(rule.getDescription())) {
				return rule;
			}
		}
		return null;
	}

}
