package com.fb.platform.wallet.util;

public interface GenerateSendWalletPassword {
	
	public void sendWalletPassword(long userId,String randomPassword,boolean isReset);
	public String generateSendWalletPassword(long userId,boolean isReset);

}
