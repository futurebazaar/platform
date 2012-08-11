package com.fb.platform.user.manager.interfaces;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.exception.EmailNotFoundException;
import com.fb.platform.user.manager.exception.InvalidUserNameException;
import com.fb.platform.user.manager.exception.PhoneNotFoundException;
import com.fb.platform.user.manager.exception.UserAlreadyExistsException;
import com.fb.platform.user.manager.exception.UserNotFoundException;
import com.fb.platform.user.manager.model.admin.UpdateUserStatusEnum;
import com.fb.platform.user.manager.model.admin.User;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.UserEmail;
import com.fb.platform.user.manager.model.admin.email.VerifyUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.UserPhone;
import com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneStatusEnum;

@Transactional
public interface UserAdminService {

	/**
	 * Returns the User associated with a key(User Name,EmailId,Phone or UserId).
	 * @param key
	 * @throws UserNotFoundException When no user is found matching the key.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return User
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	User getUser(String key) throws UserNotFoundException,PlatformException;
	
	/**
	 * Adds the User associated with a key(mailId,Phone).
	 * @param User
	 * @throws UserAlreadyExistsException When  user is already present.
	 * @throws InvalidUserNameException when User Name is neither email nor phone.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return User
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	User addUser(User user) throws UserAlreadyExistsException,InvalidUserNameException,PlatformException;
	
	/**
	 * Update the User..
	 * @param User
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return UpdateUserStatusEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	UpdateUserStatusEnum updateUser(User user) throws UserNotFoundException,PlatformException;
	
	/**
	 * Get a User Emailids.
	 * @param userId
	 * @throws UserNotFoundException when user is absent.
	 * @throws EmailNotFoundException when the is no emailid for the user.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return List of User Email 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	List<UserEmail> getUserEmail(int userId) throws UserNotFoundException,EmailNotFoundException,PlatformException;
	
	/**
	 * Add an Email ID for User.
	 * @param userId
	 * @param userEmail
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return AddUserEmailStatusEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	AddUserEmailStatusEnum addUserEmail(int userId,UserEmail userEmail) throws UserNotFoundException,PlatformException;
	
	/**
	 * Verify an Emailid for User.
	 * @param userId
	 * @param userEmail
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return VerifyUserEmailStatusEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	VerifyUserEmailStatusEnum verifyUserEmail(int userId,String userEmail) throws PlatformException;
	
	/**
	 * Delete an Email ID for User.
	 * @param userId
	 * @param emailId
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return DeleteUserEmailStatusEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	DeleteUserEmailStatusEnum deleteUserEmail(int userId,String emailId) throws PlatformException;
	
	/**
	 * Get a User Phone.
	 * @param userId
	 * @throws UserNotFoundException when user is absent.
	 * @throws PhoneNotFoundException when the is no phone for the user.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return List of User Phone 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	List<UserPhone> getUserPhone(int userId) throws UserNotFoundException,PhoneNotFoundException,PlatformException;
	
	/**
	 * Add an Phone for User.
	 * @param userId
	 * @param userPhone
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return AddUserPhoneStatusEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	AddUserPhoneStatusEnum addUserPhone(int userId,UserPhone userPhone) throws UserNotFoundException,PlatformException;
	
	/**
	 * Verify an Phone for User.
	 * @param userId
	 * @param userPhone
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return VerifyUserPhoneStatusEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	VerifyUserPhoneStatusEnum verifyUserPhone(int userId,String userPhone) throws PlatformException;
	
	/**
	 * Delete an Phone for User.
	 * @param userId
	 * @param phone
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return DeleteUserPhoneStatusEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	DeleteUserPhoneStatusEnum deleteUserPhone(int userId,String phone) throws PlatformException;

	@Transactional(propagation = Propagation.SUPPORTS)
	public UserBo getUserByUserId(int userId);
}
