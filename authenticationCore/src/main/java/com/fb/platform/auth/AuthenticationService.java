/**
 * 
 */
package com.fb.platform.auth;

import java.rmi.RemoteException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
@Transactional
public interface AuthenticationService {

    /**
     * Authenticate a session token. Token is passed as a parameter
     * 
     * @param token
     * @return
     * @throws RemoteException
     * @throws PlatformException
     */
    @Transactional(propagation=Propagation.SUPPORTS)
    public AuthenticationTO	 authenticate(String token) throws PlatformException;

}
