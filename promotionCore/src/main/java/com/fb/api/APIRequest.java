package com.fb.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class APIRequest {
	/**
	 * A class to represent an API request. Handles default translation of HTTP
	 * request into to more API friendly object. Exposes methods to do common
	 * operations during API request response cycle
	 */

	public static String HTTP_METHOD_GET = "GET";
	public static String HTTP_METHOD_POST = "POST";
	public static String HTTP_METHOD_DELETE = "DELETE";
	public static String HTTP_METHOD_PUT = "PUT";
	private static SimpleDateFormat MMddyyyy = new SimpleDateFormat("MMddyyyy");

	private String httpMethod = HTTP_METHOD_GET;
	private Map<String, String[]> params;
	private JsonObject postData;
	private JsonObject header;

	private static final JsonParser jsonParser = new JsonParser();
	
	public static APIRequest createAPIRequest(HttpServletRequest req) throws IOException {
		APIRequest apiRequest = new APIRequest();
		apiRequest.setParams(req.getParameterMap());
	
		if(req.getMethod().equals("GET")){
			apiRequest.setHttpMethod(APIRequest.HTTP_METHOD_GET);
		}

		if(req.getMethod().equals("POST")){
			apiRequest.setHttpMethod(APIRequest.HTTP_METHOD_POST);
		}
		
		if(req.getMethod().equals("DELETE")){
			apiRequest.setHttpMethod(APIRequest.HTTP_METHOD_DELETE);
		}
		if(req.getMethod().equals("PUT")){
			apiRequest.setHttpMethod(APIRequest.HTTP_METHOD_PUT);
		}
		return apiRequest;
		
	}
	
	public static APIRequest createAPIRequest(HttpServletRequest req, String postData) throws IOException {
		APIRequest apiRequest = createAPIRequest(req);
		JsonObject jsonObject = (JsonObject)jsonParser.parse(postData);
		apiRequest.setPostData(jsonObject);
		return apiRequest;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public JsonObject getPostData() {
		return postData;
	}

	public void setPostData(JsonObject postData) {
		this.postData = postData;
	}

	public JsonObject getHeader() {
		return header;
	}

	public void setHeader(JsonObject header) {
		this.header = header;
	}

	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	public Map<String, String[]> getParams() {
		return this.params;
	}

	/**
	 * Returns a query parameter as string. Returns null if param is not set
	 * 
	 * @param param
	 *            The name of query string parameter
	 * @return String or null
	 */
	public String getParamAsString(String param) {
		if (params.containsKey(param)) {
			if (params.get(param).length > 0) {
				return params.get(param)[0];
			}
		}
		return null;
	}

	/**
	 * Returns a query parameter as string or the supplied opt
	 * 
	 * @param param
	 * @param opt
	 * @return String or null
	 */
	public String getParamAsString(String param, String opt) {
		String s = getParamAsString(param);
		if (s != null) {
			return s;
		}
		return opt;
	}

	/**
	 * Returns query param as integer. Returns null if param is not set
	 * 
	 * @param param
	 *            Name of the query parameter
	 * @return
	 */
	public Integer getParamAsInt(String param) {
		String s = getParamAsString(param);
		if (s != null) {
			return Integer.parseInt(s);
		}
		return null;
	}

	/**
	 * Returns query param as integer or the opt if not found
	 * 
	 * @param param
	 * @param opt
	 * @return
	 */
	public Integer getParamAsInt(String param, Integer opt) {
		Integer i = getParamAsInt(param);
		if (i != null) {
			return i;
		}
		return opt;
	}

	/**
	 * Returns query param as long. Returns null if param is not set
	 * 
	 * @param param
	 *            Name of the query parameter
	 * @return
	 */
	public Long getParamAsLong(String param) {
		String s = getParamAsString(param);
		if (s != null) {
			return Long.parseLong(s);
		}
		return null;
	}

	/**
	 * Returns query param as long or the opt if not found
	 * 
	 * @param param
	 * @param opt
	 * @return
	 */
	public Long getParamAsLong(String param, Long opt) {
		Long l = getParamAsLong(param);
		if (l != null) {
			return l;
		}
		return opt;
	}

	/**
	 * Returns query param as java date object. We expect the date to be in
	 * MMddyyyy format If the date is not the format expected, we return null so
	 * that API code assume that date is not set. We are assuming that wrong
	 * date format is as good as no date. Returns null if param is not set
	 * 
	 * @param param
	 *            Name of the query parameter
	 * @return
	 */
	public Date getParamAsDate(String param) {
		String s = getParamAsString(param);
		if (s != null) {
			try {
				return MMddyyyy.parse(s);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Returns query param as java date object or the supplied opt
	 * 
	 * @param param
	 *            Name of the query parameter
	 * @param opt
	 * @return
	 */
	public Date getParamAsDate(String param, Date opt) {
		Date date = getParamAsDate(param);
		if (date != null) {
			return date;
		}
		return opt;
	}

	/**
	 * Return query param as boolean. Return null if not set
	 * 
	 * @param param
	 *            Name of the query parameter
	 * @return
	 */
	public Boolean getParamAsBool(String param) {
		String s = getParamAsString(param);
		if (s != null) {
			return Boolean.parseBoolean(s);
		}
		return null;
	}

	/**
	 * Return query param as boolean or the supplied opt
	 * 
	 * @param param
	 *            Name of the query parameter
	 * @param opt
	 *            Optional boolean value to be returned if param is not set
	 * @return
	 */
	public Boolean getParamAsBool(String param, Boolean opt) {
		Boolean b = getParamAsBool(param);
		if (b != null) {
			return b;
		}
		return opt;
	}

	/**
	 * Zero based index of start position. Useful for get requests returning a
	 * list of objects and want to do it in chunks. Defaults to 0. Also see
	 * getCount
	 * 
	 * @return Zero based index of start position in a list of objects to be
	 *         returned
	 */
	public long getStartIndex() {
		return getParamAsLong(APIConstants.APIKeys.START_INDEX, 0l);
	}

	/**
	 * How many objects are to be returned. Defaults to DEFAULT_COUNT. Restricts
	 * to MAX_COUNT if client requested count is more than MAX_COUNT. Also see
	 * getStartIndex
	 * 
	 * @return How many objects are to be returned
	 */
	public long getNumRows() {
		return Math.max(
				getParamAsLong(APIConstants.APIKeys.NUM_ROWS,
						APIConstants.DEFAULT_COUNT), APIConstants.MAX_COUNT);
	}

	/**
	 * Is request fired for debugging
	 * 
	 * @return
	 */
	public boolean isDebug() {
		// Clients can enable debugging by sending special parameter name DEBUG
		return this.params.containsKey(APIConstants.APIKeys.DEBUG);
	}

	public String getAction() {
		if (this.httpMethod.equals(HTTP_METHOD_POST)) {
			if (this.postData != null) {
				if (this.header != null) {
					if (this.header.has(APIConstants.APIKeys.ACTION)) {
						return this.header.get(APIConstants.APIKeys.ACTION)
								.getAsString();
					}
				}
			}
		}
		return APIConstants.NO_ACTION;
	}

	public JsonObject getData() {
		if (this.httpMethod.equals(HTTP_METHOD_POST)) {
			if (this.postData != null) {
				if (this.postData.has(APIConstants.APIKeys.DATA)) {
					return this.postData.get(APIConstants.APIKeys.DATA)
							.getAsJsonObject();
				}
			}
		}
		return null;
	}
}
