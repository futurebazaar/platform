/**
 * 
 */
package com.fb.platform.ifs.caching;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.ifs.domain.ProductBo;

/**
 * @author sarvesh
 *
 */
@Component
public class ProductGroupCacheAccess extends AbstractCacheAccess {

    public void remove(String key) {
        platformCachingManager.remove(NamedCachesEnum.PRODUCT_GROUP_CACHE, key);
    }
    
	public void put(String key, ProductBo productBo) {
		platformCachingManager.put(NamedCachesEnum.PRODUCT_GROUP_CACHE, key, productBo);
	}
	
	public ProductBo get(String key) {
		return (ProductBo) platformCachingManager.get(NamedCachesEnum.PRODUCT_GROUP_CACHE, key);
	}
	
	public void lock(String key) {
        platformCachingManager.lock(NamedCachesEnum.PRODUCT_GROUP_CACHE, key);
    }
    
    public void unlock(String key) {
        platformCachingManager.unlock(NamedCachesEnum.PRODUCT_GROUP_CACHE, key);
    }
}
