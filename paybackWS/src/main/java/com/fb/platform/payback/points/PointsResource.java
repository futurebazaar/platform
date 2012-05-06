/**
 * 
 */
package com.fb.platform.payback.points;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import com.fb.platform.payback._1_0.OrderItem;
import com.fb.platform.payback._1_0.StorePointsRequest;
import com.fb.platform.payback._1_0.StorePointsResponse;
import com.fb.platform.payback._1_0.StorePointsStatus;
import com.fb.platform.payback.service.PointsManager;
import com.fb.platform.payback.to.StorePointsHeaderRequest;
import com.fb.platform.payback.to.StorePointsItemRequest;


/**
 * @author anubhav
 *
 */
@Path("/points")
@Component
@Scope("request")
public class PointsResource {

	private static Log logger = LogFactory.getLog(PointsResource.class);

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.payback._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	@Autowired
	private PointsManager pointsManager;

	@POST
	@Path("/store")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String storePoints(String storePointsXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("storePointsRequestXML : \n" + storePointsXml);
		}
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StorePointsRequest xmlStorePointsRequest = (StorePointsRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(storePointsXml)));
			StorePointsHeaderRequest storePointsHeaderRequest = new StorePointsHeaderRequest();
			storePointsHeaderRequest.setAmount(xmlStorePointsRequest.getAmount());
			storePointsHeaderRequest.setLoyaltyCard(xmlStorePointsRequest.getLoyaltyCard());
			storePointsHeaderRequest.setOrderId(xmlStorePointsRequest.getOrderId());
			storePointsHeaderRequest.setTxnActionCode(xmlStorePointsRequest.getActionCode());
			storePointsHeaderRequest.setReason(xmlStorePointsRequest.getReason());
			
			for (OrderItem xmlOrderItem : xmlStorePointsRequest.getOrderItem()) {
				com.fb.platform.payback.to.StorePointsItemRequest storePointsItemRequest = createStorePointsItem(xmlOrderItem);
				storePointsHeaderRequest.getStorePointsItemRequest().add(storePointsItemRequest);
			}
			
			com.fb.platform.payback.to.StorePointsResponse storePointResponse = pointsManager.getPointsReponse(storePointsHeaderRequest);
			StorePointsResponse xmlStorePointsResponse = new StorePointsResponse();	
			xmlStorePointsResponse.setActionCode(storePointResponse.getActionCode());
			xmlStorePointsResponse.setStorePointsStatus(StorePointsStatus.valueOf(storePointResponse.getStorePointsResponseCodeEnum().name()));
			xmlStorePointsResponse.setMessage(storePointResponse.getStorePointsResponseCodeEnum().toString());
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlStorePointsResponse, outStringWriter);
			
			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("CouponXML response :\n" + xmlResponse);
			}
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the storePoints call.", e);
			return null;
		}
	}

	private StorePointsItemRequest createStorePointsItem(OrderItem xmlOrderItem) {
		StorePointsItemRequest storePointsItemRequest = new StorePointsItemRequest();
		storePointsItemRequest.setAmount(xmlOrderItem.getAmount());
		storePointsItemRequest.setId(xmlOrderItem.getItemId());
		storePointsItemRequest.setQuantity(xmlOrderItem.getQuantity());
		storePointsItemRequest.setDepartmentCode(xmlOrderItem.getDepartmentCode());
		storePointsItemRequest.setDepartmentName(xmlOrderItem.getDepartmentName());
		storePointsItemRequest.setArticleId(xmlOrderItem.getArticleId());
		return storePointsItemRequest;
	}
	
	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform PayBack Websevice.\n");
		sb.append("To store Points post to : http://hostname:port/paybackWS/points/store\n");
		return sb.toString();
	}
	
	@GET
	@Path("/xsd")
	@Produces("application/xml")
	public String getXsd() {	
		InputStream paybackXsd = this.getClass().getClassLoader().getResourceAsStream("payback.xsd");
		String paybackXsdString = convertInputStreamToString(paybackXsd);
		return paybackXsdString;
	}
	
	private String convertInputStreamToString(InputStream inputStream) {
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		try {
			String line = bufReader.readLine();
			while( line != null ) {
				sb.append( line + "\n" );
				line = bufReader.readLine();
			}
			inputStream.close();
		} catch(IOException exception) {
			logger.error("payback.xsd loading error : " + exception.getMessage() );
		}
		return sb.toString();
	}
	

}
