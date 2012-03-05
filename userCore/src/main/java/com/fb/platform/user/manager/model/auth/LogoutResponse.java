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

	private LogoutStatusEnum status = null;

	public LogoutStatusEnum getStatus() {
		return status;
	}

	public void setStatus(LogoutStatusEnum status) {
		this.status = status;
	}
}
