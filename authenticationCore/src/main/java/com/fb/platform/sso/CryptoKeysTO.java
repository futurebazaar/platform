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
public class CryptoKeysTO implements Serializable {

	private Key _currentKey;
    private Key _oldKey;
    private Key _nextKey;
	
	
	public CryptoKeysTO(Key currentKey, Key oldKey) {
		_currentKey = currentKey;
		_oldKey = oldKey;
	}
	
	public CryptoKeysTO(String currentKey, String oldKey, String nextKey) {
		_currentKey = CryptoKeyUtils.fromExternalForm(currentKey);
        _nextKey = CryptoKeyUtils.fromExternalForm(nextKey);
        if (oldKey != null) {
            _oldKey = CryptoKeyUtils.fromExternalForm(oldKey);
        }
        else {
            _oldKey = null;
        }
	}

	public Key getCurrentKey() {
		return _currentKey;
	}

	public Key getOldKey() {
		return _oldKey;
	}

    public Key getNextKey() {
        return _nextKey;
    }
    
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(663570131, -318818185)
				.appendSuper(super.hashCode())
				.append(this._oldKey)
                .append(this._currentKey)
                .append(this._nextKey)
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof CryptoKeysTO)) {
			return false;
		}
		CryptoKeysTO rhs = (CryptoKeysTO) object;
		return new EqualsBuilder()
				.append(this._oldKey, rhs._oldKey)
                .append(this._currentKey, rhs._currentKey)
                .append(this._nextKey, rhs._nextKey)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("_oldKey", this._oldKey)
                .append("_currentKey", this._currentKey)
                .append("_nextKey", this._nextKey)
				.toString();
	}
}
