package com.fb.platform.sap.bapi.table.mapper;

import java.util.List;
import java.util.Map;

import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.table.TableType;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.fb.platform.sap.bapi.utils.SapUtils;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ItemScheduleMapper {

	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> lineItemTOList, TinlaOrderType orderType) {
		Map<TableType, BapiTable> schedulesTables = PlatformBapiHandlerFactory.getScheduleTables(orderType);
		JCoTable orderScheduleIN= bapiFunction.getTableParameterList().getTable(schedulesTables.get(TableType.VALUE_TABLE).toString());
		JCoTable orderScheduleINX = bapiFunction.getTableParameterList().getTable(schedulesTables.get(TableType.COMMIT_TABLE).toString());
		
		for (LineItemTO itemTO: lineItemTOList) {
			orderScheduleIN.appendRow();
			orderScheduleINX.appendRow();
			
			String commonDate = SapUtils.convertDateToFormat(orderHeaderTO.getCreatedOn(), "yyyMMdd");

			if (itemTO.getRequiredDeliveryDate() != null) {
				commonDate = SapUtils.convertDateToFormat(itemTO.getRequiredDeliveryDate(), "yyyMMdd");
			}
			
			orderScheduleIN.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			orderScheduleINX.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());

			orderScheduleIN.setValue(SapConstants.REQ_DATE, commonDate);
			orderScheduleINX.setValue(SapConstants.REQ_DATE, SapConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapConstants.REQUIRED_QUANTITY, itemTO.getQuantity().toString());
			orderScheduleINX.setValue(SapConstants.REQUIRED_QUANTITY, SapConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapConstants.TP_DATE, commonDate);
			orderScheduleINX.setValue(SapConstants.TP_DATE, SapConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapConstants.MS_DATE, commonDate);
			orderScheduleINX.setValue(SapConstants.MS_DATE, SapConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapConstants.LOAD_DATE, commonDate);
			orderScheduleINX.setValue(SapConstants.LOAD_DATE, SapConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapConstants.GI_DATE, commonDate);
			orderScheduleINX.setValue(SapConstants.GI_DATE, SapConstants.COMMIT_FLAG);

			orderScheduleIN.setValue(SapConstants.SCHEDULE_LINE, SapConstants.DEFAULT_SCHEDULE_LINE);
			orderScheduleINX.setValue(SapConstants.SCHEDULE_LINE, SapConstants.DEFAULT_SCHEDULE_LINE);

			if (orderType.equals(TinlaOrderType.NEW_ORDER)) {
				orderScheduleINX.setValue(SapConstants.OPERATION_FLAG, SapConstants.INSERT_FLAG);
			} else {
				orderScheduleINX.setValue(SapConstants.OPERATION_FLAG, SapConstants.UPDATE_FLAG);
			}
		}
	}
}
