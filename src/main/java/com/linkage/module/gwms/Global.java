/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ACS.RPCManager;
import PreProcess.PPManager;
import ResourceBind.BlManager;
import SoftUp.SoftUpManager;
import StbCm.CMManager;
import SuperGather.SuperGatherManager;

import com.linkage.commons.jms.MQPublisher;
import com.linkage.commons.jms.obj.MQConfig;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.init.obj.CpeFaultcodeOBJ;

/**
 * Global information.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 19, 2009
 * @see
 * @since 1.0
 */
public class Global {
	
	public static String G_MQPoolPath = null;

	/** PreProcess CORBA */
/**	public static PPManager G_PPManager = null; */
	public static PPManager G_PPManager_itms = null;
	public static PPManager G_PPManager_bbms = null;
	public static CMManager G_CMManager_stb = null;

	/** SuperGManager CORBA */
/**	public static SuperGatherManager G_SuperGatherManager = null; */
	public static SuperGatherManager G_SuperGatherManager_itms = null;
	public static SuperGatherManager G_SuperGatherManager_bbms = null;
	
	/** ResourceBind corba */
/**	public static BlManager G_ResourceBind = null; */
	public static BlManager G_ResourceBind_itms = null;
	public static BlManager G_ResourceBind_bbms = null;
	public static BlManager G_ResourceBind_stb = null;
	/** Softgrade corba */
	public static SoftUpManager G_Softgrade_itms = null;
	public static SoftUpManager G_Softgrade_bbms = null;
	public static SoftUpManager G_Softgrade_stb = null;
	
	public static Map<Integer, String> G_ResourceBind_statusCode = new HashMap<Integer, String>();
	public static Map<Integer, String> G_ResourceBind_resultCode = new HashMap<Integer, String>();

	/** ACS CORBA */
/**	public static RPCManager G_ACSManager = null; */

	/** ACS CORBA map(gather_id,RPCManager) */
	public static Map<String, RPCManager> G_ACS_CORBA_Map = new HashMap<String, RPCManager>();

	/** the table name of user. */
	public static String G_UserTab = null;

	/** cpe fault code map(fault_code, CpeFaultcodeOBJ) */
	public static Map<Integer, CpeFaultcodeOBJ> G_Fault_Map = new HashMap<Integer, CpeFaultcodeOBJ>();

	/** tab_static_src map(src_type,Map(src_code,src_value))**/
	public static Map<String,Map<String,String>> G_Src_Code_Map = new HashMap<String,Map<String,String>>();
	
	/** tab_static_src map(src_type,Map(src_key,src_value))**/
	public static Map<String,Map<String,String>> G_Src_Key_Map = new HashMap<String,Map<String,String>>();
	
	/**
	 * @author onelinesky(4174)
	 * 
	 * 以下属地的全局变量，理论上不允许私有调用，需要调用的话，请使用CityDAO来调用
	 * 另外以下初始化的顺序有以下
	 * 1、G_CityIds
	 * 2、G_CityId_CityName_Map
	 * 3、G_City_Pcity_Map
	 * 4、G_City_Child_List_Map
	 * 如初始化顺序有变化，则出现初始化失败的情况
	 */
	/**属地List<city_id>*/
	public static ArrayList<String> G_CityIds = null;
	/**属地ID、属地名Map<city_id,city_name>*/
	public static Map<String,String> G_CityId_CityName_Map = null;
	/**属地Map<city_id,parent_id>*/
	public static Map<String, String> G_City_Pcity_Map = null;
	/**属地Map<city_id,List<String>>**/
	public static Map<String, ArrayList<String>> G_City_Child_List_Map = null;
	
	/**终端规格<id ,spec_name> */
	public static Map<String,String> G_BssDev_PortName_Map = null;
	/**
	 * @author onelinesky(4174)
	 * 
	 * 以下域的全局变量，理论上不允许私有调用，需要调用的话，请使用AreaDAO来调用
	 * 另外以下初始化的顺序有以下
	 * 1、G_Area_Id_List
	 * 2、G_Area_PArea_Map
	 * 3、G_PArea_AreaList_Map
	 * 4、G_CityId_AreaId_Map
	 * 如初始化顺序有变化，则出现初始化失败的情况
	 */
	/**域List<area_id>*/
	public static ArrayList<String> G_Area_Id_List = null;
	/**域Map<area_id,parent_id>*/
	public static Map<String, String> G_Area_PArea_Map = null;
	/**域Map<area_id,List<String>>**/
	public static Map<String, ArrayList<String>> G_PArea_AreaList_Map = null;
	/**属地域对应关系Map<city_id,area_id>**/
	public static Map<String, String> G_CityId_AreaId_Map = null;
	
	/** 用户工单来源,用户类型 **/
	public static Map<String, String> G_UserTypeId_UserName_Map = null;
	
	/** BSS工单接口的服务器地址和端口 */
	public static String G_BBMS_Sheet_Server;
	public static int G_BBMS_Sheet_Port;
	
