package com.fb.platform.payback.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fb.commons.util.MailSender;

public class PointsUtil {
	
	public static String getPreviousDayDate() {
		DateTime datetime = new DateTime();
		datetime = datetime.minusDays(1); 
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		return fmt.print(datetime);
	}
	
	public static String convertDateToFormat(String settlementDate, String pattern) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime datetime = format.parseDateTime(settlementDate);
		format = DateTimeFormat.forPattern(pattern);
		String newSettlementDate = format.print(datetime);
		return newSettlementDate;
	}
	
	public static DateTime getDateTimeFromString(String date, String pattern) {
		DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
		DateTime datetime = format.parseDateTime(date);
		return datetime;
	}
	
	public static String convertDateToFormat(DateTime currentTime, String dateFormat) {
		DateTimeFormatter format = DateTimeFormat.forPattern(dateFormat);
		return format.print(currentTime);
	}

	public static Properties getProperties(String fileName){
		InputStream inStream = PointsUtil.class.getClassLoader().getResourceAsStream(fileName);
		Properties props = new Properties();
		try {
			props.load(inStream);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

	public static void sendMail(String txnActionCode, String merchantId, String fileName, String fileContent, String type) {
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
	
	public static boolean isValidLoyaltyCard(String cardNumber) {
		if (cardNumber == null){
			return false;
		}
		return cardNumber.matches("[0-9]{16}");
	}
	

	public static String getSequenceNumber() {
		DateTime datetime = new DateTime();
		int seconds = (datetime.getSecondOfDay()%999999) + 1;
		String sequenceNumber = String.valueOf(seconds);
		while(sequenceNumber.length() < 6){
			sequenceNumber = "0" + sequenceNumber;
		}
		return sequenceNumber;
	}
	
	public static String getMapValue(String map, String key) {
		HashMap<String, String> generatedMap = new HashMap<String, String>();
		StringTokenizer mapTokenizer = new StringTokenizer(map, ",");
		while (mapTokenizer.hasMoreTokens()){
			String singleMap = mapTokenizer.nextToken().replaceAll(" ", "");
			generatedMap.put(singleMap.split("=")[0], singleMap.split("=")[1]);
		}
		return generatedMap.get(key);
	}
	
	public static String getStackTrace(Throwable throwable) {
		  Writer writer = new StringWriter();
		  PrintWriter printWriter = new PrintWriter(writer);
		  throwable.printStackTrace(printWriter);
		  return writer.toString();
	}
	
}
