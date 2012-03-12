/**
 * 
 */
package com.fb.platform.user;

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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.auth._1_0.LoginRequest;
import com.fb.platform.auth._1_0.LoginResponse;
import com.fb.platform.auth._1_0.LoginStatus;
import com.fb.platform.auth._1_0.LogoutRequest;
import com.fb.platform.auth._1_0.LogoutResponse;
import com.fb.platform.auth._1_0.LogoutStatus;
import com.fb.platform.user.manager.interfaces.UserManager;

/**
 * @author vinayak
 *
 */
@Path("/auth")
@Scope("request")
@Component
public class AuthResource {

	private static Logger logger = Logger.getLogger(AuthResource.class);

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();
	
	@Autowired
	private UserManager userManager = null;

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.auth._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}

	@POST
	@Path("/login")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String login(String loginXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("LoginXML request: \n" + loginXml);
		}

		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			LoginRequest xmlLoginReq = (LoginRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(loginXml)));

			com.fb.platform.user.manager.model.auth.LoginRequest apiLoginReq = new com.fb.platform.user.manager.model.auth.LoginRequest();
			apiLoginReq.setIpAddress(xmlLoginReq.getIpAddress());
			apiLoginReq.setPassword(xmlLoginReq.getPassword());
			apiLoginReq.setUsername(xmlLoginReq.getUsername());

			com.fb.platform.user.manager.model.auth.LoginResponse apiLoginResp = userManager.login(apiLoginReq);

			LoginResponse xmlLoginResp = new LoginResponse();
			xmlLoginResp.setSessionToken(apiLoginResp.getSessionToken());
			xmlLoginResp.setUserId(apiLoginResp.getUserId());
			xmlLoginResp.setLoginStatus(LoginStatus.fromValue(apiLoginResp.getLoginStatus().name()));

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlLoginResp, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.info("LoginXML response :\n" + xmlResponse);
			}
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the login call.", e);
			return "error"; //TODO return proper error response
		}
	}

	@POST
	@Path("/logout")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String logout(String logoutXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("LogoutXML request :\n" + logoutXml);
		}
		
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			LogoutRequest xmlLogoutReq = (LogoutRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(logoutXml)));

			com.fb.platform.user.manager.model.auth.LogoutRequest apiLogoutReq = new com.fb.platform.user.manager.model.auth.LogoutRequest();
			apiLogoutReq.setSessionToken(xmlLogoutReq.getSessionToken());

			com.fb.platform.user.manager.model.auth.LogoutResponse apiLogoutResp = userManager.logout(apiLogoutReq);

			LogoutResponse xmlLogoutResp = new LogoutResponse();
			xmlLogoutResp.setLogoutStatus(LogoutStatus.fromValue(apiLogoutResp.getLogoutStatus().name()));

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlLogoutResp, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.info("LogoutXML response :\n" + xmlResponse);
			}
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the logout call.", e);
			return "error"; //TODO return proper error response
		}
	}

	@GET
	public String ping() {
		return "hello";
	}
}
