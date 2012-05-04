package com.fb.platform.user.manager.interfaces;

import com.fb.platform.user.manager.model.address.AddAddressRequest;
import com.fb.platform.user.manager.model.address.AddAddressResponse;
import com.fb.platform.user.manager.model.address.DeleteAddressRequest;
import com.fb.platform.user.manager.model.address.DeleteAddressResponse;
import com.fb.platform.user.manager.model.address.GetAddressByIdRequest;
import com.fb.platform.user.manager.model.address.GetAddressByIdResponse;
import com.fb.platform.user.manager.model.address.GetAddressRequest;
import com.fb.platform.user.manager.model.address.GetAddressResponse;
import com.fb.platform.user.manager.model.address.UpdateAddressRequest;
import com.fb.platform.user.manager.model.address.UpdateAddressResponse;

public interface UserAddressManager {

	GetAddressResponse getAddress(GetAddressRequest getAddressRequest);
	
	GetAddressByIdResponse getAddress(GetAddressByIdRequest getAddressByIdRequest);
	
	AddAddressResponse addAddress(AddAddressRequest addAddressRequest);
	
	UpdateAddressResponse updateAddress(UpdateAddressRequest updateAddressRequest);
	
	DeleteAddressResponse deleteAddress(DeleteAddressRequest deleteAddressRequest);
}
