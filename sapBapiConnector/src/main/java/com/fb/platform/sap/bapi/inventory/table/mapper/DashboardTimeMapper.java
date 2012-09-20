package com.fb.platform.sap.bapi.inventory.table.mapper;

import com.fb.platform.sap.bapi.commons.SapConstants;
import com.fb.platform.sap.bapi.commons.SapUtils;
import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.sap.conn.jco.JCoFunction;

public class DashboardTimeMapper {

	public static void setDetails(JCoFunction bapiFunction, SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		bapiFunction.getImportParameterList().setValue(BapiInventoryTable.FROM_DATE.toString(), SapUtils.convertDateToFormat(inventoryDashboardRequestTO.getFromDateTime(), SapConstants.DATE_FORMAT));	
		bapiFunction.getImportParameterList().setValue(BapiInventoryTable.FROM_TIME.toString(), SapUtils.convertDateToFormat(inventoryDashboardRequestTO.getFromDateTime(), SapConstants.TIME_FORMAT));
		bapiFunction.getImportParameterList().setValue(BapiInventoryTable.TO_DATE.toString(), SapUtils.convertDateToFormat(inventoryDashboardRequestTO.getToDateTime(), SapConstants.DATE_FORMAT));
		bapiFunction.getImportParameterList().setValue(BapiInventoryTable.TO_TIME.toString(), SapUtils.convertDateToFormat(inventoryDashboardRequestTO.getToDateTime(), SapConstants.TIME_FORMAT));
	}
	
}
