package com.fb.platform.sap.bapi.order.table.mapper;

import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.utils.SapOrderConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class PointsMapper {
	public static void setDetails(JCoFunction bapiFunction, PricingTO pricingTO, String cardNumber) {
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_TEXT.toString());
		orderText.appendRow();
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.DEFAULT_TEXT_ID);
		String pointsLine = cardNumber + "||" + 
											pricingTO.getPointsEarn() + "||" + pricingTO.getPointsEarnValue() + "||" + 
											pricingTO.getPointsBurn() + "||" + pricingTO.getPointsBurnValue(); 
		orderText.setValue(SapOrderConstants.TEXT_LINE, pointsLine);
		
	}
}
