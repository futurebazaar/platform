/**
 * 
 */
package com.fb.platform.promotion.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.promotion.admin.service.PromotionAdminManager;
import com.fb.platform.promotion.admin.service.PromotionAdminService;
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
import com.fb.platform.promotion.admin.to.UpdatePromotionEnum;
import com.fb.platform.promotion.admin.to.UpdatePromotionRequest;
import com.fb.platform.promotion.admin.to.UpdatePromotionResponse;
import com.fb.platform.promotion.admin.to.ViewPromotionEnum;
import com.fb.platform.promotion.admin.to.ViewPromotionRequest;
import com.fb.platform.promotion.admin.to.ViewPromotionResponse;
import com.fb.platform.promotion.rule.RuleConfigDescriptor;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RulesEnum;
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
		int isActive = searchPromotionRequest.isActive() ? 1 : 0;
		List<PromotionTO> promotionList = promotionAdminService.searchPromotion(	searchPromotionRequest.getPromotionName(), 
																					searchPromotionRequest.getValidFrom(), 
																					searchPromotionRequest.getValidTill(),
																					isActive,
																					searchPromotionRequest.getSearchPromotionOrderBy(),
																					searchPromotionRequest.getSearchPromotionOrderByOrder(),
																					searchPromotionRequest.getStartRecord(), 
																					searchPromotionRequest.getBatchSize());
		if(promotionList.isEmpty()) {
			searchPromotionResponse.setPromotionsList(null);
			searchPromotionResponse.setTotalCount(0);
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.NO_DATA_FOUND);
		} else {
			searchPromotionResponse.setPromotionsList(promotionList);
			int promotionCount = promotionAdminService.getPromotionCount(	searchPromotionRequest.getPromotionName(), 
																			searchPromotionRequest.getValidFrom(), 
																			searchPromotionRequest.getValidTill(),
																			isActive);
			searchPromotionResponse.setTotalCount(promotionCount);
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.SUCCESS);
		}
		return searchPromotionResponse;
	}
	
	@Override
	public ViewPromotionResponse viewPromotion(ViewPromotionRequest viewPromotionRequest) {
		ViewPromotionResponse viewPromotionResponse = new ViewPromotionResponse();
		String requestInvalidationList = viewPromotionRequest.isValid();
		
		if(!StringUtils.isEmpty(requestInvalidationList)) {
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.INSUFFICIENT_DATA);
			viewPromotionResponse.setErrorCause(requestInvalidationList);
			return viewPromotionResponse;
		}
		
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(viewPromotionRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.NO_SESSION);
			return viewPromotionResponse;
		}
		viewPromotionResponse.setSessionToken(viewPromotionRequest.getSessionToken());
		
		PromotionTO promotionCompleteView = promotionAdminService.viewPromotion(viewPromotionRequest.getPromotionId());
		if(promotionCompleteView != null) {
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.SUCCESS);
			viewPromotionResponse.setPromotionCompleteView(promotionCompleteView);
		} else {
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.NO_DATA_FOUND);
			viewPromotionResponse.setPromotionCompleteView(null);
		}
		return viewPromotionResponse;
	}
	
	@Override
	public UpdatePromotionResponse updatePromotion(UpdatePromotionRequest updatePromotionRequest) {
		UpdatePromotionResponse updatePromotionResponse = new UpdatePromotionResponse();
		
		String requestInvalidationList = updatePromotionRequest.isValid();
		
		if(!StringUtils.isEmpty(requestInvalidationList)) {
			updatePromotionResponse.setUpdatePromotionEnum(UpdatePromotionEnum.INSUFFICIENT_DATA);
			updatePromotionResponse.setErrorCause(requestInvalidationList);
			return updatePromotionResponse;
		}
		
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(updatePromotionRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			updatePromotionResponse.setUpdatePromotionEnum(UpdatePromotionEnum.NO_SESSION);
			return updatePromotionResponse;
		}
		
		updatePromotionResponse.setSessionToken(updatePromotionRequest.getSessionToken());
		String missingConfigsList = hasValidRuleConfig(updatePromotionRequest.getPromotion());
		
		if(!StringUtils.isEmpty(missingConfigsList)) {
			updatePromotionResponse.setUpdatePromotionEnum(UpdatePromotionEnum.RULE_CONFIG_MISSING);
			updatePromotionResponse.setErrorCause(missingConfigsList);
			return updatePromotionResponse;
		}
		
		boolean updateSuccessful = promotionAdminService.updatePromotion(updatePromotionRequest.getPromotion());
		if(updateSuccessful) {
			updatePromotionResponse.setUpdatePromotionEnum(UpdatePromotionEnum.SUCCESS);
		} else {
			updatePromotionResponse.setUpdatePromotionEnum(UpdatePromotionEnum.NO_DATA_FOUND);
		}
		return updatePromotionResponse;
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

}
