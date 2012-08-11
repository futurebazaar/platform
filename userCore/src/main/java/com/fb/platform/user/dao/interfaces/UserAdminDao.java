package com.fb.platform.user.dao.interfaces;

import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;

public interface UserAdminDao {

	/**
	 * @param key elther the phone number or the emailid of the user
	 * @return the user object
	 */
	public UserBo load(String key);

	public UserBo loadByUserId(int userId);

	/**
	 * Add a new user
	 * @param UserBo
	 * @return UserBo
	 */
	public UserBo add(UserBo userBo);

	/**
	 * Update an existing user
	 * @param UserBo
	 * @return UserBo
	 */
	public UserBo update(UserBo userBo);

	public boolean changePassword(int userId, String newPassword);
	
	public boolean addUserEmail(int userId,UserEmailBo userEmailBo);
	public boolean deleteUserEmail(int userId,String emailId);
	
	public boolean addUserPhone(int userId,UserPhoneBo userPhoneBo);
	public boolean deleteUserPhone(int userId,String phone);

	public boolean verifyUserPhone(int userId, String phone);
}
