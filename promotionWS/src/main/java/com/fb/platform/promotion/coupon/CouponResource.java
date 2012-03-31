/**
 * 
 */
package com.fb.platform.promotion.coupon;

import java.io.StringReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion._1_0.CouponRequest;
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
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
