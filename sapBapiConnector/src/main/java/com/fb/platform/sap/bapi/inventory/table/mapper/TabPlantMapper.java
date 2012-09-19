package com.fb.platform.sap.bapi.inventory.table.mapper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.client.commons.SapInventoryConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class TabPlantMapper {
	
	private static Log logger = LogFactory.getLog(TabPlantMapper.class);

	public static void setDetails(JCoFunction bapiFunction, SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		if (StringUtils.isNotBlank(inventoryDashboardRequestTO.getPlant())) {
			logger.info("Setting Inventory  Dashboard Plant details for :" + inventoryDashboardRequestTO);
			JCoTable tabPlant = bapiFunction.getTableParameterList().getTable(BapiInventoryTable.TAB_PLANT.toString());	
			tabPlant.appendRow();
			tabPlant.setValue(SapInventoryConstants.PLANT, inventoryDashboardRequestTO.getPlant());
		}
	}
	
}
