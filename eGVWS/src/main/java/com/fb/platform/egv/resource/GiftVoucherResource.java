/**
 * 
 */
package com.fb.platform.egv.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.egv.mapper.ApplyMapper;
import com.fb.platform.egv.mapper.CancelMapper;
import com.fb.platform.egv.mapper.CreateGVMapper;
import com.fb.platform.egv.mapper.GetInfoMapper;
import com.fb.platform.egv.mapper.RollbackUseMapper;
import com.fb.platform.egv.mapper.UseMapper;
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
					
			String getInfoResponseXml = GetInfoMapper.coreResponseToXml(getResponse);

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
			logger.debug("createRequestXML : \n" + createGVXml);
		}
		try {
			com.fb.platform.egv.to.CreateRequest createRequest = CreateGVMapper.xmlToCoreRequest(createGVXml);
			
			com.fb.platform.egv.to.CreateResponse createResponse = giftVoucherManager.create(createRequest);
			
			String createResponseXml = CreateGVMapper.coreResponseToXml(createResponse);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Create Response XML :\n" + createResponseXml);
			}
			
			return createResponseXml;

		} catch (JAXBException e) {
			logger.error("Error in the create GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the create GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		}
	}

	
	@POST
	@Path("/cancel")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String cancel(String cancelRequestXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("CancelRequestXML : \n" + cancelRequestXml);
		}
		try {
			com.fb.platform.egv.to.CancelRequest cancelRequest = CancelMapper.xmlToCoreRequest(cancelRequestXml);
			
			com.fb.platform.egv.to.CancelResponse cancelResponse = giftVoucherManager.cancel(cancelRequest);
					
			String cancelResponseXml = CancelMapper.coreResponseToXml(cancelResponse);

			if (logger.isDebugEnabled()) {
				logger.debug("Cancel Response XML :\n" + cancelResponseXml);
			}
			
			return cancelResponseXml;

		} catch (JAXBException e) {
			logger.error("Error in the cancel GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the cancel GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/apply")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String apply(String applyRequestXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("ApplyRequestXML : \n" + applyRequestXml);
		}
		try {
			com.fb.platform.egv.to.ApplyRequest applyRequest = ApplyMapper.xmlToCoreRequest(applyRequestXml);
			
			com.fb.platform.egv.to.ApplyResponse applyResponse = giftVoucherManager.apply(applyRequest);
					
			String applyResponseXml = ApplyMapper.coreResponseToXml(applyResponse);

			if (logger.isDebugEnabled()) {
				logger.debug("Apply Response XML :\n" + applyResponseXml);
			}
			
			return applyResponseXml;

		} catch (JAXBException e) {
			logger.error("Error in the apply GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the apply GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/use")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String use(String useRequestXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("UseRequestXML : \n" + useRequestXml);
		}
		try {
			com.fb.platform.egv.to.UseRequest useRequest = UseMapper.xmlToCoreRequest(useRequestXml);
			
			com.fb.platform.egv.to.UseResponse useResponse = giftVoucherManager.use(useRequest);
			
			String useResponseXml = UseMapper.coreResponseToXml(useResponse);

			if (logger.isDebugEnabled()) {
				logger.debug("Use Response XML :\n" + useResponseXml);
			}
			
			return useResponseXml;

		} catch (JAXBException e) {
			logger.error("Error in the use GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the use GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		}
	}


	@POST
	@Path("/rollbackUse")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String rollbackUse(String rollbackUseRequestXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("RollbackUseRequestXML : \n" + rollbackUseRequestXml);
		}
		try {
			com.fb.platform.egv.to.RollbackUseRequest useRollbackRequest = RollbackUseMapper.xmlToCoreRequest(rollbackUseRequestXml);
			
			com.fb.platform.egv.to.RollbackUseResponse useRollbackResponse = giftVoucherManager.rollbackUse(useRollbackRequest);
			
			String useRollbackResponseXml = RollbackUseMapper.coreResponseToXml(useRollbackResponse);

			if (logger.isDebugEnabled()) {
				logger.debug("Use Response XML :\n" + useRollbackResponseXml);
			}
			
			return useRollbackResponseXml;

		} catch (JAXBException e) {
			logger.error("Error in the use GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the use GiftVoucher call : ", e);
			return "error"; //TODO return proper error response
		}
	}

	
	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform Gift Voucher Websevice.\n");
		sb.append("To get GiftVoucher Information post to : http://hostname:port/eGVWS/egv/getInfo\n");
		sb.append("To create a new Gift Voucher post to : http://hostname:port/eGVWS/egv/create\n");
		sb.append("To apply a Gift Voucher post to : http://hostname:port/eGVWS/egv/apply\n");
		sb.append("To use a Gift Voucher post to : http://hostname:port/eGVWS/egv/use\n");
		sb.append("To cancel a Gift Voucher post to : http://hostname:port/eGVWS/egv/cancel\n");
		return sb.toString();
	}
	
	@GET
	@Path("/xsd")
	@Produces("application/xml")
	public String getXsd() {	
		InputStream eGVXsd = this.getClass().getClassLoader().getResourceAsStream("egv.xsd");
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
			logger.error("egv.xsd loading error : " + exception.getMessage() );
		}
		return sb.toString();
	}
	
}
