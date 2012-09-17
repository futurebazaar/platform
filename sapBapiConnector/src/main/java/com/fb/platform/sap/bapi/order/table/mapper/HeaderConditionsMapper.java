package com.fb.platform.sap.bapi.order.table.mapper;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.commons.SapOrderConstants;
import com.fb.platform.sap.bapi.commons.TinlaClient;
import com.fb.platform.sap.bapi.factory.BapiTableFactory;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class HeaderConditionsMapper {
	
	private static Log logger = LogFactory.getLog(HeaderConditionsMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, TinlaOrderType orderType) {
		Map<OrderTableType, BapiOrderTable> conditionTables = BapiTableFactory.getConditionTables(orderType, TinlaClient.valueOf(orderHeaderTO.getClient()));
		JCoTable orderConditionsIN= bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.VALUE_TABLE).toString());
		JCoTable orderConditionsINX = bapiFunction.getTableParameterList().getTable(conditionTables.get(OrderTableType.COMMIT_TABLE).toString());
		BigDecimal discount = orderHeaderTO.getPricingTO().getCouponDiscount();
		if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
			orderConditionsIN.appendRow();
			orderConditionsINX.appendRow();
			orderConditionsIN.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
			orderConditionsINX.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
			orderConditionsIN.setValue(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.DISCOUNT_CONDITION_TYPE);
			orderConditionsINX.setValue(SapOrderConstants.CONDITION_TYPE, SapOrderConstants.DISCOUNT_CONDITION_TYPE);
			orderConditionsIN.setValue(SapOrderConstants.CONDITION_VALUE, discount.toString());
			orderConditionsINX.setValue(SapOrderConstants.CONDITION_VALUE, SapOrderConstants.COMMIT_FLAG);
			orderConditionsINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.INSERT_FLAG);
		}
	}

}
