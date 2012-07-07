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
public class FirstPurchaseBuyWorthXGetYRsOffRuleMetadata extends RuleConfigMetadata {

	public FirstPurchaseBuyWorthXGetYRsOffRuleMetadata() {

		List<RuleConfigItemMetadata> ruleConfigItemMetadata = new ArrayList<RuleConfigItemMetadata>();
		
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.BRAND_LIST, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF, true));
		
		setRuleConfigItemMetadata(ruleConfigItemMetadata);
		
	}
	
}
