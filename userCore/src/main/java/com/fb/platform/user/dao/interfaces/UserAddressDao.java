package com.fb.platform.user.dao.interfaces;

import java.util.Collection;

import com.fb.platform.user.domain.UserAddressBo;

/**
 * @author kumar
 *
 */
public interface UserAddressDao {
	
	/**
	 * @param userid
	 * @return collection of address beloging to the user
	 */
	Collection<UserAddressBo> load(int userid);

	/**
	 * Adds an address to the user
	 * @param userAddressBo
	 * @throws Exception
	 */
	void add(UserAddressBo userAddressBo);
	
	/**
	 * updates a users address
	 * @param userAddressBo
	 */
	void update(UserAddressBo userAddressBo);

}
