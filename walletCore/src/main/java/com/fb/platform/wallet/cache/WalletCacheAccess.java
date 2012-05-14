package com.fb.platform.wallet.cache;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.wallet.model.Wallet;

@Component
public class WalletCacheAccess extends AbstractCacheAccess {
	
	public void put(long walletId , Wallet wallet){
		platformCachingManager.put(NamedCachesEnum.WALLET_CACHE, walletId, wallet);
	}
	
	public Wallet get(long walletId) {
		return (Wallet) platformCachingManager.get(NamedCachesEnum.WALLET_CACHE, walletId);
	}

	public boolean clear(long walletId) {
		return platformCachingManager.remove(NamedCachesEnum.WALLET_CACHE, walletId);
	}

	public void lock(long walletId) {
        platformCachingManager.lock(NamedCachesEnum.WALLET_CACHE, walletId);
    }
    
    public void unlock(long walletId) {
        platformCachingManager.unlock(NamedCachesEnum.WALLET_CACHE, walletId);
    }
}
