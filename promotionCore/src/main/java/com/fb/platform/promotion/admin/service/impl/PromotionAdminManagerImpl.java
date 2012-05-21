/**
 * 
 */
package com.fb.platform.promotion.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.promotion.admin.service.NoDataFoundException;
import com.fb.platform.promotion.admin.service.PromotionAdminManager;
import com.fb.platform.promotion.admin.service.PromotionAdminService;
import com.fb.platform.promotion.admin.service.PromotionNameDuplicateException;
import com.fb.platform.promotion.admin.to.AssignCouponToUserRequest;
import com.fb.platform.promotion.admin.to.AssignCouponToUserResponse;
import com.fb.platform.promotion.admin.to.AssignCouponToUserStatusEnum;
import com.fb.platform.promotion.admin.to.CouponBasicDetails;
import com.fb.platform.promotion.admin.to.CouponTO;
import com.fb.platform.promotion.admin.to.CreateCouponRequest;
import com.fb.platform.promotion.admin.to.CreateCouponResponse;
import com.fb.platform.promotion.admin.to.CreateCouponStatusEnum;
import com.fb.platform.promotion.admin.to.CreatePromotionEnum;
import com.fb.platform.promotion.admin.to.CreatePromotionRequest;
import com.fb.platform.promotion.admin.to.CreatePromotionResponse;
import com.fb.platform.promotion.admin.to.FetchRuleRequest;
import com.fb.platform.promotion.admin.to.FetchRuleResponse;
import com.fb.platform.promotion.admin.to.FetchRulesEnum;
import com.fb.platform.promotion.admin.to.PromotionTO;
import com.fb.platform.promotion.admin.to.RuleConfigItemTO;
import com.fb.platform.promotion.admin.to.SearchCouponRequest;
import com.fb.platform.promotion.admin.to.SearchCouponResponse;
import com.fb.platform.promotion.admin.to.SearchCouponResultBO;
import com.fb.platform.promotion.admin.to.SearchCouponStatusEnum;
import com.fb.platform.promotion.admin.to.SearchPromotionEnum;
import com.fb.platform.promotion.admin.to.SearchPromotionRequest;
import com.fb.platform.promotion.admin.to.SearchPromotionResponse;
import com.fb.platform.promotion.admin.to.UpdatePromotionEnum;
import com.fb.platform.promotion.admin.to.UpdatePromotionRequest;
import com.fb.platform.promotion.admin.to.UpdatePromotionResponse;
import com.fb.platform.promotion.admin.to.ViewCouponRequest;
import com.fb.platform.promotion.admin.to.ViewCouponResponse;
import com.fb.platform.promotion.admin.to.ViewCouponStatusEnum;
import com.fb.platform.promotion.admin.to.ViewPromotionEnum;
import com.fb.platform.promotion.admin.to.ViewPromotionRequest;
import com.fb.platform.promotion.admin.to.ViewPromotionResponse;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.rule.RuleConfigDescriptor;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;
import com.fb.platform.promotion.service.CouponCodeGenerationException;
import com.fb.platform.promotion.service.CouponNotFoundException;
import com.fb.platform.promotion.service.InvalidCouponTypeException;
import com.fb.platform.promotion.service.PromotionNotFoundException;
import com.fb.platform.promotion.util.PromotionRuleFactory;
import com.fb.platform.user.manager.exception.UserNotFoundException;
import com.fb.platform.user.manager.interfaces.UserAdminService;
import com.fb.platform.user.manager.model.admin.User;

/**
 * @author nehaga
 *
 */
public class PromotionAdminManagerImpl implements PromotionAdminManager {
	
	private Log log = LogFactory.getLog(PromotionAdminManagerImpl.class);
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private PromotionAdminService promotionAdminService = null;

	@Autowired
	private UserAdminService userAdminService = null;

