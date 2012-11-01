/**
 * 
 */
package com.fb.platform.promotion.rule.config.data.convertor;

import java.math.BigDecimal;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.DiscountOnClearanceProductPercentOffRuleData;
import com.fb.platform.promotion.rule.config.data.RuleData;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;

/**
 * @author vinayak
 *
 */
public class DiscountOnClearanceProductPercentOffRuleDataConvertor implements RuleDataConvertor {

	@Override
	public RuleData convert(RuleConfiguration ruleConfig, RuleConfigMetadata metadata) {

		DiscountOnClearanceProductPercentOffRuleData data = new DiscountOnClearanceProductPercentOffRuleData();
		
		data.setMinOrderValue((Money) RuleConfigDescriptorEnum.MIN_ORDER_VALUE.parse(ruleConfig, metadata));
		data.setDiscountPercentage((BigDecimal)RuleConfigDescriptorEnum.DISCOUNT_PERCENTAGE.parse(ruleConfig, metadata));
		
		return data;
	}
}
