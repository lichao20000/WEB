
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
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.bio.BridgeToRouteCountBIO;
import com.linkage.module.gwms.util.StringUtil;
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 桥接、路由统计
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class BridgeToRouteCountACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(BridgeToRouteCountACT.class);
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
	private List<Map> hgwList = null;
	// 工作模式 "1"桥接模式 "2"路由模式
	private String sessionType;
	private BridgeToRouteCountBIO bio;
	
	private String ajax = "";

	/**
	 * 统计初始化页面
	 * 
	 * @author wangsenbo
	 * @date Jul 19, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		return "init";
	}

	/**
	 * 统计
	 * 
	 * @author wangsenbo
	 * @date Jul 19, 2010
	 * @param
	 * @return String
	 */
	public String count()
	{
		logger.debug("iTVCount()");
		this.setTime();
		
		data = bio.count(starttime1, endtime1, cityId);
		return "list";
	}

	/**
	 * 统计导出Excel
	 * 
	 * @author wangsenbo
	 * @date Jul 19, 2010
	 * @param
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "IPTV";
		title = new String[] { "属地", "桥接开户", "路由开户" };
		column = new String[] { "city_name", "bridgetotal", "routetotal" };
		data = bio.count(starttime1, endtime1, cityId);
		return "excel";
	}

	/**
	 * 统计用户列表
	 * 
	 * @author wangsenbo
	 * @date Jul 19, 2010
	 * @param
	 * @return String
	 */
	public String getHgw()
	{
		logger.debug("getHgw()");
		hgwList = bio.getHgwList(starttime1, endtime1, cityId, sessionType,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getHgwCount(starttime1, endtime1, cityId, sessionType,
				curPage_splitPage, num_splitPage);
		return "hgwlist";
	}

	/**
	 * 统计用户列表Excel
	 * 
	 * @author wangsenbo
	 * @date Jul 19, 2010
	 * @param
	 * @return String
	 */
	public String getHgwExcel()
	{
		logger.debug("getHgwExcel()");
		fileName = "IPTV用户";
		title = new String[] { "属地", "宽带账号", "设备序列号", "工作模式" };
		column = new String[] { "city_name", "username", "device", "sessionType" };
		data = bio.getHgwExcel(starttime1, endtime1, cityId, sessionType);
		return "excel";
	}
	
	
	//////////////////////////////////////山东联通RSM新增桥接路由统计需求///////////////////////////////////////////////
	
	/**
	 *统计
	 * 
	 * @return
	 */
	public String count2()
	{
		logger.debug("iTVCount()");
		this.setTime();
		data = bio.count2(starttime1, endtime1, "00");
		return "list2";
	}
	
	/**
	 * 校验城市是否可以导出
	 * 
	 * @return
	 */
	public String checkCity2(){
		logger.debug("checkCity2()");
		String[] cityIdArr = null;
		String cityIdList = LipossGlobals.getLipossProperty("bridgeToRouteNoCityList");
		if(!StringUtil.IsEmpty(cityIdList)){
			cityIdArr = cityIdList.split(",");
		}
		if(null != cityIdArr){
			Arrays.sort(cityIdArr);
			if(Arrays.binarySearch(cityIdArr, cityId) >= 0){
				ajax = "1"; 
			}
		}
		
		return "ajax";
	}
	
	/**
	 * 统计导出Excel
	 * 
	 * @return
	 */
	public String getExcel2()
	{
		logger.debug("getExcel()");
		fileName = "桥接统计详单";
		if("2".equals(sessionType)){
			fileName = "路由统计详单";
		}
		title = new String[] { "区县", "LOID", "宽带账号","设备厂商","设备型号","硬件版本","软件版本","设备序列号" };
		column = new String[] { "city_name", "loid", "net_account","vendor_add","device_model","hardwareversion","softwareversion","device_serialnumber"};
		data = bio.exportExcel2(starttime1, endtime1, cityId, sessionType);
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
	 * @return the sessionType
	 */
	public String getSessionType()
	{
		return sessionType;
	}

	/**
	 * @param sessionType
	 *            the sessionType to set
	 */
	public void setSessionType(String sessionType)
	{
		this.sessionType = sessionType;
	}

	/**
	 * @return the bio
	 */
	public BridgeToRouteCountBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(BridgeToRouteCountBIO bio)
	{
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
}
