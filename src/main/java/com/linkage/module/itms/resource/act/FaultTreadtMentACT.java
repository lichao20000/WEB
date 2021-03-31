package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.litms.software.VersionManage;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.diagnostics.bio.interf.I_DeviceInfoBIO;
import com.linkage.module.gwms.diagnostics.obj.PONInfoOBJ;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;
import com.linkage.module.gwms.obj.gw.GwTr069OBJ;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.itms.resource.bio.FaultTreadtMentBIO;

/**
 * 
 * @author os_hanzz
 * date 2015-06-09
 */
public class FaultTreadtMentACT implements ServletRequestAware,SessionAware
{
	// 日志操作
	Logger logger = LoggerFactory.getLogger(FaultTreadtMentACT.class);

	private FaultTreadtMentBIO bio;

	private String queryParam;
	private String usernamereturn;
	private String device_serialnumberreturn;
	private String queryType;
	private String device_id_judge;

	private String deviceinfo;
	private Map deviceMap;
	private List showList;
	private List userList;
	private List deviceList;
	private Map userMap;
	private Map<String, Object> session;

	private List<Map> serlist;
	private List<Map<String, String>> netAccList = null;
	private List<Map<String, String>> serTypeList;
	private List configInfoList;
	private Map<String,String> bssIssuedConfigMap;
	private HttpServletRequest request;

	private String deviceSN;
	private String ajax;

	private String msg;

	private String device_id;

	private String user_id;

	private String oid;

	private Map<String,String> softVersionMap;


	private String serUsername;

	private String servType_id;

	private String servstauts;

	private String wanType;

	private String servTypeId;

	private String netServUp;

	private String isRealtimeQuery;

	private List bssSheetList;

	private List bssParaList;

	private String city_id;

	private String gw_type;

	// 类型名字
	private String type_name;

	private String nameType;

	private String userName;

	private String device_serialnumber;
	private String instArea;
	private String chkinstuser;
	private String servUserName = null;

	private String _userCityId;

	private String _userId;

	private String deviceCityId;

	private String _username;

	private String oui;

	private String oid_type;

	private String type;

	private String flag;

	private String strategy_type;

	private String vlanIdMark;

	private String ssid;

	private String wireless_port;

	private String buss_level;

	private String channel;

	private String awifi_type;

	private String netNum;
	// 符合条件的loid
	private static String isSuccess = "1";
	/**
	 * 注入
	 */
	I_DeviceInfoBIO deviceInfoBIO;
	/**
	 * 宽带上网
	 */
	private String wideNetMsg = null;
	/**
	 * IPTV
	 */
	private String iptvMsg = null;
	/**
	 * VOIP
	 */
	private String voipMsg = null;
	/**
	 * 线路信息
	 */
	private String wireMsg = null;

	/**
	 * 管理通道信息
	 */
	private String tr069Msg = null;

	/**
	 * LAN侧信息
	 */
	private String lanMsg = null;
	/**
	 * 宽带信息
	 */
	List<WanConnSessObj> wideNetInfoList = null;
	/**
	 * IPTV
	 */
	List<WanConnSessObj> iptvInfoList = null;
	/**
	 * voipInfo
	 */
	List<Map> voipInfoList = null;
	/**
	 * ACS Corba
	 */
	private ACSCorba acsCorba = new ACSCorba();
	/**
	 * 调用corba返回状态
	 */
	private String corbaMsg = null;
	/**
	 * VOIP协议类型0-IMS SIP,1-软交换 SIP,2-H.248
	 */
	private String voipProtocalTypeStr;
	/**
	 * WLAN侧信息
	 */
	private String wlanMsg = null;
	/**
	 * LAN测信息
	 */
	List lanEthList = null;
	/**
	 * 地址池信息
	 */
	Map gwLanHostconfMap = null;

