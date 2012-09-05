package com.fb.platform.sap.bapi.table.mapper;

import java.util.List;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.fb.platform.sap.bapi.utils.SapUtils;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;

public class HeaderMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, TinlaOrderType orderType) {
		JCoStructure orderHeaderIN = bapiFunction.getImportParameterList().getStructure(BapiTable.ORDER_HEADER_IN.toString());
		JCoStructure orderHeaderINX = bapiFunction.getImportParameterList().getStructure(BapiTable.ORDER_HEADER_INX.toString());

		String billDate = SapUtils.convertDateToFormat(orderHeaderTO.getCreatedOn(), "yyyMMdd");
		
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
		
		orderHeaderINX.setValue( SapConstants.OPERATION_FLAG, SapConstants.UPDATE_FLAG);
		
	}

	private static void setNewOrderDetails(OrderHeaderTO orderHeaderTO,
			JCoStructure orderHeaderIN, JCoStructure orderHeaderINX, String billDate) {
		
		orderHeaderIN.setValue(SapConstants.REFERENCE_DOCUMENT_CATEGORY, SapConstants.REFERENCE_DOCUMENT_FLAG);
		orderHeaderINX.setValue(SapConstants.REFERENCE_DOCUMENT_CATEGORY, SapConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapConstants.DOCUMENT_TYPE, orderHeaderTO.getSalesDocType());
		orderHeaderINX.setValue(SapConstants.DOCUMENT_TYPE, SapConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapConstants.THIRD_PARTY_ORDER, orderHeaderTO.getThirdPartyOrder());
		orderHeaderINX.setValue(SapConstants.THIRD_PARTY_ORDER, SapConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapConstants.CURRENCY, orderHeaderTO.getPricingTO().getCurrency());
		orderHeaderINX.setValue(SapConstants.CURRENCY, SapConstants.COMMIT_FLAG);
		
		// This should be set as BP Number?
		orderHeaderIN.setValue(SapConstants.SALES_ORGANIZATION, SapConstants.FBIL_SALES_ORGANIZATION);
		orderHeaderINX.setValue(SapConstants.SALES_ORGANIZATION, SapConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapConstants.DISTRIBUTION_CHANNEL, SapConstants.DEFAULT_DISTRIBUTION_CHANNEL);
		orderHeaderINX.setValue(SapConstants.DISTRIBUTION_CHANNEL, SapConstants.COMMIT_FLAG);
		
		orderHeaderIN.setValue(SapConstants.DIVISION, SapConstants.DEFAULT_DIVISION);
		orderHeaderINX.setValue(SapConstants.DIVISION, SapConstants.COMMIT_FLAG);
		
		// orderHeaderINTable.setValue(FBConstants.Z1, "CUST_GROUP");
		orderHeaderIN.setValue(SapConstants.CUSTOMER_GROUP, SapConstants.FB_CUSTOMER_GROUP);
		orderHeaderINX.setValue(SapConstants.CUSTOMER_GROUP, SapConstants.COMMIT_FLAG);
		
		//orderHeaderIN.setValue(SapConstants.BLOCK_DELIVERY, "");
		//orderHeaderINX.setValue(SapConstants.COMMIT_FLAG, SapConstants.BLOCK_DELIVERY);
		
		orderHeaderIN.setValue(SapConstants.CHANNEL_TYPE, orderHeaderTO.getChannelType());
		orderHeaderINX.setValue(SapConstants.CLIENT_NAME, SapConstants.COMMIT_FLAG);

		// CHeck what are these
		orderHeaderIN.setValue(SapConstants.REFERENCE_FIELD, orderHeaderTO.getClient());
		orderHeaderINX.setValue(SapConstants.REFERENCE_FIELD, SapConstants.COMMIT_FLAG);

		//Figure out if this is required
		//orderHeaderINX.setValue(SapConstants.COMMIT_FLAG, SapConstants.PRIMARY_PHONE);

		// setting default paymentTerms
		if (SapConstants.COD_ACCOUNT_NUMBER.equals(orderHeaderTO.getAccountNumber())) {
			orderHeaderIN.setValue(SapConstants.PAYMENT_TERM, SapConstants.COD_PAYMENT_TERM);
		} else {
			orderHeaderIN.setValue(SapConstants.PAYMENT_TERM, SapConstants.DEFAULT_PAYMENT_TERM);
		}
		orderHeaderINX.setValue(SapConstants.PAYMENT_TERM, SapConstants.COMMIT_FLAG);
		
		orderHeaderINX.setValue(SapConstants.REF_1_S, SapConstants.COMMIT_FLAG);
		orderHeaderINX.setValue(SapConstants.OPERATION_FLAG, SapConstants.INSERT_FLAG);
		
	}

	private static void setCommonDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO,
			JCoStructure orderHeaderIN, JCoStructure orderHeaderINX, String billDate, TinlaOrderType orderType) {
		
		bapiFunction.getImportParameterList().setValue(PlatformBapiHandlerFactory.getSalesDocument(orderType), orderHeaderTO.getReferenceID());
		orderHeaderIN.setValue(SapConstants.DOCUMENT_DATE, billDate);
		orderHeaderINX.setValue(SapConstants.DOCUMENT_DATE, SapConstants.COMMIT_FLAG);
		orderHeaderIN.setValue(SapConstants.REQUIRED_DATE, billDate);
		orderHeaderINX.setValue(SapConstants.REQUIRED_DATE, SapConstants.COMMIT_FLAG);
		
	}
	
	public static void setReturnDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> lineItemTOList, TinlaOrderType orderType) {
		
		bapiFunction.getImportParameterList().setValue(PlatformBapiHandlerFactory.getSalesDocument(orderType), orderHeaderTO.getReturnOrderID());
		bapiFunction.getImportParameterList().setValue(SapConstants.DOCUMENT_TYPE, SapConstants.RETURN_ORDER_TYPE);
		bapiFunction.getImportParameterList().setValue(SapConstants.REFERENCE_DOCUMENT, orderHeaderTO.getReferenceID());
		bapiFunction.getImportParameterList().setValue(SapConstants.ORDER_REASON, lineItemTOList.get(0).getReasonCode());
	}
}
