package com.fb.platform.sap.bapi.inventory.table.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardResponseTO;
import com.fb.platform.sap.client.commons.SapInventoryConstants;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class TabResponseMapper {
	
	private static Log logger = LogFactory.getLog(TabResponseMapper.class);
	
	public static List<SapInventoryDashboardResponseTO> getDetails(JCoFunction bapiFunction, com.fb.platform.sap.client.connector.SapClientConnector bapiConnector) throws JCoException {
		logger.info("Executing Inventory Dashboard details");
		List<SapInventoryDashboardResponseTO> responseTOList = new ArrayList<SapInventoryDashboardResponseTO>();
		bapiFunction.execute(bapiConnector.getBapiDestination());
		JCoTable dashboardResponse = bapiFunction.getTableParameterList().getTable(BapiInventoryTable.TAB_RETURN.toString());
		for (int i = 0; i < dashboardResponse.getNumRows(); i++) {
			dashboardResponse.setRow(i);
			SapInventoryDashboardResponseTO responseTO = new SapInventoryDashboardResponseTO();
			responseTO.setIdocNumber(dashboardResponse.getString(SapInventoryConstants.IDOC_NUMBER));
			responseTO.setCreationDate(dashboardResponse.getDate(SapInventoryConstants.CREATION_DATE));
			responseTO.setCreationTime(dashboardResponse.getTime(SapInventoryConstants.CREATION_TIME));
			responseTO.setActualQuantity(dashboardResponse.getInt(SapInventoryConstants.ACTUAL_QTY));
			responseTO.setArticle(dashboardResponse.getString(SapInventoryConstants.ARTICLE_ID));
			responseTO.setArticleDocument(dashboardResponse.getString(SapInventoryConstants.ARTICLE_DOCUMENT));
			responseTO.setCancelGR(dashboardResponse.getString(SapInventoryConstants.CANCEL_GR));
			responseTO.setMovementType(dashboardResponse.getString(SapInventoryConstants.MOVEMENT_TYPE));
			responseTO.setPoNumber(dashboardResponse.getString(SapInventoryConstants.PO_NUMBER));
			responseTO.setReceivingLocation(dashboardResponse.getInt(SapInventoryConstants.RECEIVING_LOCATIION));
			responseTO.setSupplyingLocation(dashboardResponse.getInt(SapInventoryConstants.SUPPLYING_LOCATION));
			responseTO.setSupplyingSite(dashboardResponse.getInt(SapInventoryConstants.SUPPLYING_SITE));
			responseTO.setReceivingSite(dashboardResponse.getInt(SapInventoryConstants.RECEIVING_SITE));
			responseTO.setSegmentNumber(dashboardResponse.getInt(SapInventoryConstants.SEGMENT_NUMBER));
			responseTO.setTransactionCode(dashboardResponse.getString(SapInventoryConstants.TRANSACTION_CODE));
			responseTO.setTransferQuantity(dashboardResponse.getString(SapInventoryConstants.TRANSFER_QTY));
			responseTO.setUnit(dashboardResponse.getString(SapInventoryConstants.DASHBOARD_UNIT));
			responseTOList.add(responseTO);
			logger.info("Inventory Dashboard Response:" + responseTO);
		}
		return responseTOList;
	}

}
