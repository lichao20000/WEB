package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.SimulateSheetHLJBIO;

public class SimulateSheetHLJACT implements SessionAware {

	// Session
	private Map<String, Object> session;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(SimulateSheetACT.class);
	// 地区标识
	private String instAreaName;
	// BIO
	private SimulateSheetHLJBIO bio;
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	// 业务类型
	private String servTypeId;
	// 操作类型
	private String operateType;
	// AJAX
	private String ajax;
	// 工单受理时间
	private String dealdate;
	// 用户类型
	private String userType;
	// 逻辑SN
	private String username;
	// 属地
	private String cityId;
	// 局向
	private String officeId = "";
	// 小区
	private String zoneId = "";
	// 接入方式 (订单类型)
	private int orderType;
	// 联系人
	private String linkman;
	// 联系人电话
	private String linkphone;
	// 联系人Email
	private String linkEmail;
	// 联系人手机
	private String linkMobile;
	// 家庭住址
	private String homeAddr;
	// 证件号码
	private String credNo;
	// 客户ID
	private String customerId;
	// 客户帐号
	private String customerAccount;
	// 客户密码
	private String customerPwd;
	// 宽带账号
	private String netUsername;
	// 宽带密码
	private String netPassword;
	// vlanId LAN和PON上行时必须
	private String vlanId;
	// 上网方式
	private String wlantype;
	// 网关
	private String netway;
	// DNS
	private String dns;
	// 掩码
	private String code;
	// IP地址
	private String ipaddress;
	// 用户IP类型
	private String useriptype;
	// 业务电话号码
	private String voipTelepone;
	// h248 终端标识
	private String regId;
	// 终端标识类型
	private String regIdType;
	// SIP服务器地址
	private String sipIp;
	// SIP服务器端口
	private int sipPort;
	// SIP备用服务器地址
	private String standSipIp;
	// SIP备用服务器端口
	private int standSipPort;
	// 终端开通的线路端口：V1, V2
	private String linePort;

	/*
	 * 初始化页面
	 */
	public String init() {
		logger.debug("excute()");
		return "success";
	}

