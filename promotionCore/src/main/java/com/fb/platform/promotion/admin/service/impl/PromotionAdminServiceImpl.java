package com.fb.platform.promotion.admin.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.admin.dao.CouponAdminDao;
import com.fb.platform.promotion.admin.dao.PromotionAdminDao;
import com.fb.platform.promotion.admin.service.PromotionAdminService;
import com.fb.platform.promotion.admin.to.CouponBasicDetails;
import com.fb.platform.promotion.admin.to.CouponTO;
import com.fb.platform.promotion.admin.to.PromotionTO;
import com.fb.platform.promotion.admin.to.RuleConfigItemTO;
import com.fb.platform.promotion.admin.to.SearchCouponOrderBy;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderByOrder;
import com.fb.platform.promotion.admin.to.SortOrder;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;
import com.fb.platform.promotion.service.CouponNotFoundException;
import com.fb.platform.promotion.service.InvalidCouponTypeException;
import com.fb.platform.promotion.service.PromotionService;
import com.fb.platform.promotion.util.CouponCodeCreator;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserBo;

/**
 * 
 * @author neha
 *
 */
public class PromotionAdminServiceImpl implements PromotionAdminService {

	private Log log = LogFactory.getLog(PromotionAdminServiceImpl.class);
	
	@Autowired
	private RuleDao ruleDao = null;

	@Autowired
	private CouponAdminDao couponAdminDao = null;

	@Autowired
	private PromotionAdminDao promotionAdminDao = null;
	
	@Autowired
	public UserAdminDao userAdminDao = null;

	@Autowired
	private PromotionService promotionService = null;

	@Autowired
	private CouponCodeCreator couponCodeCreator = null;

	private static final int COUPON_GENERATION_BATCH_SIZE = 1000;

	@Override
	public List<RulesEnum> getAllPromotionRules() {
		List<RulesEnum> promotionRulesList = ruleDao.getAllPromotionRules();
		return promotionRulesList;
	}
	
	@Override
	public int createPromotion(PromotionTO promotionTO) {
		int isActive = promotionTO.isActive() ? 1 : 0;
		int ruleId = ruleDao.getRuleId(promotionTO.getRuleName());
		int promotionId = 0;
		
		promotionId = promotionAdminDao.createPromotion(promotionTO.getPromotionName(), 
				promotionTO.getDescription(), 
				ruleId, 
				promotionTO.getValidFrom(), 
				promotionTO.getValidTill(),
				isActive);
		if(promotionId > 0) {
			promotionAdminDao.createPromotionLimitConfig(	promotionId, 
					promotionTO.getMaxUses(), 
					promotionTO.getMaxAmount(), 
					promotionTO.getMaxUsesPerUser(), 
					promotionTO.getMaxAmountPerUser());
			
			for(RuleConfigItemTO ruleConfigItemTO : promotionTO.getConfigItems()) {
				promotionAdminDao.createPromotionRuleConfig(ruleConfigItemTO.getRuleConfigName(), 
						ruleConfigItemTO.getRuleConfigValue(), 
						promotionId, 
						ruleId);
			}
			
		}
		
		return promotionId;
		
	}
	
	@Override
	public boolean updatePromotion(PromotionTO promotionTO) {
		int isActive = promotionTO.isActive() ? 1 : 0;
		boolean updateSuccesfull = false;
		int ruleId = ruleDao.getRuleId(promotionTO.getRuleName());
		
		int promotionUpdated = promotionAdminDao.updatePromotion(promotionTO.getPromotionId(), 
				promotionTO.getPromotionName(), 
				promotionTO.getDescription(), 
				promotionTO.getValidFrom(), 
				promotionTO.getValidTill(), 
				isActive,
				ruleId);
		
		updateSuccesfull = (promotionUpdated == 1) ? true : false;
		 
		if(updateSuccesfull) {
			promotionAdminDao.updatePromotionLimitConfig(promotionTO.getPromotionId(), 
					promotionTO.getMaxUses(), 
					promotionTO.getMaxAmount(), 
					promotionTO.getMaxUsesPerUser(), 
					promotionTO.getMaxAmountPerUser());
			
			int rowsDeleted = promotionAdminDao.deletePromotionRuleConfig(promotionTO.getPromotionId());
			
			for(RuleConfigItemTO ruleConfigItemTO : promotionTO.getConfigItems()) {
				if(updateSuccesfull) {
					int promotionRuleUpdated = promotionAdminDao.createPromotionRuleConfig(ruleConfigItemTO.getRuleConfigName(), 
							ruleConfigItemTO.getRuleConfigValue(), 
							promotionTO.getPromotionId(), 
							ruleId);
					updateSuccesfull = (promotionRuleUpdated == 1) ? true : false;
				} else {
					break;
				}
			}
		}
		
		
		return updateSuccesfull;
		
	}
	
	@Override
	public List<PromotionTO> searchPromotion(String promotionName, DateTime validFrom, DateTime validTill, int isActive, SearchPromotionOrderBy orderBy,
			SearchPromotionOrderByOrder order, int startRecord, int batchSize) {
		List<PromotionTO> promotionsList = promotionAdminDao.searchPromotion(promotionName, validFrom, validTill, isActive, orderBy, order, startRecord, batchSize);
		return promotionsList;
	}
	@Override
	public int getPromotionCount(String promotionName, DateTime validFrom, DateTime validTill, int isActive) {
		int promotionsCount = promotionAdminDao.getPromotionCount(promotionName, validFrom, validTill, isActive);
		return promotionsCount;
	}
	
