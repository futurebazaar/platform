/**
 * 
 */
package com.fb.platform.promotion.rule.config.data.convertor;

import java.util.List;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.exception.MandatoryDataMissingException;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.BuyXQuantityGetVariablePercentOffRuleData;
import com.fb.platform.promotion.rule.config.data.RuleData;
import com.fb.platform.promotion.rule.config.type.QuantityDiscountMap;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;

/**
 * @author keith
 *
 */
public class BuyXQuantityGetVariablePercentOffRuleDataConvertor implements
RuleDataConvertor {

	/* (non-Javadoc)
	* @see com.fb.platform.promotion.rule.config.data.convertor.DBConfigToRuleDataConvertor#convert(com.fb.platform.promotion.rule.config.RuleConfiguration)
	*/
	@Override
	public RuleData convert(RuleConfiguration ruleConfig, RuleConfigMetadata metadata) throws MandatoryDataMissingException{
		BuyXQuantityGetVariablePercentOffRuleData data = new BuyXQuantityGetVariablePercentOffRuleData();
		
		data.setClientList((List<Integer>)RuleConfigDescriptorEnum.CLIENT_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.CLIENT_LIST)));
		data.setIncludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST)));
		data.setExcludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST)));
		data.setBrands((List<Integer>)RuleConfigDescriptorEnum.BRAND_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.BRAND_LIST)));
		data.setMinOrderValue((Money) RuleConfigDescriptorEnum.MIN_ORDER_VALUE.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.MIN_ORDER_VALUE)));
		data.setMaxDiscountPerUse((Money)RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE)));
		data.setQuantityDiscountMap((QuantityDiscountMap)RuleConfigDescriptorEnum.VARIABLE_DISCOUNT_PERCENTAGE.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.VARIABLE_DISCOUNT_PERCENTAGE)));
		
		return data;
	
	}
	

}
