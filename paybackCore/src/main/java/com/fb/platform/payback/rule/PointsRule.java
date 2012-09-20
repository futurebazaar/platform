package com.fb.platform.payback.rule;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.util.PointsUtil;

/**
 * 
 * @author anubhav
 *
 */
public interface PointsRule extends Serializable {

	public void init(RuleConfiguration ruleConfig);

	boolean isApplicable(OrderRequest orderRequest, OrderItemRequest itemRequest);

	BigDecimal execute(OrderRequest orderRequest, OrderItemRequest itemRequest);

	public void setPointsUtil(PointsUtil pointsUtil);

	/**
	 * Indicates whether next rule in the chain should be executed.
	 * @param request
	 * @param itemRequest
	 * @return
	 */
	boolean allowNext(OrderRequest request, OrderItemRequest itemRequest);

}