	/*
	 * 显示模拟工单
	 */
	public String showSheet() {
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		dealdate = getNowDate();
		// 终端资料接口
		if (20 == StringUtil.getIntegerValue(servTypeId)) {
			if ("1".equals(operateType)) {
				logger.warn("资料接口工单");
				return "open";
			} else if ("3".equals(operateType)) {
				logger.warn("全业务销户");
				return "closeall";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		} else if (22 == StringUtil.getIntegerValue(servTypeId)) { // 上网业务
			if ("1".equals(operateType)) {
				logger.warn("上网业务开户工单");
				return "netopen";
			} else if ("3".equals(operateType)) {
				logger.warn("上网业务销户工单");
				return "netstop";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		} else if (15 == StringUtil.getIntegerValue(servTypeId)) { // H248 VOIP
			if ("1".equals(operateType)) {
				logger.warn("VOIP业务开户工单");
				return "h248voipopen";
			} else if ("3".equals(operateType)) {
				logger.warn("VOIP业务销户工单");
				return "h248voipstop";
			} else {
				ajax = "未知操作类型";
				return "ajax";
			}
		} else {
			ajax = "未知业务类型";
			return "ajax";
		}
	}

	/*
	 * 发送模拟工单
	 */
	public String sendSheet() {
		if (StringUtil.IsEmpty(userType)) {
			userType = "1";
		}
		logger.debug("servTypeId = " + servTypeId);
		// 终端资料接口工单
		if (20 == StringUtil.getIntegerValue(servTypeId)) {
			logger.warn("operateType = " + operateType);
			if ("1".equals(operateType)) {
				ajax = bio.sendOpenSheet(servTypeId, operateType, dealdate,
						userType, username, cityId, officeId, zoneId,
						orderType, linkman, linkphone, linkEmail, linkMobile,
						homeAddr, credNo, customerId, customerAccount,
						customerPwd);
				logger.warn("返回值:" + ajax);
			} else if ("3".equals(operateType)) {
				ajax = bio.sendStopSheet(servTypeId, operateType, dealdate,
						userType, username, cityId);
			} else {
				ajax = "1未知操作类型";
			}
		} else if (22 == StringUtil.getIntegerValue(servTypeId)) { // 上网业务
			if ("1".equals(operateType)) { // 开户
				ajax = bio.sendNetOpenSheet(servTypeId, operateType, dealdate,
						userType, username, netUsername, netPassword, cityId,
						vlanId, wlantype, ipaddress, code, netway, dns,
						useriptype);

			} else if ("3".equals(operateType)) { // 销户
				ajax = bio.sendStopSheet(servTypeId, operateType, dealdate,
						userType, username, cityId);
			} else {
				ajax = "1未知操作类型";
			}
		} else if (15 == StringUtil.getIntegerValue(servTypeId)) { // VOIPh248业务
			if ("1".equals(operateType)) { // 开户
				ajax = bio.sendH248VoipOpenSheet(servTypeId, operateType,
						dealdate, userType, username, cityId, regId, regIdType,
						sipIp, sipPort, standSipIp, standSipPort, linePort,
						voipTelepone);
			} else if ("3".equals(operateType)) { // 销户
				ajax = bio.sendH248VoipStopSheet(servTypeId, operateType,
						dealdate, userType, username, cityId, voipTelepone);
			} else {
				ajax = "1未知操作类型";
			}
		}
		if ("0".equals(ajax.substring(0, 1))) {
			ajax = "成功|||" + ajax;
		} else {
			ajax = "失败|||" + ajax;
		}
		if ("22".equals(servTypeId) || "15".equals(servTypeId)) {
			int serv_type_id = 0;
			String serv_account = "";
			String[] results = ajax.split("\\|\\|\\|");
			if ("22".equals(servTypeId)) {
				serv_type_id = 10;
				serv_account = netUsername;
			}
			logger.debug("servTypeId" + servTypeId);
			if ("15".equals(servTypeId)) {
				serv_type_id = 14;
				serv_account = voipTelepone;
			}
			UserRes curUser = (UserRes) session.get("curUser");
			User user = curUser.getUser();
			String id = user.getId()
					+ StringUtil
							.getStringValue(Math.round(Math.random() * 1000000000000L));
			String city_id = curUser.getCityId();
			int oper_type = Integer.valueOf(operateType);
			long oper_time = new Date().getTime() / 1000;
			long occ_id = user.getId();
			String occ_ip = WebUtil.getCurrentUserIP();
			bio.insertSheet(id, username, serv_account, city_id, serv_type_id,
					oper_type, Integer.valueOf(results[1]), results[0],
					oper_time, occ_id, occ_ip);
		}

		return "sheetResult";
	}

	// 当前时间的
	private String getNowDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getInstAreaName() {
		return instAreaName;
	}

	public void setInstAreaName(String instAreaName) {
		this.instAreaName = instAreaName;
	}

	public static Logger getLogger() {
		return logger;
	}

	public SimulateSheetHLJBIO getBio() {
		return bio;
	}

	public void setBio(SimulateSheetHLJBIO bio) {
		this.bio = bio;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public String getDealdate() {
		return dealdate;
	}

	public void setDealdate(String dealdate) {
		this.dealdate = dealdate;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkphone() {
		return linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	public String getLinkEmail() {
		return linkEmail;
	}

	public void setLinkEmail(String linkEmail) {
		this.linkEmail = linkEmail;
	}

	public String getLinkMobile() {
		return linkMobile;
	}

	public void setLinkMobile(String linkMobile) {
		this.linkMobile = linkMobile;
	}

	public String getHomeAddr() {
		return homeAddr;
	}

	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
	}

	public String getCredNo() {
		return credNo;
	}

	public void setCredNo(String credNo) {
		this.credNo = credNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getCustomerPwd() {
		return customerPwd;
	}

	public void setCustomerPwd(String customerPwd) {
		this.customerPwd = customerPwd;
	}

	public String getNetUsername() {
		return netUsername;
	}

	public void setNetUsername(String netUsername) {
		this.netUsername = netUsername;
	}

	public String getVlanId() {
		return vlanId;
	}

	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}

	public String getWlantype() {
		return wlantype;
	}

	public void setWlantype(String wlantype) {
		this.wlantype = wlantype;
	}

	public String getNetway() {
		return netway;
	}

	public void setNetway(String netway) {
		this.netway = netway;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getUseriptype() {
		return useriptype;
	}

	public void setUseriptype(String useriptype) {
		this.useriptype = useriptype;
	}

	public String getVoipTelepone() {
		return voipTelepone;
	}

	public void setVoipTelepone(String voipTelepone) {
		this.voipTelepone = voipTelepone;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getRegIdType() {
		return regIdType;
	}

	public void setRegIdType(String regIdType) {
		this.regIdType = regIdType;
	}

	public String getSipIp() {
		return sipIp;
	}

	public void setSipIp(String sipIp) {
		this.sipIp = sipIp;
	}

	public int getSipPort() {
		return sipPort;
	}

	public void setSipPort(int sipPort) {
		this.sipPort = sipPort;
	}

	public String getStandSipIp() {
		return standSipIp;
	}

	public void setStandSipIp(String standSipIp) {
		this.standSipIp = standSipIp;
	}

	public int getStandSipPort() {
		return standSipPort;
	}

	public void setStandSipPort(int standSipPort) {
		this.standSipPort = standSipPort;
	}

	public String getLinePort() {
		return linePort;
	}

	public void setLinePort(String linePort) {
		this.linePort = linePort;
	}

	public String getNetPassword() {
		return netPassword;
	}

	public void setNetPassword(String netPassword) {
		this.netPassword = netPassword;
	}
}
