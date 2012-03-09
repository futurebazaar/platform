/**
 * 
 */
package com.fb.platform.sso;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fb.commons.PlatformException;
import com.fb.platform.sso.util.CryptoKeyUtils;

/**
 * @author vinayak
 *
 */
public class SSOSessionId implements Serializable {

	private static final Pattern VALID_SESSION_CHARS = Pattern.compile("[0-9A-Fa-f/-]+");

	private String _sessionId = null;
	
    /**
     * Added to make glue work !
     */
    public SSOSessionId() {}
    
	public SSOSessionId(String sessionId) {
		_sessionId = sessionId;
	}
	
	
	public SSOSessionId(SSOToken sessionToken, CryptoKeysTO keys) {
		this(CryptoKeyUtils.decrypt(sessionToken.getToken(), keys));
	}

	public String getSessionId() {
		return _sessionId;
	}
	
	public boolean isValid() {
		return _sessionId != null && VALID_SESSION_CHARS.matcher(_sessionId).matches();
	}
	
	public SSOToken getSessionToken(CryptoKeysTO keys) {
		return new SSOToken(CryptoKeyUtils.encrypt(_sessionId, keys), keys.getCurrentKey());
	}
	
	public byte[] getBytes() {
		try {
			return _sessionId.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new PlatformException("Exception obtaining bytes from SSOSessionId",e);
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-1216362783, -1611747167).append(this._sessionId).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("_sessionId", this._sessionId)
				.toString();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof SSOSessionId)) {
			return false;
		}
		SSOSessionId rhs = (SSOSessionId) object;
		return new EqualsBuilder()
				.append(this._sessionId, rhs._sessionId)
				.isEquals();
	}
}
