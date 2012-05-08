package com.fb.platform.wallet.manager.interfaces;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.wallet.manager.model.access.WalletSummaryRequest;
import com.fb.platform.wallet.manager.model.access.WalletSummaryResponse;


/**
 * @author kaushik
 *
 */
@Transactional
public interface WalletManager {

	@Transactional(propagation = Propagation.REQUIRED)
	public WalletSummaryResponse getWalletSummary(WalletSummaryRequest walletSummaryRequest);
}
