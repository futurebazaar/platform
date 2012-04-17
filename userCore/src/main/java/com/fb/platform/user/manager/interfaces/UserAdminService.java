package com.fb.platform.user.manager.interfaces;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.exception.UserNotFoundException;

@Transactional
public interface UserAdminService {

	/**
	 * Returns the User associated with a key(Username,EmailId,Phone or UserId).
	 * @param couponCode
	 * @throws UserNotFoundException When no user is found matching the key.
	 * @throws PlatformException When an unrecovarable error happens.
	 * @return UserBo
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	UserBo getUser(String key) throws UserNotFoundException,PlatformException;

	
}
