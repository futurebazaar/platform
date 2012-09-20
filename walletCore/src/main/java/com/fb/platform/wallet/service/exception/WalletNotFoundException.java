package com.fb.platform.wallet.service.exception;

import com.fb.commons.PlatformException;

public class WalletNotFoundException extends PlatformException {

	public WalletNotFoundException() {
	}

	public WalletNotFoundException(String message) {
		super(message);
	}

	public WalletNotFoundException(Throwable cause) {
		super(cause);
	}

	public WalletNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
