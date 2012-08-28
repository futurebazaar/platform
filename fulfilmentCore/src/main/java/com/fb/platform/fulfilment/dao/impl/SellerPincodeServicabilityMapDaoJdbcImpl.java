package com.fb.platform.fulfilment.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;

import com.fb.platform.fulfilment.dao.SellerPincodeServicabilityMapDao;
import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;
import com.fb.platform.fulfilment.service.NonServicablePincodeException;

public class SellerPincodeServicabilityMapDaoJdbcImpl implements SellerPincodeServicabilityMapDao{
	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(SellerPincodeServicabilityMapDao.class);

	private static final String GET_SELLER_FOR_PINCODE_QUERY = 
		"SELECT " +
		"	pincode," + 
		"	seller_id " +
		"FROM fulfillment_sellerpincodemap " +
		"WHERE pincode = ?";

	@Override
	public SellerPincodeServicabilityMap getSellersForPincode(String pincode) {
		
		SellerPincodeServicabilityMap sellerPincodeMap = null;
		SellerPincodeMapRowCallbackHandler spmrch = new SellerPincodeMapRowCallbackHandler();
		jdbcTemplate.query(GET_SELLER_FOR_PINCODE_QUERY, spmrch, pincode);
		
		if(!spmrch.pincodeFound){
			if(log.isDebugEnabled()) {
				log.debug("No seller found for pincode -" + pincode);
			}
			throw new NonServicablePincodeException("No seller found for Pincode " + pincode);
		}

		sellerPincodeMap = spmrch.sellerPincodeMap;
		return sellerPincodeMap;
	}

	private static class SellerPincodeMapRowCallbackHandler extends RowCountCallbackHandler {

		private SellerPincodeServicabilityMap sellerPincodeMap = new SellerPincodeServicabilityMap(); 
		private boolean pincodeFound = false;
		@Override
		public void processRow(ResultSet rs, int rowNum) throws SQLException {
			sellerPincodeMap.setPincode(rs.getString("pincode"));
			List<Integer> seller_id_list = sellerPincodeMap.getSellerId();
			seller_id_list.add(rs.getInt("seller_id"));
			sellerPincodeMap.setSellerId(seller_id_list);
			pincodeFound = true;
		}
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
