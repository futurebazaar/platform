package com.fb.platform.payback.dao;

import com.fb.platform.payback.rule.BurnPointsRuleEnum;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;

public interface PointsRuleDao {

	public PointsRule loadEarnRule(EarnPointsRuleEnum ruleName, String clientName);

	public PointsRule loadBurnRule(BurnPointsRuleEnum ruleName, String clientName);
}
