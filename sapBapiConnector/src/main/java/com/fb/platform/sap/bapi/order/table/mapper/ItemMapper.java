package com.fb.platform.sap.bapi.order.table.mapper;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.commons.SapConstants;
import com.fb.platform.sap.bapi.commons.SapOrderConstants;
import com.fb.platform.sap.bapi.commons.SapUtils;
import com.fb.platform.sap.bapi.commons.TinlaClient;
import com.fb.platform.sap.bapi.factory.BapiTableFactory;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ItemMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> LineItemTOList, TinlaOrderType orderType) {
		Map<OrderTableType, BapiOrderTable> conditionTables = BapiTableFactory.getItemTables(orderType, TinlaClient.valueOf(orderHeaderTO.getClient()));
		JCoTable orderItemIN= bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.VALUE_TABLE).toString());
		JCoTable orderItemINX = bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.COMMIT_TABLE).toString());
		for (LineItemTO itemTO : LineItemTOList) {
			orderItemIN.appendRow();
			// Set New Order conditions
			if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
				setNewOrderDetails(itemTO, orderHeaderTO, orderItemIN, orderItemINX);
			} else {
				//set modification and Cancellation Conditions
				setUpdateOrderDetails(bapiFunction, itemTO, orderHeaderTO, orderItemIN, orderItemINX);
			}
			// set Common Conditions
			setCommonDetails(itemTO, orderHeaderTO, orderItemIN, orderItemINX);			
		}
	}

	private static void setUpdateOrderDetails(JCoFunction bapiFunction, LineItemTO itemTO, OrderHeaderTO orderHeaderTO, JCoTable orderItemIN, JCoTable orderItemINX) {
		JCoTable changeIndicator = bapiFunction.getTableParameterList().getTable(BapiOrderTable.CHANGE_INDICATOR.toString());
		changeIndicator.appendRow();
		changeIndicator.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		changeIndicator.setValue(SapOrderConstants.CHANGE_TYPE, SapOrderConstants.CHANGE_FLAG);
		changeIndicator.setValue( SapOrderConstants.UPDATE_INDICATOR, SapOrderConstants.UPDATE_FLAG);
		if (!StringUtils.isBlank(itemTO.getReasonCode()) && itemTO.getReasonCode().equals(SapOrderConstants.CANCEL_FLAG)){
			orderItemIN.setValue(SapOrderConstants.REJECTION_REASON, itemTO.getReasonCode());
			orderItemINX.setValue(SapOrderConstants.COMMIT_FLAG, itemTO.getReasonCode());
			changeIndicator.setValue( SapOrderConstants.UPDATE_INDICATOR, SapOrderConstants.CANCEL_FLAG);
		}
		orderItemINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.UPDATE_FLAG);
	}

	private static void setNewOrderDetails(LineItemTO itemTO, OrderHeaderTO orderHeaderTO, JCoTable orderItemIN, JCoTable orderItemINX) {
		orderItemIN.setValue(SapOrderConstants.ITEM_CATEGORY, SapOrderConstants.DEFAULT_ITEM_CATEGORY);
		if (!StringUtils.isBlank(itemTO.getItemCategory())) {
			orderItemIN.setValue(SapOrderConstants.ITEM_CATEGORY, itemTO.getItemCategory());
		}
		orderItemINX.setValue(SapOrderConstants.ITEM_CATEGORY, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.DESCRIPTION, itemTO.getDescription());
		orderItemINX.setValue(SapOrderConstants.DESCRIPTION, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.COMP_QUANT, itemTO.getItemCategory());
		orderItemINX.setValue(SapOrderConstants.COMP_QUANT, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.SHIPPING_MODE, itemTO.getShippingMode());
		orderItemINX.setValue(SapOrderConstants.SHIPPING_MODE, SapOrderConstants.COMMIT_FLAG);
		orderItemINX.setValue(SapOrderConstants.REFERENCE_FIELD, SapOrderConstants.COMMIT_FLAG);
		orderItemINX.setValue(SapOrderConstants.SHIPMENT_TYPE, SapOrderConstants.COMMIT_FLAG);
		orderItemINX.setValue(SapOrderConstants.DELIVERY_PRIORITY, SapOrderConstants.COMMIT_FLAG);
		orderItemINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.INSERT_FLAG);		
	}

	private static void setCommonDetails(LineItemTO itemTO, 	OrderHeaderTO orderHeaderTO, JCoTable orderItemIN, JCoTable orderItemINX) {
		String billDate = SapUtils.convertDateToFormat(orderHeaderTO.getCreatedOn(), SapConstants.DATE_FORMAT);
		orderItemIN.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		orderItemINX.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		orderItemIN.setValue( SapOrderConstants.ARTICLE_ID, itemTO.getArticleID());
		orderItemINX.setValue(SapOrderConstants.ARTICLE_ID, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.QUANTITY, itemTO.getQuantity().toString());
		orderItemINX.setValue(SapOrderConstants.QUANTITY, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.BILLING_DATE, billDate);
		orderItemINX.setValue(SapOrderConstants.BILLING_DATE, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.RELATIONSHIP_ARTICLE_ID, itemTO.getRelationshipArticleID());
		orderItemINX.setValue(SapOrderConstants.RELATIONSHIP_ARTICLE_ID, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue( SapConstants.PLANT, itemTO.getPlantId());
		orderItemINX.setValue(SapConstants.PLANT, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.STORAGE_LOCATION, itemTO.getStorageLocation());
		orderItemINX.setValue(SapOrderConstants.STORAGE_LOCATION, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.SALES_UNIT, itemTO.getSalesUnit());
		orderItemINX.setValue(SapOrderConstants.SALES_UNIT, SapOrderConstants.COMMIT_FLAG);
	}
	
	public static void setReturnItemDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> LineItemTOList, TinlaOrderType orderType) {
		JCoTable returnItem = bapiFunction.getTableParameterList().getTable(BapiOrderTable.RETURN_ITEM.toString());
		for (LineItemTO itemTO : LineItemTOList) {
			returnItem.appendRow();
			returnItem.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			returnItem.setValue(SapOrderConstants.QUANTITY, itemTO.getQuantity().toString());
			returnItem.setValue(SapConstants.PLANT, itemTO.getPlantId());
			returnItem.setValue(SapOrderConstants.STORAGE_LOCATION, itemTO.getStorageLocation());
		}
	}

}
