/**
 * 
 */
package com.fb.platform.user.manager.interfaces;

import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.user.manager.model.UserTO;
import com.fb.platform.user.manager.model.admin.AddUserRequest;
import com.fb.platform.user.manager.model.admin.AddUserResponse;
import com.fb.platform.user.manager.model.admin.GetUserRequest;
import com.fb.platform.user.manager.model.admin.GetUserResponse;
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

	@Transactional(propagation=Propagation.SUPPORTS)
	public GetUserResponse getUser(GetUserRequest getUserRequest);

	@Transactional(propagation=Propagation.REQUIRED)
	public AddUserResponse addUser(AddUserRequest addUserRequest);

	@Transactional(propagation=Propagation.REQUIRED)
	public UpdateUserReponse updateUser(UpdateUserRequest updateUserRequest);

	/*@Transactional(propagation=Propagation.SUPPORTS)
	public GetUsersReponse getUsers(GetUsersRequest getUsersRequest);*/

}
