package com.fb.platform.sap.bapi.inventory.table.mapper;

import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.utils.SapInventoryConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class TabPlantMapper {

	public static void setDetails(JCoFunction bapiFunction, SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		JCoTable tabPlant = bapiFunction.getTableParameterList().getTable(BapiInventoryTable.TAB_PLANT.toString());	
		tabPlant.appendRow();
		tabPlant.setValue(SapInventoryConstants.PLANT, inventoryDashboardRequestTO.getPlant());
	}
	
}
