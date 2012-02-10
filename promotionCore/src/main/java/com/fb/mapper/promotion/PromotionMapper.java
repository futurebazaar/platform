package com.fb.mapper.promotion;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fb.bo.promotion.PromotionBO;

/**
 * 
 * @author Keith Fernandez
 * 
 * Yet to May Values, ProductBundle and some more attributes
 */
public class PromotionMapper implements RowMapper {

    /**
     * Implementation of the RowMapper interface for fb_promotion and domain model PromotionBo
     * @param resultSet
     * @param rowNum
     * @return fbPromotion
     * @throws SQLException 
     */
    @Override
    public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        
        PromotionBO fbPromotion = new PromotionBO();
       
        fbPromotion.setPromotionId(resultSet.getInt("id"));
        fbPromotion.setAppliesOn(resultSet.getString("applies_on"));
        /*fbPromotion.setDiscountType(resultSet.getString("discount_type"));
        fbPromotion.setDiscountValue(resultSet.getInt("discount_value"));
        fbPromotion.setMinAmountValue(resultSet.getDouble("min_order_value"));
        fbPromotion.setMaxUsesPerUser(resultSet.getInt("max_uses_per_user"));
        fbPromotion.setMaxUses(resultSet.getInt("max_uses"));
        fbPromotion.setTotalUses(resultSet.getInt("total_uses"));
        fbPromotion.setCanBeClaimed(resultSet.getString("can_be_claimed_by"));*/
        /*fbPromotion.setPromotionName(resultSet.getString("promotion_name"));
        fbPromotion.setStartDate(resultSet.getTimestamp("start_date"));
        fbPromotion.setEndDate(resultSet.getTimestamp("end_date"));
        fbPromotion.setCreatedOn(resultSet.getTimestamp("created_on"));
        fbPromotion.setLastModifedOn(resultSet.getTimestamp("last_modified_on"));        
        fbPromotion.setCreatedBy(resultSet.getString("created_by"));
        fbPromotion.setCeil(resultSet.getDouble("celin"));
        
        fbPromotion.setBundleId(resultSet.getInt("bundle_id"));
        fbPromotion.setDiscountBundleId(resultSet.getInt("discount_bundle_id"));
        
        fbPromotion.setPromotionType(resultSet.getString("promotion_type"));*/
        
		fbPromotion.setCreatedOn(resultSet.getTimestamp("created_on"));
		fbPromotion.setCreatedBy(resultSet.getString("created_by"));
		fbPromotion.setValidFrom(resultSet.getTimestamp("valid_from"));
		fbPromotion.setValidTill(resultSet.getTimestamp("valid_till"));
		fbPromotion.setLastModifiedOn(resultSet.getTimestamp("last_modified_on"));
		fbPromotion.setPromotionName(resultSet.getString("promotion_name"));
		fbPromotion.setDisplayText(resultSet.getString("display_text"));
		fbPromotion.setPromotionDescription(resultSet.getString("promotion_description"));
		fbPromotion.setLastUsedOn(resultSet.getTimestamp("last_used_on"));
		//fbPromotion.setPromotionType(resultSet.get(""));
		//fbPromotion.setPromotionUses(resultSet.get(""));
		fbPromotion.setRuleId(resultSet.getInt("rule_id"));
		fbPromotion.setCoupon(resultSet.getInt("is_coupon")==1);
		//fbPromotion.setAmountType(resultSet.getInt(""));
		fbPromotion.setActive(resultSet.getInt("is_active")==1);
		
		
//		"promotion_type," +
//		"promotion_uses," +
//		"amount_type," +
//		"priority" +

		
		//values
		//priority
        
        return fbPromotion;
    }

}
