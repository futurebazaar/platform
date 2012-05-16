package com.fb.platform.wallet.service.exception;

import com.fb.commons.PlatformException;

public class WalletNOtFoundException extends PlatformException {

	public WalletNOtFoundException() {
	}

	public WalletNOtFoundException(String message) {
		super(message);
	}

	public WalletNOtFoundException(Throwable cause) {
		super(cause);
	}

	public WalletNOtFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
