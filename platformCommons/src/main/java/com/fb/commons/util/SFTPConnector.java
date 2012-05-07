package com.fb.commons.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fb.commons.PlatformException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPConnector {	
	
	private String host;
	private int port = 22;
	private String username;
	private String password;
	private Channel channel = null;
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public void connect(boolean strictHostKeyChecking){
		JSch jsch = new JSch();
		Session session = null;
		
		try {
			session = jsch.getSession(this.username, this.host, this.port);
			session.setPassword(this.password);
			java.util.Properties config = new java.util.Properties();
			if (!strictHostKeyChecking) config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			session.setServerAliveCountMax(3000);
			
			this.channel = session.openChannel("sftp");
			this.channel.connect();
		} catch (JSchException jschException) {
			jschException.printStackTrace();
			throw new PlatformException("Java SFTP Error.");
		}
		
	}
	
	public boolean isConnected(){
		if (this.channel == null ){
			return false;
		}
		return this.channel.isConnected();
	}
	
	public void closeConnection(){
		this.channel.disconnect();
	}
	
	public int upload(String dataToUpload, String fileName, String remoteDirectory) throws PlatformException{		
		try {
			ChannelSftp channelSFTP = (ChannelSftp) this.channel;
			channelSFTP.cd(channelSFTP.getHome() + remoteDirectory);
			InputStream inStream = new ByteArrayInputStream(dataToUpload.getBytes("UTF-8"));
			channelSFTP.put(inStream, fileName);
			return 0;
		}catch(IOException ioException){
			ioException.printStackTrace();
			throw new PlatformException("No File Found to be put.");
			
		} catch (SftpException sftpException) {
			sftpException.printStackTrace();
			throw new PlatformException("Unable to Connect to SFTP.");	
		}
	}

}
