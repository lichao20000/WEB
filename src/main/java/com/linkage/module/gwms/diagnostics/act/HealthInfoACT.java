package com.linkage.module.gwms.diagnostics.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.diagnostics.bio.HealthInfoBIO;
import com.linkage.module.gwms.obj.gw.HealthLanWlanOBJ;
import com.linkage.module.gwms.obj.gw.HealthWanDslOBJ;
import com.linkage.module.gwms.obj.gw.VoipAddressOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.opensymphony.xwork2.ActionSupport;

public class HealthInfoACT extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(HealthInfoACT.class);
	
	private HealthInfoBIO healthInfoBio;
	
	private HgwServUserObj wanBussObj;
	
	private HgwServUserObj iptvBussObj;
	
	private HgwServUserObj voipBussObj;
	
	/**
	 * deviceId
	 */
	private String deviceId = null; 
	
	/**
	 * 调用corba返回状态
	 */
	private String corbaMsg = null;
	
	/**
	 * userId
	 */
	private String userId = null;
	
	
	/** HealthWanDslOBJ */
	private HealthWanDslOBJ[] wireInfoObjArr = null;
	
	private HealthLanWlanOBJ wlanInfoObj;
	
	private VoipAddressOBJ voipAddrObj;
	
	/** ajax */
	private String ajax = "";
	
	private String businessId;
	
	/** 业务信息 */
	private String username;
	private String password;
	private String servType;
	private String wanType;
	private String vpi;
	private String vci;
	private String vlanid;
	private String ip;
	private String mask;
	private String gateway;
	private String dns;
	private String bindport;
	
	/** 业务VOIP信息 */
	private String proxServer;
	private String proxPort;
	private String proxServer2;
	private String proxPort2;
	private String regiServ;
	private String regiPort;
	private String standRegiServ;
	private String standRegiPort;
	private String outBoundProxy;
	private String outBoundPort;
	private String standOutBoundProxy;
	private String standOutBoundPort;
	
	/** 线路信息 */
	private String upAttenMin;
	private String upAttenMax;
	private String downAttenMin;
	private String downAttenMax;
	private String upMaxRateMin;
	private String upMaxRateMax;
	private String downMaxRateMin;
	private String downMaxRateMax;
	private String upNoiseMin;
	private String upNoiseMax;
	private String downNoiseMin;
	private String downNoiseMax;
	private String interDepthMin;
	private String interDepthMax;
	

	private String datePath;
	private String gwType;
	/** 无线信息 */
	private String powerlevel;
	private String powervalue;
	
	/**
	 * execute方法
	 */
	public String execute() throws Exception {
		
		logger.debug("execute()");
		
		searchUserId(gwType);
		if (null == userId) {
			logger.warn("deviceId为{}的userId为空", deviceId);
			getWireInfo();
			
		} else {
			getWireInfo();
			getWanInfo();
			getIptvInfo();
			getVoipInfo();
			getVoipAddress();
		}
		
		return SUCCESS;
	}

	
	/**
	 * 获得上网业务信息
	 * @author gongsj
	 * @date 2009-8-13
	 */
	public void getWanInfo() {
		logger.debug("getWanInfo()");
		
		wanBussObj = healthInfoBio.getUserInfo(userId, "10",gwType);
	}
	
	/**
	 * 获得IPTV业务信息
	 * @author gongsj
	 * @date 2009-8-13
	 */
	public void getIptvInfo() {
		logger.debug("getIptvInfo()");
		
		iptvBussObj = healthInfoBio.getUserInfo(userId, "11",gwType);
	}

	/**
	 * 获得VOIP业务信息
	 * @author gongsj
	 * @date 2009-8-13
	 */
	public void getVoipInfo() {
		logger.debug("getVoipInfo()");
		
		voipBussObj = healthInfoBio.getUserInfo(userId, "14",gwType);
	}

	/**
	 * 获得线路信息，如数据中已存在，则直接取，如不存在则采集入库
	 * @author gongsj
	 * @date 2009-8-18
	 * @return
	 */
	public String getWireInfo(){
		
		logger.debug("wireinfoInfo:deviceId=>{},userId=>{}",deviceId,userId);
		
		//检查gw_wan_dsl_inter_conf_health表中是否已存在
		boolean healthWire = healthInfoBio.searchRecordExist(deviceId, "gw_wan_dsl_inter_conf_health");
		
		if (!healthWire) {
			logger.debug("device_id为{}的设备在gw_wan_dsl_inter_conf_health表中不存在，开始采集", deviceId);
			int rsintWire = healthInfoBio.getDataFromSG(deviceId, 21);
			
			if (rsintWire != 1) {
				logger.warn("获得线路信息失败！");
				this.corbaMsg = Global.G_Fault_Map.get(rsintWire)+"！";
				
				if (null == this.corbaMsg) { 
					this.corbaMsg = Global.G_Fault_Map.get(100000)+"！"; 
				}
				
				ajax = "false";
				return "ajax";
			}
		} 
		
		//检查gw_lan_wlan_health表中是否已存在
		boolean healthWlan = healthInfoBio.searchRecordExist(deviceId, "gw_lan_wlan_health");
		
		if (!healthWlan) {
			int rsintWlan = healthInfoBio.getDataFromSG(deviceId, 12);
			if (rsintWlan != 1) {
				logger.warn("获得无线信息失败！");
				this.corbaMsg = Global.G_Fault_Map.get(rsintWlan)+"！";
				
				if (null == this.corbaMsg) { 
					this.corbaMsg = Global.G_Fault_Map.get(100000)+"！"; 
				}
				
				ajax = "false";
				return "ajax";
			}
			
		}

		//把wire表中的数据复制到gw_wan_dsl_inter_conf_health
		//把wlan表中数据复制到gw_lan_wlan_health表中
		healthInfoBio.insertHealthInfo(deviceId, false, false);
		
		this.wireInfoObjArr = healthInfoBio.getDslHealthInfo(deviceId);
		
		this.wlanInfoObj = healthInfoBio.getWlanHealthInfo(deviceId);
		
		ajax = "true";
		return "ajax";
	}
	
	
	/**
	 * 采集设备入库（把原来的数据清除）
	 * @author gongsj
	 * @date 2009-8-18
	 * @return
	 */
	public String gatherWireInfo(){
		
		logger.debug("wireinfoInfo:deviceId=>{},userId=>{}",deviceId,userId);
		
		logger.debug("开始采集device_id为{}的健康库线路信息", deviceId);
		int rsintWire = healthInfoBio.getDataFromSG(deviceId, 21);
		
		if (rsintWire != 1) {
			logger.warn("获得线路信息失败！");
			this.corbaMsg = Global.G_Fault_Map.get(rsintWire)+"！";
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000)+"！"; 
			}
			
			ajax = corbaMsg;
			return "ajax";
		}
		
		logger.debug("开始采集device_id为{}的健康库无线信息", deviceId);
		int rsintWlan = healthInfoBio.getDataFromSG(deviceId, 12);
		if (rsintWlan != 1) {
			logger.warn("获得无线信息失败！");
			this.corbaMsg = Global.G_Fault_Map.get(rsintWlan)+"！";
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000)+"！"; 
			}
			
			ajax = corbaMsg;
			return "ajax";
		}
		
		//删除gw_wan_dsl_inter_conf_health表中已存在的记录
		boolean healthWire = healthInfoBio.deleteRecordExist(deviceId, "gw_wan_dsl_inter_conf_health");
		//删除gw_lan_wlan_health表中已存在的记录
		boolean healthWlan = healthInfoBio.deleteRecordExist(deviceId, "gw_lan_wlan_health");
		
		//把wire表中的数据复制到gw_wan_dsl_inter_conf_health
		//把wlan表中数据复制到gw_lan_wlan_health表中
		healthInfoBio.insertHealthInfo(deviceId, healthWire, healthWlan);
		
		this.wireInfoObjArr = healthInfoBio.getDslHealthInfo(deviceId);
		this.wlanInfoObj = healthInfoBio.getWlanHealthInfo(deviceId);
		
		ajax = "true";
		return "ajax";
	}
	
	/**
	 * 编辑业务信息
	 * @author gongsj
	 * @date 2009-8-17
	 * @return
	 */
	public String editBussInfo() {
		
		logger.debug("HealthInfoACT editBussInfo()");
		
		if (healthInfoBio.editBussInfo(userId, username, password, servType, wanType, vpi, vci, vlanid, ip, mask, gateway, dns, bindport)) {
			ajax = "true";
		} else {
			ajax = "false";
		}
		
		if ("3".equals(businessId)) {
			logger.debug("businessId为3,VOIP业务,更新VOIP地址表");
			if (healthInfoBio.editVoipAddr(deviceId, proxServer, proxPort, proxServer2, proxPort2, regiServ, regiPort, standRegiServ, standRegiPort, outBoundProxy, outBoundPort, standOutBoundProxy, standOutBoundPort)) {
				ajax = "true";
			} else {
				ajax = "false";
			}
		}
		
		
		return "ajax";
	}
	
	/**
	 * 更新健康库信息
	 * @author gongsj
	 * @date 2009-8-18
	 * @return
	 */
	public String editHealthInfo() {
		
		logger.debug("HealthInfoACT editHealthInfo()");
		
		if (healthInfoBio.editHealthInfo(deviceId, upAttenMin, upAttenMax, downAttenMin, downAttenMax, upMaxRateMin, upMaxRateMax, downMaxRateMin, downMaxRateMax, upNoiseMin, upNoiseMax, downNoiseMin, downNoiseMax, interDepthMin, interDepthMax, datePath, powerlevel, powervalue)) {
			ajax = "true";
		} else {
			ajax = "false";
		}
		
		return "ajax";
		
	}
	
	/**
	 * 获得VOIP地址
	 * @author gongsj
	 * @date 2009-8-19
	 * @param deviceId
	 * @return
	 */
	public void getVoipAddress() {
		logger.debug("getVoipAddress(device_id={})", deviceId);
		
		this.voipAddrObj = healthInfoBio.getVoipAddress(deviceId);
		
	}
	
	/**
	 * 获得业务信息
	 * @author gongsj
	 * @date 2009-8-17
	 * @return
	 */
	public String getBussInfo() {
		
		//searchUserId();
		if (null == userId) {
			logger.warn("deviceId为{}的userId为空", deviceId);
		} else {
			getWanInfo();
			getIptvInfo();
			getVoipInfo();
		}
		
		ajax = "true";
		return "ajax";
	}
	
	
	/**
	 * 获得userId(根据deviceId)
	 * @author gongsj
	 * @param gwType1 
	 * @date 2009-8-17
	 */
	private void searchUserId(String gwType1) {
		logger.debug("searchUserId");
		
	    String userId = healthInfoBio.getUserId(deviceId,gwType1);
	    
	    if (null == userId) {
	    	
	    	this.userId = null;
	    	
	    } else {
	    	
	    	this.userId = userId;
	    }
	    
	    logger.debug("searchUserId userId:{}", userId);
	    
	}
	
	
	public HgwServUserObj getWanBussObj() {
		return wanBussObj;
	}


	public void setWanBussObj(HgwServUserObj wanBussObj) {
		this.wanBussObj = wanBussObj;
	}


	public HgwServUserObj getIptvBussObj() {
		return iptvBussObj;
	}


	public void setIptvBussObj(HgwServUserObj iptvBussObj) {
		this.iptvBussObj = iptvBussObj;
	}


	public HgwServUserObj getVoipBussObj() {
		return voipBussObj;
	}


	public void setVoipBussObj(HgwServUserObj voipBussObj) {
		this.voipBussObj = voipBussObj;
	}


	public String getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public String getCorbaMsg() {
		return corbaMsg;
	}


	public void setCorbaMsg(String corbaMsg) {
		this.corbaMsg = corbaMsg;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public void setHealthInfoBio(HealthInfoBIO healthInfoBio) {
		this.healthInfoBio = healthInfoBio;
	}

	public HealthWanDslOBJ[] getWireInfoObjArr() {
		return wireInfoObjArr;
	}

	public void setWireInfoObjArr(HealthWanDslOBJ[] wireInfoObjArr) {
		this.wireInfoObjArr = wireInfoObjArr;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getServType() {
		return servType;
	}


	public void setServType(String servType) {
		this.servType = servType;
	}


	public String getWanType() {
		return wanType;
	}


	public void setWanType(String wanType) {
		this.wanType = wanType;
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


	public String getVlanid() {
		return vlanid;
	}


	public void setVlanid(String vlanid) {
		this.vlanid = vlanid;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getMask() {
		return mask;
	}


	public void setMask(String mask) {
		this.mask = mask;
	}


	public String getGateway() {
		return gateway;
	}


	public void setGateway(String gateway) {
		this.gateway = gateway;
	}


	public String getDns() {
		return dns;
	}


	public void setDns(String dns) {
		this.dns = dns;
	}


	public String getBindport() {
		return bindport;
	}


	public void setBindport(String bindport) {
		this.bindport = bindport;
	}


	public String getAjax() {
		return ajax;
	}


	public void setAjax(String ajax) {
		this.ajax = ajax;
	}


	public HealthLanWlanOBJ getWlanInfoObj() {
		return wlanInfoObj;
	}


	public void setWlanInfoObj(HealthLanWlanOBJ wlanInfoObj) {
		this.wlanInfoObj = wlanInfoObj;
	}


	public String getUpAttenMin() {
		return upAttenMin;
	}


	public void setUpAttenMin(String upAttenMin) {
		this.upAttenMin = upAttenMin;
	}


	public String getUpAttenMax() {
		return upAttenMax;
	}


	public void setUpAttenMax(String upAttenMax) {
		this.upAttenMax = upAttenMax;
	}


	public String getDownAttenMin() {
		return downAttenMin;
	}


	public void setDownAttenMin(String downAttenMin) {
		this.downAttenMin = downAttenMin;
	}


	public String getDownAttenMax() {
		return downAttenMax;
	}


	public void setDownAttenMax(String downAttenMax) {
		this.downAttenMax = downAttenMax;
	}


	public String getUpMaxRateMin() {
		return upMaxRateMin;
	}


	public void setUpMaxRateMin(String upMaxRateMin) {
		this.upMaxRateMin = upMaxRateMin;
	}


	public String getUpMaxRateMax() {
		return upMaxRateMax;
	}


	public void setUpMaxRateMax(String upMaxRateMax) {
		this.upMaxRateMax = upMaxRateMax;
	}


	public String getDownMaxRateMin() {
		return downMaxRateMin;
	}


	public void setDownMaxRateMin(String downMaxRateMin) {
		this.downMaxRateMin = downMaxRateMin;
	}


	public String getDownMaxRateMax() {
		return downMaxRateMax;
	}


	public void setDownMaxRateMax(String downMaxRateMax) {
		this.downMaxRateMax = downMaxRateMax;
	}


	public String getUpNoiseMin() {
		return upNoiseMin;
	}


	public void setUpNoiseMin(String upNoiseMin) {
		this.upNoiseMin = upNoiseMin;
	}


	public String getUpNoiseMax() {
		return upNoiseMax;
	}


	public void setUpNoiseMax(String upNoiseMax) {
		this.upNoiseMax = upNoiseMax;
	}


	public String getDownNoiseMin() {
		return downNoiseMin;
	}


	public void setDownNoiseMin(String downNoiseMin) {
		this.downNoiseMin = downNoiseMin;
	}


	public String getDownNoiseMax() {
		return downNoiseMax;
	}


	public void setDownNoiseMax(String downNoiseMax) {
		this.downNoiseMax = downNoiseMax;
	}


	public String getInterDepthMin() {
		return interDepthMin;
	}


	public void setInterDepthMin(String interDepthMin) {
		this.interDepthMin = interDepthMin;
	}


	public String getInterDepthMax() {
		return interDepthMax;
	}


	public void setInterDepthMax(String interDepthMax) {
		this.interDepthMax = interDepthMax;
	}


	public String getDatePath() {
		return datePath;
	}


	public void setDatePath(String datePath) {
		this.datePath = datePath;
	}


	public String getPowerlevel() {
		return powerlevel;
	}


	public void setPowerlevel(String powerlevel) {
		this.powerlevel = powerlevel;
	}


	public String getPowervalue() {
		return powervalue;
	}


	public void setPowervalue(String powervalue) {
		this.powervalue = powervalue;
	}


	public HealthInfoBIO getHealthInfoBio() {
		return healthInfoBio;
	}


	public String getProxServer() {
		return proxServer;
	}


	public void setProxServer(String proxServer) {
		this.proxServer = proxServer;
	}


	public String getProxPort() {
		return proxPort;
	}


	public void setProxPort(String proxPort) {
		this.proxPort = proxPort;
	}


	public String getProxServer2() {
		return proxServer2;
	}


	public void setProxServer2(String proxServer2) {
		this.proxServer2 = proxServer2;
	}


	public String getProxPort2() {
		return proxPort2;
	}


	public void setProxPort2(String proxPort2) {
		this.proxPort2 = proxPort2;
	}


	public String getRegiServ() {
		return regiServ;
	}


	public void setRegiServ(String regiServ) {
		this.regiServ = regiServ;
	}


	public String getRegiPort() {
		return regiPort;
	}


	public void setRegiPort(String regiPort) {
		this.regiPort = regiPort;
	}


	public String getStandRegiServ() {
		return standRegiServ;
	}


	public void setStandRegiServ(String standRegiServ) {
		this.standRegiServ = standRegiServ;
	}


	public String getStandRegiPort() {
		return standRegiPort;
	}


	public void setStandRegiPort(String standRegiPort) {
		this.standRegiPort = standRegiPort;
	}


	public String getOutBoundProxy() {
		return outBoundProxy;
	}


	public void setOutBoundProxy(String outBoundProxy) {
		this.outBoundProxy = outBoundProxy;
	}


	public String getOutBoundPort() {
		return outBoundPort;
	}


	public void setOutBoundPort(String outBoundPort) {
		this.outBoundPort = outBoundPort;
	}


	public String getStandOutBoundProxy() {
		return standOutBoundProxy;
	}


	public void setStandOutBoundProxy(String standOutBoundProxy) {
		this.standOutBoundProxy = standOutBoundProxy;
	}


	public String getStandOutBoundPort() {
		return standOutBoundPort;
	}


	public void setStandOutBoundPort(String standOutBoundPort) {
		this.standOutBoundPort = standOutBoundPort;
	}


	public VoipAddressOBJ getVoipAddrObj() {
		return voipAddrObj;
	}


	public void setVoipAddrObj(VoipAddressOBJ voipAddrObj) {
		this.voipAddrObj = voipAddrObj;
	}


	public String getBusinessId() {
		return businessId;
	}


	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	public String getGwType()
	{
		return gwType;
	}


	
	public void setGwType(String gwType)
	{
		this.gwType = gwType;
	}
}














