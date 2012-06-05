/**
 * 
 */
package com.fb.platform.egv.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.egv._1_0.*;
import com.fb.platform.egv.mapper.CreateGVMapper;
import com.fb.platform.egv.mapper.GetInfoMapper;
import com.fb.platform.egv.service.GiftVoucherManager;

/**
 * @author keith
 *
 */
@Path("/egv")
@Component
@Scope("request")
public class GiftVoucherResource {

	private static Log logger = LogFactory.getLog(GiftVoucherResource.class);

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	public static JAXBContext getContext() {
		return context;
	}

	@Autowired
	private GiftVoucherManager giftVoucherManager = null;

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.egv._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}

	@POST
	@Path("/getInfo")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String getInfo(String getInfoRequestXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("getInfoRequestXML : \n" + getInfoRequestXml);
		}
		try {
			com.fb.platform.egv.to.GetInfoRequest getRequest = GetInfoMapper.xmlToCoreRequest(getInfoRequestXml);
			
			com.fb.platform.egv.to.GetInfoResponse getResponse = giftVoucherManager.getInfo(getRequest);
					
			String getInfoResponseXml = GetInfoMapper.CoreResponseToXml(getResponse);

			if (logger.isDebugEnabled()) {
				logger.debug("GetInfo Response XML :\n" + getInfoResponseXml);
			}
			
			return getInfoResponseXml;

		} catch (JAXBException e) {
			logger.error("Error in the getGiftVoucherInfo call : ", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the getGiftVoucherInfo call : ", e);
			return "error"; //TODO return proper error response
		}
	}

	@POST
	@Path("/create")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String create(String createGVXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("getInfoRequestXML : \n" + createGVXml);
		}
		try {
			com.fb.platform.egv.to.CreateRequest createRequest = CreateGVMapper.xmlToCoreRequest(createGVXml);
			
			com.fb.platform.egv.to.CreateResponse createResponse = giftVoucherManager.create(createRequest);
					
			String createResponseXml = CreateGVMapper.CoreResponseToXml(createResponse);
			
			if (logger.isDebugEnabled()) {
				logger.debug("GetInfo Response XML :\n" + createResponseXml);
			}
			
			return createResponseXml;

		} catch (JAXBException e) {
			logger.error("Error in the getGiftVoucherInfo call : ", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the getGiftVoucherInfo call : ", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform Promotion Websevice.\n");
		sb.append("To get GiftVoucher Information post to : http://hostname:port/eGVWS/egv/getInfo\n");
		sb.append("To create a new Gift Voucher post to : http://hostname:port/eGVWS/egv/create\n");
		return sb.toString();
	}
	
	@GET
	@Path("/xsd")
	@Produces("application/xml")
	public String getXsd() {	
		InputStream eGVXsd = this.getClass().getClassLoader().getResourceAsStream("promotion.xsd");
		String eGVXsdString = convertInputStreamToString(eGVXsd);
		return eGVXsdString;
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
	
}
