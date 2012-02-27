/**
 * 
 */
package com.fb.platform.sso.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;
import com.fb.platform.sso.dao.SingleSignonDao;

/**
 * @author vinayak
 *
 */
public class SingleSignonDaoJdbcImpl implements SingleSignonDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(SingleSignonDaoJdbcImpl.class);

	public static final String CREATE_SSO_SESSION_QUERY = 
			"INSERT INTO sso_session (" +
			"ip_address," +
			"user_id," +
			"timestamp_login," +
			"timestamp_logout," +
			"timestamp_lastTransaction," +
			"session_id," +
			"app_data" +
			")" +
			"VALUES" +
			"(" +
			"?,?,?,?,?,?,?" +
			")";

	public static final String EXPIRE_SSO_SESSION_SQL = "UPDATE sso_session SET timestamp_logout = now() where session_id = ?";

	public static final String UPDATE_SSO_SESSION_SQL = "UPDATE sso_session SET timestamp_lastTransaction = now() where session_id = ?";

	public static final String RETRIEVE_SSO_SESSION_QUERY = 
			"SELECT " +
			"ip_address, " +
			"user_id, " +
			"session_id, " +
			"app_data " +
			"FROM sso_session WHERE session_id = ? and timestamp_logout is null";

	/* (non-Javadoc)
	 * @see com.fb.platform.sso.dao.SingleSignonDAO#createSSOSession(com.fb.platform.sso.SSOSessionTO)
	 */
	@Override
	public void createSSOSession(SSOSessionTO session, SSOSessionId sessionId) {
		Timestamp currentTime = new java.sql.Timestamp(System.currentTimeMillis());
		jdbcTemplate.update(CREATE_SSO_SESSION_QUERY, 
				session.getIpAddress(),
				session.getUserId(),
				currentTime,
				null,
				currentTime,
				sessionId.getSessionId(),
				session.getAppData());
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sso.dao.SingleSignonDAO#logoutSSOSession(com.fb.platform.sso.SSOSessionId)
	 */
	@Override
	public void logoutSSOSession(SSOSessionId sessionId) {
		jdbcTemplate.update(EXPIRE_SSO_SESSION_SQL,
				sessionId.getSessionId());
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sso.dao.SingleSignonDAO#updateSSOSession(com.fb.platform.sso.SSOSessionId)
	 */
	@Override
	public void updateSSOSession(SSOSessionId sessionId) {
		jdbcTemplate.update(UPDATE_SSO_SESSION_SQL,
				sessionId.getSessionId());
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sso.dao.SingleSignonDAO#loadSessionDetails(com.fb.platform.sso.SSOSessionId)
	 */
	@Override
	public SSOSessionTO loadSessionDetails(SSOSessionId sessionId) {
		List<SSOSessionTO> sessions = jdbcTemplate.query(RETRIEVE_SSO_SESSION_QUERY, new Object [] {sessionId.getSessionId()}, new SSOMapper());
		if (sessions.size() == 1) {
			return sessions.get(0);
		}
		//TODO decide whether to throw exception, probably not.
		return null;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static class SSOMapper implements RowMapper<SSOSessionTO> {

		@Override
		public SSOSessionTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SSOSessionTO session = new SSOSessionTO();

			session.setAppData(rs.getString("app_data"));
			session.setIpAddress(rs.getString("ip_address"));
			session.setUserId(rs.getInt("user_id"));
			
			return session;
		}
	}
}
