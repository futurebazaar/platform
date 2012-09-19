package com.fb.platform.sap.bapi.inventory.table.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.client.commons.SapInventoryConstants;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.sap.conn.jco.JCoFunction;

public class InventoryStockMapper {
	
	private static Log logger = LogFactory.getLog(InventoryStockMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, SapInventoryLevelRequestTO inventoryLevelRequestTO) {
		logger.info("Setting Inventory Level details for :" + inventoryLevelRequestTO);
		bapiFunction.getImportParameterList().setValue(SapOrderConstants.ARTICLE_ID, inventoryLevelRequestTO.getMaterial());
		bapiFunction.getImportParameterList().setValue(SapInventoryConstants.PLANT, inventoryLevelRequestTO.getPlant());
		bapiFunction.getImportParameterList().setValue(SapInventoryConstants.INPUT_STORAGE_LOCATION, inventoryLevelRequestTO.getStorageLocation());
	}
}
