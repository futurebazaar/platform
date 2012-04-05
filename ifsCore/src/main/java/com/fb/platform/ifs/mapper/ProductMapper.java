package com.fb.platform.ifs.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.ifs.domain.ProductBo;

/**
 * @author sarvesh
 *
 */
public class ProductMapper implements RowMapper<ProductBo> {
	/**
     * Implementation of the RowMapper interface for fb_promotion and domain model serviceabilityRequestBo
     * @param resultSet
     * @param rowNum
     * @return productBo
     * @throws SQLException 
     */
    public ProductBo mapRow(ResultSet resultSet, int rowNum) throws SQLException {

    	ProductBo productBo = new ProductBo();
    	productBo.setProductGroup(resultSet.getString("name"));
    	productBo.setArticleId(resultSet.getString("article_id"));
    	productBo.setShipLocalOnly(resultSet.getBoolean("local_tag"));
    	productBo.setHighValueFlag(resultSet.getBoolean("high_value_flag"));
    	productBo.setShippingMode(resultSet.getString("ship_mode"));
    	productBo.setThresholdAmt(resultSet.getInt("threshold_amount"));
    	
		return productBo;
    
    }

	
}
