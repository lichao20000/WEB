
package com.linkage.module.bbms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.bio.ServiceReportBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 设备业务统计
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class ServiceReportACT extends splitPageAction implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ServiceReportACT.class);
	// session
	private Map session;
	/** 统计时间 */
	private String stat_day = "";
	/** 统计时间 */
	private String stat_day1 = "";
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
	private ServiceReportBIO serviceReportBio;
	// 是否小计 1是小计 0不是小计
	private String isAll;
	private List<Map> titleList = null;
	/** 业务列表 */
	private List<Map<String, String>> serviceTypeList = null;
	/** 业务ID */
	private String serviceId = "-1";
	/** 报表类型 */
	private String reportType;

	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		serviceTypeList = serviceReportBio.getServiceTypeList();
		return "init";
	}

	/**
	 * 统计
	 * 
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		this.setTime();
		titleList = serviceReportBio.getServiceList(cityId, stat_day1, serviceId,
				reportType);
		data = serviceReportBio.countService(cityId, stat_day1, serviceId, reportType);
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
		this.setTime();
		fileName = "业务使用";
		titleList = serviceReportBio.getServiceList(cityId, stat_day1, serviceId,
				reportType);
		title = serviceReportBio.getTitle(titleList);
		column = serviceReportBio.getColumn(titleList);
		data = serviceReportBio.countService(cityId, stat_day1, serviceId, reportType);
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + stat_day);
//		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (stat_day == null || "".equals(stat_day))
		{
			stat_day1 = null;
		}
		stat_day1 = stat_day;
//		else
//		{
//			stat_day1 = stat_day.replace("-", "");
//		}
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
	 * @return the isAll
	 */
	public String getIsAll()
	{
		return isAll;
	}

	/**
	 * @param isAll
	 *            the isAll to set
	 */
	public void setIsAll(String isAll)
	{
		this.isAll = isAll;
	}

	/**
	 * @return the stat_day
	 */
	public String getStat_day()
	{
		return stat_day;
	}

	/**
	 * @param stat_day
	 *            the stat_day to set
	 */
	public void setStat_day(String stat_day)
	{
		this.stat_day = stat_day;
	}

	/**
	 * @return the stat_day1
	 */
	public String getStat_day1()
	{
		return stat_day1;
	}

	/**
	 * @param stat_day1
	 *            the stat_day1 to set
	 */
	public void setStat_day1(String stat_day1)
	{
		this.stat_day1 = stat_day1;
	}

	/**
	 * @return the serviceReportBio
	 */
	public ServiceReportBIO getServiceReportBio()
	{
		return serviceReportBio;
	}

	/**
	 * @param serviceReportBio
	 *            the serviceReportBio to set
	 */
	public void setServiceReportBio(ServiceReportBIO serviceReportBio)
	{
		this.serviceReportBio = serviceReportBio;
	}

	/**
	 * @return the titleList
	 */
	public List<Map> getTitleList()
	{
		return titleList;
	}

	/**
	 * @param titleList
	 *            the titleList to set
	 */
	public void setTitleList(List<Map> titleList)
	{
		this.titleList = titleList;
	}

	/**
	 * @return the serviceTypeList
	 */
	public List<Map<String, String>> getServiceTypeList()
	{
		return serviceTypeList;
	}

	/**
	 * @param serviceTypeList
	 *            the serviceTypeList to set
	 */
	public void setServiceTypeList(List<Map<String, String>> serviceTypeList)
	{
		this.serviceTypeList = serviceTypeList;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId()
	{
		return serviceId;
	}

	/**
	 * @param serviceId
	 *            the serviceId to set
	 */
	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
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
}
