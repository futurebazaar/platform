package com.fb.platform.sap.bapi.inventory.table.mapper;

import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.bapi.utils.SapInventoryConstants;
import com.fb.platform.sap.bapi.utils.SapOrderConstants;
import com.sap.conn.jco.JCoFunction;

public class InventoryStockMapper {
	public static void setDetails(JCoFunction bapiFunction, SapInventoryLevelRequestTO inventoryLevelRequestTO) {
		bapiFunction.getImportParameterList().setValue(SapOrderConstants.ARTICLE_ID, inventoryLevelRequestTO.getMaterial());
		bapiFunction.getImportParameterList().setValue(SapInventoryConstants.PLANT, inventoryLevelRequestTO.getPlant());
		bapiFunction.getImportParameterList().setValue(SapInventoryConstants.INPUT_STORAGE_LOCATION, inventoryLevelRequestTO.getStorageLocation());
	}
}
