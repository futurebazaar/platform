package com.fb.platform.fulfilment.dao;

import java.util.List;

import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;

public interface SellerPincodeServicabilityMapDao {
	public SellerPincodeServicabilityMap getSellersForPincode(String pincode);
}
