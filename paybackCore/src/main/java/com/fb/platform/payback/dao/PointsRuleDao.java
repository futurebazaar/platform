package com.fb.platform.payback.dao;

import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.RuleConfiguration;


public interface PointsRuleDao {

	public PointsRule loadEarnRule(EarnPointsRuleEnum ruleName);

	public RuleConfiguration loadRuleConfiguration(long ruleId);
}
