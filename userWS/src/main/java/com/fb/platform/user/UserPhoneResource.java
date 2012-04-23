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
import com.fb.platform.user.phone._1_0.UserPhone;
import com.fb.platform.user.phone._1_0.GetUserPhoneRequest;
import com.fb.platform.user.phone._1_0.GetUserPhoneResponse;
import com.fb.platform.user.phone._1_0.GetUserPhoneStatus;
import com.fb.platform.user.phone._1_0.AddUserPhoneRequest;
import com.fb.platform.user.phone._1_0.AddUserPhoneResponse;
import com.fb.platform.user.phone._1_0.AddUserPhoneStatus;
import com.fb.platform.user.phone._1_0.DeleteUserPhoneRequest;
import com.fb.platform.user.phone._1_0.DeleteUserPhoneResponse;
import com.fb.platform.user.phone._1_0.DeleteUserPhoneStatus;
import com.fb.platform.user.phone._1_0.VerifyUserPhoneRequest;
import com.fb.platform.user.phone._1_0.VerifyUserPhoneResponse;
import com.fb.platform.user.phone._1_0.VerifyUserPhoneStatus;

import com.fb.platform.user.manager.interfaces.UserAdminManager;


/**
 * @author kislaya
 *
 */
@Path("/user/phone")
@Component
@Scope("request")

public class UserPhoneResource {
	
	private static final Log logger = LogFactory.getLog(UserPhoneResource.class);


	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();
	
