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
		
		data.setClientList((List<Integer>)RuleConfigDescriptorEnum.CLIENT_LIST.parse(ruleConfig, metadata));
		data.setIncludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.parse(ruleConfig, metadata));
		data.setExcludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST.parse(ruleConfig, metadata));
		data.setBrands((List<Integer>)RuleConfigDescriptorEnum.BRAND_LIST.parse(ruleConfig, metadata));
		data.setMinOrderValue((Money) RuleConfigDescriptorEnum.MIN_ORDER_VALUE.parse(ruleConfig, metadata));
		data.setFixedRsOff((Money)RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF.parse(ruleConfig, metadata));
		data.setNoOfTimesinMonth((Integer)RuleConfigDescriptorEnum.NUMBER_OF_TIMES_IN_MONTH.parse(ruleConfig, metadata));   
		
		return data;
	}

}
