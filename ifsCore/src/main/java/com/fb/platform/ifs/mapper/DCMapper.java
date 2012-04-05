/**
 * 
 */
package com.fb.platform.ifs.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.ifs.domain.DCBo;


/**
 * @author sarvesh
 *
 */
public class DCMapper implements RowMapper<DCBo> {

	ResourceBundle ifsResBndl = ResourceBundle.getBundle("ifsResources");
	
    /**
     * Implementation of the RowMapper interface for fb_promotion and domain model serviceabilityRequestBo
     * @param resultSet
     * @param rowNum
     * @return serviceabilityRequestBo
     * @throws SQLException 
     */
    @Override
    public DCBo mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        DCBo dcBo = new DCBo();
        dcBo.setInvId(resultSet.getString("inv_id"));
        dcBo.setId(resultSet.getString("id"));
        dcBo.setDcId(resultSet.getString("code"));
        dcBo.setStockLevel(resultSet.getInt("stock_level"));
        String deltaForBackorder = ifsResBndl.getString("deltaForBackorder");
        int delta = (resultSet.getInt("delta") > 0) ? resultSet.getInt("delta") : 0;
        dcBo.setDeltaDeliveryTime(delta);
        boolean isBackorderable = false;
        boolean isMadeToOrder = false;
        boolean isPreOrder= false;
        String inventoryType = resultSet.getString("type");
        System.out.println("inventoryType "+inventoryType);
        if (!StringUtils.isEmpty(inventoryType)) {
        	if(inventoryType.equalsIgnoreCase("backorder"))
        	{
        		isBackorderable = true;
        		dcBo.setDeltaDeliveryTime(Integer.parseInt(deltaForBackorder));
        	} 
        	else if (inventoryType.equalsIgnoreCase("madetoorder")) {
				isMadeToOrder = true;
			} 
        	else if (inventoryType.equalsIgnoreCase("preorder")) {
				isPreOrder = true;
			}
		}
        dcBo.setBackOrderable(isBackorderable);
        dcBo.setMadeToOrder(isMadeToOrder);
        dcBo.setPreOrder(isPreOrder);
        dcBo.setExpectedInDays(resultSet.getInt("expected_in"));
        dcBo.setExpectedOnDate(resultSet.getDate("expected_on"));
        dcBo.setType(inventoryType);
        dcBo.setCod(resultSet.getBoolean("cod_flag"));
        boolean localDC = (StringUtils.isEmpty(resultSet.getString("local_dc"))) ? false : true; 
        dcBo.setLocalDC(localDC);
        
        return dcBo;
    }

}
