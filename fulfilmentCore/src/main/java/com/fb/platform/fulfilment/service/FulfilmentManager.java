package com.fb.platform.fulfilment.service;

import com.fb.platform.fulfilment.to.SellerByPincodeRequest;
import com.fb.platform.fulfilment.to.SellerByPincodeResponse;

public interface FulfilmentManager {

	public SellerByPincodeResponse getSellerByPincode(SellerByPincodeRequest sellerByPincodeRequest);

}
