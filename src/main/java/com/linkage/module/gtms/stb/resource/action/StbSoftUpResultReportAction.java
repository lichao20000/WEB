package com.linkage.module.gtms.stb.resource.action;

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
import com.linkage.module.gtms.stb.resource.serv.GwDeviceQueryBIO;
import com.linkage.module.gtms.stb.resource.serv.StbSoftUpResultReportBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 机顶盒软件升级结果统计
 * 
 */
@SuppressWarnings("rawtypes")
public class StbSoftUpResultReportAction extends splitPageAction implements SessionAware 
{
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(StbSoftUpResultReportAction.class);
	// session
	private Map session;
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
	/** 一级属地ID */
	private String cityId = "-1";
	/** 二级属地ID */
	private String city_id = "-1";
	/** 厂商列表 */
	private List<Map<String, String>> vendorList = null;
	/** 厂商ID */
	private String vendorId = null;
	private String modelId = null;
	
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private StbSoftUpResultReportBIO bio;
	private List<Map> devList = null;
	private String status;
	private String resultId;
	private String isSoftUp = "0";// 是否查询软件升级策略（gw_serv_strategy_soft）。0：否，1：是
	private String InstArea=Global.instAreaShortName;
	/**用于页面展示一级属地*/
	private String city_ids;
	private String ajax;

	/**
	 * 初始化页面
	 * 
	 */
	public String init() 
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		if(Global.HBLT.equals(InstArea)){
			city_ids=new GwDeviceQueryBIO().getCity(cityId);
		}
		
		DateTimeUtil dt = new DateTimeUtil();
		starttime = dt.getDate();
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil(start_time * 1000);
		starttime = dt.getLongDate();
		dt = new DateTimeUtil((start_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();

		return "init";
	}
	
	/**
	 * 获取下级属地，不包含自己
	 */
	public String getCityNextChild()
	{
		ajax=bio.getCity(cityId);
		return "ajax";
	}

	/**
	 * 软件升级结果统计
	 * 
	 */
	public String execute() {
		logger.debug("execute [{}],[{}],[{}],[{}],[{}],[{}]",
				new Object[]{starttime1, endtime1, cityId, isSoftUp,vendorId,modelId});
		this.setTime();
		if(!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)){
			cityId=city_id;
		}
		data = bio.countSoftUpResult(starttime1, endtime1, cityId, isSoftUp,vendorId,modelId);
		
		return "list";
	}

	/**
	 * @author wangsenbo
	 * @date May 19, 2010
	 * @param
	 * @return String
	 */
	public String getExcel() {
		logger.debug("getExcel [{}],[{}],[{}],[{}],[{}],[{}]",
				new Object[]{starttime1, endtime1, cityId, isSoftUp,vendorId,modelId});
		logger.debug("getExcel()");
		fileName = "软件升级统计";
		// this.setTime();
		data = bio.countSoftUpResult(starttime1, endtime1, cityId, isSoftUp,vendorId,modelId);

		title = new String[] { "属地", "总配置数", "成功", "未做", "等待重做", "彻底失败",
				"成功率" };
		column = new String[] { "city_name", "allup", "successnum",
				"noupnum", "nextnum", "failnum", "percent" };

		return "excel";
	}

	/**
	 * 获取设备列表
	 * 
	 */
	public String getDev() {
		logger.debug("getDev [{}],[{}],[{}],[{}],[{}],[{}],[{}],[{}]",
				new Object[]{resultId,status,starttime1, endtime1, cityId, isSoftUp,vendorId,modelId});
		devList = bio.getDevList(starttime1, endtime1, cityId, status,
				resultId, curPage_splitPage, num_splitPage, isSoftUp,vendorId,modelId);
		maxPage_splitPage = bio.getDevCount(starttime1, endtime1,
				cityId, status, resultId, curPage_splitPage,
				num_splitPage, isSoftUp,vendorId,modelId);
		return "devlist";
	}
	

	/**
	 * 设备列表导出
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getDevExcel() {
		logger.debug("getDevExcel [{}],[{}],[{}],[{}],[{}],[{}],[{}],[{}]",
				new Object[]{resultId,status,starttime1, endtime1, cityId, isSoftUp,vendorId,modelId});
		fileName = "软件升级统计详细信息";
		data = bio.getDevExcel(starttime1, endtime1, cityId, status,
				resultId, isSoftUp,vendorId,modelId);
	
		title = new String[] { "属 地", "厂商", "型号", "版本", "设备序列号", "IP或域",
				"失败原因" };
		column = new String[] { "city_name", "vendor_add", "device_model",
				"softwareversion", "device", "loopback_ip", "fault_desc" };
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)) {
			starttime1 = null;
		} else {
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)) {
			endtime1 = null;
		} else {
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getStarttime1() {
		return starttime1;
	}

	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getEndtime1() {
		return endtime1;
	}

	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}

	public StbSoftUpResultReportBIO getBio() {
		return bio;
	}

	public void setBio(StbSoftUpResultReportBIO bio) {
		this.bio = bio;
	}

	public List<Map> getDevList() {
		return devList;
	}

	public void setDevList(List<Map> devList) {
		this.devList = devList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public List<Map<String, String>> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<Map<String, String>> vendorList) {
		this.vendorList = vendorList;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	
	public String getIsSoftUp() {
		return isSoftUp;
	}

	public void setIsSoftUp(String isSoftUp) {
		this.isSoftUp = isSoftUp;
	}

	public String getInstArea() {
		return InstArea;
	}

	public void setInstArea(String instArea) {
		InstArea = instArea;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCity_ids() {
		return city_ids;
	}

	public void setCity_ids(String city_ids) {
		this.city_ids = city_ids;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	
	
}
