/**
 * 
 */
package com.fb.platform.franchise.domain;

import java.util.Date;

/**
 * @author ashish
 * 
 */
public class FranchiseCommisionOnBO {

	private int id;
	private int networkID;
	private int commisionID;
	private int sellerRateChartID;
	private Date timestamp;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
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
	 * @return the commisionID
	 */
	public int getCommisionID() {
		return commisionID;
	}
	/**
	 * @param commisionID the commisionID to set
	 */
	public void setCommisionID(int commisionID) {
		this.commisionID = commisionID;
	}
	/**
	 * @return the sellerRateChartID
	 */
	public int getSellerRateChartID() {
		return sellerRateChartID;
	}
	/**
	 * @param sellerRateChartID the sellerRateChartID to set
	 */
	public void setSellerRateChartID(int sellerRateChartID) {
		this.sellerRateChartID = sellerRateChartID;
	}
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + commisionID;
		result = prime * result + id;
		result = prime * result + networkID;
		result = prime * result + sellerRateChartID;
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
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
		if (!(obj instanceof FranchiseCommisionOnBO))
			return false;
		FranchiseCommisionOnBO other = (FranchiseCommisionOnBO) obj;
		if (commisionID != other.commisionID)
			return false;
		if (id != other.id)
			return false;
		if (networkID != other.networkID)
			return false;
		if (sellerRateChartID != other.sellerRateChartID)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	
}
