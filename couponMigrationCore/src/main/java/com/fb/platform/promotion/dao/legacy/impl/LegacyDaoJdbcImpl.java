/**
 * 
 */
package com.fb.platform.promotion.dao.legacy.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.promotion.dao.legacy.LegacyDao;
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

	public static final String LOAD_PROMOTION_QUERY =
			"SELECT " +
			"	promotion_id," +
			"	applied_on," +
			"	discount_type," +
			"	discount_value," +
			"	min_order_value," +
			"	max_uses_per_user," +
			"	max_uses_per_coupon," +
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
			"	cp.user_id" +
			"FROM coupon_profile cp WHERE coupon_code = ?";

	@Override
	public LegacyPromotion loadPromotion(int promotionId) {
		LegacyPromotion legacyPromotion = jdbcTemplate.queryForObject(LOAD_PROMOTION_QUERY, new LegacyPromotionMapper(), promotionId);
		List<LegacyPromotionCoupon> coupons = loadCoupons(promotionId);
		legacyPromotion.setCoupons(coupons);
		return legacyPromotion;
	}

	@Override
	public List<LegacyPromotionCoupon> loadCoupons(int promotionId) {
		List<LegacyPromotionCoupon> coupons = jdbcTemplate.query(LOAD_COUPONS_QUERY, new LegacyPromotionCouponMapper(), promotionId);
		for (LegacyPromotionCoupon coupon : coupons) {
			List<LegacyCouponUser> couponUsers = loadCouponUsers(coupon.getCouponCode());
			coupon.setCouponUsers(couponUsers);
		}
		return coupons;
	}

	@Override
	public List<LegacyCouponUser> loadCouponUsers(String couponCode) {
		List<LegacyCouponUser> couponUsers = jdbcTemplate.query(LOAD_COUPON_USERS_QUERY, new LegacyCouponUserMapper(), couponCode);
		return couponUsers;
	}

	@Override
	public List<Integer> loadIdsToMigrate(int startRecord, int batchSize) {
		List<Integer> promotiondIds = jdbcTemplate.queryForList(LOAD_PROMOTION_ID_BATCH_QUERY, Integer.class, startRecord, batchSize);
		log.debug("PromotionIds to migrate : " + promotiondIds);
		return promotiondIds;
	}

	private static class LegacyPromotionMapper implements RowMapper<LegacyPromotion> {

		@Override
		public LegacyPromotion mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			LegacyPromotion fbPromotion = new LegacyPromotion();
	        fbPromotion.setPromotionId(resultSet.getInt("promotion_id"));
	        fbPromotion.setAppliesOn(resultSet.getString("applied_on"));
	        fbPromotion.setDiscountType(resultSet.getString("discount_type"));
	        fbPromotion.setDiscountValue(resultSet.getInt("discount_value"));
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
}
