
package com.linkage.module.gwms.report.act;

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
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.bio.NoActiveDevBIO;

/**
 * 不活跃设备统计查询
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class NoActiveDevACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(NoActiveDevACT.class);
	// session
	private Map session;
	/** 活跃时间 */
	private String activetime = "";
	/** 活跃时间 */
	private String activetime1 = "";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地ID */
	private String cityId = "-1";
	/**区分ITMS/BBMS功能*/
	private String gw_type;
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	// 是否江苏ITMS “1”是，“0”不是 
	private String isJSITMS = "0";
	private List<Map> devList = null;
	private NoActiveDevBIO bio;

	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		DateTimeUtil dt = new DateTimeUtil();
		activetime = dt.getNextDate("month", -1);
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		return "init";
	}

	/**
	 * 统计不活跃设备
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return String
	 */
	public String count()
	{
		logger.debug("count()");
		this.setTime();
		logger.warn("统计不活跃设备gw_type="+gw_type);
		data = bio.count(activetime1, cityId ,gw_type);
		return "list";
	}

	/**
	 * 统计不活跃设备导出excel
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "不活跃设备";
		this.setTime();
		title = new String[] { "属地", "不活跃设备数" };
		column = new String[] { "city_name", "total" };
		data = bio.count(activetime1, cityId,gw_type);
		return "excel";
	}

	/**
	 * 设备列表
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return String
	 */
	public String getDeviceList()
	{
		logger.debug("getDeviceList()");
		if (LipossGlobals.IsITMS())
		{
			if (Global.JSDX.equals(Global.instAreaShortName))
			{
				isJSITMS = "1";
			}
		}
		devList = bio.getDevList(activetime1, cityId, curPage_splitPage, num_splitPage,gw_type);
		maxPage_splitPage = bio.getDevCount(activetime1, cityId, curPage_splitPage,
				num_splitPage,gw_type);
		return "devlist";
	}

	/**
	 * 设备列表导出excel
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return String
	 */
	public String getDeviceExcel()
	{
		logger.debug("getDeviceExcel()");
		fileName = "不活跃设备列表";
		title = new String[] { "属地", "设备厂商", "型号", "软件版本", "设备序列号", "域名或IP", "绑定账号" };
		column = new String[] { "city_name", "vendor_add", "device_model",
				"softwareversion", "device_serialnumber", "loopback_ip", "username" };
		if (LipossGlobals.IsITMS())
		{
			if (Global.JSDX.equals(Global.instAreaShortName))
			{
				title = new String[] { "属地", "设备厂商", "型号", "软件版本", "设备序列号", "域名或IP",
						"绑定账号", "是否开启iTV" };
				column = new String[] { "city_name", "vendor_add", "device_model",
						"softwareversion", "device_serialnumber", "loopback_ip",
						"username", "iTV" };
			}
		}
		data = bio.getDevExcel(activetime1, cityId,gw_type);
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + activetime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (activetime == null || "".equals(activetime))
		{
			activetime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(activetime);
			activetime1 = String.valueOf(dt.getLongTime());
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
	 * @return the activetime
	 */
	public String getActivetime()
	{
		return activetime;
	}

	/**
	 * @param activetime
	 *            the activetime to set
	 */
	public void setActivetime(String activetime)
	{
		this.activetime = activetime;
	}

	/**
	 * @return the activetime1
	 */
	public String getActivetime1()
	{
		return activetime1;
	}

	/**
	 * @param activetime1
	 *            the activetime1 to set
	 */
	public void setActivetime1(String activetime1)
	{
		this.activetime1 = activetime1;
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
	 * @return the isJSITMS
	 */
	public String getIsJSITMS()
	{
		return isJSITMS;
	}

	/**
	 * @param isJSITMS
	 *            the isJSITMS to set
	 */
	public void setIsJSITMS(String isJSITMS)
	{
		this.isJSITMS = isJSITMS;
	}

	/**
	 * @return the bio
	 */
	public NoActiveDevBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(NoActiveDevBIO bio)
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

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

    
	
}
