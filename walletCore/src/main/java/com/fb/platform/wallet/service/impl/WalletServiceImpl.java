package com.fb.platform.wallet.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.dao.WalletDao;
import com.fb.platform.wallet.dao.WalletTransactionDao;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.TransactionType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletGifts;
import com.fb.platform.wallet.model.WalletSubTransaction;
import com.fb.platform.wallet.to.WalletTransaction;
import com.fb.platform.wallet.to.WalletTransactionResultSet;
import com.fb.platform.wallet.util.Encrypt;
import com.fb.platform.wallet.util.GenerateSendWalletPassword;
import com.fb.platform.wallet.service.WalletService;
import com.fb.platform.wallet.service.exception.AlreadyRefundedException;
import com.fb.platform.wallet.service.exception.InSufficientFundsException;
import com.fb.platform.wallet.service.exception.InvalidTransactionIdException;
import com.fb.platform.wallet.service.exception.RefundExpiredException;
import com.fb.platform.wallet.service.exception.WalletNotFoundException;
import com.fb.platform.wallet.service.exception.WorngRefundIdException;
import com.fb.platform.wallet.service.exception.WrongWalletPassword;

public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletDao walletDao;
	@Autowired
	private WalletTransactionDao walletTransactionDao;
	
	private int refundExpiryDays;
	
	private GenerateSendWalletPassword generateSendWalletPassword = null;
	
	private Log log = LogFactory.getLog(WalletServiceImpl.class);
	
	@Override
	public Wallet load(long walletId) throws WalletNotFoundException,PlatformException {
		log.info("load walletid ::" + walletId);
		try{
			Wallet wallet = walletDao.load(walletId);
			return walletReturnOperations(wallet);
		}catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet found with this walletId");
		}	
	}

	@Override
	public Wallet load(long userId, long clientId) throws PlatformException {
		return load(userId,clientId,true);			
	}
	
	private Wallet load(long userId,long clientId,boolean createNew) 
			throws WalletNotFoundException,PlatformException {
		log.info("load wallet for userId,clientId  ::" + userId +"," + clientId ) ;
		try{
			Wallet wallet = walletDao.load(userId,clientId,createNew);
			if(wallet != null){
				return walletReturnOperations(wallet);
			}else{
				throw new PlatformException("No wallet found or created with this userId:" + userId + " and clientId ::" + clientId);
			}
		}catch (WalletNotFoundException e) {
			throw new WalletNotFoundException();
		}catch (PlatformException e){
			throw new PlatformException();
		}
	}

	@Override
	public WalletTransactionResultSet walletHistory(long walletId,
			DateTime fromDate, DateTime toDate, SubWalletType subWalletType) {
		log.info("getting wallet history for walletId  ::" + walletId ) ;
		try{
			WalletTransactionResultSet walletTransactionResultSet = new WalletTransactionResultSet();
			Wallet wallet = load(walletId);
			List<WalletTransaction> walletTransactions = new ArrayList<WalletTransaction>();
			com.fb.platform.wallet.model.WalletTransactionResultSet walletTranResult = walletTransactionDao.walletHistory(wallet, fromDate, toDate);
			for (com.fb.platform.wallet.model.WalletTransaction walletTransaction : walletTranResult.getWalletTransactions()){
				walletTransactions.add(walletTransactionModeltoTO(walletTransaction));
			}
			walletTransactionResultSet.setWalletTransactions(walletTransactions);
			walletTransactionResultSet.setTotalTransactionSize(walletTranResult.getTotalNumberTransations());
			return walletTransactionResultSet;
		}catch (WalletNotFoundException e){
			throw new WalletNotFoundException("Exception no wallet for this wallet id");
		}catch (PlatformException e) {
			throw new PlatformException("Exception while loading wallet transactions");
		}
	}
	
	@Override
	public WalletTransactionResultSet walletHistory(long userId, long clientId,
			int pageNumber, int resultPerPage, SubWalletType subWalletType) {
		log.info("getting wallet history for userId  ::" + userId ) ;
		try{
			WalletTransactionResultSet walletTransactionResultSet = new WalletTransactionResultSet();
			Wallet wallet = load(userId,clientId,false);
			List<WalletTransaction> walletTransactions = new ArrayList<WalletTransaction>();
			com.fb.platform.wallet.model.WalletTransactionResultSet walletTranResult = walletTransactionDao.walletHistory(wallet, pageNumber, resultPerPage);
			for (com.fb.platform.wallet.model.WalletTransaction walletTransaction : walletTranResult.getWalletTransactions()){
				walletTransactions.add(walletTransactionModeltoTO(walletTransaction));
			}
			walletTransactionResultSet.setWalletTransactions(walletTransactions);
			walletTransactionResultSet.setTotalTransactionSize(walletTranResult.getTotalNumberTransations());
			return walletTransactionResultSet;
		}catch (WalletNotFoundException e){
			throw new WalletNotFoundException("Exception no wallet for this wallet id");
		}catch (PlatformException e) {
			throw new PlatformException("Exception while loading wallet transactions");
		}
	}

	@Override
	public WalletTransaction credit(long walletId, Money amount,
			String subWalletType, long paymentId, long refundId,
			String gitfCoupon,DateTime giftExpiry) throws WalletNotFoundException,
			PlatformException {
		log.info("credit to wallet ::: " ) ;
		log.info("walletId:" +walletId);
		log.info("Amount::" + amount.getAmount());
		log.info("subWalletType::" + subWalletType);
		try {
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(walletId);
			long gift_id = 0L;
			if (SubWalletType.valueOf(subWalletType).equals(SubWalletType.GIFT)){
				gift_id = walletTransactionDao.insertGift(wallet.getId(),gitfCoupon,giftExpiry,amount);
			}
			com.fb.platform.wallet.model.WalletTransaction walletTransaction = wallet
					.credit(amount, SubWalletType.valueOf(subWalletType), paymentId, refundId,gift_id);
			walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(walletTransaction));
			walletTransactionRes.setWallet(walletReturnOperations(walletDao.update(wallet)));
			return walletTransactionRes;
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");
		}
		catch (PlatformException e) {
			throw new PlatformException("No wallet with this wallet id");
		}
	}

	@Override
	public WalletTransaction debit(long userId,
			long clientId, Money amount, long orderId,String walletPassword)
			throws WalletNotFoundException, InSufficientFundsException,WrongWalletPassword,
			PlatformException {
		log.info("debit from wallet ::: " ) ;
		log.info("userIDId:" + userId);
		log.info("Amount::" + amount.getAmount());
		log.info("orderId::" + orderId);
		try{
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(userId,clientId,false);
			//if(wallet.verifyPassword(walletPassword)){
				if(wallet.isSufficientFund(amount)){
					com.fb.platform.wallet.model.WalletTransaction walletTransaction = wallet
							.debit(amount, orderId);
					walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(walletTransaction));
					walletTransactionRes.setWallet(walletReturnOperations(walletDao.update(wallet)));
					return walletTransactionRes;
				}else{
					throw new InSufficientFundsException("Insufficient fund in wallet");
				}
			/*}else {
				throw new WrongWalletPassword("Insufficient fund in wallet");
			}*/
		} catch (InSufficientFundsException e){
			throw new InSufficientFundsException("Not enough fund in the wallet");
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");
		} catch (PlatformException e) {
			throw new PlatformException("No wallet with this wallet id");
		}
		
	}

	@Override
	public WalletTransaction refund(long userId,
			long clientId, Money amount, long refundId, boolean ignoreExpiry)
			throws WalletNotFoundException, AlreadyRefundedException,RefundExpiredException,InSufficientFundsException,
			PlatformException {
		log.info("refund from wallet ::: " ) ;
		log.info("userID:" + userId);
		log.info("Amount::" + amount.getAmount());
		log.info("refundId::" + refundId);
		
		try {
			WalletTransaction walletTransactionRes =  new WalletTransaction();
			Wallet wallet = load(userId,clientId,false);
			if(walletTransactionDao.isRefundable(wallet,refundId,amount)){
				/***
				 * Removing the Expiry date cade as the expiry tome is getting ignored.
				 *  || walletTransaction.getTimeStamp().plusDays(refundExpiryDays).isAfterNow()
				 */
				if(ignoreExpiry){
					walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(wallet.refund(amount,refundId)));
					walletTransactionRes.setWallet(walletReturnOperations(walletDao.update(wallet)));							
				}/***else {
					throw new RefundExpiredException();
				}*/
			}
			return walletTransactionRes;
		} catch (InSufficientFundsException e){
			throw new InSufficientFundsException("Not enough fund in the wallet");
		} catch (WorngRefundIdException e){
			throw new WorngRefundIdException("This refund id does not belog to this wallet");
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");
		} catch (PlatformException e) {
			throw new PlatformException("An unrecoverable exception has occured while refund");
		}
	}

	@Override
	public WalletTransaction reverseTransaction(
			long userId, long clientId, String transactionId,Money amount,long newRefundId)
			throws WalletNotFoundException, InvalidTransactionIdException,
			PlatformException {
		
		log.info("]wallet transaction reversal::: " ) ;
		log.info("userIDId:" + userId);
		if (amount != null){
			log.info("Amount::" + amount.getAmount());
		}else{
			log.info("Reversing total Amount for transaction" );
		}
		log.info("orderId::" + transactionId);
		
		try{
			Wallet wallet = load(userId,clientId,false);
			com.fb.platform.wallet.model.WalletTransaction walletTransaction = walletTransactionDao.transactionById(wallet.getId(), transactionId);
			Money moneyAlreadyReversed = walletTransactionDao.amountAlreadyReversedByTransactionId(wallet.getId(), walletTransaction.getId());
			Money amountToBeReversed = amount != null ? amount : walletTransaction.getAmount();
			if(walletTransaction.getAmount().gteq(amountToBeReversed.plus(moneyAlreadyReversed))){
				com.fb.platform.wallet.model.WalletTransaction walletTransactionNew = wallet.reverseTransaction(walletTransaction,amountToBeReversed,moneyAlreadyReversed,newRefundId);
				walletTransactionNew.setTransactionId(walletTransactionDao.insertTransaction(walletTransactionNew));
				walletTransactionNew.setWallet(walletReturnOperations(walletDao.update(wallet)));
				return walletTransactionModeltoTO(walletTransactionNew);
			}else{
				throw new InSufficientFundsException();
			}
		}  catch (InSufficientFundsException e){
			throw new InSufficientFundsException("The amount is invalid for reversing the transaction");		
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");		
		}  catch (InvalidTransactionIdException e) {
			throw new InvalidTransactionIdException("This is an invalid transaction Id");
		} 	catch (PlatformException e) {
			e.printStackTrace();
			throw new PlatformException("an unhandeled exception has occured while trnasaction reversal");
		}
	}
	
	public void setWalletPasswordSender(GenerateSendWalletPassword generateSendWalletPassword){
		this.generateSendWalletPassword = generateSendWalletPassword;
	}
	
	public void setWalletDao(WalletDao walletDao) {
		this.walletDao = walletDao;
	}
	public void setWalletTransactionDao(WalletTransactionDao walletTransactionDao) {
		this.walletTransactionDao = walletTransactionDao;
	}
	public void setRefundExpiryDays(int refundExpiryDays) {
		this.refundExpiryDays = refundExpiryDays;
	}

	private WalletTransaction walletTransactionModeltoTO (com.fb.platform.wallet.model.WalletTransaction walletTransaction){
		WalletTransaction walletTransactionRes = new WalletTransaction();
		walletTransactionRes.setTransactionId(walletTransaction.getTransactionId());
		walletTransactionRes.setWallet(walletTransaction.getWallet());
		walletTransactionRes.setAmount(walletTransaction.getAmount());
		walletTransactionRes.setTimeStamp(walletTransaction.getTimeStamp());
		walletTransactionRes.setTransactionType(walletTransaction.getTransactionType());
		walletTransactionRes.setWalletBalance(walletTransaction.getWalletBalance());
		List<com.fb.platform.wallet.to.WalletSubTransaction> subTransactionList = new ArrayList<com.fb.platform.wallet.to.WalletSubTransaction>();
		for(com.fb.platform.wallet.model.WalletSubTransaction subTransaction: walletTransaction.getWalletSubTransaction()){
			com.fb.platform.wallet.to.WalletSubTransaction sTransaction = new com.fb.platform.wallet.to.WalletSubTransaction();
			sTransaction.setAmount(subTransaction.getAmount());
			//sTransaction.setGiftCode(subTransaction.getGiftCode()); //TO DO :: get gift code from gift id
			sTransaction.setOrderId(subTransaction.getOrderId());
			sTransaction.setPaymentId(subTransaction.getPaymentId());
			sTransaction.setPaymentReversalId(subTransaction.getPaymentReversalId());
			sTransaction.setRefundId(subTransaction.getRefundId());
			sTransaction.setSubWalletType(subTransaction.getSubWalletType());
			
			subTransactionList.add(sTransaction);
		}
		walletTransactionRes.setWalletSubTransaction(subTransactionList);
		return walletTransactionRes;
	}
	private Wallet walletReturnOperations(Wallet wallet){
		wallet = walletTransactionDao.updateGiftExpiry(wallet);
		wallet.setRefundableAmount(walletTransactionDao.getWalletRefundAmount(wallet.getId()));
		List<WalletGifts> walletGifts = walletTransactionDao.getWalletGifts(wallet.getId()); 
		for (int i= 0 ; i < walletGifts.size() && i < 2 ;i++){
			switch (i) {
			case 0:
				wallet.setGiftExpiryAmt1(walletGifts.get(i).getAmountRemaining());
				wallet.setGiftExpiryDt1(walletGifts.get(i).getGiftExpiry());
				break;
			case 1:
				wallet.setGiftExpiryAmt2(walletGifts.get(i).getAmountRemaining());
				wallet.setGiftExpiryDt2(walletGifts.get(i).getGiftExpiry());
				break;
			default:
				break;
			}
		}
		return wallet;
	}

	@Override
	public Wallet verifyWallet(long userId, long clientId,String password) 
			throws WalletNotFoundException, WrongWalletPassword, 
			PlatformException {
		try{
			Wallet wallet = load(userId,clientId,false);
			if(!wallet.verifyPassword(password)){
				throw new WrongWalletPassword("Wrong wallet password");
			}
			return wallet;
		} catch (WrongWalletPassword e){
			throw new WrongWalletPassword("The password provided for the wallet is incorrect");
		}  
		catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");
		}
		catch (PlatformException e) {
			throw new PlatformException("No wallet with this wallet id");
		}
		
	}

	@Override
	public void changeWalletPassword(long userId, long clientId,
			String oldPassword, String newPassword)
			throws WalletNotFoundException, WrongWalletPassword,
			PlatformException {
		try{
			Wallet wallet = load(userId,clientId,false);
			if(!wallet.verifyPassword(oldPassword)){
				throw new WrongWalletPassword();
			}else{
				wallet.setWalletPassword(Encrypt.encrypt(newPassword));
				walletDao.update(wallet);
			}
		} catch (WrongWalletPassword e){
			throw new WrongWalletPassword("The password provided for the wallet is incorrect");
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this user id");
		} catch (PlatformException e) {
			throw new PlatformException("The password could not be changed at this time");
		}		
	}

	@Override
	public void resetWalletPassword(long userId, long clientId)
			throws WalletNotFoundException, PlatformException {
		try{
			Wallet wallet = load(userId,clientId,false);
			wallet.setWalletPassword(Encrypt.encrypt(generateSendWalletPassword.generateSendWalletPassword(userId,true)));
			walletDao.update(wallet);
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this userId");
		} catch (PlatformException e) {
			throw new PlatformException("The password could not be reset at this time");
		}		
	}
}
