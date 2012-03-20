/**
 * 
 */
package com.fb.platform.franchise.domain;

/**
 * @author ashish
 *
 */
public class NetworkBO {
	private int networkID;
	private String name;
	private double share;
	private int parentNetworkID;
	private int clientsID;
	private int userID;
	/**
	 * @return the networkID
	 */
	public int getNetworkID() {
		return networkID;
	}
		/**
	 * @param networkID the networkID to set
	 */
	public void setNetworkID(int networkID) {
		this.networkID = networkID;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the share
	 */
	public double getShare() {
		return share;
	}
	/**
	 * @param share the share to set
	 */
	public void setShare(double share) {
		this.share = share;
	}
	/**
	 * @return the parentNetworkID
	 */
	public int getParentNetworkID() {
		return parentNetworkID;
	}
	/**
	 * @param parentNetworkID the parentNetworkID to set
	 */
	public void setParentNetworkID(int parentNetworkID) {
		this.parentNetworkID = parentNetworkID;
	}
	/**
	 * @return the clientsID
	 */
	public int getClientsID() {
		return clientsID;
	}
	/**
	 * @param clientsID the clientsID to set
	 */
	public void setClientsID(int clientsID) {
		this.clientsID = clientsID;
	}
	/**
	 * @return the userID
	 */
	public int getUserID() {
		return userID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clientsID;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + networkID;
		result = prime * result + parentNetworkID;
		long temp;
		temp = Double.doubleToLongBits(share);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + userID;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NetworkBO))
			return false;
		NetworkBO other = (NetworkBO) obj;
		if (clientsID != other.clientsID)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (networkID != other.networkID)
			return false;
		if (parentNetworkID != other.parentNetworkID)
			return false;
		if (Double.doubleToLongBits(share) != Double
				.doubleToLongBits(other.share))
			return false;
		if (userID != other.userID)
			return false;
		return true;
	}

}
