package com.linkage.module.gtms.stb.resource.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.StbGwDeviceQueryBIO;
import com.linkage.module.gwms.Global;
import com.linkage.litms.uss.DateUtil;

/**
 * 机顶盒设备信息管理
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-1-30
 */
@SuppressWarnings("rawtypes")
public class StbGwDeviceQueryACT extends splitPageAction implements SessionAware,ServletRequestAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(StbGwDeviceQueryACT.class);
	
	/** 属地ID */
	private String cityId = "";
	/** 属地ID */
	private String citynext = "";
	/** 厂商ID */
	private String vendorId = "";
	/** 型号 */
	private String deviceModelId = "";
	/** 版本id */
	private String deviceTypeId = "";
	/** 设备id */
	private String deviceId = "";
	/** 设备序列号 */
	private String deviceSn = "";
	/** 设备mac地址 */
	private String deviceMac = "";
	/** oui */
	private String oui = "";
	private String ajax = "";
	/** 下发结果查询用service */
	private StbGwDeviceQueryBIO bio;
	/** 设备列表 */
	private List<Map<String, String>> deviceList;
	/** 查询总数 */
	private int queryCount;
	/** 硬件版本 */
	private String hardwareversion = "";
	/** 软件版本 */
	private String softwareversion = "";
	/** 注册时间 */
	private String completeTime = "";
	/** 业务账号 */
	private String servAccount = "";
	/** 开始时间 */
	private String startTime = "";
	/** 结束时间 */
	private String endTime = "";
	/** 最后一次上报开始时间 */
	private String lastStartTime = "";
	/** 最后一次上报结束时间 */
	private String lastEndTime = "";
	/** 权限限制，默认展示“详细信息”、“编辑”、“删除”，传入1，只有“详细信息”，传入2，展示“详细信息”和“编辑” */
	private String showType="";
	/** 设备类别 0：行货，1：串货 */
	private int category;
	// 结果集
	private Map<String, String> data;
	private Map session;
	private String instArea=Global.instAreaShortName;

	private HttpServletRequest request;
	
	public String init()
	{
		startTime = setTime(0);
		endTime = setTime(1);
		
		return "init";
	}
	
	/**
	 * 机顶盒列表查询
	 */
	public String getStbDeviceList()
	{
		startTime = setTime(startTime);
		endTime = setTime(endTime);
		if (null == this.cityId || "-1".equals(this.cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			this.cityId = curUser.getCityId();
		}
		
		if(Global.HNLT.equals(instArea)){
			lastStartTime = setTime(lastStartTime);
			lastEndTime = setTime(lastEndTime);
			deviceList = bio.getStbDeviceList_hnlt(curPage_splitPage, num_splitPage, vendorId,
					deviceModelId, deviceSn, deviceMac, servAccount, cityId, citynext, 
					startTime, endTime,lastStartTime,lastEndTime);
			logger.debug(deviceList.toString());
			maxPage_splitPage = bio.countStbDeviceList_hnlt(curPage_splitPage, num_splitPage, vendorId,
					deviceModelId, deviceSn, deviceMac ,servAccount, cityId, citynext,
					startTime, endTime,lastStartTime,lastEndTime);
			queryCount = bio.getQueryCount();
		}else{
			deviceList = bio.getStbDeviceList(curPage_splitPage, num_splitPage, vendorId,
					deviceModelId, deviceSn, deviceMac, servAccount, cityId, citynext, startTime, endTime);
			logger.debug(deviceList.toString());
			maxPage_splitPage = bio.countStbDeviceList(curPage_splitPage, num_splitPage, vendorId,
					deviceModelId, deviceSn, deviceMac ,servAccount, cityId, citynext, startTime, endTime);
			queryCount = bio.getQueryCount();
		}
		
		return "queryList";
	}
	
	/**
	 * 新增设备
	 */
	public String add() 
	{
		completeTime = setTime(completeTime);
		if(Global.HNLT.equals(instArea)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			ajax = bio.addGwDevice_hn(user_id,user_ip,deviceSn,oui,deviceMac,vendorId,deviceModelId,deviceTypeId,cityId,completeTime,citynext);
		}else{
			ajax = bio.addGwDevice(deviceSn, oui, deviceMac, vendorId, deviceModelId, deviceTypeId, cityId, completeTime, citynext);
		}
		
		return "ajax";
	}
	
	/**
	 * 删除当前设备
	 */
	public String deleteDevice()
	{
		if(Global.HNLT.equals(instArea)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			ajax = bio.deleteDevice_hn(user_id,user_ip,deviceId, deviceMac);
		}else{
			ajax = bio.deleteDevice(deviceId, deviceMac);
		}
		
		return "ajax";
	}
	
	/**
	 * 修改当前设备
	 */
	public String modifyDevice() 
	{
		if(Global.HNLT.equals(instArea)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			ajax = bio.modifyDevice_hn(user_id,user_ip,deviceId, deviceSn, oui, deviceMac, vendorId, deviceModelId, deviceTypeId, cityId, citynext,category);
		}else{
			ajax = bio.modifyDevice(deviceId, deviceSn, oui, deviceMac, vendorId, deviceModelId, deviceTypeId, cityId, citynext);
		}
		
		return "ajax";
	}
	
	/**
	 * 根据Id获取当前设备的信息
	 */
	public String queryDeviceById() 
	{
		data = bio.queryDeviceById(deviceId);
		logger.warn("queryDeviceById device_id:[{}],data:[{}]",deviceId,data.toString());
		return "deviceDetail";
	}
	
	/**
	 * 根据Id获取当前设备的信息
	 */
	public String queryDeviceInfoById() 
	{
		data = bio.queryDeviceById(deviceId);
		logger.warn("queryDeviceInfoById device_id:[{}],data:[{}]",deviceId,data.toString());
		return "deviceDetailInfo";
	}
	
	/**
	 * 获取当前时间或本月的初始时间
	 */
	private String setTime(int i)
	{
		if(i==0){
			return DateUtil.firstDayOfCurrentMonth("yyyy-MM-dd HH:mm:ss");
		}else{
			Calendar now = Calendar.getInstance();
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
		}
	}
	
	/**
	 * 时间转化
	 */
	private String setTime(String time)
	{
		logger.debug("setTime()" + time);
		if (!StringUtil.IsEmpty(time)){
			time = StringUtil.getStringValue(new DateTimeUtil(time).getLongTime());
			return time;
		}
		return "";
	}
	
	public String getVendorId(){
		return vendorId;
	}

	public void setVendorId(String vendorId){
		this.vendorId = vendorId;
	}
	
	public String getAjax(){
		return ajax;
	}

	public void setAjax(String ajax){
		this.ajax = ajax;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public String getHardwareversion() {
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion) {
		this.hardwareversion = hardwareversion;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
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

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public List<Map<String, String>> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Map<String, String>> deviceList) {
		this.deviceList = deviceList;
	}

	public String getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion;
	}

	public String getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}

	public StbGwDeviceQueryBIO getBio() {
		return bio;
	}

	public void setBio(StbGwDeviceQueryBIO bio) {
		this.bio = bio;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCitynext() {
		return citynext;
	}

	public void setCitynext(String citynext) {
		this.citynext = citynext;
	}

	public String getServAccount() {
		return servAccount;
	}

	public void setServAccount(String servAccount) {
		this.servAccount = servAccount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(String lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

	public String getLastEndTime() {
		return lastEndTime;
	}

	public void setLastEndTime(String lastEndTime) {
		this.lastEndTime = lastEndTime;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public int getCategory(){
		return category;
	}

	public void setCategory(int category){
		this.category = category;
	}
}
