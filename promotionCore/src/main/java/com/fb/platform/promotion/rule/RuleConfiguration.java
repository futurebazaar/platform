/**
 * 
 */
package com.fb.platform.promotion.rule;

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
}
