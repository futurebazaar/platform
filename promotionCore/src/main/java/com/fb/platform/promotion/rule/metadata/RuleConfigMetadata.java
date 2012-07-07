/**
 * 
 */
package com.fb.platform.promotion.rule.metadata;

import java.util.List;

import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;

/**
 * @author keith
 *
 */
public class RuleConfigMetadata {
	
	private List<RuleConfigBundleMetadata> ruleConfigBundleMetadata;
	
	private List<RuleConfigItemMetadata> ruleConfigItemMetadata;

	public List<RuleConfigBundleMetadata> getRuleConfigBundleMetadata() {
		return ruleConfigBundleMetadata;
	}

	public void setRuleConfigBundleMetadata(List<RuleConfigBundleMetadata> ruleConfigBundleMetadata) {
		this.ruleConfigBundleMetadata = ruleConfigBundleMetadata;
	}

	public List<RuleConfigItemMetadata> getRuleConfigItemMetadata() {
		return ruleConfigItemMetadata;
	}

	public void setRuleConfigItemMetadata(List<RuleConfigItemMetadata> ruleConfigItemMetadata) {
		this.ruleConfigItemMetadata = ruleConfigItemMetadata;
	}
	
	public boolean isMandatory(RuleConfigDescriptorEnum configItem) {
		return false;
	}

}
