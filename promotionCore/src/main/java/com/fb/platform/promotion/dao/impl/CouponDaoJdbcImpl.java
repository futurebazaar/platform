/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUses;

/**
 * @author vinayak
 *
 */
public class CouponDaoJdbcImpl implements CouponDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(CouponDaoJdbcImpl.class);

	private static final String LOAD_COUPON_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_code, " +
			"	promotion_id, " +
			"	coupon_type " +
			"FROM coupon WHERE coupon_code = ?";

	private static final String LOAD_COUPON_LIMITS_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_id, " +
			"	max_uses, " +
			"	max_amount, " +
			"	max_uses_per_user, " +
			"	max_amount_per_user " +
			"FROM coupon_limits_config WHERE coupon_id = ?";

	private static final String LOAD_GLOBAL_COUPON_USES_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_id, " +
			"	current_count, " +
			"	current_amount " +
			"FROM global_coupon_uses WHERE coupon_id = ?";

	private static final String LOAD_USER_COUPON_USES_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_id, " +
			"	user_id, " +
			"	current_count, " +
			"	current_amount " +
			"FROM user_coupon_uses WHERE coupon_id = ? AND user_id = ?";

	@Override
	public Coupon load(String couponCode) {
		Coupon coupon = null;
		try {
			coupon = jdbcTemplate.queryForObject(LOAD_COUPON_QUERY, new Object [] {couponCode}, new CouponMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//this means we dont have any coupon with this coupon code in DB
			log.warn("Coupon not found for couponCode - " + couponCode);
			return null;
		}

		CouponLimitsConfig limitsConfig = null;
		try {
			limitsConfig = jdbcTemplate.queryForObject(LOAD_COUPON_LIMITS_QUERY, new Object[] {coupon.getId()}, new CouponLimitsConfigMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			log.error("Coupon Limits are not configured for the coupon code - " + couponCode);
			throw new PlatformException("Coupon Limits are not configured for the coupon code - " + couponCode, e);
		}
		coupon.setLimitsConfig(limitsConfig);

		return coupon;
	}

	@Override
	public GlobalCouponUses loadGlobalUses(int couponId) {
		GlobalCouponUses globalCouponUses = null;
		try {
			globalCouponUses = jdbcTemplate.queryForObject(LOAD_GLOBAL_COUPON_USES_QUERY, new Object [] {couponId}, new GlobalCouponUsesMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//no global uses set, that means this is first time use of this promotion
		}
		return globalCouponUses;
	}

	@Override
	public UserCouponUses loadUserUses(int couponId, int userId) {
		UserCouponUses userCouponUses = null;
		try {
			userCouponUses = jdbcTemplate.queryForObject(LOAD_USER_COUPON_USES_QUERY, new Object[] {couponId, userId}, new UserCouponUsesMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//no user uses set, that means this is first time use of this promotion
		}
		return userCouponUses;
	}

	@Override
	public boolean updateGlobalUses(int couponId, BigDecimal valueApplied) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUserUses(int couponId, int userId, BigDecimal valueApplied) {
		// TODO Auto-generated method stub
		return false;
	}

	private static class CouponMapper implements RowMapper<Coupon> {

		@Override
		public Coupon mapRow(ResultSet rs, int rowNum) throws SQLException {
			Coupon coupon = new Coupon();

			coupon.setId(rs.getInt("id"));
			coupon.setPromotionId(rs.getInt("promotion_id"));
			coupon.setCode(rs.getString("coupon_code"));
			coupon.setType(CouponType.valueOf(rs.getString("coupon_type")));

			return coupon;
		}
	}

	private static class CouponLimitsConfigMapper implements RowMapper<CouponLimitsConfig> {

		@Override
		public CouponLimitsConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
			CouponLimitsConfig config = new CouponLimitsConfig();

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

	private static class GlobalCouponUsesMapper implements RowMapper<GlobalCouponUses> {

		@Override
		public GlobalCouponUses mapRow(ResultSet rs, int rowNum) throws SQLException {
			GlobalCouponUses globalUses = new GlobalCouponUses();
			globalUses.setCouponId(rs.getInt("coupon_id"));
			globalUses.setCurrentCount(rs.getInt("current_count"));

			BigDecimal currentAmount = rs.getBigDecimal("current_amount");
			if (currentAmount == null) {
				currentAmount = BigDecimal.ZERO;
			}
			globalUses.setCurrentAmount(new Money(currentAmount));

			return globalUses;
		}
	}

	private static class UserCouponUsesMapper implements RowMapper<UserCouponUses> {

		@Override
		public UserCouponUses mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserCouponUses userUses = new UserCouponUses();
			userUses.setCouponId(rs.getInt("coupon_id"));
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