	/**
	 * WLAN测信息
	 */
	List wlanList = null;
	/**
	 * 管理通道信息
	 */
	GwTr069OBJ gwTr069OBJ = null;
	//上行方式：1:ADSL 2:Ethernet(LAN) 3:EPON 4:POTS -1:未知
	private String accessType;
	/**
	 * 线路信息
	 */
	DeviceWireInfoObj[] wireInfoObjArr = null;
	//pon信息
	private PONInfoOBJ[] ponInfoOBJArr = null;
	// pon上行类别，分为Epon和Gpon
	private String pon_type = "EPON";
	/**
	 * itms设备绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		if (true == StringUtil.IsEmpty(_userCityId))
		{
			_userCityId = curUser.getCityId();
		}
		ajax = bio.itmsInst(curUser.getUser().getId(), _userId,
				_username, _userCityId, device_id, deviceCityId, oui, deviceSN, curUser
				.getUser().getAccount(), 1, 1 ,gw_type);
		return "ajax";
	}
	public String queryConnMsgACT()
	{	
		ajax = bio.queryConnMsgBIO(device_id_judge);
		return "ajax";
	}
	public String queryUserAndDeviceInfo()
	{
		String goalPage = "querydata";
		List list = bio.queryUserAndDeviceInfo(queryParam, queryType,isRealtimeQuery);
		if(0!=list.size()&&null!=list){
			//取list最后一个值判断是否有添加 标记区分单条和多条记录
			Map<String, String> showMap = new HashMap();
			if(0 != ((List) list.get(list.size()-1)).size()){
				showMap = (Map<String, String>) ((List)list.get(list.size()-1)).get(0);
			}
			if(showMap.containsKey("showList")){
				showList = new ArrayList<Map<String,String>>();
				list.remove(list.size()-1);
				showList.addAll(list);
				goalPage = "querydatalist";
			}else{
				deviceMap = (Map) list.get(0);
				userMap = (Map) list.get(1);
				serlist = (List) list.get(2);
				if(null != serlist){
					
					netAccList = new ArrayList<Map<String, String>>();
					for(Map mapnew:serlist){
						if("10".equals(mapnew.get("serv_type_id"))){
							String serUsername = (String)mapnew.get("serusername");
							Map<String, String> map = new HashMap<String, String>();
							map.put("netAcc_id", "10");
							map.put("netAcc_name", serUsername);
							netAccList.add(map);
						}
					}
				}
				serTypeList=(List<Map<String, String>>) list.get(3);
				if(deviceMap == null && userMap == null)
				{
					msg = "false";
				}else{
					msg = "true";
				}
				goalPage = "querydata";
			}

		}else{
			msg = "false";
			goalPage = "querydata";
		}
		return goalPage;
	}
	//查询出多条记录时点击单条记录详细信息（根据username查询,如果为空则根据device_serialnumber）
	public String queryUserAndDeviceInfoList()
	{
		if(null != usernamereturn&&usernamereturn!=""){
			String username = "username";
			queryParam = usernamereturn;
			queryType = username;

		}else if(null != device_serialnumberreturn&&device_serialnumberreturn!=""){
			String devicesn = "devicesn";
			queryParam = device_serialnumberreturn;
			queryType = devicesn;
		}
		List list = bio.queryUserAndDeviceInfo(queryParam, queryType,isRealtimeQuery);
		if(0==list.size()||null==list){
			msg = "false";
			return "querydata";
		}
		deviceMap = (Map) list.get(0);
		userMap = (Map) list.get(1);
		serlist = (List) list.get(2);
		if(null != serlist){
			
			netAccList = new ArrayList<Map<String, String>>();
			for(Map mapnew:serlist){
				if("10".equals(mapnew.get("serv_type_id"))){
					String serUsername = (String)mapnew.get("serusername");
					Map<String, String> map = new HashMap<String, String>();
					map.put("netAcc_id", "10");
					map.put("netAcc_name", serUsername);
					netAccList.add(map);
				}
			}
		}
		serTypeList=(List<Map<String, String>>) list.get(3);
		if(deviceMap == null && userMap == null)
		{
			msg = "false";
		}else{
			msg = "true";
		}

		return "querydata";
	}
	/**
	 * 重启
	 * @return
	 */
	public String Devicerestart()
	{
		logger.warn("Devicerestart->into");
		ajax = bio.Devicerestart(request);
		return "ajax";
	}

	/**
	 * 获取软件版本
	 * @return
	 */
	public String querySoftVersion()
	{
		softVersionMap = bio.querySoftVersion(device_id);
		return "versioninfo";
	}

	/**
	 * 软件升级
	 * @return
	 */
	public String softUpgrade()
	{
		String prot_type = request.getParameter("prot_type");
		if (prot_type == null)
			prot_type = "";
		String[] device_list = request.getParameterValues("device_id");
		//由数据库中查询获得采集点，而不是从页面中
		DeviceAct act = new DeviceAct();
		HashMap deviceInfo = act.getDeviceInfo(device_list[0]);
		String strGatherId = "";
		String devicetype_id = "";
		if (deviceInfo != null && !deviceInfo.isEmpty())
		{
			strGatherId = (String) deviceInfo.get("gather_id");
			devicetype_id = (String) deviceInfo.get("devicetype_id");
		}
		String execu_type = request.getParameter("excute_type");
		String strSql = "";
		String errorSheet = "";
		//策略定制,判断这个策略是否合法
		UserRes current_user = (UserRes) session.get("curUser");
		long user_id = current_user.getUser().getId();
		VersionManage versionManage =new VersionManage();
		String strategyId = versionManage.softup(request, devicetype_id, user_id);
		logger.warn("softUpgrade->strategyId={}",strategyId);
		if(!StringUtil.IsEmpty(strategyId))
		{
			ajax="升级成功";
		}else
		{
			ajax="升级失败";
		}
		return "ajax";
	}


