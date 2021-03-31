package com.linkage.module.gtms.stb.resource.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.stb.resource.serv.StbSetConfParamBIO;

/**
 */
@SuppressWarnings("unused")
public class StbSetConfParamACT extends splitPageAction 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(StbSetConfParamACT.class);
	
	/** 设备序列号 */
	private String deviceSn = "";
	/** 设备mac地址 */
	private String deviceMac = "";
	/** 厂商ID */
	private String vendorId = "";
	/** 型号 */
	private String deviceModelId = "";
	/** 属地ID */
	private String cityId = "";
	/** 属地ID */
	private String citynext = "";
	/** 修改开始时间 */
	private String updateStartTime = "";
	/** 修改结束时间 */
	private String updateEndTime = "";
	/** 下发开始时间 */
	private String setLastStartTime = "";
	/** 下发结束时间 */
	private String setLastEndTime = "";
	/** 自动待机开关(0关、1开) */
	private String auto_sleep_mode = "";
	/** 自动待机关闭时长 */
	private String auto_sleep_time = "";
	/** 有线网络类型(1：IPv4，2：IPv6，3：IPv4/v6) */
	private String ip_protocol_version_lan = "";
	/** 无线网络类型(1：IPv4，2：IPv6，3：IPv4/v6) */
	private String ip_protocol_version_wifi = "";
	/** 业务账号 */
	private String servAccount = "";
	
	/** 数据集 */
	public List<Map<String, String>> data;
	private Map<String, String> paramMap;
	/** 查询总数 */
	public int queryCount;
	
	/**湖南联通特制 	传入参数showType；
	 * 1：“查看详细”、“编辑”、“删除”；
	 * 2：“查看详细”、“编辑”、；
	 * 3：“查看详细”*/
	private String showType="3";
	
	private String deviceId;
	private String ajax;
	private StbSetConfParamBIO bio;
	
	private String newParams;
	
	
	public String init()
	{
//		updateStartTime=setTime(0);
//		updateEndTime=setTime(1);
//		setLastStartTime=setTime(0);
//		setLastEndTime=setTime(1);

		return "init";
	}
	
	/**
	 * 获取设备零配置参数
	 */
	public String getConfParam() 
	{
		ajax = bio.getConfParam(deviceId);
		return "ajax";
	}
	
	/**
	 * 修改设备零配置参数
	 */
	public String updateConfParam()
	{
		logger.warn("[{}]-updateConfParam({})",deviceId,newParams);
		ajax = bio.updateConfParam(deviceId,newParams);
		
		return "ajax";
	}
	
	/**
	 * 获取设备列表
	 */
	public String getConfParamList()
	{
		data = bio.getConfParamList(curPage_splitPage,num_splitPage,
					deviceSn,deviceMac,vendorId,deviceModelId,cityId,citynext,
					setTime(updateStartTime),setTime(updateEndTime),
					setTime(setLastStartTime),setTime(setLastEndTime),
					auto_sleep_mode,auto_sleep_time,ip_protocol_version_lan,
					ip_protocol_version_wifi,servAccount);
		maxPage_splitPage = bio.countConfParamList(num_splitPage,
				deviceSn,deviceMac,vendorId,deviceModelId,cityId,citynext,
				setTime(updateStartTime),setTime(updateEndTime),
				setTime(setLastStartTime),setTime(setLastEndTime),
				auto_sleep_mode,auto_sleep_time,ip_protocol_version_lan,
				ip_protocol_version_wifi,servAccount);
		queryCount = bio.getQueryCount();
		return "list";
	}
	
	/**
	 * 获取设备零配置参数
	 */
	public String getConParamInfo() 
	{
		paramMap=bio.getConfParamMap(deviceId);
		return "info";
	}
	
	/**
	 * 删除设备零配置参数
	 */
	public String deleteConParamInfo()
	{
		logger.warn("[{}]-deleteConParamInfo()",deviceId);
		ajax = bio.deleteConParamInfo(deviceId);
	
		return "ajax";
	}
	

	/**
	 * 获取当前时间或本月的初始时间
	 */
	private String setTime(int i)
	{
		if(i==0){
			return DateUtil.firstDayOfCurrentMonth("yyyy-MM-dd HH:mm:ss");
		}else{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
		}
	}
	
	/**
	 * 时间转化
	 */
	private long setTime(String time)
	{
		if (!StringUtil.IsEmpty(time)){
			return new DateTimeUtil(time).getLongTime();
		}
		return 0;
	}
	
	
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCitynext() {
		return citynext;
	}

	public void setCitynext(String citynext) {
		this.citynext = citynext;
	}
	
	public String getUpdateStartTime() {
		return updateStartTime;
	}

	public void setUpdateStartTime(String updateStartTime) {
		this.updateStartTime = updateStartTime;
	}

	public String getUpdateEndTime() {
		return updateEndTime;
	}

	public void setUpdateEndTime(String updateEndTime) {
		this.updateEndTime = updateEndTime;
	}

	public String getSetLastStartTime() {
		return setLastStartTime;
	}

	public void setSetLastStartTime(String setLastStartTime) {
		this.setLastStartTime = setLastStartTime;
	}

	public String getSetLastEndTime() {
		return setLastEndTime;
	}

	public void setSetLastEndTime(String setLastEndTime) {
		this.setLastEndTime = setLastEndTime;
	}

	public String getAuto_sleep_mode() {
		return auto_sleep_mode;
	}

	public void setAuto_sleep_mode(String auto_sleep_mode) {
		this.auto_sleep_mode = auto_sleep_mode;
	}

	public String getAuto_sleep_time() {
		return auto_sleep_time;
	}

	public void setAuto_sleep_time(String auto_sleep_time) {
		this.auto_sleep_time = auto_sleep_time;
	}

	public String getIp_protocol_version_lan() {
		return ip_protocol_version_lan;
	}

	public void setIp_protocol_version_lan(String ip_protocol_version_lan) {
		this.ip_protocol_version_lan = ip_protocol_version_lan;
	}

	public String getIp_protocol_version_wifi() {
		return ip_protocol_version_wifi;
	}

	public void setIp_protocol_version_wifi(String ip_protocol_version_wifi) {
		this.ip_protocol_version_wifi = ip_protocol_version_wifi;
	}

	public String getServAccount() {
		return servAccount;
	}

	public void setServAccount(String servAccount) {
		this.servAccount = servAccount;
	}

	public List<Map<String, String>> getData() {
		return data;
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public StbSetConfParamBIO getBio() {
		return bio;
	}

	public void setBio(StbSetConfParamBIO bio) {
		this.bio = bio;
	}

	public String getNewParams() {
		return newParams;
	}

	public void setNewParams(String newParams) {
		this.newParams = newParams;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
}
