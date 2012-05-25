package com.fb.platform.payback.rule;

import java.math.BigDecimal;

import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.util.PointsUtil;

public interface PointsRule {
	
	public void init(RuleConfiguration ruleConfig);

	boolean isApplicable(OrderRequest orderRequest, OrderItemRequest itemRequest);

	BigDecimal execute(OrderRequest orderRequest, OrderItemRequest itemRequest);
	
	public void setPointsUtil(PointsUtil pointsUtil);
	
	boolean allowNext();
	
}