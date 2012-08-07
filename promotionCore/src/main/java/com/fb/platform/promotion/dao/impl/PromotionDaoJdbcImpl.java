/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.PromotionDates;
import com.fb.platform.promotion.model.PromotionLimitsConfig;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.model.UserPromotionUsesEntry;
import com.fb.platform.promotion.model.coupon.CouponPromotion;
import com.fb.platform.promotion.product.dao.PromotionConfigDao;
import com.fb.platform.promotion.product.model.PromotionConfig;
import com.fb.platform.promotion.product.model.promotion.AutoPromotion;
import com.fb.platform.promotion.rule.PromotionRule;

/**
 * @author vinayak
 *
 */
public class PromotionDaoJdbcImpl implements PromotionDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(PromotionDaoJdbcImpl.class);

	private RuleDao ruleDao = null;

	private PromotionConfigDao promotionConfigDao = null;

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
			"FROM user_promotion_uses upu WHERE promotion_id = ? " +
			"group by promotion_id";
	
	private static final String LOAD_USER_PROMOTION_USES_QUERY = 
			"SELECT " +
			"	count(*) as current_count, " +
			"	sum(upu.discount_amount) as current_amount, " +
			"	promotion_id, " +
			"	user_id " +
			"FROM user_promotion_uses upu WHERE promotion_id = ? AND user_id = ? " +
			"group by promotion_id";

	private static final String CREATE_USER_USES = 
			"INSERT INTO user_promotion_uses " +
			"	(promotion_id, " +
			"	user_id, " +
			"	order_id, " +
			"	discount_amount, " +
			"	created_on, " +
			"	last_modified_on) " +
			"VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String DELETE_USER_USES = 
			"DELETE from user_promotion_uses " +
			"where promotion_id = ? AND user_id = ? AND order_id = ?";
	
	private static final String CREATE_RELEASED_PROMOTION = 
			"INSERT INTO released_promotion " +
			"	(promotion_id, " +
			"	user_id, " +
			"	order_id, " +
			"	discount_amount, " +
			"	created_on) " +
			"VALUES (?, ?, ?, ?, ?)";

	private static final String LOAD_USER_ORDER_PROMOTION_QUERY = 
			"SELECT " +
			"	id, " +
			"	promotion_id, " +
			"	user_id, " +
			"	order_id, " +
			"	discount_amount " +
			"FROM user_promotion_uses WHERE promotion_id = ? AND user_id = ? AND order_id = ?";
	
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

		if (promotion instanceof CouponPromotion) {
			PromotionRule rule = ruleDao.load(promotionId, prcbh.ruleId);
			((CouponPromotion)promotion).setRule(rule);
		} else {
			PromotionConfig promotionConfig = promotionConfigDao.load(promotionId);
			((AutoPromotion)promotion).setPromotionConfig(promotionConfig);
		}

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
			log.warn("No global uses set for promotion id " + promotionId + " , that means this is first time use of this promotion");
			globalPromotionUses = new GlobalPromotionUses();
			globalPromotionUses.setPromotionId(promotionId);
			globalPromotionUses.setCurrentAmount(new Money(BigDecimal.ZERO));
			globalPromotionUses.setCurrentCount(0);
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
			log.warn("No user uses set for promotion id " + promotionId + " , that means this is first time use of this promotion");
			//no user uses set, that means this is first time use of this promotion
			userPromotionUses = new UserPromotionUses();
			userPromotionUses.setPromotionId(promotionId);
			userPromotionUses.setCurrentAmount(new Money(BigDecimal.ZERO));
			userPromotionUses.setCurrentCount(0);
			userPromotionUses.setUserId(userId);
		}
		return userPromotionUses;
	}

	@Override
	public boolean updateUserUses(int promotionId, int userId, BigDecimal valueApplied, int orderId) {
		
		//for every use of the coupon, create a new object
		return createUserUses(promotionId, userId, valueApplied, orderId);
	}

	@Override
	public boolean releasePromotion(final int promotionId, final int userId, final int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Released promotion for user : " + userId + " , applied promotion id : " + promotionId + " , on order id : " + orderId);
		}
		BigDecimal discountAmount = getDiscountValue(promotionId, userId, orderId);
		boolean isReleasedPromotionCreated = createReleasedPromotion(promotionId, userId, discountAmount, orderId);
		boolean isUserPromotionUsesDeleted = cancelUserUses(promotionId, userId, orderId);
		
		return isReleasedPromotionCreated && isUserPromotionUsesDeleted;
	}

	
	private boolean createReleasedPromotion(final int promotionId,final int userId, final BigDecimal discountAmount,final int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Insert in the released_promotion table record for user : " + userId + " , applied promotion id : " + promotionId + " , on order id : " + orderId);
		}
		KeyHolder userUsesKeyHolder = new GeneratedKeyHolder();
		int rowAffected = 0;
		try {
			rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_RELEASED_PROMOTION, new String [] {"id"});
					ps.setInt(1, promotionId);
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

	@Override
	public boolean isPromotionApplicable(int promotionId, int userId, int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Get from the user promotion uses table record for user : " + userId + " , applied promotion id : " + promotionId + " , on order id : " + orderId);
		}
		UserPromotionUsesEntry userPromotionUsesEntry = null;
		try {
			userPromotionUsesEntry = load(promotionId, userId, orderId);
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn("No entry found for userId" + userId + " with promotionId "+ promotionId + " and orderId " + orderId);
			//throw new PlatformException("No entry in user_coupon_uses found for userId" + userId + " with couponId "+ couponId + " and orderId " + orderId);
			return true;
		}
		
		return userPromotionUsesEntry==null ? true : false;
	}
	
	@Override
	public UserPromotionUsesEntry load(int promotionId, int userId, int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Get from the user promotion uses table record for user : " + userId + " , applied promotion id : " + promotionId + " , on order id : " + orderId);
		}
		UserPromotionUsesEntry userPromotionUsesEntry = null;
		try {
			userPromotionUsesEntry = jdbcTemplate.queryForObject(LOAD_USER_ORDER_PROMOTION_QUERY, new Object [] {promotionId, userId, orderId}, new UserOrderPromotionMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn("No entry found for userId" + userId + " with promotionId "+ promotionId + " and orderId " + orderId);
			//throw new PlatformException("No entry in user_coupon_uses found for userId" + userId + " with couponId "+ couponId + " and orderId " + orderId);
			return null;
		}
		
		return userPromotionUsesEntry;
	}
	
	private BigDecimal getDiscountValue(int promotionId, int userId, int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Get from the user promotion uses table record for user : " + userId + " , applied promotion id : " + promotionId + " , on order id : " + orderId);
		}
		UserPromotionUsesEntry userPromotionUsesEntry = null;
		BigDecimal discountValue = null;
		try {
			userPromotionUsesEntry = jdbcTemplate.queryForObject(LOAD_USER_ORDER_PROMOTION_QUERY, new Object [] {promotionId, userId, orderId}, new UserOrderPromotionMapper());
			discountValue = userPromotionUsesEntry.getDiscountAmount().getAmount();
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn("No entry found for userId" + userId + " with promotionId "+ promotionId + " and orderId " + orderId);
			throw new PlatformException("No entry in user_coupon_uses found for userId" + userId + " with promotionId "+ promotionId + " and orderId " + orderId);
		}
		
		if(discountValue==null){
			throw new PlatformException("Error while getting discount value for userId " + userId + " with promotionId "+ promotionId + " and orderId " + orderId);
		}
		return discountValue;
	}
	
	private boolean createUserUses(final int promotionId, final int userId, final BigDecimal valueApplied, final int orderId) {
		
		if(log.isDebugEnabled()) {
			log.debug("Insert in the user_promotion_uses table record for user : " + userId + " , applied promotion id : " + promotionId + " , on order id : " + orderId + " , discount value applied : " + valueApplied );
		}
		int rowAffected = 0;
		KeyHolder userUsesKeyHolder = new GeneratedKeyHolder();
		
		try {
			rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_USER_USES, new String [] {"id"});
					ps.setInt(1, promotionId);
					ps.setInt(2, userId);
					ps.setInt(3, orderId);
					ps.setBigDecimal(4, valueApplied);
					Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
					ps.setTimestamp(5, timestamp);
					ps.setTimestamp(6, timestamp);
					return ps;
				}
			}, userUsesKeyHolder);
		} catch (DuplicateKeyException e) {
			log.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		
		return rowAffected>0 ? true : false;
	}

	private boolean cancelUserUses(final int promotionId, final int userId, final int orderId){
		if(log.isDebugEnabled()) {
			log.debug("Cancelling the promotion id : " + promotionId + ", applied on order id : " + orderId + " for user : " + userId );
		}
		int rowAffected = -1;
		try {
			rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(DELETE_USER_USES);
					ps.setInt(1, promotionId);
					ps.setInt(2, userId);
					ps.setInt(3, orderId);
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
		private boolean isCouponPromotion = false;

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			isCouponPromotion = rs.getBoolean("is_coupon");
			boolean isCouponNull = rs.wasNull(); //existing promotions were all coupon promotions but isCoupon was null which is mapped to false in jdbc
			if (isCouponPromotion || isCouponNull) {
				promotion = new CouponPromotion();
			} else {
				promotion = new AutoPromotion();
			}
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
				throw new PlatformException("Max uses cannot be null or zero. Invalid Promotion data.");
			}
			maxUses = maxUses > 0 ? maxUses : -1;
			config.setMaxUses(maxUses);
			
			int maxUsesPerUser = rs.getInt("max_uses_per_user");
			if(maxUsesPerUser==0){
				//max uses per user cannot be null or zero in the databases
				throw new PlatformException("Max uses per user cannot be null or zero. Invalid Promotion data.");
			}
			maxUsesPerUser = maxUsesPerUser > 0 ? maxUsesPerUser : -1;
			config.setMaxUsesPerUser(maxUsesPerUser);

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

	private static class UserOrderPromotionMapper implements RowMapper<UserPromotionUsesEntry> {

		@Override
		public UserPromotionUsesEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserPromotionUsesEntry releasePromotion = new UserPromotionUsesEntry();

			releasePromotion.setId(rs.getInt("id"));
			releasePromotion.setPromotionId(rs.getInt("promotion_id"));
			releasePromotion.setUserId(rs.getInt("user_Id"));
			releasePromotion.setOrderId(rs.getInt("order_id"));

			BigDecimal discountAmount = rs.getBigDecimal("discount_amount");
			if (discountAmount == null) {
				discountAmount = BigDecimal.ZERO;
			}
			releasePromotion.setDiscountAmount(new Money(discountAmount));
			
			return releasePromotion;
		}
	}
	
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}

	public void setPromotionConfigDao(PromotionConfigDao promotionConfigDao) {
		this.promotionConfigDao = promotionConfigDao;
	}
}
