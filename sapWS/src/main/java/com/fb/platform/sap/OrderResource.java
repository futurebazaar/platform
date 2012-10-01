package com.fb.platform.sap;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap._1_0.CommonOrderRequest;
import com.fb.platform.sap._1_0.Customer;
import com.fb.platform.sap._1_0.DefaultShippingAddress;
import com.fb.platform.sap._1_0.DelItem;
import com.fb.platform.sap._1_0.Details;
import com.fb.platform.sap._1_0.LineItem;
import com.fb.platform.sap._1_0.ModifiedOrderHeader;
import com.fb.platform.sap._1_0.ModifyOrderRequest;
import com.fb.platform.sap._1_0.OrderHeader;
import com.fb.platform.sap._1_0.OrderLineItems;
import com.fb.platform.sap._1_0.PaymentGroups;
import com.fb.platform.sap._1_0.ReturnHeader;
import com.fb.platform.sap._1_0.ReturnItem;
import com.fb.platform.sap._1_0.ReturnOrderRequest;
import com.fb.platform.sap._1_0.SapOrderResponse;
import com.fb.platform.sap._1_0.ShipAddress;
import com.fb.platform.sap._1_0.ShippingAddress;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.handler.impl.SapClientHandler;

@Path("/order/")
@Component
@Scope("request")
public class OrderResource {
	
	private static Log logger = LogFactory.getLog(OrderResource.class);
	
