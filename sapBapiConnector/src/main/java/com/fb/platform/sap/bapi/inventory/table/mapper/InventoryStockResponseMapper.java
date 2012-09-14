package com.fb.platform.sap.bapi.inventory.table.mapper;

import com.fb.platform.sap.bapi.BapiConnector;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.fb.platform.sap.bapi.utils.SapInventoryConstants;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class InventoryStockResponseMapper {
	
	public static SapInventoryLevelResponseTO getDetails(JCoFunction bapiFunction, BapiConnector bapiConnector) throws JCoException {
		SapInventoryLevelResponseTO inventoryLevelResponseTO = new SapInventoryLevelResponseTO();
		bapiFunction.execute(bapiConnector.getBapiDestination());
		inventoryLevelResponseTO.setSite(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.SITE).toString());
		inventoryLevelResponseTO.setArticle(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.INV_ARTICLE).toString());
		inventoryLevelResponseTO.setStockQuantity(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.STOCK_QUANTITY).toString());
		inventoryLevelResponseTO.setUnit(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.UNIT).toString());
		inventoryLevelResponseTO.setStorageLocation(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.OUTPUT_STORAGE_LOCATION).toString());
		return inventoryLevelResponseTO;
	}

}
