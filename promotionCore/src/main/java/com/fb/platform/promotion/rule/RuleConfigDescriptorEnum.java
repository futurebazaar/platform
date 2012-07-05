package com.fb.platform.promotion.rule;

/**
 * @author nehaga
 *	This enum will store the rule configs, description and type.
 */
public enum RuleConfigDescriptorEnum {
	
	CLIENT_LIST("Client ID", RuleConfigDescriptorType.CSI),
	CATEGORY_INCLUDE_LIST("Include category ID", RuleConfigDescriptorType.CSI),
	CATEGORY_EXCLUDE_LIST("Exclude category ID", RuleConfigDescriptorType.CSI),
	BRAND_LIST("Brand ID", RuleConfigDescriptorType.CSI),
	PRODUCT_ID("Product ID", RuleConfigDescriptorType.CSI),
	MIN_ORDER_VALUE("Minimum order value", RuleConfigDescriptorType.DECIMAL),
	FIXED_DISCOUNT_RS_OFF("Fixed discount", RuleConfigDescriptorType.DECIMAL),
	DISCOUNT_PERCENTAGE("Discount percentage", RuleConfigDescriptorType.PERCENT),
	MAX_DISCOUNT_CEIL_IN_VALUE("Maximum discount in rupees", RuleConfigDescriptorType.DECIMAL),
	PRODUCT_DISCOUNT_RS_OFF("Product discount", RuleConfigDescriptorType.DECIMAL),
	PRODUCT_DISCOUNTED_VALUE("Product discounted value", RuleConfigDescriptorType.DECIMAL),
	VARIABLE_DISCOUNT_PERCENTAGE("Variable Discount percentage", RuleConfigDescriptorType.CSKV);
	
	private String description = null;
	
	private RuleConfigDescriptorType type = null;
	
	private RuleConfigDescriptorEnum(String description, RuleConfigDescriptorType type) {
		this.description = description;
		this.type = type;
	}
	
	public RuleConfigDescriptorType getType() {
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
}
