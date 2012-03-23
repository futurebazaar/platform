/**
 * 
 */
package com.fb.platform.franchise.domain;

/**
 * @author ashish
 * 
 */
public class MerchantTypeKeyBO {

	private int id;
	private double percentage;
	private String key;
	private int networkID;
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
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + networkID;
		long temp;
		temp = Double.doubleToLongBits(percentage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (!(obj instanceof MerchantTypeKeyBO))
			return false;
		MerchantTypeKeyBO other = (MerchantTypeKeyBO) obj;
		if (id != other.id)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (networkID != other.networkID)
			return false;
		if (Double.doubleToLongBits(percentage) != Double
				.doubleToLongBits(other.percentage))
			return false;
		return true;
	}
}
