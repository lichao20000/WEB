package com.linkage.module.itms.service.bio;

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

public class SimulateSheetNxNewBIO {
	private static Logger logger = LoggerFactory
			.getLogger(SimulateSheetNxNewBIO.class);
	private SimulateSheetNxNewDAO dao;

	public String checkUsername(String username, String gw_type) {
		List<Map<String, String>> userList = this.dao.getUserInfo(username,
				gw_type);
		int size = userList.size();
		if (size == 1) {
			return "1#"
					+ (String) ((Map<String, String>) userList.get(0))
							.get("orderType");
		}
		if (size > 1) {
			return "0#请输入完整的逻辑SN";
		}
		return "-1#逻辑SN不存在，请先走建设流程";
	}

	public String sendOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String officeId, String zoneId, int orderType, String linkman,
			String linkphone, String linkEmail, String linkMobile,
			String homeAddr, String credNo, String customerId,
			String customerAccount, String customerPwd) {
		logger.debug("sendOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(officeId).append("|||");
		sbuffer.append(zoneId).append("|||");
		sbuffer.append(orderType).append("|||");
		sbuffer.append(linkman).append("|||");
		sbuffer.append(linkphone).append("|||");
		sbuffer.append(linkEmail).append("|||");
		sbuffer.append(linkMobile).append("|||");
		sbuffer.append(homeAddr).append("|||");
		sbuffer.append(credNo).append("|||");
		sbuffer.append(customerId).append("|||");
		sbuffer.append(customerAccount).append("|||");
		sbuffer.append(customerPwd).append("|||");
		sbuffer.append("e8cp42").append("FROMWEB");
		logger.warn("开户工单：" + sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}

	public String sendStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType) {
		logger.debug("sendStopSheet()");

		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("FROMWEB");
		return sendSheet(sbuffer.toString());
	}

	public String sendStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType, String sheettype) {
		logger.debug("sendStopSheet()");

		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(sheettype).append("FROMWEB");
		return sendSheet(sbuffer.toString());
	}

	public String sendIptvStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType,
			String iptvUsername, String iptvLanPort) {
		logger.debug("sendStopSheet()");

		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(iptvLanPort).append("FROMWEB");
		return sendSheet(sbuffer.toString());
	}

	public String sendNetOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String netUsername, String netPassword, String vlanId,
			String userType, String wlantype, String ipaddress, String code,
			String netway, String dns, String vpi, String vci, String ipType, String sheettype) {
		logger.warn("sendNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(netPassword).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(vlanId).append("|||");
		sbuffer.append(wlantype).append("|||");
		sbuffer.append(ipaddress).append("|||");
		sbuffer.append(code).append("|||");
		sbuffer.append(netway).append("|||");
		sbuffer.append(dns).append("|||");
		sbuffer.append(vpi).append("|||");
		sbuffer.append(vci).append("|||");
		sbuffer.append(ipType).append("|||");
		sbuffer.append(sheettype).append("FROMWEB");
		logger.warn("上网:" + sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}

	public String sendH248VoipOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String sipIp,
			int sipPort, String standSipIp, int standSipPort, String linePort,
			String voipTelepone, String userType, String regId) {
		logger.debug("sendNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(voipTelepone).append("|||");
		sbuffer.append(cityId).append("|||");

		sbuffer.append(regId).append("|||");
		sbuffer.append(1).append("|||");
		sbuffer.append(sipIp).append("|||");
		sbuffer.append(sipPort).append("|||");
		sbuffer.append(standSipIp).append("|||");
		sbuffer.append(standSipPort).append("|||");
		sbuffer.append(linePort).append("|||");
		sbuffer.append("45").append("FROMWEB");
		logger.warn("h248工单:" + sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}

	public String sendVoipStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String voipTelepone, String userType) {
		logger.debug("sendStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipTelepone).append("FROMWEB");
		return sendSheet(sbuffer.toString());
	}

	public String sendIptvOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String voipUsername, String iptvUsername, int servNum,
			String iptvLanPort, String userType) {
		logger.debug("sendIptvOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(servNum).append("|||");
		sbuffer.append(iptvLanPort).append("|||");
		sbuffer.append("43").append("FROMWEB");
		logger.warn("iptv工单:" + sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}
	   
	
	public String sendSIPCloseSheet(String servTypeId, String operateType, String dealdate,String userType,
			String  username,String cityId,String voipAccount){
		logger.debug("sendSIPOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipAccount).append("LINKAGE");
		logger.warn("sendSIPCloseSheet----"+sbuffer.toString());
		return sendSheet(sbuffer.toString());
	}
	
	public String sendSIPOpenSheet(String servTypeId, String operateType,String dealdate, String userType,
			String username, String bussinessTel, String cityId,
			String voipAccount, String voipPassword, String ProxyServer,
			String ProxyServerPort, String ProxyServer_a,String ProxyServerPort_a, String voiceport,
			String RegistrarServer, String RegistrarServerPort,String RegistrarServer_a, String RegistrarServerPort_a,
			String OutboundProxy, String OutboundProxyPort,String OutboundProxy_a, String OutboundProxyPort_a,
			String agreementType, String vlanId,String wlantype, String ipaddress,String code,
			String netway,String dns,String vpi,String vci) {
		logger.debug("sendSIPOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(bussinessTel).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipAccount).append("|||");
		sbuffer.append(voipPassword).append("|||");
		sbuffer.append(ProxyServer).append("|||");
		sbuffer.append(ProxyServerPort).append("|||");
		sbuffer.append(ProxyServer_a).append("|||");
		sbuffer.append(ProxyServerPort_a).append("|||");
		sbuffer.append(voiceport).append("|||");
		sbuffer.append(RegistrarServer).append("|||");
		sbuffer.append(RegistrarServerPort).append("|||");
		sbuffer.append(RegistrarServer_a).append("|||");
		sbuffer.append(RegistrarServerPort_a).append("|||");
		sbuffer.append(OutboundProxy).append("|||");
		sbuffer.append(OutboundProxyPort).append("|||");
		sbuffer.append(OutboundProxy_a).append("|||");
		sbuffer.append(OutboundProxyPort_a).append("|||");
		sbuffer.append(agreementType).append("|||");
		sbuffer.append(vlanId).append("|||");
		sbuffer.append(wlantype).append("|||");
		sbuffer.append(ipaddress).append("|||");
		sbuffer.append(code).append("|||");
		sbuffer.append(netway).append("|||");
		sbuffer.append(dns).append("LINKAGE");
//		sbuffer.append(vpi).append("|||");
//		sbuffer.append(vci).append("LINKAGE");
		logger.warn("ims工单:" + sbuffer.toString());
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
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil
				.sendStrMesg(server, port, bssSheet + "\n");
		if (null == retResult || "".equals(retResult)) {
			return "";
		}
		return retResult;
	}

	public SimulateSheetNxNewDAO getDao() {
		return dao;
	}

	public void setDao(SimulateSheetNxNewDAO dao) {
		this.dao = dao;
	}

	public static Logger getLogger() {
		return logger;
	}
}
