package com.fb.platform.sap.test;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.PlatformException;
import com.fb.platform.sap.InventoryResource;
import com.fb.platform.sap.LspResource;
import com.fb.platform.sap._1_0.CommonOrderRequest;
import com.fb.platform.sap._1_0.InventoryLevelRequest;
import com.fb.platform.sap._1_0.ModifyOrderRequest;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.client.connector.SapClientConnector;
import com.fb.platform.sap.client.handler.impl.SapClientHandler;

public class TestResource {
	
	private static Log logger = LogFactory.getLog(TestResource.class);
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.sap._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	public static void main(String[] args) {
		SapClientHandler sapClientHandler = new SapClientHandler();
		SapClientConnector sapClientConnector = new SapClientConnector();
		sapClientHandler.setBapiConnector(sapClientConnector);
		
		logger.info("Setting xml");
		try {
//			InventoryResource sapInventoryResource = new InventoryResource();
//			sapInventoryResource.setSapClientHandler(sapClientHandler);
//			String inventoryLevelXml = new TestSapResource().getInventoryLevelXml();
//			System.out.println(inventoryLevelXml);
//			System.out.println(sapInventoryResource.inventorylevel(inventoryLevelXml));
//			
//			String inventoryDashboardXml = new TestSapResource().getInventoryDashboardXml();
//			System.out.println(inventoryDashboardXml);
//			System.out.println(sapInventoryResource.inventoryDashboard(inventoryDashboardXml));
//			
//			LspResource sapLspResource = new LspResource();
//			sapLspResource.setSapClientHandler(sapClientHandler);
//			String lspAwbUpdateXml = new TestSapResource().getLspAwbUpdateXml();
//			System.out.println(lspAwbUpdateXml);
//			System.out.println(sapLspResource.assignAwbToDeliveryl(lspAwbUpdateXml));
			
			
			String commonXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><commonOrderRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><Header><MessageType>NEWORD</MessageType><BussOpr /></Header><OrderHeader><OrderRefID>5055843910</OrderRefID><OrderRefID2 /><OrderRefID3 /><CreateDate>20120128</CreateDate><OrderType>order</OrderType><DeliveryDate /><SoldToParty>2100000103</SoldToParty><SalesDocType>ZATG</SalesDocType><SalesChannel>true</SalesChannel><SalesOrganisation>false</SalesOrganisation><isThirdParty>false</isThirdParty><Currency>INR</Currency><OrderDiscount>0.00</OrderDiscount><OrderOfferPrice>17980.00</OrderOfferPrice><Reason /><Location>FUTUREBAZAAR</Location><Text1>false</Text1><Text2 /><Text3 /></OrderHeader><PaymentGroups><Details><PayMethod>CHEQ</PayMethod><Type>icici</Type><Amount>26970.00</Amount><AuthAmount>26970.00</AuthAmount><AuthDate>20120128</AuthDate><AuthTime>172225</AuthTime><ReAmount /><MerchantID /><TranRefID>20120128153513277451245055843910</TranRefID><TranRefID2 /><TranRefID3 /><AuthRefCode /><Currency>INR</Currency><RRN /><BankName>icici</BankName><AccNo /><ChequeNo>47899</ChequeNo><ValidDate>20120126</ValidDate><Status /><RefCode /><Text1 /><Text2 /><Text3 /></Details></PaymentGroups><DefaultShippingAddress><FirstName>Pradyumna Kumar</FirstName><MiddleName /><LastName /><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></DefaultShippingAddress><Customer><CustomerRole>AG</CustomerRole><CustomerID>826468</CustomerID><FirstName>Pradyumna</FirstName><MiddleName /><LastName>Kumar</LastName><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></Customer><OrderLineItems><LineItem><ItemSno>10</ItemSno><ItemID>000000000999999732</ItemID><ItemDesc>Electrolux EJ30CSS6 30L Microwave Oven + Starter kit</ItemDesc><RefID /><ShipGroupCode /><Quantity>2</Quantity><ReqDelivDate /><ItemState /><SalesUnit /><ItemCategory /><isThirdParty>false</isThirdParty><Vendor /><MRP>13290.00</MRP><SalesPrice>8990.00</SalesPrice><OfferPrice>8990.00</OfferPrice><Amount>17980.00</Amount><Discount /><NLC /><Plant>2786</Plant><Catalogs>|FutureBazaar</Catalogs><ProductGroupId /><ModeOfTransport /><ShippingMode /><LSP /><ShipCharge>0.00</ShipCharge><EANUPC /><Reason /><Notes /><ItemSno1 /><GV /><Bundle /><ShipAddress><FirstName>Pradyumna Kumar</FirstName><MiddleName /><LastName /><Address1>TEST ORDER PLEASE CANCEL</Address1><Address2 /><City>Mumbai</City><State>13</State><Country>IN</Country><PoCode>400008</PoCode><Telephone1>9967442905</Telephone1><Telephone2 /></ShipAddress><BackorderText /><Text1>true</Text1><Text2 /><Text3 /></LineItem></OrderLineItems></commonOrderRequest>";
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			CommonOrderRequest commonOrderRequest = (CommonOrderRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(commonXml)));
			System.out.println(commonOrderRequest.getHeader().getMessageType());
			
			String modifiedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><modifyOrderRequest xmlns=\"http://www.fb.com/platform/sap/1.0.0\"><deliveryItemsList><DelItem><itemDesc>Electrolux EJ30CSS6 30L Microwave Oven + Starter kit</itemDesc><itemID>000000000999999732</itemID><lineItemId>10</lineItemId><itemState /><lspName /><lspNumber /><deliveryNumber /><invoiceNumber /><invoiceDate /><trackingNumber /><quantity>1</quantity><reasonOfCancellation /><operation>U</operation><plant>2786</plant><salesPrice>8990.00</salesPrice><discount /><listPrice>13290.00</listPrice><shippingPrice>0.00</shippingPrice><isThirdParty>false</isThirdParty><vendor /><nlc>0.0</nlc><EANUPC /><onSale /><bundle /><gvClaimCodes /><requiredDeliveryDate /><ShippingAddress><firstName>Pradyumna Kumar</firstName><middleName /><lastName /><address1>TEST ORDER PLEASE CANCEL</address1><address2 /><city>Mumbai</city><state>13</state><country>IN</country><postalCode>400008</postalCode><telephone1>9967442905</telephone1><telephone2 /></ShippingAddress></DelItem></deliveryItemsList><modifiedOrderHeader><orderId>5055843910</orderId><dateOfCreation>20120128</dateOfCreation><dateOfSubmission>20120130</dateOfSubmission><shippingAmount>0.00</shippingAmount><orderTotal>8990.00</orderTotal><discount>4300.00</discount><bpNumber>2100000103</bpNumber><loyaltyCardNumber /><pointsEarn>0.0</pointsEarn><pointsEarnValue>0.0</pointsEarnValue><pointsBurn>0.0</pointsBurn><priceAdjustments><PriceAdjustment><totalAdjustment>8990.00</totalAdjustment><description>Order Subtotal</description></PriceAdjustment></priceAdjustments></modifiedOrderHeader></modifyOrderRequest>";;
			ModifyOrderRequest modifyOrderRequest = (ModifyOrderRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(modifiedXml)));
			System.out.println(modifyOrderRequest.getModifiedOrderHeader().getBpNumber());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
