/**
 * 
 */
package com.fb.platform.promotion.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion._1_0.ApplyAutoPromotionRequest;
import com.fb.platform.promotion._1_0.ApplyAutoPromotionResponse;
import com.fb.platform.promotion._1_0.ApplyAutoPromotionStatus;
import com.fb.platform.promotion._1_0.CommitAutoPromotionRequest;
import com.fb.platform.promotion._1_0.CommitAutoPromotionResponse;
import com.fb.platform.promotion._1_0.CommitAutoPromotionStatus;
import com.fb.platform.promotion._1_0.GetAppliedAutoPromotionRequest;
import com.fb.platform.promotion._1_0.GetAppliedAutoPromotionResponse;
import com.fb.platform.promotion._1_0.GetAppliedAutoPromotionStatus;
import com.fb.platform.promotion._1_0.OrderDiscount;
import com.fb.platform.promotion._1_0.OrderItem;
import com.fb.platform.promotion._1_0.OrderRequest;
import com.fb.platform.promotion._1_0.Product;
import com.fb.platform.promotion._1_0.Promotion;
import com.fb.platform.promotion._1_0.RefreshAutoPromotionRequest;
import com.fb.platform.promotion._1_0.RefreshAutoPromotionResponse;
import com.fb.platform.promotion._1_0.RefreshAutoPromotionStatus;
import com.fb.platform.promotion.service.AutoPromotionManager;

/**
 * @author vinayak
 *
 */
@Path("/autoPromotion")
@Component
@Scope("request")
public class AutoPromotionResource {

