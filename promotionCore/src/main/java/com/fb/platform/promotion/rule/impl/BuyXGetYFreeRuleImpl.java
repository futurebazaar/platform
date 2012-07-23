/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.metadata.BuyXGetYFreeRuleMetadata;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;
import com.fb.platform.promotion.to.PromotionStatusEnum;

/**
 * @author vinayak
 * 
 */
public class BuyXGetYFreeRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory
			.getLog(BuyWorthXGetYRsOffRuleImpl.class);
	private Product xProduct;
	private Product yProduct;

	@Override
	public void init(RuleConfiguration ruleConfig) {
		// String xProductId = ruleConfig.getConfigItemValue("xProd");
		// String yProductId = ruleConfig.getConfigItemValue("yProd");
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request, int userId,
			boolean isCouponCommitted) {
		if (log.isDebugEnabled()) {
			log.debug("Checking if BuyXGetYFreeRuleImpl applies on order : "
					+ request.getOrderId());
		}
		return null;
	}

	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {
		OrderRequest request = orderDiscount.getOrderRequest();
		if (log.isDebugEnabled()) {
			log.debug("Executing BuyXGetYFreeRuleImpl on order : "
					+ request.getOrderId());
		}
		return null;
	}

	@Override
	public RuleConfigDescriptor getRuleConfigs() {
		RuleConfigDescriptor ruleConfigDescriptor = new RuleConfigDescriptor();

		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, false));
		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, false));
		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.BRAND_LIST, false));
		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));

		return ruleConfigDescriptor;
	}

	@Override
	public RuleConfigMetadata getRuleConfigMetadata() {
		return new BuyXGetYFreeRuleMetadata();
	}
}
