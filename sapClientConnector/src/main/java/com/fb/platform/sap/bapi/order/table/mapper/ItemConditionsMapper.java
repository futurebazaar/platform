package com.fb.platform.sap.bapi.order.table.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.client.commons.ItemConditionsType;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.TinlaClient;
import com.fb.platform.sap.bapi.factory.BapiPricingConditionFactory;
import com.fb.platform.sap.bapi.factory.BapiTableFactory;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ItemConditionsMapper {
	
	private static Log logger = LogFactory.getLog(ItemConditionsMapper.class);

	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> lineItemTOList, TinlaOrderType orderType) {
		logger.info("Setting Item Condition details for : " + orderType + " " + orderHeaderTO.getReferenceID());
		for (LineItemTO itemTO : lineItemTOList) {
			if (itemTO.getPricingTO().getListPrice() != null && itemTO.getPricingTO().getListPrice().compareTo(BigDecimal.ZERO) > 0) {
				setItemCondition(bapiFunction, ItemConditionsType.MRP_CONDITION_TYPE, itemTO.getPricingTO().getListPrice(), itemTO, orderType, orderHeaderTO);
			}
			if (itemTO.getPricingTO().getOfferPrice() != null && itemTO.getPricingTO().getOfferPrice().compareTo(BigDecimal.ZERO) > 0) {
				setItemCondition(bapiFunction, ItemConditionsType.LIST_CONDITION_TYPE, itemTO.getPricingTO().getOfferPrice(), itemTO, orderType, orderHeaderTO);
				setItemCondition(bapiFunction, ItemConditionsType.SALES_CONDITION_TYPE, itemTO.getPricingTO().getOfferPrice(), itemTO, orderType, orderHeaderTO);
			}
			if (itemTO.getPricingTO().getExtraDiscount() != null && itemTO.getPricingTO().getExtraDiscount().compareTo(BigDecimal.ZERO) > 0) {
				setItemCondition(bapiFunction, ItemConditionsType.ITEM_DISCOUNT_CONDITION_TYPE, itemTO.getPricingTO().getExtraDiscount(), itemTO, orderType, orderHeaderTO);
			}
			if (itemTO.getPricingTO().getCouponDiscount() != null && itemTO.getPricingTO().getCouponDiscount().compareTo(BigDecimal.ZERO) > 0) {
				setItemCondition(bapiFunction, ItemConditionsType.COUPON_CONDITION_TYPE, itemTO.getPricingTO().getCouponDiscount(), itemTO, orderType, orderHeaderTO);
			}
			if (itemTO.getPricingTO().getShippingAmount() != null && itemTO.getPricingTO().getShippingAmount().compareTo(BigDecimal.ZERO) > 0 ) {
				setItemCondition(bapiFunction, ItemConditionsType.SHIPPING_CONDITION_TYPE,itemTO.getPricingTO().getShippingAmount(), itemTO, orderType, orderHeaderTO);
			}
			if (itemTO.getPricingTO().getWarrantyPrice() != null && itemTO.getPricingTO().getWarrantyPrice().compareTo(BigDecimal.ZERO) > 0 ) { 
				setItemCondition(bapiFunction, ItemConditionsType.WARRANTY_CONDITION_TYPE, itemTO.getPricingTO().getWarrantyPrice(), itemTO, orderType, orderHeaderTO);
			}
			if (itemTO.getPricingTO().getCommissionAmount() != null && itemTO.getPricingTO().getCommissionAmount().compareTo(BigDecimal.ZERO) > 0 ) {
				setItemCondition(bapiFunction, ItemConditionsType.ITZ_CONDITION_TYPE, itemTO.getPricingTO().getCommissionAmount(), itemTO, orderType, orderHeaderTO);
			}
			if (itemTO.getPricingTO().getNlc() != null && itemTO.getPricingTO().getNlc().compareTo(BigDecimal.ZERO) > 0 ) { 
				setItemCondition(bapiFunction, ItemConditionsType.NLC_CONDITION_TYPE, itemTO.getPricingTO().getNlc(), itemTO, orderType, orderHeaderTO);
			}
		}
	}

	private static void setItemCondition(JCoFunction bapiFunction, ItemConditionsType conditionType, BigDecimal conditionValue, LineItemTO itemTO, TinlaOrderType orderType, OrderHeaderTO orderHeaderTO) {
		TinlaClient client = TinlaClient.valueOf(orderHeaderTO.getClient());
		if (client.equals(TinlaClient.BIGBAZAAR) && orderType.equals(TinlaOrderType.MOD_ORDER) && itemTO.getOperationCode().equals(SapOrderConstants.CANCEL_FLAG)) {
			return;
		}
		logger.info("Setting : " +conditionType + " with value: " +  conditionValue + " for : " + orderType + " " + orderHeaderTO.getReferenceID());

		Map<OrderTableType, BapiOrderTable> conditionTables = BapiTableFactory.getConditionTables(orderType, client);
		Map<String, String> conditionKeyValueMap = BapiPricingConditionFactory.conditionValueMap(conditionType, client);
		JCoTable orderConditionIN= bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.VALUE_TABLE).toString());
		orderConditionIN.appendRow();
		orderConditionIN.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		orderConditionIN.setValue(SapOrderConstants.CONDITION_TYPE, conditionKeyValueMap.get(SapOrderConstants.CONDITION_TYPE));
		orderConditionIN.setValue(SapOrderConstants.CONDITION_VALUE, conditionValue.toString());
		orderConditionIN.setValue(SapOrderConstants.CURRENCY, SapOrderConstants.DEFAULT_CURRENCY);
		if (!orderType.equals(TinlaOrderType.NEW_ORDER)) {
			orderConditionIN.setValue(SapOrderConstants.CONDITION_STEP_NUMBER, conditionKeyValueMap.get(SapOrderConstants.CONDITION_STEP_NUMBER));
			orderConditionIN.setValue(SapOrderConstants.CONDITION_COUNTER, conditionKeyValueMap.get(SapOrderConstants.CONDITION_COUNTER));
		}
		if (!orderType.equals(TinlaOrderType.RET_ORDER)) {
			JCoTable orderConditionINX = bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.COMMIT_TABLE).toString());
			orderConditionINX.appendRow();
			orderConditionINX.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			orderConditionINX.setValue(SapOrderConstants.CONDITION_TYPE, conditionKeyValueMap.get(SapOrderConstants.CONDITION_TYPE));
			orderConditionINX.setValue(SapOrderConstants.CONDITION_VALUE, SapOrderConstants.COMMIT_FLAG);
			orderConditionINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.INSERT_FLAG);
			orderConditionINX.setValue(SapOrderConstants.CURRENCY, SapOrderConstants.COMMIT_FLAG);
			if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
				orderConditionINX.setValue(SapOrderConstants.CONDITION_STEP_NUMBER, conditionKeyValueMap.get(SapOrderConstants.CONDITION_STEP_NUMBER));
				orderConditionINX.setValue(SapOrderConstants.CONDITION_COUNTER, conditionKeyValueMap.get(SapOrderConstants.CONDITION_COUNTER));
				orderConditionINX.setValue(SapOrderConstants.OPERATION_FLAG, itemTO.getOperationCode());
			}
		}
	}
}
