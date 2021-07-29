
package com.linkage.module.gtms.config.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.StackRefreshCountBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zhaixx (Ailk No.)
 * @version 1.0
 * @category com.linkage.module.gtms.config.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class StackRefreshCountACT extends splitPageAction implements SessionAware {

	private static final long serialVersionUID = -3464620678936120546L;
	private Map<String, Object> session;
	private static Logger logger = LoggerFactory.getLogger(StackRefreshCountACT.class);
	private StackRefreshCountBIO bio;
	// 类型查询区分单双栈
	private String type = "";
	// 属地
	private String cityId = "00";
	// 查询结果数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 状态 1成功 2未做 3失败 空串全部
	private String status;
	// 导出文件名
	private String fileName;

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

	  public String queryStackRefreshInfo()
	  {
	    logger.debug("queryStackRefreshInfo()");
	    UserRes curUser = (UserRes)this.session.get("curUser");
	    this.cityId = curUser.getCityId();
	    this.data = this.bio.queryStackRefreshInfo(this.type, this.cityId);
	    logger.debug("queryStackRefreshInfo(data):{}", this.data);
	    return "shouRefreshInfoList";
	  }

	  public String getInfoExcel() {
	    this.data = this.bio.queryStackRefreshInfo(this.type, this.cityId);
	    this.fileName = "双栈刷新统计信息";
	    this.title = new String[] { "属地", "总配置数", "成功", "未触发", "失败" };
	    this.column = new String[] { "city_name", "allup", "successnum", "noupnum", "failnum" };
	    return "excel";
	  }

	  public String getDev()
	  {
	    logger.debug("getDev()");
	    this.data = this.bio.getDev(this.type, this.status, this.cityId, this.curPage_splitPage, this.num_splitPage);
	    this.maxPage_splitPage = this.bio.getDevCount(this.type, this.status, this.cityId, this.curPage_splitPage, this.num_splitPage);
	    return "devList";
	  }

	  public String getDevExcel() {
	    logger.debug("getDevExcel()");
	    this.fileName = "双栈刷新统计详细信息";
	    this.data = this.bio.getDevExcel(this.type, this.cityId, this.status);
	    if (Global.ZJLT.equals(Global.instAreaShortName)){
	    	this.title = new String[] { "属 地", "厂商", "型号", "版本", "设备序列号", "LOID", "宽带账号","IP或域", "失败原因" };
		    this.column = new String[] { "city_name", "vendor_add", "device_model", "softwareversion", "device","loid","username","loopback_ip", 
		      "fault_desc" };
	    }
	    else{
	    	this.title = new String[] { "属 地", "厂商", "型号", "版本", "设备序列号","IP或域", "失败原因" };
		    this.column = new String[] { "city_name", "vendor_add", "device_model", "softwareversion", "device", "loopback_ip", 
		      "fault_desc" };
	    }
	    
	    return "excel";
	  }
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
		return "init";
	}
	/**
	 * 统计结果查询
	 * 
	 * @return
	 */
	public String queryStackRefreshInfoByTime() {
		logger.debug("queryStackRefreshInfo()");
		this.setTime();
		data = bio.queryStackRefreshInfo(starttime1, endtime1, type, cityId);
		logger.debug("queryStackRefreshInfo(data):{}",data);
		return "shouRefreshInfoListByTime";
	}

	public String getInfoExcelByTime() {
		this.setTime();
		data = bio.queryStackRefreshInfo(starttime1, endtime1, type, cityId);
		fileName = "双栈刷新统计信息";
		title = new String[] { "属地", "总配置数", "成功", "未触发", "失败" };
		column = new String[] { "city_name", "allup", "successnum", "noupnum", "failnum" };
		return "excel";
	}

	/**
	 * 结果详情页带时间
	 * 
	 * @return
	 */
	public String getDevByTime() {
		logger.debug("getDev()");
		this.setTime();
		data = bio.getDev(type, status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
		maxPage_splitPage = bio.getDevCount(type, status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
		logger.debug("getDev(data):{}",data);
		return "devListByTime";
	}

	public String getDevExcelByTime() {
		logger.debug("getDevExcel()");
		this.setTime();
		fileName = "双栈刷新统计详细信息";
		data = bio.getDevExcel(type, cityId, status, starttime1, endtime1);
		title = new String[] { "属 地", "厂商", "型号", "版本", "LOID", "宽带账号", "设备序列号", "IP或域", "失败原因" };
		column = new String[] { "city_name", "vendor_add", "device_model", "softwareversion", "loid", "serv_account","device", "loopback_ip",
				"fault_desc" };
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

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public StackRefreshCountBIO getBio() {
		return bio;
	}

	public void setBio(StackRefreshCountBIO bio) {
		this.bio = bio;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	
	public List<Map<String, String>> getCityList() {
		return cityList;
	}
	
	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}
	
}
