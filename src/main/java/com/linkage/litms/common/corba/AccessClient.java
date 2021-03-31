package com.linkage.litms.common.corba;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.util.corba.ACSCorba;

public class AccessClient {

	/**
	 * 
	 */
	public AccessClient() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int DoRPC(String access_flag) {
		int sheetObj = 0;
		if (access_flag != null) {
			sheetObj = new ACSCorba().chgInfo(StringUtil.getIntegerValue(access_flag), 3, "");
		}
		return sheetObj;
	}

	public int reDoRPC(String access_flag) {
		int sheetObj = 0;
		if (access_flag != null) {
			sheetObj = new ACSCorba().chgInfo(StringUtil.getIntegerValue(access_flag), 3, "");
		}
		return sheetObj;
	}
}
