package com.fb.platform.sap.bapi.test.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;

public class TestBapiOrderTO {
	
	public SapOrderRequestTO getBapiTO() {
		SapOrderRequestTO bapiTO = new SapOrderRequestTO();
		bapiTO.setOrderType(TinlaOrderType.NEW_ORDER);
		bapiTO.setOrderHeaderTO(getOrderTO());
		bapiTO.setLineItemTO(getLineItemTO());
		bapiTO.setBillingAddressTO(getAddressTO());
		bapiTO.setPaymentTO(getPaymentTO());
		return bapiTO;
	}
	
	private List<PaymentTO> getPaymentTO() {
		List<PaymentTO> paymentTOList = new ArrayList<PaymentTO>();
		PaymentTO paymentTO1 = new PaymentTO();
		paymentTO1.setAuthCode("1");
		paymentTO1.setBank("");
		paymentTO1.setInstrumentNumber("");
		paymentTO1.setMerchantID("");
		paymentTO1.setPaymentGateway("GVFB");
		paymentTO1.setPaymentMode("egv");
		paymentTO1.setPaymentTime(DateTime.now());
		paymentTO1.setPgTransactionID("223344");
		paymentTO1.setPricingTO(getPricingTO());
		paymentTO1.setTransactionID("5051235");
		paymentTO1.setValidTill(DateTime.now().plusMonths(4));
		paymentTOList.add(paymentTO1);
		
		PaymentTO paymentTO2 = new PaymentTO();
		paymentTO2.setAuthCode("1234");
		paymentTO2.setBank("ICICI");
		paymentTO2.setInstrumentNumber("123456");
		paymentTO2.setMerchantID("1982");
		paymentTO2.setPaymentGateway("ICI3");
		paymentTO2.setPaymentMode("cheque");
		paymentTO2.setPaymentTime(DateTime.now());
		paymentTO2.setPgTransactionID("223344");
		paymentTO2.setPricingTO(getPricingTO());
		paymentTO2.setTransactionID("5051235");
		paymentTO2.setValidTill(DateTime.now().plusMonths(4));
		paymentTOList.add(paymentTO2);
		return paymentTOList;
	}

	private OrderHeaderTO getOrderTO() {
		OrderHeaderTO orderHeaderTO = new OrderHeaderTO();
		orderHeaderTO.setAccountNumber("2100000104");
		orderHeaderTO.setChannelType("WEBSITE");
		orderHeaderTO.setClient("FUTUREBAZAAR");
		orderHeaderTO.setCreatedOn(DateTime.now());
		orderHeaderTO.setReason("I");
		orderHeaderTO.setSalesChannel("true");
		orderHeaderTO.setSubmittedOn(DateTime.now());
		//orderHeaderTO.set
		orderHeaderTO.setSalesDocType("ZATG");
		orderHeaderTO.setReferenceID("5049999894");
		orderHeaderTO.setReturnOrderID("6699999999");
		orderHeaderTO.setLoyaltyCardNumber("1234123412341234");
		orderHeaderTO.setPricingTO(getPricingTO());
		
		return orderHeaderTO;
	}
	
	private PricingTO getPricingTO() {
		PricingTO pricingTO = new PricingTO();
		pricingTO.setCurrency("INR");
		pricingTO.setListPrice(new BigDecimal("1200.00"));
		pricingTO.setOfferPrice(new BigDecimal("1100.00"));
		pricingTO.setCouponDiscount(new BigDecimal("50"));
		pricingTO.setPayableAmount(new BigDecimal("1100.00"));
		pricingTO.setPointsEarn(new BigDecimal("40"));
		pricingTO.setPointsEarnValue(new BigDecimal("10"));
		return pricingTO;
	}

	private List<LineItemTO> getLineItemTO() {
		List<LineItemTO> lineItemTOList = new ArrayList<LineItemTO>();
		LineItemTO lineItemTO1 = new LineItemTO();
		lineItemTO1.setPricingTO(getPricingTO());
		lineItemTO1.setArticleID("000000000300000560");
		lineItemTO1.setSapDocumentId(10);
		lineItemTO1.setQuantity(new BigDecimal("5.00"));
		lineItemTO1.setDescription("TEST ARTICLE");
		lineItemTO1.setPlantId("2786");
		lineItemTO1.setSalesUnit("EA");
		lineItemTO1.setStorageLocation(10);
		lineItemTO1.setReasonCode("103");
		lineItemTO1.setItemCategory("ZATX");
		lineItemTO1.setOperationCode("C");
		lineItemTO1.setLspCode("300413");
		lineItemTO1.setAddressTO(getAddressTO());
		lineItemTOList.add(lineItemTO1);
		
		LineItemTO lineItemTO2 = new LineItemTO();
		lineItemTO2.setPricingTO(getPricingTO());
		lineItemTO2.setArticleID("000000000300000561");
		lineItemTO2.setSapDocumentId(20);
		lineItemTO2.setQuantity(new BigDecimal("5.00"));
		lineItemTO2.setDescription("TEST ARTICLE");
		lineItemTO2.setPlantId("2786");
		lineItemTO2.setSalesUnit("EA");
		lineItemTO2.setStorageLocation(10);
		lineItemTO2.setReasonCode("103");
		lineItemTO2.setItemCategory("ZATX");
		lineItemTO2.setOperationCode("U");
		lineItemTO2.setAddressTO(getAddressTO());
		lineItemTOList.add(lineItemTO2);
		
		LineItemTO lineItemTO3 = new LineItemTO();
		lineItemTO3.setPricingTO(getPricingTO());
		lineItemTO3.setArticleID("000000000300000558");
		lineItemTO3.setSapDocumentId(30);
		lineItemTO3.setQuantity(new BigDecimal("5.00"));
		lineItemTO3.setDescription("TEST ARTICLE");
		lineItemTO3.setPlantId("2786");
		lineItemTO3.setSalesUnit("EA");
		lineItemTO3.setStorageLocation(10);
		lineItemTO3.setReasonCode("103");
		lineItemTO3.setItemCategory("ZATX");
		lineItemTO3.setOperationCode("I");
		lineItemTO3.setAddressTO(getAddressTO());
		lineItemTOList.add(lineItemTO3);
		
		return lineItemTOList;
	}
	
	private AddressTO getAddressTO() {
		AddressTO addressTO = new AddressTO();
		addressTO.setCity("MUMBAI");
		addressTO.setFirstName("ANUBHAV");
		addressTO.setLastName("JAIN");
		addressTO.setPincode("400060");
		addressTO.setPrimaryTelephone("01234567890");
		addressTO.setState("13");
		addressTO.setAddress("TEST ORDER");
		addressTO.setCountry("IN");
		return addressTO;
	}

}
