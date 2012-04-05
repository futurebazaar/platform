/**
 * 
 */
package com.fb.platform.ifs.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.ifs.domain.LspDcBo;

/**
 * @author sarvesh
 *
 */
public class LspDcMapper implements RowMapper<LspDcBo> {
	
	ResourceBundle ifsResBndl = ResourceBundle.getBundle("ifsResources");

	@Override
	public LspDcBo mapRow(ResultSet rs, int rowNum) throws SQLException {
		LspDcBo lspDcBo = new LspDcBo();
		lspDcBo.setDcId(rs.getString("dc_id"));
		lspDcBo.setLspId(rs.getString("lsp_id"));
		lspDcBo.setZipGroupId(rs.getString("zipgroup_id"));
		lspDcBo.setDcCode(rs.getString("dc_code"));
		lspDcBo.setOrderType(rs.getString("type"));
		ResultSetMetaData meta = rs.getMetaData();
		int numCol = meta.getColumnCount();
		boolean exist = false;
		for (int i = 1; i <= numCol; i++) {
			if(meta.getColumnName(i).equalsIgnoreCase("transit_time")) {
		         exist = true;
		         break;
		     }

		}
		if(exist)
		{
			lspDcBo.setDeliveryTime(rs.getInt("transit_time"));
			lspDcBo.setTotalDeliveryTime(rs.getInt("transit_time"));
		}
		else {
			String localDeliveryTimeStr = ifsResBndl.getString("localDeliveryTime");
			int localDeliveryTime = 0;
			if(!StringUtils.isEmpty(localDeliveryTimeStr))
			{
				localDeliveryTime = Integer.parseInt(localDeliveryTimeStr);
			}
				
			lspDcBo.setDeliveryTime(localDeliveryTime);
			lspDcBo.setTotalDeliveryTime(localDeliveryTime);
		}
		return lspDcBo;
	}

}
