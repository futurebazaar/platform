package com.fb.platform.sap.bapi.order.table.mapper;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.platform.sap.client.commons.SapConstants;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.commons.TinlaClient;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class PaymentMapper {

	private static Log logger = LogFactory.getLog(PaymentMapper.class);
	private static final String DEFAULT_CREDIT_CARD = "12345";
			
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<PaymentTO> paymentTOList) {
		// No Payment conditions defined in SAP for Big Bazaar and hence skipping
		TinlaClient client = TinlaClient.valueOf(orderHeaderTO.getClient());
		if (SapUtils.isBigBazaar(client)) {
			return;
		}
		logger.info("Setting Item Condition details for : " + orderHeaderTO.getReferenceID());
		for (PaymentTO paymentTO : paymentTOList) {
			String paymentMode = paymentTO.getPaymentMode();
			logger.info("Payment Detail for payment mode " + paymentMode + " is: " + paymentTO);
			if (paymentMode.equalsIgnoreCase("cod")) { 
				setCodDetails(bapiFunction, paymentMode, orderHeaderTO, paymentTO);
			} else if (paymentMode.equalsIgnoreCase("cheque")) { 
				setChequeDetails(bapiFunction, paymentMode, orderHeaderTO, paymentTO);
			} else {
				setCardDetails(bapiFunction, paymentMode, orderHeaderTO,  paymentTO);
			}
		}
	}
	
	private static void setChequeDetails(JCoFunction bapiFunction, String paymentMode, OrderHeaderTO orderHeaderTO, PaymentTO paymentTO) {
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_TEXT.toString());
		orderText.appendRow();
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.CHEQUE_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		String chequeDetail = "Amount: " +  paymentTO.getPricingTO().getPayableAmount() +
													"Auth ref code: " +  paymentTO.getAuthCode() +
													"Bank: " +  paymentTO.getBank() +
													"Cheque date: " + paymentTO.getValidTill() +
													"Cheque No: " + paymentTO.getInstrumentNumber() +
													"Currency: " + paymentTO.getPricingTO().getCurrency();
													
		orderText.setValue(SapOrderConstants.TEXT_LINE, chequeDetail);
		
	}

	private static void setCardDetails(JCoFunction bapiFunction, String paymentMode, OrderHeaderTO orderHeaderTO, PaymentTO paymentTO) {
		JCoTable orderCreditCard = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_CCARD.toString());
		orderCreditCard.appendRow();
		String paymentDate = SapUtils.convertDateToFormat(paymentTO.getPaymentTime(), SapConstants.DATE_FORMAT);
		String paymentTime = SapUtils.convertDateToFormat(paymentTO.getPaymentTime(), SapConstants.TIME_FORMAT);
		String instrumentNo = DEFAULT_CREDIT_CARD;
		if (StringUtils.isNotBlank(paymentTO.getInstrumentNumber())) {
			instrumentNo = paymentTO.getInstrumentNumber();
		}
		orderCreditCard.setValue(SapOrderConstants.INSTRUMENT_NO, instrumentNo);
		if (paymentTO.getValidTill() != null) {
			orderCreditCard.setValue(SapOrderConstants.VALID_TILL, SapUtils.convertDateToFormat(paymentTO.getValidTill(), SapConstants.DATE_FORMAT));			
		}
		orderCreditCard.setValue(SapOrderConstants.PAYMENT_DATE, paymentDate);
		orderCreditCard.setValue(SapOrderConstants.PAYMENT_TIME, paymentTime);
		String gateway = paymentTO.getPaymentGateway();
		if (StringUtils.isBlank(paymentTO.getPaymentGateway())) {
			gateway = paymentMode.toUpperCase();
		}
		orderCreditCard.setValue(SapOrderConstants.GATEWAY, gateway);
		orderCreditCard.setValue(SapOrderConstants.BILLING_AMOUNT, paymentTO.getPricingTO().getPayableAmount().toString());
		orderCreditCard.setValue(SapOrderConstants.PG_TRANSACTION_ID, paymentTO.getPgTransactionID());
		orderCreditCard.setValue( SapOrderConstants.AUTHORIZED_AMOUNT, paymentTO.getPricingTO().getPayableAmount().toString());
		orderCreditCard.setValue(SapOrderConstants.CURRENCY, paymentTO.getPricingTO().getCurrency());
		orderCreditCard.setValue(SapOrderConstants.CARD_HOLDER_NAME, "");
		orderCreditCard.setValue(SapOrderConstants.MERCHANT_ID, paymentTO.getMerchantID());
		orderCreditCard.setValue(SapOrderConstants.AUTH_FLAG, SapOrderConstants.COMMIT_FLAG);
		orderCreditCard.setValue(SapOrderConstants.AUTH_REFERENCE_NO, "");
		orderCreditCard.setValue(SapOrderConstants.CC_REACT, SapOrderConstants.CHAR_A);
		orderCreditCard.setValue(SapOrderConstants.CC_STAT_EX, SapOrderConstants.CANCEL_FLAG);
		orderCreditCard.setValue(SapOrderConstants.CC_REACT_T, SapOrderConstants.CANCEL_FLAG);
		orderCreditCard.setValue(SapOrderConstants.RADRCHECK1, SapOrderConstants.CHAR_A);
		orderCreditCard.setValue(SapOrderConstants.RADRCHECK2, SapOrderConstants.CHAR_D);
		orderCreditCard.setValue(SapOrderConstants.RADRCHECK3, SapOrderConstants.CHAR_Z);
		orderCreditCard.setValue(SapOrderConstants.RCARDCHECK, SapOrderConstants.CHAR_R);
	}
	
	private static void setCodDetails(JCoFunction bapiFunction, String paymentMode, OrderHeaderTO orderHeaderTO, PaymentTO paymentTO) {
		JCoStructure orderHeaderIN = bapiFunction.getImportParameterList().getStructure(BapiOrderTable.ORDER_HEADER_IN.toString());
		JCoStructure orderHeaderINX = bapiFunction.getImportParameterList().getStructure(BapiOrderTable.ORDER_HEADER_INX.toString());
		orderHeaderIN.setValue(SapOrderConstants.PAYMENT_TERM, SapOrderConstants.COD_PAYMENT_TERM);
		orderHeaderINX.setValue(SapOrderConstants.PAYMENT_TERM, SapOrderConstants.COMMIT_FLAG);
	}

}
