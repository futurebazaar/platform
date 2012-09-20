package com.fb.platform.sap.bapi.lsp.table.mapper;

import com.fb.platform.sap.bapi.lsp.table.BapiLspTable;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.sap.conn.jco.JCoFunction;

public class LspAwbUpdateMapper {
	
	public static void setDetails(JCoFunction bapiFunction, SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO) {
		bapiFunction.getImportParameterList().setValue(BapiLspTable.AWB.toString(), lspAwbUpdateRequestTO.getAwb());
		bapiFunction.getImportParameterList().setValue(BapiLspTable.DELIVERY.toString(), lspAwbUpdateRequestTO.getDeliveryNumber());
		bapiFunction.getImportParameterList().setValue(BapiLspTable.LSP.toString(), lspAwbUpdateRequestTO.getLspCode());
	}
}
