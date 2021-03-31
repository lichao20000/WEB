
package com.linkage.module.bbms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.bio.BandwidthTopNBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 端口带宽占用率
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class BandwidthTopNACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BandwidthTopNACT.class);
	// session
	private Map session;
	/** 统计时间 */
	private String stat_time = "";
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
	private BandwidthTopNBIO bandwidthTopNBio;
	/** 报表类型 */
	private String reportType;
	private String countDesc;
	private String message;

	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date Mar 18, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		DateTimeUtil dt = new DateTimeUtil();
		stat_time = dt.getDate();
		return "init";
	}

	/**
	 * 统计
	 * 
	 * @author wangsenbo
	 * @date Mar 18, 2010
	 * @param
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		data = bandwidthTopNBio.getTopNReport(reportType, cityId, stat_time, countDesc);
		message = bandwidthTopNBio.getMessage();
		return "list";
	}

	/**
	 * 导出Excel
	 * 
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "端口带宽占用率TopN";
		title = new String[] { "属地", "设备序列号", "设备IP", "端口信息", "平均利用率", "客户名称", "用户账号" };
		column = new String[] { "city_name", "device_serialnumber", "loopback_ip",
				"port_info", "avg_count", "customer_name", "username" };
		data = bandwidthTopNBio.getTopNReport(reportType, cityId, stat_time, countDesc);
		return "excel";
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
	 * @return the reportType
	 */
	public String getReportType()
	{
		return reportType;
	}

	/**
	 * @param reportType
	 *            the reportType to set
	 */
	public void setReportType(String reportType)
	{
		this.reportType = reportType;
	}

	/**
	 * @return the stat_time
	 */
	public String getStat_time()
	{
		return stat_time;
	}

	/**
	 * @param stat_time
	 *            the stat_time to set
	 */
	public void setStat_time(String stat_time)
	{
		this.stat_time = stat_time;
	}

	/**
	 * @return the bandwidthTopNBio
	 */
	public BandwidthTopNBIO getBandwidthTopNBio()
	{
		return bandwidthTopNBio;
	}

	/**
	 * @param bandwidthTopNBio
	 *            the bandwidthTopNBio to set
	 */
	public void setBandwidthTopNBio(BandwidthTopNBIO bandwidthTopNBio)
	{
		this.bandwidthTopNBio = bandwidthTopNBio;
	}

	/**
	 * @return the countDesc
	 */
	public String getCountDesc()
	{
		return countDesc;
	}

	/**
	 * @param countDesc
	 *            the countDesc to set
	 */
	public void setCountDesc(String countDesc)
	{
		this.countDesc = countDesc;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
}
