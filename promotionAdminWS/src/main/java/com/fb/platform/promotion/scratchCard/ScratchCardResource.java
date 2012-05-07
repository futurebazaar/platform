/**
 * 
 */
package com.fb.platform.promotion.scratchCard;

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
import com.fb.platform.promotion._1_0.ApplyScratchCardRequest;
import com.fb.platform.promotion._1_0.ApplyScratchCardResponse;
import com.fb.platform.promotion._1_0.ApplyScratchCardStatus;
import com.fb.platform.promotion.service.PromotionManager;

/**
 * @author vinayak
 *
 */
@Path("/scratchCard")
@Component
@Scope("request")
public class ScratchCardResource {

	private static Log logger = LogFactory.getLog(ScratchCardResource.class);

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
	public String apply(String applyScratchCardXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("applyScratchCardRequestXML : \n" + applyScratchCardXml);
		}
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			ApplyScratchCardRequest xmlRequest = (ApplyScratchCardRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(applyScratchCardXml)));

			com.fb.platform.promotion.to.ApplyScratchCardRequest apiRequest = new com.fb.platform.promotion.to.ApplyScratchCardRequest();
			apiRequest.setCardNumber(xmlRequest.getCardNumber());
			apiRequest.setSessionToken(xmlRequest.getSessionToken());

			com.fb.platform.promotion.to.ApplyScratchCardResponse apiResponse = promotionManager.applyScratchCard(apiRequest);

			ApplyScratchCardResponse xmlResponse = new ApplyScratchCardResponse();
			xmlResponse.setCouponCode(apiResponse.getCouponCode());
			xmlResponse.setApplyScratchCardStatus(ApplyScratchCardStatus.fromValue(apiResponse.getApplyScratchCardStatus().toString()));
			xmlResponse.setSessionToken(apiResponse.getSessionToken());

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlResponse, outStringWriter);

			String xmlResponseStr = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("applyScratchCardXML response :\n" + xmlResponseStr);
			}
			return xmlResponseStr;

		} catch (JAXBException e) {
			logger.error("Error while marshalling and unmarshalling applyScratchCard request and response.", e);
			return "error"; //TODO
		} catch (Exception e) {
			logger.error("Error while processing applyScratchCard request.", e);
			return "error"; //TODO
		}
	}

	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform Promotions ScratchCard Websevice.\n");
		sb.append("To apply scratchCard post to : http://hostname:port/promotionWS/scratchCard/apply\n");
		sb.append("To get the xml schema definition Get to : http://hostname:port/promotionWS/coupon/xsd\n");
		return sb.toString();
	}

}
