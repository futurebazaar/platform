package com.fb.platform.wallet.manager.interfaces;

import com.fb.platform.wallet.manager.model.access.VerifyWalletRequest;
import com.fb.platform.wallet.manager.model.access.VerifyWalletResponse;
import com.fb.platform.wallet.manager.model.access.WalletSummaryRequest;
import com.fb.platform.wallet.manager.model.access.WalletSummaryResponse;
import com.fb.platform.wallet.manager.model.access.WalletHistoryRequest;
import com.fb.platform.wallet.manager.model.access.WalletHistoryResponse;
import com.fb.platform.wallet.manager.model.access.FillWalletRequest;
import com.fb.platform.wallet.manager.model.access.FillWalletResponse;
import com.fb.platform.wallet.manager.model.access.PayRequest;
import com.fb.platform.wallet.manager.model.access.PayResponse;
import com.fb.platform.wallet.manager.model.access.RefundRequest;
import com.fb.platform.wallet.manager.model.access.RefundResponse;
import com.fb.platform.wallet.manager.model.access.RevertRequest;
import com.fb.platform.wallet.manager.model.access.RevertResponse;


/**
 * @author kaushik
 * 
 */
public interface WalletManager {

	public WalletSummaryResponse getWalletSummary(
			WalletSummaryRequest walletSummaryRequest);

	public WalletHistoryResponse getWalletHistory(
			WalletHistoryRequest walletHistoryRequest);
	
	public FillWalletResponse fillWallet(FillWalletRequest fillWalletRequest);
	
	public PayResponse payFromWallet(PayRequest payRequest);
	
	public RefundResponse refundFromWallet(RefundRequest refundRequest);
	
	public RevertResponse revertWalletTransaction(RevertRequest revertRequest);

	public VerifyWalletResponse verifyWallet(VerifyWalletRequest apiVerifyReq);
}
