package com.linkage.module.gwms.util.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jason(3412)
 * @date 2009-9-22
 */
public class WanTypeUtil {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(WanTypeUtil.class);
	
	/**
	 * 根据wantype获取接入方式, wanType between (1,12)
	 * 
	 * 
	 * @param 业务用户表中的wan_type字段的值
	 * @author Jason(3412)
	 * @date 2009-9-22
	 * @return int 1:ADSL,2:LAN,3:EPON
	 */
	public static int getAccessType(int wanType){
		logger.debug("getAccessType({})", wanType);
		int accessType = -1;
		if(wanType >= 1 && wanType < 5){
			accessType = 1;
		} else if(wanType >= 5 && wanType < 9){
			accessType = 2;
		} else if(wanType >= 9 && wanType < 13){
			accessType = 3;
		}
		return accessType;
	}
	
	/**
	 * 根据wantype获取上网方式, wanType between (1,12)
	 * 
	 * 
	 * @param 业务用户表中的wan_type字段的值
	 * @author Jason(3412)
	 * @date 2009-9-22
	 * @return int 1:桥接,2:路由,3:静态IP,4:DHCP
	 */
	public static int getNetType(int wanType){
		logger.debug("getAccessType({})", wanType);
		int netType = -1;
		if(wanType == 1 || wanType == 5 || wanType == 9){
			netType = 1;
		} else if(wanType == 2 || wanType == 6 || wanType == 10){
			netType = 2;
		} else if(wanType == 3 || wanType == 7 || wanType == 11){
			netType = 3;
		} else if(wanType == 4 || wanType == 8 || wanType == 12){
			netType = 4;
		}
		return netType;
	}
	
	
	/**
	 * 根据接入方式和上网方式获取wan_type
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-23
	 * @return int
	 */
	public static int getWanType(int accessType, int netType){
		logger.debug("getWanType({},{})", accessType, netType);
		if(accessType <= 0 || accessType > 3){
			return 0;
		}
		if(netType <= 0 || netType > 4){
			return 0;
		}
		switch(accessType){
		case 1:
			return netType;
		case 2:
			return netType + 4;
		case 3:
			return netType + 8;
		default:
			return 0;
		}
	}
}
