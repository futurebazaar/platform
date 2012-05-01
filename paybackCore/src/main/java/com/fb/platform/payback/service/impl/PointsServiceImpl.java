package com.fb.platform.payback.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.fb.commons.util.MailSender;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.util.PointsUtil;

public class PointsServiceImpl implements PointsService{
	
	private PointsUtil pointsUtil = null;
	private PointsDao pointsDao;


	public void setPointsDao(PointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}
	
	
	@Override
	public void sendMail(String txnActionCode, String merchantId) {
		try{
			Properties props = pointsUtil.getProperties("payback.properties");					
			String host = props.getProperty("mailHost");
			int port = Integer.parseInt(props.getProperty("mailPort"));
			MailSender mailSender = new MailSender(host, port, props.getProperty("mailUsername"),props.getProperty("mailPassword"));
			mailSender.setFrom(props.getProperty("burnFROM"));
			mailSender.setTO(props.getProperty("burnTO"));
			mailSender.setCC(props.getProperty("burnCC"));
			mailSender.setSubject(txnActionCode + " " + merchantId);
			mailSender.sendMail();
		}catch(IOException ie){
			ie.printStackTrace();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
		
	}
	
	@Override
	public void sendMail(String txnActionCode, String merchantId, String fileName, String fileContent) {
		try{
			Properties props = pointsUtil.getProperties("payback.properties");					
			String host = props.getProperty("mailHost");
			int port = Integer.parseInt(props.getProperty("mailPort"));
			MailSender mailSender = new MailSender(host, port, props.getProperty("mailUsername"),props.getProperty("mailPassword"));
			mailSender.setFrom(props.getProperty("burnFROM"));
			mailSender.setTO(props.getProperty("burnTO"));
			mailSender.setCC(props.getProperty("burnCC"));
			mailSender.setSubject(txnActionCode + " " + merchantId);
			mailSender.setText("Please find the attached " + txnActionCode + " file");
			mailSender.setFileContent(fileContent, fileName);
			mailSender.sendMail();
		}catch(IOException ie){
			ie.printStackTrace();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
		
	}

	@Override
	public void sendMail(String txnActionCode, String merchantId, String fileName, File file) {
		try{
			Properties props = pointsUtil.getProperties("payback.properties");					
			String host = props.getProperty("mailHost");
			int port = Integer.parseInt(props.getProperty("mailPort"));
			MailSender mailSender = new MailSender(host, port, props.getProperty("mailUsername"),props.getProperty("mailPassword"));
			mailSender.setFrom(props.getProperty("burnFROM"));
			mailSender.setTO(props.getProperty("burnTO"));
			mailSender.setCC(props.getProperty("burnCC"));
			mailSender.setSubject(txnActionCode + " " + merchantId);
			mailSender.setText("Please find the attached " + txnActionCode + " file");
			mailSender.setFile(file, fileName);
			mailSender.sendMail();
		}catch(IOException ie){
			ie.printStackTrace();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}

		
	}

}
