package com.linkage.module.gwms.config.bio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.module.gwms.ConstantClass;
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
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 应用服务模块：配置IPTV BIO（网络应用）
 * @author gongsj
 * @date 2009-7-14
 */
public class IptvBIO {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(IptvBIO.class);

	/** DAO */
	private DevDAO devDAO;
	private WanDAO wanDAO;
	private WanConnDAO wanConnDAO;
	private WanConnSessDAO wanConnSessDAO;
	private LocalServParamDAO localServParamDAO;
	private WlanDAO wlanDAO;
	private LanEthDAO lanEthDAO;
	
	/** CAO */
	private WanRelatedCAO wanCAO;
	
	private String deviceId;
	
	private String wanId;
	
	private String wanConnId;
	
	private String wanConnSessId;
	
	private String WAN_PATH = "InternetGatewayDevice.WANDevice.";
	private String WANCONN_PATH = ".WANConnectionDevice.";
	private String PVC_PATH = ".WANDSLLinkConfig.DestinationAddress";
	private String VLANID_PATH = ".WANEthernetLinkConfig.X_CT-COM_VLANIDMark";
	private String PPP_PATH = ".WANPPPConnection.";
	private String IP_PATH = ".WANIPConnection.";
	private String TOTAL_NUM_PATH = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.TotalTerminalNumber";
	
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
	
	private List<Map<String, String>> wanConfigList= new ArrayList<Map<String, String>>();
	
	StringBuilder paramNames = new StringBuilder();
	StringBuilder paramValues = new StringBuilder();
	StringBuilder paramTypes = new StringBuilder();
	String[] paramsnameArr = null;
	String[] paramsValueArr = null;
	String[] paraTypeIdArr = null;
	
	private Map<String, String> bindPortMap = new HashMap<String, String>();
	
	public int getSuperCorba(String deviceId,int code){
		logger.debug("getSuperCorba(deviceId:{},code{})",deviceId,code);
		return wanCAO.getDataFromSG(deviceId, code);
	}
	
