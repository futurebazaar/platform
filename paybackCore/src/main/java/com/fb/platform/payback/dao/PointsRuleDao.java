package com.fb.platform.payback.dao;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.RuleConfiguration;


public interface PointsRuleDao {

	public PointsRule load(int ruleId);

	public RuleConfiguration loadRuleConfiguration(int ruleId);
}
