/**
 * 
 */
package com.fb.platform.promotion.rule.metadata;

import java.util.List;

/**
 * @author keith
 *
 */
public class RuleConfigBundleMetadata {
	
	private String bundleName;
	private List<RuleConfigItemMetadata> configItemMetadataList;
	private RuleConfigRepeatEnum ruleConfigItemRepeatEnum;
	private boolean isMandatory;
	
	public RuleConfigBundleMetadata(String bundleName,
			List<RuleConfigItemMetadata> configItemMetadataList,
			RuleConfigRepeatEnum ruleConfigItemRepeatEnum,
			boolean isMandatory) {
		this.bundleName = bundleName;
		this.configItemMetadataList = configItemMetadataList;
		this.isMandatory = isMandatory;
		this.ruleConfigItemRepeatEnum = ruleConfigItemRepeatEnum;
	}

	public RuleConfigBundleMetadata(List<RuleConfigItemMetadata> configItemMetadataList,
			RuleConfigRepeatEnum ruleConfigItemRepeatEnum,
			boolean isMandatory) {
		this.configItemMetadataList = configItemMetadataList;
		this.bundleName = null;
		this.isMandatory = isMandatory;
		this.ruleConfigItemRepeatEnum = ruleConfigItemRepeatEnum;
	}
	
	public RuleConfigBundleMetadata(RuleConfigRepeatEnum ruleConfigItemRepeatEnum,
			boolean isMandatory) {
		this.configItemMetadataList = null;
		this.bundleName = null;
		this.isMandatory = isMandatory;
		this.ruleConfigItemRepeatEnum = ruleConfigItemRepeatEnum;
	}
	
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public List<RuleConfigItemMetadata> getConfigItemMetadataList() {
		return configItemMetadataList;
	}

	public void setConfigItemMetadataList(
			List<RuleConfigItemMetadata> configItemMetadataList) {
		this.configItemMetadataList = configItemMetadataList;
	}

	public RuleConfigRepeatEnum getRuleConfigItemRepeatEnum() {
		return ruleConfigItemRepeatEnum;
	}

	public void setRuleConfigItemRepeatEnum(
			RuleConfigRepeatEnum ruleConfigItemRepeatEnum) {
		this.ruleConfigItemRepeatEnum = ruleConfigItemRepeatEnum;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	
	
	

}
