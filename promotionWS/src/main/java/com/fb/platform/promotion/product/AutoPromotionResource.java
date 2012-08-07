/**
 * 
 */
package com.fb.platform.promotion.product;

import java.io.StringReader;
import java.io.StringWriter;

import javax.ws.rs.Consumes;
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
import com.fb.platform.promotion._1_0.ClearPromotionCacheRequest;
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
}