	/**
	 * 获取WAN上网相关信息
	 * @date 2009-7-14
	 * @param deviceId
	 */
	//@SuppressWarnings("unchecked")
	public List<Map<String, String>> getData(String deviceId) {
		
		if (deviceId == null) {
			logger.warn("getData deviceId is null");
			return null;
		}
		
		//gw_wan
		WanObj wanObj = wanDAO.getWan(deviceId, "1");
		if (wanObj == null) {
			logger.debug("wanObj 为空");
			this.acceessType = null;
			return null;
		}
		
		this.acceessType = String.valueOf(wanObj.getAccessType());
		
		String servType = "11";
		String[] pvcTable = localServParamDAO.getServPvc(servType);
		if(null==pvcTable){
			logger.error("iptv获取配置表的pvc失败！");
			return null;
		}
		
		//gw_wan_conn
		WanConnObj wanConnObj = null;
		List<WanConnObj> wanConnObjList = new ArrayList<WanConnObj>();
		List<WanConnObj> wanConnObjListTmp = new ArrayList<WanConnObj>();
		
		for(int i=0;i<pvcTable.length;i++){
			String[] pvcOne = pvcTable[i].split("/");
			if(1==pvcOne.length){
				wanConnObj = wanConnDAO.queryDevWanConn(deviceId, pvcOne[0]);
				if (wanConnObj != null && null!=wanConnObj.getWan_id() && !"".equals(wanConnObj.getWan_id())) {
					wanConnObjList.add(wanConnObj);
				}
			}else{
				//网络应用，页面点击iptv，这里的sql带上vpi_id vci_id 2个条件查询不到值，电信也是如此，都一样。
				//wanConnObj = wanConnDAO.queryDevWanConn(deviceId, pvcOne[0], pvcOne[1]);
				wanConnObjListTmp = wanConnDAO.queryDevWanConnIPTVNEWs(deviceId, "IPTV");
				if (wanConnObjListTmp != null) {
					wanConnObjList.addAll(wanConnObjListTmp);
				}
			}
		}
		
		if (wanConnObjList == null || 0==wanConnObjList.size()) {
			logger.debug("wanConnObj 为空");
			return null;
		}
		
		//gw_wan_conn_session
		ArrayList<WanConnSessObj> wanConnSessObjList = new ArrayList<WanConnSessObj>();
		for(int i=0;i<wanConnObjList.size();i++){
			WanConnSessObj[] wanConnSessObjArr = wanConnSessDAO.queryDevWanConnSession(wanConnObjList.get(i));
			wanConnSessObjList.addAll(new ArrayList<WanConnSessObj>(Arrays.asList(wanConnSessObjArr)));
		}
		
		if (wanConnSessObjList.size() == 0) {
			logger.debug("WanConnSessObjArr 为空");

			return null;
		}
		
		bindPortMap.put(LAN1, "LAN1");
		bindPortMap.put(LAN2, "LAN2");
		bindPortMap.put(LAN3, "LAN3");
		bindPortMap.put(LAN4, "LAN4");
		
		bindPortMap.put(WLAN1, "WLAN1");
		bindPortMap.put(WLAN2, "WLAN2");
		bindPortMap.put(WLAN3, "WLAN3");
		bindPortMap.put(WLAN4, "WLAN4");
		
		bindPortMap.put(LAN1+".", "LAN1");
		bindPortMap.put(LAN2+".", "LAN2");
		bindPortMap.put(LAN3+".", "LAN3");
		bindPortMap.put(LAN4+".", "LAN4");

		bindPortMap.put(WLAN1+".", "WLAN1");
		bindPortMap.put(WLAN2+".", "WLAN2");
		bindPortMap.put(WLAN3+".", "WLAN3");
		bindPortMap.put(WLAN4+".", "WLAN4");
		
		/** 目前只考虑一个PVC或VLANID对应一个Session，不考虑多Session的情况 */
		/** 2020-05-06 支持多业务 */
		for(int i=0;i<wanConnSessObjList.size();i++){
			HashMap<String, String> wanConfigMap = new HashMap<String, String>();
			
			String bindport = "";
			String bindportStr = String.valueOf(wanConnSessObjList.get(i).getBindPort());
			String[] bindportArr = bindportStr.split(",");
			if (null != bindportStr && !"null".equals(bindportStr.trim())) {
				for (int j = 0; j < bindportArr.length; j++) {
					bindport += bindPortMap.get(bindportArr[j]);
					
					if (j != bindportArr.length - 1) {
						bindport += ",";
					}
					
				}
			}
			
			wanConfigMap.put("accessType", acceessType);
			
			wanConfigMap.put("vpi",  String.valueOf(wanConnObjList.get(i).getVpi_id()));
			wanConfigMap.put("vci", String.valueOf(wanConnObjList.get(i).getVci_id()));
			wanConfigMap.put("vlanid",  String.valueOf(wanConnObjList.get(i).getVlan_id()));
			
			wanConfigMap.put("wanId", String.valueOf(wanConnSessObjList.get(i).getWanId()));
			wanConfigMap.put("wanConnId",  String.valueOf(wanConnSessObjList.get(i).getWanConnId()));
			wanConfigMap.put("wanConnSessId", String.valueOf(wanConnSessObjList.get(i).getWanConnSessId()));
			wanConfigMap.put("bindPort",  String.valueOf(bindport));
			wanConfigMap.put("bindportStr",  bindportStr);
			wanConfigMap.put("servList",  String.valueOf(wanConnSessObjList.get(i).getServList()));
			if("1".equals(wanConnSessObjList.get(i).getSessType())){
				wanConfigMap.put("connType",Global.G_Src_Key_Map.get("1").get(wanConnSessObjList.get(i).getConnType()));
				wanConfigMap.put("connStatus",Global.G_Src_Key_Map.get("3").get(wanConnSessObjList.get(i).getStatus()));
			}else{
				wanConfigMap.put("connType", Global.G_Src_Key_Map.get("2").get(wanConnSessObjList.get(i).getConnType()));
				wanConfigMap.put("connStatus", Global.G_Src_Key_Map.get("4").get(wanConnSessObjList.get(i).getStatus()));
			}
			wanConfigMap.put("ip",  String.valueOf(wanConnSessObjList.get(i).getIp()));
			wanConfigMap.put("dns", String.valueOf(wanConnSessObjList.get(i).getDns()));
			wanConfigMap.put("username",  String.valueOf(wanConnSessObjList.get(i).getUsername()));
			wanConfigMap.put("natEnable", String.valueOf(wanConnSessObjList.get(i).getNatEnable()));
			wanConfigMap.put("connError", Global.G_Src_Key_Map.get("7").get(wanConnSessObjList.get(i).getConnError()));
			wanConfigMap.put("sessType", String.valueOf(wanConnSessObjList.get(i).getSessType()));
			
			//wanConfigMap.put("num", String.valueOf(mwBandList.get(0).get("total_number")));
			
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
	public String addWanRelatedConn(long accOId, WanObj wanObj, WanConnObj wanConnObj, WanConnSessObj wanConnSessObj) {
		
		int iptvServiceId = 1101;
		
		int iptvStrategyType = 0;
		
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
				//桥接IPTV
				iptvServiceId = 1101;
				sheetPara.append("PVC:").append(wanConnObj.getVpi_id()).append("/").append(wanConnObj.getVci_id());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
				
			} else if ("1".equals(sessionType) && "IP_Routed".equals(connType)) {
				//路由IPTV
				iptvServiceId = 0000;
				sheetPara.append("PVC:").append(wanConnObj.getVpi_id()).append("/").append(wanConnObj.getVci_id());
				sheetPara.append("|||" + wanConnSessObj.getUsername());
				sheetPara.append("|||" + wanConnSessObj.getPassword());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
				
			} else if ("2".equals(sessionType) && "DHCP".equals(ipType)) {
				//DHCP
				
			} else if ("2".equals(sessionType) && "Static".equals(ipType)) {
				//静态IP
				iptvServiceId = 0000;
				sheetPara.append("PVC:").append(wanConnObj.getVpi_id()).append("/").append(wanConnObj.getVci_id());
				sheetPara.append("|||" + wanConnSessObj.getIp());
				sheetPara.append("|||" + wanConnSessObj.getMask());
				sheetPara.append("|||" + wanConnSessObj.getGateway());
				sheetPara.append("|||" + wanConnSessObj.getDns());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
			} else {
				logger.error("不能确定service_id");
				return "false";
			}
		} else {
			logger.debug("LAN上行");
			if ("1".equals(sessionType) && "PPPoE_Bridged".equals(connType)) {
				//桥接IPTV
				iptvServiceId = 2101;
				sheetPara.append(wanConnObj.getVlan_id());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
				
			} else if ("1".equals(sessionType) && "IP_Routed".equals(connType)) {
				//路由IPTV
				iptvServiceId = 0000;
				sheetPara.append(wanConnObj.getVlan_id());
				sheetPara.append("|||" + wanConnSessObj.getUsername());
				sheetPara.append("|||" + wanConnSessObj.getPassword());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
				
			} else if ("2".equals(sessionType) && "DHCP".equals(ipType)) {
				//DHCP
				
			} else if ("2".equals(sessionType) && "Static".equals(ipType)) {
				//静态IP
				iptvServiceId = 0000;
				sheetPara.append(wanConnObj.getVlan_id());
				sheetPara.append("|||" + wanConnSessObj.getIp());
				sheetPara.append("|||" + wanConnSessObj.getMask());
				sheetPara.append("|||" + wanConnSessObj.getGateway());
				sheetPara.append("|||" + wanConnSessObj.getDns());
				sheetPara.append("|||" + wanConnSessObj.getBindPort());
			} else {
				logger.error("不能确定service_id");
				return "false";
			}
		}
		
		strategyOBJ.setServiceId(iptvServiceId);
		
		strategyOBJ.setSheetPara(sheetPara.toString());

		strategyOBJ.setAccOid(accOId);
		strategyOBJ.setOrderId(1);
		strategyOBJ.setType(iptvStrategyType);
		strategyOBJ.setTempId(iptvServiceId);
		strategyOBJ.setIsLastOne(1);

		// PP
		if (wanConnSessDAO.addStrategy(strategyOBJ)) {
			
			wanCAO.addStrategyToPP(String.valueOf(strategyOBJ.getId()));
			return "true;"+strategyOBJ.getId();
		}else{
			return "false";
		}
		
		
		
		
	}
	
	
	/**
	 * 删除上网连接
	 * @author gongsj
	 * @date 2009-7-16
	 * @param wanConnObj
	 * @return
	 */
	public boolean delWanConn(WanConnObj wanConnObj, String gw_type) {
		
		int delResult = wanCAO.delWanConn(wanConnObj, gw_type);
		
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
		
		deviceId = wanConnObj.getDevice_id();
		
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
	 * @param type   type为0则先采集后获取端口;为1则先查数据库，查不到再采集
	 * @return
	 */
	public String getLanInter(String deviceId, String type){
		
		logger.debug("getLanIntf(deviceId:{})",deviceId);
		
		int rsint;

		StringBuffer html = new StringBuffer();
		LanEthObj[] lanEthObj = null;
		if("0".equals(type)){
			rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_ETHERNET);
			if ( rsint == 1 ) {
				lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
			}else{
				logger.debug("LAN口获取失败");
				return "端口获取失败！";
			}
		}else if("1".equals(type)){
			lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
			if(null==lanEthObj){
				//SG
				rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_ETHERNET);
				if ( rsint == 1 ) {
					lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
				}else{
					logger.debug("LAN口获取失败");
					return "端口获取失败！";
				}
			}			
		}else{
			logger.debug("传入type错误");
			return "端口获取失败！";
		}
		
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
			}
		}
		
		List wlanList;
		if("0".equals(type)){
			rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_WLAN);
			if ( rsint == 1 ) {
				wlanList = wlanDAO.getData(deviceId);
			}else{
				logger.debug("WLAN口获取失败");
				return "端口获取失败！";
			}
		}else if("1".equals(type)){
			wlanList = wlanDAO.getData(deviceId);
			if(wlanList.size()<=0){
				//SG
				rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_WLAN);
				if ( rsint == 1 ) {
					wlanList = wlanDAO.getData(deviceId);
				}else{
					logger.debug("WLAN口获取失败");
					return "端口获取失败！";
				}
			}			
		}else{
			logger.debug("传入type错误");
			return "端口获取失败！";
		}
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

	/**
	 * 获取该设备的所有lan口
	 * 
	
	 *
	 * @author wangsenbo
	 * @date Mar 23, 2010
	 * @param deviceId
	 * @param type   type为0则先采集后获取端口;为1则先查数据库，查不到再采集
	 * @return String
	 */
	public String getLanInter(String deviceId, String type, String bindPort)
	{
		logger.debug("getLanIntf({},{},{})",new Object[]{deviceId,type,bindPort});
		
		int rsint;

		StringBuffer html = new StringBuffer();
		LanEthObj[] lanEthObj = null;
		if("0".equals(type)){
			rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_ETHERNET);
			if ( rsint == 1 ) {
				lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
			}else{
				logger.debug("LAN口获取失败");
				return "端口获取失败！";
			}
		}else if("1".equals(type)){
			lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
			if(null==lanEthObj){
				//SG
				rsint = getSuperCorba(deviceId, ConstantClass.GATHER_LAN_ETHERNET);
				if ( rsint == 1 ) {
					lanEthObj = lanEthDAO.getLanEthObj(deviceId, "");
				}else{
					logger.debug("LAN口获取失败");
					return "端口获取失败！";
				}
			}			
		}else{
			logger.debug("传入type错误");
			return "端口获取失败！";
		}
		
		if(null!=lanEthObj){
			String LANPortList = "";
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
				html.append("'");
				if(-1!=bindPort.indexOf(value.toString())){
					html.append(" checked='checked'");
					LANPortList = LANPortList +value.toString()+",";
				}
				html.append("/>");
				html.append(code);
			}
			html.append("<input type='hidden' name='LANPortList' value='").append(LANPortList).append("'>");
		}
		return html.toString();
	}

	/**
	 * ITMS系统IPTV多终端业务开通
	 *
	 * @author wangsenbo
	 * @date Mar 23, 2010
	 * @param 
	 * @return String
	 */
	public String editIptv(long accOId, String deviceId, String deviceSn, String wanId,
			String wanConnId, String wanConnSessId, String bindPort, String configType)
	{
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("iTVs");
		
		Element Lan = root.addElement("Lan");
		Lan.addElement("i").addText(wanId);
		Lan.addElement("j").addText(wanConnId);
		Lan.addElement("k").addText(wanConnSessId);
		
		Element Wlan = root.addElement("Wlan");
		Wlan.addAttribute("flag", configType);
		if(deviceSn.length()>5){
			Wlan.addElement("Ssid").addText("iTV"+deviceSn.substring(deviceSn.length()-5));
		}else{
			Wlan.addElement("Ssid").addText("iTV"+deviceSn);
		}
		if(deviceSn.length()>8){
			Wlan.addElement("PreSharedKey").addText(deviceSn.substring(deviceSn.length()-8));
		}else{
			Wlan.addElement("PreSharedKey").addText(deviceSn);
		}
		root.addElement("BindPort").addText(bindPort==null?"":bindPort);
		strXml = doc.asXML();
		// 立即执行
		int strategyType = 0;
		// IPTV配置的service_id
		int ServiceId = 116;
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.createId();
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(accOId);
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(deviceId);
		// QOS serviceId
		strategyObj.setServiceId(ServiceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strXml);
		strategyObj.setTempId(ServiceId);
		strategyObj.setIsLastOne(1);
		// 入策略表
		if (lanEthDAO.addStrategy(strategyObj))
		{
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(LipossGlobals.getGw_Type(deviceId)).processOOBatch(String.valueOf(strategyObj
					.getId())))
			{
				logger.debug("成功");
				return 1 + ";调用后台成功;" + strategyObj.getId();
			}
			else
			{
				logger.warn("调用预读失败");
				return -1 + ";调用后台失败";
			}
		}
		else
		{
			logger.warn("策略入库失败");
			return -1 + ";策略入库失败";
		}
	}
	
	
	/**
	 * 增加一条IPTV连接，生成策略、调用预读
	 * @date 2009-7-15
	 */
	public int add(long accOId, String vlanId, String deviceId, String bindPort) {
		
		return wanCAO.addWanConn(accOId, vlanId, deviceId, bindPort);
		
//		AddWanUtil addWan = new AddWanUtil(deviceId);
//		String num = addWan.work();
//		logger.warn("accOId[{}]管理员新增[{}]设备iptvWan连接，vlanId[{}]新增节点值=[{}]=", new Object[]{accOId, deviceId, vlanId, num});
//		String pathTmp = wanPath + num + pppcPath + "1";
//		String pathTmpShort = wanPath + num;
//		
//		RPCObject[] rpcObj = new RPCObject[8];
//		rpcObj[0] = (SetParameterValues)acsCorba.getSetParameterValues(
//				new String[] {pathTmpShort + mode}, new String[]{"2"}, new String[] { "1" });
//		rpcObj[1] = (SetParameterValues)acsCorba.getSetParameterValues(
//				new String[] {pathTmpShort + vlanIdMark}, new String[]{vlanId}, new String[] { "1" });
//		rpcObj[2] = (SetParameterValues)acsCorba.getSetParameterValues(
//				new String[] {pathTmp + connectionType}, new String[]{"PPPoE_Bridged"}, new String[] { "1" });
//		rpcObj[3] = (SetParameterValues)acsCorba.getSetParameterValues(
//				new String[] {pathTmp + lanInterface}, new String[]{"InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2,InternetGatewayDevice.LANDevice.1.WLANConfiguration.2"}, new String[] { "1" });
//		rpcObj[4] = (SetParameterValues)acsCorba.getSetParameterValues(
//				new String[] {pathTmp + serviceList}, new String[]{"OTHER"}, new String[] { "1" });
//		rpcObj[5] = (SetParameterValues)acsCorba.getSetParameterValues(
//				new String[] {pathTmp + enable}, new String[]{"1"}, new String[] { "1" });
//		rpcObj[6] = (SetParameterValues)acsCorba.getSetParameterValues(
//				new String[] {pathTmp + dHCPEnable}, new String[]{"0"}, new String[] { "1" });
//		rpcObj[7] = (SetParameterValues)acsCorba.getSetParameterValues(
//				new String[] {pathTmp + multicastVlan}, new String[]{"50"}, new String[] { "1" });
//		
//		DevRpc[] devRPCArr = acsCorba.getDevRPCArr(deviceId,rpcObj);
//		int res = callACS(devRPCArr, 1, accOId);
//		logger.warn("accOId[{}]管理员新增[{}]设备iptvWan连接，vlanId[{}]新增节点值[{}]，设值结果[{}]", new Object[]{accOId, deviceId, vlanId, num, res});
//		
//		return res;
	}
		
	
	
	
}
