package com.fb.platform.user.manager.interfaces;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
import com.fb.platform.user.manager.exception.AddressNotFoundException;
import com.fb.platform.user.manager.exception.UserNotFoundException;
import com.fb.platform.user.manager.model.address.DeleteAddressStatusEnum;
import com.fb.platform.user.manager.model.address.UpdateAddressStatusEnum;
import com.fb.platform.user.manager.model.address.UserAddress;

@Transactional
public interface UserAddressService {

	/**
	 * Returns the Address Associated with a Userid.
	 * @param userId
	 * @throws UserNotFoundException When no user is found.
	 * @throws AddressNotFoundException When no user address is found.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return List of User Address
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	List<UserAddress> getAddress(int userId) throws UserNotFoundException,
			AddressNotFoundException, PlatformException;
	
	/**
	 * Returns the Address Associated with a Addressid.
	 * @param addressId
	 * @throws AddressNotFoundException When no user address is found.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return User Address
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	UserAddress getAddress(long addressId) throws AddressNotFoundException, PlatformException;

	/**
	 * Add a new address for the user.
	 * @param userId
	 * @param userAddress 
	 * @throws UserNotFoundException When no user is found.
	 * @throws AddressNotFoundException When no user address is found.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return UserAddress
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	UserAddress addAddress(int userId, UserAddress userAddress)
			throws UserNotFoundException, AddressNotFoundException,
			PlatformException;

	/**
	 * Update an existing userAddress.
	 * @param userId
	 * @param userAddress
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return UpdateAddressStatusEnum
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	UpdateAddressStatusEnum updateAddress(int userId, UserAddress userAddress)
			throws PlatformException;

	/**
	 * Delete an existing userAddress.
	 * @param userId
	 * @param addressId
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return DeleteAddressStatusEnum
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	DeleteAddressStatusEnum deleteAddress(int userId, long addressId)
			throws PlatformException;
}
