package com.fb.platform.sap.bapi.handler.impl;

import com.fb.platform.sap.bapi.BapiConnector;
import com.fb.platform.sap.bapi.BapiTemplate;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandler;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.table.mapper.HeaderConditionsMapper;
import com.fb.platform.sap.bapi.table.mapper.HeaderMapper;
import com.fb.platform.sap.bapi.table.mapper.HeaderPartnerMapper;
import com.fb.platform.sap.bapi.table.mapper.ItemConditionsMapper;
import com.fb.platform.sap.bapi.table.mapper.ItemMapper;
import com.fb.platform.sap.bapi.table.mapper.ItemPartnerMapper;
import com.fb.platform.sap.bapi.table.mapper.ItemScheduleMapper;
import com.fb.platform.sap.bapi.table.mapper.PaymentMapper;
import com.fb.platform.sap.bapi.table.mapper.PointsMapper;
import com.fb.platform.sap.bapi.test.TestBapiTO;
import com.fb.platform.sap.bapi.to.BapiTO;
import com.fb.platform.sap.bapi.to.ResponseTO;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class SapBapiHandler implements PlatformBapiHandler {
	
	private BapiConnector bapiConnector;
    
	@Override
    public synchronized ResponseTO execute(String environment, BapiTO bapiTO) {
		ResponseTO responseTO = new ResponseTO();
		responseTO.setOrderID(bapiTO.getOrderHeaderTO().getReferenceID());
		responseTO.setType(bapiTO.getOrderType().toString());
    	try {
    		TinlaOrderType orderType = bapiTO.getOrderType();
    		
    		BapiTemplate template = PlatformBapiHandlerFactory.getTemplate(orderType);
    		System.out.println(template);
    		
			bapiConnector.connect(environment, template);
			JCoFunction bapiFunction = bapiConnector.getBapiFunction();
			
			if (orderType.equals(TinlaOrderType.RET_ORDER)) {
				HeaderMapper.setReturnDetails(bapiFunction,  bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
				ItemMapper.setReturnItemDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
			} else {
				HeaderMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), orderType);
				HeaderConditionsMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), orderType);
				ItemMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
				ItemScheduleMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
				
				if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
					HeaderPartnerMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getAddressTO(), orderType);
					PaymentMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getPaymentTO());
					ItemPartnerMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getAddressTO(), bapiTO.getLineItemTO());
				}
			}
			
			PointsMapper.setDetails(bapiFunction, bapiTO.getPricingTO(), bapiTO.getOrderHeaderTO().getLoyaltyCardNumber());
			ItemConditionsMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
			
			// call commit and return the SAP response
			responseTO = commit(bapiConnector, responseTO);
			
		} catch (JCoException e) {
			e.printStackTrace();
			responseTO.setSapMessage(e.getMessage());
			responseTO.setOrderID(bapiTO.getOrderHeaderTO().getReferenceID());
			
		}
    	return responseTO;
    }

	private ResponseTO commit(BapiConnector bapiConnector, ResponseTO responseTO) throws JCoException {
		JCoFunction bapiFunction = bapiConnector.getBapiFunction();
		bapiFunction.execute(bapiConnector.getBapiDestination());
		JCoTable jcoResponse = bapiFunction.getTableParameterList().getTable(BapiTable.RETURN.toString());
		String message = "";
		for (int i = 0; i < jcoResponse.getNumRows(); i++) {
			jcoResponse.setRow(i);
			String type = jcoResponse.getValue("TYPE").toString();
			String typeMessage =  jcoResponse.getValue("MESSAGE").toString();
			if (!(type.equals(SapConstants.SUCCESS_FLAG) && typeMessage.startsWith("SALES"))) {
				message += "ID: " + jcoResponse.getValue("ID").toString() + " || ";
				message += "TYPE: " + type + " || ";
				message += "MESSAGE: " + typeMessage + "\n";
			}
		}
		
		responseTO.setSapMessage(message);
		return responseTO;
	}

	public void setBapiConnector(BapiConnector bapiConnector) {
		this.bapiConnector = bapiConnector;
	}
	
}