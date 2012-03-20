package com.fb.platform.franchise.domain;


public class FranchiseBO {
	
	private int franchiseID;
	private String role;
	private int networkID;
	private int userID;
	private boolean isActive;
	
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the FranchiseID
	 */
	public int getFranchiseID() {
		return franchiseID;
	}
	/**
	 * @param userid the FranchiseID to set
	 */
	public void setFranchiseID(int franchiseID) {
		this.franchiseID = franchiseID;
	}
	
	/**
	 * @return the FranchiseID
	 */
	public int getNetworkID() {
		return networkID;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setNetworkID(int networkID) {
		this.networkID = networkID;
	}
	
	/**
	 * @return the name
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param name the name to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	
	

}
