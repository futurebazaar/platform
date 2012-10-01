package com.fb.platform.sap.bapi.test.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;

public class TestBBBapiOrderTO {
	
	public SapOrderRequestTO getBapiTO() {
		SapOrderRequestTO bapiTO = new SapOrderRequestTO();
		bapiTO.setOrderType(TinlaOrderType.MOD_ORDER);
		bapiTO.setOrderHeaderTO(getOrderTO());
		bapiTO.setLineItemTO(getLineItemTO());
		bapiTO.setBillingAddressTO(getAddressTO());
		bapiTO.setDefaultShippingAddressTO(getAddressTO());
		return bapiTO;
	}
	
	private OrderHeaderTO getOrderTO() {
		OrderHeaderTO orderHeaderTO = new OrderHeaderTO();
		orderHeaderTO.setAccountNumber("G000001005");
		orderHeaderTO.setChannelType("WEBSITE");
		orderHeaderTO.setClient("BIGBAZAAR");
		orderHeaderTO.setCreatedOn(DateTime.now());
		orderHeaderTO.setReason("I");
		orderHeaderTO.setSalesChannel("true");
		orderHeaderTO.setSubmittedOn(DateTime.now());
		//orderHeaderTO.set
		orderHeaderTO.setSalesDocType("ZFGB");
		orderHeaderTO.setReferenceID("I000001012");
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
		lineItemTO1.setArticleID("000000000100000105");
		lineItemTO1.setSapDocumentId(10);
		lineItemTO1.setQuantity(new BigDecimal("2.00"));
		lineItemTO1.setDescription("TEST ARTICLE");
		lineItemTO1.setPlantId("4608");
		lineItemTO1.setSalesUnit("EA");
		lineItemTO1.setStorageLocation(10);
		lineItemTO1.setReasonCode("01");
		lineItemTO1.setOperationCode("C");
		lineItemTO1.setAddressTO(getAddressTO());
		lineItemTOList.add(lineItemTO1);
		
		LineItemTO lineItemTO2 = new LineItemTO();
		lineItemTO2.setPricingTO(getPricingTO());
		lineItemTO2.setArticleID("000000000100000002");
		lineItemTO2.setSapDocumentId(20);
		lineItemTO2.setQuantity(new BigDecimal("4.00"));
		lineItemTO2.setDescription("TEST ARTICLE");
		lineItemTO2.setPlantId("4608");
		lineItemTO2.setSalesUnit("EA");
		lineItemTO2.setStorageLocation(10);
		lineItemTO2.setReasonCode("01");
		lineItemTO2.setOperationCode("I");
		lineItemTO2.setAddressTO(getAddressTO());
		lineItemTOList.add(lineItemTO2);
//		
//		LineItemTO lineItemTO3 = new LineItemTO();
//		lineItemTO3.setPricingTO(getPricingTO());
//		lineItemTO3.setArticleID("000000000100000001");
//		lineItemTO3.setSapDocumentId(30);
//		lineItemTO3.setQuantity(new BigDecimal("4.00"));
//		lineItemTO3.setDescription("TEST ARTICLE");
//		lineItemTO3.setPlantId("4608");
//		lineItemTO3.setSalesUnit("EA");
//		lineItemTO3.setStorageLocation(10);
//		lineItemTO3.setReasonCode("103");
//		lineItemTO3.setOperationCode("I");
//		lineItemTO3.setAddressTO(getAddressTO());
//		lineItemTOList.add(lineItemTO3);
		return lineItemTOList;
	}
	
	private AddressTO getAddressTO() {
		AddressTO addressTO = new AddressTO();
		addressTO.setCity("MUMBAI");
		addressTO.setFirstName("ANUBHAV");
		addressTO.setLastName("JAIN");
		addressTO.setPincode("400060");
		addressTO.setPrimaryTelephone("01234567890");
		//addressTO.setSecondaryTelephone("11234567890");
		addressTO.setState("13");
		addressTO.setAddress("FUTUREBAZZAAR SECOND PARTNER TEST ORDER. CHECK IT IN SAP AND HENCE VERIFY");
		addressTO.setCountry("IN");
		return addressTO;
	}
	
	@Test
	public void dummy() {
		
	}
}
