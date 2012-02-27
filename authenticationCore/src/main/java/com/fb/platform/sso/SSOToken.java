/**
 * 
 */
package com.fb.platform.sso;

import java.io.Serializable;
import java.security.Key;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fb.platform.sso.util.CryptoKeyUtils;

/**
 * @author vinayak
 *
 */
public class SSOToken implements Serializable {

	private String _token;
	private Key _key;
	
    public SSOToken() {};
    
	public SSOToken(SSOSessionId sessionId, CryptoKeysTO keys) {
		this(CryptoKeyUtils.encrypt(sessionId.getSessionId(), keys), keys.getCurrentKey());
	}
	
	public SSOToken(String token, Key key) {
		_token = token;
		_key = key;
	}

	public String getToken() {
		return _token;
	}
	
	public Key getKey() {
		return _key;
	}
	
	public SSOSessionId getSessionId(CryptoKeysTO keys) {
		return new SSOSessionId(CryptoKeyUtils.decrypt(_token, keys));
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("_token", this._token)
				.append("_key", this._key)
				.toString();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof SSOToken)) {
			return false;
		}
		SSOToken rhs = (SSOToken) object;
		return new EqualsBuilder()
				.append(this._token, rhs._token)
				.append(this._key, rhs._key)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(932261761, -797746513)
				.append(this._token)
				.append(this._key)
				.toHashCode();
	}
}
