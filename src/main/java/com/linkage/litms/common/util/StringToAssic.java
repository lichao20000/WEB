package com.linkage.litms.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StringToAssic {
	private static Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);
	public static String toAssic(String str) {
		
		String assicStr = "";
		
		byte[] v = null;
		
		if(str != null && !str.equals("")){
			try {
				v = str.getBytes("US-ASCII");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
		for (int i = 0; i < v.length; i++){
			
			assicStr += String.valueOf(v[i]);
			logger.debug(String.valueOf(v[i]));
		}
		
		logger.debug("assicStr 转换成assic ：" + assicStr);
		
		return assicStr;
	}
	
	

}
