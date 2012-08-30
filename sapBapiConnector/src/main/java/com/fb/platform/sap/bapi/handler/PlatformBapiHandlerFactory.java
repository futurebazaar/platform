package com.fb.platform.sap.bapi.handler;

import java.util.HashMap;
import java.util.Map;

import com.fb.platform.sap.bapi.BapiTemplate;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.table.TableType;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.ItemConditionsType;
import com.fb.platform.sap.bapi.utils.SapConstants;

public class PlatformBapiHandlerFactory {
	
	public static BapiTemplate getTemplate(TinlaOrderType orderType) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			return BapiTemplate.ZATG_BAPI_SO_FB_FECIL_CHNG_NEW;
		} else if (orderType.equals(TinlaOrderType.RET_ORDER)) {
			return BapiTemplate.ZCUST_RETURN_ORDER_CREATE;
		}
		return  BapiTemplate.ZATG_BAPI_FBILFECIL_SO_CR8_NEW;
	}
	
	public static Map<TableType, BapiTable> getConditionTables(TinlaOrderType orderType) {
		Map<TableType, BapiTable> tableMap = new HashMap<TableType, BapiTable>();
		if (!orderType.equals(TinlaOrderType.NEW_ORDER)) {
			tableMap.put(TableType.VALUE_TABLE, BapiTable.CONDITIONS_IN );
			tableMap.put(TableType.COMMIT_TABLE, BapiTable.CONDITIONS_INX);
			return tableMap;
		}
		tableMap.put(TableType.VALUE_TABLE, BapiTable.ORDER_CONDITIONS_IN );
		tableMap.put(TableType.COMMIT_TABLE, BapiTable.ORDER_CONDITIONS_INX);
		return tableMap;
	}
		
	public static Map<TableType, BapiTable> getScheduleTables(TinlaOrderType orderType) {
		Map<TableType, BapiTable> tableMap = new HashMap<TableType, BapiTable>();
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			tableMap.put(TableType.VALUE_TABLE, BapiTable.SCHEDULE_LINES );
			tableMap.put(TableType.COMMIT_TABLE, BapiTable.SCHEDULE_LINESX);
			return tableMap;
		}
		else {
			tableMap.put(TableType.VALUE_TABLE, BapiTable.ORDER_SCHEDULES_IN );
			tableMap.put(TableType.COMMIT_TABLE, BapiTable.ORDER_SCHEDULES_INX);
			return tableMap;
		}
	}
	
	public static Map<TableType, BapiTable> getItemTables(TinlaOrderType orderType) {
		Map<TableType, BapiTable> tableMap = new HashMap<TableType, BapiTable>();
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			tableMap.put(TableType.VALUE_TABLE, BapiTable.ORDER_ITEM_IN);
			tableMap.put(TableType.COMMIT_TABLE, BapiTable.ORDER_ITEM_INX);
			return tableMap;
		}
		else {
			tableMap.put(TableType.VALUE_TABLE, BapiTable.ORDER_ITEMS_IN );
			tableMap.put(TableType.COMMIT_TABLE, BapiTable.ORDER_ITEMS_INX);
			return tableMap;
		}
	}
	
	public static String getSalesDocument(TinlaOrderType orderType) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			return SapConstants.SALES_DOCUMENT;
		}
		return SapConstants.INPUT_SALES_DOCUMENT;
	}
	
	public static BapiTable getPartnersTable(TinlaOrderType orderType) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			return BapiTable.PARTNERS;
		}
		return BapiTable.ORDER_PARTNERS;
	}
	
	public static Map<String, String> conditionValueMap(ItemConditionsType conditionType) {
		Map<String, String> conditionValueMap = new HashMap<String, String>();
		if (conditionType.equals(ItemConditionsType.COUPON_CONDITION_TYPE)) {
			conditionValueMap.put(SapConstants.CONDITION_STEP_NUMBER, SapConstants.COUPON_STEP_NUMBER);
			conditionValueMap.put(SapConstants.CONDITION_COUNTER, SapConstants.DEFAULT_CONDITION_COUNTER);
		}
		if (conditionType.equals(ItemConditionsType.ITEM_DISCOUNT_CONDITION_TYPE)) {
			conditionValueMap.put(SapConstants.CONDITION_STEP_NUMBER, SapConstants.ITEM_DISCOUNT_STEP_NUMBER);
			conditionValueMap.put(SapConstants.CONDITION_COUNTER, SapConstants.DEFAULT_CONDITION_COUNTER);
		}
		if (conditionType.equals(ItemConditionsType.ITZ_CONDITION_TYPE)) {
			conditionValueMap.put(SapConstants.CONDITION_STEP_NUMBER, SapConstants.ITZ_STEP_NUMBER);
			conditionValueMap.put(SapConstants.CONDITION_COUNTER, SapConstants.DEFAULT_CONDITION_COUNTER);
		}
		if (conditionType.equals(ItemConditionsType.SHIPPING_CONDITION_TYPE)) {
			conditionValueMap.put(SapConstants.CONDITION_STEP_NUMBER, SapConstants.SHIPPING_STEP_NUMBER);
			conditionValueMap.put(SapConstants.CONDITION_COUNTER, SapConstants.DEFAULT_CONDITION_COUNTER);
		}
		if (conditionType.equals(ItemConditionsType.MRP_CONDITION_TYPE)) {
			conditionValueMap.put(SapConstants.CONDITION_STEP_NUMBER, SapConstants.MRP_STEP_NUMBER);
			conditionValueMap.put(SapConstants.CONDITION_COUNTER, SapConstants.DEFAULT_CONDITION_COUNTER);
		}
		if (conditionType.equals(ItemConditionsType.LIST_CONDITION_TYPE)) {
			conditionValueMap.put(SapConstants.CONDITION_STEP_NUMBER, SapConstants.LIST_STEP_NUMBER);
			conditionValueMap.put(SapConstants.CONDITION_COUNTER, SapConstants.DEFAULT_CONDITION_COUNTER);
		}
		if (conditionType.equals(ItemConditionsType.SALES_CONDITION_TYPE)) {
			conditionValueMap.put(SapConstants.CONDITION_STEP_NUMBER, SapConstants.SALES_STEP_NUMBER);
			conditionValueMap.put(SapConstants.CONDITION_COUNTER, SapConstants.DEFAULT_CONDITION_COUNTER);
		}
		if (conditionType.equals(ItemConditionsType.WARRANTY_CONDITION_TYPE)) {
			conditionValueMap.put(SapConstants.CONDITION_STEP_NUMBER, SapConstants.WARRANTY_STEP_NUMBER);
			conditionValueMap.put(SapConstants.CONDITION_COUNTER, SapConstants.DEFAULT_CONDITION_COUNTER);
		}
		if (conditionType.equals(ItemConditionsType.NLC_CONDITION_TYPE)) {
			conditionValueMap.put(SapConstants.CONDITION_STEP_NUMBER, SapConstants.NLC_STEP_NUMBER);
			conditionValueMap.put(SapConstants.CONDITION_COUNTER, SapConstants.DEFAULT_CONDITION_COUNTER);
		}

		return conditionValueMap;
	}
}
