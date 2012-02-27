package com.fb.platform.user.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.platform.user.dao.interfaces.UserDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;
import com.fb.platform.user.mapper.UserEmailMapper;
import com.fb.platform.user.mapper.UserMapper;
import com.fb.platform.user.mapper.UserPhoneMapper;
import com.fb.platform.user.service.PasswordService;

/**
 * @author kumar
 *
 */
public class UserDaoImpl implements UserDao {
	
	private JdbcTemplate jdbcTemplate;
	private PasswordService passwordService = PasswordService.getInstance();

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
    
    /* (non-Javadoc)
     * @see com.fb.platform.user.dao.interfaces.UserDao#load(java.lang.String)
     */
    @Override
	public UserBo load(String key) {
		boolean isemail = (this.jdbcTemplate.queryForInt("select count(0) from users_email WHERE email = '" + key + "'") > 0)  ? true : false;
		boolean isphone = (this.jdbcTemplate.queryForInt("select count(0) from users_phone WHERE phone = '" + key + "'") > 0)  ? true : false;
		boolean isuserid = (this.jdbcTemplate.queryForInt("select count(0) from users_profile WHERE id = " + key ) > 0)  ? true : false;
		if (isemail)
			return getUserByEmail(key);
		else if (isphone)
			return getUserByPhone(key);
		else if (isuserid)
			return getUserByUserId(key);
		else 
			return null;
	}
    

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserDao#getUsers()
	 */
	@Override
	public Collection<UserBo> getUsers() {
		String sql = "select up.id,up.full_name,up.salutation,up.date_of_birth,up.password, from tinla.users_profile up ";
		List<UserBo> users = jdbcTemplate.query(sql, new UserMapper());
		for (UserBo user : users){
			user.setUserEmail(getEmailByUserid(user.getUserid()));
			user.setUserPhone(getPhoneByUserid(user.getUserid()));
		}
		return users;
		
	}
	

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserDao#add(com.fb.platform.user.domain.UserBo)
	 */
	@Override
	public void add(UserBo userBo) {
		
		try {
			final UserBo userbo = userBo;
			final java.util.Date today = new java.util.Date();
			KeyHolder keyHolderprofile = new GeneratedKeyHolder();
			final String sqluserprofile = "insert into users_profile (primary_phone,secondary_phone,"
						+ "buyer_or_seller,acquired_through_account_id,password,full_name,primary_email,secondary_email,gender,salutation," 
						+ "marketing_alerts,created_on,date_of_birth,is_agent,webpage,facebook,twitter,email_notification,sms_alert,verify_code,"
						+ "profession,user_photo) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(sqluserprofile,new String[]{"id"});
					ps.setString(1, "");
					ps.setString(2, "");
					ps.setString(3, "buyer");
					ps.setString(4, null);
					ps.setString(5, passwordService.getencrypt(userbo.getPassword()));
					ps.setString(6, userbo.getFirstname() + " " +  userbo.getLastname());
					ps.setString(7, "");
					ps.setString(8, "");
					ps.setString(9, userbo.getGender());
					ps.setString(10, userbo.getSalutation());
					ps.setString(11, "neutral");
					ps.setDate(12, new Date(today.getTime()));
					ps.setDate(13, (Date)userbo.getDateofbirth());
					ps.setBoolean(14, false);
					ps.setString(15, "");
					ps.setString(16, "");
					ps.setString(17, "");
					ps.setBoolean(18, false);
					ps.setBoolean(19, false);
					ps.setInt(21, 0);
					ps.setString(22, null);
					ps.setString(23, null);
					return ps;
				}
			}, keyHolderprofile);
			 
			int userid = (Integer)keyHolderprofile.getKey();
			
			for(UserEmailBo userEmailBo : userBo.getUserEmail()){
				String sqlemailinsert = "insert into users_email (email,type,userid) values (?,?,?)";
				Object objs[] = new Object[3];
				objs[0] = userEmailBo.getEmail();
				objs[1] = userEmailBo.getType();
				objs[2] = userid ;
				jdbcTemplate.update(sqlemailinsert, objs);
			}
			
			for(UserPhoneBo userPhoneBo : userBo.getUserPhone()){
				String sqlphoneinsert = "insert into users_phone (phone,type,userid) values (?,?,?)";
				Object objs[] = new Object[3];
				objs[0] = userPhoneBo.getPhoneno();
				objs[1] = userPhoneBo.getType();
				objs[2] = userid ;
				jdbcTemplate.update(sqlphoneinsert, objs);
			}
		} catch (InvalidDataAccessApiUsageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserDao#update(com.fb.platform.user.domain.UserBo)
	 */
	@Override
	public UserBo update(UserBo userBo) {
		
		final UserBo userbo = userBo;
		final java.util.Date today = new java.util.Date();
		KeyHolder keyHolderprofile = new GeneratedKeyHolder();
		final String sqluserprofile = "update users_profile set primary_phone = ? , secondary_phone = ? ,"
					+ "buyer_or_seller = ? ,acquired_through_account_id,password = ? ,full_name= ? ,primary_email= ? ,secondary_email= ? ,gender= ? ,salutation= ? ," 
					+ "marketing_alerts= ? ,created_on= ? ,date_of_birth= ? ,is_agent= ? ,webpage= ? ,facebook= ? ,twitter= ? ,email_notification= ? ,sms_alert= ? ,verify_code= ? ,"
					+ "profession= ? ,user_photo = ? where id = " + userBo.getUserid();
			
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqluserprofile,new String[]{"id"});
				ps.setString(1, "");
				ps.setString(2, "");
				ps.setString(3, "buyer");
				ps.setString(4, null);
				ps.setString(5, passwordService.getencrypt(userbo.getPassword()));
				ps.setString(6, userbo.getFirstname() + " " +  userbo.getLastname());
				ps.setString(7, "");
				ps.setString(8, "");
				ps.setString(9, userbo.getGender());
				ps.setString(10, userbo.getSalutation());
				ps.setString(11, "neutral");
				ps.setDate(12, new Date(today.getTime()));
				ps.setDate(13, (Date)userbo.getDateofbirth());
				ps.setBoolean(14, false);
				ps.setString(15, "");
				ps.setString(16, "");
				ps.setString(17, "");
				ps.setBoolean(18, false);
				ps.setBoolean(19, false);
				ps.setInt(21, 0);
				ps.setString(22, null);
				ps.setString(23, null);
				return ps;
			}
		}, keyHolderprofile);
		
		return load(Long.toString(userBo.getUserid()));
		
	}

	/**
	 * @param email
	 * @return
	 */
	private UserBo getUserByEmail(String email) {
		
		try {
			String sql = "select ue.user_id,up.full_name from tinla.users_email ue left join tinla.users_profile up "
					+ "on ue.user_id = up.id where ue.email = '" + email + "'";
			UserBo user = jdbcTemplate.queryForObject(sql, new UserMapper());
			user.setUserEmail(getEmailByUserid(user.getUserid()));
			user.setUserPhone(getPhoneByUserid(user.getUserid()));
			return user;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	private UserBo getUserByPhone(String phone) {
		try {
			String sql = "select uph.user_id,up.full_name from users_phone uph left join tinla.users_profile up "
					+ "on uph.user_id = up.id where uph.phone = '" + phone + "'";
			UserBo user = jdbcTemplate.queryForObject(sql, new UserMapper());
			user.setUserEmail(getEmailByUserid(user.getUserid()));
			user.setUserPhone(getPhoneByUserid(user.getUserid()));
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private UserBo getUserByUserId(String Userid){
		try {
			String sql = "select up.id,up.full_name from users_profile up "
					+ "up.id = " + Userid ;
			UserBo user = jdbcTemplate.queryForObject(sql, new UserMapper());
			user.setUserEmail(getEmailByUserid(user.getUserid()));
			user.setUserPhone(getPhoneByUserid(user.getUserid()));
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<UserEmailBo> getEmailByUserid(long userid){
		String qry = "Select email,type from tinla.users_email where user_id = " + userid ;
		List<UserEmailBo> userEmailBo = jdbcTemplate.query(qry, new UserEmailMapper());
		return userEmailBo;
	}
	private List<UserPhoneBo> getPhoneByUserid(long userid){
		String qry = "Select phone,type from tinla.users_phone where user_id = " + userid ;
		List<UserPhoneBo> userPhoneBo = jdbcTemplate.query(qry, new UserPhoneMapper());
		return userPhoneBo;
	}

	@Override
	public UserBo login(String username ,String password) {
		UserBo userBo = load(username);
		if (passwordService.compareencrypt(password, userBo.getPassword())){
			// TO DO atfter signin ing
			return userBo;
		}else {
			return null;
		}
	}

	@Override
	public UserBo logout(UserBo userBo) {
		// TO Call authentication layer to invalidate session
		return null;
	}

	@Override
	public UserBo changepassword(UserBo userBo,String newpassword) {
		UserBo userbo = load(Long.toString(userBo.getUserid()));
		if (passwordService.compareencrypt(userBo.getPassword(), userbo.getPassword())){
			userBo.setPassword(passwordService.getencrypt(newpassword));
			return update(userBo);			
			
		}else {
			return null;
		}
	}
	
	

	
}
