package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;
import com.fb.platform.user.manager.exception.EmailNotFoundException;
import com.fb.platform.user.manager.exception.InvalidUserNameException;
import com.fb.platform.user.manager.exception.PhoneNotFoundException;
import com.fb.platform.user.manager.exception.UserAlreadyExistsException;
import com.fb.platform.user.manager.exception.UserNotFoundException;
import com.fb.platform.user.manager.interfaces.UserAdminService;
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
import com.fb.platform.user.util.ValidatorUtil;

public class UserAdminServiceImpl implements UserAdminService {
	
	@Autowired
	private UserAdminDao userAdminDao = null;
	
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

	@Override
	public User getUser(String key) throws UserNotFoundException,
			PlatformException {
		UserBo userBo = new UserBo();
		try{
			userBo = userAdminDao.load(key);
		}catch (DataAccessException e) {
			throw new PlatformException("Error while loading the user. key = " + key, e);
		}
		if (userBo != null) {
			return ConvertUserBotoUser(userBo);
		} else {
			throw new UserNotFoundException("User not found with key : " + key);
		}
	}

	@Override
	public User addUser(User user) throws UserAlreadyExistsException,
			InvalidUserNameException, PlatformException {
		try{
			boolean isEmail = ValidatorUtil.isValidEmailAddress(user.getUserName());
			boolean isPhone = ValidatorUtil.isValidPhoneNumber(user.getUserName());
			if (!(isEmail | isPhone )){
				throw new InvalidUserNameException("User name is invalid its neither email or phone");
			}
			UserBo userPresent = userAdminDao.load(user.getUserName());
			if(userPresent != null && userPresent.getUserid() > 0){
				throw new UserAlreadyExistsException("User already exists with this username");
			}
			UserBo userBo = new UserBo();
			userBo.setUsername(user.getUserName());
			userBo.setPassword(user.getPassword());
			if(isEmail){
				UserEmailBo userEmailBo = new UserEmailBo();
				userEmailBo.setEmail(user.getUserName());
				userEmailBo.setType("primary");
				List<UserEmailBo> userEmailBos = new ArrayList<UserEmailBo>();
				userEmailBos.add(userEmailBo);
				userBo.setUserEmail(userEmailBos);
			}
			if(isPhone){
				UserPhoneBo userPhoneBo = new UserPhoneBo();
				userPhoneBo.setPhoneno(user.getUserName());
				userPhoneBo.setType("primary");
				List<UserPhoneBo> userPhoneBos = new ArrayList<UserPhoneBo>();
				userPhoneBos.add(userPhoneBo);
				userBo.setUserPhone(userPhoneBos);
			}
			UserBo userBO = userAdminDao.add(userBo);
			return ConvertUserBotoUser(userBO);
		}catch (InvalidUserNameException e){
			throw new InvalidUserNameException("User name is invalid its neither email or phone");
		}catch (UserAlreadyExistsException e){
			throw new UserAlreadyExistsException("User already exists with this username");
		}catch (DataAccessException e) {
			throw new PlatformException("Error while adding user to the database");
		}catch (Exception e) {
			throw new PlatformException("Error while adding user to the database");
		}
	}
	@Override
	public UpdateUserStatusEnum updateUser(User user) throws UserNotFoundException,
			PlatformException {
		try {
			UserBo userBo = userAdminDao.loadByUserId(user.getUserId());
			if (userBo != null){
				if (user.getDateOfBirth() != null) {
					userBo.setDateofbirth(user.getDateOfBirth());
				}
				if (!(StringUtils.isBlank(user.getFirstName()))) {
					userBo.setFirstname(user.getFirstName());
				}
				if (!(StringUtils.isBlank(user.getLastName()))) {
					userBo.setLastname(user.getLastName());
				}
				if (!(StringUtils.isBlank(user.getGender()))) {
					userBo.setGender(user.getGender());
				}
				if (!(StringUtils.isBlank(user.getSalutation()))) {
					userBo.setSalutation(user.getSalutation());
				}
				userAdminDao.update(userBo);
				return UpdateUserStatusEnum.SUCCESS;
			}else{
				throw new UserNotFoundException();
			}
		} catch (UserNotFoundException e) {
			throw new UserNotFoundException("No user exists with the given userid. userId = " + user.getUserId(), e);
		}catch (DataAccessException e) {
			throw new PlatformException("Error while updating the user. userId = " + user.getUserId(), e);
		} catch (PlatformException e) {
			throw new PlatformException("Error updating user");
		}
	}

