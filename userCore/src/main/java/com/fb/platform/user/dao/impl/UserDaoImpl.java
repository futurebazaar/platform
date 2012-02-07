package com.fb.platform.user.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
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

/**
 * @author kumar
 *
 */
public class UserDaoImpl implements UserDao {
	
	private JdbcTemplate jdbcTemplate;

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
		if (isemail)
			return getUserByEmail(key);
		else if (isphone)
			return getUserByPhone(key);
		else 
			return null;
	}
    

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserDao#getUsers()
	 */
	@Override
	public Collection<UserBo> getUsers() {
		String sql = "select up.user_id,up.full_name from tinla.users_profile up ";
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
		final String sqlauthuser = "Insert into auth_user (username,first_name,last_name,email,password,is_staff,is_active,is_superuser,last_login,date_joined)"
				+ "values (?,?,?,?,?,?,?,?,?)";
		final java.util.Date today = new java.util.Date();
		final UserBo userbo = userBo;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqlauthuser,new String[]{"id"});
				ps.setString(1, userbo.getUsername());
				ps.setString(2, userbo.getFirstname());
				ps.setString(3, userbo.getLastname());
				ps.setString(4, "");
				ps.setString(5, userbo.getPassword());
				ps.setInt(6, 0);
				ps.setInt(7, 1);
				ps.setInt(8, 0);
				ps.setDate(9, new Date(today.getTime()));
				ps.setDate(10, new Date(today.getTime()));
				return ps;
			}
		}, keyHolder);
		
		KeyHolder keyHolderprofile = new GeneratedKeyHolder();
		final String sqluserprofile = "insert into users_profile (user_id,primary_phone,secondary_phone,"
					+ "buyer_or_seller,acquired_through_account_id,full_name,primary_email,secondary_email,gender,salutation,salt,passcode," 
					+ "marketing_alerts,created_on,date_of_birth,is_agent,webpage,facebook,twitter,email_notification,sms_alert,verify_code,"
					+ "profession,user_photo,atg_username,transaction_password,atg_login,atg_password) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		final int user_id = (Integer)keyHolder.getKey();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqluserprofile,new String[]{"id"});
				ps.setInt(1, user_id);
				ps.setString(2, "");
				ps.setString(3, "");
				ps.setString(4, "buyer");
				ps.setString(5, null);
				ps.setString(6, userbo.getFirstname() + " " +  userbo.getLastname());
				ps.setString(7, "");
				ps.setString(8, "");
				ps.setString(9, userbo.getGender());
				ps.setString(10, "");
				ps.setString(11, "");
				ps.setString(12, "");
				ps.setString(13, "neutral");
				ps.setDate(14, new Date(today.getTime()));
				ps.setDate(15, (Date)userbo.getDateofbirth());
				ps.setBoolean(15, false);
				ps.setString(16, "");
				ps.setString(17, "");
				ps.setString(18, "");
				ps.setBoolean(19, false);
				ps.setBoolean(20, false);
				ps.setInt(21, 0);
				ps.setString(22, null);
				ps.setString(23, null);
				ps.setString(24, null);
				ps.setString(25, null);
				ps.setString(26, null);
				ps.setString(27, null);				
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
		
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserDao#update(com.fb.platform.user.domain.UserBo)
	 */
	@Override
	public void update(UserBo userBo) {
		// TODO Auto-generated method stub
		
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
			String sql = "select uph.user_id,up.full_name from tinla.users_phone uph left join tinla.users_profile up "
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
	
	private List<UserEmailBo> getEmailByUserid(int userid){
		String qry = "Select email,type from tinla.users_email where user_id = " + userid ;
		List<UserEmailBo> userEmailBo = jdbcTemplate.query(qry, new UserEmailMapper());
		return userEmailBo;
	}
	private List<UserPhoneBo> getPhoneByUserid(int userid){
		String qry = "Select phone,type from tinla.users_phone where user_id = " + userid ;
		List<UserPhoneBo> userPhoneBo = jdbcTemplate.query(qry, new UserPhoneMapper());
		return userPhoneBo;
	}
	
	

	
}
