package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.user.dao.interfaces.UserAddressDao;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserAddressBo;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.exception.AddressNotFoundException;
import com.fb.platform.user.manager.exception.UserNotFoundException;
import com.fb.platform.user.manager.interfaces.UserAddressService;
import com.fb.platform.user.manager.model.address.DeleteAddressStatusEnum;
import com.fb.platform.user.manager.model.address.UpdateAddressStatusEnum;
import com.fb.platform.user.manager.model.address.UserAddress;

public class UserAddressServiceImpl implements UserAddressService {
	@Autowired
	private UserAdminDao userAdminDao = null;

	@Autowired
	private UserAddressDao userAddressDao = null;

	@Override
	public List<UserAddress> getAddress(int userId)
			throws UserNotFoundException, AddressNotFoundException,
			PlatformException {
		try {
			UserBo user = userAdminDao.loadByUserId(userId);
			if (user == null) {
				throw new UserNotFoundException();
			}
			Collection<UserAddressBo> userAddressBos = userAddressDao.load(userId);
			if(userAddressBos != null){
				if(userAddressBos.size() >0 ){
					List<UserAddress> userAddressLst = new ArrayList<UserAddress>();
					Iterator<UserAddressBo> itr = userAddressBos.iterator();
					while (itr.hasNext()){
						UserAddressBo userAddressBo = itr.next();
						UserAddress userAddress =  new UserAddress();
						userAddress.setAddress(userAddressBo.getAddress());
						userAddress.setAddressId(userAddressBo.getAddressid());
						userAddress.setAddressType(userAddressBo.getAddresstype());
						userAddress.setCity(userAddressBo.getCity());
						userAddress.setState(userAddressBo.getState());
						userAddress.setCountry(userAddressBo.getCountry());
						userAddress.setPinCode(userAddressBo.getPincode());
						userAddressLst.add(userAddress);
					}
					return userAddressLst;
				}else{
					throw new AddressNotFoundException();
				}
			}else{
				throw new AddressNotFoundException();
			}
		}catch(UserNotFoundException e){
			throw new UserNotFoundException("No user is found with this userid::" + userId);
		}catch(AddressNotFoundException e){
			throw new AddressNotFoundException("No address is associated with this userid::" + userId);
		}catch(PlatformException e){
			throw new PlatformException("Something went wrong while retriving addressed");
		}		
	}

	/**
	 * @return the userAdminDao
	 */
	public UserAdminDao getUserAdminDao() {
		return userAdminDao;
	}

	/**
	 * @param userAdminDao the userAdminDao to set
	 */
	public void setUserAdminDao(UserAdminDao userAdminDao) {
		this.userAdminDao = userAdminDao;
	}

	/**
	 * @return the userAddressDao
	 */
	public UserAddressDao getUserAddressDao() {
		return userAddressDao;
	}

	/**
	 * @param userAddressDao the userAddressDao to set
	 */
	public void setUserAddressDao(UserAddressDao userAddressDao) {
		this.userAddressDao = userAddressDao;
	}

	@Override
	public UserAddress addAddress(int userId, UserAddress userAddress)
			throws UserNotFoundException, AddressNotFoundException,
			PlatformException {
		try{
			UserBo user = userAdminDao.loadByUserId(userId);
			if (user == null) {
				throw new UserNotFoundException();
			}
			if(StringUtils.isBlank(userAddress.getAddress())){
				throw new AddressNotFoundException();
			}
			UserAddressBo userAddressBo = new UserAddressBo();
			userAddressBo.setAddress(userAddress.getAddress());
			userAddressBo.setAddresstype(userAddress.getAddressType());
			userAddressBo.setCity(userAddress.getCity());
			userAddressBo.setState(userAddress.getState());
			userAddressBo.setCountry(userAddress.getCountry());
			userAddressBo.setPincode(userAddress.getPinCode());
			userAddressBo.setUserid(userId);
			UserAddressBo userAddressBoAdd = userAddressDao.add(userAddressBo);
			
			UserAddress userAddressResp = new UserAddress();
			userAddressResp.setAddressId(userAddressBoAdd.getAddressid());
			return userAddressResp;
		}catch(UserNotFoundException e){
			throw new UserNotFoundException("User does not exists with the provided userid");
		}catch (AddressNotFoundException e) {
			throw new AddressNotFoundException("No address to be added");
		}catch (PlatformException e) {
			throw new PlatformException("Error performing add address at this time");
		}
	}

	@Override
	public UpdateAddressStatusEnum updateAddress(int userId,
			UserAddress userAddress) throws PlatformException {
		try{
			if (userAddress == null) {
				return UpdateAddressStatusEnum.ERROR_UPDATING_ADDRESS;
			}
			if (userAddress.getAddressId() <= 0) {				
				return UpdateAddressStatusEnum.ADDRESSID_ABSENT;
			}
			UserBo user = userAdminDao.loadByUserId(userId);
			if (user == null) {
				return UpdateAddressStatusEnum.INVALID_USER;
			}
			UserAddressBo userAddressBo = userAddressDao.getAddressById(userAddress.getAddressId());
			if (userAddressBo.getUserid() != userId){
				return UpdateAddressStatusEnum.USER_ADDRESSID_MISMATCH;
			}
			if(!StringUtils.isBlank(userAddress.getAddress())){
				userAddressBo.setAddress(userAddress.getAddress());
			}
			if(!StringUtils.isBlank(userAddress.getAddressType())){
				userAddressBo.setAddresstype(userAddress.getAddressType());
			}
			if(!StringUtils.isBlank(userAddress.getCity())){
				userAddressBo.setCity(userAddress.getCity());
			}
			if(!StringUtils.isBlank(userAddress.getState())){
				userAddressBo.setState(userAddress.getState());
			}
			if(!StringUtils.isBlank(userAddress.getCountry())){
				userAddressBo.setCountry(userAddress.getCountry());
			}
			if(!StringUtils.isBlank(userAddress.getPinCode())){
				userAddressBo.setPincode(userAddress.getPinCode());
			}			
			userAddressDao.update(userAddressBo);
			return UpdateAddressStatusEnum.SUCCESS;			
		}catch(PlatformException pe){
			throw new PlatformException("Error updating user address"); 
		}catch(Exception e){
			throw new PlatformException("Error updating user address"); 
		}
	}

	@Override
	public DeleteAddressStatusEnum deleteAddress(int userId, long addressId)
			throws PlatformException {
		try {
			if (addressId <= 0) {
				return DeleteAddressStatusEnum.ADDRESSID_ABSENT;
			}
			UserBo user = userAdminDao.loadByUserId(userId);
			if (user == null) {
				return DeleteAddressStatusEnum.INVALID_USER;
			}
			UserAddressBo userAddressBo = userAddressDao.getAddressById(addressId);
			if(userAddressBo == null){
				return DeleteAddressStatusEnum.ADDRESSID_ABSENT;
			}
			if (userAddressBo.getUserid() != userId){
				return DeleteAddressStatusEnum.USER_ADDRESSID_MISMATCH;
			}
			
			boolean success = userAddressDao.deleteAddressById(addressId);
			if (success){
				return DeleteAddressStatusEnum.SUCCESS;
			}else{
				return DeleteAddressStatusEnum.ERROR_DELETING_ADDRESS;
			}			
		} catch (PlatformException e) {
			throw new PlatformException("Errror deleting address");
		}catch (Exception e) {
			throw new PlatformException("Errror deleting address");
		}
	}
	
	
}
