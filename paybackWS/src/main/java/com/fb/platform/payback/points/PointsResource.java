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
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fb.commons.PlatformException;
import com.fb.platform.payback._1_0.OrderItemRequest;
import com.fb.platform.payback._1_0.PointsRequest;
import com.fb.platform.payback._1_0.PointsResponse;
import com.fb.platform.payback._1_0.PointsStatus;
import com.fb.platform.payback.service.PointsManager;


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
	public String storePoints(String pointsXml) {
		
		logger.info("pointsRequestXML : \n" + pointsXml);
		
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			PointsRequest xmlPointsRequest = (PointsRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(pointsXml)));
			com.fb.platform.payback.to.PointsRequest pointsHeaderRequest = new com.fb.platform.payback.to.PointsRequest();
			pointsHeaderRequest.setTxnActionCode(xmlPointsRequest.getActionCode());
			com.fb.platform.payback.to.OrderRequest orderRequest = new com.fb.platform.payback.to.OrderRequest();
			orderRequest.setAmount(xmlPointsRequest.getOrderRequest().getAmount());
			orderRequest.setLoyaltyCard(xmlPointsRequest.getOrderRequest().getLoyaltyCard());
			orderRequest.setOrderId(xmlPointsRequest.getOrderRequest().getOrderId());
			orderRequest.setTxnTimestamp(new DateTime(xmlPointsRequest.getOrderRequest().getTimestamp().getMillisecond()));
			orderRequest.setReason(xmlPointsRequest.getOrderRequest().getReason());
			
			for (OrderItemRequest xmlOrderItem : xmlPointsRequest.getOrderRequest().getOrderItemRequest()) {
				com.fb.platform.payback.to.OrderItemRequest orderItemRequest = createStorePointsItem(xmlOrderItem);
				orderRequest.getOrderItemRequest().add(orderItemRequest);
			}
			
			com.fb.platform.payback.to.PointsResponse pointResponse = pointsManager.getPointsReponse(pointsHeaderRequest);
			PointsResponse xmlPointsResponse = new PointsResponse();	
			xmlPointsResponse.setActionCode(pointResponse.getActionCode());
			xmlPointsResponse.setPointsStatus(PointsStatus.valueOf(pointResponse.getStorePointsResponseCodeEnum().name()));
			xmlPointsResponse.setMessage(pointResponse.getStorePointsResponseCodeEnum().toString());
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlPointsResponse, outStringWriter);
			
			String xmlResponse = outStringWriter.toString();
			logger.info("PointsXML response :\n" + xmlResponse);
			return xmlResponse;
		} catch (JAXBException e) {
			logger.error("Error in the Points call.", e);
			return null;
		}
	}

	private com.fb.platform.payback.to.OrderItemRequest createStorePointsItem(OrderItemRequest xmlOrderItem) {
		com.fb.platform.payback.to.OrderItemRequest pointsItemRequest = new com.fb.platform.payback.to.OrderItemRequest();
		pointsItemRequest.setAmount(xmlOrderItem.getAmount());
		pointsItemRequest.setId(xmlOrderItem.getItemId());
		pointsItemRequest.setQuantity(xmlOrderItem.getQuantity());
		pointsItemRequest.setDepartmentCode(xmlOrderItem.getDepartmentCode());
		pointsItemRequest.setDepartmentName(xmlOrderItem.getDepartmentName());
		pointsItemRequest.setArticleId(xmlOrderItem.getArticleId());
		return pointsItemRequest;
	}
	
	@GET
	public String uploadEarnOnSFTP(){
		return null;
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

	private String getIp(){
		return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
			       .getRequest().getRemoteAddr();
	}

}
