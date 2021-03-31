/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.init.bio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sendCorbaHeartBeat.RunSendCorbaHeartBeatTask;
import ACS.RPCManagerHelper;
import PreProcess.PPManagerHelper;
import ResourceBind.BlManagerHelper;
import SoftUp.SoftUpManagerHelper;
import StbCm.CMManagerHelper;
import SuperGather.SuperGatherManagerHelper;

import com.linkage.commons.elasticsearch.bio.ElasticsearchInitBIO;
import com.linkage.commons.jms.MQConfigParser;
import com.linkage.commons.jms.MQPublisher;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.AreaDAO;
import com.linkage.module.gwms.dao.tabquery.BssDevPortDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.dao.tabquery.UserTypeDAO;
import com.linkage.module.gwms.diagnostics.bio.ComputeUtil;
import com.linkage.module.gwms.init.dao.AppInitDAO;
import com.linkage.module.gwms.init.obj.CpeFaultcodeOBJ;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.database.Cursor;

/**
 * init data for application.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 19, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class AppInitBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(AppInitBIO.class);

	private static ORB orb = initORB();
	
	private static ORB initORB(){
		String[] args = null;
		return ORB.init(args, null);
	}
	
	/**
	 * init app.
	 * 
	 * @return
	 */
	public boolean init() {

		boolean flag = true;
		Global.G_MQPoolPath =  LipossGlobals.getLipossHome()+ File.separator + "WEB-INF"
				+ File.separator +"MQPool.xml";
		Global.MQ_POOL_PUBLISHER_MAP = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "publisher");
		Global.MQ_POOL_PUBLISHER_MAP_ITMS = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "itms");
		Global.MQ_POOL_PUBLISHER_MAP_BBMS = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "bbms");
		Global.MQ_POOL_PUBLISHER_MAP_STB = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "stb");
		
		// 调用配置模块和绑定模块类型
		Global.PRE_PROCESS_TYPE = LipossGlobals.getLipossProperty("preProcessType");
		Global.STB_PRE_PROCESS_TYPE = LipossGlobals.getLipossProperty("stbPreProcessType");
		Global.RESOURCE_BIND_TYPE = LipossGlobals.getLipossProperty("resourceBindType");
		Global.STB_RESOURCE_BIND_TYPE = LipossGlobals.getLipossProperty("stbResourceBindType");
		//新疆bug引入
		Global.SERV_STRATEGY_TABLE = LipossGlobals.getLipossProperty("strategy_tabname.stb.serv");
		// 调用配置模块和绑定模块主题
		List<String> suffixList = new ArrayList<String>();
		suffixList.add(".serv");
		suffixList.add(".soft");
		suffixList.add(".batch");
		suffixList.add(".strategy");
		// 调用配置模块和绑定模块主题
		List<String> suffixListStb = new ArrayList<String>();
		suffixListStb.add(".servStb");
		suffixListStb.add(".softStb");
		suffixListStb.add(".strategyStb");
		suffixListStb.add(".picStb");
		Global.PROCESS_PUBLISHER_ITMS = new MQPublisher("cm",suffixList,Global.MQ_POOL_PUBLISHER_MAP_ITMS);
		Global.PROCESS_PUBLISHER_BBMS = new MQPublisher("cm",suffixList,Global.MQ_POOL_PUBLISHER_MAP_BBMS);
		Global.PROCESS_PUBLISHER_STB = new MQPublisher("cm",suffixListStb,Global.MQ_POOL_PUBLISHER_MAP_STB);
		
		Global.RESOURCE_BIND_PUBLISHER_ITMS = new MQPublisher("res.Interface",Global.MQ_POOL_PUBLISHER_MAP_ITMS);
		Global.RESOURCE_BIND_PUBLISHER_BBMS = new MQPublisher("res.Interface",Global.MQ_POOL_PUBLISHER_MAP_BBMS);
		Global.RESOURCE_BIND_PUBLISHER_STB = new MQPublisher("res.InterfaceStb",Global.MQ_POOL_PUBLISHER_MAP_STB);

		Global.COMMON_PUBLISHER = new MQPublisher("term.newestinfo",Global.MQ_POOL_PUBLISHER_MAP);
		
		Global.FILE_EXPORT_PUBLISHER = new MQPublisher("fileExport",Global.MQ_POOL_PUBLISHER_MAP_ITMS);
		
		this.initUserTable();
		
