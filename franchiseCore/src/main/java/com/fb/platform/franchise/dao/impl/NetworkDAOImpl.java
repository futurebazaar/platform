/**
 * 
 */
package com.fb.platform.franchise.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.franchise.dao.interfaces.INetworkDAO;
import com.fb.platform.franchise.domain.NetworkBO;

/**
 * @author ashish
 *
 */
public class NetworkDAOImpl implements INetworkDAO {

	private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private Log log = LogFactory.getLog(NetworkDAOImpl.class);

	private static final String GET_NETWORK_QUERY = 
			"SELECT " +
			"	id, " +
			"	name, " +
			"	share, " +
			"	parent_network_id, " +
			"	clients_id, " +
			"	user_id, " +
			"FROM franchise_network where id = ?";
	
	private static final String GET_NETWORK_BY_PARENTNETWORK_QUERY = 
			"SELECT " +
			"	id, " +
			"	name, " +
			"	share, " +
			"	parent_network_id, " +
			"	clients_id, " +
			"	user_id, " +
			"FROM franchise_network where parent_network_id = ?";
	
	/* (non-Javadoc)
	 * @see com.fb.platform.franchise.dao.interfaces.INetworkDAO#getNetwork(java.lang.String)
	 */
	@Override
	public NetworkBO getNetwork(String networkID) {
		log.info("Getting the Netowrk with id : " + networkID);
		List<NetworkBO> netowrk = (List<NetworkBO>) jdbcTemplate.query(GET_NETWORK_QUERY, new Object [] {networkID}, new NetworkMapper());

		if (netowrk.size() == 1) {
			return netowrk.get(0);
		}
		log.warn("Unable to find the Netowrk with id : " + networkID);
		throw new RuntimeException("Unable to find the Netowrk with id : " + networkID); //TODO create custom exception
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.franchise.dao.interfaces.INetworkDAO#getNetworkByParentID(java.lang.String)
	 */
	@Override
	public Set<NetworkBO> getNetworkByParentID(String parentNetworkID) {
		log.info("Getting the Netowrk with parent network id : " + parentNetworkID);
		List<NetworkBO> netowrkList = (List<NetworkBO>) jdbcTemplate.query(GET_NETWORK_BY_PARENTNETWORK_QUERY, new Object [] {parentNetworkID}, new NetworkMapper());

		if (netowrkList.size() >= 1) {
			return new HashSet<NetworkBO>(netowrkList);
		}
		log.warn("Unable to find the Netowrk with parent network id : " + parentNetworkID);
		throw new RuntimeException("Unable to find the Netowrk with parent network id : " + parentNetworkID); //TODO create custom exception
	}

	private static class NetworkMapper implements RowMapper<NetworkBO>{

		@Override
		public NetworkBO mapRow(ResultSet rs, int rowNum) throws SQLException {	
			NetworkBO networkBO = new NetworkBO();

			/*
			 */
			
			networkBO.setNetworkID(rs.getInt("id"));
			networkBO.setClientsID(rs.getInt("clients_id"));
			networkBO.setUserID(rs.getInt("user_id"));
			networkBO.setName(rs.getString("name"));
			networkBO.setParentNetworkID(rs.getInt("parent_network_id"));
			networkBO.setShare(rs.getDouble("share"));
			
			return networkBO;
		}
	}
}

