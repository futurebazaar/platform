/**
 * 
 */
package com.fb.platform.promotion.admin.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.admin.dao.CouponAdminDao;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;

/**
 * @author vinayak
 *
 */
public class CouponAdminDaoJdbcImpl implements CouponAdminDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(CouponAdminDaoJdbcImpl.class);

	private static final String LOAD_COUPON_ID_QUERY = 
			"SELECT " +
			"	id " +
			"FROM coupon WHERE coupon_code = ?";

	private static final String ASSGIN_COUPON_TO_USER = 
			"INSERT INTO platform_coupon_user (" +
			"	coupon_id, " +
			"	user_id, " +
			"	override_user_uses_limit) " +
			"VALUES (?, ?, ?)";

	private static final String COUPON_CODE_PLACEHOLDER = "COUPON_CODE_LIST";

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
			if (!(count == couponCodes.size())) {
				sb.append(COMMA);
			}
		}
		return sb.toString();
	}

	@Override
	public void createCouponsInBatch(List<String> couponCodes, int promotionId, CouponType couponType, CouponLimitsConfig limitsConfig) {
		
		
	}

	private static class CreateCouponBatchPSSetter implements BatchPreparedStatementSetter {

		public CreateCouponBatchPSSetter(List<String> couponCodes, int promotionId, CouponType couponType) {
			
		}
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			// TODO Auto-generated method stub
		}

		@Override
		public int getBatchSize() {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
