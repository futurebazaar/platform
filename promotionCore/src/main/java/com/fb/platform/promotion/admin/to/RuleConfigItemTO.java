package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fb.platform.promotion.rule.RuleConfigDescriptorEnum;


/**
 * @author nehaga
 *
 */
public class RuleConfigItemTO {
	
	private String ruleConfigName;
	private String ruleConfigValue;
	
	public String getRuleConfigValue() {
		return ruleConfigValue;
	}
	public void setRuleConfigValue(String ruleConfigValue) {
		this.ruleConfigValue = ruleConfigValue;
	}
	public String getRuleConfigName() {
		return ruleConfigName;
	}
	public void setRuleConfigName(String ruleConfigName) {
		this.ruleConfigName = ruleConfigName;
	}
	
	public String isValid() {
		List<String> ruleNameInvalidationList = new ArrayList<String>();
		if(StringUtils.isEmpty(ruleConfigName)) {
			ruleNameInvalidationList.add("Rule name empty");
		}
		if(!RuleConfigDescriptorEnum.isRuleConfigValid(ruleConfigName)) {
			ruleNameInvalidationList.add("Invalid rule config name " + ruleConfigName);
		}
		return StringUtils.join(ruleNameInvalidationList.toArray(), ",");
	}
	
}
