/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.promotion.dao.OrderDao;
import com.fb.platform.promotion.dao.PromotionDao;


import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigConstants;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
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
		fixedRsOff = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.FIXED_DISCOUNT_RS_OFF))));
		if(ruleConfig.isConfigItemPresent(RuleConfigConstants.CATEGORY_INCLUDE_LIST)){
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigConstants.CATEGORY_INCLUDE_LIST),",");
			includeCategoryList = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigConstants.CATEGORY_EXCLUDE_LIST)){
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigConstants.CATEGORY_EXCLUDE_LIST),",");
			excludeCategoryList = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigConstants.CLIENT_LIST)){
			StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigConstants.CLIENT_LIST),",");
			clientList = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigConstants.MIN_ORDER_VALUE)){
			minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.MIN_ORDER_VALUE))));
			log.info("minOrderValue : " + minOrderValue.toString());
		}
		else{
			log.warn("Minimum Order Value not specified for this rule");
		}
		if(ruleConfig.isConfigItemPresent(RuleConfigConstants.BRAND_LIST)){
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigConstants.BRAND_LIST),",");
			brands = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		}
		log.info("minOrderValue : " + minOrderValue.toString() + ", fixedRsOff : " + fixedRsOff.toString());
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if FirstPurchaseBuyWorthXGetYRsOffRuleImpl applies on order : " + request.getOrderId());
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
	
	
}
