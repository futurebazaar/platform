/**
 * 
 */
package com.fb.platform.payback.rule;

import java.util.List;

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
		for (RuleConfigItem rci : configItems) {
			if (rci.getKey().equalsIgnoreCase(key)) {
				return rci;
			}
		}
		return null;
	}

	public String getConfigItemValue(int index) {
		return configItems.get(index).getValue();
	}

	public String getConfigItemValue(String key) {
		for (RuleConfigItem rci : configItems) {
			if (rci.getKey().equalsIgnoreCase(key)) {
				return rci.getValue();
			}
		}
		return null;
	}
}
