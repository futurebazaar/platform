/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;
import com.fb.platform.promotion.to.PromotionStatusEnum;

/**
 * @author vinayak
 *
 */
public class BuyXGetYFreeRuleImpl implements PromotionRule, Serializable{

	private static transient Log log = LogFactory.getLog(BuyWorthXGetYRsOffRuleImpl.class);
	private Product xProduct;
	private Product yProduct;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		//String xProductId = ruleConfig.getConfigItemValue("xProd");
		//String yProductId = ruleConfig.getConfigItemValue("yProd");
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if BuyXGetYFreeRuleImpl applies on order : " + request.getOrderId());
		}
		return null;
	}

	@Override
	public Money execute(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyXGetYFreeRuleImpl on order : " + request.getOrderId());
		}
		return null;
	}
}
