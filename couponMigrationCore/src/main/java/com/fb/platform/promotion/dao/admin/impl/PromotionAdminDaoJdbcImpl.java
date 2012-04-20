/**
 * 
 */
package com.fb.platform.promotion.dao.admin.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.admin.PromotionAdminDao;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.UserPromotionUsesEntry;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.UserCouponUsesEntry;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.rule.RuleConfigItem;
import com.fb.platform.promotion.rule.RuleConfiguration;

/**
 * @author vinayak
 *
 */
public class PromotionAdminDaoJdbcImpl implements PromotionAdminDao {

	private JdbcTemplate jdbcTemplate;

	private Log infoLog = LogFactory.getLog("LOGINFO");

	private Log errorLog = LogFactory.getLog("LOGERROR");

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
	
	private static final String CREATE_COUPON_SQL = 
			"INSERT INTO " +
			"	coupon " +
			"		(created_on, " +
			"		last_modified_on, " +
			"		coupon_code, " +
			"		promotion_id, " +
			"		coupon_type) " +
			"VALUES (?, ?, ?, ?, ?)";


	private static final String CREATE_PROMOTION_RULE_CONFIG = 
			"INSERT INTO " +
					"	promotion_rule_config " +
					"		(name, " +
					"		value, " +
					"		promotion_id, " +
					"		rule_id) " +
					"VALUES (?, ?, ?, ?)";
	
	private static final String CREATE_PROMOTION_LIMIT_CONFIG_SQL = 
			"INSERT INTO " +
			"	promotion_limits_config " +
			"		(max_uses, " +
			"		max_amount, " +
			"		max_uses_per_user, " +
			"		max_amount_per_user, " +
			"		promotion_id) " +
			"VALUES (?, ?, ?, ?, ?)";
	
	private static final String CREATE_COUPON_LIMIT_CONFIG_SQL = 
			"INSERT INTO " +
			"	coupon_limits_config " +
			"		(max_uses, " +
			"		max_amount, " +
			"		max_uses_per_user, " +
			"		max_amount_per_user, " +
			"		coupon_id) " +
			"VALUES (?, ?, ?, ?, ?)";

	private static final String CREATE_USER_PROMOTION_USES_SQL = 
			"INSERT INTO " +
			"	user_promotion_uses " +
			"		(promotion_id, " +
			"		user_id, " +
			"		order_id, " +
			"		discount_amount, " +
			"		created_on, " +
			"		last_modified_on) " +
			"VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String CREATE_USER_COUPON_USES_SQL = 
			"INSERT INTO " +
			"	user_coupon_uses " +
			"		(coupon_id, " +
			"		user_id, " +
			"		order_id, " +
			"		discount_amount, " +
			"		created_on, " +
			"		last_modified_on) " +
			"VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String CREATE_COUPON_USER_MAPPING_SQL = 
			"INSERT INTO " +
			"	platform_coupon_user " +
			"		(coupon_id, " +
			"		user_id, " +
			"		override_user_uses_limit) " +
			"VALUES (?, ?, ?)";
	
