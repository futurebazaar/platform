package com.fb.platform.user.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;
import com.fb.platform.user.util.PasswordUtil;

public class UserAdminDaoImpl implements UserAdminDao {

	private static final String CHECK_EMAIL_IS_USERNAME_QUERY = "SELECT count(0) from users_email WHERE email = ?";
	private static final String CHECK_PHONE_IS_USERNAME_QUERY = "SELECT count(0) from users_phone WHERE phone = ?";
	private static final String CHECK_USERID_IS_USERNAME_QUERY = "SELECT count(0) from users_profile WHERE id = ?";

	private static final String SELECT_USER_FIELDS = "SELECT " +
			"up.id as id, " +
			"up.primary_phone, " +
			"up.secondary_phone, " +
			"up.buyer_or_seller, " +
			"up.acquired_through_account_id, " +
			"up.password, " +
			"up.full_name, " +
			"up.primary_email, " +
			"up.gender, " +
			"up.salutation, " +
			"up.marketing_alerts, " +
			"up.created_on, " +
			"up.date_of_birth, " +
			"up.is_agent, " +
			"up.webpage, " +
			"up.facebook, " +
			"up.twitter, " +
			"up.email_notification, " +
			"up.sms_alert, " +
			"up.profession, " +
			"up.user_photo ";

	private static final String SELECT_USER_BY_EMAIL_QUERY = 
			SELECT_USER_FIELDS + 
			" FROM users_profile up, users_email ue WHERE up.id = ue.user_id AND ue.email = ?";

	private static final String SELECT_USER_BY_PHONE_QUERY = 
			SELECT_USER_FIELDS +
			" FROM users_profile up, users_phone uph WHERE up.id = uph.user_id AND uph.phone = ?";

	private static final String SELECT_USER_BY_USER_ID = 
			SELECT_USER_FIELDS +
			" FROM users_profile up WHERE up.id = ?";

	private static final String SELECT_EMAILS_BY_USER_ID = "SELECT email, type FROM users_email WHERE user_id = ?";

	private static final String SELECT_PHONES_BY_USER_ID = "SELECT phone, type FROM users_phone WHERE user_id = ?";
	
	private static final String SELECT_ALL_USERS = "SELECT up.id,up.full_name,up.salutation,up.date_of_birth,up.password from users_profile up " ;
	
	private static final String INSERT_NEW_USER = "INSERT " +
			"into users_profile (" +
			"primary_phone," +
			"secondary_phone," +
			"buyer_or_seller," +
			"acquired_through_account_id," +
			"password," +
			"full_name," +
			"primary_email," +
			"secondary_email," +
			"gender," +
			"salutation," +
			"marketing_alerts," +
			"created_on," +
			"date_of_birth," +
			"is_agent,webpage," +
			"facebook," +
			"twitter," +
			"email_notification," +
			"sms_alert," +
			"profession," +
			"user_photo)" +
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	private static final String INSERT_NEW_EMAIL = "INSERT into users_email (" +
			"email," +
			"type," +
			"user_id) " +
			"values (?,?,?)"; 
	
	private static final String INSERT_NEW_PHONE = "INSERT into users_phone (" +
			"phone," +
			"type," +
			"userid) " +
			"values (?,?,?)";
	
	private static final String UPDATE_USER = "UPDATE users_profile set " +
			"primary_phone = ? , " +
			"secondary_phone = ? ," +
			"buyer_or_seller = ? ," +
			"acquired_through_account_id = ? ," +
			"password = ? ," +
			"full_name= ? ," +
			"primary_email= ? ," +
			"secondary_email= ? ," +
			"gender= ? ," +
			"salutation= ? ," +
			"marketing_alerts= ? ," +
			"created_on= ? ," +
			"date_of_birth= ? ," +
			"is_agent= ? ," +
			"webpage= ? ," +
			"facebook= ? ," +
			"twitter= ? ," +
			"email_notification= ? ," +
			"sms_alert= ? ," +
			"verify_code= ? ," +
			"profession= ? ," +
			"user_photo = ? " +
			"where id = ?" ;

