package com.fb.platform.user.manager;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.manager.interfaces.UserAdminManager;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.admin.AddUserRequest;
import com.fb.platform.user.manager.model.admin.AddUserResponse;
import com.fb.platform.user.manager.model.admin.AddUserStatusEnum;
import com.fb.platform.user.manager.model.admin.GetUserRequest;
import com.fb.platform.user.manager.model.admin.GetUserResponse;
import com.fb.platform.user.manager.model.admin.GetUserStatusEnum;
import com.fb.platform.user.manager.model.admin.IsValidUserEnum;
import com.fb.platform.user.manager.model.admin.IsValidUserRequest;
import com.fb.platform.user.manager.model.admin.IsValidUserResponse;
import com.fb.platform.user.manager.model.admin.UpdateUserRequest;
import com.fb.platform.user.manager.model.admin.UpdateUserResponse;
import com.fb.platform.user.manager.model.admin.UpdateUserStatusEnum;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.UserEmail;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.UserPhone;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;
import com.fb.platform.user.manager.model.auth.LoginStatusEnum;
import com.fb.platform.user.manager.model.auth.LogoutRequest;
import com.fb.platform.user.manager.model.auth.LogoutResponse;

public class UserAdminManagerTest extends BaseTestCase {

	private static final String key  = "jasvipul@gmail.com";

	@Autowired
	private UserAdminManager userAdminManager;
	
	@Autowired
	private UserManager userManager = null;

