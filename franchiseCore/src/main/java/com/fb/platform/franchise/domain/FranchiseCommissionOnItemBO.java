/**
 * 
 */
package com.fb.platform.franchise.domain;

/**
 * @author ashish
 * 
 */
public class FranchiseCommissionOnItemBO {

	private int id;
	private int franchiseOrderID;
	private int orderItemID;
	private double franchiseCommissionAmt;
	private double networkCommissionAmt;
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
	 * @return the franchiseOrderID
	 */
	public int getFranchiseOrderID() {
		return franchiseOrderID;
	}
	/**
	 * @param franchiseOrderID the franchiseOrderID to set
	 */
	public void setFranchiseOrderID(int franchiseOrderID) {
		this.franchiseOrderID = franchiseOrderID;
	}
	/**
	 * @return the orderItemID
	 */
	public int getOrderItemID() {
		return orderItemID;
	}
	/**
	 * @param orderItemID the orderItemID to set
	 */
	public void setOrderItemID(int orderItemID) {
		this.orderItemID = orderItemID;
	}
	/**
	 * @return the franchiseCommissionAmt
	 */
	public double getFranchiseCommissionAmt() {
		return franchiseCommissionAmt;
	}
	/**
	 * @param franchiseCommissionAmt the franchiseCommissionAmt to set
	 */
	public void setFranchiseCommissionAmt(double franchiseCommissionAmt) {
		this.franchiseCommissionAmt = franchiseCommissionAmt;
	}
	/**
	 * @return the networkCommissionAmt
	 */
	public double getNetworkCommissionAmt() {
		return networkCommissionAmt;
	}
	/**
	 * @param networkCommissionAmt the networkCommissionAmt to set
	 */
	public void setNetworkCommissionAmt(double networkCommissionAmt) {
		this.networkCommissionAmt = networkCommissionAmt;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(franchiseCommissionAmt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + franchiseOrderID;
		result = prime * result + id;
		temp = Double.doubleToLongBits(networkCommissionAmt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + orderItemID;
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
		if (!(obj instanceof FranchiseCommissionOnItemBO))
			return false;
		FranchiseCommissionOnItemBO other = (FranchiseCommissionOnItemBO) obj;
		if (Double.doubleToLongBits(franchiseCommissionAmt) != Double
				.doubleToLongBits(other.franchiseCommissionAmt))
			return false;
		if (franchiseOrderID != other.franchiseOrderID)
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(networkCommissionAmt) != Double
				.doubleToLongBits(other.networkCommissionAmt))
			return false;
		if (orderItemID != other.orderItemID)
			return false;
		return true;
	}
	
}
