package com.fb.platform.ifs.util;

import com.google.gson.JsonObject;

public interface Jsonizable {
	JsonObject toJson() throws Exception;
}
