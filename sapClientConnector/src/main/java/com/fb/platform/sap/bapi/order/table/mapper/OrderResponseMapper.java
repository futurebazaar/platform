package com.fb.platform.sap.bapi.order.table.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.client.commons.SapConstants;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.SapResponseStatus;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class OrderResponseMapper {
	
	private static Log logger = LogFactory.getLog(HeaderConditionsMapper.class);
	
	public static SapOrderResponseTO getDetails(JCoFunction bapiFunction, JCoDestination destination) throws JCoException {
		logger.info("Executing order details");
		SapOrderResponseTO orderResponseTO = new SapOrderResponseTO();
		bapiFunction.execute(destination);
		JCoTable jcoResponse = bapiFunction.getTableParameterList().getTable(BapiOrderTable.RETURN.toString());
		String message = "";
		SapResponseStatus status = SapResponseStatus.ERROR;
		for (int i = 0; i < jcoResponse.getNumRows(); i++) {
			jcoResponse.setRow(i);
			String type = jcoResponse.getValue(SapConstants.TYPE).toString();
			if (type.equals(SapOrderConstants.SUCCESS_FLAG)) {
				status = SapResponseStatus.SUCCESS;
			}
			else if (type.equals(SapOrderConstants.ERROR_FLAG)) {
				status = SapResponseStatus.FAILED;
			}
			else if (type.equals(SapOrderConstants.WARNING_FLAG)) {
				status = SapResponseStatus.WARNING;
			}
			String typeMessage =  jcoResponse.getValue(SapConstants.MESSAGE).toString();
			String typeId =  jcoResponse.getValue("ID").toString();
			logger.info("Response Type: " + type + " || Message: " + typeMessage + " || ID: " + typeId);
			if (!(type.equals(SapOrderConstants.SUCCESS_FLAG)) || (typeMessage.contains("FECIL") || typeMessage.contains("FBIL") || typeMessage.contains("FBG"))) {
				message += "ID: " + typeId + " || ";
				message += "TYPE: " + type + " || ";
				message += "MESSAGE: " + typeMessage + "\n";
			}
		}
		orderResponseTO.setStatus(status);
		orderResponseTO.setSapMessage(message);
		return orderResponseTO;
			
	}

}
