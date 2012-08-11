package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fb.commons.to.PlatformMessage;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
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
	
	public List<PlatformMessage> isValid() {
		List<PlatformMessage> ruleNameInvalidationList = new ArrayList<PlatformMessage>();
		RuleConfigDescriptorEnum ruleDescriptor = null;
		if(StringUtils.isBlank(ruleConfigName)) {
			ruleNameInvalidationList.add(new PlatformMessage("EPA7", null));
		}
		if(!RuleConfigDescriptorEnum.isRuleConfigValid(ruleConfigName)) {
			ruleNameInvalidationList.add(new PlatformMessage("EPA9", new Object[] {ruleConfigName}));
		} else if(StringUtils.isNotBlank(ruleConfigValue)){
			ruleDescriptor = RuleConfigDescriptorEnum.valueOf(ruleConfigName);
			switch(ruleDescriptor.getType()) {
			case CSI:
					if(!StringToIntegerList.isListValid(ruleConfigValue.split(","))) {
						ruleNameInvalidationList.add(new PlatformMessage("EPA10", new Object[] {ruleDescriptor.getDescription(), ruleConfigValue}));
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
					ruleNameInvalidationList.add(new PlatformMessage("EPA11", new Object[] {ruleDescriptor.getDescription()}));
				}
				break;
			case PERCENT:
				if(!RuleValidatorUtils.isValidPositiveDecimal(ruleConfigValue)) {
					ruleNameInvalidationList.add(new PlatformMessage("EPA11", new Object[] {ruleDescriptor.getDescription()}));
				} else if (RuleValidatorUtils.isValidPositiveDecimal(ruleConfigValue) && new Float(ruleConfigValue) > 100) {
					ruleNameInvalidationList.add(new PlatformMessage("EPA12", new Object[] {ruleDescriptor.getDescription()}));
				}
				break;
			}
		}
		return ruleNameInvalidationList;
	}
	
}
