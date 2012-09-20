package com.fb.platform.sap.bapi.inventory.table.mapper;

import org.apache.commons.lang.StringUtils;

import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.utils.SapInventoryConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class TabArticleMapper {
	
	public static void setDetails(JCoFunction bapiFunction, SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		if (StringUtils.isNotBlank(inventoryDashboardRequestTO.getArticle())) {
			JCoTable tabArticle = bapiFunction.getTableParameterList().getTable(BapiInventoryTable.TAB_ARTICLE.toString());
			tabArticle.appendRow();
			tabArticle.setValue(SapInventoryConstants.ARTICLE, inventoryDashboardRequestTO.getArticle());
		}
	}

}
