package com.fb.platform.user;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserModel {
	
	 	private String firstname;
	 	private String lastname;
	    private String username; // either phone or emailid as when the user signs up ot signs in
	    private String phonnumber;
	    private String email;
	    private String password;
	    private String newpassword;
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
		 * @return the phonnumber
		 */
		public String getPhonnumber() {
			return phonnumber;
		}
		/**
		 * @param phonnumber the phonnumber to set
		 */
		public void setPhonnumber(String phonnumber) {
			this.phonnumber = phonnumber;
		}
		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}
		/**
		 * @param email the email to set
		 */
		public void setEmail(String email) {
			this.email = email;
		}
		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}
		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}
		/**
		 * @return the newpassword
		 */
		public String getNewpassword() {
			return newpassword;
		}
		/**
		 * @param newpassword the newpassword to set
		 */
		public void setNewpassword(String newpassword) {
			this.newpassword = newpassword;
		}
	    
	    
	    
	 
	    

}