	@Test
	public void testUserManager() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
	    GetUserResponse record = new GetUserResponse();
	    GetUserRequest putreq = new GetUserRequest();
	    putreq.setKey(key);
	    putreq.setSessionToken(response.getSessionToken());
	    record = userAdminManager.getUser(putreq);
	    assertNotNull(record);
	    assertNotNull(record.getUserName());
	    assertNotNull(record.getSessionToken());
	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
    }
	@Test
	public void testGetUserNull() {
	    GetUserResponse record = new GetUserResponse();
	    GetUserRequest putreq =null;
	    record = userAdminManager.getUser(putreq);
	    assertNotNull(record);
	    assertEquals(GetUserStatusEnum.NO_USER_KEY,record.getStatus());
	}
	@Test
	public void testGetUserNoSession() {
	    GetUserResponse record = new GetUserResponse();
	    GetUserRequest putreq = new GetUserRequest();
	    putreq.setKey("anykey");
	    putreq.setSessionToken("");
	    record = userAdminManager.getUser(putreq);
	    assertNotNull(record);
	    assertEquals(GetUserStatusEnum.NO_SESSION,record.getStatus());
	}
	@Test
	public void testGetUserWrongSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
	    GetUserResponse record = new GetUserResponse();
	    GetUserRequest putreq = new GetUserRequest();
	    putreq.setKey(key);
	    putreq.setSessionToken(response.getSessionToken()+"asdasd");
	    record = userAdminManager.getUser(putreq);
	    assertNotNull(record);
	    assertEquals(GetUserStatusEnum.NO_SESSION,record.getStatus());
	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
    }
	@Test
	public void testGetUserInvalidUser() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
	    GetUserResponse record = new GetUserResponse();
	    GetUserRequest putreq = new GetUserRequest();
	    putreq.setKey("failkey");
	    putreq.setSessionToken(response.getSessionToken());
	    record = userAdminManager.getUser(putreq);
	    assertNotNull(record);
	    assertEquals(GetUserStatusEnum.INVALID_USER,record.getStatus());
	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
    }
	
	@Test
	public void testAddUser() {
		AddUserRequest addUserRequest = new AddUserRequest();
		addUserRequest.setUserName("testingemail123@test.com");
		addUserRequest.setPassword("testpass");
		AddUserResponse addUserResponse = userAdminManager.addUser(addUserRequest);
		assertNotNull(addUserResponse);
		assertNotNull(addUserResponse.getSessionToken());
		assertEquals(AddUserStatusEnum.SUCCESS,addUserResponse.getStatus());
	}
	@Test
	public void testPhoneAddUser() {
		AddUserRequest addUserRequest = new AddUserRequest();
		addUserRequest.setUserName("9876598765");
		addUserRequest.setPassword("testpass");
		AddUserResponse addUserResponse = userAdminManager.addUser(addUserRequest);
		assertNotNull(addUserResponse);
		assertNotNull(addUserResponse.getSessionToken());
		assertEquals(AddUserStatusEnum.SUCCESS,addUserResponse.getStatus());
	}
	@Test
	public void testAddUserNull() {
		AddUserRequest addUserRequest = null;
		AddUserResponse addUserResponse = userAdminManager.addUser(addUserRequest);
		assertNotNull(addUserResponse);
		assertEquals(AddUserStatusEnum.NO_USER_PROVIDED,addUserResponse.getStatus());
	}
	@Test
	public void testAddUserNullUserName() {
		AddUserRequest addUserRequest = new AddUserRequest();
		addUserRequest.setUserName("");
		AddUserResponse addUserResponse = userAdminManager.addUser(addUserRequest);
		assertNotNull(addUserResponse);
		assertEquals(AddUserStatusEnum.NO_USER_PROVIDED,addUserResponse.getStatus());
	}
	@Test
	public void testAddUserNoPhoneNoEmail() {
		AddUserRequest addUserRequest = new AddUserRequest();
		addUserRequest.setUserName("asdfdasf");
		addUserRequest.setPassword("asdfdasf123");
		AddUserResponse addUserResponse = userAdminManager.addUser(addUserRequest);
		assertNotNull(addUserResponse);
		assertEquals(AddUserStatusEnum.INVALID_USER_NAME,addUserResponse.getStatus());
		assertEquals(0, addUserResponse.getUserId().longValue());
	}
	@Test
	public void testAddUserDuplicate() {
		AddUserRequest addUserRequest = new AddUserRequest();
		addUserRequest.setUserName("jasvipul@gmail.com");
		addUserRequest.setPassword("testpass");
		AddUserResponse addUserResponse = userAdminManager.addUser(addUserRequest);
		assertNotNull(addUserResponse);
		assertEquals(AddUserStatusEnum.USER_ALREADY_EXISTS,addUserResponse.getStatus());
	}
	@Test
	public void testAddUserError() {
		AddUserRequest addUserRequest = new AddUserRequest();
		addUserRequest.setPassword("testpass");
		AddUserResponse addUserResponse = userAdminManager.addUser(addUserRequest);
		assertNotNull(addUserResponse);
		assertEquals(AddUserStatusEnum.NO_USER_PROVIDED,addUserResponse.getStatus());
	}
	@Test
	public void testUpdateUser() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		UpdateUserRequest updateUserRequest = new  UpdateUserRequest();
		updateUserRequest.setSessionToken(response.getSessionToken());
		updateUserRequest.setGender("M");
		updateUserRequest.setFirstName("Newname");
		updateUserRequest.setLastName("test");
		updateUserRequest.setDateOfBirth(new Date());
		updateUserRequest.setSalutation("Mr.");
		
		UpdateUserResponse updateUserResponse = userAdminManager.updateUser(updateUserRequest);
		assertNotNull(updateUserResponse);
		assertNotNull(updateUserResponse.getSessionToken());
		assertEquals(UpdateUserStatusEnum.SUCCESS, updateUserResponse.getStatus());

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testUpdateUserMoreParameter() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		UpdateUserRequest updateUserRequest = new  UpdateUserRequest();
		updateUserRequest.setSessionToken(response.getSessionToken());
		updateUserRequest.setPrimaryEmail("123566@test.com");
		updateUserRequest.setSecondaryEmail("76889@test.com");
		updateUserRequest.setPrimaryPhone("98776543210");
		updateUserRequest.setSecondaryPhone("98776456320");
		
		UpdateUserResponse updateUserResponse = userAdminManager.updateUser(updateUserRequest);
		assertNotNull(updateUserResponse);
		assertNotNull(updateUserResponse.getSessionToken());
		assertEquals(UpdateUserStatusEnum.SUCCESS, updateUserResponse.getStatus());

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testUpdateUserNull() {
		UpdateUserRequest updateUserRequest = null;
		UpdateUserResponse updateUserResponse = userAdminManager.updateUser(updateUserRequest);
		assertNotNull(updateUserResponse);
		assertEquals(UpdateUserStatusEnum.NO_USER_PROVIDED, updateUserResponse.getStatus());
	}
	@Test
	public void testUpdateUserWrongSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		UpdateUserRequest updateUserRequest = new  UpdateUserRequest();
		updateUserRequest.setSessionToken(response.getSessionToken()+"asdasd");
		updateUserRequest.setGender("M");
		updateUserRequest.setFirstName("Newname");
		updateUserRequest.setLastName("test");		
		UpdateUserResponse updateUserResponse = userAdminManager.updateUser(updateUserRequest);
		assertNotNull(updateUserResponse);
		assertEquals(UpdateUserStatusEnum.NO_SESSION, updateUserResponse.getStatus());

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testUpdateUserFailNoSession() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		UpdateUserRequest updateUserRequest = new  UpdateUserRequest();
		updateUserRequest.setSessionToken("");
		updateUserRequest.setGender("Male");
		updateUserRequest.setFirstName("Newname");
		updateUserRequest.setLastName("test");
		
		UpdateUserResponse updateUserResponse = userAdminManager.updateUser(updateUserRequest);
		assertNotNull(updateUserResponse);
		assertNull(updateUserResponse.getSessionToken());
		assertEquals(UpdateUserStatusEnum.NO_SESSION, updateUserResponse.getStatus());

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}

	@Test
	public void testIsValidUser(){
		IsValidUserRequest isValidUserRequest = new IsValidUserRequest();
		isValidUserRequest.setUserName("jasvipul@gmail.com");
		IsValidUserResponse isValidUserResponse = userAdminManager.isValidUser(isValidUserRequest);
		assertNotNull(isValidUserResponse);
		assertNotNull(isValidUserResponse.getUserId());
		assertEquals(IsValidUserEnum.VALID_USER, isValidUserResponse.getIsValidUserStatus());
		
	}
	@Test
	public void testIsValidUserNull(){
		IsValidUserRequest isValidUserRequest = null;
		IsValidUserResponse isValidUserResponse = userAdminManager.isValidUser(isValidUserRequest);
		assertNotNull(isValidUserResponse);
		assertEquals(IsValidUserEnum.INVALID_USER, isValidUserResponse.getIsValidUserStatus());		
	}
	@Test
	public void testNotIsValidUser(){
		IsValidUserRequest isValidUserRequest = new IsValidUserRequest();
		isValidUserRequest.setUserName("jasvipul12345@gmail.com");
		IsValidUserResponse isValidUserResponse = userAdminManager.isValidUser(isValidUserRequest);
		assertNotNull(isValidUserResponse);
		assertNotNull(isValidUserResponse.getUserId());
		assertEquals(IsValidUserEnum.INVALID_USER, isValidUserResponse.getIsValidUserStatus());
		
	}
	@Test
	public void testGetUserEmail(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		GetUserEmailRequest getUserEmailRequest = new GetUserEmailRequest();
		getUserEmailRequest.setSessionToken(response.getSessionToken());
		getUserEmailRequest.setUserId(response.getUserId());
		GetUserEmailResponse getUserEmailResponse = userAdminManager.getUserEmail(getUserEmailRequest);
		assertNotNull(getUserEmailResponse);
		assertNotNull(getUserEmailResponse.getUserId());
		assertNotNull(getUserEmailResponse.getUserEmail());
		assertNotNull(getUserEmailResponse.getSessionToken());
		assertEquals(GetUserEmailStatusEnum.SUCCESS, getUserEmailResponse.getGetUserEmailStatus());

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
		
	}
	@Test
	public void testGetUserEmailNoMails() {
		LoginRequest request = new LoginRequest();
		request.setUsername("-5");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		assertEquals(-5, response.getUserId().intValue());
		
		GetUserEmailRequest getUserEmailRequest = new GetUserEmailRequest();
		getUserEmailRequest.setSessionToken(response.getSessionToken());
		getUserEmailRequest.setUserId(response.getUserId());
		GetUserEmailResponse getUserEmailResponse = userAdminManager.getUserEmail(getUserEmailRequest);
		assertNotNull(getUserEmailResponse);
		assertNotNull(getUserEmailResponse.getUserId());
		assertEquals(GetUserEmailStatusEnum.NO_EMAIL_ID, getUserEmailResponse.getGetUserEmailStatus());
		LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
		
	}
	@Test
	public void testGetUserEmailFailNoSession(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		GetUserEmailRequest getUserEmailRequest = new GetUserEmailRequest();
		getUserEmailRequest.setSessionToken("");
		getUserEmailRequest.setUserId(response.getUserId());
		GetUserEmailResponse getUserEmailResponse = userAdminManager.getUserEmail(getUserEmailRequest);
		assertNotNull(getUserEmailResponse);
		assertNull(getUserEmailResponse.getUserEmail());
		assertEquals(GetUserEmailStatusEnum.NO_SESSION, getUserEmailResponse.getGetUserEmailStatus());	

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testGetUserEmailFailInvalidSession(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		GetUserEmailRequest getUserEmailRequest = new GetUserEmailRequest();
		getUserEmailRequest.setSessionToken(response.getSessionToken()+"asdad");
		getUserEmailRequest.setUserId(response.getUserId());
		GetUserEmailResponse getUserEmailResponse = userAdminManager.getUserEmail(getUserEmailRequest);
		assertNotNull(getUserEmailResponse);
		assertNull(getUserEmailResponse.getUserEmail());
		assertEquals(GetUserEmailStatusEnum.NO_SESSION, getUserEmailResponse.getGetUserEmailStatus());	

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testGetUserEmailInvalid(){
			
		GetUserEmailRequest getUserEmailRequest = null;
		GetUserEmailResponse getUserEmailResponse = userAdminManager.getUserEmail(getUserEmailRequest);
		assertNotNull(getUserEmailResponse);
		assertNull(getUserEmailResponse.getUserEmail());
		assertEquals(GetUserEmailStatusEnum.INVALID_USER, getUserEmailResponse.getGetUserEmailStatus());		
	}
	@Test
	public void testAddUserEmail(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserEmail userEmail = new UserEmail();
		userEmail.setEmail("testing@new.email");
		userEmail.setType("primary");
		
		AddUserEmailRequest addUserEmailRequest = new AddUserEmailRequest();
		addUserEmailRequest.setSessionToken(response.getSessionToken());
		addUserEmailRequest.setUserId(response.getUserId());
		addUserEmailRequest.setUserEmail(userEmail);
		AddUserEmailResponse addUserEmailResponse = userAdminManager.addUserEmail(addUserEmailRequest);
		assertNotNull(addUserEmailResponse);
		assertNotNull(addUserEmailResponse.getSessionToken());
		assertEquals(AddUserEmailStatusEnum.SUCCESS, addUserEmailResponse.getAddUserEmailStatus());		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testAddUserEmailInvalid(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserEmail userEmail = new UserEmail();
		userEmail.setEmail("test@@ing@new.email");
		userEmail.setType("primary");
		
		AddUserEmailRequest addUserEmailRequest = new AddUserEmailRequest();
		addUserEmailRequest.setSessionToken(response.getSessionToken());
		addUserEmailRequest.setUserId(response.getUserId());
		addUserEmailRequest.setUserEmail(userEmail);
		AddUserEmailResponse addUserEmailResponse = userAdminManager.addUserEmail(addUserEmailRequest);
		assertNotNull(addUserEmailResponse);
		assertEquals(AddUserEmailStatusEnum.INVALID_EMAIL, addUserEmailResponse.getAddUserEmailStatus());		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testAddUserEmailNull(){
		
		AddUserEmailRequest addUserEmailRequest = null;
		AddUserEmailResponse addUserEmailResponse = userAdminManager.addUserEmail(addUserEmailRequest);
		assertNotNull(addUserEmailResponse);
		assertEquals(AddUserEmailStatusEnum.INVALID_EMAIL, addUserEmailResponse.getAddUserEmailStatus());		
	}
	@Test
	public void testAddUserEmailNOSession(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserEmail userEmail = new UserEmail();
		userEmail.setEmail("testing@new.email");
		userEmail.setType("primary");
		
		AddUserEmailRequest addUserEmailRequest = new AddUserEmailRequest();
		addUserEmailRequest.setSessionToken("");
		addUserEmailRequest.setUserId(response.getUserId());
		addUserEmailRequest.setUserEmail(userEmail);
		AddUserEmailResponse addUserEmailResponse = userAdminManager.addUserEmail(addUserEmailRequest);
		assertNotNull(addUserEmailResponse);
		assertEquals(AddUserEmailStatusEnum.NO_SESSION, addUserEmailResponse.getAddUserEmailStatus());		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testAddUserEmailInvalidSession(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserEmail userEmail = new UserEmail();
		userEmail.setEmail("testing@new.email");
		userEmail.setType("primary");
		
		AddUserEmailRequest addUserEmailRequest = new AddUserEmailRequest();
		addUserEmailRequest.setSessionToken("asdasda");
		addUserEmailRequest.setUserId(response.getUserId());
		addUserEmailRequest.setUserEmail(userEmail);
		AddUserEmailResponse addUserEmailResponse = userAdminManager.addUserEmail(addUserEmailRequest);
		assertNotNull(addUserEmailResponse);
		assertEquals(AddUserEmailStatusEnum.NO_SESSION, addUserEmailResponse.getAddUserEmailStatus());		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testAddUserEmailAlready(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserEmail userEmail = new UserEmail();
		userEmail.setEmail("jasvipul@gmail.com");
		userEmail.setType("primary");
		
		AddUserEmailRequest addUserEmailRequest = new AddUserEmailRequest();
		addUserEmailRequest.setSessionToken(response.getSessionToken());
		addUserEmailRequest.setUserId(response.getUserId());
		addUserEmailRequest.setUserEmail(userEmail);
		AddUserEmailResponse addUserEmailResponse = userAdminManager.addUserEmail(addUserEmailRequest);
		assertNotNull(addUserEmailResponse);
		assertNotNull(addUserEmailResponse.getSessionToken());
		assertEquals(AddUserEmailStatusEnum.ALREADY_PRESENT, addUserEmailResponse.getAddUserEmailStatus());		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testDeleteUserEmail(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserEmail userEmail = new UserEmail();
		userEmail.setEmail("deletetest@new.email");
		userEmail.setType("primary");
		
		AddUserEmailRequest addUserEmailRequest = new AddUserEmailRequest();
		addUserEmailRequest.setSessionToken(response.getSessionToken());
		addUserEmailRequest.setUserId(response.getUserId());
		addUserEmailRequest.setUserEmail(userEmail);
		AddUserEmailResponse addUserEmailResponse = userAdminManager.addUserEmail(addUserEmailRequest);
		assertNotNull(addUserEmailResponse);
		assertNotNull(addUserEmailResponse.getSessionToken());
		assertEquals(AddUserEmailStatusEnum.SUCCESS, addUserEmailResponse.getAddUserEmailStatus());		
		
		DeleteUserEmailRequest deleteUserEmailRequest = new DeleteUserEmailRequest();
		deleteUserEmailRequest.setEmailId("deletetest@new.email");
		deleteUserEmailRequest.setSessionToken(response.getSessionToken());
		deleteUserEmailRequest.setUserId(response.getUserId());
		DeleteUserEmailResponse deleteUserEmailResponse = userAdminManager.deleteUserEmail(deleteUserEmailRequest);
		assertNotNull(deleteUserEmailResponse);
		assertNotNull(deleteUserEmailResponse.getSessionToken());
		assertEquals(DeleteUserEmailStatusEnum.SUCCESS, deleteUserEmailResponse.getDeleteUserEmailStatus());
		
		DeleteUserEmailRequest deleteUserEmailRequestnoEmail = new DeleteUserEmailRequest();
		deleteUserEmailRequestnoEmail.setEmailId("deletetest123@new.email");
		deleteUserEmailRequestnoEmail.setSessionToken(response.getSessionToken());
		deleteUserEmailRequestnoEmail.setUserId(response.getUserId());
		DeleteUserEmailResponse deleteUserEmailResponseNoEmail = userAdminManager.deleteUserEmail(deleteUserEmailRequestnoEmail);
		assertNotNull(deleteUserEmailResponseNoEmail);
		assertEquals(DeleteUserEmailStatusEnum.NO_EMAIL_ID, deleteUserEmailResponseNoEmail.getDeleteUserEmailStatus());
		
	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testDeleteUserEmailNull(){
		DeleteUserEmailRequest deleteUserEmailRequest = null;
		DeleteUserEmailResponse deleteUserEmailResponse = userAdminManager.deleteUserEmail(deleteUserEmailRequest);
		assertNotNull(deleteUserEmailResponse);
		assertEquals(DeleteUserEmailStatusEnum.INVALID_REQUEST, deleteUserEmailResponse.getDeleteUserEmailStatus());
	}
	@Test
	public void testDeleteUserEmailNOSession(){
		DeleteUserEmailRequest deleteUserEmailRequest = new DeleteUserEmailRequest();
		deleteUserEmailRequest.setSessionToken("");
		deleteUserEmailRequest.setEmailId("test@test.com");
		DeleteUserEmailResponse deleteUserEmailResponse = userAdminManager.deleteUserEmail(deleteUserEmailRequest);
		assertNotNull(deleteUserEmailResponse);
		assertEquals(DeleteUserEmailStatusEnum.NO_SESSION, deleteUserEmailResponse.getDeleteUserEmailStatus());
	}
	@Test
	public void testDeleteUserEmailWrongSession(){
		DeleteUserEmailRequest deleteUserEmailRequest = new DeleteUserEmailRequest();
		deleteUserEmailRequest.setSessionToken("asdasdassd");
		deleteUserEmailRequest.setEmailId("test@test.com");
		DeleteUserEmailResponse deleteUserEmailResponse = userAdminManager.deleteUserEmail(deleteUserEmailRequest);
		assertNotNull(deleteUserEmailResponse);
		assertEquals(DeleteUserEmailStatusEnum.NO_SESSION, deleteUserEmailResponse.getDeleteUserEmailStatus());
	}
	@Test
	public void testGetUserPhone(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		GetUserPhoneRequest getUserPhoneRequest = new GetUserPhoneRequest();
		getUserPhoneRequest.setSessionToken(response.getSessionToken());
		getUserPhoneRequest.setUserId(response.getUserId());
		GetUserPhoneResponse getUserPhoneResponse = userAdminManager.getUserPhone(getUserPhoneRequest);
		assertNotNull(getUserPhoneResponse);
		assertNotNull(getUserPhoneResponse.getUserId());
		assertNotNull(getUserPhoneResponse.getUserPhone());
		assertNotNull(getUserPhoneResponse.getSessionToken());
		assertEquals(GetUserPhoneStatusEnum.SUCCESS, getUserPhoneResponse.getGetUserPhoneStatus());
		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
		
	}
	@Test
	public void testGetUserPhoneNoPhone() {
		LoginRequest request = new LoginRequest();
		request.setUsername("-5");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		assertEquals(-5, response.getUserId().intValue());
		
		GetUserPhoneRequest getUserPhoneRequest = new GetUserPhoneRequest();
		getUserPhoneRequest.setSessionToken(response.getSessionToken());
		getUserPhoneRequest.setUserId(response.getUserId());
		GetUserPhoneResponse getUserPhoneResponse = userAdminManager.getUserPhone(getUserPhoneRequest);
		assertNotNull(getUserPhoneResponse);
		assertNotNull(getUserPhoneResponse.getUserId());
		assertEquals(GetUserPhoneStatusEnum.NO_PHONE, getUserPhoneResponse.getGetUserPhoneStatus());
		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
		
	}
	@Test
	public void testGetUserPhoneFailNoSession(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		GetUserPhoneRequest getUserPhoneRequest = new GetUserPhoneRequest();
		getUserPhoneRequest.setSessionToken("");
		getUserPhoneRequest.setUserId(response.getUserId());
		GetUserPhoneResponse getUserPhoneResponse = userAdminManager.getUserPhone(getUserPhoneRequest);
		assertNotNull(getUserPhoneResponse);
		assertNull(getUserPhoneResponse.getUserPhone());
		assertEquals(GetUserPhoneStatusEnum.NO_SESSION, getUserPhoneResponse.getGetUserPhoneStatus());	

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testGetUserPhoneFailInvalidSession(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		GetUserPhoneRequest getUserPhoneRequest = new GetUserPhoneRequest();
		getUserPhoneRequest.setSessionToken(response.getSessionToken()+"asdasd");
		getUserPhoneRequest.setUserId(response.getUserId());
		GetUserPhoneResponse getUserPhoneResponse = userAdminManager.getUserPhone(getUserPhoneRequest);
		assertNotNull(getUserPhoneResponse);
		assertNull(getUserPhoneResponse.getUserPhone());
		assertEquals(GetUserPhoneStatusEnum.NO_SESSION, getUserPhoneResponse.getGetUserPhoneStatus());	

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testGetUserPhoneInvalid(){
			
		GetUserPhoneRequest getUserPhoneRequest = null;
		GetUserPhoneResponse getUserPhoneResponse = userAdminManager.getUserPhone(getUserPhoneRequest);
		assertNotNull(getUserPhoneResponse);
		assertNull(getUserPhoneResponse.getUserPhone());
		assertEquals(GetUserPhoneStatusEnum.INVALID_USER, getUserPhoneResponse.getGetUserPhoneStatus());		
	}
	@Test
	public void testAddUserPhone(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserPhone userPhone = new UserPhone();
		userPhone.setPhone("7498775830");
		userPhone.setType("primary");
		
		AddUserPhoneRequest addUserPhoneRequest = new AddUserPhoneRequest();
		addUserPhoneRequest.setSessionToken(response.getSessionToken());
		addUserPhoneRequest.setUserId(response.getUserId());
		addUserPhoneRequest.setUserPhone(userPhone);
		AddUserPhoneResponse addUserPhoneResponse = userAdminManager.addUserPhone(addUserPhoneRequest);
		assertNotNull(addUserPhoneResponse);
		assertNotNull(addUserPhoneResponse.getSessionToken());
		assertEquals(AddUserPhoneStatusEnum.SUCCESS, addUserPhoneResponse.getAddUserPhoneStatus());	
		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testAddUserPhoneInvalid(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserPhone userPhone = new UserPhone();
		userPhone.setPhone("897879");
		userPhone.setType("primary");
		
		AddUserPhoneRequest addUserPhoneRequest = new AddUserPhoneRequest();
		addUserPhoneRequest.setSessionToken(response.getSessionToken());
		addUserPhoneRequest.setUserId(response.getUserId());
		addUserPhoneRequest.setUserPhone(userPhone);
		AddUserPhoneResponse addUserPhoneResponse = userAdminManager.addUserPhone(addUserPhoneRequest);
		assertNotNull(addUserPhoneResponse);
		assertEquals(AddUserPhoneStatusEnum.INVALID_PHONE, addUserPhoneResponse.getAddUserPhoneStatus());	

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testAddUserPhoneNull(){
		
		AddUserPhoneRequest addUserPhoneRequest = null;
		AddUserPhoneResponse addUserPhoneResponse = userAdminManager.addUserPhone(addUserPhoneRequest);
		assertNotNull(addUserPhoneResponse);
		assertEquals(AddUserPhoneStatusEnum.INVALID_PHONE, addUserPhoneResponse.getAddUserPhoneStatus());		
	}
	@Test
	public void testAddUserPhoneNOSession(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserPhone userPhone = new UserPhone();
		userPhone.setPhone("87686876876");
		userPhone.setType("primary");
		
		AddUserPhoneRequest addUserPhoneRequest = new AddUserPhoneRequest();
		addUserPhoneRequest.setSessionToken("");
		addUserPhoneRequest.setUserId(response.getUserId());
		addUserPhoneRequest.setUserPhone(userPhone);
		AddUserPhoneResponse addUserPhoneResponse = userAdminManager.addUserPhone(addUserPhoneRequest);
		assertNotNull(addUserPhoneResponse);
		assertEquals(AddUserPhoneStatusEnum.NO_SESSION, addUserPhoneResponse.getAddUserPhoneStatus());	

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testAddUserPhoneInvalidSession(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserPhone userPhone = new UserPhone();
		userPhone.setPhone("87686876876");
		userPhone.setType("primary");
		
		AddUserPhoneRequest addUserPhoneRequest = new AddUserPhoneRequest();
		addUserPhoneRequest.setSessionToken("asdasdasda");
		addUserPhoneRequest.setUserId(response.getUserId());
		addUserPhoneRequest.setUserPhone(userPhone);
		AddUserPhoneResponse addUserPhoneResponse = userAdminManager.addUserPhone(addUserPhoneRequest);
		assertNotNull(addUserPhoneResponse);
		assertEquals(AddUserPhoneStatusEnum.NO_SESSION, addUserPhoneResponse.getAddUserPhoneStatus());	

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	
	@Test
	public void testAddUserPhoneError(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserPhone userPhone = new UserPhone();
		userPhone.setPhone("9870587074");
		userPhone.setType("primary");
		
		AddUserPhoneRequest addUserPhoneRequest = new AddUserPhoneRequest();
		addUserPhoneRequest.setSessionToken(response.getSessionToken());
		addUserPhoneRequest.setUserId(response.getUserId());
		addUserPhoneRequest.setUserPhone(userPhone);
		AddUserPhoneResponse addUserPhoneResponse = userAdminManager.addUserPhone(addUserPhoneRequest);
		assertNotNull(addUserPhoneResponse);
		assertNotNull(addUserPhoneResponse.getSessionToken());
		assertEquals(AddUserPhoneStatusEnum.ERROR_ADDING_PHONE, addUserPhoneResponse.getAddUserPhoneStatus());		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testAddUserPhoneAlready(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		UserPhone userPhone = new UserPhone();
		userPhone.setPhone("9326164025");
		userPhone.setType("primary");
		
		AddUserPhoneRequest addUserPhoneRequest = new AddUserPhoneRequest();
		addUserPhoneRequest.setSessionToken(response.getSessionToken());
		addUserPhoneRequest.setUserId(response.getUserId());
		addUserPhoneRequest.setUserPhone(userPhone);
		AddUserPhoneResponse addUserPhoneResponse = userAdminManager.addUserPhone(addUserPhoneRequest);
		assertNotNull(addUserPhoneResponse);
		assertNotNull(addUserPhoneResponse.getSessionToken());
		assertEquals(AddUserPhoneStatusEnum.ALREADY_PRESENT, addUserPhoneResponse.getAddUserPhoneStatus());		

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testDeleteUserPhone(){
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");
		LoginResponse response = userManager.login(request);
		
		UserPhone userPhone = new UserPhone();
		userPhone.setPhone("7498775831");
		userPhone.setType("primary");
		
		AddUserPhoneRequest addUserPhoneRequest = new AddUserPhoneRequest();
		addUserPhoneRequest.setSessionToken(response.getSessionToken());
		addUserPhoneRequest.setUserId(response.getUserId());
		addUserPhoneRequest.setUserPhone(userPhone);
		AddUserPhoneResponse addUserPhoneResponse = userAdminManager.addUserPhone(addUserPhoneRequest);
		assertNotNull(addUserPhoneResponse);
		assertNotNull(addUserPhoneResponse.getSessionToken());
		assertEquals(AddUserPhoneStatusEnum.SUCCESS, addUserPhoneResponse.getAddUserPhoneStatus());
		
		DeleteUserPhoneRequest deleteUserPhoneRequest = new DeleteUserPhoneRequest();
		deleteUserPhoneRequest.setSessionToken(response.getSessionToken());
		deleteUserPhoneRequest.setUserId(response.getUserId());
		deleteUserPhoneRequest.setPhone("7498775831");
		DeleteUserPhoneResponse deleteUserPhoneResponse = userAdminManager.deleteUserPhone(deleteUserPhoneRequest);
		assertNotNull(deleteUserPhoneResponse);
		assertNotNull(deleteUserPhoneResponse.getSessionToken());
		assertEquals(DeleteUserPhoneStatusEnum.SUCCESS, deleteUserPhoneResponse.getDeleteUserPhoneStatus());
		
		DeleteUserPhoneRequest deleteUserPhoneRequestSame = new DeleteUserPhoneRequest();
		deleteUserPhoneRequestSame.setSessionToken(response.getSessionToken());
		deleteUserPhoneRequestSame.setUserId(response.getUserId());
		deleteUserPhoneRequestSame.setPhone("7498775831");
		DeleteUserPhoneResponse deleteUserPhoneResponseSame = userAdminManager.deleteUserPhone(deleteUserPhoneRequestSame);
		assertNotNull(deleteUserPhoneResponseSame);
		assertNotNull(deleteUserPhoneResponseSame.getSessionToken());
		assertEquals(DeleteUserPhoneStatusEnum.NO_PHONE, deleteUserPhoneResponseSame.getDeleteUserPhoneStatus());

	    LogoutRequest logoutRequest = new LogoutRequest();
	    logoutRequest.setSessionToken(response.getSessionToken());
	    LogoutResponse logoutResponse = userManager.logout(logoutRequest);
	}
	@Test
	public void testDeleteUserPhoneNull(){
		DeleteUserPhoneRequest deleteUserPhoneRequest = null;
		DeleteUserPhoneResponse deleteUserPhoneResponse = userAdminManager.deleteUserPhone(deleteUserPhoneRequest);
		assertNotNull(deleteUserPhoneResponse);
		assertEquals(DeleteUserPhoneStatusEnum.INVALID_REQUEST, deleteUserPhoneResponse.getDeleteUserPhoneStatus());		
	}
	public void testDeleteUserPhoneNoSession(){
		DeleteUserPhoneRequest deleteUserPhoneRequest = new DeleteUserPhoneRequest();
		deleteUserPhoneRequest.setSessionToken("");
		DeleteUserPhoneResponse deleteUserPhoneResponse = userAdminManager.deleteUserPhone(deleteUserPhoneRequest);
		assertNotNull(deleteUserPhoneResponse);
		assertEquals(DeleteUserPhoneStatusEnum.NO_SESSION, deleteUserPhoneResponse.getDeleteUserPhoneStatus());		
	}
	public void testDeleteUserPhoneInvalidSession(){
		DeleteUserPhoneRequest deleteUserPhoneRequest = new DeleteUserPhoneRequest();
		deleteUserPhoneRequest.setSessionToken("asdasdasda");
		DeleteUserPhoneResponse deleteUserPhoneResponse = userAdminManager.deleteUserPhone(deleteUserPhoneRequest);
		assertNotNull(deleteUserPhoneResponse);
		assertEquals(DeleteUserPhoneStatusEnum.NO_SESSION, deleteUserPhoneResponse.getDeleteUserPhoneStatus());		
	}
}
