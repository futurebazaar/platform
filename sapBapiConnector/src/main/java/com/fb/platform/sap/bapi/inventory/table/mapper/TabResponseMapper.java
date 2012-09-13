package com.fb.platform.sap.bapi.inventory.table.mapper;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.sap.bapi.BapiConnector;
import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardResponseTO;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class TabResponseMapper {
	
	public static List<SapInventoryDashboardResponseTO> getDetails(JCoFunction bapiFunction, BapiConnector bapiConnector) throws JCoException {
		List<SapInventoryDashboardResponseTO> responseTOList = new ArrayList<SapInventoryDashboardResponseTO>();
		bapiFunction.execute(bapiConnector.getBapiDestination());
		JCoTable dashboardResponse = bapiFunction.getTableParameterList().getTable(BapiInventoryTable.TAB_RETURN.toString());
		System.out.println(dashboardResponse.getNumRows() + " " + dashboardResponse.getNumColumns());
		for (int i = 0; i < dashboardResponse.getNumRows(); i++) {
			dashboardResponse.setRow(i);
			SapInventoryDashboardResponseTO responseTO = new SapInventoryDashboardResponseTO();
			responseTO.setIdocNumber(dashboardResponse.getString("DOCNUM"));
			responseTO.setCreationDate(dashboardResponse.getDate("CREDAT"));
			responseTO.setCreationTime(dashboardResponse.getTime("CRETIM"));
			responseTO.setActualQuantity(dashboardResponse.getInt("ACT_QUAN"));
			responseTO.setArticle(dashboardResponse.getString("MATNR"));
			responseTO.setArticleDocument(dashboardResponse.getString("MAT_DOC"));
			responseTO.setCancelGR(dashboardResponse.getString("CAN_GR"));
			responseTO.setMovementType(dashboardResponse.getString("BWART"));
			responseTO.setPoNumber(dashboardResponse.getString("PO_NUM"));
			responseTO.setReceivingLocation(dashboardResponse.getInt("R_LGORT"));
			responseTO.setSupplyingLocation(dashboardResponse.getInt("I_LGORT"));
			responseTO.setSupplyingSite(dashboardResponse.getInt("I_WERKS"));
			responseTO.setReceivingSite(dashboardResponse.getInt("R_WERKS"));
			responseTO.setSegmentNumber(dashboardResponse.getInt("SEG_NUM"));
			responseTO.setTransactionCode(dashboardResponse.getString("T_CODE"));
			responseTO.setTransferQuantity(dashboardResponse.getString("TRNSFR_QUAN"));
			responseTO.setUnit(dashboardResponse.getString("MEINS"));
			
			System.out.println(responseTO);
			responseTOList.add(responseTO);
		}
		return responseTOList;
	}

}
