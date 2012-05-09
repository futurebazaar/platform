/**
 * 
 */
package com.fb.platform.promotion.admin.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.admin.dao.PromotionAdminDao;

/**
 * @author keith
 *
 */
public class PromotionAdminDaoJdbcImpl  implements PromotionAdminDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(PromotionAdminDaoJdbcImpl.class);

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
			"	coupon_limit_config " +
			"		(max_uses, " +
			"		max_amount, " +
			"		max_uses_per_user, " +
			"		max_amount_per_user, " +
			"		coupon_id) " +
			"VALUES (?, ?, ?, ?, ?)";

	private static final String CREATE_USER_PROMOTION_USES_SQL = 
			"INSERT INTO " +
			"	user_promotion_uses " +
			"		(coupon_id, " +
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
	public int createPromotion(final String name, final String description, final int ruleId,
			final DateTime validFrom, final DateTime validTill, final int active) throws PlatformException{

			log.info("Insert in the platform_promotion table => name " + name + " , description : " + description + " , validFrom : " + validFrom + " , validTill : " + validTill);
			
			KeyHolder promotionKeyHolder = new GeneratedKeyHolder();
			
			int rowUpdated = 0;
			try {
				rowUpdated = jdbcTemplate.update(new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(CREATE_PROMOTION_SQL, new String [] {"id"});
						Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
						ps.setTimestamp(1, timestamp);
						ps.setTimestamp(2, timestamp);
						ps.setInt(3, ruleId);
						if(validFrom != null) {
							ps.setTimestamp(4, new Timestamp(validFrom.getMillis()));
						} else {
							ps.setNull(4, java.sql.Types.TIMESTAMP);
						}
						if(validTill != null) {
							ps.setTimestamp(5, new Timestamp(validTill.getMillis()));
						} else {
							ps.setNull(5, java.sql.Types.TIMESTAMP);
						}
						ps.setString(6, name);
						ps.setString(7, description);
						ps.setInt(8, active);
						return ps;
					}
				}, promotionKeyHolder);
			} catch (DuplicateKeyException e) {
				log.error("Duplicate key insertion exception " + e);
				throw new PlatformException("Duplicate key insertion exception "+e);
			}
			
			return promotionKeyHolder.getKey().intValue();

	}

	@Override
	public void createPromotionLimitConfig(int promotionId, int maxUses, Money maxAmount,
		 int maxUsesPerUser, Money maxAmountPerUser) {
		
		int rowsUpdated = jdbcTemplate.update(CREATE_PROMOTION_LIMIT_CONFIG_SQL,
				maxUses,
				maxAmount.getAmount(),
				maxUsesPerUser,
				maxAmountPerUser.getAmount(),
				promotionId);
		
		if (rowsUpdated != 1) {
			throw new PlatformException("Unable to create entry in promotion_limits_config for promotionId : " + promotionId);
		}
		
	}

	@Override
	public void createPromotionRuleConfig(String name, String value, int promotionId, int ruleId) {
		int rowsUpdated = jdbcTemplate.update(CREATE_PROMOTION_RULE_CONFIG,
				name,
				value,
				promotionId,
				ruleId);
		
		if (rowsUpdated != 1) {
			throw new PlatformException("Unable to create entry in promotion_rule_config for promotionId : " + promotionId);
		}
		
	}


	@Override
	public boolean createCoupon(String couponCode, int promotionId,
			int maxUsesPerCoupon, String appliedOn, String DiscountType) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean createCouponUser(String couponCode, int userId) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean createCouponUses() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean createPromotionUses() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

}
