package com.fb.platform.ifs.manager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fb.platform.ifs.util.Jsonizable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class SingleArticleServiceabilityResponseTO implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String responseCode;
	private String responseMessage;
	private List<Jsonizable> items = new ArrayList<Jsonizable>();
	private long numFound;
	private long numReturned = 101;
	private long startIndex = 0;
	private List<String> errors;
	private String sessionToken = null;
	
	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getStatusCode() {
		return responseCode;
	}

	public void setStatusCode(String statusCode) {
		this.responseCode = statusCode;
	}

	public String getStatusMessage() {
		return responseMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.responseMessage = statusMessage;
	}

	public List<Jsonizable> getObjects() {
		return items;
	}


	public void setObjects(List<Jsonizable> objects) {
		this.items = objects;
	}

	public void addObject(Jsonizable object) {
		this.items.add(object);
	}

	public String toJsonString() throws Exception{
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		

		for(Jsonizable jObj : items){
			jsonArray.add((JsonElement) jObj.toJson());
		}
		jsonObject.add("items",jsonArray);
		jsonObject.addProperty("responseCode", responseCode);
		jsonObject.addProperty("responseMessage", responseMessage);
		jsonObject.addProperty("num_found", numFound);
		jsonObject.addProperty("num_returned",numReturned);
		jsonObject.addProperty("startIndex", startIndex);
		
		JsonArray jsonErrorArray = new JsonArray();
		if(errors != null){
			for(String error : errors){
				jsonErrorArray.add(new JsonPrimitive(error));
			}
		}
		
		jsonObject.add("errors",jsonErrorArray);

		String result = jsonObject.toString(); 
		
		return result;
	}

	/**
	 * @return the numFound
	 */
	public long getNumFound() {
		return numFound;
	}

	/**
	 * @param numFound the numFound to set
	 */
	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}

	/**
	 * @return the numReturned
	 */
	public long getNumReturned() {
		return numReturned;
	}

	/**
	 * @param numReturned the numReturned to set
	 */
	public void setNumReturned(long numReturned) {
		this.numReturned = numReturned;
	}

	/**
	 * @return the startIndex
	 */
	public long getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
