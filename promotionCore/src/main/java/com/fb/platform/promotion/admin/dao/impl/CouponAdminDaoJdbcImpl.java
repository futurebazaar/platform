/**
 * 
 */
package com.fb.platform.promotion.admin.dao.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.admin.dao.CouponAdminDao;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.admin.to.CouponBasicDetails;
import com.fb.platform.promotion.admin.to.CouponTO;
import com.fb.platform.promotion.admin.to.SearchCouponOrderBy;
import com.fb.platform.promotion.admin.to.SortOrder;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;
import com.fb.platform.promotion.service.CouponNotFoundException;

/**
 * @author vinayak
 *
 */
public class CouponAdminDaoJdbcImpl implements CouponAdminDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(CouponAdminDaoJdbcImpl.class);

	private static final String LOAD_COUPON_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_code, " +
			"	promotion_id, " +
			"	created_on, " +
			"	last_modified_on, " +
			"	coupon_type " +
			"FROM coupon WHERE coupon_code = ?";

	private static final String LOAD_COUPON_BY_ID_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_code, " +
			"	promotion_id, " +
			"	created_on, " +
			"	last_modified_on, " +
			"	coupon_type " +
			"FROM coupon WHERE id = ?";
	
	private static final String LOAD_COUPON_ID_QUERY = 
			"SELECT " +
			"	id " +
			"FROM coupon WHERE coupon_code = ?";

	private static final String LOAD_COUPON_DATA_ONLY_QUERY = "" +
			"SELECT " +
			"	coupon_code," +
			"	coupon_type " +
			"FROM coupon " +
			"WHERE coupon_code = ?";


	private static final String LOAD_COUPON_LIMITS_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_id, " +
			"	max_uses, " +
			"	max_amount, " +
			"	max_uses_per_user, " +
			"	max_amount_per_user " +
			"FROM coupon_limits_config WHERE coupon_id = ?";
	
	private static final String ASSGIN_COUPON_TO_USER = 
			"INSERT INTO platform_coupon_user (" +
			"	coupon_id, " +
			"	user_id, " +
			"	override_user_uses_limit) " +
			"VALUES (?, ?, ?)";

	private static final String COUPON_CODE_PLACEHOLDER = "COUPON_CODE_LIST";

	private static final String COUPON_ID_PLACEHOLDER = "COUPON_ID_LIST";
	
	private static final String SELECT_EXISTING_COUPON_CODES_QUERY = 
			"SELECT " +
			"	coupon_code " +
			"FROM coupon " +
			"WHERE coupon_code in (" + COUPON_CODE_PLACEHOLDER + ")";

	private static final String APPOSTROPHE = "'";
	private static final String COMMA = ",";

	private static final String CREATE_COUPON_SQL = 
			"INSERT INTO " +
			"	coupon " +
			"		(created_on, " +
			"		last_modified_on, " +
			"		coupon_code, " +
			"		promotion_id, " +
			"		coupon_type) " +
			"VALUES (?, ?, ?, ?, ?)";

	private static final String CREATE_COUPON_LIMIT_SQL = 
			"INSERT INTO " +
			"	coupon_limits_config " +
			"		(coupon_id, " +
			"		max_uses, " +
			"		max_amount, " +
			"		max_uses_per_user, " +
			"		max_amount_per_user) " +
			"VALUES (?, ?, ?, ?, ?)";

	private static final String LOAD_PRE_ISSUE_COUPON_USER_QUERY = 
			"SELECT " +
			"	coupon_id " +
			"FROM platform_coupon_user WHERE user_id = ?";

	private static final String LOAD_USED_COUPON_USER_QUERY = 
			"SELECT " +
			"	coupon_id " +
			"FROM user_coupon_uses WHERE user_id = ?";

	private static final String SELECT_USER_COUPON_QUERY = 
			"SELECT " +
			"	id, " +
			"	coupon_code, " +
			"	coupon_type " +			
			"FROM coupon WHERE ";

	private static String AND_JOINT = " AND ";
	
	private static String SELECT_COUPON_CODE_FILTER_SQL = 
			" coupon_code LIKE ? ";
	
	private static String SELECT_COUPON_ID_FILTER_SQL = 
			" id IN (" + COUPON_ID_PLACEHOLDER +") ";
	
	private static String ORDER_BY_CLAUSE = 
			" ORDER BY ";
	
	private static String ORDER_BY_COUPON_CODE = " coupon_code ";
	private static String ORDER_BY_COUPON_TYPE = " coupon_type ";
	
	private static String ORDER_BY_ASCENDING =
			" ASC ";
	
	private static String ORDER_BY_DESCENDING =
			" DESC ";
	
	private static String LIMIT_FILTER_SQL = 
			" LIMIT ?,? ";
	
	@Override
	public void assignToUser(int userId, String couponCode, int overriddenUserLimit) {
		try {
			int couponId = jdbcTemplate.queryForInt(LOAD_COUPON_ID_QUERY, couponCode);

			int rowsUpdated = jdbcTemplate.update(ASSGIN_COUPON_TO_USER, couponId, userId, overriddenUserLimit);
			if (rowsUpdated != 1) {
				throw new PlatformException("Unable to assign couponId to user. CouponId : " + couponId + ", userId : " + userId);
			}
		} catch (DataIntegrityViolationException e) {
			//this means the user already has access to this coupon.
			throw new CouponAlreadyAssignedToUserException("Coupon is already assigned to user. couponCode : " + couponCode + ", userId : " + userId);
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<String> findExistingCodes(List<String> newCodes) {
		String couponCodesForQuery = buildCouponCodeStringForQuery(newCodes);
		String queryString = SELECT_EXISTING_COUPON_CODES_QUERY.replace(COUPON_CODE_PLACEHOLDER, couponCodesForQuery);

		List<String> existingCodes = jdbcTemplate.queryForList(queryString, String.class);
		return existingCodes;
	}

	private String buildCouponCodeStringForQuery(List<String> couponCodes) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (String code : couponCodes) {
			sb.append(APPOSTROPHE);
			sb.append(code);
			sb.append(APPOSTROPHE);
			count ++;
			if (count != couponCodes.size()) {
				sb.append(COMMA);
			}
		}
		return sb.toString();
	}

	@Override
	public void createCouponsInBatch(List<String> couponCodes, int promotionId, CouponType couponType, CouponLimitsConfig limitsConfig) {
		
		try {
			jdbcTemplate.batchUpdate(CREATE_COUPON_SQL, new CreateCouponBatchPSSetter(couponCodes, promotionId, couponType));
		} catch (DataAccessException e) {
			log.error("Error while doing batch update for promotion id = "+promotionId+" and coupon code batch size = "+couponCodes.size());
			throw new PlatformException("Unable to do batch update for creating coupons of batch size: " + couponCodes.size() + ", promotion id = "+promotionId);
		}
		//get the first coupon code committed and find its couponId
		// use this couponId to deduce the rest of the batch couponIds
		String firstCouponCode = couponCodes.get(0);
		int couponId = jdbcTemplate.queryForInt(LOAD_COUPON_ID_QUERY, firstCouponCode);
		
		try {
			jdbcTemplate.batchUpdate(CREATE_COUPON_LIMIT_SQL, new CreateCouponLimitsBatchPSSetter(couponId, couponCodes.size(), limitsConfig));
		} catch (DataAccessException e) {
			log.error("Error while doing batch update for promotion id = "+promotionId+" and coupon limits batch size = "+couponCodes.size());
			throw new PlatformException("Unable to do batch update for creating coupon limits of batch size: " + couponCodes.size() + ", promotion id = "+promotionId);
		}
	}

	@Override
	public Coupon loadCouponWithoutConfig(String couponCode) {
		try {
			Coupon coupon = jdbcTemplate.queryForObject(LOAD_COUPON_DATA_ONLY_QUERY, new SimpleCouponMapper(), couponCode);
			return coupon;
		} catch (IncorrectResultSizeDataAccessException e) {
			//this coupon code does not exist in the DB
			return null;
		}
	}

	private static class SimpleCouponMapper implements RowMapper<Coupon> {

		@Override
		public Coupon mapRow(ResultSet rs, int rowNum) throws SQLException {
			Coupon coupon = new Coupon();
			coupon.setCode(rs.getString("coupon_code"));
			coupon.setType(CouponType.valueOf(rs.getString("coupon_type")));
			return coupon;
		}
	}

	private static class CreateCouponBatchPSSetter implements BatchPreparedStatementSetter {

		private List<String> couponCodes;
		private int promotionId;
		private CouponType couponType;
		
		public CreateCouponBatchPSSetter(List<String> couponCodes, int promotionId, CouponType couponType) {
			this.couponCodes = couponCodes;
			this.promotionId = promotionId;
			this.couponType = couponType;
		}
		
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
			ps.setTimestamp(1, timestamp);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, couponCodes.get(i));
			ps.setInt(4, promotionId);
			ps.setString(5, couponType.name());
		}

		@Override
		public int getBatchSize() {
			return couponCodes.size();
		}
	}
	
	private static class CreateCouponLimitsBatchPSSetter implements BatchPreparedStatementSetter {

		private int couponID;
		private int batchSize;
		private CouponLimitsConfig couponLimitsConfig;
		
		public CreateCouponLimitsBatchPSSetter(int couponID, int batchSize, CouponLimitsConfig couponLimitsConfig) {
			this.couponID = couponID;
			this.batchSize = batchSize;
			this.couponLimitsConfig = couponLimitsConfig;
		}
		
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			ps.setInt(1, couponID + i);
			ps.setInt(2, couponLimitsConfig.getMaxUses());
			ps.setBigDecimal(3, couponLimitsConfig.getMaxAmount().getAmount());
			ps.setInt(4, couponLimitsConfig.getMaxUsesPerUser());
			ps.setBigDecimal(5, couponLimitsConfig.getMaxAmountPerUser().getAmount());
		}

		@Override
		public int getBatchSize() {
			return batchSize;
		}
	}
	
	@Override
	public Set<Integer> loadAllCouponForUser(int userId){
		log.info("Getting all coupon Ids for userId = " + userId);
		
		List<Integer> preIssueCouponIds = new ArrayList<Integer>(0);
		try {
			preIssueCouponIds = jdbcTemplate.query(LOAD_PRE_ISSUE_COUPON_USER_QUERY, new Object [] {userId}, new CouponIdMapper());
		} catch (DataAccessException e) {
			if (log.isDebugEnabled()) {
				log.warn("Error getting pre issue coupon IDs for userId = " + userId, e);
			}
		}
		
		List<Integer> usedCouponIds = new ArrayList<Integer>(0);
		try {
			usedCouponIds = jdbcTemplate.query(LOAD_USED_COUPON_USER_QUERY, new Object [] {userId}, new CouponIdMapper());
		} catch (DataAccessException e) {
			if (log.isDebugEnabled()) {
				log.warn("Error getting used coupon IDs for userId = " + userId, e);
			}
		}
		
		Set<Integer> allCouponIds = new HashSet<Integer>(preIssueCouponIds.size() + usedCouponIds.size());
		allCouponIds.addAll(preIssueCouponIds);
		allCouponIds.addAll(usedCouponIds);
		
		return allCouponIds;
	
	}
	
	private static class CouponIdMapper implements RowMapper<Integer> {

		@Override
		public Integer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			return resultSet.getInt("coupon_id");
		}
	}
	
	@Override
	public List<CouponBasicDetails> searchCoupons(String couponCode, Set<Integer> allCouponIdsForUser, SearchCouponOrderBy orderBy,
			SortOrder sortOrder, int startRecord, int batchSize){
		log.info("Search coupon with details => couponCode:" + couponCode + " , allCouponIdsForUser:" + allCouponIdsForUser + " ,startRecord:" + " ,batchSize:" + batchSize);
		List<String> searchCouponFilterList = new ArrayList<String>();
		String searchCouponQuery = SELECT_USER_COUPON_QUERY;
		List<Object> args = new ArrayList<Object>();
		
		if(StringUtils.isNotBlank(couponCode)) {
			searchCouponFilterList.add(SELECT_COUPON_CODE_FILTER_SQL);
			args.add("%" + couponCode + "%");
		}
		
		StringBuilder commaSeparatedCouponIds = new StringBuilder();
		for (Integer couponId : allCouponIdsForUser) {
			String couponIdString = couponId.toString();
			if(StringUtils.isNotBlank(couponIdString)){
				commaSeparatedCouponIds.append(couponIdString).append(COMMA);
			}
		}
		
		if(StringUtils.isNotBlank(commaSeparatedCouponIds.toString())){
			String commaSeparatedCouponIdsClean = commaSeparatedCouponIds.toString();
			if(commaSeparatedCouponIds.toString().endsWith(COMMA)){
				commaSeparatedCouponIdsClean = commaSeparatedCouponIds.substring(0, commaSeparatedCouponIds.length()-1);
			}
			SELECT_COUPON_ID_FILTER_SQL = SELECT_COUPON_ID_FILTER_SQL.replace(COUPON_ID_PLACEHOLDER, commaSeparatedCouponIdsClean);
			searchCouponFilterList.add(SELECT_COUPON_ID_FILTER_SQL);
		}
		
		searchCouponQuery += (StringUtils.join(searchCouponFilterList.toArray(), AND_JOINT));
	
		searchCouponQuery = searchCouponOrderByClause(orderBy, searchCouponQuery);
		
		searchCouponQuery = searchCouponSortByClause(sortOrder, searchCouponQuery);
	
		searchCouponQuery += LIMIT_FILTER_SQL;
		args.add(startRecord);
		args.add(batchSize);
		
		List<CouponBasicDetails> couponsList = jdbcTemplate.query(searchCouponQuery, args.toArray(), new CouponBasicDetailMapper());
		
		
		return couponsList;
	}

	private String searchCouponSortByClause(SortOrder sortOrder, String searchCouponQuery) {
		if(sortOrder != null) {
			switch (sortOrder) {
			case ASCENDING:
				searchCouponQuery += ORDER_BY_ASCENDING;
				break;
			case DESCENDING:
				searchCouponQuery += ORDER_BY_DESCENDING;
				break;
			default:
				break;
			}
		} else {
			searchCouponQuery += ORDER_BY_ASCENDING;;
		}
		return searchCouponQuery;
	}

	private String searchCouponOrderByClause(SearchCouponOrderBy orderBy, String searchCouponQuery) {
		if(orderBy != null) {
			switch(orderBy) {
			case COUPON_CODE :
				searchCouponQuery += (ORDER_BY_CLAUSE + ORDER_BY_COUPON_CODE);
				break;
			case COUPON_TYPE :
				searchCouponQuery += (ORDER_BY_CLAUSE + ORDER_BY_COUPON_TYPE);
				break;
			default:
				searchCouponQuery += (ORDER_BY_CLAUSE + ORDER_BY_COUPON_CODE);
				break;
			}
		} else {
			searchCouponQuery += (ORDER_BY_CLAUSE + ORDER_BY_COUPON_CODE);
		}
		return searchCouponQuery;
	}
	
	private static class CouponBasicDetailMapper implements RowMapper<CouponBasicDetails> {

		@Override
		public CouponBasicDetails mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			CouponBasicDetails couponBasicDetails = new CouponBasicDetails();
			couponBasicDetails.setCouponCode(resultSet.getString("coupon_code"));
			couponBasicDetails.setCouponId(resultSet.getInt("id"));
			couponBasicDetails.setCouponType(CouponType.valueOf(resultSet.getString("coupon_type")));
			return couponBasicDetails;
		}
	}
	
	@Override
	public CouponTO load(int couponId) {
		CouponTO coupon = null;
		log.info("Getting the coupon details for the coupon ID : " + couponId);
		try {
			coupon = jdbcTemplate.queryForObject(LOAD_COUPON_BY_ID_QUERY, new Object [] {couponId}, new CouponMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//this means we dont have any coupon with this coupon code in DB
			log.warn("Coupon not found for couponId - " + couponId);
			throw new CouponNotFoundException("Coupon not found for couponId - " + couponId, e);
		}

		CouponLimitsConfig limitsConfig = null;
		log.info("Getting the coupon maximum limit for the coupon ID : " + couponId);
		try {
			limitsConfig = jdbcTemplate.queryForObject(LOAD_COUPON_LIMITS_QUERY, new Object[] {coupon.getCouponId()}, new CouponLimitsConfigMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			log.error("Coupon Limits are not configured for the coupon ID - " + couponId);
			throw new PlatformException("Coupon Limits are not configured for the coupon ID - " + couponId, e);
		}
		coupon.setMaxAmount(limitsConfig.getMaxAmount());
		coupon.setMaxAmountPerUser(limitsConfig.getMaxAmountPerUser());
		coupon.setMaxUses(limitsConfig.getMaxUses());
		coupon.setMaxUsesPerUser(limitsConfig.getMaxUsesPerUser());

		return coupon;
	}
	
	@Override
	public CouponTO load(String couponCode) {
		CouponTO coupon = null;
		log.debug("Getting the coupon details for the coupon code : " + couponCode);
		try {
			coupon = jdbcTemplate.queryForObject(LOAD_COUPON_QUERY, new Object [] {couponCode}, new CouponMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			//this means we dont have any coupon with this coupon code in DB
			log.warn("Coupon not found for couponCode - " + couponCode);
			throw new CouponNotFoundException("Coupon not found for couponCode - " +couponCode, e);
		}

		CouponLimitsConfig limitsConfig = null;
		if(log.isDebugEnabled()) {
			log.debug("Getting the coupon maximum limit for the coupon code : " + couponCode);
		}
		try {
			limitsConfig = jdbcTemplate.queryForObject(LOAD_COUPON_LIMITS_QUERY, new Object[] {coupon.getCouponId()}, new CouponLimitsConfigMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			log.error("Coupon Limits are not configured for the coupon code - " + couponCode);
			throw new PlatformException("Coupon Limits are not configured for the coupon code - " + couponCode, e);
		}
		coupon.setMaxAmount(limitsConfig.getMaxAmount());
		coupon.setMaxAmountPerUser(limitsConfig.getMaxAmountPerUser());
		coupon.setMaxUses(limitsConfig.getMaxUses());
		coupon.setMaxUsesPerUser(limitsConfig.getMaxUsesPerUser());

		return coupon;
	}
	
	private static class CouponMapper implements RowMapper<CouponTO> {

		@Override
		public CouponTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CouponTO coupon = new CouponTO();

			coupon.setCouponId(rs.getInt("id"));
			coupon.setPromotionId(rs.getInt("promotion_id"));
			coupon.setCouponCode(rs.getString("coupon_code"));
			coupon.setCouponType(CouponType.valueOf(rs.getString("coupon_type")));
			coupon.setCreatedOn(new DateTime(rs.getTimestamp("created_on")));
			coupon.setCreatedOn(new DateTime(rs.getTimestamp("last_modified_on")));
			
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
}
