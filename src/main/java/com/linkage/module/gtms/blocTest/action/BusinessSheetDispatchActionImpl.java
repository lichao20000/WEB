package com.linkage.module.gtms.blocTest.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.blocTest.obj.VoipSheetOBJ;
import com.linkage.module.gtms.blocTest.serv.BusinessSheetDispatchServ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * IPv6 业务工单下发
 * 
 * @author bell
 * @date 2012年5月15日 17:28:01
 */
public class BusinessSheetDispatchActionImpl extends ActionSupport implements
		ServletRequestAware {

	/** sid */
	private static final long serialVersionUID = -7948213365992547983L;

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(BusinessSheetDispatchActionImpl.class);

	/** ipv6iptv业务类型 */
	public final static String IPV6IPTV = "16";
	/** 上网业务类型 */
	public final static String IPV6NET = "17";
	/** voip业务类型 */
	public final static String IPV6VOIP = "18";
	/** device_id */
	private String deviceId;
	/** 工单业务类型 上网:1 或IPTV:2或VOIP:3 */
	private String sheetType;
	/** 上行方式（接入方式）LAN上行或ADSL上行或者是PON上行 */
	private String accessType;
	/** 连接方式 IP方式或者PPP拨号 */
	private String sessionType;
	/** 连接类型 桥接或路由 */
	private String connType;
	private String vpi;
	private String vci;
	/**地址类型*/
	private String ipType;
	private String vlanId;

	/** 多终端上网数 */
	private String num;

	private String username;
	private String password;

	private String ip;
	private String gateway;
	/** 子网掩码 */
	private String mask;
	private String dns;
	/** 绑定端口 */
	private String bindPort;

	private String wanType;

	/**IPV6  start*/
	/**地址分配机制*/
	private String addressOrigin ;
	/**Address Family Translation Router*/
	private String aftr  ;
	/**IP地址*/
	private String ipv6AddressIp ;
	/**DNS*/
	private String ipv6AddressDNS ;
	/**地址前缀*/
	private String ipv6AddressPrefix;
	/**IPV6  end*/
	/** VOIP start **********/
	/** voip认证帐号 */
	private String voipUsername;
	/** voip认证密码 */
	private String voipPasswd;
	/** SIP服务器地址 */
	private String sipIp;
	/** SIP服务器端口 */
	private int sipPort;
	/** SIP备用服务器地址 */
	private String standSipIp;
	/** SIP备用服务器端口 */
	private int standSipPort;
	/** ims */
	private String registrarServer;

	private int registrarServerPort;

	private String standRegistrarServer;

	private int standRegistrarServerPort;

	private String outboundProxy;

	private int outboundProxyPort;

	private String standOutboundProxy;

	private int standOutboundProxyPort;
	/** 终端开通的线路端口：V1, V2 */
	private String linePort;
	/** 业务电话号码 */
	private String voipTelepone;
	/** 协议 */
	private int protocol;
	/*********** voip end ***************/
	/** 操作类型 */
	private String operTypeId = "1";
	/** 系统类型 ,默认ITMS*/
	private String gw_type ="1";
	/** 设备序列号 */
	private String deviceSn;
	/** 用户帐号 */
	private String userAccount;
	/** serv */
	private BusinessSheetDispatchServ businessSheetDispatchServ;
	/** ajax*/
	private String ajax ="";
	/** 查询时报的错 */
	private String caseErr = "";
	/**map*/
    private Map devMap =null;
	/** request */
	private HttpServletRequest request;

	
	
	public String init() throws Exception {
		logger.debug("init()");
		return SUCCESS;
	}
	/**
	 * 查询设备的一些基本信息
	 * @return
	 */
	public String getBaseInfo(){
		
		logger.debug("getBaseInfo :序列号：{}，用户账号：{}", deviceSn, userAccount);
		
		if ((null == deviceSn && null == userAccount) || ("".equals(deviceSn.trim()) && "".equals(userAccount.trim()))) {
		  	logger.warn("查询时序列号和用户账号都为NULL，出错返回");
		  	ajax = "1";
 		} else {
			
 			devMap = businessSheetDispatchServ.getBaseInfo(deviceSn, userAccount);
			
			if (null == devMap) {
				ajax = "2";
				
			} else if(StringUtil.getIntegerValue(devMap.get("deviceNum")) > 1) {
				ajax = "3";
			} else {
				ajax = StringUtil.getStringValue(devMap.get("deviceId"));
			}
		}
		
		return "ajax";
	}
	/**
	 * 手工工单业务下发
	 * 
	 * @date 2012年5月15日 
	 */
	public String add() {
		logger.debug("add()");
		HgwServUserObj hgwServUserObj = new HgwServUserObj();
		
		wanType = StringUtil.getStringValue(getWanType(sessionType,connType,ipType));
		String userId = businessSheetDispatchServ.getUserId(deviceId);
		hgwServUserObj.setBindPort(bindPort);
		hgwServUserObj.setDns(dns);
		hgwServUserObj.setGateway(gateway);
		hgwServUserObj.setIpAddress(ip);
		hgwServUserObj.setIpMask(mask);
		hgwServUserObj.setServTypeId(sheetType);
		hgwServUserObj.setUserId(userId); 
		hgwServUserObj.setVciid(vci);
		hgwServUserObj.setVpiid(vpi);
		hgwServUserObj.setVlanid(vlanId);
		hgwServUserObj.setWanType(wanType);
		
		hgwServUserObj.setAddressOrigin(addressOrigin);
		hgwServUserObj.setAftr(aftr);
		hgwServUserObj.setIpv6AddressIp(ipv6AddressIp);
		hgwServUserObj.setIpv6AddressDNS(ipv6AddressDNS);
		hgwServUserObj.setIpv6AddressPrefix(ipv6AddressPrefix);
		
		hgwServUserObj.setPasswd(password);
		hgwServUserObj.setUsername(username);
		logger.warn("前台传来数据 "+hgwServUserObj.toString());
		logger.warn("^^^*****"+hgwServUserObj.toString());
		// VOIP 工单
		VoipSheetOBJ voipSheetOBJ = new VoipSheetOBJ();

		voipSheetOBJ.setUserId(userId);
		voipSheetOBJ.setVoipUsername(voipUsername);
		voipSheetOBJ.setVoipPasswd(voipPasswd);
		voipSheetOBJ.setLinePort(linePort);
		voipSheetOBJ.setProtocol(protocol);
		voipSheetOBJ.setVoipTelepone(voipTelepone);

		voipSheetOBJ.setSipPort(sipPort);
		voipSheetOBJ.setSipIp(sipIp);
		voipSheetOBJ.setStandSipIp(standSipIp);
		voipSheetOBJ.setStandSipPort(standSipPort);

		voipSheetOBJ.setOutboundPort(outboundProxyPort);
		voipSheetOBJ.setOutboundProxy(outboundProxy);
		voipSheetOBJ.setStandOutboundProxy(standOutboundProxy);
		voipSheetOBJ.setStandOutboundPort(standOutboundProxyPort);

		voipSheetOBJ.setRegistrarServer(registrarServer);
		voipSheetOBJ.setRegistrarServerPort(registrarServerPort);
		voipSheetOBJ.setStandRegistrarServer(standRegistrarServer);
		voipSheetOBJ.setStandRegistrarServerPort(standRegistrarServerPort);

		int iret = -1;

		if (IPV6VOIP.equals(sheetType)) {

			hgwServUserObj.setPasswd(voipPasswd);
			hgwServUserObj.setUsername(voipUsername);
			iret = businessSheetDispatchServ.voipOpen(hgwServUserObj,voipSheetOBJ, deviceId,
					deviceSn, operTypeId, gw_type);

		} else {
			iret = businessSheetDispatchServ.netOpen(hgwServUserObj, deviceId,
					deviceSn, operTypeId, gw_type);
		}
		ajax = StringUtil.getStringValue(iret);

		return "ajax";
	}

	/**
	 * 根据上网方式获取 wan_type
	 * 
	 * @param String sessionType 连接方式   PPPOE value=1或IP value=2
	 * @param String connType    连接类型   桥接 value=1或路由 value=2
	 * @param String ipType    地址类型   Static Ip value=3或DHCP vlaue = 4
	 * @author Bell
	 * @date 2012年5月25日 
	 * @return int  wanType
	 */
	public static int getWanType(String sessionType,String connType, String ipType){
		logger.debug("getWanType({},{},{})", new Object[] {sessionType, connType,ipType});
		int wanType = 0 ;
		if(StringUtil.getIntegerValue(sessionType)==1){
			//桥接
			if("PPPoE_Bridged".equals(connType)){
				wanType = 1;
			}
			//路由
			else if ("IP_Routed".equals(connType)){
				wanType = 2;
			}
		}else if(StringUtil.getIntegerValue(sessionType)==2){
			//静态IP
			if("Static".equals(ipType)){
				wanType = 3 ;
			}
			//DHCP
			else if("DHCP".equals(ipType)) {
				wanType = 4 ; 
			}
		}
	    return wanType;
	}
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	public String getVlanId() {
		return vlanId;
	}

	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}

	public String getBindPort() {
		return bindPort;
	}

	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
	}

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	public String getIpType() {
		return ipType;
	}

	public void setIpType(String ipType) {
		this.ipType = ipType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	/**
	 * get:ajax
	 * 
	 * @return the ajax
	 */
	public String getAjax() {
		logger.debug("getAjax()");

		return ajax;
	}

	/**
	 * set:ajax
	 * 
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax) {
		logger.debug("setAjax({})", ajax);

		this.ajax = ajax;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getSheetType() {
		return sheetType;
	}

	public void setSheetType(String sheetType) {
		this.sheetType = sheetType;
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

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public BusinessSheetDispatchServ getBusinessSheetDispatchServ() {
		return businessSheetDispatchServ;
	}

	public void setBusinessSheetDispatchServ(
			BusinessSheetDispatchServ businessSheetDispatchServ) {
		this.businessSheetDispatchServ = businessSheetDispatchServ;
	}
	public String getGw_type() {
		return gw_type;
	}
	public void setGw_type(String gwType) {
		gw_type = gwType;
	}
	public Map getDevMap() {
		return devMap;
	}
	public void setDevMap(Map devMap) {
		this.devMap = devMap;
	}
	public void setOperTypeId(String operTypeId) {
		this.operTypeId = operTypeId;
	}
	public String getCaseErr() {
		return caseErr;
	}
	public void setCaseErr(String caseErr) {
		this.caseErr = caseErr;
	}
	public String getAddressOrigin() {
		return addressOrigin;
	}
	public void setAddressOrigin(String addressOrigin) {
		this.addressOrigin = addressOrigin;
	}
	public String getAftr() {
		return aftr;
	}
	public void setAftr(String aftr) {
		this.aftr = aftr;
	}
	public String getIpv6AddressIp() {
		return ipv6AddressIp;
	}
	public void setIpv6AddressIp(String ipv6AddressIp) {
		this.ipv6AddressIp = ipv6AddressIp;
	}
	public String getIpv6AddressDNS() {
		return ipv6AddressDNS;
	}
	public void setIpv6AddressDNS(String ipv6AddressDNS) {
		this.ipv6AddressDNS = ipv6AddressDNS;
	}
	public String getIpv6AddressPrefix() {
		return ipv6AddressPrefix;
	}
	public void setIpv6AddressPrefix(String ipv6AddressPrefix) {
		this.ipv6AddressPrefix = ipv6AddressPrefix;
	}
	
}
