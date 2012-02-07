/**
 * 
 */
package com.fb.platform.dao.account.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.account.Client;
import com.fb.platform.dao.account.ClientDao;

/**
 * @author vinayak
 *
 */
public class ClientDaoJdbcImpl implements ClientDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(ClientDaoJdbcImpl.class);

	private static final String GET_CLIENT_QUERY = 
			"SELECT " +
			"	id, " +
			"	name, " +
			"	confirmed_order_email, " +
			"	pending_order_email, " +
			"	share_product_email, " +
			"	signature, " +
			"	pending_order_helpline, " +
			"	confirmed_order_helpline, " +
			"	sms_mask, " +
			"	noreply_email, " +
			"	feedback_email, " +
			"	promotions_email, " +
			"	sale_pricelist, " +
			"	list_pricelist, " +
			"	clientdomain_name, " +
			"	slug " +
			"FROM accounts_client where id = ?";

	private static final String GET_CLIENT_BY_NAME_QUERY =
			"SELECT " +
			"	id, " +
			"	name, " +
			"	confirmed_order_email, " +
			"	pending_order_email, " +
			"	share_product_email, " +
			"	signature, " +
			"	pending_order_helpline, " +
			"	confirmed_order_helpline, " +
			"	sms_mask, " +
			"	noreply_email, " +
			"	feedback_email, " +
			"	promotions_email, " +
			"	sale_pricelist, " +
			"	list_pricelist, " +
			"	clientdomain_name, " +
			"	slug " +
			"FROM accounts_client where name = ?";

	private static final String CREATE_CLIENT_QUERY = 
			"INSERT INTO accounts_client ( " +
			"	name, " +
			"	confirmed_order_email, " +
			"	pending_order_email, " +
			"	share_product_email, " +
			"	signature, " +
			"	pending_order_helpline, " +
			"	confirmed_order_helpline, " +
			"	sms_mask, " +
			"	noreply_email, " +
			"	feedback_email, " +
			"	promotions_email, " +
			"	sale_pricelist, " +
			"	list_pricelist, " +
			"	clientdomain_name, " +
			"	slug) " +
			"values ( " +
			"	?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/* (non-Javadoc)
	 * @see com.fb.platform.dao.account.ClientDao#get(java.lang.Integer)
	 */
	@Override
	public Client get(Integer id) {
		log.info("Getting the Client with id : " + id);
		List<Client> clients = (List<Client>) jdbcTemplate.query(GET_CLIENT_QUERY, new Object [] {id}, new ClientMapper());

		if (clients.size() == 1) {
			return clients.get(0);
		}
		log.warn("Unable to find the Client with id : " + id);
		throw new RuntimeException(); //TODO create custom exception
	}

	private Client get(String name) {
		log.info("Getting the Client with name : " + name);
		List<Client> clients = (List<Client>) jdbcTemplate.query(GET_CLIENT_BY_NAME_QUERY, new Object [] {name}, new ClientMapper());

		if (clients.size() == 1) {
			return clients.get(0);
		}
		log.warn("Unable to find the Client with name : " + name);
		throw new RuntimeException(); //TODO create custom exception
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.dao.account.ClientDao#create(com.fb.platform.account.Client)
	 */
	@Override
	public Client create(Client client) {

		/*
			"	name, " +
			"	confirmed_order_email, " +
			"	pending_order_email, " +
			"	share_product_email, " +
			"	signature, " +
			"	pending_order_helpline, " +
			"	confirmed_order_helpline, " +
			"	sms_mask, " +
			"	noreply_email, " +
			"	feedback_email, " +
			"	promotions_email, " +
			"	sale_pricelist, " +
			"	list_pricelist, " +
			"	clientdomain_name, " +
			"	slug) " +
		 */
		int rowsCreated = jdbcTemplate.update(CREATE_CLIENT_QUERY,
				client.getName(),
				client.getConfirmedOrderEmail(),
				client.getPendingOrderEmail(), 
				client.getShareProductEmail(), 
				client.getSignature(), 
				client.getPendingOrderHelpline(),
				client.getConfirmedOrderHelpline(), 
				client.getSmsMask(), 
				client.getNoReplyEmail(), 
				client.getFeedbackEmail(), 
				client.getPromotionsEmail(), 
				client.getSalePriceList(), 
				client.getListPriceList(), 
				client.getClientDomainName(),
				client.getSlug());

		if (rowsCreated == 0) {
			throw new RuntimeException(); //TODO throw custom exception
		}

		return get(client.getName());
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static class ClientMapper implements RowMapper<Client> {

		@Override
		public Client mapRow(ResultSet rs, int rowNum) throws SQLException {	
			Client client = new Client();

			/*
				id, name, confirmed_order_email, pending_order_email, share_product_email, signature, pending_order_helpline, confirmed_order_helpline, 
				sms_mask, noreply_email, feedback_email, promotions_email, sale_pricelist, list_pricelist, clientdomain_name, slug
			 */
			client.setId(rs.getInt("id"));
			client.setClientDomainName(rs.getString("clientdomain_name"));
			client.setConfirmedOrderEmail(rs.getString("confirmed_order_email"));
			client.setConfirmedOrderHelpline(rs.getString("confirmed_order_helpline"));
			client.setFeedbackEmail(rs.getString("feedback_email"));
			client.setListPriceList(rs.getString("list_pricelist"));
			client.setName(rs.getString("name"));
			client.setNoReplyEmail(rs.getString("noreply_email"));
			client.setPendingOrderEmail(rs.getString("pending_order_email"));
			client.setPendingOrderHelpline(rs.getString("pending_order_helpline"));
			client.setPromotionsEmail(rs.getString("promotions_email"));
			client.setSalePriceList(rs.getString("sale_pricelist"));
			client.setShareProductEmail(rs.getString("share_product_email"));
			client.setSignature(rs.getString("signature"));
			client.setSlug(rs.getString("slug"));
			client.setSmsMask(rs.getString("sms_mask"));

			return client;
		}
	}
}
