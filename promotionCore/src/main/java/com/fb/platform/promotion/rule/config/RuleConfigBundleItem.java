/**
 * 
 */
package com.fb.platform.promotion.rule.config;

import java.io.Serializable;
import java.util.List;

/**
 * @author keith
 * 
 */
public class RuleConfigBundleItem implements Serializable {

	private List<RuleConfigItem> configItems = null;

	public RuleConfigBundleItem(List<RuleConfigItem> bundleItems) {
		this.setConfigItems(bundleItems);
	}

	public List<RuleConfigItem> getConfigItems() {
		return configItems;
	}

	public void setConfigItems(List<RuleConfigItem> configItems) {
		this.configItems = configItems;
	}

}
