/**
 * 
 */
package com.fb.platform.promotion.rule.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author keith
 * 
 */
public class RuleConfigBundleDescriptor {

	private List<RuleConfigItemDescriptor> ruleConfigItemsList = null;
	private boolean isMandatory = false;

	public RuleConfigBundleDescriptor() {
		ruleConfigItemsList = new ArrayList<RuleConfigItemDescriptor>();
	}

	public List<RuleConfigItemDescriptor> getRuleConfigItemsList() {
		return ruleConfigItemsList;
	}

	public void setRuleConfigItemsList(List<RuleConfigItemDescriptor> ruleConfigItemsList) {
		this.ruleConfigItemsList = ruleConfigItemsList;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public void addConfigItemDescriptor(RuleConfigItemDescriptor configItemDesc) {
		this.ruleConfigItemsList.add(configItemDesc);
	}

}
