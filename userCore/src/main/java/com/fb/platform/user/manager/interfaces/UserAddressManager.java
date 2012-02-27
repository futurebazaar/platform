package com.fb.platform.user.manager.interfaces;

import java.util.Collection;

import com.fb.platform.user.manager.model.UserAddressTO;

public interface UserAddressManager {
	
	Collection<UserAddressTO> getAddress (int userid);
	void addAddress (UserAddressTO userAddressTO);
	void updateAddress (UserAddressTO userAddressTO);

}
