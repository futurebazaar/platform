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
public class BuyXBrandGetYRsOffOnZProductRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(BuyXBrandGetYRsOffOnZProductRuleImpl.class);
	private Money minOrderValue;
	private List<Integer> brands;
	private List<Integer> clientList;
	private int productId;
	private Money productDiscountValue;
	private List<RuleConfigDescriptorItem> ruleConfigs = new ArrayList<RuleConfigDescriptorItem>();
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.MIN_ORDER_VALUE.name()))));
		StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.BRAND_LIST.name()),",");
		brands = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CLIENT_LIST.name()),",");
		clientList = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
		productId = Integer.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.PRODUCT_ID.name())).intValue();
		productDiscountValue = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.PRODUCT_DISCOUNTED_VALUE.name()))));
		log.info("minOrderValue : " + minOrderValue.toString() 
				+ ", productId : " + productId 
				+ ", productDiscountValue : " + productDiscountValue 
				+ ", clientList : " + clientList
				+ ", brandList : " + brands);
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if BuyXBrandGetYRsOffOnZProductRuleImpl applies on order : " + request.getOrderId());
		}
		Money orderValue = new Money(request.getOrderValue());
		if(!request.isValidClient(clientList)){
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if(orderValue.lt(minOrderValue)){
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		if(!request.isAnyProductInBrand(brands)){
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		Money brandOrderValue = request.getOrderValueForBrand(brands);
		if(brandOrderValue.lt(minOrderValue)){
			return PromotionStatusEnum.LESS_ORDER_AMOUNT_OF_BRAND_PRODUCTS;
		}
		if(!request.isProductPresent(productId)){
			return PromotionStatusEnum.PRODUCT_NOT_PRESENT;
		}
		return PromotionStatusEnum.SUCCESS;
	}

	@Override
	public Money execute(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyXBrandGetYRsOffOnZProductRuleImpl on order : " + request.getOrderId());
		}
		return request.getProductPrice(productId).minus(productDiscountValue);
	}
	
	@Override
	public List<RuleConfigDescriptorItem> getRuleConfigs() {
		List<RuleConfigDescriptorItem> ruleConfigs = new ArrayList<RuleConfigDescriptorItem>();
		
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.BRAND_LIST, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.PRODUCT_ID, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.PRODUCT_DISCOUNTED_VALUE, true));
		return ruleConfigs;
	}
}
