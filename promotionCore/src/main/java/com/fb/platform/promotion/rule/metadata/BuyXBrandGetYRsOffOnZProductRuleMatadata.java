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
public class BuyXBrandGetYRsOffOnZProductRuleMatadata extends
		RuleConfigMetadata {

	public BuyXBrandGetYRsOffOnZProductRuleMatadata() {
		List<RuleConfigItemMetadata> ruleConfigItemMetadata = new ArrayList<RuleConfigItemMetadata>();
		
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.BRAND_LIST, true));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.PRODUCT_ID, true));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, true));
		ruleConfigItemMetadata.add(new RuleConfigItemMetadata(RuleConfigDescriptorEnum.PRODUCT_DISCOUNTED_VALUE, true));
		
		setRuleConfigItemMetadata(ruleConfigItemMetadata);
		
	}
	
}
