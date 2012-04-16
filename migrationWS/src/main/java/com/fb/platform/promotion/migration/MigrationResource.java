/**
 * 
 */
package com.fb.platform.promotion.migration;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
@Path("/migration")
@Component
@Scope("request")
public class MigrationResource {

	private static Log logger = LogFactory.getLog(MigrationResource.class);

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.promotion._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}

	@POST
	@Path("/migrate")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String applyCoupon(String migrationXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("migrationRequestXML : \n" + migrationXml);
		}
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

//			ApplyCouponRequest xmlCouponRequest = (ApplyCouponRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(applyCouponXml)));
//
//			com.fb.platform.promotion.to.ApplyCouponRequest apiCouponRequest = new com.fb.platform.promotion.to.ApplyCouponRequest();
//			apiCouponRequest.setCouponCode(xmlCouponRequest.getCouponCode());
//			apiCouponRequest.setSessionToken(xmlCouponRequest.getSessionToken());
//
//			OrderRequest xmlOrderRequest = xmlCouponRequest.getOrderRequest();
//			com.fb.platform.promotion.to.OrderRequest apiOrderRequest = getApiOrderRequest(xmlOrderRequest);
//
//			apiCouponRequest.setOrderReq(apiOrderRequest);
//
//			ApplyCouponResponse xmlCouponResponse = new ApplyCouponResponse();
//			com.fb.platform.promotion.to.ApplyCouponResponse apiCouponResponse = promotionManager.applyCoupon(apiCouponRequest);
//
//			xmlCouponResponse.setCouponCode(apiCouponResponse.getCouponCode());
//			xmlCouponResponse.setCouponStatus(CouponStatus.fromValue(apiCouponResponse.getCouponStatus().toString()));
//			xmlCouponResponse.setDiscountValue(apiCouponResponse.getDiscountValue());
//			xmlCouponResponse.setSessionToken(apiCouponResponse.getSessionToken());
//			xmlCouponResponse.setPromoName(apiCouponResponse.getPromoName());
//			xmlCouponResponse.setPromoDescription(apiCouponResponse.getPromoDescription());
//			xmlCouponResponse.setStatusMessage(apiCouponResponse.getStatusMessage());
//
//			StringWriter outStringWriter = new StringWriter();
//			Marshaller marsheller = context.createMarshaller();
//			marsheller.marshal(xmlCouponResponse, outStringWriter);
//
//			String xmlResponse = outStringWriter.toString();
//			if (logger.isDebugEnabled()) {
//				logger.debug("CouponXML response :\n" + xmlResponse);
//			}
//			return xmlResponse;
			return null;

		} catch (JAXBException e) {
			logger.error("Error in the applyCoupon call.", e);
			return "error"; //TODO return proper error response
		}
	}

}
