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
public class BuyXGetYFreeRuleMetadata extends RuleConfigMetadata {

	public BuyXGetYFreeRuleMetadata() {
		List<RuleConfigItemMetadata> ruleConfigItemMetadata = new ArrayList<RuleConfigItemMetadata>();
		
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.BRAND_LIST, false));
		
		setRuleConfigItemMetadata(ruleConfigItemMetadata);
		
	}
}
