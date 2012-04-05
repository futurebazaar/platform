/**
 * 
 */
package com.fb.platform.sso.impl;

import java.security.Key;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.sso.CryptoKeyManager;
import com.fb.platform.sso.CryptoKeysTO;
import com.fb.platform.sso.SSOMasterService;
import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;
import com.fb.platform.sso.SSOToken;
import com.fb.platform.sso.caching.SessionCacheAccess;
import com.fb.platform.sso.caching.SessionTokenCacheAccess;
import com.fb.platform.sso.dao.SingleSignonDao;

/**
 * @author vinayak
 *
 */
public class SSOMasterServiceImpl implements SSOMasterService {

	private Log log = LogFactory.getLog(SSOMasterServiceImpl.class);

	private SingleSignonDao singleSignonDao = null;
	private CryptoKeyManager cryptoKeyManager = null;

	@Autowired
	private SessionTokenCacheAccess sessionTokenCacheAccess = null;

	@Autowired
	private SessionCacheAccess sessionCacheAccess = null;

	/* (non-Javadoc)
	 * @see com.fb.platform.sso.SSOMasterService#authenticate(com.fb.platform.sso.SSOSessionId)
	 */
	@Override
	public SSOSessionTO authenticate(SSOSessionId sessionId) throws PlatformException {
		if(log.isDebugEnabled()) {
			log.debug("Authenticate session : " + sessionId.getSessionId());
		}
		return singleSignonDao.loadSessionDetails(sessionId);
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sso.SSOMasterService#authenticateToken(java.lang.String)
	 */
	@Override
	public SSOSessionTO authenticateToken(String sessionToken) throws PlatformException {
		if(log.isDebugEnabled()) {
			log.debug("Authenticate token : " + sessionToken);
		}
    	boolean tokenCached = true;

    	CryptoKeysTO keys = cryptoKeyManager.loadKeys();
        if (keys == null) {
            log.error("CryptoKeys not available - unable to validate session token");
            return null;
        }

        Key currentKey = keys.getCurrentKey();
        SSOToken ssoToken = new SSOToken(sessionToken, currentKey);

        // Check the token cache for the session id
        SSOSessionId sessionId = sessionTokenCacheAccess.get(ssoToken);

        // If session Id is null, token is not cached, try decrypting
        if (sessionId == null) {
            tokenCached = false;
    		sessionId = ssoToken.getSessionId(keys);
        }

        // check if we been unable to decrypt the token
        if (sessionId == null || sessionId.isValid() == false) {
            if (log.isDebugEnabled()) {
            	log.debug("unable to validate session token");
            }
            return null;
        }

        // retrieve the session information from the cache
        SSOSessionTO session = sessionCacheAccess.get(sessionId);

        // no cache hit - ask the master copy
        if (session == null) {
            try {
                session = this.authenticate(sessionId);
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
                log.error("Unable to authenticate with sso master", e);
            }
        }

        // unable to authenticate
        if (session == null) {
            log.debug("All authentication attempts failed");
            return null;
        }

        // keys have changed - re-encrypt and store in cache
        if (tokenCached == false) {
            SSOToken newToken = sessionId.getSessionToken(keys);
            try {
                sessionTokenCacheAccess.lock(newToken);
                if (sessionTokenCacheAccess.get(newToken) == null) {
                    sessionTokenCacheAccess.put(newToken, sessionId);
                }
            } finally {
                sessionTokenCacheAccess.unlock(newToken);
            }
        }

        return session;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sso.SSOMasterService#keepAlive(com.fb.platform.sso.SSOSessionId)
	 */
	@Override
	public void keepAlive(SSOSessionId sessionId) throws PlatformException {
		singleSignonDao.updateSSOSession(sessionId);
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sso.SSOMasterService#createSSOSession(com.fb.platform.sso.SSOSessionTO)
	 */
	@Override
	public SSOSessionId createSSOSession(SSOSessionTO session) throws PlatformException {
		if(log.isDebugEnabled()) {
			log.debug("Create session for user id : " + session.getUserId());
		}
		UUID uuid = UUID.randomUUID();
		SSOSessionId sessionId = new SSOSessionId(uuid.toString());
		singleSignonDao.createSSOSession(session, sessionId);
		return sessionId;
	}

	@Override
	public SSOToken createSessionToken(SSOSessionId sessionId) throws PlatformException {
		if(log.isDebugEnabled()) {
			log.debug("Create session token : " + sessionId.getSessionId());
		}
		return sessionId.getSessionToken(cryptoKeyManager.loadKeys());
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sso.SSOMasterService#removeSSOSession(com.fb.platform.sso.SSOSessionId)
	 */
	@Override
	public void removeSSOSession(SSOSessionId sessionId) throws PlatformException {
		if(log.isDebugEnabled()) {
			log.debug("Remove SSO session : " + sessionId.getSessionId());
		}
		singleSignonDao.logoutSSOSession(sessionId);
		sessionCacheAccess.remove(sessionId);
	}

	public SingleSignonDao getSingleSignonDao() {
		return singleSignonDao;
	}

	public void setSingleSignonDao(SingleSignonDao singleSignonDao) {
		this.singleSignonDao = singleSignonDao;
	}

	public CryptoKeyManager getCryptoKeyManager() {
		return cryptoKeyManager;
	}

	public void setCryptoKeyManager(CryptoKeyManager cryptoKeyManager) {
		this.cryptoKeyManager = cryptoKeyManager;
	}

}
