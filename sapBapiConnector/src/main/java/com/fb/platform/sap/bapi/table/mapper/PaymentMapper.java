package com.fb.platform.sap.bapi.table.mapper;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.fb.platform.sap.bapi.utils.SapUtils;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class PaymentMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<PaymentTO> paymentTOList) {

		for (PaymentTO paymentTO : paymentTOList) {
			String paymentMode = paymentTO.getPaymentMode();
			if (paymentMode.equalsIgnoreCase("cod")) { 
				setCodDetails(bapiFunction, paymentMode, orderHeaderTO, paymentTO);
			}
			else {
				setCardDetails(bapiFunction, paymentMode, orderHeaderTO,  paymentTO);
			}
		}
	}
	
	private static void setCardDetails(JCoFunction bapiFunction, String paymentMode,
			OrderHeaderTO orderHeaderTO, PaymentTO paymentTO) {
		JCoTable orderCreditCard = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_CCARD.toString());
		
		orderCreditCard.appendRow();

		String validTill = SapUtils.convertDateToFormat(paymentTO.getValidTill(), "yyyMMdd");
		String paymentDate = SapUtils.convertDateToFormat(paymentTO.getPaymentTime(), "yyyMMdd");
		String paymentTime = SapUtils.convertDateToFormat(paymentTO.getPaymentTime(), "HHmmss");;  
		orderCreditCard.setValue(SapConstants.INSTRUMENT_NO, paymentTO.getInstrumentNumber());
		orderCreditCard.setValue(SapConstants.VALID_TILL, validTill);
		orderCreditCard.setValue(SapConstants.PAYMENT_DATE, paymentDate);
		orderCreditCard.setValue(SapConstants.PAYMENT_TIME, paymentTime);
		
		String gateway = paymentTO.getPaymentGateway();
		if (StringUtils.isBlank(paymentTO.getPaymentGateway())) {
			gateway = paymentMode.toUpperCase();
		}
		orderCreditCard.setValue(SapConstants.GATEWAY, gateway);
		orderCreditCard.setValue(SapConstants.BILLING_AMOUNT, paymentTO.getPricingTO().getPayableAmount().toString());
		orderCreditCard.setValue(SapConstants.PG_TRANSACTION_ID, paymentTO.getPgTransactionID());
		orderCreditCard.setValue( SapConstants.AUTHORIZED_AMOUNT, paymentTO.getPricingTO().getPayableAmount().toString());
		orderCreditCard.setValue(SapConstants.CURRENCY, paymentTO.getPricingTO().getCurrency());
		orderCreditCard.setValue(SapConstants.CARD_HOLDER_NAME, "");
		orderCreditCard.setValue(SapConstants.MERCHANT_ID, paymentTO.getMerchantID());
		
		orderCreditCard.setValue(SapConstants.AUTH_FLAG, SapConstants.COMMIT_FLAG);
		orderCreditCard.setValue(SapConstants.AUTH_REFERENCE_NO, "");
		orderCreditCard.setValue(SapConstants.CC_REACT, SapConstants.CHAR_A);
		orderCreditCard.setValue(SapConstants.CC_STAT_EX, SapConstants.CANCEL_FLAG);
		orderCreditCard.setValue(SapConstants.CC_REACT_T, SapConstants.CANCEL_FLAG);
		
		
		orderCreditCard.setValue(SapConstants.RADRCHECK1, SapConstants.CHAR_A);
		orderCreditCard.setValue(SapConstants.RADRCHECK2, SapConstants.CHAR_D);
		orderCreditCard.setValue(SapConstants.RADRCHECK3, SapConstants.CHAR_Z);
		orderCreditCard.setValue(SapConstants.RCARDCHECK, SapConstants.CHAR_R);

	}
	
	private static void setCodDetails(JCoFunction bapiFunction, String paymentMode, OrderHeaderTO orderHeaderTO, PaymentTO paymentTO) {
		JCoStructure orderHeaderIN = bapiFunction.getImportParameterList().getStructure(BapiOrderTable.ORDER_HEADER_IN.toString());
		JCoStructure orderHeaderINX = bapiFunction.getImportParameterList().getStructure(BapiOrderTable.ORDER_HEADER_INX.toString());
		orderHeaderIN.setValue(SapConstants.PAYMENT_TERM, SapConstants.COD_PAYMENT_TERM);
		orderHeaderINX.setValue(SapConstants.PAYMENT_TERM, SapConstants.COMMIT_FLAG);

	}

}
