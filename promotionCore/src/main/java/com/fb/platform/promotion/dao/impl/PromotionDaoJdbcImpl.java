/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.model.GlobalPromotioUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.PromotionDates;
import com.fb.platform.promotion.model.PromotionLimitsConfig;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.rule.RuleConfiguration;

/**
 * @author vinayak
 *
 */
public class PromotionDaoJdbcImpl implements PromotionDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(PromotionDaoJdbcImpl.class);

	private static final String GET_PROMOTION_QUERY = 
			"SELECT " +
			"id, " +
			"valid_from, " +
			"valid_till, " +
			"name, " +
			"description, " +
			"is_coupon, " +
			"is_active " +
			"FROM promotion where id = ?";

	private static final String GET_PROMOTION_LIMITS_QUERY = 
			"SELECT " +
			"id, " +
			"promotion_id, " +
			"max_uses, " +
			"max_amount, " +
			"max_uses_per_user, " +
			"max_amount_per_user " +
			"FROM promotion_limits_config where promotion_id = ?";

	private static final String LOAD_GLOABL_PROMOTION_USES_QUERY = 
			"SELECT " +
			"id, " +
			"promotion_id, " +
			"current_count, " +
			"current_amount " +
			"FROM global_promotion_uses WHERE promotion_id = ?";

	private static final String LOAD_USER_PROMOTION_USES_QUERY = 
			"SELECT " +
			"id, " +
			"promotion_id, " +
			"user_id, " +
			"current_count, " +
			"current_amount " +
			"FROM user_promotion_uses WHERE promotion_id = ? AND user_id = ?";

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#load(int)
	 */
	@Override
	public Promotion load(int promotionId) {
		Promotion promotion = jdbcTemplate.queryForObject(GET_PROMOTION_QUERY, new Object [] {promotionId}, new PromotionMapper());

		PromotionLimitsConfig limits = null;
		try {
			limits = jdbcTemplate.queryForObject(GET_PROMOTION_LIMITS_QUERY, new Object [] {promotionId}, new PromotionLimitsConfigMapper());
		} catch (EmptyResultDataAccessException e) {
			log.error("Promotion Limits are not configured for the promotion id - " + promotionId, e);
			throw new PlatformException("Promotion Limits are not configured for the promotion id - " + promotionId, e);
		}
		promotion.setLimitsConfig(limits);

		return promotion;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#loadGlobalUses(int)
	 */
	@Override
	public GlobalPromotioUses loadGlobalUses(int promotionId) {
		GlobalPromotioUses globalPromotioUses = null;
		try {
			globalPromotioUses = jdbcTemplate.queryForObject(LOAD_GLOABL_PROMOTION_USES_QUERY, new Object [] {promotionId}, new GlobalPromotionUsesMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//no default global uses set, that means this is first time use of this promotion
			/*globalPromotioUses = new GlobalPromotioUses();
			globalPromotioUses.setPromotionId(promotionId);
			globalPromotioUses.setCurrentAmount(new Money(BigDecimal.ZERO));*/
		}
		return globalPromotioUses;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#loadUserUses(int, int)
	 */
	@Override
	public UserPromotionUses loadUserUses(int promotionId, int userId) {
		UserPromotionUses userPromotionUses = null;
		try {
			userPromotionUses = jdbcTemplate.queryForObject(LOAD_USER_PROMOTION_USES_QUERY, new Object[] {promotionId, userId}, new UserPromotionUsesMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//no default user uses set, that means this is first time use of this promotion
			/*userPromotionUses = new UserPromotionUses();
			userPromotionUses.setCurrentAmount(new Money(BigDecimal.ZERO));
			userPromotionUses.setPromotionId(promotionId);
			userPromotionUses.setUserId(userId);*/
		}
		return userPromotionUses;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#loadRuleConfiguration(int)
	 */
	@Override
	public RuleConfiguration loadRuleConfiguration(int promotionId) {
		// TODO Auto-generated method stub
		return null;
	}

	private static class PromotionMapper implements RowMapper<Promotion> {

		@Override
		public Promotion mapRow(ResultSet rs, int rowNum) throws SQLException {
			Promotion promotion = new Promotion();
			promotion.setDescription(rs.getString("description"));
			promotion.setId(rs.getInt("id"));
			promotion.setName(rs.getString("name"));
			promotion.setActive(rs.getBoolean("is_active"));

			PromotionDates dates = new PromotionDates();
			Timestamp validFromTS = rs.getTimestamp("valid_from");
			if (validFromTS != null) {
				dates.setValidFrom(new DateTime(validFromTS));
			}
			Timestamp validTillTS = rs.getTimestamp("valid_till");
			if (validTillTS != null) {
				dates.setValidTill(new DateTime(validTillTS));
			}
			promotion.setDates(dates);
			
			return promotion;
		}
	}

	private static class PromotionLimitsConfigMapper implements RowMapper<PromotionLimitsConfig> {

		@Override
		public PromotionLimitsConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
			PromotionLimitsConfig config = new PromotionLimitsConfig();

			BigDecimal maxAmountBD = rs.getBigDecimal("max_amount");
			if (maxAmountBD != null) {
				config.setMaxAmount(new Money(maxAmountBD));
			}

			BigDecimal maxAmountPerUserBD = rs.getBigDecimal("max_amount_per_user");
			if (maxAmountPerUserBD != null) {
				config.setMaxAmountPerUser(new Money(maxAmountPerUserBD));
			}

			config.setMaxUses(rs.getInt("max_uses"));
			config.setMaxUsesPerUser(rs.getInt("max_uses_per_user"));

			return config;
		}
	}

	private static class GlobalPromotionUsesMapper implements RowMapper<GlobalPromotioUses> {

		@Override
		public GlobalPromotioUses mapRow(ResultSet rs, int rowNum) throws SQLException {
			GlobalPromotioUses globalUses = new GlobalPromotioUses();
			globalUses.setPromotionId(rs.getInt("promotion_id"));
			globalUses.setCurrentCount(rs.getInt("current_count"));

			BigDecimal currentAmount = rs.getBigDecimal("current_amount");
			if (currentAmount == null) {
				currentAmount = BigDecimal.ZERO;
			}
			globalUses.setCurrentAmount(new Money(currentAmount));

			return globalUses;
		}
	}

	private static final class UserPromotionUsesMapper implements RowMapper<UserPromotionUses> {

		@Override
		public UserPromotionUses mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserPromotionUses userUses = new UserPromotionUses();
			userUses.setPromotionId(rs.getInt("promotion_id"));
			userUses.setUserId(rs.getInt("user_id"));
			userUses.setCurrentCount(rs.getInt("current_count"));

			BigDecimal currentAmount = rs.getBigDecimal("current_amount");
			if (currentAmount == null) {
				currentAmount = BigDecimal.ZERO;
			}
			userUses.setCurrentAmount(new Money(currentAmount));

			return userUses;
		}
	}

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