	/** BSS工单接口的服务器地址和端口 */
	public static String G_ITMS_Sheet_Server;
	public static int G_ITMS_Sheet_Port;
	
	/** BSS模拟工单接口的服务器地址和端口 */
	public static String G_ITMS_Sheet_BSS_Server;
	public static int G_ITMS_Sheet_BSS_Port;
	
	/** 命令类型：0：检测连接、1：普通命令（获取参数、设置参数）、2：诊断命令、3：文件下发（软件升级）*/
	public static int RpcTest_Type = 0;
	public static int RpcCmd_Type = 1;
	public static int DiagCmd_Type = 2;
	public static int FileCmd_Type = 3;
	
	/** priority:1,hig;2,low  */
	public static int Priority_Hig = 1;
	public static int Priority_Low = 2;
	
	/** 1：表示ITMS； 2：表示BBMS;4:表示机顶盒  */
	public static final String GW_TYPE_ITMS = "1";
	public static final String GW_TYPE_BBMS = "2";
	public static final String GW_TYPE_STB = "4";
	
	/** 
	 * 客户类型融合标识，标识家庭客户和政企客户信息融合，
	 * 政企信息都放在家庭客户信息表中，即政企和家庭处理方式相同
	 */
	public static final String GW_TYPE_ALL = "3";
	
	/**业务类型VOIP id*/
	public static String SERVER_TYPE_ID ="14";
	/** 定义String的常量，用于拼接 */
	public static String SYSTEM_ITMS_PREFIX = "ITMS_"; 
	public static String SYSTEM_BBMS_PREFIX = "BBMS_"; 
	public static String SYSTEM_STB_PREFIX = "STB_"; 
	
	public static String SYSTEM_ITMS_SUFFIX = "_itms";
	public static String SYSTEM_BBMS_SUFFIX = "_bbms";
	public static String SYSTEM_STB_SUFFIX = "_stb";
	
	public static String SYSTEM_ACS = "ACS";
	/** 采集 */
	public static String SYSTEM_SUPER_GATHER = "SuperGather";  
	/** 配置 */
	public static String SYSTEM_PREPROCESS = "PreProcess";        
	public static String SYSTEM_PREPROCESS_POA = "PreProcess_Poa";
	public static String SYSTEM_BUSINESS_LOGIC = "BusinessLogic";
	
	/** add by chenjie 2013-10-28
	 *  运营商信息
	 ***/
	/**联通*/
	public static String TELECOM_CUC = "CUC"; 
	/**移动*/
	public static String TELECOM_CMC = "CMC"; 
	/**电信*/
	public static String TELECOM_CTC = "CTC"; 
	
	
	/** 获取前缀名称 */
	public static String getPrefixName (String gwType){
		if("0".equals(LipossGlobals.getLipossProperty("SystemType"))){

			if(GW_TYPE_ITMS.equals(gwType)){
				return SYSTEM_ITMS_PREFIX;
			} else if(GW_TYPE_BBMS.equals(gwType)){
				return SYSTEM_BBMS_PREFIX;
			} else if (GW_TYPE_STB.equals(gwType)) {
				return SYSTEM_STB_PREFIX;
			}else {
				return "";
			}
		}else{
			return "";
		}
	}
	
	
	/**  获取后缀名称 */
	public static String getSuffixName (String gwType){
		if(GW_TYPE_ITMS.equals(gwType)){
			return "";
		} else if(GW_TYPE_BBMS.equals(gwType)){
			return SYSTEM_BBMS_SUFFIX;
		} else if(GW_TYPE_STB.equals(gwType)) {
			return SYSTEM_STB_SUFFIX;
		} else {
			return "";
		}
	}

	/** 
	 * 根据deviceId判断gw_type,修改表名
	 * @param deviceId
	 * @param tabName
	 * @return
	 */
	public static String getTabName(String deviceId, String tabName)
	{
		String gwType = LipossGlobals.getGw_Type(deviceId);
		// add by chenjie 2013-10-25 运营商信息
		if(LipossGlobals.getLipossProperty("telecom").equals(TELECOM_CUC))
		{
			tabName = "cuc_" + tabName; 
		}
		return tabName + Global.getSuffixName(gwType);
	}

	/** 定期删除excel周期,单位:秒  **/
	public static final int EXCEL_DELETE_INTERVAL = 60 * 60;
	
	
	static class ResourceBind
	{
		//		成功
		public static String BIND_STATUS_SUSS = "1";
		//		非法客户端
		public static String BIND_STATUS_UNKNOW_clientId = "0";
		//		参数错误
		public static String BIND_STATUS_PARAM_ERROR = "-1";
		//		成功
		public static String BIND_RESULT_SUSS = "1";
		//		参数错误
		public static String BIND_RESULT_PARAM_ERROR = "-1";
		//		终端类型不匹配
		public static String BIND_RESULT_TYPE_ERROR = "-2";
		//		没用户
		public static String BIND_RESULT_NOUSER_ERROR = "-3";
		//		没设备
		public static String BIND_RESULT_NODEVICE_ERROR = "-4";
		//		不存在绑定关系（针对解绑）
		public static String BIND_RESULT_NOBIND_ERROR = "-5";
		//		数据库操作失败
		public static String BIND_RESULT_DBOPARATE_ERROR = "-6";
		//		未知错误
		public static String BIND_RESULT_UNKNOW_ERROR = "-10000";

	}
	
