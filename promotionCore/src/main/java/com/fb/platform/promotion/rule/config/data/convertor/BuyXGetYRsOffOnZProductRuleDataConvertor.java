/**
 * 
 */
package com.fb.platform.promotion.rule.config.data.convertor;

import java.util.List;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.exception.MandatoryDataMissingException;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.BuyWorthXGetYRsOffRuleData;
import com.fb.platform.promotion.rule.config.data.BuyXGetYRsOffOnZProductRuleData;
import com.fb.platform.promotion.rule.config.data.RuleData;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;

/**
 * @author keith
 * 
 */
public class BuyXGetYRsOffOnZProductRuleDataConvertor implements RuleDataConvertor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fb.platform.promotion.rule.config.data.convertor.
	 * DBConfigToRuleDataConvertor
	 * #convert(com.fb.platform.promotion.rule.config.RuleConfiguration)
	 */
	@Override
	public RuleData convert(RuleConfiguration ruleConfig, RuleConfigMetadata metadata)
			throws MandatoryDataMissingException {

		BuyXGetYRsOffOnZProductRuleData data = new BuyXGetYRsOffOnZProductRuleData();
		
		data.setClientList((List<Integer>)RuleConfigDescriptorEnum.CLIENT_LIST.parse(ruleConfig, metadata));
		data.setIncludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.parse(ruleConfig, metadata));
		data.setExcludeCategoryList((List<Integer>)RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST.parse(ruleConfig, metadata));
		data.setBrandList((List<Integer>)RuleConfigDescriptorEnum.BRAND_LIST.parse(ruleConfig, metadata));
		data.setSellerList((List<Integer>)RuleConfigDescriptorEnum.SELLER_LIST.parse(ruleConfig, metadata));
		data.setMinOrderValue((Money) RuleConfigDescriptorEnum.MIN_ORDER_VALUE.parse(ruleConfig, metadata));
		data.setProductDiscountValue((Money)RuleConfigDescriptorEnum.PRODUCT_DISCOUNTED_VALUE.parse(ruleConfig, metadata));
		data.setProductId((Integer)RuleConfigDescriptorEnum.PRODUCT_ID.parse(ruleConfig, metadata));

		return data;

	}

}
