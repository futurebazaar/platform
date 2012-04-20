/**
 * 
 */
package com.fb.platform.promotion.service.migration.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.admin.PromotionAdminDao;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.PromotionDates;
import com.fb.platform.promotion.model.PromotionLimitsConfig;
import com.fb.platform.promotion.model.UserPromotionUsesEntry;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.model.coupon.UserCouponUsesEntry;
import com.fb.platform.promotion.model.legacy.LegacyCouponOrder;
import com.fb.platform.promotion.model.legacy.LegacyCouponUser;
import com.fb.platform.promotion.model.legacy.LegacyPromotion;
import com.fb.platform.promotion.model.legacy.LegacyPromotionCoupon;
import com.fb.platform.promotion.rule.RuleConfigConstants;
import com.fb.platform.promotion.rule.RuleConfigItem;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.service.migration.MigrationService;

/**
 * @author vinayak
 *
 */
public class MigrationServiceImpl implements MigrationService {

	private Log infoLog = LogFactory.getLog("LOGINFO");
	
	private Log errorLog = LogFactory.getLog("LOGERROR");


	@Autowired
	private PromotionAdminDao promotionAdminDao = null;

	@Override
	public void migrate(LegacyPromotion legacyPromotion) {
		infoLog.info("Migration Service : Start migrating legacyPromotion : " + legacyPromotion.getPromotionId());

		if (legacyPromotion.getEndDate() != null) {
			DateTime validTill = new DateTime(legacyPromotion.getEndDate());
			DateTimeComparator dateComparator = DateTimeComparator.getDateOnlyInstance();
			if (dateComparator.compare(validTill, null) < 0) {
				infoLog.info("EXPIRED PROMOTION : " + legacyPromotion.getPromotionId());
				return;
			}
		}
		Promotion promotion = createPromotion(legacyPromotion);

		Set<Integer> clientList = new HashSet<Integer>();

		List<Coupon> coupons = createCoupons(legacyPromotion, clientList);

		infoLog.info("Created new coupons for legacyPromotion : " + legacyPromotion.getPromotionId() + ", No of new coupons : " + coupons.size());

		RuleConfiguration ruleConfig = createRule(legacyPromotion, clientList);
		promotion.setRuleId(ruleConfig.getRuleId());

		promotionAdminDao.createPromotion(promotion, ruleConfig, coupons);

		infoLog.info("Migration Service : Done migrating legacyPromotion : " + legacyPromotion.getPromotionId() + ". New promotion created : " + promotion.getId());
	}

	private Promotion createPromotion(LegacyPromotion legacyPromotion) {
		Promotion promotion = new Promotion();
		promotion.setActive(legacyPromotion.isActive());
		promotion.setDescription(legacyPromotion.getPromotionName());
		promotion.setName(legacyPromotion.getPromotionName());

		promotion.setDates(createPromotionDates(legacyPromotion));
		promotion.setLimitsConfig(createLimitsConfig(legacyPromotion));

		return promotion;
	}

	private PromotionDates createPromotionDates(LegacyPromotion legacyPromotion) {
		PromotionDates dates = new PromotionDates();
		dates.setCreatedOn(new DateTime(legacyPromotion.getCreatedOn()));
		dates.setValidFrom(new DateTime(legacyPromotion.getStartDate()));
		dates.setValidTill(new DateTime(legacyPromotion.getEndDate()));
		dates.setLastModifiedOn(new DateTime(legacyPromotion.getLastModifedOn()));
		return dates;
	}

	private PromotionLimitsConfig createLimitsConfig(LegacyPromotion legacyPromotion) {
		PromotionLimitsConfig limits = new PromotionLimitsConfig();
		int maxUses;
		if(legacyPromotion.getMaxUses() == 0) {
			maxUses = -1;
		} else {
			maxUses = legacyPromotion.getMaxUses();
		}
		limits.setMaxUses(maxUses);
		int maxUsesPerUser;
		if(legacyPromotion.getMaxUsesPerUser() == 0) {
			maxUsesPerUser = -1;
		} else {
			maxUsesPerUser = legacyPromotion.getMaxUsesPerUser();
		}
		limits.setMaxUsesPerUser(maxUsesPerUser);
		limits.setMaxAmount(new Money(new BigDecimal("-1.00")));
		limits.setMaxAmountPerUser(new Money(new BigDecimal("-1.00")));
		
		return limits;
	}

	private RuleConfiguration createRule(LegacyPromotion legacyPromotion, Set<Integer> clientList) {
		String clientsStr = createCommaSeparatedClients(clientList);
		RuleConfiguration ruleConfig = new RuleConfiguration();

		String discountType = legacyPromotion.getDiscountType();
		if (discountType.equals("Amount")) {
			//this is fixed rs off rule
			ruleConfig.setRuleId(2);
			RuleConfigItem minOrderValue = new RuleConfigItem(RuleConfigConstants.MIN_ORDER_VALUE, doubleToString(legacyPromotion.getMinAmountValue()));
			RuleConfigItem fixedDiscountOff = new RuleConfigItem(RuleConfigConstants.FIXED_DISCOUNT_RS_OFF, legacyPromotion.getDiscountValue());
			RuleConfigItem clientListConfig = new RuleConfigItem(RuleConfigConstants.CLIENT_LIST, clientsStr);

			ruleConfig.add(minOrderValue);
			ruleConfig.add(fixedDiscountOff);
			ruleConfig.add(clientListConfig);
		} else if (discountType.equals("Percent")) {
			//this is percent off rule
			ruleConfig.setRuleId(3);
			RuleConfigItem minOrderValue = new RuleConfigItem(RuleConfigConstants.MIN_ORDER_VALUE, doubleToString(legacyPromotion.getMinAmountValue()));
			RuleConfigItem fixedDiscountOff = new RuleConfigItem(RuleConfigConstants.DISCOUNT_PERCENTAGE, legacyPromotion.getDiscountValue());
			RuleConfigItem maxDiscount = new RuleConfigItem(RuleConfigConstants.MAX_DISCOUNT_CEIL_IN_VALUE, "10000.00");
			RuleConfigItem clientListConfig = new RuleConfigItem(RuleConfigConstants.CLIENT_LIST, clientsStr);

			ruleConfig.add(minOrderValue);
			ruleConfig.add(fixedDiscountOff);
			ruleConfig.add(maxDiscount);
			ruleConfig.add(clientListConfig);
		} else {
			errorLog.error("Discount Type not found. Nothing to do. Promotion ID : " + legacyPromotion.getPromotionId());
		}
		return ruleConfig;
	}

