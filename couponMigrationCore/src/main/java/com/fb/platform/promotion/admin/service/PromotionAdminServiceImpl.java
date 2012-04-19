/**
 * 
 */
package com.fb.platform.promotion.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.admin.dao.CouponAdminDao;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;

/**
 * @author vinayak
 *
 */
public class PromotionAdminServiceImpl implements PromotionAdminService {

	@Autowired
	private CouponAdminDao couponAdminDao = null;

	@Override
	public void assignCouponToUser(int userId, String couponCode, int overriddenUserLimit) throws CouponAlreadyAssignedToUserException {
		try {
			couponAdminDao.assignToUser(userId, couponCode, overriddenUserLimit);
		} catch (DataAccessException e) {
			throw new PlatformException("Error while assigning couponCode : " + couponCode + " to userId : " + userId, e);
		}
	}

	public void setCouponAdminDao(CouponAdminDao couponAdminDao) {
		this.couponAdminDao = couponAdminDao;
	}
}
