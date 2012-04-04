package com.fb.platform.user.manager.interfaces;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.franchise.manager.model.AddFranchiseRequest;
import com.fb.platform.franchise.manager.model.AddFranchiseResponse;
import com.fb.platform.franchise.manager.model.ChangeFranchiseRoleRequest;
import com.fb.platform.franchise.manager.model.ChangeFranchiseRoleResponse;
import com.fb.platform.franchise.manager.model.GetAllFranchiseRequest;
import com.fb.platform.franchise.manager.model.GetAllFranchiseResponse;
import com.fb.platform.franchise.manager.model.GetDirectFranchiseRequest;
import com.fb.platform.franchise.manager.model.GetDirectFranchiseResponse;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;
import com.fb.platform.user.manager.model.auth.LogoutRequest;
import com.fb.platform.user.manager.model.auth.LogoutResponse;



/**
 * @author ashish
 *
 */
@Transactional
public interface INetworkManager {
	
	@Transactional(propagation=Propagation.REQUIRED)
	public LoginResponse login(LoginRequest loginRequest);

	@Transactional(propagation=Propagation.REQUIRED)
	public LogoutResponse logout(LogoutRequest logoutRequest);

	@Transactional(propagation=Propagation.REQUIRED)
	public GetDirectFranchiseResponse getDirectFranchises(GetDirectFranchiseRequest request);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public GetAllFranchiseResponse getAllFranchises(GetAllFranchiseRequest request);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public AddFranchiseResponse addFranchise(AddFranchiseRequest request);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public ChangeFranchiseRoleResponse changeFranchiseRole(ChangeFranchiseRoleRequest request);
}