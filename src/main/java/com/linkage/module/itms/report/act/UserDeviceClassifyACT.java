
package com.linkage.module.itms.report.act;

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
import com.linkage.module.itms.report.bio.UserDeviceClassifyBIO;

/**
 * 用户设备分类统计(桥接 路由 e8c e8b 是否支持wifi)及其详细信息
 * @author Fanjm 35572
 * @version 1.0
 * @since 2016年11月21日
 * @category com.linkage.module.itms.report.act
 * @copyright 2016 亚信安全.版权所有
 */
@SuppressWarnings("unchecked")
public class UserDeviceClassifyACT extends splitPageAction implements SessionAware
{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -8542881299840748199L;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(UserDeviceClassifyACT.class);
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
	/** 是否根地区 */
	private boolean isRoot = true;
	/** 查询分类条件*/
	private String classfy = "";
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private UserDeviceClassifyBIO bio;
	private List<Map> devList = null;


	/**
	 * 初始化页面
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil(end_time * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil((end_time-24*3600*365) * 1000);
		starttime = dt.getLongDate();
		
		return "init";
	}

	/**
	 * 地区用户分类统计
	 */
	public String execute()
	{
		logger.debug("execute()");
		this.setTime();
		this.isRootArea(cityId);
		data = bio.countUserClassifyResult(starttime1, endtime1, cityId, isRoot);
		
		return "list";
		
	}

	
	/**
	 * 判断查询的是否是根地区
	 * @param city_Id
	 */
	private void isRootArea(String city_Id) {
		if("-1".equals(Global.G_City_Pcity_Map.get(city_Id)))
		{
			isRoot = true;
		}
		else
		{
			isRoot = false;
		}
		
	}

	/**
	 * Excel导出统计数据
	 * @return String 
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "用户设备分类统计报表";
		this.isRootArea(cityId);
		data = bio.countUserClassifyResult(starttime1, endtime1, cityId, isRoot);
		
		title = new String[] { "属地", "桥接用户", "路由用户", "E8-C", "E8-B", "支持WIFI", "不支持WIFI" };
		column = new String[] { "city_name", "wan_type1", "wan_type2", "device_type_e8c", "device_type_e8b", "wlan_num_support", "wlan_num_not_support" };
		
		return "excel";
	}

	/**
	 * 点击相应地区统计值时获取符合要求设备列表
	 * @return
	 */
	public String getDev()
	{
		logger.debug("getDev()");
		this.isRootArea(cityId);
		devList = bio.getDevList(starttime1, endtime1, cityId,curPage_splitPage, num_splitPage, classfy, isRoot);
		maxPage_splitPage = bio.getDevCount(starttime1, endtime1, cityId, curPage_splitPage, num_splitPage,classfy,isRoot);
		return "devlist";
		
	}

	/**
	 * 设备列表导出
	 * @return
	 */
	public String getDevExcel()
	{
		logger.debug("getDevExcel()");
		fileName = "用户设备分类统计详细信息";
		this.isRootArea(cityId);
		data = bio.getDevExcel(starttime1, endtime1, cityId, classfy, isRoot);
		title = new String[] { "属 地", "区县", "厂家", "型号", "软件版本", "硬件版本","设备序列号","逻辑ID","宽带账号" };
		column = new String[] { "parent_name", "city_name", "vendor_name", "device_model", "softwareversion", "hardwareversion","device_serialnumber", "logicId","username"};
		return "excel";
	}

	/**
	 * 时间转化为距1970年1月1日以来的时间（秒）
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


	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
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

	

	public String getClassfy() {
		return classfy;
	}

	public void setClassfy(String classfy) {
		this.classfy = classfy;
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




	public UserDeviceClassifyBIO getBio() {
		return bio;
	}


	public void setBio(UserDeviceClassifyBIO bio) {
		this.bio = bio;
	}
	
}
