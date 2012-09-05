package com.fb.platform.sap.bapi.table.mapper;

import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class PointsMapper {
	public static void setDetails(JCoFunction bapiFunction, PricingTO pricingTO, String cardNumber) {
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiTable.ORDER_TEXT.toString());
		orderText.appendRow();
		orderText.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapConstants.TEXT_ID, SapConstants.DEFAULT_TEXT_ID);
		String pointsLine = cardNumber + "||" + 
											pricingTO.getPointsEarn() + "||" + pricingTO.getPointsEarnValue() + "||" + 
											pricingTO.getPointsBurn() + "||" + pricingTO.getPointsBurnValue(); 
		orderText.setValue(SapConstants.TEXT_LINE, pointsLine);
		
	}
}
