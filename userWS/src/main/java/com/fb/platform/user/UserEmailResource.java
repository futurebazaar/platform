package com.fb.platform.user;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
import com.fb.platform.user.email._1_0.UserEmail;
import com.fb.platform.user.email._1_0.GetUserEmailRequest;
import com.fb.platform.user.email._1_0.GetUserEmailResponse;
import com.fb.platform.user.email._1_0.GetUserEmailStatus;
import com.fb.platform.user.email._1_0.AddUserEmailRequest;
import com.fb.platform.user.email._1_0.AddUserEmailResponse;
import com.fb.platform.user.email._1_0.AddUserEmailStatus;
import com.fb.platform.user.email._1_0.DeleteUserEmailRequest;
import com.fb.platform.user.email._1_0.DeleteUserEmailResponse;
import com.fb.platform.user.email._1_0.DeleteUserEmailStatus;

import com.fb.platform.user.manager.interfaces.UserAdminManager;


/**
 * @author kislaya
 *
 */
@Path("/user/email")
@Component
@Scope("request")

public class UserEmailResource {
	
	private static final Log logger = LogFactory.getLog(UserEmailResource.class);


	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();
	
	@Autowired
	private UserAdminManager userAdminManager = null;
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.user.email._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	@POST
	@Path("/add")
	@Consumes("application/xml")
	@Produces("application/xml")	
	public String add(String addEmailXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("Add Email XML request: \n" + addEmailXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			AddUserEmailRequest xmlAddUserEmailReq = (AddUserEmailRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(addEmailXml)));
			com.fb.platform.user.manager.model.admin.email.AddUserEmailRequest apiAddUserEmailReq = new com.fb.platform.user.manager.model.admin.email.AddUserEmailRequest();
			apiAddUserEmailReq.setSessionToken(xmlAddUserEmailReq.getSessionToken());
			apiAddUserEmailReq.setUserId(xmlAddUserEmailReq.getUserId());
			com.fb.platform.user.manager.model.admin.email.UserEmail userEmail = new com.fb.platform.user.manager.model.admin.email.UserEmail();
			userEmail.setEmail(xmlAddUserEmailReq.getUserEmail().getEmailId());
			userEmail.setType(xmlAddUserEmailReq.getUserEmail().getType());
			apiAddUserEmailReq.setUserEmail(userEmail);
			
			com.fb.platform.user.manager.model.admin.email.AddUserEmailResponse apiAddUserEmailRes = userAdminManager.addUserEmail(apiAddUserEmailReq);
			
			AddUserEmailResponse xmlAddUserEmailRes = new AddUserEmailResponse();
			xmlAddUserEmailRes.setAddUserEmailStatus(AddUserEmailStatus.fromValue(apiAddUserEmailRes.getAddUserEmailStatus().name()));
			xmlAddUserEmailRes.setSessionToken(apiAddUserEmailRes.getSessionToken());
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlAddUserEmailRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Add Add USER Email XMl response :\n" + xmlAddUserEmailRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the get email call.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/get")
	@Consumes("application/xml")
	@Produces("application/xml")	
	public String get(String getEmailXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("GET Email XML request: \n" + getEmailXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GetUserEmailRequest xmlGetUserEmailReq = (GetUserEmailRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(getEmailXml)));
			com.fb.platform.user.manager.model.admin.email.GetUserEmailRequest apiGetUserEmailReq = new com.fb.platform.user.manager.model.admin.email.GetUserEmailRequest();
			apiGetUserEmailReq.setSessionToken(xmlGetUserEmailReq.getSessionToken());
			apiGetUserEmailReq.setUserId(apiGetUserEmailReq.getUserId());
			
			com.fb.platform.user.manager.model.admin.email.GetUserEmailResponse apiGetUserEmailRes = userAdminManager.getUserEmail(apiGetUserEmailReq);
			
			GetUserEmailResponse xmlGetUserEmailRes = new GetUserEmailResponse();
			xmlGetUserEmailRes.setGetUserEmailStatus(GetUserEmailStatus.fromValue(apiGetUserEmailRes.getGetUserEmailStatus().name()));
			xmlGetUserEmailRes.setSessionToken(apiGetUserEmailRes.getSessionToken());
			xmlGetUserEmailRes.setUserId(apiGetUserEmailRes.getUserId());
			
			List<UserEmail> userEmailLst = new ArrayList<UserEmail>();
			if(apiGetUserEmailRes.getUserEmail() != null){
				for (com.fb.platform.user.manager.model.admin.email.UserEmail apiuserEmail : apiGetUserEmailRes.getUserEmail()){
					UserEmail userEmail = new UserEmail();
					userEmail.setEmailId(apiuserEmail.getEmail());
					userEmail.setType(apiuserEmail.getType());
					userEmailLst.add(userEmail);
				}
			}
			xmlGetUserEmailRes.getUserEmail().addAll(userEmailLst);	
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlGetUserEmailRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Add GET USER Email XMl response :\n" + xmlGetUserEmailRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the get email call.", e);
			return "error"; //TODO return proper error response
		}
	}
	@POST
	@Path("/delete")
	@Consumes("application/xml")
	@Produces("application/xml")	
	public String delete(String deleteEmailXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("Delete Email XML request: \n" + deleteEmailXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			DeleteUserEmailRequest xmlDeleteUserEmailReq = (DeleteUserEmailRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(deleteEmailXml)));
			com.fb.platform.user.manager.model.admin.email.DeleteUserEmailRequest apiDeleteUserEmailReq = new com.fb.platform.user.manager.model.admin.email.DeleteUserEmailRequest();
			apiDeleteUserEmailReq.setSessionToken(xmlDeleteUserEmailReq.getSessionToken());
			apiDeleteUserEmailReq.setUserId(xmlDeleteUserEmailReq.getUserId());
			apiDeleteUserEmailReq.setEmailId(xmlDeleteUserEmailReq.getEmailId());
			
			com.fb.platform.user.manager.model.admin.email.DeleteUserEmailResponse apiDeleteUserEmailRes = userAdminManager.deleteUserEmail(apiDeleteUserEmailReq);
			
			DeleteUserEmailResponse xmlDeleteUserEmailRes = new DeleteUserEmailResponse();
			xmlDeleteUserEmailRes.setDeleteUserEmailStatus(DeleteUserEmailStatus.fromValue(apiDeleteUserEmailRes.getDeleteUserEmailStatus().name()));
			xmlDeleteUserEmailRes.setSessionToken(apiDeleteUserEmailRes.getSessionToken());
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlDeleteUserEmailRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Add Delete USER Email XMl response :\n" + xmlDeleteUserEmailRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the get email call.", e);
			return "error"; //TODO return proper error response
		}
	}
	

}
