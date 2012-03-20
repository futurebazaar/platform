package com.fb.platform.franchise.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.franchise.dao.interfaces.IFranchiseDAO;
import com.fb.platform.franchise.domain.FranchiseBO;
import com.fb.platform.franchise.mapper.FranchiseMapper;

public class FranchiseDAOImpl implements IFranchiseDAO {
	
	private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private Log log = LogFactory.getLog(FranchiseDAOImpl.class);

	private static final String GET_FRANCHISE_QUERY = 
			"SELECT " +
			"	id, " +
			"	is_active, " +
			"	user_id, " +
			"	network_id, " +
			"	role, " +
			"FROM franchise_franchise where id = ?";
	
	@Override
	public FranchiseBO getFranchise(int key) {
		log.info("Getting the Franchise with id : " + key);
		List<FranchiseBO> franchise = (List<FranchiseBO>) jdbcTemplate.query(GET_FRANCHISE_QUERY, new Object [] {key}, new FranchiseMapper());

		if (franchise.size() == 1) {
			return franchise.get(0);
		}
		log.warn("Unable to find the Franchise with id : " + key);
		throw new RuntimeException("Unable to find the Franchise with id : " + key); //TODO create custom exception
	}
	
	@Override
	public FranchiseBO getFranchiseNetwork(int franchiseID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addFranchise() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFranchise(FranchiseBO franchiseBO) {
		// TODO Auto-generated method stub
		return false;
	}
}
