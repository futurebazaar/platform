package com.fb.platform.wallet.dao;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletTransaction;

public class WalletTransactionDaoTest extends BaseTestCase {
	
	@Autowired
	private WalletDao walletDao;
	
	@Autowired
	private WalletTransactionDao walletTransactionDao;
	
	
	@Test
	public void newCreditTransaction(){
		Wallet wallet = walletDao.load(6, -5);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal(100.00)), SubWalletType.GIFT_SUB_WALLET, 0, 0, "EGV");
		assertNotNull(walletTransaction);
		
		String transactionId = walletTransactionDao.insertTransaction(walletTransaction);
		assertNotNull(transactionId);
		Wallet wallet2 = walletDao.update(wallet);
		assertNotNull(wallet2);
		assertNotNull(wallet2.getId());
		
		
	}
	
	@Test
	public void WalletHistory(){
		Wallet wallet = walletDao.load(6, -5);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal(100.00)), SubWalletType.GIFT_SUB_WALLET, 0, 0, "EGV");
		assertNotNull(walletTransaction);
		String transactionId = walletTransactionDao.insertTransaction(walletTransaction);
		assertNotNull(transactionId);
		Wallet wallet2 = walletDao.update(wallet);
		assertNotNull(wallet2);
		assertNotNull(wallet2.getId());
		
		List<WalletTransaction> walletTransactions = walletTransactionDao.walletHistory(wallet2, null, null);
		WalletTransaction wallTransaction = walletTransactionDao.transactionById(wallet2.getId(), transactionId);
		assertNotNull(wallTransaction);
		assertNotNull(walletTransactions);
		for(WalletTransaction walletTransaction2 :walletTransactions){
			System.out.println("asdas::::" + walletTransaction2.toString());
		}
		
	}
}
