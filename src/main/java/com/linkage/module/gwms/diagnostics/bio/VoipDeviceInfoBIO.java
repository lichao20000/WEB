package com.linkage.module.gwms.diagnostics.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.WanRelatedCAO;
import com.linkage.module.gwms.dao.gw.LocalServParamDAO;
import com.linkage.module.gwms.dao.gw.VoiceServiceProfileDAO;
import com.linkage.module.gwms.dao.gw.VoiceServiceProfileLineDAO;
import com.linkage.module.gwms.dao.gw.WanConnDAO;
import com.linkage.module.gwms.dao.gw.WanConnSessDAO;
import com.linkage.module.gwms.dao.gw.WanDAO;
import com.linkage.module.gwms.diagnostics.dao.VoipDeviceInfoDAO;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileH248OBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileLineObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WanObj;
import com.linkage.module.gwms.util.StringUtil;
public class VoipDeviceInfoBIO {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(VoipDeviceInfoBIO.class);
	
	/** 上行方式 */
	private String acceessType;
	
	/** DAO */
	private WanDAO wanDAO;
	private WanConnDAO wanConnDAO;
	private WanConnSessDAO wanConnSessDAO;
	private LocalServParamDAO localServParamDAO;
	private VoiceServiceProfileDAO voiceServiceDAO;
	private VoiceServiceProfileLineDAO voiceServiceLineDAO;
	
	/** CAO */
	private WanRelatedCAO wanCAO;

	private Map<String, String> wanConfigMap = null;
	
	private List<Map<String, String>> wanConfigList= new ArrayList<Map<String, String>>();
	
	private VoipDeviceInfoDAO voipDeviceInfoDAO;

	
	/**
	 * 获取设备的相关信息，包括用户信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map<String, String> getDeviceInfo(String deviceId, String gw_type) {
		
		Map<String, String> deviceInfoMap = null;

		List devInfo = voipDeviceInfoDAO.getDeviceInfo(deviceId);

		if (null != devInfo && devInfo.size() > 0) {

			deviceInfoMap = new HashMap<String, String>();

			Map oneDevInfo = (Map) devInfo.get(0);

			String device_Id = StringUtil.getStringValue(oneDevInfo.get("device_id"));
			String oui = StringUtil.getStringValue(oneDevInfo.get("oui"));
			String device_serialnumber = StringUtil.getStringValue(oneDevInfo.get("device_serialnumber"));
			String loopback_ip = StringUtil.getStringValue(oneDevInfo.get("loopback_ip"));
			String vendor_add = StringUtil.getStringValue(oneDevInfo.get("vendor_add"));
			String device_model = StringUtil.getStringValue(oneDevInfo.get("device_model"));
			String hardwareversion = StringUtil.getStringValue(oneDevInfo.get("hardwareversion"));
			String softwareversion = StringUtil.getStringValue(oneDevInfo.get("softwareversion"));
			String macaddress = StringUtil.getStringValue(oneDevInfo.get("cpe_mac"));

			deviceInfoMap.put("device_Id", device_Id);
			deviceInfoMap.put("oui", oui);
			deviceInfoMap.put("device_serialnumber", device_serialnumber);
			deviceInfoMap.put("loopback_ip", loopback_ip);
			deviceInfoMap.put("vendor_add", vendor_add);
			deviceInfoMap.put("device_model", device_model);
			deviceInfoMap.put("hardwareversion", hardwareversion);
			deviceInfoMap.put("softwareversion", softwareversion);
			deviceInfoMap.put("macaddress", macaddress);
			deviceInfoMap.put("register", "已注册");
			// gw_type
			deviceInfoMap.put("gw_type", StringUtil.getStringValue(gw_type));
			
			// bbms
			if(2 ==LipossGlobals.SystemType() || (LipossGlobals.SystemType() == 0 && gw_type.equals("2"))){
				
				List userInfo = voipDeviceInfoDAO.getDeviceEUserInfo(deviceId);

				if (userInfo.size() > 0) {
					
					Map oneUserInfo = (Map) userInfo.get(0);
					String user_id = StringUtil.getStringValue(oneUserInfo.get("user_id"));
					String username = StringUtil.getStringValue(oneUserInfo.get("username"));

					deviceInfoMap.put("user_id", user_id);
					deviceInfoMap.put("username", username);

				}
			}
			// itms
			else if(1 ==LipossGlobals.SystemType() || (LipossGlobals.SystemType() == 0 && gw_type.equals("1"))){
				
				List userInfo = voipDeviceInfoDAO.getDeviceHUserInfo(deviceId);

				if (userInfo.size() > 0) {

					Map oneUserInfo = (Map) userInfo.get(0);
					String user_id = StringUtil.getStringValue(oneUserInfo.get("user_id"));
					String username = StringUtil.getStringValue(oneUserInfo.get("username"));

					deviceInfoMap.put("user_id", user_id);
					deviceInfoMap.put("username", username);
				}
			}
		}

		return deviceInfoMap;
	}
	
	
	
	/**
	 * 获取VoIP 的 servList
	 */
	public List<Map> getAllChannel(String device_id){
		List<Map> list = voipDeviceInfoDAO.getAllChannel(device_id);
		
		List<Map> voip_list = new ArrayList<Map>();
		
		Map map = null;
		String servList = null;
		for(int i=0; i<list.size(); i++)
		{
			map = list.get(i);
			if(map == null || map.size()==0)
				continue;
			servList = (String)map.get("serv_list");
			if(StringUtil.IsEmpty(servList)){
				continue;
			} else {
				if(servList.toUpperCase().indexOf("VOIP") != -1){
					voip_list.add(map);
				}
			}
		}
		return voip_list;
	}
	
	
	
