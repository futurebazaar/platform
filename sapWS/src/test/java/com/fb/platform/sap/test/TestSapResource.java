package com.fb.platform.sap.test;

import java.io.StringWriter;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.junit.Test;

import com.fb.platform.sap.OrderResource;
import com.fb.platform.sap._1_0.AwbUpdateRequest;
import com.fb.platform.sap._1_0.InventoryDashboardRequest;
import com.fb.platform.sap._1_0.InventoryLevelRequest;
import com.fb.platform.sap.client.connector.impl.SapClientConnector;
import com.fb.platform.sap.client.handler.impl.SapClientHandler;

public class TestSapResource {
	
	private static Log logger = LogFactory.getLog(OrderResource.class);
	
	private String getInventoryLevelXml() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.sap._1_0");
		InventoryLevelRequest request = new InventoryLevelRequest();
		request.setMaterial("300000560");
		request.setStorageLocation(10);
		request.setPlant("2786");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);
		return sw.toString();
	}
	
	private String getInventoryDashboardXml() throws JAXBException, DatatypeConfigurationException {
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.sap._1_0");
		InventoryDashboardRequest request = new InventoryDashboardRequest();
		request.setArticle("000000000300000560");
		GregorianCalendar gregCal = new DateTime(2012, 9, 4, 0, 0, 0).toGregorianCalendar();
		request.setFromDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		//request.setToDateTime(new DateTime(2012, 9, 13, 23, 59, 59));
		gregCal = DateTime.now().toGregorianCalendar();
		request.setToDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		request.setPlant("");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);
		return sw.toString();
	}
	
	private String getLspAwbUpdateXml() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.sap._1_0");
		AwbUpdateRequest request = new AwbUpdateRequest();
		request.setAwb("1234");
		request.setDeliveryNumber("8000018246");
		request.setLspCode("1234");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);
		return sw.toString();
		
	}
	
	private String getCommonOrderXml() {
		String commonXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><orderXmlRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><orderType>NEW_ORDER</orderType><orderHeader><referenceOrderId>5049999975</referenceOrderId><thirdPartyOrder /><creationDate>20120128</creationDate><accountNumber>2100000103</accountNumber><salesDocType>ZATG</salesDocType><currency>INR</currency><discount>0.00</discount><offerPrice>17980.00</offerPrice><payableAmount>17980.00</payableAmount><client>FUTUREBAZAAR</client><channel>WEBSITE</channel><loyaltyCardNumber>1234123412341234</loyaltyCardNumber><pointsEarn>10</pointsEarn><pointsBurn>0</pointsBurn><pointsEarnValue>3</pointsEarnValue><pointsBurnValue>0</pointsBurnValue></orderHeader><paymentHeader><paymentAttempt><paymentMode>credit-card</paymentMode><gateway>ICI3</gateway><amount>26970.00</amount><paymentTime>20120128T00:00:00</paymentTime><merchantId /><transactionId>20120128153513277451245055843910</transactionId><pgTransactionId>abcde1245</pgTransactionId><authCode /><currency>INR</currency><RRN /><bank>icici</bank><instrumentNo>47899</instrumentNo><ValidDate>20120126</ValidDate></paymentAttempt></paymentHeader><shippingAddress><firstName>Pradyumna Kumar</firstName><middleName /><lastName /><address1>TEST ORDER PLEASE CANCEL</address1><address2 /><city>Mumbai</city><state>13</state><country>IN</country><pincode>400008</pincode><telephone1>9967442905</telephone1><telephone2 /></shippingAddress><billingAddress><firstName>Pradyumna</firstName><middleName /><lastName>Kumar</lastName><address1>TEST ORDER PLEASE CANCEL</address1><address2 /><city>Mumbai</city><state>13</state><country>IN</country><pincode>400008</pincode><telephone1>9967442905</telephone1><telephone2 /></billingAddress><lineItems><item><sapDocumentId>10</sapDocumentId><articleId>000000000300000560</articleId><itemDescription>Electrolux EJ30CSS6 30L Microwave Oven + Starter kit</itemDescription><quantity>2</quantity><reqDeliveryDate /><salesUnit>EA</salesUnit><itemCategory /><vendor /><listPrice>13290.00</listPrice><offerPrice>8990.00</offerPrice><amount>17980.00</amount><discount /><nlc /><plant>2786</plant><shippingMode></shippingMode><lspCode>0000300413</lspCode><shippingCharge>0.00</shippingCharge><operation /><reasonCode /><GV /><warrantyCategory /><warrantyPrice /><couponDiscount>0.00</couponDiscount><commissionAmount>0.00</commissionAmount><shippingAddress><firstName>Pradyumna Kumar</firstName><middleName /><lastName>Kumar</lastName><address1>TEST ORDER PLEASE CANCEL</address1><address2 /><city>Mumbai</city><state>13</state><country>IN</country><pincode>400008</pincode><telephone1>9967442905</telephone1><telephone2 /></shippingAddress></item></lineItems></orderXmlRequest>";
		String bbCommonXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><orderXmlRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><orderType>NEW_ORDER</orderType><orderHeader><referenceOrderId>I049999980</referenceOrderId><thirdPartyOrder /><creationDate>20120128</creationDate><accountNumber>G100000103</accountNumber><salesDocType>ZFGB</salesDocType><currency>INR</currency><discount>0.00</discount><offerPrice>17980.00</offerPrice><payableAmount>17980.00</payableAmount><client>BIGBAZAAR</client><channel>WEBSITE</channel><loyaltyCardNumber>1234123412341234</loyaltyCardNumber><pointsEarn>10</pointsEarn><pointsBurn>0</pointsBurn><pointsEarnValue>3</pointsEarnValue><pointsBurnValue>0</pointsBurnValue></orderHeader><paymentHeader><paymentAttempt><paymentMode>credit-card</paymentMode><gateway>ICI3</gateway><amount>26970.00</amount><paymentTime>20120128T00:00:00</paymentTime><merchantId /><transactionId>20120128153513277451245055843910</transactionId><pgTransactionId>abcde1245</pgTransactionId><authCode /><currency>INR</currency><RRN /><bank>icici</bank><instrumentNo>47899</instrumentNo><ValidDate>20120126</ValidDate></paymentAttempt></paymentHeader><shippingAddress><firstName>Pradyumna Kumar</firstName><middleName /><lastName /><address1>TEST ORDER PLEASE CANCEL</address1><address2 /><city>Mumbai</city><state>13</state><country>IN</country><pincode>400008</pincode><telephone1>9967442905</telephone1><telephone2 /></shippingAddress><billingAddress><firstName>Pradyumna</firstName><middleName /><lastName>Kumar</lastName><address1>TEST ORDER PLEASE CANCEL</address1><address2 /><city>Mumbai</city><state>13</state><country>IN</country><pincode>400008</pincode><telephone1>9967442905</telephone1><telephone2 /></billingAddress><lineItems><item><sapDocumentId>10</sapDocumentId><articleId>000000000100000002</articleId><itemDescription>Electrolux EJ30CSS6 30L Microwave Oven + Starter kit</itemDescription><quantity>2</quantity><reqDeliveryDate /><salesUnit>EA</salesUnit><itemCategory /><vendor /><listPrice>13290.00</listPrice><offerPrice>8990.00</offerPrice><amount>17980.00</amount><discount /><nlc /><plant>4608</plant><shippingMode></shippingMode><lspCode></lspCode><shippingCharge>0.00</shippingCharge><operation /><reasonCode /><GV /><warrantyCategory /><warrantyPrice /><couponDiscount>0.00</couponDiscount><commissionAmount>0.00</commissionAmount><shippingAddress><firstName>Pradyumna Kumar</firstName><middleName /><lastName>Kumar</lastName><address1>TEST ORDER PLEASE CANCEL</address1><address2 /><city>Mumbai</city><state>13</state><country>IN</country><pincode>400008</pincode><telephone1>9967442905</telephone1><telephone2 /></shippingAddress></item></lineItems></orderXmlRequest>";
		return commonXml;
	}
	
	private String getReturnOrderXml() {
		String returnXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><returnOrderRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><returnHeader><originalOrderId>5049999897</originalOrderId><returnOrderId>6607187175</returnOrderId><loyaltyCardNumber>1234123412341234</loyaltyCardNumber><pointsEarn>10</pointsEarn><pointsBurn>0</pointsBurn><pointsEarnValue>3</pointsEarnValue><pointsBurnValue>0</pointsBurnValue></returnHeader><returnOrderItems><returnItem><quantityToReturn>1</quantityToReturn><sapItemId>20</sapItemId><storageLocation>10</storageLocation><shippingCharge>0.00</shippingCharge><itemDiscount>0.00</itemDiscount><couponDiscount>0.00</couponDiscount><plant>9010</plant><returnReason>110</returnReason></returnItem><returnItem><quantityToReturn>1</quantityToReturn><sapItemId>30</sapItemId><storageLocation>10</storageLocation><shippingCharge>0.00</shippingCharge><itemDiscount>0.00</itemDiscount><couponDiscount>0.00</couponDiscount><plant>9010</plant><returnReason>110</returnReason></returnItem></returnOrderItems></returnOrderRequest>";
		return returnXml;
	}
	
	@Test
	public void dummy() {
		SapClientConnector sapClientConnector = new SapClientConnector();
		SapClientHandler sapClientHandler = new SapClientHandler();
		sapClientHandler.setSapClientConnector(sapClientConnector);
		OrderResource orderResource = new OrderResource();
		orderResource.setSapClientHandler(sapClientHandler);
		//System.out.println(orderResource.writeOrder(getCommonOrderXml()));
		//System.out.println(orderResource.returnOrder(getReturnOrderXml()));
		
	}
}
