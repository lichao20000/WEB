package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;
import com.linkage.module.itms.service.bio.BssSheetServStandardBIO;

/**
 * 业务查询
 * 
 * @author 王森博 1.0
 */
public class BssSheetServStandardACT extends splitPageAction implements
		SessionAware, ServletRequestAware {

	private static final long serialVersionUID = 2425363349057904543L;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(BssSheetServStandardACT.class);
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
	private BssSheetServStandardBIO bio;
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

	private Map<String, String> bssIssuedConfigMap;

	private String user_id;
	private String serv_type_id;

	// 用户工单来源
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
	// @Deprecated 页面请使用ms:inArea标签
	private String instAreaName;
	/**
	 * <pre>
	 * 系统整改，将宽带业务参数迁移到表tab_net_serv_param中，但由于有区域没有数据迁移。
	 * 故增加该字段用于标识是否从tab_net_serv_param表中获取宽带业务参数
	 * 当前仅当该值为"true"时，才用宽带业务表中获取数据。
	 * </pre>
	 */
	private String netServUp;
	private String realBindPort;
	
	private static final String JSDX="js_dx";
	private static final String SDLT="sd_lt";
	private static final String HBLT="hb_lt";
	
	
	/**
	 * 页面初始化
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public String init() {
		logger.debug("init()");
		instAreaName = com.linkage.module.gwms.Global.instAreaShortName;
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		devicetypeList = Global.G_DeviceTypeID_Name_List_Map;
		// BSS业务查询条件：终端规格江苏电信默认是E8C，其他默认为全部
		devicetype = LipossGlobals.inArea(JSDX) ? BssSheetServStandardACT.DEVICETYPE_DEFAULT_JSDX
				: BssSheetServStandardACT.DEVICETYPE_ALL;
		logger.warn("default device type id is " + devicetype);
		// DateTimeUtil dt = new DateTimeUtil();
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();

		if (request.getParameter("flag") != null) {
			// 家庭网关用户查询菜单
			return "init2";
		}
		return "init";
	}

	/**
	 * 查询业务信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return String
	 */
	public String getBssSheetServInfo() {
		logger.debug("getBssSheetServInfo()");
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		instAreaName = com.linkage.module.gwms.Global.instAreaShortName;
		bssSheetServList = bio.getBssSheetServInfo(cityId, startOpenDate1,
				endOpenDate1, usernameType, username, servTypeId, openstatus,
				devicetype, curPage_splitPage, num_splitPage, gw_type,
				voipProtocalType, spec_id, cust_type_id, user_type_id);
		maxPage_splitPage = bio.getMaxPage_splitPage();
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
	public String getBssSheetServInfoExcel() {
		logger.debug("getBssSheetServInfoExcel()");
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		bssSheetServList = bio.getBssSheetServInfoExcel(cityId, startOpenDate1,
				endOpenDate1, usernameType, username, servTypeId, openstatus,
				devicetype, gw_type, voipProtocalType, spec_id, cust_type_id,
				user_type_id);
		String excelCol = null;
		String excelTitle = null;
		if (LipossGlobals.inArea(SDLT)) {
			excelCol = "username#city_name#dealdate#serv_type#serv_status#serUsername#spec_name#device_serialnumber#open_status";
			excelTitle = "逻辑SN#属地#BSS受理时间#业务类型#业务状态#用户帐号#终端规格#设备序列号#开通状态";
		}else if(LipossGlobals.inArea(HBLT))
		{
			excelCol = "username#city_name#user_type_id#dealdate#serv_type#spec_name#device_serialnumber#open_status#type_id#gigabit_port";
			excelTitle = "逻辑SN#属地#用户工单来源#BSS受理时间#业务类型#终端规格#设备序列号#开通状态#BSS终端类型#是否存在光猫标识";
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

	public String getBssSheetServInfoCount() {
		this.setTime();
		total = bio.getBssSheetServInfoCount(cityId, startOpenDate1,
				endOpenDate1, usernameType, username, servTypeId, openstatus,
				devicetype, gw_type, voipProtocalType, spec_id, cust_type_id,
				user_type_id);
		ajax = total + "";
		return "ajax";
	}

	/**
	 * 配置下发信息
	 * 
	 * @return
	 */
	public String getBssIssuedConfigDetail() {
		bssIssuedConfigMap = bio.getBssIssuedConfigDetail(user_id,
				serv_type_id, serUsername);
		if (LipossGlobals.getLipossProperty("telecom").equals(
				com.linkage.module.gwms.Global.TELECOM_CUC)) {
			logger.warn("bssIssued_cuc");
			return "bssIssued_cuc";
		} else if ((LipossGlobals.getLipossProperty("telecom")
				.equals(com.linkage.module.gwms.Global.TELECOM_CMC))) {
			return "bssIssued_cmc";
		} else {
			return "bssIssued";
		}

	}

	/**
	 * BSS用户查询
	 */
	public String getBssCustomerServInfo() {
		logger.debug("getBssCustomerServInfo()");
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		bssSheetServList = bio.getBssCustomerServInfo(cityId, startOpenDate1,
				endOpenDate1, usernameType, username, devicetype,
				curPage_splitPage, num_splitPage, gw_type);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "bssSheetServInfo2";
	}

	public String toExcel() {
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		bssSheetServList = bio.getBssCustomerServInfo(cityId, startOpenDate1,
				endOpenDate1, usernameType, username, devicetype, gw_type);
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
	public String getConfigInfo() {
		logger.debug("getConfigInfo()");
		configInfoList = bio.getConfigInfo(deviceId, servTypeId, servstauts,
				wanType, serUsername, gw_type);
		softUpList = bio.getSoftUpInfo(deviceId, servTypeId, servstauts,
				wanType, serUsername);
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
	public String getConfigDetail() {
		logger.debug("getConfigDetail()");
		configDetailList = bio.getConfigDetail(strategyId,gw_type);
		return "configDetail";
	}

	/**
	 * 将密码密文进行解密，在页面展示明文
	 */
	public String decodePassword() {
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
	public String getConfigLogInfo() {
		logger.debug("getConfigLogInfo()");
		configLogInfoList = bio.getConfigLogInfo(deviceId, servTypeId,
				servstauts, wanType, gw_type);
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
	public String getConfigLogDetail() {
		logger.debug("getConfigDetail()");
		configDetailList = bio.getConfigLogDetail(strategyId, gw_type);
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
	public String getBssSheet() {
		// 当使用宽带业务信息表查询数据时
		if (("10".equals(servTypeId) && "true".equals(netServUp))
				|| ("25".equals(servTypeId) && "true".equals(netServUp))) {
			bssSheetList = bio.getInternetBssSheet(cityId, userId, servTypeId,
					serUsername, gw_type);
			return "internet";
		}

		bssSheetList = bio.getBssSheet(cityId, userId, servTypeId, serUsername,
				gw_type,realBindPort);
		bssParaList = bio.getBssParaList();
		if ("10".equals(servTypeId) || "33".equals(servTypeId)) {
			return "internet";
		} else if ("11".equals(servTypeId)) {
			return "iptv";
		} else if ("28".equals(servTypeId)) {
			return "iptv";
		}else if ("47".equals(servTypeId)) {
			return "cloudNet";
		}
		// 路由宽带
		else if ("16".equals(servTypeId)) {
			return "router";
		} else {
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
	public String getBssSheet2() {
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
	public String checkdevice() {
		logger.debug("checkdevice()");
		ajax = bio.checkdevice(username);
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
	public String callPreProcess() {
		logger.debug("callPreProcess()");
		if(SDLT.equals(com.linkage.module.gwms.Global.instAreaShortName) && "10".equals(servTypeId) && "2".equals(servstauts)){
			//山东联通宽带停机业务
			ajax = bio.callPreProcess(userId, deviceId, oui, deviceSN, servTypeId,
					servstauts,serUsername);
		}else{
			ajax = bio.callPreProcess(userId, deviceId, oui, deviceSN, servTypeId,
					servstauts);
		}
		return "ajax";
	}

	public String getSolution() {
		logger.warn("结果 ID;" + result_id);
		solutionData = bio.getSolutionInfo(result_id);
		logger.warn("处理意见;" + solutionData);
		return "solutioninfo";
	}

	public String getUser_type_id() {
		return user_type_id;
	}

	public void setUser_type_id(String user_type_id) {
		this.user_type_id = user_type_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getServ_type_id() {
		return serv_type_id;
	}

	public void setServ_type_id(String serv_type_id) {
		this.serv_type_id = serv_type_id;
	}

	public Map<String, String> getBssIssuedConfigMap() {
		return bssIssuedConfigMap;
	}

	public void setBssIssuedConfigMap(Map<String, String> bssIssuedConfigMap) {
		this.bssIssuedConfigMap = bssIssuedConfigMap;
	}

	/**
	 * 批量激活工单
	 * 
	 * @author chenjie
	 * @date 2011-3-31
	 */
	public String callPreProcessBatch() {
		logger.debug("callPreProcessBatch()");
		// System.err.println(batchReset);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(batchReset);
		JSONArray datas = (JSONArray) jsonObject.get("datas");
		List<PreServInfoOBJ> objList = new ArrayList<PreServInfoOBJ>();
		for (int i = 0; i < datas.size(); i++) {
			JSONObject temp = (JSONObject) datas.get(i);
			PreServInfoOBJ preObj = new PreServInfoOBJ(temp.get("user_id")
					.toString(), temp.get("device_id").toString(), temp.get(
					"oui").toString(), temp.get("device_serialnumber")
					.toString(), temp.get("serv_type_id").toString(), temp.get(
					"serv_status").toString());
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
	private void setTime() {
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate)) {
			startOpenDate1 = null;
		} else {
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate)) {
			endOpenDate1 = null;
		} else {
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	// 当前日期 00:00:00
	private String getStartDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		Calendar cal = Calendar.getInstance();
		now.set(GregorianCalendar.YEAR, cal.get(Calendar.YEAR));
		now.set(GregorianCalendar.MONTH, cal.get(Calendar.MONTH));
		now.set(GregorianCalendar.DATE, cal.get(Calendar.DATE));
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = "";
		time = fmtrq.format(now.getTime());
		return time;
	}

	public String getServByUser() {
		logger.debug("getServiceByUser()");
		userId = request.getParameter("userId");
		bssSheetServList = bio.getServByUser(userId, gw_type);
		return "bssSheetServInfo3";
	}

	/**
	 * @return the session
	 */
	public Map getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * @return the cityList
	 */
	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the usernameType
	 */
	public String getUsernameType() {
		return usernameType;
	}

	/**
	 * @param usernameType
	 *            the usernameType to set
	 */
	public void setUsernameType(String usernameType) {
		this.usernameType = usernameType;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the servstauts
	 */
	public String getServstauts() {
		return servstauts;
	}

	/**
	 * @param servstauts
	 *            the servstauts to set
	 */
	public void setServstauts(String servstauts) {
		this.servstauts = servstauts;
	}

	/**
	 * @return the openstatus
	 */
	public String getOpenstatus() {
		return openstatus;
	}

	/**
	 * @param openstatus
	 *            the openstatus to set
	 */
	public void setOpenstatus(String openstatus) {
		this.openstatus = openstatus;
	}

	/**
	 * @return the startOpenDate
	 */
	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getStartOpenDate1() {
		return startOpenDate1;
	}

	public void setStartOpenDate1(String startOpenDate1) {
		this.startOpenDate1 = startOpenDate1;
	}

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getEndOpenDate1() {
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1) {
		this.endOpenDate1 = endOpenDate1;
	}

	public BssSheetServStandardBIO getBio() {
		return bio;
	}

	public void setBio(BssSheetServStandardBIO bio) {
		this.bio = bio;
	}

	public List<Map> getBssSheetServList() {
		return bssSheetServList;
	}

	public void setBssSheetServList(List<Map> bssSheetServList) {
		this.bssSheetServList = bssSheetServList;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getDeviceSN() {
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	public String getWanType() {
		return wanType;
	}

	public void setWanType(String wanType) {
		this.wanType = wanType;
	}

	public List<Map> getConfigInfoList() {
		return configInfoList;
	}

	public void setConfigInfoList(List<Map> configInfoList) {
		this.configInfoList = configInfoList;
	}

	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public List<Map> getConfigDetailList() {
		return configDetailList;
	}

	/**
	 * @param configDetailList
	 *            the configDetailList to set
	 */
	public void setConfigDetailList(List<Map> configDetailList) {
		this.configDetailList = configDetailList;
	}

	/**
	 * @return the configLogInfoList
	 */
	public List<Map> getConfigLogInfoList() {
		return configLogInfoList;
	}

	/**
	 * @param configLogInfoList
	 *            the configLogInfoList to set
	 */
	public void setConfigLogInfoList(List<Map> configLogInfoList) {
		this.configLogInfoList = configLogInfoList;
	}

	/**
	 * @return the bssSheetList
	 */
	public List<Map> getBssSheetList() {
		return bssSheetList;
	}

	/**
	 * @param bssSheetList
	 *            the bssSheetList to set
	 */
	public void setBssSheetList(List<Map> bssSheetList) {
		this.bssSheetList = bssSheetList;
	}

	/**
	 * @return the bssParaList
	 */
	public List<Map> getBssParaList() {
		return bssParaList;
	}

	/**
	 * @param bssParaList
	 *            the bssParaList to set
	 */
	public void setBssParaList(List<Map> bssParaList) {
		this.bssParaList = bssParaList;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the oui
	 */
	public String getOui() {
		return oui;
	}

	/**
	 * @param oui
	 *            the oui to set
	 */
	public void setOui(String oui) {
		this.oui = oui;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax() {
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}

	public String getBatchReset() {
		return batchReset;
	}

	public void setBatchReset(String batchReset) {
		this.batchReset = batchReset;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String typeName) {
		type_name = typeName;
	}

	public String getSerUsername() {
		return serUsername;
	}

	public void setSerUsername(String serUsername) {
		this.serUsername = serUsername;
	}

	public String getVoipProtocalType() {
		return voipProtocalType;
	}

	public void setVoipProtocalType(String voipProtocalType) {
		this.voipProtocalType = voipProtocalType;
	}

	public List<Map> getSoftUpList() {
		return softUpList;
	}

	public void setSoftUpList(List<Map> softUpList) {
		this.softUpList = softUpList;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public String getCust_type_id() {
		return cust_type_id;
	}

	public void setCust_type_id(String cust_type_id) {
		this.cust_type_id = cust_type_id;
	}

	public String getResult_id() {
		return result_id;
	}

	public void setResult_id(String result_id) {
		this.result_id = result_id;
	}

	public String getSolutionData() {
		return solutionData;
	}

	public void setSolutionData(String solutionData) {
		this.solutionData = solutionData;
	}

	public String getInstAreaName() {
		return instAreaName;
	}

	public void setInstAreaName(String instAreaName) {
		this.instAreaName = instAreaName;
	}

	public String getNetServUp() {
		return netServUp;
	}

	public void setNetServUp(String netServUp) {
		this.netServUp = netServUp;
	}

	public List<Map<String, String>> getDevicetypeList() {
		return devicetypeList;
	}

	public void setDevicetypeList(List<Map<String, String>> devicetypeList) {
		this.devicetypeList = devicetypeList;
	}

	public String getRealBindPort() {
		return realBindPort;
	}

	public void setRealBindPort(String realBindPort) {
		this.realBindPort = realBindPort;
	}
}
