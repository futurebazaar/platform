package com.fb.platform.fulfilment.service;

import java.util.List;

import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;

public interface FulfilmentService {

	public SellerPincodeServicabilityMap getSellersForPincode(String pincode);
}
