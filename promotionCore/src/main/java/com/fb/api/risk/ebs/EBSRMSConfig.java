package com.fb.api.risk.ebs;


public class EBSRMSConfig {

	private String validateTransection;
	private String proxyIP;
	private String proxyPort;	
	private String rmsLicenseKey;
	private String rmsTemplate;
	private String rmsSite;
	private String requestURL;
	private String accountID;
	private String ebsServerOne;
	private String ebsServerTwo;

	public String getEbsServerOne() {
		return ebsServerOne;
	}
	public void setEbsServerOne() {
		this.ebsServerOne = EBSConfigPropertyReader.getValue(ebsServerOne);
	}
	public String getEbsServerTwo() {
		return ebsServerTwo;
	}
	public void setEbsServerTwo() {
		this.ebsServerTwo = EBSConfigPropertyReader.getValue(ebsServerTwo);
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID() {
		this.accountID = EBSConfigPropertyReader.getValue(accountID);
	}
	public String getValidateTransection() 
	{
		return validateTransection;
	}
	public void setValidateTransection() 
	{
		this.validateTransection = EBSConfigPropertyReader.getValue(validateTransection);
	}	
	
	public String getProxyIP() 
	{
		return proxyIP;
	}	
	public void setProxyIP() 
	{
		this.proxyIP=EBSConfigPropertyReader.getValue(proxyIP);
	}
	
	public String getProxyPort() 
	{
		return proxyPort;
	}
	public void setProxyPort() 
	{
		this.proxyPort = EBSConfigPropertyReader.getValue(proxyPort);
	}
	
	public String getRmsLicenseKey() 
	{
		return rmsLicenseKey;
	}
	public void setRmsLicenseKey() 
	{
		this.rmsLicenseKey = EBSConfigPropertyReader.getValue(rmsLicenseKey);
	}
	
	public String getRmsTemplate() 
	{
		return rmsTemplate;
	}
	public void setRmsTemplate() 
	{
		this.rmsTemplate = EBSConfigPropertyReader.getValue(rmsTemplate);
	}
	
	public String getRmsSite() 
	{
		return rmsSite;
	}
	public void setRmsSite() {
		this.rmsSite = EBSConfigPropertyReader.getValue(rmsSite);
	}	
	
	public String getRequestURL() {
		return requestURL;
	}
	public void setRequestURL() {
		this.requestURL = EBSConfigPropertyReader.getValue(requestURL);
	}
}
