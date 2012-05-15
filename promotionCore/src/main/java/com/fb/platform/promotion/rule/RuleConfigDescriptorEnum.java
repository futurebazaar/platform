package com.fb.platform.promotion.rule;

/**
 * @author nehaga
 *	This enum will store the rule configs, description and type.
 */
public enum RuleConfigDescriptorEnum {
	CLIENT_LIST("Comma separated list of supported client ids.", "String"),
	BRAND_LIST("Comma separated list of brands", "String"),
	MIN_ORDER_VALUE("Minimum value of an order on which we apply this promotion.", "decimal"),
	FIXED_DISCOUNT_RS_OFF("Fixed amount to be given off on any order.", "decimal"),
	DISCOUNT_PERCENTAGE("Discount percentage to be given on any order.", "decimal"),
	MAX_DISCOUNT_CEIL_IN_VALUE("Maximum discount to be given on any order.", "decimal"),
	PRODUCT_ID("Product Id on which the promotion has to be applied.", "String"),
	PRODUCT_LIST("Comma separated list of products", "String"),
	PRODUCT_DISCOUNT_RS_OFF("Fixed amount to be given off on a product.", "decimal"),
	PRODUCT_DISCOUNTED_VALUE("", "decimal"),
	CATEGORY_INCLUDE_LIST("Comma separated list of categories included.", "String"),
	CATEGORY_EXCLUDE_LIST("Comma separated list of categories excluded.", "String");
	
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
