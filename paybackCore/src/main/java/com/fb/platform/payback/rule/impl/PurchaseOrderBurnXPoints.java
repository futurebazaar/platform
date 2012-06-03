package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.util.PointsUtil;

public class PurchaseOrderBurnXPoints implements PointsRule {
	private BigDecimal burnRatio;
	private PointsUtil pointsUtil;
	
	@Override
	public void setPointsUtil(PointsUtil pointsUtil) {
		this.pointsUtil = pointsUtil;
	}

	@Override
	public void init(RuleConfiguration ruleConfig) {
		this.burnRatio = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.BURN_RATIO));
	}

	@Override
	public boolean isApplicable(OrderRequest request, OrderItemRequest itemRequest) {
		return true;
	}

	@Override
	public BigDecimal execute(OrderRequest request, OrderItemRequest itemRequest) {
		return burnRatio.multiply(request.getAmount());
	}

	@Override
	public boolean allowNext() {
		return true;
	}

}
