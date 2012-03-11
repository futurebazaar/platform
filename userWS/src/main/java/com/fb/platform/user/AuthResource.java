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

import com.fb.platform.auth._1_0.LoginRequest;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginResponse;

/**
 * @author vinayak
 *
 */
@Path("/auth")
@Scope("request")
@Component
public class AuthResource {

	private static Logger logger = Logger.getLogger(AuthResource.class);

	@Autowired
	private UserManager userManager = null;

	@POST
	@Path("/login")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String login(String loginXml) {
		logger.info("LoginXML : \n" + loginXml);

		try {
			JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
			Unmarshaller unmarshaller = context.createUnmarshaller();

			LoginRequest xmlLoginReq = (LoginRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(loginXml)));

			com.fb.platform.user.manager.model.auth.LoginRequest apiLoginReq = new com.fb.platform.user.manager.model.auth.LoginRequest();
			apiLoginReq.setIpAddress(xmlLoginReq.getIpAddress());
			apiLoginReq.setPassword(xmlLoginReq.getPassword());
			apiLoginReq.setUsername(xmlLoginReq.getUsername());

			LoginResponse apiLoginResp = userManager.login(apiLoginReq);

			com.fb.platform.auth._1_0.LoginResponse xmlLoginResp = new com.fb.platform.auth._1_0.LoginResponse();
			xmlLoginResp.setSessionToken(apiLoginResp.getSessionToken());
			xmlLoginResp.setUserId(apiLoginResp.getUserId());

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlLoginResp, outStringWriter);

			logger.info("Got the login Response :\n" + outStringWriter.toString());
			return outStringWriter.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return "error"; //TODO return proper error response
	}

	@GET
	public String test() {
		return "hello";
	}
}
