package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;

public class CancelOrderReverseXBurnPoints implements PointsRule{
	private BigDecimal earnRatio;

	@Override
	public void init(RuleConfiguration ruleConfig) {
		this.earnRatio = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_RATIO));
	}

	@Override
	public boolean isApplicable(OrderRequest request, OrderItemRequest itemRequest) {
		return true;
	}

	@Override
	public BigDecimal execute(OrderRequest request, OrderItemRequest itemRequest) {
		return earnRatio.multiply(itemRequest.getAmount());
	}

	@Override
	public boolean allowNext() {
		return true;
	}

}
