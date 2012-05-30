package com.fb.platform.payback.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fb.commons.util.MailSender;

public class PointsUtil implements Serializable{
	
	public String getPreviousDayDate(){
		DateTime datetime = new DateTime();
		datetime = datetime.minusDays(1); 
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		return fmt.print(datetime);
	}
	
	public String convertDateToFormat(String settlementDate, String pattern) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime datetime = format.parseDateTime(settlementDate);
		format = DateTimeFormat.forPattern(pattern);
		String newSettlementDate = format.print(datetime);
		return newSettlementDate;
	}
	
	public DateTime getDateTimeFromString(String date, String pattern){
		try{
			DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
			DateTime datetime = format.parseDateTime(date);
			return datetime;
		} catch (Exception e){
			return null;
		}
				
	}
	
	public String convertDateToFormat(DateTime currentTime, String dateFormat) {
		DateTimeFormatter format = DateTimeFormat.forPattern(dateFormat);
		return format.print(currentTime);
	}

	public Properties getProperties(String fileName){
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		Properties props = new Properties();
		try {
			props.load(inStream);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

	public boolean isValidDate(String validTill) {
		try{
			DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
			DateTime validTillDate = format.parseDateTime(validTill);
			return validTillDate.isAfterNow();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;

	}
	

	public void sendMail(String txnActionCode, String merchantId, String fileName, String fileContent, String type) {
		try{
			Properties props = getProperties("payback.properties");					
			String host = props.getProperty("mailHost");
			int port = Integer.parseInt(props.getProperty("mailPort"));
			MailSender mailSender = new MailSender(host, port, props.getProperty("mailUsername"), 
					props.getProperty("mailPassword"), 50000);
			mailSender.setFrom(props.getProperty(type + "_FROM"));
			mailSender.setTO(props.getProperty(type + "_TO"));
			mailSender.setCC(props.getProperty(type + "_CC"));
			mailSender.setSubject(txnActionCode + " " + merchantId + " " + getPreviousDayDate());
			mailSender.setText("Please find the attached " + txnActionCode + " file");
			if (fileContent != null) mailSender.setFileContent(fileContent, fileName);
			mailSender.sendMail();
		}catch(IOException ie){
			ie.printStackTrace();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
		
	}
	
	public boolean isValidLoyaltyCard(String cardNumber){
		return cardNumber.matches("[0-9]{16}");
	}
	

	public String getSequenceNumber() {
		DateTime datetime = new DateTime();
		int seconds = (datetime.getSecondOfDay()%999999) + 1;
		String sequenceNumber = String.valueOf(seconds);
		while(sequenceNumber.length() < 6){
			sequenceNumber = "0" + sequenceNumber;
		}
		return sequenceNumber;
	}


	
}
