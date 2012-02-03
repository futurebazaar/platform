package com.fb.platform.user.dao.interfaces;

import java.util.Collection;

import com.fb.platform.user.domain.UserBo;


/**
 * @author kislaya
 *
 */ 
public interface UserDao {
	
	/**
	 * @param key elther the phone number or the emailid of the user
	 * @return the user object
	 */
	public UserBo load(String key);

	/**
	 * @return the complete list of users in the database
	 */
	public Collection<UserBo> getUsers();

	/**
	 * Add a new user
	 * @param userBo
	 */
	public void add(UserBo userBo);
	
	/**
	 * Update an existing user
	 * @param userBo
	 */
	public void update(UserBo userBo);
}
