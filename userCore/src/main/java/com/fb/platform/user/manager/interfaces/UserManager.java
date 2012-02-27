package com.fb.platform.user.manager.interfaces;

import java.util.Collection;

import com.fb.platform.user.domain.UserBo;
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
	
	Collection<UserTO> getUsers();
	
	void adduser(UserTO userTO);
	
	UserTO updateuser (UserTO userTO);

	public UserTO login(String username , String password);
	
	public UserTO logout(UserTO userTO);
	
	public UserTO  changepassword(UserTO userTO,String newpassword);
	
}
