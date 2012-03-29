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
import com.fb.platform.user.manager.model.admin.UpdateUserReponse;
import com.fb.platform.user.manager.model.admin.UpdateUserRequest;

/**
 * @author kumar
 *
 * @author vinayak
 *
 */
@Transactional
public interface UserAdminManager {

	@Transactional(propagation = Propagation.SUPPORTS)
	GetUserResponse getUser(GetUserRequest getUserRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	AddUserResponse addUser(AddUserRequest addUserRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	UpdateUserReponse updateUser(UpdateUserRequest updateUserRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	IsValidUserResponse isValidUser(IsValidUserRequest isValidUserRequest);

	/*@Transactional(propagation=Propagation.SUPPORTS)
	public GetUsersReponse getUsers(GetUsersRequest getUsersRequest);*/

}
