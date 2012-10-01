package com.fb.platform.sap.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.junit.Test;

import com.fb.platform.sap._1_0.AwbUpdateRequest;
import com.fb.platform.sap._1_0.CommonOrderRequest;
import com.fb.platform.sap._1_0.InventoryDashboardRequest;
import com.fb.platform.sap._1_0.InventoryLevelRequest;
import com.fb.platform.sap._1_0.ModifyOrderRequest;
import com.fb.platform.sap._1_0.ReturnOrderRequest;

public class TestSapResource {
	
	public String getInventoryLevelXml() throws JAXBException {
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
	
	public String getInventoryDashboardXml() throws JAXBException, DatatypeConfigurationException {
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
	
	public String getLspAwbUpdateXml() throws JAXBException {
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
	
	public String getReturnOrderXml() {
		String returnXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><returnOrderRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><returnHeader><MessageType>RETORD</MessageType><originalOrderId>5049999897</originalOrderId><returnOrderId>6607187175</returnOrderId><points_details>9401153228288109||19||633||0||0</points_details></returnHeader><returnOrderItems><returnItem><quantityToReturn>1</quantityToReturn><sapItemId>20</sapItemId><storageLocation>10</storageLocation><shippingCharge>0.00</shippingCharge><itemDiscount>0.00</itemDiscount><couponDiscount>0.00</couponDiscount><plant>9010</plant><returnReason>110</returnReason></returnItem><returnItem><quantityToReturn>1</quantityToReturn><sapItemId>30</sapItemId><storageLocation>10</storageLocation><shippingCharge>0.00</shippingCharge><itemDiscount>0.00</itemDiscount><couponDiscount>0.00</couponDiscount><plant>9010</plant><returnReason>110</returnReason></returnItem></returnOrderItems></returnOrderRequest>";
		return returnXml;
	}
	
	public String getCommonOrderXml() {
		String commonXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><commonOrderRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><Header><MessageType>NEWORD</MessageType><BussOpr /></Header><OrderHeader><OrderRefID>5055843910</OrderRefID><OrderRefID2 /><OrderRefID3 /><CreateDate>20120128</CreateDate><OrderType>order</OrderType><DeliveryDate /><SoldToParty>2100000103</SoldToParty><SalesDocType>ZATG</SalesDocType><SalesChannel>true</SalesChannel><SalesOrganisation>false</SalesOrganisation><isThirdParty>false</isThirdParty><Currency>INR</Currency><OrderDiscount>0.00</OrderDiscount><OrderOfferPrice>17980.00</OrderOfferPrice><Reason /><Location>FUTUREBAZAAR</Location><Text1>false</Text1><Text2 /><Text3 /></OrderHeader><PaymentGroups><Details><PayMethod>CHEQ</PayMethod><Type>icici</Type><Amount>26970.00</Amount><AuthAmount>26970.00</AuthAmount><AuthDate>20120128</AuthDate><AuthTime>172225</AuthTime><ReAmount /><MerchantID /><TranRefID>20120128153513277451245055843910</TranRefID><TranRefID2 /><TranRefID3 /><AuthRefCode /><Currency>INR</Currency><RRN /><BankName>icici</BankName><AccNo /><ChequeNo>47899</ChequeNo><ValidDate>20120126</ValidDate><Status /><RefCode /><Text1 /><Text2 /><Text3 /></Details></PaymentGroups><DefaultShippingAddress><FirstName>Pradyumna Kumar</FirstName><MiddleName /><LastName /><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></DefaultShippingAddress><Customer><CustomerRole>AG</CustomerRole><CustomerID>826468</CustomerID><FirstName>Pradyumna</FirstName><MiddleName /><LastName>Kumar</LastName><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></Customer><OrderLineItems><LineItem><ItemSno>10</ItemSno><ItemID>000000000300000560</ItemID><ItemDesc>Electrolux EJ30CSS6 30L Microwave Oven + Starter kit</ItemDesc><RefID /><ShipGroupCode /><Quantity>2</Quantity><ReqDelivDate /><ItemState /><SalesUnit /><ItemCategory /><isThirdParty>false</isThirdParty><Vendor /><MRP>13290.00</MRP><SalesPrice>8990.00</SalesPrice><OfferPrice>8990.00</OfferPrice><Amount>17980.00</Amount><Discount /><NLC /><Plant>2786</Plant><Catalogs>|FutureBazaar</Catalogs><ProductGroupId /><ModeOfTransport /><ShippingMode /><LSP /><ShipCharge>0.00</ShipCharge><EANUPC /><Reason /><Notes /><ItemSno1 /><GV /><Bundle /><ShipAddress><FirstName>Pradyumna Kumar</FirstName><MiddleName /><LastName /><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></ShipAddress><BackorderText /><Text1>true</Text1><Text2 /><Text3 /></LineItem></OrderLineItems></commonOrderRequest>";
		String bbCommonXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><commonOrderRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><Header><MessageType>NEWORD</MessageType><BussOpr /></Header><OrderHeader><OrderRefID>I000001022</OrderRefID><OrderRefID2 /><OrderRefID3 /><CreateDate>20120128</CreateDate><OrderType>order</OrderType><DeliveryDate /><SoldToParty>G100000103</SoldToParty><SalesDocType>ZFGB</SalesDocType><SalesChannel>true</SalesChannel><SalesOrganisation>false</SalesOrganisation><isThirdParty>false</isThirdParty><Currency>INR</Currency><OrderDiscount>0.00</OrderDiscount><OrderOfferPrice>17980.00</OrderOfferPrice><Reason /><Location>BIGBAZAAR#WEBSITE</Location><Text1>false</Text1><Text2 /><Text3 /></OrderHeader><PaymentGroups><Details><PayMethod>CHEQ</PayMethod><Type>icici</Type><Amount>26970.00</Amount><AuthAmount>26970.00</AuthAmount><AuthDate>20120128</AuthDate><AuthTime>172225</AuthTime><ReAmount /><MerchantID /><TranRefID>20120128153513277451245055843910</TranRefID><TranRefID2 /><TranRefID3 /><AuthRefCode /><Currency>INR</Currency><RRN /><BankName>icici</BankName><AccNo /><ChequeNo>47899</ChequeNo><ValidDate>20120126</ValidDate><Status /><RefCode /><Text1 /><Text2 /><Text3 /></Details></PaymentGroups><DefaultShippingAddress><FirstName>Pradyumna Kumar</FirstName><MiddleName /><LastName /><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></DefaultShippingAddress><Customer><CustomerRole>AG</CustomerRole><CustomerID>826468</CustomerID><FirstName>Pradyumna</FirstName><MiddleName /><LastName>Kumar</LastName><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></Customer><OrderLineItems><LineItem><ItemSno>10</ItemSno><ItemID>000000000100000002</ItemID><ItemDesc>Electrolux EJ30CSS6 30L Microwave Oven + Starter kit</ItemDesc><RefID /><ShipGroupCode /><Quantity>2</Quantity><ReqDelivDate /><ItemState /><SalesUnit /><ItemCategory /><isThirdParty>false</isThirdParty><Vendor /><MRP>13290.00</MRP><SalesPrice>8990.00</SalesPrice><OfferPrice>8990.00</OfferPrice><Amount>17980.00</Amount><Discount /><NLC /><Plant>4608</Plant><Catalogs>|FutureBazaar</Catalogs><ProductGroupId /><ModeOfTransport /><ShippingMode /><LSP /><ShipCharge>0.00</ShipCharge><EANUPC /><Reason /><Notes /><ItemSno1 /><GV /><Bundle /><ShipAddress><FirstName>Pradyumna Kumar</FirstName><MiddleName /><LastName /><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></ShipAddress><BackorderText /><Text1>true</Text1><Text2 /><Text3 /></LineItem></OrderLineItems></commonOrderRequest>";
		String bbCancelCommonXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><commonOrderRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><Header><MessageType>CANORD</MessageType><BussOpr /></Header><OrderHeader><OrderRefID>I000001012</OrderRefID><OrderRefID2 /><OrderRefID3 /><CreateDate>20120128</CreateDate><OrderType>order</OrderType><DeliveryDate /><SoldToParty>G100000103</SoldToParty><SalesDocType>ZFGB</SalesDocType><SalesChannel>true</SalesChannel><SalesOrganisation>false</SalesOrganisation><isThirdParty>false</isThirdParty><Currency>INR</Currency><OrderDiscount>0.00</OrderDiscount><OrderOfferPrice>17980.00</OrderOfferPrice><Reason>01</Reason><Location>BIGBAZAAR#WEBSITE</Location><Text1>false</Text1><Text2 /><Text3 /></OrderHeader><PaymentGroups><Details><PayMethod>CHEQ</PayMethod><Type>icici</Type><Amount>26970.00</Amount><AuthAmount>26970.00</AuthAmount><AuthDate>20120128</AuthDate><AuthTime>172225</AuthTime><ReAmount /><MerchantID /><TranRefID>20120128153513277451245055843910</TranRefID><TranRefID2 /><TranRefID3 /><AuthRefCode /><Currency>INR</Currency><RRN /><BankName>icici</BankName><AccNo /><ChequeNo>47899</ChequeNo><ValidDate>20120126</ValidDate><Status /><RefCode /><Text1 /><Text2 /><Text3 /></Details></PaymentGroups><DefaultShippingAddress><FirstName>Pradyumna Kumar</FirstName><MiddleName /><LastName /><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></DefaultShippingAddress><Customer><CustomerRole>AG</CustomerRole><CustomerID>826468</CustomerID><FirstName>Pradyumna</FirstName><MiddleName /><LastName>Kumar</LastName><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></Customer><OrderLineItems><LineItem><ItemSno>10</ItemSno><ItemID>000000000100000105</ItemID><ItemDesc>Electrolux EJ30CSS6 30L Microwave Oven + Starter kit</ItemDesc><RefID /><ShipGroupCode /><Quantity>2</Quantity><ReqDelivDate /><ItemState /><SalesUnit /><ItemCategory /><isThirdParty>false</isThirdParty><Vendor /><MRP>13290.00</MRP><SalesPrice>8990.00</SalesPrice><OfferPrice>8990.00</OfferPrice><Amount>17980.00</Amount><Discount /><NLC /><Plant>4608</Plant><Catalogs>|FutureBazaar</Catalogs><ProductGroupId /><ModeOfTransport /><ShippingMode /><LSP /><ShipCharge>0.00</ShipCharge><EANUPC /><Reason>01</Reason><Notes /><ItemSno1 /><GV /><Bundle /><ShipAddress><FirstName>Pradyumna Kumar</FirstName><MiddleName /><LastName /><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></ShipAddress><BackorderText /><Text1>true</Text1><Text2 /><Text3 /></LineItem><LineItem><ItemSno>20</ItemSno><ItemID>000000000100000002</ItemID><ItemDesc>Electrolux EJ30CSS6 30L Microwave Oven + Starter kit</ItemDesc><RefID /><ShipGroupCode /><Quantity>2</Quantity><ReqDelivDate /><ItemState /><SalesUnit /><ItemCategory /><isThirdParty>false</isThirdParty><Vendor /><MRP>13290.00</MRP><SalesPrice>8990.00</SalesPrice><OfferPrice>8990.00</OfferPrice><Amount>17980.00</Amount><Discount /><NLC /><Plant>4608</Plant><Catalogs>|FutureBazaar</Catalogs><ProductGroupId /><ModeOfTransport /><ShippingMode /><LSP /><ShipCharge>0.00</ShipCharge><EANUPC /><Reason>01</Reason><Notes /><ItemSno1 /><GV /><Bundle /><ShipAddress><FirstName>Pradyumna Kumar</FirstName><MiddleName /><LastName /><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></ShipAddress><BackorderText /><Text1>true</Text1><Text2 /><Text3 /></LineItem></OrderLineItems></commonOrderRequest>";
		return bbCancelCommonXml;
	}
	
	public String getModifiedOrderXml() {
		String modifiedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><modifyOrderRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><deliveryItemsList><DelItem><itemDesc>Electrolux EJ30CSS6 30L Microwave Oven + Starter kit</itemDesc><itemID>000000000100000002</itemID><lineItemId>10</lineItemId><itemState /><lspName /><lspNumber /><deliveryNumber /><invoiceNumber /><invoiceDate /><trackingNumber /><quantity>1</quantity><reasonOfCancellation /><operation>U</operation><plant>4608</plant><salesPrice>8990.00</salesPrice><discount /><listPrice>13290.00</listPrice><shippingPrice>0.00</shippingPrice><isThirdParty>false</isThirdParty><vendor /><nlc>0.0</nlc><EANUPC /><onSale /><bundle /><gvClaimCodes /><requiredDeliveryDate /><ShippingAddress><firstName>Pradyumna Kumar</firstName><middleName /><lastName /><address1>TEST ORDER PLEASE CANCEL</address1><address2 /><city>Mumbai</city><state>13</state><country>IN</country><postalCode>400008</postalCode><telephone1>9967442905</telephone1><telephone2 /></ShippingAddress></DelItem></deliveryItemsList><modifiedOrderHeader><orderId>I000001021</orderId><dateOfCreation>20120128</dateOfCreation><dateOfSubmission>20120130</dateOfSubmission><shippingAmount>0.00</shippingAmount><orderTotal>8990.00</orderTotal><discount>4300.00</discount><bpNumber>G100000103</bpNumber><loyaltyCardNumber /><pointsEarn>0.0</pointsEarn><pointsEarnValue>0.0</pointsEarnValue><pointsBurn>0.0</pointsBurn><priceAdjustments><PriceAdjustment><totalAdjustment>8990.00</totalAdjustment><description>Order Subtotal</description></PriceAdjustment></priceAdjustments><client>BIGBAZAAR</client></modifiedOrderHeader></modifyOrderRequest>";
		return modifiedXml;
	}
	
	@Test
	public void dummy() {
		
	}
}
