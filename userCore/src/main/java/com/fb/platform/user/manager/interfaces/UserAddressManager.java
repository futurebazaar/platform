package com.fb.platform.user.manager.interfaces;

import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.user.manager.model.UserAddressTO;

public interface UserAddressManager {
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public GetAddressResponse getAddress (GetAddressRequest getAddressRequest);
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public AddAddressResponse addAddress (AddAddressRequest addAddressRequest);
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public  UpdateAddressResponse updateAddress (UpdateAddressRequest updateAddressRequest);

}
