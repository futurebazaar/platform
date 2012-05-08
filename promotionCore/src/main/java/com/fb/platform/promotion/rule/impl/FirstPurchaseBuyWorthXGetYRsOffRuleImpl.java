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
import com.fb.platform.promotion.util.StringToIntegerList;

/**
 * @author keith
 *
 */
public class FirstPurchaseBuyWorthXGetYRsOffRuleImpl  implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(FirstPurchaseBuyWorthXGetYRsOffRuleImpl.class);
	private Money minOrderValue;
	private Money fixedRsOff;
	private List<Integer> clientList;
	private List<Integer> categories;
	
	private OrderDao orderDao = null;
	
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.MIN_ORDER_VALUE.name()))));
		fixedRsOff = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF.name()))));
		StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CATEGORY_LIST.name()),",");
		categories = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CLIENT_LIST.name()),",");
		clientList = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
		log.info("minOrderValue : " + minOrderValue.toString() + ", fixedRsOff : " + fixedRsOff.toString());
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if FirstPurchaseBuyWorthXGetYRsOffRuleImpl applies on order : " + request.getOrderId());
		}
		ApplicableResponse ar = new ApplicableResponse();
		Money orderValue = new Money(request.getOrderValue());
		if(!request.isValidClient(clientList)){
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if(!request.isAllProductsInCategory(categories)){
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if(orderValue.lt(minOrderValue)){
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
		
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_LIST, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.BRAND_LIST, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, true));
		
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CLIENT_LIST, true));
		return ruleConfigs;
	}
	
}
