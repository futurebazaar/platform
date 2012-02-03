package com.fb.platform.user.manager.mapper;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;
import com.fb.platform.user.manager.model.UserEmailTO;
import com.fb.platform.user.manager.model.UserPhoneTO;
import com.fb.platform.user.manager.model.UserTO;

/**
 * @author kumar
 *
 */
public class UserBoToMapper {
	
	/**
	 * @param userBo
	 * @return userTO
	 */
	public UserTO fromBOtoTO(UserBo userBo){
		UserTO userTO = new UserTO();
		userTO.setName(userBo.getName());
		userTO.setUserEmail(fromEmailBOtoEmailTO(userBo.getUserEmail()));
		userTO.setUserPhone(fromPhoneBOtoPhoneTO(userBo.getUserPhone()));
		return userTO;
		
	}
	
	/**
	 * @param userTO
	 * @return userBO
	 */
	public UserBo fromTOtoBO(UserTO userTO){
		UserBo userBo = new UserBo();
		userBo.setName(userTO.getName());
		userBo.setUserEmail(fromEmailTOtoEmailBO(userTO.getUserEmail()));
		userBo.setUserPhone(fromPhoneTOtoPhoneBO(userTO.getUserPhone()));
		return userBo;
		
	}
	
	
	public List<UserEmailTO> fromEmailBOtoEmailTO(List<UserEmailBo> userEmailBos){
		try{
			List<UserEmailTO> userEmailTOlist = new ArrayList<UserEmailTO>();
			for(UserEmailBo userEmailBo : userEmailBos ){
				UserEmailTO userEmailTO = new UserEmailTO();
				userEmailTO.setEmail(userEmailBo.getEmail());
				userEmailTO.setType(userEmailBo.getType());
				userEmailTOlist.add(userEmailTO);
			}
			return userEmailTOlist;
		}catch(NullPointerException ne){
			return null;
		}
	}
	
	
	public List<UserEmailBo> fromEmailTOtoEmailBO(List<UserEmailTO> userEmailTos){
		try{
			List<UserEmailBo> userEmailBOlist = new ArrayList<UserEmailBo>();
			for(UserEmailTO userEmailTO : userEmailTos ){
				UserEmailBo userEmailBO = new UserEmailBo();
				userEmailBO.setEmail(userEmailTO.getEmail());
				userEmailBO.setType(userEmailTO.getType());
				userEmailBOlist.add(userEmailBO);
			}
			return userEmailBOlist;
		}catch(NullPointerException ne){
			return null;
		}
	}
	
	public List<UserPhoneTO> fromPhoneBOtoPhoneTO(List<UserPhoneBo> userPhoneBos){
		try{
			List<UserPhoneTO> userPhoneTOlist = new ArrayList<UserPhoneTO>();
			for(UserPhoneBo userPhoneBo : userPhoneBos ){
				UserPhoneTO userPhoneTO = new UserPhoneTO();
				userPhoneTO.setPhoneno(userPhoneBo.getPhoneno());
				userPhoneTO.setType(userPhoneBo.getType());
				userPhoneTOlist.add(userPhoneTO);
			}
			return userPhoneTOlist;
		} catch(NullPointerException ne) {
			return null;
		}
	}
	
	
	public List<UserPhoneBo> fromPhoneTOtoPhoneBO(List<UserPhoneTO> userPhoneTos){
		try{
			List<UserPhoneBo> userPhoneBOlist = new ArrayList<UserPhoneBo>();
			for(UserPhoneTO userPhoneTo : userPhoneTos ){
				UserPhoneBo userPhoneBO = new UserPhoneBo();
				userPhoneBO.setPhoneno(userPhoneTo.getPhoneno());
				userPhoneBO.setType(userPhoneTo.getType());
				userPhoneBOlist.add(userPhoneBO);
			}
			return userPhoneBOlist;
		} catch(NullPointerException ne) {
			return null;
		}
	}
}