	@Override
	public void createPromotion(Promotion promotion, RuleConfiguration ruleConfig, List<Coupon> couponsList) {
		int promotionId = createPromotion(	promotion.getName(),
												promotion.getDescription(),
												promotion.getDates().getValidFrom(),
												promotion.getDates().getValidTill(),
												promotion.isActive(),
												promotion.getRuleId());
		
		if(promotionId > 0) {
			createPromotionsLimitConfig(promotion.getLimitsConfig().getMaxUses(),
										promotion.getLimitsConfig().getMaxAmount(),
										promotion.getLimitsConfig().getMaxUsesPerUser(),
										promotion.getLimitsConfig().getMaxAmountPerUser(),
										promotionId);
			
			List<RuleConfigItem> ruleConfigList = ruleConfig.getConfigItems();
			for(RuleConfigItem ruleConfigItem : ruleConfigList ) {
				createPromotionRuleConfig(  ruleConfigItem.getKey(),
											ruleConfigItem.getValue(), 
											promotionId, 
											promotion.getRuleId());
			}

			for (Coupon coupon : couponsList){
				int couponId = createCoupon(coupon.getCode(), 
											promotionId, 
											coupon.getType().toString());
				if(couponId > 0) {
					coupon.setId(couponId);
				
					createCouponLimitsConfig(	coupon.getLimitsConfig().getMaxUses(), 
												coupon.getLimitsConfig().getMaxAmount(), 
												coupon.getLimitsConfig().getMaxUsesPerUser(), 
												coupon.getLimitsConfig().getMaxAmountPerUser(), 
												couponId);
	
					for (UserCouponUsesEntry couponUse : coupon.getCouponUses()) {
						createUserCouponUses(couponUse, couponId);
					}
	
					for (Integer userId : coupon.getUsers()) {
						assignUserToCoupon(userId, couponId);
					}
					for (UserPromotionUsesEntry promotionUse : coupon.getPromotionUses()) {
						createUserPromotionUses(promotionUse, promotionId);
					}
				}
			}
		} else {
			errorLog.error("Promotion not migrated for legacy promotion : " + promotion.getName());
		}
	}

