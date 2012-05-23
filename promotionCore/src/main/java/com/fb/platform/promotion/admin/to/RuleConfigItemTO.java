package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fb.platform.promotion.rule.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.util.RuleValidatorUtils;
import com.fb.platform.promotion.util.StringToIntegerList;


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
		RuleConfigDescriptorEnum ruleDescriptor = null;
		if(StringUtils.isBlank(ruleConfigName)) {
			ruleNameInvalidationList.add("Rule name empty");
		}
		if(!RuleConfigDescriptorEnum.isRuleConfigValid(ruleConfigName)) {
			ruleNameInvalidationList.add("Invalid rule config name " + ruleConfigName);
		} else if(StringUtils.isNotBlank(ruleConfigValue)){
			ruleDescriptor = RuleConfigDescriptorEnum.valueOf(ruleConfigName);
			switch(ruleDescriptor.getType()) {
			case CSI:
					if(!StringToIntegerList.isListValid(ruleConfigValue.split(","))) {
						ruleNameInvalidationList.add(ruleDescriptor.getDescription() + " invalid value : " + ruleConfigValue);
					} else {
						List<String> idList = new ArrayList<String>();
						for (String id : StringUtils.split(ruleConfigValue, ",")) {
							idList.add(id.trim());
						}
						ruleConfigValue = StringUtils.join(idList.toArray(), ",");
					}
				break;
			case DECIMAL:
				if(!RuleValidatorUtils.isValidPositiveDecimal(ruleConfigValue)) {
					ruleNameInvalidationList.add(ruleDescriptor.getDescription() + " invalid number");
				}
				break;
			case PERCENT:
				if(!RuleValidatorUtils.isValidPositiveDecimal(ruleConfigValue)) {
					ruleNameInvalidationList.add(ruleDescriptor.getDescription() + " invalid number.");
				} else if (RuleValidatorUtils.isValidPositiveDecimal(ruleConfigValue) && new Float(ruleConfigValue) > 100) {
					ruleNameInvalidationList.add(ruleDescriptor.getDescription() + " cannot be greater than 100%");
				}
				break;
			}
		}
		return StringUtils.join(ruleNameInvalidationList.toArray(), ",");
	}
	
}
