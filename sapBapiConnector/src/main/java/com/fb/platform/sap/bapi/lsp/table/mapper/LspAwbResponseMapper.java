package com.fb.platform.sap.bapi.lsp.table.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.client.commons.SapConstants;
import com.fb.platform.sap.bapi.inventory.table.mapper.TabPlantMapper;
import com.fb.platform.sap.bapi.lsp.table.BapiLspTable;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateResponseTO;
import com.fb.platform.sap.client.connector.SapClientConnector;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class LspAwbResponseMapper {
	
	private static Log logger = LogFactory.getLog(LspAwbResponseMapper.class);

	public static SapLspAwbUpdateResponseTO getDetails(JCoFunction bapiFunction, SapClientConnector bapiConnector) throws JCoException {
		logger.info("Executing Lsp Awb Update details");
		SapLspAwbUpdateResponseTO responseTO = new SapLspAwbUpdateResponseTO();
		bapiFunction.execute(bapiConnector.getBapiDestination());
		JCoTable sapResponse = bapiFunction.getTableParameterList().getTable(BapiLspTable.RETURN.toString());
		sapResponse.setRow(0);
		responseTO.setMessage(sapResponse.getValue(SapConstants.MESSAGE).toString());
		responseTO.setType(sapResponse.getValue(SapConstants.TYPE).toString());
		for (int i = 0; i < sapResponse.getNumRows(); i++) {
			logger.info("Lsp Awb Update Response type : " + sapResponse.getValue(SapConstants.TYPE).toString());
			logger.info("Lsp Awb Update Response message : " + sapResponse.getValue(SapConstants.MESSAGE).toString());
		}
		return responseTO;
	}
	
}
