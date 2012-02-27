/**
 * 
 */
package com.fb.platform.sso;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
@Transactional
public interface SSOMasterService {

    /**
     * Authenticate a session token. 
     *
     * @param token
     * @return
     * @throws RemoteException
     * @throws SportexException
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public SSOSessionTO	authenticate(SSOSessionId sessionId) throws PlatformException;

	@Transactional(propagation=Propagation.REQUIRED)
    public SSOSessionTO	authenticateToken(java.lang.String sessionToken) throws PlatformException;

	@Transactional(propagation=Propagation.REQUIRED)
    public void keepAlive(SSOSessionId sessionId) throws PlatformException;

	@Transactional(propagation=Propagation.REQUIRED)
    public SSOSessionId createSSOSession(SSOSessionTO session) throws PlatformException;

	@Transactional(propagation=Propagation.REQUIRED)
    public void removeSSOSession(SSOSessionId sessionId) throws PlatformException;

	@Transactional(propagation=Propagation.REQUIRED)
	public SSOToken createSessionToken(SSOSessionId sessionId) throws PlatformException;
}
