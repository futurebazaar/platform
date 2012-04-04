package com.fb.platform.franchise.manager.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FranchiseLoginRequest implements Serializable {

	private String username;
	private String password;
	private String ipAddress;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
