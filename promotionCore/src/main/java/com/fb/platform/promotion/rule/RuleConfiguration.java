/**
 * 
 */
package com.fb.platform.promotion.rule;

import java.util.ArrayList;
import java.util.List;


/**
 * @author vinayak
 *
 */
public abstract class RuleConfiguration {

	protected List<RuleConfigItem> configItems = new ArrayList<RuleConfigItem>();

	public void add(RuleConfigItem item) {
		configItems.add(item);
	}
}
