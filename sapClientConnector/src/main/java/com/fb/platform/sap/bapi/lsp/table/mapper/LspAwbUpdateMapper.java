package com.fb.platform.sap.bapi.lsp.table.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.lsp.table.BapiLspTable;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.sap.conn.jco.JCoFunction;

public class LspAwbUpdateMapper {
	
	private static Log logger = LogFactory.getLog(LspAwbUpdateMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO) {
		logger.info("Setting AwbUpdate Details for : " +lspAwbUpdateRequestTO);
		bapiFunction.getImportParameterList().setValue(BapiLspTable.AWB.toString(), lspAwbUpdateRequestTO.getAwb());
		bapiFunction.getImportParameterList().setValue(BapiLspTable.DELIVERY.toString(), lspAwbUpdateRequestTO.getDeliveryNumber());
		bapiFunction.getImportParameterList().setValue(BapiLspTable.LSP.toString(), lspAwbUpdateRequestTO.getLspCode());
	}
}
