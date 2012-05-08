package com.fb.platform.promotion.rule;

/**
 * @author nehaga
 * This class will be a composition of RuleConfigDescriptorEnum that will hold all the ruleConfig constants
 * and properties to be associated with these ruleConfig constants. 
 */
public class RuleConfigDescriptorItem {
	
	private RuleConfigDescriptorEnum ruleConfigDescriptorEnum = null;
	private boolean isMandatory = false;
	
	public RuleConfigDescriptorItem() {
		super();
	}
	
	public RuleConfigDescriptorItem(RuleConfigDescriptorEnum ruleConfigDescriptorEnum, boolean isMandatory) {
		this.ruleConfigDescriptorEnum = ruleConfigDescriptorEnum;
		this.isMandatory = isMandatory;
	}
	public RuleConfigDescriptorEnum getRuleConfigDescriptorEnum() {
		return ruleConfigDescriptorEnum;
	}
	public void setRuleConfigDescriptorEnum(
			RuleConfigDescriptorEnum ruleConfigDescriptorEnum) {
		this.ruleConfigDescriptorEnum = ruleConfigDescriptorEnum;
	}
	public boolean isMandatory() {
		return isMandatory;
	}
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	
	

}
