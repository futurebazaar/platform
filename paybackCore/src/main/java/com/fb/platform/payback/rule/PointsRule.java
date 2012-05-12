package com.fb.platform.payback.rule;

import com.fb.platform.payback.to.OrderRequest;

public interface PointsRule {
	
	public void init(RuleConfiguration ruleConfig);

	boolean isApplicable(OrderRequest request);

	int execute(OrderRequest request);
	
}
