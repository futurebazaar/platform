package com.fb.platform.user.manager.interfaces;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.user.manager.model.address.AddAddressRequest;
import com.fb.platform.user.manager.model.address.AddAddressResponse;
import com.fb.platform.user.manager.model.address.GetAddressRequest;
import com.fb.platform.user.manager.model.address.GetAddressResponse;
import com.fb.platform.user.manager.model.address.UpdateAddressRequest;
import com.fb.platform.user.manager.model.address.UpdateAddressResponse;

public interface UserAddressManager {

	@Transactional(propagation = Propagation.SUPPORTS)
	GetAddressResponse getAddress(GetAddressRequest getAddressRequest);

	@Transactional(propagation = Propagation.SUPPORTS)
	AddAddressResponse addAddress(AddAddressRequest addAddressRequest);

	@Transactional(propagation = Propagation.SUPPORTS)
	UpdateAddressResponse updateAddress(UpdateAddressRequest updateAddressRequest);

}
