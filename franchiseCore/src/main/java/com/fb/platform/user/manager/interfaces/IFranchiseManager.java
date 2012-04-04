/**
 * 
 */
package com.fb.platform.user.manager.interfaces;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.franchise.manager.model.FranchiseLoginRequest;
import com.fb.platform.franchise.manager.model.FranchiseLoginResponse;
import com.fb.platform.franchise.manager.model.FranchiseLogoutRequest;
import com.fb.platform.franchise.manager.model.FranchiseLogoutResponse;

/**
 * @author ashish
 *
 */
public interface IFranchiseManager {

	@Transactional(propagation=Propagation.REQUIRED)
	public FranchiseLoginResponse login(FranchiseLoginRequest loginRequest);

	@Transactional(propagation=Propagation.REQUIRED)
	public FranchiseLogoutResponse logout(FranchiseLogoutRequest logoutRequest);

	/*@Transactional(propagation=Propagation.REQUIRED)
	public FranchiseChangePasswordResponse changePassword(FranchiseChangePasswordRequest request);*/

	//public FranchiseTO getFranchise(String key); 
}