	/**
	 * 配置信息
	 * @return
	 */
	public String getConfigInfo(){
		configInfoList = bio.getConfigInfo(device_id, servType_id, servstauts, wanType,isRealtimeQuery,serUsername);
		return "configInfo";
	}

	/**
	 * 查询工单详细信息
	 * 
	 * @author wangsenbo
	 * @date Sep 14, 2010
	 * @param
	 * @return String
	 */
	public String getBssSheet()
	{
		// 当使用宽带业务信息表查询数据时
		if (("10".equals(servTypeId) && "true".equals(netServUp)) || ("25".equals(servTypeId) && "true".equals(netServUp)))
		{
			bssSheetList = bio.getInternetBssSheet(city_id, user_id, servTypeId, serUsername, gw_type,isRealtimeQuery);
			return "internet";
		}

		bssSheetList = bio.getBssSheet(city_id, user_id, servTypeId, serUsername, gw_type,isRealtimeQuery);
		bssParaList = bio.getBssParaList();
		if ("10".equals(servTypeId))
		{
			return "internet";
		}
		else if ("11".equals(servTypeId))
		{
			return "iptv";
		}
		// 路由宽带
		else if ("16".equals(servTypeId))
		{
			return "router";
		}
		else
		{
			return "bssSheet";
		}
	}

	/**
	 * 配置下发信息
	 * @return
	 */
	public String getBssIssuedConfigDetail(){
		bssIssuedConfigMap = bio.getBssIssuedConfigDetail(user_id, servTypeId,isRealtimeQuery,serUsername);
		return "bssIssued";
	}

