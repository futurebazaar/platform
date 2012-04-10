/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUsesEntry;
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
			"	count(*) as current_count, " +
			"	sum(ucu.discount_amount) as current_amount, " +
			"	coupon_id " +
			"FROM user_coupon_uses ucu WHERE coupon_id = ? " +
			"group by coupon_id";
	
	private static final String LOAD_USER_COUPON_USES_QUERY = 
			"SELECT " +
			"	count(*) as current_count, " +
			"	sum(ucu.discount_amount) as current_amount, " +
			"	coupon_id, " +
			"	user_id " +
			"FROM user_coupon_uses ucu WHERE coupon_id = ? AND user_id = ? " +
			"group by coupon_id";

	private static final String CREATE_USER_USES = 
			"INSERT INTO user_coupon_uses " +
			"	(coupon_id, " +
			"	user_id, " +
			"	order_id, " +
			"	discount_amount, " +
			"	created_on, " +
			"	last_modified_on) " +
			"VALUES (?, ?, ?, ?, ?, ?)";

	private static final String CREATE_RELEASED_COUPON = 
			"INSERT INTO released_coupon " +
			"	(coupon_id, " +
			"	user_id, " +
			"	order_id, " +
			"	discount_amount, " +
			"	created_on) " +
			"VALUES (?, ?, ?, ?, ?)";
	
	private static final String LOAD_COUPON_USER_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_id, " +
			"	user_id, " +
			"	override_user_uses_limit " +
			"FROM platform_coupon_user WHERE coupon_id = ? AND user_id = ?";

	private static final String LOAD_USER_ORDER_COUPON_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_id, " +
			"	user_id, " +
			"	order_id, " +
			"	discount_amount " +
			"FROM user_coupon_uses WHERE coupon_id = ? AND user_id = ? AND order_id = ?";
	
	private static final String DELETE_USER_USES = 
			"DELETE from user_coupon_uses " +
			"where coupon_id = ? AND user_id = ? AND order_id = ?";
	
	@Override
	public Coupon load(String couponCode, int userId) {
		Coupon coupon = null;
		if(log.isDebugEnabled()) {
			log.debug("Getting the coupon details for the coupon code : " + couponCode);
		}
		try {
			coupon = jdbcTemplate.queryForObject(LOAD_COUPON_QUERY, new Object [] {couponCode}, new CouponMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//this means we dont have any coupon with this coupon code in DB
			log.warn("Coupon not found for couponCode - " + couponCode);
			return null;
		}

		CouponLimitsConfig limitsConfig = null;
		if(log.isDebugEnabled()) {
			log.debug("Getting the coupon maximum limit for the coupon code : " + couponCode);
		}
		try {
			limitsConfig = jdbcTemplate.queryForObject(LOAD_COUPON_LIMITS_QUERY, new Object[] {coupon.getId()}, new CouponLimitsConfigMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			log.error("Coupon Limits are not configured for the coupon code - " + couponCode);
			throw new PlatformException("Coupon Limits are not configured for the coupon code - " + couponCode, e);
		}
		coupon.setLimitsConfig(limitsConfig);

		if (coupon.getType() == CouponType.PRE_ISSUE) {
			//PRE_ISSUE coupons are issued to a particular user.
			//find out the user associated with this coupon
			if(log.isDebugEnabled()) {
				log.debug("Getting the coupon details for the coupon code : " + couponCode + " ,issued to the user : " + userId);
			}
			CouponUserRowCallbackHandler curch = new CouponUserRowCallbackHandler();
			jdbcTemplate.query(LOAD_COUPON_USER_QUERY, curch, coupon.getId(), userId);

			if (curch.getColumnCount() == 0) {
				//this coupon does not belong to this user. sorry bye
				//TODO return a proper error or throw correct exectption. for the time being return null coupon which will map to no coupon found message.
				log.error("Coupon code : " + couponCode + " does not belong to userId : " + userId);
				return null;
			}
			//see if the user uses limit is overridden for this user. if so update our limits objects
			if (curch.userUsesLimitOverride != 0) {
				limitsConfig.setMaxUsesPerUser(curch.userUsesLimitOverride);
			}
		}

		return coupon;
	}

	@Override
	public GlobalCouponUses loadGlobalUses(int couponId) {
		if(log.isDebugEnabled()) {
			log.debug("Getting the global coupon usage for the coupon id : " + couponId );
		}
		GlobalCouponUses globalCouponUses = null;
		try {
			globalCouponUses = jdbcTemplate.queryForObject(LOAD_GLOBAL_COUPON_USES_QUERY, new Object [] {couponId}, new GlobalCouponUsesMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//no global uses set, that means this is first time use of this promotion
			log.warn("No global uses set for coupon id " + couponId + " , that means this is first time use of this promotion");
			globalCouponUses = new GlobalCouponUses();
			globalCouponUses.setCouponId(couponId);
			globalCouponUses.setCurrentAmount(new Money(BigDecimal.ZERO));
			globalCouponUses.setCurrentCount(0);
		}
		return globalCouponUses;
	}

	@Override
	public UserCouponUses loadUserUses(int couponId, int userId) {
		if(log.isDebugEnabled()) {
			log.debug("Getting the coupon usage for the coupon id : " + couponId + " ,by the user : " + userId);
		}
		UserCouponUses userCouponUses = null;
		try {
			userCouponUses = jdbcTemplate.queryForObject(LOAD_USER_COUPON_USES_QUERY, new Object[] {couponId, userId}, new UserCouponUsesMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn("No user uses set for coupon id " + couponId + " , that means this is first time use of this promotion");
			//no user uses set, that means this is first time use of this promotion
			userCouponUses = new UserCouponUses();
			userCouponUses.setCouponId(couponId);
			userCouponUses.setCurrentAmount(new Money(BigDecimal.ZERO));
			userCouponUses.setCurrentCount(0);
			userCouponUses.setUserId(userId);
		}
		return userCouponUses;
	}

	@Override
	public boolean updateUserUses(int couponId, int userId, BigDecimal valueApplied, int orderId) {
		
		//for every use of coupon a new entry is created
		return createUserUses(couponId, userId, valueApplied, orderId);
	}

	@Override
	public boolean releaseCoupon(final int couponId,final int userId,final int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Released coupon for user : " + userId + " , applied coupon id : " + couponId + " , on order id : " + orderId);
		}
		
		BigDecimal discountAmount = getDiscountValue(couponId, userId, orderId);
		boolean isReleasedCouponCreated = createReleasedCoupon(couponId, userId, discountAmount, orderId);
		boolean isUserCouponUsesDeleted = cancelUserUses(couponId, userId, orderId);
		
		return isReleasedCouponCreated && isUserCouponUsesDeleted;
	}
	
	@Override
	public boolean isCouponApplicable(int couponId, int userId, int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Get from the user coupon uses table record for user : " + userId + " , applied coupon id : " + couponId + " , on order id : " + orderId);
		}
		UserCouponUsesEntry userCouponUsesEntry = null;
		try {
			userCouponUsesEntry = jdbcTemplate.queryForObject(LOAD_USER_ORDER_COUPON_QUERY, new Object [] {couponId, userId, orderId}, new UserOrderCouponMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn("No entry found for userId" + userId + " with couponId "+ couponId + " and orderId " + orderId);
			//throw new PlatformException("No entry in user_coupon_uses found for userId" + userId + " with couponId "+ couponId + " and orderId " + orderId);
			return true;
		}
		
		return userCouponUsesEntry==null ? true : false;
	}
	
	private BigDecimal getDiscountValue(int couponId, int userId, int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Get from the user coupon uses table record for user : " + userId + " , applied coupon id : " + couponId + " , on order id : " + orderId);
		}
		UserCouponUsesEntry userCouponUsesEntry = null;
		BigDecimal discountValue = null;
		try {
			userCouponUsesEntry = jdbcTemplate.queryForObject(LOAD_USER_ORDER_COUPON_QUERY, new Object [] {couponId, userId, orderId}, new UserOrderCouponMapper());
			discountValue = userCouponUsesEntry.getDiscountAmount().getAmount();
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn("No entry found for userId " + userId + " with couponId "+ couponId + " and orderId " + orderId);
			throw new PlatformException("No entry in user_coupon_uses found for userId" + userId + " with couponId "+ couponId + " and orderId " + orderId);
		}
		
		if(discountValue==null){
			throw new PlatformException("Error while getting discount value for userId " + userId + " with couponId "+ couponId + " and orderId " + orderId);
		}
		return discountValue;
	}
	
	private boolean createUserUses(final int couponId, final int userId, final BigDecimal valueApplied, final int orderId) {
		if(log.isDebugEnabled()) {
			log.debug("Insert in the user_coupon_uses table record for user : " + userId + " , applied coupon id : " + couponId + " , on order id : " + orderId + " , discount value applied : " + valueApplied );
		}
		KeyHolder userUsesKeyHolder = new GeneratedKeyHolder();
		int rowAffected = 0;
		try {
			rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_USER_USES, new String [] {"id"});
					ps.setInt(1, couponId);
					ps.setInt(2, userId);
					ps.setInt(3, orderId);
					ps.setBigDecimal(4, valueApplied);
					ps.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
					return ps;
				}
			}, userUsesKeyHolder);
		} catch (DuplicateKeyException e) {
			log.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		
		return rowAffected>0 ? true : false;
	}

	private boolean createReleasedCoupon(final int couponId,final int userId,final BigDecimal discountAmount,final int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Insert in the released_coupon table record for user : " + userId + " , applied coupon id : " + couponId + " , on order id : " + orderId + " , discount value applied : " + discountAmount );
		}
		KeyHolder userUsesKeyHolder = new GeneratedKeyHolder();
		int rowAffected = 0;
		try {
			rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_RELEASED_COUPON, new String [] {"id"});
					ps.setInt(1, couponId);
					ps.setInt(2, userId);
					ps.setInt(3, orderId);
					ps.setBigDecimal(4, discountAmount);
					ps.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
					return ps;
				}
			}, userUsesKeyHolder);
		} catch (DuplicateKeyException e) {
			log.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		
		return rowAffected>0 ? true : false;
	}
	
	private boolean cancelUserUses(final int couponId, final int userId, final int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Deleting (cancelling) the coupon id : " + couponId + ", applied on order id : " + orderId + " for user : " + userId );
		}
		
		int rowAffected = -1;
		try {
			rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(DELETE_USER_USES);
					ps.setInt(1, couponId);
					ps.setInt(2, userId);
					ps.setInt(3, orderId);
					return ps;
				}
			});
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn( "Tried cancelling coupon id " + couponId + " ,but entry not found.");
			//failed to update the row
			new PlatformException("Deleting user coupon uses failed : " + couponId + ", applied on order id : " + orderId + " for user : " + userId );
		}
		
		return rowAffected>0 ? true : false;
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

	private static class UserOrderCouponMapper implements RowMapper<UserCouponUsesEntry> {

		@Override
		public UserCouponUsesEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserCouponUsesEntry releaseCoupon = new UserCouponUsesEntry();

			releaseCoupon.setId(rs.getInt("id"));
			releaseCoupon.setCouponId(rs.getInt("coupon_id"));
			releaseCoupon.setUserId(rs.getInt("user_Id"));
			releaseCoupon.setOrderId(rs.getInt("order_id"));

			BigDecimal discountAmount = rs.getBigDecimal("discount_amount");
			if (discountAmount == null) {
				discountAmount = BigDecimal.ZERO;
			}
			releaseCoupon.setDiscountAmount(new Money(discountAmount));
			
			return releaseCoupon;
		}
	}
	
	private static class CouponUserRowCallbackHandler extends RowCountCallbackHandler {

		private int userId = 0;
		private int userUsesLimitOverride = 0;

		@Override
		public void processRow(ResultSet rs, int rowNum) throws SQLException {
			userId = rs.getInt("user_id");
			userUsesLimitOverride = rs.getInt("override_user_uses_limit");
		}
	}
	
	private static class CouponLimitsConfigMapper implements RowMapper<CouponLimitsConfig> {

		@Override
		public CouponLimitsConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
			CouponLimitsConfig config = new CouponLimitsConfig();

			BigDecimal maxAmountBD = rs.getBigDecimal("max_amount");
			if (maxAmountBD != null) {
				config.setMaxAmount(new Money(maxAmountBD));
			}else{
				//maxAmount cannot be null or zero in the database
				throw new PlatformException("Max Amount cannot be null or zero. Invalid Coupon data.");
			}

			BigDecimal maxAmountPerUserBD = rs.getBigDecimal("max_amount_per_user");
			if (maxAmountPerUserBD != null) {
				config.setMaxAmountPerUser(new Money(maxAmountPerUserBD));
			}else{
				//max Amount per user cannot be null or zero in the databases
				throw new PlatformException("Max Amount Per User cannot be null or zero. Invalid Coupon data.");
			}

			int maxUses = rs.getInt("max_uses");
			if(maxUses==0){
				//max uses cannot be null or zero in the databases
				throw new PlatformException("Max uses cannot be null or zero. Invalid Coupon data.");
			}
			maxUses = maxUses > 0 ? maxUses : -1;
			config.setMaxUses(maxUses);
			
			int maxUsesPerUser = rs.getInt("max_uses_per_user");
			if(maxUsesPerUser==0){
				//max uses per user cannot be null or zero in the databases
				throw new PlatformException("Max uses per user cannot be null or zero. Invalid Coupon data.");
			}
			maxUsesPerUser = maxUsesPerUser > 0 ? maxUsesPerUser : -1;
			config.setMaxUsesPerUser(maxUsesPerUser);

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
