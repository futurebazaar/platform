package com.fb.platform.user.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import sun.misc.BASE64Encoder;
import sun.misc.CharacterEncoder;

public final class PasswordService
{
  private static PasswordService instance;

  private PasswordService()
  {
  }

  public synchronized String getencrypt(String plaintext)  
  {
    MessageDigest md = null;
    String randomstr =  UUID.randomUUID().toString().substring(0,5);
    try
    {
      md = MessageDigest.getInstance("SHA-1"); //step 2
    }
    catch(NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    try
    {
      md.update((randomstr+plaintext).getBytes("UTF-8")); //step 3
    }
    catch(UnsupportedEncodingException e)
    {
    	e.printStackTrace();
    }

    byte raw[] = md.digest(); //step 4
    String hash = (new BASE64Encoder()).encode(raw); //step 5
    String password = "sha1$" + randomstr + "$" + hash;
    return password; //step 6
  }
  
  public synchronized boolean compareencrypt(String plaintext,String hashedpassword)  
  {
	String algo = hashedpassword.split("$")[0],randomstr = hashedpassword.split("$")[1] ,hashpass = hashedpassword.split("$")[2];
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("SHA-1"); //step 2
    }
    catch(NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    try
    {
      md.update((randomstr+plaintext).getBytes("UTF-8")); //step 3
    }
    catch(UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }

    byte raw[] = md.digest(); //step 4
    String hash = (new BASE64Encoder()).encode(raw); //step 5
    return (hashpass.equals(hash)); //step 6
  }
  
  public static synchronized PasswordService getInstance() //step 1
  {
    if(instance == null)
    {
       instance = new PasswordService(); 
    } 
    return instance;
  }
}