	private static final String UPDATE_PASSWORD_SQL = "UPDATE users_profile SET " +
			"password = ? " +
			"WHERE id = ?";

	private JdbcTemplate jdbcTemplate;

    /* (non-Javadoc)
     * @see com.fb.platform.user.dao.interfaces.UserDao#load(java.lang.String)
     */
    @Override
	public UserBo load(String key) {
    	boolean isEmail = isUsernameEmail(key);
    	if (isEmail) {
    		return getUserByEmail(key);
    	}

    	boolean isPhone = isUsernamePhone(key);
    	if (isPhone) {
    		return getUserByPhone(key);
    	}

    	boolean isUserId = isUsernameUserId(key);
    	if (isUserId) {
    		return getUserByUserId(Integer.parseInt(key));
    	}

    	return null;
	}

    private boolean isUsernameEmail(String username) {
    	int emailCount = this.jdbcTemplate.queryForInt(CHECK_EMAIL_IS_USERNAME_QUERY, username);
    	if (emailCount > 0) {
    		return true;
    	}
    	return false;
    }

    private boolean isUsernamePhone(String username) {
    	int phoneCount = this.jdbcTemplate.queryForInt(CHECK_PHONE_IS_USERNAME_QUERY, username);
    	if (phoneCount > 0) {
    		return true;
    	}
    	return false;
    }

    private boolean isUsernameUserId(String username) {
    	int userIdCount = this.jdbcTemplate.queryForInt(CHECK_USERID_IS_USERNAME_QUERY, username);
    	if (userIdCount > 0) {
    		return true;
    	}
    	return false;
    }

