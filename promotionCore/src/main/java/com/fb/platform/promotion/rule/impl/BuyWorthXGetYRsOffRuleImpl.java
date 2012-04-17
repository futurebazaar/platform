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
public class BuyWorthXGetYRsOffRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(BuyWorthXGetYRsOffRuleImpl.class);
	private Money minOrderValue;
	private Money fixedRsOff;
	private List<Integer> client_list;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue("MIN_ORDER_VALUE"))));
		fixedRsOff = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue("FIXED_DISCOUNT_RS_OFF"))));
		StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigConstants.CLIENT_LIST),",");
		client_list = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
		log.info("minOrderValue : " + minOrderValue.toString() + ", fixedRsOff : " + fixedRsOff.toString());
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if BuyWorthXGetYRsOffRuleImpl applies on order : " + request.getOrderId());
		}
		ApplicableResponse ar = new ApplicableResponse();
		Money orderValue = new Money(request.getOrderValue());
		if(request.isValidClient(client_list)){
			if(orderValue.gteq(minOrderValue)){
				return PromotionStatusEnum.SUCCESS;
			}
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		return PromotionStatusEnum.INVALID_CLIENT;
	}

	@Override
	public Money execute(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyWorthXGetYRsOffRuleImpl on order : " + request.getOrderId());
		}
		return fixedRsOff;
	}
}