	private String createCommaSeparatedClients(Set<Integer> clientList) {
		StringBuffer sb = new StringBuffer();
		int count = 0;
		for (Integer clientId : clientList) {
			sb.append(clientId);
			count ++;
			if (count != clientList.size()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	private String doubleToString(double inValue){
		DecimalFormat twoDecimal = new DecimalFormat("0.00");
		twoDecimal.setGroupingUsed(false);
		return twoDecimal.format(inValue);
	}

	public void setPromotionAdminDao(PromotionAdminDao promotionAdminDao) {
		this.promotionAdminDao = promotionAdminDao;
	}

	private List<Coupon> createCoupons(LegacyPromotion legacyPromotion, Set<Integer> clientList) {
		List<LegacyPromotionCoupon> legacyCoupons = legacyPromotion.getCoupons();
		List<Coupon> coupons = new ArrayList<Coupon>();

		for (LegacyPromotionCoupon legacyCoupon : legacyCoupons) {
			Coupon coupon = new Coupon();
			coupon.setCode(legacyCoupon.getCouponCode());
			
			if (legacyPromotion.getGlobal() == 0) {
				coupon.setType(CouponType.PRE_ISSUE);
				coupon.setCouponUses(createCouponUses(legacyCoupon));
				coupon.setPromotionUses(createPromotionUses(legacyCoupon));
			} else {
				coupon.setType(CouponType.POST_ISSUE);
			}
			List<Integer> couponUsers = createCouponUsers(legacyCoupon);
			coupon.setUsers(couponUsers);

			CouponLimitsConfig couponLimits = createCouponLimits(legacyPromotion);
			coupon.setLimitsConfig(couponLimits);

			coupons.add(coupon);

			if (legacyCoupon.getClientId() != 0) {
				clientList.add(legacyCoupon.getClientId());
			}
		}

		return coupons;
	}

	private List<UserCouponUsesEntry> createCouponUses(LegacyPromotionCoupon legacyCoupon) {

		List<UserCouponUsesEntry> couponUses = new ArrayList<UserCouponUsesEntry>();

		for (LegacyCouponOrder legacyCouponOrder : legacyCoupon.getCouponOrders()) {
			UserCouponUsesEntry couponUse = new UserCouponUsesEntry();
			if (legacyCouponOrder.getConfirmingTimeStamp() != null) {
				couponUse.setCreatedDate(new DateTime(legacyCouponOrder.getConfirmingTimeStamp()));
			}
			couponUse.setDiscountAmount(new Money(legacyCouponOrder.getCouponDiscount()));
			couponUse.setOrderId(legacyCouponOrder.getOrderId());
			couponUse.setUserId(legacyCouponOrder.getUserId());

			couponUses.add(couponUse);
		}
		return couponUses;
	}

	private List<UserPromotionUsesEntry> createPromotionUses(LegacyPromotionCoupon legacyCoupon) {

		List<UserPromotionUsesEntry> promotionUses = new ArrayList<UserPromotionUsesEntry>();

		for (LegacyCouponOrder legacyCouponOrder : legacyCoupon.getCouponOrders()) {
			UserPromotionUsesEntry promotionUse = new UserPromotionUsesEntry();
			if (legacyCouponOrder.getConfirmingTimeStamp() != null) {
				promotionUse.setCreatedDate(new DateTime(legacyCouponOrder.getConfirmingTimeStamp()));
			}
			promotionUse.setDiscountAmount(new Money(legacyCouponOrder.getCouponDiscount()));
			promotionUse.setOrderId(legacyCouponOrder.getOrderId());
			promotionUse.setUserId(legacyCouponOrder.getUserId());

			promotionUses.add(promotionUse);
		}
		return promotionUses;
	}

	private CouponLimitsConfig createCouponLimits(LegacyPromotion legacyPromotion) {
		CouponLimitsConfig limits = new CouponLimitsConfig();
		
		int maxUses;
		if(legacyPromotion.getMaxUsesPerCoupon() == 0) {
			maxUses = -1;
		} else {
			maxUses = legacyPromotion.getMaxUsesPerCoupon();
		}
		limits.setMaxUses(maxUses);
		limits.setMaxUsesPerUser(-1);
		limits.setMaxAmount(new Money(new BigDecimal("-1.00")));
		limits.setMaxAmountPerUser(new Money(new BigDecimal("-1.00")));
		
		return limits;
	}

	private List<Integer> createCouponUsers(LegacyPromotionCoupon legacyCoupon) {
		List<Integer> users = new ArrayList<Integer>();
		List<LegacyCouponUser> legacyCouponUsers = legacyCoupon.getCouponUsers();
		for (LegacyCouponUser legacyUser : legacyCouponUsers) {
			users.add(legacyUser.getUserId());
		}
		return users;
	}
}
