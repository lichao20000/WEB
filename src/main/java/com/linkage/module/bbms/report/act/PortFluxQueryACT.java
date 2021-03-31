
package com.linkage.module.bbms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.bio.PortFluxQueryBIO;

/**
 * 端口流量利用率
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class PortFluxQueryACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(PortFluxQueryACT.class);
	// session
	private Map session;
	/** 查询时间 */
	private String stat_time = "";
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private PortFluxQueryBIO portFluxQueryBio;
	/** 查询方式 */
	private String queryType;
	/** 设备SN */
	private String deviceSN;
	/** 设备IP */
	private String loopbackIp;
	/** 用户账号 */
	private String userName;
	/** 客户名称 */
	private String customerName;
	/** 联系电话 */
	private String linkphone;
	/** 报表类型 */
	private String reportType;
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
		UserRes curUser = (UserRes) session.get("curUser");
		data = portFluxQueryBio.portFluxQuery(reportType, stat_time, queryType, deviceSN,
				loopbackIp, userName, customerName, linkphone, curUser.getCityId());
		message = portFluxQueryBio.getMessage();
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
		UserRes curUser = (UserRes) session.get("curUser");
		fileName = "端口流量利用率";
		title = new String[] { "属地", "设备序列号", "设备IP", "端口信息", "平均利用率", "最大利用率", "客户名称",
				"用户账号" };
		column = new String[] { "city_name", "device_serialnumber", "loopback_ip", "port_info",
				"avg_count", "max_count", "customer_name", "username" };
		data = portFluxQueryBio.portFluxQuery(reportType, stat_time, queryType, deviceSN,
				loopbackIp, userName, customerName, linkphone, curUser.getCityId());
		return "excel";
	}

	/**
	 * 导出Excel
	 * 
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String getAllExcel()
	{
		logger.debug("getAllExcel()");
		UserRes curUser = (UserRes) session.get("curUser");
		fileName = "端口流量利用率";
		title = new String[] { "属地", "设备序列号", "设备IP", "端口信息", "平均利用率", "最大利用率", "客户名称",
				"用户账号" };
		column = new String[] { "city_name", "device_serialnumber", "loopback_ip", "port_info",
				"avg_count", "max_count", "customer_name", "username" };
		DateTimeUtil dt = new DateTimeUtil();
		data = portFluxQueryBio.portFluxQuery("month", dt.getNextDate("month", -1),
				"device", null, null, null, null, null, curUser.getCityId());
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
	 * @return the portFluxQueryBio
	 */
	public PortFluxQueryBIO getPortFluxQueryBio()
	{
		return portFluxQueryBio;
	}

	/**
	 * @param portFluxQueryBio
	 *            the portFluxQueryBio to set
	 */
	public void setPortFluxQueryBio(PortFluxQueryBIO portFluxQueryBio)
	{
		this.portFluxQueryBio = portFluxQueryBio;
	}

	/**
	 * @return the queryType
	 */
	public String getQueryType()
	{
		return queryType;
	}

	/**
	 * @param queryType
	 *            the queryType to set
	 */
	public void setQueryType(String queryType)
	{
		this.queryType = queryType;
	}

	/**
	 * @return the loopbackIp
	 */
	public String getLoopbackIp()
	{
		return loopbackIp;
	}

	/**
	 * @param loopbackIp
	 *            the loopbackIp to set
	 */
	public void setLoopbackIp(String loopbackIp)
	{
		this.loopbackIp = loopbackIp;
	}

	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
		try
		{
			this.userName = java.net.URLDecoder.decode(userName, "UTF-8");
		}
		catch (Exception e)
		{
			this.userName = userName;
		}
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName()
	{
		return customerName;
	}

	/**
	 * @param customerName
	 *            the customerName to set
	 */
	public void setCustomerName(String customerName)
	{
		try
		{
			this.customerName = java.net.URLDecoder.decode(customerName, "UTF-8");
		}
		catch (Exception e)
		{
			this.customerName = customerName;
		}
	}

	/**
	 * @return the linkphone
	 */
	public String getLinkphone()
	{
		return linkphone;
	}

	/**
	 * @param linkphone
	 *            the linkphone to set
	 */
	public void setLinkphone(String linkphone)
	{
		this.linkphone = linkphone;
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
	 * @return the deviceSN
	 */
	public String getDeviceSN()
	{
		return deviceSN;
	}

	/**
	 * @param deviceSN
	 *            the deviceSN to set
	 */
	public void setDeviceSN(String deviceSN)
	{
		this.deviceSN = deviceSN;
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
