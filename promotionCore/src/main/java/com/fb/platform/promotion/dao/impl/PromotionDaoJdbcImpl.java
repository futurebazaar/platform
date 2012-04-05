/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import org.joda.time.DateTime;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.PromotionDates;
import com.fb.platform.promotion.model.PromotionLimitsConfig;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.rule.PromotionRule;

/**
 * @author vinayak
 *
 */
public class PromotionDaoJdbcImpl implements PromotionDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(PromotionDaoJdbcImpl.class);

	private RuleDao ruleDao = null;

	private static final String GET_PROMOTION_QUERY = 
			"SELECT " +
			"	id, " +
			"	valid_from, " +
			"	valid_till, " +
			"	name, " +
			"	description, " +
			"	is_coupon, " +
			"	is_active," +
			"	rule_id " +
			"FROM platform_promotion where id = ?";

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
			"	count(*) as current_count, " +
			"	sum(upu.discount_amount) as current_amount, " +
			"	promotion_id " +
			"FROM user_promotion_uses upu WHERE promotion_id = ? AND is_cancelled = 0";
	
	private static final String LOAD_USER_PROMOTION_USES_QUERY = 
			"SELECT " +
			"	count(*) as current_count, " +
			"	sum(upu.discount_amount) as current_amount, " +
			"	promotion_id, " +
			"	user_id " +
			"FROM user_promotion_uses upu WHERE promotion_id = ? AND user_id = ? AND is_cancelled = 0";

	private static final String CREATE_USER_USES = 
			"INSERT INTO user_promotion_uses " +
			"	(promotion_id, " +
			"	user_id, " +
			"	order_id, " +
			"	discount_amount, " +
			"	created_on, " +
			"	last_modified_on, " +
			"	is_cancelled) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String CANCEL_USER_USES = 
			"UPDATE user_promotion_uses set " +
			"	is_cancelled = true, " +
			"	last_modified_on = ? " +
			"where promotion_id = ? AND user_id = ? AND order_id = ?";

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#load(int)
	 */
	@Override
	public Promotion load(int promotionId) {
		
		if(log.isDebugEnabled()) {
			log.debug("Geting the details for the promotion id : " + promotionId);
		}
		
		Promotion promotion = null;

		PromotionRowCallBackHandler prcbh = new PromotionRowCallBackHandler(); 
		jdbcTemplate.query(GET_PROMOTION_QUERY, prcbh, promotionId);
		if (prcbh.promotion == null) {
			//this means there is no promotion in the database for this id, fine
			log.error("No Promotion found for the id - " + promotionId);
			return null;
		}
		promotion = prcbh.promotion;

		PromotionLimitsConfig limits = null;
		try {
			limits = jdbcTemplate.queryForObject(GET_PROMOTION_LIMITS_QUERY, new Object [] {promotionId}, new PromotionLimitsConfigMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			log.error("Promotion Limits are not configured for the promotion id - " + promotionId);
			throw new PlatformException("Promotion Limits are not configured for the promotion id - " + promotionId, e);
		}
		promotion.setLimitsConfig(limits);

		PromotionRule rule = ruleDao.load(promotionId, prcbh.ruleId);

		promotion.setRule(rule);

		return promotion;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#loadGlobalUses(int)
	 */
	@Override
	public GlobalPromotionUses loadGlobalUses(int promotionId) {
		if(log.isDebugEnabled()) {
			log.debug("Getting the global promotion usage for the promotion id : " + promotionId );
		}
		
		GlobalPromotionUses globalPromotionUses = null;
		try {
			globalPromotionUses = jdbcTemplate.queryForObject(LOAD_GLOABL_PROMOTION_USES_QUERY, new Object [] {promotionId}, new GlobalPromotionUsesMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//no global uses set, that means this is first time use of this promotion
		}
		return globalPromotionUses;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#loadUserUses(int, int)
	 */
	@Override
	public UserPromotionUses loadUserUses(int promotionId, int userId) {
		if(log.isDebugEnabled()) {
			log.debug("Getting the promotion usage for the promotion id : " + promotionId + " ,by the user : " + userId);
		}
		
		UserPromotionUses userPromotionUses = null;
		try {
			userPromotionUses = jdbcTemplate.queryForObject(LOAD_USER_PROMOTION_USES_QUERY, new Object[] {promotionId, userId}, new UserPromotionUsesMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//no user uses set, that means this is first time use of this promotion
		}
		return userPromotionUses;
	}

/*	@Override
	public boolean updateGlobalUses(int promotionId, BigDecimal valueApplied) {
		GlobalPromotionUses globalUses = loadGlobalUses(promotionId);
		if (globalUses == null) {
			//first time use of the coupon, create a new object
			createGlobalUses(promotionId, valueApplied);
		} else {
			incrementGlobalUses(promotionId, valueApplied);
		}
		return true;
	}*/

/*	private void incrementGlobalUses(int promotionId, BigDecimal valueApplied) {
		int update = jdbcTemplate.update(INCREASE_GLOBAL_USES, valueApplied, promotionId);
		if (update != 1) {
			throw new PlatformException("Unable to update the global promotion uses for promotionId : " + promotionId);
		}
	}

	private void createGlobalUses(final int promotionId, final BigDecimal valueApplied) {
		KeyHolder globalUsesKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement psgu = con.prepareStatement(CREATE_GLOBAL_USES, new String [] {"id"});
				psgu.setInt(1, promotionId);
				psgu.setInt(2, 1);
				psgu.setBigDecimal(3, valueApplied);
				return psgu;
			}
		}, globalUsesKeyHolder);
	}*/

	@Override
	public boolean updateUserUses(int promotionId, int userId, BigDecimal valueApplied, int orderId) {
		
		//for every use of the coupon, create a new object
		createUserUses(promotionId, userId, valueApplied, orderId);
		
		/*UserPromotionUses userUses = loadUserUses(promotionId, userId);
		if (userUses == null) {
			//first time use of the coupon, create a new object
			createUserUses(promotionId, userId, valueApplied, orderId);
		} else {
			incrementUserUses(promotionId, userId, valueApplied);
		}*/
		return true;
	}

/*	private void incrementUserUses(int promotionId, int userId, BigDecimal valueApplied) {
		int update = jdbcTemplate.update(INCREASE_USER_USES, valueApplied, promotionId, userId);
		if (update != 1) {
			throw new PlatformException("Unable to update the user coupon uses for couponId : " + promotionId + " and userId : " + userId);
		}
	}*/

	private void createUserUses(final int promotionId, final int userId, final BigDecimal valueApplied, final int orderId) {
		
		if(log.isDebugEnabled()) {
			log.debug("Insert in the user_promotion_uses table record for user : " + userId + " , applied promotion id : " + promotionId + " , on order id : " + orderId + " , discount value applied : " + valueApplied );
		}
		
		KeyHolder userUsesKeyHolder = new GeneratedKeyHolder();
		final java.util.Date today = new java.util.Date();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(CREATE_USER_USES, new String [] {"id"});
				ps.setInt(1, promotionId);
				ps.setInt(2, userId);
				ps.setInt(3, orderId);
				ps.setBigDecimal(4, valueApplied);
				ps.setDate(5, new Date(today.getTime()));
				ps.setDate(6, new Date(today.getTime()));
				ps.setBoolean(7, false);
				return ps;
			}
		}, userUsesKeyHolder);
	}

	@Override
	public boolean cancelUserUses(final int promotionId, final int userId, final int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Cancelling the promotion id : " + promotionId + ", applied on order id : " + orderId + " for user : " + userId );
		}
		int rowAffected = -1;
		final java.util.Date today = new java.util.Date();
		try {
			rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(CANCEL_USER_USES);
					ps.setDate(1, new Date(today.getTime()));
					ps.setInt(2, promotionId);
					ps.setInt(3, userId);
					ps.setInt(4, orderId);
					return ps;
				}
			});
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn( " Tried cancelling promotion id " + promotionId + " ,but entry not found.");
			//failed to update the row
		}
		
		return rowAffected>0 ? true : false;
	}

	private static class PromotionRowCallBackHandler implements RowCallbackHandler {

		private int ruleId;
		private Promotion promotion;

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			promotion = new Promotion();
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

			ruleId = rs.getInt("rule_id");
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

	private static class GlobalPromotionUsesMapper implements RowMapper<GlobalPromotionUses> {

		@Override
		public GlobalPromotionUses mapRow(ResultSet rs, int rowNum) throws SQLException {
			GlobalPromotionUses globalUses = new GlobalPromotionUses();
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

	private static class UserPromotionUsesMapper implements RowMapper<UserPromotionUses> {

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

	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
}