	private UserBo getUserByEmail(String email) {

		UserBo user = jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL_QUERY, 
				new Object[] {email},
				new UserMapper());
		user.setUserEmail(getEmailByUserid(user.getUserid()));
		user.setUserPhone(getPhoneByUserid(user.getUserid()));
		return user;
	}

	private UserBo getUserByPhone(String phone) {

		UserBo user = jdbcTemplate.queryForObject(SELECT_USER_BY_PHONE_QUERY, 
				new Object[] {phone},
				new UserMapper());
		user.setUserEmail(getEmailByUserid(user.getUserid()));
		user.setUserPhone(getPhoneByUserid(user.getUserid()));
		return user;
	}

	@Override
	public UserBo loadByUserId(int userId) {
		return getUserByUserId(userId);
	}

	private UserBo getUserByUserId(int userId) {

		UserBo user = jdbcTemplate.queryForObject(SELECT_USER_BY_USER_ID, 
				new Object[] {userId},
				new UserMapper());
		user.setUserEmail(getEmailByUserid(user.getUserid()));
		user.setUserPhone(getPhoneByUserid(user.getUserid()));
		return user;
	}

	private List<UserEmailBo> getEmailByUserid(long userId){

		List<UserEmailBo> userEmailBo = jdbcTemplate.query(SELECT_EMAILS_BY_USER_ID,
				new Object[] {userId},
				new UserEmailMapper());
		return userEmailBo;
	}

	private List<UserPhoneBo> getPhoneByUserid(long userId){

		List<UserPhoneBo> userPhoneBo = jdbcTemplate.query(SELECT_PHONES_BY_USER_ID, 
				new Object[] {userId}, 
				new UserPhoneMapper());
		return userPhoneBo;
	}


    /* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserDao#getUsers()
	 */
	@Override
	public Collection<UserBo> getUsers() {
		List<UserBo> users = jdbcTemplate.query(SELECT_ALL_USERS, new UserMapper());
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
	public void add(final UserBo userBo) {
		
		try {
			final java.util.Date today = new java.util.Date();
			KeyHolder keyHolderprofile = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(INSERT_NEW_USER,new String[]{"id"});
					ps.setString(1, "");
					ps.setString(2, "");
					ps.setString(3, "buyer");
					ps.setString(4, null);
					ps.setString(5, PasswordUtil.getEncryptedPassword(userBo.getPassword()));
					ps.setString(6, userBo.getFirstname() + " " +  userBo.getLastname());
					ps.setString(7, "");
					ps.setString(8, "");
					ps.setString(9, userBo.getGender());
					ps.setString(10, userBo.getSalutation());
					ps.setString(11, "neutral");
					ps.setDate(12, new Date(today.getTime()));
					ps.setDate(13, (Date)userBo.getDateofbirth());
					ps.setBoolean(14, false);
					ps.setString(15, "");
					ps.setString(16, "");
					ps.setString(17, "");
					ps.setBoolean(18, false);
					ps.setBoolean(19, false);
					ps.setString(20, null);
					ps.setString(21, null);
					return ps;
				}
			}, keyHolderprofile);
			 
			long userid = (Long)keyHolderprofile.getKey();
			if (userBo.getUserEmail() != null) {
				for(UserEmailBo userEmailBo : userBo.getUserEmail()){
					Object objs[] = new Object[3];
					objs[0] = userEmailBo.getEmail();
					objs[1] = userEmailBo.getType();
					objs[2] = userid ;
					jdbcTemplate.update(INSERT_NEW_EMAIL, objs);
				}
			}
			if (userBo.getUserPhone() != null) {
				for(UserPhoneBo userPhoneBo : userBo.getUserPhone()){
					Object objs[] = new Object[3];
					objs[0] = userPhoneBo.getPhoneno();
					objs[1] = userPhoneBo.getType();
					objs[2] = userid ;
					jdbcTemplate.update(INSERT_NEW_PHONE, objs);
				}
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
				
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(UPDATE_USER,new String[]{"id"});
				ps.setString(1, "");
				ps.setString(2, "");
				ps.setString(3, "buyer");
				ps.setString(4, null);
				ps.setString(5, PasswordUtil.getEncryptedPassword(userbo.getPassword()));
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
				ps.setLong(24, userbo.getUserid());
				return ps;
			}
		}, keyHolderprofile);
		
		return load(Long.toString(userBo.getUserid()));
		
	}

	
	
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

    private static class UserMapper implements RowMapper<UserBo> {

    	@Override
    	public UserBo mapRow(ResultSet rs, int rowNum) throws SQLException {

    		UserBo user = new UserBo();
			user.setUserid(rs.getInt("id"));
			user.setName(rs.getString("full_name"));
			user.setGender(rs.getString("gender"));
			user.setDateofbirth(rs.getDate("date_of_birth"));
			user.setPassword(rs.getString("password"));
			user.setSalutation(rs.getString("salutation"));
			
			return user;
    	}
    }

    private static class UserEmailMapper implements RowMapper<UserEmailBo> {

		@Override
		public UserEmailBo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserEmailBo userEmailBo = new UserEmailBo();
			userEmailBo.setEmail(rs.getString("email"));
			userEmailBo.setType(rs.getString("type"));

			return userEmailBo;
		}
    }

    private static class UserPhoneMapper implements RowMapper<UserPhoneBo> {

    	@Override
    	public UserPhoneBo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserPhoneBo userPhoneBo = new UserPhoneBo();
			userPhoneBo.setPhoneno(rs.getString("phone"));
			userPhoneBo.setType(rs.getString("type"));

			return userPhoneBo;
    	}
    }

	@Override
	public boolean changePassword(int userId, String newPassword) {
		int update = jdbcTemplate.update(UPDATE_PASSWORD_SQL, new Object[] {newPassword, userId});
		if (update > 0) {
			//update indicate number of rows affected by the above sql query. 
			//if we have managed to change data in a row means we have successfully updated the password.
			return true;
		}
		return false;
	}
}
