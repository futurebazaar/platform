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
public class BuyWorthXGetYPercentOffRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(BuyWorthXGetYPercentOffRuleImpl.class);
	private Money minOrderValue;
	private BigDecimal discountPercentage;
	private Money maxDiscountPerUse;
	private List<Integer> client_list;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.MIN_ORDER_VALUE))));
		discountPercentage = BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.DISCOUNT_PERCENTAGE)));
		maxDiscountPerUse = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.MAX_DISCOUNT_CEIL_IN_VALUE))));
		StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigConstants.CLIENT_LIST),",");
		client_list = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
		log.info("minOrderValue : " + minOrderValue.toString() + ", discountPercentage : " + discountPercentage.toString() + " ,maxDiscountPerUse" + maxDiscountPerUse.toString());
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if BuyWorthXGetYPercentOffRuleImpl applies on order : " + request.getOrderId());
		}
		Money orderValue = new Money(request.getOrderValue());
		if(request.isValidClient(client_list)){
			if(orderValue.gteq(minOrderValue)){
				return PromotionStatusEnum.SUCCESS;
			}
			return PromotionStatusEnum.SUCCESS;
		}
		return PromotionStatusEnum.INVALID_CLIENT;
	}

	@Override
	public Money execute(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyWorthXGetYPercentOffRuleImpl on order : " + request.getOrderId());
		}
		Money orderVal = new Money(request.getOrderValue());
		Money discountAmount = (orderVal.times(discountPercentage.doubleValue())).div(100); 
		if(discountAmount.gteq(maxDiscountPerUse)){
			return maxDiscountPerUse;
		}
		else{
			return discountAmount;
		}
	}
}
