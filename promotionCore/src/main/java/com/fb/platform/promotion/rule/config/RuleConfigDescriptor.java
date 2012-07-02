package com.fb.platform.promotion.rule.config;

import java.util.List;

import com.fb.platform.promotion.rule.RulesEnum;

/**
 * @author keith
 *
 */
public class RuleConfigDescriptor {
	
	private List<RuleConfigItemDescriptor> ruleConfigItemsList = null;
	private RulesEnum rulesEnum;
	
	public List<RuleConfigItemDescriptor> getRuleConfigItemsList() {
		return ruleConfigItemsList;
	}
	public void setRuleConfigItemsList(
			List<RuleConfigItemDescriptor> ruleConfigItemsList) {
		this.ruleConfigItemsList = ruleConfigItemsList;
	}
	public RulesEnum getRulesEnum() {
		return rulesEnum;
	}
	public void setRulesEnum(RulesEnum rulesEnum) {
		this.rulesEnum = rulesEnum;
	}
	
}
