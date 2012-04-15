package platform.fb.com.migration.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.tree.RowMapper;

/**
 * 
 * @author Keith
 */

public class PromotionMapper {


	
    public  static OldPromo mapRow(ResultSet resultSet)  {
        
        OldPromo fbPromotion = new OldPromo();
       try{
        fbPromotion.setPromotionId(resultSet.getInt("promotion_id"));
        fbPromotion.setAppliesOn(resultSet.getString("applied_on"));
        fbPromotion.setDiscountType(resultSet.getString("discount_type"));
        fbPromotion.setDiscountValue(resultSet.getInt("discount_value"));
        fbPromotion.setMinAmountValue(resultSet.getDouble("min_order_value"));
        fbPromotion.setMaxUsesPerUser(resultSet.getInt("max_uses_per_user"));
        fbPromotion.setMaxUsesPerCoupon(resultSet.getInt("max_uses_per_coupon"));
        fbPromotion.setMaxUses(resultSet.getInt("max_uses"));
        fbPromotion.setTotalUses(resultSet.getInt("total_uses"));
        fbPromotion.setPromotionName(resultSet.getString("name_of_promotion"));
        fbPromotion.setStartDate(resultSet.getTimestamp("start_date"));
        fbPromotion.setEndDate(resultSet.getTimestamp("end_date"));
        fbPromotion.setCreatedOn(resultSet.getTimestamp("created_on"));
        fbPromotion.setLastModifedOn(resultSet.getTimestamp("last_modified_on"));        
        fbPromotion.setPromotionType(resultSet.getString("promotion_type"));
        fbPromotion.setActive(resultSet.getInt("active"));
        fbPromotion.setGlobal(resultSet.getInt("global"));
        return fbPromotion;
       }
       catch (Exception e) {
		// TODO: handle exception
	}
       return null;
        
    }

}
