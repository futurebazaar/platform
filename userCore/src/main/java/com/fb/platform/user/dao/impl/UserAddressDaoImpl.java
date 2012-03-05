package com.fb.platform.user.dao.impl;

import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.user.dao.interfaces.UserAddressDao;
import com.fb.platform.user.domain.UserAddressBo;
import com.fb.platform.user.mapper.UserAddressMapper;

/**
 * @author kumar
 *
 */
public class UserAddressDaoImpl implements UserAddressDao {
	
	private static final String SELECT_USER_ADDRESS = "SELECT la.id as addressid,la.profile_id as userid,la.address,ua.type,lc.name as city"
			+ ",lcoun.name as country,ls.name as state,la.pincode from"
			+ "tinla.locations_address la join "
			+ "tinla.locations_state ls on la.state_id = ls.id"
			+ "tinla.locations_city lc on la.city_id = lc.id"
			+ "tinla.locations_country lcoun on la.country_id = lcoun.id"
			+ "where la.profile_id = ?";
	
	private static final String INSERT_NEW_ADDRESS = "INSERT into locations_address " +
			"(pincode," +
			"city_id," +
			"state_id," +
			"country_id," +
			"type,address," +
			"profile_id," +
			"account_id," +
			"uses,name," +
			"phone,email," +
			"defaddress," +
			"first_name," +
			"last_name)" +
			"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String SELECT_CITYID_BYNAME = "Select id from locations_city where name = '?'";
	private static final String SELECT_STATEID_BYNAME = "Select id from locations_state where name = '?'";
	private static final String SELECT_COUNTRYID_BYNAME = "Select id from locations_country where name = '?'";
	
	private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserAddressDao#load(int)
	 */
	@Override
	public Collection<UserAddressBo> load(int userid) {
		Collection<UserAddressBo> userAddressBos = jdbcTemplate.query(SELECT_USER_ADDRESS,new Object[]{userid} ,new UserAddressMapper());
		return userAddressBos;
		
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserAddressDao#add(com.fb.platform.user.domain.UserAddressBo)
	 */
	@Override
	public void add(UserAddressBo userAddressBo) {
		Object objs[] = new Object[15];
		objs[0]=userAddressBo.getPincode();
		objs[1]=getcityidbyname(userAddressBo.getCity());
		objs[2]=getstateidbyname(userAddressBo.getState());
		objs[3]=getcountryidbyname(userAddressBo.getCountry());
		objs[4]=userAddressBo.getAddresstype();
		objs[5]=userAddressBo.getAddress();
		objs[6]=userAddressBo.getUserid();
		objs[7]=null;
		objs[8]=0;
		objs[9]="";
		objs[10]="";
		objs[11]="";
		objs[12]=0;
		objs[13]="";
		objs[14]="";				
		jdbcTemplate.update(INSERT_NEW_ADDRESS, objs);

	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserAddressDao#update(com.fb.platform.user.domain.UserAddressBo)
	 */
	@Override
	public void update(UserAddressBo userAddressBo) {
		// TODO Auto-generated method stub

	}

	private int getcityidbyname(String city){
		int cityid = jdbcTemplate.queryForInt(SELECT_CITYID_BYNAME , new Object[] {city});
		return cityid;
	}
	

	private int getcountryidbyname(String country){
		int countryid = jdbcTemplate.queryForInt(SELECT_COUNTRYID_BYNAME , new Object[] {country});
		return countryid;
	}


	private int getstateidbyname(String state){
		int stateid = jdbcTemplate.queryForInt(SELECT_STATEID_BYNAME , new Object[] {state});
		return stateid;
	}

}
