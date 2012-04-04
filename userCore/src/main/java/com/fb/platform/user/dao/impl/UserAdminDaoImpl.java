package com.fb.platform.user.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
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
	
	private static final Logger logger = Logger.getLogger(UserAdminDaoImpl.class);

	private static final String CHECK_EMAIL_IS_USERNAME_QUERY = "SELECT count(0) from users_email WHERE email = ?";
	private static final String CHECK_PHONE_IS_USERNAME_QUERY = "SELECT count(0) from users_phone WHERE phone = ?";
	private static final String CHECK_USERID_IS_USERNAME_QUERY = "SELECT count(0) from users_profile WHERE id = ?";

	private static final String SELECT_USER_FIELDS = "SELECT "
			+ "up.id as id, "
			+ "up.primary_phone, "
			+ "up.secondary_phone, "
			+ "up.buyer_or_seller, "
			+ "up.acquired_through_account_id, "
			+ "au.password, "
			+ "up.full_name, "
			+ "up.primary_email, "
			+ "up.gender, "
			+ "up.salutation, "
			+ "up.marketing_alerts, "
			+ "up.created_on, "
			+ "up.date_of_birth, "
			+ "up.is_agent, "
			+ "up.webpage, "
			+ "up.facebook, "
			+ "up.twitter, "
			+ "up.email_notification, "
			+ "up.sms_alert, "
			+ "up.profession, "
			+ "up.user_photo ";

	private static final String SELECT_USER_BY_EMAIL_QUERY =
			SELECT_USER_FIELDS
			+ " FROM users_profile up, users_email ue , auth_user au WHERE au.id = up.user_id AND up.id = ue.user_id AND ue.email = ?";

	private static final String SELECT_USER_BY_PHONE_QUERY =
			SELECT_USER_FIELDS
			+ " FROM users_profile up, users_phone uph , auth_user au WHERE au.id = up.user_id AND up.id = uph.user_id AND uph.phone = ?";

	private static final String SELECT_USER_BY_USER_ID =
			SELECT_USER_FIELDS
			+ " FROM users_profile up , auth_user au WHERE au.id = up.user_id AND up.id = ?";

	private static final String SELECT_EMAILS_BY_USER_ID = "SELECT email, type FROM users_email WHERE user_id = ?";

	private static final String SELECT_PHONES_BY_USER_ID = "SELECT phone, type FROM users_phone WHERE user_id = ?";

	private static final String SELECT_ALL_USERS = SELECT_USER_FIELDS + "from users_profile up , auth_user au WHERE au.id = up.user_id";

	private static final String INSERT_NEW_USER_AUTH = "INSERT "
			+ "into auth_user ("
			+ "username,"
			+ "first_name,"
			+ "last_name,"
			+ "email,"
			+ "password ,"
			+ "is_staff,"
			+ "is_active,"
			+ "is_superuser,"
			+ "last_login,"
			+ "date_joined ) values"
			+ "(?,?,?,?,?,?,?,?,?,?)";

	private static final String INSERT_NEW_USER = "INSERT "
			+ "into users_profile ("
			+ "user_id,"
			+ "primary_phone ,"
			+ "secondary_phone ,"
			+ "buyer_or_seller ,"
			+ "acquired_through_account_id,"
			+ "full_name,"
			+ "primary_email,"
			+ "secondary_email,"
			+ "gender,"
			+ "salutation ,"
			+ "salt,"
			+ "passcode,"
			+ "marketing_alerts,"
			+ "created_on,"
			+ "date_of_birth ,"
			+ "is_agent ,"
			+ "webpage ,"
			+ "facebook ,"
			+ "twitter ,"
			+ "email_notification ,"
			+ "sms_alert ,"
			+ "verify_code ,"
			+ "profession ,"
			+ "user_photo ,"
			+ "atg_username ,"
			+ "transaction_password ,"
			+ "atg_login,"
			+ "atg_password ," 
			+ "cod_status ,"
			+ "is_verified ,"
			+ "verification_code )"
			+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

	private static final String INSERT_NEW_EMAIL = "INSERT into users_email ("
			+ "email,"
			+ "type,"
			+ "user_id,"
			+ "cleaned_email ) "
			+ "values (?,?,?,?)";

	private static final String INSERT_NEW_PHONE = "INSERT into users_phone ("
			+ "phone,"
			+ "type,"
			+ "userid,"
			+ "is_verified,"
			+ "verified_on ,"
			+ "verification_code) "
			+ "values (?,?,?,?,?,?)";

	private static final String UPDATE_USER = "UPDATE users_profile set "
			+ "primary_phone = ?,"
			+ "secondary_phone = ?,"
			+ "buyer_or_seller = ?,"
			+ "acquired_through_account_id= ?,"
			+ "full_name= ?,"
			+ "primary_email= ?,"
			+ "secondary_email= ?,"
			+ "gender= ?,"
			+ "salutation= ? ,"
			+ "salt= ?,"
			+ "passcode= ?,"
			+ "marketing_alerts= ?,"
			+ "created_on= ?,"
			+ "date_of_birth= ? ,"
			+ "is_agent= ? ,"
			+ "webpage = ?,"
			+ "facebook = ?,"
			+ "twitter = ?,"
			+ "email_notification = ?,"
			+ "sms_alert = ?,"
			+ "verify_code = ?,"
			+ "profession = ?,"
			+ "user_photo = ?,"
			+ "atg_username = ?,"
			+ "transaction_password = ?,"
			+ "atg_login= ?,"
			+ "atg_password= ? "
			+ "where id = ?";

	private static final String SELECT_AUTH_USER_ID = "SELECT user_id from users_profile where "
			+ "id=?";

	private static final String UPDATE_PASSWORD_SQL = "UPDATE auth_user SET "
			+ "password = ? "
			+ "WHERE id = ?";

	private JdbcTemplate jdbcTemplate;

    /* (non-Javadoc)
     * @see com.fb.platform.user.dao.interfaces.UserDao#load(java.lang.String)
     */
    @Override
	public UserBo load(String key) {
    	logger.info("Getting user by the key name :::" + key);    	
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
		
		logger.info("Getting user by Emailid :::" + email);  
		UserBo user = jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL_QUERY,
				new Object[] {email},
				new UserMapper());
		user.setUserEmail(getEmailByUserid(user.getUserid()));
		user.setUserPhone(getPhoneByUserid(user.getUserid()));
		return user;
	}

	private UserBo getUserByPhone(String phone) {
		logger.info("Getting user by phone :::" + phone);  
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

	private List<UserEmailBo> getEmailByUserid(long userId) {

		List<UserEmailBo> userEmailBo = jdbcTemplate.query(SELECT_EMAILS_BY_USER_ID,
				new Object[] {userId},
				new UserEmailMapper());
		return userEmailBo;
	}

	private List<UserPhoneBo> getPhoneByUserid(long userId) {

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
		for (UserBo user : users) {
			user.setUserEmail(getEmailByUserid(user.getUserid()));
			user.setUserPhone(getPhoneByUserid(user.getUserid()));
		}
		return users;
	}
	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserDao#add(com.fb.platform.user.domain.UserBo)
	 */
	@Override
	public UserBo add(final UserBo userBo) {

		try {
			final KeyHolder keyHolderAuthUser = new GeneratedKeyHolder();
			final java.util.Date today = new java.util.Date();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(INSERT_NEW_USER_AUTH, new String[]{"id"});
					ps.setString(1, userBo.getUsername());
					ps.setString(2, userBo.getFirstname());
					ps.setString(3, userBo.getLastname());
					ps.setString(4, "");
					ps.setString(5, PasswordUtil.getEncryptedPassword(userBo.getPassword()));
					ps.setInt(6, 0);
					ps.setInt(7, 1);
					ps.setInt(8, 0);
					ps.setDate(9, new Date(today.getTime()));
					ps.setDate(10, new Date(today.getTime()));
					return ps;
				}
			}, keyHolderAuthUser);

			KeyHolder keyHolderprofile = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(INSERT_NEW_USER, new String[]{"id"});
					ps.setLong(1, (Long) keyHolderAuthUser.getKey());
					ps.setString(2, "");
					ps.setString(3, "");
					ps.setString(4, "buyer");
					ps.setString(5, null);
					ps.setString(6, userBo.getFirstname() + " " +  userBo.getLastname());
					ps.setString(7, "");
					ps.setString(8, "");
					ps.setString(9, (userBo.getGender() == null ? "" : userBo.getGender()));
					ps.setString(10, (userBo.getSalutation() == null ? "" : userBo.getSalutation()));
					ps.setString(11, "");
					ps.setString(12, "");
					ps.setString(13, "neutral");
					ps.setDate(14, new Date(today.getTime()));
					ps.setDate(15, (Date) userBo.getDateofbirth());
					ps.setBoolean(16, false);
					ps.setString(17, "");
					ps.setString(18, "");
					ps.setString(19, "");
					ps.setBoolean(20, false);
					ps.setBoolean(21, false);
					ps.setInt(22, 0);
					ps.setString(23, null);
					ps.setString(24, null);
					ps.setString(25, null);
					ps.setString(26, null);
					ps.setString(27, null);
					ps.setString(28, null);
					ps.setString(29, "neutral");
					ps.setInt(30, 0);
					ps.setString(31, null);
					return ps;
				}
			}, keyHolderprofile);

			long userid = (Long) keyHolderprofile.getKey();
			if (userBo.getUserEmail() != null) {
				for (UserEmailBo userEmailBo : userBo.getUserEmail()) {
					Object[] objs = new Object[4];
					objs[0] = userEmailBo.getEmail();
					objs[1] = userEmailBo.getType();
					objs[2] = userid;
					objs[3] = userEmailBo.getEmail();
					jdbcTemplate.update(INSERT_NEW_EMAIL, objs);
				}
			}
			if (userBo.getUserPhone() != null) {
				for (UserPhoneBo userPhoneBo : userBo.getUserPhone()) {
					Object[] objs = new Object[6];
					objs[0] = userPhoneBo.getPhoneno();
					objs[1] = userPhoneBo.getType();
					objs[2] = userid;
					objs[3] = 0;
					objs[4] = null;
					objs[5] = null;
					jdbcTemplate.update(INSERT_NEW_PHONE, objs);
				}
			}
			return load(Long.toString(userid));
		} catch (InvalidDataAccessApiUsageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserDao#update(com.fb.platform.user.domain.UserBo)
	 */
	@Override
	public UserBo update(final UserBo userBo) {

		final java.util.Date today = new java.util.Date();
		KeyHolder keyHolderprofile = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(UPDATE_USER, new String[]{"id"});
				ps.setString(1, "");
				ps.setString(2, "");
				ps.setString(3, "buyer");
				ps.setString(4, null);
				ps.setString(5, userBo.getFirstname() + " " +  userBo.getLastname());
				ps.setString(6, "");
				ps.setString(7, "");
				ps.setString(8, userBo.getGender());
				ps.setString(9, userBo.getSalutation());
				ps.setString(10, "");
				ps.setString(11, "");
				ps.setString(12, "neutral");
				ps.setDate(13, new Date(today.getTime()));
				ps.setDate(14, (Date) userBo.getDateofbirth());
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
				ps.setLong(28, userBo.getUserid());
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
		int user_id = jdbcTemplate.queryForInt(SELECT_AUTH_USER_ID , new Object[]{userId});
		int update = jdbcTemplate.update(UPDATE_PASSWORD_SQL, new Object[] {newPassword, user_id});
		if (update > 0) {
			//update indicate number of rows affected by the above sql query.
			//if we have managed to change data in a row means we have successfully updated the password.
			return true;
		}
		return false;
	}
}
