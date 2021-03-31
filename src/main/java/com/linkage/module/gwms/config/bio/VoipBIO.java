package com.linkage.module.gwms.config.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.VoipCAO;
import com.linkage.module.gwms.cao.gw.WanRelatedCAO;
import com.linkage.module.gwms.dao.gw.LanEthDAO;
import com.linkage.module.gwms.dao.gw.LocalServParamDAO;
import com.linkage.module.gwms.dao.gw.VoiceServiceProfileDAO;
import com.linkage.module.gwms.dao.gw.VoiceServiceProfileLineDAO;
import com.linkage.module.gwms.dao.gw.WanConnDAO;
import com.linkage.module.gwms.dao.gw.WanConnSessDAO;
import com.linkage.module.gwms.dao.gw.WanDAO;
import com.linkage.module.gwms.dao.gw.WlanDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.LanEthObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileH248OBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileLineObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WanObj;
import com.linkage.module.gwms.resource.dao.DevDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 应用服务模块：配置VOIP BIO（网络应用）
 * @author gongsj
 * @date 2009-7-14
 */
public class VoipBIO {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(VoipBIO.class);

	/** DAO */
	private DevDAO devDAO;
	private WanDAO wanDAO;
	private WanConnDAO wanConnDAO;
	private WanConnSessDAO wanConnSessDAO;
	private LocalServParamDAO localServParamDAO;
	private WlanDAO wlanDAO;
	private LanEthDAO lanEthDAO;
	private VoiceServiceProfileDAO voiceServiceDAO;
	private VoiceServiceProfileLineDAO voiceServiceLineDAO;
	
	/** CAO */
	private WanRelatedCAO wanCAO;
	private VoipCAO voipCAO;
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
	
