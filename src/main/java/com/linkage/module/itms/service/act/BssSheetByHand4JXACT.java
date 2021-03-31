package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.service.bio.BssSheetByHand4JXBIO;

public class BssSheetByHand4JXACT implements SessionAware {

	public static Logger logger = LoggerFactory
			.getLogger(BssSheetByHand4JXACT.class);

	/** 政企网关，或家庭网关 */
	private String gwType = "1";
	private String userInfo = "";
	private String netInfo = "";
	private String itvInfo = "";
	private String voipInfo = "";
	private String loid = "";

	private String ajax = null;
	@SuppressWarnings("rawtypes")
	private Map session;

	private List<Map<String, String>> cityList = null;
	private List<Map<String, String>> deviceType = null;

	private BssSheetByHand4JXBIO bio;
	private String servTypeId;
	private String operateType;
	private String dealdate;
	private String devType;
	private String userType;
	private String username;
	private String cityId;
	private String officeId = "";
	private String zoneId = "";
	private int orderType;
	private String linkman;
	private String linkphone;
	private String linkEmail;
	private String linkMobile;
	private String homeAddr;
	private String credNo;
	private String netUsername;
	private String netPassword;
	private String vlanId;
	private String vpi;
	private String vci;
	private String voipUsername;
	private String voipPasswd;
	private String sipIp;
	private int sipPort;
	private String standSipIp;
	private int standSipPort;
	private String registrarServer;
	private int registrarServerPort;
	private String standRegistrarServer;
	private int standRegistrarServerPort;
	private String outboundProxy;
	private int outboundProxyPort;
	private String standOutboundProxy;
	private int standOutboundProxyPort;
	private int protocol;
	private String maxDownSpeed;
	private String maxUpSpeed;
	private String maxNetNum;
	private String oui;
	private String deviceSn;
	private String adslPhone;
	private int iptvNum;
	private String linePort;
	private String voipTelepone;
	private String regId;
	protected int servNum;
	protected String iptvLanPort;
	private String iptvUsername;
	private String customerId;
	private String customerAccount;
	private String customerPwd;
	private String gw_type;
	private String voiceNumList="";
	

	public String execute() {
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		deviceType = bio.getDeviceType(gwType);
		if(null!=deviceType && deviceType.size()>0){
			for(Map<String, String> map : deviceType){
				voiceNumList += (StringUtil.getStringValue(map, "spec_name", "") + "##" +
						StringUtil.getStringValue(map, "voice_num", "")) + "####";
			}
			if(!StringUtil.IsEmpty(voiceNumList) && voiceNumList.endsWith("####")){
				voiceNumList = voiceNumList.substring(0,voiceNumList.length()-4);
			}
		}
		
		return "success";
	}

	public String checkLoid() {

		logger.debug("checkLoid()");

		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());

