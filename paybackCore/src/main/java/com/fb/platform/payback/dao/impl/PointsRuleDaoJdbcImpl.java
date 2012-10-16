package com.fb.platform.payback.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.PlatformException;
import com.fb.platform.payback.dao.PointsRuleDao;
import com.fb.platform.payback.rule.BurnPointsRuleEnum;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.RuleConfigItem;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.rule.impl.PointsRuleFactory;

public class PointsRuleDaoJdbcImpl implements PointsRuleDao {

	private Log log = LogFactory.getLog(PointsRuleDaoJdbcImpl.class);

	private static String LOAD_RULE_CONFIG_ITEMS_QUERY = "SELECT name, value FROM "
			+ "payback_rule_config WHERE " + 
			"rule_id = ? AND client_name = ? " ;

	private static String LOAD_RULE_QUERY = "SELECT * FROM " + "rules WHERE "
			+ "name = ? ";

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public PointsRule loadEarnRule(EarnPointsRuleEnum ruleName, String clientName) {
		if (log.isDebugEnabled()) {
			log.debug("Geting the payback rule details for the rule id : " + ruleName);
		}
		PointsRuleRowCallBackHandler rcbh = new PointsRuleRowCallBackHandler();
		jdbcTemplate.query(LOAD_RULE_QUERY, rcbh, ruleName.name());

		if (!rcbh.ruleFound) {
			return null;
		}

		EarnPointsRuleEnum ruleEnum = EarnPointsRuleEnum.valueOf(rcbh.ruleName);

		RuleConfiguration ruleConfig = loadRuleConfiguration(rcbh.ruleId, clientName);

		PointsRule rule = PointsRuleFactory.createRule(ruleEnum, ruleConfig, clientName);

		return rule;
	}

	private class PointsRuleRowCallBackHandler implements RowCallbackHandler {

		private boolean ruleFound = false;
		private long ruleId = 0;
		private String ruleName = null;

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			ruleName = rs.getString("name");
			ruleId = rs.getLong("id");
			ruleFound = true;
		}
	}

	private RuleConfiguration loadRuleConfiguration(long ruleId, String clientName) {
		if (log.isDebugEnabled()) {
			log.debug("Geting the rule details for the Payback rule id : "
					+ ruleId);
		}
		List<com.fb.platform.payback.rule.RuleConfigItem> ruleConfigItems = jdbcTemplate
				.query(LOAD_RULE_CONFIG_ITEMS_QUERY, new Object[] {ruleId, clientName}, 
						new RuleConfigItemRowMapper());
		if (ruleConfigItems == null || ruleConfigItems.isEmpty()){
			throw new PlatformException("No Rule Config Items found for Rule Id: " + ruleId + " and clientName: " + clientName);
		}
		RuleConfiguration ruleConfig = new RuleConfiguration(ruleConfigItems);
		return ruleConfig;
	}

	private class RuleConfigItemRowMapper implements RowMapper<RuleConfigItem> {

		@Override
		public RuleConfigItem mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			RuleConfigItem configItem = new RuleConfigItem(
					rs.getString("name"), rs.getString("value"));
			return configItem;
		}
	}

	@Override
	public PointsRule loadBurnRule(BurnPointsRuleEnum ruleName, String clientName) {
		if (log.isDebugEnabled()) {
			log.debug("Geting the Payback rule details for the rule id : "
					+ ruleName);
		}
		PointsRuleRowCallBackHandler rcbh = new PointsRuleRowCallBackHandler();
		jdbcTemplate.query(LOAD_RULE_QUERY, rcbh, ruleName.name());

		if (!rcbh.ruleFound) {
			return null;
		}

		BurnPointsRuleEnum ruleEnum = BurnPointsRuleEnum.valueOf(rcbh.ruleName);

		RuleConfiguration ruleConfig = loadRuleConfiguration(rcbh.ruleId, clientName);
		if (ruleConfig == null){
			return null;
		}

		PointsRule rule = PointsRuleFactory.createRule(ruleEnum, ruleConfig, clientName);

		return rule;
	}

}
