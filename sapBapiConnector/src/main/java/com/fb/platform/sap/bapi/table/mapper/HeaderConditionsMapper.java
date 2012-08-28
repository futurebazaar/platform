package com.fb.platform.sap.bapi.table.mapper;

import java.math.BigDecimal;
import java.util.Map;

import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.table.TableType;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class HeaderConditionsMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, TinlaOrderType orderType) {
		Map<TableType, BapiTable> conditionTables = PlatformBapiHandlerFactory.getConditionTables(orderType);
		JCoTable orderConditionsIN= bapiFunction.getTableParameterList().getTable(conditionTables.get(TableType.VALUE_TABLE).toString());
		JCoTable orderConditionsINX = bapiFunction.getTableParameterList().getTable(conditionTables.get(TableType.COMMIT_TABLE).toString());
		
		BigDecimal discount = orderHeaderTO.getPricingTO().getCouponDiscount();
		if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
			orderConditionsIN.appendRow();
			orderConditionsINX.appendRow();

			orderConditionsIN.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
			orderConditionsINX.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);

			orderConditionsIN.setValue(SapConstants.CONDITION_TYPE, SapConstants.DISCOUNT_CONDITION_TYPE);
			orderConditionsINX.setValue(SapConstants.CONDITION_TYPE, SapConstants.DISCOUNT_CONDITION_TYPE);

			orderConditionsIN.setValue(SapConstants.CONDITION_VALUE, discount.toString());
			orderConditionsINX.setValue(SapConstants.CONDITION_VALUE, SapConstants.COMMIT_FLAG);

			orderConditionsINX.setValue(SapConstants.OPERATION_FLAG, SapConstants.INSERT_FLAG);

		}
	}
}
