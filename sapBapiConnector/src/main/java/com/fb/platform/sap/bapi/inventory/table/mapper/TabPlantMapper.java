package com.fb.platform.sap.bapi.inventory.table.mapper;

import org.apache.commons.lang.StringUtils;

import com.fb.platform.sap.bapi.commons.SapInventoryConstants;
import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class TabPlantMapper {

	public static void setDetails(JCoFunction bapiFunction, SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		if (StringUtils.isNotBlank(inventoryDashboardRequestTO.getPlant())) {
			JCoTable tabPlant = bapiFunction.getTableParameterList().getTable(BapiInventoryTable.TAB_PLANT.toString());	
			tabPlant.appendRow();
			tabPlant.setValue(SapInventoryConstants.PLANT, inventoryDashboardRequestTO.getPlant());
		}
	}
	
}
