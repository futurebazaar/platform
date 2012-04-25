package com.fb.platform.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
import com.fb.platform.user._1_0.UpdateUserStatus;
import com.fb.platform.user.address._1_0.GetUserAddressRequest;
import com.fb.platform.user.address._1_0.GetUserAddressResponse;
import com.fb.platform.user.address._1_0.GetUserAddressStatus;
import com.fb.platform.user.address._1_0.AddUserAddressRequest;
import com.fb.platform.user.address._1_0.AddUserAddressResponse;
import com.fb.platform.user.address._1_0.AddUserAddressStatus;
import com.fb.platform.user.address._1_0.UpdateUserAddressRequest;
import com.fb.platform.user.address._1_0.UpdateUserAddressResponse;
import com.fb.platform.user.address._1_0.UpdateUserAddressStatus;
import com.fb.platform.user.address._1_0.DeleteUserAddressRequest;
import com.fb.platform.user.address._1_0.DeleteUserAddressResponse;
import com.fb.platform.user.address._1_0.DeleteUserAddressStatus;

import com.fb.platform.user.address._1_0.UserAddresses;
import com.fb.platform.user.manager.interfaces.UserAddressManager;

/**
 * @author kislaya
 *
 */
@Path("/user/address")
@Component
@Scope("request")
public class UserAddressResource {
	
	private static final Log logger = LogFactory.getLog(UserAddressResource.class);
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();
	
