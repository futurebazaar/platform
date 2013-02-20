package com.fb.platform.sap;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.sap._1_0.Dashboard;
import com.fb.platform.sap._1_0.InventoryDashboardRequest;
import com.fb.platform.sap._1_0.InventoryDashboardResponse;
import com.fb.platform.sap._1_0.InventoryLevelRequest;
import com.fb.platform.sap._1_0.InventoryLevelResponse;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardResponseTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.fb.platform.sap.client.handler.PlatformClientHandler;

/**
 * @author anubhav
 *
 */

@Path("/inventory")
@Component
@Scope("request")
public class InventoryResource {

	private static Log logger = LogFactory.getLog(InventoryResource.class);
	
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
	
	@Autowired
	private PlatformClientHandler sapClientHandler = null;
	
	@Path("/stock")
	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	public String inventorylevel(String inventoryLevelXml) {
		logger.info("InventoryLevelXML : \n" + inventoryLevelXml);
		
		try {	
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InventoryLevelRequest inventoryLevelRequest = (InventoryLevelRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(inventoryLevelXml)));
			SapInventoryLevelRequestTO sapInventoryLevelRequestTO = new SapInventoryLevelRequestTO();
			sapInventoryLevelRequestTO.setMaterial(inventoryLevelRequest.getMaterial());
			sapInventoryLevelRequestTO.setPlant(inventoryLevelRequest.getPlant());
			sapInventoryLevelRequestTO.setStorageLocation(inventoryLevelRequest.getStorageLocation());
			sapInventoryLevelRequestTO.setClient(inventoryLevelRequest.getClient());
			SapInventoryLevelResponseTO sapInventoryLevelResponseTO = sapClientHandler.processInventoryLevel(sapInventoryLevelRequestTO);
			InventoryLevelResponse inventoryLevelResponse = new InventoryLevelResponse();
			inventoryLevelResponse.setArticle(sapInventoryLevelResponseTO.getArticle());
			inventoryLevelResponse.setSite(sapInventoryLevelResponseTO.getSite());
			inventoryLevelResponse.setStockQuantity(sapInventoryLevelResponseTO.getStockQuantity());
			inventoryLevelResponse.setStorageLocation(sapInventoryLevelResponseTO.getStorageLocation());
			inventoryLevelResponse.setUnit(sapInventoryLevelResponseTO.getUnit());
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(inventoryLevelResponse, outStringWriter);
			String xmlResponse = outStringWriter.toString();
			logger.info("Sap InventoryLevelXML response :\n" + xmlResponse);
			return xmlResponse;
		} catch (Exception e) {
			logger.error("Error in the Sap Inventory Level call.", e);
			return "error";
		}
	}
	
	@Path("/dashboard")
	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	public String inventoryDashboard(String inventoryDashboardXml) {
		logger.info("InventoryDashboardlXML : \n" + inventoryDashboardXml);
		
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InventoryDashboardRequest inventoryDashboardRequest = (InventoryDashboardRequest)unmarshaller.unmarshal(new StreamSource(new StringReader(inventoryDashboardXml)));
			SapInventoryDashboardRequestTO sapInventoryDashboardRequestTO = setInventoryRequestDetails(inventoryDashboardRequest);
			List<SapInventoryDashboardResponseTO> sapInventoryDashboardResponseTOList = sapClientHandler.processInventoryDashboard(sapInventoryDashboardRequestTO);
			InventoryDashboardResponse inventoryDashboardResponse = new InventoryDashboardResponse();
			for (SapInventoryDashboardResponseTO inventoryDashboardResponseTO : sapInventoryDashboardResponseTOList) {
				inventoryDashboardResponse.getDashboard().add(setDashboardResponseDetails(inventoryDashboardResponseTO));
			}
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(inventoryDashboardResponse, outStringWriter);
			String xmlResponse = outStringWriter.toString();
			logger.info("Sap InventoryDashboardXML response :\n" + xmlResponse);
			return xmlResponse;
		} catch (Exception e) {
			logger.error("Error in the Sap Inventory Dashboard call.", e);
			return "error";
		}
	}
	
	private Dashboard setDashboardResponseDetails(SapInventoryDashboardResponseTO inventoryDashboardResponseTO) throws DatatypeConfigurationException {
		Dashboard inventoryDashboard = new Dashboard();
		inventoryDashboard.setActualQuantity(inventoryDashboardResponseTO.getActualQuantity());
		inventoryDashboard.setArticle(inventoryDashboardResponseTO.getArticle());
		inventoryDashboard.setArticleDocument(inventoryDashboardResponseTO.getArticleDocument());
		inventoryDashboard.setCancelGR(inventoryDashboardResponseTO.getCancelGR());
		if (inventoryDashboardResponseTO.getCreationDateTime() != null) {
			GregorianCalendar gregCal = inventoryDashboardResponseTO.getCreationDateTime().toGregorianCalendar();
			inventoryDashboard.setCreationDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));	
		}
		inventoryDashboard.setIdocNumber(inventoryDashboardResponseTO.getIdocNumber());
		inventoryDashboard.setMovementType(inventoryDashboardResponseTO.getMovementType());
		inventoryDashboard.setPoNumber(inventoryDashboardResponseTO.getPoNumber());
		inventoryDashboard.setReceivingLocation(inventoryDashboardResponseTO.getReceivingLocation());
		inventoryDashboardResponseTO.setReceivingSite(inventoryDashboardResponseTO.getReceivingSite());
		inventoryDashboard.setSegmentNumber(inventoryDashboardResponseTO.getSegmentNumber());
		inventoryDashboardResponseTO.setSupplyingLocation(inventoryDashboardResponseTO.getSupplyingLocation());
		inventoryDashboard.setSupplyingSite(inventoryDashboardResponseTO.getSupplyingSite());
		inventoryDashboard.setTransactionCode(inventoryDashboardResponseTO.getTransactionCode());
		inventoryDashboard.setTransferQuantity(inventoryDashboardResponseTO.getTransferQuantity());
		inventoryDashboardResponseTO.setUnit(inventoryDashboardResponseTO.getUnit());
		return inventoryDashboard;
	}

	private SapInventoryDashboardRequestTO setInventoryRequestDetails(InventoryDashboardRequest inventoryDashboardRequest) {
		SapInventoryDashboardRequestTO sapInventoryDashboardRequestTO = new SapInventoryDashboardRequestTO();
		sapInventoryDashboardRequestTO.setArticle(inventoryDashboardRequest.getArticle());
		XMLGregorianCalendar gregCal = inventoryDashboardRequest.getFromDateTime();
		sapInventoryDashboardRequestTO.setFromDateTime(new DateTime(new DateTime(gregCal.getYear(), gregCal.getMonth(), gregCal.getDay(), 
				gregCal.getHour(), gregCal.getMinute())));
		sapInventoryDashboardRequestTO.setPlant(inventoryDashboardRequest.getPlant());
		gregCal = inventoryDashboardRequest.getToDateTime();
		sapInventoryDashboardRequestTO.setToDateTime(new DateTime(new DateTime(gregCal.getYear(), gregCal.getMonth(), gregCal.getDay(), 
				gregCal.getHour(), gregCal.getMinute())));
		sapInventoryDashboardRequestTO.setClient(inventoryDashboardRequest.getClient());
		return sapInventoryDashboardRequestTO;
	}
}
