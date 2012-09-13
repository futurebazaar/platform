package com.fb.platform.sap.bapi.order.table.mapper;

import java.math.BigDecimal;
import java.util.Map;

import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapOrderConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class HeaderConditionsMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, TinlaOrderType orderType) {
		Map<OrderTableType, BapiOrderTable> conditionTables = PlatformBapiHandlerFactory.getConditionTables(orderType);
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
