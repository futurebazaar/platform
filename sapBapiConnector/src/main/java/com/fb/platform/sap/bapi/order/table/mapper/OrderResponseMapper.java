package com.fb.platform.sap.bapi.order.table.mapper;

import com.fb.platform.sap.bapi.BapiConnector;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.fb.platform.sap.bapi.utils.SapOrderConstants;
import com.fb.platform.sap.bapi.utils.SapResponseStatus;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class OrderResponseMapper {
	
	public static SapOrderResponseTO getDetails(JCoFunction bapiFunction, BapiConnector bapiConnector) throws JCoException {
		SapOrderResponseTO orderResponseTO = new SapOrderResponseTO();
		bapiFunction.execute(bapiConnector.getBapiDestination());
		JCoTable jcoResponse = bapiFunction.getTableParameterList().getTable(BapiOrderTable.RETURN.toString());
		bapiFunction.getImportParameterList().setValue("", "");
		String message = "";
		SapResponseStatus status = SapResponseStatus.FAILURE;
		for (int i = 0; i < jcoResponse.getNumRows(); i++) {
			jcoResponse.setRow(i);
			String type = jcoResponse.getValue(SapConstants.TYPE).toString();
			if (type.equals(SapOrderConstants.SUCCESS_FLAG)) {
				status = SapResponseStatus.SUCCESS;
			}
			else if (type.equals(SapOrderConstants.ERROR_FLAG)) {
				status = SapResponseStatus.ERROR;
			}
			else if (type.equals(SapOrderConstants.WARNING_FLAG)) {
				status = SapResponseStatus.WARNING;
			}
			
			String typeMessage =  jcoResponse.getValue(SapConstants.MESSAGE).toString();
			if (!(type.equals(SapOrderConstants.SUCCESS_FLAG) && typeMessage.startsWith("SALES"))) {
				message += "ID: " + jcoResponse.getValue("ID").toString() + " || ";
				message += "TYPE: " + type + " || ";
				message += "MESSAGE: " + typeMessage + "\n";
			}
		}
		orderResponseTO.setStatus(status);
		orderResponseTO.setSapMessage(message);
		System.out.println(orderResponseTO);
		return orderResponseTO;
			
	}

}
