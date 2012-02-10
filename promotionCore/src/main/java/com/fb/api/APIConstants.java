package com.fb.api;

public class APIConstants {

	public class APIKeys {
		public static final String ACTION = "action";
		public static final String DATA = "data";
		public static final String NUM_ROWS = "nr";
		public static final String START_INDEX = "si";
		// The set of keys which are not really functional stuff are prefixed and suffixed
		// with _ so that we do not pollute the application key space.
		public static final String DEBUG = "_debug_";
	}
	
	public static final String NO_ACTION = "NO_ACTION";
	public static final Long DEFAULT_COUNT = 10l;
	public static final Long MAX_COUNT = 100l;

}
