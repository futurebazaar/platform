package com.fb.platform.user.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.manager.interfaces.UserAddressManager;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.address.AddAddressRequest;
import com.fb.platform.user.manager.model.address.AddAddressResponse;
import com.fb.platform.user.manager.model.address.AddAddressStatusEnum;
import com.fb.platform.user.manager.model.address.DeleteAddressRequest;
import com.fb.platform.user.manager.model.address.DeleteAddressResponse;
import com.fb.platform.user.manager.model.address.DeleteAddressStatusEnum;
import com.fb.platform.user.manager.model.address.GetAddressRequest;
import com.fb.platform.user.manager.model.address.GetAddressResponse;
import com.fb.platform.user.manager.model.address.GetAddressStatusEnum;
import com.fb.platform.user.manager.model.address.UpdateAddressRequest;
import com.fb.platform.user.manager.model.address.UpdateAddressResponse;
import com.fb.platform.user.manager.model.address.UpdateAddressStatusEnum;
import com.fb.platform.user.manager.model.address.UserAddress;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;
import com.fb.platform.user.manager.model.auth.LogoutRequest;
import com.fb.platform.user.manager.model.auth.LogoutResponse;

public class UserAddressManagerTest extends BaseTestCase {

	@Autowired
	private UserAddressManager userAddressManager = null;
	
	@Autowired
	private UserManager userManager = null;

