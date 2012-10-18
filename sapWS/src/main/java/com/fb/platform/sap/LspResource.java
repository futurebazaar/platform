package com.fb.platform.sap;

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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.sap._1_0.AwbUpdateRequest;
import com.fb.platform.sap._1_0.AwbUpdateResponse;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateResponseTO;
import com.fb.platform.sap.client.handler.PlatformClientHandler;

@Path("/lsp")
@Component
@Scope("request")
public class LspResource {

private static Log logger = LogFactory.getLog(LspResource.class);
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.sap._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	private PlatformClientHandler sapClientHandler = null;
	
	@Path("/assignAWB")
	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	public String assignAwbToDeliveryl(String lspAwbUpdateXml) {
		logger.info("LspAwbUpdateXML : \n" + lspAwbUpdateXml);
		
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			AwbUpdateRequest awbUpdateRequest = (AwbUpdateRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(lspAwbUpdateXml)));
			SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO = new SapLspAwbUpdateRequestTO();
			lspAwbUpdateRequestTO.setAwb(awbUpdateRequest.getAwb());
			lspAwbUpdateRequestTO.setDeliveryNumber(awbUpdateRequest.getDeliveryNumber());
			lspAwbUpdateRequestTO.setLspCode(awbUpdateRequest.getLspCode());
			SapLspAwbUpdateResponseTO lspAwbUpdateResponseTO = sapClientHandler.processLspAwbUpdate(lspAwbUpdateRequestTO);
			AwbUpdateResponse awbUpdateResponse = new AwbUpdateResponse();
			awbUpdateResponse.setMessage(lspAwbUpdateResponseTO.getMessage());
			awbUpdateResponse.setType(lspAwbUpdateResponseTO.getType());
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(awbUpdateResponse, outStringWriter);
			String xmlResponse = outStringWriter.toString();
			logger.info("Sap InventoryDashboardXML response :\n" + xmlResponse);
			return xmlResponse;
		} catch (Exception e) {
			logger.error("Error in the Sap Inventory Dashboard call.", e);
			return "error";
		}
	}
	
	public void setSapClientHandler(PlatformClientHandler sapClientHandler) {
		this.sapClientHandler = sapClientHandler;
	}
	
}
