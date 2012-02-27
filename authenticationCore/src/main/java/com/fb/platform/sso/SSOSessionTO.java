/**
 * 
 */
package com.fb.platform.sso;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author vinayak
 *
 */
public class SSOSessionTO implements Serializable {

	private int _userId;
	private String _ipAddress;
	private String _appData;

    public SSOSessionTO() {};
    
	public SSOSessionTO(int userId) {
		_userId = userId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("_appData", this._appData)
				.append("_userId", this._userId)
				.append("_ipAddress", this._ipAddress)
				.toString();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof SSOSessionTO)) {
			return false;
		}
		SSOSessionTO rhs = (SSOSessionTO) object;
		return new EqualsBuilder()
				.appendSuper(super.equals(object))
				.append(this._appData, rhs._appData)
				.append(this._userId, rhs._userId)
				.append(this._ipAddress, rhs._ipAddress)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-812474131, 1815867517)
				.appendSuper(super.hashCode())
				.append(this._appData)
				.append(this._userId)
				.append(this._ipAddress)
				.toHashCode();
	}

	public String getAppData() {
		return _appData;
	}

	public void setAppData(String appData) {
		_appData = appData;
	}

	public String getIpAddress() {
		return _ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		_ipAddress = ipAddress;
	}

	public int getUserId() {
		return _userId;
	}

	public void setUserId(int userId) {
		_userId = userId;
	}

}
