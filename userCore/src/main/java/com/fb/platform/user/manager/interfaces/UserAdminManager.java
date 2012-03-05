/**
 * 
 */
package com.fb.platform.user.manager.interfaces;

import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.user.manager.model.UserTO;

/**
 * @author vinayak
 *
 */
@Transactional
public interface UserAdminManager {

	@Transactional(propagation=Propagation.SUPPORTS)
	public UserTO getUser(String key);

	@Transactional(propagation=Propagation.REQUIRED)
	public void addUser(UserTO user);

	@Transactional(propagation=Propagation.REQUIRED)
	public UserTO updateUser(UserTO user);

	@Transactional(propagation=Propagation.SUPPORTS)
	public Collection<UserTO> getUsers();

}