	@Test
	public void testGetAddress() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		GetAddressRequest getAddressRequest = new GetAddressRequest();
		getAddressRequest.setSessionToken(response.getSessionToken());
		getAddressRequest.setUserId(response.getUserId());
		GetAddressResponse getAddressResponse = userAddressManager.getAddress(getAddressRequest);
		assertNotNull(getAddressResponse);
		assertNotNull(getAddressResponse.getSessionToken());
		assertNotNull(getAddressResponse.getUserAddress());
		assertEquals(GetAddressStatusEnum.SUCCESS,getAddressResponse.getGetAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testGetAddressNull() {
		GetAddressRequest getAddressRequest = null;
		GetAddressResponse getAddressResponse = userAddressManager.getAddress(getAddressRequest);
		assertNotNull(getAddressResponse);
		assertEquals(GetAddressStatusEnum.INVALID_USER,getAddressResponse.getGetAddressStatus());
	}
	@Test
	public void testGetAddressNoSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		GetAddressRequest getAddressRequest = new GetAddressRequest();
		getAddressRequest.setSessionToken("");
		getAddressRequest.setUserId(response.getUserId());
		GetAddressResponse getAddressResponse = userAddressManager.getAddress(getAddressRequest);
		assertNotNull(getAddressResponse);
		assertEquals(GetAddressStatusEnum.NO_SESSION,getAddressResponse.getGetAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testGetAddressWrongSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		GetAddressRequest getAddressRequest = new GetAddressRequest();
		getAddressRequest.setSessionToken(response.getSessionToken()+"asdas");
		getAddressRequest.setUserId(response.getUserId());
		GetAddressResponse getAddressResponse = userAddressManager.getAddress(getAddressRequest);
		assertNotNull(getAddressResponse);
		assertEquals(GetAddressStatusEnum.NO_SESSION,getAddressResponse.getGetAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testGetAddressWrongUserId() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		GetAddressRequest getAddressRequest = new GetAddressRequest();
		getAddressRequest.setSessionToken(response.getSessionToken());
		getAddressRequest.setUserId(0);
		GetAddressResponse getAddressResponse = userAddressManager.getAddress(getAddressRequest);
		assertNotNull(getAddressResponse);
		assertEquals(GetAddressStatusEnum.INVALID_USER,getAddressResponse.getGetAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testGetAddressNOAddress() {
		LoginRequest request = new LoginRequest();
		request.setUsername("testonlyusername");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		GetAddressRequest getAddressRequest = new GetAddressRequest();
		getAddressRequest.setSessionToken(response.getSessionToken());
		getAddressRequest.setUserId(response.getUserId());
		GetAddressResponse getAddressResponse = userAddressManager.getAddress(getAddressRequest);
		assertNotNull(getAddressResponse);
		assertEquals(GetAddressStatusEnum.NO_ADDRESSES,getAddressResponse.getGetAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testAddAddress() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new adding adderss impl");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testAddAddressNull() {
		AddAddressRequest addAddressRequest = null;
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		assertEquals(AddAddressStatusEnum.INVALID_USER,addAddressResponse.getAddAddressStatus());				
	}
	@Test
	public void testAddAddressNoSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken("");
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new adding adderss impl");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		assertEquals(AddAddressStatusEnum.NO_SESSION,addAddressResponse.getAddAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testAddAddressWrongSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken()+"asdas");
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new adding adderss impl");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		assertEquals(AddAddressStatusEnum.NO_SESSION,addAddressResponse.getAddAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testAddAddressWrongUserId() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(0);
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new adding adderss impl");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		assertEquals(AddAddressStatusEnum.INVALID_USER,addAddressResponse.getAddAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testAddAddressEmptyAddress() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		assertEquals(AddAddressStatusEnum.EMPTY_ADDRESS,addAddressResponse.getAddAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testUpdateAddress() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new adding adderss for update");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		
		UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
		updateAddressRequest.setSessionToken(response.getSessionToken());
		updateAddressRequest.setUserId(response.getUserId());
		UserAddress userAddressUpdate = new UserAddress();
		userAddressUpdate.setAddress("testing new adding after update");
		userAddressUpdate.setAddressType("Delivery");
		userAddressUpdate.setCity("Mumbai");
		userAddressUpdate.setState("Maharastra");
		userAddressUpdate.setCountry("India");
		userAddressUpdate.setPinCode("400001");
		userAddress.setName("Test123");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		userAddressUpdate.setAddressId(addressidAdd);
		updateAddressRequest.setUserAddress(userAddressUpdate);
		UpdateAddressResponse updateAddressResponse = userAddressManager.updateAddress(updateAddressRequest);
		assertNotNull(updateAddressResponse);
		assertNotNull(updateAddressResponse.getSessionToken());
		assertEquals(UpdateAddressStatusEnum.SUCCESS, updateAddressResponse.getUpdateAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testUpdateAddressNUll() {
		UpdateAddressRequest updateAddressRequest = null;
		UpdateAddressResponse updateAddressResponse = userAddressManager.updateAddress(updateAddressRequest);
		assertNotNull(updateAddressResponse);
		assertEquals(UpdateAddressStatusEnum.INVALID_USER, updateAddressResponse.getUpdateAddressStatus());		
	}
	@Test
	public void testUpdateAddressNoSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new3 test adding adderss for update");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		
		UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
		updateAddressRequest.setSessionToken("");
		updateAddressRequest.setUserId(response.getUserId());
		UserAddress userAddressUpdate = new UserAddress();
		userAddressUpdate.setAddress("testing new adding after update");
		userAddressUpdate.setAddressId(addressidAdd);
		updateAddressRequest.setUserAddress(userAddressUpdate);
		UpdateAddressResponse updateAddressResponse = userAddressManager.updateAddress(updateAddressRequest);
		assertNotNull(updateAddressResponse);
		assertEquals(UpdateAddressStatusEnum.NO_SESSION, updateAddressResponse.getUpdateAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testUpdateAddressInvalidUser() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new5 test adding adderss for update");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		
		UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
		updateAddressRequest.setSessionToken(addAddressResponse.getSessionToken());
		updateAddressRequest.setUserId(0);
		UserAddress userAddressUpdate = new UserAddress();
		userAddressUpdate.setAddress("testing new adding after update");
		userAddressUpdate.setAddressId(addressidAdd);
		updateAddressRequest.setUserAddress(userAddressUpdate);
		UpdateAddressResponse updateAddressResponse = userAddressManager.updateAddress(updateAddressRequest);
		assertNotNull(updateAddressResponse);
		assertEquals(UpdateAddressStatusEnum.INVALID_USER, updateAddressResponse.getUpdateAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testUpdateAddressNoAddress() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
		updateAddressRequest.setSessionToken(response.getSessionToken());
		updateAddressRequest.setUserId(response.getUserId());
		UpdateAddressResponse updateAddressResponse = userAddressManager.updateAddress(updateAddressRequest);
		assertNotNull(updateAddressResponse);
		assertEquals(UpdateAddressStatusEnum.ERROR_UPDATING_ADDRESS, updateAddressResponse.getUpdateAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testUpdateAddressNOAddressId() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
		updateAddressRequest.setSessionToken(response.getSessionToken());
		updateAddressRequest.setUserId(response.getUserId());
		UserAddress userAddressUpdate = new UserAddress();
		userAddressUpdate.setAddress("testing new adding after update");
		userAddressUpdate.setAddressId(0);
		updateAddressRequest.setUserAddress(userAddressUpdate);
		UpdateAddressResponse updateAddressResponse = userAddressManager.updateAddress(updateAddressRequest);
		assertNotNull(updateAddressResponse);
		assertEquals(UpdateAddressStatusEnum.ADDRESSID_ABSENT, updateAddressResponse.getUpdateAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testUpdateAddressWrongSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new1 adding adderss for update");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		
		UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
		updateAddressRequest.setSessionToken(addAddressResponse.getSessionToken()+"asdasda");
		updateAddressRequest.setUserId(response.getUserId());
		UserAddress userAddressUpdate = new UserAddress();
		userAddressUpdate.setAddress("testing new adding after update");
		userAddressUpdate.setAddressId(addressidAdd);
		updateAddressRequest.setUserAddress(userAddressUpdate);
		UpdateAddressResponse updateAddressResponse = userAddressManager.updateAddress(updateAddressRequest);
		assertNotNull(updateAddressResponse);
		assertEquals(UpdateAddressStatusEnum.NO_SESSION, updateAddressResponse.getUpdateAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testUpdateAddressUserAddressMismatch() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new2 adding adderss for update");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		
		UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
		updateAddressRequest.setSessionToken(addAddressResponse.getSessionToken());
		updateAddressRequest.setUserId(-5);
		UserAddress userAddressUpdate = new UserAddress();
		userAddressUpdate.setAddress("testing new adding after update");
		userAddressUpdate.setAddressId(addressidAdd);
		updateAddressRequest.setUserAddress(userAddressUpdate);
		UpdateAddressResponse updateAddressResponse = userAddressManager.updateAddress(updateAddressRequest);
		assertNotNull(updateAddressResponse);
		assertEquals(UpdateAddressStatusEnum.USER_ADDRESSID_MISMATCH, updateAddressResponse.getUpdateAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testDeleteAddress() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new adding adderss for delete");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		DeleteAddressRequest deleteAddressRequest = new DeleteAddressRequest();
		deleteAddressRequest.setAddressId(addressidAdd);
		deleteAddressRequest.setSessionToken(addAddressResponse.getSessionToken());
		deleteAddressRequest.setUserId(response.getUserId());
		DeleteAddressResponse deleteAddressResponse = userAddressManager.deleteAddress(deleteAddressRequest);
		assertNotNull(deleteAddressResponse);
		assertNotNull(deleteAddressResponse.getSessionToken());
		assertEquals(DeleteAddressStatusEnum.SUCCESS,deleteAddressResponse.getDeleteAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testDeleteAddressNull() {
		DeleteAddressRequest deleteAddressRequest = null;
		DeleteAddressResponse deleteAddressResponse = userAddressManager.deleteAddress(deleteAddressRequest);
		assertNotNull(deleteAddressResponse);
		assertEquals(DeleteAddressStatusEnum.INVALID_USER,deleteAddressResponse.getDeleteAddressStatus());
				
	}
	@Test
	public void testDeleteAddressBlankSession() {
		DeleteAddressRequest deleteAddressRequest = new DeleteAddressRequest();
		deleteAddressRequest.setAddressId(3);
		deleteAddressRequest.setSessionToken("");
		deleteAddressRequest.setUserId(1);
		DeleteAddressResponse deleteAddressResponse = userAddressManager.deleteAddress(deleteAddressRequest);
		assertNotNull(deleteAddressResponse);
		assertEquals(DeleteAddressStatusEnum.NO_SESSION,deleteAddressResponse.getDeleteAddressStatus());
				
	}
	@Test
	public void testDeleteAddressNoAddressId() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		DeleteAddressRequest deleteAddressRequest = new DeleteAddressRequest();
		deleteAddressRequest.setAddressId(0);
		deleteAddressRequest.setSessionToken(response.getSessionToken());
		deleteAddressRequest.setUserId(response.getUserId());
		DeleteAddressResponse deleteAddressResponse = userAddressManager.deleteAddress(deleteAddressRequest);
		assertNotNull(deleteAddressResponse);
		assertEquals(DeleteAddressStatusEnum.ADDRESSID_ABSENT,deleteAddressResponse.getDeleteAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testDeleteAddressWrongSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new adding adderss for delete");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		DeleteAddressRequest deleteAddressRequest = new DeleteAddressRequest();
		deleteAddressRequest.setAddressId(addressidAdd);
		deleteAddressRequest.setSessionToken(addAddressResponse.getSessionToken()+"asdasd");
		deleteAddressRequest.setUserId(response.getUserId());
		DeleteAddressResponse deleteAddressResponse = userAddressManager.deleteAddress(deleteAddressRequest);
		assertNotNull(deleteAddressResponse);
		assertEquals(DeleteAddressStatusEnum.NO_SESSION,deleteAddressResponse.getDeleteAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testDeleteAddressNoUser() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new adding adderss for delete");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		DeleteAddressRequest deleteAddressRequest = new DeleteAddressRequest();
		deleteAddressRequest.setAddressId(addressidAdd);
		deleteAddressRequest.setSessionToken(addAddressResponse.getSessionToken());
		deleteAddressRequest.setUserId(0);
		DeleteAddressResponse deleteAddressResponse = userAddressManager.deleteAddress(deleteAddressRequest);
		assertNotNull(deleteAddressResponse);
		assertEquals(DeleteAddressStatusEnum.INVALID_USER,deleteAddressResponse.getDeleteAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testDeleteAddressAddressUserMismatch() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new adding adderss for delete");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		DeleteAddressRequest deleteAddressRequest = new DeleteAddressRequest();
		deleteAddressRequest.setAddressId(addressidAdd);
		deleteAddressRequest.setSessionToken(addAddressResponse.getSessionToken());
		deleteAddressRequest.setUserId(-5);
		DeleteAddressResponse deleteAddressResponse = userAddressManager.deleteAddress(deleteAddressRequest);
		assertNotNull(deleteAddressResponse);
		assertEquals(DeleteAddressStatusEnum.USER_ADDRESSID_MISMATCH,deleteAddressResponse.getDeleteAddressStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
	@Test
	public void testDeleteAddressRepete() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		AddAddressRequest addAddressRequest = new AddAddressRequest();
		addAddressRequest.setSessionToken(response.getSessionToken());
		addAddressRequest.setUserId(response.getUserId());
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress("testing new1234 adding adderss for delete");
		userAddress.setAddressType("Delivery");
		userAddress.setCity("Mumbai");
		userAddress.setState("Maharastra");
		userAddress.setCountry("India");
		userAddress.setPinCode("400001");
		userAddress.setName("Test");
		userAddress.setFirstName("TestF");
		userAddress.setLastName("TestL");
		userAddress.setPhone("98987777676");
		userAddress.setEmail("asd@asd.com");
		addAddressRequest.setUserAddress(userAddress);
		AddAddressResponse addAddressResponse = userAddressManager.addAddress(addAddressRequest);
		assertNotNull(addAddressResponse);
		long addressidAdd = addAddressResponse.getAddressId();
		assertEquals(AddAddressStatusEnum.SUCCESS,addAddressResponse.getAddAddressStatus());
		
		DeleteAddressRequest deleteAddressRequest = new DeleteAddressRequest();
		deleteAddressRequest.setAddressId(addressidAdd);
		deleteAddressRequest.setSessionToken(addAddressResponse.getSessionToken());
		deleteAddressRequest.setUserId(response.getUserId());
		DeleteAddressResponse deleteAddressResponse = userAddressManager.deleteAddress(deleteAddressRequest);
		assertNotNull(deleteAddressResponse);
		assertEquals(DeleteAddressStatusEnum.SUCCESS,deleteAddressResponse.getDeleteAddressStatus());
		
		DeleteAddressRequest deleteAddressRequestSame = new DeleteAddressRequest();
		deleteAddressRequestSame.setAddressId(addressidAdd);
		deleteAddressRequestSame.setSessionToken(addAddressResponse.getSessionToken());
		deleteAddressRequestSame.setUserId(response.getUserId());
		DeleteAddressResponse deleteAddressResponseSame = userAddressManager.deleteAddress(deleteAddressRequest);
		assertNotNull(deleteAddressResponseSame);
		assertEquals(DeleteAddressStatusEnum.ADDRESSID_ABSENT,deleteAddressResponseSame.getDeleteAddressStatus());
		
		
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
				
	}
}
