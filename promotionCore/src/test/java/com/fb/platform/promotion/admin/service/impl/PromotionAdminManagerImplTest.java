package com.fb.platform.promotion.admin.service.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.auth.AuthenticationService;
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
import com.fb.platform.promotion.admin.to.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderByOrder;
import com.fb.platform.promotion.admin.to.SearchPromotionRequest;
import com.fb.platform.promotion.admin.to.SearchPromotionResponse;
import com.fb.platform.promotion.admin.to.UpdatePromotionEnum;
import com.fb.platform.promotion.admin.to.UpdatePromotionRequest;
import com.fb.platform.promotion.admin.to.UpdatePromotionResponse;
import com.fb.platform.promotion.admin.to.ViewPromotionEnum;
import com.fb.platform.promotion.admin.to.ViewPromotionRequest;
import com.fb.platform.promotion.admin.to.ViewPromotionResponse;
import com.fb.platform.promotion.rule.RuleConfigDescriptor;
import com.fb.platform.promotion.rule.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;

public class PromotionAdminManagerImplTest extends BaseTestCase {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private PromotionAdminService promotionAdminService = null;
	
	@Autowired
	private PromotionAdminManager promotionAdminManager = null;
	
	@Autowired
	private UserManager userManager = null;
	
	LoginResponse responseUser = null;
	
	private static int promotionId = -4100;
	
	private static int[] validFromSortAsc = new int[] {-3001, -3002, -3003, -3007, -3005, -3004};
	
	private static int[] validFromSortDesc = new int[] {-3007, -3005, -3004, -3003, -3002, -3001};
	
	private static int[] validTillSortAsc = new int[] {-3003, -3002, -3001, -3007, -3005, -3004};
	
	private static int[] validTillSortDesc = new int[] {-3007, -3005, -3004, -3001, -3002, -3003};
	
	private static int[] nameSort = new int[] {-3004, -3005, -3001, -3002, -3003, -3007};
	
	private static List<RulesEnum> ruleList = new ArrayList<RulesEnum>() {{
		add(RulesEnum.BUY_WORTH_X_GET_Y_PERCENT_OFF);
		add(RulesEnum.BUY_WORTH_X_GET_Y_PERCENT_OFF_ON_Z_CATEGORY);
		add(RulesEnum.BUY_WORTH_X_GET_Y_RS_OFF);
		add(RulesEnum.BUY_WORTH_X_GET_Y_RS_OFF_ON_Z_CATEGORY);
		add(RulesEnum.BUY_X_BRAND_GET_Y_RS_OFF_ON_Z_PRODUCT);
		add(RulesEnum.BUY_X_GET_Y_FREE);
		add(RulesEnum.FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF);
	}};
	
	@Before
	public void loginUser() {
		
		LoginRequest request = new LoginRequest();
		request.setUsername("neha.garani@gmail.com");
		request.setPassword("testpass");

		responseUser = userManager.login(request);
	}
	
	@Test
	public void testFetchRules() {
		int count = 0;
		FetchRuleRequest fetchRuleRequest = new FetchRuleRequest();
		FetchRuleResponse fetchRuleResponse = promotionAdminManager.fetchRules(fetchRuleRequest);
		assertEquals(FetchRulesEnum.NO_SESSION, fetchRuleResponse.getFetchRulesEnum());
		
		fetchRuleRequest.setSessionToken("INVALID_SESSION");
		
		fetchRuleResponse = promotionAdminManager.fetchRules(fetchRuleRequest);
		assertEquals(FetchRulesEnum.NO_SESSION, fetchRuleResponse.getFetchRulesEnum());
		
		fetchRuleRequest.setSessionToken(responseUser.getSessionToken());
		
		fetchRuleResponse = promotionAdminManager.fetchRules(fetchRuleRequest);
		assertEquals(FetchRulesEnum.SUCCESS, fetchRuleResponse.getFetchRulesEnum());
		assertNotNull(fetchRuleResponse.getRulesList());
		assertEquals(7, fetchRuleResponse.getRulesList().size());
		assertNotNull(fetchRuleResponse.getSessionToken());
		for(RuleConfigDescriptor ruleConfig : fetchRuleResponse.getRulesList()) {
			assertEquals(ruleList.get(count), ruleConfig.getRulesEnum());
			count++;
		}
		
	}
	
