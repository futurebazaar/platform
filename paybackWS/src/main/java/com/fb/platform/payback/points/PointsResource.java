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

import javax.naming.NoPermissionException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.payback._1_0.ActionCode;
import com.fb.platform.payback._1_0.ClearCacheRequest;
import com.fb.platform.payback._1_0.ClearCacheResponse;
import com.fb.platform.payback._1_0.DisplayPointsRequest;
import com.fb.platform.payback._1_0.DisplayPointsResponse;
import com.fb.platform.payback._1_0.ItemResponse;
import com.fb.platform.payback._1_0.OrderItemRequest;
import com.fb.platform.payback._1_0.PointsRequest;
import com.fb.platform.payback._1_0.PointsResponse;
import com.fb.platform.payback._1_0.RollbackPointsRequest;
import com.fb.platform.payback._1_0.RollbackPointsResponse;
import com.fb.platform.payback.service.PointsManager;
import com.fb.platform.payback.to.PointsResponseCodeEnum;


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
	private PointsManager pointsManager = null;

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
			pointsHeaderRequest.setTxnActionCode(xmlPointsRequest.getActionCode().name());
			pointsHeaderRequest.setClientName(xmlPointsRequest.getClientName());
			pointsHeaderRequest.setSessionToken(xmlPointsRequest.getSessionToken());
			
			com.fb.platform.payback.to.OrderRequest orderRequest = new com.fb.platform.payback.to.OrderRequest();
			orderRequest.setAmount(xmlPointsRequest.getOrderRequest().getAmount());
			orderRequest.setLoyaltyCard(xmlPointsRequest.getOrderRequest().getLoyaltyCard());
			orderRequest.setOrderId(xmlPointsRequest.getOrderRequest().getOrderId());
			XMLGregorianCalendar gregCal = xmlPointsRequest.getOrderRequest().getTimestamp();
			orderRequest.setTxnTimestamp(new DateTime(gregCal.getYear(), gregCal.getMonth(), gregCal.getDay(), 
					gregCal.getHour(), gregCal.getMinute()));
			orderRequest.setReferenceId(xmlPointsRequest.getOrderRequest().getReferenceId());
			orderRequest.setReason(xmlPointsRequest.getOrderRequest().getReason());
			
			for (OrderItemRequest xmlOrderItem : xmlPointsRequest.getOrderRequest().getOrderItemRequest()) {
				com.fb.platform.payback.to.OrderItemRequest orderItemRequest = createStorePointsItem(xmlOrderItem);
				orderRequest.getOrderItemRequest().add(orderItemRequest);
			}
			
			pointsHeaderRequest.setOrderRequest(orderRequest);
			
			com.fb.platform.payback.to.PointsResponse pointsResponse = pointsManager.getPointsReponse(pointsHeaderRequest);
			PointsResponse xmlPointsResponse = new PointsResponse();	
			xmlPointsResponse.setActionCode(ActionCode.valueOf(pointsResponse.getActionCode().name()));
			xmlPointsResponse.setMessage(pointsResponse.getStatusMessage());
			xmlPointsResponse.setStatusCode(pointsResponse.getPointsResponseCodeEnum().name());
			xmlPointsResponse.setTotalPoints(pointsResponse.getTxnPoints());
			xmlPointsResponse.setHeaderId(pointsResponse.getPointsHeaderId());
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
		pointsItemRequest.setCategoryId(xmlOrderItem.getCategoryId());
		pointsItemRequest.setArticleId(xmlOrderItem.getArticleId());
		pointsItemRequest.setSellerRateChartId(xmlOrderItem.getSellerRateChartId());
		return pointsItemRequest;
	}
	
	@POST
	@Path("/clear/rule")
	public String clearRuleCache(String pointsXml){
		logger.info("clearCacheRequestXML : \n" + pointsXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ClearCacheRequest xmlClearCacheRequest = (ClearCacheRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(pointsXml)));
			com.fb.platform.payback.to.ClearCacheRequest clearCacheRequest= new com.fb.platform.payback.to.ClearCacheRequest();
			clearCacheRequest.setRuleName(xmlClearCacheRequest.getRuleName());
			clearCacheRequest.setSessionToken(xmlClearCacheRequest.getSessionToken());
			
			PointsResponseCodeEnum responseCodeEnum = pointsManager.clearPointsCache(clearCacheRequest);
			ClearCacheResponse clearCacheResponse =  new ClearCacheResponse();
			clearCacheResponse.setStatusCode(responseCodeEnum.name());
			clearCacheResponse.setRuleName(clearCacheRequest.getRuleName());
			clearCacheResponse.setSessionToken(clearCacheRequest.getSessionToken());

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(clearCacheResponse, outStringWriter);
			
			String xmlResponse = outStringWriter.toString();
			logger.info("Clear Cache Response : \n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the Points call.", e);
			return null;
		}
	}

	@POST
	@Path("/display")
	public String displayPoints(String pointsXml) throws NoPermissionException{
		logger.info("Display Points Request XML : \n" + pointsXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			DisplayPointsRequest xmlDisplayPointsRequest = (DisplayPointsRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(pointsXml)));
			com.fb.platform.payback.to.PointsRequest pointsRequest = new com.fb.platform.payback.to.PointsRequest();
			
			pointsRequest.setTxnActionCode(xmlDisplayPointsRequest.getActionCode().name());
			
			com.fb.platform.payback.to.OrderRequest orderRequest = new com.fb.platform.payback.to.OrderRequest();
			orderRequest.setAmount(xmlDisplayPointsRequest.getOrderAmount());
			XMLGregorianCalendar gregCal = xmlDisplayPointsRequest.getTimestamp();
			orderRequest.setTxnTimestamp(new DateTime(gregCal.getYear(), gregCal.getMonth(), gregCal.getDay(), 
					gregCal.getHour(), gregCal.getMinute()));
			
			for (OrderItemRequest xmlOrderItem : xmlDisplayPointsRequest.getOrderItemRequest()) {
				com.fb.platform.payback.to.OrderItemRequest orderItemRequest = createStorePointsItem(xmlOrderItem);
				orderRequest.getOrderItemRequest().add(orderItemRequest);
			}
			
			pointsRequest.setOrderRequest(orderRequest);
			
			com.fb.platform.payback.to.PointsRequest newPointsRequest= pointsManager.getPointsToBeDisplayed(pointsRequest);
			
			DisplayPointsResponse xmlDisplayPointsResponse = new DisplayPointsResponse();
			xmlDisplayPointsResponse.setTotalPoints(newPointsRequest.getOrderRequest().getTxnPoints().intValue());
			xmlDisplayPointsResponse.setBonusPoints(newPointsRequest.getOrderRequest().getBonusPoints().intValue());
			for (com.fb.platform.payback.to.OrderItemRequest itemRequest : newPointsRequest.getOrderRequest().getOrderItemRequest()){
				ItemResponse xmlItemResponse = new ItemResponse();
				xmlItemResponse.setAmount(itemRequest.getAmount());
				xmlItemResponse.setItemId(itemRequest.getId());
				xmlItemResponse.setTxnPoints(itemRequest.getTxnPoints().intValue());
				xmlDisplayPointsResponse.getItemResponse().add(xmlItemResponse);
			}

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlDisplayPointsResponse, outStringWriter);
			
			String xmlResponse = outStringWriter.toString();
			logger.info("Display Points Response : \n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the Points call.", e);
			return null;
		}

	}
	
	@POST
	@Path("/rollback")
	public String rollbackPoints(String pointsXml) throws NoPermissionException{
		logger.info("Rollback Points Request XML : \n" + pointsXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			RollbackPointsRequest xmlRollbackPointsRequest = (RollbackPointsRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(pointsXml)));
			com.fb.platform.payback.to.RollbackRequest rollbackRequest = new com.fb.platform.payback.to.RollbackRequest();
			
			rollbackRequest.setSessionToken(xmlRollbackPointsRequest.getSessionToken());
			rollbackRequest.setHeaderId(xmlRollbackPointsRequest.getHeaderId());
			
			com.fb.platform.payback.to.RollbackResponse rollbackResponse = pointsManager.rollbackTransaction(rollbackRequest);
			
			RollbackPointsResponse xmlRollbackPointsResponse = new RollbackPointsResponse();
			xmlRollbackPointsResponse.setDeletedHeaderRows(rollbackResponse.getDeletedHeaderRows());
			xmlRollbackPointsResponse.setDeletedItemRows(rollbackResponse.getDeletedItemRows());
			xmlRollbackPointsResponse.setHeaderId(rollbackResponse.getHeaderId());
			xmlRollbackPointsResponse.setStatusCode(rollbackResponse.getResponseEnum().name());

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlRollbackPointsResponse, outStringWriter);
			
			String xmlResponse = outStringWriter.toString();
			logger.info("Rollback Points Response : \n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the Points call.", e);
			return null;
		}

	}
	
	@GET
	@Path("/")
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform PAYBACK Websevice. \n");
		sb.append("To store Points post to : http://hostname:port/paybackWS/points/store \n");
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
