package com.fb.platform.promotion.rule.config;

import com.fb.platform.promotion.exception.MandatoryDataMissingException;
import com.fb.platform.promotion.rule.config.type.RuleConfigDescriptorTypeEnum;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;

/**
 * @author nehaga
 *	This enum will store the rule configs, description and type.
 */
public enum RuleConfigDescriptorEnum {
	
	CLIENT_LIST("Client ID", RuleConfigDescriptorTypeEnum.CSI),
	CATEGORY_INCLUDE_LIST("Include category ID", RuleConfigDescriptorTypeEnum.CSI),
	CATEGORY_EXCLUDE_LIST("Exclude category ID", RuleConfigDescriptorTypeEnum.CSI),
	BRAND_LIST("Brand ID", RuleConfigDescriptorTypeEnum.CSI),
	PRODUCT_ID("Product ID", RuleConfigDescriptorTypeEnum.CSI),
	MIN_ORDER_VALUE("Minimum order value", RuleConfigDescriptorTypeEnum.MONEY),
	FIXED_DISCOUNT_RS_OFF("Fixed discount", RuleConfigDescriptorTypeEnum.MONEY),
	DISCOUNT_PERCENTAGE("Discount percentage", RuleConfigDescriptorTypeEnum.PERCENT),
	MAX_DISCOUNT_CEIL_IN_VALUE("Maximum discount in rupees", RuleConfigDescriptorTypeEnum.MONEY),
	PRODUCT_DISCOUNT_RS_OFF("Product discount", RuleConfigDescriptorTypeEnum.MONEY),
	PRODUCT_DISCOUNTED_VALUE("Product discounted value", RuleConfigDescriptorTypeEnum.MONEY),
	VARIABLE_DISCOUNT_PERCENTAGE("Variable Discount percentage", RuleConfigDescriptorTypeEnum.CSKV);
	
	private String description = null;
	
	private RuleConfigDescriptorTypeEnum type = null;
	
	private RuleConfigDescriptorEnum(String description, RuleConfigDescriptorTypeEnum type) {
		this.description = description;
		this.type = type;
	}
		
	public RuleConfigDescriptorTypeEnum getType() {
		return this.type;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public static boolean isRuleConfigValid(String ruleConfigName) {
		RuleConfigDescriptorEnum[] ruleConfigs = RuleConfigDescriptorEnum.values();
		for(RuleConfigDescriptorEnum ruleConfigEnum : ruleConfigs) {
			if(ruleConfigEnum.name().equals(ruleConfigName)) {
				return true;
			}
		}
		return false;
	}
	
	public Object parse(RuleConfiguration ruleConfig, RuleConfigMetadata metadata) throws MandatoryDataMissingException {
		return parse(ruleConfig,metadata,null);
	}
	
	public Object parse(RuleConfiguration ruleConfig, RuleConfigMetadata metadata, String newConfigName) throws MandatoryDataMissingException {
		String configItemName = new String((newConfigName == null)?this.name():newConfigName); 
		if (ruleConfig.isConfigItemPresent(configItemName)) {
			return this.type.convert(ruleConfig.getConfigItemValue(configItemName));
		}
		if(metadata.isMandatory(this)) {
			throw new MandatoryDataMissingException("Mandatory Data " + this.name() + " missing!" );
		}
		return null;
	}
	
	// Given the number of the configName to be fetched. Example (DISCOUNT_PERCENT, 1 ) will try to get DISCOUNT_PERCENT_1
	// Used incase of repeatable config items
	public Object parse(RuleConfiguration ruleConfig, RuleConfigMetadata metadata, int num) throws MandatoryDataMissingException {
		return parse(ruleConfig,metadata,new String(this.name()+"_"+num));
	}
}
