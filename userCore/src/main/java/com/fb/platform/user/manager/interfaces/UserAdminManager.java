/**
 *
 */
package com.fb.platform.user.manager.interfaces;

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
public interface UserAdminManager {

	GetUserResponse getUser(GetUserRequest getUserRequest);

	AddUserResponse addUser(AddUserRequest addUserRequest);

	UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest);

	IsValidUserResponse isValidUser(IsValidUserRequest isValidUserRequest);

	GetUserEmailResponse getUserEmail(GetUserEmailRequest getUserEmailRequest);
	
	AddUserEmailResponse addUserEmail(AddUserEmailRequest addUserEmailRequest);

	VerifyUserEmailResponse verifyUserEmail(VerifyUserEmailRequest verifyUserEmailRequest);
	
	DeleteUserEmailResponse deleteUserEmail(DeleteUserEmailRequest deleteUserEmailRequest);
	
	GetUserPhoneResponse getUserPhone(GetUserPhoneRequest getUserPhoneRequest);

	AddUserPhoneResponse addUserPhone(AddUserPhoneRequest addUserPhoneRequest);

	VerifyUserPhoneResponse verifyUserPhone(VerifyUserPhoneRequest verifyUserPhoneRequest);
	
	DeleteUserPhoneResponse deleteUserPhone(DeleteUserPhoneRequest deleteUserPhoneRequest);
}
