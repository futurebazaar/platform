package com.fb.platform.user.domain;

import java.util.Date;
import java.util.List;

public class UserBo {
	
	private long userid;
	private String password;
	private List<UserPhoneBo> userPhone;
	private List<UserEmailBo> userEmail;
	private String name;
	private String username;
	private String firstname ;
	private String lastname;
	private String gender;
	private String salutation;
	private Date dateofbirth;
	
	
	
	
	

	
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
	 * @return the passwd
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param passwd the passwd to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the userPhone
	 */
	public List<UserPhoneBo> getUserPhone() {
		return userPhone;
	}
	/**
	 * @param userPhone the userPhone to set
	 */
	public void setUserPhone(List<UserPhoneBo> userPhone) {
		this.userPhone = userPhone;
	}
	/**
	 * @return the userEmail
	 */
	public List<UserEmailBo> getUserEmail() {
		return userEmail;
	}
	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(List<UserEmailBo> userEmail) {
		this.userEmail = userEmail;
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the dateofbirth
	 */
	public Date getDateofbirth() {
		return dateofbirth;
	}
	/**
	 * @param dateofbirth the dateofbirth to set
	 */
	public void setDateofbirth(Date dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	/**
	 * @return the salutation
	 */
	public String getSalutation() {
		return salutation;
	}
	/**
	 * @param salutation the salutation to set
	 */
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	
	
	
	
	
	
	
}
