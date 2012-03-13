package com.fb.platform.promotion.model;

public class Rule {

	private int ruleId;
	private String ruleDescription;
	private String ruleFunctionName;
	private Priority rulePriority;
	public int getRuleId() {
		return ruleId;
	}
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleDescription() {
		return ruleDescription;
	}
	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}
	public String getRuleFunctionName() {
		return ruleFunctionName;
	}
	public void setRuleFunctionName(String ruleFunctionName) {
		this.ruleFunctionName = ruleFunctionName;
	}
	public Priority getRulePriority() {
		return rulePriority;
	}
	public void setRulePriority(Priority rulePriority) {
		this.rulePriority = rulePriority;
	}
	
}
