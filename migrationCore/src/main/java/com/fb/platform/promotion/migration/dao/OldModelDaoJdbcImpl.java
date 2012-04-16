/**
 * 
 */
package com.fb.platform.promotion.migration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.promotion.migration.oldModel.OldCouponUser;
import com.fb.platform.promotion.migration.oldModel.OldPromoCoupon;
import com.fb.platform.promotion.migration.oldModel.OldPromotion;


/**
 * @author keith
 *
 */
public class OldModelDaoJdbcImpl implements OldModelDao{

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(OldModelDao.class);

	
	public static final String LOAD_PROMOTION_QUERY =
			"SELECT "
					+ "promotion_id," + "applied_on," + "discount_type,"
					+ "discount_value," + "min_order_value," + "max_uses_per_user," + "max_uses_per_coupon,"
					+ "max_uses,"
					+ "name_of_promotion," + "start_date," + "end_date,"
					+ "created_on," + "last_modified_on," + "promotion_type,"
					+ "active," + "global" 
					+ " FROM promotion WHERE promotion_id = ?";
	
	public static final String LOAD_COUPONS_QUERY =
			"SELECT "
					+ "promotion_id,"+"coupon_code,"+"client_id"
					+ " FROM promo_coupon WHERE promotion_id = ?";
	
	public static final String LOAD_COUPON_USERS_QUERY =
			"SELECT "
					+"cp.coupon_code,"+"cp.user_id"
					+ " FROM coupon_profile cp WHERE coupon_code = ?";
	
	@Override
	public OldPromotion loadOldPromotion(int promotionId) {
		// TODO Auto-generated method stub
		OldPromotion oldPromo = jdbcTemplate.queryForObject(LOAD_PROMOTION_QUERY, new Object [] {promotionId}, new OldPromotionMapper());
		return oldPromo;
	}

	@Override
	public List<OldPromoCoupon> loadOldCoupons(final int promotionId) {
		List<OldPromoCoupon> oldCoupons = jdbcTemplate.query( new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(LOAD_COUPONS_QUERY);
				ps.setInt(1, promotionId);
				return null;
			}
		},new OldPromoCouponMapper());
		return oldCoupons;
	}

	@Override
	public List<OldCouponUser> loadCouponUsers(final String couponCode) {
		List<OldCouponUser> oldCouponUsers = jdbcTemplate.query( new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(LOAD_COUPON_USERS_QUERY);
				ps.setString(1, couponCode);
				return null;
			}
		},new OldCouponUserMapper());
		return oldCouponUsers;
	}

	
	private static class OldPromotionMapper implements RowMapper<OldPromotion> {

		@Override
		public OldPromotion mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			 OldPromotion fbPromotion = new OldPromotion();
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


	private static class OldPromoCouponMapper implements RowMapper<OldPromoCoupon> {

		@Override
		public OldPromoCoupon mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			OldPromoCoupon fbCoupon = new OldPromoCoupon();
	        fbCoupon.setPromotionId(resultSet.getInt("promotion_id"));
	        fbCoupon.setCouponCode(resultSet.getString("coupon_code"));
	        fbCoupon.setClientId(resultSet.getInt("client_id"));
	        return fbCoupon;
	        }
	}

	private static class OldCouponUserMapper implements RowMapper<OldCouponUser> {

		@Override
		public OldCouponUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			OldCouponUser fbCouponUser = new OldCouponUser();
	        fbCouponUser.setCouponCode(resultSet.getString("coupon_code"));
	        fbCouponUser.setUserId(resultSet.getInt("profile_id"));
	        return fbCouponUser;
	        }
	}
}
