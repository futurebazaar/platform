package com.fb.platform.wallet.dao;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletTransaction;

@SuppressWarnings("unused")
public class WalletDaoTest extends BaseTestCase {
	
	@Autowired
	private WalletDao walletDao;
	
	@Test
	public void testLoadWallet(){
		Wallet wallet = walletDao.load(6, -5,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
	}
	
	@Test
	public void testLoadWalletAlredyCreated(){
		Wallet wallet = walletDao.load(1, -5,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		
		Wallet walletReLoad = walletDao.load(1, -5,false);
		assertNotNull(walletReLoad);
		assertNotNull(walletReLoad.getId());
		
		assertEquals(wallet.getId(), walletReLoad.getId());
		assertEquals(true, walletReLoad.equals(wallet));
		assertEquals(wallet.toString(), walletReLoad.toString());
	}
	
	@Test
	public void testLoadWalletById(){
		Wallet wallet = walletDao.load(3, -5,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		
		Wallet walletById = walletDao.load(wallet.getId());
		assertNotNull(walletById);
		assertNotNull(walletById.getId());
	}

	@Test
	public void testLoadWalletByWrongId(){
		try{
			Wallet walletById = walletDao.load(0);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.WalletNotFoundException",e.getClass().getCanonicalName());
		}
		
	}
	
	@Test
	public void testUpdateWallet(){
		Wallet wallet = walletDao.load(2, -5,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), wallet.getTotalAmount());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), wallet.getRefundSubWallet());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), wallet.getCashSubWallet());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), wallet.getGiftSubWallet());
		Wallet wallet2 = wallet;
		assertEquals(wallet2, wallet);
		
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal(100.00)), SubWalletType.GIFT, 0, 0, "EGV",new DateTime(2015,5,31,12,12,12));
		assertNotNull(walletTransaction);
		
		Wallet walletPostUpdate = walletDao.update(wallet);
		assertNotNull(walletPostUpdate);
		assertEquals(new Money(new BigDecimal("100.00")), walletPostUpdate.getTotalAmount());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), walletPostUpdate.getRefundSubWallet());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), walletPostUpdate.getCashSubWallet());
		assertEquals(new Money(new BigDecimal("100.00")), walletPostUpdate.getGiftSubWallet());				
	}
	
	@Test
	public void testUpdateWalletFailure(){
		Wallet wallet = walletDao.load(2, -5,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), wallet.getTotalAmount());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), wallet.getRefundSubWallet());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), wallet.getCashSubWallet());
		assertEquals(new Money(new BigDecimal(BigInteger.ZERO,2)), wallet.getGiftSubWallet());
		Wallet wallet2 = wallet;
		assertEquals(wallet2, wallet);
		
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal(100.00)), SubWalletType.GIFT, 0, 0, "EGV",new DateTime(2015,5,31,12,12,12));
		assertNotNull(walletTransaction);
		
		wallet.setId(10293L); // setting an invalid id to fail the wallet test
		
		Wallet walletPostUpdate = walletDao.update(wallet);
		assertNull(walletPostUpdate);
	}
}
