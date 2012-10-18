package com.fb.commons.mom.to;

import java.io.Serializable;

public class AddressTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5117312460451747403L;
	
	private String firstName;
	private String lastName;
	private String middleName;
	private String address;
	private String city;
	private String pincode;
	private String state;
	private String country;
	private String primaryTelephone;
	private String secondaryTelephone;
	
	public String getFirstName() {
		return firstName;
	}
	public String getAddress() {
		return address.replaceAll("\r\n", "");
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPrimaryTelephone() {
		return primaryTelephone;
	}
	public void setPrimaryTelephone(String primaryTelephone) {
		this.primaryTelephone = primaryTelephone;
	}
	public String getSecondaryTelephone() {
		return secondaryTelephone;
	}
	public void setSecondaryTelephone(String secondaryTelephone) {
		this.secondaryTelephone = secondaryTelephone;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	@Override
	public String toString() {
		return "AddressTO [firstName=" + firstName + ", lastName=" + lastName
				+ ", middleName=" + middleName + ", address=" + address
				+ ", city=" + city + ", pincode=" + pincode + ", state="
				+ state + ", country=" + country + ", primaryTelephone="
				+ primaryTelephone + ", secondaryTelephone="
				+ secondaryTelephone + "]";
	}
	
}
