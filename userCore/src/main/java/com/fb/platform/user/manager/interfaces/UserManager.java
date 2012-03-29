package com.fb.platform.user.manager.interfaces;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.user.manager.model.auth.ChangePasswordRequest;
import com.fb.platform.user.manager.model.auth.ChangePasswordResponse;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;
import com.fb.platform.user.manager.model.auth.LogoutRequest;
import com.fb.platform.user.manager.model.auth.LogoutResponse;


/**
 * @author kumar
 * @author vinayak
 *
 */
@Transactional
public interface UserManager {

	@Transactional(propagation = Propagation.REQUIRED)
	public LoginResponse login(LoginRequest loginRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	public LogoutResponse logout(LogoutRequest logoutRequest);

	@Transactional(propagation = Propagation.REQUIRED)
	public ChangePasswordResponse changePassword(ChangePasswordRequest request);
}
