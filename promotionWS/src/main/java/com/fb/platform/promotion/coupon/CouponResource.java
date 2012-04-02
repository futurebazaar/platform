/**
 * 
 */
package com.fb.platform.promotion.coupon;

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
import com.fb.platform.promotion._1_0.CommitCouponRequest;
import com.fb.platform.promotion._1_0.CommitCouponResponse;
import com.fb.platform.promotion._1_0.CommitCouponStatus;
import com.fb.platform.promotion._1_0.CouponRequest;
import com.fb.platform.promotion._1_0.CouponResponse;
import com.fb.platform.promotion._1_0.CouponStatus;
import com.fb.platform.promotion._1_0.OrderItem;
import com.fb.platform.promotion._1_0.OrderRequest;
import com.fb.platform.promotion._1_0.Product;
import com.fb.platform.promotion.service.PromotionManager;

/**
 * @author vinayak
 *
 */
@Path("/coupon")
@Component
@Scope("request")
public class CouponResource {

	private static Log logger = LogFactory.getLog(CouponResource.class);

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	@Autowired
	private PromotionManager promotionManager = null;

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.promotion._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}

	@POST
	@Path("/apply")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String applyCoupon(String applyCouponXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("applyCouponRequestXML : \n" + applyCouponXml);
		}
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			CouponRequest xmlCouponRequest = (CouponRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(applyCouponXml)));

			com.fb.platform.promotion.to.CouponRequest apiCouponRequest = new com.fb.platform.promotion.to.CouponRequest();
			apiCouponRequest.setClientId(xmlCouponRequest.getClientId());
			apiCouponRequest.setCouponCode(xmlCouponRequest.getCouponCode());
			apiCouponRequest.setSessionToken(xmlCouponRequest.getSessionToken());

			OrderRequest xmlOrderRequest = xmlCouponRequest.getOrderRequest();
			com.fb.platform.promotion.to.OrderRequest apiOrderRequest = getApiOrderRequest(xmlOrderRequest);

			apiCouponRequest.setOrderReq(apiOrderRequest);

			CouponResponse xmlCouponResponse = new CouponResponse();
			com.fb.platform.promotion.to.CouponResponse apiCouponResponse = promotionManager.applyCoupon(apiCouponRequest);

			xmlCouponResponse.setCouponCode(apiCouponResponse.getCouponCode());
			xmlCouponResponse.setCouponStatus(CouponStatus.fromValue(apiCouponResponse.getCouponStatus().toString()));
			xmlCouponResponse.setDiscountValue(apiCouponResponse.getDiscountValue());
			xmlCouponResponse.setSessionToken(apiCouponResponse.getSessionToken());

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlCouponResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("CouponXML response :\n" + xmlResponse);
			}
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the applyCoupon call.", e);
			return "error"; //TODO return proper error response
		}
	}

	@POST
	@Path("/commit")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String commitCoupon(String commitCouponXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("commitCouponRequestXML : \n" + commitCouponXml);
		}
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			CommitCouponRequest xmlCommitCouponRequest = (CommitCouponRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(commitCouponXml)));

			com.fb.platform.promotion.to.CommitCouponRequest apiCommitCouponRequest = new com.fb.platform.promotion.to.CommitCouponRequest();
			apiCommitCouponRequest.setCouponCode(xmlCommitCouponRequest.getCouponCode());
			apiCommitCouponRequest.setDiscountValue(xmlCommitCouponRequest.getDiscountValue());
			apiCommitCouponRequest.setOrderId(xmlCommitCouponRequest.getOrderId());
			apiCommitCouponRequest.setSessionToken(xmlCommitCouponRequest.getSessionToken());

			com.fb.platform.promotion.to.CommitCouponResponse apiCommitCouponResponse = promotionManager.commitCouponUse(apiCommitCouponRequest);

			CommitCouponResponse xmlCommitCouponResponse = new CommitCouponResponse();
			xmlCommitCouponResponse.setCommitCouponStatus(CommitCouponStatus.fromValue(apiCommitCouponResponse.getCommitCouponStatus().toString()));
			xmlCommitCouponResponse.setSessionToken(apiCommitCouponResponse.getSessionToken());

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlCommitCouponResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("CouponXML response :\n" + xmlResponse);
			}
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the commitCoupon call.", e);
			return "error"; //TODO return proper error response
		}
	}

	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform Promotion Websevice.\n");
		sb.append("To apply Coupon post to : http://hostname:port/promotionWS/coupon/apply\n");
		sb.append("To commit Coupon post to : http://hostname:port/promotionWS/coupon/commit\n");
		return sb.toString();
	}

	private com.fb.platform.promotion.to.OrderRequest getApiOrderRequest(OrderRequest xmlOrderRequest) {
		com.fb.platform.promotion.to.OrderRequest apiOrderRequest = new com.fb.platform.promotion.to.OrderRequest();
		apiOrderRequest.setOrderId(xmlOrderRequest.getOrderId());
		apiOrderRequest.setOrderValue(xmlOrderRequest.getOrderValue());

		for (OrderItem xmlOrderItem : xmlOrderRequest.getOrderItem()) {
			com.fb.platform.promotion.to.OrderItem apiOrderItem = createApiOrderItem(xmlOrderItem);
			apiOrderRequest.getOrderItems().add(apiOrderItem);
		}
		return apiOrderRequest;
	}

	private com.fb.platform.promotion.to.OrderItem createApiOrderItem(OrderItem xmlOrderItem) {
		com.fb.platform.promotion.to.OrderItem apiOrderItem = new com.fb.platform.promotion.to.OrderItem();
		apiOrderItem.setPrice(xmlOrderItem.getPrice());
		apiOrderItem.setQuantity(xmlOrderItem.getQuantity());

		Product xmlProduct = xmlOrderItem.getProduct();
		com.fb.platform.promotion.to.Product apiProduct = createApiProduct(xmlProduct);
		apiOrderItem.setProduct(apiProduct);

		return apiOrderItem;
	}

	private com.fb.platform.promotion.to.Product createApiProduct(Product xmlProduct) {
		com.fb.platform.promotion.to.Product apiProduct = new com.fb.platform.promotion.to.Product();
		apiProduct.setBrands(xmlProduct.getBrand());
		apiProduct.setCategories(xmlProduct.getCategory());
		apiProduct.setPrice(xmlProduct.getPrice());
		apiProduct.setProductId(xmlProduct.getProductId());
		return apiProduct;
	}
}
