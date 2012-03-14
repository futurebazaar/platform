/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

//import com.fb.bo.promotion.PromotionBundle;
import com.fb.platform.promotion.dao.PromotionBundleDao;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.model.Priority;
import com.fb.platform.promotion.model.PromotionLimit;
import com.fb.platform.promotion.model.PromotionType;
import com.fb.platform.promotion.model.PromotionValue;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.Rule;

/**
 * @author Keith Fernandez
 *
 */
public class PromotionDaoJdbcImpl implements PromotionDao {

	private JdbcTemplate jdbcTemplate;

	private PromotionBundleDao promotionBundleDao;

	private static final String GET_PROMOTION_QUERY = "SELECT " +
			"id," +
			"applies_on," +
			"created_on," +
			"created_by," +
			"valid_from," +
			"valid_till," +
			"last_modified_on," +
			"promotion_name," +
			"display_text," +
			"promotion_description," +
			"last_used_on," +
			"promotion_type," +
			"promotion_uses," +
			"rule_id," +
			"is_coupon," +
			"amount_type," +
			"is_active," +
			"priority" +
			" FROM promotion WHERE id = ?";

	private static final String DELETE_PROMOTION_QUERY = "DELETE FROM promotion  WHERE promotion_id = ?";

	private static final String UPDATE_PROMOTION_QUERY = "UPDATE promotion SET " +
			"applies_on=?," +
			"created_on=?," +
			"created_by=?," +
			"valid_from=?," +
			"valid_till=?," +
			"last_modified_on=?," +
			"promotion_name=?," +
			"display_text=?," +
			"promotion_description=?," +
			"last_used_on=?," +
			"promotion_type=?," +
			"promotion_uses=?," +
			"rule_id=?," +
			"is_coupon=?," +
			"amount_type=?," +
			"is_active=?," +
			"priority=?," +
			" WHERE id = ?";

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#update(com.fb.bo.promotion.PromotionBo)
	 */
	@Override
	public void update(Promotion promotion) {
//		this.jdbcTemplate.update(UPDATE_PROMOTION_QUERY,
//				promotion.getAppliesOn(),
//				promotion.getCreatedOn(),
//				promotion.getCreatedBy(),
//				promotion.getValidFrom(),
//				promotion.getValidTill(),
//				promotion.getLastModifiedOn(),
//				promotion.getPromotionName(),
//				promotion.getDisplayText(),
//				promotion.getPromotionDescription(),
//				promotion.getLastUsedOn(),
//				promotion.getPromotionType(),
//				promotion.getPromotionUses(),
//				promotion.getRule(),
//				promotion.isCoupon(),
//				promotion.getAmountType(),
//				promotion.isActive(),
//				promotion.getPriority()
//				);

	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#get(java.lang.Integer)
	 */
	@Override
	public Promotion loadPromotionById(Integer promotionId) {
		return null;
	}
	
	@Override
	public Promotion loadPromotionByCouponCode(String couponCode) {
		return null;
	}
	

	@Override
	public List<Promotion> getAllGlobalCoupons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Promotion> getAllCouponsOnCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private static class RuleMapper implements RowMapper<Rule> {

    	@Override
    	public Rule mapRow(ResultSet rs, int rowNum) throws SQLException {

    		Rule rule = new Rule();
    		rule.setRuleId(rs.getInt("id"));
			rule.setRuleDescription(rs.getString("rule_desc"));
			rule.setRuleFunctionName(rs.getString("rule_function"));
			rule.setRulePriority(Priority.get(rs.getInt("priority")));
    		
			return rule;
    	}
    }
	
	private static class PromotionValueMapper implements RowMapper<PromotionValue> {

    	@Override
    	public PromotionValue mapRow(ResultSet rs, int rowNum) throws SQLException {

    		PromotionValue promoValue = new PromotionValue();
    		promoValue.setId(rs.getInt("id"));
    		promoValue.setPromoId(rs.getInt(rs.getInt("promo_id")));
    		promoValue.setValueData(rs.getString("value_data"));
    		promoValue.setValueDesc(rs.getString("value_desc"));
    		promoValue.setValueName(rs.getString("value_name"));
    		promoValue.setValueType(PromotionType.get(rs.getInt("value_type")));
    		
			return promoValue;
    	}
    }
	

	private static class PromotionLimitMapper implements RowMapper<PromotionLimit> {

    	@Override
    	public PromotionLimit mapRow(ResultSet rs, int rowNum) throws SQLException {

    		PromotionLimit promoLimit = new PromotionLimit();
    		promoLimit.setId(rs.getInt("id"));
    		promoLimit.setPromoId(rs.getInt(rs.getInt("promo_id")));
    		promoLimit.setMaxBudget(rs.getLong("max_budget"));
    		promoLimit.setValueClaimedTillNow(rs.getLong("value_claimed_till_now"));
    		promoLimit.setMaxPerUser(rs.getInt("max_per_user"));
    		promoLimit.setMaxTotalUsagesAllowed(rs.getInt("max_total_usages_allowed"));
    		promoLimit.setNoOfUsesTillNow(rs.getInt("no_of_uses_till_now"));
    		promoLimit.setPaymentModeToBeUsed(rs.getInt("payment_mode_to_be_used"));
    		promoLimit.setViaChannel(rs.getInt("via_channel"));
    		
			return promoLimit;
    	}
    }

	

}
