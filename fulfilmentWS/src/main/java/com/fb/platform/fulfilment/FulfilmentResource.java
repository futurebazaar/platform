package com.fb.platform.fulfilment;

import java.io.StringReader;
import java.io.StringWriter;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.fulfilment.service.FulfilmentManager;

import com.fb.platform.fulfilment._1_0.SellerByPincodeRequest;
import com.fb.platform.fulfilment._1_0.SellerByPincodeResponse;

/**
 * @author suhas
 *
 */
@Path("/fulfilment")
@Component
@Scope("request")
public class FulfilmentResource {
	
	private static Log logger = LogFactory.getLog(FulfilmentResource.class);

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	@Autowired
	private FulfilmentManager fulfilmentManager = null;

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.fulfilment._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	@POST
	@Path("/sellerbypincode")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String getSellersByPincode(String sellerByPincodeXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("sellerByPincodeXML : \n" + sellerByPincodeXml);
		}
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			SellerByPincodeRequest xmlSellerByPincodeRequest = (SellerByPincodeRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(sellerByPincodeXml)));

			com.fb.platform.fulfilment.to.SellerByPincodeRequest apiSellerByPincodeRequest = new com.fb.platform.fulfilment.to.SellerByPincodeRequest();
			apiSellerByPincodeRequest.setPincode(xmlSellerByPincodeRequest.getPincode());
			
			SellerByPincodeResponse xmlSellerByPincodeResponse = new SellerByPincodeResponse();
			com.fb.platform.fulfilment.to.SellerByPincodeResponse apiSellerByPincodeResponse = fulfilmentManager.getSellerByPincode(apiSellerByPincodeRequest);

			xmlSellerByPincodeResponse.setPincode(apiSellerByPincodeResponse.getPincode());
			
			List<Integer> seller_id = apiSellerByPincodeResponse.getSellerId();
			String seller_id_string = "";
			if(seller_id != null){
				seller_id_string = seller_id.toString();
			}else{
				seller_id_string = "[]";
			}
			xmlSellerByPincodeResponse.setSellers(seller_id_string);
			
			xmlSellerByPincodeResponse.setStatusCode(apiSellerByPincodeResponse.getStatusCode());
			xmlSellerByPincodeResponse.setStatusMessage(apiSellerByPincodeResponse.getStatusMessage());

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlSellerByPincodeResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("SellerByPincodeXML response :\n" + xmlResponse);
			}
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the applyCoupon call.", e);
			return "error"; //TODO return proper error response
		}
	}

}