	/**
	 * 绑定端口map
	 */
	public static Map<String, String> bindPortMap = new HashMap<String, String>();
	
	public static long MAX_UNUSED_STRATEGYID = -1L;
	
	public static long MIN_UNUSED_STRATEGYID = -1L;
	
	public static int SUM_UNUSED_STRATEGYID = 50;
	
	public static Map<String, MQConfig> MQ_POOL_PUBLISHER_MAP = new HashMap<String, MQConfig>();
	public static Map<String, MQConfig> MQ_POOL_PUBLISHER_MAP_ITMS = new HashMap<String, MQConfig>();
	public static Map<String, MQConfig> MQ_POOL_PUBLISHER_MAP_BBMS = new HashMap<String, MQConfig>();
	public static Map<String, MQConfig> MQ_POOL_PUBLISHER_MAP_STB = new HashMap<String, MQConfig>();
	
	/**
	 * corba心跳时间
	 */
	public static int SEND_CORBA_HEART_BEAT_PERIOD = 20;
	
	/** 调用配置模块类型1：corba，2：发送消息*/
	public static String PRE_PROCESS_TYPE = "1";
	
	/** 机顶盒调用配置模块类型1：corba，2：发送消息*/
	public static String STB_PRE_PROCESS_TYPE = "1";
	
	/** 调用绑定模块类型1：corba，2：发送消息*/
	public static String RESOURCE_BIND_TYPE = "1";
	/** 机顶盒调用绑定模块类型1：corba，2：发送消息*/
	public static String STB_RESOURCE_BIND_TYPE = "1";
	
	/** cm.Interface主题的生产者*/
	public static MQPublisher PROCESS_PUBLISHER_ITMS = null;
	public static MQPublisher PROCESS_PUBLISHER_BBMS = null;
	public static MQPublisher PROCESS_PUBLISHER_STB = null;
	
	/** res.Interface主题的生产者*/
	public static MQPublisher RESOURCE_BIND_PUBLISHER_ITMS = null;
	public static MQPublisher RESOURCE_BIND_PUBLISHER_BBMS = null;
	public static MQPublisher RESOURCE_BIND_PUBLISHER_STB = null;
	
	/** cm.Interface主题的生产者*/
	public static MQPublisher COMMON_PUBLISHER = null;
	
	/** fileExport主题的生产者*/
	public static MQPublisher FILE_EXPORT_PUBLISHER = null;

	public static String SERV_STRATEGY_TABLE = "";
	
	/**数据库类型*/
	public static final int DB_ORACLE=1;
	public static final int DB_SYBASE=2;
	public static final int DB_MYSQL=3;
	/**随机数*/
	public static Random rand = new Random();
	
	/**省份运营商节点*/
	public static String shortName="InstArea.ShortName";
	/**省份运营商，如ah_dx*/
	public static String instAreaShortName=LipossGlobals.getLipossProperty(shortName);
	/**湖南联通*/
	public static final String HNLT="hn_lt";
	/**重庆电信*/
	public static final String CQDX="cq_dx";
	/**宁夏电信*/
	public static final String NXDX="nx_dx";
	/**宁夏联通*/
	public static final String NXLT="nx_lt";
	/**新疆电信*/
	public static final String XJDX="xj_dx";
	/**山东电信*/
	public static final String SDDX="sd_dx";
	/**山东联通*/
	public static final String SDLT="sd_lt";
	/**山西联通*/
	public static final String SXLT="sx_lt";
	/**陕西电信*/
	public static final String SXDX="sx_dx";
	/**河北电信*/
	public static final String HBDX="hb_dx";
	/**河北联通*/
	public static final String HBLT="hb_lt";
	/**江西电信*/
	public static final String JXDX="jx_dx";
	/**江西联通*/
	public static final String JXLT="jx_lt";
	/**安徽电信*/
	public static final String AHDX="ah_dx";
	/**安徽联通*/
	public static final String AHLT="ah_lt";
	/**江苏电信*/
	public static final String JSDX="js_dx";
	/**甘肃电信*/
	public static final String GSDX="gs_dx";
	/**内蒙古电信*/
	public static final String NMGDX="nmg_dx";
	/**黑龙江电信*/
	public static final String HLJDX="hlj_dx";
	/**吉林电信*/
	public static final String JLDX="jl_dx";
	/**吉林联通*/
	public static final String JLLT="jl_lt";
	/**云南联通*/
	public static final String YNLT="yn_lt";
	/**浙江联通*/
	public static final String ZJLT="zj_lt";
}
