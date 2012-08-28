package com.fb.platform.sap.bapi.handler.impl;

import java.awt.image.RescaleOp;

import com.fb.commons.mom.to.OrderHeaderTO;
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
    	try {
    		TinlaOrderType orderType = bapiTO.getOrderType();
    		
    		BapiTemplate template = PlatformBapiHandlerFactory.getTemplate(orderType);
    		System.out.println(template);
    		
			bapiConnector.connect(environment, template);
			JCoFunction bapiFunction = bapiConnector.getBapiFunction();
			
			if (!orderType.equals(TinlaOrderType.RET_ORDER)) {
				HeaderMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), orderType);
				HeaderConditionsMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), orderType);
				HeaderPartnerMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getAddressTO(), orderType);
				ItemMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
				ItemPartnerMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getAddressTO(), bapiTO.getLineItemTO());
				ItemScheduleMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
				
				if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
					PaymentMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getPaymentTO());
				}
				
			} else {
				HeaderMapper.setReturnDetails(bapiFunction,  bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
				ItemMapper.setReturnItemDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
			}
			PointsMapper.setDetails(bapiFunction, bapiTO.getPricingTO(), bapiTO.getOrderHeaderTO().getLoyaltyCardNumber());
			ItemConditionsMapper.setDetails(bapiFunction, bapiTO.getOrderHeaderTO(), bapiTO.getLineItemTO(), orderType);
			
			// call commit and return the SAP response
			return commit(bapiConnector, bapiTO.getOrderHeaderTO(), orderType);
			
		} catch (JCoException e) {
			e.printStackTrace();
		}
    }

	private ResponseTO commit(BapiConnector bapiConnector, OrderHeaderTO orderHeaderTO, TinlaOrderType orderType) throws JCoException {
		JCoFunction bapiFunction = bapiConnector.getBapiFunction();
		bapiFunction.execute(bapiConnector.getBapiDestination());
		JCoTable jcoResponse = bapiFunction.getTableParameterList().getTable(BapiTable.RETURN.toString());
		ResponseTO responseTO = new ResponseTO();
		String message = "";
		for (int i = 0; i < jcoResponse.getNumRows(); i++) {
			jcoResponse.setRow(i);
			String type = jcoResponse.getValue("TYPE").toString();
			if (type.equals(SapConstants.SUCCESS_FLAG)) {
				message += jcoResponse.getValue("MESSAGE");
			}
		}
		
		responseTO.setSapMessage(message);
		responseTO.setOrderID(orderHeaderTO.getReferenceID());
		System.out.println(responseTO.toString());
		return responseTO;
	}

	public void setBapiConnector(BapiConnector bapiConnector) {
		this.bapiConnector = bapiConnector;
	}
    
	public static void main(String[] args) {
		BapiTO bapiTO = new TestBapiTO().getBapiTO();
		
		BapiConnector bapiConnector = new BapiConnector();
		SapBapiHandler bh = new SapBapiHandler();
		bh.setBapiConnector(bapiConnector);
		System.out.println(bh.execute("", bapiTO));;
	}
}