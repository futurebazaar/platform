package com.fb.platform.user.dao.interfaces;

import java.util.Collection;

import com.fb.platform.user.domain.UserBo;


/**
 * @author kislaya
 *
 */ 
public interface UserDao {
	
	//public UserBo login(String username , String password);
	
	//public UserBo logout(UserBo userBo);
	
	public boolean changePassword(UserBo userBo,String newPassword);
}
