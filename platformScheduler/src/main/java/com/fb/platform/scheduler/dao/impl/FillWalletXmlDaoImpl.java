package com.fb.platform.scheduler.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.to.Money;
import com.fb.platform.scheduler.dao.FillWalletXmlDao;
import com.fb.platform.scheduler.to.FillWalletXmlTO;

public class FillWalletXmlDaoImpl implements FillWalletXmlDao {
	
	private static Log logger = LogFactory.getLog(FillWalletXmlDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	
	private static final String LOAD_FILL_WALLET_XMLS = 
			"SELECT " +
			"id, " +
		    "wallet_id, " + 
		    "user_id, " +
		    "payment_mode, " +
		    "amount, " +
			"wallet_fill_xml, " +
			"created_date, " +
			"status " +
			"FROM owallets_fill_xml WHERE " +
			"status in ( ? ) ORDER BY id ASC";
	
	private static final String UPDATE_FILL_WALLET_XML = 
			"UPDATE orders_orderxml SET " +
			"status = ? " +
			"WHERE id = ? ";

	@Override
	public List<FillWalletXmlTO> getFillWalletXmlLst() {
		List<FillWalletXmlTO> fillWalletXmlTOList = new ArrayList<FillWalletXmlTO>();
		try{ 
			logger.info("Getting fill wallet Xmls");
			fillWalletXmlTOList = jdbcTemplate.query(LOAD_FILL_WALLET_XMLS, new Object[] {false}, new FillWalletXmlMapper());
		} catch (Exception e) {
			logger.error("Exception occured while getting XMls", e);
		}
		logger.info("fill wallet Xml lists :" + fillWalletXmlTOList);
		return fillWalletXmlTOList;
	}

	@Override
	public int updateFillWalletXml(long id, boolean status) {
		logger.info("Updating fill wallet Xml: " + id);
		try {
			return jdbcTemplate.update(UPDATE_FILL_WALLET_XML, new Object[] {status,id});
		} catch (Exception e) {
			logger.error("Excetion occured while update Id: " + id, e);
			return 0;
		}
	}
	
	private static class FillWalletXmlMapper implements RowMapper<FillWalletXmlTO> {

		@Override
		public FillWalletXmlTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			FillWalletXmlTO fillWalletXmlTO = new FillWalletXmlTO();
			fillWalletXmlTO.setId(rs.getLong("id"));
			fillWalletXmlTO.setWalletId(rs.getLong("wallet_id"));
			fillWalletXmlTO.setUserId(rs.getLong("user_id"));
			fillWalletXmlTO.setPaymentMode(rs.getString("payment_mode"));
			fillWalletXmlTO.setAmount(new Money(rs.getBigDecimal("amount")));
			fillWalletXmlTO.setWalletFillXml(rs.getString("wallet_fill_xml"));
			fillWalletXmlTO.setCreatedDate(new DateTime(rs.getDate("created_date")));
			fillWalletXmlTO.setStatus(rs.getBoolean("status"));
			return fillWalletXmlTO;
		}
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
