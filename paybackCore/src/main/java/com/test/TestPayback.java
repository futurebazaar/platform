package com.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.platform.payback.util.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class TestPayback {
	
	private static String host;
	private static int port = 22;
	private static String username;
	private static String password;
	private static String remoteDirectory;
	
	
	public static void main(String[] args) {
		host = "10.40.15.119";
		port = 22;
		username = "futureba";
		password = "17d@Gil+2";
		remoteDirectory = "/futureba/in";
		
		String settlementDate = "2012-04-16";
		String txnActionCode = "EARN_REVERSAL";
		
		String[] serviceResources = {"applicationContext-dao.xml", "applicationContext-service.xml", 
				"applicationContext-resources.xml"};	

		ApplicationContext orderServiceContext = new ClassPathXmlApplicationContext(serviceResources);
		Object manager = orderServiceContext.getBean("pointsEarnManager");
		//((PointsEarnManagerImpl) manager).storeEarnData();
		//int manager1 = ((PointsManagerImpl) manager).mailBurnData();
		//int earnManager = ((PointsEarnManagerImpl) manager).putEarnDataOnSftp();
		
		
		Object burnManager = orderServiceContext.getBean("pointsBurnManager");
		//((PointsBurnManagerImpl) burnManager).mailBurnData();
		// SFTP put
		/*try {
			upload("/home/anubhav/Documents/a.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//sendMail();
		PointsUtil pointsUtil = new PointsUtil();
		//pointsUtil.sendMail("EARN", "1234", "anubhav", "anubhav");
		
		/*BigDecimal txnPoints = (new BigDecimal(10.6)).setScale(0, BigDecimal.ROUND_HALF_DOWN);
		System.out.println(txnPoints.setScale(2));
		
		DateTimeFormatter format = DateTimeFormat.forPattern("ddMMyyyy");
		System.out.println(format.print(DateTime.now()));*/
		//String ab = "1234";
		//System.out.println(ab.substring(0, 29));
		/*StringTokenizer factorIterator = new StringTokenizer("1234", ",");
		int earnFactor = Integer.parseInt(factorIterator.nextToken());
		System.out.println(earnFactor);
		
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
		DateTime validTillDate = format.parseDateTime("20120503");
		System.out.println(validTillDate);
		System.out.println(DateTime.now());
		System.out.println(DateTime.now().isAfter(validTillDate));
		System.out.println(validTillDate.toDate().before(DateTime.now().toDate()));
		System.out.println(new BigDecimal(3).compareTo(new BigDecimal(2.25)));
		
		
		InputStream inStream = TestPayback.class.getClassLoader().getResourceAsStream("payback.properties");
		Properties props = new Properties();
		System.out.println(props.getProperty("sftpUsername"));
		
		File files = new File("/");
		if (files.isDirectory()){
			String[] file = files.list();
			System.out.println(file[0]);
		}*/
		System.out.println(PointsTxnClassificationCodeEnum.valueOf(txnActionCode).toString().split(",")[0]);
		Date d = DateTime.now().toDate();
		System.out.println(d);
		
		System.out.println(pointsUtil.convertDateToFormat(DateTime.now(), "hhmmss"));
		
	}
	
	public static int upload(String fileToUpload) throws IOException{
		JSch jsch = new JSch();
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSFTP = null;
		
		try {
			session = jsch.getSession(username, host, port);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			
			channel = session.openChannel("sftp");
			channel.connect();
			channelSFTP = (ChannelSftp) channel;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// Will throw a proper Exception
			throw new IOException();
		}
		
		try{
			channelSFTP.cd(remoteDirectory);
			File file = new File(fileToUpload);
			channelSFTP.put(new FileInputStream(file), file.getName());
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			throw new IOException();
		} catch (SftpException e) {
			// TODO Auto-generated catch 	block
			e.printStackTrace();
			throw new IOException();
		}
			
		return 0;
	}
	
	public static int sendMail(){
			
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "10.0.101.39");
		properties.put("mail.smtp.port", "25");
		//properties.put("mail.smtp.auth", "true");
		//properties.put("mail.smtp.starttls.enable", "true");
		javax.mail.Session session = javax.mail.Session.getInstance(properties);
		
		try{
			
	         MimeMessage message = new MimeMessage(session);
	         message.setFrom(new InternetAddress("fbadmin@futurebazaar.com"));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress("anubhav.jain@futuregroup.in"));
	         message.addRecipient(Message.RecipientType.CC, new InternetAddress("salim.majgaonkar@futuregroup.in"));
	         message.setSubject("Test Mail");
	         
	         BodyPart messageBodyPart = new MimeBodyPart();
	         messageBodyPart.setText("testing");
	         Multipart multipart = new MimeMultipart();
	         multipart.addBodyPart(messageBodyPart);
	         
	          MimeBodyPart attachmentPart = new MimeBodyPart();
	         
	         DataSource source = new ByteArrayDataSource("Anubhav".getBytes("UTF-8"), "text/plain");
	         attachmentPart.setDataHandler(new DataHandler(source));
	         attachmentPart.setFileName("BurnReversal.csv");
	         multipart.addBodyPart(attachmentPart);
	         message.setContent(multipart );
	         System.out.println("Sending message successfully....");
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	      
		}catch(AddressException addressException) {
	    	addressException.printStackTrace();
	    	
	    }catch (MessagingException messageException) {
	         messageException.printStackTrace();
	         
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
