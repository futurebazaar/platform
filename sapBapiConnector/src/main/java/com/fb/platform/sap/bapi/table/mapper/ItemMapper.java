package com.fb.platform.sap.bapi.table.mapper;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.table.TableType;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ItemMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> LineItemTOList, TinlaOrderType orderType) {
		Map<TableType, BapiTable> conditionTables = PlatformBapiHandlerFactory.getItemTables(orderType);
		JCoTable orderItemIN= bapiFunction.getTableParameterList().getTable(conditionTables.get(TableType.VALUE_TABLE).toString());
		JCoTable orderItemINX = bapiFunction.getTableParameterList().getTable(conditionTables.get(TableType.COMMIT_TABLE).toString());

		for (LineItemTO itemTO : LineItemTOList) {
			orderItemIN.appendRow();
			orderItemINX.appendRow();
			
			setCommonDetails(itemTO, orderHeaderTO, orderItemIN, orderItemINX);
			// Set New Order conditions
			if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
				setNewOrderDetails(itemTO, orderHeaderTO, orderItemIN, orderItemINX);
			} else {
				//set modification and Cancellation Conditions
				setUpdateOrderDetails(bapiFunction, itemTO, orderHeaderTO, orderItemIN, orderItemINX);
			}
			
			// set Common Conditions
			
						
		}
	}

	private static void setUpdateOrderDetails(JCoFunction bapiFunction, LineItemTO itemTO, OrderHeaderTO orderHeaderTO, JCoTable orderItemIN, JCoTable orderItemINX) {
		
		JCoTable changeIndicator = bapiFunction.getTableParameterList().getTable(BapiTable.CHANGE_INDICATOR.toString());
		changeIndicator.appendRow();
		changeIndicator.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		changeIndicator.setValue(SapConstants.CHANGE_TYPE, SapConstants.CHANGE_FLAG);
		changeIndicator.setValue( SapConstants.UPDATE_INDICATOR, SapConstants.UPDATE_FLAG);
		
		if (!StringUtils.isBlank(itemTO.getReasonCode()) && itemTO.getReasonCode().equals(SapConstants.CANCEL_FLAG)){
			orderItemIN.setValue(SapConstants.REJECTION_REASON, itemTO.getReasonCode());
			orderItemINX.setValue(SapConstants.COMMIT_FLAG, itemTO.getReasonCode());
			changeIndicator.setValue( SapConstants.UPDATE_INDICATOR, SapConstants.CANCEL_FLAG);
			
		}
		
		orderItemINX.setValue(SapConstants.OPERATION_FLAG, SapConstants.UPDATE_FLAG);
		
	}

	private static void setNewOrderDetails(LineItemTO itemTO, OrderHeaderTO orderHeaderTO, JCoTable orderItemIN, JCoTable orderItemINX) {
		
		orderItemIN.setValue(SapConstants.ITEM_CATEGORY, SapConstants.DEFAULT_ITEM_CATEGORY);
		if (!StringUtils.isBlank(itemTO.getItemCategory())) {
			orderItemIN.setValue(SapConstants.ITEM_CATEGORY, itemTO.getItemCategory());
		}
		orderItemINX.setValue(SapConstants.ITEM_CATEGORY, SapConstants.COMMIT_FLAG);
		
		orderItemIN.setValue(SapConstants.DESCRIPTION, itemTO.getDescription());
		orderItemINX.setValue(SapConstants.DESCRIPTION, SapConstants.COMMIT_FLAG);
		
		orderItemIN.setValue(SapConstants.COMP_QUANT, itemTO.getItemCategory());
		orderItemINX.setValue(SapConstants.COMP_QUANT, SapConstants.COMMIT_FLAG);
		
		orderItemIN.setValue(SapConstants.SHIPPING_MODE, itemTO.getShippingMode());
		orderItemINX.setValue(SapConstants.SHIPPING_MODE, SapConstants.COMMIT_FLAG);
		orderItemINX.setValue(SapConstants.REFERENCE_FIELD, SapConstants.COMMIT_FLAG);
		
		orderItemINX.setValue(SapConstants.SHIPMENT_TYPE, SapConstants.COMMIT_FLAG);
		orderItemINX.setValue(SapConstants.DELIVERY_PRIORITY, SapConstants.COMMIT_FLAG);
		
		orderItemINX.setValue(SapConstants.OPERATION_FLAG, SapConstants.INSERT_FLAG);		
		
	}

	private static void setCommonDetails(LineItemTO itemTO, 	OrderHeaderTO orderHeaderTO, JCoTable orderItemIN, JCoTable orderItemINX) {
		String billDate = orderHeaderTO.getCreatedOn().getYear() + "-" + orderHeaderTO.getCreatedOn().getMonthOfYear() + "-" + orderHeaderTO.getCreatedOn().getDayOfMonth();
		
		orderItemIN.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		orderItemINX.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		
		orderItemIN.setValue( SapConstants.ARTICLE_ID, itemTO.getArticleID());
		orderItemINX.setValue(SapConstants.ARTICLE_ID, SapConstants.COMMIT_FLAG);

		orderItemIN.setValue(SapConstants.QUANTITY, itemTO.getQuantity().toString());
		orderItemINX.setValue(SapConstants.QUANTITY, SapConstants.COMMIT_FLAG);
		
		orderItemIN.setValue(SapConstants.BILLING_DATE, billDate);
		orderItemINX.setValue(SapConstants.BILLING_DATE, SapConstants.COMMIT_FLAG);
		
		orderItemIN.setValue(SapConstants.RELATIONSHIP_ARTICLE_ID, itemTO.getRelationshipArticleID());
		orderItemINX.setValue(SapConstants.RELATIONSHIP_ARTICLE_ID, SapConstants.COMMIT_FLAG);
		
		orderItemIN.setValue( SapConstants.PLANT, itemTO.getPlantId());
		orderItemINX.setValue(SapConstants.PLANT, SapConstants.COMMIT_FLAG);
		
		orderItemIN.setValue(SapConstants.STORAGE_LOCATION, itemTO.getStorageLocation());
		orderItemINX.setValue(SapConstants.STORAGE_LOCATION, SapConstants.COMMIT_FLAG);
		
		orderItemIN.setValue(SapConstants.SALES_UNIT, itemTO.getSalesUnit());
		orderItemINX.setValue(SapConstants.SALES_UNIT, SapConstants.COMMIT_FLAG);
		
	}
	
	public static void setReturnItemDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> LineItemTOList, TinlaOrderType orderType) {
		JCoTable returnItem = bapiFunction.getTableParameterList().getTable(BapiTable.RETURN_ITEM.toString());
		for (LineItemTO itemTO : LineItemTOList) {
			returnItem.appendRow();
			returnItem.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			returnItem.setValue(SapConstants.QUANTITY, itemTO.getQuantity().toString());
			returnItem.setValue(SapConstants.PLANT, itemTO.getPlantId());
			returnItem.setValue(SapConstants.STORAGE_LOCATION, itemTO.getStorageLocation());
		}
	}
	
}