	@Override
	public List<UserEmail> getUserEmail(int userId)
			throws UserNotFoundException,EmailNotFoundException, PlatformException {
		try{
			List<UserEmail> userEmails = null;
			UserBo userBo = getUserByUserId(userId);
			if (userBo.getUserEmail() != null && userBo.getUserEmail().size() > 0){
					userEmails = new ArrayList<UserEmail>();
					for(UserEmailBo userEmailBo : userBo.getUserEmail()){
						UserEmail userEmail = new UserEmail();
						userEmail.setEmail(userEmailBo.getEmail());
						userEmail.setType(userEmailBo.getType());
						userEmails.add(userEmail);
					}
					return userEmails;
				}else{
					throw new EmailNotFoundException();
				}
		}catch(UserNotFoundException e){
			throw new UserNotFoundException("No User Exists for userid :: " + userId);
		}catch(EmailNotFoundException e){
			throw new EmailNotFoundException("No emails for for userid :: " + userId);
		}catch (PlatformException e) {
			throw new PlatformException("Error geting email records.");
		}		
	}

	@Override
	public AddUserEmailStatusEnum addUserEmail(int userId, UserEmail userEmail)
			throws UserNotFoundException, PlatformException {
		try{
			if(!ValidatorUtil.isValidEmailAddress(userEmail.getEmail())){
				return AddUserEmailStatusEnum.INVALID_EMAIL;
			}
			UserBo userBo = getUserByUserId(userId);
			UserEmailBo userEmailBo = new UserEmailBo();
			userEmailBo.setEmail(userEmail.getEmail());
			userEmailBo.setType(userEmail.getType());
			if (userBo.getUserEmail() != null){
				for(UserEmailBo _userEmailBo : userBo.getUserEmail()){
					if(userEmailBo.equals(_userEmailBo)){
						return AddUserEmailStatusEnum.ALREADY_PRESENT;
					}
				}
			}
			boolean success = userAdminDao.addUserEmail(userId, userEmailBo);
			if(success){
				return AddUserEmailStatusEnum.SUCCESS;
			}else{
				return AddUserEmailStatusEnum.ERROR_ADDING_EMAIL;
			}
		}catch(UserNotFoundException e){
			throw new UserNotFoundException("No user with this userId:" + userId);
		}catch (PlatformException e) {
			throw new PlatformException("Error adding email records.");
		}
	}
	@Override
	public VerifyUserEmailStatusEnum verifyUserEmail(int userId,
			String userEmail) throws PlatformException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteUserEmailStatusEnum deleteUserEmail(int userId,
			String emailId) throws PlatformException {
		try{
			boolean success = userAdminDao.deleteUserEmail(userId, emailId);			
			if(success){
				return DeleteUserEmailStatusEnum.SUCCESS;
			}else{
				return DeleteUserEmailStatusEnum.NO_EMAIL_ID;
			}
		}catch (PlatformException e) {
			throw new PlatformException("Error  deleting email record");
		}
	}

