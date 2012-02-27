/**
 * 
 */
package com.fb.platform.sso.dao;

import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;

/**
 * @author vinayak
 *
 */
public interface SingleSignonDao {

	/**
	 * Create the Single Signon session record .
	 * @param session - session details used to create the session
	 * @param sessionId - sessionId to be associated with this session
	 */
	public abstract void createSSOSession(SSOSessionTO session, SSOSessionId sessionId);

	/**
	 * remove the session from the database 
	 * @param sessionId
	 */
	public abstract void logoutSSOSession(SSOSessionId sessionId);

	/**
	 * Update the <code>lastUpdated</code> timestamp associated with this session
	 * @param sessionId
	 */
	public abstract void updateSSOSession(SSOSessionId sessionId);

	/**
	 * Load an active session record - A session record that has either timed out, or has been closed by the user
	 * will not be returned
	 * @param sessionId
	 * @return SSOSessionTO if sessionId identifies a current session, otherwise null
	 */
	public abstract SSOSessionTO loadSessionDetails(SSOSessionId sessionId);
}
