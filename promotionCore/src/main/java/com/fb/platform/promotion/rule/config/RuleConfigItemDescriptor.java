package com.fb.platform.promotion.rule.config;

/**
 * @author keith
 * This class will be a composition of RuleConfigDescriptorEnum that will hold all the ruleConfig constants
 * and properties to be associated with these ruleConfig constants. 
 */
public class RuleConfigItemDescriptor {
	
	private RuleConfigDescriptorEnum ruleConfigDescriptorEnum = null;
	private boolean isMandatory = false;
	private boolean isRepeatable = false;
	
	public RuleConfigItemDescriptor(RuleConfigDescriptorEnum ruleConfigDescriptorEnum, boolean isMandatory, boolean isRepeatable) {
		this.ruleConfigDescriptorEnum = ruleConfigDescriptorEnum;
		this.isMandatory = isMandatory;
		this.isRepeatable = isRepeatable;
	}
	
	/*
	 * isRepeatable will be defaulted to false 
	 */
	public RuleConfigItemDescriptor(RuleConfigDescriptorEnum ruleConfigDescriptorEnum, boolean isMandatory) {
		this.ruleConfigDescriptorEnum = ruleConfigDescriptorEnum;
		this.isMandatory = isMandatory;
	}
	
	/*
	 * isRepeatable will be defaulted to false 
	 * isMandatory will be defaulted to false
	 */
	public RuleConfigItemDescriptor(RuleConfigDescriptorEnum ruleConfigDescriptorEnum) {
		this.ruleConfigDescriptorEnum = ruleConfigDescriptorEnum;
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
	
	public boolean isRepeatable() {
		return isRepeatable;
	}
	
	public void setRepeatable(boolean isRepeatable) {
		this.isRepeatable = isRepeatable;
	}

}
