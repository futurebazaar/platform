package com.fb.platform.payback.rule;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;

public interface PointsRule extends Serializable {

	public void init(RuleConfiguration ruleConfig);

	boolean isApplicable(OrderRequest orderRequest, OrderItemRequest itemRequest);

	BigDecimal execute(OrderRequest orderRequest, OrderItemRequest itemRequest);

	boolean allowNext(OrderRequest orderRequest);

}