	private static Log logger = LogFactory.getLog(AutoPromotionResource.class);

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	@Autowired
	private AutoPromotionManager promotionManager = null;

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.promotion._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}

	@POST
	@Path("/refresh")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String refreshAutoPromotion(String refreshAutoPromotionXml) {
		logger.info("refreshAutoPromotion request xml : \n" +  refreshAutoPromotionXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			RefreshAutoPromotionRequest xmlRefreshAutoPromotionRequest = (RefreshAutoPromotionRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(refreshAutoPromotionXml)));
			com.fb.platform.promotion.product.to.RefreshAutoPromotionRequest apiRefreshAutoPromotionRequest = new com.fb.platform.promotion.product.to.RefreshAutoPromotionRequest();
			
			apiRefreshAutoPromotionRequest.setSessionToken(xmlRefreshAutoPromotionRequest.getSessionToken());
			
			com.fb.platform.promotion.product.to.RefreshAutoPromotionResponse apiRefreshAutoPromotionResponse = promotionManager.refresh(apiRefreshAutoPromotionRequest);

			RefreshAutoPromotionResponse xmlRefreshAutoPromotionResponse = new RefreshAutoPromotionResponse();
			
			xmlRefreshAutoPromotionResponse.setSessionToken(apiRefreshAutoPromotionResponse.getSessionToken());
			xmlRefreshAutoPromotionResponse.setRefreshAutoPromotionStatus(RefreshAutoPromotionStatus.valueOf(apiRefreshAutoPromotionResponse.getRefreshAutoPromotionStatus().toString()));
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlRefreshAutoPromotionResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("refreshAutoPromotion response :\n" + xmlResponse);
			
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the refreshAutoPromotion call.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/apply")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String applyAutoPromotion(String applyAutoPromotionXml) {
		logger.info("applyAutoPromotion request xml : \n" +  applyAutoPromotionXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			ApplyAutoPromotionRequest xmlRequest = (ApplyAutoPromotionRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(applyAutoPromotionXml)));
			
			com.fb.platform.promotion.product.to.ApplyAutoPromotionRequest apiRequest = new com.fb.platform.promotion.product.to.ApplyAutoPromotionRequest();
			
			apiRequest.setSessionToken(xmlRequest.getSessionToken());
			apiRequest.setOrderBookingDate(convertToDateTime(xmlRequest.getOrderBookingDate()));
			apiRequest.setAppliedPromotions(getInts(xmlRequest.getAppliedPromotionsList()));
			apiRequest.setOrderReq(getApiOrderRequest(xmlRequest.getOrderRequest()));
			
			com.fb.platform.promotion.product.to.ApplyAutoPromotionResponse apiResponse = promotionManager.apply(apiRequest);
			ApplyAutoPromotionResponse xmlResponse = new ApplyAutoPromotionResponse();
			
			xmlResponse.setSessionToken(apiResponse.getSessionToken());
			xmlResponse.setApplyAutoPromotionStatus(ApplyAutoPromotionStatus.valueOf(apiResponse.getApplyAutoPromotionStatus().toString()));
			xmlResponse.setOrderDiscount(getXmlOrderDiscountValue(apiResponse.getOrderDiscount()));
			
			for(com.fb.platform.promotion.model.Promotion apiPromotion : apiResponse.getAppliedPromotions()) {
				xmlResponse.getPromotion().add(getXmlPromotion(apiPromotion));
			}
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlResponse, outStringWriter);

			String xmlSendResponse = outStringWriter.toString();
			logger.info("applyAutoPromotion response :\n" + xmlSendResponse);
			
			return xmlSendResponse;
		} catch (JAXBException e) {
			logger.error("Error in the applyAutoPromotion call.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/commit")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String commitAutoPromotion(String commitAutoPromotionXml) {
		logger.info("commitAutoPromotion request xml : \n" +  commitAutoPromotionXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			CommitAutoPromotionRequest xmlRequest = (CommitAutoPromotionRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(commitAutoPromotionXml)));
			com.fb.platform.promotion.product.to.CommitAutoPromotionRequest apiRequest = new com.fb.platform.promotion.product.to.CommitAutoPromotionRequest();
			
			apiRequest.setSessionToken(xmlRequest.getSessionToken());
			apiRequest.setOrderId(xmlRequest.getOrderId());
			apiRequest.setAppliedPromotionsList(getInts(xmlRequest.getAppliedPromotionsList()));
			
			com.fb.platform.promotion.product.to.CommitAutoPromotionResponse apiResponse = promotionManager.commit(apiRequest);
			CommitAutoPromotionResponse xmlResponse = new CommitAutoPromotionResponse();
			
			xmlResponse.setSessionToken(apiResponse.getSessionToken());
			xmlResponse.setStatusMessage(apiResponse.getStatusMessage());
			xmlResponse.setCommitAutoPromotionStatus(CommitAutoPromotionStatus.valueOf(apiResponse.getCommitAutoPromotionStatus().toString()));
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlResponse, outStringWriter);

			String xmlSendResponse = outStringWriter.toString();
			logger.info("commitAutoPromotion response :\n" + xmlSendResponse);
			
			return xmlSendResponse;
		} catch (JAXBException e) {
			logger.error("Error in the commitAutoPromotion call.", e);
			return "error"; //TODO return proper error response
		}
	}

	@POST
	@Path("/getApplied")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String getAppliedAutoPromotions(String getAppliedAutoPromotionXml) {
		logger.info("getAppliedAutoPromotions request xml : \n" +  getAppliedAutoPromotionXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			GetAppliedAutoPromotionRequest xmlRequest = (GetAppliedAutoPromotionRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(getAppliedAutoPromotionXml)));
			com.fb.platform.promotion.product.to.GetAppliedAutoPromotionRequest apiRequest = new com.fb.platform.promotion.product.to.GetAppliedAutoPromotionRequest();

			apiRequest.setOrderId(xmlRequest.getOrderId());
			apiRequest.setSessionToken(xmlRequest.getSessionToken());
			
			com.fb.platform.promotion.product.to.GetAppliedAutoPromotionResponse apiResponse = promotionManager.getAppliedPromotions(apiRequest);
			GetAppliedAutoPromotionResponse xmlResponse = new GetAppliedAutoPromotionResponse();
			
			xmlResponse.setSessionToken(apiResponse.getSessionToken());
			xmlResponse.setGetAppliedAutoPromotionStatus(GetAppliedAutoPromotionStatus.valueOf(apiResponse.getGetAppliedAutoPromotionStatus().toString()));
			
			for(com.fb.platform.promotion.model.Promotion apiPromotion : apiResponse.getPromotionList()) {
				xmlResponse.getPromotion().add(getXmlPromotion(apiPromotion));
			}
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlResponse, outStringWriter);

			String xmlSendResponse = outStringWriter.toString();
			logger.info("getAppliedAutoPromotions response :\n" + xmlSendResponse);
			
			return xmlSendResponse;
		} catch (JAXBException e) {
			logger.error("Error in the commitAutoPromotion call.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform Promotion Websevice.\n");
		sb.append("To apply auto promotion post to : http://hostname:port/promotionWS/autoPromotion/apply\n");
		sb.append("To commit auto promotion post to : http://hostname:port/promotionWS/autoPromotion/commit\n");
		sb.append("To fetch applied auto promotion for a user post to : http://hostname:port/promotionWS/autoPromotion/getApplied\n");
		sb.append("To refresh auto Promotion cache post to : http://hostname:port/promotionWS/autoPromotion/refresh\n");
		return sb.toString();
	}
	
	@GET
	@Path("/xsd")
	@Produces("application/xml")
	public String getXsd() {	
		InputStream userXsd = this.getClass().getClassLoader().getResourceAsStream("promotion.xsd");
		String userXsdString = convertInputStreamToString(userXsd);
		return userXsdString;
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
			logger.error("promotion.xsd loading error : " + exception.getMessage() );
		}
		return sb.toString();
	}
	
	private OrderDiscount getXmlOrderDiscountValue(com.fb.platform.promotion.model.OrderDiscount apiOrderDiscount) {
		OrderDiscount xmlOrderDiscount = new OrderDiscount();
		
		xmlOrderDiscount.setDiscountValue(apiOrderDiscount.getOrderDiscountValue());
		xmlOrderDiscount.setOrderRequest(getXmlOrderRequest(apiOrderDiscount.getOrderRequest()));
		
		return xmlOrderDiscount;
	}
	
	private Promotion getXmlPromotion(com.fb.platform.promotion.model.Promotion apiPromotion) {
		Promotion xmlPromotion = new Promotion();
		
		xmlPromotion.setDescription(apiPromotion.getDescription());
		xmlPromotion.setIsActive(apiPromotion.isActive());
		xmlPromotion.setPromotionId(apiPromotion.getId());
		xmlPromotion.setPromotionName(apiPromotion.getName());
		
		return xmlPromotion;
	}
	
	private OrderRequest getXmlOrderRequest(com.fb.platform.promotion.to.OrderRequest apiOrderRequest) {
		
		OrderRequest xmlOrderRequest = new OrderRequest();
		
		xmlOrderRequest.setClientId(apiOrderRequest.getClientId());
		xmlOrderRequest.setOrderId(apiOrderRequest.getOrderId());
		
		for(com.fb.platform.promotion.to.OrderItem apiOrderItem : apiOrderRequest.getOrderItems()) {
			xmlOrderRequest.getOrderItem().add(getXmlOrderItem(apiOrderItem));
		}
		
		return xmlOrderRequest;
	}
	
	private OrderItem getXmlOrderItem(com.fb.platform.promotion.to.OrderItem apiOrderItem) {
		OrderItem xmlOrderItem = new OrderItem();
		
		xmlOrderItem.setItemId(apiOrderItem.getItemId());
		xmlOrderItem.setQuantity(apiOrderItem.getQuantity());
		xmlOrderItem.setDiscountValue(apiOrderItem.getTotalDiscount());
		xmlOrderItem.setIsLocked(apiOrderItem.isLocked());
		xmlOrderItem.setProduct(getXmlProduct(apiOrderItem.getProduct()));
		
		return xmlOrderItem;
	}
	
	private Product getXmlProduct(com.fb.platform.promotion.to.Product apiProduct) {
		Product xmlProduct = new Product();

		xmlProduct.setDiscountedPrice(apiProduct.getDiscountedPrice());
		xmlProduct.setMrpPrice(apiProduct.getMrpPrice());
		xmlProduct.setPrice(apiProduct.getPrice());
		xmlProduct.setProductId(apiProduct.getProductId());
		xmlProduct.getBrand().addAll(apiProduct.getBrands());
		xmlProduct.getCategory().addAll(apiProduct.getCategories());
		
		return xmlProduct;
	}
	
	private com.fb.platform.promotion.to.OrderRequest getApiOrderRequest(OrderRequest xmlOrderRequest) {
		
		com.fb.platform.promotion.to.OrderRequest apiOrderRequest = new com.fb.platform.promotion.to.OrderRequest();
		
		apiOrderRequest.setClientId(xmlOrderRequest.getClientId());
		apiOrderRequest.setOrderId(xmlOrderRequest.getOrderId());
		
		for(OrderItem xmlOrderItem : xmlOrderRequest.getOrderItem()) {
			apiOrderRequest.getOrderItems().add(getApiOrderItem(xmlOrderItem));
		}
		
		return apiOrderRequest;
	}
	
	private com.fb.platform.promotion.to.Product getApiProduct(Product xmlProduct) {
		com.fb.platform.promotion.to.Product apiProduct = new com.fb.platform.promotion.to.Product();

		apiProduct.setDiscountedPrice(xmlProduct.getDiscountedPrice());
		apiProduct.setMrpPrice(xmlProduct.getMrpPrice());
		apiProduct.setPrice(xmlProduct.getPrice());
		apiProduct.setProductId(xmlProduct.getProductId());
		apiProduct.setBrands(xmlProduct.getBrand());
		apiProduct.setCategories(xmlProduct.getCategory());
		
		return apiProduct;
	}
	
	private com.fb.platform.promotion.to.OrderItem getApiOrderItem(OrderItem xmlOrderItem) {
		com.fb.platform.promotion.to.OrderItem apiOrderItem = new com.fb.platform.promotion.to.OrderItem();
		
		apiOrderItem.setItemId(xmlOrderItem.getItemId());
		apiOrderItem.setQuantity(xmlOrderItem.getQuantity());
		apiOrderItem.setTotalDiscount(xmlOrderItem.getDiscountValue());
		apiOrderItem.setLocked(xmlOrderItem.isIsLocked());
		apiOrderItem.setProduct(getApiProduct(xmlOrderItem.getProduct()));
		
		return apiOrderItem;
	}
	
	private List<Integer> getInts(String commaSeparatedString) {
		String[] ids = StringUtils.split(commaSeparatedString, ",");
		List<Integer> intIds = new ArrayList<Integer>();
		for(String id : ids) {
			intIds.add(new Integer(StringUtils.trim(id)));
		}
		return intIds;
	}
	
	private DateTime convertToDateTime(String inputDate){
		DateTime convertedDateTime = null;
		try {
			DateTimeFormatter fmt = ISODateTimeFormat.date();
			convertedDateTime = fmt.parseDateTime(inputDate);
		} catch (Exception e) {
			logger.error("Error in converting input date-time field into DateTime object while parsing. Input Date-time receieved in request is - "+inputDate);
		}
		return convertedDateTime;
	}
}
