/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.PromotionAdminDao;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.RulesEnum;

/**
 * @author vinayak
 *
 */
public class PromotionAdminDaoJdbcImpl implements PromotionAdminDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(PromotionDaoJdbcImpl.class);

	private static final String CREATE_PROMOTION_SQL = 
			"INSERT INTO " +
			"	platform_promotion " +
			"		(created_on, " +
			"		last_modified_on, " +
			"		rule_id, " +
			"		valid_from, " +
			"		valid_till, " +
			"		name, " +
			"		description, " +
			"		is_active) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String CREATE_RULE_CONFIG = "";

	@Override
	public int create(String name, String description, DateTime validFrom,
			DateTime validTill, int maxUses, Money maxAmount,
			int maxUsesPerUser, Money maxAmountPerUser, RulesEnum rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int createRuleConfiguration(int promotionId, int ruleId,
			RuleConfiguration ruleConfiguration) {
		// TODO Auto-generated method stub
		return 0;
	}

}
