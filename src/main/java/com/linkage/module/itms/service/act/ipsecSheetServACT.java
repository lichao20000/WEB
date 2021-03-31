
package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.ipsecSheetServBIO;

import action.splitpage.splitPageAction;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-26
 * @category com.linkage.module.itms.service.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ipsecSheetServACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	/** **/
	private static final long serialVersionUID = 1L;
	// session
	private Map session;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ipsecSheetServACT.class);
	private static String DEVICETYPE_ALL = "0";
	private static String DEVICETYPE_DEFAULT_JSDX = "2";
	private HttpServletRequest request;
	/** 属地ID */
	private String cityId = "-1";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 开始时间 */
	private String startOpenDate = "";
	/** 结束时间 */
	private String endOpenDate = "";
	/** 开始时间 */
	private String startOpenDate1 = "";
	/** 结束时间 */
	private String endOpenDate1 = "";
	/**
	 * 用户名类型 "1" 逻辑SN, "2" 宽带号码
	 */
	private String usernameType = "";
	/** 用户名 */
	private String username;
	/** 开通状态 "1" 成功,"0" 未做,"-1" 失败 */
	private String openstatus;
	private ipsecSheetServBIO bio;
	/** 业务信息列表 */
	private List<Map> ipsecSheetServList;
	/** 业务信息列表 1:tab_hgwcustomer  2:tab_egwcustomer */
	private String  type;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 文件名
	private String fileName;
	private List<Map> data;
	//用户id
	private String user_id;
	private String gw_type;
	private List<Map<String,Object>>relatedBaseInfo;
	private String userTypeName;
	private List<Map<String, Object>> serviceInfo;
	private Map<String,Object> userArea;
	private Map<String,Object> userCardInfo;
	private Map<String,String> bindTypeMap;
	private Map<String,String> userTypeMap;
	private String deviceId;
	private String oui;
	private String deviceSN;
	private String servTypeId;
	private String servstauts;
	private String ajax;
	//ipsec工单
	private List<Map<String, Object>> ipsecSheetList;
	private List<Map<String, String>> ipsecDeviceInfo;
	private List<Map<String, String>> ServiceStatByDevice;
	/**
	 * 初始化页面
	 * 
	 * @return
	 */
	public String init()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}
	/**
	 * 调用预读接口，重新激活工单 业务下发
	 * @return
	 */
	public String callPreProcess()
	{
		ajax = bio.callPreProcess(user_id, deviceId, oui, deviceSN, servTypeId, servstauts);
		return "ajax";
	}
	/**
	 * 查询单台设备信息
	 */
	/*public String getSingleDeviceInfo()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		long m_AreaId = curUser.getUser().getAreaId();
		logger.warn("deviceId==="+deviceId);
		ipsecDeviceInfo=bio.getSingleDeviceInfo(deviceId, m_AreaId);
		logger.warn("ipsecDeviceInfo==="+ipsecDeviceInfo);
		logger.warn("ipsecDeviceInfo.size()==="+ipsecDeviceInfo.size());
		ServiceStatByDevice=bio.getServiceStatByDevice(deviceId);
		logger.warn("ServiceStatByDevice==="+ServiceStatByDevice);
		return "SingleDeviceInfo";
	}*/
	/*public String queryConnCondition()
	{
		ajax=bio.queryConnCondition(deviceId);
		return "ajax"; 
	}*/
	/**
	 * 查询信息
	 * @return
	 */
	public String getIpsecSheetServInfo()
	{
		logger.warn("type="+gw_type);
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		ipsecSheetServList=bio.getIpsecSheetServInfo(cityId, startOpenDate, endOpenDate, usernameType, username, openstatus, curPage_splitPage, curPage_splitPage,gw_type);
		logger.warn("ipsecSheetServList========"+ipsecSheetServList);
		maxPage_splitPage=bio.getMaxPage_splitPage();
		return "list";
	}
	public String toExcel()
	{
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		title = new String[7];
		title[0]="LOID";
		title[1]="属地";
		title[2]="IPSEC受理时间";
		title[3]="业务类型";
		title[4]="业务账号";
		title[5]="设备序列号";
		title[6]="开通状态";
		column = new String[7];
		column[0]="username";
		column[1]="city_name";
		column[2]="opendate";
		column[3]="serv_type";
		column[4]="serUsername";
		column[5]="device_serialnumber";
		column[6]="open_status";
		fileName="云网关业务查询";
		data=bio.getIpsecExcel(cityId, startOpenDate, endOpenDate, usernameType, username, openstatus, gw_type);
		return"excel";
	}
	/**
	 * 查询用户信息
	 *//*
	public String queryHgwUserRelatedInfo()
	{
		if(null!=user_id){
			relatedBaseInfo=bio.queryUserRelatedBaseInfo(user_id, gw_type);
			serviceInfo=bio.getServiceInfo(user_id, gw_type);
		}
	}*/
	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate = String.valueOf(dt.getLongTime());
		}
	}

	// 当前年的1月1号
	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.DATE, 1);
		now.set(Calendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = "";
		/*
		 * if ("false".equals(isRealtimeQuery)) { Calendar cal = Calendar.getInstance();
		 * cal.add(Calendar.DATE,-1); String yesterday = new
		 * SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime()); time =
		 * yesterday+"23:"+"59:"+"59"; }else{
		 */
		time = fmtrq.format(now.getTime());
		/* } */
		return time;
	}
	/**
 	* 查询工单详情
 	* @return
 	*/
	public String queryBssSheet()
	{
		ipsecSheetList = new ArrayList<Map<String,Object>>();
		ipsecSheetList=bio.getIpsecInfeoInternet(user_id, gw_type);
		logger.warn("ipsecSheetList===="+ipsecSheetList);
		return "internet";
	}
	public static String getDEVICETYPE_ALL()
	{
		return DEVICETYPE_ALL;
	}

	public static void setDEVICETYPE_ALL(String dEVICETYPE_ALL)
	{
		DEVICETYPE_ALL = dEVICETYPE_ALL;
	}

	public static String getDEVICETYPE_DEFAULT_JSDX()
	{
		return DEVICETYPE_DEFAULT_JSDX;
	}

	public static void setDEVICETYPE_DEFAULT_JSDX(String dEVICETYPE_DEFAULT_JSDX)
	{
		DEVICETYPE_DEFAULT_JSDX = dEVICETYPE_DEFAULT_JSDX;
	}

	public Map getSession()
	{
		return session;
	}

	public List<Map> getIpsecSheetServList()
	{
		return ipsecSheetServList;
	}

	public void setIpsecSheetServList(List<Map> ipsecSheetServList)
	{
		this.ipsecSheetServList = ipsecSheetServList;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public String getStartOpenDate()
	{
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}

	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	public ipsecSheetServBIO getBio()
	{
		return bio;
	}

	public void setBio(ipsecSheetServBIO bio)
	{
		this.bio = bio;
	}

	@Override
	public void setSession(Map session)
	{
		this.session = session;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		// TODO Auto-generated method stub
	}

	public String getStartOpenDate1()
	{
		return startOpenDate1;
	}

	public void setStartOpenDate1(String startOpenDate1)
	{
		this.startOpenDate1 = startOpenDate1;
	}

	public String getEndOpenDate1()
	{
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1)
	{
		this.endOpenDate1 = endOpenDate1;
	}

	public String getUsernameType()
	{
		return usernameType;
	}

	public void setUsernameType(String usernameType)
	{
		this.usernameType = usernameType;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getOpenstatus()
	{
		return openstatus;
	}

	public void setOpenstatus(String openstatus)
	{
		this.openstatus = openstatus;
	}

	
	public String getType()
	{
		return type;
	}

	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String[] getTitle()
	{
		return title;
	}
	
	public void setTitle(String[] title)
	{
		this.title = title;
	}
	
	public String[] getColumn()
	{
		return column;
	}
	
	public void setColumn(String[] column)
	{
		this.column = column;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public List<Map> getData()
	{
		return data;
	}
	
	public void setData(List<Map> data)
	{
		this.data = data;
	}
	
	public String getUser_id()
	{
		return user_id;
	}
	
	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}
	
	public String getGw_type()
	{
		return gw_type;
	}
	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}
	
	public List<Map<String, Object>> getRelatedBaseInfo()
	{
		return relatedBaseInfo;
	}
	
	public void setRelatedBaseInfo(List<Map<String, Object>> relatedBaseInfo)
	{
		this.relatedBaseInfo = relatedBaseInfo;
	}
	public String getUserTypeName()
	{
		return userTypeName;
	}
	
	public void setUserTypeName(String userTypeName)
	{
		this.userTypeName = userTypeName;
	}
	
	public List<Map<String, Object>> getServiceInfo()
	{
		return serviceInfo;
	}
	
	public void setServiceInfo(List<Map<String, Object>> serviceInfo)
	{
		this.serviceInfo = serviceInfo;
	}
	
	public Map<String, Object> getUserArea()
	{
		return userArea;
	}
	
	public void setUserArea(Map<String, Object> userArea)
	{
		this.userArea = userArea;
	}
	
	public Map<String, Object> getUserCardInfo()
	{
		return userCardInfo;
	}
	
	public void setUserCardInfo(Map<String, Object> userCardInfo)
	{
		this.userCardInfo = userCardInfo;
	}
	
	public Map<String, String> getBindTypeMap()
	{
		return bindTypeMap;
	}
	
	public void setBindTypeMap(Map<String, String> bindTypeMap)
	{
		this.bindTypeMap = bindTypeMap;
	}
	
	public Map<String, String> getUserTypeMap()
	{
		return userTypeMap;
	}
	
	public void setUserTypeMap(Map<String, String> userTypeMap)
	{
		this.userTypeMap = userTypeMap;
	}
	
	public List<Map<String, Object>> getIpsecSheetList()
	{
		return ipsecSheetList;
	}
	
	public void setIpsecSheetList(List<Map<String, Object>> ipsecSheetList)
	{
		this.ipsecSheetList = ipsecSheetList;
	}

	
	public String getDeviceId()
	{
		return deviceId;
	}

	
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	
	public String getOui()
	{
		return oui;
	}

	
	public void setOui(String oui)
	{
		this.oui = oui;
	}

	
	public String getDeviceSN()
	{
		return deviceSN;
	}

	
	public void setDeviceSN(String deviceSN)
	{
		this.deviceSN = deviceSN;
	}

	
	public String getServTypeId()
	{
		return servTypeId;
	}

	
	public void setServTypeId(String servTypeId)
	{
		this.servTypeId = servTypeId;
	}

	
	public String getServstauts()
	{
		return servstauts;
	}

	
	public void setServstauts(String servstauts)
	{
		this.servstauts = servstauts;
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
	public List<Map<String, String>> getIpsecDeviceInfo()
	{
		return ipsecDeviceInfo;
	}
	
	public void setIpsecDeviceInfo(List<Map<String, String>> ipsecDeviceInfo)
	{
		this.ipsecDeviceInfo = ipsecDeviceInfo;
	}
	
	public List<Map<String, String>> getServiceStatByDevice()
	{
		return ServiceStatByDevice;
	}
	
	public void setServiceStatByDevice(List<Map<String, String>> serviceStatByDevice)
	{
		ServiceStatByDevice = serviceStatByDevice;
	}
	
}