	private SapClientHandler sapClientHandler = null;
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.sap._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	@Path("/common")
	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	public String writeOrder(String commonOrderXml) {
		logger.info("CommonOrderXML : \n" + commonOrderXml);
		try {	
			Unmarshaller unmarshaller = context.createUnmarshaller();
			CommonOrderRequest commonOrderRequest = (CommonOrderRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(commonOrderXml)));
			SapOrderRequestTO orderRequestTO = new SapOrderRequestTO();
			orderRequestTO.setOrderType(TinlaOrderType.NEW_ORDER);
			if (commonOrderRequest.getHeader().getMessageType().equals("CANORD")) {
				orderRequestTO.setOrderType(TinlaOrderType.MOD_ORDER);
			}
			setHeaderDetails(orderRequestTO, commonOrderRequest);
			setPaymentDetails(orderRequestTO, commonOrderRequest);
			setLineItemDetails(orderRequestTO, commonOrderRequest);
			setBillingAddress(orderRequestTO, commonOrderRequest);
			setDefaultShippingAddress(orderRequestTO, commonOrderRequest);
			SapOrderResponseTO orderResponseTO = sapClientHandler.processOrder(orderRequestTO);
			SapOrderResponse sapOrderResponseXml = setOrderResponseXml(orderResponseTO);
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(sapOrderResponseXml, outStringWriter);
			String xmlResponse = outStringWriter.toString();
			logger.info("CommonOrderXml response :\n" + xmlResponse);
			return xmlResponse;
		} catch (Exception e) {
			logger.error("Error in the Common Order call", e);
			return "error";
		}
	}
	
	private void setDefaultShippingAddress(SapOrderRequestTO orderRequestTO, CommonOrderRequest commonOrderRequest) {
		AddressTO addressTO = new AddressTO();
		DefaultShippingAddress shippingDetail = commonOrderRequest.getDefaultShippingAddress();
		addressTO.setCity(shippingDetail.getCity());
		addressTO.setFirstName(shippingDetail.getFirstName());
		addressTO.setLastName(shippingDetail.getLastName());
		addressTO.setPincode(shippingDetail.getPoCode());
		addressTO.setPrimaryTelephone(shippingDetail.getTelephone1());
		addressTO.setSecondaryTelephone(shippingDetail.getTelephone2());
		addressTO.setState(shippingDetail.getState());
		addressTO.setAddress(shippingDetail.getAddress1());
		addressTO.setCountry(shippingDetail.getCountry());
		orderRequestTO.setDefaultShippingAddressTO(addressTO);
		
	}
	
	private void setBillingAddress(SapOrderRequestTO orderRequestTO, CommonOrderRequest commonOrderRequest) {
		AddressTO addressTO = new AddressTO();
		Customer customerDetail = commonOrderRequest.getCustomer();
		addressTO.setCity(customerDetail.getCity());
		addressTO.setFirstName(customerDetail.getFirstName());
		addressTO.setLastName(customerDetail.getLastName());
		addressTO.setPincode(customerDetail.getPoCode());
		addressTO.setPrimaryTelephone(customerDetail.getTelephone1());
		addressTO.setSecondaryTelephone(customerDetail.getTelephone2());
		addressTO.setState(customerDetail.getState());
		addressTO.setAddress(customerDetail.getAddress1());
		addressTO.setCountry(customerDetail.getCountry());
		orderRequestTO.setBillingAddressTO(addressTO);
	}

	private void setItemAddressDetails(LineItemTO lineItemTO, ShipAddress shipAddress) {
		AddressTO addressTO = new AddressTO();
		addressTO.setCity(shipAddress.getCity());
		addressTO.setFirstName(shipAddress.getFirstName());
		addressTO.setLastName(shipAddress.getLastName());
		addressTO.setPincode(shipAddress.getPoCode());
		addressTO.setPrimaryTelephone(shipAddress.getTelephone1());
		addressTO.setSecondaryTelephone(shipAddress.getTelephone2());
		addressTO.setState(shipAddress.getState());
		addressTO.setAddress(shipAddress.getAddress1());
		addressTO.setCountry(shipAddress.getCountry());
		lineItemTO.setAddressTO(addressTO);
	}

	private SapOrderResponse setOrderResponseXml(SapOrderResponseTO orderResponseTO) {
		SapOrderResponse sapOrderResponseXml = new SapOrderResponse();
		sapOrderResponseXml.setMessage(orderResponseTO.getSapMessage());
		sapOrderResponseXml.setOrderId(orderResponseTO.getOrderId());
		sapOrderResponseXml.setReturnOrderId(orderResponseTO.getReturnOrderId());
		sapOrderResponseXml.setStatus(orderResponseTO.getStatus().toString());
		sapOrderResponseXml.setType(orderResponseTO.getType());
		return sapOrderResponseXml;
	}

	private void setLineItemDetails(SapOrderRequestTO orderRequestTO, CommonOrderRequest commonOrderRequest) {
		List<LineItemTO> LineItemTOList = new ArrayList<LineItemTO>();
		OrderLineItems lineItemsList = commonOrderRequest.getOrderLineItems();
		List<LineItem> lineItemList  = (List<LineItem>) lineItemsList.getLineItem();
		for (LineItem lineItem : lineItemList) {
			LineItemTO lineItemTO = new LineItemTO();
			if (commonOrderRequest.getHeader().getMessageType().equals("CANORD")) {
				lineItemTO.setOperationCode(SapOrderConstants.CANCEL_FLAG);
			}
			lineItemTO.setArticleID(lineItem.getItemID());
			lineItemTO.setDescription(lineItem.getItemDesc());
			lineItemTO.setItemCategory(lineItem.getItemCategory());
			lineItemTO.setItemState(lineItem.getItemState());
			lineItemTO.setCatalog(lineItem.getCatalogs());
			lineItemTO.setVendor(lineItem.getVendor());
			lineItemTO.setThirdParty(lineItem.isIsThirdParty());
			lineItemTO.setQuantity(lineItem.getQuantity());
			lineItemTO.setPlantId(lineItem.getPlant());
			lineItemTO.setSalesUnit(lineItem.getSalesUnit());
			lineItemTO.setSapDocumentId(lineItem.getItemSno());
			lineItemTO.setStorageLocation(10);
			lineItemTO.setReasonCode(lineItem.getReason());
			
			PricingTO pricingTO = new PricingTO();
			pricingTO.setCurrency(commonOrderRequest.getOrderHeader().getCurrency());
			pricingTO.setListPrice(lineItem.getMRP());
			pricingTO.setOfferPrice(lineItem.getOfferPrice());
			pricingTO.setCouponDiscount(lineItem.getDiscount());
			pricingTO.setPayableAmount(lineItem.getAmount());
			lineItemTO.setPricingTO(pricingTO);
			setItemAddressDetails(lineItemTO,lineItem.getShipAddress());
			LineItemTOList.add(lineItemTO);
		}
		orderRequestTO.setLineItemTO(LineItemTOList);		
	}

	private void setPaymentDetails(SapOrderRequestTO orderRequestTO, CommonOrderRequest commonOrderRequest) {
		List<PaymentTO> paymentTOList = new ArrayList<PaymentTO>();
		PaymentGroups paymentGroups = commonOrderRequest.getPaymentGroups();
		List<Details> paymentDetailList = (List<Details>) paymentGroups.getDetails();
		for (Details paymentDetail : paymentDetailList) {
			PaymentTO paymentTO = new PaymentTO();
			paymentTO.setAuthCode(paymentDetail.getAuthRefCode());
			paymentTO.setBank(paymentDetail.getBankName());
			paymentTO.setInstrumentNumber(paymentDetail.getChequeNo());
			paymentTO.setMerchantID(paymentDetail.getMerchantID());
			paymentTO.setPaymentGateway(paymentDetail.getType());
			paymentTO.setPaymentMode(paymentDetail.getPayMethod());
			paymentTO.setPgTransactionID(paymentDetail.getTranRefID2());
			paymentTO.setTransactionID(paymentDetail.getTranRefID());
			paymentTO.setRRN(paymentDetail.getRRN());
			paymentTO.setValidTill(SapUtils.getDateTimeFromString(paymentDetail.getValidDate(), "yyyyMMdd"));
			PricingTO pricingTO = new PricingTO();
			pricingTO.setPayableAmount(paymentDetail.getAmount());
			paymentTO.setPricingTO(pricingTO);
			paymentTOList.add(paymentTO);
		}
		orderRequestTO.setPaymentTO(paymentTOList);
	}

	// Common Order Header Details
	private void setHeaderDetails(SapOrderRequestTO orderRequestTO, CommonOrderRequest commonOrderRequest) {
		OrderHeaderTO orderHeaderTO = new OrderHeaderTO();
		OrderHeader orderHeaderXml = commonOrderRequest.getOrderHeader();
		orderHeaderTO.setAccountNumber(orderHeaderXml.getSoldToParty());
		String location = orderHeaderXml.getLocation();
		try {
			String client = location.split("#")[0];
			orderHeaderTO.setClient(client);
			String channel = location.split("#")[1];
			orderHeaderTO.setChannelType(channel);
		} catch (Exception e) {
			logger.error("Error setting client/channel for location : " + location);
		}
		orderHeaderTO.setReferenceID(orderHeaderXml.getOrderRefID());
		orderHeaderTO.setSalesDocType(orderHeaderXml.getSalesDocType());
		orderHeaderTO.setSalesChannel(orderHeaderXml.getSalesChannel());
		orderHeaderTO.setThirdParty(orderHeaderXml.isIsThirdParty());
		//orderHeaderTO.setCreatedOn(SapUtils.getDateTimeFromString(arg0, arg1));
		//gregCal = orderHeaderXml.getDeliveryDate();
		//orderHeaderTO.setSubmittedOn(new DateTime(gregCal.getYear(), gregCal.getMonth(), gregCal.getDay(), gregCal.getHour(), gregCal.getMinute(), gregCal.getSecond()));
		PricingTO pricingTO = new PricingTO();
		pricingTO.setCouponDiscount(orderHeaderXml.getOrderDiscount());
		pricingTO.setOfferPrice(orderHeaderXml.getOrderOfferPrice());
		pricingTO.setCurrency(orderHeaderXml.getCurrency());
		try {
			setPointsDetails(orderHeaderTO, pricingTO, commonOrderRequest.getPaymentGroups().getDetails().get(0).getText2());
		} catch (Exception e) {
			logger.error("invalid payment points", e);
		}
		orderHeaderTO.setPricingTO(pricingTO);
		orderRequestTO.setOrderHeaderTO(orderHeaderTO);
	}

	private void setPointsDetails(OrderHeaderTO orderHeaderTO, PricingTO pricingTO, String pointsDetails) {
		StringTokenizer st = new StringTokenizer(pointsDetails, "||");
		orderHeaderTO.setLoyaltyCardNumber(st.nextToken());
		pricingTO.setPointsEarn(new BigDecimal(st.nextToken()));
		pricingTO.setPointsEarnValue(new BigDecimal(st.nextToken()));
		pricingTO.setPointsBurn(new BigDecimal(st.nextToken()));
		pricingTO.setPointsBurnValue(new BigDecimal(st.nextToken()));
	}

	@Path("/return")
	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	public String returnOrder(String returnOrderXml) {
		logger.info("ReturnOrderXML : \n" + returnOrderXml);
		try {	
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ReturnOrderRequest returnOrderRequest = (ReturnOrderRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(returnOrderXml)));
			SapOrderRequestTO orderRequestTO = new SapOrderRequestTO();
			orderRequestTO.setOrderType(TinlaOrderType.RET_ORDER);
			ReturnHeader returnHeader = returnOrderRequest.getReturnHeader();
			OrderHeaderTO orderHeaderTO = new OrderHeaderTO();
			orderHeaderTO.setReferenceID(returnHeader.getOriginalOrderId());
			orderHeaderTO.setReturnOrderID(returnHeader.getReturnOrderId());
			orderHeaderTO.setClient("FUTUREBAZAAR");
			PricingTO headerPricingTO = new PricingTO();
			setPointsDetails(orderHeaderTO, headerPricingTO, returnHeader.getPointsDetails());
			orderHeaderTO.setPricingTO(headerPricingTO);
			orderRequestTO.setOrderHeaderTO(orderHeaderTO);
			List<ReturnItem> returnItemList = returnOrderRequest.getReturnOrderItems().getReturnItem();
			List<LineItemTO> lineItemTOList = new ArrayList<LineItemTO>();
			for (ReturnItem returnItem : returnItemList) {
				LineItemTO lineItemTO = new LineItemTO();
				lineItemTO.setSapDocumentId(returnItem.getSapItemId());
				lineItemTO.setQuantity(returnItem.getQuantityToReturn());
				lineItemTO.setStorageLocation(returnItem.getStorageLocation());
				lineItemTO.setReasonCode(returnItem.getReturnReason());
				lineItemTO.setPlantId(returnItem.getPlant());
				PricingTO itemPricingTO = new PricingTO();
				itemPricingTO.setCouponDiscount(returnItem.getCouponDiscount());
				itemPricingTO.setExtraDiscount(returnItem.getItemDiscount());
				itemPricingTO.setShippingAmount(returnItem.getShippingCharge());
				lineItemTO.setPricingTO(itemPricingTO);
				lineItemTOList.add(lineItemTO);
			}
			orderRequestTO.setLineItemTO(lineItemTOList);
			SapOrderResponseTO orderResponseTO = sapClientHandler.processOrder(orderRequestTO);
			SapOrderResponse sapOrderResponseXml = setOrderResponseXml(orderResponseTO);
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(sapOrderResponseXml, outStringWriter);
			String xmlResponse = outStringWriter.toString();
			logger.info("ReturnOrderXML response :\n" + xmlResponse);
			return xmlResponse;
		} catch (Exception e) {
			logger.error("Error in the Sap Inventory Level call.", e);
			return "error";
		}
	}
	
	@Path("/modify")
	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	public String modifyOrder(String modifiedOrderXml) {
		logger.info("ModifiedOrderXML : \n" + modifiedOrderXml);
		
		try {	
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ModifyOrderRequest modifyOrderRequest = (ModifyOrderRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(modifiedOrderXml)));
			SapOrderRequestTO orderRequestTO = new SapOrderRequestTO();
			setHeaderDetails(orderRequestTO, modifyOrderRequest);
			orderRequestTO.setOrderType(TinlaOrderType.MOD_ORDER);
			setHeaderDetails(orderRequestTO, modifyOrderRequest);
			setItemDetails(orderRequestTO, modifyOrderRequest);
			SapOrderResponseTO orderResponseTO = sapClientHandler.processOrder(orderRequestTO);
			SapOrderResponse sapOrderResponseXml = setOrderResponseXml(orderResponseTO);
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(sapOrderResponseXml, outStringWriter);
			String xmlResponse = outStringWriter.toString();
			logger.info("ReturnOrderXML response :\n" + xmlResponse);
			return xmlResponse;
		} catch (Exception e) {
			logger.error("Error in the Sap Common Order call.", e);
			return "error";
		}
	}
	
	//Modify order item Details
	private void setItemDetails(SapOrderRequestTO orderRequestTO, ModifyOrderRequest modifyOrderRequest) {
		List<LineItemTO> lineItemTOList = new ArrayList<LineItemTO>();
		List<DelItem> modifiedItemList = modifyOrderRequest.getDeliveryItemsList().getDelItem();
		for (DelItem modifiedItem : modifiedItemList) {
			LineItemTO lineItemTO = new LineItemTO();
			lineItemTO.setArticleID(modifiedItem.getItemID());
			lineItemTO.setSapDocumentId(modifiedItem.getLineItemId());
			lineItemTO.setItemState(modifiedItem.getItemState());
			lineItemTO.setLspName(modifiedItem.getLspName());
			lineItemTO.setLspCode(modifiedItem.getLspNumber());
			lineItemTO.setDeliveryNumber(modifiedItem.getDeliveryNumber());
			lineItemTO.setQuantity(modifiedItem.getQuantity());
			lineItemTO.setReasonCode(modifiedItem.getReasonOfCancellation());
			lineItemTO.setOperationCode(modifiedItem.getOperation());
			lineItemTO.setPlantId(modifiedItem.getPlant());
			lineItemTO.setThirdParty(modifiedItem.isIsThirdParty());
			lineItemTO.setVendor(modifiedItem.getVendor());
			lineItemTO.setBundle(modifiedItem.getBundle());
			lineItemTO.setGiftVoucherDetails(modifiedItem.getGvClaimCodes());
			lineItemTO.setDeliveryDate(SapUtils.getDateTimeFromString(modifiedItem.getRequiredDeliveryDate(), "yyyyMMdd"));
			setItemPricingDetails(lineItemTO, modifiedItem);
			setItemAddressDetails(lineItemTO, modifiedItem.getShippingAddress());
			lineItemTOList.add(lineItemTO);
		}
		orderRequestTO.setLineItemTO(lineItemTOList);
	}

	private void setItemAddressDetails(LineItemTO lineItemTO, ShippingAddress shipAddress) {
		AddressTO addressTO = new AddressTO();
		addressTO.setCity(shipAddress.getCity());
		addressTO.setFirstName(shipAddress.getFirstName());
		addressTO.setLastName(shipAddress.getLastName());
		addressTO.setPincode(shipAddress.getPostalCode());
		addressTO.setPrimaryTelephone(shipAddress.getTelephone1());
		addressTO.setSecondaryTelephone(shipAddress.getTelephone2());
		addressTO.setState(shipAddress.getState());
		addressTO.setAddress(shipAddress.getAddress1());
		addressTO.setCountry(shipAddress.getCountry());
		lineItemTO.setAddressTO(addressTO);
	}

	private void setItemPricingDetails(LineItemTO lineItemTO, DelItem modifiedItem) {
		PricingTO pricingTO = new PricingTO();
		pricingTO.setExtraDiscount(modifiedItem.getDiscount());
		pricingTO.setCouponDiscount(modifiedItem.getCouponDiscount());
		pricingTO.setCommissionAmount(modifiedItem.getItemCommisionAmount());
		pricingTO.setShippingAmount(modifiedItem.getShippingPrice());
		pricingTO.setListPrice(modifiedItem.getListPrice());
		pricingTO.setOfferPrice(modifiedItem.getSalesPrice());
		pricingTO.setNlc(modifiedItem.getNlc());
		lineItemTO.setPricingTO(pricingTO);
	}

	//Modified order Header Details
	private void setHeaderDetails(SapOrderRequestTO orderRequestTO, ModifyOrderRequest modifyOrderRequest) {
		OrderHeaderTO orderHeaderTO = new OrderHeaderTO();
		ModifiedOrderHeader modifiedOrderHeader = modifyOrderRequest.getModifiedOrderHeader();
		orderHeaderTO.setClient(modifiedOrderHeader.getClient());
		orderHeaderTO.setLoyaltyCardNumber(modifiedOrderHeader.getLoyaltyCardNumber());
		orderHeaderTO.setReferenceID(modifiedOrderHeader.getOrderId());
		orderHeaderTO.setAccountNumber(modifiedOrderHeader.getBpNumber());
		orderHeaderTO.setCreatedOn(SapUtils.getDateTimeFromString(modifiedOrderHeader.getDateOfCreation(), "yyyyMMdd"));
		orderHeaderTO.setSubmittedOn(SapUtils.getDateTimeFromString(modifiedOrderHeader.getDateOfSubmission(), "yyyyMMdd"));
		PricingTO pricingTO = new PricingTO();
		pricingTO.setPointsEarn(modifiedOrderHeader.getPointsEarn());
		pricingTO.setPointsBurn(modifiedOrderHeader.getPointsBurn());
		pricingTO.setPointsEarnValue(modifiedOrderHeader.getPointsEarnValue());
		pricingTO.setShippingAmount(modifiedOrderHeader.getShippingAmount());
		pricingTO.setPayableAmount(modifiedOrderHeader.getOrderTotal());
		pricingTO.setExtraDiscount(modifiedOrderHeader.getDiscount());
		orderHeaderTO.setPricingTO(pricingTO);
		orderRequestTO.setOrderHeaderTO(orderHeaderTO);
		
	}

	public void setSapClientHandler(SapClientHandler sapClientHandler) {
		this.sapClientHandler = sapClientHandler;
	}

}