	private int createPromotion( final String name,
								 final String description,
								 final DateTime validFrom,
								 final DateTime validTill,
								 final boolean active,
								 final int rule_id) {
		
		infoLog.info("Insert in the platform_promotion table => name " + name + " , description : " + description + " , validFrom : " + validFrom + " , validTill : " + validTill + " , Rule id : " + rule_id);
		
		KeyHolder promotionKeyHolder = new GeneratedKeyHolder();
		
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_PROMOTION_SQL, new String[] {"id"});
					Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
					ps.setTimestamp(1, timestamp); //created_on
					ps.setTimestamp(2, timestamp); //last_modified_on
					ps.setInt(3, rule_id); 
					ps.setTimestamp(4, new java.sql.Timestamp(validFrom.getMillis()));
					ps.setTimestamp(5, new java.sql.Timestamp(validTill.getMillis()));
					ps.setString(6, name);
					ps.setString(7, description);
					ps.setBoolean(8, active);
					return ps;
				}
			}, promotionKeyHolder);
		} catch (DuplicateKeyException e) {
			errorLog.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		infoLog.info("platform_promotion id : " + promotionKeyHolder.getKey().intValue());
		return promotionKeyHolder.getKey().intValue();
	}
	
	private void createPromotionsLimitConfig(final int maxUses,
											final Money maxAmount,
											final int maxUsesPerUser,
											final Money maxAmountPerUser,
											final int promotionId ) throws PlatformException {
		infoLog.info("Insert into promotion_limit_config promotionId : " + promotionId + " , maxUses : " + maxUses + " , maxAmount :" + maxAmount.toString() + " , maxUsesPerUser : " + maxAmountPerUser.toString());
		KeyHolder promotionLimitConfigKeyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_PROMOTION_LIMIT_CONFIG_SQL, new String[] {"id"});
					ps.setInt(1, maxUses);
					ps.setBigDecimal(2, maxAmount.getAmount());
					ps.setInt(3, maxUsesPerUser);
					ps.setBigDecimal(4, maxAmountPerUser.getAmount());
					ps.setInt(5, promotionId);
					return ps;
				}
			}, promotionLimitConfigKeyHolder);
		} catch (DuplicateKeyException e) {
			errorLog.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		infoLog.info("promotion_limit_config id : " + promotionLimitConfigKeyHolder.getKey().intValue());
	}
	
	private void createPromotionRuleConfig(  final String name,
											final String value,
											final int promotionId,
											final int ruleId) {
		infoLog.info("Insert in promotion_rule_config promotionId : " + promotionId + " , ruleId : " + " , name : " + name + " , value : " + value);
		KeyHolder promotionRuleConfigKeyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_PROMOTION_RULE_CONFIG, new String[] {"id"});
					ps.setString(1, name);
					ps.setString(2, value);
					ps.setInt(3, promotionId);
					ps.setInt(4, ruleId);
					return ps;
				}
			}, promotionRuleConfigKeyHolder);
		} catch (DuplicateKeyException e) {
			errorLog.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		infoLog.info("promotion_rule_config id : " + promotionRuleConfigKeyHolder.getKey().intValue());
	}
	
	private int createCoupon(	final String couponCode,
								final int promotionId,
								final String couponType) {
		infoLog.info("Insert into coupon couponCode : " + couponCode + " , promotionId : " + promotionId + " , couponType : " + couponType);
		KeyHolder couponKeyHolder = new GeneratedKeyHolder();
		
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_COUPON_SQL, new String[] {"id"});
					Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
					ps.setTimestamp(1, timestamp); //created_on
					ps.setTimestamp(2, timestamp); //last_modified_onmaxUses
					ps.setString(3, couponCode);
					ps.setInt(4, promotionId);
					ps.setString(5, couponType);
					return ps;
				}
			}, couponKeyHolder);
		} catch (DuplicateKeyException e) {
			errorLog.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		
		infoLog.info("coupon id : " + couponKeyHolder.getKey().intValue());
		return couponKeyHolder.getKey().intValue();
	}
	
	private void createCouponLimitsConfig(	final int maxUses,
											final Money maxAmount,
											final int maxUsesPerUser,
											final Money maxAmountPerUser,
											final int couponId) {
		infoLog.info("Insert into coupon_limit_config couponId : " + couponId + " , maxUses : " + maxUses + " , maxAmount :" + maxAmount.toString() + " , maxUsesPerUser : " + maxAmountPerUser.toString());
		KeyHolder couponLimitConfigKeyHolder = new GeneratedKeyHolder();
		
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_COUPON_LIMIT_CONFIG_SQL, new String[] {"id"});
					ps.setInt(1, maxUses);
					ps.setBigDecimal(2, maxAmount.getAmount());
					ps.setInt(3, maxUsesPerUser);
					ps.setBigDecimal(4, maxAmountPerUser.getAmount());
					ps.setInt(5, couponId);
					return ps;
				}
			}, couponLimitConfigKeyHolder);
			
		} catch (DuplicateKeyException e) {
			errorLog.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		
		infoLog.info("coupon_limit_config id : " + couponLimitConfigKeyHolder.getKey().intValue());
		
	}

	private void createUserCouponUses(UserCouponUsesEntry couponUse, int couponId) {

		int rowsUpdated = jdbcTemplate.update(CREATE_USER_COUPON_USES_SQL, 
				couponId, 
				couponUse.getUserId(),
				couponUse.getOrderId(), 
				couponUse.getDiscountAmount().getAmount(), 
				new java.sql.Timestamp(couponUse.getCreatedDate().getMillis()), 
				new java.sql.Timestamp(System.currentTimeMillis()));

		if (rowsUpdated != 1) {
			throw new PlatformException("Unable to create entry in user_coupon_uses for couponId : " + couponId + ", userId : " + couponUse.getUserId());
		}
	}


	private void createUserPromotionUses(UserPromotionUsesEntry promotionUse, int promotionId) {
		int rowsUpdated = jdbcTemplate.update(CREATE_USER_PROMOTION_USES_SQL, 
				promotionId, 
				promotionUse.getUserId(),
				promotionUse.getOrderId(), 
				promotionUse.getDiscountAmount().getAmount(),
				new java.sql.Timestamp(promotionUse.getCreatedDate().getMillis()), 
				new java.sql.Timestamp(System.currentTimeMillis()));

		if (rowsUpdated != 1) {
			throw new PlatformException("Unable to create entry in user_promotion_uses for promotionId : " + promotionId + ", userId : " + promotionUse.getUserId());
		}
	}

	private void assignUserToCoupon(int userId, int couponId) {
		int rowsUpdated = jdbcTemplate.update(CREATE_COUPON_USER_MAPPING_SQL, couponId, userId, 0);
		if (rowsUpdated != 1) {
			throw new PlatformException("Unable to create entry in platform_coupon_user for couponId : " + couponId + ", userId : " + userId);
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
