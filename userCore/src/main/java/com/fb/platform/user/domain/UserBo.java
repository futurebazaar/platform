package com.fb.platform.user.domain;

import java.util.Date;
import java.util.List;

public class UserBo {

	private int userid;
	private String password;
	private List<UserPhoneBo> userPhone;
	private List<UserEmailBo> userEmail;
	private String name;
	private String username;
	private String firstname;
	private String lastname;
	private String gender;
	private String salutation;
	private Date dateofbirth;
	private boolean isActive;

	/**
	 * @return the userid
	 */
	public int getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(int userid) {
		this.userid = userid;
	}

	/**
	 * @return the passwd
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
	
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateofbirth == null) ? 0 : dateofbirth.hashCode());
		result = prime * result
				+ ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result
				+ ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((salutation == null) ? 0 : salutation.hashCode());
		result = prime * result
				+ ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result
				+ ((userPhone == null) ? 0 : userPhone.hashCode());
		result = prime * result + userid;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserBo other = (UserBo) obj;
		if (dateofbirth == null) {
			if (other.dateofbirth != null) {
				return false;
			}
		} else if (!dateofbirth.equals(other.dateofbirth)) {
			return false;
		}
		if (firstname == null) {
			if (other.firstname != null) {
				return false;
			}
		} else if (!firstname.equals(other.firstname)) {
			return false;
		}
		if (gender == null) {
			if (other.gender != null) {
				return false;
			}
		} else if (!gender.equals(other.gender)) {
			return false;
		}
		if (lastname == null) {
			if (other.lastname != null) {
				return false;
			}
		} else if (!lastname.equals(other.lastname)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (salutation == null) {
			if (other.salutation != null) {
				return false;
			}
		} else if (!salutation.equals(other.salutation)) {
			return false;
		}
		if (userEmail == null) {
			if (other.userEmail != null) {
				return false;
			}
		} else if (!userEmail.equals(other.userEmail)) {
			return false;
		}
		if (userPhone == null) {
			if (other.userPhone != null) {
				return false;
			}
		} else if (!userPhone.equals(other.userPhone)) {
			return false;
		}
		if (userid != other.userid) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserBo [userid=");
		builder.append(userid);
		builder.append(", password=");
		builder.append(password);
		builder.append(", userPhone=");
		builder.append(userPhone);
		builder.append(", userEmail=");
		builder.append(userEmail);
		builder.append(", name=");
		builder.append(name);
		builder.append(", username=");
		builder.append(username);
		builder.append(", firstname=");
		builder.append(firstname);
		builder.append(", lastname=");
		builder.append(lastname);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", salutation=");
		builder.append(salutation);
		builder.append(", dateofbirth=");
		builder.append(dateofbirth);
		builder.append(", is_active=");
		builder.append(isActive);
		builder.append("]");
		return builder.toString();
	}

}
