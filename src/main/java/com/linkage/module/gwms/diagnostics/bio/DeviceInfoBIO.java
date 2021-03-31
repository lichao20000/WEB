/**
 * 
 */
package com.linkage.module.gwms.diagnostics.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.interf.I_CAO;
import com.linkage.module.gwms.dao.gw.WanDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.diagnostics.bio.interf.I_DeviceInfoBIO;
import com.linkage.module.gwms.diagnostics.dao.interf.I_DeviceInfoDAO;
import com.linkage.module.gwms.diagnostics.obj.PONInfoOBJ;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;
import com.linkage.module.gwms.obj.gw.GwTr069OBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileH248OBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileLineObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-25
 * @category com.linkage.module.gwms.diagnostics.bio
 * 
 */
public class DeviceInfoBIO implements I_DeviceInfoBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(DeviceInfoBIO.class);

	/**
	 * 注入
	 */
	I_DeviceInfoDAO deviceInfoDAO;
	
	/**
	 * 获取上行方式
	 */
	WanDAO wanDao;

	/**
	 * 管理PVC/VLAN
	 */
	String managePass;
	
	/**
	 * 
	 */
	I_CAO wanCAO;

	private String LAN1 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1";
	private String LAN2 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2";
	private String LAN3 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3";
	private String LAN4 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4";
	
	private String WLAN1 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1";
	private String WLAN2 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.2";
	private String WLAN3 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.3";
	private String WLAN4 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.4";
	
	/**
	 * 获取设备的相关信息，包括用户信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map<String, String> getDeviceInfo(String deviceId, String gw_type) {
		
		logger.warn("获取设备信息->基本信息,deviceId:【{}】",deviceId);
		
		Map<String, String> deviceInfoMap = null;

		List devInfo = deviceInfoDAO.getDeviceInfo(deviceId);

		if (devInfo.size() > 0) {

			deviceInfoMap = new HashMap<String, String>();

			Map oneDevInfo = (Map) devInfo.get(0);
			if(Global.HBLT.equals(Global.instAreaShortName)){
			String gigabit_port = String.valueOf(oneDevInfo.get("gigabit_port")).toString();
			deviceInfoMap.put("gigabit_port", gigabit_port);
			}
			if(Global.NXDX.equals(Global.instAreaShortName)){
				String mac = StringUtil.getStringValue(oneDevInfo.get("mac"));
				if (!StringUtil.IsEmpty(mac)) {
					deviceInfoMap.put("is100MTo1000M", "是");
				}else {
					deviceInfoMap.put("is100MTo1000M", "否");
				}
			}
			String device_Id = String.valueOf(oneDevInfo.get("device_id")).toString();
			String oui = String.valueOf(oneDevInfo.get("oui")).toString();
			String device_serialnumber = String.valueOf(oneDevInfo.get("device_serialnumber")).toString();
			String loopback_ip = String.valueOf(oneDevInfo.get("loopback_ip")).toString();
			String vendor_add = String.valueOf(oneDevInfo.get("vendor_add")).toString();
			String device_model = String.valueOf(oneDevInfo.get("device_model")).toString();
			String hardwareversion = String.valueOf(oneDevInfo.get("hardwareversion")).toString();
			String softwareversion = String.valueOf(oneDevInfo.get("softwareversion")).toString();
            String cpe_mac=StringUtil.getStringValue(oneDevInfo.get("cpe_mac"));
            String ip_model_type = StringUtil.getStringValue(oneDevInfo.get("ip_model_type")); 
			deviceInfoMap.put("device_Id", device_Id);
			deviceInfoMap.put("oui", oui);
			deviceInfoMap.put("device_serialnumber", device_serialnumber);
			deviceInfoMap.put("loopback_ip", loopback_ip);
			deviceInfoMap.put("vendor_add", vendor_add);
			deviceInfoMap.put("device_model", device_model);
			deviceInfoMap.put("hardwareversion", hardwareversion);
			deviceInfoMap.put("softwareversion", softwareversion);
			deviceInfoMap.put("register", "已注册");
			deviceInfoMap.put("cpe_mac", cpe_mac);
			deviceInfoMap.put("ip_model_type", ip_model_type);
			// gw_type
			deviceInfoMap.put("gw_type", String.valueOf(gw_type));
			
			// bbms
			if(Global.GW_TYPE_BBMS.equals(gw_type)){
				
				List userInfo = deviceInfoDAO.getDeviceEUserInfo(deviceId);

				if (userInfo.size() > 0) {

					Map oneUserInfo = (Map) userInfo.get(0);

					Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
					String user_id = String.valueOf(oneUserInfo.get("user_id")).toString();
					String username = String.valueOf(oneUserInfo.get("username")).toString();
					String customer_name = String.valueOf(oneUserInfo.get("customer_name")).toString();
					String customer_address = String.valueOf(oneUserInfo.get("customer_address")).toString();
					String linkman = String.valueOf(oneUserInfo.get("linkman")).toString();
					String linkphone = String.valueOf(oneUserInfo.get("linkphone")).toString();
					String city_id = String.valueOf(oneUserInfo.get("city_id")).toString();
					String city_name = cityMap.get(String.valueOf(oneUserInfo.get("city_id")).toString());
					String device_port = String.valueOf(oneUserInfo.get("device_port")).toString();
					cityMap = null;
					deviceInfoMap.put("user_id", user_id);
					deviceInfoMap.put("username", username);
					deviceInfoMap.put("customer_name", customer_name);
					deviceInfoMap.put("customer_address", customer_address);
					deviceInfoMap.put("linkman", linkman);
					deviceInfoMap.put("linkphone", linkphone);
					deviceInfoMap.put("city_name", city_name);
					deviceInfoMap.put("city_id", city_id);
					deviceInfoMap.put("device_port", device_port);
				}
				
			}
			// itms
			else if(Global.GW_TYPE_ITMS.equals(gw_type)){
				
				List userInfo = deviceInfoDAO.getDeviceHUserInfo(deviceId);

				if (userInfo.size() > 0) {

					Map oneUserInfo = (Map) userInfo.get(0);
					
					String user_id = StringUtil.getStringValue(oneUserInfo.get("user_id"));
					String username = StringUtil.getStringValue(oneUserInfo.get("username"));
					String linkman = StringUtil.getStringValue(oneUserInfo.get("linkman"));
					String linkaddress = StringUtil.getStringValue(oneUserInfo.get("linkaddress"));
					String credno = StringUtil.getStringValue(oneUserInfo.get("credno"));
					String linkphone = StringUtil.getStringValue(oneUserInfo.get("linkphone"));
					String device_port = StringUtil.getStringValue(oneUserInfo.get("device_port"));
					String knname = StringUtil.getStringValue(oneUserInfo.get("kdname"));	
					String macaddress = StringUtil.getStringValue(oneUserInfo.get("macaddress"));
					String city_id = StringUtil.getStringValue(oneUserInfo.get("city_id"));
					

					deviceInfoMap.put("city_id", city_id);
					deviceInfoMap.put("user_id", user_id);
					deviceInfoMap.put("username", username);
					deviceInfoMap.put("linkman", linkman);
					deviceInfoMap.put("linkaddress", linkaddress);
					deviceInfoMap.put("credno", credno);
					deviceInfoMap.put("linkphone", linkphone);
					deviceInfoMap.put("device_port", device_port);
					deviceInfoMap.put("kdname", knname);
					deviceInfoMap.put("macaddress", macaddress);
				}
			}
		}

		logger.warn("获取设备信息->基本信息结束,deviceId:【{}】",deviceId);
		return deviceInfoMap;
	}

	/**
	 * 获取网关能力
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map<String, String> getAbilityInfo(String deviceId) {
		
		logger.warn("获取网关能力,deviceId:【{}】",deviceId);
		

		Map<String, String> abilityInfo = deviceInfoDAO.getAbilityInfo(deviceId);
		
		logger.warn("获取网关能力结束,deviceId:【{}】", deviceId);
		return abilityInfo;
	}

	/**
	 * 获取宽带信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List<WanConnSessObj> getWideNetInfo(List<Map> listNet)
	{
		List<Map> list = new ArrayList<Map>();
		List<String> wanConn = new ArrayList<String>();
		//相同的wan连接 取一个。
		if(listNet != null && listNet.size() > 0)
		{
			for (Map map : listNet)
			{
//				String wan = (String) map.get("wan_conn_id");
				if(map.get("wan_conn_id") != null && !wanConn.contains(StringUtil.getStringValue(map.get("wan_conn_id"))))
				{
					wanConn.add(StringUtil.getStringValue(map.get("wan_conn_id")));
					list.add(map);
				}
			}
		}
		List<WanConnSessObj> wanConnSess = new ArrayList<WanConnSessObj>();
		WanConnObj wanConnObj = null;
		WanConnSessObj[] one = null;
		if (list != null && list.size() > 0)
		{
			// 由于获取所有的Sesstion连接属性中的查询条件是i和j，没有k查询，导致多处循环查询的结果相同，故只循环一次
			for(int i=0; i<list.size(); i++)
//			for (int i = 0; i < 1; i++)
			{
				// add by chenjie 2013-10-25 运营商信息
//				if(LipossGlobals.getLipossProperty("telecom").equals(Global.TELECOM_CUC))
//				{
//					one = deviceInfoDAO.queryDevWanConnSession(list.get(i));
//				}
//				else
//				{
					wanConnObj = deviceInfoDAO.queryDevWanConn(list.get(i));
					// String connPvc = wanConnObj.getVpi_id()+"/"+wanConnObj.getVci_id();
					one = deviceInfoDAO.queryDevWanConnSession(wanConnObj);
//				}
				if (null != one)
				{
					for (int j = 0; j < one.length; j++)
					{
						if (!"".equals(one[j].getBindPort())
								&& null != one[j].getBindPort()
								&& !"null".equals(one[j].getBindPort()))
						{
							logger.debug("bind_all_port:{}", one[j].getBindPort());
							String[] bindPortArr = one[j].getBindPort().split(",");
							
							StringBuffer bindPortSB = new StringBuffer();
							logger.debug("bind_all_split_port:{}", bindPortArr);
							for (int m = 0; m < bindPortArr.length; m++)
							{
								if (0 != m)
								{
									bindPortSB.append(",");
								}
								String temStr = bindPortArr[m];
								logger.debug("bind_port_first:{}", temStr);
								if (".".equals(temStr.substring(temStr.length() - 1,
										temStr.length())))
								{
									temStr = temStr.substring(0, temStr.length() - 1);
								}
								logger.debug("bind_port_two:{}", temStr);
								bindPortSB.append(Global.bindPortMap.get(temStr));
							}
							one[j].setBindPort(bindPortSB.toString());
							if ("1".equals(one[j].getSessType()))
							{
								one[j].setConnType(Global.G_Src_Key_Map.get("1").get(
										one[j].getConnType()));
								one[j].setStatus(Global.G_Src_Key_Map.get("3").get(
										one[j].getStatus()));
							}
							else
							{
								one[j].setConnType(Global.G_Src_Key_Map.get("2").get(
										one[j].getConnType()));
								one[j].setStatus(Global.G_Src_Key_Map.get("4").get(
										one[j].getStatus()));
							}
							one[j].setConnError(Global.G_Src_Key_Map.get("7").get(
									one[j].getConnError()));
						}
						wanConnSess.add(one[j]);
					}
				}
			}
		}
		return wanConnSess;
	}

	/**
	 * 获取宽带信息
	 * 
	 * @param deviceId
	 * @return
	 */
	/**
	public List<WanConnSessObj> getWideNetInfo_BBMS(String deviceId, String userId) {

		Map<String, String> bindPortMap = new HashMap<String, String>();
		
		bindPortMap.put(LAN1, "LAN1");
		bindPortMap.put(LAN2, "LAN2");
		bindPortMap.put(LAN3, "LAN3");
		bindPortMap.put(LAN4, "LAN4");
		
		bindPortMap.put(WLAN1, "WLAN1");
		bindPortMap.put(WLAN2, "WLAN2");
		bindPortMap.put(WLAN3, "WLAN3");
		bindPortMap.put(WLAN4, "WLAN4");
		
		logger.debug("getWideNetInfo({},{})", deviceId,userId);

		List<WanConnSessObj> wanConnSess = new ArrayList<WanConnSessObj>();

		WanConnObj[] wanConnObj = deviceInfoDAO.queryDevWanConn(deviceId);
		
		if (wanConnObj == null) {
			logger.warn("wanConnObj 为空");
			return null;
		}
		
		for(int i=0;i<wanConnObj.length;i++){

			WanConnSessObj[] one = deviceInfoDAO.queryDevWanConnSession(wanConnObj[i]);
			if(null!=one){
				for (int j = 0; j < one.length; j++) {
					
					if(!"".equals(one[j].getBindPort()) && null!=one[j].getBindPort()){
						String[] bindPortArr = one[j].getBindPort().split(",");
						StringBuffer bindPortSB = new StringBuffer();
						for(int m=0;m<bindPortArr.length;m++){
							if(0!=m){
								bindPortSB.append(",");
							}
							bindPortSB.append(bindPortMap.get(bindPortArr[m]));
						}
						one[j].setBindPort(bindPortSB.toString());
						if("1".equals(one[j].getSessType())){
							one[j].setConnType(Global.G_Src_Key_Map.get("1").get(one[j].getConnType()));
							one[j].setStatus(Global.G_Src_Key_Map.get("3").get(one[j].getStatus()));
						}else{
							one[j].setConnType(Global.G_Src_Key_Map.get("2").get(one[j].getConnType()));
							one[j].setStatus(Global.G_Src_Key_Map.get("4").get(one[j].getStatus()));
						}
						one[j].setConnError(Global.G_Src_Key_Map.get("7").get(one[j].getConnError()));
					}
					wanConnSess.add(one[j]);
				}
			}
		
		}

		return wanConnSess;
	}
	**/
	
	/**
	 * 获取IPTV相关信息
	 * 
	 * @param deviceId
	 * @param userId
	 * @return
	 */
	public List<WanConnSessObj> getIptvInfo(List<Map> list) {
		
		logger.warn("getIptvInfo({})", list);

		List<WanConnSessObj> wanConnSess = new ArrayList<WanConnSessObj>();

		WanConnObj obj = null;
		WanConnSessObj[] one = null;
		for(int i=0; i<list.size(); i++)
		{	
			// add by chenjie 2013-10-25 运营商信息
//			if(LipossGlobals.getLipossProperty("telecom").equals(Global.TELECOM_CUC))
//			{
//				one = deviceInfoDAO.queryDevWanConnSession(list.get(i));
//			}
//			else
//			{
				obj  = deviceInfoDAO.queryDevWanConn(list.get(i));
				if(obj == null)
				{
					continue;
				}
				one = deviceInfoDAO.queryDevWanConnSession(obj);
//			}
			
			
			if(null!=one){
				for (int j = 0; j < one.length; j++) {
					
					/* remove by chenjie 2012-3-28
					 * bindPortMap统一使用Global类里面的
					 * 
					Map<String, String> bindPortMap = new HashMap<String, String>();
					
					bindPortMap.put(LAN1, "LAN1");
					bindPortMap.put(LAN2, "LAN2");
					bindPortMap.put(LAN3, "LAN3");
					bindPortMap.put(LAN4, "LAN4");
					
					bindPortMap.put(WLAN1, "WLAN1");
					bindPortMap.put(WLAN2, "WLAN2");
					bindPortMap.put(WLAN3, "WLAN3");
					bindPortMap.put(WLAN4, "WLAN4");
					*/
					
					if(!"".equals(one[j].getBindPort()) && null!=one[j].getBindPort() && !"null".equals(one[j].getBindPort())){
						String[] bindPortArr = one[j].getBindPort().split(",");
						StringBuffer bindPortSB = new StringBuffer();
						for(int m=0;m<bindPortArr.length;m++){
							if(0!=m){
								bindPortSB.append(",");
							}
							bindPortSB.append(Global.bindPortMap.get(bindPortArr[m]));
						}
						one[j].setBindPort(bindPortSB.toString());
						if("1".equals(one[j].getSessType())){
							one[j].setConnType(Global.G_Src_Key_Map.get("1").get(one[j].getConnType()));
							one[j].setStatus(Global.G_Src_Key_Map.get("3").get(one[j].getStatus()));
						}else{
							one[j].setConnType(Global.G_Src_Key_Map.get("2").get(one[j].getConnType()));
							one[j].setStatus(Global.G_Src_Key_Map.get("4").get(one[j].getStatus()));
						}
					}
					
					wanConnSess.add(one[j]);
				}
			}
		}
		
		return wanConnSess;
	}
	
	/**
	 * 获取voip信息
	 * 
	 * @param deviceId
	 * @param userId
	 * @return
	 */
	public List<Map> getVoipInfo(List<Map> list,String voipProtocalType) {
		
		logger.debug("getVoipInfo({})", list);
		int voipProtocalTypeTemp = StringUtil.getIntegerValue(voipProtocalType);
		//返回值
		List<Map> rs = new ArrayList<Map>();
		Map map = null;
		WanConnObj obj = null;
 		WanConnSessObj[]  wanConnSess = null;
     	for(int k=0; k<list.size(); k++)
		{   
     		map = list.get(k);
     		// add by chenjie 2013-10-25 运营商信息
//     		if(LipossGlobals.getLipossProperty("telecom").equals(Global.TELECOM_CUC))
//			{
//     			wanConnSess = deviceInfoDAO.queryDevWanConnSession(map);
//			}
//     		else
//     		{
     			obj = deviceInfoDAO.queryDevWanConn(list.get(k));
     			wanConnSess = null;
     			if(null==obj){
    				logger.debug("device_id:{}没查到相关Wan连接！", map.get("device_id"));
    				
    			}else{
    				wanConnSess = deviceInfoDAO.queryDevWanConnSession(obj);
    				
    				if(null==wanConnSess || 1>wanConnSess.length){
    					logger.debug("device_id:{}没查到相关session！", map.get("device_id"));
    					
    				}
    			}
//     		}
			
			if(2==voipProtocalTypeTemp){
				rs =  getVoipInfoH248(wanConnSess,map);
			}else{
				rs =  getVoipInfoIR(wanConnSess,map);
			}
		}
     	 return rs;
	}
	/**
	 * 软件换和IMS
	 * @param wanConnSess
	 * @param map
	 * @return
	 */
	public  List<Map> getVoipInfoIR(WanConnSessObj[] wanConnSess ,Map map){
		//返回值
		List<Map> rs = new ArrayList<Map>();
		VoiceServiceProfileObj[] voipProfJ = deviceInfoDAO.getVoipProf(map);
		//VoiceServiceProfileObj[] voipProfJ = deviceInfoDAO.getVoipProf(StringUtil.getStringValue(map.get("device_id")));
		if(null != voipProfJ){
			for(int i=0;i<voipProfJ.length;i++){
				logger.debug("voipProfJ[i].getDeviceId({})",voipProfJ[i].getDeviceId());
				logger.debug("voipProfJ[i].getProfId({})",voipProfJ[i].getProfId());
				VoiceServiceProfileLineObj[] voipProfLine = deviceInfoDAO.getVoipProfLine(voipProfJ[i]);

				if(null != voipProfLine){
					for(int j=0;j<voipProfLine.length;j++){
						Map<String, String> oneVoip = new HashMap<String, String>();
						if(null!=wanConnSess && 0<wanConnSess.length){
							oneVoip.put("pvc",wanConnSess[0].getPvc());
							oneVoip.put("vlanid", wanConnSess[0].getVlanid());
							oneVoip.put("connType",Global.G_Src_Key_Map.get("5").get(wanConnSess[0].getConnType()));
							oneVoip.put("status",Global.G_Src_Key_Map.get("6").get(wanConnSess[0].getStatus()));
							oneVoip.put("ip", wanConnSess[0].getIp());
						}
						oneVoip.put("prox_serv",voipProfJ[i].getProxServ());
						oneVoip.put("prox_serv_2",voipProfJ[i].getProxServ2());
						oneVoip.put("username",voipProfLine[j].getUsername());
						oneVoip.put("password",voipProfLine[j].getPassword());
						oneVoip.put("regist_status",voipProfLine[j].getStatus());
                        oneVoip.put("line",voipProfLine[j].getLineId());
						rs.add(oneVoip);
					}
				}else{
					Map<String, String> oneVoip = new HashMap<String, String>();
					if(null!=wanConnSess)
					{
						if(0<wanConnSess.length)
						{
							oneVoip.put("pvc",wanConnSess[0].getPvc());
							oneVoip.put("vlanid", wanConnSess[0].getVlanid());
							oneVoip.put("connType",Global.G_Src_Key_Map.get("5").get(wanConnSess[0].getConnType()));
							oneVoip.put("status",Global.G_Src_Key_Map.get("6").get(wanConnSess[0].getStatus()));
						}
					}
					oneVoip.put("prox_serv",voipProfJ[i].getProxServ());
					oneVoip.put("prox_serv_2",voipProfJ[i].getProxServ2());
					rs.add(oneVoip);
				}
			}
		}
		return rs;
	}
	/**
	 * H248
	 * @param wanConnSess
	 * @param map
	 * @return
	 */
	public List<Map>  getVoipInfoH248(WanConnSessObj[] wanConnSess,Map map){
		//返回值
		List<Map> rs = new ArrayList<Map>();
		VoiceServiceProfileH248OBJ[] voipProfJ = deviceInfoDAO.getVoipProfH(map);
		if(null != voipProfJ){
			for(int i=0;i<voipProfJ.length;i++){
				logger.debug("voipProfJ[i].getDeviceId({})",voipProfJ[i].getDeviceId());
				logger.debug("voipProfJ[i].getProfId({})",voipProfJ[i].getProfId());
				VoiceServiceProfileLineObj[] voipProfLine = deviceInfoDAO.getVoipProfLineH(voipProfJ[i]);

				if(null != voipProfLine){
					for(int j=0;j<voipProfLine.length;j++){
						Map<String, String> oneVoip = new HashMap<String, String>();
						if(null!=wanConnSess && 0<wanConnSess.length){
							oneVoip.put("pvc",wanConnSess[0].getPvc());
							oneVoip.put("vlanid", wanConnSess[0].getVlanid());
							oneVoip.put("connType",Global.G_Src_Key_Map.get("5").get(wanConnSess[0].getConnType()));
							oneVoip.put("status",Global.G_Src_Key_Map.get("6").get(wanConnSess[0].getStatus()));
							oneVoip.put("ip", wanConnSess[0].getIp());
						}
						oneVoip.put("media_gateway_controler",voipProfJ[i].getMediaGatewayControler());
						oneVoip.put("media_gateway_controler_2",voipProfJ[i].getMediaGatewayControler2());
						oneVoip.put("h248_device_id",voipProfJ[i].getH248DeviceId());
						oneVoip.put("physical_term_id",voipProfLine[j].getPhysicalTermId());
						oneVoip.put("line",voipProfLine[j].getLineId());
						if(LipossGlobals.inArea(Global.HBLT)){
							if(!StringUtil.IsEmpty(voipProfLine[j].getStatus())){
								oneVoip.put("interfaceState",voipProfLine[j].getStatus());
							}
							else{
								oneVoip.put("interfaceState",voipProfJ[i].getInterfaceState());
							}
						}
						
						rs.add(oneVoip);
					}
				}else{
					Map<String, String> oneVoip = new HashMap<String, String>();
					if(null!=wanConnSess)
					{
						if(0<wanConnSess.length)
						{
							oneVoip.put("pvc",wanConnSess[0].getPvc());
							oneVoip.put("vlanid", wanConnSess[0].getVlanid());
							oneVoip.put("connType",Global.G_Src_Key_Map.get("5").get(wanConnSess[0].getConnType()));
							oneVoip.put("status",Global.G_Src_Key_Map.get("6").get(wanConnSess[0].getStatus()));
						}
					}
					oneVoip.put("media_gateway_controler",voipProfJ[i].getMediaGatewayControler());
					oneVoip.put("media_gateway_controler_2",voipProfJ[i].getMediaGatewayControler2());
					oneVoip.put("interfaceState",voipProfJ[i].getInterfaceState());
					rs.add(oneVoip);
				}
			}
		}
		return rs;
	}
	/**
	 * <p>
	 * [获取设备绑定用户的VOIP的开通协议类型]
	 * </p>
	 * @param deviceId
	 * @return
	 */
	public String getBssVoipSheetProtocalByDeviceId(String deviceId)
	{
	    List userInfo = deviceInfoDAO.getDeviceHUserInfo(deviceId);

        if (userInfo.size() > 0) {

            Map oneUserInfo = (Map) userInfo.get(0);
            
            String user_id = StringUtil.getStringValue(oneUserInfo.get("user_id"));
            
            if(null != user_id && !"".equals(user_id))
            {
                userInfo = deviceInfoDAO.getBssVoipSheet(user_id);
                if(userInfo.size() > 0)
                {
                    oneUserInfo = (Map) userInfo.get(0);
                    return StringUtil.getStringValue(oneUserInfo.get("protocol"));
                }
            }
            
        }
        return null;
	}
	
	/**
	 * 获取线路信息
	 * 
	 * @param deviceId
	 * @param userId
	 * 
	 * @return
	 */
	public DeviceWireInfoObj[] queryDevWireInfo(String deviceId, String userId){
		
		logger.debug("queryDevWireInfo({},{})", deviceId,userId);
		return deviceInfoDAO.queryDevWireInfo(deviceId);
		
	}
	
	/**
	 * 获取LAN侧信息
	 * 
	 * @param deviceId
	 * @param userId
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List queryLanEth(String deviceId,String userId){
		logger.debug("queryLanHost({},{})", deviceId,userId);
		
		List rs = new ArrayList();
		List list = deviceInfoDAO.queryLanEth(deviceId);
		
		if(null != list){
			for(int i=0;i<list.size();i++){
				Map map = (Map) list.get(i);
				map.put("status",Global.G_Src_Key_Map.get("8").get(map.get("status")));
				rs.add(map);
			}
		}
		
		return rs;
	}
	
	/**
	 * 获取Wlan 信息
	 * 
	 * @param deviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getWalnData(String deviceId,String userId){
		
		logger.debug("getData({},{})", deviceId,userId);
		List rs = new ArrayList();
		List list = deviceInfoDAO.getWalnData(deviceId);
		
		if(null != list){
			for(int i=0;i<list.size();i++){
				Map map = (Map) list.get(i);
				map.put("status",Global.G_Src_Key_Map.get("9").get(map.get("status")));
				rs.add(map);
			}
		}
		return rs;
	}
	
	/**
	 * 查询设备管理通道相关信息
	 * 
	 * @param deviceId
	 * @param userId
	 * @return
	 */
	public GwTr069OBJ getTr09Info(List<Map> list){
		
		logger.debug("getTr09Info({})", list);
		
		//TR069通道可能也有多个，这个地方还没考虑XXX
		String deviceId = StringUtil.getStringValue(list.get(0).get("device_id"));
		
		GwTr069OBJ obj = deviceInfoDAO.getTr09Info(deviceId);
		
		if(null==obj){
			return null;
		}
		
//		List<String> pvc = getServPvc(userId, "13");
//		if(pvc.size()>0){
//			obj.setPvc(pvc.get(0));
//		}
		
		WanConnObj wanConnObj = null;
		WanConnSessObj[] one = null;
		for(int i=0;i<list.size();i++){

			// 默认只处理一条管理通道
			
			// add by chenjie 2013-10-25 运营商信息
//			if(LipossGlobals.getLipossProperty("telecom").equals(Global.TELECOM_CUC))
//			{
//				one = deviceInfoDAO.queryDevWanConnSession(list.get(0));
//			}
//			else {
				wanConnObj = deviceInfoDAO.queryDevWanConn(list.get(0));
				if (wanConnObj == null) {
					logger.warn("wanConnObj 为空");
					continue;
				}
				one = deviceInfoDAO.queryDevWanConnSession(wanConnObj);
//			}
			
			if(null!=one){
				for (int j = 0; j < one.length; j++) {
					//获取管理PVC/VLAN
					if(false == StringUtil.IsEmpty(one[j].getServList()) && 
							(one[j].getServList().contains("BOTH") 
									|| one[j].getServList().contains("TR069")  
									|| one[j].getServList().contains("Management"))){
						if(wanDao.isADSL(deviceId)){
							managePass = one[j].getPvc();
						}else{
							managePass = one[j].getVlanid();
						}
						break;
					}
				}
			}
		}

		obj.setPvc(managePass);
		
		List list2 = deviceInfoDAO.getDeviceTr069Username(deviceId);
		
		if(list2.size()>0){
			Map map = (Map) list2.get(0);
			obj.setAcsUsername(String.valueOf(map.get("acs_username")).toString());
			obj.setAcsPasswd(String.valueOf(map.get("acs_passwd")).toString());
			obj.setCpeUsername(String.valueOf(map.get("cpe_username")).toString());
			obj.setCpePasswd(String.valueOf(map.get("cpe_passwd")).toString());
		}
		
		obj.setLoopbackIp(deviceInfoDAO.getDeviceIp(deviceId));
		
		return obj;
		
	}
	
	/**
	 * 从业务用户表中取PVC值,如果获取不到就根据业务类型从参数配置表中获取
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return String
	 */
	public List<String> getServPvc(String userId, String servTypeId) {

		logger.debug("getServPvc(userId:{}, servTypeId:{})", userId,
						servTypeId);

		List<String> pvc = new ArrayList<String>();
		
		if(null == servTypeId || "".equals(servTypeId)){
			return null;
		}
		
		/**
		if(null == userId || "".equals(userId)){
			logger.warn("userId[{}]为空!",userId);;
		}else{
			HgwServUserObj servUserObj = deviceInfoDAO.getUserInfo(userId, servTypeId);
			
			//从业务用户表获取不到PVC值
			if (null != servUserObj) {
				pvc.add(servUserObj.getVpiid() + "/" + servUserObj.getVciid());
			} else {
				logger.warn("业务用户表中找不到相应的记录");
			}
		}
		**/
		
		// 从配置文件中取
		String arrPvc[] = deviceInfoDAO.getServPvc(servTypeId);
		if (null == arrPvc || arrPvc.length <= 0) {
			logger.error("userId:{}在配置表中取不到PVC信息", userId);
		} else {
			for (int i = 0; i < arrPvc.length; i++) {
				pvc.add(arrPvc[i]);
			}
		}

		return pvc;
	}

	/**
	 * 查询设备wlan关联设备情况 
	 * 
	 * @param deviceId
	 * @param lanId
	 * @param lanWlanId
	 * @return
	 */
	public List getGwWlanAsso(String deviceId,String lanId,String lanWlanId){
		return deviceInfoDAO.getGwWlanAsso(deviceId, lanId, lanWlanId);
	}
	
	/**
	 * 查询地址池
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map getGwLanHostconf(String deviceId){
		
		List gwLanHostconfList = deviceInfoDAO.getGwLanHostconf(deviceId);
		if(gwLanHostconfList.size()>0){
			return (Map)gwLanHostconfList.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * @param deviceId
	 * @return String 上行方式
	 */
	public int getAccessType(String deviceId){
		return wanDao.getAccessType(deviceId);
	}

	
	
	/**
	 * 调用corba
	 * 
	 * @param deviceId
	 * @param type
	 * @return
	 */
	public int getDataFromSG(String deviceId, int type) {
		
		logger.debug("getDataFromSG(deviceId:{}, type:{})", deviceId,
				type);
		return wanCAO.getDataFromSG(deviceId, type);
	}
	
	/**
	 * @return the deviceInfoDAO
	 */
	public I_DeviceInfoDAO getDeviceInfoDAO() {
		return deviceInfoDAO;
	}

	/**
	 * @param deviceInfoDAO
	 *            the deviceInfoDAO to set
	 */
	public void setDeviceInfoDAO(I_DeviceInfoDAO deviceInfoDAO) {
		this.deviceInfoDAO = deviceInfoDAO;
	}

	/**
	 * @return the wanCAO
	 */
	public I_CAO getWanCAO() {
		return wanCAO;
	}

	/**
	 * @param wanCAO the wanCAO to set
	 */
	public void setWanCAO(I_CAO wanCAO) {
		this.wanCAO = wanCAO;
	}

	/**
	 * @param WanDAO
	 */
	public void setWanDao(WanDAO wanDao) {
		this.wanDao = wanDao;
	}
	
	/**
	 * 查询PON设备信息
	 *
	 * @author wangsenbo
	 * @date Nov 4, 2010
	 * @param 
	 * @return PONInfoOBJ[]
	 */
	public PONInfoOBJ[] queryPONInfo(String deviceId, String userId,String accessType){
		logger.debug("PONInfoOBJ({},{})",deviceId,userId);
		return deviceInfoDAO.queryPONInfo(deviceId,accessType);
	}

	/**
	 * 获取servList
	 */
	public Map<String, List<Map>> getAllChannel(String device_id)
	{
		List<Map> list = deviceInfoDAO.getAllChannel(device_id);
		
		List<Map> internet_list = new ArrayList<Map>();
		List<Map> iptv_list = new ArrayList<Map>();
		List<Map> voip_list = new ArrayList<Map>();
		List<Map> tr069_list = new ArrayList<Map>();
		List<Map> cloudNet_list = new ArrayList<Map>();
		Map map = null;
		String servList = null;
		for(int i=0; i<list.size(); i++)
		{
			map = list.get(i);
			if(map == null || map.size()==0)
				continue;
			servList = (String)map.get("serv_list");
			if(StringUtil.IsEmpty(servList))
				continue;
			else
			{
				if(servList.toUpperCase().indexOf("INTERNET") != -1)
				{
					internet_list.add(map);
				}
				// iptv
				else if(servList.toUpperCase().indexOf("OTHER") != -1 || servList.toUpperCase().indexOf("IPTV") != -1)
				{
					iptv_list.add(map);
				}
				else if(servList.toUpperCase().indexOf("VOIP") != -1 || servList.toUpperCase().indexOf("VOICE") != -1)
				{
					voip_list.add(map);
				}
				else if(servList.toUpperCase().indexOf("TR069") != -1)
				{
					tr069_list.add(map);
				}
				else if(servList.toUpperCase().indexOf("SPECIAL_SERVICE_VR") != -1)
				{
					cloudNet_list.add(map);
				}
			}
		}
		
		Map<String, List<Map>> data = new HashMap<String, List<Map>>();
		data.put("INTERNET", internet_list);
		data.put("IPTV", iptv_list);
		data.put("TR069", tr069_list);
		data.put("VOIP", voip_list);
		if(LipossGlobals.inArea(Global.GSDX))
		{
			data.put("SPECIAL_SERVICE_VR", cloudNet_list);
		}
		return data;
	}
	
	
	public List<Map> getDeviceCheckProject(String gw_type) {
		List list = deviceInfoDAO.getDeviceCheckProject(gw_type);
		return list;
		}

}
