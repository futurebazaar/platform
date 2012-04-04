/**
 * 
 */
package com.fb.platform.user.manager.model.auth;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public enum LoginStatusEnum implements Serializable {

	LOGIN_SUCCESS("LOGIN_SUCCESS"),
	GUEST_LOGIN_SUCCESS("GUEST_LOGIN_SUCCESS"),
	INVALID_USERNAME_PASSWORD("INVALID_USERNAME_PASSWORD"),
	LOGIN_FAILURE("LOGIN_FAILURE"),
	LOGIN_ACCOUNT_LOCKED("LOGIN_ACCOUNT_LOCKED"),
	LOGIN_ACCOUNT_CLOSED("LOGIN_ACCOUNT_CLOSED");

	private String status = null;

	private LoginStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
