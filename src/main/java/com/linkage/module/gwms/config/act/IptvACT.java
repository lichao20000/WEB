package com.linkage.module.gwms.config.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.config.bio.IptvBIO;
import com.linkage.module.gwms.obj.gw.MwbandOBJ;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WanObj;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 应用服务模块：配置上网参数ACT（网络应用）
 * @author gongsj
 * @date 2009-7-14
 */
public class IptvACT extends ActionSupport implements ServletRequestAware {
	/** sid */
	private static final long serialVersionUID = -7948213365992547983L;

	/** log */
	private static Logger logger = LoggerFactory.getLogger(IptvACT.class);

	/** device_id */
	private String deviceId;
	
	private String gw_type = null;
	
	/** 上行方式（接入方式）LAN上行或ADSL上行*/
	private String accessType;
	
	private String sessionType;
	private String servList;
	
	private String connType;
	private String vpi;
	private String vci;
	
	private String ipType;
	private String vlanId;
	
	private String natEnable;
	
	/** 多终端上网数 */
	private String num;
	
	private String username;
	private String password;
	
	private String ip;
	private String gateway;
	private String mask;
	private String dns;
	
	private String bindPort;
	
	
	private String wanId;
	private String wanConnId;
	private String wanConnSessId;
	
	/** 只读 */
	private String connStatus;
	
	/** 只读 */ 
	private String connError;
	
	/** wanConfig的列表 */
	private List<Map<String, String>> wanConfigList;
	
	/** BIO */
	private IptvBIO bio;
	
	/** ajax */
	private String ajax = "";
	
	/** request */
	private HttpServletRequest request;

	/** session */
	private HttpSession session;
	
	/**
	 * 端口获取：type为0则先采集后获取端口；为1则先查数据库，查不到再采集
	 */
	private String type;
	private String SSID2;
	/**
	 * WPA认证密钥
	 */
	private String preSharedKey;
	private String deviceSn;
	private String configType;
	
	/**
	 * 作为调用corba失败返回的信息
	 */
	private String corbaMsg = null;
	
	/**
	 * 增加一条IPTV连接，生成策略、调用预读
	 * @date 2009-7-15
	 */
	public String add_bak_20181226() {
		
		WanObj wanOBJ = new WanObj();
		wanOBJ.setAccessType(accessType);
		
		WanConnObj wanConnOBJ = new WanConnObj();
		wanConnOBJ.setDevice_id(deviceId);
		wanConnOBJ.setVpi_id(vpi);
		wanConnOBJ.setVci_id(vci);
		wanConnOBJ.setVlan_id(vlanId);
		
		WanConnSessObj wanConnSessOBJ = new WanConnSessObj();
		
		wanConnSessOBJ.setSessType(sessionType);
		wanConnSessOBJ.setServList(servList);
		
		wanConnSessOBJ.setConnType(connType);
		
		wanConnSessOBJ.setIpType(ipType);
		wanConnSessOBJ.setNatEnable(natEnable);
		
		wanConnSessOBJ.setUsername(username);
		wanConnSessOBJ.setPassword(password);
		
		wanConnSessOBJ.setIp(ip);
		wanConnSessOBJ.setGateway(gateway);
		wanConnSessOBJ.setMask(mask);
		wanConnSessOBJ.setDns(dns);
		
		wanConnSessOBJ.setBindPort(bindPort);
		
		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();
		
		ajax = bio.addWanRelatedConn(accOId, wanOBJ, wanConnOBJ, wanConnSessOBJ);
		
		return "ajax";
	}
	
	/**
	 * 增加一条IPTV连接，生成策略、调用预读
	 * @date 2009-7-15
	 */
	public String add() {
		
		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();
		logger.warn("accOId[{}]管理员新增[{}]设备iptvWan连接，vlanId[{}]", new Object[]{accOId, deviceId, vlanId, num});

		int res = bio.add(accOId, vlanId, deviceId, bindPort);
		
		if(1==res || 0==res){
			ajax="true|成功";
		}else{
			ajax="false|失败";
		}
		
		return "ajax";
	}
	
