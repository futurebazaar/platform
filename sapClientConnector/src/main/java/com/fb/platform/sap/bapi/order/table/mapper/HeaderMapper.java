package com.fb.platform.sap.bapi.order.table.mapper;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.factory.BapiTableFactory;
import com.fb.platform.sap.bapi.factory.SapOrderConfigFactory;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.client.commons.SapConstants;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.commons.TinlaClient;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;

public class HeaderMapper {
	
	private static Log logger = LogFactory.getLog(HeaderMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, TinlaOrderType orderType) {
		logger.info("Setting Header details for : " + orderType + " " + orderHeaderTO.getReferenceID());
		logger.info("Header details are : " + orderHeaderTO);
		JCoStructure orderHeaderIN = bapiFunction.getImportParameterList().getStructure(BapiOrderTable.ORDER_HEADER_IN.toString());
		JCoStructure orderHeaderINX = bapiFunction.getImportParameterList().getStructure(BapiOrderTable.ORDER_HEADER_INX.toString());
		String billDate = SapUtils.convertDateToFormat(orderHeaderTO.getCreatedOn(), SapConstants.DATE_FORMAT);
		// common conditions
		setCommonDetails(bapiFunction, orderHeaderTO, orderHeaderIN, orderHeaderINX, billDate, orderType);
		if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
			setNewOrderDetails(orderHeaderTO, orderHeaderIN, orderHeaderINX, billDate);
		} else {
			setUpdateOrderDetails(orderHeaderTO, orderHeaderIN, orderHeaderINX, billDate);
		}
	}

	private static void setUpdateOrderDetails(OrderHeaderTO orderHeaderTO, JCoStructure orderHeaderIN, JCoStructure orderHeaderINX, String billDate) {
		orderHeaderINX.setValue( SapOrderConstants.OPERATION_FLAG, SapOrderConstants.UPDATE_FLAG);
	}

	private static void setNewOrderDetails(OrderHeaderTO orderHeaderTO, JCoStructure orderHeaderIN, JCoStructure orderHeaderINX, String billDate) {
		TinlaClient client = TinlaClient.valueOf(orderHeaderTO.getClient());
		orderHeaderIN.setValue(SapOrderConstants.REFERENCE_DOCUMENT_CATEGORY, SapOrderConstants.REFERENCE_DOCUMENT_FLAG);
		orderHeaderINX.setValue(SapOrderConstants.REFERENCE_DOCUMENT_CATEGORY, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.DOCUMENT_TYPE, orderHeaderTO.getSalesDocType());
		orderHeaderINX.setValue(SapOrderConstants.DOCUMENT_TYPE, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.THIRD_PARTY_ORDER, orderHeaderTO.getThirdPartyOrder());
		orderHeaderINX.setValue(SapOrderConstants.THIRD_PARTY_ORDER, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.CURRENCY, orderHeaderTO.getPricingTO().getCurrency());
		orderHeaderINX.setValue(SapOrderConstants.CURRENCY, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.SALES_ORGANIZATION, SapOrderConfigFactory.getConfigValue(SapOrderConstants.SALES_ORGANIZATION,  client, TinlaOrderType.NEW_ORDER));
		orderHeaderINX.setValue(SapOrderConstants.SALES_ORGANIZATION, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.DISTRIBUTION_CHANNEL, SapOrderConstants.DEFAULT_DISTRIBUTION_CHANNEL);
		orderHeaderINX.setValue(SapOrderConstants.DISTRIBUTION_CHANNEL, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.DIVISION, SapOrderConstants.DEFAULT_DIVISION);
		orderHeaderINX.setValue(SapOrderConstants.DIVISION, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.CUSTOMER_GROUP, SapOrderConfigFactory.getConfigValue(SapOrderConstants.CUSTOMER_GROUP,  client, TinlaOrderType.NEW_ORDER));
		orderHeaderINX.setValue(SapOrderConstants.CUSTOMER_GROUP, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.CHANNEL_TYPE, orderHeaderTO.getChannelType());
		orderHeaderINX.setValue(SapOrderConstants.CLIENT_NAME, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.REFERENCE_FIELD, orderHeaderTO.getClient());
		orderHeaderINX.setValue(SapOrderConstants.REFERENCE_FIELD, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.HEADER_LSP, SapOrderConfigFactory.getConfigValue(SapOrderConstants.HEADER_LSP,  client, TinlaOrderType.NEW_ORDER));
		orderHeaderINX.setValue(SapOrderConstants.HEADER_LSP, SapOrderConstants.COMMIT_FLAG);
		// setting default paymentTerms
		if (SapUtils.isBigBazaar(client) || SapOrderConstants.COD_ACCOUNT_NUMBER.equals(orderHeaderTO.getAccountNumber())) {
			orderHeaderIN.setValue(SapOrderConstants.PAYMENT_TERM, SapOrderConstants.COD_PAYMENT_TERM);
		} else {
			orderHeaderIN.setValue(SapOrderConstants.PAYMENT_TERM, SapOrderConstants.DEFAULT_PAYMENT_TERM);
		}
		orderHeaderINX.setValue(SapOrderConstants.PAYMENT_TERM, SapOrderConstants.COMMIT_FLAG);
		orderHeaderINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.INSERT_FLAG);
		
	}

	private static void setCommonDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, JCoStructure orderHeaderIN, JCoStructure orderHeaderINX, String billDate, TinlaOrderType orderType) {
		bapiFunction.getImportParameterList().setValue(BapiTableFactory.getSalesDocument(orderType, TinlaClient.valueOf(orderHeaderTO.getClient())), orderHeaderTO.getReferenceID());
		orderHeaderIN.setValue(SapOrderConstants.DOCUMENT_DATE, billDate);
		orderHeaderINX.setValue(SapOrderConstants.DOCUMENT_DATE, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.REQUIRED_DATE, billDate);
		orderHeaderINX.setValue(SapOrderConstants.REQUIRED_DATE, SapOrderConstants.COMMIT_FLAG);
		
	}
	
	public static void setReturnDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> lineItemTOList, TinlaOrderType orderType) {
		logger.info("Setting Header Return details for : " + orderType + " " + orderHeaderTO.getReferenceID());
		logger.info("Header Return details are : " + orderHeaderTO);
		bapiFunction.getImportParameterList().setValue(BapiTableFactory.getSalesDocument(orderType, TinlaClient.valueOf(orderHeaderTO.getClient())), orderHeaderTO.getReturnOrderID());
		bapiFunction.getImportParameterList().setValue(SapOrderConstants.DOCUMENT_TYPE, SapOrderConstants.RETURN_ORDER_TYPE);
		bapiFunction.getImportParameterList().setValue(SapOrderConstants.REFERENCE_DOCUMENT, orderHeaderTO.getReferenceID());
		bapiFunction.getImportParameterList().setValue(SapOrderConstants.ORDER_REASON, lineItemTOList.get(0).getReasonCode());
		
	}
}
