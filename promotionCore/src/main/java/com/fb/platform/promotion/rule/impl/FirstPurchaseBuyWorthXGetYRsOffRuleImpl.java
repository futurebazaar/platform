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
import com.fb.platform.promotion.dao.OrderDao;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.RuleValidatorUtils;
import com.fb.platform.promotion.util.StringToIntegerList;

/**
 * @author keith
 *
 */
public class FirstPurchaseBuyWorthXGetYRsOffRuleImpl  implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(FirstPurchaseBuyWorthXGetYRsOffRuleImpl.class);
	
	private Money fixedRsOff;
	private Money minOrderValue = null;
	private List<Integer> clientList = null;
	private List<Integer> includeCategoryList = null;
	private List<Integer> excludeCategoryList = null;
	private List<Integer> brands;	
	private OrderDao orderDao = null;
	
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public void init(RuleConfiguration ruleConfig) {
		fixedRsOff = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF.name()))));
		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.name())){
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.name()),",");
			includeCategoryList = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
			log.info("includeCategoryList = "+ includeCategoryList);
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST.name())){
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST.name()),",");
			excludeCategoryList = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
			log.info("excludeCategoryList = "+ excludeCategoryList);
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CLIENT_LIST.name())){
			StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CLIENT_LIST.name()),",");
			clientList = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
			log.info("clientList = "+ clientList);
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.MIN_ORDER_VALUE.name())){
			minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.MIN_ORDER_VALUE.name()))));
			log.info("minOrderValue : " + minOrderValue.toString());
		}
		else{
			log.warn("Minimum Order Value not specified for this rule");
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.BRAND_LIST.name())){
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.BRAND_LIST.name()),",");
			brands = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
			log.info("brandsList = "+ brands);
		}

		//log.info("minOrderValue : " + minOrderValue.toString() + ", fixedRsOff : " + fixedRsOff.toString());
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if FirstPurchaseBuyWorthXGetYRsOffRuleImpl applies on order : " + request.getOrderId());
		}
		Money orderValue = new Money(request.getOrderValue());
		if(RuleValidatorUtils.isValidList(clientList) && !request.isValidClient(clientList)){
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if(RuleValidatorUtils.isValidList(includeCategoryList) && !request.isAllProductsInCategory(includeCategoryList)){
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if(RuleValidatorUtils.isValidList(excludeCategoryList) && request.isAnyProductInCategory(excludeCategoryList)){
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if(RuleValidatorUtils.isValidList(brands) && !request.isAllProductsInBrand(brands)){
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		if(minOrderValue!=null && orderValue.lt(minOrderValue)){
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		if(!isCouponCommitted){
			if(!orderDao.isUserFirstOrder(userId)){
				return PromotionStatusEnum.NOT_FIRST_PURCHASE;
			}
		}
		return PromotionStatusEnum.SUCCESS;
	}
	
	@Override
	public Money execute(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Executing FirstPurchaseBuyWorthXGetYRsOffRuleImpl on order : " + request.getOrderId());
		}
		return fixedRsOff;
	}
	
	@Override
	public List<RuleConfigDescriptorItem> getRuleConfigs() {
		List<RuleConfigDescriptorItem> ruleConfigs = new ArrayList<RuleConfigDescriptorItem>();
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.BRAND_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF, true));
		return ruleConfigs;
	}
	
}
