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
import com.linkage.module.itms.service.dao.SimulateSheetDAO;

/**
 * 
 * @author zhangshimin(工号)
 * @version 1.0
 * @since 2011-5-21 下午02:42:44
 * @category com.linkage.module.itms.service.bio
 * @copyright 南京联创科技 网管科技部
 * 
 */
public class SimulateSheetNMGBIO {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(SimulateSheetNMGBIO.class);
	private SimulateSheetDAO dao;

	public String checkUsername(String username, String gw_type) {
		List<Map<String, String>> userList = dao.getUserInfo(username, gw_type);
		int size = userList.size();
		if (size == 1) {
			logger.warn("1#" + userList.get(0).get("orderType"));
			return "1#" + userList.get(0).get("orderType");
		} else if (size > 1) {
			return "0#请输入完整的逻辑SN";
		} else {
			return "-1#逻辑SN不存在，请先走建设流程";
		}
	}

	/**
	 * 生成资料接口开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param devType
	 * @param username
	 * @param cityId
	 * @param officeId
	 * @param zoneId
	 * @param orderType
	 * @param linkman
	 * @param linkphone
	 * @param linkEmail
	 * @param linkMobile
	 * @param homeAddr
	 * @param credNo
	 * @return
	 */
	public String sendOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String officeId, String zoneId, int orderType, String linkman,
			String linkphone, String linkEmail, String linkMobile,
			String homeAddr, String credNo, String customerId,
			String customerAccount, String customerPwd, String instAreaName) {
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
		
		if (Global.NMGDX.equals(instAreaName)) {
			sbuffer.append(transferDateFormate(customerId)).append("|||");
		} else {
			sbuffer.append(customerId).append("|||");
		}
		sbuffer.append(customerAccount).append("|||");

		if (Global.NMGDX.equals(instAreaName)) {
			sbuffer.append(customerPwd);
			sbuffer.append("LINKAGE");
		} else {
			sbuffer.append(customerPwd).append("|||");
			sbuffer.append("e8cp42").append("FROMWEB");
		}
		logger.warn("开户工单：" + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 全业务销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 *            LOID
	 * @param userAccount
	 *            业务帐号
	 * @param cityId
	 * @return
	 */
	public String sendStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String netUsername) {
		logger.debug("sendStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(netUsername).append("LINKAGE");
		logger.warn(sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 建设流程销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 *            LOID
	 * @param userAccount
	 *            业务帐号
	 * @param cityId
	 * @return
	 */
	public String sendStopSheetNew(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType,
			String instAreaName) {
		logger.debug("sendStopSheet()");
		// TODO 需要调整工单
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		if (Global.NMGDX.equals(instAreaName)) {
			sbuffer.append(cityId).append("LINKAGE");
		} else {
			sbuffer.append(cityId).append("FROMWEB");
		}
		logger.warn("sendStopSheetNew: " + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * iptv业务销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param userType
	 * @param iptvUsername
	 * @param iptvLanPort
	 * @return
	 */
	public String sendIptvStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String iptvUsername) {
		logger.debug("sendStopSheet()");
		// TODO 需要调整工单
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(iptvUsername).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 上网业务开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param netUsername
	 * @param netPassword
	 * @param vlanId
	 * @return
	 */
	public String sendNetOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String netUsername, String vlanId, String userType, String netway,
			String dns, String code, String ipaddress, String useriptype,
			String wlantype) {
		logger.warn("sendNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append("e8c").append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(vlanId).append("|||");
		sbuffer.append(useriptype).append("|||");
		sbuffer.append(wlantype).append("|||");
		sbuffer.append(ipaddress).append("|||");
		sbuffer.append(code).append("|||");
		sbuffer.append(netway).append("|||");
		sbuffer.append(dns).append("|||");
		sbuffer.append(userType).append("LINKAGE");
		logger.warn("上网:" + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * VOIP业务开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipUsername
	 * @param voipPasswd
	 * @param sipIp
	 * @param sipPort
	 * @param standSipIp
	 * @param standSipPort
	 * @param linePort
	 * @param voipTelepone
	 * @return
	 */
	public String sendVoipOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String voipUsername, String voipPasswd, String voipTelepone,
			String linePort) {
		logger.debug("sendNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append("e8c").append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(voipTelepone).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipUsername).append("|||");
		sbuffer.append(voipPasswd).append("|||");
		sbuffer.append(linePort).append("LINKAGE");
		logger.warn("VOIP业务(SIP):" + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * VOIP销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipTelepone
	 * @return
	 */
	public String sendVoipStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String voipTelepone) {
		logger.debug("sendVoipStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipTelepone).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * VOIP销户工单2
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipTelepone
	 * @return
	 */
	public String sendVoipStopSheet2(String servTypeId, String operateType,
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
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 新VOIP销户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipTelepone
	 * @return
	 */
	public String sendVoipStopSheetNew(String servTypeId, String operateType,
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
		sbuffer.append(voipTelepone).append("LINKAGE");
		logger.warn("sendVoipStopSheetNew: " + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * H248 VOIP 开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipUsername
	 * @param voipPasswd
	 * @param sipIp
	 * @param sipPort
	 * @param standSipIp
	 * @param standSipPort
	 * @param linePort
	 * @param voipTelepone
	 * @param devType
	 * @return
	 */
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
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * H248 VOIP 新开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipUsername
	 * @param voipPasswd
	 * @param sipIp
	 * @param sipPort
	 * @param standSipIp
	 * @param standSipPort
	 * @param linePort
	 * @param voipTelepone
	 * @param devType
	 * @param netType
	 * @param ipAddress
	 * @param mask
	 * @param gateway
	 * @param dsn
	 * @return
	 */
	public String sendH248VoipOpenSheetNew(String servTypeId,
			String operateType, String dealdate, String userType,
			String username, String voipTelepone, String cityId, String regId,
			String regIdType, String sipIp, int sipPort, String standSipIp,
			int standSipPort, String linePort, String netType,
			String ipAddress, String mask, String gateway, String dsn, String rTPPrefix, String ephemeralTermIDStart, String ephemeralTermIDAddLen) {
		logger.debug("sendH248VoipOpenSheetNew()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(voipTelepone).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(regId).append("|||");
		sbuffer.append(regIdType).append("|||");
		sbuffer.append(sipIp).append("|||");
		sbuffer.append(sipPort).append("|||");
		sbuffer.append(standSipIp).append("|||");
		sbuffer.append(standSipPort).append("|||");
		sbuffer.append(linePort).append("|||");
		sbuffer.append(netType).append("|||");
		sbuffer.append(ipAddress).append("|||");
		sbuffer.append(mask).append("|||");
		sbuffer.append(gateway).append("|||");
		sbuffer.append(dsn).append("|||");
		sbuffer.append(rTPPrefix).append("|||");
		sbuffer.append(ephemeralTermIDStart).append("|||");
		sbuffer.append(ephemeralTermIDAddLen).append("LINKAGE");
		logger.warn("sendH248VoipOpenSheetNew: " + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * IPTV开户工单
	 * 
	 * @param servTypeId
	 * @param operateType
	 * @param dealdate
	 * @param username
	 * @param cityId
	 * @param voipUsername
	 * @param iptvUsername
	 * @param servNum
	 * @param iptvLanPort
	 * @return
	 */
	public String sendIptvOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String iptvUsername, int servNum) {
		logger.debug("sendIptvOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append("E8c").append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(servNum).append("|||");
		sbuffer.append("").append("LINKAGE");
		logger.warn("iptv工单:" + sbuffer.toString());
		return this.sendSheet(sbuffer.toString());
	}

	public String sendE8bNetOpenSheet(String servTypeId, String operateType,
			String dealdate, String oui, String deviceSn, String devType,
			String netUsername, String netPassword, String adslPhone,
			String maxDownSpeed, String maxUpSpeed, String maxNetNum,
			String vlanid, String vpi, String vci, String cityId,
			String officeId, String zoneId, String linkman, String linkphone,
			String linkEmail, String linkMobile, String homeAddr,
			int orderType, String credNo) {
		logger.debug("sendE8bNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(oui).append("|||");
		sbuffer.append(deviceSn).append("|||");
		sbuffer.append(devType).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(netPassword).append("|||");

		sbuffer.append(adslPhone).append("|||");
		sbuffer.append(maxDownSpeed).append("|||");
		sbuffer.append(maxUpSpeed).append("|||");
		sbuffer.append(maxNetNum).append("|||");
		sbuffer.append(vlanid).append("|||");
		sbuffer.append(vpi).append("|||");
		sbuffer.append(vci).append("|||");

		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append("").append("|||");

		sbuffer.append(cityId).append("|||");
		sbuffer.append(officeId).append("|||");
		sbuffer.append(zoneId).append("|||");
		sbuffer.append(linkman).append("|||");
		sbuffer.append(linkphone).append("|||");

		sbuffer.append(linkEmail).append("|||");
		sbuffer.append(linkMobile).append("|||");
		sbuffer.append(homeAddr).append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append(orderType).append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append(credNo).append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendE8bNetStopSheet(String servTypeId, String operateType,
			String dealdate, String oui, String deviceSn, String netUsername,
			String cityId) {
		logger.debug("sendE8bNetStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(oui).append("|||");
		sbuffer.append(deviceSn).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(cityId).append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendE8bIptvOpenSheet(String servTypeId, String operateType,
			String dealdate, String oui, String deviceSn, String devType,
			String netUsername, String netPassword, String adslPhone,
			String maxDownSpeed, String maxUpSpeed, String vlanid, String vpi,
			String vci, String cityId, String officeId, String zoneId,
			String linkman, String linkphone, String linkEmail,
			String linkMobile, String homeAddr, int orderType, String credNo,
			String iptvUsername, int iptvNum) {
		logger.debug("sendE8bIptvOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(oui).append("|||");
		sbuffer.append(deviceSn).append("|||");
		sbuffer.append(devType).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(netPassword).append("|||");

		sbuffer.append(adslPhone).append("|||");
		sbuffer.append(maxDownSpeed).append("|||");
		sbuffer.append(maxUpSpeed).append("|||");
		sbuffer.append(vlanid).append("|||");
		sbuffer.append(vpi).append("|||");
		sbuffer.append(vci).append("|||");

		sbuffer.append(cityId).append("|||");
		sbuffer.append(officeId).append("|||");
		sbuffer.append(zoneId).append("|||");
		sbuffer.append(linkman).append("|||");
		sbuffer.append(linkphone).append("|||");

		sbuffer.append(linkEmail).append("|||");
		sbuffer.append(linkMobile).append("|||");
		sbuffer.append(homeAddr).append("|||");
		sbuffer.append("").append("|||");
		sbuffer.append(orderType).append("|||");
		sbuffer.append("").append("|||");

		sbuffer.append(credNo).append("|||");
		sbuffer.append(iptvNum).append("|||");
		sbuffer.append("").append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendE8bIptvStopSheet(String servTypeId, String operateType,
			String dealdate, String oui, String deviceSn, String netUsername,
			String iptvUsername, String cityId) {
		logger.debug("sendE8bIptvStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(oui).append("|||");
		sbuffer.append(deviceSn).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(cityId).append("FROMWEB");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendtianyiOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String netUsername, String vlanId) {
		logger.debug("sendtianyiOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append("e8c").append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(vlanId).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendRouterOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String iptvUsername, int servNum) {
		logger.debug("sendRouterOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append("50").append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(iptvUsername).append("|||");
		sbuffer.append(servNum).append("|||");
		sbuffer.append(username).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendtianyiStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String netUsername) {
		logger.debug("sendtianyiStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(netUsername).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	public String sendRouterStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String netUsername) {
		logger.debug("sendRouterStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append("50").append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(netUsername).append("LINKAGE");
		return this.sendSheet(sbuffer.toString());
	}

	/**
	 * 时间格式转换
	 * 
	 * @param dealdate
	 *            yyy-MM-dd HH:mm:ss
	 * @return yyyyMMddHHmmss
	 */
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

	/**
	 * 向工单接口发送模拟的BSS工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @param 模拟工单数据
	 * @author zhangsm
	 * @date 2011-05-21
	 * @return String 回单信息
	 */
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
		return retResult;
	}

	public void insertSheet(String id, String username, String serv_account,
			String city_id, int serv_type_id, int oper_type, int result_id,
			String result_desc, long oper_time, long occ_id,String occ_ip) {
		dao.insertSheet(id, username, serv_account, city_id, serv_type_id,
				oper_type, result_id, result_desc, oper_time, occ_id,occ_ip);
	}

	public int qyeryLoid(String loid) {
		return dao.qyeryLoid(loid);
	}

	public int checkIptvUser(String iptvUserName, String loid) {
		return dao.checkIptvUser(iptvUserName, loid);
	}

	public SimulateSheetDAO getDao() {
		return dao;
	}

	public void setDao(SimulateSheetDAO dao) {
		this.dao = dao;
	}

	public String sendVoipOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String voipUsername,
			String voipPasswd, String sipIp, int sipPort, String standSipIp,
			int standSipPort, String linePort, String voipTelepone, String devType,
			String registrarServer, int registrarServerPort, String standRegistrarServer,
			int standRegistrarServerPort, String outboundProxy, int outboundProxyPort,
			String standOutboundProxy, int standOutboundProxyPort, int protocol)
	{
		logger.debug("sendNetOpenSheet()");
	    StringBuffer sbuffer = new StringBuffer();
	    sbuffer.append(servTypeId).append("|||");
	    sbuffer.append(operateType).append("|||");
	    sbuffer.append(transferDateFormate(dealdate)).append("|||");
	    sbuffer.append(devType).append("|||");
	    sbuffer.append(username).append("|||");
	    sbuffer.append(voipTelepone).append("|||");
	    sbuffer.append(cityId).append("|||");

	    sbuffer.append(voipUsername).append("|||");
	    sbuffer.append(voipPasswd).append("|||");
	    sbuffer.append(sipIp).append("|||");
	    sbuffer.append(sipPort).append("|||");
	    sbuffer.append(standSipIp).append("|||");
	    sbuffer.append(standSipPort).append("|||");
	    sbuffer.append(linePort).append("|||");

	    sbuffer.append(registrarServer).append("|||");
	    sbuffer.append(registrarServerPort).append("|||");
	    sbuffer.append(standRegistrarServer).append("|||");
	    sbuffer.append(standRegistrarServerPort).append("|||");

	    sbuffer.append(outboundProxy).append("|||");
	    sbuffer.append(outboundProxyPort).append("|||");
	    sbuffer.append(standOutboundProxy).append("|||");
	    sbuffer.append(standOutboundProxyPort).append("|||");

	    sbuffer.append(protocol).append("FROMWEB");
	    return sendSheet(sbuffer.toString());
	}

}
