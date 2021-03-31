package com.linkage.litms.paramConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

/**
 * bio for wan/wlan config.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ParamInfoAct
{
	private static Logger logger = LoggerFactory.getLogger(ParamInfoAct.class);
	private boolean fromDB = false;
	private Map<String, String> accessTypeMapAll;
	private Map<String, String> sessionTypeMapAll;
	ParamTreeObject paramTreeObject = new ParamTreeObject();
	ParamInfoCORBA paramInfoCorba = new ParamInfoCORBA();
	ParamInfoDAO paramInfoDao = new ParamInfoDAO();
	WlanConfigDAO wlanConfDao = new WlanConfigDAO();
	
	/**华为厂商*/
	public static final String VENDORID_HW="2";
	/**中兴厂商*/
	public static final String VENDORID_ZX="10";
	private String vendorId="";
	/**运营商*/
	public static String instArea=Global.instAreaShortName;

	public Map<String, String> getSessionTypeMapAll(){
		return sessionTypeMapAll;
	}

	public void setSessionTypeMapAll(Map<String, String> sessionTypeMapAll){
		this.sessionTypeMapAll = sessionTypeMapAll;
	}

	public Map<String, String> getAccessTypeMapAll(){
		return accessTypeMapAll;
	}

	public void setAccessTypeMapAll(Map<String, String> accessTypeMapAll){
		this.accessTypeMapAll = accessTypeMapAll;
	}

	public ParamInfoAct(boolean fromDB){
		this.fromDB = fromDB;
	}
	
	public ParamInfoAct(String vendorId){
		this.vendorId = vendorId;
	}

	public ParamInfoAct(){}

	static Map<String, String> paramTypesMap = new HashMap<String, String>();
	static
	{
		paramTypesMap.put("enable", "4");
		paramTypesMap.put("name", "1");
		paramTypesMap.put("conn_type", "1");
		paramTypesMap.put("serv_list", "1");
		paramTypesMap.put("bind_port", "1");
		paramTypesMap.put("username", "1");
		paramTypesMap.put("password", "1");
		paramTypesMap.put("ip_type", "1");
		paramTypesMap.put("ip", "1");
		paramTypesMap.put("mask", "1");
		paramTypesMap.put("gateway", "1");
		paramTypesMap.put("dns_enab", "4");
		paramTypesMap.put("dns", "1");
	}

	/**
	 * 根据device_id获取WAN连接
	 */
	public String[] getWAN(String device_id)
	{
		if (fromDB)
		{
			WanObj[] wanObj = paramInfoDao.getWAN(device_id);
			if (null == wanObj){
				logger.warn("[{}] 从数据库未取到值",device_id);
				return null;
			}
			
			String[] wan_id_arr = new String[wanObj.length];
			for (int i = 0; i < wanObj.length; i++){
				wan_id_arr[i] = wanObj[i].getWanId();
			}
			return wan_id_arr;
		}
		
		String gather_time = new DateTimeUtil().getLongTime() + "";
		WanObj wanObj;
		List<WanObj> wanObjList = new ArrayList<WanObj>();
		// 从设备上实时去采取
		String wanDevPath = "InternetGatewayDevice.WANDevice.";
		String wanDevSubPath = "";
		String accessTypeValue = "";
		String wan_id = "";
		Map<String, String> accessTypeMap;
		Map<String, String> wanMap = new HashMap<String, String>();
		StringBuffer returnWAN = new StringBuffer();
		String gwType = LipossGlobals.getGw_Type(device_id);
		// 取得WANDevice.下所有的子节点
		Collection collection = WANConnDeviceAct.getParameterNameList(device_id,wanDevPath);
		Iterator iterator = collection.iterator();
		if (collection.size() == 0){
			return null;
		}

		while (iterator.hasNext())
		{
			// InternetGatewayDevice.WANDevice.1.
			wanDevSubPath = (String) iterator.next();
			if (wanDevPath.equals(wanDevSubPath))
			{
				logger.debug("子参数相同，继续...");
				continue;
			}
			wan_id = getLastString(wanDevPath, wanDevSubPath);
			returnWAN.append(wan_id + ",");
			paramTreeObject.setGwType(gwType);
			accessTypeMap = paramTreeObject.getParaValueMap(
						wanDevSubPath + "WANCommonInterfaceConfig.WANAccessType", device_id);
			accessTypeValue = paramTreeObject.getParaVlue(accessTypeMap);
			wanMap.put(wan_id, accessTypeValue);
			wanObj = new WanObj();
			wanObj.setDevId(device_id);
			wanObj.setGatherTime(gather_time);
			wanObj.setWanId(wan_id);
			wanObj.setAccessType(accessTypeValue);
			wanObj.setWanConnNum("1");
			wanObjList.add(wanObj);
		}
		// 设置wanMap
		setAccessTypeMapAll(wanMap);
		
		// 入库
		if (paramInfoDao.setWAN(device_id, convertListToWanObj(wanObjList))){
			logger.warn("[{}] 入库成功！",device_id);
		}else{
			logger.warn("[{}] 入库失败！",device_id);
		}
		return returnWAN.toString().split(",");
	}

	/**
	 * 根据device_id, wan_id获取PVC相关连接对象
	 */
	public PvcObj[] getPvcObj(String device_id, String wan_id)
	{
		if (null == device_id || null == wan_id){
			logger.error("device_id或wan_id为空，返回");
			return null;
		}
		
		if (fromDB)
		{
			PvcObj[] pvcObj = paramInfoDao.getWANConn(device_id, wan_id);
			if (null == pvcObj){
				logger.warn("[{}] 从数据库未取到值",device_id);
				return null;
			}
			return pvcObj;
		}

		PvcObj pvcObj;
		List<PvcObj> pvcObjList = new ArrayList<PvcObj>();
		String wan_conn_id = "";
		String rootPara = "InternetGatewayDevice.WANDevice." + wan_id+ ".WANConnectionDevice.";
		// 取得WANConnectionDevice...下所有的子节点
		Collection collection_1 = WANConnDeviceAct.getParameterNameList(device_id,rootPara);
		if (null == collection_1 || collection_1.size() == 0){
			logger.error("[{}] 取WANConncetionDevice下结点为空，退出",device_id);
			return null;
		}
		
		Iterator iterator_1 = collection_1.iterator();
		String pathWANConn = null;
		String pvcValue = "";
		String vlanIdValue = "";
		String wanPPPConnNumValue = "";
		String wanIPConnNumValue = "";
		Map<String, String> wanIdMap;
		String vpi_id = "";
		String vci_id = "";
		String gather_time = new DateTimeUtil().getLongTime() + "";
		Map<String, String> sessTypeMap = new HashMap<String, String>();
		Map<String, String> paramInfoMap = new HashMap<String, String>();
		wanIdMap = getAccessTypeMapAll();
		if (null == wanIdMap){
			logger.warn("[{}] 连接类型为空，返回null",device_id);
			return null;
		}
		
		while (iterator_1.hasNext())
		{
			// InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.
			pathWANConn = (String) iterator_1.next();
			logger.warn("[{}] WANConnectionDevice下结点路径：{}",device_id,pathWANConn);
			String[] paramArr = new String[3];
			paramArr[0] = pathWANConn + "WANPPPConnectionNumberOfEntries";
			paramArr[1] = pathWANConn + "WANIPConnectionNumberOfEntries";
			if (rootPara.equals(pathWANConn)){
				logger.debug("子参数相同，继续...");
				continue;
			}
			wan_conn_id = getLastString(rootPara, pathWANConn);
			logger.warn("[{}] WANConnectionDevice下实例值：{}",device_id,wan_conn_id);
			if ("DSL".equalsIgnoreCase(wanIdMap.get(wan_id)))
			{
				paramArr[2] = pathWANConn + "WANDSLLinkConfig.DestinationAddress";
				paramInfoMap = paramInfoCorba.getParaValue_multi(paramArr, device_id);
				if (null == paramInfoMap){
					logger.error("[{}] 批量获取PPP参数值失败，继续下一个",device_id);
					continue;
				}
				pvcValue = paramInfoMap.get(paramArr[2]);
				if (null != pvcValue && pvcValue.length() == 8)
				{
					vpi_id = pvcValue.substring(4, 5);
					vci_id = pvcValue.substring(6, 8);
				}
			}
			else if ("Ethernet".equalsIgnoreCase(wanIdMap.get(wan_id)))
			{
				paramArr[2] = pathWANConn + "WANEthernetLinkConfig.X_CT-COM_VLANIDMark";
				paramInfoMap = paramInfoCorba.getParaValue_multi(paramArr, device_id);
				if (null == paramInfoMap)
				{
					logger.error("[{}] 批量获取PPP参数值失败，继续下一个",device_id);
					continue;
				}
				vlanIdValue = paramInfoMap.get(paramArr[2]);
			}
			wanPPPConnNumValue = paramInfoMap.get(paramArr[0]);
			wanIPConnNumValue = paramInfoMap.get(paramArr[1]);
			pvcObj = new PvcObj();
			pvcObj.setDevice_id(device_id);
			pvcObj.setWan_id(wan_id);
			pvcObj.setWan_conn_id(wan_conn_id);
			pvcObj.setGather_time(gather_time);
			pvcObj.setPpp_conn_num(wanPPPConnNumValue);
			pvcObj.setIp_conn_num(wanIPConnNumValue);
			pvcObj.setVpi_id(vpi_id);
			pvcObj.setVci_id(vci_id);
			pvcObj.setVlan_id(vlanIdValue);
			pvcObjList.add(pvcObj);
			sessTypeMap.put(wan_id + wan_conn_id + "wanPPPConnNumValue",wanPPPConnNumValue);
			sessTypeMap.put(wan_id + wan_conn_id + "wanIPConnNumValue", wanIPConnNumValue);
		}
		setSessionTypeMapAll(sessTypeMap);
		PvcObj[] pvcObjArr = convertListToPvcObj(pvcObjList);
		// 入库
		if (paramInfoDao.setWANConn(device_id, wan_id, pvcObjArr)){
			logger.warn("[{}] 入库成功！",device_id);
		}else{
			logger.warn("[{}] 入库失败！",device_id);
		}
		return pvcObjArr;
	}

	/**
	 * 根据device_id, wan_id, wan_conn_id获取PPP或IP连接相关对象
	 */
	public ConnObj[] getConnectionObj(String device_id, String wan_id,
			String wan_conn_id, String wanPPPConnNumValue, String wanIPConnNumValue)
	{
		if (null == device_id || null == wan_id || null == wan_conn_id
				|| null == wanPPPConnNumValue || null == wanIPConnNumValue)
		{
			logger.error("device_id或wan_id或wan_conn_id或wanPPPConnNumValue或wanIPConnNumValue为空，返回");
			return null;
		}
		
		if (fromDB)
		{
			ConnObj[] connObj = paramInfoDao.getWANConnSession(device_id, wan_id,wan_conn_id);
			if (null == connObj)
			{
				logger.warn("[{}] 从数据库未取到值",device_id);
				return null;
			}
			return connObj;
		}

		Map<String, String> sessionTypeMap;
		sessionTypeMap = getSessionTypeMapAll();
		String pppConnNum = "";
		String ipConnNum = "";
		String sessionType = null;
		if (null == sessionTypeMap)
		{
			logger.warn("[{}] session类型为空，从参数中取pppConnNum或ipConnNum",device_id);
			pppConnNum = wanPPPConnNumValue;
			ipConnNum = wanIPConnNumValue;
		}
		else
		{
			pppConnNum = sessionTypeMap.get(wan_id + wan_conn_id + "wanPPPConnNumValue");
			ipConnNum = sessionTypeMap.get(wan_id + wan_conn_id + "wanIPConnNumValue");
		}
		
		if (!"0".equalsIgnoreCase(pppConnNum) && !StringUtil.IsEmpty(pppConnNum)){
			sessionType = "1";
		}else if (!"0".equalsIgnoreCase(ipConnNum) && !StringUtil.IsEmpty(ipConnNum)){
			sessionType = "2";
		}else{
			logger.warn("[{}] 不能确定PPP或IP，返回",device_id);
			return null;
		}
		Map<String, String> paramNamesMap = new HashMap<String, String>();
		ConnObj connObj;
		List<ConnObj> connObjList = new ArrayList<ConnObj>();
		NodeObj nodeObj;
		List<NodeObj> nodeObjList = new ArrayList<NodeObj>();
		String wan_conn_sess_id = "";
		String rootPara = "";
		if ("1".equalsIgnoreCase(sessionType))
		{
			rootPara = "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANPPPConnection.";
		}
		else if ("2".equalsIgnoreCase(sessionType))
		{
			rootPara = "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection.";
		}
		
		// 取得WANConnectionDevice...下所有的子节点
		Collection collection_1 = WANConnDeviceAct.getParameterNameList(device_id,rootPara);
		if (null == collection_1 || collection_1.size() == 0)
		{
			logger.error("[{}] 取WANConncetionDevice下结点为空，退出",device_id);
			return null;
		}
		Iterator iterator_1 = collection_1.iterator();
		String pathSubConn = null;
		String gather_time = new DateTimeUtil().getLongTime() + "";
		while (iterator_1.hasNext())
		{
			// InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.(或WANIPConnection.)
			pathSubConn = (String) iterator_1.next();
			logger.warn("[{}] WANPPPConnection.(或WANIPConnection.)下结点路径：{}",device_id,pathSubConn);
			if (rootPara.equals(pathSubConn))
			{
				logger.debug("子参数相同，继续...");
				continue;
			}
			wan_conn_sess_id = getLastString(rootPara, pathSubConn);
			logger.warn("[{}] WANPPPConnection.(或WANIPConnection.)下值：{}",device_id,wan_conn_sess_id);
			if ("1".equalsIgnoreCase(sessionType))
			{
				String[] paramArr = new String[7];
				paramArr[0] = pathSubConn + "Enable";
				paramArr[1] = pathSubConn + "Name";
				paramArr[2] = pathSubConn + "ConnectionType";
				paramArr[3] = pathSubConn + "X_CT-COM_ServiceList";
				paramArr[4] = pathSubConn + "X_CT-COM_LanInterface";
				paramArr[5] = pathSubConn + "Username";
				paramArr[6] = pathSubConn + "Password";
				paramNamesMap.put(paramArr[0], "enable");
				paramNamesMap.put(paramArr[1], "name");
				paramNamesMap.put(paramArr[2], "conn_type");
				paramNamesMap.put(paramArr[3], "serv_list");
				paramNamesMap.put(paramArr[4], "bind_port");
				paramNamesMap.put(paramArr[5], "username");
				paramNamesMap.put(paramArr[6], "password");
				
				Map<String,String> paramInfoMap=paramInfoCorba.getParaValue_multi(paramArr,device_id);
				if (null == paramInfoMap){
					logger.error("[{}] 批量获取PPP参数值失败，继续下一个",device_id);
					continue;
				}
				
				for (int i = 0; i < paramInfoMap.size(); i++)
				{
					nodeObj = new NodeObj();
					nodeObj.setName(paramNamesMap.get(paramArr[i]));
					nodeObj.setValue(paramInfoMap.get(paramArr[i]));
					nodeObj.setIsModified("0");
					nodeObj.setParamType(paramTypesMap.get(paramNamesMap.get(paramArr[i])));
					nodeObjList.add(nodeObj);
				}
			}
			else if ("2".equalsIgnoreCase(sessionType))
			{
				String[] paramArr = new String[10];
				paramArr[0] = pathSubConn + "Enable";
				paramArr[1] = pathSubConn + "Name";
				paramArr[2] = pathSubConn + "ConnectionType";
				paramArr[3] = pathSubConn + "X_CT-COM_ServiceList";
				paramArr[4] = pathSubConn + "X_CT-COM_LanInterface";
				paramArr[5] = pathSubConn + "AddressingType";
				paramArr[6] = pathSubConn + "ExternalIPAddress";
				paramArr[7] = pathSubConn + "SubnetMask";
				paramArr[8] = pathSubConn + "DefaultGateway";
				paramArr[9] = pathSubConn + "DNSServers";
				paramNamesMap.put(paramArr[0], "enable");
				paramNamesMap.put(paramArr[1], "name");
				paramNamesMap.put(paramArr[2], "conn_type");
				paramNamesMap.put(paramArr[3], "serv_list");
				paramNamesMap.put(paramArr[4], "bind_port");
				paramNamesMap.put(paramArr[5], "ip_type");
				paramNamesMap.put(paramArr[6], "ip");
				paramNamesMap.put(paramArr[7], "mask");
				paramNamesMap.put(paramArr[8], "gateway");
				paramNamesMap.put(paramArr[9], "dns");
				
				Map<String,String> paramInfoMap=paramInfoCorba.getParaValue_multi(paramArr,device_id);
				if (null == paramInfoMap){
					logger.error("[{}] 批量获取IP参数值失败，继续下一个",device_id);
					continue;
				}
				
				for (int i = 0; i < paramInfoMap.size(); i++)
				{
					nodeObj = new NodeObj();
					nodeObj.setName(paramNamesMap.get(paramArr[i]));
					nodeObj.setValue(paramInfoMap.get(paramArr[i]));
					nodeObj.setIsModified("0");
					nodeObj.setParamType(paramTypesMap.get(paramNamesMap.get(paramArr[i])));
					nodeObjList.add(nodeObj);
				}
				// 开始：dns_enab单独设置，固定值，因为有些设备无此结点
				nodeObj = new NodeObj();
				nodeObj.setName("dns_enab");
				nodeObj.setValue("1");
				nodeObj.setIsModified("0");
				nodeObj.setParamType("4");
				nodeObjList.add(nodeObj);
			}
			connObj = new ConnObj();
			connObj.setDevice_id(device_id);
			connObj.setWan_id(wan_id);
			connObj.setGather_time(gather_time);
			connObj.setWan_conn_id(wan_conn_id);
			connObj.setWan_conn_sess_id(wan_conn_sess_id);
			connObj.setSess_type(sessionType);
			connObj.setNodeObj(convertListToNodeObj(nodeObjList));
			connObjList.add(connObj);
		}
		ConnObj[] connObjArr = convertListToConnObj(connObjList);
		// 入库
		logger.debug("数据入库");
		if (paramInfoDao.setWANConnSession(device_id, wan_id, wan_conn_id, connObjArr)){
			logger.warn("[{}] 入库成功！",device_id);
		}else{
			logger.warn("[{}] 入库失败！",device_id);
		}
		return connObjArr;
	}

	/**
	 * 获取设备WLAN的所有连接及参数
	 */
	public WlanObj[] getWlanObj(String device_id)
	{
		logger.warn("[{}] getWlanObj({})",device_id,instArea);
		if (null == device_id)
		{
			logger.error("device_id为空，返回");
			return null;
		}
		vendorId=wlanConfDao.getDevVendorId(device_id);
		
		if (fromDB)
		{
			WlanObj[] wlanObj = wlanConfDao.getWlan(device_id);
			if (null == wlanObj){
				logger.warn("[{}] 从数据库未取到值",device_id);
				return null;
			}
			
			return wlanObj;
		}
		
		WlanObj wlanObj;
		List<WlanObj> wlanObjList = new ArrayList<WlanObj>();
		String gather_time = new DateTimeUtil().getLongTime() + "";
		String rootPara = "InternetGatewayDevice.LANDevice.";
		// 取得WANConnectionDevice.下所有的子节点
		Collection collection_1 = WANConnDeviceAct.getParameterNameList(device_id,rootPara);
		if (null == collection_1 || collection_1.size() == 0)
		{
			logger.error("[{}] 未取到LANDevice.下的节点，返回",device_id);
			return null;
		}
		Iterator iterator_1 = collection_1.iterator();
		Collection collection_2 = null;
		Iterator iterator_2 = null;
		String lanDevConn = null;
		String wlanDevConn = null;
		String lan_id = null;
		String wlan_id = null;
		String[] paramArr_1 = null;
		String[] paramArr_2 = null;
		StringBuffer paramSb = null;
		while (iterator_1.hasNext())
		{
			// InternetGatewayDevice.LANDevice.1.
			lanDevConn = (String) iterator_1.next();
			logger.warn("[{}] InternetGatewayDevice.LANDevice.下结点：{}",device_id,lanDevConn);
			collection_2 = WANConnDeviceAct.getParameterNameList(device_id, lanDevConn+ "WLANConfiguration.");
			if (null == collection_2 || collection_2.size() == 0)
			{
				logger.error("[{}] 未取到{}WLANConfiguration.下的节点，继续下一个",device_id,lanDevConn);
				continue;
			}
			if (rootPara.equals(lanDevConn))
			{
				continue;
			}
			// 获得lan_id
			lan_id = getLastString(rootPara, lanDevConn);
			iterator_2 = collection_2.iterator();
			while (iterator_2.hasNext())
			{
				paramSb = new StringBuffer();
				// InternetGatewayDevice.LANDevice.1.WLANConfiguration.1
				wlanDevConn = (String) iterator_2.next();
				logger.warn("[{}] {}.WLANConfiguration.下结点：{}", device_id,lanDevConn, wlanDevConn);
				if ((lanDevConn + "WLANConfiguration.").equals(wlanDevConn))
				{
					logger.debug("子参数相同，继续...");
					continue;
				}
				// 获得wlan_id
				wlan_id = getLastString(lanDevConn + "WLANConfiguration.", wlanDevConn);
				if(Global.XJDX.equals(instArea)){
					paramArr_1 = new String[11];
				}else{
					paramArr_1 = new String[9];
				}
				
				paramArr_1[0] = wlanDevConn + "Enable";
				paramArr_1[1] = wlanDevConn + "SSID";
				paramArr_1[2] = wlanDevConn + "Standard";
				paramArr_1[3] = wlanDevConn + "BeaconType";
				paramArr_1[4] = wlanDevConn + "Channel";
				paramArr_1[5] = wlanDevConn + "X_CT-COM_SSIDHide";
				paramArr_1[6] = wlanDevConn + "X_CT-COM_Powerlevel";
				paramArr_1[7] = wlanDevConn + "X_CT-COM_PowerValue";
				paramArr_1[8] = wlanDevConn + "X_CT-COM_APModuleEnable";
				if(Global.XJDX.equals(instArea)){
					paramArr_1[9] = wlanDevConn + "X_CT-COM_WPSKeyWord";
					paramArr_1[10] = wlanDevConn + "Status";
				}
				Map<String, String> paramInfoMap = paramInfoCorba.getParaValue_multi(paramArr_1, device_id);
				if (null == paramInfoMap)
				{
					logger.error("[{}] 第一次批量获取WLAN参数值失败，返回null",device_id);
					return null;
				}
				
				wlanObj = new WlanObj();
				wlanObj.setDeviceId(device_id);
				wlanObj.setLanId(lan_id);
				wlanObj.setLanWlanId(wlan_id);
				wlanObj.setEnable(paramInfoMap.get(paramArr_1[0]));
				wlanObj.setSsid(paramInfoMap.get(paramArr_1[1]));
				wlanObj.setStandard(paramInfoMap.get(paramArr_1[2]));
				
				String beacontType=paramInfoMap.get(paramArr_1[3]);
				if(Global.XJDX.equals(instArea))
				{
					if("Basic".equalsIgnoreCase(beacontType)){
						beacontType="WEP";
					}else if("WPA".equalsIgnoreCase(beacontType)){
						beacontType="WPA-PSK";
					}else if("None".equalsIgnoreCase(beacontType)){
						beacontType="None";
					}
					
					if(VENDORID_HW.equals(vendorId) || VENDORID_ZX.equals(vendorId)){
						if("WPA2".equalsIgnoreCase(beacontType)){
							beacontType="WPA2-PSK";
						}else if("WPA/WPA2".equalsIgnoreCase(beacontType)){
							beacontType="WPA-PSK/WPA2-PSK";
						}
					}else{
						if("11i".equalsIgnoreCase(beacontType)){
							beacontType="WPA2-PSK";
						}else if("WPAand11i".equalsIgnoreCase(beacontType)){
							beacontType="WPA-PSK/WPA2-PSK";
						}
					}
				}
				
				
				wlanObj.setBeacontType(beacontType);
				wlanObj.setChannel(paramInfoMap.get(paramArr_1[4]));
				wlanObj.setRadioEnable("0");
				wlanObj.setHide(paramInfoMap.get(paramArr_1[5]));
				wlanObj.setPowerLevel(paramInfoMap.get(paramArr_1[6]));
				wlanObj.setPowerValue(paramInfoMap.get(paramArr_1[7]));
				wlanObj.setApEnable(paramInfoMap.get(paramArr_1[8]));
				if(Global.XJDX.equals(instArea)){
					wlanObj.setWpsKeyWord(paramInfoMap.get(paramArr_1[9]));
					wlanObj.setStatus(paramInfoMap.get(paramArr_1[10]));
				}
				wlanObj.setGatherTime(gather_time);
				
				String beacontType1=paramInfoMap.get(paramArr_1[3]);
				if (null == beacontType1
						|| null == paramInfoMap.get(paramArr_1[4]))
				{
					logger.error("[{}] BeaconType或Channel为空，返回null",device_id);
					return null;
				}
				if ("None".equalsIgnoreCase(beacontType1))
				{
					wlanObj.setBasicAuthMode("OpenSystem");
				}
				else if ("Basic".equalsIgnoreCase(beacontType1))
				{
					wlanObj.setBasicAuthMode("Both");
					if(Global.XJDX.equals(instArea)){
						paramSb.append("BasicAuthenticationMode,WEPKeyIndex,WEPKey.1.WEPKey,");
					}else{
						paramSb.append("WEPKeyIndex,WEPEncryptionLevel,WEPKey.1.WEPKey,");
					}
				}
				else if ("WPA".equalsIgnoreCase(beacontType1))
				{
					paramSb.append("WPAEncryptionModes,IEEE11iAuthenticationMode,WPAAuthenticationMode,PreSharedKey.1.PreSharedKey,");
				}
				else if(Global.XJDX.equals(instArea))
				{
					if(VENDORID_HW.equals(vendorId) || VENDORID_ZX.equals(vendorId)){
						if("WPA2".equalsIgnoreCase(beacontType1) 
								|| "WPA/WPA2".equalsIgnoreCase(beacontType1)){
							paramSb.append("WPAEncryptionModes,IEEE11iAuthenticationMode,WPAAuthenticationMode,PreSharedKey.1.PreSharedKey,");
						}
					}else{
						if("11i".equalsIgnoreCase(beacontType1) 
								|| "WPAand11i".equalsIgnoreCase(beacontType1)){
							paramSb.append("WPAEncryptionModes,IEEE11iAuthenticationMode,WPAAuthenticationMode,PreSharedKey.1.PreSharedKey,");
						}
					}
				}
				else
				{
					logger.warn("[{}] BeaconType值[{}]不可识别，返回null",device_id,beacontType1);
					return null;
				}
				
				if(Global.XJDX.equals(instArea)){
					paramSb.append("WEPEncryptionLevel,");
				}
				
				paramArr_2 = paramSb.toString().split(",");
				for (int i = 0; i < paramArr_2.length; i++){
					paramArr_2[i] = wlanDevConn + paramArr_2[i];
					logger.warn("[{}] 第二次需要获取的参数：{}",device_id,paramArr_2[i]);
				}
				
				Map<String, String> paramInfoMap2 = paramInfoCorba.getParaValue_multi(paramArr_2, device_id);
				if (null == paramInfoMap2){
					logger.error("[{}] 第二次批量获取WLAN参数值失败，返回null",device_id);
					return null;
				}
				
				wlanObj.setWepKeyId("1");
				wlanObj.setWepEncrLevel(paramInfoMap2.get(wlanDevConn+ "WEPEncryptionLevel"));
				wlanObj.setWepKey(paramInfoMap2.get(wlanDevConn + "WEPKey.1.WEPKey"));
				wlanObj.setWpaEncrMode(paramInfoMap2.get(wlanDevConn+ "WPAEncryptionModes"));
				wlanObj.setWpaAuthMode(paramInfoMap2.get(wlanDevConn+ "WPAAuthenticationMode"));
				wlanObj.setWpaKey(paramInfoMap2.get(wlanDevConn+ "PreSharedKey.1.PreSharedKey"));
				wlanObj.setPossChannel(paramInfoMap2.get(wlanDevConn + "PossibleChannels"));
				wlanObj.setChannelInUse(paramInfoMap.get(paramArr_1[4]));
				
				wlanObjList.add(wlanObj);
				
				paramInfoMap.clear();
				paramInfoMap = null;
				paramInfoMap2.clear();
				paramInfoMap2 = null;
				paramSb = null;
				wlanObj = null;
			}
		}
		
		WlanObj[] wlanObjArr = convertListToWlanObj(wlanObjList);
		if (wlanConfDao.setWlan(device_id, wlanObjArr)){
			logger.warn("[{}] insert into gw_lan_wlan 入库成功！",device_id);
		}else{
			logger.warn("[{}] insert into gw_lan_wlan 入库失败！",device_id);
		}

		wlanObjList.clear();
		wlanObjList = null;
		return wlanObjArr;
	}

	/**
	 * @param accType 是设置PVC或VLANID 0: PVC 1: VLANID
	 * @param sessType 是设置PPPConnection或IPConnection 1: PPP 2: IP
	 */
	public boolean AddConn(PvcObj pvcObj, ConnObj connObj, int accType, int sessType)
	{
		if (null == pvcObj || null == connObj)
		{
			logger.error("pvcObj为null或connObj为空，返回");
			return false;
		}
		String device_id = pvcObj.getDevice_id();
		if (null == device_id)
		{
			logger.error("device_id为空，返回");
			return false;
		}
		
		String pvc = "";
		String vlanid = "";
		String wan_id = pvcObj.getWan_id();
		if (null == wan_id)
		{
			logger.error("wan_id为空，返回");
			return false;
		}
		String pvcDB = null;
		String wanConnIdDB = null;
		PvcObj[] pvcObjDB = paramInfoDao.getWANConn(device_id, wan_id);
		if (null == pvcObjDB)
		{
			logger.warn("[{}] 从数据库未取到值",device_id);
			return false;
		}
		for (PvcObj po : pvcObjDB)
		{
			pvcDB = "PVC:" + po.getVpi_id() + "/" + po.getVci_id();
			if (pvcDB.equalsIgnoreCase("PVC:" + pvcObj.getVpi_id() + "/"
					+ pvcObj.getVci_id()))
			{
				// 如果要设置的PVC在数据库中已存在
				wanConnIdDB = po.getWan_conn_id();
			}
		}
		StringBuilder paramNames = new StringBuilder();
		StringBuilder paramValues = new StringBuilder();
		StringBuilder paramTypes = new StringBuilder();
		String[] params_name = null;
		String[] params_value = null;
		String[] para_type_id = null;
		StringBuilder wanConnDevPathSB = new StringBuilder().append(
				"InternetGatewayDevice.WANDevice.").append(wan_id).append(
				".WANConnectionDevice.");
		String wanConnDevPath = wanConnDevPathSB.toString();
		int wanConn_i = 0;
		String gather_time = new DateTimeUtil().getLongTime() + "";
		if (null == wanConnIdDB)
		{
			logger.debug("该PVC在数据库中不存在，开始增加连接");
			// InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.
			/** 1)增加WANConnectionDevice下的结点，返回结点的i值 */
			logger.debug("开始增加WANConnectionDevice下的结点");
			logger.debug("增加WANConnectionDevice下的结点，路径={}", wanConnDevPath);
			wanConn_i = paramInfoCorba.addPara(wanConnDevPath, device_id);
			wanConnDevPathSB = null;
			if (0 == wanConn_i)
			{
				logger.error("新增失败，返回");
				return false;
			}
			pvcObj.setWan_conn_id(String.valueOf(wanConn_i));
			pvcObj.setGather_time(gather_time);
			logger.debug("增加WANConnectionDevice下的结点成功");
			/** 2)设置WANConnectionDevice.i下的参数值 */
			logger.debug("开始设置WANConnectionDevice.i参数值");
			paramNames.append(wanConnDevPath + wanConn_i + ".WANDSLLinkConfig.LinkType|");
			paramValues.append("EoA|");
			paramTypes.append("1|");
			paramNames.append(wanConnDevPath + wanConn_i + ".WANDSLLinkConfig.Enable|");
			paramValues.append("1|");
			paramTypes.append("4|");
			if (0 == accType)
			{
				String vpi = pvcObj.getVpi_id();
				String vci = pvcObj.getVci_id();
				StringBuilder pvcsb = new StringBuilder();
				pvc = pvcsb.append("PVC:").append(vpi).append("/").append(vci).toString();
				paramNames.append(wanConnDevPath + wanConn_i
						+ ".WANDSLLinkConfig.DestinationAddress");
				paramValues.append(pvc);
				paramTypes.append("1");
			}
			else if (1 == accType)
			{
				vlanid = pvcObj.getVlan_id();
				paramNames.append(wanConnDevPath + wanConn_i
						+ ".WANEthernetLinkConfig.COM_VLANIDMark");
				paramValues.append(vlanid);
				paramTypes.append("1");
			}
			else
			{
				logger.error("[{}] accType[{}]不可识别，返回",device_id,accType);
				paramNames = null;
				paramValues = null;
				paramTypes = null;
				return false;
			}
			params_name = paramNames.toString().split("\\|");
			params_value = paramValues.toString().split("\\|");
			para_type_id = paramTypes.toString().split("\\|");
			
			if (!paramInfoCorba.setParamValue_multi(params_name, params_value,
					para_type_id, device_id))
			{
				// 设置失败
				logger.error("[{}] 设置WANConnectionDevice下参数失败，返回",device_id);
				paramNames = null;
				paramValues = null;
				paramTypes = null;
				return false;
			}
			paramNames = null;
			paramValues = null;
			paramTypes = null;
			params_name = null;
			params_value = null;
			para_type_id = null;
		}
		else
		{
			wanConn_i = Integer.parseInt(wanConnIdDB);
		}
		/** 3)增加WANPPPConnectionDevice或WANIPConnectionDevice下的结点，返回结点的i值 */
		String wanConnPath = "";
		if (1 == sessType){
			wanConnPath = wanConnDevPath + wanConn_i + ".WANPPPConnection.";
		}else if (2 == sessType){
			wanConnPath = wanConnDevPath + wanConn_i + ".WANIPConnection.";
		}else{
			logger.error("[{}] sessType[{}]不可识别，返回",device_id, sessType);
			return false;
		}
		int wanIPorPPPConn_i = paramInfoCorba.addPara(wanConnPath, device_id);
		if (0 == wanIPorPPPConn_i)
		{
			logger.error("[{}] 新增失败，返回",device_id);
			return false;
		}
		connObj.setWan_conn_id(String.valueOf(wanConn_i));
		connObj.setWan_conn_sess_id(String.valueOf(wanIPorPPPConn_i));
		connObj.setGather_time(gather_time);
		logger.debug("增加PPP或IP下结点成功");
		/** 4)设置WANPPPConnectionDevice或WANIPConnectionDevice.i下的参数值 */
		paramNames = new StringBuilder();
		paramValues = new StringBuilder();
		paramTypes = new StringBuilder();
		String enable = null;
		String connType = null;
		String servList = null;
		String bindPort = null;
		String username = null;
		String password = null;
		String ipType = null;
		String ip = null;
		String mask = null;
		String gateway = null;
		String dns = null;
		NodeObj[] nodeObj = connObj.getNodeObj();
		if (null == nodeObj || nodeObj.length <= 0)
		{
			logger.error("[{}] nodeObj为空，返回",device_id);
			return false;
		}
		for (NodeObj no : nodeObj)
		{
			if ("enable".equalsIgnoreCase(no.getName())){
				enable = no.getValue();
				no.setParamType("4"); // 再次设置其类型，入库用
			}else if ("conn_type".equalsIgnoreCase(no.getName())){
				connType = no.getValue();
			}else if ("serv_list".equalsIgnoreCase(no.getName())){
				servList = no.getValue();
			}else if ("bind_port".equalsIgnoreCase(no.getName())){
				bindPort = no.getValue();
			}else if ("username".equalsIgnoreCase(no.getName())){
				username = no.getValue();
			}else if ("password".equalsIgnoreCase(no.getName())){
				password = no.getValue();
			}else if ("ip_type".equalsIgnoreCase(no.getName())){
				ipType = no.getValue();
			}else if ("ip".equalsIgnoreCase(no.getName())){
				ip = no.getValue();
			}else if ("mask".equalsIgnoreCase(no.getName())){
				mask = no.getValue();
			}else if ("gateway".equalsIgnoreCase(no.getName())){
				gateway = no.getValue();
			}else if ("dns".equalsIgnoreCase(no.getName())){
				dns = no.getValue();
			}
		}
		
		if (!StringUtil.IsEmpty(bindPort))
		{
			paramNames.append(wanConnPath + wanIPorPPPConn_i + ".X_CT-COM_LanInterface|");
			paramValues.append(bindPort + "|");
			paramTypes.append("1|");
		}
		
		if (!StringUtil.IsEmpty(servList))
		{
			paramNames.append(wanConnPath + wanIPorPPPConn_i + ".X_CT-COM_ServiceList|");
			paramValues.append(servList + "|");
			paramTypes.append("1|");
		}
		if (!StringUtil.IsEmpty(enable))
		{
			paramNames.append(wanConnPath + wanIPorPPPConn_i + ".Enable|");
			paramValues.append(enable + "|");
			paramTypes.append("4|");
		}
		if (1 == sessType)
		{
			// PPP
			if ("PPPoE_Bridged".equalsIgnoreCase(connType))
			{
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".ConnectionType|");
				paramValues.append("PPPoE_Bridged|");
				paramTypes.append("1|");
			}
			else if ("IP_Routed".equalsIgnoreCase(connType))
			{
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".ConnectionType|");
				paramValues.append("IP_Routed|");
				paramTypes.append("1|");
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".Username|");
				paramValues.append(username + "|");
				paramTypes.append("1|");
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".Password|");
				paramValues.append(password + "|");
				paramTypes.append("1|");
			}
			else
			{
				logger.error("[{}] connType[{}]不可识别，返回",device_id,connType);
				return false;
			}
		}
		else if (2 == sessType)
		{
			paramNames.append(wanConnPath + wanIPorPPPConn_i + ".ConnectionType|");
			paramValues.append("IP_Routed|");
			paramTypes.append("1|");
			if ("Static".equalsIgnoreCase(ipType))
			{
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".AddressingType|");
				paramValues.append("Static|");
				paramTypes.append("1|");
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".ExternalIPAddress|");
				paramValues.append(ip + "|");
				paramTypes.append("1|");
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".SubnetMask|");
				paramValues.append(mask + "|");
				paramTypes.append("1|");
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".DefaultGateway|");
				paramValues.append(gateway + "|");
				paramTypes.append("1|");
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".DNSServers|");
				paramValues.append(dns + "|");
				paramTypes.append("1|");
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".DNSEnabled|");
				paramValues.append("1|");
				paramTypes.append("4|");
			}
			else if ("DHCP".equalsIgnoreCase(ipType))
			{
				paramNames.append(wanConnPath + wanIPorPPPConn_i + ".AddressingType|");
				paramValues.append("DHCP|");
				paramTypes.append("1|");
			}
			else
			{
				logger.error("[{}] ipType[{}]不可识别，返回 ",device_id,ipType);
				return false;
			}
		}
		else
		{
			logger.error("[{}] sessType[{}]不可识别，返回",device_id, sessType);
			paramNames = null;
			paramValues = null;
			paramTypes = null;
			wanConnDevPathSB = null;
			return false;
		}
		
		params_name = paramNames.toString().split("\\|");
		params_value = paramValues.toString().split("\\|");
		para_type_id = paramTypes.toString().split("\\|");
		
		if (!paramInfoCorba.setParamValue_multi(params_name, params_value, para_type_id,device_id))
		{
			logger.error("[{}] 设置WANConnectionDevice下参数失败，返回",device_id);
			paramNames = null;
			paramValues = null;
			paramTypes = null;
			return false;
		}
		
		if (paramInfoDao.addWan(pvcObj, connObj)){
			logger.warn("[{}] 操作数据库成功！",device_id);
		}else{
			logger.warn("[{}] 操作数据库失败！",device_id);
		}
		
		paramNames = null;
		paramValues = null;
		paramTypes = null;
		params_name = null;
		params_value = null;
		para_type_id = null;
		return true;
	}

	/**
	 * 增加WLAN结点
	 */
	public boolean AddWlan(WlanObj wlanObj)
	{
		if (null == wlanObj)
		{
			logger.error("wlanObj为null，返回");
			return false;
		}
		String device_id = wlanObj.getDeviceId();
		if (null == device_id)
		{
			logger.error("device_id为空，返回");
			return false;
		}

		String lan_id = wlanObj.getLanId();
		if (null == lan_id)
		{
			logger.error("[{}] lan_id为空，默认设置为1",device_id);
			// 如果为空，则默认为1
			lan_id = "1";
		}
		String gather_time = new DateTimeUtil().getLongTime() + "";
		StringBuilder wlanDevPathSB = new StringBuilder().append(
				"InternetGatewayDevice.LANDevice.").append(lan_id).append(
				".WLANConfiguration.");
		String wlanDevPath = wlanDevPathSB.toString();
		int wlan_i = 0;
		/** 1)增加WLANConfiguration下的结点，返回结点的i值 */
		logger.debug("开始增加WLANConfiguration下的结点");
		wlan_i = paramInfoCorba.addPara(wlanDevPath, device_id);
		wlanDevPathSB = null;
		if (0 == wlan_i)
		{
			logger.error("新增失败，返回");
			return false;
		}
		logger.debug("增加WLANConfiguration下的结点成功");
		/** 2)设置WLANConfiguration.i下的参数值 */
		logger.debug("开始设置WLANConfiguration.i参数值");
		// 调用更新方法，全部设置所有参数
		wlanObj.setLanId(lan_id);
		wlanObj.setLanWlanId(String.valueOf(wlan_i));
		// 页面没 有设置，直接设为可用
		wlanObj.setEnable("1");
		wlanObj.setGatherTime(gather_time);
		wlanObj.setWepKeyId("1");
		boolean updateWlanResult = updateWlanConnection(wlanObj);
		if (!updateWlanResult)
		{
			logger.error("[{}] 设置WLAN参数失败，返回",device_id);
			return false;
		}
		// 入数据库
		if (wlanConfDao.addWlan(wlanObj)){
			logger.warn("[{}] 操作数据库成功！",device_id);
		}else{
			logger.warn("[{}] 操作数据库失败！",device_id);
		}

		return true;
	}

	/**
	 * 删除WLAN连接
	 */
	public boolean deleteWlanConnection(String device_id, String lan_id, String lan_wan_id,String gwType)
	{
		if (null == device_id || null == lan_id || null == lan_wan_id)
		{
			logger.error("device_id或lan_id或lan_wan_id为空，返回");
			return false;
		}

		String paraPath = "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wan_id + ".";
		logger.warn("[{}] 删除的路径：{}", device_id,paraPath);
		paramTreeObject.setGwType(gwType);
		int result = paramTreeObject.delPara(paraPath, device_id);
		if (result == 1)
		{
			if (wlanConfDao.delWlan(device_id, lan_id, lan_wan_id)){
				logger.warn("[{}] 操作数据库成功！",device_id);
			}else{
				logger.warn("[{}] 操作数据库失败！",device_id);
			}
			return true;
		}
		
		return false;
	}

	/**
	 * 删除PPP或IP连接
	 */
	public boolean deleteConnection(String device_id, String wan_id, String wan_conn_id,
			String sessionType, String wan_conn_sess_id)
	{
		if (null == device_id || null == wan_id || null == wan_conn_id
				|| null == sessionType || null == wan_conn_sess_id)
		{
			logger.error("device_id或wan_id或wan_conn_id或sessionType或wan_conn_sess_id为空，返回");
			return false;
		}

		String sessTypeStr = "";
		if ("1".equalsIgnoreCase(sessionType)){
			sessTypeStr = "WANPPPConnection";
		}else if ("2".equalsIgnoreCase(sessionType)){
			sessTypeStr = "WANIPConnection";
		}else{
			logger.error("[{}] sessionType不可识别，返回 sessionType={}",device_id, sessionType);
			return false;
		}
		String paraPath = "InternetGatewayDevice.WANDevice." + wan_id
				+ ".WANConnectionDevice." + wan_conn_id + "." + sessTypeStr + "."
				+ wan_conn_sess_id + ".";
		logger.debug("删除的路径：{}", paraPath);
		int result = paramTreeObject.delPara(paraPath, device_id);
		if (result == 1)
		{
			if (paramInfoDao.delWANConnSession(device_id, wan_id, wan_conn_id,
					wan_conn_sess_id, sessionType)){
				logger.warn("[{}] 操作数据库成功！",device_id);
			}else{
				logger.warn("[{}] 操作数据库失败！",device_id);
			}
			return true;
		}
		
		return false;
	}

	/**
	 * 删除PVC
	 */
	public boolean deleteConnection(String device_id, String wan_id, String wan_conn_id)
	{
		if (null == device_id || null == wan_id || null == wan_conn_id)
		{
			logger.error("device_id或wan_id或wan_conn_id为空，返回");
			return false;
		}

		String paraPath = "InternetGatewayDevice.WANDevice." + wan_id
				+ ".WANConnectionDevice." + wan_conn_id + ".";
		logger.debug("删除的路径：{}", paraPath);
		int result = paramTreeObject.delPara(paraPath, device_id);
		if (result == 1)
		{
			if (paramInfoDao.delWANConn(device_id, wan_id, wan_conn_id)){
				logger.warn("[{}] 操作数据库成功！",device_id);
			}else{
				logger.warn("[{}] 操作数据库失败！",device_id);
			}
			return true;
		}
		
		return false;
	}

	/**
	 * 更新WLAN相关参数
	 */
	public boolean updateWlanConnection(WlanObj wlanObj)
	{
		if (null == wlanObj)
		{
			logger.error("传入的WlanObj为空，返回");
			return false;
		}
		String device_id = wlanObj.getDeviceId();
		String lan_id = wlanObj.getLanId();
		String lan_wlan_id = wlanObj.getLanWlanId();

		Map<String, String> paramNamesMap = new HashMap<String, String>();
		paramNamesMap.put("ap_enable", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".X_CT-COM_APModuleEnable");
		paramNamesMap.put("powerlevel", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".X_CT-COM_Powerlevel");
		paramNamesMap.put("powervalue", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".X_CT-COM_PowerValue");
		paramNamesMap.put("enable", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".Enable");
		paramNamesMap.put("ssid", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".SSID");
		paramNamesMap.put("standard", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".Standard");
		paramNamesMap.put("beacontype", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".BeaconType");
		paramNamesMap.put("basic_auth_mode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".BasicAuthenticationMode");
		paramNamesMap.put("wep_encr_level", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WEPEncryptionLevel");
		paramNamesMap.put("wep_key_id", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WEPKeyIndex");
		paramNamesMap.put("wep_key", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WEPKey.1.WEPKey");
		paramNamesMap.put("wpa_auth_mode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WPAAuthenticationMode");
		paramNamesMap.put("wpa_encr_mode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WPAEncryptionModes");
		paramNamesMap.put("wpa_key", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".PreSharedKey.1.PreSharedKey");
		paramNamesMap.put("radio_enable", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".RadioEnabled");
		paramNamesMap.put("hide", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".X_CT-COM_SSIDHide");
		paramNamesMap.put("poss_channel", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".PossibleChannels");
		paramNamesMap.put("channel", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".Channel");
		paramNamesMap.put("channel_in_use", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".ChannelsInUse");
		StringBuffer modifiedParamNames = new StringBuffer();
		StringBuffer modifiedParamNamesForDB = new StringBuffer();
		StringBuffer modifiedParamValues = new StringBuffer();
		StringBuffer modifiedParamTypes = new StringBuffer();
		// 从数据库中获取对象
		WlanObj wlanObjFromDB = wlanConfDao.getWlan(device_id, lan_id, lan_wlan_id);
		if (null == wlanObjFromDB)
		{
			logger.error("[{}] 该设备在数据库中不存在记录，全部更新",device_id);
			if (null != wlanObj.getApEnable())
			{
				modifiedParamNames.append(paramNamesMap.get("ap_enable")).append("|");
				modifiedParamNamesForDB.append("ap_enable|");
				modifiedParamValues.append(wlanObj.getApEnable()).append("|");
				modifiedParamTypes.append("4").append("|");
			}
			if (null != wlanObj.getPowerLevel())
			{
				modifiedParamNames.append(paramNamesMap.get("powerlevel")).append("|");
				modifiedParamNamesForDB.append("powerlevel|");
				modifiedParamValues.append(wlanObj.getPowerLevel()).append("|");
				modifiedParamTypes.append("3").append("|");
			}

			if (null != wlanObj.getSsid())
			{
				modifiedParamNames.append(paramNamesMap.get("ssid")).append("|");
				modifiedParamNamesForDB.append("ssid|");
				modifiedParamValues.append(wlanObj.getSsid()).append("|");
				modifiedParamTypes.append("1").append("|");
			}
			
			if (null != wlanObj.getStandard())
			{
				modifiedParamNames.append(paramNamesMap.get("standard")).append("|");
				modifiedParamNamesForDB.append("standard|");
				modifiedParamValues.append(wlanObj.getStandard()).append("|");
				modifiedParamTypes.append("1").append("|");
			}
			
			if (null != wlanObj.getBeacontType())
			{
				modifiedParamNames.append(paramNamesMap.get("beacontype")).append("|");
				modifiedParamNamesForDB.append("beacontype|");
				modifiedParamValues.append(wlanObj.getBeacontType()).append("|");
				modifiedParamTypes.append("1").append("|");
			}
			
			if (null != wlanObj.getChannel())
			{
				modifiedParamNames.append(paramNamesMap.get("channel")).append("|");
				modifiedParamNamesForDB.append("channel|");
				modifiedParamValues.append(wlanObj.getChannel()).append("|");
				modifiedParamTypes.append("3").append("|");
			}

			if (null != wlanObj.getHide())
			{
				modifiedParamNames.append(paramNamesMap.get("hide")).append("|");
				modifiedParamNamesForDB.append("hide|");
				modifiedParamValues.append(wlanObj.getHide()).append("|");
				modifiedParamTypes.append("4").append("|");
			}

			if ("None".equalsIgnoreCase(wlanObj.getBeacontType()))
			{
				// BasicAuthenticationMode为OpenSystem
				logger.debug("BasicAuthenticationMode为OpenSystem");
				modifiedParamNames.append(paramNamesMap.get("basic_auth_mode")).append("|");
				modifiedParamNamesForDB.append("basic_auth_mode|");
				modifiedParamValues.append("OpenSystem|");
				modifiedParamTypes.append("1").append("|");
			}
			else if ("Basic".equalsIgnoreCase(wlanObj.getBeacontType()))
			{
				// BasicAuthenticationMode为Both
				logger.debug("BasicAuthenticationMode为Both");
				modifiedParamNames.append(paramNamesMap.get("basic_auth_mode")).append("|");
				modifiedParamNamesForDB.append("basic_auth_mode|");
				modifiedParamValues.append("Both|");
				modifiedParamTypes.append("1").append("|");
				modifiedParamNames.append(paramNamesMap.get("wep_encr_level")).append("|");
				modifiedParamNamesForDB.append("wep_encr_level|");
				modifiedParamValues.append("40-bit|");
				modifiedParamTypes.append("1").append("|");
				if (null != wlanObj.getWepKey())
				{
					modifiedParamNames.append(paramNamesMap.get("wep_key")).append("|");
					modifiedParamNamesForDB.append("wep_key|");
					modifiedParamValues.append(wlanObj.getWepKey()).append("|");
					modifiedParamTypes.append("1").append("|");
				}
			}
			else if ("WPA".equalsIgnoreCase(wlanObj.getBeacontType()))
			{
				logger.debug("BeaconType值为WPA");
				if (null != wlanObj.getWpaAuthMode())
				{
					modifiedParamNames.append(paramNamesMap.get("wpa_auth_mode")).append("|");
					modifiedParamNamesForDB.append("wpa_auth_mode|");
					modifiedParamValues.append(wlanObj.getWpaAuthMode()).append("|");
					modifiedParamTypes.append("1").append("|");
				}
				if (null != wlanObj.getWpaEncrMode())
				{
					modifiedParamNames.append(paramNamesMap.get("wpa_encr_mode")).append("|");
					modifiedParamNamesForDB.append("wpa_encr_mode|");
					modifiedParamValues.append(wlanObj.getWpaEncrMode()).append("|");
					modifiedParamTypes.append("1").append("|");
				}
				if (null != wlanObj.getWpaKey())
				{
					modifiedParamNames.append(paramNamesMap.get("wpa_key")).append("|");
					modifiedParamNamesForDB.append("wpa_key|");
					modifiedParamValues.append(wlanObj.getWpaKey()).append("|");
					modifiedParamTypes.append("1").append("|");
				}
			}
			else
			{
				logger.warn("[{}] BeaconType值不可识别：{}", device_id,wlanObj.getBeacontType());
			}
			
			if ("0".equalsIgnoreCase(wlanObj.getChannel()))
			{
				logger.debug("Channel为0，需要读ChannelsInUse结点");
				if (null != wlanObj.getChannelInUse())
				{
					modifiedParamNames.append(paramNamesMap.get("channel_in_use"))
							.append("|");
					modifiedParamNamesForDB.append("channel_in_use|");
					modifiedParamValues.append(wlanObj.getChannelInUse()).append("|");
					modifiedParamTypes.append("1").append("|");
				}
			}
		}
		else
		{
			if (null != wlanObj.getApEnable()
					&& !wlanObj.getApEnable().equalsIgnoreCase(wlanObjFromDB.getApEnable()))
			{
				modifiedParamNames.append(paramNamesMap.get("ap_enable")).append("|");
				modifiedParamNamesForDB.append("ap_enable|");
				modifiedParamValues.append(wlanObj.getApEnable()).append("|");
				modifiedParamTypes.append("4").append("|");
			}
			
			if (null != wlanObj.getPowerLevel()
					&& !wlanObj.getPowerLevel().equalsIgnoreCase(wlanObjFromDB.getPowerLevel()))
			{
				modifiedParamNames.append(paramNamesMap.get("powerlevel")).append("|");
				modifiedParamNamesForDB.append("powerlevel|");
				modifiedParamValues.append(wlanObj.getPowerLevel()).append("|");
				modifiedParamTypes.append("3").append("|");
			}
			// 因页面上没有Enable按钮，所以如果未起用，则使之能启用
			if (null == wlanObj.getEnable() || "0".equals(wlanObj.getEnable()))
			{
				wlanObj.setEnable("1");
			}
			
			if (null != wlanObj.getSsid()
					&& !wlanObj.getSsid().equalsIgnoreCase(wlanObjFromDB.getSsid()))
			{
				modifiedParamNames.append(paramNamesMap.get("ssid")).append("|");
				modifiedParamNamesForDB.append("ssid|");
				modifiedParamValues.append(wlanObj.getSsid()).append("|");
				modifiedParamTypes.append("1").append("|");
			}
			
			if (null != wlanObj.getStandard()
					&& !wlanObj.getStandard().equalsIgnoreCase(wlanObjFromDB.getStandard()))
			{
				modifiedParamNames.append(paramNamesMap.get("standard")).append("|");
				modifiedParamNamesForDB.append("standard|");
				modifiedParamValues.append(wlanObj.getStandard()).append("|");
				modifiedParamTypes.append("1").append("|");
			}
			
			if (null != wlanObj.getBeacontType())
			{
				// 如果beacontype变了，则它相关的参数都要变
				if (!wlanObj.getBeacontType().equalsIgnoreCase(wlanObjFromDB.getBeacontType()))
				{
					logger.debug("如果beacontype变了，则它相关的参数都要变");
					modifiedParamNames.append(paramNamesMap.get("beacontype")).append("|");
					modifiedParamNamesForDB.append("beacontype|");
					modifiedParamValues.append(wlanObj.getBeacontType()).append("|");
					modifiedParamTypes.append("1").append("|");
				}
				if ("None".equalsIgnoreCase(wlanObj.getBeacontType()))
				{
					// BasicAuthenticationMode为OpenSystem
					logger.debug("BasicAuthenticationMode为OpenSystem");
					if (!"OpenSystem".equalsIgnoreCase(wlanObjFromDB.getBasicAuthMode()))
					{
						modifiedParamNames.append(paramNamesMap.get("basic_auth_mode")).append("|");
						modifiedParamNamesForDB.append("basic_auth_mode|");
						modifiedParamValues.append("OpenSystem").append("|");
						modifiedParamTypes.append("1").append("|");
					}
				}
				else if ("Basic".equalsIgnoreCase(wlanObj.getBeacontType()))
				{
					// BasicAuthenticationMode为Both
					if (!"Both".equalsIgnoreCase(wlanObjFromDB.getBasicAuthMode()))
					{
						modifiedParamNames.append(paramNamesMap.get("basic_auth_mode")).append("|");
						modifiedParamNamesForDB.append("basic_auth_mode|");
						modifiedParamValues.append("Both").append("|");
						modifiedParamTypes.append("1").append("|");
						modifiedParamNames.append(paramNamesMap.get("wep_encr_level")).append("|");
						modifiedParamNamesForDB.append("wep_encr_level|");
						modifiedParamValues.append("40-bit|");
						modifiedParamTypes.append("1").append("|");
					}
					
					if (null != wlanObj.getWepKey()
							&& !wlanObj.getWepKey().equalsIgnoreCase(wlanObjFromDB.getWepKey()))
					{
						modifiedParamNames.append(paramNamesMap.get("wep_key")).append("|");
						modifiedParamNamesForDB.append("wep_key|");
						modifiedParamValues.append(wlanObj.getWepKey()).append("|");
						modifiedParamTypes.append("1").append("|");
					}
				}
				else if ("WPA".equalsIgnoreCase(wlanObj.getBeacontType()))
				{
					logger.debug("BeaconType值为WPA");
					if (null != wlanObj.getWpaAuthMode()
							&& !wlanObj.getWpaAuthMode().equalsIgnoreCase(wlanObjFromDB.getWpaAuthMode()))
					{
						modifiedParamNames.append(paramNamesMap.get("wpa_auth_mode")).append("|");
						modifiedParamNamesForDB.append("wpa_auth_mode|");
						modifiedParamValues.append(wlanObj.getWpaAuthMode()).append("|");
						modifiedParamTypes.append("1").append("|");
					}
					if (null != wlanObj.getWpaEncrMode()
							&& !wlanObj.getWpaEncrMode().equalsIgnoreCase(wlanObjFromDB.getWpaEncrMode()))
					{
						modifiedParamNames.append(paramNamesMap.get("wpa_encr_mode")).append("|");
						modifiedParamNamesForDB.append("wpa_encr_mode|");
						modifiedParamValues.append(wlanObj.getWpaEncrMode()).append("|");
						modifiedParamTypes.append("1").append("|");
					}
					if (null != wlanObj.getWpaKey()
							&& !wlanObj.getWpaKey().equalsIgnoreCase(wlanObjFromDB.getWpaKey()))
					{
						modifiedParamNames.append(paramNamesMap.get("wpa_key")).append("|");
						modifiedParamNamesForDB.append("wpa_key|");
						modifiedParamValues.append(wlanObj.getWpaKey()).append("|");
						modifiedParamTypes.append("1").append("|");
					}
				}
				else
				{
					logger.warn("[{}] BeaconType值不可识别，返回：{}",device_id,wlanObj.getBeacontType());
				}
			}
			if (null != wlanObj.getChannel()
					&& !wlanObj.getChannel().equalsIgnoreCase(wlanObjFromDB.getChannel()))
			{
				modifiedParamNames.append(paramNamesMap.get("channel")).append("|");
				modifiedParamNamesForDB.append("channel|");
				modifiedParamValues.append(wlanObj.getChannel()).append("|");
				modifiedParamTypes.append("3").append("|");
				if ("0".equalsIgnoreCase(wlanObj.getChannel()))
				{
					logger.debug("Channel为0，需要判断ChannelsInUse结点");
					if (null != wlanObj.getChannelInUse()
							&& !wlanObj.getChannelInUse().equalsIgnoreCase(wlanObjFromDB.getChannelInUse()))
					{
						modifiedParamNames.append(paramNamesMap.get("channel_in_use")).append("|");
						modifiedParamNamesForDB.append("channel_in_use|");
						modifiedParamValues.append(wlanObj.getChannelInUse()).append("|");
						modifiedParamTypes.append("1").append("|");
					}
				}
			}
			if (null != wlanObj.getRadioEnable()
					&& !wlanObj.getRadioEnable().equalsIgnoreCase(wlanObjFromDB.getRadioEnable()))
			{
				modifiedParamNames.append(paramNamesMap.get("radio_enable")).append("|");
				modifiedParamNamesForDB.append("radio_enable|");
				modifiedParamValues.append(wlanObj.getRadioEnable()).append("|");
				modifiedParamTypes.append("4").append("|");
			}
			if (null != wlanObj.getHide()
					&& !wlanObj.getHide().equalsIgnoreCase(wlanObjFromDB.getHide()))
			{
				modifiedParamNames.append(paramNamesMap.get("hide")).append("|");
				modifiedParamNamesForDB.append("hide|");
				modifiedParamValues.append(wlanObj.getHide()).append("|");
				modifiedParamTypes.append("4").append("|");
			}
		}
		
		if (modifiedParamNames.length() == 0 && modifiedParamValues.length() == 0
				&& modifiedParamTypes.length() == 0)
		{
			logger.warn("[{}] 不需要修改任何值，返回成功",device_id);
			return true;
		}
		String[] params_name = modifiedParamNames.toString().split("\\|");
		String[] params_value = modifiedParamValues.toString().split("\\|");
		String[] para_type_id = modifiedParamTypes.toString().split("\\|");
		String[] params_name_db = modifiedParamNamesForDB.toString().split("\\|");
		
		if (paramInfoCorba.setParamValue_multi(params_name, params_value, para_type_id,device_id))
		{
			if (wlanConfDao.setWlan(device_id, lan_id, lan_wlan_id, params_name_db,
					params_value, para_type_id)){
				logger.warn("[{}] 操作数据库成功！",device_id);
			}else{
				logger.warn("[{}] 操作数据库失败！",device_id);
			}
			
			if (null != wlanObj.getEnable())
			{
				String[] enableName = { paramNamesMap.get("enable") };
				String[] enableValue = { wlanObj.getEnable() };
				String[] enableType = { "4" };
				String[] enableNameDB = { "enable" };
				if (null != wlanObjFromDB)
				{
					if (!wlanObj.getEnable().equalsIgnoreCase(wlanObjFromDB.getEnable()))
					{
						if (!paramInfoCorba.setParamValue_multi(enableName, enableValue,
								enableType, device_id))
						{
							logger.error("[{}] 更新设备Enable结点失败，返回",device_id);
							return false;
						}
						
						if (wlanConfDao.setWlan(device_id, lan_id, lan_wlan_id,
								enableNameDB, enableValue, enableType)){
							logger.warn("[{}] 操作数据库成功！",device_id);
						}else{
							logger.warn("[{}] 操作数据库失败！",device_id);
						}
					}
				}
				else
				{
					if (!paramInfoCorba.setParamValue_multi(enableName, enableValue,
							enableType, device_id)){
						logger.error("[{}] 更新设备Enable结点失败，返回",device_id);
						return false;
					}
					
					if (wlanConfDao.setWlan(device_id, lan_id, lan_wlan_id, enableNameDB,
							enableValue, enableType)){
						logger.warn("[{}] 操作数据库成功！",device_id);
					}else{
						logger.warn("[{}] 操作数据库失败！",device_id);
					}
				}
			}
			paramNamesMap.clear();
			paramNamesMap = null;
			return true;
		}
		return false;
	}

	/**
	 * 更新WAN连接结点值
	 */
	public boolean updateConnection(String device_id, String wan_id, String wan_conn_id,
			ConnObj connObj)
	{
		if (null == device_id || null == wan_id || null == wan_conn_id || null == connObj)
		{
			logger.error("device_id或wan_id或wan_conn_id或connObj为空，返回");
			return false;
		}

		String wan_conn_sess_id = connObj.getWan_conn_sess_id();
		String sess_type = connObj.getSess_type();
		NodeObj[] nodeObj = connObj.getNodeObj();
		Map<String, String> paramNamesMap = new HashMap<String, String>();
		StringBuffer modifiedParamNames = new StringBuffer();
		StringBuffer modifiedParamValues = new StringBuffer();
		StringBuffer modifiedParamTypes = new StringBuffer();
		if ("1".equalsIgnoreCase(sess_type))
		{
			paramNamesMap.put("enable", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANPPPConnection."
					+ wan_conn_sess_id + ".Enable");
			paramNamesMap.put("name", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANPPPConnection."
					+ wan_conn_sess_id + ".Name");
			paramNamesMap.put("conn_type", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANPPPConnection."
					+ wan_conn_sess_id + ".ConnectionType");
			paramNamesMap.put("serv_list", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANPPPConnection."
					+ wan_conn_sess_id + ".X_CT-COM_ServiceList");
			paramNamesMap.put("bind_port", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANPPPConnection."
					+ wan_conn_sess_id + ".X_CT-COM_LanInterface");
			paramNamesMap.put("username", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANPPPConnection."
					+ wan_conn_sess_id + ".Username");
			paramNamesMap.put("password", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANPPPConnection."
					+ wan_conn_sess_id + ".Password");
			for (int i = 0; i < ConnObj.NodeList.length; i++)
			{
				if ("1".equalsIgnoreCase(nodeObj[i].getIsModified()))
				{
					modifiedParamNames.append(paramNamesMap.get(nodeObj[i].getName())
							+ "|");
					modifiedParamValues.append(nodeObj[i].getValue() + "|");
					modifiedParamTypes.append(paramTypesMap.get(nodeObj[i].getName())
							+ "|");
					nodeObj[i].setParamType(paramTypesMap.get(nodeObj[i].getName()));
				}
			}
		}
		else if ("2".equalsIgnoreCase(sess_type))
		{
			paramNamesMap.put("enable", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".Enable");
			paramNamesMap.put("name", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".Name");
			paramNamesMap.put("conn_type", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".ConnectionType");
			paramNamesMap.put("serv_list", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".X_CT-COM_ServiceList");
			paramNamesMap.put("bind_port", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".X_CT-COM_LanInterface");
			paramNamesMap.put("ip_type", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".AddressingType");
			paramNamesMap.put("ip", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".ExternalIPAddress");
			paramNamesMap.put("mask", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".SubnetMask");
			paramNamesMap.put("gateway", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".DefaultGateway");
			paramNamesMap.put("dns_enab", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".DNSEnabled");
			paramNamesMap.put("dns", "InternetGatewayDevice.WANDevice." + wan_id
					+ ".WANConnectionDevice." + wan_conn_id + ".WANIPConnection."
					+ wan_conn_sess_id + ".DNSServers");
			for (int i = 0; i < ConnObj.NodeList.length; i++)
			{
				if ("1".equalsIgnoreCase(nodeObj[i].getIsModified()))
				{
					modifiedParamNames.append(paramNamesMap.get(nodeObj[i].getName())+ "|");
					modifiedParamValues.append(nodeObj[i].getValue() + "|");
					modifiedParamTypes.append(paramTypesMap.get(nodeObj[i].getName())+ "|");
					nodeObj[i].setParamType(paramTypesMap.get(nodeObj[i].getName()));
				}
			}
		}
		else
		{
			logger.warn("[{}] session值不可识别，返回！",device_id,sess_type);
			return false;
		}

		if (modifiedParamNames.length() == 0 && modifiedParamValues.length() == 0
				&& modifiedParamTypes.length() == 0)
		{
			logger.warn("[{}] 不需要修改任何值，返回成功",device_id);
			return true;
		}
		// 清空容器
		paramNamesMap.clear();
		paramNamesMap = null;
		String[] params_name = modifiedParamNames.toString().split("\\|");
		String[] params_value = modifiedParamValues.toString().split("\\|");
		String[] para_type_id = modifiedParamTypes.toString().split("\\|");
		if (paramInfoCorba.setParamValue_multi(params_name, params_value, para_type_id,
				device_id))
		{
			if (paramInfoDao.setWANConnSessionNode(device_id, wan_id, wan_conn_id,
					wan_conn_sess_id, nodeObj)){
				logger.warn("[{}] 操作数据库成功！",device_id);
			}else{
				logger.warn("[{}] 操作数据库失败！",device_id);
			}
			return true;
		}
		return false;
	}

	/**
	 * 把WlanObj的List转化为数组
	 */
	private WlanObj[] convertListToWlanObj(List<WlanObj> list)
	{
		WlanObj[] wlanObjArr = new WlanObj[list.size()];
		for (int i = 0; i < wlanObjArr.length; i++)
		{
			wlanObjArr[i] = list.get(i);
		}
		return wlanObjArr;
	}

	/**
	 * 把WanObj的List转化为数组
	 */
	private WanObj[] convertListToWanObj(List<WanObj> list)
	{
		WanObj[] wanObjArr = new WanObj[list.size()];
		for (int i = 0; i < wanObjArr.length; i++)
		{
			wanObjArr[i] = list.get(i);
		}
		return wanObjArr;
	}

	/**
	 * 把PvcObj的List转化为数组
	 */
	private PvcObj[] convertListToPvcObj(List<PvcObj> list)
	{
		PvcObj[] pvcObjArr = new PvcObj[list.size()];
		for (int i = 0; i < pvcObjArr.length; i++)
		{
			pvcObjArr[i] = list.get(i);
		}
		return pvcObjArr;
	}

	/**
	 * 把nodeObj的List转化为数组
	 */
	private NodeObj[] convertListToNodeObj(List<NodeObj> list)
	{
		NodeObj[] nodeObjArr = new NodeObj[list.size()];
		for (int i = 0; i < nodeObjArr.length; i++)
		{
			nodeObjArr[i] = list.get(i);
		}
		return nodeObjArr;
	}

	/**
	 * 把ConnObj的List转化为数组
	 */
	private ConnObj[] convertListToConnObj(List<ConnObj> list)
	{
		ConnObj[] connObjArr = new ConnObj[list.size()];
		for (int i = 0; i < connObjArr.length; i++)
		{
			connObjArr[i] = list.get(i);
		}
		return connObjArr;
	}

	/**
	 * 获取实例的数值
	 */
	private String getLastString(String parPath, String subPath)
	{
		String value = null;
		value = trimLastDot(value = subPath.substring(parPath.length()));
		return value;
	}

	/**
	 * 去掉字符串最后一个点
	 */
	private String trimLastDot(String value)
	{
		int dotIndex = value.lastIndexOf(".");
		if (dotIndex == value.length() - 1){
			value = value.substring(0, dotIndex);
		}
		return value;
	}
	
	/**
	 * 修改节点值
	 */
	public boolean updateNode(WlanObj wlanObj)
	{
		if (null == wlanObj)
		{
			logger.error("传入的WlanObj为空，返回");
			return false;
		}
		String device_id = wlanObj.getDeviceId();
		String lan_id = wlanObj.getLanId();
		String lan_wlan_id = wlanObj.getLanWlanId();
		//页面未设置，默认可用
		wlanObj.setEnable("1");
		
		Map<String, String> paramNamesMap = new HashMap<String, String>();
		paramNamesMap.put("enable", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".Enable");
		paramNamesMap.put("ssid", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".SSID");
		paramNamesMap.put("beacontype", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".BeaconType");
		paramNamesMap.put("iEEE11iAuthenticationMode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".IEEE11iAuthenticationMode");
		paramNamesMap.put("basic_auth_mode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".BasicAuthenticationMode");
		paramNamesMap.put("wep_key", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WEPKey.1.WEPKey");
		paramNamesMap.put("wpa_encr_mode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WPAEncryptionModes");
		paramNamesMap.put("wpa_key", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".PreSharedKey.1.PreSharedKey");
		paramNamesMap.put("hide", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".X_CT-COM_SSIDHide");
		
		StringBuffer modifiedParamNames = new StringBuffer();
		StringBuffer modifiedParamValues = new StringBuffer();
		StringBuffer modifiedParamTypes = new StringBuffer();
		StringBuffer modifiedParamNamesForDB = new StringBuffer();
		
		// 从数据库中获取对象
		WlanObj wlanObjFromDB = wlanConfDao.getWlan(device_id, lan_id, lan_wlan_id);
		if (null != wlanObj.getSsid()
				&& !wlanObj.getSsid().equalsIgnoreCase(wlanObjFromDB.getSsid()))
		{
			modifiedParamNames.append(paramNamesMap.get("ssid")).append("|");
			modifiedParamValues.append(wlanObj.getSsid()).append("|");
			modifiedParamTypes.append("1").append("|");
			modifiedParamNamesForDB.append("ssid|");
		}
		
		if (null != wlanObj.getHide()
				&& !wlanObj.getHide().equalsIgnoreCase(wlanObjFromDB.getHide()))
		{
			modifiedParamNames.append(paramNamesMap.get("hide")).append("|");
			modifiedParamValues.append(wlanObj.getHide()).append("|");
			modifiedParamTypes.append("4").append("|");
			modifiedParamNamesForDB.append("hide|");
		}
	
		// 如果beacontype变了，则它相关的参数都要变
		if (null!=wlanObj.getBeacontType() 
				&& !wlanObj.getBeacontType().equalsIgnoreCase(wlanObjFromDB.getBeacontType()))
		{
			modifiedParamNames.append(paramNamesMap.get("beacontype")).append("|");
			modifiedParamValues.append(wlanObj.getBeacontType()).append("|");
			modifiedParamTypes.append("1").append("|");
			modifiedParamNamesForDB.append("beacontype|");
			
			if ("None".equalsIgnoreCase(wlanObj.getBeacontType()))
			{
				if (!"OpenSystem".equalsIgnoreCase(wlanObjFromDB.getBasicAuthMode()))
				{
					modifiedParamNames.append(paramNamesMap.get("basic_auth_mode")).append("|");
					modifiedParamValues.append("OpenSystem").append("|");
					modifiedParamTypes.append("1").append("|");
					modifiedParamNamesForDB.append("basic_auth_mode|");
				}
			}
			else if ("Basic".equalsIgnoreCase(wlanObj.getBeacontType()))
			{
				if (!wlanObj.getBasicAuthMode().equalsIgnoreCase(wlanObjFromDB.getBasicAuthMode()))
				{
					modifiedParamNames.append(paramNamesMap.get("basic_auth_mode")).append("|");
					modifiedParamValues.append(wlanObj.getBasicAuthMode()).append("|");
					modifiedParamTypes.append("1").append("|");
					modifiedParamNamesForDB.append("basic_auth_mode|");
				}
				
				modifiedParamNames.append(paramNamesMap.get("wep_key")).append("|");
				modifiedParamValues.append(wlanObj.getWepKey()).append("|");
				modifiedParamTypes.append("1").append("|");
				modifiedParamNamesForDB.append("wep_key|");
			}
			else if ("WPA".equalsIgnoreCase(wlanObj.getBeacontType()) 
					|| ((VENDORID_HW.equals(vendorId) || VENDORID_ZX.equals(vendorId)) 
							&& ("WPA2".equalsIgnoreCase(wlanObj.getBeacontType()) 
									|| "WPA/WPA2".equalsIgnoreCase(wlanObj.getBeacontType()))) 
					|| ((!VENDORID_HW.equals(vendorId) && !VENDORID_ZX.equals(vendorId)) 
							&& ("11i".equalsIgnoreCase(wlanObj.getBeacontType()) 
									|| "WPAand11i".equalsIgnoreCase(wlanObj.getBeacontType())))) 
			{
				modifiedParamNames.append(paramNamesMap.get("iEEE11iAuthenticationMode")).append("|");
				modifiedParamValues.append("PSKAuthentication").append("|");
				modifiedParamTypes.append("1").append("|");
				modifiedParamNamesForDB.append("iee11i_auth_mode|");
				
				if (null != wlanObj.getWpaEncrMode()
						&& !wlanObj.getWpaEncrMode().equalsIgnoreCase(
								wlanObjFromDB.getWpaEncrMode()))
				{
					modifiedParamNames.append(paramNamesMap.get("wpa_encr_mode")).append("|");
					modifiedParamValues.append(wlanObj.getWpaEncrMode()).append("|");
					modifiedParamTypes.append("1").append("|");
					modifiedParamNamesForDB.append("wpa_encr_mode|");
				}
				
				modifiedParamNames.append(paramNamesMap.get("wpa_key")).append("|");
				modifiedParamValues.append(wlanObj.getWpaKey()).append("|");
				modifiedParamTypes.append("1").append("|");
				modifiedParamNamesForDB.append("wpa_key|");
			}
			else
			{
				logger.warn("{} BeaconType值不可识别，返回：{}",device_id,wlanObj.getBeacontType());
				return false;
			}
		}
		
		if (modifiedParamNames.length() == 0 && modifiedParamValues.length() == 0
				&& modifiedParamTypes.length() == 0)
		{
			logger.warn("[{}] 不需要修改任何值，返回成功",device_id);
			return true;
		}
		
		String[] paramNames = modifiedParamNames.toString().split("\\|");
		String[] paramValues = modifiedParamValues.toString().split("\\|");
		String[] paramTypes = modifiedParamTypes.toString().split("\\|");
		String[] params_name_db = modifiedParamNamesForDB.toString().split("\\|");
		
		String[] params_name = new String[paramNames.length+1];
		for(int i=0;i<params_name.length;i++){
			if(i==paramNames.length){
				params_name[i]=paramNamesMap.get("enable");
			}else{
				params_name[i]=paramNames[i];
			}
		}
		
		String[] params_value = new String[paramValues.length+1];
		String[] params_value_db = new String[paramValues.length+1];
		for(int i=0;i<params_value.length;i++){
			if(i==paramValues.length){
				params_value[i]=wlanObj.getEnable();
				params_value_db[i]=wlanObj.getEnable();
			}else{
				params_value[i]=paramValues[i];
				if ("Basic".equalsIgnoreCase(paramValues[i]) 
						&& "Basic".equalsIgnoreCase(wlanObj.getBeacontType())){
					params_value_db[i]="WEP";
				}else if ("WPA".equalsIgnoreCase(paramValues[i]) 
						&& "WPA".equalsIgnoreCase(wlanObj.getBeacontType())){
					params_value_db[i]="WPA-PSK";
				}else if (("WPA2".equalsIgnoreCase(paramValues[i]) 
							&& "WPA2".equalsIgnoreCase(wlanObj.getBeacontType())) 
						|| ("11i".equalsIgnoreCase(paramValues[i]) 
								&& "11i".equalsIgnoreCase(wlanObj.getBeacontType()))){
					params_value_db[i]="WPA2-PSK";
				}else if (("WPA/WPA2".equalsIgnoreCase(paramValues[i]) 
							&& "WPA/WPA2".equalsIgnoreCase(wlanObj.getBeacontType())) 
						|| ("WPAand11i".equalsIgnoreCase(paramValues[i]) 
								&& "WPAand11i".equalsIgnoreCase(wlanObj.getBeacontType()))){
					params_value_db[i]="WPA-PSK/WPA2-PSK";	
				}else{
					params_value_db[i]=paramValues[i];
				}
			}
		}
		
		String[] para_type_id = new String[paramTypes.length+1];
		for(int i=0;i<para_type_id.length;i++){
			if(i==paramTypes.length){
				para_type_id[i]="4";
			}else{
				para_type_id[i]=paramTypes[i];
			}
		}
		
		modifiedParamNames=null;
		modifiedParamValues=null;
		modifiedParamTypes=null;
		modifiedParamNamesForDB=null;
		paramNames=null;
		paramValues=null;
		paramTypes=null;
		logger.warn("[{}] updateNode({},{},{})",device_id,
										Arrays.toString(params_name),
										Arrays.toString(params_value),
										Arrays.toString(para_type_id));
		
		if(paramInfoCorba.setParamValue_multi(params_name,params_value,para_type_id,device_id))
		{
			params_name=null;
			logger.warn("[{}] updateNode(节点修改成功)",device_id);
			
			logger.warn("[{}] updateNodeSql({},{})",device_id,
					Arrays.toString(params_name_db),
					Arrays.toString(params_value_db));
			if (wlanConfDao.setWlan(device_id, lan_id, lan_wlan_id, params_name_db,
					params_value_db, para_type_id)){
				logger.warn("[{}] 操作数据库成功！",device_id);
				
				lan_id=null;
				lan_wlan_id=null;
				params_name_db=null;
				return true;
			}else{
				logger.warn("[{}] 操作数据库失败！",device_id);
			}
		}
		
		lan_id=null;
		lan_wlan_id=null;
		params_name_db=null;
		return false;
	}
	
	/**
	 * 新增节点
	 */
	public boolean addNode(WlanObj wlanObj)
	{
		if (null == wlanObj){
			logger.error("wlanObj为null，返回");
			return false;
		}
		
		String device_id = wlanObj.getDeviceId();
		if (null == device_id){
			logger.error("device_id为空，返回",device_id);
			return false;
		}

		String lan_id = wlanObj.getLanId();
		if (null == lan_id){
			logger.error("[{}] lan_id为空，默认设置为1",device_id);
			lan_id = "1";
		}
		String wlanDevPath = "InternetGatewayDevice.LANDevice."+lan_id+".WLANConfiguration.";
		int wlan_i = 0;
		/** 1)增加WLANConfiguration下的结点，返回结点的i值 */
		wlan_i = paramInfoCorba.addPara(wlanDevPath, device_id);
		logger.warn("[{}] wlan_i:{}",device_id,wlan_i);
		if (0 == wlan_i){
			logger.error("[{}] 新增失败，返回",device_id);
			return false;
		}
		
		String lan_wlan_id=String.valueOf(wlan_i);
		/** 2)设置WLANConfiguration.i下的参数值 */
		wlanObj.setLanId(lan_id);
		wlanObj.setLanWlanId(lan_wlan_id);
		// 页面没 有设置，直接设为可用
		wlanObj.setEnable("1");
		wlanObj.setWepKeyId("1");
		
		Map<String, String> paramNamesMap = new HashMap<String, String>();
		paramNamesMap.put("enable", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".Enable");
		paramNamesMap.put("ssid", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".SSID");
		paramNamesMap.put("beacontype", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".BeaconType");
		paramNamesMap.put("iEEE11iAuthenticationMode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".IEEE11iAuthenticationMode");
		paramNamesMap.put("basic_auth_mode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".BasicAuthenticationMode");
		paramNamesMap.put("wep_key", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WEPKey.1.WEPKey");
		paramNamesMap.put("wpa_auth_mode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WPAAuthenticationMode");
		paramNamesMap.put("wpa_encr_mode", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".WPAEncryptionModes");
		paramNamesMap.put("wpa_key", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".PreSharedKey.1.PreSharedKey");
		paramNamesMap.put("hide", "InternetGatewayDevice.LANDevice." + lan_id
				+ ".WLANConfiguration." + lan_wlan_id + ".X_CT-COM_SSIDHide");
		
		StringBuffer modifiedParamNames = new StringBuffer();
		StringBuffer modifiedParamValues = new StringBuffer();
		StringBuffer modifiedParamTypes = new StringBuffer();
		
		if (null != wlanObj.getSsid())
		{
			modifiedParamNames.append(paramNamesMap.get("ssid")).append("|");
			modifiedParamValues.append(wlanObj.getSsid()).append("|");
			modifiedParamTypes.append("1").append("|");
		}
		
		if (null != wlanObj.getHide())
		{
			modifiedParamNames.append(paramNamesMap.get("hide")).append("|");
			modifiedParamValues.append(wlanObj.getHide()).append("|");
			modifiedParamTypes.append("4").append("|");
		}
		
		if(null!=wlanObj.getBeacontType())
		{
			modifiedParamNames.append(paramNamesMap.get("beacontype")).append("|");
			modifiedParamValues.append(wlanObj.getBeacontType()).append("|");
			modifiedParamTypes.append("1").append("|");
			
			if ("None".equalsIgnoreCase(wlanObj.getBeacontType()))
			{
				modifiedParamNames.append(paramNamesMap.get("basic_auth_mode")).append("|");
				modifiedParamValues.append("OpenSystem").append("|");
				modifiedParamTypes.append("1").append("|");
				wlanObj.setBasicAuthMode(null);
			}
			else if ("Basic".equalsIgnoreCase(wlanObj.getBeacontType()))
			{
				modifiedParamNames.append(paramNamesMap.get("basic_auth_mode")).append("|");
				modifiedParamValues.append(wlanObj.getBasicAuthMode()).append("|");
				modifiedParamTypes.append("1").append("|");
				
				modifiedParamNames.append(paramNamesMap.get("wep_key")).append("|");
				modifiedParamValues.append(wlanObj.getWepKey()).append("|");
				modifiedParamTypes.append("1").append("|");
			}
			else if ("WPA".equalsIgnoreCase(wlanObj.getBeacontType()) 
					|| ((VENDORID_HW.equals(vendorId) || VENDORID_ZX.equals(vendorId)) 
							&& ("WPA2".equalsIgnoreCase(wlanObj.getBeacontType()) 
									|| "WPA/WPA2".equalsIgnoreCase(wlanObj.getBeacontType()))) 
					|| ((!VENDORID_HW.equals(vendorId) && !VENDORID_ZX.equals(vendorId)) 
							&& ("11i".equalsIgnoreCase(wlanObj.getBeacontType()) 
									|| "WPAand11i".equalsIgnoreCase(wlanObj.getBeacontType()))))
			{
				modifiedParamNames.append(paramNamesMap.get("iEEE11iAuthenticationMode")).append("|");
				modifiedParamValues.append("PSKAuthentication").append("|");
				modifiedParamTypes.append("1").append("|");
				
				if (null != wlanObj.getWpaEncrMode())
				{
					modifiedParamNames.append(paramNamesMap.get("wpa_encr_mode")).append("|");
					modifiedParamValues.append(wlanObj.getWpaEncrMode()).append("|");
					modifiedParamTypes.append("1").append("|");
				}
				
				modifiedParamNames.append(paramNamesMap.get("wpa_key")).append("|");
				modifiedParamValues.append(wlanObj.getWpaKey()).append("|");
				modifiedParamTypes.append("1").append("|");
			}
			else
			{
				logger.warn("[{}] BeaconType值不可识别，返回：{}",device_id,wlanObj.getBeacontType());
				return false;
			}
		}
		
		if (modifiedParamNames.length() == 0 
				&& modifiedParamValues.length() == 0
				&& modifiedParamTypes.length() == 0)
		{
			logger.warn("[{}] 不需要新增任何值，返回成功",device_id);
			return true;
		}
		
		String[] paramNames = modifiedParamNames.toString().split("\\|");
		String[] paramValues = modifiedParamValues.toString().split("\\|");
		String[] paramTypes = modifiedParamTypes.toString().split("\\|");
		
		String[] params_name = new String[paramNames.length+1];
		for(int i=0;i<params_name.length;i++){
			if(i==paramNames.length){
				params_name[i]=paramNamesMap.get("enable");
			}else{
				params_name[i]=paramNames[i];
			}
		}
		
		String[] params_value = new String[paramValues.length+1];
		for(int i=0;i<params_value.length;i++){
			if(i==paramValues.length){
				params_value[i]=wlanObj.getEnable();
			}else{
				params_value[i]=paramValues[i];
			}
		}
		
		String[] para_type_id = new String[paramTypes.length+1];
		for(int i=0;i<para_type_id.length;i++){
			if(i==paramTypes.length){
				para_type_id[i]="4";
			}else{
				para_type_id[i]=paramTypes[i];
			}
		}
		
		modifiedParamNames=null;
		modifiedParamValues=null;
		modifiedParamTypes=null;
		paramNames=null;
		paramValues=null;
		paramTypes=null;
		
		logger.warn("[{}] addNode({},{},{})",device_id,
										Arrays.toString(params_name),
										Arrays.toString(params_value),
										Arrays.toString(para_type_id));
		if(paramInfoCorba.setParamValue_multi(params_name,params_value,para_type_id,device_id))
		{
			logger.warn("[{}] addNode(新增节点成功)",device_id);
			params_name=null;
			
			if ("Basic".equalsIgnoreCase(wlanObj.getBeacontType())){
				wlanObj.setBeacontType("WEP");
			}else if ("WPA".equalsIgnoreCase(wlanObj.getBeacontType())){
				wlanObj.setBeacontType("WPA-PSK");
			}else if ("WPA2".equalsIgnoreCase(wlanObj.getBeacontType()) 
					|| "11i".equalsIgnoreCase(wlanObj.getBeacontType())){
				wlanObj.setBeacontType("WPA2-PSK");
			}else if ("WPA/WPA2".equalsIgnoreCase(wlanObj.getBeacontType()) 
					|| "WPAand11i".equalsIgnoreCase(wlanObj.getBeacontType())){
				wlanObj.setBeacontType("WPA-PSK/WPA2-PSK");	
			}
			
			if (wlanConfDao.addWlan(wlanObj)){
				logger.warn("[{}] 操作数据库成功！",device_id);
				
				wlanObj=null;
				lan_id=null;
				lan_wlan_id=null;
				return true;
			}else{
				logger.warn("[{}] 操作数据库失败！",device_id);
			}
		}
		
		wlanObj=null;
		lan_id=null;
		lan_wlan_id=null;
		return false;
	}
}
