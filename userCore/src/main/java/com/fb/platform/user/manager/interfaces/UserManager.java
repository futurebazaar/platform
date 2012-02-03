package com.fb.platform.user.manager.interfaces;

import com.fb.platform.user.manager.model.UserTO;



/**
 * @author kumar
 *
 */
public interface UserManager {
	/**
	 * @param key
	 * @return
	 */
	UserTO getuser(String key);
}
