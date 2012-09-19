package com.fb.platform.sap.bapi.inventory.table.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.client.commons.SapConstants;
import com.fb.platform.sap.client.commons.SapUtils;
import com.sap.conn.jco.JCoFunction;

public class DashboardTimeMapper {
	
	private static Log logger = LogFactory.getLog(DashboardTimeMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		logger.info("Setting Inventory Dashboard Time details for :" + inventoryDashboardRequestTO);
		bapiFunction.getImportParameterList().setValue(BapiInventoryTable.FROM_DATE.toString(), SapUtils.convertDateToFormat(inventoryDashboardRequestTO.getFromDateTime(), SapConstants.DATE_FORMAT));	
		bapiFunction.getImportParameterList().setValue(BapiInventoryTable.FROM_TIME.toString(), SapUtils.convertDateToFormat(inventoryDashboardRequestTO.getFromDateTime(), SapConstants.TIME_FORMAT));
		bapiFunction.getImportParameterList().setValue(BapiInventoryTable.TO_DATE.toString(), SapUtils.convertDateToFormat(inventoryDashboardRequestTO.getToDateTime(), SapConstants.DATE_FORMAT));
		bapiFunction.getImportParameterList().setValue(BapiInventoryTable.TO_TIME.toString(), SapUtils.convertDateToFormat(inventoryDashboardRequestTO.getToDateTime(), SapConstants.TIME_FORMAT));
	}
	
}
