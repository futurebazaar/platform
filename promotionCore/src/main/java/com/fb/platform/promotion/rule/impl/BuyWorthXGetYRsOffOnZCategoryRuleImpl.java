/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigConstants;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.util.StringToIntegerList;
import com.fb.commons.to.Money;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author keith
 *
 */
public class BuyWorthXGetYRsOffOnZCategoryRuleImpl implements PromotionRule, Serializable {

	private Log log = LogFactory.getLog(BuyWorthXGetYRsOffOnZCategoryRuleImpl.class);
	private Money minOrderValue;
	private Money fixedRsOff;
	private List<Integer> categories;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.MIN_ORDER_VALUE))));
		fixedRsOff = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.FIXED_DISCOUNT_RS_OFF))));
		StrTokenizer strTok = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigConstants.CATEGORY_LIST),",");
		categories = StringToIntegerList.convert((List<String>)strTok.getTokenList());
		log.info("minOrderValue : " + minOrderValue.toString() + ", fixedRsOff : " + fixedRsOff.toString());
	}

	@Override
	public boolean isApplicable(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if BuyWorthXGetYRsOffOnZCategoryRuleImpl applies on order : " + request.getOrderId());
		}
		Money orderValue = new Money(request.getOrderValue());
		if(orderValue.gteq(minOrderValue) && request.isAllProductsInCategory(categories)){
			return true;
		}
		return false;
	}

	@Override
	public Money execute(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyWorthXGetYRsOffOnZCategoryRuleImpl on order : " + request.getOrderId());
		}
		return fixedRsOff;
	}
}
