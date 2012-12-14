package com.fb.platform.sap.bapi.order.table.mapper;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.client.commons.SapConstants;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.commons.TinlaClient;
import com.fb.platform.sap.bapi.factory.BapiTableFactory;
import com.fb.platform.sap.bapi.factory.SapOrderConfigFactory;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ItemMapper {
	
	private static Log logger = LogFactory.getLog(ItemMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> LineItemTOList, TinlaOrderType orderType) {
		logger.info("Setting Item Condition details for : " + orderType + " " + orderHeaderTO.getReferenceID());
		Map<OrderTableType, BapiOrderTable> conditionTables = BapiTableFactory.getItemTables(orderType, TinlaClient.valueOf(orderHeaderTO.getClient()));
		JCoTable orderItemIN= bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.VALUE_TABLE).toString());
		JCoTable orderItemINX = bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.COMMIT_TABLE).toString());
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_TEXT.toString());
		for (LineItemTO itemTO : LineItemTOList) {
			logger.info("Item : " + itemTO);
			orderItemIN.appendRow();
			orderItemINX.appendRow();
			orderText.appendRow();
			// Set New Order conditions
			if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
				setNewOrderDetails(bapiFunction, itemTO, orderHeaderTO, orderItemIN, orderItemINX, orderText);
			} else {
				//set modification and Cancellation Conditions
				setUpdateOrderDetails(bapiFunction, itemTO, orderHeaderTO, orderItemIN, orderItemINX, orderType);
			}
			// set Common Conditions
			setCommonDetails(itemTO, orderHeaderTO, orderItemIN, orderItemINX);			
		}
	}

	private static void setUpdateOrderDetails(JCoFunction bapiFunction, LineItemTO itemTO, OrderHeaderTO orderHeaderTO, JCoTable orderItemIN, JCoTable orderItemINX, TinlaOrderType orderType) {
		JCoTable changeIndicator = bapiFunction.getTableParameterList().getTable(BapiOrderTable.CHANGE_INDICATOR.toString());
		changeIndicator.appendRow();
		changeIndicator.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		changeIndicator.setValue(SapOrderConstants.CHANGE_TYPE, SapOrderConstants.CHANGE_FLAG);
		changeIndicator.setValue( SapOrderConstants.UPDATE_INDICATOR, SapOrderConstants.UPDATE_FLAG);
		String operationCode = itemTO.getOperationCode();
		if (!StringUtils.isBlank(itemTO.getReasonCode()) && itemTO.getOperationCode().equals(SapOrderConstants.CANCEL_FLAG)) {
			orderItemIN.setValue(SapOrderConstants.REJECTION_REASON, itemTO.getReasonCode());
			orderItemINX.setValue(SapOrderConstants.REJECTION_REASON, SapOrderConstants.COMMIT_FLAG);
			operationCode = SapOrderConstants.UPDATE_FLAG;
		}
		changeIndicator.setValue( SapOrderConstants.UPDATE_INDICATOR, itemTO.getOperationCode());
		orderItemINX.setValue(SapOrderConstants.OPERATION_FLAG, operationCode);
	}

	private static void setNewOrderDetails(JCoFunction bapiFunction, LineItemTO itemTO, OrderHeaderTO orderHeaderTO, JCoTable orderItemIN, JCoTable orderItemINX, JCoTable orderText) {
		TinlaClient client = TinlaClient.valueOf(orderHeaderTO.getClient());
		
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		orderText.setValue(SapOrderConstants.TEXT_LINE, itemTO.getDescription());
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.ITEM_DESC_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		
		orderItemIN.setValue(SapOrderConstants.ITEM_CATEGORY, SapOrderConfigFactory.getConfigValue(SapOrderConstants.ITEM_CATEGORY,  client, TinlaOrderType.NEW_ORDER, orderHeaderTO));
		if (!StringUtils.isBlank(itemTO.getItemCategory())) {
			orderItemIN.setValue(SapOrderConstants.ITEM_CATEGORY, itemTO.getItemCategory());
			if (SapOrderConstants.GV_ITEM_CATEGORY.equals(itemTO.getItemCategory())) {
				orderText.appendRow();
				logger.info("Setting GV Text Details: " + itemTO.getGiftVoucherDetails());
				orderText.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
				orderText.setValue(SapOrderConstants.TEXT_LINE, itemTO.getGiftVoucherDetails());
				orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.THIRD_PARTNER_TEXT_ID);
				orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
			}
		}
		orderItemINX.setValue(SapOrderConstants.ITEM_CATEGORY, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.DESCRIPTION, itemTO.getDescription());
		orderItemINX.setValue(SapOrderConstants.DESCRIPTION, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.COMP_QUANT, itemTO.getQuantity());
		orderItemINX.setValue(SapOrderConstants.COMP_QUANT, SapOrderConstants.COMMIT_FLAG);
		orderItemINX.setValue(SapOrderConstants.REFERENCE_FIELD, SapOrderConstants.COMMIT_FLAG);
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
		
		if (!SapOrderConstants.GV_ITEM_CATEGORY.equals(itemTO.getItemCategory())) {
			orderItemIN.setValue(SapOrderConstants.STORAGE_LOCATION, itemTO.getStorageLocation());
			orderItemINX.setValue(SapOrderConstants.STORAGE_LOCATION, SapOrderConstants.COMMIT_FLAG);
		}
		orderItemIN.setValue(SapOrderConstants.SALES_UNIT, itemTO.getSalesUnit());
		orderItemINX.setValue(SapOrderConstants.SALES_UNIT, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.SHIPMENT_TYPE, itemTO.getShippingMode());
		orderItemINX.setValue(SapOrderConstants.SHIPMENT_TYPE, SapOrderConstants.COMMIT_FLAG);
		orderItemINX.setValue(SapOrderConstants.SALES_UNIT, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.HEADER_LSP, "");
		orderItemINX.setValue(SapOrderConstants.HEADER_LSP, SapOrderConstants.COMMIT_FLAG);
		orderItemIN.setValue(SapOrderConstants.REFERENCE_FIELD, orderHeaderTO.getClient());
		orderItemINX.setValue(SapOrderConstants.REFERENCE_FIELD, SapOrderConstants.COMMIT_FLAG);
	}
	
	public static void setReturnItemDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> LineItemTOList, TinlaOrderType orderType) {
		logger.info("Setting Item Condition details for : " + orderType + " " + orderHeaderTO.getReferenceID());
		JCoTable returnItem = bapiFunction.getTableParameterList().getTable(BapiTableFactory.getReturnItemTables(orderType, TinlaClient.valueOf(orderHeaderTO.getClient())).toString());
		for (LineItemTO itemTO : LineItemTOList) {
			logger.info("Item : " + itemTO);
			returnItem.appendRow();
			returnItem.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			returnItem.setValue(SapOrderConstants.QUANTITY, itemTO.getQuantity().toString());
			returnItem.setValue(SapConstants.PLANT, itemTO.getPlantId());
			returnItem.setValue(SapOrderConstants.STORAGE_LOCATION, itemTO.getStorageLocation());
			if (!TinlaClient.valueOf(orderHeaderTO.getClient()).equals(TinlaClient.FUTUREBAZAAR)) {
				returnItem.setValue(SapOrderConstants.RETURN_UNIT, itemTO.getSalesUnit());
			}
		}
	}

}
