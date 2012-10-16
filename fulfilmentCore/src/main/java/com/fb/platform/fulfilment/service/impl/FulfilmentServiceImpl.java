package com.fb.platform.fulfilment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.platform.fulfilment.cache.SellerPincodeMapCacheAccess;
import com.fb.platform.fulfilment.dao.SellerPincodeServicabilityMapDao;
import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;
import com.fb.platform.fulfilment.service.FulfilmentService;
import com.fb.platform.fulfilment.service.NonServicablePincodeException;

public class FulfilmentServiceImpl implements FulfilmentService{

	@Autowired
	private SellerPincodeServicabilityMapDao sellerPincodeServicabilityMapDao = null;
	
	@Autowired
	private SellerPincodeMapCacheAccess sellerForPincodeCacheAccess = null;
	
	@Override
	public SellerPincodeServicabilityMap getSellersForPincode(String pincode) throws NonServicablePincodeException, PlatformException {
		
		//check if we have pincode resolution is already cached.
		SellerPincodeServicabilityMap sellerPincodeMap = sellerForPincodeCacheAccess.get(pincode);
		if (sellerPincodeMap == null) {
			//load it using dao
			try {
				sellerPincodeMap = sellerPincodeServicabilityMapDao.getSellersForPincode(pincode);
			} catch (DataAccessException e) {
				throw new PlatformException("Error while resolving sellers for pincode : " + pincode , e);
			}

			if (sellerPincodeMap != null) {
				cacheSellerPincodeMap(pincode, sellerPincodeMap);
			} else {
				throw new NonServicablePincodeException("No seller found for Pincode " + pincode);
			}
		}
		
		return sellerPincodeMap;
	}
	
	private void cacheSellerPincodeMap(String pincode, SellerPincodeServicabilityMap sellerPincodeMap) {
		//cache pincode-sellers mapping
		try {
			sellerForPincodeCacheAccess.lock(pincode);
			if (sellerForPincodeCacheAccess.get(pincode) == null) {
				sellerForPincodeCacheAccess.put(pincode, sellerPincodeMap);
			}
		} finally {
			sellerForPincodeCacheAccess.unlock(pincode);
		}
	}

}
