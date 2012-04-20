package com.fb.platform.user.manager.interfaces;

import com.fb.platform.user.manager.model.address.AddAddressRequest;
import com.fb.platform.user.manager.model.address.AddAddressResponse;
import com.fb.platform.user.manager.model.address.DeleteAddressRequest;
import com.fb.platform.user.manager.model.address.DeleteAddressResponse;
import com.fb.platform.user.manager.model.address.GetAddressRequest;
import com.fb.platform.user.manager.model.address.GetAddressResponse;
import com.fb.platform.user.manager.model.address.UpdateAddressRequest;
import com.fb.platform.user.manager.model.address.UpdateAddressResponse;

public interface UserAddressManager {

	GetAddressResponse getAddress(GetAddressRequest getAddressRequest);
	
	AddAddressResponse addAddress(AddAddressRequest addAddressRequest);
	
	UpdateAddressResponse updateAddress(UpdateAddressRequest updateAddressRequest);
	
	DeleteAddressResponse deleteAddress(DeleteAddressRequest deleteAddressRequest);
}
