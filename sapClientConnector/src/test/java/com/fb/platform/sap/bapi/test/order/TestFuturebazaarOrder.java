package com.fb.platform.sap.bapi.test.order;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.commons.mom.to.PricingTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;
import com.fb.platform.sap.client.handler.PlatformClientHandler;

public class TestFuturebazaarOrder extends BaseTestCase {
	
	private SapOrderRequestTO getBapiTO() {
		SapOrderRequestTO bapiTO = new SapOrderRequestTO();
		bapiTO.setOrderType(TinlaOrderType.NEW_ORDER);
		bapiTO.setOrderHeaderTO(getOrderTO());
		bapiTO.setLineItemTO(getLineItemTO());
		bapiTO.setBillingAddressTO(getAddressTO());
		bapiTO.setDefaultShippingAddressTO(getAddressTO());
		bapiTO.setPaymentTO(getPaymentTO());
		return bapiTO;
	}
	
	private List<PaymentTO> getPaymentTO() {
		List<PaymentTO> paymentTOList = new ArrayList<PaymentTO>();
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
		orderHeaderTO.setAccountNumber("2100000103");
		orderHeaderTO.setChannelType("WEBSITE");
		orderHeaderTO.setClient("FUTUREBAZAAR");
		orderHeaderTO.setCreatedOn(DateTime.now());
		//orderHeaderTO.set
		orderHeaderTO.setSalesDocType("ZATG");
		orderHeaderTO.setReferenceID("5049999978");
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
		lineItemTO1.setLspCode("0000300413");
		lineItemTO1.setAddressTO(getAddressTO());
		lineItemTOList.add(lineItemTO1);
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
	
	@Test
	public void testFutureBazaarOrder() {
		SapOrderRequestTO orderRequestTO = getBapiTO();
		ApplicationContext context = new ClassPathXmlApplicationContext("test-applicationContext-service.xml");
		PlatformClientHandler bh = (PlatformClientHandler) context.getBean("sapClientHandler");
		SapOrderResponseTO responseTO = bh.processOrder(orderRequestTO);
		assertEquals(orderRequestTO.getOrderHeaderTO().getReferenceID(), responseTO.getOrderId());
		assertEquals("ID: TEST VALUE || TYPE: TEST VALUE || MESSAGE: FBG TEST MESSAGE", responseTO.getSapMessage().trim());
	}
}
