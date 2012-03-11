/**
 * 
 */
package com.fb.platform.user.manager.model.auth;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class LogoutResponse implements Serializable {

	private LogoutStatusEnum logoutStatus = null;

	public LogoutStatusEnum getLogoutStatus() {
		return logoutStatus;
	}

	public void setLogoutStatus(LogoutStatusEnum logoutStatus) {
		this.logoutStatus = logoutStatus;
	}

}
