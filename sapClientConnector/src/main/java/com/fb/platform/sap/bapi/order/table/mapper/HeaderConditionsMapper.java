package com.fb.platform.sap.bapi.order.table.mapper;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.factory.BapiTableFactory;
import com.fb.platform.sap.bapi.factory.SapOrderConfigFactory;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.TinlaClient;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class HeaderConditionsMapper {
	
	private static Log logger = LogFactory.getLog(HeaderConditionsMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, TinlaOrderType orderType) {
		logger.info("Setting Header Condition details for : " + orderType + " " + orderHeaderTO.getReferenceID());
		Map<OrderTableType, BapiOrderTable> conditionTables = BapiTableFactory.getConditionTables(orderType, TinlaClient.valueOf(orderHeaderTO.getClient()));
		JCoTable orderConditionsIN= bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.VALUE_TABLE).toString());
		JCoTable orderConditionsINX = bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.COMMIT_TABLE).toString());
		TinlaClient client = TinlaClient.valueOf(orderHeaderTO.getClient());
		BigDecimal discount = orderHeaderTO.getPricingTO().getCouponDiscount();
		if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
			logger.info("Setting Header Coupon Discount : " +  orderHeaderTO.getPricingTO().getCouponDiscount() + " for : " + orderType + " " + orderHeaderTO.getReferenceID());
			orderConditionsIN.appendRow();
			orderConditionsINX.appendRow();
			orderConditionsIN.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
			orderConditionsINX.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
			orderConditionsIN.setValue(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.DISCOUNT_CONDITION_TYPE);
			orderConditionsINX.setValue(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.DISCOUNT_CONDITION_TYPE);
			orderConditionsIN.setValue(SapOrderConstants.CONDITION_VALUE, discount.toString());
			orderConditionsINX.setValue(SapOrderConstants.CONDITION_VALUE, SapOrderConstants.COMMIT_FLAG);
			orderConditionsINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.INSERT_FLAG);
			orderConditionsIN.setValue(SapOrderConstants.CURRENCY, SapOrderConfigFactory.getConfigValue(SapOrderConstants.CURRENCY, client, orderType));
			orderConditionsINX.setValue(SapOrderConstants.CURRENCY, SapOrderConstants.COMMIT_FLAG);
			if (orderType.equals(TinlaOrderType.MOD_ORDER)) {
				orderConditionsINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.UPDATE_FLAG);
			}
		}
	}

}
