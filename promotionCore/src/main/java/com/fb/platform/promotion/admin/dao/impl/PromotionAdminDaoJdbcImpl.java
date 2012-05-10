/**
 * 
 */
package com.fb.platform.promotion.admin.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.admin.dao.PromotionAdminDao;
import com.fb.platform.promotion.admin.to.PromotionTO;

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
	
	/**
	 * QUERY
	 * SELECT pp.id as promotionId, pp.valid_from as validFrom, pp.valid_till as validTill, pp.name as promotionName, pp.description as description, pp.is_active as isActive, pr.name as ruleName FROM platform_promotion pp INNER JOIN  promotion_rule pr ON pr.id=pp.rule_id WHERE pp.name LIKE '%Promotion%' AND pp.valid_from>='2012-01-04' AND pp.valid_till<='2012-05-20' LIMIT 0,10
	 */
	
	private static String SELECT_PROMOTION_FIELDS =
			" SELECT pp.id as promotionId, " +
			"	pp.valid_from as validFrom, " +
			"	pp.valid_till as validTill, " +
			"	pp.name as promotionName, " +
			"	pp.description as description, " +
			"	pp.is_active as isActive, " +
			"	pr.name as ruleName " +
			" FROM " +
			"	platform_promotion pp " +
			"	INNER JOIN  promotion_rule pr ON pr.id=pp.rule_id ";

	private static String WHERE_CLAUSE = " WHERE ";
	
	private static String AND_JOINT = " AND ";
	
	private static String SELECT_PROMOTION_NAME_FILTER_SQL = 
			" pp.name LIKE ? ";
	
	private static String SELECT_PROMOTION_VALID_FROM_FILTER_SQL = 
			" pp.valid_from >= ? ";
	
	private static String SELECT_PROMOTION_VALID_TILL_FILTER_SQL = 
			" pp.valid_till <= ? ";
	
	private static String LIMIT_FILTER_SQL = 
			" LIMIT ?,? ";
	
	/**
	 * QUERY
	 * SELECT pp.id as promotionId, pp.rule_id as ruleId, pp.valid_from as validFrom, pp.valid_till as validTill, pp.name as promotionName, pp.description as description, pp.is_active as isActive, plc.max_uses as maxUses, plc.max_amount as maxAmount, plc.max_uses_per_user as maxUsesPerUser, plc.max_amount_per_user as maxAmountPerUser, pr.name as ruleName FROM platform_promotion pp INNER JOIN promotion_limits_config plc ON plc.promotion_id = pp.id INNER JOIN  promotion_rule pr ON pr.id=pp.rule_id WHERE pp.id=-3003
	 */
	private static String SELECT_PROMOTION_COMPLETE_VIEW_SQL =
			" SELECT " +
			"	pp.id as promotionId, " +
			"	pp.rule_id as ruleId, " +
			"	pp.valid_from as validFrom, " +
			"	pp.valid_till as validTill, " +
			"	pp.name as promotionName, " +
			"	pp.description as description, " +
			"	pp.is_active as isActive, " +
			"	plc.max_uses as maxUses, " +
			"	plc.max_amount as maxAmount, " +
			"	plc.max_uses_per_user as maxUsesPerUser, " +
			"	plc.max_amount_per_user as maxAmountPerUser, " +
			"	pr.name as ruleName " +
			" FROM " +
			"	platform_promotion pp " +
			"	INNER JOIN promotion_limits_config plc ON plc.promotion_id = pp.id " +
			"	INNER JOIN  promotion_rule pr ON pr.id=pp.rule_id " +
			" WHERE pp.id=?";
	
	
	/**
	 * QUERY
	 * SELECT prc.name as promotionRuleName, prc.value as promotionRuleValue FROM promotion_rule_config prc where promotion_id=-3003 and rule_id=-4
	 */
	private static String SELECT_PROMOTION_RULE_CONFIG_SQL = 
			" SELECT " +
			"	prc.name as promotionRuleName, " +
			"	prc.value as promotionRuleValue " +
			" FROM " +
			"	promotion_rule_config prc " +
			" WHERE " +
			"	prc.promotion_id=? AND prc.rule_id=?";
	
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
		
		log.info("Insert in the promotion_limits_config table => promotionId " + promotionId + " , maxUses : " + maxUses + " , maxAmount : " + maxAmount + " , maxAmountPerUser : " + maxAmountPerUser);
		
		int rowsUpdated = jdbcTemplate.update(CREATE_PROMOTION_LIMIT_CONFIG_SQL,
				maxUses,
				maxAmount.getAmount(),
				maxUsesPerUser,
				maxAmountPerUser.getAmount(),
				promotionId);
		
		if (rowsUpdated != 1) {
			log.error("Unable to create entry in promotion_limits_config for promotionId : " + promotionId);
			throw new PlatformException("Unable to create entry in promotion_limits_config for promotionId : " + promotionId);
		}
		
	}

	@Override
	public void createPromotionRuleConfig(String name, String value, int promotionId, int ruleId) {
		
		log.info("Insert in the promotion_rule_config table => name " + name + " , value : " + value + " , promotionId : " + promotionId + " , ruleId : " + ruleId);
		
		int rowsUpdated = jdbcTemplate.update(CREATE_PROMOTION_RULE_CONFIG,
				name,
				value,
				promotionId,
				ruleId);
		
		if (rowsUpdated != 1) {
			log.error("Unable to create entry in promotion_rule_config for promotionId : " + promotionId);
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
	
	@Override
	public List<PromotionTO> searchPromotion(String promotionName, DateTime validFrom, DateTime validTill, int startRecord, int batchSize) {
		if(log.isDebugEnabled()) {
			log.debug("Search promotion with details => promotionName:" + promotionName + " , validFrom:" + validFrom + " ,validTill:" + validTill + " ,startRecord:" + " ,batchSize:" + batchSize);
		}
		List<String> searchPromotionFilterList = new ArrayList<String>();
		String searchPromotionQuery = SELECT_PROMOTION_FIELDS;
		List<Object> args = new ArrayList<Object>();
		
		if(!StringUtils.isBlank(promotionName)) {
			searchPromotionFilterList.add(SELECT_PROMOTION_NAME_FILTER_SQL);
			args.add("%" + promotionName + "%");
		}
		if(validFrom != null) {
			searchPromotionFilterList.add(SELECT_PROMOTION_VALID_FROM_FILTER_SQL);
			args.add(validFrom.toDate());
		}
		if(validTill != null) {
			searchPromotionFilterList.add(SELECT_PROMOTION_VALID_TILL_FILTER_SQL);
			args.add(validTill.toDate());
		}
		
		args.add(startRecord);
		args.add(batchSize);
		
		if(!searchPromotionFilterList.isEmpty()) {
			searchPromotionQuery += (WHERE_CLAUSE + StringUtils.join(searchPromotionFilterList.toArray(), AND_JOINT) + LIMIT_FILTER_SQL);
		} else {
			searchPromotionQuery += LIMIT_FILTER_SQL;
		}
		
		List<PromotionTO> promotionsList = jdbcTemplate.query(searchPromotionQuery, args.toArray(), new PromotionViewMapper());
		
		return promotionsList;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}
	
	private static class PromotionViewMapper implements RowMapper<PromotionTO> {

		@Override
		public PromotionTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			PromotionTO promotionView = new PromotionTO();
			promotionView.setPromotionId(resultSet.getInt("promotionId"));
			promotionView.setValidFrom(new DateTime(resultSet.getTimestamp("validFrom")));
			promotionView.setValidTill(new DateTime(resultSet.getTimestamp("validTill")));
			promotionView.setPromotionName(resultSet.getString("promotionName"));
			promotionView.setDescription(resultSet.getString("description"));
			boolean isActive = false;
			if(resultSet.getInt("isActive") == 1) {
				isActive = true;
			}
			promotionView.setActive(isActive);
			promotionView.setRuleName(resultSet.getString("ruleName"));
			return promotionView;
		}
	}

}
