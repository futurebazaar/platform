package com.fb.platform.sap.bapi.factory;

import java.util.HashMap;
import java.util.Map;

import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.commons.TinlaClient;

public class BapiTableFactory {

	public static Map<OrderTableType, BapiOrderTable> getConditionTables(TinlaOrderType orderType, TinlaClient client) {
		Map<OrderTableType, BapiOrderTable> tableMap = new HashMap<OrderTableType, BapiOrderTable>();
		if (!orderType.equals(TinlaOrderType.NEW_ORDER) && client.equals(TinlaClient.FUTUREBAZAAR)) {
			tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.CONDITIONS_IN );
			tableMap.put(OrderTableType.COMMIT_TABLE, BapiOrderTable.CONDITIONS_INX);
			return tableMap;
		}
		if (orderType.equals(TinlaOrderType.RET_ORDER) && SapUtils.isBigBazaar(client)) {
			tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.RETURN_CONDITIONS);
			return tableMap;
		}
		tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.ORDER_CONDITIONS_IN );
		tableMap.put(OrderTableType.COMMIT_TABLE, BapiOrderTable.ORDER_CONDITIONS_INX);
		return tableMap;
	}
		
	public static Map<OrderTableType, BapiOrderTable> getScheduleTables(TinlaOrderType orderType, TinlaClient client) {
		Map<OrderTableType, BapiOrderTable> tableMap = new HashMap<OrderTableType, BapiOrderTable>();
		if (orderType.equals(TinlaOrderType.MOD_ORDER) && client.equals(TinlaClient.FUTUREBAZAAR)) {
			tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.SCHEDULE_LINES );
			tableMap.put(OrderTableType.COMMIT_TABLE, BapiOrderTable.SCHEDULE_LINESX);
			return tableMap;
		}
		else {
			tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.ORDER_SCHEDULES_IN );
			tableMap.put(OrderTableType.COMMIT_TABLE, BapiOrderTable.ORDER_SCHEDULES_INX);
			return tableMap;
		}
	}
	
	public static Map<OrderTableType, BapiOrderTable> getItemTables(TinlaOrderType orderType, TinlaClient client) {
		Map<OrderTableType, BapiOrderTable> tableMap = new HashMap<OrderTableType, BapiOrderTable>();
		if (orderType.equals(TinlaOrderType.MOD_ORDER) && client.equals(TinlaClient.FUTUREBAZAAR)) {
			tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.ORDER_ITEM_IN);
			tableMap.put(OrderTableType.COMMIT_TABLE, BapiOrderTable.ORDER_ITEM_INX);
			return tableMap;
		}
		else {
			tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.ORDER_ITEMS_IN );
			tableMap.put(OrderTableType.COMMIT_TABLE, BapiOrderTable.ORDER_ITEMS_INX);
			return tableMap;
		}
	}	
	
	public static BapiOrderTable getReturnItemTables(TinlaOrderType orderType, TinlaClient client) {
		Map<OrderTableType, BapiOrderTable> tableMap = new HashMap<OrderTableType, BapiOrderTable>();
		if (client.equals(TinlaClient.FUTUREBAZAAR)) {
			return BapiOrderTable.RETURN_ITEM;
		}
		return BapiOrderTable.RETURN_ITEMS;
	}	 
	
	public static String getSalesDocument(TinlaOrderType orderType, TinlaClient client) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER) && client.equals(TinlaClient.FUTUREBAZAAR)) {
			return SapOrderConstants.SALES_DOCUMENT;
		} else if (orderType.equals(TinlaOrderType.RET_ORDER) && client.equals(TinlaClient.BIGBAZAAR)) {
			return SapOrderConstants.RETURN_ORDER;
		}
		return SapOrderConstants.INPUT_SALES_DOCUMENT;
	}
	
	public static BapiOrderTable getPartnersTable(TinlaOrderType orderType, TinlaClient client) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER) && client.equals(TinlaClient.FUTUREBAZAAR)) {
			return BapiOrderTable.PARTNERS;
		}
		return BapiOrderTable.ORDER_PARTNERS;
	}
	
}