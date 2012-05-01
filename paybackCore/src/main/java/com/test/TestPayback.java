package com.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import com.fb.platform.payback.dao.impl.*;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.service.impl.PointsServiceImpl;
import com.fb.platform.payback.service.impl.PointsBurnManagerImpl;
import com.fb.platform.payback.service.impl.PointsEarnManagerImpl;
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
	
	private static String to;
	private static String from;
	private static String subject;
	private static String text;
	private static String file;
	private static String cc;
	
	public static void main(String[] args) {
		host = "10.40.15.119";
		port = 22;
		username = "futureba";
		password = "17d@Gil+2";
		remoteDirectory = "/futureba/in";
		
		from = "anubhav.j89@gmail.com";
		to = "anubhav.j89@gmail.com";
		subject = "Test Mail";
		text = "Hello";
		cc = "anubhav.jain@futuregroup.in";
		
		String settlementDate = "2012-04-16";
		String txnActionCode = "EARN_REVERSAL";
		
		String[] serviceResources = {"applicationContext-dao.xml", "applicationContext-service.xml", 
				"applicationContext-resources.xml"};	

		ApplicationContext orderServiceContext = new ClassPathXmlApplicationContext(serviceResources);
		Object manager = orderServiceContext.getBean("pointsEarnManager");
		//((PointsEarnManagerImpl) manager).storeEarnData();
		//int manager1 = ((PointsManagerImpl) manager).mailBurnData();
		//int earnManager = ((PointsEarnManagerImpl) manager).putEarnData();
		
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
		
		System.out.println(Integer.parseInt("2"));

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
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		//properties.put("mail.user", "anubhav.j89");
		//properties.put("mail.password", "!n89sep02");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		
		javax.mail.Session session = javax.mail.Session.getInstance(properties, 
				new javax.mail.Authenticator(){
					protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
						return new javax.mail.PasswordAuthentication("anubhav.j89", "!n89sep02");
					}
		});
		
		try{
			
	         MimeMessage message = new MimeMessage(session);
	         message.setFrom(new InternetAddress(from));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	         message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
	         message.setSubject(subject);
	         
	         BodyPart messageBodyPart = new MimeBodyPart();
	         messageBodyPart.setText(text);
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
