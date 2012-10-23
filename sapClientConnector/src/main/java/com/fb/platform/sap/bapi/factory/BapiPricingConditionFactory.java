package com.fb.platform.sap.bapi.factory;

import java.util.HashMap;
import java.util.Map;

import com.fb.platform.sap.client.commons.ItemConditionsType;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.commons.TinlaClient;

public class BapiPricingConditionFactory {

	public static Map<String, String> conditionValueMap(ItemConditionsType conditionType, TinlaClient client) {
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
			if (SapUtils.isBigBazaar(client)) {
				conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.BB_SHIPPING_STEP_NUMBER);
				conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.BB_SHIPPING_CONDITION_TYPE);
			} else {
				conditionValueMap.put(SapOrderConstants.CONDITION_STEP_NUMBER, SapOrderConstants.SHIPPING_STEP_NUMBER);
				conditionValueMap.put(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.SHIPPING_CONDITION_TYPE);
			}
			conditionValueMap.put(SapOrderConstants.CONDITION_COUNTER, SapOrderConstants.DEFAULT_CONDITION_COUNTER);
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
