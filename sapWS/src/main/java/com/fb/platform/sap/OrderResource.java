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
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap._1_0.CommonOrderRequest;
import com.fb.platform.sap._1_0.Details;
import com.fb.platform.sap._1_0.InventoryLevelRequest;
import com.fb.platform.sap._1_0.InventoryLevelResponse;
import com.fb.platform.sap._1_0.OrderHeader;
import com.fb.platform.sap._1_0.PaymentGroups;
import com.fb.platform.sap.bapi.factory.SapOrderConfigFactory;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
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
			//setAddressDetails(orderRequestTO, commonOrderRequest);
			//setPointsDetails(orderRequestTO, commonOrderRequest);
			SapOrderResponseTO orderResponseTO = sapClientHandler.processOrder(orderRequestTO);
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(null, outStringWriter);
			String xmlResponse = outStringWriter.toString();
			logger.info("Sap InventoryLevelXML response :\n" + xmlResponse);
			return xmlResponse;
		} catch (Exception e) {
			logger.error("Error in the Sap Inventory Level call.", e);
			return "error";
		}
	}
	
	private void setLineItemDetails(SapOrderRequestTO orderRequestTO, CommonOrderRequest commonOrderRequest) {
		
	}

	private void setPaymentDetails(SapOrderRequestTO orderRequestTO, CommonOrderRequest commonOrderRequest) {
		List<PaymentTO> paymentTOList = new ArrayList<PaymentTO>();
		PaymentGroups paymentGroups = commonOrderRequest.getPaymentGroups();
		List<Details> paymentDetailList = paymentGroups.getDetails();
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
		try {
			String loyaltyDetails = orderHeaderXml.getText2();
			orderHeaderTO.setLoyaltyCardNumber(loyaltyDetails.split("||")[0]);
		} catch (Exception e) {
			logger.info("No card details found");
		}
		
		PricingTO pricingTO = new PricingTO();
		pricingTO.setCouponDiscount(orderHeaderXml.getOrderDiscount());
		pricingTO.setOfferPrice(orderHeaderXml.getOrderOfferPrice());
		pricingTO.setCurrency(orderHeaderXml.getCurrency());
		orderHeaderTO.setPricingTO(pricingTO);
		
		orderRequestTO.setOrderHeaderTO(orderHeaderTO);
	}

	public void setSapClientHandler(SapClientHandler sapClientHandler) {
		this.sapClientHandler = sapClientHandler;
	}

}
