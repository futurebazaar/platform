package com.fb.platform.sap.bapi.order.table.mapper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class PointsMapper {
	
	private static Log logger = LogFactory.getLog(HeaderConditionsMapper.class);

	public static void setDetails(JCoFunction bapiFunction, PricingTO pricingTO, String cardNumber) {
		logger.info("Setting Payback details for card Number: " + cardNumber);
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_TEXT.toString());
		orderText.appendRow();
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.PAYBACK_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		String pointsLine = cardNumber + "||" + 
											pricingTO.getPointsEarn() + "||" + pricingTO.getPointsEarnValue() + "||" + 
											pricingTO.getPointsBurn() + "||" + pricingTO.getPointsBurnValue(); 
		orderText.setValue(SapOrderConstants.TEXT_LINE, pointsLine);
	}

}
