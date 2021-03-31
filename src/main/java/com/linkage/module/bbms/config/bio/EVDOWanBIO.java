package com.linkage.module.bbms.config.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.WanRelatedCAO;
import com.linkage.module.gwms.dao.gw.LanEthDAO;
import com.linkage.module.gwms.dao.gw.LocalServParamDAO;
import com.linkage.module.gwms.dao.gw.WanConnDAO;
import com.linkage.module.gwms.dao.gw.WanConnSessDAO;
import com.linkage.module.gwms.dao.gw.WanDAO;
import com.linkage.module.gwms.dao.gw.WlanDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.LanEthObj;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WanObj;
import com.linkage.module.gwms.resource.dao.DevDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * EVDO模块：配置上网参数BIO
 * @author 漆学启
 * @date 2009-10-13
 */
public class EVDOWanBIO {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(EVDOWanBIO.class);

	/** DAO */
	private DevDAO devDAO;
	private WanDAO wanDAO;
	private WanConnDAO wanConnDAO;
	private WanConnSessDAO wanConnSessDAO;
	private LocalServParamDAO localServParamDAO;
	
	/** CAO */
	private WanRelatedCAO wanCAO;
	private WlanDAO wlanDAO;
	private LanEthDAO lanEthDAO;
	
	private String wanId;
	
	private String wanConnId;
	
	private String wanConnSessId;
	
	private String WAN_PATH = "InternetGatewayDevice.WANDevice.";
	private String WANCONN_PATH = ".WANConnectionDevice.";
	private String PVC_PATH = ".WANDSLLinkConfig.DestinationAddress";
	private String VLANID_PATH = ".WANEthernetLinkConfig.X_CT-COM_VLANIDMark";
	private String PPP_PATH = ".WANPPPConnection.";
	private String IP_PATH = ".WANIPConnection.";
	
	private String LAN1 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1";
	private String LAN2 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2";
	private String LAN3 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3";
	private String LAN4 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4";
	
	private String WLAN1 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1";
	private String WLAN2 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.2";
	private String WLAN3 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.3";
	private String WLAN4 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.4";
	
	private String acceessType;
	
	private Map<String, String> wanConfigMap = null;
	
	private List<Map<String, String>> wanConfigList = new ArrayList<Map<String, String>>();
	
	StringBuilder paramNames = new StringBuilder();
	StringBuilder paramValues = new StringBuilder();
	StringBuilder paramTypes = new StringBuilder();
	String[] paramsnameArr = null;
	String[] paramsValueArr = null;
	String[] paraTypeIdArr = null;
	
	public int getSuperCorba(String deviceId,int code){
		logger.debug("getSuperCorba(deviceId:{},code{})",deviceId,code);
		return wanCAO.getDataFromSG(deviceId, code);
	}
	
	/**
	 * 获取WAN上网相关信息(BBMS)
	 * @date 2009-10-13
	 * @param deviceId
	 */
	//@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDataByBBMS(String deviceId) {
		
		if (deviceId == null) {
			logger.warn("getData deviceId is null");
			return null;
		}
		
		//gw_wan
		WanObj wanObj = wanDAO.getWan(deviceId, "1");
		if (wanObj == null) {
			logger.debug("wanObj 为空");
			return null;
		}
		
		// gw_wan_conn
		WanConnObj[] wanConnObj = wanConnDAO.queryDevWanConn(deviceId);
		if (wanConnObj == null) {
			logger.warn("wanConnObj 为空");
			return null;
		}
		
