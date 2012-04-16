/**
 * 
 */
package com.fb.platform.promotion.admin.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.admin.dao.CouponAdminDao;
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
}
