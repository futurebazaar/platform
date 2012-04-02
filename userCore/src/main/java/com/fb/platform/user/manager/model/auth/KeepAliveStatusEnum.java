package com.fb.platform.user.manager.model.auth;

import java.io.Serializable;

public enum KeepAliveStatusEnum implements Serializable {
	KEEPALIVE_SUCCESS("KEEPALIVE_SUCCESS"),
	KEEPALIVE_FAILED("KEEPALIVE_FAILED"),
	NO_SESSION("NO_SESSION");

	private String keepAliveStatus = null;

	private KeepAliveStatusEnum(String keepAliveStatus) {
		this.keepAliveStatus = keepAliveStatus;
	}

	@Override
	public String toString() {
		return this.keepAliveStatus;
	}
}
