/**
 * 
 */
package com.fb.platform.sso.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.PlatformException;
import com.fb.platform.sso.CryptoKeysTO;
import com.fb.platform.sso.dao.CryptoKeysDao;

/**
 * @author vinayak
 *
 */
public class CryptoKeysDaoJdbcImpl implements CryptoKeysDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(CryptoKeysDaoJdbcImpl.class);

	private static final String GET_CRYPTO_KEYS_QUERY = 
			"SELECT cryptokey_current, cryptokey_last, cryptokey_next FROM crypto_key";

	@Override
	public CryptoKeysTO loadCryptoKeys() {
		List<CryptoKeysTO> cryptoKeys = jdbcTemplate.query(GET_CRYPTO_KEYS_QUERY, new CryptoKeysMapper());

		if (cryptoKeys.size() == 1) {
			return cryptoKeys.get(0);
		}
		throw new PlatformException("Unable to load the crypto keys from DB.");
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static class CryptoKeysMapper implements RowMapper<CryptoKeysTO> {

		@Override
		public CryptoKeysTO mapRow(ResultSet rs, int rowNum) throws SQLException {

			CryptoKeysTO cryptoKeys = new CryptoKeysTO(
					rs.getString("cryptokey_current"), 
					rs.getString("cryptokey_last"),
					rs.getString("cryptokey_next"));

			return cryptoKeys;
		}
		
	}
}
