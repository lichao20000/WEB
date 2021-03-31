package com.linkage.module.bbms.resource.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-10-13
 */
public class SendBssSheetBIO {

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory
			.getLogger(SendBssSheetBIO.class);
	
	
	
	/**
	 * 向工单接口发送模拟的BSS工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @param 模拟工单数据
	 * @author Jason(3412)
	 * @date 2009-10-13
	 * @return String 回单信息
	 */
	public String sendSheet(String bssSheet){
		logger.debug("sendSheet({})", bssSheet);
		if(StringUtil.IsEmpty(bssSheet)){
			logger.warn("bssSheet is null");
			return null;
		}
		String server = Global.G_BBMS_Sheet_Server;
		int port = Global.G_BBMS_Sheet_Port;
		String retResult = SocketUtil.sendStrMesg(server, port, bssSheet + "\n");
		return retResult;
	}
	
}
