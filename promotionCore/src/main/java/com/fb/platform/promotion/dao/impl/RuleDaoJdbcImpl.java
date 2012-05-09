/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigItem;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.util.PromotionRuleFactory;

/**
 * @author vinayak
 *
 */
public class RuleDaoJdbcImpl implements RuleDao {

	private Log log = LogFactory.getLog(PromotionDaoJdbcImpl.class);

	private JdbcTemplate jdbcTemplate;

	private static final String LOAD_PROMOTION_RULE_QUERY = 
			"SELECT " +
			"	id, " +
			"	name " +
			"FROM promotion_rule WHERE id = ?";

	private static final String LOAD_RULE_CONFIG_ITEMS_QUERY = 
			"SELECT " +
			"	id, " +
			"	name, " +
			"	value, " +
			"	promotion_id, " +
			"	rule_id " +
			"FROM promotion_rule_config WHERE promotion_id = ?";
	/**
	 * QUERY
	 * SELECT id,name FROM promotion_rule
	 */
	private static final String LOAD_ALL_PROMOTION_RULES =
			"SELECT " +
			"	id," +
			"	name " +
			"FROM promotion_rule ";
	
	/**
	 * SELECT id FROM promotion_rule where name = 'BUY_X_GET_Y_FREE'
	 */
	
	private static final String GET_RULE_ID = 
			"SELECT " +
			"	id " +
			"FROM " +
			"	promotion_rule " +
			"WHERE " +
			"	name = ?";

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.RuleDao#load(int, int)
	 */
	@Override
	public PromotionRule load(int promotionId, int ruleId) {
		if(log.isDebugEnabled()) {
			log.debug("Geting the promotion rule details for the rule id : " + ruleId );
		}
		PromotionRuleRowCallBackHandler rcbh = new PromotionRuleRowCallBackHandler();
		jdbcTemplate.query(LOAD_PROMOTION_RULE_QUERY, rcbh, ruleId);

		if (!rcbh.ruleFound) {
			return null;
		}

		RulesEnum ruleName = RulesEnum.valueOf(rcbh.ruleName);

		RuleConfiguration ruleConfig = loadRuleConfiguration(promotionId, ruleId);

		PromotionRule rule = PromotionRuleFactory.createRule(ruleName, ruleConfig);

		return rule;
	}
	
	@Override
	public List<RulesEnum> getAllPromotionRules() {
		List<RulesEnum> promotionRulesList = jdbcTemplate.query(LOAD_ALL_PROMOTION_RULES, new PromotionAllRulesRowCallBackHandler());
		return promotionRulesList;
	}
	
	@Override
	public int getRuleId(String ruleName) {
		int ruleId = jdbcTemplate.queryForInt(GET_RULE_ID, ruleName);
		return ruleId;
	}

	public RuleConfiguration loadRuleConfiguration(int promotionId, int ruleId) {
		if(log.isDebugEnabled()) {
			log.debug("Geting the promotion rule details for the promotion rule id : " + promotionId );
		}
		List<RuleConfigItem> ruleConfigItems = jdbcTemplate.query(LOAD_RULE_CONFIG_ITEMS_QUERY, new RuleConfigItemRowMapper(), promotionId);
		RuleConfiguration ruleConfig = new RuleConfiguration(ruleConfigItems);
		return ruleConfig;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private class PromotionRuleRowCallBackHandler implements RowCallbackHandler {

		private boolean ruleFound = false;
		//private int ruleId = 0;
		private String ruleName = null;

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			//ruleId = rs.getInt("id");
			ruleName = rs.getString("name");
			ruleFound = true;
		}
	}
	
	private class PromotionAllRulesRowCallBackHandler implements RowMapper<RulesEnum> {
		@Override
		public RulesEnum mapRow(ResultSet rs, int rowNum) throws SQLException {
			return RulesEnum.valueOf(rs.getString("name"));
		}
	}

	private class RuleConfigItemRowMapper implements RowMapper<RuleConfigItem> {

		@Override
		public RuleConfigItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			RuleConfigItem configItem = new RuleConfigItem(rs.getString("name"), rs.getString("value"));
			return configItem;
		}
	}
}
