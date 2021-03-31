package com.linkage.module.gtms.stb.resource.serv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.service.dao.SimulateSheetNxNewDAO;

public class SimulateSheetStbBIO {
	private static Logger logger = LoggerFactory
			.getLogger(SimulateSheetStbBIO.class);


	public String sendOpenSheet(String servTypeId,String operateType,String dealdate,String userType,
			String loidMsg,String netUsername,String cityId,String bussAccount,
			String bussPwd,String wlantype,String ConnAccount,String connPwd,String ipAddr,
			String hideNode,String netCheck,String dnsMsg,String stbMacMsg) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(loidMsg).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(bussAccount).append("|||");
		sbuffer.append(bussPwd).append("|||");
		sbuffer.append(wlantype).append("|||");
		sbuffer.append(ConnAccount).append("|||");
		sbuffer.append(connPwd).append("|||");
		sbuffer.append(ipAddr).append("|||");
		sbuffer.append(hideNode).append("|||");
		sbuffer.append(netCheck).append("|||");
		sbuffer.append(dnsMsg).append("|||");
		sbuffer.append(stbMacMsg).append("LINKAGE");
		logger.debug("新装工单：" + sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}
	public String sendStopSheet(String servTypeId,String operateType,String dealdate,String userType,
			String loidMsg,String cityId,String bussAccount) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(loidMsg).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(bussAccount).append("LINKAGE");
		logger.debug("拆机工单：" + sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}
	
	public String sendChangeAccSheet(String servTypeId,String operateType,String dealdate,String userType,
			String loidMsg,String cityId,String oldBussAccount,String newBussAccount) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(loidMsg).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(oldBussAccount).append("|||");
		sbuffer.append(newBussAccount).append("LINKAGE");
		logger.debug("修改业务账号工单：" + sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}
	
	public String sendChangeAccPwdSheet(String servTypeId,String operateType,String dealdate,String userType,
			String loidMsg,String cityId,String bussAccount,String newPwd) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(loidMsg).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(bussAccount).append("|||");
		sbuffer.append(newPwd).append("LINKAGE");
		logger.debug("修改业务帐号密码工单：" + sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}
	
	public String changeStb(String servTypeId,String operateType,String dealdate,String userType,
			String loidMsg,String cityId,String bussAccount,String stbMacMsg) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(loidMsg).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(bussAccount).append("|||");
		sbuffer.append(stbMacMsg).append("LINKAGE");
		logger.debug("更换机顶盒工单：" + sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}
	
	private String transferDateFormate(String dealdate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(dealdate);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.warn("工单受理时间转换异常");
		}
		dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}

	public String sendSheet(String bssSheet) {
		logger.debug("sendSheet({})", bssSheet);
		if (StringUtil.IsEmpty(bssSheet)) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_BSS_Server;
		int port = Global.G_ITMS_Sheet_BSS_Port;
		String retResult = SocketUtil
				.sendStrMesg(server, port, bssSheet + "\n");
		if (null == retResult || "".equals(retResult)) {
			return "";
		}
		return retResult;
	}



	public static Logger getLogger() {
		return logger;
	}
}
