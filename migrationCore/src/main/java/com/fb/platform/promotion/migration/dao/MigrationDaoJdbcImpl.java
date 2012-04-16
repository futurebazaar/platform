/**
 * 
 */
package com.fb.platform.promotion.migration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;

/**
 * @author keith
 *
 */
public class MigrationDaoJdbcImpl  implements MigrationDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(MigrationDaoJdbcImpl.class);

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


	private static final String CREATE_RULE_CONFIG = 
			"INSERT INTO " +
					"	promotion_rule_config " +
					"		(name, " +
					"		value, " +
					"		promotion_id, " +
					"		rule_id) " +
					"VALUES (?, ?, ?, ?)";
	
	private static final String CREATE_PROMOTION_LIMIT_CONFIG_SQL = 
			"INSERT INTO " +
			"	promotion_limit_config " +
			"		(max_uses, " +
			"		max_amount, " +
			"		max_uses_per_user, " +
			"		max_amuont_per_user, " +
			"		promotion_id) " +
			"VALUES (?, ?, ?, ?, ?)";
	
	private static final String CREATE_COUPON_LIMIT_CONFIG_SQL = 
			"INSERT INTO " +
			"	coupon_limit_config " +
			"		(max_uses, " +
			"		max_amount, " +
			"		max_uses_per_user, " +
			"		max_amuont_per_user, " +
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
	public int createPromotion(final String name, final String description,
			final Timestamp validFrom, final Timestamp validTill, final int maxUses,
			final Money maxAmount, final int maxUsesPerUser, final Money maxAmountPerUser,
			final int global, final int active) throws PlatformException{

			log.info("Insert in the platform_promotion table => name " + name + " , description : " + description + " , validFrom : " + validFrom + " , validTill : " + validTill);// + " , Rule " + rule);
			
			int rowAffected = 0;
			KeyHolder promotionKeyHolder = new GeneratedKeyHolder();
			
			try {
				rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(CREATE_PROMOTION_SQL, new String [] {"id"});
						Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
						ps.setTimestamp(1, timestamp);
						ps.setTimestamp(2, timestamp);
//						ps.setInt(3, RulesEnum);
						ps.setTimestamp(4, validFrom);
						ps.setTimestamp(5, validTill);
						ps.setString(6, name);
						ps.setString(7, description);
						return ps;
					}
				}, promotionKeyHolder);
			} catch (DuplicateKeyException e) {
				log.error("Duplicate key insertion exception " + e);
				throw new PlatformException("Duplicate key insertion exception "+e);
			}
			
			if(rowAffected > 0){
				KeyHolder promotionLimitsKeyHolder = new GeneratedKeyHolder();
				
				try {
					rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
						
						@Override
						public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
							PreparedStatement ps = con.prepareStatement(CREATE_PROMOTION_LIMIT_CONFIG_SQL, new String [] {"id"});
							Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
							ps.setInt(1, maxUses);
							ps.setBigDecimal(2, maxAmount.getAmount());
							ps.setInt(3, maxUsesPerUser);
							ps.setBigDecimal(4, maxAmountPerUser.getAmount());
							return ps;
						}
					}, promotionLimitsKeyHolder);
				} catch (DuplicateKeyException e) {
					log.error("Duplicate key insertion exception " + e);
					throw new PlatformException("Duplicate key insertion exception "+e);
				}
			}
			
			return promotionKeyHolder.getKey().intValue();

	}



	@Override
	public boolean createPromotionRuleConfig(String appliedOn,
			String discountType, Money minOrderValue, Money discountValue,
			String promotionType, int clientId) {
		// TODO Auto-generated method stub
		return false;
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

}