	/* 
	 * @see com.fb.platform.promotion.service.PromotionAdminManager#fetchRules(com.fb.platform.promotion.to.FetchRuleRequest)
	 */
	@Override
	public FetchRuleResponse fetchRules(FetchRuleRequest fetchRuleRequest) {
		FetchRuleResponse fetchRuleResponse = new FetchRuleResponse();
		
		if (fetchRuleRequest == null || StringUtils.isBlank(fetchRuleRequest.getSessionToken())) {
			log.info("Session cannot be null or empty.");
			fetchRuleResponse.setFetchRulesEnum(FetchRulesEnum.NO_SESSION);
			return fetchRuleResponse;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(fetchRuleRequest.getSessionToken());
		if (authentication == null) {
			log.info("Invalid session token : " + fetchRuleRequest.getSessionToken());
			fetchRuleResponse.setFetchRulesEnum(FetchRulesEnum.NO_SESSION);
			return fetchRuleResponse;
		}
		
		try {
			
			List<RulesEnum> rulesList = promotionAdminService.getAllPromotionRules();
			List<RuleConfigDescriptor> rulesConfigList = new ArrayList<RuleConfigDescriptor>();
			for(RulesEnum rulesEnum : rulesList) {
				List<RuleConfigDescriptorItem> rulesConfigItemList = PromotionRuleFactory.getRuleConfig(rulesEnum);
				RuleConfigDescriptor ruleConfigDescriptor = new RuleConfigDescriptor();
				ruleConfigDescriptor.setRulesEnum(rulesEnum);
				ruleConfigDescriptor.setRuleConfigItemsList(rulesConfigItemList);
				rulesConfigList.add(ruleConfigDescriptor);
			}
			fetchRuleResponse.setRulesList(rulesConfigList);
			fetchRuleResponse.setSessionToken(fetchRuleRequest.getSessionToken());
			fetchRuleResponse.setFetchRulesEnum(FetchRulesEnum.SUCCESS);
			
		} catch (PlatformException e) {
			log.error("Internal error while fetching promotion rules.");
			fetchRuleResponse.setFetchRulesEnum(FetchRulesEnum.INTERNAL_ERROR);
		}
		
		return fetchRuleResponse;
	}
	
	@Override
	public CreatePromotionResponse createPromotion(CreatePromotionRequest createPromotionRequest) {
		CreatePromotionResponse createPromotionResponse = new CreatePromotionResponse();
		
		String requestInvalidationList = createPromotionRequest.isValid();
		
		if(!StringUtils.isEmpty(requestInvalidationList)) {
			log.info("Create promotion request insufficient data : " + requestInvalidationList);
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.INSUFFICIENT_DATA);
			createPromotionResponse.setErrorCause(requestInvalidationList);
			return createPromotionResponse;
		}
		
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(createPromotionRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			log.info("Invalid session token : " + createPromotionRequest.getSessionToken());
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.NO_SESSION);
			return createPromotionResponse;
		}
		
		String missingConfigsList = hasValidRuleConfig(createPromotionRequest.getPromotion());
		
