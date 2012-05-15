package com.fb.platform.promotion.admin.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.admin.service.PromotionAdminManager;
import com.fb.platform.promotion.admin.to.AssignCouponToUserRequest;
import com.fb.platform.promotion.admin.to.AssignCouponToUserResponse;
import com.fb.platform.promotion.admin.to.AssignCouponToUserStatusEnum;
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
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;

/**
 * @author nehaga
 *
 */
public class PromotionAdminManagerImplTest extends BaseTestCase {
	
	@Autowired
	private PromotionAdminManager promotionAdminManager = null;
	
	@Autowired
	private UserManager userManager = null;
	
	LoginResponse responseUser = null;
	
	private static int promotionId = -4100;
	
	private static int[] validFromSort = new int[] {-3001, -3002, -3003};
	
	private static int[] validTillSort = new int[] {-3003, -3002, -3001};
	
	private static int[] nameSort = new int[] {-3001, -3002, -3003};
	
	private static int[] nameSearch = new int[] {-3001, -3002};
	
	private static int[] validFromSearch = new int[] {-3001, -3002};
	
	private static int[] validTillSearch = new int[] {-3, -3002};
	
	private static int[] nameValidTillSearch = new int[] {-3002, -3003};
	
	private static int[] validFromValidTillSearch = new int[] {-3002, -3003};
	
	private static int[] nameValidFromSearch = new int[] {-3001, -3002};
	
	private static int[] filterSearch = new int[] {-3001, -3002};
	
	private static List<RulesEnum> ruleList = new ArrayList<RulesEnum>() {{
		add(RulesEnum.BUY_WORTH_X_GET_Y_RS_OFF_ON_Z_CATEGORY);
		add(RulesEnum.BUY_WORTH_X_GET_Y_RS_OFF);
		add(RulesEnum.BUY_WORTH_X_GET_Y_PERCENT_OFF);
		add(RulesEnum.BUY_X_BRAND_GET_Y_RS_OFF_ON_Z_PRODUCT);
		add(RulesEnum.BUY_WORTH_X_GET_Y_PERCENT_OFF_ON_Z_CATEGORY);
		add(RulesEnum.FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF);
		add(RulesEnum.BUY_X_GET_Y_FREE);
	}};
	
	@Before
	public void loginUser() {
		
		LoginRequest request = new LoginRequest();
		request.setUsername("neha.garani@gmail.com");
		request.setPassword("testpass");

		responseUser = userManager.login(request);
	}
	
	@Test
	public void testFetchRulesNoSession() {
		FetchRuleRequest fetchRuleRequest = new FetchRuleRequest();
		FetchRuleResponse fetchRuleResponse = promotionAdminManager.fetchRules(fetchRuleRequest);
		assertEquals(FetchRulesEnum.NO_SESSION, fetchRuleResponse.getFetchRulesEnum());
		
		fetchRuleRequest.setSessionToken("INVALID_SESSION");
		
		fetchRuleResponse = promotionAdminManager.fetchRules(fetchRuleRequest);
		assertEquals(FetchRulesEnum.NO_SESSION, fetchRuleResponse.getFetchRulesEnum());
		
		fetchRuleRequest.setSessionToken(responseUser.getSessionToken());
		
		fetchRuleResponse = promotionAdminManager.fetchRules(fetchRuleRequest);
		assertEquals(FetchRulesEnum.SUCCESS, fetchRuleResponse.getFetchRulesEnum());
		
	}
	
	@Test
	public void testFetchRules() {
		FetchRuleRequest fetchRuleRequest = new FetchRuleRequest();
		fetchRuleRequest.setSessionToken(responseUser.getSessionToken());
		
		FetchRuleResponse fetchRuleResponse = promotionAdminManager.fetchRules(fetchRuleRequest);
		
		assertEquals(FetchRulesEnum.SUCCESS, fetchRuleResponse.getFetchRulesEnum());
		assertNotNull(fetchRuleResponse.getSessionToken());
		assertNotNull(fetchRuleResponse.getRulesList());
		assertEquals(5, fetchRuleResponse.getRulesList().size());
		assertNotNull(fetchRuleResponse.getSessionToken());
		for(RuleConfigDescriptor ruleConfig : fetchRuleResponse.getRulesList()) {
			assertTrue(ruleList.contains(ruleConfig.getRulesEnum()));
		}
		
	}
	
