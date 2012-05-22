package com.fb.platform.promotion.rule;

import java.util.List;

/**
 * @author nehaga
 *
 */
public class RuleConfigDescriptor {
	
	private List<RuleConfigDescriptorItem> ruleConfigItemsList = null;
	private RulesEnum rulesEnum;
	
	public List<RuleConfigDescriptorItem> getRuleConfigItemsList() {
		return ruleConfigItemsList;
	}
	public void setRuleConfigItemsList(
			List<RuleConfigDescriptorItem> ruleConfigItemsList) {
		this.ruleConfigItemsList = ruleConfigItemsList;
	}
	public RulesEnum getRulesEnum() {
		return rulesEnum;
	}
	public void setRulesEnum(RulesEnum rulesEnum) {
		this.rulesEnum = rulesEnum;
	}
	
}
