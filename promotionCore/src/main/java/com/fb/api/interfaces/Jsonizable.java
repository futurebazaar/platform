package com.fb.api.interfaces;

import com.google.gson.JsonObject;

public interface Jsonizable {
	JsonObject toJson() throws Exception;
}
