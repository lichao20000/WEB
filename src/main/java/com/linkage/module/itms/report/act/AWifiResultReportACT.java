
package com.linkage.module.itms.report.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.AWifiResultReportBIO;
import com.linkage.module.itms.report.bio.SoftUpResultReportBIO;

/**
 * awifi业务开通情况统计
 * 
 * @author 周立
 */
@SuppressWarnings("unchecked")
public class AWifiResultReportACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(AWifiResultReportACT.class);
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
	private AWifiResultReportBIO bio;
	private List<Map> devList = null;
	private String status;
	private String resultId;
	private String isMgr;
	private String isSoftUp = "0";//是否查询软件升级策略（gw_serv_strategy_soft）。0：否，1：是
	private String vendorId;
	private String deviceModelId;
	private String strategy;
	private String awifi_type="1";

	public String getIsSoftUp()
	{
		return isSoftUp;
	}

	
	public void setIsSoftUp(String isSoftUp)
	{
		this.isSoftUp = isSoftUp;
	}

	/**区分ITMS和BBMS*/
	private String gw_type;
	private String ajax;
	private String gwShare_vendorId;
	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date May 19, 2010
	 * @param
	 * @return String
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
		
		HashMap<String, String> vendorMap = VendorModelVersionDAO.getVendorMap();
		HashMap<String, String> deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		
		return "init";
	}

	/**
	 * 软件升级结果统计
	 * 
	 * @author wangsenbo
	 * @date May 19, 2010
	 * @param
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		this.setTime();
		String tableName = LipossGlobals.getLipossProperty("strategy_tabname."+strategy);
		data = bio.countAWifiResult(tableName, starttime1, endtime1, cityId ,vendorId, deviceModelId, gw_type,awifi_type,isSoftUp);
		
		if(LipossGlobals.inArea(Global.JSDX)){
			return "jsList";
		}else{
			return "list";
		}
		
	}

	/**
	 * @author wangsenbo
	 * @date May 19, 2010
	 * @param
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		if("1".equals(awifi_type)){
			fileName = "awifi业务开通情况统计";
		}else{
			fileName = "校园网业务开通情况统计";
		}
		
		String tableName = LipossGlobals.getLipossProperty("strategy_tabname."+strategy);

		data = bio.countAWifiResult(tableName, starttime1, endtime1, cityId,vendorId, deviceModelId, gw_type,awifi_type, isSoftUp);
		if(LipossGlobals.inArea(Global.JSDX)){
			title = new String[] { "属地", "总配置数", "成功", "未触发", "失败", "成功率" };
			column = new String[] { "city_name", "allup", "successnum", "noupnum", "failnum", "percent" };
		}else{
			title = new String[] { "属地", "总配置数", "成功", "未做", "等待重做", "彻底失败", "成功率" };
			column = new String[] { "city_name", "allup", "successnum", "noupnum", "nextnum",
					"failnum", "percent" };
		}
		
		return "excel";
	}

	/**
	 * 获取设备列表
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getDev()
	{
		logger.debug("getDev()");
		String tableName = LipossGlobals.getLipossProperty("strategy_tabname."+strategy);
		devList = bio.getDevList(tableName, gw_type,awifi_type,starttime1, endtime1, cityId, status, resultId, isMgr,
				curPage_splitPage, num_splitPage,isSoftUp,vendorId, deviceModelId);
		maxPage_splitPage = bio.getDevCount(tableName,gw_type,awifi_type,starttime1, endtime1, cityId, status,
				resultId, isMgr, curPage_splitPage, num_splitPage,isSoftUp,vendorId, deviceModelId);
		if(LipossGlobals.inArea(Global.JSDX))
		{
			return "jsdevlist";
		}
		else
		{
			return "devlist";
		}
		
	}

	/**
	 * 设备列表导出
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getDevExcel()
	{
		logger.debug("getDevExcel()");
		if("1".equals(awifi_type)){
			fileName = "awifi业务开通详细信息";
		}else{
			fileName = "校园网业务开通详细信息";
		}
		
		String tableName = LipossGlobals.getLipossProperty("strategy_tabname."+strategy);
		data = bio.getDevExcel(tableName,gw_type,awifi_type,starttime1, endtime1, cityId, status, resultId, isMgr,isSoftUp,vendorId, deviceModelId);
		if (LipossGlobals.inArea(Global.JSDX))
		{
			title = new String[] { "属 地", "厂商", "型号", "版本", "设备序列号", "IP或域" };
			column = new String[] { "city_name", "vendor_add", "device_model",
					"softwareversion", "device", "loopback_ip" };
		}
		else
		{
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
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}
	
	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendor() {
		logger.debug("GwDeviceQueryACT=>getVendor()");
		this.ajax = bio.getVendor();
		return "ajax";
	}
	
	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel() {
		logger.debug("GwDeviceQueryACT=>getDeviceModel()");
		this.ajax = bio.getDeviceModel(gwShare_vendorId);
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
	 * @return the starttime
	 */
	public String getStarttime()
	{
		return starttime;
	}

	/**
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	/**
	 * @return the starttime1
	 */
	public String getStarttime1()
	{
		return starttime1;
	}

	/**
	 * @param starttime1
	 *            the starttime1 to set
	 */
	public void setStarttime1(String starttime1)
	{
		this.starttime1 = starttime1;
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
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime()
	{
		return endtime;
	}

	/**
	 * @param endtime
	 *            the endtime to set
	 */
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	/**
	 * @return the endtime1
	 */
	public String getEndtime1()
	{
		return endtime1;
	}

	/**
	 * @param endtime1
	 *            the endtime1 to set
	 */
	public void setEndtime1(String endtime1)
	{
		this.endtime1 = endtime1;
	}

	public AWifiResultReportBIO getBio()
	{
		return bio;
	}

	public void setBio(AWifiResultReportBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the devList
	 */
	public List<Map> getDevList()
	{
		return devList;
	}

	/**
	 * @param devList
	 *            the devList to set
	 */
	public void setDevList(List<Map> devList)
	{
		this.devList = devList;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the resultId
	 */
	public String getResultId()
	{
		return resultId;
	}

	/**
	 * @param resultId
	 *            the resultId to set
	 */
	public void setResultId(String resultId)
	{
		this.resultId = resultId;
	}

	/**
	 * @return the isMgr
	 */
	public String getIsMgr()
	{
		return isMgr;
	}

	/**
	 * @param isMgr
	 *            the isMgr to set
	 */
	public void setIsMgr(String isMgr)
	{
		this.isMgr = isMgr;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

	public String getVendorId()
	{
		return vendorId;
	}
	
	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}
	
	public String getDeviceModelId()
	{
		return deviceModelId;
	}
	
	public void setDeviceModelId(String deviceModelId)
	{
		this.deviceModelId = deviceModelId;
	}
	
	public String getStrategy()
	{
		return strategy;
	}
	
	public void setStrategy(String strategy)
	{
		this.strategy = strategy;
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
	public String getGwShare_vendorId()
	{
		return gwShare_vendorId;
	}
	
	public void setGwShare_vendorId(String gwShare_vendorId)
	{
		this.gwShare_vendorId = gwShare_vendorId;
	}


	public String getAwifi_type() {
		return awifi_type;
	}


	public void setAwifi_type(String awifi_type) {
		this.awifi_type = awifi_type;
	}

	
	
}
