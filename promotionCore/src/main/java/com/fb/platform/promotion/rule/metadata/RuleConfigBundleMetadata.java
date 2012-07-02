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
	private RuleConfigItemRepeatEnum ruleConfigItemRepeatEnum;
	private boolean isMandatory;
	
	public RuleConfigBundleMetadata(String bundleName,
			List<RuleConfigItemMetadata> configItemMetadataList) {
		this.bundleName = bundleName;
		this.configItemMetadataList = configItemMetadataList;
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
	
	
	

}
