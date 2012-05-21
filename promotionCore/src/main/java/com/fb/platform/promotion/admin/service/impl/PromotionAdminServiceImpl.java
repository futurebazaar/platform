package com.fb.platform.promotion.admin.service.impl;

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
import com.fb.platform.promotion.admin.service.NoDataFoundException;
import com.fb.platform.promotion.admin.service.PromotionAdminService;
import com.fb.platform.promotion.admin.to.CouponBasicDetails;
import com.fb.platform.promotion.admin.to.CouponTO;
import com.fb.platform.promotion.admin.to.PromotionTO;
import com.fb.platform.promotion.admin.to.RuleConfigItemTO;
import com.fb.platform.promotion.admin.to.SearchCouponOrderBy;
import com.fb.platform.promotion.admin.to.SearchCouponResultBO;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin.to.SortOrder;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;
import com.fb.platform.promotion.service.CouponNotFoundException;
import com.fb.platform.promotion.service.InvalidCouponTypeException;
import com.fb.platform.promotion.service.PromotionNotFoundException;
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

	private static final int COUPON_GENERATION_BATCH_SIZE = 1000;

	@Override
	public List<RulesEnum> getAllPromotionRules() {
		
		List<RulesEnum> promotionRulesList = null;
		
		try {
			promotionRulesList = ruleDao.getAllPromotionRules();
		} catch (DataAccessException e) {
			log.error("Error while fetching promotion rules.",e);
			throw new PlatformException("Exception while fetching promotion rules", e);
		}
		
		return promotionRulesList;
	}
	
	@Override
	public int createPromotion(PromotionTO promotionTO) {
		int isActive = promotionTO.isActive() ? 1 : 0;
		int ruleId = ruleDao.getRuleId(promotionTO.getRuleName());
		int promotionId = 0;
		
		try {
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
			} else {
				throw new PlatformException("Unable to create new promotion");
			}
		} catch (DataAccessException e) {
			log.error("DataAccess Exception while creating promotion.", e);
			throw new PlatformException("Exception while creating new promotion", e);
		}
		return promotionId;
	}
	
	@Override
	public void updatePromotion(PromotionTO promotionTO) throws PromotionNotFoundException {

		//see if the promotion is valid, this will throw promotion not found exception
		promotionService.getPromotion(promotionTO.getId());

		try {
			int ruleId = ruleDao.getRuleId(promotionTO.getRuleName());
			
			promotionAdminDao.updatePromotion(promotionTO.getId(), 
					promotionTO.getPromotionName(), 
					promotionTO.getDescription(), 
					promotionTO.getValidFrom(), 
					promotionTO.getValidTill(), 
					promotionTO.isActive(),
					ruleId);

			promotionAdminDao.updatePromotionLimitConfig(promotionTO.getId(), 
					promotionTO.getMaxUses(), 
					promotionTO.getMaxAmount(), 
					promotionTO.getMaxUsesPerUser(), 
					promotionTO.getMaxAmountPerUser());
				
			promotionAdminDao.deletePromotionRuleConfig(promotionTO.getId());
				
			for(RuleConfigItemTO ruleConfigItemTO : promotionTO.getConfigItems()) {
					promotionAdminDao.createPromotionRuleConfig(ruleConfigItemTO.getRuleConfigName(), 
							ruleConfigItemTO.getRuleConfigValue(), 
							promotionTO.getId(), 
							ruleId);
			}
		} catch (DataAccessException e) {
			log.error("DataAccess Exception while updating the promotion, promotionId : " + promotionTO.getId(), e);
			throw new PlatformException("DataAccess Exception while updating the promotion, promotionId : " + promotionTO.getId(), e);
		}
	}
	
	@Override
	public List<PromotionTO> searchPromotion(String promotionName, DateTime validFrom, DateTime validTill, int isActive, SearchPromotionOrderBy orderBy,
			SortOrder order, int startRecord, int batchSize) throws NoDataFoundException{
		
		List<PromotionTO> promotionsList = null;
		
		try {
			promotionsList = promotionAdminDao.searchPromotion(promotionName, validFrom, validTill, isActive, orderBy, order, startRecord, batchSize);
			if(promotionsList == null || promotionsList.size() == 0) {
				log.error("No promotions found for the search criteria promotionName : " + promotionName + " ,validFrom : " + validFrom + " ,validTill : " + validTill + " ,isActive : " + isActive );
				throw new NoDataFoundException("No promotions found for the search criteria promotionName : " + promotionName + " ,validFrom : " + validFrom + " ,validTill : " + validTill + " ,isActive : " + isActive );
			}
		} catch (DataAccessException e) {
			log.error("Exception while searching for promotion.", e);
			throw new PlatformException("Exception while searching for promotion.", e);
		}
		
		return promotionsList;
	}
	@Override
	public int getPromotionCount(String promotionName, DateTime validFrom, DateTime validTill, int isActive) {
		int promotionsCount = 0;
		try {
			promotionsCount = promotionAdminDao.getPromotionCount(promotionName, validFrom, validTill, isActive);
		} catch (DataAccessException e) {
			log.error("Exception while fetching promotion count.",e);
			throw new PlatformException("Exception while fetching promotion count.", e);
		}
		return promotionsCount;
	}
	
	@Override
	public PromotionTO viewPromotion(int promotionId) {
		PromotionTO promotionCompleteView = null;
		try {
			promotionCompleteView = promotionAdminDao.viewPromotion(promotionId);
		} catch (PlatformException e) {
			log.error("Promotion not found. Promotion Id : " + promotionId);
			throw new PromotionNotFoundException("Promotion not found. Promotion Id : " + promotionId);
		} catch (DataAccessException e) {
			log.error("Exception while fetching promotion complete view, promotion id : " + promotionId, e);
			throw new PlatformException("Exception while fetching promotion complete view, promotion id : " + promotionId, e);
		}
		return promotionCompleteView;
	}
	
	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
	
	@Override
	public int promotionCouponCount(int promotionId) {
		int couponCount = 0;
		try {
			couponCount = promotionAdminDao.getCouponCount(promotionId);
		} catch (DataAccessException e) {
			log.error("Exception while fetching coupon count.",e);
			throw new PlatformException("Exception while fetching coupon count.", e);
		}
		return couponCount;
	}
	
	public void setPromotionAdminDao(PromotionAdminDao promotionAdminDao) {
		this.promotionAdminDao = promotionAdminDao;
	}

	@Override
	public List<String> createCoupons(int count, int length, String startsWith, String endsWith, int promotionId, CouponType type, CouponLimitsConfig limits) {

		try {
			//see if the promotion is valid, if not this will throw promotion not found exception
			promotionService.getPromotion(promotionId);

			CouponCodeCreator couponCodeCreator = new CouponCodeCreator();
			couponCodeCreator.setCouponAdminDao(couponAdminDao);

			couponCodeCreator.init(count, length, startsWith, endsWith, COUPON_GENERATION_BATCH_SIZE);

			while (couponCodeCreator.nextBatchAvailable()) {
				List<String> batchOfCouponCodes = couponCodeCreator.getNextBatch();
				createCouponsInBatch(batchOfCouponCodes, promotionId, type, limits);
			}

			return couponCodeCreator.getGeneratedCoupons();
		} catch (DataAccessException e) {
			log.error("DataAccess Exception while creating coupon for promotionId : " + promotionId, e);
			throw new PlatformException("Error while creating coupons.", e);
		}
	}

	private void createCouponsInBatch(List<String> batchOfCoupons, int promotionId, CouponType couponType, CouponLimitsConfig limits) {
		couponAdminDao.createCouponsInBatch(batchOfCoupons, promotionId, couponType, limits);
	}

	@Override
	public SearchCouponResultBO searchCoupons(String couponCode, String userName, SearchCouponOrderBy orderBy,
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
		
		SearchCouponResultBO searchCouponResultBO = new SearchCouponResultBO();
		try {
			List<CouponBasicDetails> allUserCoupons = couponAdminDao.searchCoupons(couponCode, allCouponIdsForUser, orderBy, sortOrder, startRecord, batchSize);
			int totalCount = -1;
			// if user Id was present in search criteria then there can be more than one coupon in the result otherwise
			// if coupon code is present in the search criteria then the result size can only be zero or one.
			if(isUserValid){
				totalCount = couponAdminDao.countCoupons(userId);
			}else{
				totalCount = allUserCoupons.size();
			}
			
			searchCouponResultBO.setCouponBasicDetailsSet(new HashSet<CouponBasicDetails>(allUserCoupons));
			searchCouponResultBO.setTotalCount(totalCount);
		} catch (Exception e) {
			log.error("Error while searching coupon details for userId = "+userId + " couponCode = "+ couponCode, e);
			throw new PlatformException("Error while searching coupon details for userId = "+userId + " couponCode = "+ couponCode, e);
		}
		
		return searchCouponResultBO;
	}
	
	public CouponTO viewCoupons(String couponCode){
		log.info("Viewing coupon using coupon code = "+ couponCode);
		// can throw COupon not found exception or platform exception
		CouponTO couponTO = couponAdminDao.load(couponCode);
		
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
			log.error("Exception while assigning Coupon to user. CouponCode : " + couponCode + ". UserId : "  + userId, e);
			throw new PlatformException("Exception while assigning Coupon to user. CouponCode : " + couponCode + ". UserId : "  + userId, e);
		}
	}

	public void setUserAdminDao(UserAdminDao userAdminDao) {
		this.userAdminDao = userAdminDao;
	}
}
