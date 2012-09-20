package com.fb.platform.wallet;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.wallet._1_0.WalletSummaryRequest;
import com.fb.platform.wallet._1_0.WalletSummaryResponse;
import com.fb.platform.wallet._1_0.WalletSummaryStatus;
import com.fb.platform.wallet._1_0.WalletDetails;

import com.fb.platform.wallet._1_0.WalletHistoryRequest;
import com.fb.platform.wallet._1_0.WalletHistoryRequestPaginated;
import com.fb.platform.wallet._1_0.WalletHistoryResponse;
import com.fb.platform.wallet._1_0.WalletHistoryStatus;
import com.fb.platform.wallet._1_0.SubWallet;
import com.fb.platform.wallet._1_0.Transaction;
import com.fb.platform.wallet._1_0.SubTransaction;

import com.fb.platform.wallet._1_0.FillWalletRequest;
import com.fb.platform.wallet._1_0.FillWalletResponse;
import com.fb.platform.wallet._1_0.FillWalletStatus;

import com.fb.platform.wallet._1_0.PayRequest;
import com.fb.platform.wallet._1_0.PayResponse;
import com.fb.platform.wallet._1_0.PayStatus;

import com.fb.platform.wallet._1_0.RefundRequest;
import com.fb.platform.wallet._1_0.RefundResponse;
import com.fb.platform.wallet._1_0.RefundStatus;

import com.fb.platform.wallet._1_0.RevertRequest;
import com.fb.platform.wallet._1_0.RevertResponse;
import com.fb.platform.wallet._1_0.RevertStatus;

import com.fb.platform.wallet._1_0.VerifyWalletRequest;
import com.fb.platform.wallet._1_0.VerifyWalletResponse;
import com.fb.platform.wallet._1_0.VerifyWalletStatus;

import com.fb.platform.wallet._1_0.ChangeWalletPasswordRequest;
import com.fb.platform.wallet._1_0.ChangeWalletPasswordResponse;
import com.fb.platform.wallet._1_0.ChangeWalletPasswordStatus;


import com.fb.platform.wallet._1_0.ResetWalletPasswordRequest;
import com.fb.platform.wallet._1_0.ResetWalletPasswordResponse;
import com.fb.platform.wallet._1_0.ResetWalletPasswordStatus;

import com.fb.platform.wallet.manager.interfaces.WalletManager;
import com.fb.platform.wallet.manager.model.access.SubWalletEnum;

/**
 * @author Rajesh
 *
 */
@Path("/wallet")
@Component
@Scope("request")
public class WalletResource {

