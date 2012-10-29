package com.fb.platform.sap.bapi.inventory.table.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardResponseTO;
import com.fb.platform.sap.client.commons.SapConstants;
import com.fb.platform.sap.client.commons.SapInventoryConstants;
import com.fb.platform.sap.client.commons.SapUtils;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class TabResponseMapper {
	
	private static Log logger = LogFactory.getLog(TabResponseMapper.class);
	
	public static List<SapInventoryDashboardResponseTO> getDetails(JCoFunction bapiFunction, JCoDestination destination) throws JCoException {
		logger.info("Executing Inventory Dashboard details");
		List<SapInventoryDashboardResponseTO> responseTOList = new ArrayList<SapInventoryDashboardResponseTO>();
		bapiFunction.execute(destination);
		JCoTable dashboardResponse = bapiFunction.getTableParameterList().getTable(BapiInventoryTable.TAB_RETURN.toString());
		logger.info("Total Idoc Count for Dashboard request  ---- " +dashboardResponse.getNumRows());
		for (int i = 0; i < dashboardResponse.getNumRows(); i++) {
			dashboardResponse.setRow(i);
			SapInventoryDashboardResponseTO responseTO = new SapInventoryDashboardResponseTO();
			responseTO.setIdocNumber(dashboardResponse.getString(SapInventoryConstants.IDOC_NUMBER));
			String creationDate = dashboardResponse.getString(SapInventoryConstants.CREATION_DATE);
			String creationTime = dashboardResponse.getString(SapInventoryConstants.CREATION_TIME);
			DateTime creationDateTime = SapUtils.getDateTimeFromString(creationDate + " " + creationTime, SapConstants.JOIN_DATE_TIME_FORMAT);
			responseTO.setCreationDateTime(creationDateTime);
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