package com.fb.platform.fulfilment.cache;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;

/**
 * @author suhas
 *
*/
@Component
public class SellerPincodeMapCacheAccess extends AbstractCacheAccess{

	public void put(String pincode, SellerPincodeServicabilityMap sellerByPincode) {
		platformCachingManager.put(NamedCachesEnum.FULFILMENT_CACHE, pincode, sellerByPincode);
	}

	public SellerPincodeServicabilityMap get(String pincode) {
		return (SellerPincodeServicabilityMap) platformCachingManager.get(NamedCachesEnum.FULFILMENT_CACHE, pincode);
	}

	public boolean clear(String pincode) {
		return platformCachingManager.remove(NamedCachesEnum.FULFILMENT_CACHE, pincode);
	}

	public void lock(String pincode) {
        platformCachingManager.lock(NamedCachesEnum.FULFILMENT_CACHE, pincode);
    }
    
    public void unlock(String pincode) {
        platformCachingManager.unlock(NamedCachesEnum.FULFILMENT_CACHE, pincode);
    }
}
