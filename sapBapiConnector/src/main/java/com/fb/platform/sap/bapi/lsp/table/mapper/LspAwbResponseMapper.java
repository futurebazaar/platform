package com.fb.platform.sap.bapi.lsp.table.mapper;

import com.fb.platform.sap.bapi.BapiConnector;
import com.fb.platform.sap.bapi.commons.SapConstants;
import com.fb.platform.sap.bapi.lsp.table.BapiLspTable;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateResponseTO;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class LspAwbResponseMapper {

	public static SapLspAwbUpdateResponseTO getDetails(JCoFunction bapiFunction, BapiConnector bapiConnector) throws JCoException {
		SapLspAwbUpdateResponseTO responseTO = new SapLspAwbUpdateResponseTO();
		bapiFunction.execute(bapiConnector.getBapiDestination());
		JCoTable sapResponse = bapiFunction.getTableParameterList().getTable(BapiLspTable.RETURN.toString());
		sapResponse.setRow(0);
		responseTO.setMessage(sapResponse.getValue(SapConstants.MESSAGE).toString());
		responseTO.setType(sapResponse.getValue(SapConstants.TYPE).toString());
		return responseTO;
	}
	
}