	private static final Log logger = LogFactory.getLog(WalletResource.class);


	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	@Autowired
	private WalletManager walletManager = null;

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.wallet._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}

	@POST
	@Path("/summary")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String summaryRequest(String walletSummaryryXml) {
		
		logger.info("WALLET SUMMARY XML request: \n" + walletSummaryryXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			WalletSummaryRequest xmlWalletSummaryReq = (WalletSummaryRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(walletSummaryryXml)));

			com.fb.platform.wallet.manager.model.access.WalletSummaryRequest apiWalletSummaryReq = new com.fb.platform.wallet.manager.model.access.WalletSummaryRequest();
			apiWalletSummaryReq.setUserId(xmlWalletSummaryReq.getUserId());
			apiWalletSummaryReq.setClientId(xmlWalletSummaryReq.getClientId());
			apiWalletSummaryReq.setSessionToken(xmlWalletSummaryReq.getSessionToken());

			com.fb.platform.wallet.manager.model.access.WalletSummaryResponse apiWalletSummaryResp = walletManager.getWalletSummary(apiWalletSummaryReq);

			WalletSummaryResponse xmlWalletSummaryResponse = new WalletSummaryResponse();
			xmlWalletSummaryResponse.setSessionToken(apiWalletSummaryResp.getSessionToken());
			xmlWalletSummaryResponse.setWalletSummaryStatus(WalletSummaryStatus.fromValue(apiWalletSummaryResp.getWalletSummaryStatus().name() ));
			WalletDetails walletDetails = new WalletDetails();
			com.fb.platform.wallet.manager.model.access.WalletDetails apiWalletDetails = apiWalletSummaryResp.getWalletDetails();
			if(apiWalletDetails != null){
				walletDetails.setWalletId(apiWalletDetails.getWalletId());
				walletDetails.setCashAmount(apiWalletDetails.getCashAmount());
				walletDetails.setRefundAmount(apiWalletDetails.getRefundAmount());
				walletDetails.setGiftAmount(apiWalletDetails.getGiftAmount());
				walletDetails.setTotalAmount(apiWalletDetails.getTotalAmount());
				walletDetails.setRefundableAmount(apiWalletDetails.getRefundableAmount().getAmount());
				if(apiWalletDetails.getGiftExpiryAmt1()!= null && apiWalletDetails.getGiftExpiryDt1() != null){
					walletDetails.setGiftExpiryAmt1(apiWalletDetails.getGiftExpiryAmt1().getAmount());
					walletDetails.setGiftExpiryDt1(apiWalletDetails.getGiftExpiryDt1());
				}
				if(apiWalletDetails.getGiftExpiryAmt2()!= null && apiWalletDetails.getGiftExpiryDt2() != null){
					walletDetails.setGiftExpiryAmt2(apiWalletDetails.getGiftExpiryAmt2().getAmount());
					walletDetails.setGiftExpiryDt2(apiWalletDetails.getGiftExpiryDt2());
				}
			}
			xmlWalletSummaryResponse.setWalletDetails(walletDetails);
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlWalletSummaryResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("walletSummaryryXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the Wallet Summary request.", e);
			return "error";
		}
	}

	@POST
	@Path("/history")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String historyRequest(String walletHistoryXml) {
		
		logger.info("WALLET HISTORY XML request: \n" + walletHistoryXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			WalletHistoryRequest xmlWalletHistoryReq = (WalletHistoryRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(walletHistoryXml)));

			com.fb.platform.wallet.manager.model.access.WalletHistoryRequest apiWalletHistoryReq = new com.fb.platform.wallet.manager.model.access.WalletHistoryRequest();
			apiWalletHistoryReq.setWalletId(xmlWalletHistoryReq.getWalletId());
			apiWalletHistoryReq.setSessionToken(xmlWalletHistoryReq.getSessionToken());
			if(xmlWalletHistoryReq.getSubWallet() != null){
				apiWalletHistoryReq.setSubWallet(SubWalletEnum.valueOf(xmlWalletHistoryReq.getSubWallet().value()));
			}
			if (xmlWalletHistoryReq.getFromDate() != null){
				apiWalletHistoryReq.setFromDate(xmlWalletHistoryReq.getFromDate());
			}
			if (xmlWalletHistoryReq.getToDate() != null){
				apiWalletHistoryReq.setToDate(xmlWalletHistoryReq.getToDate());
			}

			com.fb.platform.wallet.manager.model.access.WalletHistoryResponse apiWalletHistoryResp = walletManager.getWalletHistory(apiWalletHistoryReq);
			WalletHistoryResponse xmlWalletHistoryResponse = makeWalletHistoryResponse(apiWalletHistoryResp);
				
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlWalletHistoryResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("walletHistoryXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the Wallet History request.", e);
			return "error";
		}
	}
	
	@POST
	@Path("/pagedhistory")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String pagedHistoryRequest(String walletHistoryXml) {
		
		logger.info("WALLET HISTORY XML request: \n" + walletHistoryXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			WalletHistoryRequestPaginated xmlWalletHistoryReq = (WalletHistoryRequestPaginated) unmarshaller.unmarshal(new StreamSource(new StringReader(walletHistoryXml)));

			com.fb.platform.wallet.manager.model.access.WalletHistoryRequest apiWalletHistoryReq = new com.fb.platform.wallet.manager.model.access.WalletHistoryRequest();
			apiWalletHistoryReq.setUserId(xmlWalletHistoryReq.getUserId());
			apiWalletHistoryReq.setClientId(xmlWalletHistoryReq.getClientId());
			apiWalletHistoryReq.setSessionToken(xmlWalletHistoryReq.getSessionToken());
			apiWalletHistoryReq.setPageNumber(xmlWalletHistoryReq.getPageNumber());
			apiWalletHistoryReq.setResultsPerPage(xmlWalletHistoryReq.getResultsPerPage());
			if(xmlWalletHistoryReq.getSubWallet() != null){
				apiWalletHistoryReq.setSubWallet(SubWalletEnum.valueOf(xmlWalletHistoryReq.getSubWallet().value()));
			}
			com.fb.platform.wallet.manager.model.access.WalletHistoryResponse apiWalletHistoryResp = walletManager.getWalletHistoryPaged(apiWalletHistoryReq);
			WalletHistoryResponse xmlWalletHistoryResponse = makeWalletHistoryResponse(apiWalletHistoryResp);
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlWalletHistoryResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("walletHistoryXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in the Wallet History request.", e);
			return "error";
		}
	}


	@POST
	@Path("/fill")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String fillRequest(String fillWalletXml) {
		
		logger.info("FILL WALLET XML request: \n" + fillWalletXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			FillWalletRequest xmlFillWalletReq = (FillWalletRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(fillWalletXml)));

			com.fb.platform.wallet.manager.model.access.FillWalletRequest apiFillWalletReq = new com.fb.platform.wallet.manager.model.access.FillWalletRequest();
			apiFillWalletReq.setSessionToken(xmlFillWalletReq.getSessionToken());
			apiFillWalletReq.setUserId(xmlFillWalletReq.getUserId());
			apiFillWalletReq.setClientId(xmlFillWalletReq.getClientId());
			apiFillWalletReq.setAmount(xmlFillWalletReq.getAmount());
			apiFillWalletReq.setSubWallet(SubWalletEnum.valueOf(xmlFillWalletReq.getSubWallet().value()));
			apiFillWalletReq.setPaymentId(xmlFillWalletReq.getPaymentId());
			apiFillWalletReq.setRefundId(xmlFillWalletReq.getRefundId());
			apiFillWalletReq.setGiftCode(xmlFillWalletReq.getGiftCode());
			apiFillWalletReq.setExpiryDate(xmlFillWalletReq.getExpiryDate());
			apiFillWalletReq.setIsEgv(xmlFillWalletReq.isIsEgv());
						
			com.fb.platform.wallet.manager.model.access.FillWalletResponse apiFillWalletResp = walletManager.fillWallet(apiFillWalletReq);

			FillWalletResponse xmlFillWalletResponse = new FillWalletResponse();
			xmlFillWalletResponse.setSessionToken(apiFillWalletResp.getSessionToken());
			xmlFillWalletResponse.setWalletId(apiFillWalletResp.getWalletId());
			xmlFillWalletResponse.setTransactionId(apiFillWalletResp.getTransactionId());
			xmlFillWalletResponse.setFillWalletStatus(FillWalletStatus.fromValue(apiFillWalletResp.getStatus().name() ));
			
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlFillWalletResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("fillWalletXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in filling the Wallet:", e);
			return "error";
		}
	}
	
	@POST
	@Path("/pay")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String payRequest(String payFromWalletXml) {
		
		logger.info("PAY FROM WALLET XML request: \n" + payFromWalletXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			PayRequest xmlPayReq = (PayRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(payFromWalletXml)));

			com.fb.platform.wallet.manager.model.access.PayRequest apiPayReq = new com.fb.platform.wallet.manager.model.access.PayRequest();
			apiPayReq.setSessionToken(xmlPayReq.getSessionToken());
			apiPayReq.setUserId(xmlPayReq.getUserId());
			apiPayReq.setClientId(xmlPayReq.getClientId());
			apiPayReq.setOrderId(xmlPayReq.getOrderId());
			apiPayReq.setAmount(xmlPayReq.getAmount());

			com.fb.platform.wallet.manager.model.access.PayResponse apiPayResponse = walletManager.payFromWallet(apiPayReq);

			PayResponse xmlPayResponse = new PayResponse();
			xmlPayResponse.setSessionToken(apiPayResponse.getSessionToken());
			xmlPayResponse.setTransactionId(apiPayResponse.getTransactionId());
			xmlPayResponse.setPayStatus(PayStatus.fromValue(apiPayResponse.getStatus().name() ));
			
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlPayResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("PayFromWalletXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in paying from the Wallet:", e);
			return "error";
		}
	}
	

	@POST
	@Path("/refund")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String refundRequest(String refundRequestXml) {
		
		logger.info("REFUND REQUEST XML request: \n" + refundRequestXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			RefundRequest xmlRefundReq = (RefundRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(refundRequestXml)));

			com.fb.platform.wallet.manager.model.access.RefundRequest apiRefundReq = new com.fb.platform.wallet.manager.model.access.RefundRequest();
			apiRefundReq.setSessionToken(xmlRefundReq.getSessionToken());
			apiRefundReq.setUserId(xmlRefundReq.getUserId());
			apiRefundReq.setClientId(xmlRefundReq.getClientId());
			apiRefundReq.setRefundId(xmlRefundReq.getRefundId());
			apiRefundReq.setAmount(xmlRefundReq.getAmount());
			apiRefundReq.setIgnoreExpiry(xmlRefundReq.isIgnoreExpiry());

			com.fb.platform.wallet.manager.model.access.RefundResponse apiRefundResponse = walletManager.refundFromWallet(apiRefundReq);

			RefundResponse xmlRefundResponse = new RefundResponse();
			xmlRefundResponse.setSessionToken(apiRefundResponse.getSessionToken());
			xmlRefundResponse.setTransactionId(apiRefundResponse.getTransactionId());
			xmlRefundResponse.setRefundStatus(RefundStatus.fromValue(apiRefundResponse.getStatus().name() ));
			
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlRefundResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("RefundFromWalletXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in refunding from the Wallet:", e);
			return "error";
		}
	}

	@POST
	@Path("/revert")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String revertRequest(String revertRequestXml) {
		
		logger.info("REVERT REQUEST XML request: \n" + revertRequestXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			RevertRequest xmlRevertReq = (RevertRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(revertRequestXml)));

			com.fb.platform.wallet.manager.model.access.RevertRequest apiRevertReq = new com.fb.platform.wallet.manager.model.access.RevertRequest();
			apiRevertReq.setSessionToken(xmlRevertReq.getSessionToken());
			apiRevertReq.setUserId(xmlRevertReq.getUserId());
			apiRevertReq.setClientId(xmlRevertReq.getClientId());
			apiRevertReq.setAmount(xmlRevertReq.getAmount());
			apiRevertReq.setTransactionIdToRevert(xmlRevertReq.getTransactionIdToRevert());

			com.fb.platform.wallet.manager.model.access.RevertResponse apiRevertResponse = walletManager.revertWalletTransaction(apiRevertReq);

			RevertResponse xmlRevertResponse = new RevertResponse();
			xmlRevertResponse.setSessionToken(apiRevertResponse.getSessionToken());
			xmlRevertResponse.setTransactionId(apiRevertResponse.getTransactionId());
			xmlRevertResponse.setRevertStatus(RevertStatus.fromValue(apiRevertResponse.getStatus().name() ));
			
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlRevertResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("RevertFromWalletXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in reverting wallet transaction:", e);
			return "error";
		}
	}


	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform Wallet Websevice.\n");
		sb.append("To get wallet summary post to : http://hostname:port/walletWS/wallet/summary\n");
		sb.append("To get wallet transaction history post to : http://hostname:port/walletWS/wallet/history\n");
		sb.append("To fill wallet post to : http://hostname:port/walletWS/wallet/fill\n");
		sb.append("To pay from wallet post to : http://hostname:port/walletWS/wallet/pay\n");
		sb.append("To get refund from wallet post to : http://hostname:port/walletWS/wallet/refund\n");
		sb.append("To revert wallet transaction post to : http://hostname:port/walletWS/wallet/revert\n");
		sb.append("To verify wallet : http://hostname:port/walletWS/wallet/verify\n");
		sb.append("To change wallet password: http://hostname:port/walletWS/wallet/changepassword\n");
		sb.append("To reset wallet password: http://hostname:port/walletWS/wallet/resetpassword\n");
		return sb.toString();
	}
	
	@GET
	@Path("/xsd")
	@Produces("application/xml")
	public String getXsd() {	
		InputStream walletXsd = this.getClass().getClassLoader().getResourceAsStream("wallet.xsd");
		String walletXsdString = convertInputStreamToString(walletXsd);
		return walletXsdString;
	}

	private String convertInputStreamToString(InputStream inputStream) {
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		try {
			String line = bufReader.readLine();
			while( line != null ) {
				sb.append( line + "\n" );
				line = bufReader.readLine();
			}
			inputStream.close();
		} catch(IOException exception) {
			logger.error("User.xsd loading error : " + exception.getMessage() );
		}
		return sb.toString();
	}
	
	@POST
	@Path("/verify")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String verifyWallet (String verifyWalletXml) {
		
		logger.info("VERIFY WALLET XML request: \n" + verifyWalletXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			VerifyWalletRequest xmlVerifyReq = (VerifyWalletRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(verifyWalletXml)));

			com.fb.platform.wallet.manager.model.access.VerifyWalletRequest apiVerifyReq = new com.fb.platform.wallet.manager.model.access.VerifyWalletRequest();
			apiVerifyReq.setSessionToken(xmlVerifyReq.getSessionToken());
			apiVerifyReq.setUserId(xmlVerifyReq.getUserId());
			apiVerifyReq.setClientId(xmlVerifyReq.getClientId());
			apiVerifyReq.setAmount(xmlVerifyReq.getAmount());
			apiVerifyReq.setPassword(xmlVerifyReq.getPassword());

			com.fb.platform.wallet.manager.model.access.VerifyWalletResponse apiVerifyResponse = walletManager.verifyWallet(apiVerifyReq);

			VerifyWalletResponse xmlVerifyResponse = new VerifyWalletResponse();
			xmlVerifyResponse.setSessionToken(apiVerifyResponse.getSessionToken());
			xmlVerifyResponse.setWalletBalance(apiVerifyResponse.getAmount());
			xmlVerifyResponse.setVerifyWalletStatus(VerifyWalletStatus.fromValue(apiVerifyResponse.getStatus().name() ));
			
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlVerifyResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("VerifyWalletXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in verifying the Wallet:", e);
			return "error";
		}
	}
	
	private WalletHistoryResponse makeWalletHistoryResponse(com.fb.platform.wallet.manager.model.access.WalletHistoryResponse apiWalletHistoryResp){
		WalletHistoryResponse xmlWalletHistoryResponse = new WalletHistoryResponse();
		xmlWalletHistoryResponse.setSessionToken(apiWalletHistoryResp.getSessionToken());
		xmlWalletHistoryResponse.setWalletHistoryStatus(WalletHistoryStatus.fromValue(apiWalletHistoryResp.getWalletHistoryStatus().name() ));
		xmlWalletHistoryResponse.setTotalNumberOfTransaction(apiWalletHistoryResp.getTotalTransactionSize());
		List<Transaction> transactionList = new ArrayList<Transaction>();
		if(apiWalletHistoryResp.getTransactionList() != null){
			for (com.fb.platform.wallet.to.WalletTransaction apiTransaction : apiWalletHistoryResp.getTransactionList()){
				Transaction transaction = new Transaction();
				transaction.setTransactionId(apiTransaction.getTransactionId());
				transaction.setType(apiTransaction.getTransactionType().name());
				transaction.setAmount(apiTransaction.getAmount().getAmount());
				transaction.setTimestamp(apiTransaction.getTimeStamp());
				transaction.setWalletBalance(apiTransaction.getWalletBalance().getAmount());
				List<SubTransaction> subTransactionList = new ArrayList<SubTransaction>();
				if (apiTransaction.getWalletSubTransaction() != null){
					for (com.fb.platform.wallet.to.WalletSubTransaction apiSubTransaction : apiTransaction.getWalletSubTransaction()){
						SubTransaction subTransaction = new SubTransaction();
						subTransaction.setSubWallet(SubWallet.fromValue(apiSubTransaction.getSubWalletType().toString()));
						subTransaction.setAmount(apiSubTransaction.getAmount().getAmount());
						subTransaction.setOrderId(apiSubTransaction.getOrderId());
						subTransaction.setPaymentId(apiSubTransaction.getPaymentId());
						subTransaction.setRefundId(apiSubTransaction.getRefundId());
						subTransaction.setPaymentReversalId(apiSubTransaction.getPaymentReversalId());
						subTransaction.setGiftCode(apiSubTransaction.getGiftCode());
						
						subTransactionList.add(subTransaction);
					}
					transaction.getSubTransaction().addAll(subTransactionList);
				}
				transactionList.add(transaction);
			}
			
		}
		xmlWalletHistoryResponse.getTransaction().addAll(transactionList);
		return xmlWalletHistoryResponse;
	}
	
	
	@POST
	@Path("/changepassword")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String changeWalletPassword (String changeWalletPasswordXml) {
		
		logger.info("CHANGE WALLET PASSWORD XML request: \n" + changeWalletPasswordXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ChangeWalletPasswordRequest xmlchangeWalletPasswordReq = (ChangeWalletPasswordRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(changeWalletPasswordXml)));

			com.fb.platform.wallet.manager.model.access.ChangeWalletPasswordRequest apichangeWalletPasswordReq = new com.fb.platform.wallet.manager.model.access.ChangeWalletPasswordRequest();
			apichangeWalletPasswordReq.setSessionToken(xmlchangeWalletPasswordReq.getSessionToken());
			apichangeWalletPasswordReq.setUserId(xmlchangeWalletPasswordReq.getUserId());
			apichangeWalletPasswordReq.setClientId(xmlchangeWalletPasswordReq.getClientId());
			apichangeWalletPasswordReq.setOldPassword(xmlchangeWalletPasswordReq.getOldPassword());
			apichangeWalletPasswordReq.setNewPassword(xmlchangeWalletPasswordReq.getNewPassword());

			com.fb.platform.wallet.manager.model.access.ChangeWalletPasswordResponse apichangeWalletPasswordResponse = walletManager.changeWalletPassword(apichangeWalletPasswordReq);

			ChangeWalletPasswordResponse xmlchangeWalletPasswordResponse = new ChangeWalletPasswordResponse();
			xmlchangeWalletPasswordResponse.setSessionToken(apichangeWalletPasswordResponse.getSessionToken());
			xmlchangeWalletPasswordResponse.setChangeWalletPasswordStatus(ChangeWalletPasswordStatus.fromValue(apichangeWalletPasswordResponse.getStatus().name() ));
			
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlchangeWalletPasswordResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("VerifyWalletXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in verifying the Wallet:", e);
			return "error";
		}
	}
	
	
	@POST
	@Path("/resetpassword")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String resetWalletPassword (String resetWalletPasswordXml) {
		
		logger.info("RESET WALLET PASSWORD XML request: \n" + resetWalletPasswordXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ResetWalletPasswordRequest xmlresetWalletPasswordReq = (ResetWalletPasswordRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(resetWalletPasswordXml)));

			com.fb.platform.wallet.manager.model.access.ResetWalletPasswordRequest apiresetWalletPasswordReq = new com.fb.platform.wallet.manager.model.access.ResetWalletPasswordRequest();
			apiresetWalletPasswordReq.setSessionToken(xmlresetWalletPasswordReq.getSessionToken());
			apiresetWalletPasswordReq.setUserId(xmlresetWalletPasswordReq.getUserId());
			apiresetWalletPasswordReq.setClientId(xmlresetWalletPasswordReq.getClientId());
			
			com.fb.platform.wallet.manager.model.access.ResetWalletPasswordResponse apiresetWalletPasswordResponse = walletManager.resetWalletPassword(apiresetWalletPasswordReq);

			ResetWalletPasswordResponse xmlresetWalletPasswordResponse = new ResetWalletPasswordResponse();
			xmlresetWalletPasswordResponse.setSessionToken(apiresetWalletPasswordResponse.getSessionToken());
			xmlresetWalletPasswordResponse.setResetWalletPasswordStatus(ResetWalletPasswordStatus.fromValue(apiresetWalletPasswordResponse.getStatus().name() ));
			
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlresetWalletPasswordResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("VerifyWalletXml response :\n" + xmlResponse);
			return xmlResponse;

		} catch (JAXBException e) {
			logger.error("Error in verifying the Wallet:", e);
			return "error";
		}
	}

}
