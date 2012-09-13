package com.fb.platform.sap.bapi.order.table.mapper;

import java.util.List;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.fb.platform.sap.bapi.utils.SapOrderConstants;
import com.fb.platform.sap.bapi.utils.SapUtils;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;

public class HeaderMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, TinlaOrderType orderType) {
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

	private static void setUpdateOrderDetails(OrderHeaderTO orderHeaderTO,
			JCoStructure orderHeaderIN, JCoStructure orderHeaderINX, String billDate) {
		
		orderHeaderINX.setValue( SapOrderConstants.OPERATION_FLAG, SapOrderConstants.UPDATE_FLAG);
		
	}

	private static void setNewOrderDetails(OrderHeaderTO orderHeaderTO,
			JCoStructure orderHeaderIN, JCoStructure orderHeaderINX, String billDate) {
		
		orderHeaderIN.setValue(SapOrderConstants.REFERENCE_DOCUMENT_CATEGORY, SapOrderConstants.REFERENCE_DOCUMENT_FLAG);
		orderHeaderINX.setValue(SapOrderConstants.REFERENCE_DOCUMENT_CATEGORY, SapOrderConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapOrderConstants.DOCUMENT_TYPE, orderHeaderTO.getSalesDocType());
		orderHeaderINX.setValue(SapOrderConstants.DOCUMENT_TYPE, SapOrderConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapOrderConstants.THIRD_PARTY_ORDER, orderHeaderTO.getThirdPartyOrder());
		orderHeaderINX.setValue(SapOrderConstants.THIRD_PARTY_ORDER, SapOrderConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapOrderConstants.CURRENCY, orderHeaderTO.getPricingTO().getCurrency());
		orderHeaderINX.setValue(SapOrderConstants.CURRENCY, SapOrderConstants.COMMIT_FLAG);
		
		// This should be set as BP Number?
		orderHeaderIN.setValue(SapOrderConstants.SALES_ORGANIZATION, SapOrderConstants.FBIL_SALES_ORGANIZATION);
		orderHeaderINX.setValue(SapOrderConstants.SALES_ORGANIZATION, SapOrderConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapOrderConstants.DISTRIBUTION_CHANNEL, SapOrderConstants.DEFAULT_DISTRIBUTION_CHANNEL);
		orderHeaderINX.setValue(SapOrderConstants.DISTRIBUTION_CHANNEL, SapOrderConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapOrderConstants.DIVISION, SapOrderConstants.DEFAULT_DIVISION);
		orderHeaderINX.setValue(SapOrderConstants.DIVISION, SapOrderConstants.COMMIT_FLAG);
		
		// orderHeaderINTable.setValue(FBConstants.Z1, "CUST_GROUP");
		orderHeaderIN.setValue(SapOrderConstants.CUSTOMER_GROUP, SapOrderConstants.FB_CUSTOMER_GROUP);
		orderHeaderINX.setValue(SapOrderConstants.CUSTOMER_GROUP, SapOrderConstants.COMMIT_FLAG);
		
		//orderHeaderIN.setValue(SapConstants.BLOCK_DELIVERY, "");
		//orderHeaderINX.setValue(SapConstants.COMMIT_FLAG, SapConstants.BLOCK_DELIVERY);
		
		orderHeaderIN.setValue(SapOrderConstants.CHANNEL_TYPE, orderHeaderTO.getChannelType());
		orderHeaderINX.setValue(SapOrderConstants.CLIENT_NAME, SapOrderConstants.COMMIT_FLAG);

		// CHeck what are these
		orderHeaderIN.setValue(SapOrderConstants.REFERENCE_FIELD, orderHeaderTO.getClient());
		orderHeaderINX.setValue(SapOrderConstants.REFERENCE_FIELD, SapOrderConstants.COMMIT_FLAG);

		//Figure out if this is required
		//orderHeaderINX.setValue(SapConstants.COMMIT_FLAG, SapConstants.PRIMARY_PHONE);

		// setting default paymentTerms
		if (SapOrderConstants.COD_ACCOUNT_NUMBER.equals(orderHeaderTO.getAccountNumber())) {
			orderHeaderIN.setValue(SapOrderConstants.PAYMENT_TERM, SapOrderConstants.COD_PAYMENT_TERM);
		} else {
			orderHeaderIN.setValue(SapOrderConstants.PAYMENT_TERM, SapOrderConstants.DEFAULT_PAYMENT_TERM);
		}
		orderHeaderINX.setValue(SapOrderConstants.PAYMENT_TERM, SapOrderConstants.COMMIT_FLAG);
		
		orderHeaderINX.setValue(SapOrderConstants.REF_1_S, SapOrderConstants.COMMIT_FLAG);
		orderHeaderINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.INSERT_FLAG);
		
	}

	private static void setCommonDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO,
			JCoStructure orderHeaderIN, JCoStructure orderHeaderINX, String billDate, TinlaOrderType orderType) {
		
		bapiFunction.getImportParameterList().setValue(PlatformBapiHandlerFactory.getSalesDocument(orderType), orderHeaderTO.getReferenceID());
		orderHeaderIN.setValue(SapOrderConstants.DOCUMENT_DATE, billDate);
		orderHeaderINX.setValue(SapOrderConstants.DOCUMENT_DATE, SapOrderConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapOrderConstants.REQUIRED_DATE, billDate);
		orderHeaderINX.setValue(SapOrderConstants.REQUIRED_DATE, SapOrderConstants.COMMIT_FLAG);
		
	}
	
	public static void setReturnDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> lineItemTOList, TinlaOrderType orderType) {
		
		bapiFunction.getImportParameterList().setValue(PlatformBapiHandlerFactory.getSalesDocument(orderType), orderHeaderTO.getReturnOrderID());
		bapiFunction.getImportParameterList().setValue(SapOrderConstants.DOCUMENT_TYPE, SapOrderConstants.RETURN_ORDER_TYPE);
		bapiFunction.getImportParameterList().setValue(SapOrderConstants.REFERENCE_DOCUMENT, orderHeaderTO.getReferenceID());
		bapiFunction.getImportParameterList().setValue(SapOrderConstants.ORDER_REASON, lineItemTOList.get(0).getReasonCode());
	}
}
