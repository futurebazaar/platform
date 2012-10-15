package com.fb.platform.sap.bapi.inventory.table.mapper;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.client.commons.SapInventoryConstants;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class InventoryStockResponseMapper {
	
	private static Log logger = LogFactory.getLog(InventoryStockResponseMapper.class);
	
	public static SapInventoryLevelResponseTO getDetails(JCoFunction bapiFunction, JCoDestination destination) throws JCoException {
		logger.info("Executing Inventory Level details");
		SapInventoryLevelResponseTO inventoryLevelResponseTO = new SapInventoryLevelResponseTO();
		bapiFunction.execute(destination);
		inventoryLevelResponseTO.setSite(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.SITE).toString());
		inventoryLevelResponseTO.setArticle(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.INV_ARTICLE).toString());
		String quantity = bapiFunction.getExportParameterList().getString(SapInventoryConstants.STOCK_QUANTITY);
		try {
			inventoryLevelResponseTO.setStockQuantity(new BigDecimal(quantity));
		} catch(NumberFormatException e) {
			logger.error("Invalid number for stock quantity :"+quantity , e);
		}
		inventoryLevelResponseTO.setUnit(bapiFunction.getExportParameterList().getValue(SapInventoryConstants.UNIT).toString());
		inventoryLevelResponseTO.setStorageLocation(bapiFunction.getExportParameterList().getInt(SapInventoryConstants.OUTPUT_STORAGE_LOCATION));
		logger.info("Inventory Level response :" + inventoryLevelResponseTO);
		return inventoryLevelResponseTO;
	}

}