	/**
	 * 删除IPTV连接
	 * @author gongsj
	 * @date 2009-7-16
	 * @return
	 */
	public String del() {
		WanConnObj wanConnOBJ = new WanConnObj();
		wanConnOBJ.setDevice_id(deviceId);
		wanConnOBJ.setWan_id(wanId);
		wanConnOBJ.setWan_conn_id(wanConnId);
		
		if (bio.delWanConn(wanConnOBJ, gw_type)) {
			ajax = "true";
		} else {
			ajax = "false";
		}
		
		return "ajax";
	}
	/**
	 * 配置
	 */
	public String config() {
		logger.debug("config()");

		WanObj wanOBJ = new WanObj();
		wanOBJ.setAccessType(accessType);
		
		WanConnObj wanConnOBJ = new WanConnObj();
		wanConnOBJ.setDevice_id(deviceId);
		wanConnOBJ.setVpi_id(vpi);
		wanConnOBJ.setVci_id(vci);
		wanConnOBJ.setVlan_id(vlanId);
		
		WanConnSessObj wanConnSessOBJ = new WanConnSessObj();
		wanConnSessOBJ.setWanId(wanId);
		wanConnSessOBJ.setWanConnId(wanConnId);
		wanConnSessOBJ.setWanConnSessId(wanConnSessId);
		
		wanConnSessOBJ.setConnType(connType);
		wanConnSessOBJ.setBindPort(bindPort);
		wanConnSessOBJ.setServList(servList);
		wanConnSessOBJ.setStatus(connStatus);
		wanConnSessOBJ.setIp(ip);
		wanConnSessOBJ.setDns(dns);
		wanConnSessOBJ.setUsername(username);
		wanConnSessOBJ.setNatEnable(natEnable);
		wanConnSessOBJ.setConnError(connError);
		
		MwbandOBJ mwbandOBJ = new MwbandOBJ();
		mwbandOBJ.setTotalNumber(Integer.parseInt(num));
		
		if (bio.configWanConn(wanOBJ, wanConnOBJ, wanConnSessOBJ)) {
			ajax = "true";
		} else {
			ajax = "false";
		}
		
		return ajax;
	}
	
	
	
	/**
	 * get data.
	 * 
	 * @return
	 */
	public String execute() {
		logger.debug("execute()");
		
		int rsint = bio.getSuperCorba(deviceId, 2);
		if (rsint!= 1) {
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
			return SUCCESS;
		}
		
		this.wanConfigList = bio.getData(deviceId);
		if (null == wanConfigList || wanConfigList.isEmpty()) {
			this.corbaMsg = "设备上没有IPTV连接！";
		}
		
		String accessType = bio.getAccessType();
		if (accessType == null) {
			logger.warn("从设备上取得的上行方式为空，默认设为DSL");
			accessType = "DSL";
		}
		this.accessType = accessType;
		
		return SUCCESS;
	}

	/**
	 * 查询端口
	 * 
	 * @return
	 */
	public String addWlanInit(){
		
		logger.debug("addWanInit()");

		ajax = bio.getLanInter(deviceId,type);

		return "ajax";
	}
	
	/**
	 * 获取IPTV数据（ITMS系统IPTV多终端业务开通）
	 *
	 * @author wangsenbo
	 * @date Mar 23, 2010
	 * @param 
	 * @return String
	 */
	public String getIptvData()
	{
		logger.debug("getIptvData()");
		int rsint = bio.getSuperCorba(deviceId, 2);
		if (rsint != 1)
		{
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			if (null == this.corbaMsg)
			{
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason();
			}
			return "list";
		}
		this.wanConfigList = bio.getData(deviceId);
		if (null == wanConfigList || wanConfigList.isEmpty())
		{
			this.corbaMsg = "设备上没有IPTV连接！";
		}
		String accessType = bio.getAccessType();
		if (accessType == null)
		{
			logger.warn("从设备上取得的上行方式为空，默认设为DSL");
			accessType = "DSL";
		}
		this.accessType = accessType;
		return "list";
	}
	
