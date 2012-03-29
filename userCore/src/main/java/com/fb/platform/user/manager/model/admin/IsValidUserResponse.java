package com.fb.platform.user.manager.model.admin;

public class IsValidUserResponse {

	private IsValidUserEnum isValidUserStatus;
	private int userId;

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

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
