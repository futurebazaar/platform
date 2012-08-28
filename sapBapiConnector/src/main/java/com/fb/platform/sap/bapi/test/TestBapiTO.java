package com.fb.platform.sap.bapi.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.to.BapiTO;

public class TestBapiTO {
	
	public BapiTO getBapiTO() {
		BapiTO bapiTO = new BapiTO();
		bapiTO.setOrderType(TinlaOrderType.NEW_ORDER);
		bapiTO.setOrderHeaderTO(getOrderTO());
		bapiTO.setLineItemTO(getLineItemTO());
		bapiTO.setAddressTO(getAddressTO());
		bapiTO.setPricingTO(getPricingTO());
		bapiTO.setPaymentTO(getPaymentTO());
		return bapiTO;
	}
	
	private List<PaymentTO> getPaymentTO() {
		List<PaymentTO> paymentTOList = new ArrayList<PaymentTO>();
		PaymentTO paymentTO1 = new PaymentTO();
		paymentTO1.setAuthCode("1234");
		paymentTO1.setBank("ICICI");
		paymentTO1.setInstrumentNumber("123456");
		paymentTO1.setMerchantID("1982");
		paymentTO1.setPaymentGateway("ICIC");
		paymentTO1.setPaymentMode("credit-card-emi-web");
		paymentTO1.setPaymentTime(DateTime.now());
		paymentTO1.setPgTransactionID("223344");
		paymentTO1.setPricingTO(getPricingTO());
		paymentTO1.setTransactionID("5051235");
		paymentTO1.setValidTill(DateTime.now().plusMonths(4));
		paymentTOList.add(paymentTO1);
		return paymentTOList;
	}

	private OrderHeaderTO getOrderTO() {
		OrderHeaderTO orderHeaderTO = new OrderHeaderTO();
		orderHeaderTO.setAccountNumber("2100000103");
		orderHeaderTO.setChannelType("WEBSITE");
		orderHeaderTO.setClient("FUTUREBAZAAR");
		orderHeaderTO.setCreatedOn(DateTime.now());
		orderHeaderTO.setReason("I");
		orderHeaderTO.setSalesChannel("true");
		orderHeaderTO.setSubmittedOn(DateTime.now());
		//orderHeaderTO.set
		//orderHeaderTO.setType("NEW_ORDER");
		orderHeaderTO.setSalesDocType("ZATG");
		orderHeaderTO.setReferenceID("5049999921");
		orderHeaderTO.setPricingTO(getPricingTO());
		
		return orderHeaderTO;
	}
	
	private PricingTO getPricingTO() {
		PricingTO pricingTO = new PricingTO();
		pricingTO.setCurrency("INR");
		pricingTO.setListPrice(new BigDecimal("1100.00"));
		pricingTO.setOfferPrice(new BigDecimal("1000.00"));
		pricingTO.setPayableAmount(new BigDecimal("1000.00"));
		return pricingTO;
	}

	private List<LineItemTO> getLineItemTO() {
		List<LineItemTO> lineItemTOList = new ArrayList<LineItemTO>();
		LineItemTO lineItemTO1 = new LineItemTO();
		lineItemTO1.setPricingTO(getPricingTO());
		lineItemTO1.setArticleID("000000000300000560");
		lineItemTO1.setSapDocumentId(10);
		lineItemTO1.setQuantity(new BigDecimal("1.00"));
		lineItemTO1.setDescription("TEST ARTICLE");
		lineItemTO1.setPlantId("2786");
		lineItemTOList.add(lineItemTO1);
		lineItemTO1.setSalesUnit("EA");
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
