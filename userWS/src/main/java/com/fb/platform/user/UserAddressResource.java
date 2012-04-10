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
import com.fb.platform.user.address._1_0.GetUserAddressRequest;
import com.fb.platform.user.address._1_0.GetUserAddressResponse;
import com.fb.platform.user.address._1_0.GetUserAddressStatus;
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
					userAddress.setAddresstype(apiUserAddress.getAddressType());
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

}
