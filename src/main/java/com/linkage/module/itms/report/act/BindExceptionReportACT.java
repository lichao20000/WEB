
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.BindExceptionReportBIO;

/**
 * 异常绑定统计
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class BindExceptionReportACT extends splitPageAction implements SessionAware
{
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(BindExceptionReportACT.class);
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
	private BindExceptionReportBIO bindExceptionReportBio;
	private String isAll;
	private List<Map> hgwList = null;
	private String gw_type;

	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date Mar 3, 2010
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
	 * 统计
	 * 
	 * @author wangsenbo
	 * @date Mar 1, 2010
	 * @param
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		this.setTime();
		data = bindExceptionReportBio.countBindException(starttime1, endtime1, cityId,gw_type);
		return "list";
	}

	/**
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "BINDEXCEPTION";
		title = new String[] { "属地", "IPOSS没有同步到相应用户记录", "IPOSS同步到用户对应的MAC地址在ITMS中不存在",
				"终端未上报MAC地址", "终端上报的MAC地址，在IPOSS同步数据中没有相应记录" };
		column = new String[] { "city_name", "nobindUserNoIposs", "noBindUserNoMac",
				"noBindDevcieNoMac", "noBindDeviceNoIposs" };
		data = bindExceptionReportBio.countBindException(starttime1, endtime1, cityId,gw_type);
		return "excel";
	}

	/**
	 * 获取用户列表
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getHgw()
	{
		logger.debug("getHgw()");
		return "hgwlist";
	}

	/**
	 * 用户信息导出
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getHgwExcel()
	{
		logger.debug("getHgw()");
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
	 * @return the hgwList
	 */
	public List<Map> getHgwList()
	{
		return hgwList;
	}

	/**
	 * @param hgwList
	 *            the hgwList to set
	 */
	public void setHgwList(List<Map> hgwList)
	{
		this.hgwList = hgwList;
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

	/**
	 * @return the bindExceptionReportBio
	 */
	public BindExceptionReportBIO getBindExceptionReportBio()
	{
		return bindExceptionReportBio;
	}

	/**
	 * @param bindExceptionReportBio
	 *            the bindExceptionReportBio to set
	 */
	public void setBindExceptionReportBio(BindExceptionReportBIO bindExceptionReportBio)
	{
		this.bindExceptionReportBio = bindExceptionReportBio;
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

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}
	
}
