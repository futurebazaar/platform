package com.fb.platform.sap.bapi.inventory.table.mapper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.client.commons.SapInventoryConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class TabArticleMapper {
	
	private static Log logger = LogFactory.getLog(TabArticleMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		if (StringUtils.isNotBlank(inventoryDashboardRequestTO.getArticle())) {
			logger.info("Setting Inventory Dashboard Article details for :" + inventoryDashboardRequestTO);
			JCoTable tabArticle = bapiFunction.getTableParameterList().getTable(BapiInventoryTable.TAB_ARTICLE.toString());
			tabArticle.appendRow();
			tabArticle.setValue(SapInventoryConstants.ARTICLE, inventoryDashboardRequestTO.getArticle());
		}
	}

}