//		if("1".equals(LipossGlobals.getLipossProperty("mq.itms.itmsEnable"))){
//			AppInitBIO.initSuperGather(Global.GW_TYPE_ITMS);
//			AppInitBIO.initPreProcess(Global.GW_TYPE_ITMS);
//			AppInitBIO.initResourceBind(Global.GW_TYPE_ITMS);
//			AppInitBIO.initSoftUpgradeCorba(Global.GW_TYPE_ITMS);
//		}
//		
//		if("1".equals(LipossGlobals.getLipossProperty("mq.bbms.bbmsEnable"))){
//			AppInitBIO.initSuperGather(Global.GW_TYPE_BBMS);
//			AppInitBIO.initPreProcess(Global.GW_TYPE_BBMS);
//			AppInitBIO.initResourceBind(Global.GW_TYPE_BBMS);
//			AppInitBIO.initSoftUpgradeCorba(Global.GW_TYPE_BBMS);
//		}
//		
//		if("1".equals(LipossGlobals.getLipossProperty("mq.stb.stbEnable"))){
//			AppInitBIO.initCMPreProcess(Global.GW_TYPE_STB);
//		}
		
		if(null != MQConfigParser.getMQConfig(Global.G_MQPoolPath, "itms"))
		{
			logger.warn("启动itms相关corba");
			AppInitBIO.initSuperGather(Global.GW_TYPE_ITMS);
			AppInitBIO.initPreProcess(Global.GW_TYPE_ITMS);
			AppInitBIO.initResourceBind(Global.GW_TYPE_ITMS);
//			AppInitBIO.initSoftUpgradeCorba(Global.GW_TYPE_ITMS);
			// corba心跳
			sendCorbaHeartBeatPeriod();
		}
		if(null != MQConfigParser.getMQConfig(Global.G_MQPoolPath, "bbms"))
		{
			logger.warn("启动bbms相关corba");
			AppInitBIO.initSuperGather(Global.GW_TYPE_BBMS);
			AppInitBIO.initPreProcess(Global.GW_TYPE_BBMS);
			AppInitBIO.initResourceBind(Global.GW_TYPE_BBMS);
//			AppInitBIO.initSoftUpgradeCorba(Global.GW_TYPE_BBMS);
		}
		if(null != MQConfigParser.getMQConfig(Global.G_MQPoolPath, "stb"))
		{
			logger.warn("启动stb相关corba");
			AppInitBIO.initCMPreProcess(Global.GW_TYPE_STB);
			AppInitBIO.initResourceBind(Global.GW_TYPE_STB);
//			AppInitBIO.initSoftUpgradeCorba(Global.GW_TYPE_STB);
		}
		