	@Autowired
	private UserAdminManager userAdminManager = null;
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.user.phone._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	@POST
	@Path("/add")
	@Consumes("application/xml")
	@Produces("application/xml")	
	public String add(String addPhoneXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("Add Phone XML request: \n" + addPhoneXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			AddUserPhoneRequest xmlAddUserPhoneReq = (AddUserPhoneRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(addPhoneXml)));
			com.fb.platform.user.manager.model.admin.phone.AddUserPhoneRequest apiAddUserPhoneReq = new com.fb.platform.user.manager.model.admin.phone.AddUserPhoneRequest();
			apiAddUserPhoneReq.setSessionToken(xmlAddUserPhoneReq.getSessionToken());
			apiAddUserPhoneReq.setUserId(xmlAddUserPhoneReq.getUserId());
			com.fb.platform.user.manager.model.admin.phone.UserPhone userPhone = new com.fb.platform.user.manager.model.admin.phone.UserPhone();
			userPhone.setPhone(xmlAddUserPhoneReq.getUserPhone().getPhone());
			userPhone.setType(xmlAddUserPhoneReq.getUserPhone().getType());
			apiAddUserPhoneReq.setUserPhone(userPhone);
			
			com.fb.platform.user.manager.model.admin.phone.AddUserPhoneResponse apiAddUserPhoneRes = userAdminManager.addUserPhone(apiAddUserPhoneReq);
			
			AddUserPhoneResponse xmlAddUserPhoneRes = new AddUserPhoneResponse();
			xmlAddUserPhoneRes.setAddUserPhoneStatus(AddUserPhoneStatus.fromValue(apiAddUserPhoneRes.getAddUserPhoneStatus().name()));
			xmlAddUserPhoneRes.setSessionToken(apiAddUserPhoneRes.getSessionToken());
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlAddUserPhoneRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Add Add USER Phone XMl response :\n" + xmlAddUserPhoneRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the get Phone call.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/get")
	@Consumes("application/xml")
	@Produces("application/xml")	
	public String get(String getPhoneXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("ADD Phone XML request: \n" + getPhoneXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GetUserPhoneRequest xmlGetUserPhoneReq = (GetUserPhoneRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(getPhoneXml)));
			com.fb.platform.user.manager.model.admin.phone.GetUserPhoneRequest apiGetUserPhoneReq = new com.fb.platform.user.manager.model.admin.phone.GetUserPhoneRequest();
			apiGetUserPhoneReq.setSessionToken(xmlGetUserPhoneReq.getSessionToken());
			apiGetUserPhoneReq.setUserId(apiGetUserPhoneReq.getUserId());
			
			com.fb.platform.user.manager.model.admin.phone.GetUserPhoneResponse apiGetUserPhoneRes = userAdminManager.getUserPhone(apiGetUserPhoneReq);
			
			GetUserPhoneResponse xmlGetUserPhoneRes = new GetUserPhoneResponse();
			xmlGetUserPhoneRes.setGetUserPhoneStatus(GetUserPhoneStatus.fromValue(apiGetUserPhoneRes.getGetUserPhoneStatus().name()));
			xmlGetUserPhoneRes.setSessionToken(apiGetUserPhoneRes.getSessionToken());
			xmlGetUserPhoneRes.setUserId(apiGetUserPhoneRes.getUserId());
			
			List<UserPhone> userPhoneLst = new ArrayList<UserPhone>();
			if(apiGetUserPhoneRes.getUserPhone() != null){
				for (com.fb.platform.user.manager.model.admin.phone.UserPhone apiuserPhone : apiGetUserPhoneRes.getUserPhone()){
					UserPhone userPhone = new UserPhone();
					userPhone.setPhone(apiuserPhone.getPhone());
					userPhone.setType(apiuserPhone.getType());
					userPhoneLst.add(userPhone);
				}
			}
			xmlGetUserPhoneRes.getUserPhone().addAll(userPhoneLst);	
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlGetUserPhoneRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Add GET USER Phone XMl response :\n" + xmlGetUserPhoneRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the get Phone call.", e);
			return "error"; //TODO return proper error response
		}
	}
	@POST
	@Path("/delete")
	@Consumes("application/xml")
	@Produces("application/xml")	
	public String delete(String deletePhoneXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("Delete Phone XML request: \n" + deletePhoneXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			DeleteUserPhoneRequest xmlDeleteUserPhoneReq = (DeleteUserPhoneRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(deletePhoneXml)));
			com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneRequest apiDeleteUserPhoneReq = new com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneRequest();
			apiDeleteUserPhoneReq.setSessionToken(xmlDeleteUserPhoneReq.getSessionToken());
			apiDeleteUserPhoneReq.setUserId(xmlDeleteUserPhoneReq.getUserId());
			apiDeleteUserPhoneReq.setPhone(xmlDeleteUserPhoneReq.getPhone());
			
			com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneResponse apiDeleteUserPhoneRes = userAdminManager.deleteUserPhone(apiDeleteUserPhoneReq);
			
			DeleteUserPhoneResponse xmlDeleteUserPhoneRes = new DeleteUserPhoneResponse();
			xmlDeleteUserPhoneRes.setDeleteUserPhoneStatus(DeleteUserPhoneStatus.fromValue(apiDeleteUserPhoneRes.getDeleteUserPhoneStatus().name()));
			xmlDeleteUserPhoneRes.setSessionToken(apiDeleteUserPhoneRes.getSessionToken());
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlDeleteUserPhoneRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Add Delete USER Phone XMl response :\n" + xmlDeleteUserPhoneRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the get Phone call.", e);
			return "error"; //TODO return proper error response
		}
	}
	@POST
	@Path("/verify")
	@Consumes("application/xml")
	@Produces("application/xml")	
	public String verify(String verifyPhoneXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("Verify Phone XML request: \n" + verifyPhoneXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			VerifyUserPhoneRequest xmlVerifyUserPhoneReq = (VerifyUserPhoneRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(verifyPhoneXml)));
			com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneRequest apiVerifyUserPhoneReq = new com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneRequest();
			apiVerifyUserPhoneReq.setSessionToken(xmlVerifyUserPhoneReq.getSessionToken());
			apiVerifyUserPhoneReq.setUserId(xmlVerifyUserPhoneReq.getUserId());
			apiVerifyUserPhoneReq.setPhone(xmlVerifyUserPhoneReq.getPhone());
			apiVerifyUserPhoneReq.setVerificationCode(xmlVerifyUserPhoneReq.getVerificationCode());
			
			com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneResponse apiVerifyUserPhoneRes = userAdminManager.verifyUserPhone(apiVerifyUserPhoneReq);
			
			VerifyUserPhoneResponse xmlVerifyUserPhoneRes = new VerifyUserPhoneResponse();
			xmlVerifyUserPhoneRes.setVerifyUserPhoneStatus(VerifyUserPhoneStatus.fromValue(apiVerifyUserPhoneRes.getVerifyUserPhoneStatus().name()));
			xmlVerifyUserPhoneRes.setSessionToken(apiVerifyUserPhoneRes.getSessionToken());
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlVerifyUserPhoneRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Verify USER Phone XMl response :\n" + xmlVerifyUserPhoneRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the get Phone call.", e);
			return "error"; //TODO return proper error response
		}
	}
}
