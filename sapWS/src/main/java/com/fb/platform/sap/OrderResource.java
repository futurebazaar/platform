package com.fb.platform.sap;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap._1_0.BillingAddress;
import com.fb.platform.sap._1_0.Item;
import com.fb.platform.sap._1_0.LineItems;
import com.fb.platform.sap._1_0.OrderHeader;
import com.fb.platform.sap._1_0.OrderXmlRequest;
import com.fb.platform.sap._1_0.PaymentAttempt;
import com.fb.platform.sap._1_0.PaymentHeader;
import com.fb.platform.sap._1_0.ReturnHeader;
import com.fb.platform.sap._1_0.ReturnItem;
import com.fb.platform.sap._1_0.ReturnOrderRequest;
import com.fb.platform.sap._1_0.SapOrderResponse;
import com.fb.platform.sap._1_0.ShippingAddress;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.handler.PlatformClientHandler;

@Path("/order/")
@Component
@Scope("request")
public class OrderResource {

	private static Log logger = LogFactory.getLog(OrderResource.class);
	
	@Autowired
	private PlatformClientHandler sapClientHandler = null;
	
	public void setSapClientHandler(PlatformClientHandler sapClientHandler) {
		this.sapClientHandler = sapClientHandler;
	}
	
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
	
	@Path("/common/")
	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	public String writeOrder(String commonOrderXml) {
		logger.info("CommonOrderXML : \n" + commonOrderXml);
		try {	
			Unmarshaller unmarshaller = context.createUnmarshaller();
			OrderXmlRequest orderXmlRequest = (OrderXmlRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(commonOrderXml)));
			SapOrderRequestTO orderRequestTO = new SapOrderRequestTO();
			orderRequestTO.setOrderType(TinlaOrderType.NEW_ORDER);
			if (!orderXmlRequest.getOrderType().equals("NEW_ORDER")) {
				orderRequestTO.setOrderType(TinlaOrderType.MOD_ORDER);
			}
			setHeaderDetails(orderRequestTO, orderXmlRequest);
			setPaymentDetails(orderRequestTO, orderXmlRequest);
			setLineItemDetails(orderRequestTO, orderXmlRequest);
			orderRequestTO.setDefaultShippingAddressTO(getAddressTO(orderXmlRequest.getShippingAddress(), null));
			orderRequestTO.setBillingAddressTO(getAddressTO(null, orderXmlRequest.getBillingAddress()));
			SapOrderResponseTO orderResponseTO = sapClientHandler.processOrder(orderRequestTO);
			SapOrderResponse sapOrderResponseXml = setOrderResponseXml(orderResponseTO);
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(sapOrderResponseXml, outStringWriter);
			String xmlResponse = outStringWriter.toString();
			logger.info("CommonOrderXml response :\n" + xmlResponse);
			return xmlResponse;
		} catch (JAXBException e) {
			logger.error("Error in Sap Common Call", e);
			return "unmarshal";
		} catch (Exception e) {
			logger.error("Error in Sap Common Call", e);
			return "error";
		}
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

	private void setHeaderDetails(SapOrderRequestTO orderRequestTO, OrderXmlRequest orderXmlRequest) {
		OrderHeaderTO orderHeaderTO = new OrderHeaderTO();
		OrderHeader orderHeaderXml = orderXmlRequest.getOrderHeader();
		orderHeaderTO.setAccountNumber(orderHeaderXml.getAccountNumber());
		orderHeaderTO.setClient(orderHeaderXml.getClient().trim().toUpperCase());
		orderHeaderTO.setChannelType(orderHeaderXml.getChannel());
		orderHeaderTO.setReferenceID(orderHeaderXml.getReferenceOrderId());
		orderHeaderTO.setSalesDocType(orderHeaderXml.getSalesDocType());
		orderHeaderTO.setCreatedOn(SapUtils.getDateTimeFromString(orderHeaderXml.getCreationDate(), "yyyyMMdd"));
		orderHeaderTO.setLoyaltyCardNumber(orderHeaderXml.getLoyaltyCardNumber());
		
		PricingTO pricingTO = new PricingTO();
		pricingTO.setCouponDiscount(orderHeaderXml.getDiscount());
		pricingTO.setOfferPrice(orderHeaderXml.getOfferPrice());
		pricingTO.setPayableAmount(orderHeaderXml.getPayableAmount());
		pricingTO.setCurrency(orderHeaderXml.getCurrency());
		pricingTO.setPointsEarn(orderHeaderXml.getPointsEarn());
		pricingTO.setPointsEarnValue(orderHeaderXml.getPointsEarnValue());
		pricingTO.setPointsBurn(orderHeaderXml.getPointsBurn());
		pricingTO.setPointsBurnValue(orderHeaderXml.getPointsBurnValue());
		orderHeaderTO.setPricingTO(pricingTO);
		orderRequestTO.setOrderHeaderTO(orderHeaderTO);
	}

