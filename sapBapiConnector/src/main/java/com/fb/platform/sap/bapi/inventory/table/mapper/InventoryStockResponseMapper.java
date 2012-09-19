package com.fb.platform.sap.bapi.inventory.table.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.client.commons.SapInventoryConstants;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.fb.platform.sap.client.connector.SapClientConnector;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class InventoryStockResponseMapper {
	
	private static Log logger = LogFactory.getLog(InventoryStockResponseMapper.class);
	
	public static SapInventoryLevelResponseTO getDetails(JCoFunction bapiFunction, SapClientConnector bapiConnector) throws JCoException {
		logger.info("Executing Inventory Level details");
		SapInventoryLevelResponseTO inventoryLevelResponseTO = new SapInventoryLevelResponseTO();
		bapiFunction.execute(bapiConnector.getBapiDestination());
		inventoryLevelResponseTO.setSite(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.SITE).toString());
		inventoryLevelResponseTO.setArticle(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.INV_ARTICLE).toString());
		inventoryLevelResponseTO.setStockQuantity(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.STOCK_QUANTITY).toString());
		inventoryLevelResponseTO.setUnit(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.UNIT).toString());
		inventoryLevelResponseTO.setStorageLocation(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.OUTPUT_STORAGE_LOCATION).toString());
		logger.info("Inventory Level response :" + inventoryLevelResponseTO);
		return inventoryLevelResponseTO;
	}

}
