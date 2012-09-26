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
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap._1_0.CommonOrderRequest;
import com.fb.platform.sap._1_0.Details;
import com.fb.platform.sap._1_0.LineItem;
import com.fb.platform.sap._1_0.OrderHeader;
import com.fb.platform.sap._1_0.OrderLineItems;
import com.fb.platform.sap._1_0.PaymentGroups;
import com.fb.platform.sap._1_0.ReturnHeader;
import com.fb.platform.sap._1_0.ReturnItem;
import com.fb.platform.sap._1_0.ReturnOrderRequest;
import com.fb.platform.sap._1_0.SapOrderResponse;
import com.fb.platform.sap._1_0.ShipAddress;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;
import com.fb.platform.sap.client.handler.impl.SapClientHandler;

@Path("/order/")
@Component
@Scope("request")
public class OrderResource {
	
	private static Log logger = LogFactory.getLog(InventoryResource.class);
	
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
	
	@Path("/new")
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
			setHeaderDetails(orderRequestTO, commonOrderRequest);
			setPaymentDetails(orderRequestTO, commonOrderRequest);
			setLineItemDetails(orderRequestTO, commonOrderRequest);
			SapOrderResponseTO orderResponseTO = sapClientHandler.processOrder(orderRequestTO);
			SapOrderResponse sapOrderResponseXml = setOrderResponseXml(orderResponseTO);
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(sapOrderResponseXml, outStringWriter);
			String xmlResponse = outStringWriter.toString();
			logger.info("CommonOrderXml response :\n" + xmlResponse);
			return xmlResponse;
		} catch (Exception e) {
			logger.error("Error in the Sap Inventory Level call.", e);
			return "error";
		}
	}
	
	private void setAddressDetails(SapOrderRequestTO orderRequestTO,ShipAddress shipAddress) {
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
		setAddressDetails(orderRequestTO,lineItemList.get(0).getShipAddress());
		for (LineItem lineItem : lineItemList) {
			LineItemTO lineItemTO = new LineItemTO();
			lineItemTO.setArticleID(lineItem.getItemID());
			lineItemTO.setDescription(lineItem.getItemDesc());
			lineItemTO.setItemCategory(lineItem.getItemCategory());
			lineItemTO.setItemState(lineItem.getItemState());
			lineItemTO.setCatalog(lineItem.getCatalogs());
			lineItemTO.setVendor(lineItem.getVendor());
			lineItemTO.setThirdParty(lineItem.isIsThirdParty());
			
			PricingTO pricingTO = new PricingTO();
			pricingTO.setCurrency(commonOrderRequest.getOrderHeader().getCurrency());
			pricingTO.setListPrice(lineItem.getMRP());
			pricingTO.setOfferPrice(lineItem.getOfferPrice());
			pricingTO.setCouponDiscount(lineItem.getDiscount());
			pricingTO.setPayableAmount(lineItem.getAmount());
			//pricingTO.setPointsEarn(lineItem.get);
			//pricingTO.setPointsEarnValue(new BigDecimal("10"));
			lineItemTO.setPricingTO(pricingTO);
			lineItemTO.setQuantity(new BigDecimal(lineItem.getQuantity()));
			lineItemTO.setPlantId(lineItem.getPlant());
			lineItemTO.setSalesUnit(lineItem.getSalesUnit());
			lineItemTO.setSapDocumentId(lineItem.getItemSno());
			lineItemTO.setStorageLocation(10);
			lineItemTO.setReasonCode(lineItem.getReason());
			lineItemTO.setOperationCode("C");
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
			XMLGregorianCalendar gregCal = paymentDetail.getValidDate();
			paymentTO.setValidTill(new DateTime(gregCal.getYear(), gregCal.getMonth(), gregCal.getDay(), gregCal.getHour(), gregCal.getMinute(), gregCal.getSecond()));
			paymentTOList.add(paymentTO);
		}
		orderRequestTO.setPaymentTO(paymentTOList);
	}

	private void setHeaderDetails(SapOrderRequestTO orderRequestTO, CommonOrderRequest commonOrderRequest) {
		OrderHeaderTO orderHeaderTO = new OrderHeaderTO();
		OrderHeader orderHeaderXml = commonOrderRequest.getOrderHeader();
		orderHeaderTO.setAccountNumber(orderHeaderXml.getSoldToParty());
		String location = orderHeaderXml.getLocation();
		String client = location.split("#")[0];
		String channel = location.split("#")[1];
		orderHeaderTO.setChannelType(channel);
		orderHeaderTO.setClient(client);
		orderHeaderTO.setReferenceID(orderHeaderXml.getOrderRefID());
		orderHeaderTO.setSalesDocType(orderHeaderXml.getSalesDocType());
		orderHeaderTO.setSalesChannel(orderHeaderXml.getSalesChannel());
		orderHeaderTO.setThirdParty(orderHeaderXml.isIsThirdParty());
		XMLGregorianCalendar gregCal = orderHeaderXml.getCreateDate();
		orderHeaderTO.setCreatedOn(new DateTime(gregCal.getYear(), gregCal.getMonth(), gregCal.getDay(), gregCal.getHour(), gregCal.getMinute(), gregCal.getSecond()));
		gregCal = orderHeaderXml.getDeliveryDate();
		orderHeaderTO.setSubmittedOn(new DateTime(gregCal.getYear(), gregCal.getMonth(), gregCal.getDay(), gregCal.getHour(), gregCal.getMinute(), gregCal.getSecond()));
		
		PricingTO pricingTO = new PricingTO();
		pricingTO.setCouponDiscount(orderHeaderXml.getOrderDiscount());
		pricingTO.setOfferPrice(orderHeaderXml.getOrderOfferPrice());
		pricingTO.setCurrency(orderHeaderXml.getCurrency());
		setPointsDetails(orderHeaderTO, pricingTO, orderHeaderXml.getText2());
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
			orderRequestTO.setOrderHeaderTO(orderHeaderTO);
			
			PricingTO headerPricingTO = new PricingTO();
			setPointsDetails(orderHeaderTO, headerPricingTO, returnHeader.getPointsDetails());
			orderRequestTO.setPricingTO(headerPricingTO);
			
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
			ReturnOrderRequest returnOrderRequest = (ReturnOrderRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(modifiedOrderXml)));
			SapOrderRequestTO orderRequestTO = new SapOrderRequestTO();
			orderRequestTO.setOrderType(TinlaOrderType.RET_ORDER);
			
			ReturnHeader returnHeader = returnOrderRequest.getReturnHeader();
			OrderHeaderTO orderHeaderTO = new OrderHeaderTO();
			orderHeaderTO.setReferenceID(returnHeader.getOriginalOrderId());
			orderHeaderTO.setReturnOrderID(returnHeader.getReturnOrderId());
			orderRequestTO.setOrderHeaderTO(orderHeaderTO);
			
			PricingTO headerPricingTO = new PricingTO();
			setPointsDetails(orderHeaderTO, headerPricingTO, returnHeader.getPointsDetails());
			orderRequestTO.setPricingTO(headerPricingTO);
			
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
	
	public void setSapClientHandler(SapClientHandler sapClientHandler) {
		this.sapClientHandler = sapClientHandler;
	}

}
