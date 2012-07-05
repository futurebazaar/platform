package com.fb.platform.promotion.rule.config.data.convertor;

import java.util.List;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.exception.MandatoryDataMissingException;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.MonthlyDiscountRsOffRuleData;
import com.fb.platform.promotion.rule.config.data.RuleData;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;

/**
 * @author salimm
 *
 */
public class MonthlyDiscountRsOffDataConverter implements RuleDataConvertor {

	@Override
	public RuleData convert(RuleConfiguration ruleConfig,RuleConfigMetadata metadata)  throws MandatoryDataMissingException {
		
		MonthlyDiscountRsOffRuleData data = new MonthlyDiscountRsOffRuleData();
		
		data.setClientList((List<Integer>)RuleConfigDescriptorEnum.CLIENT_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.CLIENT_LIST)));
		data.setIncludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST)));
		data.setExcludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST)));
		data.setBrands((List<Integer>)RuleConfigDescriptorEnum.BRAND_LIST.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.BRAND_LIST)));
		data.setMinOrderValue((Money) RuleConfigDescriptorEnum.MIN_ORDER_VALUE.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.MIN_ORDER_VALUE)));
		data.setFixedRsOff((Money)RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF)));
		data.setNoOfTimesinMonth((Integer)RuleConfigDescriptorEnum.NUMBER_OF_TIMES_IN_MONTH.parse(ruleConfig, metadata.isMandatory(RuleConfigDescriptorEnum.NUMBER_OF_TIMES_IN_MONTH) ));   
		
		return data;
	}

}
