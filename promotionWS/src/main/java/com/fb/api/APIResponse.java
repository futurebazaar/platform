package com.fb.api;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class APIResponse {

	private APIRequest request;
	private int status;
	private String statusCode;
	private String statusMessage;
	private List<String> errors;
	private long numFound;
	private long numReturned = APIConstants.DEFAULT_COUNT;
	private long startIndex = 0;
//	private List<Jsonizable> objects = new ArrayList<Jsonizable>();
	

	/**
	 * The status of response. Closely follows HTTP status codes
	 * 
	 * @return The status of the response
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Set the status of the response. Closely follow the HTTP status codes
	 * 
	 * @param status
	 *            Status of the response
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * The level two status of a response. E.g status is 400 (Bad request) and
	 * status code can either be INVALID_MOBILE or INVALID_EMAIL etc. Clients
	 * will use status to detect error flows and status code to fork out various
	 * error cases.
	 * 
	 * @return Status code of the resposne
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * Set the second level status code of response
	 * 
	 * @param statusCode
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * More verbose status message. The clients can choose to show this to the
	 * customer
	 * 
	 * @return
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Verbose the status message
	 * 
	 * @param statusMessage
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/**
	 * The list of errors during processing this request. The client can choose
	 * to show these messages to end customer
	 * 
	 * @return
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * Set the list of errors during processing this request. The client can
	 * choose to show these messages to end customer. Please be customer
	 * friendly.
	 * 
	 * @param errors
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * The request for which this is the response
	 * 
	 * @return
	 */
	public APIRequest getRequest() {
		return request;
	}

	/**
	 * Set the request for which this is the response
	 * 
	 * @param request
	 *            the request for which this is the response
	 */
	public void setRequest(APIRequest request) {
		this.request = request;
	}

	/** 
	 * How many objects are found
	 * @return Number of objects found
	 */
	public long getNumFound() {
		return numFound;
	}

	/**
	 * Set how many objects are found. Num found can be and normally is different from
	 * num returned. E.g found 183 mobiles, but returned only 10.
	 * @param numFound The number of objects found
	 */
	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}

	/**
	 * Number of objects returned in the response. Also see getNumFound
	 * @return Number of objects returned in response
	 */
	public long getNumReturned() {
		return numReturned;
	}

	/**
	 * Set the number of objects being returned.
	 * @param numReturned Number of objects being returned
	 */
	public void setNumReturned(long numReturned) {
		this.numReturned = numReturned;
	}

	/**
	 * Zero based index of the first element being returned in the entire set of objects
	 * found
	 * @return Zero based index of the first element being returned among entire set
	 */
	public long getStartIndex() {
		return startIndex;
	}

	/**
	 * Set the position of first element being returned in the entire set of results
	 * @param startIndex The zero based index of first element being returned amoung found
	 */
	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * Get the objects being returned
	 * @return the jsoinzable objects being returned
	 */
//	public List<Jsonizable> getObjects() {
//		return objects;
//	}

	/**
	 * The objects to be returned as part of response
	 * @param objects The objects to be returned as part of response
	 */
//	public void setObjects(List<Jsonizable> objects) {
//		this.objects = objects;
//	}
//
//	public void addObject(Jsonizable object) {
//		this.objects.add(object);
//	}
//
//	public String toJsonString() throws Exception{
//		JsonObject jsonObject = new JsonObject();
//		JsonArray jsonArray = new JsonArray();
//		
//
//		for(Jsonizable jObj : objects){
//			jsonArray.add((JsonElement) jObj.toJson());
//		}
//		jsonObject.add("objects",jsonArray);
//		jsonObject.addProperty("status", status);
//		jsonObject.addProperty("status_code", statusCode);
//		jsonObject.addProperty("status_message", statusMessage);
//		jsonObject.addProperty("num_found", numFound);
//		jsonObject.addProperty("num_returned",numReturned);
//		jsonObject.addProperty("startIndex", startIndex);
//		
//		
//		JsonArray jsonErrorArray = new JsonArray();
//		if(errors != null){
//			for(String error : errors){
//				jsonErrorArray.add(new JsonPrimitive(error));
//			}
//		}
//		
//		jsonObject.add("errors",jsonErrorArray);
//
//		String result = jsonObject.toString(); 
//		
//		return result;
//	}
}
