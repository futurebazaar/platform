/**
 * 
 */
package com.fb.platform.ifs.caching;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.ifs.domain.LSPBo;

/**
 * @author sarvesh
 *
 */
@Component
public class LSPCacheAccess extends AbstractCacheAccess {

    public void remove(String key) {
        platformCachingManager.remove(NamedCachesEnum.LSP_CACHE, key);
    }
    
	public void put(String key, List<LSPBo> lspBoList) {
		platformCachingManager.put(NamedCachesEnum.LSP_CACHE, key, lspBoList);
	}
	
	@SuppressWarnings("unchecked")
	public List<LSPBo> get(String key) {
		return (List<LSPBo>) platformCachingManager.get(NamedCachesEnum.LSP_CACHE, key);
	}
	
	public void lock(String key) {
        platformCachingManager.lock(NamedCachesEnum.LSP_CACHE, key);
    }
    
    public void unlock(String key) {
        platformCachingManager.unlock(NamedCachesEnum.LSP_CACHE, key);
    }
}
