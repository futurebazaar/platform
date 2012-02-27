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
	
	private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserAddressDao#load(int)
	 */
	@Override
	public Collection<UserAddressBo> load(int userid) {
		String sql = "select la.id as addressid,la.profile_id as userid,la.address,ua.type,lc.name as city"
				+ ",lcoun.name as country,ls.name as state,la.pincode from"
				+ "tinla.locations_address la join "
				+ "tinla.locations_state ls on la.state_id = ls.id"
				+ "tinla.locations_city lc on la.city_id = lc.id"
				+ "tinla.locations_country lcoun on la.country_id = lcoun.id"
				+ "where la.profile_id = " + userid;
		Collection<UserAddressBo> userAddressBos = jdbcTemplate.query(sql, new UserAddressMapper());
		return userAddressBos;
		
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserAddressDao#add(com.fb.platform.user.domain.UserAddressBo)
	 */
	@Override
	public void add(UserAddressBo userAddressBo) {
		String sql = "Insert into locations_address (pincode,city_id,state_id,country_id,type,address,profile_id,account_id,uses,name,phone,email,defaddress,first_name,last_name)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
		jdbcTemplate.update(sql, objs);

	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.dao.interfaces.UserAddressDao#update(com.fb.platform.user.domain.UserAddressBo)
	 */
	@Override
	public void update(UserAddressBo userAddressBo) {
		// TODO Auto-generated method stub

	}

	private int getcityidbyname(String city){
		String sql = "Select id from locations_city where name = '" + city + "'";
		int cityid = jdbcTemplate.queryForInt(sql);
		return cityid;
	}
	

	private int getcountryidbyname(String country){
		String sql = "Select id from locations_country where name = '" + country + "'";
		int countryid = jdbcTemplate.queryForInt(sql);
		return countryid;
	}


	private int getstateidbyname(String state){
		String sql = "Select id from locations_state where name = '" + state + "'";
		int stateid = jdbcTemplate.queryForInt(sql);
		return stateid;
	}

}
