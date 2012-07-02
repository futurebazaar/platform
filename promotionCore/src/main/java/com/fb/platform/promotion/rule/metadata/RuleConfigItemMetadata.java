/**
 * 
 */
package com.fb.platform.promotion.rule.metadata;

import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;

/**
 * @author keith
 *
 */
public class RuleConfigItemMetadata {

	private RuleConfigDescriptorEnum ruleConfigDescriptorEnum;
	private RuleConfigItemRepeatEnum ruleConfigItemRepeatEnum;
	private boolean isMandatory;
	
	
	public RuleConfigItemMetadata(
			RuleConfigDescriptorEnum ruleConfigDescriptorEnum) {
		this.ruleConfigDescriptorEnum = ruleConfigDescriptorEnum;
		this.isMandatory = false;
		this.ruleConfigItemRepeatEnum = RuleConfigItemRepeatEnum.NON_REPEATABLE;
	}

	public RuleConfigItemMetadata(RuleConfigDescriptorEnum ruleConfigDescriptorEnum, 
				boolean isMandatory) {
		this.ruleConfigDescriptorEnum = ruleConfigDescriptorEnum;
		this.isMandatory = isMandatory;
		this.ruleConfigItemRepeatEnum = RuleConfigItemRepeatEnum.NON_REPEATABLE;
	}

	public RuleConfigItemMetadata(
			RuleConfigDescriptorEnum ruleConfigDescriptorEnum,
			RuleConfigItemRepeatEnum ruleConfigItemRepeatEnum,
			boolean isMandatory) {
		this.ruleConfigDescriptorEnum = ruleConfigDescriptorEnum;
		this.ruleConfigItemRepeatEnum = ruleConfigItemRepeatEnum;
		this.isMandatory = isMandatory;
	}

	public RuleConfigItemRepeatEnum getRuleConfigItemRepeatEnum() {
		return ruleConfigItemRepeatEnum;
	}


	public void setRuleConfigItemRepeatEnum(
			RuleConfigItemRepeatEnum ruleConfigItemRepeatEnum) {
		this.ruleConfigItemRepeatEnum = ruleConfigItemRepeatEnum;
	}


	public boolean isMandatory() {
		return isMandatory;
	}


	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public RuleConfigDescriptorEnum getRuleConfigDescriptorEnum() {
		return ruleConfigDescriptorEnum;
	}

	public void setRuleConfigDescriptorEnum(RuleConfigDescriptorEnum ruleConfigDescriptorEnum) {
		this.ruleConfigDescriptorEnum = ruleConfigDescriptorEnum;
	}
	
}
