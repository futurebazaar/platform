/**
 * 
 */
package com.fb.platform.promotion.rule.config;

import java.util.List;

import com.fb.commons.PlatformException;


/**
 * @author vinayak
 *
 */
public class RuleConfiguration {

	private List<RuleConfigItem> configItems = null;

	public RuleConfiguration(List<RuleConfigItem> configItems) {
		this.configItems = configItems;
	}

	public List<RuleConfigItem> getConfigItems() {
		return configItems;
	}

	public void setConfigItems(List<RuleConfigItem> configItems) {
		this.configItems = configItems;
	}
	
	public RuleConfigItem getConfigItem(int index) {
		return configItems.get(index);
	}
	
	public RuleConfigItem getConfigItem(String key) {
		for(RuleConfigItem rci : configItems) {
			if(rci.getKey().equalsIgnoreCase(key)) {
				return rci;
			}
		}
		return null;
	}
	
	public String getConfigItemValue(int index) {
		return configItems.get(index).getValue();
	}
	
	public String getConfigItemValue(String key) {
		for(RuleConfigItem rci : configItems) {
			if(rci.getKey().equalsIgnoreCase(key)) {
				return rci.getValue();
			}
		}
		throw new PlatformException("Config Item Value not found for the key - "+key);
	}

	public String getConfigItemValue(RuleConfigDescriptorEnum ruleEnum) {
		return getConfigItemValue(ruleEnum.name());
	}

	public boolean isConfigItemPresent(RuleConfigDescriptorEnum ruleEnum) {
		return isConfigItemPresent(ruleEnum.name());
	}

	public boolean isConfigItemPresent(String key) {
		return (getConfigItem(key) !=null);
	}

}
