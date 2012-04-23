package com.fb.platform.user.manager.interfaces;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
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
	@Transactional(propagation = Propagation.SUPPORTS)
	User getUser(String key) throws UserNotFoundException,PlatformException;
	
	/**
	 * Adds the User associated with a key(mailId,Phone).
	 * @param User
	 * @throws UserAlreadyExistsException When  user is already present.
	 * @throws InvalidUserNameException when User Name is neither email nor phone.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return User
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	User addUser(User user) throws UserAlreadyExistsException,InvalidUserNameException,PlatformException;
	
	/**
	 * Update the User..
	 * @param User
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return UpdateUserStatusEnum
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	UpdateUserStatusEnum updateUser(User user) throws UserNotFoundException,PlatformException;
	
	/**
	 * Get a User Emailids.
	 * @param userId
	 * @throws UserNotFoundException when user is absent.
	 * @throws EmailNotFoundException when the is no emailid for the user.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return List of User Email 
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	List<UserEmail> getUserEmail(int userId) throws UserNotFoundException,EmailNotFoundException,PlatformException;
	
	/**
	 * Add an Email ID for User.
	 * @param userId
	 * @param userEmail
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return AddUserEmailStatusEnum
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	AddUserEmailStatusEnum addUserEmail(int userId,UserEmail userEmail) throws UserNotFoundException,PlatformException;
	
	/**
	 * Verify an Emailid for User.
	 * @param userId
	 * @param userEmail
	 * @param verificationCode
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return VerifyUserEmailStatusEnum
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	VerifyUserEmailStatusEnum verifyUserEmail(int userId,String userEmail,String verificationCode) throws PlatformException;
	
	/**
	 * Delete an Email ID for User.
	 * @param userId
	 * @param emailId
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return DeleteUserEmailStatusEnum
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	DeleteUserEmailStatusEnum deleteUserEmail(int userId,String emailId) throws PlatformException;
	
	/**
	 * Get a User Phone.
	 * @param userId
	 * @throws UserNotFoundException when user is absent.
	 * @throws PhoneNotFoundException when the is no phone for the user.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return List of User Phone 
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	List<UserPhone> getUserPhone(int userId) throws UserNotFoundException,PhoneNotFoundException,PlatformException;
	
	/**
	 * Add an Phone for User.
	 * @param userId
	 * @param userPhone
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return AddUserPhoneStatusEnum
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	AddUserPhoneStatusEnum addUserPhone(int userId,UserPhone userPhone) throws UserNotFoundException,PlatformException;
	
	/**
	 * Verify an Phone for User.
	 * @param userId
	 * @param userPhone
	 * @param verificationCode
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return VerifyUserPhoneStatusEnum
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	VerifyUserPhoneStatusEnum verifyUserPhone(int userId,String userPhone,String verificationCode) throws PlatformException;
	
	/**
	 * Delete an Phone for User.
	 * @param userId
	 * @param phone
	 * @throws UserNotFoundException when user is absent.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return DeleteUserPhoneStatusEnum
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	DeleteUserPhoneStatusEnum deleteUserPhone(int userId,String phone) throws PlatformException;

}
