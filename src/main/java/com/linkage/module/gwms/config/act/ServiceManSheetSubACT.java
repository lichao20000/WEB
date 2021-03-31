package com.linkage.module.gwms.config.act;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import action.splitpage.splitPageAction;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.config.bio.ServiceManSheetSubBIO;
import com.linkage.module.gwms.obj.tabquery.HgwCustObj;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.service.WanTypeUtil;

/**
 * 业务手工工单控制类
 * 
 * @author Jason(3412)
 * @date 2010-4-21
 */
public class ServiceManSheetSubACT extends splitPageAction implements SessionAware {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ServiceManSheetSubACT.class);

	// 用户ID
	private String userId;
	private String loid;
	private String sn;
	// 用户账号
	private String username;
	// 设备ID
	private String deviceId;
	// OUI
	private String oui;
	// 设备序列号
	private String devSn;
	// 属地
	private String cityId;
	private String cityName;
	// 局向
	private String officeId;
	private String officeName;
	// 用户姓名
	private String realname;

	// 业务类型
	private String servTypeId;
	// 操作类型 (开户)
	private String operTypeId = "1";
	// 接入方式+上网方式
	private String wanType;
	// 接入方式
	private String accessType;
	// VlanID
	private String vlanid;
	// PVC
	private String vpi;
	private String vci;
	// 上网方式
	private String netType;
	// 路由方式：pppoe拨号用户账号和密码
	private String pppoeUsername;
	private String pppoePasswd;
	// 静态IP方式：IP地址,网关,子网掩码,DNS地址
	private String ip;
	private String gateway;
	private String mask;
	private String dns;
	// VOIP业务认证用户名和密码
	private String voipUsername;
	private String voipPasswd;
	// 绑定端口
	private String bindPort;

	/** ajax */
	private String ajax = "";
	// 初始化信息用到
	private String hasCust;
	private String hasServUser;

	private Map session;
	/** BIO */
	ServiceManSheetSubBIO serviceManSheetBio;

	private String gw_type;

	private String param;

	private String starttime;

	private String endtime;

	private String task_id;

	private List<Map> taskList;

	private String servType_query;
	private String status_query;
	private String expire_time_start;
	private String expire_time_end;


	private String city_id;
	private String type;
	private String countNum;

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	/**
	 * 初始化信息
	 */
	public String execute() {
		logger.debug("execute()");
		if (null != servTypeId && null != deviceId) {
			// 如果以超链接的方式连到该页面,需要传递的参数

		} else if (null != servTypeId && null != deviceId) {
			// 如果以超链接的方式连到该页面,需要传递的参数

		}
		return SUCCESS;
	}

	/**
	 * 查询业务用户信息,初始化页面的信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-22
	 * @return String
	 */
	public String queryServUserInfo() {
		logger.debug("queryServUserInfo()");
		logger.debug("业务类型:{},序列号：{},用户账号：{}", new Object[] { servTypeId,
				devSn, username });
		if (StringUtil.IsEmpty(devSn) && StringUtil.IsEmpty(username)) {
			logger.warn("查询时序列号和用户账号都为NULL，出错返回");
			hasCust = "-1";
		} else {
			HgwCustObj[] custObjArr = serviceManSheetBio.getCustObj(username,
					devSn, gw_type);
			if (null == custObjArr || custObjArr.length == 0) {
				logger.debug("null == custObj");
				hasCust = "-1";
			} else if (custObjArr.length > 1) {
				logger.debug("custObjArr.length > 1");
				hasCust = "-2";
			} else {
				HgwCustObj custObj = custObjArr[0];
				// 初始化用户相关信息
				deviceId = custObj.getDeviceId();
				userId = custObj.getUserId();
				username = custObj.getUsername();
				oui = custObj.getOui();
				devSn = custObj.getDeviceSerial();
				cityId = custObj.getCityId();
				cityName = custObj.getCityName();
				officeId = custObj.getOfficeId();
				officeName = custObj.getOfficeName();
				realname = custObj.getRealname();
				// 获取业务用户信息
				HgwServUserObj servUserObj = serviceManSheetBio.getServUserObj(
						userId, servTypeId, gw_type);
				if (null != servUserObj) {
					logger.debug("null != servUserObj");
					// 初始化业务用户信息
					vpi = servUserObj.getVpiid();
					vci = servUserObj.getVciid();
					vlanid = servUserObj.getVlanid();
					wanType = servUserObj.getWanType();
					accessType = StringUtil
							.getStringValue(WanTypeUtil
									.getAccessType(StringUtil
											.getIntegerValue(servUserObj
													.getWanType())));
					netType = StringUtil.getStringValue(WanTypeUtil
							.getNetType(StringUtil.getIntegerValue(servUserObj
									.getWanType())));
					pppoeUsername = servUserObj.getUsername();
					pppoePasswd = servUserObj.getPasswd();

					ip = servUserObj.getIpAddress();
					mask = servUserObj.getIpMask();
					gateway = servUserObj.getGateway();
					dns = servUserObj.getDns();

					voipUsername = servUserObj.getUsername();
					voipPasswd = servUserObj.getPasswd();
				} else {
					logger.debug("null == servUserObj");
					hasServUser = "-1";
				}
			}
		}
		return "sheetInfo";
	}

	/**
	 * 手工工单业务下发
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-22
	 * @return String
	 */
	public String doService() {
		logger.debug("doService()");
		long nowTime = new Date().getTime() / 1000;
		HgwServUserObj hgwServUserObj = new HgwServUserObj();

		wanType = StringUtil.getStringValue(WanTypeUtil.getWanType(
				StringUtil.getIntegerValue(accessType),
				StringUtil.getIntegerValue(netType)));

		hgwServUserObj.setBindPort(bindPort);
		hgwServUserObj.setDns(dns);
		hgwServUserObj.setGateway(gateway);
		hgwServUserObj.setIpAddress(ip);
		hgwServUserObj.setIpMask(mask);
		hgwServUserObj.setServTypeId(servTypeId);
		hgwServUserObj.setUserId(userId);
		hgwServUserObj.setVciid(vci);
		hgwServUserObj.setVpiid(vpi);
		hgwServUserObj.setVlanid(vlanid);
		hgwServUserObj.setWanType(wanType);

		hgwServUserObj.setPasswd(pppoePasswd);
		hgwServUserObj.setUsername(pppoeUsername);

		int iret = -1;
		if (ConstantClass.ITMS_NET == StringUtil.getIntegerValue(servTypeId)) {

			iret = serviceManSheetBio.netOpen(hgwServUserObj, hasServUser,
					deviceId, oui, devSn, operTypeId, gw_type);
		} else if (ConstantClass.ITMS_IPTV == StringUtil
				.getIntegerValue(servTypeId)) {

			iret = serviceManSheetBio.netOpen(hgwServUserObj, hasServUser,
					deviceId, oui, devSn, operTypeId, gw_type);
		} else if (ConstantClass.ITMS_VOIP == StringUtil
				.getIntegerValue(servTypeId)) {
			// hgwServUserObj.setPasswd(voipPasswd);
			// hgwServUserObj.setUsername(voipUsername);
			// iret = serviceManSheetBio.voipOpen(hgwServUserObj, hasServUser,
			// deviceId, oui, devSn, operTypeId);
		} else if (ConstantClass.ITMS_WLAN == StringUtil
				.getIntegerValue(servTypeId)) {

			iret = serviceManSheetBio.netOpen(hgwServUserObj, hasServUser,
					deviceId, oui, devSn, operTypeId, gw_type);
		}
		ajax = StringUtil.getStringValue(iret);
		return "ajax";
	}

	/**
	 * 终端业务下发，向终端下发业务用户表中已经存在的业务（BSS受理的业务）
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-6-11
	 * @return String
	 */
	public String servDone() {
		// 获取参数
		String deviceId = this.deviceId;
		int servTypeId = StringUtil.getIntegerValue(this.servTypeId);

		// 调用BIO下发业务
		int iret = serviceManSheetBio
				.serviceDone(deviceId, servTypeId, gw_type);
		ajax = StringUtil.getStringValue(iret);
		return "ajax";
	}

	public String queryUndoNum() {
		logger.debug("queryUndoNum()");
		ajax = String.valueOf(serviceManSheetBio.queryUndoNum());
		return "ajax";
	}
	/**
	 * 批量终端业务下发
	 * 
	 * @return String
	 */
	public String serviceDoneByBatch() {
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		long user_oid = user.getId();
		int operlog_type = 1;
		long oper_time = new DateTimeUtil().getLongTime();
		String oper_object = "批量业务下发";
		String oper_result = "";
		// 获取参数
		String deviceIds = this.deviceId;
		String[] deviceStr = null;
		List<String> list = new ArrayList<String>();
		if (deviceIds.contains(",")) { // 多个设备
			deviceStr = deviceIds.split(",");
			for (int i = 0; i < deviceStr.length; i++) {
				String deviceId = deviceStr[i];
				list.add(deviceId);
			}
		} else { // 单个设备
			list.add(deviceIds);
		}
		int servTypeId = StringUtil.getIntegerValue(this.servTypeId);
		// 调用BIO下发业务
		int iret = serviceManSheetBio.serviceDoneByBatch(list, servTypeId,
				gw_type,user_oid);
		ajax = StringUtil.getStringValue(iret);
		if("1".equals(ajax)){
			oper_result = "1";
		}else{
			oper_result = "0";
		}
		serviceManSheetBio.serviceDoneRecord(user_oid, operlog_type, oper_time, oper_object, oper_result);
		return "ajax";
	}


	/**
	 * 批量终端业务下发
	 *
	 * @return String
	 */
	public String serviceDoneByBatchXJDX() {
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		long user_oid = user.getId();
		int operlog_type = 1;
		long oper_time = new DateTimeUtil().getLongTime();
		String oper_object = "批量业务下发";
		String oper_result = "";
		// 获取参数
		String deviceIds = this.deviceId;
		String[] deviceStr = null;
		List list = new ArrayList<String>();
		if("0".equals(deviceIds)){
			logger.warn("入任务表-设备id列表/sql(高级查询)");
			if(StringUtil.IsEmpty(param))
			{
				logger.warn("param为空");
			}
			String[] _param = param.split("\\|");
			String matchSQL = _param[10];
			list = serviceManSheetBio.getListBySql(matchSQL);
		}else {

			if (deviceIds.contains(",")) { // 多个设备
				deviceStr = deviceIds.split(",");
				for (int i = 0; i < deviceStr.length; i++) {
					String deviceId = deviceStr[i];
					Map inMap = new HashMap();
					inMap.put("device_id",deviceId);
					list.add(inMap);
				}
			} else { // 单个设备
				Map inMap = new HashMap();
				inMap.put("device_id",deviceId);
				list.add(inMap);
			}
		}
		int servTypeId = StringUtil.getIntegerValue(this.servTypeId);
		// 调用BIO下发业务
		int iret = serviceManSheetBio.serviceDoneByBatchXJDX(list, servTypeId,
				gw_type,user_oid);
		ajax = StringUtil.getStringValue(iret);
		if("1".equals(ajax)){
			oper_result = "1";
		}else{
			oper_result = "0";
		}
		serviceManSheetBio.serviceDoneRecord(user_oid, operlog_type, oper_time, oper_object, oper_result);
		return "ajax";
	}
	
	
	
	/**
	 * 批量组播下移
	 * 
	 * @return String
	 */
	public String setMulticastBatch() {
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		long user_oid = user.getId();
		int operlog_type = 1;
		long oper_time = new DateTimeUtil().getLongTime();
		String oper_object = "批量组播下移";
		String oper_result = "";
		// 获取参数
		String deviceIds = this.deviceId;
		String sns = this.sn;
		String loids = this.loid;
		String userids = this.userId;
		String[] deviceStr = null;
		String[] snStr = null;
		String[] loidStr = null;
		String[] useridStr = null;
		List<String> list = new ArrayList<String>();
		List<String> snlist = new ArrayList<String>();
		List<String> loidlist = new ArrayList<String>();
		List<String> useridlist = new ArrayList<String>();
		if (deviceIds.contains(",")) { // 多个设备
			deviceStr = deviceIds.split(",");
			for (int i = 0; i < deviceStr.length; i++) {
				String deviceId = deviceStr[i];
				list.add(deviceId);
			}
			
			snStr = sns.split(",");
			for (int i = 0; i < snStr.length; i++) {
				String sn = snStr[i];
				snlist.add(sn);
			}
			
			loidStr = loids.split(",");
			for (int i = 0; i < loidStr.length; i++) {
				String loid = loidStr[i];
				loidlist.add(loid);
			}
			
			useridStr = userids.split(",");
			for (int i = 0; i < useridStr.length; i++) {
				String userid = useridStr[i];
				useridlist.add(userid);
			}
		} else { // 单个设备
			list.add(deviceIds);
			snlist.add(sns);
			loidlist.add(loids);
			useridlist.add(userids);
		}
		// 调用BIO下发业务
		try
		{
			serviceManSheetBio.setMulticastBatch(list,snlist,loidlist,useridlist,user_oid);
			ajax = "批量组播下移成功！";
			oper_result = "1";
		}
		catch (Exception e)
		{
			ajax = e.getMessage();
			oper_result = "0";
		}
		
		//serviceManSheetBio.serviceDoneRecord(user_oid, operlog_type, oper_time, oper_object, oper_result);
		return "ajax";
	}
	
	
	public String queryMulticastNum() {
		logger.debug("queryMulticastNum()");
		ajax = String.valueOf(serviceManSheetBio.queryMulticastNum());
		return "ajax";
	}

	public String init4XJDX()
	{
		DateTimeUtil dt = new DateTimeUtil();
		this.endtime = dt.getDate();
		this.starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(this.endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 86400L - 1L) * 1000L);
		this.endtime = dt.getLongDate();
		dt = new DateTimeUtil(this.starttime);
		this.starttime = dt.getLongDate();
		return "init4XJDX";
	}

	public String queryList() {

		taskList = serviceManSheetBio.queryTaskList(servType_query, status_query, expire_time_start, expire_time_end,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = serviceManSheetBio.queryTaskCount(servType_query, status_query, expire_time_start, expire_time_end,
				curPage_splitPage, num_splitPage);

		return "serviceList";
	}

	public String del() {
		ajax = serviceManSheetBio.delTask(task_id);
		return "ajax";
	}

	public String queryTaskDetail() throws UnsupportedEncodingException {
		servTypeId = URLDecoder.decode(servTypeId, "utf-8");
		taskList = serviceManSheetBio.queryTaskGyCityList(task_id, servTypeId, curPage_splitPage, num_splitPage);
		maxPage_splitPage = serviceManSheetBio.queryTaskGyCityCount(task_id, servTypeId, curPage_splitPage, num_splitPage);
		return "serviceDetail";
	}

	public String queryTaskDetailExcel() throws UnsupportedEncodingException {
		logger.debug("queryTaskDetailExcel()");
		servTypeId = URLDecoder.decode(servTypeId, "utf-8");
		taskList = serviceManSheetBio.queryTaskGyCityList(task_id, servTypeId, curPage_splitPage, num_splitPage);
		logger.warn(taskList.toString());
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "业务类型#属地#总数#成功数#失败数#未做数";
		excelCol = "servTypeNme#city_name#totalNum#succNum#failNum#unDoneNum";

		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "taskGyCity";
		data = taskList;
		return "excel";
	}

	public String queryDevList() {

		taskList = serviceManSheetBio.queryDevList(task_id, city_id, type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = serviceManSheetBio.queryDevCount(countNum, curPage_splitPage, num_splitPage);

		return "serviceDevList";
	}

	public String queryDevListExcel() {
		logger.debug("queryDevListExcel4CQ()");
		taskList = serviceManSheetBio.queryDevList(task_id, city_id, type, 0, 0);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "设备序列号#厂商#型号#区域#版本#创建时间#执行结果";
		excelCol = "device_serialnumber#vendor_id#device_model_id#city_name#version#add_time#status";

		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devList";
		data = taskList;
		logger.warn("....."+data.toString());
		return "excel";
	}
	// /**
	// * <p>
	// * 获取设备绑定的用户
	// * </p>
	// * @return ajax方式
	// */
	// public String getUserIdByDeviceId()
	// {
	// //获取参数
	// ajax = serviceManSheetBio.getUserIdByDeviceId(this.deviceId);
	// return "ajax";
	// }
	//
	// /**
	// * <p>
	// * 获取用户业务帐号
	// * </p>
	// * @return
	// */
	// public String getUserNameByservTypeId()
	// {
	// ajax =
	// serviceManSheetBio.getUserNameByservTypeId(userId,this.servTypeId);
	// return "ajax";
	// }

	/** getter, setter methods */
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getWanType() {
		return wanType;
	}

	public void setWanType(String wanType) {
		this.wanType = wanType;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getVlanid() {
		return vlanid;
	}

	public void setVlanid(String vlanid) {
		this.vlanid = vlanid;
	}

	public String getVpi() {
		return vpi;
	}

	public void setVpi(String vpi) {
		this.vpi = vpi;
	}

	public String getVci() {
		return vci;
	}

	public void setVci(String vci) {
		this.vci = vci;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getPppoeUsername() {
		return pppoeUsername;
	}

	public void setPppoeUsername(String pppoeUsername) {
		this.pppoeUsername = pppoeUsername;
	}

	public String getPppoePasswd() {
		return pppoePasswd;
	}

	public void setPppoePasswd(String pppoePasswd) {
		this.pppoePasswd = pppoePasswd;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getVoipUsername() {
		return voipUsername;
	}

	public void setVoipUsername(String voipUsername) {
		this.voipUsername = voipUsername;
	}

	public String getVoipPasswd() {
		return voipPasswd;
	}

	public void setVoipPasswd(String voipPasswd) {
		this.voipPasswd = voipPasswd;
	}

	public String getBindPort() {
		return bindPort;
	}

	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}

	public String getAjax() {
		return ajax;
	}

	public void setServiceManSheetBio(ServiceManSheetSubBIO serviceManSheetBio) {
		this.serviceManSheetBio = serviceManSheetBio;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getHasCust() {
		return hasCust;
	}

	public String getHasServUser() {
		return hasServUser;
	}

	@Override
	public void setSession(Map arg0) {
		session = arg0;
	}

	public void setHasCust(String hasCust) {
		this.hasCust = hasCust;
	}

	public void setHasServUser(String hasServUser) {
		this.hasServUser = hasServUser;
	}

	public String getOperTypeId() {
		return operTypeId;
	}

	public void setOperTypeId(String operTypeId) {
		this.operTypeId = operTypeId;
	}


	public static Logger getLogger() {
		return logger;
	}

	public Map getSession() {
		return session;
	}

	public ServiceManSheetSubBIO getServiceManSheetBio() {
		return serviceManSheetBio;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	
	public String getLoid()
	{
		return loid;
	}

	
	public void setLoid(String loid)
	{
		this.loid = loid;
	}

	
	public String getSn()
	{
		return sn;
	}

	
	public void setSn(String sn)
	{
		this.sn = sn;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public List<Map> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Map> taskList) {
		this.taskList = taskList;
	}

	public String getServType_query() {
		return servType_query;
	}

	public void setServType_query(String servType_query) {
		this.servType_query = servType_query;
	}

	public String getStatus_query() {
		return status_query;
	}

	public void setStatus_query(String status_query) {
		this.status_query = status_query;
	}

	public String getExpire_time_start() {
		return expire_time_start;
	}

	public void setExpire_time_start(String expire_time_start) {
		this.expire_time_start = expire_time_start;
	}

	public String getExpire_time_end() {
		return expire_time_end;
	}

	public void setExpire_time_end(String expire_time_end) {
		this.expire_time_end = expire_time_end;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCountNum() {
		return countNum;
	}

	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}
}
