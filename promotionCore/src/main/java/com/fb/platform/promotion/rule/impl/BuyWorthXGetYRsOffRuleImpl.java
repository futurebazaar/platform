/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.StringToIntegerList;

/**
 * @author keith
 *
 */
public class BuyWorthXGetYRsOffRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(BuyWorthXGetYRsOffRuleImpl.class);
	
	private Money fixedRsOff = null;
	private Money minOrderValue = null;
	private List<Integer> clientList = null;
	private List<Integer> includeCategoryList = null;
	private List<Integer> excludeCategoryList = null;
	private List<Integer> brands;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {

		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST)){
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST),",");
			includeCategoryList = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST)){
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST),",");
			excludeCategoryList = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CLIENT_LIST)){
			StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CLIENT_LIST),",");
			clientList = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.MIN_ORDER_VALUE)){
			minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.MIN_ORDER_VALUE))));
			log.info("minOrderValue : " + minOrderValue.toString());
		}
		else{
			log.warn("Minimum Order Value not specified for this rule");
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.BRAND_LIST)){
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.BRAND_LIST),",");
			brands = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		}
		fixedRsOff = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF))));
		log.info("fixedRsOff : " + fixedRsOff.toString());
		log.info("minOrderValue : " + minOrderValue);
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if BuyWorthXGetYRsOffRuleImpl applies on order : " + request.getOrderId());
		}
		Money orderValue = new Money(request.getOrderValue());
		if( clientList != null && !request.isValidClient(clientList)){
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if( includeCategoryList != null && !request.isAllProductsInCategory(includeCategoryList)){
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if( excludeCategoryList != null && request.isAnyProductInCategory(excludeCategoryList)){
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if( brands != null && !request.isAllProductsInBrand(brands)){
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		if(minOrderValue!=null && orderValue.lt(minOrderValue)){
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		return PromotionStatusEnum.SUCCESS;
	}

	@Override
	public Money execute(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyWorthXGetYRsOffRuleImpl on order : " + request.getOrderId());
		}
		return fixedRsOff;
	}
	
	@Override
	public List<RuleConfigDescriptorItem> getRuleConfigs() {
		List<RuleConfigDescriptorItem> ruleConfigs = new ArrayList<RuleConfigDescriptorItem>();
		
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.BRAND_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, false));
		
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		return ruleConfigs;
	}
}
