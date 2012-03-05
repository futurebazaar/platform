/**
 * 
 */
package com.fb.platform.user.manager.model.auth;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public enum LogoutStatusEnum implements Serializable {

	LOGOUT_SUCCESS("LOGOUT_SUCCESS"),
	LOGOUT_FAILED("LOGOUT_FAILED"),
	NO_SESSION("NO_SESSION");

	private String status = null;

	private LogoutStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