	@Autowired
	private UserAddressManager userAddressManager;
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.user.address._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	@POST
	@Path("/get")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String get(String getAddressXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("Get Address XML request: \n" + getAddressXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GetUserAddressRequest xmlGetUserAddressReq = (GetUserAddressRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(getAddressXml)));
			com.fb.platform.user.manager.model.address.GetAddressRequest apiGetUserAddressReq = new com.fb.platform.user.manager.model.address.GetAddressRequest();
			apiGetUserAddressReq.setSessionToken(xmlGetUserAddressReq.getSessionToken());
			apiGetUserAddressReq.setUserId(xmlGetUserAddressReq.getUserId());
			
			com.fb.platform.user.manager.model.address.GetAddressResponse apiGetUserAddressRes = userAddressManager.getAddress(apiGetUserAddressReq);
			
			GetUserAddressResponse xmlGetUserAddressRes = new GetUserAddressResponse();
			xmlGetUserAddressRes.setGetUserAddressStatus(GetUserAddressStatus.fromValue(apiGetUserAddressRes.getGetAddressStatus().name()));
			xmlGetUserAddressRes.setSessionToken(apiGetUserAddressRes.getSessionToken());
					
			List<UserAddresses> userAddresslLst = new ArrayList<UserAddresses>();
			if(apiGetUserAddressRes.getUserAddress() != null){
				for (com.fb.platform.user.manager.model.address.UserAddress apiUserAddress : apiGetUserAddressRes.getUserAddress()){
					UserAddresses userAddress = new UserAddresses();
					userAddress.setAddressId(apiUserAddress.getAddressId());
					userAddress.setAddress(apiUserAddress.getAddress());
					userAddress.setAddressType(apiUserAddress.getAddressType());
					userAddress.setCity(apiUserAddress.getCity());
					userAddress.setState(apiUserAddress.getState());
					userAddress.setCountry(apiUserAddress.getCountry());
					userAddress.setPincode(apiUserAddress.getPinCode());
					userAddresslLst.add(userAddress);
				}
			}
			xmlGetUserAddressRes.getUserAddresses().addAll(userAddresslLst);	
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlGetUserAddressRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Add GET USER Addresss XMl response :\n" + xmlGetUserAddressRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the get address call.", e);
			return "error"; //TODO return proper error response
		}
	}
	@POST
	@Path("/add")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String add(String addAddressXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("Add Address XML request: \n" + addAddressXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			AddUserAddressRequest xmlAddUserAddressReq = (AddUserAddressRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(addAddressXml)));
			com.fb.platform.user.manager.model.address.AddAddressRequest apiAddUserAddressReq = new com.fb.platform.user.manager.model.address.AddAddressRequest();
			apiAddUserAddressReq.setSessionToken(xmlAddUserAddressReq.getSessionToken());
			apiAddUserAddressReq.setUserId(xmlAddUserAddressReq.getUserId());
			
			com.fb.platform.user.manager.model.address.UserAddress apiUserAddress = new com.fb.platform.user.manager.model.address.UserAddress();
			apiUserAddress.setAddress(xmlAddUserAddressReq.getUserAddresses().getAddress());
			apiUserAddress.setCity(xmlAddUserAddressReq.getUserAddresses().getCity());
			apiUserAddress.setCountry(xmlAddUserAddressReq.getUserAddresses().getCountry());
			apiUserAddress.setState(xmlAddUserAddressReq.getUserAddresses().getState());
			apiUserAddress.setPinCode(xmlAddUserAddressReq.getUserAddresses().getPincode());
			apiUserAddress.setAddressType(xmlAddUserAddressReq.getUserAddresses().getAddressType());
			apiAddUserAddressReq.setUserAddress(apiUserAddress);
			
			com.fb.platform.user.manager.model.address.AddAddressResponse apiAddUserAddressRes = userAddressManager.addAddress(apiAddUserAddressReq);
			
			AddUserAddressResponse xmlAddUserAddressRes = new AddUserAddressResponse();
			xmlAddUserAddressRes.setAddUserAddressStatus(AddUserAddressStatus.fromValue(apiAddUserAddressRes.getAddAddressStatus().name()));
			xmlAddUserAddressRes.setSessionToken(apiAddUserAddressRes.getSessionToken());
			xmlAddUserAddressRes.setAddressId(apiAddUserAddressRes.getAddressId());		
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlAddUserAddressRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Add ADD USER Addresss XMl response :\n" + xmlAddUserAddressRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the add address call.", e);
			return "error"; //TODO return proper error response
		}
	}
	@POST
	@Path("/update")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String update(String updateAddressXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("Update Address XML request: \n" + updateAddressXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			UpdateUserAddressRequest xmlUpdateUserAddressReq = (UpdateUserAddressRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(updateAddressXml)));
			com.fb.platform.user.manager.model.address.UpdateAddressRequest apiUpdateUserAddressReq = new com.fb.platform.user.manager.model.address.UpdateAddressRequest();
			apiUpdateUserAddressReq.setSessionToken(xmlUpdateUserAddressReq.getSessionToken());
			apiUpdateUserAddressReq.setUserId(xmlUpdateUserAddressReq.getUserId());
			
			com.fb.platform.user.manager.model.address.UserAddress apiUserAddress = new com.fb.platform.user.manager.model.address.UserAddress();
			apiUserAddress.setAddressId(xmlUpdateUserAddressReq.getUserAddresses().getAddressId());
			apiUserAddress.setAddress(xmlUpdateUserAddressReq.getUserAddresses().getAddress());
			apiUserAddress.setCity(xmlUpdateUserAddressReq.getUserAddresses().getCity());
			apiUserAddress.setCountry(xmlUpdateUserAddressReq.getUserAddresses().getCountry());
			apiUserAddress.setState(xmlUpdateUserAddressReq.getUserAddresses().getState());
			apiUserAddress.setPinCode(xmlUpdateUserAddressReq.getUserAddresses().getPincode());
			apiUserAddress.setAddressType(xmlUpdateUserAddressReq.getUserAddresses().getAddressType());
			apiUpdateUserAddressReq.setUserAddress(apiUserAddress);
			
			com.fb.platform.user.manager.model.address.UpdateAddressResponse apiUpdateUserAddressRes = userAddressManager.updateAddress(apiUpdateUserAddressReq);
			
			UpdateUserAddressResponse xmlUpdateUserAddressRes = new UpdateUserAddressResponse();
			xmlUpdateUserAddressRes.setUpdateUserAddressStatus(UpdateUserAddressStatus.fromValue(apiUpdateUserAddressRes.getUpdateAddressStatus().name()));
			xmlUpdateUserAddressRes.setSessionToken(apiUpdateUserAddressRes.getSessionToken());
					
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlUpdateUserAddressRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Update USER Addresss XMl response :\n" + xmlUpdateUserAddressRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the add address call.", e);
			return "error"; //TODO return proper error response
		}
	}
	@POST
	@Path("/delete")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String delete(String deleteAddressXml) {
		if (logger.isDebugEnabled()) {
			logger.debug("Delete Address XML request: \n" + deleteAddressXml);
		}
		try{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			DeleteUserAddressRequest xmlDeleteUserAddressReq = (DeleteUserAddressRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(deleteAddressXml)));
			com.fb.platform.user.manager.model.address.DeleteAddressRequest apiDeleteUserAddressReq = new com.fb.platform.user.manager.model.address.DeleteAddressRequest();
			apiDeleteUserAddressReq.setSessionToken(xmlDeleteUserAddressReq.getSessionToken());
			apiDeleteUserAddressReq.setUserId(xmlDeleteUserAddressReq.getUserId());
			apiDeleteUserAddressReq.setAddressId(xmlDeleteUserAddressReq.getAddressId());
			
			com.fb.platform.user.manager.model.address.DeleteAddressResponse apiDeleteUserAddressRes = userAddressManager.deleteAddress(apiDeleteUserAddressReq);
			
			DeleteUserAddressResponse xmlDeleteUserAddressRes = new DeleteUserAddressResponse();
			xmlDeleteUserAddressRes.setDeleteUserAddressStatus(DeleteUserAddressStatus.fromValue(apiDeleteUserAddressRes.getDeleteAddressStatus().name()));
			xmlDeleteUserAddressRes.setSessionToken(apiDeleteUserAddressRes.getSessionToken());
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlDeleteUserAddressRes, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Delete USER Addresss XMl response :\n" + xmlDeleteUserAddressRes);
			}
			return xmlResponse;
			
			
		}catch (JAXBException e) {
			logger.error("Error in the delete address call.", e);
			return "error"; //TODO return proper error response
		}
	}
	@GET
	@Path("/xsd")
	@Produces("application/xml")
	public String getXsd() {	
		InputStream addressXsd = this.getClass().getClassLoader().getResourceAsStream("useraddress.xsd");
		String addressXsdString = convertInputStreamToString(addressXsd);
		return addressXsdString;
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
			logger.error("address.xsd loading error : " + exception.getMessage() );
		}
		return sb.toString();
	}
}
