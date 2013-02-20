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
import com.fb.platform.sap.bapi.order.TinlaOrderType;
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
import com.fb.platform.sap.client.commons.SapResponseStatus;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.commons.TinlaClient;
import com.fb.platform.sap.client.connector.PlatformClientConnector;
import com.fb.platform.sap.client.handler.PlatformClientHandler;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class SapClientHandler implements PlatformClientHandler {
	
	private static Log logger = LogFactory.getLog(SapClientHandler.class);
	private PlatformClientConnector sapClientConnector;
    
	@Override
    public SapOrderResponseTO processOrder(SapOrderRequestTO orderRequestTO) {
		logger.info("Order Request : " + orderRequestTO + " for order id: " + orderRequestTO.getOrderHeaderTO().getReferenceID());
		SapOrderResponseTO orderResponseTO = new SapOrderResponseTO();
		orderResponseTO.setStatus(SapResponseStatus.ERROR);
		TinlaOrderType orderType = orderRequestTO.getOrderType();
		TinlaClient client = TinlaClient.valueOf(orderRequestTO.getOrderHeaderTO().getClient());
    	try {
    		BapiOrderTemplate template = PlatformBapiHandlerFactory.getTemplate(orderType, orderRequestTO.getOrderHeaderTO().getClient());
			logger.info("Connecting Bapi template : " + template + "for : " + orderType + " " + orderRequestTO.getOrderHeaderTO().getReferenceID());
			JCoDestination destination = sapClientConnector.connectSap(client);
			JCoFunction bapiFunction = destination.getRepository().getFunctionTemplate(template.toString()).getFunction(); 
			if (orderType.equals(TinlaOrderType.RET_ORDER)) {
				HeaderMapper.setReturnDetails(bapiFunction,  orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType, client);
				ItemMapper.setReturnItemDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
			} else {
				HeaderMapper.setDetails(bapiFunction, orderRequestTO, orderRequestTO.getOrderHeaderTO(), orderType);
				//Below condition is not used now
				//HeaderConditionsMappernFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
				ItemScheduleMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
				if (!(orderType.equals(TinlaOrderType.MOD_ORDER) && SapUtils.isBigBazaar(client))) {
					ItemPartnerMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
					HeaderPartnerMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getDefaultShippingAddressTO(), orderRequestTO.getBillingAddressTO(), orderType);
				}
				if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
					PaymentMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getPaymentTO());
				}
			}
			PointsMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO().getPricingTO(), orderRequestTO.getOrderHeaderTO().getLoyaltyCardNumber());
			ItemConditionsMapper.setDetails(bapiFunction, orderRequestTO.getOrderHeaderTO(), orderRequestTO.getLineItemTO(), orderType);
			orderResponseTO = OrderResponseMapper.getDetails(bapiFunction, destination);
		} catch (JCoException e) {
			logger.error("Failed to connect Order Bapi  for " + orderType + " " + orderRequestTO.getOrderHeaderTO().getReferenceID(), e);
			orderResponseTO.setSapMessage(e.getMessage());
		} catch (IOException e) {
			logger.error("Failed to connect Order Bapi  for " + orderType + " " + orderRequestTO.getOrderHeaderTO().getReferenceID(), e);
			orderResponseTO.setSapMessage(e.getMessage());
		}
    	orderResponseTO.setOrderId(orderRequestTO.getOrderHeaderTO().getReferenceID());
    	orderResponseTO.setReturnOrderId(orderRequestTO.getOrderHeaderTO().getReturnOrderID());
		orderResponseTO.setType(orderRequestTO.getOrderType().toString());
		logger.info("Response for " + orderType + " " + orderRequestTO.getOrderHeaderTO().getReferenceID() + " is : " + orderResponseTO);
    	return orderResponseTO;
    }
	
	@Override
	public List<SapInventoryDashboardResponseTO> processInventoryDashboard(SapInventoryDashboardRequestTO inventoryDashboardRequestTO) {
		logger.info("Inventory Dashboard Request : " + inventoryDashboardRequestTO);
		List<SapInventoryDashboardResponseTO> inventoryDashboardResponseTO = new ArrayList<SapInventoryDashboardResponseTO>();
		try {
			JCoDestination destination = sapClientConnector.connectSap(TinlaClient.valueOf(inventoryDashboardRequestTO.getClient()));
			JCoFunction bapiFunction = destination.getRepository().getFunctionTemplate(BapiInventoryTemplate.ZFB_INVCHECK.toString()).getFunction();
			DashboardTimeMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			TabArticleMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			TabPlantMapper.setDetails(bapiFunction, inventoryDashboardRequestTO);
			inventoryDashboardResponseTO = TabResponseMapper.getDetails(bapiFunction, destination);
			logger.info("Inventory Dashboard response : " + inventoryDashboardResponseTO + " for inventory Dashboard request :" + inventoryDashboardRequestTO);
		} catch (JCoException e) {
			logger.error("Failed to connect Bapi Dashboard for request " + inventoryDashboardRequestTO, e);
		} catch (IOException e) {
			logger.error("Failed to connect Bapi Dashboard for request " + inventoryDashboardRequestTO, e);
		}
		return inventoryDashboardResponseTO;
	}
	
	@Override
	public SapInventoryLevelResponseTO processInventoryLevel(SapInventoryLevelRequestTO inventoryLevelRequestTO) {
		logger.info("Inventory Level Request : " + inventoryLevelRequestTO);
		SapInventoryLevelResponseTO inventoryLevelResponseTO = new SapInventoryLevelResponseTO();
		try {
			JCoDestination destination = sapClientConnector.connectSap(TinlaClient.valueOf(inventoryLevelRequestTO.getClient()));
			JCoFunction bapiFunction = destination.getRepository().getFunctionTemplate(BapiInventoryTemplate.ZBAPI_FM_TINLASTKQTY.toString()).getFunction();
			InventoryStockMapper.setDetails(bapiFunction, inventoryLevelRequestTO);
			inventoryLevelResponseTO = InventoryStockResponseMapper.getDetails(bapiFunction, destination);
			logger.info("Inventory Level response : " + inventoryLevelResponseTO + " for inventory level request :" + inventoryLevelRequestTO);
		} catch (JCoException e) {
			logger.error("Failed to connect Bapi Inventory Stock for request: " + inventoryLevelRequestTO, e);
		} catch (IOException e) {
			logger.error("Failed to connect Bapi Inventory Stock for request: " + inventoryLevelRequestTO, e);
		}
		return inventoryLevelResponseTO;
	}
	
	@Override
	public SapLspAwbUpdateResponseTO processLspAwbUpdate(SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO) {
		logger.info("Awb Update Request : " + lspAwbUpdateRequestTO);
		SapLspAwbUpdateResponseTO lspAwbUpdateResponseTO = new SapLspAwbUpdateResponseTO();
		try {
			JCoDestination destination = sapClientConnector.connectSap(TinlaClient.valueOf(lspAwbUpdateRequestTO.getClient()));
			JCoFunction bapiFunction = destination.getRepository().getFunctionTemplate(BapiLspTemplate.ZFB_DELV_UPDT_AWB.toString()).getFunction();
			LspAwbUpdateMapper.setDetails(bapiFunction, lspAwbUpdateRequestTO);
			lspAwbUpdateResponseTO = LspAwbResponseMapper.getDetails(bapiFunction, destination);
			logger.info("Lsp Awb Update response : " + lspAwbUpdateResponseTO + " for lsp awb update request :" + lspAwbUpdateRequestTO);
		} catch (JCoException e) {
			logger.error("Failed to connect Bapi Awb Update for request: " + lspAwbUpdateRequestTO, e);
		} catch (IOException e) {
			logger.error("Failed to connect Bapi Awb Update for request  " + lspAwbUpdateRequestTO, e);
		}
		return lspAwbUpdateResponseTO;
	}

	@Override
	public SapResponseStatus sendIdoc(String idocXml) {
		logger.info("Sending Idoc..." + idocXml);
		try {
			sapClientConnector.sendIdoc(idocXml);
			return SapResponseStatus.SUCCESS;
		} catch (Exception e) {
			logger.error("Sending Idoc Failed", e);
			return SapResponseStatus.ERROR;
		}
	}
	
	public void setSapClientConnector(PlatformClientConnector sapClientConnector) {
		this.sapClientConnector = sapClientConnector;
	}
	
}
