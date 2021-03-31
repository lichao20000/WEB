/**
 * @(#)AcctUtil.java 2006-2-9
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.liposs.aaa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;

/**
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class AcctUtil {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(AcctUtil.class);
	/*
	 * default info of accounts
	 */
	public static final String	DEFAULT_CREATOR				= "unkownCreator";
	public static final String	DEFAULT_CITY				= "unkownCity";
	public static final String	DEFAULT_MEMO				= "noMemo";
	public static final Date	DEFAULT_CREATE_DATE			= new Date();
	public static final Date	DEFAULT_EFFECT_DATE			= new Date();
	public static final Date	DEFAULT_EXPIRED_DATE		= new Date(2999,11,31);
	public static final String	DEFAULT_CREATE_DATE_STR		= String.valueOf(DEFAULT_CREATE_DATE.getTime()/1000);
	public static final String	DEFAULT_EFFECT_DATE_STR		= String.valueOf(DEFAULT_EFFECT_DATE.getTime()/1000);
	public static final String	DEFAULT_EXPIRED_DATE_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000);
	public static final String	DEFAULT_NO_VALID_DEVICE		= "1.1.1.1";
	/*
	 * 86400		=1  day
	 * 2592000		=30 day  =1 mon
	 * 31536000		=1  year
	 */
	public static final String	DEFAULT_EXPIRED_1_DAY_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000 + 86400);
	public static final String	DEFAULT_EXPIRED_7_DAY_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000 + 86400*7);
	public static final String	DEFAULT_EXPIRED_15_DAY_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000 + 86400*15);
	public static final String	DEFAULT_EXPIRED_1_MON_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000 + 86400*30);
	public static final String	DEFAULT_EXPIRED_2_MON_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000 + 86400*30*2);
	public static final String	DEFAULT_EXPIRED_3_MON_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000 + 86400*30*3);
	public static final String	DEFAULT_EXPIRED_6_MON_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000 + 86400*30*6);
	public static final String	DEFAULT_EXPIRED_1_YEAR_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000 + 86400*365);
	public static final String	DEFAULT_EXPIRED_2_YEAR_STR	= String.valueOf(DEFAULT_EXPIRED_DATE.getTime()/1000 + 86400*365*2);
	/*
	 * result of operation for feeding back.
	 */
	public static final int ACCOUNTS_CREATE_SUCCESS		= 1;
	public static final int ACCOUNTS_DEL_SUCCESS		= 2;
	public static final int ACCOUNTS_MOD_SUCCESS		= 3;
	public static final int ACCOUNTS_EXISTED			= -1;
	public static final int ACCOUNTS_NOT_EXISTED		= -2;
	public static final int ACCOUNTS_INVALID_USERNAME	= -3;
	public static final int ACCOUNTS_INVALID_PASSWORD	= -4;
	public static final int ACCOUNTS_DEL_FAILED			= -5;
	public static final int ACCOUNTS_MOD_FAILED			= -6;
	public static final int ACCOUNTS_DATEBASE_FAILED	= -10;
	public static final int ACCOUNTS_UNKOWN_ERROR		= -100;
	
	/////////////////////////////////////////////////////////////////
	public static UserManagerImpl DEFAULT_USER_MANAGER = new UserManagerImpl();
	public static int 		DEFAULT_MAX_RETRY_TIME		= 10;
	public static int		DEFAULT_SEGMENT_OF_TIME		= 3600;//seconds
	public static String 	TIME_NOW					= String.valueOf(new Date().getTime()/1000);
	public static String	TIME_START_POINT			= String.valueOf(new Date().getTime()/1000 - DEFAULT_SEGMENT_OF_TIME);
	
	public static int		UTIL_OK						= 10;
	public static int		UTIL_BLOCKED				= -10;
	public static int		UTIL_USER_NAME_OK			= 20;
	public static int		UTIL_USER_NAME_ILLEGAL		= -20;
	public static int		UTIL_USER_PWD_OK			= 30;
	public static int		UTIL_USER_PWD_ILLEGAL		= -30;
	public static int		UTIL_UNKOWN_ERROR			= -1000;
	
	/**
	 * find out weather some guy has been refused many times
	 * within a short time(1 hour etc.)
	 * @param userName
	 * 		user name
	 * @return
	 * 		result
	 */
	public static int isBlocked(String userName) {
		String sql = "select count(*) from rad_record " +
				"where server_reply='Access-Reject' and " +
				"login_time between '" + TIME_START_POINT +"' and '" + TIME_NOW + 
				"' and user_name='" + userName + "'";
//		log("isBlocked: " + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		ArrayList rec = DEFAULT_USER_MANAGER.getRecord(psql.getSQL());
		if(rec == null) {
			return UTIL_UNKOWN_ERROR;
		}
		
		String result = ( (Map)rec.get(0) ).toString();
		int len = result.length();
		//logger.debug("isBlocked: result: " + result);
		result = result.substring(2, len-1);
		//logger.debug("isBlocked: result: " + result);
		
		int retried = Integer.parseInt(result);
		logger.debug("isBlocked: retried: " + retried);
		if(retried >= DEFAULT_MAX_RETRY_TIME) {
			return UTIL_BLOCKED;
		}
		return UTIL_OK;
	}
	
	/**
	 * find out weather some guy has been refused many times
	 * within a short time(1 hour etc.)
	 * @param acct
	 * 		accounts
	 * @return
	 * 		result
	 */
	public static int isBlocked(Accounts acct) {
		return isBlocked(acct.getUserName());
	}
	
	/**
	 * 检测用户名密码是否合法.
	 * 
	 * @param userName
	 * @param pwd
	 * @return
	 */
	public static int verifyUser(String userName, String pwd) {
		if(userName.length() <= 2 || userName.length() >= 51) {
//			AcctUtil.log("verifyUser: Code: " + ACCOUNTS_INVALID_USERNAME +
//								", invalid user name suplied.");
			return ACCOUNTS_INVALID_USERNAME;
		}
		for(int i=0; i<userName.length(); i++){
			if (!((userName.charAt(i) <= '9'&& userName.charAt(i) >= '0') || 
				(userName.charAt(i) <= 'z'&& userName.charAt(i) >= 'a') ||
				(userName.charAt(i) <= 'Z'&& userName.charAt(i) >= 'A') ||
				userName.charAt(i) == '_' || 
				userName.charAt(i) == '-')){
//				AcctUtil.log("verifyUser: Code: " + ACCOUNTS_INVALID_USERNAME +
//									", invalid user name suplied.");
				return ACCOUNTS_INVALID_USERNAME;
			}	
		}
		if(pwd.length() <= 2 || pwd.length() >= 51) {
//			AcctUtil.log("verifyUser: Code: " + ACCOUNTS_INVALID_PASSWORD +
//								", invalid password suplied.");
			return ACCOUNTS_INVALID_PASSWORD;
		}
		for(int i=0; i<pwd.length(); i++){
			if (pwd.charAt(i) == '`'  ||
				pwd.charAt(i) == '"'  ||
				pwd.charAt(i) == '\'' ||
				pwd.charAt(i) == '?'  ||
				pwd.charAt(i) == '%') {
//				AcctUtil.log("verifyUser: Code: " + ACCOUNTS_INVALID_PASSWORD +
//				", invalid password suplied.");
				return ACCOUNTS_INVALID_PASSWORD;
			}
		}
		return UTIL_USER_NAME_OK;
	}
	
	/**
	 * log some important message
	 * @param message
	 */
    public static void log(final String message){
    	logger.debug(message);
    	File file=null;
    	FileWriter logit=null;
	    try {
			file = new File("d:\\Radius_web\\Radius_web.log");
			logit = new FileWriter(file, true);
			Date time = new Date();
			logit.write('[');
			logit.write(String.valueOf(time.getYear()+1900));
			logit.write('-');
			logit.write(String.valueOf(time.getMonth()+1));
			logit.write('-');
			logit.write(String.valueOf(time.getDate()));
			logit.write(' ');
			logit.write(String.valueOf(time.getHours()));
			logit.write(':');
			logit.write(String.valueOf(time.getMinutes()));
			logit.write(':');
			logit.write(String.valueOf(time.getSeconds()));
			logit.write("] ");
			logit.write(message);
			logit.write('\n');
			logit.close();
	    }
	    catch (IOException e) {
			logger.warn(e.getMessage());
	    }finally{
	    	try {
	    		if(logit!=null){
	    			logit.close();
	    		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    }
}