	private String VOICE_PATH = "";
	private String VOICE_PROF_PATH = "";
	private String SIP_PATH = "";
	private String LINE_PATH = "";
	
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
		logger.debug("getSuperCorba(deviceId:{},code:{})",deviceId,code);
		return wanCAO.getDataFromSG(deviceId, code);
	}
	
	/**
	 * 获取WAN上网相关信息
	 * @date 2009-7-14
	 * @param deviceId
	 */
	//@SuppressWarnings("unchecked")
	public List<Map<String, String>> getData(String deviceId, String gw_type) {
		
		if (deviceId == null) {
			logger.warn("getData deviceId is null");
			return null;
		}

		//gw_wan
		WanObj wanObj = wanDAO.getWan(deviceId, "1");
		if (wanObj == null) {
			logger.warn("[{}] wanObj 为空",deviceId);
			this.acceessType = null;
			return null;
		}
		
		this.acceessType = String.valueOf(wanObj.getAccessType());
		
		// 注释 by zhangchy 2013-04-17  表local_serv_param已停止使用  ----begin-----
//		String servType = "12";
//		String[] pvcTable = localServParamDAO.getServPvc(servType);
//		if(null==pvcTable){
//			logger.error("Voip获取配置表的pvc失败！");
//			return null;
//		}
//		String[] pvcOne = pvcTable[0].split("/");
//		
//		// gw_wan_conn
//		WanConnObj wanConnObj = wanConnDAO.queryDevWanConn(deviceId, pvcOne[0], pvcOne[1]);
		// 注释 by zhangchy 2013-04-17  表local_serv_param已停止使用  ----end-----
		
		// add by zhangchy 2013-04-17 获取servList，用于下面获取wan连接的信息
		Map<String, List<Map>> allChannelMap = getAllChannel(deviceId, gw_type);
		List<Map> voip_list = allChannelMap.get("VOIP");
		if(null == voip_list || voip_list.isEmpty()){
			logger.warn("[{}] voip_list 为空",deviceId);
			return null;
		}
        WanConnObj wanConnObj = wanConnDAO.queryDevWanConn(voip_list.get(0));
        
		if (wanConnObj == null) {
			logger.warn("[{}] wanConnObj 为空",deviceId);
			return null;
		}
		
		//gw_wan_conn_session
		WanConnSessObj[] wanConnSessObjArr = wanConnSessDAO.queryDevWanConnSession(wanConnObj);
		if (wanConnSessObjArr == null || wanConnSessObjArr.length == 0) {
			logger.warn("[{}] WanConnSessObjArr 为空",deviceId);
			return null;
		}
		
		bindPortMap.put(LAN1, "LAN1");
		bindPortMap.put(LAN2, "LAN2");
		bindPortMap.put(LAN3, "LAN3");
		bindPortMap.put(LAN4, "LAN4");
		
		/** 目前只考虑一个PVC或VLANID对应一个Session，不考虑多Session的情况 */
		wanConfigMap = new HashMap<String, String>();
		
		String bindport = "";
		String bindportStr = String.valueOf(wanConnSessObjArr[0].getBindPort());
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
		
		wanConfigMap.put("vpi",  String.valueOf(wanConnObj.getVpi_id()));
		wanConfigMap.put("vci", String.valueOf(wanConnObj.getVci_id()));
		wanConfigMap.put("vlanId",  String.valueOf(wanConnObj.getVlan_id()));
		
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
		
		//wanConfigMap.put("num", String.valueOf(mwBandList.get(0).get("total_number")));
		
		wanConfigList.add(wanConfigMap);
		
		logger.debug("调用后台返回的LIST：{}", wanConfigList);
		
		return wanConfigList;
		
	}
	
	
	/**
	 * 获取servList
	 */
	public Map<String, List<Map>> getAllChannel(String device_id, String gw_type)
	{
		List<Map> list = voiceServiceDAO.getAllChannel(device_id, gw_type);
		
		List<Map> internet_list = new ArrayList<Map>();
		List<Map> iptv_list = new ArrayList<Map>();
		List<Map> voip_list = new ArrayList<Map>();
		List<Map> tr069_list = new ArrayList<Map>();
		
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
				else if(servList.toUpperCase().indexOf("OTHER") != -1||servList.toUpperCase().indexOf("IPTV") != -1)
				{
					iptv_list.add(map);
				}
				else if(servList.toUpperCase().indexOf("VOIP") != -1||servList.toUpperCase().indexOf("VOICE") != -1)
				{
					voip_list.add(map);
				}
				else if(servList.toUpperCase().indexOf("TR069") != -1)
				{
					tr069_list.add(map);
				}
			}
		}
		
		Map<String, List<Map>> data = new HashMap<String, List<Map>>();
		data.put("INTERNET", internet_list);
		data.put("IPTV", iptv_list);
		data.put("TR069", tr069_list);
		data.put("VOIP", voip_list);
		
		return data;
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
		
		VoiceServiceProfileH248OBJ[] voipProfH248 = voiceServiceDAO.getVoipProfH248(deviceId);
		if(null != voipProfH248){
			for(int i=0;i<voipProfH248.length;i++){
				VoiceServiceProfileLineObj[] voipProfLine = voiceServiceDAO.getVoipProfLine(voipProfH248[i]);
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
            
            if(null != user_id && !"".equals(user_id))
            {
                userInfo = voiceServiceDAO.getBssVoipSheet(user_id);
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
	public String addWanRelatedConn(String isAddWan,long accOId, WanObj wanObj, WanConnObj wanConnObj, 
			WanConnSessObj wanConnSessObj, VoiceServiceProfileObj voipProfObj, VoiceServiceProfileLineObj voipProfLineObj) {
		
		int voipServiceId = 1101;
		
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
		
		String sheetPara = null;
		
		if ("ADSL".equals(accessType)) {
			logger.debug("ADSL上行");
			
			if ("1".equals(sessionType) && "PPPoE_Bridged".equals(connType)) {
				//桥接IPTV
				voipServiceId = 1001;
				
			} else if ("1".equals(sessionType) && "IP_Routed".equals(connType)) {
				//路由IPTV
				voipServiceId = 1008;
				
			} else if ("2".equals(sessionType) && "DHCP".equals(ipType)) {
				//DHCP
				
			} else if ("2".equals(sessionType) && "Static".equals(ipType)) {
				//静态IP
				voipServiceId = 1010;
				
			} else {
				logger.error("不能确定service_id");
				return "false";
			}
		} else {
			logger.debug("LAN上行");
			if ("1".equals(sessionType) && "PPPoE_Bridged".equals(connType)) {
				//桥接IPTV
				voipServiceId = 2001;
				
			} else if ("1".equals(sessionType) && "IP_Routed".equals(connType)) {
				//路由IPTV
				voipServiceId = 2008;
				
			} else if ("2".equals(sessionType) && "DHCP".equals(ipType)) {
				//DHCP
				
			} else if ("2".equals(sessionType) && "Static".equals(ipType)) {
				//静态IP
				voipServiceId = 2010;
				
			} else {
				logger.error("不能确定service_id");
				return "false";
			}
		}
		
		sheetPara = voipObj2Xml(isAddWan,voipServiceId, wanObj, wanConnObj, wanConnSessObj, voipProfObj, voipProfLineObj);
		
		strategyOBJ.setServiceId(1401);
		
		strategyOBJ.setSheetPara(sheetPara.toString());

		strategyOBJ.setAccOid(accOId);
		strategyOBJ.setOrderId(1);
		strategyOBJ.setType(iptvStrategyType);
		
		//是新类型的策略，策略参数为XML，组装模板
		strategyOBJ.setSheetType(2);
		
		strategyOBJ.setTempId(1401);
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
	 * 删除VoIP连接
	 * @author gongsj
	 * @date 2009-7-16
	 * @param wanConnObj
	 * @return
	 */
	public boolean delVoipInfo(VoiceServiceProfileObj voiceObj) {
		
		int delResult = voipCAO.delVoipInfo(voiceObj);
		
		if (0 == delResult || 1 == delResult) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 配置VOIP
	 * @date 2009-7-2
	 * @param wanConnObj
	 * @param wanConnSessObj
	 * @param VsProfileObj
	 * @param VsProfileLineObj
	 * @return
	 */
	public boolean configVoip(WanConnObj wanConnObj, WanConnSessObj wanConnSessObj,
			VoiceServiceProfileObj vsProfileObj, VoiceServiceProfileLineObj vsProfileLineObj) {
		
		wanId = wanConnSessObj.getWanId();
		wanConnId = wanConnSessObj.getWanConnId();
		wanConnSessId = wanConnSessObj.getWanConnSessId();
		
		String vpi     = wanConnObj.getVpi_id();
		String vci     = wanConnObj.getVci_id();
		String vlanId = wanConnObj.getVlan_id();
		String voipId  = vsProfileLineObj.getVoipId();
		String profId  = vsProfileLineObj.getProfId();
		String lineId  = vsProfileLineObj.getLineId();
		
		String pvcPath = null;
		String vlanIdPath = null;
		String connTypePath = null;
		String connStatusPath = null;
		String proxServPath   = null;
		String proxPortPath   = null;
		String proxServ2Path  = null;
		String proxPort2Path  = null;
		String voipUsernamePath = null;
		String voipPasswordPath = null;
		
		if (vlanId == null) {
			logger.debug("该连接为PPP，设置其相关参数");
			pvcPath        = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PVC_PATH;
			connTypePath   = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PPP_PATH + wanConnSessId + ".ConnectionType";
			connStatusPath = WAN_PATH + wanId + WANCONN_PATH + wanConnId + PPP_PATH + wanConnSessId + ".ConnectionStatus";
			
			paramNames.append(pvcPath).append("|").append(connTypePath).append("|").append(connStatusPath).append("|");

			paramValues.append("PVC:"+vpi+"/"+vci).append("|").append(wanConnSessObj.getConnType()).append("|")
					   .append(wanConnSessObj.getStatus()).append("|");

			paramTypes.append("1").append("|").append("1").append("|").append("1").append("|");
			
		} else {
			logger.debug("该连接为IP，设置其相关参数");
			
			vlanIdPath     = WAN_PATH + wanId + WANCONN_PATH + wanConnId + VLANID_PATH;
			connTypePath   = WAN_PATH + wanId + WANCONN_PATH + wanConnId + IP_PATH + wanConnSessId + ".ConnectionType";
			connStatusPath = WAN_PATH + wanId + WANCONN_PATH + wanConnId + IP_PATH + wanConnSessId + ".ConnectionStatus";
			
			paramNames.append(vlanIdPath).append("|").append(connTypePath).append("|").append(connStatusPath).append("|");

			paramValues.append(vlanId).append("|").append(wanConnSessObj.getConnType()).append("|")
					   .append(wanConnSessObj.getStatus()).append("|");

			paramTypes.append("1").append("|").append("1").append("|").append("1").append("|");
	
		}
		
		proxServPath    = VOICE_PATH + voipId + VOICE_PROF_PATH + profId + SIP_PATH + "ProxyServer";
		proxPortPath    = VOICE_PATH + voipId + VOICE_PROF_PATH + profId + SIP_PATH + "ProxyServerPort";
		proxServ2Path   = VOICE_PATH + voipId + VOICE_PROF_PATH + profId + SIP_PATH + "X_CT-COM_Standby-ProxyServer";
		proxPort2Path   = VOICE_PATH + voipId + VOICE_PROF_PATH + profId + SIP_PATH + "X_CT-COM_Standby-ProxyServerPort";
		
		voipUsernamePath = VOICE_PATH + voipId + VOICE_PROF_PATH + profId + LINE_PATH + lineId + SIP_PATH + "AuthUserName";
		voipPasswordPath = VOICE_PATH + voipId + VOICE_PROF_PATH + profId + LINE_PATH + lineId + SIP_PATH + "AuthPassword";
		
		paramNames.append(proxServPath).append("|").append(proxPortPath).append("|").append(proxServ2Path).append("|")
			      .append(proxPort2Path).append("|").append(voipUsernamePath).append("|").append(voipPasswordPath);

		paramValues.append(vsProfileObj.getProxServ()).append("|").append(vsProfileObj.getProxPort()).append("|")
				   .append(vsProfileObj.getProxServ2()).append("|").append(vsProfileObj.getProxPort2())
				   .append("|").append(vsProfileLineObj.getUsername()).append("|").append(vsProfileLineObj.getPassword());

		paramTypes.append("1").append("|").append("3").append("|").append("1").append("|").append("3")
	       		  .append("|").append("1").append("|").append("1");
		
		paramsnameArr = paramNames.toString().split("\\|");
		paramsValueArr = paramValues.toString().split("\\|");
		paraTypeIdArr = paramTypes.toString().split("\\|");
		
		paramNames = null;
		paramValues = null;
		paramTypes = null;
		
		for (int i = 0; i < paramsnameArr.length; i++) {
			logger.debug("名:{}；值:{}；属性:{}", new Object[]{paramsnameArr[i], paramsValueArr[i], paraTypeIdArr[i]});
		}
		
		//return CAO.realSetParams(deviceId, paramsnameArr, paramsValueArr, paraTypeIdArr);
		
		return false;
	}
	
	/**
	 * VOIP对象生成XML
	 * @author gongsj
	 * @date 2009-7-23
	 * @param voipServiceId
	 * @param wanObj
	 * @param wanConnObj
	 * @param wanConnSessObj
	 * @param voipProfObj
	 * @param voipProfLineObj
	 * @return
	 */
	private String voipObj2Xml(String isAddWan,int voipServiceId, WanObj wanObj, WanConnObj wanConnObj, 
			WanConnSessObj wanConnSessObj, VoiceServiceProfileObj voipProfObj, VoiceServiceProfileLineObj voipProfLineObj) {
		
		logger.debug("进入voipObj2Xml...");
		
		String strXml = null;
		if(null == wanObj || null == wanConnObj || null == wanConnSessObj || null == voipProfObj || null == voipProfLineObj){
			logger.debug("voipObj2Xml中有对象为空");
			return null;
		}
		
		//new doc
		Document doc = DocumentHelper.createDocument();
		//root node: Voip
		Element root = doc.addElement("VOIP");
		Element wan = root.addElement("WAN");
		if("1".equals(isAddWan)){
			wan.addAttribute("flag", "1");
		}else{
			wan.addAttribute("flag", "0");
		}
		
		wan.addElement("AccessType").addText(wanObj.getAccessType());
		wan.addElement("ServiceId").addText(String.valueOf(voipServiceId));
		wan.addElement("Pvc").addText("PVC:"+wanConnObj.getVpi_id()+"/"+wanConnObj.getVci_id());
		wan.addElement("BindPort").addText(null == wanConnSessObj.getBindPort() ? "" : wanConnSessObj.getBindPort());
		wan.addElement("Username").addText(null == wanConnSessObj.getUsername() ? "" : wanConnSessObj.getUsername());
		wan.addElement("Password").addText(null == wanConnSessObj.getPassword() ? "" : wanConnSessObj.getPassword());
		
		wan.addElement("VlanId").addText(null == wanConnObj.getVlan_id() ? "" : wanConnObj.getVlan_id());
		wan.addElement("Ip").addText(null == wanConnSessObj.getIp() ? "" : wanConnSessObj.getIp());
				
		wan.addElement("Mask").addText(null == wanConnSessObj.getMask() ? "" : wanConnSessObj.getMask());
		wan.addElement("Gateway").addText(null == wanConnSessObj.getGateway() ? "" : wanConnSessObj.getGateway());
		
		wan.addElement("Dns").addText(null == wanConnSessObj.getDns() ? "" : wanConnSessObj.getDns());
		
		Element services = root.addElement("Services");
		services.addAttribute("flag", "1");
		
		Element voiceService = services.addElement("VoiceService");
		Element voiceProfile = voiceService.addElement("VoiceProfile");
		
		Element voiceSip = voiceProfile.addElement("SIP");
		voiceSip.addElement("ProxyServer").addText(null == voipProfObj.getProxServ() ? "" : voipProfObj.getProxServ());
		voiceSip.addElement("ProxyServerPort").addText(null == voipProfObj.getProxPort() ? "" : voipProfObj.getProxPort());
		voiceSip.addElement("X_CT-COM_Standby-ProxyServer").addText(null == voipProfObj.getProxServ2() ? "" : voipProfObj.getProxServ2());
		voiceSip.addElement("X_CT-COM_Standby-ProxyServerPort").addText(null == voipProfObj.getProxPort2() ? "" : voipProfObj.getProxPort2());
		
		voiceSip.addElement("RegistrarServer").addText(null == voipProfObj.getRegiServ() ? "" : voipProfObj.getRegiServ());
		voiceSip.addElement("RegistrarServerPort").addText(null == voipProfObj.getRegiPort() ? "" : voipProfObj.getRegiPort());
		voiceSip.addElement("X_CT-COM_Standby-RegistrarServer").addText(null == voipProfObj.getStandRegiServ() ? "" : voipProfObj.getStandRegiServ());
		voiceSip.addElement("X_CT-COM_Standby-RegistrarServerPort").addText(null == voipProfObj.getStandRegiPort() ? "" : voipProfObj.getStandRegiPort());
		voiceSip.addElement("OutboundProxy").addText(null == voipProfObj.getOutBoundProxy() ? "" : voipProfObj.getOutBoundProxy());
		voiceSip.addElement("OutboundProxyPort").addText(null == voipProfObj.getOutBoundPort() ? "" : voipProfObj.getOutBoundPort());
		voiceSip.addElement("X_CT-COM_Standby-OutboundProxy").addText(null == voipProfObj.getStandOutBoundProxy() ? "" : voipProfObj.getStandOutBoundProxy());
		voiceSip.addElement("X_CT-COM_Standby-OutboundProxyPort").addText(null == voipProfObj.getStandOutBoundPort() ? "" : voipProfObj.getStandOutBoundPort());
		
		Element line = voiceProfile.addElement("Line");
		line.addElement("Enable").addText(null == voipProfLineObj.getEnable() ? "" : voipProfLineObj.getEnable());
		
		Element lineSip = line.addElement("SIP");
		lineSip.addElement("AuthUserName").addText(null == voipProfLineObj.getUsername() ? "" : voipProfLineObj.getUsername());
		lineSip.addElement("AuthPassword").addText(null == voipProfLineObj.getPassword() ? "" : voipProfLineObj.getPassword());
		
		strXml = doc.asXML();
		
		logger.debug("VOIP对象生成XML：{}", strXml);
		return strXml;
	}
	
	
	/**
	 * 获得VOIP初始化的相关参数
	 * @author gongsj
	 * @date 2009-8-12
	 * @param cityId
	 * @return
	 */
	public Map<String, String> getVoipSIP(String deviceId) {
		return localServParamDAO.getVoipInitParam(deviceId);
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
		
		logger.debug("绑定端口的XML：{}", html.toString());
		
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

	public void setLocalServParamDAO(LocalServParamDAO localServParamDAO) {
		this.localServParamDAO = localServParamDAO;
	}

	public void setWlanDAO(WlanDAO wlanDAO) {
		this.wlanDAO = wlanDAO;
	}

	public void setLanEthDAO(LanEthDAO lanEthDAO) {
		this.lanEthDAO = lanEthDAO;
	}

	/**
	 * @return the voiceServiceDAO
	 */
	public VoiceServiceProfileDAO getVoiceServiceDAO() {
		return voiceServiceDAO;
	}

	/**
	 * @param voiceServiceDAO the voiceServiceDAO to set
	 */
	public void setVoiceServiceDAO(VoiceServiceProfileDAO voiceServiceDAO) {
		this.voiceServiceDAO = voiceServiceDAO;
	}

	/**
	 * @return the voiceServiceLineDAO
	 */
	public VoiceServiceProfileLineDAO getVoiceServiceLineDAO() {
		return voiceServiceLineDAO;
	}

	/**
	 * @param voiceServiceLineDAO the voiceServiceLineDAO to set
	 */
	public void setVoiceServiceLineDAO(
			VoiceServiceProfileLineDAO voiceServiceLineDAO) {
		this.voiceServiceLineDAO = voiceServiceLineDAO;
	}

	/**
	 * @return the voipCAO
	 */
	public VoipCAO getVoipCAO() {
		return voipCAO;
	}

	/**
	 * @param voipCAO the voipCAO to set
	 */
	public void setVoipCAO(VoipCAO voipCAO) {
		this.voipCAO = voipCAO;
	}
	
}
