package com.linkage.module.gtms.service.serv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;

public class SendRouterOpenSheetServImp implements SendRouterOpenSheetServ{
	
	private static Logger logger = LoggerFactory
			.getLogger(SendRouterOpenSheetServImp.class);
	
	
	/**
	 * 路由业务  发模拟工单
	 * 
	 * @param servTypeId
	 * @param operateType  1：开户   3：销户
	 * @param cityId
	 * @param netUsername
	 * @param netPassword
	 * @param gw_type
	 * @return
	 */
	public String sendSheet(String servTypeId, String operateType, String cityId,
			String netUsername, String netPassword, String gw_type){
		
		logger
				.debug("SendRouterOpenSheetServImp==>sendSheet({},{},{},{},{},)",
						new Object[] { servTypeId, operateType, cityId, netUsername,
								netPassword });
		
		DateTimeUtil dt = new DateTimeUtil();
		
		StringBuffer bssSheet = new StringBuffer("");
		
		bssSheet.append("6").append("|||");
		bssSheet.append(servTypeId).append("|||");
		bssSheet.append(operateType).append("|||");
		bssSheet.append(dt.getYYYYMMDDHHMMSS()).append("|||");
		bssSheet.append(cityId).append("|||");
		bssSheet.append(netUsername);
		bssSheet.append("LINKAGE");
		
//		if ("3".equals(operateType)) {
//			bssSheet.append("LINKAGE");
//		} else {
//			bssSheet.append("|||").append(netPassword).append("LINKAGE");
//		}
		
		logger.warn(bssSheet.toString());
		
		return this.sendSheet(bssSheet.toString(), gw_type);
	}
	
	
	
	/**
	 * 向工单接口发送模拟的BSS工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @param 模拟工单数据
	 * @author zhangchy
	 * @date 2012-06-25
	 * @return String 回单信息
	 */
	public String sendSheet(String bssSheet, String gw_type)
	{
		logger.debug("sendSheet({})", bssSheet);
		if (StringUtil.IsEmpty(bssSheet))
		{
			logger.warn("sendSheet is null");
			return null;
		}
		
		if ("1".equals(gw_type)) {
			return SocketUtil.sendStrMesg(Global.G_ITMS_Sheet_Server,
					Global.G_ITMS_Sheet_Port, bssSheet + "\n");
		} else {
			return SocketUtil.sendStrMesg(Global.G_BBMS_Sheet_Server,
					Global.G_BBMS_Sheet_Port, bssSheet + "\n");
		}
	}
}
