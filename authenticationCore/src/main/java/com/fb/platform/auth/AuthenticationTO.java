/**
 * 
 */
package com.fb.platform.auth;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fb.commons.to.LoginType;
import com.fb.platform.sso.SSOSessionAppData;
import com.fb.platform.sso.SSOSessionId;

/**
 * @author vinayak
 *
 */
public class AuthenticationTO implements Serializable {

    private int userID = -1;
    private String token;
    private String appData;
    private SSOSessionId sessionId = null;
    private LoginType loginType = null;
    
    /**
     * Get the authentication token to be used for the next time.
     * The reason for returning a new authentication token is that
     * the encryption key may have changed, in which case the token
     * will be re-encrypted using the new key.
     * 
     * @return String encrypted token
     */
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * Get the user id associated with the authenticated token
     * 
     * @return user id
     */
    public int getUserID() {
        return userID;
    }
    
    public void setUserID(int userID) {
        this.userID = userID;
    }

	public String getAppData() {
		return appData;
	}

	public void setAppData(String appData) {
		this.appData = appData;
		SSOSessionAppData sessionData = SSOSessionAppData.parse(appData);
		if (sessionData != null) {
			this.loginType = sessionData.getLoginType();
		}
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof AuthenticationTO)) {
			return false;
		}
		AuthenticationTO rhs = (AuthenticationTO) object;
		return new EqualsBuilder()
				.appendSuper(super.equals(object))
				.append(this.userID, rhs.userID)
				.append(this.token, rhs.token)
			.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(1331189241, 1539790263)
				.appendSuper(super.hashCode())
				.append(this.userID)
				.append(this.token)
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("userID", this.userID)
				.append("token", this.token)
				.toString();
	}

    public void setSessionId(SSOSessionId sessionId) {
        this.sessionId = sessionId;
    }

    public SSOSessionId getSessionId() {
        return sessionId;
    }

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}
}
