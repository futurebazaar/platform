package com.fb.mapper.promotion;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fb.bo.promotion.PromotionBundleProduct;

/* Yet to write the mapper for Bundle*/

public class BundleProductMapper implements RowMapper{

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					PromotionBundleProduct bp = new PromotionBundleProduct();
					/*bp.setId(rs.getInt("id"));
					bp.setDiscountPrice(rs.getDouble("discounted_price"));
					bp.setDiscountType(rs.getString("discount_type"));
					bp.setDiscount(rs.getDouble("discount_value"));
					bp.setProductId(rs.getInt("product_id"));*/
		    		return bp;
   		}

}
