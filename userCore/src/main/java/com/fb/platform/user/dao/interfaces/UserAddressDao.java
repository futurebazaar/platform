package com.fb.platform.user.dao.interfaces;

import java.util.Collection;

import com.fb.platform.user.domain.UserAddressBo;

/**
 * @author kumar
 *
 */
public interface UserAddressDao {

	/**
	 * @param int userid
	 * @return collection of address beloging to the user
	 */
	public Collection<UserAddressBo> load(int userid);

	/**
	 * Adds an address to the user
	 * @param userAddressBo
	 * @throws Exception
	 */
	public UserAddressBo add(UserAddressBo userAddressBo);

	/**
	 * updates a users address
	 * @param userAddressBo
	 */
	public UserAddressBo update(UserAddressBo userAddressBo);
	
	public boolean deleteAddressById(long addressId);
	
	public UserAddressBo getAddressById(long addressId);
}
