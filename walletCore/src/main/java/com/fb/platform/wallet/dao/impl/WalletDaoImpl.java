package com.fb.platform.wallet.dao.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import sun.misc.BASE64Encoder;

import com.fb.commons.communication.to.SmsTO;
import com.fb.commons.mail.MailSender;
import com.fb.commons.mom.to.MailTO;
import com.fb.commons.sms.SmsSender;
import com.fb.commons.to.Money;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;
import com.fb.platform.user.manager.interfaces.UserAdminService;
import com.fb.platform.wallet.dao.WalletDao;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.service.exception.WalletCreationError;
import com.fb.platform.wallet.service.exception.WalletNotFoundException;
import com.fb.platform.wallet.util.MailHelper;
import com.fb.platform.wallet.util.SmsHelper;

public class WalletDaoImpl implements WalletDao {
	
	private final String SELECT_WALLET_ID = "Select "
			+ "id, "
			+ "total_amount, "
			+ "cash_amount, "
			+ "gift_amount, "
			+ "refund_amount, "
			+ "created_on, "
			+ "wallet_password "
			+ "from wallets_wallet "
			+ "where id = ?";
	private final String UPDATE_WALLET_ID = "Update wallets_wallet set "
			+ "total_amount = ?, "
			+ "cash_amount = ?, "
			+ "gift_amount =?, "
			+ "refund_amount=?, "
			+ "modified_on = CURDATE() "
			+ "where id = ?";
	private final String GET_WALLET_ID_USER_CLIENT = "Select "
			+ "wallet_id " 
			+ "from users_wallet "
			+ "where user_id = ? and client_id = ?";
	private final String CREATE_NEW_WALLET = "Insert "
			+ "into wallets_wallet"
			+ "(total_amount,cash_amount,gift_amount,refund_amount,created_on,modified_on,wallet_password) "
			+ "values (0,0,0,0,CURDATE(),CURDATE(),?)" ;
	private final String CREATE_USER_CLIENT_WALLET = "Insert "
			+ "into users_wallet "
			+ "(user_id,client_id,wallet_id) "
			+ "values (?,?,?)";
	private JdbcTemplate jdbcTemplate;
	private Log log = LogFactory.getLog(WalletDaoImpl.class);
	
	@Autowired
	private UserAdminService userAdminService;
	
	@Autowired
	private MailSender mailSender = null;

	@Autowired
	private SmsSender smsSender = null;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}
	
	
	@Override
	public Wallet load(long walletId) {
		try{
			Wallet wallet = jdbcTemplate.queryForObject(SELECT_WALLET_ID, 
					new Object[] {walletId},
					new WalletMapper());
			return wallet;
		}catch (EmptyResultDataAccessException e) {
			throw new WalletNotFoundException();
		}
	}

	@Override
	public Wallet load(long userId, long clientId ,boolean isCreateNew) {
		try{
			Wallet wallet = load(jdbcTemplate.queryForLong(GET_WALLET_ID_USER_CLIENT,new Object[] {userId,clientId}));
			return wallet;
		}catch (EmptyResultDataAccessException e) {
			if(isCreateNew){
				Wallet wallet = create(userId, clientId);
				if(wallet != null){
					return wallet;
				}else{
					throw new WalletCreationError("Either UserId or ClientId is wrong");
				}
			}else{
				throw new WalletNotFoundException();
			}
		}
	}
	
	private Wallet create(long userId, long clientId) {
		try{
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			String randomPassword = RandomStringUtils.random(6, true, true).toUpperCase();
			try {
				UserBo user = userAdminService.getUserByUserId(safeLongToInt(userId));
				for(UserEmailBo userEmailBo : user.getUserEmail()){
					if(userEmailBo.isVerified()){
						MailTO message = MailHelper.createMailTO(userEmailBo.getEmail(), randomPassword, user.getName());
						mailSender.send(message);
					}
				}
				for(UserPhoneBo userPhoneBo : user.getUserPhone()){
					if(userPhoneBo.isVerified()){
						SmsTO sms = SmsHelper.createSmsTO(userPhoneBo.getPhoneno(), randomPassword, user.getName());
						smsSender.send(sms);
					}
				}
			} catch (Exception e){}
			final String passwordEncrypted = encrypt(randomPassword);
			log.info("Wallet password getnerated for userId: " + userId + " clentId :" + clientId + " :::::" + randomPassword);
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_NEW_WALLET, new String[]{"id"});
					ps.setString(1,passwordEncrypted);
					return ps;
				}
			}, keyHolder);
			long walletId = keyHolder.getKey().longValue();
			jdbcTemplate.update(CREATE_USER_CLIENT_WALLET, new Object[]{userId,clientId,walletId});
			return load(walletId);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setUserAdminService(UserAdminService userAdminService) {
		this.userAdminService = userAdminService;
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
		}catch (WalletNotFoundException e) {
			log.warn( " Tried updating the wallet with id " + wallet.getId() + " ,but entry not found.");
			return null;
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
	}
	 
	private static class WalletMapper implements RowMapper<Wallet> {

    	@Override
    	public Wallet mapRow(ResultSet rs, int rowNum) throws SQLException {

    		Wallet wallet = new Wallet();
    		wallet.setCreatedOn(new DateTime(rs.getTimestamp("created_on")));
			wallet.setId(rs.getLong("id"));
			wallet.setTotalAmount(new Money(rs.getBigDecimal("total_amount")));
			wallet.setCashSubWallet(new Money(rs.getBigDecimal("cash_amount")));
			wallet.setRefundSubWallet(new Money(rs.getBigDecimal("refund_amount")));
			wallet.setGiftSubWallet(new Money(rs.getBigDecimal("gift_amount")));
			wallet.setWalletPassword(rs.getString("wallet_password"));
			return wallet;
    	}
    }
	
	private int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
	private String encrypt(String plaintext) throws Exception {
        MessageDigest msgDigest = null;
        String hashValue = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(plaintext.getBytes("UTF-8"));
            byte rawByte[] = msgDigest.digest();
            hashValue = (new BASE64Encoder()).encode(rawByte); 
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm Exists");
        } catch (UnsupportedEncodingException e) {
            System.out.println("The Encoding Is Not Supported");
        }
        return hashValue;
    }

}
