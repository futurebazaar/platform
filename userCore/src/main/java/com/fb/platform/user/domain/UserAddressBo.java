package com.fb.platform.user.domain;

/**
 * @author kumar
 *
 */
public class UserAddressBo {
	
	long userid;
	long addressid;
	String addresstype;
	String address;
	String pincode;
	String city;
	String state;
	String country;
	/**
	 * @return the userid
	 */
	public long getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(long userid) {
		this.userid = userid;
	}
	
	
	/**
	 * @return the addressid
	 */
	public long getAddressid() {
		return addressid;
	}
	/**
	 * @param addressid the addressid to set
	 */
	public void setAddressid(long addressid) {
		this.addressid = addressid;
	}
	/**
	 * @return the addresstype
	 */
	public String getAddresstype() {
		return addresstype;
	}
	/**
	 * @param addresstype the addresstype to set
	 */
	public void setAddresstype(String addresstype) {
		this.addresstype = addresstype;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the pincode
	 */
	public String getPincode() {
		return pincode;
	}
	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
	

}