	@Override
	public PromotionTO viewPromotion(int promotionId) {
		PromotionTO promotionCompleteView = promotionAdminDao.viewPromotion(promotionId);
		if(promotionCompleteView != null) {
			int couponCount = promotionAdminDao.getCouponCount(promotionId);
			promotionCompleteView.setCouponCount(couponCount);
		}
		return promotionCompleteView;
	}
	
	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
	
	public void setPromotionAdminDao(PromotionAdminDao promotionAdminDao) {
		this.promotionAdminDao = promotionAdminDao;
	}

	@Override
	public List<String> createCoupons(int count, int length, String startsWith, String endsWith, int promotionId, CouponType type, CouponLimitsConfig limits) {

		//see if the promotion is valid, if not this will throw promotion not found exception
		promotionService.getPromotion(promotionId);

		couponCodeCreator.init(count, length, startsWith, endsWith, COUPON_GENERATION_BATCH_SIZE);

		while (couponCodeCreator.nextBatchAvailable()) {
			List<String> batchOfCouponCodes = couponCodeCreator.getNextBatch();
			createCouponsInBatch(batchOfCouponCodes, promotionId, type, limits);
		}

		return couponCodeCreator.getGeneratedCoupons();
	}

	private void createCouponsInBatch(List<String> batchOfCoupons, int promotionId, CouponType couponType, CouponLimitsConfig limits) {
		couponAdminDao.createCouponsInBatch(batchOfCoupons, promotionId, couponType, limits);
	}

	@Override
	public Set<CouponBasicDetails> searchCoupons(String couponCode, String userName, SearchCouponOrderBy orderBy,
			SortOrder sortOrder, int startRecord, int batchSize){
		// 1) if userId is present then use it to get all
		//		- couponId from the coupon_user table (PRE_ISSUE)
		//		- couponId from the user_coupon_uses table (all kind coupons)
		// combine these two sets of couponIds.
		// 2) Use the above couponId set to get the coupons detail from coupon table
		//		and use the couponCode input if present in request
		int userId = -28; // a random negative value
		boolean isUserValid = false;
		if(StringUtils.isNotBlank(userName)){
			try {
				UserBo userBO = userAdminDao.load(userName);
				if(userBO!=null){
					userId = userBO.getUserid();
					isUserValid = true;
				}
			} catch (Exception e) {
				log.error("Error while getting userId for userName = "+userName, e);
			}
		}
		Set<Integer> allCouponIdsForUser = new HashSet<Integer>(0);
		if(isUserValid){
			allCouponIdsForUser = couponAdminDao.loadAllCouponForUser(userId);
		}
		
		List<CouponBasicDetails> allUserCoupons = new ArrayList<CouponBasicDetails>(0);
		try {
			allUserCoupons = couponAdminDao.searchCoupons(couponCode, allCouponIdsForUser, orderBy, sortOrder, startRecord, batchSize);
		} catch (Exception e) {
			log.error("Error while searching coupon details for userId = "+userId + " couponCode = "+ couponCode, e);
			throw new PlatformException("Error while searching coupon details for userId = "+userId + " couponCode = "+ couponCode, e);
		}
		
		return new HashSet<CouponBasicDetails>(allUserCoupons);
	}
	
	public CouponTO viewCoupons(String couponCode){
		log.info("Viewing coupon using coupon code = "+ couponCode);
		CouponTO couponTO = null;
		try {
			couponTO = couponAdminDao.load(couponCode);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return couponTO;
	}
	
	public CouponTO viewCoupons(int couponId){
		log.info("Viewing coupon using couponId = "+ couponId);
		CouponTO couponTO = couponAdminDao.load(couponId);
		return couponTO;
	}
	
	public void setCouponAdminDao(CouponAdminDao couponAdminDao) {
		this.couponAdminDao = couponAdminDao;
	}

	public void setPromotionService(PromotionService promotionService) {
		this.promotionService = promotionService;
	}

	public void setCouponCodeCreator(CouponCodeCreator couponCodeCreator) {
		this.couponCodeCreator = couponCodeCreator;
	}

	@Override
	public void assignCouponToUser(String couponCode, int userId, int overriddenUserLimit) 
			throws CouponNotFoundException, CouponAlreadyAssignedToUserException, InvalidCouponTypeException {

		try {
			Coupon coupon = couponAdminDao.loadCouponWithoutConfig(couponCode);
			if (coupon == null) {
				throw new CouponNotFoundException("Assigning invalid CouponCode to user. CouponCode : " + couponCode + ". UserId : "  + userId);
			}

			if (!(coupon.getType() == CouponType.PRE_ISSUE)) {
				throw new InvalidCouponTypeException("Only PRE_ISSUE coupons can be assigned to a user. CouponCode : " + couponCode + ". UserId : "  + userId + ". CouponType : " + coupon.getType());
			}

			couponAdminDao.assignToUser(userId, couponCode, overriddenUserLimit);

		} catch (DataAccessException e) {
			throw new PlatformException("Exception while assigning Coupon to user. CouponCode : " + couponCode + ". UserId : "  + userId, e);
		}
	}

	public void setUserAdminDao(UserAdminDao userAdminDao) {
		this.userAdminDao = userAdminDao;
	}
}
