
package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.SimulateSheetNxNewBIO;

public class SimulateSheetNxNewACT implements SessionAware
{

	private static Logger logger = LoggerFactory.getLogger(SimulateSheetACT.class);
	// 业务类型
	private String servTypeId;
	// 操作类型
	private String operateType;
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
	// VPI
	private String vpi;
	// VCI
	private String vci;
	// 语音帐号
	private String voipUsername;
	// 语音密码
	private String voipPasswd;
	// 主SIP服务器地址
	private String sipIp;
	// 主SIP服务器端口
	private int sipPort;
	// 备SIP服务器地址
	private String standSipIp;
	// SIP备用服务器端口
	private int standSipPort;
	// 终端物理标识
	private String linePort;
	// 业务电话号码
	private String voipTelepone;
	// 终端标识
	private String regId;
	// IPTV个数
	protected int servNum;
	// 开通端口
	protected String iptvLanPort;
	// IPTV接入账号
	private String iptvUsername;
	// 客户ID
	private String customerId;
	// 客户帐号
	private String customerAccount;
	// 客户密码
	private String customerPwd;
	// 环境类型
	private String gw_type;
	// 业务电话号码
	private String bussinessTel;
	// VOIP认证帐号
	private String voipAccount;
	// VOIP认证密码
	private String voipPassword;
	//主ProxyServer
	private String ProxyServer;
	//主ProxyServerPort	
	private String ProxyServerPort;
	//备ProxyServer
	private String ProxyServer_a;	
	//备ProxyServerPort
	private String ProxyServerPort_a;		
	//语音端口
	private String voiceport;
	//主RegistrarServer
	private String RegistrarServer;
	//主RegistrarServerPort
	private String RegistrarServerPort;
	//备RegistrarServer
	private String RegistrarServer_a;
	//备RegistrarServerPort
	private String RegistrarServerPort_a;
	//主OutboundProxy
	private String OutboundProxy;
	//主OutboundProxyPort
	private String OutboundProxyPort;
	//备OutboundProxy
	private String OutboundProxy_a;
	//备OutboundProxyPort
	private String OutboundProxyPort_a;
	//协议类型
	private String agreementType;
	//IP类型
	private String ipType;
	//工单类型
	private String sheettype;

	// session
	private Map<String, Object> session;
	// ajax
	private String ajax;
	// bio
	private SimulateSheetNxNewBIO bio;
	// 地市
	private List<Map<String, String>> cityList = null;

	public String excute()
	{
		logger.debug("excute()");
		return "success";
	}

