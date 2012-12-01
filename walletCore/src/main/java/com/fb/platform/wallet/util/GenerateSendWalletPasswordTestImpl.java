package com.fb.platform.wallet.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenerateSendWalletPasswordTestImpl implements GenerateSendWalletPassword{

	private Log log = LogFactory.getLog(GenerateSendWalletPasswordTestImpl.class);
	
	@Override
	public String generateSendWalletPassword(long userId,boolean isReset){
		String randomPassword = RandomStringUtils.random(4, false, true);
		return randomPassword;
	}
	
	@Override
	public void sendWalletPassword(long userId,String randomPassword,boolean isReset){
		log.info("Wallet password generated for userId: " + userId + " :::::" + randomPassword);
	}
}
