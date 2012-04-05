package com.fb.platform.ifs;


import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityRequestTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class IFSJsonTOFactory {

	private static final Gson gson = new Gson();
	/**
	 * Converts json into object
	 * @param postData
	 * @return
	 */
	public static SingleArticleServiceabilityRequestTO fromJson(JsonObject postData) {
		SingleArticleServiceabilityRequestTO ifsto = null;
		try {
			// XXX: Does this check that all members are sent in the json?
			// XXX: What happens in the call below if json only contains subset of
			// XXX: IFSTO members?
			ifsto = gson.fromJson(postData, SingleArticleServiceabilityRequestTO.class);
		} catch (Exception e) {
			// XXX: We should raise the exception here
			// TODO: handle exception
		}
		
		return ifsto;
	}
	
}
