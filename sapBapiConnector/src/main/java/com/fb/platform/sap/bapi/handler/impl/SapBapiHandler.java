package com.fb.platform.sap.bapi.handler.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.BapiConnector;
import com.fb.platform.sap.bapi.factory.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandler;
import com.fb.platform.sap.bapi.inventory.BapiInventoryTemplate;
import com.fb.platform.sap.bapi.inventory.table.mapper.DashboardTimeMapper;
import com.fb.platform.sap.bapi.inventory.table.mapper.InventoryStockMapper;
import com.fb.platform.sap.bapi.inventory.table.mapper.InventoryStockResponseMapper;
import com.fb.platform.sap.bapi.inventory.table.mapper.TabArticleMapper;
import com.fb.platform.sap.bapi.inventory.table.mapper.TabPlantMapper;
import com.fb.platform.sap.bapi.inventory.table.mapper.TabResponseMapper;
import com.fb.platform.sap.bapi.lsp.BapiLspTemplate;
import com.fb.platform.sap.bapi.lsp.table.mapper.LspAwbResponseMapper;
import com.fb.platform.sap.bapi.lsp.table.mapper.LspAwbUpdateMapper;
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
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateResponseTO;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class SapBapiHandler implements PlatformBapiHandler {
	
	private static Log logger = LogFactory.getLog(SapBapiHandler.class);
	private BapiConnector bapiConnector;
    
	@Override
    public synchronized SapOrderResponseTO processOrder(String environment, SapOrderRequestTO orderRequestTO) {
		logger.info("Environment : " + environment + " Order Request : " + orderRequestTO);
		SapOrderResponseTO orderResponseTO = new SapOrderResponseTO();
    	try {
    		TinlaOrderType orderType = orderRequestTO.getOrderType();
    		BapiOrderTemplate template = PlatformBapiHandlerFactory.getTemplate(orderType, orderRequestTO.getOrderHeaderTO().getClient());
			bapiConnector.connect(environment, template.toString());
			logger.info("Connected Bapi template : " + template + "for orderType : " + orderType); 
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
			orderResponseTO = OrderResponseMapper.getDetails(bapiFunction, bapiConnector);
		} catch (JCoException e) {
			logger.error("Environment : " + environment + " Failed to connet Order Bapi  for request : " + orderRequestTO, e);
			orderResponseTO.setSapMessage(e.getMessage());
		} catch (IOException e) {
			logger.error("Environment : " + environment + " Failed to connet Order Bapi  for request : " + orderRequestTO, e);
			orderResponseTO.setSapMessage(e.getMessage());
		}
    	orderResponseTO.setOrderID(orderRequestTO.getOrderHeaderTO().getReferenceID());
		orderResponseTO.setType(orderRequestTO.getOrderType().toString());
		logger.info("Response for order Request : " + orderRequestTO + " is : " + orderResponseTO);
    	return orderResponseTO;
    }
	
	@Override
	public List<SapInventoryDashboardResponseTO> processInventoryDashboard(String environment, SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		logger.info("Environment : " + environment + " Inventory Dashboard Request : " + inventoryDashboardRequestTO);
		try {
			bapiConnector.connect(environment, BapiInventoryTemplate.ZFB_INVCHECK.toString());
			JCoFunction bapiFunction = bapiConnector.getBapiFunction();
			DashboardTimeMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			TabArticleMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			TabPlantMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			return TabResponseMapper.getDetails(bapiFunction, bapiConnector);
		} catch (JCoException e) {
			logger.error("Environment : " + environment + " Failed to connet Bapi Dashboard for request " + inventoryDashboardRequestTO, e);
		} catch (IOException e) {
			logger.error("Environment : " + environment + " Failed to connet Bapi Dashboard for request " + inventoryDashboardRequestTO, e);
		}
		return null;
	}
	
	@Override
	public SapInventoryLevelResponseTO processInventoryLevel(String environment, SapInventoryLevelRequestTO inventoryLevelRequestTO) {
		logger.info("Environment : " + environment + " Inventory Level Request : " + inventoryLevelRequestTO);
		try {
			bapiConnector.connect(environment, BapiInventoryTemplate.ZBAPI_FM_TINLASTKQTY.toString());
			JCoFunction bapiFunction = bapiConnector.getBapiFunction();
			InventoryStockMapper.setDetails(bapiFunction, inventoryLevelRequestTO);
			return InventoryStockResponseMapper.getDetails(bapiFunction, bapiConnector);
		} catch (JCoException e) {
			logger.error("Environment : " + environment + " Failed to connet Bapi Inventory Stock for request: " + inventoryLevelRequestTO, e);
		} catch (IOException e) {
			logger.error("Environment : " + environment + " Failed to connet Bapi Inventory Stock for request: " + inventoryLevelRequestTO, e);
		}
		return null;
	}
	
	@Override
	public SapLspAwbUpdateResponseTO processLspAwbUpdate(String environment, SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO) {
		logger.info("Environment : " + environment + " Awb Update Request : " + lspAwbUpdateRequestTO);
		try {
			bapiConnector.connect(environment, BapiLspTemplate.ZFB_DELV_UPDT_AWB.toString());
			JCoFunction bapiFunction = bapiConnector.getBapiFunction();
			LspAwbUpdateMapper.setDetails(bapiFunction, lspAwbUpdateRequestTO);
			return LspAwbResponseMapper.getDetails(bapiFunction, bapiConnector);
		} catch (JCoException e) {
			logger.error("Environment : " + environment + " Failed to connet Bapi Awb Update for request: " + lspAwbUpdateRequestTO, e);
		} catch (IOException e) {
			logger.error("Environment : " + environment + " Failed to connet Bapi Awb Update for request  " + lspAwbUpdateRequestTO, e);
		}
		return null;
	}

	public void setBapiConnector(BapiConnector bapiConnector) {
		this.bapiConnector = bapiConnector;
	}
	
}
