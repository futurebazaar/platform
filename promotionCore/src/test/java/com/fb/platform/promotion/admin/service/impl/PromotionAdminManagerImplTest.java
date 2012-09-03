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
import com.fb.platform.promotion.admin.to.CouponBasicDetails;
import com.fb.platform.promotion.admin.to.CreateCouponRequest;
import com.fb.platform.promotion.admin.to.CreateCouponResponse;
import com.fb.platform.promotion.admin.to.CreateCouponStatusEnum;
import com.fb.platform.promotion.admin.to.CreatePromotionEnum;
import com.fb.platform.promotion.admin.to.CreatePromotionRequest;
import com.fb.platform.promotion.admin.to.CreatePromotionResponse;
import com.fb.platform.promotion.admin.to.FetchRuleRequest;
import com.fb.platform.promotion.admin.to.FetchRuleResponse;
import com.fb.platform.promotion.admin.to.FetchRulesEnum;
import com.fb.platform.promotion.admin.to.GetPromotionUsageEnum;
import com.fb.platform.promotion.admin.to.GetPromotionUsageRequest;
import com.fb.platform.promotion.admin.to.GetPromotionUsageResponse;
import com.fb.platform.promotion.admin.to.PromotionTO;
import com.fb.platform.promotion.admin.to.RuleConfigItemTO;
import com.fb.platform.promotion.admin.to.SearchCouponRequest;
import com.fb.platform.promotion.admin.to.SearchCouponResponse;
import com.fb.platform.promotion.admin.to.SearchCouponStatusEnum;
import com.fb.platform.promotion.admin.to.SearchPromotionEnum;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin.to.SearchPromotionRequest;
import com.fb.platform.promotion.admin.to.SearchPromotionResponse;
import com.fb.platform.promotion.admin.to.SearchScratchCardRequest;
import com.fb.platform.promotion.admin.to.SearchScratchCardResponse;
import com.fb.platform.promotion.admin.to.SearchScratchCardStatusEnum;
import com.fb.platform.promotion.admin.to.SortOrder;
import com.fb.platform.promotion.admin.to.UpdatePromotionEnum;
import com.fb.platform.promotion.admin.to.UpdatePromotionRequest;
import com.fb.platform.promotion.admin.to.UpdatePromotionResponse;
import com.fb.platform.promotion.admin.to.ViewPromotionEnum;
import com.fb.platform.promotion.admin.to.ViewPromotionRequest;
import com.fb.platform.promotion.admin.to.ViewPromotionResponse;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptor;
import com.fb.platform.promotion.to.AlphaNumericType;
import com.fb.platform.promotion.to.AlphabetCase;
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
	
	private static int[] validTillSearch = new int[] {-3};
	
	private static int[] nameValidTillSearch = new int[] {-3002, -3003};
	
	private static int[] validFromValidTillSearch = new int[] {-4000, -4100};
	
	private static int[] nameValidFromSearch = new int[] {-3001, -3002};
	
	private static int[] filterSearch = new int[] {-3001, -3002};
	
	private static List<RulesEnum> ruleList = new ArrayList<RulesEnum>() {{
		add(RulesEnum.BUY_WORTH_X_GET_Y_RS_OFF);
		add(RulesEnum.BUY_WORTH_X_GET_Y_PERCENT_OFF);
		add(RulesEnum.BUY_X_BRAND_GET_Y_RS_OFF_ON_Z_PRODUCT);
		add(RulesEnum.FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF);
		add(RulesEnum.BUY_X_GET_Y_FREE);
		add(RulesEnum.BUY_X_QUANTITY_GET_VARIABLE_PERCENT_OFF);
		add(RulesEnum.MONTHLY_DISCOUNT_RS_OFF);
		add(RulesEnum.CATEGORY_BASED_VARIABLE_PERCENT_OFF);
		add(RulesEnum.DISCOUNT_ON_CLEARANCE_PRODUCT);
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
		FetchRuleResponse fetchRuleResponse = promotionAdminManager
				.fetchRules(fetchRuleRequest);
		assertEquals(FetchRulesEnum.NO_SESSION,
				fetchRuleResponse.getFetchRulesEnum());

		fetchRuleRequest.setSessionToken("INVALID_SESSION");

		fetchRuleResponse = promotionAdminManager.fetchRules(fetchRuleRequest);
		assertEquals(FetchRulesEnum.NO_SESSION,
				fetchRuleResponse.getFetchRulesEnum());

		fetchRuleRequest.setSessionToken(responseUser.getSessionToken());

		fetchRuleResponse = promotionAdminManager.fetchRules(fetchRuleRequest);
		assertEquals(FetchRulesEnum.SUCCESS,
				fetchRuleResponse.getFetchRulesEnum());

	}

	@Test
	public void testFetchRules() {
		FetchRuleRequest fetchRuleRequest = new FetchRuleRequest();
		fetchRuleRequest.setSessionToken(responseUser.getSessionToken());

		FetchRuleResponse fetchRuleResponse = promotionAdminManager
				.fetchRules(fetchRuleRequest);

		assertEquals(FetchRulesEnum.SUCCESS,
				fetchRuleResponse.getFetchRulesEnum());
		assertNotNull(fetchRuleResponse.getSessionToken());
		assertNotNull(fetchRuleResponse.getRulesList());
		assertEquals(9, fetchRuleResponse.getRulesList().size());
		assertNotNull(fetchRuleResponse.getSessionToken());
		for (RuleConfigDescriptor ruleConfig : fetchRuleResponse.getRulesList()) {
			assertTrue(ruleList.contains(ruleConfig.getRulesEnum()));
		}

	}

	@Test
	public void testCreatePromotionNoSession() {
		CreatePromotionRequest createPromotionRequest = new CreatePromotionRequest();
		PromotionTO promotionTO = new PromotionTO();

		promotionTO.setPromotionName("New Promotion");
		promotionTO.setValidFrom(new DateTime(2012, 02, 29, 0, 0));
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

		CreatePromotionResponse createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.NO_SESSION,
				createPromotionResponse.getCreatePromotionEnum());
	}

	@Test
	public void testCreatePromotionInsufficientData() {
		CreatePromotionRequest createPromotionRequest = new CreatePromotionRequest();
		PromotionTO promotionTO = new PromotionTO();
		createPromotionRequest.setPromotion(promotionTO);

		CreatePromotionResponse createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA,
				createPromotionResponse.getCreatePromotionEnum());

		promotionTO.setPromotionName("New Promotion");

		createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA,
				createPromotionResponse.getCreatePromotionEnum());

		promotionTO.setValidFrom(new DateTime(2012, 02, 29, 0, 0));
		promotionTO.setValidTill(new DateTime(2011, 02, 22, 0, 0));

		createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA,
				createPromotionResponse.getCreatePromotionEnum());

		promotionTO.setValidTill(new DateTime(2013, 02, 22, 0, 0));

		promotionTO.setDescription("Test new promotion");
		promotionTO.setActive(true);

		createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA,
				createPromotionResponse.getCreatePromotionEnum());

		promotionTO.setMaxUses(20);
		promotionTO.setMaxUsesPerUser(1);
		promotionTO.setMaxAmount(new Money(new BigDecimal(20000.00)));
		promotionTO.setMaxAmountPerUser(new Money(new BigDecimal(1000.00)));

		promotionTO.setRuleName("BUY_WORTH_X_GET_Y_RS_OFF_WRONG");

		createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.INSUFFICIENT_DATA,
				createPromotionResponse.getCreatePromotionEnum());

		promotionTO.setRuleName("BUY_WORTH_X_GET_Y_RS_OFF");

		createPromotionRequest.setSessionToken(responseUser.getSessionToken());

		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		RuleConfigItemTO configItem = new RuleConfigItemTO();

		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("100");
		ruleConfigList.add(configItem);

		promotionTO.setConfigItems(ruleConfigList);

		createPromotionRequest.setPromotion(promotionTO);

		createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.SUCCESS,
				createPromotionResponse.getCreatePromotionEnum());
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

		createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.RULE_CONFIG_MISSING,
				createPromotionResponse.getCreatePromotionEnum());

		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("100");

		promotionTO.setConfigItems(ruleConfigList);

		createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.SUCCESS,
				createPromotionResponse.getCreatePromotionEnum());
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

		CreatePromotionResponse createPromotionResponse = promotionAdminManager
				.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.SUCCESS,
				createPromotionResponse.getCreatePromotionEnum());
		assertTrue(createPromotionResponse.getPromotionId() > 0);
		assertNotNull(createPromotionResponse.getSessionToken());

	}

	@Test
	public void testViewPromotionInsufficientData() {
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();

		ViewPromotionResponse viewPromotionResponse = promotionAdminManager
				.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.INSUFFICIENT_DATA,
				viewPromotionResponse.getViewPromotionEnum());

		viewPromotionRequest.setPromotionId(promotionId);
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());

		viewPromotionResponse = promotionAdminManager
				.viewPromotion(viewPromotionRequest);

		assertEquals(ViewPromotionEnum.SUCCESS,
				viewPromotionResponse.getViewPromotionEnum());
	}

	@Test
	public void testViewPromotionNoSession() {
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		viewPromotionRequest.setPromotionId(promotionId);
		viewPromotionRequest.setSessionToken("INVALID_SESSION");

		ViewPromotionResponse viewPromotionResponse = promotionAdminManager
				.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.NO_SESSION,
				viewPromotionResponse.getViewPromotionEnum());

		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());

		viewPromotionResponse = promotionAdminManager
				.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.SUCCESS,
				viewPromotionResponse.getViewPromotionEnum());
	}

	@Test
	public void testViewPromotionNoDataFound() {

		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		viewPromotionRequest.setPromotionId(5000);
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());

		ViewPromotionResponse viewPromotionResponse = promotionAdminManager
				.viewPromotion(viewPromotionRequest);
		assertEquals(ViewPromotionEnum.NO_DATA_FOUND,
				viewPromotionResponse.getViewPromotionEnum());

	}

	@Test
	public void testViewPromotion() {
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();

		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		viewPromotionRequest.setPromotionId(promotionId);

		ViewPromotionResponse viewPromotionResponse = promotionAdminManager
				.viewPromotion(viewPromotionRequest);

		assertEquals(ViewPromotionEnum.SUCCESS,
				viewPromotionResponse.getViewPromotionEnum());
		PromotionTO completePromotionView = viewPromotionResponse
				.getPromotionCompleteView();

		assertEquals("New Promotion Copy",
				completePromotionView.getPromotionName());
		assertEquals("Test new promotion via Promotion Admin",
				completePromotionView.getDescription());
		assertEquals(29, completePromotionView.getValidFrom().getDayOfMonth());
		assertEquals(2012, completePromotionView.getValidFrom().getYear());
		assertEquals(12, completePromotionView.getValidFrom().getMonthOfYear());
		assertEquals(01, completePromotionView.getValidTill().getDayOfMonth());
		assertEquals(2013, completePromotionView.getValidTill().getYear());
		assertEquals(3, completePromotionView.getValidTill().getMonthOfYear());
		assertEquals(true, completePromotionView.isActive());
		assertEquals(0, completePromotionView.getCouponCount());
		assertEquals(20, completePromotionView.getMaxUses());
		assertEquals(2000, completePromotionView.getMaxAmount().getAmount()
				.intValue());
		assertEquals(1, completePromotionView.getMaxUsesPerUser());
		assertEquals(1000, completePromotionView.getMaxAmountPerUser()
				.getAmount().intValue());
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

		UpdatePromotionResponse updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);

		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA,
				updatePromotionResponse.getUpdatePromotionEnum());

		promotionView.setPromotionName("New Promotion updated");

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA,
				updatePromotionResponse.getUpdatePromotionEnum());

		promotionView.setValidFrom(new DateTime(2012, 2, 22, 0, 0));
		promotionView.setValidTill(new DateTime(2011, 2, 22, 0, 0));

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA,
				updatePromotionResponse.getUpdatePromotionEnum());

		promotionView.setValidTill(new DateTime(2013, 2, 22, 0, 0));

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA,
				updatePromotionResponse.getUpdatePromotionEnum());

		promotionView.setDescription("Test new promotion update");
		promotionView.setActive(false);

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA,
				updatePromotionResponse.getUpdatePromotionEnum());

		promotionView.setMaxUses(22);
		promotionView.setMaxUsesPerUser(2);
		promotionView.setMaxAmount(new Money(new BigDecimal(2222.00)));
		promotionView.setMaxAmountPerUser(new Money(new BigDecimal(2222.00)));

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA,
				updatePromotionResponse.getUpdatePromotionEnum());

		promotionView
				.setRuleName("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF_WRONG");

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA,
				updatePromotionResponse.getUpdatePromotionEnum());

		promotionView.setRuleName("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF");

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INSUFFICIENT_DATA,
				updatePromotionResponse.getUpdatePromotionEnum());

		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		promotionView.setConfigItems(ruleConfigList);

		RuleConfigItemTO configItem = new RuleConfigItemTO();

		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("222");
		ruleConfigList.add(configItem);

		promotionView.setConfigItems(ruleConfigList);

		promotionView.setId(promotionId);

		updatePromotionRequest.setSessionToken(responseUser.getSessionToken());

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);

		assertEquals(UpdatePromotionEnum.SUCCESS,
				updatePromotionResponse.getUpdatePromotionEnum());

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

		promotionView.setId(promotionId);

		updatePromotionRequest.setSessionToken("INVALID_SESSION");

		UpdatePromotionResponse updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);

		assertEquals(UpdatePromotionEnum.NO_SESSION,
				updatePromotionResponse.getUpdatePromotionEnum());
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
		promotionView.setId(promotionId);
		updatePromotionRequest.setSessionToken(responseUser.getSessionToken());

		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();
		promotionView.setConfigItems(ruleConfigList);

		UpdatePromotionResponse updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.RULE_CONFIG_MISSING,
				updatePromotionResponse.getUpdatePromotionEnum());

		RuleConfigItemTO configItem = new RuleConfigItemTO();

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.RULE_CONFIG_MISSING,
				updatePromotionResponse.getUpdatePromotionEnum());

		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("222");
		ruleConfigList.add(configItem);

		updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);

		assertEquals(UpdatePromotionEnum.SUCCESS,
				updatePromotionResponse.getUpdatePromotionEnum());
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

		promotionView.setId(5000);

		UpdatePromotionResponse updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.INVALID_PROMOTION_ID,
				updatePromotionResponse.getUpdatePromotionEnum());
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

		promotionView.setId(promotionId);

		UpdatePromotionResponse updatePromotionResponse = promotionAdminManager
				.updatePromotion(updatePromotionRequest);
		assertEquals(UpdatePromotionEnum.SUCCESS,
				updatePromotionResponse.getUpdatePromotionEnum());

		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		viewPromotionRequest.setPromotionId(promotionId);
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		ViewPromotionResponse viewPromotionResponse = promotionAdminManager
				.viewPromotion(viewPromotionRequest);

		PromotionTO completePromotionView = viewPromotionResponse
				.getPromotionCompleteView();

		assertEquals("New Promotion updated",
				completePromotionView.getPromotionName());
		assertEquals("Test new promotion update",
				completePromotionView.getDescription());
		assertEquals(22, completePromotionView.getValidFrom().getDayOfMonth());
		assertEquals(2012, completePromotionView.getValidFrom().getYear());
		assertEquals(2, completePromotionView.getValidFrom().getMonthOfYear());
		assertEquals(22, completePromotionView.getValidTill().getDayOfMonth());
		assertEquals(2013, completePromotionView.getValidTill().getYear());
		assertEquals(2, completePromotionView.getValidTill().getMonthOfYear());
		assertEquals(false, completePromotionView.isActive());
		assertEquals(0, completePromotionView.getCouponCount());
		assertEquals(22, completePromotionView.getMaxUses());
		assertEquals(2222, completePromotionView.getMaxAmount().getAmount()
				.intValue());
		assertEquals(2, completePromotionView.getMaxUsesPerUser());
		assertEquals(2222, completePromotionView.getMaxAmountPerUser()
				.getAmount().intValue());
		assertEquals(-7, completePromotionView.getRuleId());
		assertNotNull(completePromotionView.getConfigItems());
		assertEquals(1, completePromotionView.getConfigItems().size());
		assertNotNull(updatePromotionResponse.getSessionToken());

	}

	@Test
	public void testSearchPromotionInsufficientData() {

		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.INSUFFICIENT_DATA,
				searchPromotionResponse.getSearchPromotionEnum());

		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.INSUFFICIENT_DATA,
				searchPromotionResponse.getSearchPromotionEnum());

		searchPromotionRequest.setStartRecord(-5);

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.INSUFFICIENT_DATA,
				searchPromotionResponse.getSearchPromotionEnum());

		searchPromotionRequest.setStartRecord(0);

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.INSUFFICIENT_DATA,
				searchPromotionResponse.getSearchPromotionEnum());

		searchPromotionRequest.setBatchSize(10);

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());
	}

	@Test
	public void testSearchPromotionNoSession() {

		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();

		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(10);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken("INVALID_SESSION");

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.NO_SESSION,
				searchPromotionResponse.getSearchPromotionEnum());
	}

	@Test
	public void testSearchPromotionNoDataFound() {
		SearchPromotionRequest searchPromotionRequest = new SearchPromotionRequest();

		searchPromotionRequest.setActive(true);
		searchPromotionRequest.setBatchSize(10);
		searchPromotionRequest.setStartRecord(0);
		searchPromotionRequest.setSessionToken(responseUser.getSessionToken());
		searchPromotionRequest.setPromotionName("future");

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.NO_DATA_FOUND,
				searchPromotionResponse.getSearchPromotionEnum());
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
		searchPromotionRequest.setValidTill(new DateTime(2013, 6, 30, 0, 0));
		searchPromotionRequest
				.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_FROM);
		searchPromotionRequest.setSortOrder(SortOrder.ASCENDING);

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(validFromSort[count], promotion.getId());
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
		searchPromotionRequest.setValidTill(new DateTime(2013, 6, 30, 0, 0));
		searchPromotionRequest
				.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_FROM);
		searchPromotionRequest.setSortOrder(SortOrder.DESCENDING);

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(validFromSort[count], promotion.getId());
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
		searchPromotionRequest.setValidTill(new DateTime(2013, 6, 30, 0, 0));
		searchPromotionRequest
				.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_TILL);
		searchPromotionRequest.setSortOrder(SortOrder.ASCENDING);

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(validTillSort[count], promotion.getId());
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
		searchPromotionRequest.setValidTill(new DateTime(2013, 6, 30, 0, 0));
		searchPromotionRequest
				.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_TILL);
		searchPromotionRequest.setSortOrder(SortOrder.DESCENDING);

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(validTillSort[count], promotion.getId());
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
		searchPromotionRequest.setValidTill(new DateTime(2013, 6, 30, 0, 0));
		searchPromotionRequest
				.setSearchPromotionOrderBy(SearchPromotionOrderBy.NAME);
		searchPromotionRequest.setSortOrder(SortOrder.ASCENDING);

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(nameSort[count], promotion.getId());
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
		searchPromotionRequest.setValidTill(new DateTime(2013, 6, 30, 0, 0));
		searchPromotionRequest
				.setSearchPromotionOrderBy(SearchPromotionOrderBy.NAME);
		searchPromotionRequest.setSortOrder(SortOrder.DESCENDING);

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(nameSort[count], promotion.getId());
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

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(3, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());

		int count = 0;

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(nameSearch[count], promotion.getId());
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
		assertEquals(20, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());

		int count = 0;

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(validFromSearch[count], promotion.getId());
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

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(1, searchPromotionResponse.getTotalCount());
		assertEquals(1, searchPromotionResponse.getPromotionsList().size());

		int count = 0;

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(validTillSearch[count], promotion.getId());
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
		searchPromotionRequest.setValidTill(new DateTime(2013, 5, 21, 0, 0));

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(2, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());

		int count = 0;

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(nameValidTillSearch[count], promotion.getId());
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

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(3, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());

		int count = 0;

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(nameValidFromSearch[count], promotion.getId());
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

		searchPromotionRequest.setValidFrom(new DateTime(2012, 11, 2, 0, 0));
		searchPromotionRequest.setValidTill(new DateTime(2013, 3, 22, 0, 0));

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(2, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());

		int count = 0;

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(validFromValidTillSearch[count], promotion.getId());
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

		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);
		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(1, searchPromotionResponse.getTotalCount());
		assertEquals(1, searchPromotionResponse.getPromotionsList().size());
		assertEquals(-6, searchPromotionResponse.getPromotionsList().get(0)
				.getId());
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
		assertEquals(28, searchPromotionResponse.getTotalCount());
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
		searchPromotionRequest.setValidTill(new DateTime(2013, 6, 30, 0, 0));
		SearchPromotionResponse searchPromotionResponse = promotionAdminManager
				.searchPromotion(searchPromotionRequest);

		assertEquals(SearchPromotionEnum.SUCCESS,
				searchPromotionResponse.getSearchPromotionEnum());
		assertEquals(3, searchPromotionResponse.getTotalCount());
		assertEquals(2, searchPromotionResponse.getPromotionsList().size());
		assertNotNull(searchPromotionResponse.getSessionToken());

		for (PromotionTO promotion : searchPromotionResponse
				.getPromotionsList()) {
			assertEquals(filterSearch[count], promotion.getId());
			count++;
		}
	}

	@Test
	public void assignCouponToUser() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("pre_issued_1");
		request.setUserName("test@test.com");
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.SUCCESS, response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignCouponToInvalidUser() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("pre_issued_1");
		request.setUserName("test@test");
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_USER,
				response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignInvalidCouponCode() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("i_am_garbage_i_am_not_there");
		request.setUserName("test@test.com");
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_COUPON_CODE,
				response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignGlobalCoupon() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("global_coupon_1");
		request.setUserName("test@test.com");
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_COUPON_TYPE,
				response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void reAssign() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("GLOBAL_COUPON_4448");
		request.setUserName("jasvipul@gmail.com");
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(
				AssignCouponToUserStatusEnum.COUPON_ALREADY_ASSIGNED_TO_USER,
				response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignCouponNoSession() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("pre_issued_1");
		request.setUserName("jasvipul@gmail.com");

		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.NO_SESSION,
				response.getStatus());
		assertNull(response.getSessionToken());
	}

	@Test
	public void nullAssignRequest() {
		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(null);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.NO_SESSION,
				response.getStatus());
		assertNull(response.getSessionToken());
	}

	@Test
	public void assignNullCouponCode() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setUserName("jasvipul@gmail.com");
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_COUPON_CODE,
				response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignEmptyCouponCode() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("");
		request.setUserName("jasvipul@gmail.com");
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_COUPON_CODE,
				response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void assignNoUser() {
		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setCouponCode("pre_issued_1");
		request.setSessionToken(responseUser.getSessionToken());

		AssignCouponToUserResponse response = promotionAdminManager
				.assignCouponToUser(request);

		assertNotNull(response);
		assertEquals(AssignCouponToUserStatusEnum.INVALID_USER,
				response.getStatus());
		assertNotNull(response.getSessionToken());
	}

	@Test
	public void searchCouponDataFound() {
		SearchCouponRequest searchCouponRequest = new SearchCouponRequest();

		searchCouponRequest.setCouponCode("GlobalCoupon1000Off4");
		searchCouponRequest.setSessionToken(responseUser.getSessionToken());

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(searchCouponRequest);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.SUCCESS, response.getStatus());
		assertEquals(1, response.getCouponBasicDetailsList().size());

		CouponBasicDetails couponBasicDetails = response
				.getCouponBasicDetailsList().get(0);
		assertEquals("GlobalCoupon1000Off4", couponBasicDetails.getCouponCode());
		assertEquals(CouponType.GLOBAL, couponBasicDetails.getCouponType());
		assertEquals(-2003, couponBasicDetails.getCouponId());
	}

	@Test
	public void searchCouponUserNameCouponCode() {
		SearchCouponRequest request = new SearchCouponRequest();

		request.setUserName("test.admin@gmail.com");
		request.setSessionToken(responseUser.getSessionToken());
		request.setCouponCode("WINFUTURE");

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(request);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.SUCCESS, response.getStatus());
		assertEquals(1, response.getCouponBasicDetailsList().size());
		assertEquals(1, response.getTotalCount());

	}

	@Test
	public void searchCouponOnlyUserName() {
		SearchCouponRequest searchCouponRequest = new SearchCouponRequest();

		searchCouponRequest.setUserName("test.admin@gmail.com");
		searchCouponRequest.setSessionToken(responseUser.getSessionToken());
		searchCouponRequest.setStartRecord(0);
		;
		searchCouponRequest.setBatchSize(100);

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(searchCouponRequest);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.SUCCESS, response.getStatus());
		assertEquals(8, response.getCouponBasicDetailsList().size());
		assertEquals(8, response.getTotalCount());
	}

	@Test
	public void searchCouponOnlyUserNameBatchSize() {
		SearchCouponRequest request = new SearchCouponRequest();

		request.setUserName("test.admin@gmail.com");
		request.setSessionToken(responseUser.getSessionToken());
		request.setBatchSize(4);

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(request);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.SUCCESS, response.getStatus());
		assertEquals(4, response.getCouponBasicDetailsList().size());
		assertEquals(8, response.getTotalCount());

	}

	@Test
	public void searchCouponOnlyCouponCode() {
		SearchCouponRequest request = new SearchCouponRequest();

		request.setSessionToken(responseUser.getSessionToken());
		request.setCouponCode("WINFUTURE");

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(request);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.SUCCESS, response.getStatus());
		assertEquals(1, response.getCouponBasicDetailsList().size());
		assertEquals(1, response.getTotalCount());

	}

	@Test
	public void searchCouponInvalidCouponCode() {
		SearchCouponRequest request = new SearchCouponRequest();

		request.setSessionToken(responseUser.getSessionToken());
		request.setCouponCode("WINFUTU");

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(request);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.NO_DATA_FOUND, response.getStatus());
		assertEquals(0, response.getCouponBasicDetailsList().size());
		assertEquals(0, response.getTotalCount());

	}

	@Test
	public void searchCouponUserNameInvalidCouponCode() {
		SearchCouponRequest request = new SearchCouponRequest();

		request.setUserName("test.admin@gmail.com");
		request.setSessionToken(responseUser.getSessionToken());
		request.setCouponCode("WINFUTU");

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(request);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.NO_DATA_FOUND, response.getStatus());
		assertEquals(0, response.getCouponBasicDetailsList().size());
		assertEquals(0, response.getTotalCount());
	}

	@Test
	public void searchCouponCouponCodeInvalidUserName() {
		SearchCouponRequest request = new SearchCouponRequest();

		request.setUserName("test.admin@gmail.");
		request.setSessionToken(responseUser.getSessionToken());
		request.setCouponCode("WINFUTURE");

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(request);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.INVALID_USER, response.getStatus());
		assertEquals(0, response.getCouponBasicDetailsList().size());
		assertEquals(0, response.getTotalCount());
	}

	@Test
	public void searchCouponBothInvalidCouponCodeUserName() {
		SearchCouponRequest request = new SearchCouponRequest();

		request.setUserName("test.admin@gmail.");
		request.setSessionToken(responseUser.getSessionToken());
		request.setCouponCode("WINFUTE");

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(request);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.INVALID_USER, response.getStatus());
		assertEquals(0, response.getCouponBasicDetailsList().size());
		assertEquals(0, response.getTotalCount());
	}

	@Test
	public void searchCouponInsufficientData() {
		SearchCouponRequest searchCouponRequest = new SearchCouponRequest();

		searchCouponRequest.setSessionToken(responseUser.getSessionToken());

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(searchCouponRequest);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.INSUFFICIENT_DATA,
				response.getStatus());
		assertEquals(0, response.getCouponBasicDetailsList().size());
	}

	@Test
	public void searchCouponNoDataFound() {
		SearchCouponRequest searchCouponRequest = new SearchCouponRequest();

		searchCouponRequest.setCouponCode("invalid_coupon_code");
		searchCouponRequest.setSessionToken(responseUser.getSessionToken());

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(searchCouponRequest);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.NO_DATA_FOUND, response.getStatus());
		assertEquals(0, response.getCouponBasicDetailsList().size());
	}

	@Test
	public void searchCouponNoSession() {
		SearchCouponRequest searchCouponRequest = new SearchCouponRequest();

		searchCouponRequest.setCouponCode("invalid_coupon_code");

		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(searchCouponRequest);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.INSUFFICIENT_DATA,
				response.getStatus());
		assertEquals(0, response.getCouponBasicDetailsList().size());
	}

	@Test
	public void searchCouponNullRequest() {
		SearchCouponResponse response = promotionAdminManager
				.searchCoupons(null);

		assertNotNull(response);
		assertEquals(SearchCouponStatusEnum.NO_SESSION, response.getStatus());
		assertEquals(0, response.getCouponBasicDetailsList().size());
	}

	@Test
	public void createCouponValid() {
		CreateCouponRequest request = new CreateCouponRequest();
		request.setSessionToken(responseUser.getSessionToken());
		request.setAlphabetCase(AlphabetCase.MIXED);
		request.setAlphaNumericType(AlphaNumericType.ALPHA_NUMERIC);
		request.setCount(10);
		request.setLength(10);
		request.setPromotionId(-3003);
		request.setType(CouponType.POST_ISSUE);
		request.setMaxAmount(new BigDecimal(10000));
		request.setMaxAmountPerUser(new BigDecimal(1000));
		request.setMaxUses(4);
		request.setMaxUsesPerUser(2);

		CreateCouponResponse response = promotionAdminManager
				.createCoupons(request);

		assertNotNull(response);
		assertEquals(CreateCouponStatusEnum.SUCCESS, response.getStatus());
		assertEquals(10, response.getNumberOfCouponsCreated());
	}

	public void setPromotionAdminManager(
			PromotionAdminManager promotionAdminManager) {
		this.promotionAdminManager = promotionAdminManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@Test
	public void testSearchScratchCard() {
		SearchScratchCardRequest searchScratchCardRequest = new SearchScratchCardRequest();

		searchScratchCardRequest.setScratchCardNumber("SAM2911BMJ");
		searchScratchCardRequest
				.setSessionToken(responseUser.getSessionToken());

		SearchScratchCardResponse searchScratchCardResponse = promotionAdminManager
				.searchScratchCard(searchScratchCardRequest);

		searchScratchCardResponse = promotionAdminManager
				.searchScratchCard(searchScratchCardRequest);
		assertEquals("active", searchScratchCardResponse.getCardStatus());

	}

	@Test
	public void testSearchScratchCardNullRequest() {
		SearchScratchCardRequest searchScratchCardRequest = new SearchScratchCardRequest();

		// searchScratchCardRequest.setScratchCardNumber(null);
		// searchScratchCardRequest.setSessionToken(responseUser.getSessionToken());

		SearchScratchCardResponse searchScratchCardResponse = promotionAdminManager
				.searchScratchCard(null);

		searchScratchCardResponse = promotionAdminManager
				.searchScratchCard(searchScratchCardRequest);
		assertEquals(SearchScratchCardStatusEnum.INSUFFICIENT_DATA,
				searchScratchCardResponse.getStatus());

	}

	@Test
	public void testSearchScratchCardInvalidRequest() {
		SearchScratchCardRequest searchScratchCardRequest = new SearchScratchCardRequest();

		searchScratchCardRequest.setScratchCardNumber("BB000UGDCDDD");
		searchScratchCardRequest
				.setSessionToken(responseUser.getSessionToken());

		
		SearchScratchCardResponse searchScratchCardResponse = promotionAdminManager
				.searchScratchCard(searchScratchCardRequest);

		searchScratchCardResponse = promotionAdminManager
				.searchScratchCard(searchScratchCardRequest);
		assertEquals(SearchScratchCardStatusEnum.NO_SCRATCH_CARD_FOUND,
				searchScratchCardResponse.getStatus());
		System.out.println("searchScratchCardResponse.getStatus()"+searchScratchCardResponse.getStatus());

	}

	@Test
	public void testSearchScratchCardInvalidUserRequest() {
		SearchScratchCardRequest searchScratchCardRequest = new SearchScratchCardRequest();

		searchScratchCardRequest.setScratchCardNumber("NG2911BMJ");
		searchScratchCardRequest.setSessionToken("test");

		SearchScratchCardResponse searchScratchCardResponse = promotionAdminManager
				.searchScratchCard(searchScratchCardRequest);

		searchScratchCardResponse = promotionAdminManager
				.searchScratchCard(searchScratchCardRequest);
		assertEquals(SearchScratchCardStatusEnum.NO_SESSION,
				searchScratchCardResponse.getStatus());

	}
	
	@Test
	public void testPromotionPerformanceInvalidPromotionId() {
		GetPromotionUsageRequest promotionPerformanceRequest = new GetPromotionUsageRequest();
		promotionPerformanceRequest.setpromotionId(0);
		promotionPerformanceRequest.setSessionToken(responseUser.getSessionToken());
		GetPromotionUsageResponse promotionPerformanceResponse = promotionAdminManager.getPromotionUsage(promotionPerformanceRequest);
		assertNotNull(promotionPerformanceResponse);
		assertEquals(GetPromotionUsageEnum.INVALID_PROMOTION, promotionPerformanceResponse.getStatus());
		
	}

	@Test

	public void createClearanceDiscountPromotion() {
		CreatePromotionRequest createPromotionRequest = new CreatePromotionRequest();
		PromotionTO promotionTO = new PromotionTO();

		promotionTO.setPromotionName("Test discount on clearance products promotion");
		promotionTO.setValidFrom(new DateTime(2012, 02, 29, 0, 0));
		promotionTO.setValidTill(new DateTime(2015, 02, 22, 0, 0));
		promotionTO.setDescription("Test discount on clearance products promotion description");
		promotionTO.setActive(true);
		promotionTO.setMaxUses(-1);
		promotionTO.setMaxUsesPerUser(-1);
		promotionTO.setMaxAmount(new Money(new BigDecimal(20000.00)));
		promotionTO.setMaxAmountPerUser(new Money(new BigDecimal(1000.00)));
		promotionTO.setRuleName("DISCOUNT_ON_CLEARANCE_PRODUCT");

		List<RuleConfigItemTO> ruleConfigList = new ArrayList<RuleConfigItemTO>();

		RuleConfigItemTO fixedDiscountOffConfig = new RuleConfigItemTO();
		fixedDiscountOffConfig.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		fixedDiscountOffConfig.setRuleConfigValue("500");
		ruleConfigList.add(fixedDiscountOffConfig);

		//MIN_ORDER_VALUE
		RuleConfigItemTO minOrderValueConfig = new RuleConfigItemTO();
		minOrderValueConfig.setRuleConfigName("MIN_ORDER_VALUE");
		minOrderValueConfig.setRuleConfigValue("2000");
		ruleConfigList.add(minOrderValueConfig);

		promotionTO.setConfigItems(ruleConfigList);

		createPromotionRequest.setPromotion(promotionTO);

		createPromotionRequest.setSessionToken(responseUser.getSessionToken());

		CreatePromotionResponse createPromotionResponse = promotionAdminManager.createPromotion(createPromotionRequest);
		assertEquals(CreatePromotionEnum.SUCCESS, createPromotionResponse.getCreatePromotionEnum());
		assertTrue(createPromotionResponse.getPromotionId() > 0);
		assertNotNull(createPromotionResponse.getSessionToken());

		//see if the promotion is created in the db
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		viewPromotionRequest.setSessionToken(responseUser.getSessionToken());
		viewPromotionRequest.setPromotionId(createPromotionResponse.getPromotionId());

		ViewPromotionResponse viewPromotionResponse = promotionAdminManager.viewPromotion(viewPromotionRequest);

		assertNotNull(viewPromotionResponse);
		assertNotNull(viewPromotionResponse.getPromotionCompleteView());

		PromotionTO promotion = viewPromotionResponse.getPromotionCompleteView();
		assertEquals("Test discount on clearance products promotion", promotion.getPromotionName());

		//create a coupon
		CreateCouponRequest createCouponRequest = new CreateCouponRequest();
		createCouponRequest.setSessionToken(responseUser.getSessionToken());
		createCouponRequest.setPromotionId(promotion.getId());
		createCouponRequest.setCount(1);
		createCouponRequest.setLength(9);
		createCouponRequest.setAlphabetCase(AlphabetCase.UPPER);
		createCouponRequest.setAlphaNumericType(AlphaNumericType.ALPHABETS);
		createCouponRequest.setStartsWith("CLEARANCE");
		createCouponRequest.setType(CouponType.GLOBAL);
		createCouponRequest.setMaxAmount(new BigDecimal(10000));
		createCouponRequest.setMaxAmountPerUser(new BigDecimal(1000));
		createCouponRequest.setMaxUses(-1);
		createCouponRequest.setMaxUsesPerUser(-1);

		CreateCouponResponse createCouponResponse = promotionAdminManager.createCoupons(createCouponRequest);
		assertNotNull(createCouponResponse);
		assertEquals(CreateCouponStatusEnum.SUCCESS, createCouponResponse.getStatus());
	}

	public void testPromotionPerformanceInvalidRequest() {
		GetPromotionUsageRequest promotionPerformanceRequest = new GetPromotionUsageRequest();
		promotionPerformanceRequest.setSessionToken("test arbit");
		promotionPerformanceRequest.setpromotionId(-5002);
		
		GetPromotionUsageResponse promotionPerformanceResponse = promotionAdminManager.getPromotionUsage(promotionPerformanceRequest);
		assertNotNull(promotionPerformanceResponse);
		assertEquals(GetPromotionUsageEnum.NO_SESSION, promotionPerformanceResponse.getStatus());
	}
	
	@Test
	public void testPromotionPerformance() {
		GetPromotionUsageRequest promotionPerformanceRequest = new GetPromotionUsageRequest();
		promotionPerformanceRequest.setSessionToken(responseUser.getSessionToken());
		promotionPerformanceRequest.setpromotionId(-5002);
		
		GetPromotionUsageResponse promotionPerformanceResponse = promotionAdminManager.getPromotionUsage(promotionPerformanceRequest);
		assertNotNull(promotionPerformanceResponse);
		// TODO change these..
		int orders = 2;
		assertEquals(orders, promotionPerformanceResponse.gettotalOrders());
		assertEquals(new BigDecimal("20.00"), promotionPerformanceResponse.getdiscount().getAmount());

	}
	
	@Test
	public void testPromotionPerformanceNullSession() {
		GetPromotionUsageResponse promotionPerformanceResponse = promotionAdminManager.getPromotionUsage(null);
		assertNotNull(promotionPerformanceResponse);
		assertEquals(GetPromotionUsageEnum.NO_SESSION, promotionPerformanceResponse.getStatus());
	}
}
