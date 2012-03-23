/**
 * 
 */
package com.fb.platform.franchise.domain;

import java.util.Date;

/**
 * @author ashish
 *
 */
public class FranchiseOrderBO {

	private int id;
	private int franchiseID;
	private int orderID;
	private double franchiseCommissionAmt;
	private double networkCommissionAmt;
	private Date bookingTimestamp;
	private Date confirmingTimestamp;
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
	 * @return the franchiseID
	 */
	public int getFranchiseID() {
		return franchiseID;
	}
	/**
	 * @param franchiseID the franchiseID to set
	 */
	public void setFranchiseID(int franchiseID) {
		this.franchiseID = franchiseID;
	}
	/**
	 * @return the orderID
	 */
	public int getOrderID() {
		return orderID;
	}
	/**
	 * @param orderID the orderID to set
	 */
	public void setOrderID(int orderID) {
		this.orderID = orderID;
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
	/**
	 * @return the bookingTimestamp
	 */
	public Date getBookingTimestamp() {
		return bookingTimestamp;
	}
	/**
	 * @param bookingTimestamp the bookingTimestamp to set
	 */
	public void setBookingTimestamp(Date bookingTimestamp) {
		this.bookingTimestamp = bookingTimestamp;
	}
	/**
	 * @return the confirmingTimestamp
	 */
	public Date getConfirmingTimestamp() {
		return confirmingTimestamp;
	}
	/**
	 * @param confirmingTimestamp the confirmingTimestamp to set
	 */
	public void setConfirmingTimestamp(Date confirmingTimestamp) {
		this.confirmingTimestamp = confirmingTimestamp;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((bookingTimestamp == null) ? 0 : bookingTimestamp.hashCode());
		result = prime
				* result
				+ ((confirmingTimestamp == null) ? 0 : confirmingTimestamp
						.hashCode());
		long temp;
		temp = Double.doubleToLongBits(franchiseCommissionAmt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + franchiseID;
		result = prime * result + id;
		temp = Double.doubleToLongBits(networkCommissionAmt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + orderID;
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
		if (!(obj instanceof FranchiseOrderBO))
			return false;
		FranchiseOrderBO other = (FranchiseOrderBO) obj;
		if (bookingTimestamp == null) {
			if (other.bookingTimestamp != null)
				return false;
		} else if (!bookingTimestamp.equals(other.bookingTimestamp))
			return false;
		if (confirmingTimestamp == null) {
			if (other.confirmingTimestamp != null)
				return false;
		} else if (!confirmingTimestamp.equals(other.confirmingTimestamp))
			return false;
		if (Double.doubleToLongBits(franchiseCommissionAmt) != Double
				.doubleToLongBits(other.franchiseCommissionAmt))
			return false;
		if (franchiseID != other.franchiseID)
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(networkCommissionAmt) != Double
				.doubleToLongBits(other.networkCommissionAmt))
			return false;
		if (orderID != other.orderID)
			return false;
		return true;
	}
	
}