		if(!StringUtils.isEmpty(missingConfigsList)) {
			log.info("Create promotion request rule config missing : " + missingConfigsList);
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.RULE_CONFIG_MISSING);
			createPromotionResponse.setErrorCause(missingConfigsList);
			return createPromotionResponse;
		}
		
		createPromotionResponse.setSessionToken(createPromotionRequest.getSessionToken());
		
		try {
			int promotionId = promotionAdminService.createPromotion(createPromotionRequest.getPromotion());
			if(promotionId > 0) {
				createPromotionResponse.setPromotionId(promotionId);
				createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.SUCCESS);
			} 
		} catch (PromotionNameDuplicateException e) {
			log.error("Duplicate promotion name insertion exception.");
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.DUPLICATE_PROMOTION_NAME);
		} catch (PlatformException e) {
			log.error("Internal error while creating new promotion.");
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.INTERNAL_ERROR);
		}
		
		return createPromotionResponse;
	}
	
	@Override
	public SearchPromotionResponse searchPromotion(SearchPromotionRequest searchPromotionRequest) {
		
		SearchPromotionResponse searchPromotionResponse = new SearchPromotionResponse();
		String requestInvalidationList = searchPromotionRequest.isValid();
		
		if(!StringUtils.isEmpty(requestInvalidationList)) {
			log.info("Cannot search for promotion insufficient data : " + requestInvalidationList);
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.INSUFFICIENT_DATA);
			searchPromotionResponse.setErrorCause(requestInvalidationList);
			return searchPromotionResponse;
		}
		
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(searchPromotionRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			log.info("Invalid session token : " + searchPromotionRequest.getSessionToken());
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.NO_SESSION);
			return searchPromotionResponse;
		}
		searchPromotionResponse.setSessionToken(searchPromotionRequest.getSessionToken());
		
		int isActive = searchPromotionRequest.isActive() ? 1 : 0;
		try {
			List<PromotionTO> promotionList = promotionAdminService.searchPromotion(	searchPromotionRequest.getPromotionName(), 
					searchPromotionRequest.getValidFrom(), 
					searchPromotionRequest.getValidTill(),
					isActive,
					searchPromotionRequest.getSearchPromotionOrderBy(),
					searchPromotionRequest.getSortOrder(),
					searchPromotionRequest.getStartRecord(), 
					searchPromotionRequest.getBatchSize());
			
			searchPromotionResponse.setPromotionsList(promotionList);
			int promotionCount = promotionAdminService.getPromotionCount(	searchPromotionRequest.getPromotionName(), 
																			searchPromotionRequest.getValidFrom(), 
																			searchPromotionRequest.getValidTill(),
																			isActive);
			searchPromotionResponse.setTotalCount(promotionCount);
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.SUCCESS);
			
		} catch (NoDataFoundException e) {
			log.error("No promotion matching search criteria found.");
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.NO_DATA_FOUND);
		} catch (PlatformException e) {
			log.error("Internal error while searching promotion.");
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.INTERNAL_ERROR);
		}
		
		return searchPromotionResponse;
	}
	
	@Override
	public ViewPromotionResponse viewPromotion(ViewPromotionRequest viewPromotionRequest) {
		ViewPromotionResponse viewPromotionResponse = new ViewPromotionResponse();
		
		String requestInvalidationList = viewPromotionRequest.isValid();
		
		if(!StringUtils.isEmpty(requestInvalidationList)) {
			log.info("Cannot fetch promotion details, insufficient data : " + requestInvalidationList);
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.INSUFFICIENT_DATA);
			viewPromotionResponse.setErrorCause(requestInvalidationList);
			return viewPromotionResponse;
		}
		
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(viewPromotionRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			log.info("Invalid session token : " + viewPromotionRequest.getSessionToken());
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.NO_SESSION);
			return viewPromotionResponse;
		}
		
		viewPromotionResponse.setSessionToken(viewPromotionRequest.getSessionToken());
		
		PromotionTO promotionCompleteView = null;
		
		try {
			promotionCompleteView = promotionAdminService.viewPromotion(viewPromotionRequest.getPromotionId());
			
			int couponCount = promotionAdminService.promotionCouponCount(viewPromotionRequest.getPromotionId());
			
			promotionCompleteView.setCouponCount(couponCount);
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.SUCCESS);
			viewPromotionResponse.setPromotionCompleteView(promotionCompleteView);
			
		} catch (PromotionNotFoundException e) {
			log.error("Promotion not found. Promotion Id : " + viewPromotionRequest.getPromotionId());
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.NO_DATA_FOUND);
		} catch (PlatformException e) {
			log.error("Internal error while fetching promotion.");
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.INTERNAL_ERROR);
		}
		
		return viewPromotionResponse;
	}
	
	@Override
	public UpdatePromotionResponse updatePromotion(UpdatePromotionRequest request) {
		UpdatePromotionResponse response = new UpdatePromotionResponse();
		
		String requestInvalidationList = request.isValid();
		
		if(!StringUtils.isEmpty(requestInvalidationList)) {
			log.info("Cannot update promotion, insufficient data : " + requestInvalidationList);
			response.setUpdatePromotionEnum(UpdatePromotionEnum.INSUFFICIENT_DATA);
			response.setErrorCause(requestInvalidationList);
			return response;
		}
		
		//authenticate the admin session token
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			log.info("Invalid session token : " + request.getSessionToken());
			response.setUpdatePromotionEnum(UpdatePromotionEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());
		String missingConfigsList = hasValidRuleConfig(request.getPromotion());
		
		if(!StringUtils.isEmpty(missingConfigsList)) {
			log.info("Update promotion request rule config missing : " + missingConfigsList);
			response.setUpdatePromotionEnum(UpdatePromotionEnum.RULE_CONFIG_MISSING);
			response.setErrorCause(missingConfigsList);
			return response;
		}
		
		try {
			promotionAdminService.updatePromotion(request.getPromotion());
			response.setUpdatePromotionEnum(UpdatePromotionEnum.SUCCESS);
		} catch (PromotionNotFoundException e) {
			log.error("Update promotion invalid promotion id : " + request.getPromotion().getId(), e);
			response.setUpdatePromotionEnum(UpdatePromotionEnum.INVALID_PROMOTION_ID);
		} catch (PromotionNameDuplicateException e) {
			log.error("Duplicate promotion name update exception.");
			response.setUpdatePromotionEnum(UpdatePromotionEnum.DUPLICATE_PROMOTION_NAME);
		} catch (PlatformException e) {
			log.error("Exception while updating the promotion. Promotion Id : " + request.getPromotion().getId(), e);
			response.setUpdatePromotionEnum(UpdatePromotionEnum.INTERNAL_ERROR);
		}
		return response;
	}
	
	private String hasValidRuleConfig(PromotionTO promotionTo) {
		java.util.List<String> missingConfigsList = new ArrayList<String>();
		
		List<RuleConfigDescriptorItem> requiredConfigs = PromotionRuleFactory.getRuleConfig(RulesEnum.valueOf(promotionTo.getRuleName()));
		HashMap<String, RuleConfigItemTO> receivedConfigsMap = new HashMap<String, RuleConfigItemTO>();
		
		for (RuleConfigItemTO ruleConfigItemTO : promotionTo.getConfigItems()) {
			receivedConfigsMap.put(ruleConfigItemTO.getRuleConfigName(), ruleConfigItemTO);
		}
		
		for (RuleConfigDescriptorItem ruleConfigDescriptorItem : requiredConfigs) {
			if(ruleConfigDescriptorItem.isMandatory() && !receivedConfigsMap.containsKey(ruleConfigDescriptorItem.getRuleConfigDescriptorEnum().toString())) {
				missingConfigsList.add(ruleConfigDescriptorItem.getRuleConfigDescriptorEnum().toString());
			}
		}
		
		return StringUtils.join(missingConfigsList.toArray(), ",");
	}
	
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setPromotionAdminService(PromotionAdminService promotionAdminService) {
		this.promotionAdminService = promotionAdminService;
	}

	@Override
	public CreateCouponResponse createCoupons(CreateCouponRequest request) {
		CreateCouponResponse response = new CreateCouponResponse();
		if (request == null) {
			response = new CreateCouponResponse();
			log.info("Empty request sent to create coupon.");
			response.setStatus(CreateCouponStatusEnum.NO_SESSION);
			return response;
		}
		
		response = request.validate();
		if (response != null) {
			return response;
		}

		response = new CreateCouponResponse();
		response.setSessionToken(request.getSessionToken());
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			log.info("Invalid session token : " + request.getSessionToken());
			response.setStatus(CreateCouponStatusEnum.NO_SESSION);
			return response;
		}

		CouponLimitsConfig limits = new CouponLimitsConfig();
		if (request.getMaxAmount() != null) {
			limits.setMaxAmount(new Money(request.getMaxAmount()));
		}
		if (request.getMaxAmountPerUser() != null) {
			limits.setMaxAmountPerUser(new Money(request.getMaxAmountPerUser()));
		}
		limits.setMaxUses(request.getMaxUses());
		limits.setMaxUsesPerUser(request.getMaxUsesPerUser());

		int numberOfCouponsCreated = 0;
		String commaSeparatedCouponCodes = null;
		try {
			List<String> couponsCodes = promotionAdminService.createCoupons(request.getCount(), request.getLength(), request.getStartsWith(), request.getEndsWith(), request.getPromotionId(), request.getType(), limits);
			numberOfCouponsCreated = couponsCodes.size();
			commaSeparatedCouponCodes = StringUtils.join(couponsCodes, ",");
			
			response.setNumberOfCouponsCreated(numberOfCouponsCreated);
			response.setCommaSeparatedCouponCodes(commaSeparatedCouponCodes);
			response.setStatus(CreateCouponStatusEnum.SUCCESS);
			
		} catch (PromotionNotFoundException e) {
			log.error("Promotion Not Found error in create coupon - " + request.getPromotionId(), e);
			response.setStatus(CreateCouponStatusEnum.INVALID_PROMOTION);
		} catch (CouponCodeGenerationException e) {
			log.error("Coupon Code Generation error in create coupon - ", e);
			response.setStatus(CreateCouponStatusEnum.CODE_GENERATION_FAILED);
		} catch (PlatformException e) {
			log.error("Error in create coupon - ", e);
			response.setStatus(CreateCouponStatusEnum.INTERNAL_ERROR);
		}
		
		return response;
	}

	@Override
	public AssignCouponToUserResponse assignCouponToUser(AssignCouponToUserRequest request) {
		AssignCouponToUserResponse response = null;

		if (request == null) {
			response = new AssignCouponToUserResponse();
			log.info("Empty request sent to assign coupon to user.");
			response.setStatus(AssignCouponToUserStatusEnum.NO_SESSION);
			return response;
		}

		response = request.validate();
		if (response != null) {
			return response;
		}

		response = new AssignCouponToUserResponse();
		response.setSessionToken(request.getSessionToken());

		//authenticate the admin session token
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			log.info("Invalid session token : " + request.getSessionToken());
			response.setStatus(AssignCouponToUserStatusEnum.NO_SESSION);
			return response;
		}

		try {
			//this will throw usernotfound exception if username is invalid.
			User user = userAdminService.getUser(request.getUserName());

			promotionAdminService.assignCouponToUser(request.getCouponCode(), user.getUserId(), request.getOverrideCouponUserLimit());

			response.setStatus(AssignCouponToUserStatusEnum.SUCCESS);

		} catch (CouponNotFoundException e) {
			log.error("Invalid coupon code : " + request.getCouponCode(), e);
			response.setStatus(AssignCouponToUserStatusEnum.INVALID_COUPON_CODE);
		} catch (CouponAlreadyAssignedToUserException e) {
			log.error("Coupon already assigned to user : " + request.getCouponCode(), e);
			response.setStatus(AssignCouponToUserStatusEnum.COUPON_ALREADY_ASSIGNED_TO_USER);
		} catch (InvalidCouponTypeException e) {
			log.error("Invalid coupon type.", e);
			response.setStatus(AssignCouponToUserStatusEnum.INVALID_COUPON_TYPE);
		} catch(UserNotFoundException e) {
			log.error("Invalid user id : " + request.getUserName(), e);
			response.setStatus(AssignCouponToUserStatusEnum.INVALID_USER);
		} catch (PlatformException e) {
			log.error("Error while assigning coupon to user. CouponCode : " + request.getCouponCode() + ". UserId : " + request.getUserName(), e);
			response.setStatus(AssignCouponToUserStatusEnum.INTERNAL_ERROR);
		}
		
		return response;
	}
	
	@Override
	public SearchCouponResponse searchCoupons(SearchCouponRequest request){
		SearchCouponResponse response = new SearchCouponResponse();
		if (request == null) {
			response.setStatus(SearchCouponStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());
		String requestInvalidationList = request.validate();
		
		if(StringUtils.isNotBlank(requestInvalidationList)) {
			response.setStatus(SearchCouponStatusEnum.INSUFFICIENT_DATA);
			response.setErrorCause(requestInvalidationList);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setStatus(SearchCouponStatusEnum.NO_SESSION);
			return response;
		}
		
		SearchCouponResultBO searchCouponResultBO = null;
		try {
			searchCouponResultBO = promotionAdminService.searchCoupons(request.getCouponCode(), request.getUserName(), request.getOrderBy(), request.getSortOrder(), request.getStartRecord(), request.getBatchSize());
		} catch (Exception e) {
			response.setStatus(SearchCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}
		// if no result then set response status as no data found
		if(searchCouponResultBO.getCouponBasicDetailsSet().isEmpty()){
			log.info("No coupon data found for search criteria coupon code = "+request.getCouponCode() + ", userName = "+request.getUserName());
			response.setStatus(SearchCouponStatusEnum.NO_DATA_FOUND);
			response.setErrorCause("No coupon data found for search criteria entered");
			return response;
		}
		// success case
		response.setCouponBasicDetailsList(new ArrayList<CouponBasicDetails>(searchCouponResultBO.getCouponBasicDetailsSet()));
		response.setTotalCount(searchCouponResultBO.getTotalCount());
		response.setStatus(SearchCouponStatusEnum.SUCCESS);
		
		return response;
	}
	
	@Override
	public ViewCouponResponse viewCoupon(ViewCouponRequest request){
		ViewCouponResponse response = new ViewCouponResponse();
		if (request == null) {
			response.setStatus(ViewCouponStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());
		String requestInvalidationList = request.validate();
		
		if(StringUtils.isNotBlank(requestInvalidationList)) {
			response.setStatus(ViewCouponStatusEnum.IMPROPER_REQUEST);
			response.setErrorCause(requestInvalidationList);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setStatus(ViewCouponStatusEnum.NO_SESSION);
			return response;
		}
		
		Integer couponId = request.getCouponId(); 
		boolean useCouponID = false;
		if(couponId!=null && couponId > 0){
			useCouponID = true;
		}
		
		CouponTO couponTO = null;
		try {
			if(useCouponID){
				log.info("Viewing coupon by couponId = "+couponId);
				couponTO = promotionAdminService.viewCoupons(couponId);
			}else{
				log.info("Viewing coupon by coupon code = "+request.getCouponCode());
				couponTO = promotionAdminService.viewCoupons(request.getCouponCode());
			}
			
			response.setCouponTO(couponTO);
			response.setStatus(ViewCouponStatusEnum.SUCCESS);
			
		} catch (CouponNotFoundException e) {
			log.error("Coupon not found", e);
			response.setStatus(ViewCouponStatusEnum.INVALID_COUPON);
		}catch (PlatformException e) {
			log.error("Error while loading limits of coupon", e);
			response.setStatus(ViewCouponStatusEnum.INTERNAL_ERROR);
		}catch (Exception e) {
			log.error("Unknow error occurred while loading coupon", e);
			response.setStatus(ViewCouponStatusEnum.INTERNAL_ERROR);
		}

		return response;
	}
}