//		AppInitBIO.initACS();

		this.initFaultCode();
		this.initResourceBindFaultCode();
		this.initStaticSrc();
		this.initCityInfo();
		this.initAreaInfo();
		
		this.initSheetServer();
		
		this.initBssDevPort();
		
		ComputeUtil.initExpertMap();
		
		initBindPortMap();
		
		initUserTypeMap();
		/*
		Timer timer = new Timer();
	    timer.schedule(new ExcelDeleteTimeTask(), 1000, Global.EXCEL_DELETE_INTERVAL * 1000);
		*/
		
		initCuratorFramework();
		
		initElasticSearch();
		
		return flag;
	}
	

	
	
	/**
	 * init Global.G_UserTab
	 */
	private void initUserTable() {
		logger.debug("initUserTable()");

		if (1 == LipossGlobals.SystemType()) {
			Global.G_UserTab = "tab_hgwcustomer";
		} else if (2 == LipossGlobals.SystemType()) {
			Global.G_UserTab = "tab_egwcustomer";
		} else {
			Global.G_UserTab = "tab_hgwcustomer";
		}
	}

	/**
	 * init corba PP
	 * 
	 * @return
	 */
	public static boolean initPreProcess(String gw_type) {
		logger.debug("initPreProcess()");

		String ior = StringUtil.getStringValue(AppInitDAO.getPreProcessIOR(gw_type),
				"ior");
		if (StringUtil.IsEmpty(ior, true)) {
			logger.error("PreProcess ior is null");

			return false;
		}

		if(null == orb){
			orb = initORB();
		}
		org.omg.CORBA.Object objRef = null;
		try {
			objRef = orb.string_to_object(ior);
			if(Global.GW_TYPE_ITMS.equals(gw_type)){
				Global.G_PPManager_itms = PPManagerHelper.narrow(objRef);
			}else if(Global.GW_TYPE_BBMS.equals(gw_type)){
				Global.G_PPManager_bbms = PPManagerHelper.narrow(objRef);
			}else if(Global.GW_TYPE_STB.equals(gw_type)){
				Global.G_CMManager_stb = CMManagerHelper.narrow(objRef);
			}
		} catch (RuntimeException e) {
			logger.error("PreProcess RuntimeException:{}", e.getMessage());

			return false;
		}

		return true;
	}

	
	/**
	 * init corba PP
	 * 
	 * @return
	 */
	public static boolean initCMPreProcess(String gw_type) {
		logger.debug("^^^^^^^^^^^initCMPreProcess()");

		String ior = StringUtil.getStringValue(AppInitDAO.getPreProcessIOR(gw_type),
				"ior");
		if (StringUtil.IsEmpty(ior, true)) {
			logger.error("PreProcess ior is null");

			return false;
		}

		if(null == orb){
			orb = initORB();
		}
		org.omg.CORBA.Object objRef = null;
		try {
			objRef = orb.string_to_object(ior);
		    if(Global.GW_TYPE_STB.equals(gw_type)){
				Global.G_CMManager_stb = CMManagerHelper.narrow(objRef);
			}
		} catch (RuntimeException e) {
			logger.error("PreProcess RuntimeException:{}", e.getMessage());

			return false;
		}
		logger.warn("Global.G_CMManager_stb="+Global.G_CMManager_stb);
		return true;
	}
	/**
	 * init corba SG
	 * 
	 * @return
	 */
	public static boolean initSuperGather(String gw_type) {
		logger.debug("initSuperGather()");

		String ior = StringUtil.getStringValue(AppInitDAO.getSuperGatherIOR(gw_type),
				"ior");
		if (StringUtil.IsEmpty(ior, true)) {
			logger.error("SuperGather ior is null");

			return false;
		}

		if(null == orb){
			orb = initORB();
		}
		org.omg.CORBA.Object objRef = null;
		try {
			objRef = orb.string_to_object(ior);
			if(Global.GW_TYPE_ITMS.equals(gw_type)){
				Global.G_SuperGatherManager_itms = SuperGatherManagerHelper.narrow(objRef);
			}else {
				Global.G_SuperGatherManager_bbms = SuperGatherManagerHelper.narrow(objRef);
			}
		} catch (RuntimeException e) {
			logger.error("SuperGather RuntimeException:{}", e.getMessage());

			return false;
		}

		return true;
	}

	/**
	 * init corba ACS
	 * 
	 * @return
	 */
