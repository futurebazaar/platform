/**
 * 
 */
package com.fb.platform.franchise.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.franchise.dao.interfaces.IFranchiseDAO;
import com.fb.platform.franchise.dao.interfaces.INetworkDAO;
import com.fb.platform.franchise.manager.model.AddFranchiseRequest;
import com.fb.platform.franchise.manager.model.AddFranchiseResponse;
import com.fb.platform.franchise.manager.model.ChangeFranchiseRoleRequest;
import com.fb.platform.franchise.manager.model.ChangeFranchiseRoleResponse;
import com.fb.platform.franchise.manager.model.GetAllFranchiseRequest;
import com.fb.platform.franchise.manager.model.GetAllFranchiseResponse;
import com.fb.platform.franchise.manager.model.GetDirectFranchiseRequest;
import com.fb.platform.franchise.manager.model.GetDirectFranchiseResponse;
import com.fb.platform.user.manager.interfaces.INetworkManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;
import com.fb.platform.user.manager.model.auth.LogoutRequest;
import com.fb.platform.user.manager.model.auth.LogoutResponse;

/**
 * @author ashish
 *
 */
public class NetworkManagerImpl implements INetworkManager {

	@Autowired
	private INetworkDAO networkDAO = null;
	
	@Autowired
	private IFranchiseDAO franchiseDAO = null;
	
	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogoutResponse logout(LogoutRequest logoutRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetDirectFranchiseResponse getDirectFranchises(GetDirectFranchiseRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetAllFranchiseResponse getAllFranchises(GetAllFranchiseRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AddFranchiseResponse addFranchise(AddFranchiseRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChangeFranchiseRoleResponse changeFranchiseRole(ChangeFranchiseRoleRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}
