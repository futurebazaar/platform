/**
 * 
 */
package com.fb.platform.promotion.rule.metadata;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;

/**
 * @author keith
 *
 */
public class CategoryBasedVariablePercentOffRuleMetadata extends RuleConfigMetadata {

	public CategoryBasedVariablePercentOffRuleMetadata() {
		
		List<RuleConfigItemMetadata> ruleConfigItemMetadata = new ArrayList<RuleConfigItemMetadata>();
		
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE, false));
		setRuleConfigItemMetadata(ruleConfigItemMetadata);
		
		// Bundle Metadata
		List<RuleConfigBundleMetadata> ruleBundleMetaData = new ArrayList<RuleConfigBundleMetadata>();
		RuleConfigBundleMetadata ruleBundleMetadataItems = new RuleConfigBundleMetadata(RuleConfigRepeatEnum.REPEATABLE, true);
		List<RuleConfigItemMetadata> ruleConfigBundleItemsMetadata = new ArrayList<RuleConfigItemMetadata>();
		ruleConfigBundleItemsMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.DISCOUNT_PERCENTAGE, RuleConfigRepeatEnum.REPEATABLE, false));
		ruleConfigBundleItemsMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, RuleConfigRepeatEnum.REPEATABLE, false));
		ruleBundleMetadataItems.setConfigItemMetadataList(ruleConfigBundleItemsMetadata);
		ruleBundleMetaData.add(ruleBundleMetadataItems);
		setRuleConfigBundleMetadata( ruleBundleMetaData);
	}
}