	@Test
	public void testCreatePromotionNoSession() {
		CreatePromotionRequest createPromotionRequest = new CreatePromotionRequest();
		PromotionTO promotionTO = new PromotionTO();
		
		promotionTO.setPromotionName("New Promotion");
		promotionTO.setValidFrom(new DateTime(2012, 02, 29, 0, 0));
		promotionTO.setValidTill(new DateTime(2011, 02, 22, 0, 0));
		promotionTO.setValidTill(new DateTime(2013, 02, 22, 0, 0));
		promotionTO.setDescription("Test new promotion");
		promotionTO.setActive(true);
		promotionTO.setMaxUses(20);
		promotionTO.setMaxUsesPerUser(1);
		promotionTO.setMaxAmount(new Money(new BigDecimal(20000.00)));
		promotionTO.setMaxAmountPerUser(new Money(new BigDecimal(1000.00)));
		promotionTO.setRuleName("BUY_WORTH_X_GET_Y_RS_OFF");
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("100");
		ruleConfigList.add(configItem);
		
		promotionTO.setConfigItems(ruleConfigList);
		
		createPromotionRequest.setPromotion(promotionTO);
		
		createPromotionRequest.setSessionToken("INVALILD_SESSION");
		
		CreatePromotionResponse createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.NO_SESSION, createPromotionResponse.getCreatePromotionEnum());
	}
	
	@Test
	public void testCreatePromotionInsufficientData() {
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
		
		createPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		RuleConfigItemTO configItem = new RuleConfigItemTO();

		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("100");
		ruleConfigList.add(configItem);
		
		promotionTO.setConfigItems(ruleConfigList);
		
		createPromotionRequest.setPromotion(promotionTO);
		
		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.SUCCESS, createPromotionResponse.getCreatePromotionEnum());
	}
	
	@Test
	public void testCreatePromotionRuleConfigMissing() {
		CreatePromotionRequest createPromotionRequest = new CreatePromotionRequest();
		CreatePromotionResponse createPromotionResponse = null;
		PromotionTO promotionTO = new PromotionTO();
		
		createPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		promotionTO.setPromotionName("New Promotion");
		promotionTO.setValidFrom(new DateTime(2012, 02, 29, 0, 0));
		promotionTO.setValidTill(new DateTime(2011, 02, 22, 0, 0));
		promotionTO.setValidTill(new DateTime(2013, 02, 22, 0, 0));
		promotionTO.setDescription("Test new promotion");
		promotionTO.setActive(true);
		promotionTO.setMaxUses(20);
		promotionTO.setMaxUsesPerUser(1);
		promotionTO.setMaxAmount(new Money(new BigDecimal(20000.00)));
		promotionTO.setMaxAmountPerUser(new Money(new BigDecimal(1000.00)));
		promotionTO.setRuleName("BUY_WORTH_X_GET_Y_RS_OFF");
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		ruleConfigList.add(configItem);
		
		createPromotionRequest.setPromotion(promotionTO);
		
		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.RULE_CONFIG_MISSING, createPromotionResponse.getCreatePromotionEnum());
		
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("100");
		
		promotionTO.setConfigItems(ruleConfigList);
		
		createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.SUCCESS, createPromotionResponse.getCreatePromotionEnum());
	}
	
	@Test
	public void testCreatePromotion() {
		CreatePromotionRequest createPromotionRequest = new CreatePromotionRequest();
		PromotionTO promotionTO = new PromotionTO();
		
		promotionTO.setPromotionName("New Promotion");
		promotionTO.setValidFrom(new DateTime(2012, 02, 29, 0, 0));
		promotionTO.setValidTill(new DateTime(2011, 02, 22, 0, 0));
		promotionTO.setValidTill(new DateTime(2013, 02, 22, 0, 0));
		promotionTO.setDescription("Test new promotion");
		promotionTO.setActive(true);
		promotionTO.setMaxUses(20);
		promotionTO.setMaxUsesPerUser(1);
		promotionTO.setMaxAmount(new Money(new BigDecimal(20000.00)));
		promotionTO.setMaxAmountPerUser(new Money(new BigDecimal(1000.00)));
		promotionTO.setRuleName("BUY_WORTH_X_GET_Y_RS_OFF");
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		RuleConfigItemTO configItem = new RuleConfigItemTO();

		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("100");
		ruleConfigList.add(configItem);
		
		promotionTO.setConfigItems(ruleConfigList);
		
		createPromotionRequest.setPromotion(promotionTO);
		
		createPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		CreatePromotionResponse createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.SUCCESS, createPromotionResponse.getCreatePromotionEnum());
		assertTrue(createPromotionResponse.getPromotionId() > 0);
		assertNotNull(createPromotionResponse.getSessionToken());
		
	}
	
	@Test
	public void testViewPromotionInsufficientData() {
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();

		ViewPromotionResponse viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.INSUFFICIENT_DATA, viewPromotionResponse.getViewPromotionEnum());
		
		viewPromotionRequest.setPromotionId(promotionId);
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		
		assertEquals(ViewPromotionEnum.SUCCESS, viewPromotionResponse.getViewPromotionEnum());
	}
	
	@Test
	public void testViewPromotionNoSession() {
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		viewPromotionRequest.setPromotionId(promotionId);
		viewPromotionRequest.setSessionToken("INVALID_SESSION");
		
		ViewPromotionResponse viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.NO_SESSION, viewPromotionResponse.getViewPromotionEnum());
		
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.SUCCESS, viewPromotionResponse.getViewPromotionEnum());
	}
	
	@Test
	public void testViewPromotionNoDataFound() {
		
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		viewPromotionRequest.setPromotionId(5000);
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		ViewPromotionResponse viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.NO_DATA_FOUND, viewPromotionResponse.getViewPromotionEnum());
		
	}
	
	@Test
	public void testViewPromotion() {
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		viewPromotionRequest.setPromotionId(promotionId);
		
		ViewPromotionResponse viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		
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
		assertEquals(6, completePromotionView.getConfigItems().size());
		assertNotNull(viewPromotionResponse.getSessionToken());
	}
	
	@Test
	public void testUpdatePromotionInsufficientData() {
		
		PromotionTO promotionView = new PromotionTO();
		
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
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		promotionView.setConfigItems(ruleConfigList);
		
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("222");
		ruleConfigList.add(configItem);
		
		promotionView.setConfigItems(ruleConfigList);
		
		promotionView.setPromotionId(promotionId);
		
		updatePromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		
		assertEquals(UpdatePromotionEnum.SUCCESS, updatePromotionResponse.getUpdatePromotionEnum());
		
	}
	
	@Test
	public void testUpdatePromotionNoSession() {

		PromotionTO promotionView = new PromotionTO();
		
		UpdatePromotionRequest updatePromotionRequest = new UpdatePromotionRequest();
		updatePromotionRequest.setPromotion(promotionView);
		
		promotionView.setPromotionName("New Promotion updated");
		promotionView.setValidFrom(new DateTime(2012, 2, 22, 0, 0));
		promotionView.setValidTill(new DateTime(2011, 2, 22, 0, 0));
		promotionView.setValidTill(new DateTime(2013, 2, 22, 0, 0));
		promotionView.setDescription("Test new promotion update");
		promotionView.setActive(false);
		promotionView.setMaxUses(22);
		promotionView.setMaxUsesPerUser(2);
		promotionView.setMaxAmount(new Money(new BigDecimal(2222.00)));
		promotionView.setMaxAmountPerUser(new Money(new BigDecimal(2222.00)));
		promotionView.setRuleName("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF");
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		promotionView.setConfigItems(ruleConfigList);
		
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("222");
		ruleConfigList.add(configItem);
		
		promotionView.setPromotionId(promotionId);
		
		updatePromotionRequest.setSessionToken("INVALID_SESSION");
		
		UpdatePromotionResponse updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		
		assertEquals(UpdatePromotionEnum.NO_SESSION, updatePromotionResponse.getUpdatePromotionEnum());
	}
	
	@Test
	public void testUpdatePromotionRuleConfigMissing() {

		PromotionTO promotionView = new PromotionTO();
		
		UpdatePromotionRequest updatePromotionRequest = new UpdatePromotionRequest();
		updatePromotionRequest.setPromotion(promotionView);
		
		promotionView.setPromotionName("New Promotion updated");
		promotionView.setValidFrom(new DateTime(2012, 2, 22, 0, 0));
		promotionView.setValidTill(new DateTime(2011, 2, 22, 0, 0));
		promotionView.setValidTill(new DateTime(2013, 2, 22, 0, 0));
		promotionView.setDescription("Test new promotion update");
		promotionView.setActive(false);
		promotionView.setMaxUses(22);
		promotionView.setMaxUsesPerUser(2);
		promotionView.setMaxAmount(new Money(new BigDecimal(2222.00)));
		promotionView.setMaxAmountPerUser(new Money(new BigDecimal(2222.00)));
		promotionView.setRuleName("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF");
		promotionView.setPromotionId(promotionId);
		updatePromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		promotionView.setConfigItems(ruleConfigList);
		
		UpdatePromotionResponse updatePromotionResponse  = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.RULE_CONFIG_MISSING, updatePromotionResponse.getUpdatePromotionEnum());
		
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.RULE_CONFIG_MISSING, updatePromotionResponse.getUpdatePromotionEnum());
		
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("222");
		ruleConfigList.add(configItem);
		
		updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		
		assertEquals(UpdatePromotionEnum.SUCCESS, updatePromotionResponse.getUpdatePromotionEnum());
	}
	
	@Test
	public void testUpdatePromotionNoDataFound() {
		
		PromotionTO promotionView = new PromotionTO();
		
		UpdatePromotionRequest updatePromotionRequest = new UpdatePromotionRequest();
		updatePromotionRequest.setPromotion(promotionView);
		
		promotionView.setPromotionName("New Promotion updated");
		promotionView.setValidFrom(new DateTime(2012, 2, 22, 0, 0));
		promotionView.setValidTill(new DateTime(2013, 2, 22, 0, 0));
		promotionView.setDescription("Test new promotion update");
		promotionView.setActive(false);
		promotionView.setMaxUses(22);
		promotionView.setMaxUsesPerUser(2);
		promotionView.setMaxAmount(new Money(new BigDecimal(2222.00)));
		promotionView.setMaxAmountPerUser(new Money(new BigDecimal(2222.00)));
		promotionView.setRuleName("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF");
		updatePromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		promotionView.setConfigItems(ruleConfigList);
		
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("222");
		ruleConfigList.add(configItem);
		
		promotionView.setPromotionId(5000);
		
		UpdatePromotionResponse updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.NO_DATA_FOUND, updatePromotionResponse.getUpdatePromotionEnum());
	}
	
	@Test
	public void testUpdatePromotion() {
		PromotionTO promotionView = new PromotionTO();
		
		UpdatePromotionRequest updatePromotionRequest = new UpdatePromotionRequest();
		updatePromotionRequest.setPromotion(promotionView);
		
		promotionView.setPromotionName("New Promotion updated");
		promotionView.setValidFrom(new DateTime(2012, 2, 22, 0, 0));
		promotionView.setValidTill(new DateTime(2013, 2, 22, 0, 0));
		promotionView.setDescription("Test new promotion update");
		promotionView.setActive(false);
		promotionView.setMaxUses(22);
		promotionView.setMaxUsesPerUser(2);
		promotionView.setMaxAmount(new Money(new BigDecimal(2222.00)));
		promotionView.setMaxAmountPerUser(new Money(new BigDecimal(2222.00)));
		promotionView.setRuleName("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF");
		updatePromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		promotionView.setConfigItems(ruleConfigList);
		
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("222");
		ruleConfigList.add(configItem);
		
		promotionView.setPromotionId(promotionId);
		
		UpdatePromotionResponse updatePromotionResponse = promotionAdminManager.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.SUCCESS, updatePromotionResponse.getUpdatePromotionEnum());
		
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		viewPromotionRequest.setPromotionId(promotionId);
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		ViewPromotionResponse viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);
		
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
		assertEquals(1, completePromotionView.getConfigItems().size());
		assertNotNull(updatePromotionResponse.getSessionToken());
		
	}
	
	@Test
	public void testSearchPromotionInsufficientData() {
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.INSUFFICIENT_DATA, searchPromotionResponse.getSearchPromotionEnum());
		
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
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
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
	}
	
	@Test
	public void testSearchPromotionNoSession() {
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(10);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken("INVALID_SESSION");
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.NO_SESSION, searchPromotionResponse.getSearchPromotionEnum());
	}
	
	@Test
	public void testSearchPromotionNoDataFound() {
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(10);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		searchPromotionRequest.setPromotionName("future");
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.NO_DATA_FOUND, searchPromotionResponse.getSearchPromotionEnum());
		assertNotNull(searchPromotionResponse.getSessionToken());
	}
	
	@Test
	public void testSearchPromotionValidFromSortAscending() {
		int count = 0;
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(3);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		searchPromotionRequest.setPromotionName("end to end");
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		searchPromotionRequest.setValidTill(new DateTime(2012, 6, 30, 0, 0));
		searchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_FROM);
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.ASCENDING);
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validFromSort[count], promotion.getPromotionId());
			count++;
		}
	}
	
	@Test
	public void testSearchPromotionValidFromSortDescending() {
		int count = 2;
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(3);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		searchPromotionRequest.setPromotionName("end to end");
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		searchPromotionRequest.setValidTill(new DateTime(2012, 6, 30, 0, 0));
		searchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_FROM);
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.DESCENDING);
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validFromSort[count], promotion.getPromotionId());
			count--;
		}
	}
	
	@Test
	public void testSearchPromotionValidTillSortAscending() {
		int count = 0;
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(3);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		searchPromotionRequest.setPromotionName("end to end");
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		searchPromotionRequest.setValidTill(new DateTime(2012, 6, 30, 0, 0));
		searchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_TILL);
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.ASCENDING);
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validTillSort[count], promotion.getPromotionId());
			count++;
		}
	}
	
	@Test
	public void testSearchPromotionValidTillSortDescending() {
		int count = 2;
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(3);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		searchPromotionRequest.setPromotionName("end to end");
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		searchPromotionRequest.setValidTill(new DateTime(2012, 6, 30, 0, 0));
		searchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_TILL);
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.DESCENDING);
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validTillSort[count], promotion.getPromotionId());
			count--;
		}
	}
	
	@Test
	public void testSearchPromotionNameSortAscending() {
		int count = 0;
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(3);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		searchPromotionRequest.setPromotionName("end to end");
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		searchPromotionRequest.setValidTill(new DateTime(2012, 6, 30, 0, 0));
		searchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.NAME);
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.ASCENDING);
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(nameSort[count], promotion.getPromotionId());
			count++;
		}
	}
	
	@Test
	public void testSearchPromotionNameSortDescending() {
		int count = 2;
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(3);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		searchPromotionRequest.setPromotionName("end to end");
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		searchPromotionRequest.setValidTill(new DateTime(2012, 6, 30, 0, 0));
		searchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.NAME);
		searchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.DESCENDING);
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(nameSort[count], promotion.getPromotionId());
			count--;
		}
	}
	
	@Test
	public void testSearchPromotionName() {
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(2);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		searchPromotionRequest.setPromotionName("end to end");
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(3, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());
		
		int count = 0;
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(nameSearch[count], promotion.getPromotionId());
			count++;
		}
	}
	
	@Test
	public void testSearchPromotionValidFrom() {
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(2);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(12, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());
		
		int count = 0;
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validFromSearch[count], promotion.getPromotionId());
			count++;
		}
	}
	
	@Test
	public void testSearchPromotionValidTill() {
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(2);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		searchPromotionRequest.setValidTill(new DateTime(2012, 5, 21, 0, 0));
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(3, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());
		
		int count = 0;
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validTillSearch[count], promotion.getPromotionId());
			count++;
		}	
	}
	
	@Test
	public void testSearchPromotionNameAndValidTill() {
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(2);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		searchPromotionRequest.setPromotionName("end to end");
		searchPromotionRequest.setValidTill(new DateTime(2012, 5, 21, 0, 0));
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(2, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());
		
		int count = 0;
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(nameValidTillSearch[count], promotion.getPromotionId());
			count++;
		}
	}
	
	@Test
	public void testSearchPromotionNameAndValidFrom() {
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(2);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		searchPromotionRequest.setPromotionName("end to end");
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(3, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());
		
		int count = 0;
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(nameValidFromSearch[count], promotion.getPromotionId());
			count++;
		}
	}
	
	@Test
	public void testSearchPromotionValidFromAndValidTill() {
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(2);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		searchPromotionRequest.setValidTill(new DateTime(2012, 5, 21, 0, 0));
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(2, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());
		
		int count = 0;
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(validFromValidTillSearch[count], promotion.getPromotionId());
			count++;
		}
	}
	
	@Test
	public void testSearchPromotionInActive() {
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(false);
		searchPromotionRequest.setBatchSize(10);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(1, searchPromotionResponse.getTotalCount());
		assertEquals(1, searchPromotionResponse.getPromotionsList().size());
		assertEquals(-6, searchPromotionResponse.getPromotionsList().get(0).getPromotionId());
	}
	
	@Test
	public void testSearchAllPromotion() {
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(10);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(18, searchPromotionResponse.getTotalCount());
		assertEquals(10, searchPromotionResponse.getPromotionsList().size());
	}
	
	@Test
	public void testSearchPromotion() {
		int count = 0;
		
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();
		
		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(2);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		
		searchPromotionRequest.setPromotionName("end to end");
		searchPromotionRequest.setValidFrom(new DateTime(2012, 1, 2, 0, 0));
		searchPromotionRequest.setValidTill(new DateTime(2012, 6, 30, 0, 0));
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager.searchPromotion(searchPromotionRequest);
		
		assertEquals(SearchPromotionEnum.SUCCESS, searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(3, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());
		assertNotNull(searchPromotionResponse.getSessionToken());
		
		for(PromotionTO promotion : searchPromotionResponse.getPromotionsList()) {
			assertEquals(filterSearch[count], promotion.getPromotionId());
			count++;
		}
	}

	@Test
	public void assignCouponToUser() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("pre_issued_1");
		request.setUserId(-4);
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.SUCCESS, response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignCouponToInvalidUser() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("pre_issued_1");
		request.setUserId(-2844);
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_USER_ID, response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignInvalidCouponCode() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("i_am_garbage_i_am_not_there");
		request.setUserId(-4);
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_COUPON_CODE, response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignGlobalCoupon() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("global_coupon_1");
		request.setUserId(-4);
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_COUPON_TYPE, response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void reAssign() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("pre_issued_1");
		request.setUserId(-2);
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.COUPON_ALREADY_ASSIGNED_TO_USER, response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignCouponNoSession() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("pre_issued_1");
		request.setUserId(-4);
		//request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.NO_SESSION, response.getStatus());
		assertNull(response.getSessionToken());
	}

	@Test
	public void nullAssignRequest() {
		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(null);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.NO_SESSION, response.getStatus());
		assertNull(response.getSessionToken());
	}

	@Test
	public void assignNullCouponCode() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		//request.setCouponCode("pre_issued_1");
		request.setUserId(-4);
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_COUPON_CODE, response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignEmptyCouponCode() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("");
		request.setUserId(-4);
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_COUPON_CODE, response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignNoUser() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("pre_issued_1");
		//request.setUserId(-4);
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_USER_ID, response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	public void setPromotionAdminManager(PromotionAdminManager promotionAdminManager) {
		this.promotionAdminManager = promotionAdminManager;
	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
}
