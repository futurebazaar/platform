/**
 * 
 */
package com.fb.platform.promotion.service.migration.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.PromotionDates;
import com.fb.platform.promotion.model.PromotionLimitsConfig;
import com.fb.platform.promotion.model.legacy.LegacyPromotion;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.service.migration.MigrationService;

/**
 * @author vinayak
 *
 */
public class MigrationServiceImpl implements MigrationService {

	private Log log = LogFactory.getLog(MigrationServiceImpl.class);

	@Override
	public void migrate(LegacyPromotion legacyPromotion) {
		log.info("Migration Service : Start migrating legacyPromotion : " + legacyPromotion.getPromotionId());
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
		limits.setMaxUses(legacyPromotion.getMaxUses());
		limits.setMaxUsesPerUser(legacyPromotion.getMaxUsesPerUser());
		return limits;
	}

	private PromotionRule createRule(LegacyPromotion legacyPromotion) {
		return null;
	}
}

