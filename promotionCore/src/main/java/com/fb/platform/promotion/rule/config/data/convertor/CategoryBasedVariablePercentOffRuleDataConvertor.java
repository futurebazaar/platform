/**
 * 
 */
package com.fb.platform.promotion.rule.config.data.convertor;

import java.math.BigDecimal;
import java.util.List;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.exception.MandatoryDataMissingException;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.CategoryBasedVariablePercentOffRuleData;
import com.fb.platform.promotion.rule.config.data.CategoryDiscount;
import com.fb.platform.promotion.rule.config.data.RuleData;
import com.fb.platform.promotion.rule.config.type.CategoryDiscountPair;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;

/**
 * @author keith
 *
 */
public class CategoryBasedVariablePercentOffRuleDataConvertor implements
		RuleDataConvertor {

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.config.data.convertor.DBConfigToRuleDataConvertor#convert(com.fb.platform.promotion.rule.config.RuleConfiguration)
	 */
	@Override
	public RuleData convert(RuleConfiguration ruleConfig, RuleConfigMetadata metadata) throws MandatoryDataMissingException {
		CategoryBasedVariablePercentOffRuleData data = new CategoryBasedVariablePercentOffRuleData();
		
		data.setClientList((List<Integer>)RuleConfigDescriptorEnum.CLIENT_LIST.parse(ruleConfig, metadata));
		data.setMinOrderValue((Money) RuleConfigDescriptorEnum.MIN_ORDER_VALUE.parse(ruleConfig, metadata));
		data.setMaxDiscountPerUse((Money) RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE.parse(ruleConfig, metadata));
		
		// Create CatDiscMap from ConfigBundleItem
		CategoryDiscount map = new CategoryDiscount();
		
		//Parse Bundle
		int i = 1;
		CategoryDiscountPair pair;
		while(null != RuleConfigDescriptorEnum.DISCOUNT_PERCENTAGE.parse(ruleConfig, metadata, i)) {
			pair = new CategoryDiscountPair(
					(BigDecimal)RuleConfigDescriptorEnum.DISCOUNT_PERCENTAGE.parse(ruleConfig, metadata, i),
					(List<Integer>)RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.parse(ruleConfig, metadata, i));
			map.addCategoryDiscountPair(pair);
			i++;
		}
		data.setCategoryDiscountPairs(map);
		return data;
	}

}
