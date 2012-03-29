package com.fb.platform.user.manager.model.admin;

public class IsValidUserResponse {

	private IsValidUserEnum isValidUserStatus;

	/**
	 * @return the isValidUserStatus
	 */
	public IsValidUserEnum getIsValidUserStatus() {
		return isValidUserStatus;
	}

	/**
	 * @param isValidUserStatus the isValidUserStatus to set
	 */
	public void setIsValidUserStatus(IsValidUserEnum isValidUserStatus) {
		this.isValidUserStatus = isValidUserStatus;
	}
}
