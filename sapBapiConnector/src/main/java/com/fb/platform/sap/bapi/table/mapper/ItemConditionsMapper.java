package com.fb.platform.sap.bapi.table.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.table.TableType;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ItemConditionsMapper {

	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> lineItemTOList, TinlaOrderType orderType) {
		
		for (LineItemTO itemTO : lineItemTOList) {
			
			if (itemTO.getPricingTO().getListPrice() != null && itemTO.getPricingTO().getListPrice().compareTo(BigDecimal.ZERO) > 0) { 
				setItemCondition(bapiFunction, SapConstants.MRP_CONDITION_TYPE, itemTO.getPricingTO().getListPrice(), itemTO, orderType);
			}

			if (itemTO.getPricingTO().getOfferPrice() != null && itemTO.getPricingTO().getOfferPrice().compareTo(BigDecimal.ZERO) > 0) {
				setItemCondition(bapiFunction, SapConstants.OFFER_PRICE_CONDITION_TYPE, itemTO.getPricingTO().getOfferPrice(), itemTO, orderType);
				setItemCondition(bapiFunction, SapConstants.SALES_PRICE_CONDITION_TYPE, itemTO.getPricingTO().getOfferPrice(), itemTO, orderType);
			}

			if (itemTO.getPricingTO().getExtraDiscount() != null && itemTO.getPricingTO().getExtraDiscount().compareTo(BigDecimal.ZERO) > 0) {
				setItemCondition(bapiFunction, SapConstants.ITEM_DISCOUNT_CONDITION_TYPE, itemTO.getPricingTO().getExtraDiscount(), itemTO, orderType);
			}

			if (itemTO.getPricingTO().getCouponDiscount() != null && itemTO.getPricingTO().getCouponDiscount().compareTo(BigDecimal.ZERO) > 0) {
				setItemCondition(bapiFunction, SapConstants.COUPON_CONDITION_TYPE, itemTO.getPricingTO().getCouponDiscount(), itemTO, orderType);
			}
			
			if (itemTO.getPricingTO().getShippingAmount() != null && itemTO.getPricingTO().getShippingAmount().compareTo(BigDecimal.ZERO) > 0 ) { 
				setItemCondition(bapiFunction, SapConstants.SHIPPING_CONDITION_TYPE,itemTO.getPricingTO().getShippingAmount(), itemTO, orderType);
			}
			
			if (itemTO.getPricingTO().getWarrantyPrice() != null && itemTO.getPricingTO().getWarrantyPrice().compareTo(BigDecimal.ZERO) > 0 ) { 
				setItemCondition(bapiFunction, SapConstants.WARRANTY_CONDITION_TYPE, itemTO.getPricingTO().getWarrantyPrice(), itemTO, orderType);
			}
			
			if (itemTO.getPricingTO().getCommissionAmount() != null && itemTO.getPricingTO().getCommissionAmount().compareTo(BigDecimal.ZERO) > 0 ) {
				setItemCondition(bapiFunction, SapConstants.ITZ_CONDITION_TYPE, itemTO.getPricingTO().getCommissionAmount(), itemTO, orderType);
			}
			
			if (itemTO.getPricingTO().getNlc() != null && itemTO.getPricingTO().getNlc().compareTo(BigDecimal.ZERO) > 0 ) { 
				setItemCondition(bapiFunction, SapConstants.NLC_CONDITION_TYPE, itemTO.getPricingTO().getNlc(), itemTO, orderType);
			}
		}
	}

	private static void setItemCondition(JCoFunction bapiFunction, String conditionType, BigDecimal conditionValue, LineItemTO itemTO, TinlaOrderType orderType) {
		
		String operationFlag = SapConstants.INSERT_FLAG;
		if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
			operationFlag = SapConstants.UPDATE_FLAG;
		}
		
		Map<TableType, BapiTable> conditionTables = PlatformBapiHandlerFactory.getConditionTables(orderType);		
		JCoTable orderConditionIN= bapiFunction.getTableParameterList().getTable(conditionTables.get(TableType.VALUE_TABLE).toString());
		orderConditionIN.appendRow();
		orderConditionIN.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
		orderConditionIN.setValue(SapConstants.CONDITION_TYPE, conditionType);
		orderConditionIN.setValue(SapConstants.CONDITION_VALUE, conditionValue);
		
		if (!orderType.equals(TinlaOrderType.RET_ORDER)) {
			JCoTable orderConditionINX = bapiFunction.getTableParameterList().getTable(conditionTables.get(TableType.COMMIT_TABLE).toString());
			orderConditionINX.appendRow();
			orderConditionINX.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			orderConditionINX.setValue(SapConstants.CONDITION_TYPE, conditionType);
			orderConditionINX.setValue(SapConstants.CONDITION_VALUE, SapConstants.COMMIT_FLAG);
			orderConditionINX.setValue(SapConstants.OPERATION_FLAG, operationFlag);
		}
		
	}
}
