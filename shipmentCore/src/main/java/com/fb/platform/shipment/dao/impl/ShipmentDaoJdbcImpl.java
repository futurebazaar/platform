package com.fb.platform.shipment.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.to.Money;
import com.fb.platform.shipment.dao.ShipmentDao;
import com.fb.platform.shipment.to.ParcelItem;

 /**
 * @author nehaga
 *
 */
public class ShipmentDaoJdbcImpl implements ShipmentDao {

	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	/**
	 * QUERY:
	 * SELECT
			CONCAT_WS(' ',la.first_name, la.last_name) as name, la.address as address,lci.name as cityName,ls.name as stateName, lco.name as countryName, la.pincode as pincode,la.phone as phone,oo.payable_amount as payableAmount,oo.payment_mode as paymentMode
	    FROM
			orders_deliveryinfo od 
			INNER JOIN orders_order oo ON od.order_id = oo.id 
			INNER JOIN locations_address la ON la.id = od.address_id 
			INNER JOIN locations_state ls ON ls.id = la.state_id 
			INNER JOIN locations_country lco ON lco.id = la.country_id 
			INNER JOIN locations_city lci ON lci.id = la.city_id 
		WHERE
			oo.reference_order_id = ? 
	 */
	
	private static final String FETCH_PARCEL_DETAILS = "SELECT " +
			"CONCAT_WS(' ',la.first_name, la.last_name) as name, " +
			"la.address as address, " +
			"lci.name as cityName, " +
			"ls.name as stateName, " +
			"lco.name as countryName, " +
			"la.pincode as pincode, " +
			"la.phone as phone, " +
			"oo.payable_amount as payableAmount, " +
			"oo.payment_mode as paymentMode " +
			"FROM " +
			"orders_deliveryinfo od " +
			"INNER JOIN orders_order oo ON od.order_id = oo.id " +
			"INNER JOIN locations_address la ON la.id = od.address_id " +
			"INNER JOIN locations_state ls ON ls.id = la.state_id " +
			"INNER JOIN locations_country lco ON lco.id = la.country_id " +
			"INNER JOIN locations_city lci ON lci.id = la.city_id " +
			"WHERE " +
			"oo.reference_order_id = ? ";
	
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Returns parcel details for items specified in gatePass.xml
	 * @param gatePassItem
	 * @return
	 */
	@Override
	public ParcelItem getParcelDetails(String refOrderId) {
		infoLog.info("Getting parcel details for order : " + refOrderId);
		ParcelItem parcelItem = jdbcTemplate.queryForObject(FETCH_PARCEL_DETAILS,
				new Object[] {refOrderId},
				new ParcelItemMapper());
		//TODO check for null values and constraints
		infoLog.info("Parcel details : " + parcelItem.toString());
		return parcelItem;
	}
	
	private static class ParcelItemMapper implements RowMapper<ParcelItem> {

    	@Override
    	public ParcelItem mapRow(ResultSet rs, int rowNum) throws SQLException {

    		ParcelItem parcelItem = new ParcelItem();
    		parcelItem.setAddress(rs.getString("address"));
    		parcelItem.setAmountPayable(new Money(rs.getBigDecimal("payableAmount")));
    		parcelItem.setCity(rs.getString("cityName"));
    		parcelItem.setCountry(rs.getString("countryName"));
    		parcelItem.setPaymentMode(rs.getString("paymentMode"));
    		parcelItem.setPhoneNumber(rs.getLong("phone"));
    		parcelItem.setPincode(rs.getString("pincode"));
    		parcelItem.setState(rs.getString("stateName"));
    		parcelItem.setCustomerName(rs.getString("name"));
			return parcelItem;
    	}
    }
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
