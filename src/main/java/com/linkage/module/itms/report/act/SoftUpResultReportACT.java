package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.SoftUpResultReportBIO;

/**
 * 软件升级结果统计
 * @author 王森博
 */
@SuppressWarnings("rawtypes")
public class SoftUpResultReportACT extends splitPageAction implements SessionAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SoftUpResultReportACT.class);

	private Map session;
	/**厂商*/
	private String vendorId=null;
	/**型号*/
	private String deviceModelId=null;
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地ID */
	private String cityId = "-1";
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private SoftUpResultReportBIO bio;
	private List<Map> devList = null;
	private String status;
	private String resultId;
	private String isMgr;
	private String isSoftUp = "0";//是否查询软件升级策略（gw_serv_strategy_soft）。0：否，1：是
	/**区分ITMS和BBMS*/
	private String gw_type;
	
	
	/**
	 * 初始化页面
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		DateTimeUtil dt = new DateTimeUtil();
		starttime = dt.getDate();
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil(start_time * 1000);
		starttime = dt.getLongDate();
		dt = new DateTimeUtil((start_time+24*3600-1) * 1000);
		endtime = dt.getLongDate();
		
		return "init";
	}

	/**
	 * 软件升级结果统计
	 */
	public String execute()
	{
		logger.debug("execute()");
		this.setTime();
		data = bio.countSoftUpResult(starttime1,endtime1,cityId,gw_type,isSoftUp,vendorId,deviceModelId);
		
		if(LipossGlobals.inArea(Global.JSDX)){
			return "jsList";
		}
		return "list";
	}

	/**
	 * 导出excel
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "软件升级统计";
		data = bio.countSoftUpResult(starttime1,endtime1,cityId,gw_type,isSoftUp,vendorId,deviceModelId);
		if(LipossGlobals.inArea(Global.JSDX)){
			title = new String[] { "属地", "总配置数", "成功", "未触发", "失败", "成功率" };
			column = new String[] { "city_name", "allup", "successnum", "noupnum", "failnum", "percent" };
		}else if(LipossGlobals.inArea(Global.SDDX)){
				title = new String[] { "属地", "总配置数", "成功","未触发", "等待重做", "彻底失败", "成功率" };
				column = new String[] { "city_name", "allup", "successnum","noupnum", "nextnum",
											"failnum", "percent" };
		}else{
			title = new String[] { "属地", "总配置数", "成功","执行中","未做", "等待重做", "彻底失败", "成功率" };
			column = new String[] { "city_name", "allup", "successnum","runningnum","noupnum", "nextnum",
					"failnum", "percent" };
		}
		
		return "excel";
	}

	/**
	 * 获取设备列表
	 */
	public String getDev()
	{
		logger.debug("getDev()");
		devList = bio.getDevList(gw_type,starttime1,endtime1,cityId,status,resultId,isMgr,
								curPage_splitPage, num_splitPage,isSoftUp,vendorId,deviceModelId);
		maxPage_splitPage = bio.getDevCount(gw_type,starttime1,endtime1,cityId,status,resultId,
									isMgr,curPage_splitPage,num_splitPage,isSoftUp,vendorId,deviceModelId);
		
		if(LipossGlobals.inArea(Global.JSDX)){
			return "jsdevlist";
		}
		return "devlist";
	}

	/**
	 * 设备列表导出
	 */
	public String getDevExcel()
	{
		logger.debug("getDevExcel()");
		fileName = "软件升级统计详细信息";
		data = bio.getDevExcel(gw_type,starttime1,endtime1,cityId,status,
								resultId,isMgr,isSoftUp,vendorId,deviceModelId);
		if (LipossGlobals.inArea(Global.JSDX))
		{
			title = new String[] { "属 地", "厂商", "型号", "版本", "设备序列号", "IP或域" };
			column = new String[] { "city_name", "vendor_add", "device_model",
					"softwareversion", "device", "loopback_ip" };
		}
		else if(LipossGlobals.inArea(Global.JXDX)){
			title = new String[] { "属 地", "厂商", "型号", "版本", "硬件版本","接入类型","设备序列号", "用户账号","终端支持速率",
					"注册系统时间","最近更新时间","IP或域", "失败原因" };
			column = new String[] { "city_name", "vendor_add", "device_model",
					"softwareversion", "hardwareversion","accessTypeStr","device","loid","terminalRate","completeTime",
					"lastTime","loopback_ip", "fault_desc" };
		}else {
			title = new String[] { "属 地", "厂商", "型号", "版本", "设备序列号", "IP或域", "失败原因" };
			column = new String[] { "city_name", "vendor_add", "device_model",
					"softwareversion", "device", "loopback_ip", "fault_desc" };
		}
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + starttime);
		if(StringUtil.IsEmpty(starttime)){
			starttime1 = null;
		}else{
			DateTimeUtil dt = new DateTimeUtil(starttime);
			starttime1 = StringUtil.getStringValue(dt.getLongTime());
		}
		
		if(StringUtil.IsEmpty(endtime)){
			endtime1 = null;
		}else{
			DateTimeUtil dt = new DateTimeUtil(endtime);
			endtime1 = StringUtil.getStringValue(dt.getLongTime());
		}
	}



	
	public Map getSession(){
		return session;
	}

	public void setSession(Map session){
		this.session = session;
	}

	public String getStarttime(){
		return starttime;
	}

	public void setStarttime(String starttime){
		this.starttime = starttime;
	}

	public String getStarttime1(){
		return starttime1;
	}

	public void setStarttime1(String starttime1){
		this.starttime1 = starttime1;
	}

	public List<Map<String, String>> getCityList(){
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList){
		this.cityList = cityList;
	}

	public String getCityId(){
		return cityId;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public List<Map> getData(){
		return data;
	}

	public void setData(List<Map> data){
		this.data = data;
	}

	public String[] getTitle(){
		return title;
	}

	public void setTitle(String[] title){
		this.title = title;
	}

	public String[] getColumn(){
		return column;
	}

	public void setColumn(String[] column){
		this.column = column;
	}

	public String getFileName(){
		return fileName;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getEndtime(){
		return endtime;
	}

	public void setEndtime(String endtime){
		this.endtime = endtime;
	}

	public String getEndtime1(){
		return endtime1;
	}

	public void setEndtime1(String endtime1){
		this.endtime1 = endtime1;
	}

	public SoftUpResultReportBIO getBio(){
		return bio;
	}

	public void setBio(SoftUpResultReportBIO bio){
		this.bio = bio;
	}

	public List<Map> getDevList(){
		return devList;
	}

	public void setDevList(List<Map> devList){
		this.devList = devList;
	}

	public String getStatus(){
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getResultId(){
		return resultId;
	}

	public void setResultId(String resultId){
		this.resultId = resultId;
	}

	public String getIsMgr(){
		return isMgr;
	}

	public void setIsMgr(String isMgr){
		this.isMgr = isMgr;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}
	
	public String getIsSoftUp(){
		return isSoftUp;
	}
	
	public void setIsSoftUp(String isSoftUp){
		this.isSoftUp = isSoftUp;
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
	
	
}