		ajax = bio.checkLoid(loid, gwType);
		logger.warn("ajxa======>"+ajax);
		return "ajax";
	}

	public String doBusiness() {
		UserRes curUser = (UserRes) session.get("curUser");
		String account = curUser.getUser().getAccount();
		logger.debug("BssSheetByHandACT==>doBusiness()");
		ajax = bio.doBusiness(userInfo, netInfo, itvInfo, voipInfo, gwType,account);
		
		return "ajax";
	}

	/*
	 * 工单列表显示
	 */
	public String showSheet() {
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.cityId = curUser.getCityId();
		this.cityList = CityDAO
				.getAllNextCityListByCityPid(curUser.getCityId());
		this.dealdate = getNowDate();
		if (20 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				return "closeall";
			} else {
				this.ajax = "未知操作类型";
				return "ajax";
			}
		} else if (22 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				return "netstop";
			} else {
				this.ajax = "未知操作类型";
				return "ajax";
			}
		} else if (14 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				return "voipstop";
			} else {
				this.ajax = "未知操作类型";
				return "ajax";
			}
		} else if (15 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				return "h248voipstop";
			} else {
				this.ajax = "未知操作类型";
				return "ajax";
			}
		} else if (21 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				return "iptvstop";
			} else {
				this.ajax = "未知操作类型";
				return "ajax";
			}
		} else {
			this.ajax = "未知操作类型";
			return "ajax";
		}
	}

	/*
	 * 发送工单
	 */
	public String sendSheet() {
		if (StringUtil.IsEmpty(this.userType)) {
			this.userType = "1";
		}
		if (20 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				this.ajax = this.bio.sendStopSheet(this.servTypeId,
						this.operateType, this.dealdate, this.username,
						this.cityId, this.userType);
			} else {
				this.ajax = "1未知操作类型";
			}
		} else if (22 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				this.ajax = this.bio.sendNetStopSheet(this.servTypeId,
						this.operateType, this.dealdate, this.username,
						this.cityId, this.userType, this.netUsername);
			} else {
				this.ajax = "1未知操作类型";
			}
		} else if (14 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				this.ajax = this.bio.sendVoipStopSheet(this.servTypeId,
						this.operateType, this.dealdate, this.username,
						this.cityId, this.voipUsername, this.userType);
			} else {
				this.ajax = "1未知操作类型";
			}
		} else if (15 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				this.ajax = this.bio.sendVoipStopSheet(this.servTypeId,
						this.operateType, this.dealdate, this.username,
						this.cityId, this.voipTelepone, this.userType);
			} else {
				this.ajax = "1未知操作类型";
			}
		} else if (21 == StringUtil.getIntegerValue(this.servTypeId)) {
			if ("3".equals(this.operateType)) {
				this.ajax = this.bio.sendIptvStopSheet(this.servTypeId,
						this.operateType, this.dealdate, this.username,
						this.cityId, this.userType, this.iptvUsername,
						this.iptvLanPort);
			} else {
				this.ajax = "1未知操作类型";
			}
		} else {
			this.ajax = "1未知业务类型";
		}
		if ("0".equals(ajax.substring(13, 14))) {
			this.ajax = ("成功 " + ajax.substring(13, ajax.length() - 3));
		} else {
			this.ajax = ("失败 " + ajax.substring(13, ajax.length() - 3));
		}
		return "sheetResult";
	}

	public String checkUsername() {
		this.ajax = this.bio.checkUsername(this.username, this.gw_type);
		return "ajax";
	}

	private String getNowDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getNetInfo() {
		return netInfo;
	}

	public void setNetInfo(String netInfo) {
		this.netInfo = netInfo;
	}

	public String getItvInfo() {
		return itvInfo;
	}

	public void setItvInfo(String itvInfo) {
		this.itvInfo = itvInfo;
	}

	public String getVoipInfo() {
		return voipInfo;
	}

	public void setVoipInfo(String voipInfo) {
		this.voipInfo = voipInfo;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	@SuppressWarnings("rawtypes")
	public Map getSession() {
		return session;
	}

	@SuppressWarnings("rawtypes")
	public void setSession(Map session) {
		this.session = session;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, String>> getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(List<Map<String, String>> deviceType) {
		this.deviceType = deviceType;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public BssSheetByHand4JXBIO getBio() {
		return bio;
	}

	public void setBio(BssSheetByHand4JXBIO bio) {
		this.bio = bio;
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

	public String getDealdate() {
		return dealdate;
	}

	public void setDealdate(String dealdate) {
		this.dealdate = dealdate;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
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

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
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

	public String getNetUsername() {
		return netUsername;
	}

	public void setNetUsername(String netUsername) {
		this.netUsername = netUsername;
	}

	public String getNetPassword() {
		return netPassword;
	}

	public void setNetPassword(String netPassword) {
		this.netPassword = netPassword;
	}

	public String getVlanId() {
		return vlanId;
	}

	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}

	public String getVpi() {
		return vpi;
	}

	public void setVpi(String vpi) {
		this.vpi = vpi;
	}

	public String getVci() {
		return vci;
	}

	public void setVci(String vci) {
		this.vci = vci;
	}

	public String getVoipUsername() {
		return voipUsername;
	}

	public void setVoipUsername(String voipUsername) {
		this.voipUsername = voipUsername;
	}

	public String getVoipPasswd() {
		return voipPasswd;
	}

	public void setVoipPasswd(String voipPasswd) {
		this.voipPasswd = voipPasswd;
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

	public String getRegistrarServer() {
		return registrarServer;
	}

	public void setRegistrarServer(String registrarServer) {
		this.registrarServer = registrarServer;
	}

	public int getRegistrarServerPort() {
		return registrarServerPort;
	}

	public void setRegistrarServerPort(int registrarServerPort) {
		this.registrarServerPort = registrarServerPort;
	}

	public String getStandRegistrarServer() {
		return standRegistrarServer;
	}

	public void setStandRegistrarServer(String standRegistrarServer) {
		this.standRegistrarServer = standRegistrarServer;
	}

	public int getStandRegistrarServerPort() {
		return standRegistrarServerPort;
	}

	public void setStandRegistrarServerPort(int standRegistrarServerPort) {
		this.standRegistrarServerPort = standRegistrarServerPort;
	}

	public String getOutboundProxy() {
		return outboundProxy;
	}

	public void setOutboundProxy(String outboundProxy) {
		this.outboundProxy = outboundProxy;
	}

	public int getOutboundProxyPort() {
		return outboundProxyPort;
	}

	public void setOutboundProxyPort(int outboundProxyPort) {
		this.outboundProxyPort = outboundProxyPort;
	}

	public String getStandOutboundProxy() {
		return standOutboundProxy;
	}

	public void setStandOutboundProxy(String standOutboundProxy) {
		this.standOutboundProxy = standOutboundProxy;
	}

	public int getStandOutboundProxyPort() {
		return standOutboundProxyPort;
	}

	public void setStandOutboundProxyPort(int standOutboundProxyPort) {
		this.standOutboundProxyPort = standOutboundProxyPort;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public String getMaxDownSpeed() {
		return maxDownSpeed;
	}

	public void setMaxDownSpeed(String maxDownSpeed) {
		this.maxDownSpeed = maxDownSpeed;
	}

	public String getMaxUpSpeed() {
		return maxUpSpeed;
	}

	public void setMaxUpSpeed(String maxUpSpeed) {
		this.maxUpSpeed = maxUpSpeed;
	}

	public String getMaxNetNum() {
		return maxNetNum;
	}

	public void setMaxNetNum(String maxNetNum) {
		this.maxNetNum = maxNetNum;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getAdslPhone() {
		return adslPhone;
	}

	public void setAdslPhone(String adslPhone) {
		this.adslPhone = adslPhone;
	}

	public int getIptvNum() {
		return iptvNum;
	}

	public void setIptvNum(int iptvNum) {
		this.iptvNum = iptvNum;
	}

	public String getLinePort() {
		return linePort;
	}

	public void setLinePort(String linePort) {
		this.linePort = linePort;
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

	public int getServNum() {
		return servNum;
	}

	public void setServNum(int servNum) {
		this.servNum = servNum;
	}

	public String getIptvLanPort() {
		return iptvLanPort;
	}

	public void setIptvLanPort(String iptvLanPort) {
		this.iptvLanPort = iptvLanPort;
	}

	public String getIptvUsername() {
		return iptvUsername;
	}

	public void setIptvUsername(String iptvUsername) {
		this.iptvUsername = iptvUsername;
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

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getVoiceNumList() {
		return voiceNumList;
	}

	public void setVoiceNumList(String voiceNumList) {
		this.voiceNumList = voiceNumList;
	}
	

}