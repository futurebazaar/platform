package com.fb.platform.promotion.to;

import java.util.List;

import com.fb.platform.promotion.rule.RuleConfigDescriptor;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RulesEnum;

public class FetchRuleResponse {
	
	private String sessionToken;
	private List<RuleConfigDescriptor> rulesList;
	private FetchRulesEnum fetchRulesEnum;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public List<RuleConfigDescriptor> getRulesList() {
		return rulesList;
	}
	public void setRulesList(List<RuleConfigDescriptor> rulesList) {
		this.rulesList = rulesList;
	}
	public FetchRulesEnum getFetchRulesEnum() {
		return fetchRulesEnum;
	}
	public void setFetchRulesEnum(FetchRulesEnum fetchRulesEnum) {
		this.fetchRulesEnum = fetchRulesEnum;
	}
	
}