//	public static boolean initACS() {
//		logger.debug("initACS()");
//
//		String ior = StringUtil.getStringValue(AppInitDAO.getACSIOR(), "ior");
//		if (StringUtil.IsEmpty(ior, true)) {
//			logger.error("ACS ior is null");
//
//			return false;
//		}
//
//		if(null == orb){
//			orb = initORB();
//		}
//		org.omg.CORBA.Object objRef = null;
//		try {
//			objRef = orb.string_to_object(ior);
//			Global.G_ACSManager = RPCManagerHelper.narrow(objRef);
//		} catch (RuntimeException e) {
//			logger.error("ACS RuntimeException:{}", e.getMessage());
//
//			return false;
//		}
//
//		return true;
//	}

	/**
	 * init corba ACS.
	 * 
	 * @return
	 */
	public static boolean initAllACS() {
		logger.debug("initAllACS()");

		boolean flag = true;

		List<Map> list = null;
		// List<Map> list = AppInitDAO.getACSIOR();
		if (list == null || list.size() != 1) {
			logger.error("ACS ior is null");

			flag = false;

			return flag;
		}

		if(null == orb){
			orb = initORB();
		}
		org.omg.CORBA.Object objRef = null;
		Map map = null;
		String ior = null;
		String gatherId = null;

		for (int i = 0; i < list.size(); i++) {
			map = list.get(i);
			ior = StringUtil.getStringValue(map, "ior");
			gatherId = StringUtil.getStringValue(map, "gather_id").substring(4);
			try {
				objRef = orb.string_to_object(ior);

				Global.G_ACS_CORBA_Map.put(gatherId, RPCManagerHelper
						.narrow(objRef));
			} catch (RuntimeException e) {
				logger.error("ACS RuntimeException:{}", e.getMessage());

				flag = false;
			} catch (Exception e) {
				logger.error("ACS Exception:{}", e.getMessage());

				flag = false;
			}
		}

		return flag;
	}
	/**
	 * 初始化软件升级模块
	 * @return
	 */
	public static boolean initSoftUpgradeCorba(String gw_type)
	{
		logger.debug("initSoftUpgradeCorba()");

		String ior = StringUtil.getStringValue(AppInitDAO.getPreProcessIOR(gw_type),
				"ior");
		if (StringUtil.IsEmpty(ior, true)) {
			logger.error("SoftUpgrade ior is null");

			return false;
		}

		if(null == orb){
			orb = initORB();
		}
		org.omg.CORBA.Object objRef = null;
		try {
			objRef = orb.string_to_object(ior);
			if(Global.GW_TYPE_ITMS.equals(gw_type)){
				Global.G_Softgrade_itms = SoftUpManagerHelper.narrow(objRef);
			}else if(Global.GW_TYPE_STB.equals(gw_type))
			{
				Global.G_Softgrade_stb = SoftUpManagerHelper.narrow(objRef);
			}
			else {
				Global.G_Softgrade_bbms = SoftUpManagerHelper.narrow(objRef);
			}
		} catch (RuntimeException e) {
			logger.error("SoftUpgrade RuntimeException:{}", e.getMessage());

			return false;
		}

		return true;
	}
	
	/**
	 * corba心跳
	 */
	public static void sendCorbaHeartBeatPeriod(){
		if("1".equals(LipossGlobals.getLipossProperty("isSendCorbaHeartBeat"))){
			try
			{
				Thread.sleep(60000l);
				Global.SEND_CORBA_HEART_BEAT_PERIOD = Integer.parseInt(LipossGlobals.getLipossProperty("sendCorbaHeartBeatPeriod")
						== null ? "20" : LipossGlobals.getLipossProperty("sendCorbaHeartBeatPeriod"));
			}
			catch (Exception e)
			{
				Global.SEND_CORBA_HEART_BEAT_PERIOD = 20;
				e.printStackTrace();
			}
			new Timer().schedule(new RunSendCorbaHeartBeatTask(), 
					10 * 1000l, 1000l * 60 * Global.SEND_CORBA_HEART_BEAT_PERIOD);
		}
	}
	
	/**
	 * 初始化资源绑定模块
	 * @return
	 */
	public static boolean initResourceBind(String gw_type)
	{
		logger.debug("initResourceBind()");

		String ior = StringUtil.getStringValue(AppInitDAO.getResourceBindIOR(gw_type),
				"ior");
		if (StringUtil.IsEmpty(ior, true)) {
			logger.error("ResourceBind ior is null");

			return false;
		}

		if(null == orb){
			orb = initORB();
		}
		org.omg.CORBA.Object objRef = null;
		try {
			objRef = orb.string_to_object(ior);
			if(Global.GW_TYPE_ITMS.equals(gw_type)){
				Global.G_ResourceBind_itms = BlManagerHelper.narrow(objRef);
			}
			else if (Global.GW_TYPE_STB.equals(gw_type))
			{
				Global.G_ResourceBind_stb = BlManagerHelper.narrow(objRef);
			}
			else
			{
				Global.G_ResourceBind_bbms = BlManagerHelper.narrow(objRef);
			}
		} catch (RuntimeException e) {
			logger.error("ResourceBind RuntimeException:{}", e.getMessage());

			return false;
		}

		return true;
	}

	/**
	 * init fault code.
	 * 
	 * @return
	 */
	private boolean initFaultCode() {
		logger.debug("initFaultCode()");

		boolean flag = true;

		Cursor cursor = AppInitDAO.getFaultCode();
		Map map = cursor.getNext();

		while (map != null) {
			
			CpeFaultcodeOBJ obj = new CpeFaultcodeOBJ();
			
			obj.setFaultCode(StringUtil.getIntValue(map, "fault_code"));
			
			obj.setFaultType(StringUtil.getIntValue(map, "fault_type"));
			
			obj.setFaultName(StringUtil.getStringValue(map, "fault_name"));
			
			obj.setFaultDesc(StringUtil.getStringValue(map, "fault_desc"));
			
			obj.setFaultReason(StringUtil.getStringValue(map, "fault_reason"));
			
			obj.setSolutions(StringUtil.getStringValue(map, "solutions"));
			
			
			Global.G_Fault_Map.put(obj.getFaultCode(),obj);

			map = cursor.getNext();
		}
		return flag;
	}
	
	private void initResourceBindFaultCode() {
		
		Global.G_ResourceBind_statusCode.put(1, "成功");
		Global.G_ResourceBind_statusCode.put(0, "非法客户端");
		Global.G_ResourceBind_statusCode.put(-1, "参数错误");
		Global.G_ResourceBind_resultCode.put(-11, "ITMS已经解绑成功，调用华为业务平台解绑接口失败，请到华为业务平台确认！");
		Global.G_ResourceBind_resultCode.put(-12, "ITMS已经解绑成功，调用中兴业务平台解绑接口失败，请到中兴业务平台确认！");
		Global.G_ResourceBind_resultCode.put(1, "成功");
		Global.G_ResourceBind_resultCode.put(-1, "参数错误");
		Global.G_ResourceBind_resultCode.put(-2, "终端类型不匹配");
        Global.G_ResourceBind_resultCode.put(-7, "接入方式不匹配");//增加接入类型不匹配 add by zhangcong@
		Global.G_ResourceBind_resultCode.put(-3, "没有相关用户");
		Global.G_ResourceBind_resultCode.put(-4, "没有相关设备");
		Global.G_ResourceBind_resultCode.put(-5, "不存在绑定关系");
		Global.G_ResourceBind_resultCode.put(-6, "数据库操作失败");
		// add by fangchao.页面手工绑定时，待绑定的用户和设备都是未绑定状态
		Global.G_ResourceBind_resultCode.put(-8, "设备已经绑定用户，请先解绑");
		Global.G_ResourceBind_resultCode.put(-9, "用户已经绑定设备，请先解绑");
		Global.G_ResourceBind_resultCode.put(-10, "终端规格与用户规格不匹配");
		Global.G_ResourceBind_resultCode.put(-10000, "未知错误");
	}
	
	/**
	 * init static src
	 * 
	 * @return
	 */
	private boolean initStaticSrc(){
		logger.debug("initStaticSrc()");		
		boolean flag = true;
		
		String type = "";
		Map codeMap = new HashMap<String,String>();
		Map keyMap = new HashMap<String,String>();
		
		Cursor cursor = AppInitDAO.getStaticSrc();
		Map map = cursor.getNext();
		
		if(null != map){
			type = StringUtil.getStringValue(map, "src_type");
		}
		
		while (null != map) {
			
			if(!type.equals(StringUtil.getStringValue(map, "src_type"))){
				
				Map codeMapTemp = new HashMap<String,String>();
				Map keyMapTemp = new HashMap<String,String>();
				codeMapTemp.putAll(codeMap);
				keyMapTemp.putAll(keyMap);
				
				Global.G_Src_Code_Map.put(type, codeMapTemp);
				Global.G_Src_Key_Map.put(type, keyMapTemp);
				
				type = StringUtil.getStringValue(map, "src_type");
				
				codeMap.clear();
				keyMap.clear();
			}
			
			codeMap.put(StringUtil.getStringValue(map, "src_code"),StringUtil.getStringValue(map, "src_value"));
			keyMap.put(StringUtil.getStringValue(map, "src_key"),StringUtil.getStringValue(map, "src_value"));
			
			map = cursor.getNext();
		}
		
		//将最后一个Map加进去
		Global.G_Src_Code_Map.put(type, codeMap);
		Global.G_Src_Key_Map.put(type, keyMap);
		
		return flag;
	}
	
	/**
	 * 初始化属地
	 * 必须
	 *
	 */
	public void initCityInfo(){
		CityDAO cityAct = new CityDAO();
		
		//取所有属地的ID
		Global.G_CityIds = cityAct.getAllCityIdListCore();
		//属地ID、属地名Map<city_id,city_name>
		Global.G_CityId_CityName_Map = cityAct.getCityIdCityNameMapCore();
		//取属地ID和父属地ID的对应Map
		Global.G_City_Pcity_Map = cityAct.getCityIdPidMapCore();
		//所有的属地的子属地List集合
		Global.G_City_Child_List_Map = cityAct.getAllCityIdChildListMap();
	}
	
	
	/**
	 *初始化终端规格
	 */
	private void initBssDevPort(){
		logger.debug("initBssDevPort");
		BssDevPortDAO bssAct = new BssDevPortDAO();
		Global.G_BssDev_PortName_Map = bssAct.getBssDevPortNameMapCore();
	}
	
	/**
	 * 初始化属地
	 * 必须
	 *
	 */
	public void initAreaInfo(){
		AreaDAO areaDAO = new AreaDAO();
		
		/**域List<area_id>*/
		Global.G_Area_Id_List = areaDAO.getAllAreaIdListCore();
		/**域Map<area_id,parent_id>*/
		Global.G_Area_PArea_Map = areaDAO.getAreaIdPidMapCore();
		/**域Map<area_id,List<String>>**/
		Global.G_PArea_AreaList_Map = areaDAO.getAllAreaIdChildListMapCore();
		/**属地域对应关系Map<city_id,area_id>**/
		Global.G_CityId_AreaId_Map = areaDAO.getCityIdAreaIdMapCore();
		
	}
	
	
	
	/**
	 * 初始化工单接口的服务器地址和端口
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-10-15
	 * @return void
	 */
	public void initSheetServer(){
		logger.debug("initSheetServer()");
		Global.G_BBMS_Sheet_Server = LipossGlobals.getLipossProperty("bss_sheet.server");
		Global.G_BBMS_Sheet_Port = StringUtil.getIntegerValue(LipossGlobals.getLipossProperty("bss_sheet.port"));
		if(0 == Global.G_BBMS_Sheet_Port){
			logger.debug("bss_sheet.port is 0 or not number");
			Global.G_BBMS_Sheet_Port = 30000;
		}
		
		Global.G_ITMS_Sheet_Server = LipossGlobals.getLipossProperty("bss_sheet.server");
		Global.G_ITMS_Sheet_Port = StringUtil.getIntegerValue(LipossGlobals.getLipossProperty("bss_sheet.port"));
		if(0 == Global.G_ITMS_Sheet_Port){
			logger.debug("bss_sheet.port is 0 or not number");
			Global.G_ITMS_Sheet_Port = 30000;
		}
		
		Global.G_ITMS_Sheet_BSS_Server = LipossGlobals.getLipossProperty("bss_sheet.serverStb");
		Global.G_ITMS_Sheet_BSS_Port = StringUtil.getIntegerValue(LipossGlobals.getLipossProperty("bss_sheet.portStb"));
		if(0 == Global.G_ITMS_Sheet_BSS_Port){
			logger.debug("bss_sheet.port is 0 or not number");
			Global.G_ITMS_Sheet_BSS_Port = 30000;
		}
	}
	
	/**
	 * 初始化bindPortMap 
	 */
	public void initBindPortMap()
	{
		String LAN1 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1";
		String LAN2 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2";
		String LAN3 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3";
		String LAN4 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4";
		String LAN5 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.5";
		String LAN6 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.6";
		String LAN7 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.7";
		String LAN8 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.8";
		String LAN9 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.9";
		String LAN10 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.10";
		String LAN11 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.11";
		String LAN12 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.12";
		String LAN13 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.13";
		String LAN14 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.14";
		String LAN15 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.15";
		String LAN16 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.16";
		
		String WLAN1 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1";
		String WLAN2 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.2";
		String WLAN3 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.3";
		String WLAN4 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.4";
		String WLAN5 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.5";
		String WLAN6 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.6";
		String WLAN7 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.7";
		String WLAN8 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.8";
		String WLAN9 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.9";
		String WLAN10 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.10";
		String WLAN11 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.11";
		String WLAN12 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.12";
		String WLAN13 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.13";
		String WLAN14 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.14";
		String WLAN15 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.15";
		String WLAN16 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.16";
		
		// 兼容带点的
		String LAN1_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1.";
		String LAN2_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2.";
		String LAN3_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3.";
		String LAN4_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4.";
		String LAN5_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.5.";
		String LAN6_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.6.";
		String LAN7_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.7.";
		String LAN8_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.8.";
		String LAN9_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.9.";
		String LAN10_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.10.";
		String LAN11_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.11.";
		String LAN12_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.12.";
		String LAN13_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.13.";
		String LAN14_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.14.";
		String LAN15_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.15.";
		String LAN16_DOT = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.16.";
		String WLAN1_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.";
		String WLAN2_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.2.";
		String WLAN3_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.3.";
		String WLAN4_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.4.";
		String WLAN5_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.5.";
		String WLAN6_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.6.";
		String WLAN7_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.7.";
		String WLAN8_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.8.";
		String WLAN9_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.9.";
		String WLAN10_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.10.";
		String WLAN11_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.11.";
		String WLAN12_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.12.";
		String WLAN13_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.13.";
		String WLAN14_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.14.";
		String WLAN15_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.15.";
		String WLAN16_DOT = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.16.";
		
		Global.bindPortMap.put("LAN1", LAN1);
		Global.bindPortMap.put("LAN2", LAN2);
		Global.bindPortMap.put("LAN3", LAN3);
		Global.bindPortMap.put("LAN4", LAN4);
		Global.bindPortMap.put("LAN5", LAN5);
		Global.bindPortMap.put("LAN6", LAN6);
		Global.bindPortMap.put("LAN7", LAN7);
		Global.bindPortMap.put("LAN8", LAN8);
		Global.bindPortMap.put("LAN9", LAN9);
		Global.bindPortMap.put("LAN10", LAN10);
		Global.bindPortMap.put("LAN11", LAN11);
		Global.bindPortMap.put("LAN12", LAN12);
		Global.bindPortMap.put("LAN13", LAN13);
		Global.bindPortMap.put("LAN14", LAN14);
		Global.bindPortMap.put("LAN15", LAN15);
		Global.bindPortMap.put("LAN16", LAN16);
		
		Global.bindPortMap.put("WLAN1", WLAN1);
		Global.bindPortMap.put("WLAN2", WLAN2);
		Global.bindPortMap.put("WLAN3", WLAN3);
		Global.bindPortMap.put("WLAN4", WLAN4);
		Global.bindPortMap.put("WLAN5", WLAN5);
		Global.bindPortMap.put("WLAN6", WLAN6);
		Global.bindPortMap.put("WLAN7", WLAN7);
		Global.bindPortMap.put("WLAN8", WLAN8);
		Global.bindPortMap.put("WLAN9", WLAN9);
		Global.bindPortMap.put("WLAN10", WLAN10);
		Global.bindPortMap.put("WLAN11", WLAN11);
		Global.bindPortMap.put("WLAN12", WLAN12);
		Global.bindPortMap.put("WLAN13", WLAN13);
		Global.bindPortMap.put("WLAN14", WLAN14);
		Global.bindPortMap.put("WLAN15", WLAN15);
		Global.bindPortMap.put("WLAN16", WLAN16);
		Global.bindPortMap.put("SSID2", WLAN2);
		
		Global.bindPortMap.put(LAN1, "LAN1");
		Global.bindPortMap.put(LAN2, "LAN2");
		Global.bindPortMap.put(LAN3, "LAN3");
		Global.bindPortMap.put(LAN4, "LAN4");
		Global.bindPortMap.put(LAN5, "LAN5");
		Global.bindPortMap.put(LAN6, "LAN6");
		Global.bindPortMap.put(LAN7, "LAN7");
		Global.bindPortMap.put(LAN8, "LAN8");
		Global.bindPortMap.put(LAN9, "LAN9");
		Global.bindPortMap.put(LAN10, "LAN10");
		Global.bindPortMap.put(LAN11, "LAN11");
		Global.bindPortMap.put(LAN12, "LAN12");
		Global.bindPortMap.put(LAN13, "LAN13");
		Global.bindPortMap.put(LAN14, "LAN14");
		Global.bindPortMap.put(LAN15, "LAN15");
		Global.bindPortMap.put(LAN16, "LAN16");
		
		Global.bindPortMap.put(WLAN1, "WLAN1");
		Global.bindPortMap.put(WLAN2, "WLAN2");
		Global.bindPortMap.put(WLAN3, "WLAN3");
		Global.bindPortMap.put(WLAN4, "WLAN4");
		Global.bindPortMap.put(WLAN5, "WLAN5");
		Global.bindPortMap.put(WLAN6, "WLAN6");
		Global.bindPortMap.put(WLAN7, "WLAN7");
		Global.bindPortMap.put(WLAN8, "WLAN8");
		Global.bindPortMap.put(WLAN9, "WLAN9");
		Global.bindPortMap.put(WLAN10, "WLAN10");
		Global.bindPortMap.put(WLAN11, "WLAN11");
		Global.bindPortMap.put(WLAN12, "WLAN12");
		Global.bindPortMap.put(WLAN13, "WLAN13");
		Global.bindPortMap.put(WLAN14, "WLAN14");
		Global.bindPortMap.put(WLAN15, "WLAN15");
		Global.bindPortMap.put(WLAN16, "WLAN16");
		
		//兼容带点的
		Global.bindPortMap.put(LAN1_DOT, "LAN1");
		Global.bindPortMap.put(LAN2_DOT, "LAN2");
		Global.bindPortMap.put(LAN3_DOT, "LAN3");
		Global.bindPortMap.put(LAN4_DOT, "LAN4");
		Global.bindPortMap.put(LAN5_DOT, "LAN5");
		Global.bindPortMap.put(LAN6_DOT, "LAN6");
		Global.bindPortMap.put(LAN7_DOT, "LAN7");
		Global.bindPortMap.put(LAN8_DOT, "LAN8");
		Global.bindPortMap.put(LAN9_DOT, "LAN9");
		Global.bindPortMap.put(LAN10_DOT, "LAN10");
		Global.bindPortMap.put(LAN11_DOT, "LAN11");
		Global.bindPortMap.put(LAN12_DOT, "LAN12");
		Global.bindPortMap.put(LAN13_DOT, "LAN13");
		Global.bindPortMap.put(LAN14_DOT, "LAN14");
		Global.bindPortMap.put(LAN15_DOT, "LAN15");
		Global.bindPortMap.put(LAN16_DOT, "LAN16");
		Global.bindPortMap.put(WLAN1_DOT, "WLAN1");
		Global.bindPortMap.put(WLAN2_DOT, "WLAN2");
		Global.bindPortMap.put(WLAN3_DOT, "WLAN3");
		Global.bindPortMap.put(WLAN4_DOT, "WLAN4");
		Global.bindPortMap.put(WLAN5_DOT, "WLAN5");
		Global.bindPortMap.put(WLAN6_DOT, "WLAN6");
		Global.bindPortMap.put(WLAN7_DOT, "WLAN7");
		Global.bindPortMap.put(WLAN8_DOT, "WLAN8");
		Global.bindPortMap.put(WLAN9_DOT, "WLAN9");
		Global.bindPortMap.put(WLAN10_DOT, "WLAN10");
		Global.bindPortMap.put(WLAN11_DOT, "WLAN11");
		Global.bindPortMap.put(WLAN12_DOT, "WLAN12");
		Global.bindPortMap.put(WLAN13_DOT, "WLAN13");
		Global.bindPortMap.put(WLAN14_DOT, "WLAN14");
		Global.bindPortMap.put(WLAN15_DOT, "WLAN15");
		Global.bindPortMap.put(WLAN16_DOT, "WLAN16");
	}
	
	public void initUserTypeMap(){
		logger.debug("initSheetServer()");
		UserTypeDAO userTypeDao = new UserTypeDAO();
		Global.G_UserTypeId_UserName_Map = userTypeDao.getUserTypeIdNameMapCore();
	}
	
	private void initElasticSearch(){
		
	  if (LipossGlobals.G_ServerHome == null) {
			LipossGlobals.G_ServerHome = LipossGlobals.getLipossHome();
		}
	  	//比如 /export/home/itms/WEB/WEB-INF/conf/elasticsearch.xml
		String path = LipossGlobals.G_ServerHome + File.separator + "WEB-INF";
		//初始化
		 ElasticsearchInitBIO.getInstance().setAppPath(path);
		 ElasticsearchInitBIO.getInstance().initConfig();
  }
	
	/**
	 *  初始化zookeeper
	 * 
	 */
	private void initCuratorFramework() {
		
		if(!StringUtil.IsEmpty(LipossGlobals.getLipossProperty("zkAddress"))) {
			com.linkage.litms.Global.curatorFramework = CuratorFrameworkFactory.newClient(LipossGlobals.getLipossProperty("zkAddress"), new RetryNTimes(10, 5000));
			com.linkage.litms.Global.curatorFramework.start();
		}
	}
}
