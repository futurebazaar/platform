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
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigDescriptor;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.service.PromotionNotFoundException;
import com.fb.platform.promotion.to.CreatePromotionEnum;
import com.fb.platform.promotion.to.CreatePromotionRequest;
import com.fb.platform.promotion.to.CreatePromotionResponse;
import com.fb.platform.promotion.to.FetchRuleRequest;
import com.fb.platform.promotion.to.FetchRuleResponse;
import com.fb.platform.promotion.to.FetchRulesEnum;
import com.fb.platform.promotion.to.PromotionTO;
import com.fb.platform.promotion.to.RuleConfigItemTO;
import com.fb.platform.promotion.util.CouponCodeCreator;
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
		
		if (createPromotionRequest == null || StringUtils.isBlank(createPromotionRequest.getSessionToken())) {
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.NO_SESSION);
			return createPromotionResponse;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(createPromotionRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.NO_SESSION);
			return createPromotionResponse;
		}
		
		String missingConfigsList = hasValidRuleConfig(createPromotionRequest.getPromotion());
		
		if(!StringUtils.isEmpty(missingConfigsList)) {
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.RULE_CONFIG_MISSING);
			createPromotionResponse.setErrorCause("Missing rule configs : " + missingConfigsList);
			return createPromotionResponse;
		}
		createPromotionResponse.setSessionToken(createPromotionRequest.getSessionToken());
		createPromotionResponse.setPromotionId(promotionAdminService.createPromotion(createPromotionRequest.getPromotion()));
		createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.SUCCESS);
		return createPromotionResponse;
	}
	
	private String hasValidRuleConfig(PromotionTO promotionTo) {
		java.util.List<String> missingConfigsList = new ArrayList<String>();
		List<RuleConfigDescriptorItem> requiredConfigs = PromotionRuleFactory.getRuleConfig(promotionTo.getRulesEnum());
		HashMap<String, RuleConfigItemTO> receivedConfigsMap = new HashMap<String, RuleConfigItemTO>();
		
		for(RuleConfigItemTO ruleConfigItemTO:promotionTo.getConfigItems()) {
			receivedConfigsMap.put(ruleConfigItemTO.getName(), ruleConfigItemTO);
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
