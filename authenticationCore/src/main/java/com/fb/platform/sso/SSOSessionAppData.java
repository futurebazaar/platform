/**
 * 
 */
package com.fb.platform.sso;

import com.fb.commons.to.LoginType;

/**
 * This class represents the Session data that is held in the SSO AppData as formatted string.
 * @author vinayak
 *
 */
public class SSOSessionAppData {

	private LoginType loginType = null;

	/**
	 * THIS IS VERY IMPORTANT.
	 * If you are going to add any more fields in the ssoAppDataStr, then make sure that it is added at the end,
	 * separated by a ':'.
	 * Also, keep in mind that in case of hot deployment, old session data strings will be passed in. 
	 * This situation needs to be handled with sensible default values to the new fields.
	 *  
	 * Parses the ssoAppDataStr which should be in the format 
	 * loginType
	 * @param ssoAppDataStr
	 * @return
	 */
	public static SSOSessionAppData parse(String ssoAppDataStr) {
		if (ssoAppDataStr == null) {
			return null; 
		}
		String result [] = ssoAppDataStr.split(":", -1);

		SSOSessionAppData appData = new SSOSessionAppData();
		for(int i = 0; i < result.length; i++) {
			switch (i) {
			case 0:
				appData.setLoginType(LoginType.valueOf(result[0]));
			}
		}
		return appData;
	}

	public String getSSOAppDataString() {
		StringBuffer sb = new StringBuffer();
		sb.append(loginType.toString());
		return sb.toString();
	}

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}
}
