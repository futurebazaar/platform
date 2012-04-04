/**
 * 
 */
package com.fb.platform.auth.impl;

import java.security.Key;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.sso.CryptoKeyManager;
import com.fb.platform.sso.CryptoKeysTO;
import com.fb.platform.sso.SSOMasterService;
import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;
import com.fb.platform.sso.SSOToken;
import com.fb.platform.sso.caching.SessionCacheAccess;
import com.fb.platform.sso.caching.SessionTokenCacheAccess;

/**
 * @author vinayak
 *
 */
public class AuthenticationServiceImpl implements AuthenticationService {

	private static Log logger = LogFactory.getLog(AuthenticationServiceImpl.class);

	private CryptoKeyManager cryptoKeyManager = null;
	private SSOMasterService ssoMasterService = null;

	@Autowired
	private SessionTokenCacheAccess sessionTokenCacheAccess = null;
	@Autowired
	private SessionCacheAccess sessionCacheAccess = null;

	/* (non-Javadoc)
	 * @see com.fb.platform.auth.AuthenticationService#authenticate(java.lang.String)
	 */
	@Override
	public AuthenticationTO authenticate(String token) throws PlatformException {
    	if (logger.isDebugEnabled()) {
    		//logger.debug(Log.entry("authenticate ").append(token));
    	}
    	if (token == null) {
    		return null;
    	}

    	boolean tokenCached = true;
        
    	CryptoKeysTO keys = cryptoKeyManager.loadKeys();

        if (keys == null) {
            logger.error("CryptoKeys not available - unable to validate session token");
            return null;
        }
    	
        Key currentKey = keys.getCurrentKey();        
        SSOToken ssoToken = new SSOToken(token, currentKey);  

        // Check the token cache for the session id
        SSOSessionId sessionId = sessionTokenCacheAccess.get(ssoToken);
        
        // If session Id is null, token is not cached, try decrypting
        if (sessionId == null) {
            tokenCached = false;
    		sessionId = ssoToken.getSessionId(keys);
        }
        
        // check if we been unable to decrypt the token
        if (sessionId == null || sessionId.isValid() == false) {
            if (logger.isDebugEnabled())
                logger.debug("unable to validate session token");
            return null;
        }
        
        // retrieve the session information from the cache
        SSOSessionTO session = null;
        session = sessionCacheAccess.get(sessionId);

        // no cache hit - ask the master server
        if (session == null) {
            try {
                session = ssoMasterService.authenticate(sessionId);
                if (session != null) {
                    try {
                        sessionCacheAccess.lock(sessionId);
                        if (sessionCacheAccess.get(sessionId) == null) {
                            sessionCacheAccess.put(sessionId, session);
                        }
                    } finally {
                        sessionCacheAccess.unlock(sessionId);
                    }
                }
            } 
            catch (PlatformException e) {
                logger.error("Unable to authenticate with sso master", e);
            } 
        }
        
        // unable to authenticate
        if (session == null) {
            logger.debug("All authentication attempts failed");
            return null;
        }
        
        AuthenticationTO authTO = null;
    	authTO = new AuthenticationTO();
    	authTO.setUserID(session.getUserId());
        authTO.setToken(token);
        authTO.setAppData(session.getAppData());
        authTO.setSessionId(sessionId);

        // keys have changed - re-encrypt and store in cache
        if (tokenCached == false) {
            SSOToken newToken = sessionId.getSessionToken(keys);
            authTO.setToken(newToken.getToken());
            try {
                sessionTokenCacheAccess.lock(newToken);
                if (sessionTokenCacheAccess.get(newToken) == null) {
                    sessionTokenCacheAccess.put(newToken,sessionId);
                }
            } finally {
                sessionTokenCacheAccess.unlock(newToken);
            }
        }
        
        if (logger.isDebugEnabled()) {
        	//logger.debug("authenticate ").append(authTO == null ? "null" : authTO.toString());
        }
        
        return authTO;
	}

	public CryptoKeyManager getCryptoKeyManager() {
		return cryptoKeyManager;
	}

	public void setCryptoKeyManager(CryptoKeyManager cryptoKeyManager) {
		this.cryptoKeyManager = cryptoKeyManager;
	}

	public SSOMasterService getSsoMasterService() {
		return ssoMasterService;
	}

	public void setSsoMasterService(SSOMasterService ssoMasterService) {
		this.ssoMasterService = ssoMasterService;
	}
}