		for(int i=0;i<wanConnObj.length;i++){

			//gw_wan_conn_session
			WanConnSessObj[] wanConnSessObjArr = wanConnSessDAO.queryDevWanConnSession(wanConnObj[i]);
			if (wanConnSessObjArr == null || wanConnSessObjArr.length == 0) {
				logger.debug("WanConnSessObjArr 为空");

				continue;
			}
			
			Map<String, String> bindPortMap = new HashMap<String, String>();
			
			bindPortMap.put(LAN1, "LAN1");
			bindPortMap.put(LAN2, "LAN2");
			bindPortMap.put(LAN3, "LAN3");
			bindPortMap.put(LAN4, "LAN4");

			bindPortMap.put(WLAN1, "WLAN1");
			bindPortMap.put(WLAN2, "WLAN2");
			bindPortMap.put(WLAN3, "WLAN3");
			bindPortMap.put(WLAN4, "WLAN4");
			
			/** 目前只考虑一个PVC或VLANID对应一个Session，不考虑多Session的情况 */
			wanConfigMap = new HashMap<String, String>();
			
			String bindport = "";
			String bindportStr = String.valueOf(wanConnSessObjArr[0].getBindPort());
			String[] bindportArr = bindportStr.split(",");
			if (null != bindportStr && !"null".equals(bindportStr.trim())) {
				for (int k = 0; k < bindportArr.length; k++) {
					bindport += bindPortMap.get(bindportArr[k]);
					
					if (k != bindportArr.length - 1) {
						bindport += ",";
					}
					
				}
			}
			
			this.acceessType = String.valueOf(wanObj.getAccessType());
			
			wanConfigMap.put("accessType", acceessType);
			
			wanConfigMap.put("vpi",  String.valueOf(wanConnObj[i].getVpi_id()));
			wanConfigMap.put("vci", String.valueOf(wanConnObj[i].getVci_id()));
			wanConfigMap.put("vlanId",  String.valueOf(wanConnObj[i].getVlan_id()));
			
			wanConfigMap.put("wanId", String.valueOf(wanConnSessObjArr[0].getWanId()));
			wanConfigMap.put("wanConnId",  String.valueOf(wanConnSessObjArr[0].getWanConnId()));
			wanConfigMap.put("wanConnSessId", String.valueOf(wanConnSessObjArr[0].getWanConnSessId()));
			if("1".equals(wanConnSessObjArr[0].getSessType())){
				wanConfigMap.put("connType",Global.G_Src_Key_Map.get("1").get(wanConnSessObjArr[0].getConnType()));
				wanConfigMap.put("connStatus",Global.G_Src_Key_Map.get("3").get(wanConnSessObjArr[0].getStatus()));
			}else{
				wanConfigMap.put("connType", Global.G_Src_Key_Map.get("2").get(wanConnSessObjArr[0].getConnType()));
				wanConfigMap.put("connStatus", Global.G_Src_Key_Map.get("4").get(wanConnSessObjArr[0].getStatus()));
			}
			wanConfigMap.put("bindPort",  String.valueOf(bindport));
			wanConfigMap.put("servList",  String.valueOf(wanConnSessObjArr[0].getServList()));
			wanConfigMap.put("ip",  String.valueOf(wanConnSessObjArr[0].getIp()));
			wanConfigMap.put("dns", String.valueOf(wanConnSessObjArr[0].getDns()));
			wanConfigMap.put("username",  String.valueOf(wanConnSessObjArr[0].getUsername()));
			wanConfigMap.put("natEnable", String.valueOf(wanConnSessObjArr[0].getNatEnable()));
			wanConfigMap.put("connError", Global.G_Src_Key_Map.get("7").get(wanConnSessObjArr[0].getConnError()));
			
			wanConfigMap.put("pppAuthProtocol", String.valueOf(wanConnSessObjArr[0].getPppAuthProtocol()));
			wanConfigMap.put("dialNum", String.valueOf(wanConnSessObjArr[0].getDialNum()));
			wanConfigMap.put("workMode", String.valueOf(wanConnSessObjArr[0].getWorkMode()));
			wanConfigMap.put("loadPercent", String.valueOf(wanConnSessObjArr[0].getLoadPercent()));
			
			String backupItfs = String.valueOf(wanConnSessObjArr[0].getBackupItfs());
			if(!"".equals(backupItfs)){
				
				wanConfigMap.put("backupItfs", String.valueOf(wanConnSessObjArr[0].getBackupItfs()));
			}
			
			wanConfigMap.put("connTrigger", String.valueOf(wanConnSessObjArr[0].getConnTrigger()));
			
			wanConfigList.add(wanConfigMap);
		}
		
