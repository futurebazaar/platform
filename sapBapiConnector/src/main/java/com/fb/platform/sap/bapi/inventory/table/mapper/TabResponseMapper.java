package com.fb.platform.sap.bapi.inventory.table.mapper;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.sap.bapi.inventory.table.BapiInventoryTable;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardResponseTO;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class TabResponseMapper {
	
	public static List<SapInventoryDashboardResponseTO> getDetails(JCoFunction bapiFunction) {
		List<SapInventoryDashboardResponseTO> responseTOList = new ArrayList<SapInventoryDashboardResponseTO>();
		JCoTable dashboardResponse = bapiFunction.getTableParameterList().getTable(BapiInventoryTable.TAB_RETURN.toString());
		
		for (int i = 0; i < dashboardResponse.getNumRows(); i++) {
			SapInventoryDashboardResponseTO responseTO = new SapInventoryDashboardResponseTO();
			responseTO.setIdocNumber(dashboardResponse.getInt("DOCNUM"));
			responseTO.setCreationDate(dashboardResponse.getDate(""));
			responseTO.setCreationTime(dashboardResponse.getTime(""));
			System.out.println(responseTO);
			responseTOList.add(responseTO);
		}
		return responseTOList;
	}

}
