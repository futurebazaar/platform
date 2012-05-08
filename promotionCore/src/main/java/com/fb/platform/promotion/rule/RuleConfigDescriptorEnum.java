package com.fb.platform.promotion.rule;

/**
 * @author nehaga
 *	This enum will store the rule configs, description and type.
 */
public enum RuleConfigDescriptorEnum {
	CLIENT_LIST("Comma separated list of supported client ids.", "String"),
	CATEGORY_LIST("Comma separated list of supported categories id.", "String"),
	BRAND_LIST("Comma separated list of brands", "String"),
	MIN_ORDER_VALUE("Minimum value of an order(18,2) on which we apply this promotion.", "decimal"),
	FIXED_DISCOUNT_RS_OFF("Fixed amount(18,2) to be given off on any order.", "decimal"),
	DISCOUNT_PERCENTAGE("Discount percentage(18,2) to be given on any order.", "decimal"),
	MAX_DISCOUNT_CEIL_IN_VALUE("Maximum discount(18,2) to be given on any order.", "decimal"),
	PRODUCT_ID("Product Id on which the promotion has to be applied.", "String"),
	PRODUCT_LIST("Comma separated list of products", " String"),
	PRODUCT_DISCOUNT_RS_OFF("Fixed amount(18,2) to be given off on a product.", "decimal"),
	PRODUCT_DISCOUNTED_VALUE("", "decimal");
	
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
}
