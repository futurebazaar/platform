package com.fb.platform.wallet.dao;

import com.fb.platform.wallet.model.Wallet;

public interface WalletDao {
	
	public Wallet load (long walletId);
	
	public Wallet load (long userId , long clientId);
	
	public Wallet update(Wallet wallet);

}