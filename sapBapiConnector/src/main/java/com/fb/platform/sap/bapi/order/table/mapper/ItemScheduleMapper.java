package com.fb.platform.sap.bapi.order.table.mapper;

import java.util.List;
import java.util.Map;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.OrderTableType;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapOrderConstants;
import com.fb.platform.sap.bapi.utils.SapUtils;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ItemScheduleMapper {

	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> lineItemTOList, TinlaOrderType orderType) {
		Map<OrderTableType, BapiOrderTable> schedulesTables = PlatformBapiHandlerFactory.getScheduleTables(orderType);
		JCoTable orderScheduleIN= bapiFunction.getTableParameterList().getTable(schedulesTables.get(OrderTableType.VALUE_TABLE).toString());
		JCoTable orderScheduleINX = bapiFunction.getTableParameterList().getTable(schedulesTables.get(OrderTableType.COMMIT_TABLE).toString());
		
		for (LineItemTO itemTO: lineItemTOList) {
			orderScheduleIN.appendRow();
			orderScheduleINX.appendRow();
			
			String commonDate = SapUtils.convertDateToFormat(orderHeaderTO.getCreatedOn(), "yyyMMdd");

			if (itemTO.getRequiredDeliveryDate() != null) {
				commonDate = SapUtils.convertDateToFormat(itemTO.getRequiredDeliveryDate(), "yyyMMdd");
			}
			
			orderScheduleIN.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			orderScheduleINX.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());

			orderScheduleIN.setValue(SapOrderConstants.REQ_DATE, commonDate);
			orderScheduleINX.setValue(SapOrderConstants.REQ_DATE, SapOrderConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapOrderConstants.REQUIRED_QUANTITY, itemTO.getQuantity().toString());
			orderScheduleINX.setValue(SapOrderConstants.REQUIRED_QUANTITY, SapOrderConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapOrderConstants.TP_DATE, commonDate);
			orderScheduleINX.setValue(SapOrderConstants.TP_DATE, SapOrderConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapOrderConstants.MS_DATE, commonDate);
			orderScheduleINX.setValue(SapOrderConstants.MS_DATE, SapOrderConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapOrderConstants.LOAD_DATE, commonDate);
			orderScheduleINX.setValue(SapOrderConstants.LOAD_DATE, SapOrderConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapOrderConstants.GI_DATE, commonDate);
			orderScheduleINX.setValue(SapOrderConstants.GI_DATE, SapOrderConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapOrderConstants.SCHEDULE_LINE, SapOrderConstants.DEFAULT_SCHEDULE_LINE);
			orderScheduleINX.setValue(SapOrderConstants.SCHEDULE_LINE, SapOrderConstants.DEFAULT_SCHEDULE_LINE);

			if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
				orderScheduleINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.INSERT_FLAG);
			} else {
				orderScheduleINX.setValue(SapOrderConstants.OPERATION_FLAG, SapOrderConstants.UPDATE_FLAG);
			}
		}
	}
}