	@Test
	public void testCreatePromotion() {
		CreatePromotionRequest createPromotionRequest = new CreatePromotionRequest();
		PromotionTO promotionTO = new PromotionTO();
		createPromotionRequest.setPromotion(promotionTO);
		
		CreatePromotionResponse createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA, createPromotionResponse.getCreatePromotionEnum());
		
		promotionTO.setPromotionName("New Promotion");
		
		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA, createPromotionResponse.getCreatePromotionEnum());
		
		promotionTO.setValidFrom(new DateTime(2012, 02, 29, 0, 0));
		promotionTO.setValidTill(new DateTime(2011, 02, 22, 0, 0));
		
		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA, createPromotionResponse.getCreatePromotionEnum());
		
		promotionTO.setValidTill(new DateTime(2013, 02, 22, 0, 0));
		
		promotionTO.setDescription("Test new promotion");
		promotionTO.setActive(true);

		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA, createPromotionResponse.getCreatePromotionEnum());
		
		promotionTO.setMaxUses(20);
		promotionTO.setMaxUsesPerUser(1);
		promotionTO.setMaxAmount(new Money(new BigDecimal(20000.00)));
		promotionTO.setMaxAmountPerUser(new Money(new BigDecimal(1000.00)));
		
		
		promotionTO.setRuleName("BUY_WORTH_X_GET_Y_RS_OFF_WRONG");
		
		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA, createPromotionResponse.getCreatePromotionEnum());
		
		promotionTO.setRuleName("BUY_WORTH_X_GET_Y_RS_OFF");
		
		createPromotionRequest.setSessionToken("INVALID_SESSION");
		
		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.NO_SESSION, createPromotionResponse.getCreatePromotionEnum());
		
		createPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		RuleConfigItemTO configItem = new RuleConfigItemTO();

		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.RULE_CONFIG_MISSING, createPromotionResponse.getCreatePromotionEnum());
		
		
		configItem.setRuleConfigName("CLIENT_LIST");
		configItem.setRuleConfigValue("1,2,5,8");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("MIN_ORDER_VALUE");
		configItem.setRuleConfigValue("2000");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("100");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_LIST");
		configItem.setRuleConfigValue("1,2,3,4,5,6,7,8,9,10,12,4,15,25");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("BRAND_LIST");
		configItem.setRuleConfigValue("3");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_INCLUDE_LIST_WRONG");
		configItem.setRuleConfigValue("1,2,3,4,5,6,7,8,9");
		ruleConfigList.add(configItem);

		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.RULE_CONFIG_MISSING, createPromotionResponse.getCreatePromotionEnum());
		
		ruleConfigList.remove(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_INCLUDE_LIST");
		configItem.setRuleConfigValue("1,2,3,4,5,6,7,8,9");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_EXCLUDE_LIST");
		configItem.setRuleConfigValue("10,12,4,15,25");
		ruleConfigList.add(configItem);
		
		promotionTO.setConfigItems(ruleConfigList);
		
		createPromotionRequest.setPromotion(promotionTO);
		
		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.SUCCESS, createPromotionResponse.getCreatePromotionEnum());
		assertTrue(createPromotionResponse.getPromotionId() > 0);
		assertNotNull(createPromotionResponse.getSessionToken());
		
	}
	
	@Test
	public void testViewPromotion() {
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		
		ViewPromotionResponse viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.INSUFFICIENT_DATA, viewPromotionResponse.getViewPromotionEnum());
		
		viewPromotionRequest.setPromotionId(-5000);
		viewPromotionRequest.setSessionToken("INVALID_SESSION");
		
		viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.NO_SESSION, viewPromotionResponse.getViewPromotionEnum());
		
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.NO_DATA_FOUND, viewPromotionResponse.getViewPromotionEnum());
		
		viewPromotionRequest.setPromotionId(promotionId);
		
		viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.SUCCESS, viewPromotionResponse.getViewPromotionEnum());
		
		PromotionTO completePromotionView = viewPromotionResponse.getPromotionCompleteView();
		
		assertEquals("New Promotion Copy", completePromotionView.getPromotionName());
		assertEquals("Test new promotion via Promotion Admin", completePromotionView.getDescription());
		assertEquals(29, completePromotionView.getValidFrom().getDayOfMonth());
		assertEquals(2012, completePromotionView.getValidFrom().getYear());
		assertEquals(2, completePromotionView.getValidFrom().getMonthOfYear());
		assertEquals(01, completePromotionView.getValidTill().getDayOfMonth());
		assertEquals(2013, completePromotionView.getValidTill().getYear());
		assertEquals(3, completePromotionView.getValidTill().getMonthOfYear());
		assertEquals(true, completePromotionView.isActive());
		assertEquals(0, completePromotionView.getCouponCount());
		assertEquals(20, completePromotionView.getMaxUses());
		assertEquals(2000, completePromotionView.getMaxAmount().getAmount().intValue());
		assertEquals(1, completePromotionView.getMaxUsesPerUser());
		assertEquals(1000, completePromotionView.getMaxAmountPerUser().getAmount().intValue());
		assertEquals(-2, completePromotionView.getRuleId());
		assertNotNull(completePromotionView.getConfigItems());
		assertEquals(7, completePromotionView.getConfigItems().size());
		assertNotNull(viewPromotionResponse.getSessionToken());
	}
	
	@Test
	public void testUpdatePromotion() {
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		viewPromotionRequest.setPromotionId(promotionId);
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		ViewPromotionResponse viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		
		PromotionTO promotionView = viewPromotionResponse.getPromotionCompleteView();
		
		UpdatePromotionRequest updatePromotionRequest = new UpdatePromotionRequest();
		updatePromotionRequest.setPromotion(promotionView);
		
		UpdatePromotionResponse updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA, updatePromotionResponse.getUpdatePromotionEnum());
		
		promotionView.setPromotionName("New Promotion updated");
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA, updatePromotionResponse.getUpdatePromotionEnum());	
		
		promotionView.setValidFrom(new DateTime(2012, 2, 22, 0, 0));
		promotionView.setValidTill(new DateTime(2011, 2, 22, 0, 0));
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA, updatePromotionResponse.getUpdatePromotionEnum());
		
		promotionView.setValidTill(new DateTime(2013, 2, 22, 0, 0));
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA, updatePromotionResponse.getUpdatePromotionEnum());
		
		promotionView.setDescription("Test new promotion update");
		promotionView.setActive(false);
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA, updatePromotionResponse.getUpdatePromotionEnum());
		
		promotionView.setMaxUses(22);
		promotionView.setMaxUsesPerUser(2);
		promotionView.setMaxAmount(new Money(new BigDecimal(2222.00)));
		promotionView.setMaxAmountPerUser(new Money(new BigDecimal(2222.00)));
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA, updatePromotionResponse.getUpdatePromotionEnum());
		
		promotionView.setRuleName("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF_WRONG");
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA, updatePromotionResponse.getUpdatePromotionEnum());
		
		promotionView.setRuleName("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF");
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA, updatePromotionResponse.getUpdatePromotionEnum());
		
		promotionView.setPromotionId(5000);
		
		updatePromotionRequest.setSessionToken("INVALID_SESSION");
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.NO_SESSION, updatePromotionResponse.getUpdatePromotionEnum());
		
		updatePromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		promotionView.setConfigItems(ruleConfigList);
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.RULE_CONFIG_MISSING, updatePromotionResponse.getUpdatePromotionEnum());
		
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CLIENT_LIST");
		configItem.setRuleConfigValue("1,2,3,4");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("MIN_ORDER_VALUE");
		configItem.setRuleConfigValue("2222");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("222");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_LIST");
		configItem.setRuleConfigValue("1");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("BRAND_LIST");
		configItem.setRuleConfigValue("3,1");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_INCLUDE_LIST_WRONG");
		configItem.setRuleConfigValue("1");
		ruleConfigList.add(configItem);
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA, updatePromotionResponse.getUpdatePromotionEnum());
		
		ruleConfigList.remove(configItem);
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.RULE_CONFIG_MISSING, updatePromotionResponse.getUpdatePromotionEnum());
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_INCLUDE_LIST");
		configItem.setRuleConfigValue("1");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_EXCLUDE_LIST");
		configItem.setRuleConfigValue("10");
		ruleConfigList.add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("DISCOUNT_PERCENTAGE");
		configItem.setRuleConfigValue("2");
		ruleConfigList.add(configItem);
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.NO_DATA_FOUND, updatePromotionResponse.getUpdatePromotionEnum());
		
		
		promotionView.setPromotionId(promotionId);
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.SUCCESS, updatePromotionResponse.getUpdatePromotionEnum());
		
		viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		
		PromotionTO completePromotionView = viewPromotionResponse.getPromotionCompleteView();
		
		assertEquals("New Promotion updated", completePromotionView.getPromotionName());
		assertEquals("Test new promotion update", completePromotionView.getDescription());
		assertEquals(22, completePromotionView.getValidFrom().getDayOfMonth());
		assertEquals(2012, completePromotionView.getValidFrom().getYear());
		assertEquals(2, completePromotionView.getValidFrom().getMonthOfYear());
		assertEquals(22, completePromotionView.getValidTill().getDayOfMonth());
		assertEquals(2013, completePromotionView.getValidTill().getYear());
		assertEquals(2, completePromotionView.getValidTill().getMonthOfYear());
		assertEquals(false, completePromotionView.isActive());
		assertEquals(0, completePromotionView.getCouponCount());
		assertEquals(22, completePromotionView.getMaxUses());
		assertEquals(2222, completePromotionView.getMaxAmount().getAmount().intValue());
		assertEquals(2, completePromotionView.getMaxUsesPerUser());
		assertEquals(2222, completePromotionView.getMaxAmountPerUser().getAmount().intValue());
		assertEquals(-7, completePromotionView.getRuleId());
		assertNotNull(completePromotionView.getConfigItems());
		assertEquals(8, completePromotionView.getConfigItems().size());
		assertNotNull(updatePromotionResponse.getSessionToken());
		
	}
	
	@Test
	public void testSearchPromotion() {
		
		int count = 0;
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.INSUFFICIENT_DATA, searchPromotionResponse.getSearchPromotionEnum());
		
		searchPromotionRequest.setSessionToken("INVALID_SESSION");
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.INSUFFICIENT_DATA, searchPromotionResponse.getSearchPromotionEnum());
		
		searchPromotionRequest.setStartRecord(-5);
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.INSUFFICIENT_DATA, searchPromotionResponse.getSearchPromotionEnum());
		
		searchPromotionRequest.setStartRecord(0);
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.INSUFFICIENT_DATA, searchPromotionResponse.getSearchPromotionEnum());
		
		searchPromotionRequest.setBatchSize(10);
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.NO_SESSION, searchPromotionResponse.getSearchPromotionEnum());
		
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(1, searchPromotionResponse.getTotalCount());
		assertEquals(1, searchPromotionResponse.getPromotionsList().size());
		
		searchPromotionRequest.setActive(true);
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(18, searchPromotionResponse.getTotalCount());
		assertEquals(10, searchPromotionResponse.getPromotionsList().size());
		
		searchPromotionRequest.setPromotionName("promotion");
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(16, searchPromotionResponse.getTotalCount());
		assertEquals(10, searchPromotionResponse.getPromotionsList().size());
		
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(10, searchPromotionResponse.getTotalCount());
		assertEquals(10, searchPromotionResponse.getPromotionsList().size());
		
		searchPromotionRequest.setValidTill(new DateTime(2012, 6, 30, 0, 0));
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(6, searchPromotionResponse.getTotalCount());
		assertEquals(6, searchPromotionResponse.getPromotionsList().size());
		assertNotNull(searchPromotionResponse.getSessionToken());
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validFromSortAsc[count], promotion.getPromotionId());
			count++;
		}
		
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.DESCENDING);
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		count = 0;
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validFromSortDesc[count], promotion.getPromotionId());
			count++;
		}
		
		searchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_TILL);
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.ASCENDING);
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		count = 0;
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validTillSortAsc[count], promotion.getPromotionId());
			count++;
		}
		
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.DESCENDING);
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		count = 0;
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validTillSortDesc[count], promotion.getPromotionId());
			count++;
		}
		
		searchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.NAME);
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.ASCENDING);
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		count = 0;
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(nameSort[count], promotion.getPromotionId());
			count++;
		}
		
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.DESCENDING);
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		count = 5;
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(nameSort[count], promotion.getPromotionId());
			count--;
		}
		
		searchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_FROM);
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.ASCENDING);
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		count = 0;
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validFromSortAsc[count], promotion.getPromotionId());
			count++;
		}
		
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.DESCENDING);
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		count = 0;
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validFromSortDesc[count], promotion.getPromotionId());
			count++;
		}
		
		searchPromotionRequest.setPromotionName("future");
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.NO_DATA_FOUND, searchPromotionResponse.getSearchPromotionEnum());
	}
	
	
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	public void setPromotionAdminManager(PromotionAdminManager promotionAdminManager) {
		this.promotionAdminManager = promotionAdminManager;
	}

	public void setPromotionAdminService(PromotionAdminService promotionAdminService) {
		this.promotionAdminService = promotionAdminService;
	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
}
