package com.fb.platform.payback.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fb.commons.util.MailSender;

public class PointsUtil {
	
	public String getPreviousDayDate(){
		DateTime datetime = new DateTime();
		datetime = datetime.minusDays(3); //3
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		return fmt.print(datetime);
	}
	
	public String convertDateToFormat(String settlementDate, String string) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime datetime = format.parseDateTime(settlementDate);
		format = DateTimeFormat.forPattern(string);
		String newSettlementDate = format.print(datetime);
		return newSettlementDate;
	}
	
	public String convertDateToFormat(DateTime currentTime, String dateFormat) {
		DateTimeFormatter format = DateTimeFormat.forPattern(dateFormat);
		return format.print(currentTime);
	}

	
	public Properties getProperties(String fileName) throws IOException{
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		Properties props = new Properties();
		props.load(inStream);
		inStream.close();
		return props;
	}

	public boolean isValidDate(String validTill) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
		DateTime validTillDate = format.parseDateTime("20120502");
		return DateTime.now().isAfter(validTillDate);

	}
	

	public void sendMail(String txnActionCode, String merchantId, String fileName, String fileContent) {
		try{
			Properties props = getProperties("payback.properties");					
			String host = props.getProperty("mailHost");
			int port = Integer.parseInt(props.getProperty("mailPort"));
			MailSender mailSender = new MailSender(host, port, props.getProperty("mailUsername"),props.getProperty("mailPassword"));
			mailSender.setFrom(props.getProperty("from"));
			mailSender.setTO(props.getProperty("to"));
			mailSender.setCC(props.getProperty("cc"));
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

	
}
