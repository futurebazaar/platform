package com.fb.platform.sap.bapi.factory;

import java.util.HashMap;
import java.util.Map;

import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.TinlaClient;

public class BapiTableFactory {

	public static Map<OrderTableType, BapiOrderTable> getConditionTables(TinlaOrderType orderType, TinlaClient client) {
		Map<OrderTableType, BapiOrderTable> tableMap = new HashMap<OrderTableType, BapiOrderTable>();
		if (!orderType.equals(TinlaOrderType.NEW_ORDER) && client.equals(TinlaClient.FUTUREBAZAAR)) {
			tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.CONDITIONS_IN );
			tableMap.put(OrderTableType.COMMIT_TABLE, BapiOrderTable.CONDITIONS_INX);
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
	
	public static String getSalesDocument(TinlaOrderType orderType, TinlaClient client) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER) && client.equals(TinlaClient.FUTUREBAZAAR)) {
			return SapOrderConstants.SALES_DOCUMENT;
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
