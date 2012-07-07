/**
 * 
 */
package com.fb.platform.promotion.admin.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.admin.to.CouponBasicDetails;
import com.fb.platform.promotion.admin.to.CouponTO;
import com.fb.platform.promotion.admin.to.SearchCouponOrderBy;
import com.fb.platform.promotion.admin.to.SortOrder;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.exception.CouponAlreadyAssignedToUserException;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;

/**
 * @author vinayak
 *
 */
public class CouponAdminDaoTest extends BaseTestCase {

	@Autowired
	private CouponAdminDao couponAdminDao;

	@Autowired
	private CouponDao couponDao;

	@Test
	public void loadCouponWithoutConfig() {
		Coupon coupon = couponAdminDao.loadCouponWithoutConfig("global_coupon_1");

		assertNotNull(coupon);
		assertEquals("global_coupon_1", coupon.getCode());
		assertEquals(CouponType.GLOBAL, coupon.getType());
	}

	@Test
	public void loadPreIssueCoupon() {
		Coupon coupon = couponAdminDao.loadCouponWithoutConfig("pre_issued_1");

		assertNotNull(coupon);
		assertEquals("pre_issued_1", coupon.getCode());
		assertEquals(CouponType.PRE_ISSUE, coupon.getType());
	}

	@Test
	public void loadInvalidCouponCode() {
		Coupon coupon = couponAdminDao.loadCouponWithoutConfig("i_dont_exists_sorry");

		assertNull(coupon);
	}

	@Test
	public void assignToUser() {
		couponAdminDao.assignToUser(-4, "pre_issued_1", 0);

		//try to load the newly assigned coupon for this user, should load fine
		Coupon coupon = couponDao.load("pre_issued_1", -4);

		assertNotNull(coupon);
	}

	@Test
	public void loadAllCouponForUser(){
		Set<Integer> couponsForUser = couponAdminDao.loadAllCouponForUser(-2);
		
		assertEquals(3, couponsForUser.size());
	}
	
	@Test
	public void searchCoupon(){
		Set<Integer> allCouponIdsForUser = couponAdminDao.loadAllCouponForUser(-2);
		List<CouponBasicDetails> couponsForUser = couponAdminDao.searchCoupons(null, allCouponIdsForUser, SearchCouponOrderBy.COUPON_CODE, SortOrder.ASCENDING, 0, 10);
		
		assertNotNull(couponsForUser);
		assertEquals(3, couponsForUser.size());
		
		for (CouponBasicDetails eachCouponBasicDetails : couponsForUser) {
			switch(eachCouponBasicDetails.getCouponId()){
				case -1: {
					assertEquals("global_coupon_1", eachCouponBasicDetails.getCouponCode());
					assertEquals(CouponType.GLOBAL, eachCouponBasicDetails.getCouponType());
					break;
				}
				case -2: {
					assertEquals("pre_issued_1", eachCouponBasicDetails.getCouponCode());
					assertEquals(CouponType.PRE_ISSUE, eachCouponBasicDetails.getCouponType());
					break;
				}
				case -3: {
					assertEquals("post_issued_1", eachCouponBasicDetails.getCouponCode());
					assertEquals(CouponType.POST_ISSUE, eachCouponBasicDetails.getCouponType());
					break;
				}
				default: assertNotNull(null);
			}
		}
	}
	
	@Test
	public void searchCouponNoOrderByInput(){
		Set<Integer> allCouponIdsForUser = couponAdminDao.loadAllCouponForUser(-2);
		List<CouponBasicDetails> couponsForUser = couponAdminDao.searchCoupons(null, allCouponIdsForUser, null, SortOrder.ASCENDING, 0, 10);
		
		assertNotNull(couponsForUser);
		assertEquals(3, couponsForUser.size());
		
		for (CouponBasicDetails eachCouponBasicDetails : couponsForUser) {
			switch(eachCouponBasicDetails.getCouponId()){
				case -1: {
					assertEquals("global_coupon_1", eachCouponBasicDetails.getCouponCode());
					assertEquals(CouponType.GLOBAL, eachCouponBasicDetails.getCouponType());
					break;
				}
				case -2: {
					assertEquals("pre_issued_1", eachCouponBasicDetails.getCouponCode());
					assertEquals(CouponType.PRE_ISSUE, eachCouponBasicDetails.getCouponType());
					break;
				}
				case -3: {
					assertEquals("post_issued_1", eachCouponBasicDetails.getCouponCode());
					assertEquals(CouponType.POST_ISSUE, eachCouponBasicDetails.getCouponType());
					break;
				}
				default: assertNotNull(null);
			}
		}
	}
	
	@Test
	public void loadCOuponByID() {
		CouponTO couponTO = couponAdminDao.load(-5);
		
		assertNotNull(couponTO);
		assertEquals(-5, couponTO.getCouponId());
		assertEquals("preIssuedNoCouponUserEntry", couponTO.getCouponCode());
		assertEquals(CouponType.PRE_ISSUE, couponTO.getCouponType());
		assertEquals(-5, couponTO.getPromotionId());
		assertEquals(0, couponTO.getMaxAmount().compareTo(new Money(new BigDecimal(17000.00))));
		assertEquals(0, couponTO.getMaxAmountPerUser().compareTo(new Money(new BigDecimal(6000.00))));
		assertEquals(170, couponTO.getMaxUses());
		assertEquals(20, couponTO.getMaxUsesPerUser());
		/*assertEquals("2012-01-01:00:00:00", couponTO.getCreatedOn().toString("yyyy-mm-dd:HH:mm:ss"));
		assertEquals("2012-05-31:00:00:00", couponTO.getLastModifiedOn().toString("yyyy-mm-dd:HH:mm:ss"));*/
		
	}
	
