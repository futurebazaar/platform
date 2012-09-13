package com.fb.platform.sap.bapi.handler.impl;

import com.fb.platform.sap.bapi.BapiConnector;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandler;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.inventory.BapiInventoryTemplate;
import com.fb.platform.sap.bapi.inventory.table.mapper.DashboardTimeMapper;
import com.fb.platform.sap.bapi.inventory.table.mapper.TabArticleMapper;
import com.fb.platform.sap.bapi.inventory.table.mapper.TabPlantMapper;
import com.fb.platform.sap.bapi.inventory.table.mapper.TabResponseMapper;
import com.fb.platform.sap.bapi.order.BapiOrderTemplate;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;
import com.fb.platform.sap.bapi.order.table.mapper.HeaderConditionsMapper;
import com.fb.platform.sap.bapi.order.table.mapper.HeaderMapper;
import com.fb.platform.sap.bapi.order.table.mapper.HeaderPartnerMapper;
import com.fb.platform.sap.bapi.order.table.mapper.ItemConditionsMapper;
import com.fb.platform.sap.bapi.order.table.mapper.ItemMapper;
import com.fb.platform.sap.bapi.order.table.mapper.ItemPartnerMapper;
import com.fb.platform.sap.bapi.order.table.mapper.ItemScheduleMapper;
import com.fb.platform.sap.bapi.order.table.mapper.OrderResponseMapper;
import com.fb.platform.sap.bapi.order.table.mapper.PaymentMapper;
import com.fb.platform.sap.bapi.order.table.mapper.PointsMapper;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardResponseTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class SapBapiHandler implements PlatformBapiHandler {
	
	private BapiConnector bapiConnector;
    
	@Override
    public synchronized SapOrderResponseTO processOrder(String environment, SapOrderRequestTO orderRequestTO) {
		SapOrderResponseTO orderResponseTO = new SapOrderResponseTO();
    	try {
    		TinlaOrderType orderType = orderRequestTO.getOrderType();
    		
    		BapiOrderTemplate template = PlatformBapiHandlerFactory.getTemplate(orderType);
    		
			bapiConnector.connect(environment, template.toString());
			JCoFunction bapiFunction = bapiConnector.getBapiFunction();
			
			if (orderType.equals(TinlaOrderType.RET_ORDER)) {
				HeaderMapper.setReturnDetails(bapiFunction,  orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
				ItemMapper.setReturnItemDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
			} else {
				HeaderMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderType);
				HeaderConditionsMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderType);
				ItemMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
				ItemScheduleMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
				
				if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
					HeaderPartnerMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getAddressTO(), orderType);
					PaymentMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getPaymentTO());
					ItemPartnerMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getAddressTO(), orderRequestTO.getLineItemTO());
				}
			}
			
			PointsMapper.setDetails(bapiFunction, orderRequestTO.getPricingTO(), orderRequestTO.getOrderHeaderTO().getLoyaltyCardNumber());
			ItemConditionsMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
			
			orderResponseTO = OrderResponseMapper.getDetails(bapiFunction);
			
		} catch (JCoException e) {
			e.printStackTrace();
			orderResponseTO.setSapMessage(e.getMessage());
		}
    	orderResponseTO.setOrderID(orderRequestTO.getOrderHeaderTO().getReferenceID());
		orderResponseTO.setType(orderRequestTO.getOrderType().toString());
    	return orderResponseTO;
    }
	
	@Override
	public SapInventoryDashboardResponseTO processInventoryDashboard(String environment, SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		try {
			bapiConnector.connect(environment, BapiInventoryTemplate.ZFB_INVCHECK.toString());
			JCoFunction bapiFunction = bapiConnector.getBapiFunction();
			DashboardTimeMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			TabArticleMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			TabPlantMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			TabResponseMapper.getDetails(bapiFunction);
			return null;
			
		} catch (JCoException e) {
			e.printStackTrace();
			return new SapInventoryDashboardResponseTO();
		}
	}

	@Override
	public SapInventoryLevelResponseTO processInventoryLevel(String environment, SapInventoryLevelRequestTO inventoryLevelRequestTO) {
		return null;
	}
	
	public void setBapiConnector(BapiConnector bapiConnector) {
		this.bapiConnector = bapiConnector;
	}
	
}