	/*
	 * 展示工单列表
	 */
	public String showSheet()
	{
		logger.debug("showSheet()");
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.cityId = curUser.getCityId();
		this.cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		this.dealdate = getNowDate();
		if (20 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				logger.warn("资料接口工单");
				return "open";
			}
			if ("3".equals(this.operateType))
			{
				logger.warn("全业务销户");
				return "closeall";
			}
			this.ajax = "未知操作类型";
			return "ajax";
		}
		if (22 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				logger.warn("上网业务开户工单");
				return "netopen";
			}
			if ("3".equals(this.operateType))
			{
				logger.warn("上网业务销户工单");
				return "netstop";
			}
			this.ajax = "未知操作类型";
			return "ajax";
		}
		if (15 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				logger.warn("VOIP业务开户工单");
				return "h248voipopen";
			}
			if ("3".equals(this.operateType))
			{
				logger.warn("VOIP业务销户工单");
				return "h248voipstop";
			}
			this.ajax = "未知操作类型";
			return "ajax";
		}
		if (14 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				logger.warn("SIP新装工单");
				return "sipopen";
			}
			if ("3".equals(this.operateType))
			{
				logger.warn("SIP销户工单");
				return "sipclose";
			}
			this.ajax = "未知操作类型";
			return "ajax";
		}
		if (21 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				logger.warn("IPTV业务开户工单");
				return "iptvopen";
			}
			if ("3".equals(this.operateType))
			{
				logger.warn("IPTV业务销户工单");
				return "iptvstop";
			}
			this.ajax = "未知操作类型";
			return "ajax";
		}
		this.ajax = "未知业务类型";
		return "ajax";
	}

	/*
	 * 发送工单
	 */
	public String sendSheet()
	{
		if (StringUtil.IsEmpty(this.userType))
		{
			this.userType = "1";
		}
		this.username = this.username.trim();
		if (20 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				this.ajax = this.bio.sendOpenSheet(this.servTypeId, this.operateType,
						this.dealdate, this.userType, this.username, this.cityId,
						this.officeId, this.zoneId, this.orderType, this.linkman,
						this.linkphone, this.linkEmail, this.linkMobile, this.homeAddr,
						this.credNo, this.customerId, this.customerAccount,
						this.customerPwd);
				logger.warn("返回值:" + this.ajax);
			}
			else if ("3".equals(this.operateType))
			{
				this.ajax = this.bio.sendStopSheet(this.servTypeId, this.operateType,
						this.dealdate, this.username, this.cityId, this.userType);
			}
			else
			{
				this.ajax = "1未知操作类型";
			}
		}
		else if (22 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				this.ajax = this.bio.sendNetOpenSheet(this.servTypeId, this.operateType,
						this.dealdate, this.username, this.cityId, this.netUsername,
						this.netPassword, vlanId, this.userType, wlantype, ipaddress,
						code, netway, dns, vpi, vci, ipType, this.sheettype);
			}
			else if ("3".equals(this.operateType))
			{
				this.ajax = this.bio.sendStopSheet(this.servTypeId, this.operateType,
						this.dealdate, this.username, this.cityId, this.userType,this.sheettype);
			}
			else
			{
				this.ajax = "1未知操作类型";
			}
		}
		else if (15 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				this.ajax = this.bio.sendH248VoipOpenSheet(this.servTypeId,
						this.operateType, this.dealdate, this.username, this.cityId,
						this.sipIp, this.sipPort, this.standSipIp, this.standSipPort,
						this.linePort, this.voipTelepone, this.userType, this.regId);
			}
			else if ("3".equals(this.operateType))
			{
				this.ajax = this.bio.sendVoipStopSheet(this.servTypeId, this.operateType,
						this.dealdate, this.username, this.cityId, this.voipTelepone,
						this.userType);
			}
			else
			{
				this.ajax = "1未知操作类型";
			}
		}
		else if (21 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				this.ajax = this.bio.sendIptvOpenSheet(this.servTypeId, this.operateType,
						this.dealdate, this.username, this.cityId, this.voipUsername,
						this.iptvUsername, this.servNum, this.iptvLanPort, this.userType);
			}
			else if ("3".equals(this.operateType))
			{
				this.ajax = this.bio.sendIptvStopSheet(this.servTypeId, this.operateType,
						this.dealdate, this.username, this.cityId, this.userType,
						this.iptvUsername, this.iptvLanPort);
			}
			else
			{
				this.ajax = "1未知操作类型";
			}
		}
		else if (14 == StringUtil.getIntegerValue(this.servTypeId))
		{
			if ("1".equals(this.operateType))
			{
				this.ajax = this.bio.sendSIPOpenSheet(servTypeId, operateType, dealdate,userType,username,bussinessTel,cityId,
						voipAccount,voipPassword,ProxyServer,ProxyServerPort,ProxyServer_a,ProxyServerPort_a,
						voiceport,RegistrarServer,RegistrarServerPort,RegistrarServer_a,RegistrarServerPort_a,
						OutboundProxy,OutboundProxyPort,OutboundProxy_a,OutboundProxyPort_a,agreementType,
						vlanId,wlantype,ipaddress,code,netway,dns,vpi,vci);
			}
			else if ("3".equals(this.operateType))
			{
				this.ajax = this.bio.sendSIPCloseSheet(servTypeId, operateType, dealdate,userType,username,
						cityId,voipAccount);
			}
			else
			{
				this.ajax = "1未知操作类型";
			}
		}
		else
		{
			this.ajax = "1未知业务类型";
		}
		if ("0".equals(this.ajax.substring(0, 1)))
		{
			this.ajax = ("成功 " + this.ajax);
		}
		else
		{
			this.ajax = ("失败 " + this.ajax);
		}
		logger.warn("ajax = " + this.ajax);
		return "sheetResult";
	}

	private String getNowDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public String checkUsername()
	{
		this.ajax = this.bio.checkUsername(this.username, this.gw_type);
		return "ajax";
	}

	public List<Map<String, String>> getCityList()
	{
		return this.cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public SimulateSheetNxNewBIO getBio()
	{
		return bio;
	}

	public void setBio(SimulateSheetNxNewBIO bio)
	{
		this.bio = bio;
	}

	public static Logger getLogger()
	{
		return logger;
	}

	public String getServTypeId()
	{
		return this.servTypeId;
	}

	public void setServTypeId(String servTypeId)
	{
		this.servTypeId = servTypeId;
	}

	public String getOperateType()
	{
		return this.operateType;
	}

	public void setOperateType(String operateType)
	{
		this.operateType = operateType;
	}

	public String getDealdate()
	{
		return this.dealdate;
	}

	public void setDealdate(String dealdate)
	{
		this.dealdate = dealdate;
	}

	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getCityId()
	{
		return this.cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getOfficeId()
	{
		return this.officeId;
	}

	public void setOfficeId(String officeId)
	{
		this.officeId = officeId;
	}

	public String getZoneId()
	{
		return this.zoneId;
	}

	public void setZoneId(String zoneId)
	{
		this.zoneId = zoneId;
	}

	public int getOrderType()
	{
		return this.orderType;
	}

	public void setOrderType(int orderType)
	{
		this.orderType = orderType;
	}

	public String getLinkman()
	{
		return this.linkman;
	}

	public void setLinkman(String linkman)
	{
		this.linkman = linkman;
	}

	public String getLinkphone()
	{
		return this.linkphone;
	}

	public void setLinkphone(String linkphone)
	{
		this.linkphone = linkphone;
	}

	public String getLinkEmail()
	{
		return this.linkEmail;
	}

	public void setLinkEmail(String linkEmail)
	{
		this.linkEmail = linkEmail;
	}

	public String getLinkMobile()
	{
		return this.linkMobile;
	}

	public void setLinkMobile(String linkMobile)
	{
		this.linkMobile = linkMobile;
	}

	public String getHomeAddr()
	{
		return this.homeAddr;
	}

	public void setHomeAddr(String homeAddr)
	{
		this.homeAddr = homeAddr;
	}

	public String getCredNo()
	{
		return this.credNo;
	}

	public void setCredNo(String credNo)
	{
		this.credNo = credNo;
	}

	public String getAjax()
	{
		return this.ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getNetUsername()
	{
		return this.netUsername;
	}

	public void setNetUsername(String netUsername)
	{
		this.netUsername = netUsername;
	}

	public String getNetPassword()
	{
		return this.netPassword;
	}

	public void setNetPassword(String netPassword)
	{
		this.netPassword = netPassword;
	}

	public String getVlanId()
	{
		return this.vlanId;
	}

	public void setVlanId(String vlanId)
	{
		this.vlanId = vlanId;
	}

	public String getVoipUsername()
	{
		return this.voipUsername;
	}

	public void setVoipUsername(String voipUsername)
	{
		this.voipUsername = voipUsername;
	}

	public String getVoipPasswd()
	{
		return this.voipPasswd;
	}

	public void setVoipPasswd(String voipPasswd)
	{
		this.voipPasswd = voipPasswd;
	}

	public String getSipIp()
	{
		return this.sipIp;
	}

	public void setSipIp(String sipIp)
	{
		this.sipIp = sipIp;
	}

	public int getSipPort()
	{
		return this.sipPort;
	}

	public void setSipPort(int sipPort)
	{
		this.sipPort = sipPort;
	}

	public String getStandSipIp()
	{
		return this.standSipIp;
	}

	public void setStandSipIp(String standSipIp)
	{
		this.standSipIp = standSipIp;
	}

	public int getStandSipPort()
	{
		return this.standSipPort;
	}

	public void setStandSipPort(int standSipPort)
	{
		this.standSipPort = standSipPort;
	}

	public String getLinePort()
	{
		return this.linePort;
	}

	public void setLinePort(String linePort)
	{
		this.linePort = linePort;
	}

	public String getVoipTelepone()
	{
		return this.voipTelepone;
	}

	public void setVoipTelepone(String voipTelepone)
	{
		this.voipTelepone = voipTelepone;
	}

	public int getServNum()
	{
		return this.servNum;
	}

	public void setServNum(int servNum)
	{
		this.servNum = servNum;
	}

	public String getIptvLanPort()
	{
		return this.iptvLanPort;
	}

	public void setIptvLanPort(String iptvLanPort)
	{
		this.iptvLanPort = iptvLanPort;
	}

	public String getIptvUsername()
	{
		return this.iptvUsername;
	}

	public void setIptvUsername(String iptvUsername)
	{
		this.iptvUsername = iptvUsername;
	}

	public String getVpi()
	{
		return this.vpi;
	}

	public void setVpi(String vpi)
	{
		this.vpi = vpi;
	}

	public String getVci()
	{
		return this.vci;
	}

	public void setVci(String vci)
	{
		this.vci = vci;
	}

	public String getRegId()
	{
		return this.regId;
	}

	public void setRegId(String regId)
	{
		this.regId = regId;
	}

	public String getUserType()
	{
		return this.userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getCustomerId()
	{
		return this.customerId;
	}

	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}

	public String getCustomerAccount()
	{
		return this.customerAccount;
	}

	public void setCustomerAccount(String customerAccount)
	{
		this.customerAccount = customerAccount;
	}

	public String getCustomerPwd()
	{
		return this.customerPwd;
	}

	public void setCustomerPwd(String customerPwd)
	{
		this.customerPwd = customerPwd;
	}

	public String getGw_type()
	{
		return this.gw_type;
	}

	public void setGw_type(String gwType)
	{
		this.gw_type = gwType;
	}

	public String getWlantype()
	{
		return wlantype;
	}

	public void setWlantype(String wlantype)
	{
		this.wlantype = wlantype;
	}

	public String getNetway()
	{
		return netway;
	}

	public void setNetway(String netway)
	{
		this.netway = netway;
	}

	public String getDns()
	{
		return dns;
	}

	public void setDns(String dns)
	{
		this.dns = dns;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getIpaddress()
	{
		return ipaddress;
	}

	public void setIpaddress(String ipaddress)
	{
		this.ipaddress = ipaddress;
	}

	public Map<String, Object> getSession()
	{
		return session;
	}

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	
	public String getBussinessTel()
	{
		return bussinessTel;
	}

	
	public void setBussinessTel(String bussinessTel)
	{
		this.bussinessTel = bussinessTel;
	}

	
	public String getVoipAccount()
	{
		return voipAccount;
	}

	
	public void setVoipAccount(String voipAccount)
	{
		this.voipAccount = voipAccount;
	}

	
	public String getVoipPassword()
	{
		return voipPassword;
	}

	
	public void setVoipPassword(String voipPassword)
	{
		this.voipPassword = voipPassword;
	}

	
	public String getProxyServer()
	{
		return ProxyServer;
	}

	
	public void setProxyServer(String proxyServer)
	{
		ProxyServer = proxyServer;
	}

	
	public String getProxyServerPort()
	{
		return ProxyServerPort;
	}

	
	public void setProxyServerPort(String proxyServerPort)
	{
		ProxyServerPort = proxyServerPort;
	}

	
	public String getProxyServer_a()
	{
		return ProxyServer_a;
	}

	
	public void setProxyServer_a(String proxyServer_a)
	{
		ProxyServer_a = proxyServer_a;
	}

	
	public String getProxyServerPort_a()
	{
		return ProxyServerPort_a;
	}

	
	public void setProxyServerPort_a(String proxyServerPort_a)
	{
		ProxyServerPort_a = proxyServerPort_a;
	}

	
	public String getVoiceport()
	{
		return voiceport;
	}

	
	public void setVoiceport(String voiceport)
	{
		this.voiceport = voiceport;
	}

	
	public String getRegistrarServer()
	{
		return RegistrarServer;
	}

	
	public void setRegistrarServer(String registrarServer)
	{
		RegistrarServer = registrarServer;
	}

	
	public String getRegistrarServerPort()
	{
		return RegistrarServerPort;
	}

	
	public void setRegistrarServerPort(String registrarServerPort)
	{
		RegistrarServerPort = registrarServerPort;
	}

	
	public String getRegistrarServer_a()
	{
		return RegistrarServer_a;
	}

	
	public void setRegistrarServer_a(String registrarServer_a)
	{
		RegistrarServer_a = registrarServer_a;
	}

	
	public String getRegistrarServerPort_a()
	{
		return RegistrarServerPort_a;
	}

	
	public void setRegistrarServerPort_a(String registrarServerPort_a)
	{
		RegistrarServerPort_a = registrarServerPort_a;
	}

	
	public String getOutboundProxy()
	{
		return OutboundProxy;
	}

	
	public void setOutboundProxy(String outboundProxy)
	{
		OutboundProxy = outboundProxy;
	}

	
	public String getOutboundProxyPort()
	{
		return OutboundProxyPort;
	}

	
	public void setOutboundProxyPort(String outboundProxyPort)
	{
		OutboundProxyPort = outboundProxyPort;
	}

	
	public String getOutboundProxy_a()
	{
		return OutboundProxy_a;
	}

	
	public void setOutboundProxy_a(String outboundProxy_a)
	{
		OutboundProxy_a = outboundProxy_a;
	}

	
	public String getOutboundProxyPort_a()
	{
		return OutboundProxyPort_a;
	}

	
	public void setOutboundProxyPort_a(String outboundProxyPort_a)
	{
		OutboundProxyPort_a = outboundProxyPort_a;
	}
	
	public String getAgreementType()
	{
		return agreementType;
	}
	
	public void setAgreementType(String agreementType)
	{
		this.agreementType = agreementType;
	}
	
	public String getIpType() {
		return ipType;
	}

	public void setIpType(String ipType) {
		this.ipType = ipType;
	}
	
	public String getSheettype()
	{
		return sheettype;
	}

	public void setSheettype(String sheettype)
	{
		this.sheettype = sheettype;
	}
	
}
