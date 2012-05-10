/**
 * 
 */
package com.fb.platform.promotion.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.to.Money;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.promotion.admin.service.PromotionAdminManager;
import com.fb.platform.promotion.admin.service.PromotionAdminService;
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
import com.fb.platform.promotion.admin.to.SearchPromotionEnum;
import com.fb.platform.promotion.admin.to.SearchPromotionRequest;
import com.fb.platform.promotion.admin.to.SearchPromotionResponse;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.rule.RuleConfigDescriptor;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.service.PromotionNotFoundException;
import com.fb.platform.promotion.util.PromotionRuleFactory;

/**
 * @author nehaga
 *
 */
public class PromotionAdminManagerImpl implements PromotionAdminManager {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private PromotionAdminService promotionAdminService = null;

	/* 
	 * @see com.fb.platform.promotion.service.PromotionAdminManager#fetchRules(com.fb.platform.promotion.to.FetchRuleRequest)
	 */
	@Override
	public FetchRuleResponse fetchRules(FetchRuleRequest fetchRuleRequest) {
		FetchRuleResponse fetchRuleResponse = new FetchRuleResponse();
		if (fetchRuleRequest == null || StringUtils.isBlank(fetchRuleRequest.getSessionToken())) {
			fetchRuleResponse.setFetchRulesEnum(FetchRulesEnum.NO_SESSION);
			return fetchRuleResponse;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(fetchRuleRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			fetchRuleResponse.setFetchRulesEnum(FetchRulesEnum.NO_SESSION);
			return fetchRuleResponse;
		}
		fetchRuleResponse.setSessionToken(fetchRuleRequest.getSessionToken());
		fetchRuleResponse.setFetchRulesEnum(FetchRulesEnum.SUCCESS);
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
		return fetchRuleResponse;
	}
	
	@Override
	public CreatePromotionResponse createPromotion(CreatePromotionRequest createPromotionRequest) {
		CreatePromotionResponse createPromotionResponse = new CreatePromotionResponse();
		
		String requestInvalidationList = createPromotionRequest.isValid();
		
		if(!StringUtils.isEmpty(requestInvalidationList)) {
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.INSUFFICIENT_DATA);
			createPromotionResponse.setErrorCause(requestInvalidationList);
			return createPromotionResponse;
		}
		
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(createPromotionRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.NO_SESSION);
			return createPromotionResponse;
		}
		
		createPromotionResponse.setSessionToken(createPromotionRequest.getSessionToken());
		String missingConfigsList = hasValidRuleConfig(createPromotionRequest.getPromotion());
		
		if(!StringUtils.isEmpty(missingConfigsList)) {
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.RULE_CONFIG_MISSING);
			createPromotionResponse.setErrorCause(missingConfigsList);
			return createPromotionResponse;
		}
		
		int promotionId = promotionAdminService.createPromotion(createPromotionRequest.getPromotion());
		if(promotionId > 0) {
			createPromotionResponse.setPromotionId(promotionId);
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.SUCCESS);
		} 
		return createPromotionResponse;
	}
	
	@Override
	public SearchPromotionResponse searchPromotion(SearchPromotionRequest searchPromotionRequest) {
		SearchPromotionResponse searchPromotionResponse = new SearchPromotionResponse();
		String requestInvalidationList = searchPromotionRequest.isValid();
		
		if(!StringUtils.isEmpty(requestInvalidationList)) {
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.INSUFFICIENT_DATA);
			searchPromotionResponse.setErrorCause(requestInvalidationList);
			return searchPromotionResponse;
		}
		
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(searchPromotionRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.NO_SESSION);
			return searchPromotionResponse;
		}
		searchPromotionResponse.setSessionToken(searchPromotionRequest.getSessionToken());
		List<PromotionTO> promotionList = promotionAdminService.searchPromotion(searchPromotionRequest.getPromotionName(), 
																					searchPromotionRequest.getValidFrom(), 
																					searchPromotionRequest.getValidTill(), 
																					searchPromotionRequest.getStartRecord(), 
																					searchPromotionRequest.getBatchSize());
		if(promotionList.isEmpty()) {
			searchPromotionResponse.setPromotionsList(null);
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.NO_DATA_FOUND);
		} else {
			searchPromotionResponse.setPromotionsList(promotionList);
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.SUCCESS);
		}
		return searchPromotionResponse;
	}
	
	private String hasValidRuleConfig(PromotionTO promotionTo) {
		java.util.List<String> missingConfigsList = new ArrayList<String>();
		List<RuleConfigDescriptorItem> requiredConfigs = PromotionRuleFactory.getRuleConfig(RulesEnum.valueOf(promotionTo.getRuleName()));
		HashMap<String, RuleConfigItemTO> receivedConfigsMap = new HashMap<String, RuleConfigItemTO>();
		
		for(RuleConfigItemTO ruleConfigItemTO:promotionTo.getConfigItems()) {
			receivedConfigsMap.put(ruleConfigItemTO.getRuleConfigName(), ruleConfigItemTO);
		}
		
		for(RuleConfigDescriptorItem ruleConfigDescriptorItem : requiredConfigs) {
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
		CreateCouponResponse response = null;
		if (request == null) {
			response = new CreateCouponResponse();
			response.setStatus(CreateCouponStatusEnum.NO_SESSION);
			return response;
		}

		response = request.validate();
		if (response != null) {
			return response;
		}

		response = new CreateCouponResponse();

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
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

		try {
			promotionAdminService.createCoupons(request.getCount(), request.getLength(), request.getStartsWith(), request.getEndsWith(), request.getPromotionId(), request.getType(), limits);
		} catch (PromotionNotFoundException e) {
			response.setStatus(CreateCouponStatusEnum.INVALID_PROMOTION);
		}
		return response;
	}
}
