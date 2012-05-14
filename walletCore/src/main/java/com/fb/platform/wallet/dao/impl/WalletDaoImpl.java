package com.fb.platform.wallet.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.to.Money;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.util.PasswordUtil;
import com.fb.platform.wallet.dao.WalletDao;
import com.fb.platform.wallet.model.Wallet;

public class WalletDaoImpl implements WalletDao {

	private final String SELECT_WALLET_ID = "Select"
			+ " id,"
			+ "total_amount, "
			+ "cash_amount, "
			+ "gift_amount, "
			+ "refund_amount, "
			+ "created_on "
			+ "from wallets_wallet"
			+ "where id = ?";
	
	private final String UPDATE_WALLET_ID = "Update wallets_wallet set"
			+ "total_amount = ?, "
			+ "cash_amount = ?, "
			+ "gift_amount =?, "
			+ "refund_amount=?, "
			+ "modified_on = CURDATE() "
			+ "where id = ?";
	
	private final String GET_WALLET_ID_USER_CLIENT = "Select"
			+ "wallet_id " 
			+ "from users_wallet "
			+ "where user_id = ? and client_id = ?";
	
	private final String CREATE_NEW_WALLET = "Insert "
			+ "into wallets_wallet"
			+ "(total_amount,cash_amoount,gift_amount,refund_amount,created_on,modified_on) "
			+ "values(0,0,0,0,CURDATE(),CURDATE())" ;
	
	private final String CREATE_USER_CLIENT_WALLET = "Insert"
			+ "into users_wallet"
			+ "(user_id,client_id,wallet_id) "
			+ "values(?,?,?)";
	
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Wallet load(long walletId) {
		try{
			Wallet wallet = jdbcTemplate.queryForObject(SELECT_WALLET_ID, 
					new Object[] {walletId},
					new WalletMapper());
			// TODO Auto-generated method stub
			return wallet;
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public Wallet load(long userId, long clientId) {
		try{
			return load(jdbcTemplate.queryForLong(GET_WALLET_ID_USER_CLIENT,new Object[] {userId,clientId}));
		}catch (Exception e) {
			return create(userId, clientId);
		}
	}

	private Wallet create(long userId, long clientId) {
		try{
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_NEW_WALLET, new String[]{"id"});
					return ps;
				}
			}, keyHolder);
			long walletId = keyHolder.getKey().longValue();
			jdbcTemplate.update(CREATE_USER_CLIENT_WALLET, new Object[]{userId,clientId,walletId});
			return load(walletId);
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public Wallet update(Wallet wallet) {
		try {
			jdbcTemplate.update(UPDATE_WALLET_ID, new Object[] {wallet.getTotalAmount().getAmount(),
					wallet.getCashSubWallet().getAmount(),
					wallet.getGiftSubWallet().getAmount(),
					wallet.getRefundSubWallet().getAmount(),
					wallet.getId()});
			return load(wallet.getId());
		} catch (Exception e) {
			return null;
		}		
	}

	private static class WalletMapper implements RowMapper<Wallet> {

    	@Override
    	public Wallet mapRow(ResultSet rs, int rowNum) throws SQLException {

    		Wallet wallet = new Wallet();
    		wallet.setCreatedOn(new DateTime(rs.getDate("created_on")));
			wallet.setId(rs.getLong("id"));
			wallet.setTotalAmount(new Money(rs.getBigDecimal("total_amount")));
			wallet.setCashSubWallet(new Money(rs.getBigDecimal("cash_amount")));
			wallet.setRefundSubWallet(new Money(rs.getBigDecimal("refund_amount")));
			wallet.setGiftSubWallet(new Money(rs.getBigDecimal("gift_amount")));
			return wallet;
    	}
    }
}
