package com.fb.platform.sap.bapi.handler;

import java.util.HashMap;
import java.util.Map;

import com.fb.platform.sap.bapi.BapiTemplate;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.table.TableMapperType;
import com.fb.platform.sap.bapi.table.TableType;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapConstants;

public class PlatformBapiHandlerFactory {
	
	public static BapiTemplate getTemplate(TinlaOrderType orderType) {
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			return BapiTemplate.ZATG_BAPI_FBILFECIL_SO_CR8_NEW;
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
}
