/**
 *
 */
package com.fb.platform.user.manager.interfaces;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.user.manager.model.admin.AddUserRequest;
import com.fb.platform.user.manager.model.admin.AddUserResponse;
import com.fb.platform.user.manager.model.admin.GetUserRequest;
import com.fb.platform.user.manager.model.admin.GetUserResponse;
import com.fb.platform.user.manager.model.admin.IsValidUserRequest;
import com.fb.platform.user.manager.model.admin.IsValidUserResponse;
import com.fb.platform.user.manager.model.admin.UpdateUserRequest;
import com.fb.platform.user.manager.model.admin.UpdateUserResponse;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.VerifyUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.VerifyUserEmailResponse;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneResponse;

/**
 * @author kumar
 *
 * @author vinayak
 *
 */
@Transactional
public interface UserAdminManager {

	GetUserResponse getUser(GetUserRequest getUserRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	AddUserResponse addUser(AddUserRequest addUserRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	IsValidUserResponse isValidUser(IsValidUserRequest isValidUserRequest);

	/*@Transactional(propagation=Propagation.SUPPORTS)
	public GetUsersReponse getUsers(GetUsersRequest getUsersRequest);*/
	
	@Transactional(propagation = Propagation.SUPPORTS)
	GetUserEmailResponse getUserEmail(GetUserEmailRequest getUserEmailRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	AddUserEmailResponse addUserEmail(AddUserEmailRequest addUserEmailRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	VerifyUserEmailResponse verifyUserEmail(VerifyUserEmailRequest verifyUserEmailRequest);
	
	@Transactional(propagation = Propagation.REQUIRED)
	DeleteUserEmailResponse deleteUserEmail(DeleteUserEmailRequest deleteUserEmailRequest);
	
	
	@Transactional(propagation = Propagation.SUPPORTS)
	GetUserPhoneResponse getUserPhone(GetUserPhoneRequest getUserPhoneRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	AddUserPhoneResponse addUserPhone(AddUserPhoneRequest addUserPhoneRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	VerifyUserPhoneResponse verifyUserPhone(VerifyUserPhoneRequest verifyUserPhoneRequest);
	
	@Transactional(propagation = Propagation.REQUIRED)
	DeleteUserPhoneResponse deleteUserPhone(DeleteUserPhoneRequest deleteUserPhoneRequest);



}
