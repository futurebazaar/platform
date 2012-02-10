package com.fb.exception;

import com.fb.api.interfaces.Jsonizable;
import com.fb.util.Jsonutil;
import com.google.gson.JsonObject;

public class Promotion implements Jsonizable {

	@Override
	public JsonObject toJson() throws Exception {
		return Jsonutil.gson.toJsonTree(this).getAsJsonObject();
	}
}