	private void setPaymentDetails(SapOrderRequestTO orderRequestTO, OrderXmlRequest orderXmlRequest) {
		List<PaymentTO> paymentTOList = new ArrayList<PaymentTO>();
		PaymentHeader paymentHeader = orderXmlRequest.getPaymentHeader();
		List<PaymentAttempt> paymentAttemptList = (List<PaymentAttempt>) paymentHeader.getPaymentAttempt();
		for (PaymentAttempt paymentAttempt : paymentAttemptList) {
			PaymentTO paymentTO = new PaymentTO();
			paymentTO.setAuthCode(paymentAttempt.getAuthCode());
			paymentTO.setBank(paymentAttempt.getBank());
			paymentTO.setInstrumentNumber(paymentAttempt.getInstrumentNumber());
			paymentTO.setMerchantID(paymentAttempt.getMerchantId());
			paymentTO.setPaymentGateway(paymentAttempt.getGateway());
			paymentTO.setPaymentMode(paymentAttempt.getPaymentMode());
			paymentTO.setPgTransactionID(paymentAttempt.getPgTransactionReferenceId());
			paymentTO.setTransactionID(paymentAttempt.getTransactionReferenceId());
			paymentTO.setRRN(paymentAttempt.getRRN());
			paymentTO.setValidTill(SapUtils.getDateTimeFromString(paymentAttempt.getValidDate(), "yyyyMMdd"));
			PricingTO pricingTO = new PricingTO();
			pricingTO.setPayableAmount(paymentAttempt.getAmount());
			paymentTO.setPricingTO(pricingTO);
			paymentTOList.add(paymentTO);
		}
		orderRequestTO.setPaymentTO(paymentTOList);
	}

	private void setLineItemDetails(SapOrderRequestTO orderRequestTO, OrderXmlRequest orderXmlRequest) {
	List<LineItemTO> LineItemTOList = new ArrayList<LineItemTO>();
	LineItems lineItemsList = orderXmlRequest.getLineItems();
	List<Item> lineItemList  = lineItemsList.getItem();
	for (Item lineItem : lineItemList) {
		LineItemTO lineItemTO = new LineItemTO();
		lineItemTO.setOperationCode(lineItem.getOperation());
		lineItemTO.setArticleID(lineItem.getArticleId());
		lineItemTO.setDescription(lineItem.getItemDescription());
		lineItemTO.setItemCategory(lineItem.getItemCategory());
		lineItemTO.setVendor(lineItem.getVendor());
		lineItemTO.setQuantity(lineItem.getQuantity());
		lineItemTO.setPlantId(lineItem.getPlant());
		lineItemTO.setSalesUnit(lineItem.getSalesUnit());
		lineItemTO.setSapDocumentId(lineItem.getSapDocumentId());
		lineItemTO.setStorageLocation(10);
		lineItemTO.setReasonCode(lineItem.getReasonCode());
		lineItemTO.setGiftVoucherDetails(lineItem.getGV());
		lineItemTO.setShippingMode(lineItem.getShippingMode());
		lineItemTO.setLspCode(lineItem.getLspCode());
		lineItemTO.setRequiredDeliveryDate(SapUtils.getDateTimeFromString(lineItem.getRequiredDeliveryDate(), "yyyyMMdd"));
		lineItemTO.setRelatedCategory(lineItem.getWarrantyCategory());
		
		PricingTO pricingTO = new PricingTO();
		pricingTO.setCurrency(orderXmlRequest.getOrderHeader().getCurrency());
		pricingTO.setListPrice(lineItem.getListPrice());
		pricingTO.setOfferPrice(lineItem.getOfferPrice());
		pricingTO.setCouponDiscount(lineItem.getCouponDiscount());
		pricingTO.setExtraDiscount(lineItem.getDiscount());
		pricingTO.setPayableAmount(lineItem.getAmount());
		pricingTO.setNlc(lineItem.getNlc());
		pricingTO.setWarrantyPrice(lineItem.getWarrantyPrice());
		pricingTO.setShippingAmount(lineItem.getShippingCharge());
		lineItemTO.setPricingTO(pricingTO);
		lineItemTO.setAddressTO(getAddressTO(lineItem.getShippingAddress(), null));
		LineItemTOList.add(lineItemTO);
	}
	orderRequestTO.setLineItemTO(LineItemTOList);		
}

	private AddressTO getAddressTO(ShippingAddress shippingAddress, BillingAddress billingAddress) {
		AddressTO addressTO = new AddressTO();
		if (shippingAddress != null) {
			addressTO.setCity(shippingAddress.getCity());
			addressTO.setFirstName(shippingAddress.getFirstName());
			addressTO.setLastName(shippingAddress.getLastName());
			addressTO.setPincode(shippingAddress.getPincode());
			addressTO.setPrimaryTelephone(shippingAddress.getTelephone1());
			addressTO.setSecondaryTelephone(shippingAddress.getTelephone2());
			addressTO.setState(shippingAddress.getState());
			addressTO.setAddress(shippingAddress.getAddress1());
			addressTO.setCountry(shippingAddress.getCountry());
		} else if (billingAddress != null) {
			addressTO.setCity(billingAddress.getCity());
			addressTO.setFirstName(billingAddress.getFirstName());
			addressTO.setLastName(billingAddress.getLastName());
			addressTO.setPincode(billingAddress.getPincode());
			addressTO.setPrimaryTelephone(billingAddress.getTelephone1());
			addressTO.setSecondaryTelephone(billingAddress.getTelephone2());
			addressTO.setState(billingAddress.getState());
			addressTO.setAddress(billingAddress.getAddress1());
			addressTO.setCountry(billingAddress.getCountry());
		}
		return addressTO;
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
			orderHeaderTO.setLoyaltyCardNumber(returnHeader.getLoyaltyCardNumber());
			
			PricingTO headerPricingTO = new PricingTO();
			headerPricingTO.setPointsBurn(returnHeader.getPointsBurn());
			headerPricingTO.setPointsBurnValue(returnHeader.getPointsBurnValue());
			headerPricingTO.setPointsEarn(returnHeader.getPointsEarn());
			headerPricingTO.setPointsEarnValue(returnHeader.getPointsEarnValue());
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
		} catch (JAXBException e) {
			logger.error("Error in Sap Return Call", e);
			return "unmarshal";
		} catch (Exception e) {
			logger.error("Error in Sap Return Call", e);
			return "error";
		}
	}
}