	/**
	 * 获取WAN上网相关信息
	 * 
	 * @param list
	 * @return
	 */
	public List<Map<String, String>> getData(List<Map> list) {
		
		if(null == list || list.size()<0){
			logger.warn("没有获取到VoIP的servList");
			return null;
		}
		
		for(int k=0; k<list.size(); k++){
			Map map = list.get(k);
			
			String deviceId = StringUtil.getStringValue(map.get("device_id"));
			
			if ("".equals(deviceId) || null == deviceId) {
				logger.debug("getData deviceId is null");
				return null;
			}

			//gw_wan
			WanObj wanObj = wanDAO.getWan(deviceId, "1");
			if (wanObj == null) {
				logger.debug("wanObj 为空");
				this.acceessType = null;
				return null;
			}
			this.acceessType = StringUtil.getStringValue(wanObj.getAccessType());
			
			WanConnObj wanConnObj = wanConnDAO.queryDevWanConn(list.get(k));
			if (wanConnObj == null) {
				logger.warn("wanConnObj 为空");
				return null;
			}
			String vpiid = StringUtil.getStringValue(wanConnObj.getVpi_id());
			String vciid = StringUtil.getStringValue(wanConnObj.getVci_id());
			String vlan_id = StringUtil.getStringValue(wanConnObj.getVlan_id());
			
			// gw_wan_conn
			if ("DSL".equals(acceessType)) {
				wanConnObj = wanConnDAO.queryDevWanConn(deviceId, vpiid, vciid);
			} else {
				wanConnObj = wanConnDAO.queryDevWanConn(deviceId, vlan_id);
			}

			if (wanConnObj == null) {
				logger.debug("wanConnObj 为空");
				return null;
			}
			
			//gw_wan_conn_session
			WanConnSessObj[] wanConnSessObjArr = wanConnSessDAO.queryDevWanConnSession(wanConnObj);
			if (wanConnSessObjArr == null || wanConnSessObjArr.length == 0) {
				logger.debug("WanConnSessObjArr 为空");

				return null;
			}
			
			/** 目前只考虑一个PVC或VLANID对应一个Session，不考虑多Session的情况 */
			wanConfigMap = new HashMap<String, String>();
			
			wanConfigMap.put("accessType", acceessType);
			
			wanConfigMap.put("vpi",  vpiid);
			wanConfigMap.put("vci", vciid);
			wanConfigMap.put("vlanId",  vlan_id);
			
			wanConfigMap.put("wanId", StringUtil.getStringValue(wanConnSessObjArr[0].getWanId()));
			wanConfigMap.put("wanConnId",  StringUtil.getStringValue(wanConnSessObjArr[0].getWanConnId()));
			wanConfigMap.put("wanConnSessId", StringUtil.getStringValue(wanConnSessObjArr[0].getWanConnSessId()));
			if("1".equals(wanConnSessObjArr[0].getSessType())){
				wanConfigMap.put("connType",Global.G_Src_Key_Map.get("1").get(wanConnSessObjArr[0].getConnType()));
				wanConfigMap.put("connStatus",Global.G_Src_Key_Map.get("3").get(wanConnSessObjArr[0].getStatus()));
			}else{
				wanConfigMap.put("connType", Global.G_Src_Key_Map.get("2").get(wanConnSessObjArr[0].getConnType()));
				wanConfigMap.put("connStatus", Global.G_Src_Key_Map.get("4").get(wanConnSessObjArr[0].getStatus()));
			}
			wanConfigMap.put("servList",  StringUtil.getStringValue(wanConnSessObjArr[0].getServList()));
			wanConfigMap.put("ip",  StringUtil.getStringValue(wanConnSessObjArr[0].getIp()));
			wanConfigMap.put("dns", StringUtil.getStringValue(wanConnSessObjArr[0].getDns()));
			wanConfigMap.put("username",  StringUtil.getStringValue(wanConnSessObjArr[0].getUsername()));
			wanConfigMap.put("natEnable", StringUtil.getStringValue(wanConnSessObjArr[0].getNatEnable()));
			wanConfigMap.put("connError", Global.G_Src_Key_Map.get("7").get(wanConnSessObjArr[0].getConnError()));
			wanConfigMap.put("geteway", StringUtil.getStringValue(wanConnSessObjArr[0].getGateway()));
			wanConfigMap.put("mask", StringUtil.getStringValue(wanConnSessObjArr[0].getMask()));
			wanConfigMap.put("ipType", StringUtil.getStringValue(wanConnSessObjArr[0].getIpType()));
			
			wanConfigList.add(wanConfigMap);
			
			logger.debug("调用后台返回的LIST：{}", wanConfigList);
		}
		return wanConfigList;
	}
	
