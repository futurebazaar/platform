package com.fb.platform.promotion.rule;

/**
 * @author nehaga
 *	This enum will store the rule configs, description and type.
 */
public enum RuleConfigDescriptorEnum {
	CLIENT_LIST("Client ID", "String"),
	BRAND_LIST("Brand ID", "String"),
	MIN_ORDER_VALUE("Minimum order value", "decimal"),
	FIXED_DISCOUNT_RS_OFF("Fixed discount", "decimal"),
	DISCOUNT_PERCENTAGE("Discount percentage", "decimal"),
	MAX_DISCOUNT_CEIL_IN_VALUE("Maximum discount in rupees", "decimal"),
	PRODUCT_ID("Product Ids", "String"),
	PRODUCT_DISCOUNT_RS_OFF("Product discount", "decimal"),
	PRODUCT_DISCOUNTED_VALUE("Product discounted value", "decimal"),
	CATEGORY_INCLUDE_LIST("Include category ID", "String"),
	CATEGORY_EXCLUDE_LIST("Exclude category ID", "String");
	
	private String description = null;
	
	private String type = null;
	
	private RuleConfigDescriptorEnum(String description, String type) {
		this.description = description;
		this.type = type;
	}
	
	public String getType() {
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
