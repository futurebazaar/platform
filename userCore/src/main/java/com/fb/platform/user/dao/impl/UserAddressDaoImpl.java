package com.fb.platform.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.user.dao.interfaces.UserAddressDao;
import com.fb.platform.user.domain.UserAddressBo;

/**
 * @author kumar
 *
 */
public class UserAddressDaoImpl implements UserAddressDao {
	
	private static final Log logger = LogFactory.getLog(UserAddressDaoImpl.class);
	
	private static final String SELECT_USER_ADDRESS = "SELECT "
			+ "la.id as addressid, "
			+ "la.profile_id as userid, "
			+ "la.address, "
			+ "la.type, "
			+ "lc.name as city, "
			+ "lcoun.name as country, "
			+ "ls.name as state, "
			+ "la.pincode from "
			+ "locations_address la join "
			+ "locations_state ls "
			+ "on la.state_id = ls.id "
			+ "join locations_city lc "
			+ "on la.city_id = lc.id "
			+ "join locations_country lcoun "
			+ "on la.country_id = lcoun.id "
			+ "where la.profile_id = ?";

	private static final String INSERT_NEW_ADDRESS = "INSERT into locations_address "
			+ "(pincode,"
			+ "city_id,"
			+ "state_id,"
			+ "country_id,"
			+ "type,"
			+ "address,"
			+ "profile_id,"
			+ "account_id,"
			+ "created_on, "
			+ "uses,"
			+ "name,"
			+ "phone,"
			+ "email,"
			+ "defaddress,"
			+ "first_name,"
			+ "last_name)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String UPDATE_ADDRESS = "UPDATE locations_address set "
			+ "pincode = ? ,"
			+ "city_id = ? ,"
			+ "state_id = ? ,"
			+ "country_id = ? ,"
			+ "type,address = ? ,"
			+ "profile_id = ? ,"
			+ "account_id = ? ,"
			+ "uses = ? ,"
			+ "name = ? ,"
			+ "phone = ? ,"
			+ "email = ? ,"
			+ "defaddress = ? ,"
			+ "first_name = ? ,"
			+ "last_name = ? "
			+ "where addressid = ?";

	private static final String SELECT_CITYID_BYNAME = "Select id from locations_city where name = ? LIMIT 0,1";
	private static final String SELECT_STATEID_BYNAME = "Select id from locations_state where name = ? LIMIT 0,1";
	private static final String SELECT_COUNTRYID_BYNAME = "Select id from locations_country where name = ? LIMIT 0,1";

	private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserAddressDao#load(int)
	 */
	@Override
	public Collection<UserAddressBo> load(int userid) {
		if(logger.isDebugEnabled()) {
			logger.debug("Getting address for user id : " + userid);
		}
		Collection<UserAddressBo> userAddressBos = jdbcTemplate.query(SELECT_USER_ADDRESS, new Object[]{userid} , new UserAddressMapper());
		return userAddressBos;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserAddressDao#add(com.fb.platform.user.domain.UserAddressBo)
	 */
	@Override
	public void add(UserAddressBo userAddressBo) {
		if(logger.isDebugEnabled()) {
			logger.debug("Insert new address : " + userAddressBo.toString());
		}
		Object[] objs = new Object[16];
		objs[0] = userAddressBo.getPincode();
		objs[1] = getcityidbyname(userAddressBo.getCity());
		objs[2] = getstateidbyname(userAddressBo.getState());
		objs[3] = getcountryidbyname(userAddressBo.getCountry());
		objs[4] = userAddressBo.getAddresstype();
		objs[5] = userAddressBo.getAddress();
		objs[6] = userAddressBo.getUserid();
		objs[7] = null;
		objs[8] = "2012-03-19 17:16:16";
		objs[9] = 0;
		objs[10] = "";
		objs[11] = "";
		objs[12] = "";
		objs[13] = 0;
		objs[14] = "";
		objs[15] = "";
		jdbcTemplate.update(INSERT_NEW_ADDRESS, objs);

	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserAddressDao#update(com.fb.platform.user.domain.UserAddressBo)
	 */
	@Override
	public void update(UserAddressBo userAddressBo) {
		if(logger.isDebugEnabled()) {
			logger.debug("Updating address with address id : " + userAddressBo.getAddressid() + " \n New Address : " + userAddressBo.toString() );
		}
		Object[] objs = new Object[15];
		objs[0] = userAddressBo.getPincode();
		objs[1] = getcityidbyname(userAddressBo.getCity());
		objs[2] = getstateidbyname(userAddressBo.getState());
		objs[3] = getcountryidbyname(userAddressBo.getCountry());
		objs[4] = userAddressBo.getAddresstype();
		objs[5] = userAddressBo.getAddress();
		objs[6] = userAddressBo.getUserid();
		objs[7] = null;
		objs[8] = 0;
		objs[9] = "";
		objs[10] = "";
		objs[11] = "";
		objs[12] = 0;
		objs[13] = "";
		objs[14] = "";
		objs[15] = userAddressBo.getAddressid();
		jdbcTemplate.update(UPDATE_ADDRESS, objs);
	}

	private int getcityidbyname(String city) {
		if(logger.isDebugEnabled()) {
			logger.debug("Getting the city id for the city : " + city );
		}
		int cityid = jdbcTemplate.queryForInt(SELECT_CITYID_BYNAME , new Object[] {city});
		return cityid;
	}

	private int getcountryidbyname(String country) {
		if(logger.isDebugEnabled()) {
			logger.debug("Getting the country id for the country : " + country );
		}
		int countryid = jdbcTemplate.queryForInt(SELECT_COUNTRYID_BYNAME , new Object[] {country});
		return countryid;
	}

	private int getstateidbyname(String state) {
		if(logger.isDebugEnabled()) {
			logger.debug("Getting the state id for the state : " + state );
		}
		int stateid = jdbcTemplate.queryForInt(SELECT_STATEID_BYNAME , new Object[] {state});
		return stateid;
	}

	private static class UserAddressMapper implements RowMapper<UserAddressBo> {

		@Override
		public UserAddressBo mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			try {
				UserAddressBo useraddress = new UserAddressBo();
				useraddress.setAddressid(rs.getLong("addressid"));
				useraddress.setUserid(rs.getInt("userid"));
				useraddress.setAddress(rs.getString("address"));
				useraddress.setAddresstype(rs.getString("type"));
				useraddress.setCity(rs.getString("city"));
				useraddress.setCountry(rs.getString("country"));
				useraddress.setState(rs.getString("state"));
				useraddress.setPincode(rs.getString("pincode"));
				return useraddress;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
