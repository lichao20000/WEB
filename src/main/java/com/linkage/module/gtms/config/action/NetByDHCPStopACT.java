package com.linkage.module.gtms.config.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.NetByDHCPStopServ;
import com.linkage.module.gwms.util.StringUtil;

public class NetByDHCPStopACT extends splitPageAction implements SessionAware {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(NetByDHCPStopACT.class);
	// 传参
	private String deviceIds = "";
	// Session
	private Map<String, Object> session;
	// 查询方式
	private String gwShare_queryField = "";
	// 查询参数
	private String gwShare_queryParam = "";
	// 查询宽带帐号
	private List<Map<String, Object>> deviceList;
	private NetByDHCPStopServ bio;
	private String ajax = "";
	/** 宽带帐号 **/
	private String username;
	private long userId;
	private String gw_type = "";
	// 属地
	private String cityId = "00";
	// 符合条件的LOID
	private static String isSuccess = "1";
	private String strategy_type = "";
	// 状态
	private String status = "";
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 查询列表
	private List<Map> list = new ArrayList<Map>();
	/** 获取device_id */
	private List<Map> device_idList = null;
	/** 条件内容 */
	private String condition = "";
	/** 开通状态 */
	private String openState = "";
	/** 条件 */
	private String con = "";

	/*
	 * 获取宽带帐号
	 * 
	 * public String getUsername() { ajax = bio.getUsername(deviceIds); return
	 * "ajax"; }
	 */

	/*
	 * 绑定查询校验
	 */
	public String isBind() {
		logger.warn("NetByDHCPStopACT.isBind()");
		ajax = bio.isBind(gwShare_queryField, gwShare_queryParam);
		return "ajax";
	}

	/**
	 * 配置
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doConfigAll() throws IOException {
		logger.warn("NetByDHCPStopACT.doConfigAll()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		List<String> list = new ArrayList<String>();
		String[] deviceStr = null;
		if (deviceIds.contains(",")) { // 多个设备
			deviceStr = deviceIds.split(",");
			for (String deviceId : deviceStr) {
				list.add(deviceId);
			}
		} else { // 单个设备
			list.add(deviceIds);
		}
		ajax = bio.doConfig(userId, list, strategy_type, username, gw_type);
		return "ajax";
	}

	/**
	 * 初始化页面
	 * 
	 * @author SONGJJ
	 * @date 2015.05.28
	 * @param
	 * @return String
	 */
	public String init() {
		logger.warn("NetByDHCPStopACT.init()");
		setTime();
		return "init";
	}

	public String queryNetByDHCPList() {
		logger.warn("NetByDHCPStopACT.queryNetByDHCPList()");
		device_idList = bio.getDao().getDevice_id(con, condition.trim());
		if (device_idList.size() > 1 && "-1".equals(con)) {
			ajax = StringUtil.getStringValue(device_idList.size());
			return "ajax";
		} else {
			list = bio.getDetailsForPage(device_idList, starttime, endtime,
					openState, curPage_splitPage, num_splitPage);
			maxPage_splitPage = bio.getDetailsCount(device_idList, starttime,
					endtime, openState, num_splitPage);
			return "list";
		}

	}

	public void setTime() {
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate(); // 获取当前时间
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();
		starttime = dt.getFirtDayOfMonth(); // 获取开始时间，为当月时间的第一天
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public NetByDHCPStopServ getBio() {
		return bio;
	}

	public void setBio(NetByDHCPStopServ bio) {
		this.bio = bio;
	}

	public String getGwShare_queryField() {
		return gwShare_queryField;
	}

	public void setGwShare_queryField(String gwShare_queryField) {
		this.gwShare_queryField = gwShare_queryField;
	}

	public String getGwShare_queryParam() {
		return gwShare_queryParam;
	}

	public void setGwShare_queryParam(String gwShare_queryParam) {
		this.gwShare_queryParam = gwShare_queryParam;
	}

	public List<Map<String, Object>> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Map<String, Object>> deviceList) {
		this.deviceList = deviceList;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public static String getIsSuccess() {
		return isSuccess;
	}

	public static void setIsSuccess(String isSuccess) {
		NetByDHCPStopACT.isSuccess = isSuccess;
	}

	public String getStrategy_type() {
		return strategy_type;
	}

	public void setStrategy_type(String strategy_type) {
		this.strategy_type = strategy_type;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public List<Map> getDevice_idList() {
		return device_idList;
	}

	public void setDevice_idList(List<Map> device_idList) {
		this.device_idList = device_idList;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getOpenState() {
		return openState;
	}

	public void setOpenState(String openState) {
		this.openState = openState;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

}