	/**
	 * 查询端口
	 * 
	 * @return
	 */
	public String editWlanInit(){
		
		logger.debug("editWlanInit()");

		ajax = bio.getLanInter(deviceId,type,bindPort);

		return "ajax";
	}
	
	/**
	 * ITMS系统IPTV多终端业务开通
	 * 
	 * @return
	 */
	public String edit(){
		
		logger.debug("edit()");
		
		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();
		if(wanId==null||wanConnId==null||wanConnSessId==null){
			logger.error("wanId,wanConnId,wanConnSessId为空");
		}else{
			ajax = bio.editIptv(accOId,deviceId,deviceSn,wanId,wanConnId,wanConnSessId,bindPort,configType );			
		}		
		return "ajax";
	}
	
	/**
	 * set: BIO
	 * 
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(IptvBIO bio) {
		logger.debug("setBio(IptvBIO)");

		this.bio = bio;
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

	public String getServList() {
		return servList;
	}

	public void setServList(String servList) {
		this.servList = servList;
	}

	public String getConnStatus() {
		return connStatus;
	}

	public void setConnStatus(String connStatus) {
		this.connStatus = connStatus;
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

	public String getNatEnable() {
		return natEnable;
	}

	public void setNatEnable(String natEnable) {
		this.natEnable = natEnable;
	}

	public String getConnError() {
		return connError;
	}

	public void setConnError(String connError) {
		this.connError = connError;
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


	public String getWanId() {
		return wanId;
	}


	public void setWanId(String wanId) {
		this.wanId = wanId;
	}


	public String getWanConnId() {
		return wanConnId;
	}


	public void setWanConnId(String wanConnId) {
		this.wanConnId = wanConnId;
	}


	public String getWanConnSessId() {
		return wanConnSessId;
	}


	public void setWanConnSessId(String wanConnSessId) {
		this.wanConnSessId = wanConnSessId;
	}

	
	public List<Map<String, String>> getWanConfigList() {
		return wanConfigList;
	}

	public void setWanConfigList(List<Map<String, String>> wanConfigList) {
		this.wanConfigList = wanConfigList;
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

	/**
	 * @return the corbaMsg
	 */
	public String getCorbaMsg() {
		return corbaMsg;
	}

	/**
	 * @param corbaMsg the corbaMsg to set
	 */
	public void setCorbaMsg(String corbaMsg) {
		this.corbaMsg = corbaMsg;
	}

	
	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	
	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	
	/**
	 * @return the sSID2
	 */
	public String getSSID2()
	{
		return SSID2;
	}

	/**
	 * @param ssid2 the sSID2 to set
	 */
	public void setSSID2(String ssid2)
	{
		SSID2 = ssid2;
	}

	/**
	 * @return the preSharedKey
	 */
	public String getPreSharedKey()
	{
		return preSharedKey;
	}

	/**
	 * @param preSharedKey the preSharedKey to set
	 */
	public void setPreSharedKey(String preSharedKey)
	{
		this.preSharedKey = preSharedKey;
	}

	/**
	 * @return the deviceSn
	 */
	public String getDeviceSn()
	{
		return deviceSn;
	}

	/**
	 * @param deviceSn the deviceSn to set
	 */
	public void setDeviceSn(String deviceSn)
	{
		this.deviceSn = deviceSn;
	}

	
	/**
	 * @return the configType
	 */
	public String getConfigType()
	{
		return configType;
	}

	
	/**
	 * @param configType the configType to set
	 */
	public void setConfigType(String configType)
	{
		this.configType = configType;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	
}