	@Override
	public List<UserPhone> getUserPhone(int userId)
			throws UserNotFoundException, PhoneNotFoundException,
			PlatformException {
		try{
			List<UserPhone> userPhones = null;
			UserBo userBo = getUserByUserId(userId);
			if (userBo.getUserPhone() != null && userBo.getUserPhone().size() > 0){
					userPhones = new ArrayList<UserPhone>();
					for(UserPhoneBo userPhoneBo : userBo.getUserPhone()){
						UserPhone userPhone = new UserPhone();
						userPhone.setPhone(userPhoneBo.getPhoneno());
						userPhone.setType(userPhoneBo.getType());
						userPhones.add(userPhone);
					}
					return userPhones;
				}else{
					throw new PhoneNotFoundException();
				}
		}catch(UserNotFoundException e){
			throw new UserNotFoundException("No User Exists for userid :: " + userId);
		}catch(PhoneNotFoundException e){
			throw new PhoneNotFoundException("No phone for for userid :: " + userId);
		}catch (PlatformException e) {
			throw new PlatformException("Error geting phone records.");
		}		
	}

	@Override
	public AddUserPhoneStatusEnum addUserPhone(int userId, UserPhone userPhone)
			throws UserNotFoundException, PlatformException {
		try{
			if(!ValidatorUtil.isValidPhoneNumber(userPhone.getPhone())){
				return AddUserPhoneStatusEnum.INVALID_PHONE;
			}
			UserBo userBo = getUserByUserId(userId);
			UserPhoneBo userPhoneBo = new UserPhoneBo();
			userPhoneBo.setPhoneno(userPhone.getPhone());
			userPhoneBo.setType(userPhone.getType());
			if (userBo.getUserPhone() != null){
				for(UserPhoneBo _userPhoneBo : userBo.getUserPhone()){
					if(userPhoneBo.equals(_userPhoneBo)){
						return AddUserPhoneStatusEnum.ALREADY_PRESENT;
					}
				}
			}
			boolean success = userAdminDao.addUserPhone(userId, userPhoneBo);
			if(success){
				return AddUserPhoneStatusEnum.SUCCESS;
			}else{
				return AddUserPhoneStatusEnum.ERROR_ADDING_PHONE;
			}
		}catch(UserNotFoundException e){
			throw new UserNotFoundException("No user with this userId:" + userId);
		}catch (PlatformException e) {
			throw new PlatformException("Error adding phone records.");
		}
	}

	@Override
	public DeleteUserPhoneStatusEnum deleteUserPhone(int userId, String phone)
			throws PlatformException {
		try{
			boolean success = userAdminDao.deleteUserPhone(userId, phone);			
			if(success){
				return DeleteUserPhoneStatusEnum.SUCCESS;
			}else{
				return DeleteUserPhoneStatusEnum.NO_PHONE;
			}
		}catch (PlatformException e) {
			throw new PlatformException("Error  deleting phone record");
		}
	}

	@Override
	public VerifyUserPhoneStatusEnum verifyUserPhone(int userId,
			String userPhone) throws PlatformException {
		try{
			boolean success = userAdminDao.verifyUserPhone(userId, userPhone);		
			if(success){				
				return VerifyUserPhoneStatusEnum.SUCCESS;
			}else{
				return VerifyUserPhoneStatusEnum.NO_PHONE;
			}
		}catch(PlatformException e){
			throw new PlatformException("Error Verifying phone record"); 
		}
	}
	
	private User ConvertUserBotoUser(UserBo userBo){
		User user = new User();
		user.setDateOfBirth(userBo.getDateofbirth());
		user.setFirstName(userBo.getFirstname());
		user.setGender(userBo.getGender());
		user.setLastName(userBo.getLastname());
		user.setPassword(userBo.getPassword());
		user.setSalutation(userBo.getSalutation());
		user.setUserId(userBo.getUserid());
		user.setUserName(userBo.getName());
		return user;
	}

	@Override
	public UserBo getUserByUserId(int userId) throws UserNotFoundException,
	PlatformException {
		UserBo userBo = new UserBo();
		try{
			userBo = userAdminDao.loadByUserId(userId);
		}catch (DataAccessException e) {
			throw new PlatformException("Error while loading the user. userId = " + userId, e);
		}
		if (userBo != null) {
			return userBo;
		} else {
			throw new UserNotFoundException("User not found with userId : " + userId);
		}
	}
}