		logger.debug("调用后台返回的LIST：{}", wanConfigList);
		
		return wanConfigList;
		
	}
	
	/**
	 * 返回上行方式
	 * @author gongsj
	 * @date 2009-7-16
	 * @return
	 */
	public String getAccessType() {
		return acceessType;
	}
	
	/**
	 * 增加上网连接
	 * @date 2009-7-15
	 * @param wanConnObj
	 * @param wanConnSessObj
	 * @param mwbandOBJ
	 */
	@SuppressWarnings("unchecked")
	public boolean addWanRelatedConn(long accOId, WanObj wanObj, WanConnObj wanConnObj, WanConnSessObj wanConnSessObj) {
		logger.debug("-------------------------1");
		int wanServiceId = 1001;
		
		int wanStrategyType = 0;
		
		boolean flag = false;

		/** ADSL, LAN */
		String accessType = wanObj.getAccessType();
		
		/**1: PPP 2: IP*/
		String sessionType = wanConnSessObj.getSessType();
		
		/** DHCP, Static*/
		String ipType = wanConnSessObj.getIpType();
		
		/** IP_Routed, PPPoE_Bridged*/
		String connType = wanConnSessObj.getConnType();
		
		Map map = devDAO.getDevUserByDevId(wanConnObj.getDevice_id());

		/** 增加上网连接 */
		
		StrategyOBJ strategyOBJ = new StrategyOBJ();
		strategyOBJ.setId(StaticTypeCommon.generateLongId());
		strategyOBJ.setDeviceId(wanConnObj.getDevice_id());
		strategyOBJ.setOui(StringUtil.getStringValue(map, "oui"));
		strategyOBJ.setSn(StringUtil.getStringValue(map, "device_serialnumber"));
		strategyOBJ.setUsername(StringUtil.getStringValue(map, "username"));
		
		strategyOBJ.setTime(TimeUtil.getCurrentTime());
		
		StringBuilder sheetPara = new StringBuilder();
		
		if ("ADSL".equals(accessType)) {
			logger.debug("ADSL上行");
			
			if ("1".equals(sessionType) && "PPPoE_Bridged".equals(connType)) {
				//桥接拨号上网
				wanServiceId = 1001;
				sheetPara.append("PVC:").append(wanConnObj.getVpi_id()).append("/").append(wanConnObj.getVci_id());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
				
			} else if ("1".equals(sessionType) && "IP_Routed".equals(connType)) {
				//路由拨号上网
				wanServiceId = 1008;
				sheetPara.append("PVC:").append(wanConnObj.getVpi_id()).append("/").append(wanConnObj.getVci_id());
				sheetPara.append("|||" + wanConnSessObj.getUsername());
				sheetPara.append("|||" + wanConnSessObj.getPassword());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
				
			} else if ("2".equals(sessionType) && "DHCP".equals(ipType)) {
				//DHCP
				
			} else if ("2".equals(sessionType) && "Static".equals(ipType)) {
				//静态IP
				wanServiceId = 1010;
				sheetPara.append("PVC:").append(wanConnObj.getVpi_id()).append("/").append(wanConnObj.getVci_id());
				sheetPara.append("|||" + wanConnSessObj.getIp());
				sheetPara.append("|||" + wanConnSessObj.getMask());
				sheetPara.append("|||" + wanConnSessObj.getGateway());
				sheetPara.append("|||" + wanConnSessObj.getDns());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
			} else {
				logger.error("不能确定service_id");
				return false;
			}
		} else {
			logger.debug("LAN上行");
			if ("1".equals(sessionType) && "PPPoE_Bridged".equals(connType)) {
				//桥接拨号上网
				wanServiceId = 2001;
				sheetPara.append(wanConnObj.getVlan_id());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
				
			} else if ("1".equals(sessionType) && "IP_Routed".equals(connType)) {
				//路由拨号上网
				wanServiceId = 2008;
				sheetPara.append(wanConnObj.getVlan_id());
				sheetPara.append("|||" + wanConnSessObj.getUsername());
				sheetPara.append("|||" + wanConnSessObj.getPassword());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
				
			} else if ("2".equals(sessionType) && "DHCP".equals(ipType)) {
				//DHCP
				
			} else if ("2".equals(sessionType) && "Static".equals(ipType)) {
				//静态IP
				wanServiceId = 2010;
				sheetPara.append(wanConnObj.getVlan_id());
				sheetPara.append("|||" + wanConnSessObj.getIp());
				sheetPara.append("|||" + wanConnSessObj.getMask());
				sheetPara.append("|||" + wanConnSessObj.getGateway());
				sheetPara.append("|||" + wanConnSessObj.getDns());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
			} else {
				logger.error("不能确定service_id");
				return false;
			}
		}
		
		strategyOBJ.setServiceId(wanServiceId);
		
		strategyOBJ.setSheetPara(sheetPara.toString());

		strategyOBJ.setAccOid(accOId);
		strategyOBJ.setOrderId(1);
		strategyOBJ.setType(wanStrategyType);
		strategyOBJ.setTempId(wanServiceId);
		strategyOBJ.setIsLastOne(1);

		logger.debug("-------------------------2");
		
		// PP
		if (wanConnSessDAO.addStrategy(strategyOBJ)) {
			
			wanCAO.addStrategyToPP(String.valueOf(strategyOBJ.getId()));
			flag = true;
		}
		
		return flag;
		
	}
	
	
	/**
	 * 删除上网连接
	 * @author gongsj
	 * @date 2009-7-16
	 * @param wanConnObj
	 * @return
	 */
	public boolean delWanConn(WanConnObj wanConnObj) {
		
		int delResult = wanCAO.delWanConn(wanConnObj, LipossGlobals.getGw_Type(wanConnObj.getDevice_id()));
		
		if (0 == delResult || 1 == delResult) {
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * 配置WAN连接
	 * @date 2009-7-2
	 * @param wanConnObj
	 * @param wanConnSessObj
	 * @param mwbandOBJ
	 * @return
	 */
	public boolean configWanConn(WanObj wanObj, WanConnObj wanConnObj, WanConnSessObj wanConnSessObj) {
		
		boolean wanBoolean = false;
		
		wanId = wanConnSessObj.getWanId();
		wanConnId = wanConnSessObj.getWanConnId();
		wanConnSessId = wanConnSessObj.getWanConnSessId();
		
		String vpi    = wanConnObj.getVpi_id();
		String vci    = wanConnObj.getVci_id();
		String vlanId = wanConnObj.getVlan_id();
		
		String pvcPath = null;
		String vlanIdPath = null;
		String connTypePath = null;
		String bindPortPath = null;
		String servListPath = null;
		String connStatusPath = null;
		String ipPath = null;
		String dnsPath = null;
		String usernamePath = null;
		String natEnablePath = null;
		
		if (vlanId == null) {
			logger.debug("该连接为PPP，设置其相关参数");
			pvcPath    = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PVC_PATH;
			connTypePath   = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PPP_PATH + wanConnSessId + ".ConnectionType";
			bindPortPath   = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PPP_PATH + wanConnSessId + ".X_CT-COM_LanInterface";
			servListPath   = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PPP_PATH + wanConnSessId + ".X_CT-COM_ServiceList";
			connStatusPath = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PPP_PATH + wanConnSessId + ".ConnectionStatus";
			dnsPath        = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PPP_PATH + wanConnSessId + ".DNSServers";
			natEnablePath  = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PPP_PATH + wanConnSessId + ".NATEnabled";
			usernamePath   = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PPP_PATH + wanConnSessId + ".Username";
			
			paramNames.append(pvcPath).append("|").append(connTypePath).append("|").append(bindPortPath).append("|").append(servListPath)
			          .append("|").append(connStatusPath).append("|").append(dnsPath).append("|").append(natEnablePath)
			          .append("|").append(usernamePath);
			
			paramValues.append("PVC:"+vpi+"/"+vci).append("|").append(wanConnSessObj.getConnType()).append("|")
					   .append(wanConnSessObj.getBindPort()).append("|").append(wanConnSessObj.getServList())
	          	 	   .append("|").append(wanConnSessObj.getStatus()).append("|").append(wanConnSessObj.getDns())
	          	 	   .append("|").append(wanConnSessObj.getNatEnable()).append("|").append(wanConnSessObj.getUsername());
			
			paramTypes.append("1").append("|").append("1").append("|").append("1").append("|").append("1")
	          		  .append("|").append("1").append("|").append("1").append("|").append("4")
	          		  .append("|").append("1");
			
		} else {
			logger.debug("该连接为IP，设置其相关参数");
			
			vlanIdPath = WAN_PATH + wanId + WANCONN_PATH + wanConnId + VLANID_PATH;
			connTypePath   = WAN_PATH + wanId + WANCONN_PATH + wanConnId + IP_PATH + wanConnSessId + ".ConnectionType";
			bindPortPath   = WAN_PATH + wanId + WANCONN_PATH + wanConnId + IP_PATH + wanConnSessId + ".X_CT-COM_LanInterface";
			servListPath   = WAN_PATH + wanId + WANCONN_PATH + wanConnId + IP_PATH + wanConnSessId + ".X_CT-COM_ServiceList";
			connStatusPath = WAN_PATH + wanId + WANCONN_PATH + wanConnId + IP_PATH + wanConnSessId + ".ConnectionStatus";
			dnsPath        = WAN_PATH + wanId + WANCONN_PATH + wanConnId + IP_PATH + wanConnSessId + ".DNSServers";
			natEnablePath  = WAN_PATH + wanId + WANCONN_PATH + wanConnId + IP_PATH + wanConnSessId + ".NATEnabled";
			ipPath         = WAN_PATH + wanId + WANCONN_PATH + wanConnId + IP_PATH + wanConnSessId + ".ExternalIPAddress";
			
			paramNames.append(vlanIdPath).append("|").append(connTypePath).append("|").append(bindPortPath).append("|").append(servListPath)
	          		  .append("|").append(connStatusPath).append("|").append(dnsPath).append("|").append(natEnablePath)
	          		  .append("|").append(ipPath);
			
			paramValues.append(vlanId).append("|").append(wanConnSessObj.getConnType()).append("|")
			   		   .append(wanConnSessObj.getBindPort()).append("|").append(wanConnSessObj.getServList())
			   		   .append("|").append(wanConnSessObj.getStatus()).append("|").append(wanConnSessObj.getDns())
			   		   .append("|").append(wanConnSessObj.getNatEnable()).append("|").append(wanConnSessObj.getIp());
			
			paramTypes.append("1").append("|").append("1").append("|").append("1").append("|").append("1")
    		     	  .append("|").append("1").append("|").append("1").append("|").append("4")
    		     	  .append("|").append("1");
			
		}
		
//		totalNumPath = TOTAL_NUM_PATH;
//		paramValues.append("|").append(totalNumPath);
//		paramValues.append("|").append(mwbandOBJ.getTotalNumber());
//		paramTypes.append("|").append("2");
		
		paramsnameArr = paramNames.toString().split("\\|");
		paramsValueArr = paramValues.toString().split("\\|");
		paraTypeIdArr = paramTypes.toString().split("\\|");
		
		paramNames = null;
		paramValues = null;
		paramTypes = null;
		
		for (int i = 0; i < paramsnameArr.length; i++) {
			logger.debug("名:{}；值:{}；属性:{}", new Object[]{paramsnameArr[i], paramsValueArr[i], paraTypeIdArr[i]});
		}
		
		//wanBoolean = wanCAO.realSetParams(deviceId, paramsnameArr, paramsValueArr, paraTypeIdArr);
		
		if (wanBoolean) {
			//更新数据库
			
		}
		
		return wanBoolean;
	}
	
	/**
	 * 获取该设备的所有lan口和wlan口
	 * 
	 * @param deviceId
	 * @return
	 */
	public String getLanInter(String deviceId){
		
		logger.debug("getLanIntf(deviceId:{})",deviceId);
		
		StringBuffer html = new StringBuffer();
		LanEthObj[] lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
		if(null!=lanEthObj){
			for(int i=0;i<lanEthObj.length;i++){
				String code = "LAN"+lanEthObj[i].getLanEthid();
				StringBuffer value = new StringBuffer();
				value.append("InternetGatewayDevice.LANDevice.");
				value.append(lanEthObj[i].getLanid());
				value.append(".LANEthernetInterfaceConfig.");
				value.append(lanEthObj[i].getLanEthid()); 
				
				html.append("<INPUT TYPE='checkbox' NAME='");
				html.append("LAN");
				html.append("' value='");
				html.append(value.toString());
				html.append("'/>");
				html.append(code);
				if((i+1)%4==0){
					html.append("<br>");
				}
			}
		}
		
		List wlanList = wlanDAO.getData(deviceId);
		for(int i=0;i<wlanList.size();i++){
			Map oneWlanMap = (Map) wlanList.get(i);
			String code = "WLAN"+oneWlanMap.get("lan_wlan_id");
			StringBuffer value = new StringBuffer();
			value.append("InternetGatewayDevice.LANDevice.");
			value.append(oneWlanMap.get("lan_id"));
			value.append(".WLANConfiguration.");
			value.append(oneWlanMap.get("lan_wlan_id"));
			
			html.append("<INPUT TYPE='checkbox' NAME='");
			html.append("WLAN");
			html.append("' value='");
			html.append(value.toString());
			html.append("'/>");
			html.append(code);
		}
		
		return html.toString();
	}
	
	/**
	 * setWanDAO
	 * @author gongsj
	 * @date 2009-7-16
	 * @param wanDAO
	 */
	public void setWanDAO(WanDAO wanDAO) {
		this.wanDAO = wanDAO;
	}

	/**
	 * setWanConnDAO
	 * @date 2009-7-16
	 * @param wanConnDAO
	 */
	public void setWanConnDAO(WanConnDAO wanConnDAO) {
		this.wanConnDAO = wanConnDAO;
	}

	/**
	 * setWanConnSessDAO
	 * @date 2009-7-16
	 * @param wanConnSessDAO
	 */
	public void setWanConnSessDAO(WanConnSessDAO wanConnSessDAO) {
		this.wanConnSessDAO = wanConnSessDAO;
	}

	/**
	 * setWanCAO
	 * @date 2009-7-16
	 * @param wanCAO
	 */
	public void setWanCAO(WanRelatedCAO wanCAO) {
		this.wanCAO = wanCAO;
	}

	/**
	 * setDevDAO
	 * @date 2009-7-16
	 * @param devDAO
	 */
	public void setDevDAO(DevDAO devDAO) {
		this.devDAO = devDAO;
	}

	/**
	 * @return the localServParamDAO
	 */
	public LocalServParamDAO getLocalServParamDAO() {
		return localServParamDAO;
	}

	/**
	 * @param localServParamDAO the localServParamDAO to set
	 */
	public void setLocalServParamDAO(LocalServParamDAO localServParamDAO) {
		this.localServParamDAO = localServParamDAO;
	}

	/**
	 * @return the lanEthDAO
	 */
	public LanEthDAO getLanEthDAO() {
		return lanEthDAO;
	}

	/**
	 * @param lanEthDAO the lanEthDAO to set
	 */
	public void setLanEthDAO(LanEthDAO lanEthDAO) {
		this.lanEthDAO = lanEthDAO;
	}

	/**
	 * @return the wlanDAO
	 */
	public WlanDAO getWlanDAO() {
		return wlanDAO;
	}

	/**
	 * @param wlanDAO the wlanDAO to set
	 */
	public void setWlanDAO(WlanDAO wlanDAO) {
		this.wlanDAO = wlanDAO;
	}
	
}
