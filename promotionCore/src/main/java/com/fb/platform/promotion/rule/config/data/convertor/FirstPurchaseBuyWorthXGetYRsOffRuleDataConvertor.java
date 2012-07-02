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
import com.fb.platform.promotion.rule.config.data.BuyWorthXGetYPercentOffRuleData;
import com.fb.platform.promotion.rule.config.data.BuyWorthXGetYRsOffRuleData;
import com.fb.platform.promotion.rule.config.data.FirstPurchaseBuyWorthXGetYRsOffRuleData;
import com.fb.platform.promotion.rule.config.data.RuleData;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;

/**
 * @author keith
 *
 */
public class FirstPurchaseBuyWorthXGetYRsOffRuleDataConvertor implements
		RuleDataConvertor {

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.config.data.convertor.DBConfigToRuleDataConvertor#convert(com.fb.platform.promotion.rule.config.RuleConfiguration)
	 */
	@Override
	public RuleData convert(RuleConfiguration ruleConfig, RuleConfigMetadata metadata) throws MandatoryDataMissingException{
		FirstPurchaseBuyWorthXGetYRsOffRuleData data = new FirstPurchaseBuyWorthXGetYRsOffRuleData();
		
		data.setClientList((List<Integer>)RuleConfigDescriptorEnum.CLIENT_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.CLIENT_LIST)));
		data.setIncludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST)));
		data.setExcludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST)));
		data.setBrands((List<Integer>)RuleConfigDescriptorEnum.BRAND_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.BRAND_LIST)));
		data.setMinOrderValue((Money) RuleConfigDescriptorEnum.MIN_ORDER_VALUE.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.MIN_ORDER_VALUE)));
		data.setFixedRsOff((Money)RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF)));
		
		return data;
		
	}

}