	/**
	 *  采集WAN链接
	 *  
	 * @param deviceId
	 * @param code
	 * @return
	 */
	public int getSuperCorba(String deviceId,int code){
		logger.debug("getSuperCorba(deviceId:{},code:{})",deviceId,code);
		return wanCAO.getDataFromSG(deviceId, code);
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
        List userInfo = voiceServiceDAO.getDeviceHUserInfo(deviceId);

        if (userInfo.size() > 0) {

            Map oneUserInfo = (Map) userInfo.get(0);
            
            String user_id = StringUtil.getStringValue(oneUserInfo.get("user_id"));
            
            if(null != user_id && !"".equals(user_id)) {
                userInfo = voiceServiceDAO.getBssVoipSheet(user_id);
                if(userInfo.size() > 0)
                {
                    oneUserInfo = (Map) userInfo.get(0);
                    return StringUtil.getStringValue(oneUserInfo.get("protocol"));
                }else {
					return null;
				}
            } else {
				return null;
			}
            
        }
        return null;
    }
	
	
    
    /**
	 * 获取VoIP详细信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List<Map<String, String>> getVoipInfo(String deviceId){
		
		logger.debug("getVoipInfo({},{})", deviceId);

		//返回值
		List<Map<String, String>> rs = new ArrayList<Map<String, String>>();
		
		VoiceServiceProfileObj[] voipProfJ = voiceServiceDAO.getVoipProf(deviceId);
		if(null != voipProfJ){
			for(int i=0;i<voipProfJ.length;i++){
				VoiceServiceProfileLineObj[] voipProfLine = voiceServiceLineDAO.getVoipProfLine(voipProfJ[i]);
				if(null != voipProfLine){
					for(int j=0;j<voipProfLine.length;j++){
						Map<String, String> oneVoip = new HashMap<String, String>();
						//Voip总体信息
						oneVoip.put("device_id",voipProfJ[i].getDeviceId());
						oneVoip.put("voip_id",voipProfJ[i].getVoipId());
						oneVoip.put("prof_id",voipProfJ[i].getProfId());
						oneVoip.put("gather_time",voipProfJ[i].getGatherTime());
						oneVoip.put("prox_serv",voipProfJ[i].getProxServ());
						oneVoip.put("prox_port",voipProfJ[i].getProxPort());
						oneVoip.put("prox_serv_2",voipProfJ[i].getProxServ2());
						oneVoip.put("prox_port_2",voipProfJ[i].getProxPort2());
						oneVoip.put("regi_serv",voipProfJ[i].getRegiServ());
						oneVoip.put("regi_port",voipProfJ[i].getRegiPort());
						oneVoip.put("stand_regi_serv",voipProfJ[i].getStandRegiServ());
						oneVoip.put("stand_regi_port",voipProfJ[i].getStandRegiPort());
						oneVoip.put("out_bound_proxy",voipProfJ[i].getOutBoundProxy());
						oneVoip.put("out_bound_port",voipProfJ[i].getOutBoundPort());
						oneVoip.put("stand_out_bound_proxy",voipProfJ[i].getStandOutBoundProxy());
						oneVoip.put("stand_out_bound_port",voipProfJ[i].getStandOutBoundPort());
						//注册信息
						oneVoip.put("enable",voipProfLine[j].getEnable());
						oneVoip.put("status",voipProfLine[j].getStatus());
						oneVoip.put("username",voipProfLine[j].getUsername());
						oneVoip.put("password",voipProfLine[j].getPassword());
                        oneVoip.put("line",voipProfLine[j].getLineId());
						rs.add(oneVoip);
					}
				}else{
					Map<String, String> oneVoip = new HashMap<String, String>();
					//Voip总体信息
					oneVoip.put("device_id",voipProfJ[i].getDeviceId());
					oneVoip.put("voip_id",voipProfJ[i].getVoipId());
					oneVoip.put("prof_id",voipProfJ[i].getProfId());
					oneVoip.put("gather_time",voipProfJ[i].getGatherTime());
					oneVoip.put("prox_serv",voipProfJ[i].getProxServ());
					oneVoip.put("prox_port",voipProfJ[i].getProxPort());
					oneVoip.put("prox_serv_2",voipProfJ[i].getProxServ2());
					oneVoip.put("prox_port_2",voipProfJ[i].getProxPort2());
					oneVoip.put("regi_serv",voipProfJ[i].getRegiServ());
					oneVoip.put("regi_port",voipProfJ[i].getRegiPort());
					oneVoip.put("stand_regi_serv",voipProfJ[i].getStandRegiServ());
					oneVoip.put("stand_regi_port",voipProfJ[i].getStandRegiPort());
					oneVoip.put("out_bound_proxy",voipProfJ[i].getOutBoundProxy());
					oneVoip.put("out_bound_port",voipProfJ[i].getOutBoundPort());
					oneVoip.put("stand_out_bound_proxy",voipProfJ[i].getStandOutBoundProxy());
					oneVoip.put("stand_out_bound_port",voipProfJ[i].getStandOutBoundPort());
					rs.add(oneVoip);
				}
			}
		}
		
		return rs;
	}
	
	
	
	
    /**
	 * 获取VoIP H.248 详细信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List<Map<String, String>> getVoipInfoH248(String deviceId){
		
		logger.debug("getVoipInfo({},{})", deviceId);

		//返回值
		List<Map<String, String>> rs = new ArrayList<Map<String, String>>();
		
		VoiceServiceProfileH248OBJ[] voipProfH248 = voipDeviceInfoDAO.getVoipProfH248(deviceId);
		if(null != voipProfH248){
			for(int i=0;i<voipProfH248.length;i++){
				VoiceServiceProfileLineObj[] voipProfLine = voipDeviceInfoDAO.getVoipProfLine(voipProfH248[i]);
				if(null != voipProfLine){
					for(int j=0;j<voipProfLine.length;j++){
						Map<String, String> oneVoip = new HashMap<String, String>();
						//Voip总体信息
						oneVoip.put("device_id",voipProfH248[i].getDeviceId());
						oneVoip.put("voip_id",voipProfH248[i].getVoipId());
						oneVoip.put("prof_id",voipProfH248[i].getProfId());
						oneVoip.put("gather_time",voipProfH248[i].getGatherTime());
						oneVoip.put("media_gateway_controler",voipProfH248[i].getMediaGatewayControler());
						oneVoip.put("media_gateway_controler_port",voipProfH248[i].getMediaGatewayControlerPort());
						oneVoip.put("media_gateway_controler_2",voipProfH248[i].getMediaGatewayControler2());
						oneVoip.put("media_gateway_controler_port_2",voipProfH248[i].getMediaGatewayControlerPort2());
						oneVoip.put("media_gateway_port",voipProfH248[i].getMediaGatewayPort());
						oneVoip.put("h248_device_id",voipProfH248[i].getH248DeviceId());
						oneVoip.put("h248_device_id_type",voipProfH248[i].getH248DeviceIdType());
						oneVoip.put("rtp_prefix",voipProfH248[i].getRtpPrefix());
						//注册信息
						oneVoip.put("enable",voipProfLine[j].getEnable());
						oneVoip.put("status",voipProfLine[j].getStatus());
						oneVoip.put("username",voipProfLine[j].getUsername());
						oneVoip.put("password",voipProfLine[j].getPassword());
                        oneVoip.put("line",voipProfLine[j].getLineId());
						rs.add(oneVoip);
					}
				}else{
					Map<String, String> oneVoip = new HashMap<String, String>();
					//Voip总体信息
					oneVoip.put("device_id",voipProfH248[i].getDeviceId());
					oneVoip.put("voip_id",voipProfH248[i].getVoipId());
					oneVoip.put("prof_id",voipProfH248[i].getProfId());
					oneVoip.put("gather_time",voipProfH248[i].getGatherTime());
					oneVoip.put("media_gateway_controler",voipProfH248[i].getMediaGatewayControler());
					oneVoip.put("media_gateway_controler_port",voipProfH248[i].getMediaGatewayControlerPort());
					oneVoip.put("media_gateway_controler_2",voipProfH248[i].getMediaGatewayControler2());
					oneVoip.put("media_gateway_controler_port_2",voipProfH248[i].getMediaGatewayControlerPort2());
					oneVoip.put("media_gateway_port",voipProfH248[i].getMediaGatewayPort());
					oneVoip.put("h248_device_id",voipProfH248[i].getH248DeviceId());
					oneVoip.put("h248_device_id_type",voipProfH248[i].getH248DeviceIdType());
					oneVoip.put("rtp_prefix",voipProfH248[i].getRtpPrefix());
					rs.add(oneVoip);
				}
			}
		}
		
		return rs;
	}
	
	



	public WanRelatedCAO getWanCAO() {
		return wanCAO;
	}

	public void setWanCAO(WanRelatedCAO wanCAO) {
		this.wanCAO = wanCAO;
	}

	public String getAcceessType() {
		return acceessType;
	}

	public WanDAO getWanDAO() {
		return wanDAO;
	}

	public void setWanDAO(WanDAO wanDAO) {
		this.wanDAO = wanDAO;
	}

	public WanConnDAO getWanConnDAO() {
		return wanConnDAO;
	}

	public void setWanConnDAO(WanConnDAO wanConnDAO) {
		this.wanConnDAO = wanConnDAO;
	}

	public WanConnSessDAO getWanConnSessDAO() {
		return wanConnSessDAO;
	}

	public void setWanConnSessDAO(WanConnSessDAO wanConnSessDAO) {
		this.wanConnSessDAO = wanConnSessDAO;
	}

	public LocalServParamDAO getLocalServParamDAO() {
		return localServParamDAO;
	}

	public void setLocalServParamDAO(LocalServParamDAO localServParamDAO) {
		this.localServParamDAO = localServParamDAO;
	}

	public VoiceServiceProfileDAO getVoiceServiceDAO() {
		return voiceServiceDAO;
	}

	public void setVoiceServiceDAO(VoiceServiceProfileDAO voiceServiceDAO) {
		this.voiceServiceDAO = voiceServiceDAO;
	}

	public VoiceServiceProfileLineDAO getVoiceServiceLineDAO() {
		return voiceServiceLineDAO;
	}

	public void setVoiceServiceLineDAO(
			VoiceServiceProfileLineDAO voiceServiceLineDAO) {
		this.voiceServiceLineDAO = voiceServiceLineDAO;
	}

//	public String getLAN1() {
//		return LAN1;
//	}
//
//	public void setLAN1(String lan1) {
//		LAN1 = lan1;
//	}
//
//	public String getLAN2() {
//		return LAN2;
//	}
//
//	public void setLAN2(String lan2) {
//		LAN2 = lan2;
//	}
//
//	public String getLAN3() {
//		return LAN3;
//	}
//
//	public void setLAN3(String lan3) {
//		LAN3 = lan3;
//	}
//
//	public String getLAN4() {
//		return LAN4;
//	}
//
//	public void setLAN4(String lan4) {
//		LAN4 = lan4;
//	}

	public Map<String, String> getWanConfigMap() {
		return wanConfigMap;
	}

	public void setWanConfigMap(Map<String, String> wanConfigMap) {
		this.wanConfigMap = wanConfigMap;
	}

	public List<Map<String, String>> getWanConfigList() {
		return wanConfigList;
	}

	public void setWanConfigList(List<Map<String, String>> wanConfigList) {
		this.wanConfigList = wanConfigList;
	}

//	public Map<String, String> getBindPortMap() {
//		return bindPortMap;
//	}
//
//	public void setBindPortMap(Map<String, String> bindPortMap) {
//		this.bindPortMap = bindPortMap;
//	}

	public VoipDeviceInfoDAO getVoipDeviceInfoDAO() {
		return voipDeviceInfoDAO;
	}

	public void setVoipDeviceInfoDAO(VoipDeviceInfoDAO voipDeviceInfoDAO) {
		this.voipDeviceInfoDAO = voipDeviceInfoDAO;
	}
	
}
