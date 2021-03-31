
package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.linkage.module.itms.resource.enums.AHLTDevVersionTypeEnum;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;
import action.splitpage.splitPageAction;
import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;
import com.linkage.module.itms.service.bio.BssSheetServBIO;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 业务查询
 * 
 * @author 王森博 1.0
 */
public class BssSheetServACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	private static final long serialVersionUID = 2425363349057904543L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BssSheetServACT.class);
	private static String DEVICETYPE_ALL = "0";
	private static String DEVICETYPE_DEFAULT_JSDX = "2";
	// session
	private Map session;
	/** 开始时间 */
	private String startOpenDate = "";
	/** 开始时间 */
	private String startOpenDate1 = "";
	/** 结束时间 */
	private String endOpenDate = "";
	/** 结束时间 */
	private String endOpenDate1 = "";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 设备类型列表 */
	private List<Map<String, String>> devicetypeList = null;
	/** 属地ID */
	private String cityId = "-1";
	/**
	 * 用户名类型 "1" 逻辑SN, "2" 宽带号码, "3" IPTV号码, "4" VoIP认证号码, "5" VoIP电话号码
	 */
	private String usernameType;
	/** 用户名 */
	private String username;
	private String servstauts;
	/** 开通状态 */
	private String openstatus;
	/** 终端类型 1:e8-b 2:e8-c 0:all */
	private String devicetype;
	private BssSheetServBIO bio;
	/** 业务信息列表 */
	private List<Map> bssSheetServList;
	private String deviceId;
	// 处理意见
	private String solutionData;
	private String result_id;
	/**
	 * 业务类型 "10" 上网业务, "11" IPTV, "14" VoIP
	 */
	private String servTypeId;
	/** 语音协议类型 */
	private String voipProtocalType;
	// private String operTypeId;
	private String deviceSN;
	private String wanType;
	// 配置信息列表
	private List<Map> configInfoList;
	// 软件升级列表
	private List<Map> softUpList;
	private String strategyId;
	// 配置详细信息列表
	private List<Map> configDetailList;
	// 配置历史信息列表
	private List<Map> configLogInfoList;
	// bss工单信息
	private List<Map> bssSheetList;
	// bss原始工单信息
	private List<Map> bssParaList;
	private String userId;
	private String oui;
	private String batchReset;
	private String ajax;
	private HttpServletRequest request;
	ArrayList<HashMap<String, String>> doServStatusList;
	private Map<String,String> bssIssuedConfigMap;
	
	private String user_id; 
	private String serv_type_id;
	private String vlanid;
	
	//用户工单来源
	private String user_type_id;
	
	// ********Export All Data To Excel****************
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private int total;
	// 类型名字
	private String type_name;
	private String serUsername;
	// 区分BBMS/ITMS
	private String gw_type;
	/**
	 * 用户设备规格
	 */
	private String spec_id;
	/**
	 * 客户类型：2：家庭用户，其他政企类型
	 */
	private String cust_type_id;
	// 地区(例如江苏 ，吉林)
	//@Deprecated 页面请使用ms:inArea标签
	private String instAreaName;
	/**
	 * <pre>
	 * 系统整改，将宽带业务参数迁移到表tab_net_serv_param中，但由于有区域没有数据迁移。
	 * 故增加该字段用于标识是否从tab_net_serv_param表中获取宽带业务参数
	 * 当前仅当该值为"true"时，才用宽带业务表中获取数据。
	 * </pre>
	 */
	private String netServUp;
	
	private String isRealtimeQuery;
	private String voip_passwd;
	private String voip_username;
	private String realBindPort;
	private String bindPort;
	private String maxNetNum;
	//上网方式（桥接或者路由）
	private String wanProtocalType;
	// 设备版本类型
	private List<Map<String, String>> devVersionTypeMap;
	private String deviceVersionType;
	private String loid;
	private List<Map<String,String>> bssSheetInfoList;
	//开户时间
	private String openDate;

	private static final String JSDX="js_dx";
	private static final String NXDX="nx_dx";
	private static final String GSDX="gs_dx";
	private static final String SDDX="sd_dx";
	private static final String AHDX="ah_dx";
	private static final String AHLT="ah_lt";
	private static final String SDLT="sd_lt";
	private static final String HLJDX="hlj_dx";
	private static final String JLDX="jl_dx";
	private static final String SXLT="sx_lt";
	
	/**
	 * 页面初始化
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		instAreaName = com.linkage.module.gwms.Global.instAreaShortName;
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		devicetypeList = Global.G_DeviceTypeID_Name_List_Map;
		//BSS业务查询条件：终端规格江苏电信默认是E8C，其他默认为全部
		devicetype = LipossGlobals.inArea(JSDX) || LipossGlobals.inArea(NXDX)?BssSheetServACT.DEVICETYPE_DEFAULT_JSDX:BssSheetServACT.DEVICETYPE_ALL;
		logger.warn("default device type id is " +devicetype);
		// DateTimeUtil dt = new DateTimeUtil();
		if (LipossGlobals.inArea(SDDX)) {
			startOpenDate = getStartDateSD_DX();
		}
		else {
			startOpenDate = getStartDate();
		}
		endOpenDate = getEndDate();
		if (LipossGlobals.inArea(AHLT)) {
			devVersionTypeMap = AHLTDevVersionTypeEnum.getAll();
		}
		if (request.getParameter("flag") != null)
		{
			return "init2";
		}
		return "init";
	}
	/**
	 * 将查询的操作人、ip地址、时间和查看的用户账号入表
	 */
	public String insertA8log()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String userip=request.getRemoteAddr();
		ajax=bio.insertA8log(voip_passwd, voip_username, userip, curUser);
		return "ajax";
	}
	/**
	 * 查询业务信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public String getBssSheetServInfo()
	{
		logger.warn("getBssSheetServInfo({})");
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		instAreaName = com.linkage.module.gwms.Global.instAreaShortName;
		bssSheetServList = bio.getBssSheetServInfo(cityId, startOpenDate1, endOpenDate1,
				usernameType, username, servTypeId, openstatus, devicetype,
				curPage_splitPage, num_splitPage, gw_type, voipProtocalType, spec_id,
				cust_type_id,user_type_id,isRealtimeQuery,wanProtocalType,deviceVersionType);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		this.total = bio.getTotal();
		return "bssSheetServInfo";
	}

	/**
	 * 导出业务信息
	 * 
	 * @author wwuchao
	 * @date
	 * @param
	 * @return String
	 */
	public String getBssSheetServInfoExcel()
	{
		logger.debug("getBssSheetServInfoExcel()");
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		bssSheetServList = bio.getBssSheetServInfoExcel(cityId, startOpenDate1,
				endOpenDate1, usernameType, username, servTypeId, openstatus, devicetype,
				gw_type, voipProtocalType, spec_id, cust_type_id,user_type_id,isRealtimeQuery, wanProtocalType,deviceVersionType);
		String excelCol =null;
		String excelTitle = null;
		
		/*添加黑龙江判断*/
		if (LipossGlobals.inArea(SDLT)) {
			excelCol = "username#city_name#dealdate#serv_type#serUsername#spec_name#device_serialnumber#open_status";
			excelTitle = "逻辑SN#属地#BSS受理时间#业务类型#用户帐号#终端规格#设备序列号#开通状态";
		} else if (LipossGlobals.inArea(HLJDX)) {
			excelCol = "username#city_name#user_type_id#dealdate#serv_type#serUsername#spec_name#device_serialnumber#open_status#type_id";
			excelTitle = "逻辑SN#属地#用户工单来源#BSS受理时间#业务类型#业务帐号#终端规格#设备序列号#开通状态#BSS终端类型";
		} else if (LipossGlobals.inArea(JLDX)) {
			excelCol = "username#city_name#user_type_id#dealdate#serv_type#serUsername#device_serialnumber#open_status#type_id";
			excelTitle = "逻辑SN#属地#用户工单来源#BSS受理时间#业务类型#业务帐号#设备序列号#开通状态#BSS终端类型";
		} 
		// SDLT-REQ-2018-09-13-YUZHIJIAN-001（山东电信ITMS平台工单导出功能）
		else if (LipossGlobals.inArea(SDDX)) {
			excelCol = "username#city_name#user_type_id#dealdate#serv_type#serUsername#spec_name#device_serialnumber#open_status#type_id";
			excelTitle = "逻辑SN#属地#用户工单来源#BSS受理时间#业务类型#业务帐号#终端规格#设备序列号#开通状态#BSS终端类型";
		}
		else {
			excelCol = "username#city_name#user_type_id#dealdate#serv_type#spec_name#device_serialnumber#open_status#type_id";
			excelTitle = "逻辑SN#属地#用户工单来源#BSS受理时间#业务类型#终端规格#设备序列号#开通状态#BSS终端类型";
		}
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "checkWork";
		data = bssSheetServList;
		return "excel";
	}

	public String getBssSheetServInfoCount()
	{
		this.setTime();
		total = bio.getBssSheetServInfoCount(cityId, startOpenDate1, endOpenDate1,
				usernameType, username, servTypeId, openstatus, devicetype, gw_type,
				voipProtocalType, spec_id, cust_type_id,user_type_id,isRealtimeQuery, wanProtocalType,deviceVersionType);
		ajax = total + "";
		return "ajax";
	}
	
	/**
	 * 配置下发信息
	 * @return
	 */
	public String getBssIssuedConfigDetail(){
		bssIssuedConfigMap = bio.getBssIssuedConfigDetail(gw_type,user_id, serv_type_id,isRealtimeQuery,serUsername);
		return "bssIssued";
	}

	/**
	 * BSS用户查询
	 */
	public String getBssCustomerServInfo()
	{
		logger.debug("getBssCustomerServInfo()");
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		bssSheetServList = bio.getBssCustomerServInfo(cityId, startOpenDate1,
				endOpenDate1, usernameType, username, devicetype, curPage_splitPage,
				num_splitPage, gw_type,isRealtimeQuery);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "bssSheetServInfo2";
	}

	public String toExcel()
	{
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		bssSheetServList = bio.getBssCustomerServInfo(cityId, startOpenDate1,
				endOpenDate1, usernameType, username, devicetype, gw_type,isRealtimeQuery);
		String excelCol = "username#city_name#dealdate#type_id#device_serialnumber";
		String excelTitle = "逻辑SN#属地#BSS受理时间#BSS终端类型#设备序列号";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "checkWork";
		data = bssSheetServList;
		return "excel";
	}

	/**
	 * 查询配置信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String	
	 */
	public String getConfigInfo()
	{
		logger.debug("getConfigInfo()");	
		configInfoList = bio.getConfigInfo(gw_type,deviceId, servTypeId, servstauts, wanType,isRealtimeQuery,serUsername);
		softUpList = bio.getSoftUpInfo(gw_type,deviceId, servTypeId, servstauts, wanType,isRealtimeQuery,serUsername);
		return "configInfo";
	}

	/**
	 * 查询配置详细信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public String getConfigDetail()
	{
		logger.debug("getConfigDetail()");
		configDetailList = bio.getConfigDetail(gw_type,strategyId,isRealtimeQuery);
		String inst_area=com.linkage.module.gwms.Global.instAreaShortName;
		if((JSDX.equals(inst_area)|| AHLT.equals(inst_area)) && configDetailList!=null && !configDetailList.isEmpty())
	    {
	    	 for(Map map:configDetailList){
	    		 String sheet_para=StringUtil.getStringValue(map,"sheet_para");
	    		 if (sheet_para.indexOf("Password") > 0 && sheet_para.indexOf("/Password") > 0)
	    			{
	    				String password = sheet_para.substring(
	    						sheet_para.indexOf("Password")+12,sheet_para.indexOf("/Password")-4);
	    				if(AHLT.equals(inst_area)){
	    					
	    					sheet_para = sheet_para.replace(password, "**********");
	    				}else{
	    					
	    					sheet_para = sheet_para.replace(password, "******");
	    				}
	    				logger.warn("id:{},Password:{}",StringUtil.getStringValue(map,"id"),password);
	    				map.put("sheet_para",sheet_para);
	    			}
	    	    }
	    }
		if(GSDX.equals(inst_area)||SXLT.equals(inst_area))
	    {
			doServStatus();
	    }
		return "configDetail";
	}
	
	/**
	 * 解析业务下发节点路径和值
	 * @return
	 */
	public String doServStatus()
	{
		String gatherConfig = LipossGlobals.getLipossProperty("gatherConfig");
		String sheetId = bio.getSheetId(strategyId);
		if(sheetId == null || sheetId.isEmpty())
		{
			return "doServStatusDetail";
		}					  
		doServStatusList = bio.doServStatus(gw_type, strategyId, isRealtimeQuery, sheetId);
		String faultdesc = bio.doServFaultStatus(sheetId);
		if(faultdesc != null && !faultdesc.isEmpty())
		{
			logger.warn("faultdesc is " + faultdesc);
			HashMap<String, String> faultmap = bio.strtoxml(faultdesc);
			for (Entry<String, String> entry : faultmap.entrySet())
			{
				logger.warn("faultName:"+entry.getKey().replaceAll(".\\d+.", ".ttt."));
				for(int i = 0; i < doServStatusList.size(); i++)
				{
					//对比时，如果节点是语音VoiceProfile下的节点，则不用将占位符替换ttt,否则无法分辨line.1 line.2
					if(doServStatusList.get(i).get("name").contains("VoiceProfile")){
						if(doServStatusList.get(i).get("name").equals(entry.getKey())){
							doServStatusList.get(i).remove("result");
							doServStatusList.get(i).put("result", entry.getValue());
							doServStatusList.subList(i + 1, doServStatusList.size()).clear();
						}
					}
					else if(doServStatusList.get(i).get("name").replaceAll(".%\\d+-InstanceNumber%.", ".ttt.").replaceAll(".\\d+.", ".ttt.").equals(entry.getKey().replaceAll(".\\d+.", ".ttt.")))
					{
						doServStatusList.get(i).remove("result");
						doServStatusList.get(i).put("result", entry.getValue());
						doServStatusList.subList(i + 1, doServStatusList.size()).clear();
					}
				}
			}
		}
		if(gatherConfig.equals("1")||gatherConfig.equals("2"))
		{
			dealData();
		}
		return "doServStatusDetail";
	}
	
	public static void main(String[] args)
	{
		String aa = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.%1-InstanceNumber%.WANPPPConnection.%2-InstanceNumber%.X_CU_MulticastVlan";
		aa = aa.replaceAll(".%\\d+-InstanceNumber%.", ".ttt.");
		System.out.println(aa);
	}
	
	public void dealData()
	{
		String devId = bio.getDeviceId(deviceSN);
		//需要对新增通道进行实快速采集,将采集通道赋值回下发节点路径
		HashMap<String, List<String>> ijMap = new HashMap<String, List<String>>();
		
		if("2".equals(LipossGlobals.getLipossProperty("gatherConfig"))){
			//根据strategyId取出策略表sheet_para中的vlan，以及serviceList
			String serviceList = "";
			if(servTypeId.equals("14")) serviceList = "VOIPVOICE";
			else if(servTypeId.equals("10")) serviceList = "INTERNET";
			else if(servTypeId.equals("11")) serviceList = "IPTVOTHER";
			String vlan = bio.getSheetParaById(strategyId);
			if(!vlan.contains("VlanId")) vlan = "";
			else{
				int begin = vlan.indexOf("VlanId")+"VlanId".length()+1;
				int end = vlan.indexOf("<", begin);
				vlan = vlan.substring(begin, end);
			}
			ijMap = gatherNetIJList2(devId,vlan,serviceList);
		}
		else{
			ijMap = gatherNetIJList(devId);
		}
		
		dealData(ijMap);
	}
	
	// 查询历史详情信息
	public void dealLogData()
	{
		String devId = bio.getDeviceId(deviceSN);
		//需要对新增通道进行实快速采集,将采集通道赋值回下发节点路径
		HashMap<String, List<String>> ijMap = new HashMap<String, List<String>>();
		
		if("2".equals(LipossGlobals.getLipossProperty("gatherConfig"))){
			//根据strategyId取出策略表sheet_para中的vlan，以及serviceList
			String serviceList = "";
			if(servTypeId.equals("14")) serviceList = "VOIPVOICE";
			else if(servTypeId.equals("10")) serviceList = "INTERNET";
			else if(servTypeId.equals("11")) serviceList = "IPTVOTHER";
			
			// 2020/04/26 新增历史详情信息
			String vlan = bio.getSheetParaLogById(strategyId);
			
			if(!vlan.contains("VlanId")) vlan = "";
			else{
				int begin = vlan.indexOf("VlanId")+"VlanId".length()+1;
				int end = vlan.indexOf("<", begin);
				vlan = vlan.substring(begin, end);
			}
			ijMap = gatherNetIJList2(devId,vlan,serviceList);
		}
		else{
			ijMap = gatherNetIJList(devId);
		}
		
		dealData(ijMap);
	}
	
	public void dealData(HashMap<String, List<String>> ijMap) 
	{
		int addWanCount = 0;
		String value = "";
		
		for(Map.Entry<String, List<String>> entry:ijMap.entrySet()){
			logger.warn("key="+entry.getKey());
			for(String a:entry.getValue()){
				logger.warn("value="+a);
			}
		}
		
		logger.warn("tab_sheet_para:");
		for(HashMap<String, String> a:doServStatusList){
			logger.warn("value="+a.get("name"));
		}
		
		if(servTypeId.equals("14"))
		{
			if(ijMap.get("voip") != null && !ijMap.get("voip").isEmpty() && ijMap.get("voip").size() != 0)
			{
				for(int i = 0; i < doServStatusList.size(); i++)
				{
					if(doServStatusList.get(i).get("name").contains("%1-InstanceNumber%"))
					{
						value = doServStatusList.get(i).get("name").replace("%1-InstanceNumber%", 
								ijMap.get("voip").get(0).split("##")[0]).replace("%2-InstanceNumber%",
										ijMap.get("voip").get(0).split("##")[1]);
						doServStatusList.get(i).remove("name");
						doServStatusList.get(i).put("name", value);
					}
				}
			}
		}
		else if(servTypeId.equals("11"))
		{
			if(ijMap.get("iptv") != null && !ijMap.get("iptv").isEmpty() && ijMap.get("iptv").size() != 0)
			{
				for(int i = 0; i < doServStatusList.size(); i++)
				{
					if(doServStatusList.get(i).get("name").contains("%1-InstanceNumber%"))
					{
							value = doServStatusList.get(i).get("name").replace("%1-InstanceNumber%", 
									ijMap.get("iptv").get(0).split("##")[0]).replace("%2-InstanceNumber%",
											ijMap.get("iptv").get(0).split("##")[1]);
							doServStatusList.get(i).remove("name");
							doServStatusList.get(i).put("name", value);
					}
				}
			}
		}
		else if(servTypeId.equals("10"))
		{
			for(int i = 0; i < doServStatusList.size(); i++)
			{
				//是否增加了两次wan通道实例
				if(doServStatusList.get(i).get("name").contains("%6-InstanceNumber%"))
				{
					addWanCount = 2;
				}
			}
			if(addWanCount == 2)
			{
				if(ijMap.get("internetRoute") != null && !ijMap.get("internetRoute").isEmpty() && ijMap.get("internetRoute").size() != 0
					&& ijMap.get("internetBridge") != null && !ijMap.get("internetBridge").isEmpty() && ijMap.get("internetBridge").size() != 0)
				{
					for(int i = 0; i < doServStatusList.size(); i++)
					{
						if(doServStatusList.get(i).get("name").contains("%1-InstanceNumber%"))
						{
								value = doServStatusList.get(i).get("name").replace("%1-InstanceNumber%", 
										ijMap.get("internetBridge").get(0).split("##")[0]).replace("%2-InstanceNumber%",
												ijMap.get("internetBridge").get(0).split("##")[1]);
								doServStatusList.get(i).remove("name");
								doServStatusList.get(i).put("name", value);
						}
						else if(doServStatusList.get(i).get("name").contains("%6-InstanceNumber%"))
						{
								value = doServStatusList.get(i).get("name").replace("%6-InstanceNumber%", 
										ijMap.get("internetRoute").get(0).split("##")[0]).replace("%7-InstanceNumber%",
												ijMap.get("internetRoute").get(0).split("##")[1]);
								doServStatusList.get(i).remove("name");
								doServStatusList.get(i).put("name", value);
						}
					}
				}
			}
			else
			{
				if((ijMap.get("internetRoute") != null && !ijMap.get("internetRoute").isEmpty() && ijMap.get("internetRoute").size() != 0)
					|| (ijMap.get("internetBridge") != null && !ijMap.get("internetBridge").isEmpty() && ijMap.get("internetBridge").size() != 0))
				{
					String gatherWanType = "1";
					for(int i = 0; i < doServStatusList.size(); i++)
					{
						if(doServStatusList.get(i).get("value").equals("IP_Routed"))
						{
							gatherWanType = "2";
						}
						if(doServStatusList.get(i).get("value").equals("PPPoE_Bridged"))
						{
							gatherWanType = "1";
						}
					}
					for(int i = 0; i < doServStatusList.size(); i++)
					{
						if(doServStatusList.get(i).get("name").contains("%1-InstanceNumber%"))
						{
							if(gatherWanType.equals("2"))
							{
								value = doServStatusList.get(i).get("name").replace("%1-InstanceNumber%", 
										ijMap.get("internetRoute").get(0).split("##")[0]).replace("%2-InstanceNumber%",
												ijMap.get("internetRoute").get(0).split("##")[1]);
								doServStatusList.get(i).remove("name");
								doServStatusList.get(i).put("name", value);
							}
							else if(gatherWanType.equals("1"))
							{
								value = doServStatusList.get(i).get("name").replace("%1-InstanceNumber%", 
										ijMap.get("internetBridge").get(0).split("##")[0]).replace("%2-InstanceNumber%",
												ijMap.get("internetBridge").get(0).split("##")[1]);
								doServStatusList.get(i).remove("name");
								doServStatusList.get(i).put("name", value);
							}
						}
					}
				}
			}
		}
		else if(servTypeId.equals("20"))
		{
			for(int i = 0; i < doServStatusList.size(); i++)
			{
				//是否增加了两次wan通道实例
				if(doServStatusList.get(i).get("name").contains("%9-InstanceNumber%"))
				{
					addWanCount = 2;
				}
			}
			if(addWanCount == 2)
			{
				if(ijMap.get("wifiRoute") != null && !ijMap.get("wifiRoute").isEmpty() && ijMap.get("wifiRoute").size() != 0
						&& ijMap.get("wifiBridge") != null && !ijMap.get("wifiBridge").isEmpty() && ijMap.get("wifiBridge").size() != 0)
				{
					for(int i = 0; i < doServStatusList.size(); i++)
					{
						if(doServStatusList.get(i).get("name").contains("%4-InstanceNumber%"))
						{
								value = doServStatusList.get(i).get("name").replace("%4-InstanceNumber%", 
										ijMap.get("wifiBridge").get(0).split("##")[0]).replace("%5-InstanceNumber%",
												ijMap.get("wifiBridge").get(0).split("##")[1]);
								doServStatusList.get(i).remove("name");
								doServStatusList.get(i).put("name", value);
						}
						else if(doServStatusList.get(i).get("name").contains("%9-InstanceNumber%"))
						{
								value = doServStatusList.get(i).get("name").replace("%9-InstanceNumber%", 
										ijMap.get("wifiRoute").get(0).split("##")[0]).replace("%10-InstanceNumber%",
												ijMap.get("wifiRoute").get(0).split("##")[1]);
								doServStatusList.get(i).remove("name");
								doServStatusList.get(i).put("name", value);
						}
					}
				}
			}
			else
			{
				if(ijMap.get("wifiRoute") != null && !ijMap.get("wifiRoute").isEmpty() && ijMap.get("wifiRoute").size() != 0)
				{
					for(int i = 0; i < doServStatusList.size(); i++)
					{
						if(doServStatusList.get(i).get("name").contains("%4-InstanceNumber%"))
						{
								value = doServStatusList.get(i).get("name").replace("%4-InstanceNumber%", 
										ijMap.get("wifiRoute").get(0).split("##")[0]).replace("%5-InstanceNumber%",
												ijMap.get("wifiRoute").get(0).split("##")[1]);
								doServStatusList.get(i).remove("name");
								doServStatusList.get(i).put("name", value);
						}
					}
				}
			}
		
		}
	}
	
	/**
	 * 快速采集格式:
	 * "1.1;DHCP_Routed;46;TR069","7.0;Bridged;;INTERNET","8.0;Bridged;42;OTHER","9.1;PPPoE_Routed;42;INTERNET",
	 * "10.3;PPPoE_Routed;41;INTERNET","11.1;DHCP_Routed;45;VOIP","12.0;Bridged;43;OTHER"
	 * 
	 * WAN Index实例举例(“1.2; Bridged;46;VOIP”, “2.1; PPPoE_Routed;1001;VOIP”)
	 * 示例说明:示例中包含两条WAN连接索引信息。
	 *   索引信息中的第一个字段”1.2”表示WAN连接的实例号；
	 *   第二个字段为接口类型，可选范围为Bridged，PPPoE_Routed，DHCP_Routed，STATIC_Routed；
	 *   通过第一个和第二个字段可以判断出WAN连接路径为InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.2
	 *   第三个参数为PVC号或VLAN ID
	 *   第四个参数为servicelist，可选范围为”TR069”,”INTERNET”,”VOIP”,”OTHER” ，有多个时用逗号分割
	 *  
	 * i,j即为WAN连接路径中InternetGatewayDevice.WANDevice.1.WANConnectionDevice.i.WANPPPConnection.j
	 * 
	 * @param deviceId
	 * @return
	 */
	
	public static HashMap<String, List<String>> gatherNetIJList(String deviceId)
	{
		HashMap<String, List<String>> ijMap = new HashMap<String, List<String>>();
		List<String> ijInternetBridgeList = new ArrayList<String>();
		List<String> ijInternetRouteList = new ArrayList<String>();
		List<String> ijVoipList = new ArrayList<String>();
		List<String> ijWifiBridgeList = new ArrayList<String>();
		List<String> ijWifiRouteList = new ArrayList<String>();
		List<String> ijOtherList = new ArrayList<String>();
		String wan_index = "InternetGatewayDevice.WANDevice.1.X_CT-COM_WANIndex";
		ArrayList<ParameValueOBJ> wan_index_result_list = null;
		logger.warn("[{}]获取wan连接索引", deviceId);
		wan_index_result_list = new ACSCorba("1").getValue(deviceId, wan_index);
		if (wan_index_result_list != null && !wan_index_result_list.isEmpty())
		{
			String wan_index_result = "";
			wan_index_result = wan_index_result_list.get(0).getValue()
					.replaceAll("\\(", "").replaceAll("\\)", "");
			if (!StringUtil.IsEmpty(wan_index_result))
			{
				String wan[] = wan_index_result.replace("\"", "").split(",");
				for (String wanPa : wan)
				{
					if (wanPa.contains("41"))
					{
						if (wanPa.contains(".") && wanPa.contains(";") && wanPa.contains("Bridged"))
						{
							String i = wanPa.split(";")[0].split("\\.")[0];
							String j = wanPa.split(";")[0].split("\\.")[1];
							ijInternetBridgeList.add((i + "##" + j));
						}
						else if(wanPa.contains(".") && wanPa.contains(";") && wanPa.contains("PPPoE_Routed"))
						{
							String i = wanPa.split(";")[0].split("\\.")[0];
							String j = wanPa.split(";")[0].split("\\.")[1];
							ijInternetRouteList.add((i + "##" + j));
						}
					}
					if (wanPa.contains("43"))
					{
						if (wanPa.contains(".") && wanPa.contains(";"))
						{
							String i = wanPa.split(";")[0].split("\\.")[0];
							String j = wanPa.split(";")[0].split("\\.")[1];
							ijOtherList.add((i + "##" + j));
						}
					}
					if (wanPa.contains("45"))
					{
						if (wanPa.contains(".") && wanPa.contains(";"))
						{
							String i = wanPa.split(";")[0].split("\\.")[0];
							String j = wanPa.split(";")[0].split("\\.")[1];
							String k = wanPa.split(";")[2];
							ijVoipList.add((i + "##" + j));
						}
					}
					if (wanPa.contains("42"))
					{
						if (wanPa.contains(".") && wanPa.contains(";") && wanPa.contains("Bridged"))
						{
							String i = wanPa.split(";")[0].split("\\.")[0];
							String j = wanPa.split(";")[0].split("\\.")[1];
							ijWifiBridgeList.add((i + "##" + j));
						}
						else if(wanPa.contains(".") && wanPa.contains(";") && wanPa.contains("PPPoE_Routed"))
						{
							String i = wanPa.split(";")[0].split("\\.")[0];
							String j = wanPa.split(";")[0].split("\\.")[1];
							ijWifiRouteList.add((i + "##" + j));
						}
					}
				}
			}
		}
		ijMap.put("internetBridge", ijInternetBridgeList);
		ijMap.put("internetRoute", ijInternetRouteList);
		ijMap.put("iptv", ijOtherList);
		ijMap.put("voip", ijVoipList);
		ijMap.put("wifiBridge", ijWifiBridgeList);
		ijMap.put("wifiRoute", ijWifiRouteList);
		logger.warn("ijMap is " + ijMap);
		return ijMap;
	}
	
	
	/**
	 * 非快速采集获取ij
	 * @param deviceId
	 * @param corba
	 * @return
	 */
	public static HashMap<String, List<String>> gatherNetIJList2(String deviceId, String vlan, String serviceList)
	{
		System.out.println("gatherNetIJList2 begin:"+vlan+" "+serviceList);
		ACSCorba corba = new ACSCorba("1");
		Map<String, String> restMap = new HashMap<String, String>();
		HashMap<String, List<String>> ijMap = new HashMap<String, List<String>>();
		
		String wanConnPath = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String wanServiceList = ".X_CU_ServiceList";
		String wanPPPConnection = ".WANPPPConnection.";
		String wanIPConnection = ".WANIPConnection.";
		String wanVlan = ".X_CU_VLAN";
		String connectionType = ".ConnectionType";
		List<String> ijInternetBridgeList = new ArrayList<String>();
		List<String> ijInternetRouteList = new ArrayList<String>();
		List<String> ijVoipList = new ArrayList<String>();
		List<String> ijWifiBridgeList = new ArrayList<String>();
		List<String> ijWifiRouteList = new ArrayList<String>();
		List<String> ijOtherList = new ArrayList<String>();
		
		ArrayList<String> wanConnPathsList = corba.getParamNamesPath(deviceId, wanConnPath, 0);
			
		if(null != wanConnPathsList && !wanConnPathsList.isEmpty()){
			
			List<String> tempWanConnPathsList = new ArrayList<String>();
			for(String wanConnPaths : wanConnPathsList){
				if(wanConnPaths.endsWith(".X_CU_ServiceList") || wanConnPaths.endsWith(".X_CU_VLAN")){
					tempWanConnPathsList.add(wanConnPaths);
				}
			}
			
			String[] paramNameArr = new String[tempWanConnPathsList.size()];
			for(int index=0;index<tempWanConnPathsList.size();index++){
				paramNameArr[index] = tempWanConnPathsList.get(index);
			}
			
			Map<String, String> paramValueMap = corba.getParaValueMap(deviceId,paramNameArr);
			if (null == paramValueMap || paramValueMap.isEmpty()) {
				logger.warn("[{}]获取ServiceList失败",deviceId);
			}else{
				for (Map.Entry<String, String> entry : paramValueMap.entrySet()) {
					logger.debug("[{}]{}={} ",new Object[]{deviceId, entry.getKey(), entry.getValue()});
					String paramName = entry.getKey();
					if (paramName.endsWith(wanServiceList)) {
						if (!StringUtil.IsEmpty(entry.getValue()) && serviceList.contains(entry.getValue())) {
							//InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.X_CU_VLAN
							//InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANIPConnection.1.X_CU_ServiceList
							String vlanKey = paramName.replace(".WANIPConnection.1.X_CU_ServiceList", wanVlan);
							vlanKey = vlanKey.replace(".WANPPPConnection.1.X_CU_ServiceList", wanVlan);
							if(!StringUtil.IsEmpty(paramValueMap.get(vlanKey)) && vlan.equals(paramValueMap.get(vlanKey))){
								String i = paramName.substring(wanConnPath.length(),wanConnPath.length()+1);
								int index = paramName.indexOf(wanServiceList);
								String j = paramName.substring(index-1,index);
								ijInternetBridgeList.add((i + "##" + j));
								ijInternetRouteList.add((i + "##" + j));
								ijVoipList.add((i + "##" + j));
								ijWifiBridgeList.add((i + "##" + j));
								ijWifiRouteList.add((i + "##" + j));
								ijOtherList.add((i + "##" + j));
							}
						}
					}
				}
			}
		}
		else{
			logger.warn("[{}]设备不在线,请接入设备", deviceId);
		}
		ijMap.put("internetBridge", ijInternetBridgeList);
		ijMap.put("internetRoute", ijInternetRouteList);
		ijMap.put("iptv", ijOtherList);
		ijMap.put("voip", ijVoipList);
		ijMap.put("wifiBridge", ijWifiBridgeList);
		ijMap.put("wifiRoute", ijWifiRouteList);
		logger.warn("ijMap is " + ijMap);
		return ijMap;
	}
	
	
	/**
	 * 解析历史业务下发节点路径和值
	 * @return
	 */
	public String doServLogStatus()
	{
		String gatherConfig = LipossGlobals.getLipossProperty("gatherConfig");
		String sheetId = bio.getLogSheetId(strategyId);
		if(sheetId == null || sheetId.isEmpty())
		{
			return "doServStatusDetail";
		}
		doServStatusList = bio.doServStatus(gw_type, strategyId, isRealtimeQuery, sheetId);
		String faultdesc = bio.doServFaultStatus(sheetId);
		if(faultdesc != null && !faultdesc.isEmpty())
		{
			HashMap<String, String> faultmap = bio.strtoxml(faultdesc);
			for (Entry<String, String> entry : faultmap.entrySet())
			{
				for(int i = 0; i < doServStatusList.size(); i++)
				{
					if(doServStatusList.get(i).get("name").equals(entry.getKey()))
					{
						doServStatusList.get(i).remove("result");
						doServStatusList.get(i).put("result", entry.getValue());
						doServStatusList.subList(i + 1, doServStatusList.size()).clear();
					}
				}
			}
		}
		if(gatherConfig.equals("1")||gatherConfig.equals("2"))
		{
			dealLogData();
		}
		return "doServStatusDetail";
	}
	
	/**
	 * 将密码密文进行解密，在页面展示明文
	 */
	public String decodePassword()
	{
		String configXml = request.getParameter("configXml");
		ajax = bio.decodePassword(configXml);
		return "ajax";
	}

	/**
	 * 查询配置历史信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public String getConfigLogInfo()
	{
		logger.debug("getConfigLogInfo()");
		configLogInfoList = bio.getConfigLogInfo(gw_type,deviceId, servTypeId, servstauts,
				wanType,isRealtimeQuery);
		return "configLogInfo";
	}

	/**
	 * 查询配置历史详细信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public String getConfigLogDetail()
	{
		logger.debug("getConfigDetail()");
		configDetailList = bio.getConfigLogDetail(gw_type,strategyId,isRealtimeQuery);
		String instArea=com.linkage.module.gwms.Global.instAreaShortName;
		if(GSDX.equals(instArea) || SXLT.equals(instArea))
	    {
			doServLogStatus();
	    }
		return "configLogDetail";
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
		if (("10".equals(servTypeId) && "true".equals(netServUp)) || ("25".equals(servTypeId) && "true".equals(netServUp)) || ("40".equals(servTypeId) && "true".equals(netServUp)))
		{
			bssSheetList = bio.getInternetBssSheet(cityId, userId, servTypeId, serUsername, gw_type,isRealtimeQuery);
			if (LipossGlobals.inArea(AHDX))
			{
				if(null != bssSheetList && bssSheetList.size() > 0)
				{
					String realTypeId = StringUtil.getStringValue(bssSheetList.get(0),"real_type_id");
					if("40".equals(realTypeId) || "40".equals(servTypeId))
					{
						return "schoolInternet";
					}
				}
			}
			return "internet";
		}
		
		if (LipossGlobals.inArea(SXLT) && ("11".equals(servTypeId) || "10".equals(servTypeId))){
			bssSheetList = bio.getBssSheet(cityId, userId, servTypeId, serUsername, gw_type,isRealtimeQuery,realBindPort,vlanid);
		}
		else{
			bssSheetList = bio.getBssSheet(cityId, userId, servTypeId, serUsername, gw_type,isRealtimeQuery,realBindPort);
		}
		
		bssParaList = bio.getBssParaList();
		//宽带，随选入云
		if ("10".equals(servTypeId) || "33".equals(servTypeId))
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
		else if ("20".equals(servTypeId))
		{
			return "sharedwifi";
		}
		// 智能音箱
		else if ("15".equals(servTypeId))
		{
			return "vBox";
		}
		// VPN
		else if ("38".equals(servTypeId))
		{
			return "vpn";
		}
		// 智能音箱
		else if ("32".equals(servTypeId))
		{
			return "syjw";
		}
		// 自适应wifi
		else if ("51".equals(servTypeId))
		{
			return "zysWifi";
		}
		//云网超宽带
		else if ("47".equals(servTypeId))
		{
			return "cloudNet";
		}
		else
		{
			return "bssSheet";
		}
	}

	/**
	 * 查询工单详细信息
	 * 
	 * @author wangsenbo
	 * @date Sep 14, 2010
	 * @param
	 * @return String
	 */
	public String getBssSheet2()
	{
		logger.debug("getConfigDetail()");
		bssParaList = bio.getBssPara(username);
		return "bssSheet2";
	}

	/**
	 * 根据逻辑SN检查设备上报了没
	 * 
	 * @author wangsenbo
	 * @date Oct 26, 2010
	 * @param
	 * @return String
	 */
	public String checkdevice()
	{
		logger.debug("checkdevice()");
		ajax = bio.checkdevice(username,isRealtimeQuery);
		return "ajax";
	}

	/**
	 * 调用预读接口，重新激活工单 业务下发 与 重新激活 共用此方法 modify by zhangchy 2011-10-19
	 * 
	 * @author wangsenbo
	 * @date Sep 15, 2010
	 * @param
	 * @return String
	 */
	public String callPreProcess()
	{
		logger.debug("callPreProcess()");
		ajax = bio
				.callPreProcess(userId, deviceId, oui, deviceSN, servTypeId, servstauts,isRealtimeQuery);
		return "ajax";
	}

	public String getSolution()
	{
		logger.warn("结果 ID;" + result_id);
		solutionData = bio.getSolutionInfo(result_id);
		logger.warn("处理意见;" + solutionData);
		return "solutioninfo";
	}

	
	
	
	public String getUser_type_id()
	{
		return user_type_id;
	}

	
	public void setUser_type_id(String user_type_id)
	{
		this.user_type_id = user_type_id;
	}

	public String getUser_id()
	{
		return user_id;
	}

	
	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}

	
	public String getServ_type_id()
	{
		return serv_type_id;
	}

	
	public void setServ_type_id(String serv_type_id)
	{
		this.serv_type_id = serv_type_id;
	}

	public Map<String, String> getBssIssuedConfigMap()
	{
		return bssIssuedConfigMap;
	}

	
	public void setBssIssuedConfigMap(Map<String, String> bssIssuedConfigMap)
	{
		this.bssIssuedConfigMap = bssIssuedConfigMap;
	}

	/**
	 * 批量激活工单
	 * 
	 * @author chenjie
	 * @date 2011-3-31
	 */
	public String callPreProcessBatch()
	{
		logger.debug("callPreProcessBatch()");
		// System.err.println(batchReset);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(batchReset);
		JSONArray datas = (JSONArray) jsonObject.get("datas");
		List<PreServInfoOBJ> objList = new ArrayList<PreServInfoOBJ>();
		for (int i = 0; i < datas.size(); i++)
		{
			JSONObject temp = (JSONObject) datas.get(i);
			PreServInfoOBJ preObj = new PreServInfoOBJ(temp.get("user_id").toString(),
					temp.get("device_id").toString(), temp.get("oui").toString(), temp
							.get("device_serialnumber").toString(), temp.get(
							"serv_type_id").toString(), temp.get("serv_status")
							.toString());
			preObj.setGatherId("1");
			objList.add(preObj);
		}
		// 调用批量激活
		ajax = bio.callPreProcessBatch(objList);
		return "ajax";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	// 当前年的1月1号
	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的00:00:00,如 2011-05-11 00:00:00
	private String getStartDateSD_DX()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = "";
		if ("false".equals(isRealtimeQuery)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,-1);
			String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
			time = yesterday+"23:"+"59:"+"59";
		}else{
			time = fmtrq.format(now.getTime());
		}
		return time;
	}

	
	public static int getConnectionFlag(String device_id){
		logger.debug("getConnectionFlag({})", device_id);
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, 0);
		int flag = 0;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}
	
	
	public String getServByUser()
	{
		logger.debug("getServiceByUser()");
		userId = request.getParameter("userId");
		bssSheetServList = bio.getServByUser(userId, gw_type,isRealtimeQuery);
		return "bssSheetServInfo3";
	}

	/**
	 * 根据loid获取工单详情
	 * @return
	 */
	public String getBSSInfoByLoid(){
		logger.warn("begin getBSSInfoByLoid with loid:{}",loid);
		bssSheetInfoList = bio.getBSSInfoByLoid(loid);
		return "ahltSheetInfo";
	}

	/**
	 * 安徽联通bss异常单处理页面 更改loid的开户时间
	 * @return
	 */
	public String changeOPenDateByUserId(){
		int result = bio.changeOpenDateByUserId(userId,openDate);
		ajax = result > 0 ? "修改成功！" : "修改失败！";
		return "ajax";
	}


	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the cityList
	 */
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId()
	{
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	/**
	 * @return the usernameType
	 */
	public String getUsernameType()
	{
		return usernameType;
	}

	/**
	 * @param usernameType
	 *            the usernameType to set
	 */
	public void setUsernameType(String usernameType)
	{
		this.usernameType = usernameType;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the servstauts
	 */
	public String getServstauts()
	{
		return servstauts;
	}

	/**
	 * @param servstauts
	 *            the servstauts to set
	 */
	public void setServstauts(String servstauts)
	{
		this.servstauts = servstauts;
	}

	/**
	 * @return the openstatus
	 */
	public String getOpenstatus()
	{
		return openstatus;
	}

	/**
	 * @param openstatus
	 *            the openstatus to set
	 */
	public void setOpenstatus(String openstatus)
	{
		this.openstatus = openstatus;
	}

	/**
	 * @return the startOpenDate
	 */
	public String getStartOpenDate()
	{
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}

	public String getStartOpenDate1()
	{
		return startOpenDate1;
	}

	public void setStartOpenDate1(String startOpenDate1)
	{
		this.startOpenDate1 = startOpenDate1;
	}

	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	public String getEndOpenDate1()
	{
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1)
	{
		this.endOpenDate1 = endOpenDate1;
	}

	public BssSheetServBIO getBio()
	{
		return bio;
	}

	public void setBio(BssSheetServBIO bio)
	{
		this.bio = bio;
	}

	public List<Map> getBssSheetServList()
	{
		return bssSheetServList;
	}

	public void setBssSheetServList(List<Map> bssSheetServList)
	{
		this.bssSheetServList = bssSheetServList;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getServTypeId()
	{
		return servTypeId;
	}

	public void setServTypeId(String servTypeId)
	{
		this.servTypeId = servTypeId;
	}

	public String getDeviceSN()
	{
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN)
	{
		this.deviceSN = deviceSN;
	}

	public String getWanType()
	{
		return wanType;
	}

	public void setWanType(String wanType)
	{
		this.wanType = wanType;
	}

	public List<Map> getConfigInfoList()
	{
		return configInfoList;
	}

	public void setConfigInfoList(List<Map> configInfoList)
	{
		this.configInfoList = configInfoList;
	}

	public String getStrategyId()
	{
		return strategyId;
	}

	public void setStrategyId(String strategyId)
	{
		this.strategyId = strategyId;
	}

	public List<Map> getConfigDetailList()
	{
		return configDetailList;
	}

	/**
	 * @param configDetailList
	 *            the configDetailList to set
	 */
	public void setConfigDetailList(List<Map> configDetailList)
	{
		this.configDetailList = configDetailList;
	}

	/**
	 * @return the configLogInfoList
	 */
	public List<Map> getConfigLogInfoList()
	{
		return configLogInfoList;
	}

	/**
	 * @param configLogInfoList
	 *            the configLogInfoList to set
	 */
	public void setConfigLogInfoList(List<Map> configLogInfoList)
	{
		this.configLogInfoList = configLogInfoList;
	}

	/**
	 * @return the bssSheetList
	 */
	public List<Map> getBssSheetList()
	{
		return bssSheetList;
	}

	/**
	 * @param bssSheetList
	 *            the bssSheetList to set
	 */
	public void setBssSheetList(List<Map> bssSheetList)
	{
		this.bssSheetList = bssSheetList;
	}

	/**
	 * @return the bssParaList
	 */
	public List<Map> getBssParaList()
	{
		return bssParaList;
	}

	/**
	 * @param bssParaList
	 *            the bssParaList to set
	 */
	public void setBssParaList(List<Map> bssParaList)
	{
		this.bssParaList = bssParaList;
	}

	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return the oui
	 */
	public String getOui()
	{
		return oui;
	}

	/**
	 * @param oui
	 *            the oui to set
	 */
	public void setOui(String oui)
	{
		this.oui = oui;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getDevicetype()
	{
		return devicetype;
	}

	public void setDevicetype(String devicetype)
	{
		this.devicetype = devicetype;
	}

	public String getBatchReset()
	{
		return batchReset;
	}

	public void setBatchReset(String batchReset)
	{
		this.batchReset = batchReset;
	}

	public void setServletRequest(HttpServletRequest req)
	{
		this.request = req;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public String getType_name()
	{
		return type_name;
	}

	public void setType_name(String typeName)
	{
		type_name = typeName;
	}

	public String getSerUsername()
	{
		return serUsername;
	}

	public void setSerUsername(String serUsername)
	{
		this.serUsername = serUsername;
	}

	public String getVoipProtocalType()
	{
		return voipProtocalType;
	}

	public void setVoipProtocalType(String voipProtocalType)
	{
		this.voipProtocalType = voipProtocalType;
	}

	public List<Map> getSoftUpList()
	{
		return softUpList;
	}

	public void setSoftUpList(List<Map> softUpList)
	{
		this.softUpList = softUpList;
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	public String getSpec_id()
	{
		return spec_id;
	}

	public void setSpec_id(String spec_id)
	{
		this.spec_id = spec_id;
	}

	public String getCust_type_id()
	{
		return cust_type_id;
	}

	public void setCust_type_id(String cust_type_id)
	{
		this.cust_type_id = cust_type_id;
	}

	public String getResult_id()
	{
		return result_id;
	}

	public void setResult_id(String result_id)
	{
		this.result_id = result_id;
	}

	public String getSolutionData()
	{
		return solutionData;
	}

	public void setSolutionData(String solutionData)
	{
		this.solutionData = solutionData;
	}

	public String getInstAreaName()
	{
		return instAreaName;
	}

	public void setInstAreaName(String instAreaName)
	{
		this.instAreaName = instAreaName;
	}

	public String getNetServUp()
	{
		return netServUp;
	}

	public void setNetServUp(String netServUp)
	{
		this.netServUp = netServUp;
	}

	
	public List<Map<String, String>> getDevicetypeList()
	{
		return devicetypeList;
	}

	
	public void setDevicetypeList(List<Map<String, String>> devicetypeList)
	{
		this.devicetypeList = devicetypeList;
	}

	public String getIsRealtimeQuery() {
		return isRealtimeQuery;
	}

	public void setIsRealtimeQuery(String isRealtimeQuery) {
		this.isRealtimeQuery = isRealtimeQuery;
	}
	
	public String getVoip_passwd()
	{
		return voip_passwd;
	}
	
	public void setVoip_passwd(String voip_passwd)
	{
		this.voip_passwd = voip_passwd;
	}
	
	public String getVoip_username()
	{
		return voip_username;
	}
	
	public void setVoip_username(String voip_username)
	{
		this.voip_username = voip_username;
	}
	public String getRealBindPort() {
		return realBindPort;
	}
	public void setRealBindPort(String realBindPort) {
		this.realBindPort = realBindPort;
	}
	public String getBindPort() {
		return bindPort;
	}
	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}
	public String getMaxNetNum() {
		return maxNetNum;
	}
	public void setMaxNetNum(String maxNetNum) {
		this.maxNetNum = maxNetNum;
	}
	public String getWanProtocalType()
	{
		return wanProtocalType;
	}
	public void setWanProtocalType(String wanProtocalType)
	{
		this.wanProtocalType = wanProtocalType;
	}
	
	public String getVlanid()
	{
		return vlanid;
	}
	
	public void setVlanid(String vlanid)
	{
		this.vlanid = vlanid;
	}
	
	public ArrayList<HashMap<String, String>> getDoServStatusList()
	{
		return doServStatusList;
	}
	
	public void setDoServStatusList(ArrayList<HashMap<String, String>> doServStatusList)
	{
		this.doServStatusList = doServStatusList;
	}

	public List<Map<String, String>> getDevVersionTypeMap() {
		return devVersionTypeMap;
	}

	public void setDevVersionTypeMap(List<Map<String, String>> devVersionTypeMap) {
		this.devVersionTypeMap = devVersionTypeMap;
	}

	public String getDeviceVersionType() {
		return deviceVersionType;
	}

	public void setDeviceVersionType(String deviceVersionType) {
		this.deviceVersionType = deviceVersionType;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public List<Map<String, String>> getBssSheetInfoList() {
		return bssSheetInfoList;
	}

	public void setBssSheetInfoList(List<Map<String, String>> bssSheetInfoList) {
		this.bssSheetInfoList = bssSheetInfoList;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
}
