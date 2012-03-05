/**
 * 
 */
package com.fb.platform.user.manager.model.auth;

/**
 * @author vinayak
 *
 */
public enum ChangePasswordStatusEnum {

	SUCCESS("SUCCESS"),
	CHANGE_PASSWORD_FAILED("CHANGE_PASSWORD_FAILED"),
	DIFFERENT_PASSWORDS_REQUIRED("DIFFERENT_PASSWORDS_REQUIRED"),
	NO_SESSION("NO_SESSION");

	private String status = null;

	private ChangePasswordStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
