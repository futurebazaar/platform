package com.fb.platform.user;

import java.io.StringReader;
import java.io.StringWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.fb.commons.PlatformException;
import com.fb.platform.auth._1_0.GetUserRequest;
import com.fb.platform.auth._1_0.GetUserResponse;
import com.fb.platform.auth._1_0.GetUserStatus;
import com.fb.platform.auth._1_0.AddUserRequest;
import com.fb.platform.auth._1_0.AddUserResponse;
import com.fb.platform.auth._1_0.AddUserStatus;
import com.fb.platform.user.manager.interfaces.UserAdminManager;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.sun.jersey.api.core.InjectParam;






/**
 * @author kislaya
 *
 */
@Path("/user")
@Component
@Scope("request")
public class UserResource {
	
	private static Logger logger = Logger.getLogger(UserResource.class);
	

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();
	
	@Autowired
	private UserAdminManager userAdminManager = null;
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.auth._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	 
	@POST
	@Path("/get")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String getUser(String getUserXml){
		if (logger.isDebugEnabled()) {
			logger.debug("GET USER XML request: \n" + getUserXml);
		}
		
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GetUserRequest xmlGetUserReq = (GetUserRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(getUserXml)));
			com.fb.platform.user.manager.model.admin.GetUserRequest apiGetUserReq = new com.fb.platform.user.manager.model.admin.GetUserRequest();
			apiGetUserReq.setKey(xmlGetUserReq.getKey());
			apiGetUserReq.setSessionToken(xmlGetUserReq.getSessionToken());
			
			com.fb.platform.user.manager.model.admin.GetUserResponse apiGetUserRes = userAdminManager.getUser(apiGetUserReq);
			
			GetUserResponse xmlGetUserRes = new GetUserResponse();
			xmlGetUserRes.setUserName(apiGetUserRes.getUserName());
			xmlGetUserRes.setSessionToken(apiGetUserRes.getSessionToken());
			xmlGetUserRes.setGetUserStatus(GetUserStatus.fromValue(apiGetUserRes.getStatus().name()));
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlGetUserRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.info("Get USER XMl response :\n" + xmlGetUserRes);
			}
			return xmlResponse;

			
			
		}catch(JAXBException e) {
			logger.error("Error in the login call.", e);
			return "error"; //TODO return proper error response
		}
			
	}
	@POST
	@Path("/add")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String addUser(String addUserXml){
		if (logger.isDebugEnabled()) {
			logger.debug("ADD USER XML request: \n" + addUserXml);
		}
		
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			AddUserRequest xmlGetUserReq = (AddUserRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(addUserXml)));
			com.fb.platform.user.manager.model.admin.AddUserRequest apiAddUserReq = new com.fb.platform.user.manager.model.admin.AddUserRequest();
			apiAddUserReq.setUserName(xmlGetUserReq.getUserName());
			apiAddUserReq.setPassword(xmlGetUserReq.getPassword());
			
			com.fb.platform.user.manager.model.admin.AddUserResponse apiAddUserRes = userAdminManager.addUser(apiAddUserReq);
			
			AddUserResponse xmlAddUserRes = new AddUserResponse();
			xmlAddUserRes.setSessionToken(apiAddUserRes.getSessionToken());
			xmlAddUserRes.setAddUserStatus(AddUserStatus.fromValue(apiAddUserRes.getStatus().name()));
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlAddUserRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.info("Add USER XMl response :\n" + xmlAddUserRes);
			}
			return xmlResponse;

			
			
		}catch(JAXBException e) {
			logger.error("Error in the login call.", e);
			return "error"; //TODO return proper error response
		}
			
	}
}
