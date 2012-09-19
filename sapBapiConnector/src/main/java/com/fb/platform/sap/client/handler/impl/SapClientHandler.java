package com.fb.platform.sap.client.handler.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.factory.PlatformBapiHandlerFactory;
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
import com.fb.platform.sap.client.connector.SapClientConnector;
import com.fb.platform.sap.client.handler.PlatformClientHandler;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class SapClientHandler implements PlatformClientHandler {
	
	private static Log logger = LogFactory.getLog(SapClientHandler.class);
	private SapClientConnector clientConnector;
    
	@Override
    public synchronized SapOrderResponseTO processOrder(SapOrderRequestTO orderRequestTO) {
		logger.info("Order Request : " + orderRequestTO + " for order id: " + orderRequestTO.getOrderHeaderTO().getReferenceID());
		SapOrderResponseTO orderResponseTO = new SapOrderResponseTO();
		TinlaOrderType orderType = orderRequestTO.getOrderType();
    	try {
    		BapiOrderTemplate template = PlatformBapiHandlerFactory.getTemplate(orderType, orderRequestTO.getOrderHeaderTO().getClient());
			clientConnector.connectBapi(template.toString());
			logger.info("Connected Bapi template : " + template + "for : " + orderType + " " + orderRequestTO.getOrderHeaderTO().getReferenceID()); 
			JCoFunction bapiFunction = clientConnector.getBapiFunction();
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
			orderResponseTO = OrderResponseMapper.getDetails(bapiFunction, clientConnector);
		} catch (JCoException e) {
			logger.error("Failed to connet Order Bapi  for " + orderType + " " + orderRequestTO.getOrderHeaderTO().getReferenceID(), e);
			orderResponseTO.setSapMessage(e.getMessage());
		} catch (IOException e) {
			logger.error("Failed to connet Order Bapi  for " + orderType + " " + orderRequestTO.getOrderHeaderTO().getReferenceID(), e);
			orderResponseTO.setSapMessage(e.getMessage());
		}
    	orderResponseTO.setOrderID(orderRequestTO.getOrderHeaderTO().getReferenceID());
		orderResponseTO.setType(orderRequestTO.getOrderType().toString());
		logger.info("Response for " + orderType + " " + orderRequestTO.getOrderHeaderTO().getReferenceID() + " is : " + orderResponseTO);
    	return orderResponseTO;
    }
	
	@Override
	public List<SapInventoryDashboardResponseTO> processInventoryDashboard(SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		logger.info("Inventory Dashboard Request : " + inventoryDashboardRequestTO);
		List<SapInventoryDashboardResponseTO> inventoryDashboardResponseTO = new ArrayList<SapInventoryDashboardResponseTO>();
		try {
			clientConnector.connectBapi(BapiInventoryTemplate.ZFB_INVCHECK.toString());
			JCoFunction bapiFunction = clientConnector.getBapiFunction();
			DashboardTimeMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			TabArticleMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			TabPlantMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			inventoryDashboardResponseTO = TabResponseMapper.getDetails(bapiFunction, clientConnector);
			logger.info("Inventory Dashboard response : " + inventoryDashboardResponseTO + " for inventory Dashboard request :" + inventoryDashboardRequestTO);
		} catch (JCoException e) {
			logger.error("Failed to connet Bapi Dashboard for request " + inventoryDashboardRequestTO, e);
		} catch (IOException e) {
			logger.error("Failed to connet Bapi Dashboard for request " + inventoryDashboardRequestTO, e);
		}
		return inventoryDashboardResponseTO;
	}
	
	@Override
	public SapInventoryLevelResponseTO processInventoryLevel(SapInventoryLevelRequestTO inventoryLevelRequestTO) {
		logger.info("Inventory Level Request : " + inventoryLevelRequestTO);
		SapInventoryLevelResponseTO inventoryLevelResponseTO = new SapInventoryLevelResponseTO();
		try {
			clientConnector.connectBapi(BapiInventoryTemplate.ZBAPI_FM_TINLASTKQTY.toString());
			JCoFunction bapiFunction = clientConnector.getBapiFunction();
			InventoryStockMapper.setDetails(bapiFunction, inventoryLevelRequestTO);
			inventoryLevelResponseTO = InventoryStockResponseMapper.getDetails(bapiFunction, clientConnector);
			logger.info("Inventory Level response : " + inventoryLevelResponseTO + " for inventory level request :" + inventoryLevelRequestTO);
		} catch (JCoException e) {
			logger.error("Failed to connet Bapi Inventory Stock for request: " + inventoryLevelRequestTO, e);
		} catch (IOException e) {
			logger.error("Failed to connet Bapi Inventory Stock for request: " + inventoryLevelRequestTO, e);
		}
		return inventoryLevelResponseTO;
	}
	
	@Override
	public SapLspAwbUpdateResponseTO processLspAwbUpdate(SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO) {
		logger.info("Awb Update Request : " + lspAwbUpdateRequestTO);
		SapLspAwbUpdateResponseTO lspAwbUpdateResponseTO = new SapLspAwbUpdateResponseTO();
		try {
			clientConnector.connectBapi(BapiLspTemplate.ZFB_DELV_UPDT_AWB.toString());
			JCoFunction bapiFunction = clientConnector.getBapiFunction();
			LspAwbUpdateMapper.setDetails(bapiFunction, lspAwbUpdateRequestTO);
			lspAwbUpdateResponseTO = LspAwbResponseMapper.getDetails(bapiFunction, clientConnector);
			logger.info("Lsp Awb Update response : " + lspAwbUpdateResponseTO + " for lsp awb update request :" + lspAwbUpdateRequestTO);
		} catch (JCoException e) {
			logger.error("Failed to connet Bapi Awb Update for request: " + lspAwbUpdateRequestTO, e);
		} catch (IOException e) {
			logger.error("Failed to connet Bapi Awb Update for request  " + lspAwbUpdateRequestTO, e);
		}
		return lspAwbUpdateResponseTO;
	}

	public void setBapiConnector(SapClientConnector bapiConnector) {
		this.clientConnector = bapiConnector;
	}
	
}
