package com.fb.platform.promotion.rule.config;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.rule.RulesEnum;

/**
 * @author keith
 * 
 */
public class RuleConfigDescriptor {

	private List<RuleConfigItemDescriptor> ruleConfigItemsList = null;
	private List<RuleConfigBundleDescriptor> ruleConfigBundleList = null;
	private RulesEnum rulesEnum;

	public RuleConfigDescriptor() {
		ruleConfigItemsList = new ArrayList<RuleConfigItemDescriptor>();
		ruleConfigBundleList = new ArrayList<RuleConfigBundleDescriptor>();
		rulesEnum = null;
	}

	public List<RuleConfigItemDescriptor> getRuleConfigItemsList() {
		return ruleConfigItemsList;
	}

	public List<RuleConfigBundleDescriptor> getRuleConfigBundleList() {
		return ruleConfigBundleList;
	}

	public void setRuleConfigBundleList(List<RuleConfigBundleDescriptor> ruleConfigBundleList) {
		this.ruleConfigBundleList = ruleConfigBundleList;
	}

	public void setRuleConfigItemsList(List<RuleConfigItemDescriptor> ruleConfigItemsList) {
		this.ruleConfigItemsList = ruleConfigItemsList;
	}

	public RulesEnum getRulesEnum() {
		return rulesEnum;
	}

	public void setRulesEnum(RulesEnum rulesEnum) {
		this.rulesEnum = rulesEnum;
	}

	public void addConfigItemDescriptor(RuleConfigItemDescriptor configItemDesc) {
		this.ruleConfigItemsList.add(configItemDesc);
	}

	public void addConfigBundleDescriptor(RuleConfigBundleDescriptor configBundleDesc) {
		this.ruleConfigBundleList.add(configBundleDesc);
	}
}
