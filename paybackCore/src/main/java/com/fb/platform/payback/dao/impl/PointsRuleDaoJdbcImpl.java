package com.fb.platform.payback.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.payback.dao.PointsRuleDao;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.RuleConfiguration;

public class PointsRuleDaoJdbcImpl implements PointsRuleDao{
	
	private Log log = LogFactory.getLog(PointsRuleDaoJdbcImpl.class);

	private static String LOAD_RULE_CONFIG_ITEMS_QUERY = 
			"SELECT name, value FROM " +
			"promotion_rule_config WHERE " +
			"rule_id = ?";
	
	private static String GET_RULE_QUERY = 
			"SELECT * FROM " +
			"promotion_rule WHERE " +
			"name = ?";
	
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public PointsRule load(int ruleId) {
		return null;
	}

	@Override
	public RuleConfiguration loadRuleConfiguration(int ruleId) {
		return null;
	}
	

}