	/**
	 * itms查询用户信息 绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String getInstUserInfo()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		gw_type="1";
		userList = bio.queryUser(gw_type,cityId, userName, nameType,
				device_serialnumber, "all");
		instArea = Global.instAreaShortName;
		chkinstuser = LipossGlobals.getLipossProperty("chkinstuser");
		if (true == StringUtil.IsEmpty(servUserName))
		{
			servUserName = null;
		}
		if (userList.isEmpty())
		{
			userList = null;
		}
		return "userinfo";
	}
	/**
	 * itms解绑
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String release()
	{
		logger.warn("release()");
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio.itmsRelease(user_id, userName, city_id, device_id,
				curUser.getUser().getAccount(), 1);
		logger.warn(ajax);
		return "ajax";
	}

	/**
	 * 获取设备 进行绑定操作
	 * @return
	 */
	public String queryDevice()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		instArea = Global.instAreaShortName;
		deviceList = bio.queryDevice(deviceinfo,nameType,gw_type,cityId);
		return "deviceinfo";
	}

	/**
	 * 是否支持awifi开通
	 * @param deviceId
	 * @return
	 */
	public String isAwifi() 
	{
		logger.warn("WirelessBusinessCtrlACT->isAwifi()");
		ajax = bio.isAwifi(device_id);
		return "ajax";
	}

	public String doConfig()
	{
		logger.warn("WirelessBusinessCtrlACT->doConfig()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		long userid = curUser.getUser().getId();
		List<String> list = new ArrayList<String>();
		String[] result = this.getStringValue(device_id).split(",");
		if (!result[0].equals(isSuccess)) {
			ajax = result[1];

			return "ajax";
		} else {
			ajax = result[0];
			list.add(device_id);
			logger.warn("---hanzz--act.device_id={}",device_id);
		}
		ajax = bio.doConfig(userid, list, gw_type, flag, strategy_type,
				vlanIdMark, ssid, wireless_port, buss_level, channel, awifi_type);
		return "ajax";
	}

	public String isHaveStrategy() {
		logger.debug("WirelessBusinessCtrlACT->isHaveStrategy()");
		ajax = bio.isHaveStrategy(device_id);
		return "ajax";
	}

	public String doExecute(){
		logger.debug("doExecute({})",serUsername);

		if (null == gw_type || "".equals(gw_type)) {
			gw_type = "1";
		}
		if("1".equals(flag)){
			ajax = bio.sendSSIDSheet(serUsername,city_id,gw_type,netNum);
		}else{
			ajax = bio.sendCloseSSIDSheet(serUsername,city_id,gw_type);
		}

		return "ajax";
	}

	public String getStringValue(String deviceId) {
		String msn = bio.checkLoid(device_id, gw_type);
		return msn;
	}
	/**
	 * 根据宽带帐号查询用户是否存在
	 * 该帐号为业务路宽带
	 * @return
	 */
	public String getServUserInfo(){
		logger.debug("getServUserInfo({})",serUsername);
		if (null == gw_type || "".equals(gw_type)) {
			gw_type = "1";
		}
		ajax = bio.getServUserInfo(serUsername,gw_type);
		return "ajax";
	}

	public String getAllInfo()
	{
		int rsint = deviceInfoBIO.getDataFromSG(device_id, 0);
		logger.debug("rsint:{}",rsint);

		if (rsint != 1) {
			//logger.warn("getData sg fail:{}", rsint);

			if(null == Global.G_Fault_Map.get(rsint) || "".equals(Global.G_Fault_Map.get(rsint))){
				this.wideNetMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				this.iptvMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				this.voipMsg = Global.G_Fault_Map.get(100000).getFaultReason();
				this.wireMsg = Global.G_Fault_Map.get(100000).getFaultReason();
			}else{
				this.wideNetMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.iptvMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.voipMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.wireMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			}

			if (null == this.wideNetMsg) { 
				this.wideNetMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				this.iptvMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				this.voipMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				this.wireMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
		}else{

			// 根据device_id查询session表，查看具体业务
			Map<String, List<Map>> allChannelMap = deviceInfoBIO.getAllChannel(device_id);
			List<Map> internet_list = allChannelMap.get("INTERNET");
			List<Map> iptv_list = allChannelMap.get("IPTV");
			//List<Map> voip_list = allChannelMap.get("VOIP");
			List<Map> tr069_list = allChannelMap.get("TR069");

			//宽带信息
			if(internet_list == null || internet_list.size()==0)
			{
				this.wideNetInfoList = null;
			}
			else
			{  
				this.wideNetInfoList = deviceInfoBIO.getWideNetInfo(internet_list);
				if(null == this.wideNetInfoList || this.wideNetInfoList.size()<1){
					this.wideNetMsg = "该设备没有开通此业务!";
				}
			}

			//iptv
			//						if(2!=LipossGlobals.SystemType()){
			if(!Global.GW_TYPE_BBMS.equals(gw_type)){
				if(iptv_list == null || iptv_list.size()==0)
				{
					this.iptvInfoList = null;
				}
				else
				{
					this.iptvInfoList = deviceInfoBIO.getIptvInfo(iptv_list);
					if(null == this.iptvInfoList || this.iptvInfoList.size()<1){
						this.iptvMsg = "该设备没有开通此业务!";
					}
				}


				//voip
				//rsint = deviceInfoBIO.getDataFromSG(deviceId, 34);
				if (rsint != 1) {
					this.voipMsg = Global.G_Fault_Map.get(rsint).getFaultReason();

					if (null == this.voipMsg) { 
						this.voipMsg = Global.G_Fault_Map.get(100000).getFaultReason();
					}
				}else{
					//								if(voip_list == null || voip_list.size()==0)
					//								{
					//									this.voipInfoList = null;
					//								}
					//								else
					//								{
					//调用方法来获取VOIP信息

					this.voipInfo();
					//									this.voipInfoList = deviceInfoBIO.getVoipInfo(voip_list);
					//									if(null == this.voipInfoList || this.voipInfoList.size()<1){
					//										this.voipMsg = "该设备没有开通此业务!";
					//									}
					//								}
				}
			}
			accessType = String.valueOf(deviceInfoBIO.getAccessType(device_id));
			if("1".equals(accessType)){//ADSL
				//线路
				this.wireInfoObjArr = deviceInfoBIO.queryDevWireInfo(device_id,user_id);
			}else if("2".equals(accessType)){//LAN
				this.wireInfoObjArr = deviceInfoBIO.queryDevWireInfo(device_id,user_id);
			}else if("3".equals(accessType)){//EPON
				ponInfoOBJArr = deviceInfoBIO.queryPONInfo(device_id,user_id,accessType);
				pon_type = "EPON";

			}else if("4".equals(accessType)){//GPON
				ponInfoOBJArr = deviceInfoBIO.queryPONInfo(device_id,user_id,accessType);
				pon_type = "GPON";
			}else{
				//线路
				this.wireInfoObjArr = deviceInfoBIO.queryDevWireInfo(device_id,user_id);
			}

			//管理通道
			//rsint = deviceInfoBIO.getDataFromSG(deviceId, 5);

			if (rsint != 1) {
				this.tr069Msg = Global.G_Fault_Map.get(rsint).getFaultReason();

				if (null == this.tr069Msg) { 
					this.tr069Msg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				}
			}else{
				if(tr069_list == null || tr069_list.size()==0)
				{
					this.gwTr069OBJ = null;
				}
				else
				{
					this.gwTr069OBJ = deviceInfoBIO.getTr09Info(tr069_list);
				}
			}

			//lan、wlan
			/**
			 * 取全部，暂注释
			 */
			//rsint = deviceInfoBIO.getDataFromSG(deviceId, 1);

			if (rsint != 1) {
				logger.warn("getData sg fail");
				this.lanMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
				this.wlanMsg = Global.G_Fault_Map.get(rsint).getFaultReason(); 
				if (null == this.lanMsg) { 
					this.lanMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
					this.wlanMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
				}
			}else{
				this.lanEthList = deviceInfoBIO.queryLanEth(device_id,user_id);
				this.gwLanHostconfMap = deviceInfoBIO.getGwLanHostconf(device_id);
				this.wlanList = deviceInfoBIO.getWalnData(device_id,user_id);
			}
		}
		return "allinfo";
	}

	/**
	 * VOIP
	 */
	public String voipInfo(){

		logger.debug("voipInfo()");
		logger.debug("voipInfo:deviceId=>{},userId=>{}",device_id,user_id);

		//清除缓存
		String voipProtocalType = null;
		voipInfoList = null;

		//先获取VOIP协议类型
		//InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CT-COM_ServerType
		//江西的ServerType不是通过采集获得，直接去数据查询，所以去掉采集的部分
		if(Global.JXDX.equals(Global.instAreaShortName)){
			String voipProtocal = deviceInfoBIO.getBssVoipSheetProtocalByDeviceId(device_id);
			if(null != voipProtocal && !"".equals(voipProtocal.trim()))
			{
				voipProtocalType = voipProtocal;
			}
		}else{
			acsCorba = new ACSCorba(gw_type);

			String node = "";
			// "CUC"; //联通  "CMC"; //移动    "CTC"; //电信
			if(LipossGlobals.getLipossProperty("telecom").equals("CUC"))
			{
				node = "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CU_ServerType";
			}else if (LipossGlobals.getLipossProperty("telecom").equals("CTC")){
				node =  "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CT-COM_ServerType";
			}

			ArrayList<ParameValueOBJ> result = acsCorba.getValue(device_id, node);
			if(null == result)
			{
				//获取设备绑定用户的VOIP的开通协议类型(暂时不处理H.248的情况)
				String voipProtocal = deviceInfoBIO.getBssVoipSheetProtocalByDeviceId(device_id);
				if(null != voipProtocal && !"".equals(voipProtocal.trim()))
				{
					voipProtocalType = voipProtocal;
				}
			}else
			{
				voipProtocalType = result.get(0).getValue();
			}
		}
		//无法获取，则显示无法获取VOIP信息
		if(null == voipProtocalType)
		{
			this.voipMsg = "设备没有绑定用户或者绑定的用户没有开通VOIP业务";
		}else
		{
			// 转换显示信息
			switch (StringUtil.getIntegerValue(voipProtocalType))
			{
				case 0:
				{
					voipProtocalTypeStr = "IMS SIP";
					break;
				}
				case 1:
				{
					voipProtocalTypeStr = "软交换 SIP";
					break;
				}
				case 2:
				{
					voipProtocalTypeStr = "H.248";
					break;
				}
			}

			//获取设备VOIP链路信息
			int rsint = deviceInfoBIO.getDataFromSG(device_id, 2);
			//logger.warn("rsint:"+rsint);
			if (rsint != 1)
			{   
				this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();

				if (null == this.corbaMsg)
				{
					this.corbaMsg = Global.G_Fault_Map.get(100000)
							.getFaultReason();
				}
			}
			else
			{

				rsint = deviceInfoBIO.getDataFromSG(device_id, 34);
				if (rsint != 1)
				{
					this.corbaMsg = Global.G_Fault_Map.get(rsint)
							.getFaultReason();

					if (null == this.corbaMsg)
					{
						this.corbaMsg = Global.G_Fault_Map.get(100000)
								.getFaultReason();
					}
				}
				else
				{
					Map<String, List<Map>> allChannelMap = deviceInfoBIO
							.getAllChannel(device_id);
					List<Map> voip_list = allChannelMap.get("VOIP");
					this.voipInfoList = deviceInfoBIO.getVoipInfo(voip_list,voipProtocalType);
					if (null == this.voipInfoList
							|| this.voipInfoList.size() < 1)
					{
						this.corbaMsg = "设备没有绑定用户或者绑定的用户没有开通VOIP业务!";
					}
				}
			}
		}

		return "voipInfo";
	}
	public List getConfigInfoList()
	{
		return configInfoList;
	}


	public void setConfigInfoList(List configInfoList)
	{
		this.configInfoList = configInfoList;
	}





	public String getServType_id()
	{
		return servType_id;
	}


	public void setServType_id(String servType_id)
	{
		this.servType_id = servType_id;
	}


	public String getServstauts()
	{
		return servstauts;
	}


	public void setServstauts(String servstauts)
	{
		this.servstauts = servstauts;
	}


	public String getWanType()
	{
		return wanType;
	}


	public void setWanType(String wanType)
	{
		this.wanType = wanType;
	}

	public String getSerUsername()
	{
		return serUsername;
	}


	public void setSerUsername(String serUsername)
	{
		this.serUsername = serUsername;
	}

	public Map<String, String> getSoftVersionMap()
	{
		return softVersionMap;
	}


	public void setSoftVersionMap(Map<String, String> softVersionMap)
	{
		this.softVersionMap = softVersionMap;
	}

	public FaultTreadtMentBIO getBio()
	{
		return bio;
	}


	public void setBio(FaultTreadtMentBIO bio)
	{
		this.bio = bio;
	}


	public String getQueryParam()
	{
		return queryParam;
	}


	public void setQueryParam(String queryParam)
	{
		this.queryParam = queryParam;
	}


	public String getQueryType()
	{
		return queryType;
	}


	public void setQueryType(String queryType)
	{
		this.queryType = queryType;
	}


	public Map getDeviceMap()
	{
		return deviceMap;
	}


	public void setDeviceMap(Map deviceMap)
	{
		this.deviceMap = deviceMap;
	}


	public Map getUserMap()
	{
		return userMap;
	}


	public void setUserMap(Map userMap)
	{
		this.userMap = userMap;
	}


	public String getAjax()
	{
		return ajax;
	}


	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}



	public String getMsg()
	{
		return msg;
	}



	public void setMsg(String msg)
	{
		this.msg = msg;
	}


	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;

	}



	public List<Map> getSerlist()
	{
		return serlist;
	}



	public void setSerlist(List<Map> serlist)
	{
		this.serlist = serlist;
	}



	public HttpServletRequest getRequest()
	{
		return request;
	}

	public String getDevice_id()
	{
		return device_id;
	}


	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}


	public String getOid()
	{
		return oid;
	}


	public void setOid(String oid)
	{
		this.oid = oid;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
		this.session=session;

	}


	public List<Map<String, String>> getSerTypeList()
	{
		return serTypeList;
	}


	public void setSerTypeList(List<Map<String, String>> serTypeList)
	{
		this.serTypeList = serTypeList;
	}


	public String getUser_id()
	{
		return user_id;
	}


	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}


	public Map<String, Object> getSession()
	{
		return session;
	}


	public String getServTypeId()
	{
		return servTypeId;
	}


	public void setServTypeId(String servTypeId)
	{
		this.servTypeId = servTypeId;
	}


	public String getNetServUp()
	{
		return netServUp;
	}


	public void setNetServUp(String netServUp)
	{
		this.netServUp = netServUp;
	}


	public String getIsRealtimeQuery()
	{
		return isRealtimeQuery;
	}


	public void setIsRealtimeQuery(String isRealtimeQuery)
	{
		this.isRealtimeQuery = isRealtimeQuery;
	}


	public List getBssSheetList()
	{
		return bssSheetList;
	}


	public void setBssSheetList(List bssSheetList)
	{
		this.bssSheetList = bssSheetList;
	}


	public List getBssParaList()
	{
		return bssParaList;
	}


	public void setBssParaList(List bssParaList)
	{
		this.bssParaList = bssParaList;
	}


	public String getCity_id()
	{
		return city_id;
	}


	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}


	public String getGw_type()
	{
		return gw_type;
	}


	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}


	public String getType_name()
	{
		return type_name;
	}


	public void setType_name(String type_name)
	{
		this.type_name = type_name;
	}


	public Map<String, String> getBssIssuedConfigMap()
	{
		return bssIssuedConfigMap;
	}


	public void setBssIssuedConfigMap(Map<String, String> bssIssuedConfigMap)
	{
		this.bssIssuedConfigMap = bssIssuedConfigMap;
	}


	public String getDeviceSN()
	{
		return deviceSN;
	}


	public void setDeviceSN(String deviceSN)
	{
		this.deviceSN = deviceSN;
	}


	public List getUserList()
	{
		return userList;
	}


	public void setUserList(List userList)
	{
		this.userList = userList;
	}


	public String getNameType()
	{
		return nameType;
	}


	public void setNameType(String nameType)
	{
		this.nameType = nameType;
	}


	public String getUserName()
	{
		return userName;
	}


	public void setUserName(String userName)
	{
		this.userName = userName;
	}


	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}


	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}


	public String getInstArea()
	{
		return instArea;
	}


	public void setInstArea(String instArea)
	{
		this.instArea = instArea;
	}


	public String getChkinstuser()
	{
		return chkinstuser;
	}


	public void setChkinstuser(String chkinstuser)
	{
		this.chkinstuser = chkinstuser;
	}


	public String getServUserName()
	{
		return servUserName;
	}


	public void setServUserName(String servUserName)
	{
		this.servUserName = servUserName;
	}

	public String get_userCityId()
	{
		return _userCityId;
	}

	public void set_userCityId(String _userCityId)
	{
		this._userCityId = _userCityId;
	}

	public String get_userId()
	{
		return _userId;
	}

	public void set_userId(String _userId)
	{
		this._userId = _userId;
	}

	public String getDeviceCityId()
	{
		return deviceCityId;
	}

	public void setDeviceCityId(String deviceCityId)
	{
		this.deviceCityId = deviceCityId;
	}

	public String get_username()
	{
		return _username;
	}

	public void set_username(String _username)
	{
		this._username = _username;
	}

	public String getOui()
	{
		return oui;
	}

	public void setOui(String oui)
	{
		this.oui = oui;
	}

	public String getOid_type()
	{
		return oid_type;
	}

	public void setOid_type(String oid_type)
	{
		this.oid_type = oid_type;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDeviceinfo()
	{
		return deviceinfo;
	}

	public void setDeviceinfo(String deviceinfo)
	{
		this.deviceinfo = deviceinfo;
	}

	public List getDeviceList()
	{
		return deviceList;
	}

	public void setDeviceList(List deviceList)
	{
		this.deviceList = deviceList;
	}

	public String getFlag()
	{
		return flag;
	}

	public void setFlag(String flag)
	{
		this.flag = flag;
	}

	public String getStrategy_type()
	{
		return strategy_type;
	}

	public void setStrategy_type(String strategy_type)
	{
		this.strategy_type = strategy_type;
	}

	public String getVlanIdMark()
	{
		return vlanIdMark;
	}

	public void setVlanIdMark(String vlanIdMark)
	{
		this.vlanIdMark = vlanIdMark;
	}

	public String getSsid()
	{
		return ssid;
	}

	public void setSsid(String ssid)
	{
		this.ssid = ssid;
	}

	public String getWireless_port()
	{
		return wireless_port;
	}

	public void setWireless_port(String wireless_port)
	{
		this.wireless_port = wireless_port;
	}

	public String getBuss_level()
	{
		return buss_level;
	}

	public void setBuss_level(String buss_level)
	{
		this.buss_level = buss_level;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getAwifi_type()
	{
		return awifi_type;
	}

	public void setAwifi_type(String awifi_type)
	{
		this.awifi_type = awifi_type;
	}

	public String getNetNum()
	{
		return netNum;
	}

	public void setNetNum(String netNum)
	{
		this.netNum = netNum;
	}

	public I_DeviceInfoBIO getDeviceInfoBIO()
	{
		return deviceInfoBIO;
	}

	public void setDeviceInfoBIO(I_DeviceInfoBIO deviceInfoBIO)
	{
		this.deviceInfoBIO = deviceInfoBIO;
	}

	public String getWideNetMsg()
	{
		return wideNetMsg;
	}

	public void setWideNetMsg(String wideNetMsg)
	{
		this.wideNetMsg = wideNetMsg;
	}

	public String getIptvMsg()
	{
		return iptvMsg;
	}

	public void setIptvMsg(String iptvMsg)
	{
		this.iptvMsg = iptvMsg;
	}

	public String getVoipMsg()
	{
		return voipMsg;
	}

	public void setVoipMsg(String voipMsg)
	{
		this.voipMsg = voipMsg;
	}

	public String getWireMsg()
	{
		return wireMsg;
	}

	public void setWireMsg(String wireMsg)
	{
		this.wireMsg = wireMsg;
	}

	public String getTr069Msg()
	{
		return tr069Msg;
	}

	public void setTr069Msg(String tr069Msg)
	{
		this.tr069Msg = tr069Msg;
	}

	public String getLanMsg()
	{
		return lanMsg;
	}

	public void setLanMsg(String lanMsg)
	{
		this.lanMsg = lanMsg;
	}

	public List<WanConnSessObj> getWideNetInfoList()
	{
		return wideNetInfoList;
	}

	public void setWideNetInfoList(List<WanConnSessObj> wideNetInfoList)
	{
		this.wideNetInfoList = wideNetInfoList;
	}

	public List<WanConnSessObj> getIptvInfoList()
	{
		return iptvInfoList;
	}

	public void setIptvInfoList(List<WanConnSessObj> iptvInfoList)
	{
		this.iptvInfoList = iptvInfoList;
	}

	public List<Map> getVoipInfoList()
	{
		return voipInfoList;
	}

	public void setVoipInfoList(List<Map> voipInfoList)
	{
		this.voipInfoList = voipInfoList;
	}

	public ACSCorba getAcsCorba()
	{
		return acsCorba;
	}

	public void setAcsCorba(ACSCorba acsCorba)
	{
		this.acsCorba = acsCorba;
	}

	public String getCorbaMsg()
	{
		return corbaMsg;
	}

	public void setCorbaMsg(String corbaMsg)
	{
		this.corbaMsg = corbaMsg;
	}

	public String getVoipProtocalTypeStr()
	{
		return voipProtocalTypeStr;
	}

	public void setVoipProtocalTypeStr(String voipProtocalTypeStr)
	{
		this.voipProtocalTypeStr = voipProtocalTypeStr;
	}

	public String getWlanMsg()
	{
		return wlanMsg;
	}

	public void setWlanMsg(String wlanMsg)
	{
		this.wlanMsg = wlanMsg;
	}

	public List getLanEthList()
	{
		return lanEthList;
	}

	public void setLanEthList(List lanEthList)
	{
		this.lanEthList = lanEthList;
	}

	public Map getGwLanHostconfMap()
	{
		return gwLanHostconfMap;
	}

	public void setGwLanHostconfMap(Map gwLanHostconfMap)
	{
		this.gwLanHostconfMap = gwLanHostconfMap;
	}

	public List getWlanList()
	{
		return wlanList;
	}


	public List getShowList()
	{
		return showList;
	}

	public void setShowList(List showList)
	{
		this.showList = showList;
	}
	public void setWlanList(List wlanList)
	{
		this.wlanList = wlanList;
	}

	public GwTr069OBJ getGwTr069OBJ()
	{
		return gwTr069OBJ;
	}

	public void setGwTr069OBJ(GwTr069OBJ gwTr069OBJ)
	{
		this.gwTr069OBJ = gwTr069OBJ;
	}

	public String getAccessType()
	{
		return accessType;
	}

	public void setAccessType(String accessType)
	{
		this.accessType = accessType;
	}

	public DeviceWireInfoObj[] getWireInfoObjArr()
	{
		return wireInfoObjArr;
	}

	public void setWireInfoObjArr(DeviceWireInfoObj[] wireInfoObjArr)
	{
		this.wireInfoObjArr = wireInfoObjArr;
	}

	public PONInfoOBJ[] getPonInfoOBJArr()
	{
		return ponInfoOBJArr;
	}

	public void setPonInfoOBJArr(PONInfoOBJ[] ponInfoOBJArr)
	{
		this.ponInfoOBJArr = ponInfoOBJArr;
	}

	public String getPon_type()
	{
		return pon_type;
	}

	public void setPon_type(String pon_type)
	{
		this.pon_type = pon_type;
	}

	public String getUsernamereturn()
	{
		return usernamereturn;
	}

	public void setUsernamereturn(String usernamereturn)
	{
		this.usernamereturn = usernamereturn;
	}

	public String getDevice_serialnumberreturn()
	{
		return device_serialnumberreturn;
	}

	public void setDevice_serialnumberreturn(String device_serialnumberreturn)
	{
		this.device_serialnumberreturn = device_serialnumberreturn;
	}

	public String getDevice_id_judge()
	{
		return device_id_judge;
	}

	public void setDevice_id_judge(String device_id_judge)
	{
		this.device_id_judge = device_id_judge;
	}
	
	public List<Map<String, String>> getNetAccList()
	{
		return netAccList;
	}
	
	public void setNetAccList(List<Map<String, String>> netAccList)
	{
		this.netAccList = netAccList;
	}

	

}
