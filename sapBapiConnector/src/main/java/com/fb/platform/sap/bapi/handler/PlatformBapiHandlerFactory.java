package com.fb.platform.sap.bapi.handler;

import java.util.HashMap;
import java.util.Map;
import com.fb.platform.sap.bapi.order.BapiOrderTemplate;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.ItemConditionsType;
import com.fb.platform.sap.bapi.utils.SapOrderConstants;

public class PlatformBapiHandlerFactory {
	
	public static BapiOrderTemplate getTemplate(TinlaOrderType orderType) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			return BapiOrderTemplate.ZATG_BAPI_SO_FB_FECIL_CHNG_NEW;
		} else if (orderType.equals(TinlaOrderType.RET_ORDER)) {
			return BapiOrderTemplate.ZCUST_RETURN_ORDER_CREATE;
		}
		return  BapiOrderTemplate.ZATG_BAPI_FBILFECIL_SO_CR8_NEW;
	}
	
	public static Map<OrderTableType, BapiOrderTable> getConditionTables(TinlaOrderType orderType) {
		Map<OrderTableType, BapiOrderTable> tableMap = new HashMap<OrderTableType, BapiOrderTable>();
		if (!orderType.equals(TinlaOrderType.NEW_ORDER)) {
			tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.CONDITIONS_IN );
			tableMap.put(OrderTableType.COMMIT_TABLE, BapiOrderTable.CONDITIONS_INX);
			return tableMap;
		}
		tableMap.put(OrderTableType.VALUE_TABLE, BapiOrderTable.ORDER_CONDITIONS_IN );
		tableMap.put(OrderTableType.COMMIT_TABLE, BapiOrderTable.ORDER_CONDITIONS_INX);
		return tableMap;
	}
		
	public static Map<OrderTableType, BapiOrderTable> getScheduleTables(TinlaOrderType orderType) {
		Map<OrderTableType, BapiOrderTable> tableMap = new HashMap<OrderTableType, BapiOrderTable>();
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
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
	
	public static Map<OrderTableType, BapiOrderTable> getItemTables(TinlaOrderType orderType) {
		Map<OrderTableType, BapiOrderTable> tableMap = new HashMap<OrderTableType, BapiOrderTable>();
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
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
	
	public static String getSalesDocument(TinlaOrderType orderType) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			return SapOrderConstants.SALES_DOCUMENT;
		}
		return SapOrderConstants.INPUT_SALES_DOCUMENT;
	}
	
	public static BapiOrderTable getPartnersTable(TinlaOrderType orderType) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			return BapiOrderTable.PARTNERS;
		}
		return BapiOrderTable.ORDER_PARTNERS;
	}
	
	public static Map<String, String> conditionValueMap(ItemConditionsType conditionType) {
		Map<String, String> conditionValueMap = new HashMap<String, String>();
		if (conditionType.equals(ItemConditionsType.COUPON_CONDITION_TYPE)) {
			conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.COUPON_STEP_NUMBER);
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
			conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.COUPON_CONDITION_TYPE);
		}
		if (conditionType.equals(ItemConditionsType.ITEM_DISCOUNT_CONDITION_TYPE)) {
			conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.ITEM_DISCOUNT_STEP_NUMBER);
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
			conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.ITEM_DISCOUNT_CONDITION_TYPE);
		}
		if (conditionType.equals(ItemConditionsType.ITZ_CONDITION_TYPE)) {
			conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.ITZ_STEP_NUMBER);
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
			conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.ITZ_CONDITION_TYPE);
		}
		if (conditionType.equals(ItemConditionsType.SHIPPING_CONDITION_TYPE)) {
			conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.SHIPPING_STEP_NUMBER);
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
			conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.SHIPPING_CONDITION_TYPE);
		}
		if (conditionType.equals(ItemConditionsType.MRP_CONDITION_TYPE)) {
			conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.MRP_STEP_NUMBER);
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
			conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.MRP_CONDITION_TYPE);
		}
		if (conditionType.equals(ItemConditionsType.LIST_CONDITION_TYPE)) {
			conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.LIST_STEP_NUMBER);
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
			conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.OFFER_PRICE_CONDITION_TYPE);
		}
		if (conditionType.equals(ItemConditionsType.SALES_CONDITION_TYPE)) {
			conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.SALES_STEP_NUMBER);
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
			conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.SALES_PRICE_CONDITION_TYPE);
		}
		if (conditionType.equals(ItemConditionsType.WARRANTY_CONDITION_TYPE)) {
			conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.WARRANTY_STEP_NUMBER);
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
			conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.WARRANTY_CONDITION_TYPE);
		}
		if (conditionType.equals(ItemConditionsType.NLC_CONDITION_TYPE)) {
			conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.NLC_STEP_NUMBER);
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
			conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.NLC_CONDITION_TYPE);
		}

		return conditionValueMap;
	}
}
