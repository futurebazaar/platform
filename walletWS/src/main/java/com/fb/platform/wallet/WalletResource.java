package com.fb.platform.wallet;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.fb.platform.wallet._1_0.WalletSummaryRequest;
import com.fb.platform.wallet._1_0.WalletSummaryResponse;
import com.fb.platform.wallet._1_0.WalletSummaryStatus;

import com.fb.platform.wallet._1_0.WalletSummaryDetails;
import com.fb.platform.wallet.manager.interfaces.WalletManager;

/**
 * @author Rajesh
 *
 */
@Path("/wallet")
@Component
@Scope("request")
public class WalletResource {

	private static final Log logger = LogFactory.getLog(WalletResource.class);


	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	@Autowired
	private WalletManager walletManager = null;

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.wallet._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}

	@POST
	@Path("/summary")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String getWalletSummary(String walletHistoryXml) {
		
		logger.debug("WALLET SUMMARY XML request: \n" + walletHistoryXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			WalletSummaryRequest xmlWalletSummaryReq = (WalletSummaryRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(walletHistoryXml)));

			com.fb.platform.wallet.manager.model.access.WalletSummaryRequest apiWalletSummaryReq = new com.fb.platform.wallet.manager.model.access.WalletSummaryRequest();
			apiWalletSummaryReq.setUserId(xmlWalletSummaryReq.getUserId());
			apiWalletSummaryReq.setClientId(xmlWalletSummaryReq.getClientId());
			apiWalletSummaryReq.setSessionToken(xmlWalletSummaryReq.getSessionToken());

			com.fb.platform.wallet.manager.model.access.WalletSummaryResponse apiWalletSummaryResp = walletManager.getWalletSummary(apiWalletSummaryReq);

			WalletSummaryResponse xmlWalletSummaryResponse = new WalletSummaryResponse();
			xmlWalletSummaryResponse.setSessionToken(apiWalletSummaryResp.getSessionToken());
			xmlWalletSummaryResponse.setWalletSummaryStatus(WalletSummaryStatus.fromValue(apiWalletSummaryResp.getWalletSummaryStatus().name() ));
			WalletSummaryDetails walletDetails = new WalletSummaryDetails();
			com.fb.platform.wallet.manager.model.access.WalletSummaryDetails apiWalletDetails = apiWalletSummaryResp.getWalletSummaryDetails();
			if(apiWalletDetails != null){
				walletDetails.setRefundAmount(apiWalletDetails.getRefundAmount());
				walletDetails.setGiftAmount(apiWalletDetails.getGiftAmount());
				walletDetails.setTotalAmount(apiWalletDetails.getTotalAmount());
			}
			xmlWalletSummaryResponse.setWalletSummaryDetails(walletDetails);
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlWalletSummaryResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("walletHistoryXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the Wallet Summary request.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform Wallet Websevice.\n");
		sb.append("To get wallet summary post to : http://hostname:port/walletWS/wallet/summary\n");
		return sb.toString();
	}
	
	@GET
	@Path("/xsd")
	@Produces("application/xml")
	public String getXsd() {	
		InputStream walletXsd = this.getClass().getClassLoader().getResourceAsStream("wallet.xsd");
		String walletXsdString = convertInputStreamToString(walletXsd);
		return walletXsdString;
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
			logger.error("User.xsd loading error : " + exception.getMessage() );
		}
		return sb.toString();
	}

}