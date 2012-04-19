/**
 * 
 */
package com.fb.platform.promotion.dao.legacy.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.promotion.dao.legacy.LegacyDao;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.legacy.LegacyCouponOrder;
import com.fb.platform.promotion.model.legacy.LegacyCouponUser;
import com.fb.platform.promotion.model.legacy.LegacyPromotion;
import com.fb.platform.promotion.model.legacy.LegacyPromotionCoupon;

/**
 * @author vinayak
 *
 */
public class LegacyDaoJdbcImpl implements LegacyDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(LegacyDaoJdbcImpl.class);

	private static final String LOAD_PROMOTION_ID_BATCH_QUERY = "SELECT promotion_id FROM promotion order by promotion_id asc LIMIT ?, ?";

	/*
| promotion_id        | int(11)      | NO   | PRI | NULL    | auto_increment |
| ---order_amount        | double       | YES  |     | NULL    |                |
| applied_on          | varchar(45)  | YES  |     | NULL    |                |
| discount_type       | varchar(50)  | YES  |     | NULL    |                |
| discount_value      | varchar(45)  | YES  |     | NULL    |                |
| min_order_value     | double       | YES  |     | NULL    |                |
| max_uses            | int(11)      | YES  |     | NULL    |                |
| max_uses_per_user   | int(11)      | YES  |     | NULL    |                |
| ---total_uses          | int(11)      | YES  |     | NULL    |                |
| --can_be_claimed_by   | varchar(200) | YES  |     | NULL    |                |
| name_of_promotion   | varchar(50)  | YES  |     | NULL    |                |
| start_date          | date         | YES  |     | NULL    |                |
| end_date            | date         | YES  |     | NULL    |                |
| created_on          | date         | YES  |     | NULL    |                |
| last_modified_on    | date         | YES  |     | NULL    |                |
| --created_by          | varchar(100) | YES  |     | NULL    |                |
| --celin               | double       | YES  |     | NULL    |                |
| promotion_type      | varchar(45)  | YES  | MUL | NULL    |                |
| --bundle_id           | int(11)      | YES  |     | NULL    |                |
| --discount_bundle_id  | varchar(45)  | YES  |     | NULL    |                |
| active              | int(1)       | YES  |     | 1       |                |
| global              | int(1)       | YES  |     | 0       |                |
| max_uses_per_coupon | int(11)      | YES  |     | -1      |                |
	 */
	public static final String LOAD_PROMOTION_QUERY =
			"SELECT " +
			"	promotion_id," +
			"	applied_on," +
			"	discount_type," +
			"	discount_value," +
			"	min_order_value," +
			"	max_uses_per_user," +
			"	max_uses_per_coupon," +
			"	total_uses," +
			"	max_uses," +
			"	name_of_promotion," +
			"	start_date," +
			"	end_date," +
			"	created_on," +
			"	last_modified_on," +
			"	promotion_type," +
			"	active," +
			"	global " +
			"FROM promotion WHERE promotion_id = ?";

	public static final String LOAD_COUPONS_QUERY =
			"SELECT " +
			"	promotion_id," +
			"	coupon_code," +
			"	client_id " +
			"FROM promo_coupon WHERE promotion_id = ?";
	
	public static final String LOAD_COUPON_USERS_QUERY =
			"SELECT " +
			"	cp.coupon_code," +
			"	cp.profile_id " +
			"FROM coupon_profile cp WHERE coupon_code = ?";
	
	private static final String GET_USER_ORDER_DETAILS = 
			"SELECT o.user_id,o.coupon_discount,o.confirming_timestamp,o.id" +
			"FROM orders_order o, promotions_coupon pc" +
			"WHERE o.coupon_id = pc.id and pc.code = ?   AND"+
			"o.support_state IS NOT NULL AND"+
			"o.support_state NOT IN ('booked','cancelled','returned')";

	@Override
	public LegacyPromotion loadPromotion(int promotionId) {
		LegacyPromotion legacyPromotion = jdbcTemplate.queryForObject(LOAD_PROMOTION_QUERY, new LegacyPromotionMapper(), promotionId);
		List<LegacyPromotionCoupon> coupons = loadCoupons(legacyPromotion);
		legacyPromotion.setCoupons(coupons);
		return legacyPromotion;
	}

	public List<LegacyPromotionCoupon> loadCoupons(LegacyPromotion legacyPromotion) {
		log.info("Loading coupons for promotion : " + legacyPromotion.getPromotionId());
		List<LegacyPromotionCoupon> coupons = jdbcTemplate.query(LOAD_COUPONS_QUERY, new LegacyPromotionCouponMapper(), legacyPromotion.getPromotionId());
		log.info("Loaded coupons for promotion : " + legacyPromotion.getPromotionId() + ". Number of coupons : " + coupons.size());
		for (LegacyPromotionCoupon coupon : coupons) {
			List<LegacyCouponUser> couponUsers = loadCouponUsers(coupon.getCouponCode());
			coupon.setCouponUsers(couponUsers);
			if(legacyPromotion.getGlobal() == 0) {
				List<LegacyCouponOrder> legacyCouponOrder = getUserOders(coupon.getCouponCode());
				coupon.setCouponOrders(legacyCouponOrder);
			}
		}
		return coupons;
	}

	public List<LegacyCouponUser> loadCouponUsers(String couponCode) {
		log.info("Loading coupon users for couponCode : " + couponCode);
		List<LegacyCouponUser> couponUsers = jdbcTemplate.query(LOAD_COUPON_USERS_QUERY, new LegacyCouponUserMapper(), couponCode);
		return couponUsers;
	}

	@Override
	public List<Integer> loadIdsToMigrate(int startRecord, int batchSize) {
		List<Integer> promotiondIds = jdbcTemplate.queryForList(LOAD_PROMOTION_ID_BATCH_QUERY, Integer.class, startRecord, batchSize);
		log.info("PromotionIds to migrate : " + promotiondIds);
		return promotiondIds;
	}
	
	private List<LegacyCouponOrder> getUserOders(String couponCode) {
		List<LegacyCouponOrder> couponList = new ArrayList<LegacyCouponOrder>();
		try {
			couponList = jdbcTemplate.query(GET_USER_ORDER_DETAILS, new LegacyCouponOrderMapper(), couponCode);
		} catch (EmptyResultDataAccessException e) {
			if(log.isDebugEnabled()) {
				log.debug("User first record.");
			}
		}
		return couponList;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static class LegacyPromotionMapper implements RowMapper<LegacyPromotion> {

		@Override
		public LegacyPromotion mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			LegacyPromotion fbPromotion = new LegacyPromotion();
	        fbPromotion.setPromotionId(resultSet.getInt("promotion_id"));
	        fbPromotion.setAppliesOn(resultSet.getString("applied_on"));
	        fbPromotion.setDiscountType(resultSet.getString("discount_type"));
	        fbPromotion.setDiscountValue(resultSet.getString("discount_value"));
	        fbPromotion.setMinAmountValue(resultSet.getDouble("min_order_value"));
	        fbPromotion.setMaxUsesPerUser(resultSet.getInt("max_uses_per_user"));
	        fbPromotion.setMaxUsesPerCoupon(resultSet.getInt("max_uses_per_coupon"));
	        fbPromotion.setMaxUses(resultSet.getInt("max_uses"));
	        fbPromotion.setTotalUses(resultSet.getInt("total_uses"));
	        fbPromotion.setPromotionName(resultSet.getString("name_of_promotion"));
	        fbPromotion.setStartDate(resultSet.getTimestamp("start_date"));
	        fbPromotion.setEndDate(resultSet.getTimestamp("end_date"));
	        fbPromotion.setCreatedOn(resultSet.getTimestamp("created_on"));
	        fbPromotion.setLastModifedOn(resultSet.getTimestamp("last_modified_on"));        
	        fbPromotion.setPromotionType(resultSet.getString("promotion_type"));
	        fbPromotion.setActive(resultSet.getInt("active"));
	        fbPromotion.setGlobal(resultSet.getInt("global"));
	        return fbPromotion;
		}
	}

	private static class LegacyPromotionCouponMapper implements RowMapper<LegacyPromotionCoupon> {

		@Override
		public LegacyPromotionCoupon mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			LegacyPromotionCoupon fbCoupon = new LegacyPromotionCoupon();
	        fbCoupon.setPromotionId(resultSet.getInt("promotion_id"));
	        fbCoupon.setCouponCode(resultSet.getString("coupon_code"));
	        fbCoupon.setClientId(resultSet.getInt("client_id"));
	        return fbCoupon;
		}
	}

	private static class LegacyCouponUserMapper implements RowMapper<LegacyCouponUser> {

		@Override
		public LegacyCouponUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			LegacyCouponUser fbCouponUser = new LegacyCouponUser();
	        fbCouponUser.setCouponCode(resultSet.getString("coupon_code"));
	        fbCouponUser.setUserId(resultSet.getInt("profile_id"));
	        return fbCouponUser;
		}
	}
	
	private static class LegacyCouponOrderMapper implements RowMapper<LegacyCouponOrder> {

		@Override
		public LegacyCouponOrder mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			LegacyCouponOrder legacyCouponOrder = new LegacyCouponOrder();
			legacyCouponOrder.setConfirmingTimeStamp(resultSet.getTimestamp("confirming_timestamp"));
			legacyCouponOrder.setCouponDiscount(resultSet.getBigDecimal("coupon_discount"));
			legacyCouponOrder.setOrderId(resultSet.getInt("id"));
			legacyCouponOrder.setUserId(resultSet.getInt("user_id"));
	        return legacyCouponOrder;
		}
	}
}