	@Test
	public void loadByCouponCode() {
		CouponTO couponTO = couponAdminDao.load("preIssuedNoCouponUserEntry");
		
		assertNotNull(couponTO);
		assertEquals(-5, couponTO.getCouponId());
		assertEquals("preIssuedNoCouponUserEntry", couponTO.getCouponCode());
		assertEquals(CouponType.PRE_ISSUE, couponTO.getCouponType());
		assertEquals(-5, couponTO.getPromotionId());
		assertEquals(0, couponTO.getMaxAmount().compareTo(new Money(new BigDecimal(17000.00))));
		assertEquals(0, couponTO.getMaxAmountPerUser().compareTo(new Money(new BigDecimal(6000.00))));
		assertEquals(170, couponTO.getMaxUses());
		assertEquals(20, couponTO.getMaxUsesPerUser());
		/*assertEquals("2012-01-01:00:00:00", couponTO.getCreatedOn().toString("yyyy-mm-dd:HH:mm:ss"));
		assertEquals("2012-05-31:00:00:00", couponTO.getLastModifiedOn().toString("yyyy-mm-dd:HH:mm:ss"));*/
		
	}
	
	@Test
	public void createCouponsInBatch() {
		CouponTO couponTO = couponAdminDao.load(-2100);
		CouponLimitsConfig couponLimitsConfig = new CouponLimitsConfig();
		couponLimitsConfig.setMaxAmount(couponTO.getMaxAmount());
		couponLimitsConfig.setMaxAmountPerUser(couponTO.getMaxAmountPerUser());
		couponLimitsConfig.setMaxUses(couponTO.getMaxUses());
		couponLimitsConfig.setMaxUsesPerUser(couponTO.getMaxUsesPerUser());
		
		List<String> batchOfCoupons = new ArrayList<String>();
		batchOfCoupons.add("jffbs12");
		batchOfCoupons.add("jffbs12dsdffg");
		batchOfCoupons.add("jffbs12sdf434r");
		batchOfCoupons.add("jffbs12bgbd");
		couponAdminDao.createCouponsInBatch(batchOfCoupons, -2100, CouponType.PRE_ISSUE, couponLimitsConfig);
		
		Coupon coupon1 = couponAdminDao.loadCouponWithoutConfig("jffbs12");
		assertNotNull(coupon1);
		assertEquals(CouponType.PRE_ISSUE, coupon1.getType());
		assertEquals(coupon1.getCode(), "jffbs12");
		
		Coupon coupon2 = couponAdminDao.loadCouponWithoutConfig("jffbs12dsdffg");
		assertNotNull(coupon2);
		assertEquals(CouponType.PRE_ISSUE, coupon2.getType());
		assertEquals(coupon2.getCode(), "jffbs12dsdffg");
		
		Coupon coupon3 = couponAdminDao.loadCouponWithoutConfig("jffbs12sdf434r");
		assertNotNull(coupon3);
		assertEquals(CouponType.PRE_ISSUE, coupon3.getType());
		assertEquals(coupon3.getCode(), "jffbs12sdf434r");
		
		Coupon coupon4 = couponAdminDao.loadCouponWithoutConfig("jffbs12bgbd");
		assertNotNull(coupon4);
		assertEquals(CouponType.PRE_ISSUE, coupon4.getType());
		assertEquals(coupon4.getCode(), "jffbs12bgbd");
		
		CouponTO createdCouponTO = couponAdminDao.load("jffbs12");
		
		assertNotNull(createdCouponTO);
		assertEquals("jffbs12", createdCouponTO.getCouponCode());
		assertEquals(CouponType.PRE_ISSUE, createdCouponTO.getCouponType());
		assertEquals(-2100, createdCouponTO.getPromotionId());
		assertEquals(0, createdCouponTO.getMaxAmount().compareTo(new Money(new BigDecimal(4500.00))));
		assertEquals(0, createdCouponTO.getMaxAmountPerUser().compareTo(new Money(new BigDecimal(900.00))));
		assertEquals(5, createdCouponTO.getMaxUses());
		assertEquals(1, createdCouponTO.getMaxUsesPerUser());
		
	}
	
	@Test
	public void countCouponForUser(){
		int count = couponAdminDao.countCoupons(-2);
		
		assertEquals(3, count);
	}
	
	@Test(expected = CouponAlreadyAssignedToUserException.class)
	public void reassign() {
		couponAdminDao.assignToUser(-2, "pre_issued_1", 0);
	}

	@Test(expected = CouponAlreadyAssignedToUserException.class)
	public void assignToInvalidUser() {
		couponAdminDao.assignToUser(-2800, "pre_issued_1", 0);
	}

	@Test(expected = DataAccessException.class)
	public void assignInvalidCoupon() {
		couponAdminDao.assignToUser(-2, "i_am_not_there_in_db", 0);
	}

}
