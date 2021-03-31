package com.linkage.module.gtms.report.action;

import java.text.SimpleDateFormat;
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

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.report.serv.ConfigFailInfoServ;
import com.linkage.module.gtms.service.action.OperateByHandActionImp;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.extend.struts.splitpage.SplitPageAction;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-27
 * @category com.linkage.module.gtms.report.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class ConfigFailInfoActionImpl extends splitPageAction implements ConfigFailInfoAction,SessionAware,ServletRequestAware
{
	private static Logger logger = LoggerFactory.getLogger(ConfigFailInfoActionImpl.class);
	
	private ConfigFailInfoServ configFailInfoSer;
	
	private String city_id;
	// 开始时间 
	private String startOpenDate = "";
	// 开始时间 
	private String startOpenDate1 = "";
	// 结束时间 
	private String endOpenDate = "";
	// 结束时间 
	private String endOpenDate1 = "";
	// 属地列表
	private List<Map<String, String>> cityList = null;
	//失败列表
	private List<Map> failInfoList = null;
	
	

	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private int total;
	private String ajax;
	
	// session
	private Map session = null;
		
	private HttpServletRequest request;
	
	
	
	public String init()
	{	
		logger.debug("init()");
		logger.warn("init()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}
	
	public String queryConfigInfo()
	{	
		logger.debug("queryConfigInfo");
		logger.warn("queryConfigInfo()");
		
		
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		
		failInfoList = configFailInfoSer.queryfailinfo(startOpenDate1, endOpenDate1, city_id, curPage_splitPage, num_splitPage);
		
		maxPage_splitPage = configFailInfoSer.countQueryfailinfo(startOpenDate1, endOpenDate1, city_id, curPage_splitPage, num_splitPage);
		
		logger.warn(maxPage_splitPage+"back");
		
		return "list";
	}

	public String excelConfigInfo()
	{	
		logger.debug("excelConfigInfo()");
		logger.warn("excelConfigInfo()");
		
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		
		failInfoList = configFailInfoSer.getQueryfailinfoExcel(startOpenDate1, endOpenDate1, city_id);
		String excelCol = "city_name#device_model#device_serialnumber#device_id_ex#times#fault_desc#fault_reason";
		String excelTitle = "属地名称#设备型号#设备序列号#设备逻辑号#下发时间#错误描述#错误详细描述";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "failMessage";
		data = failInfoList;
		return "excel";
	}
	
	public String getConfigInfoCount(){
		logger.debug("getConfigInfoCount()");
		
		logger.warn("getConfigInfoCount()");
		
		this.setTime();
		ajax = "";
		total = configFailInfoSer.countQueryfailinfoExcel(startOpenDate1, endOpenDate1, city_id);
		ajax = total + "";
		return "ajax";
	}
	
	
	// 当前年的1月1号
	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
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
		String time = fmtrq.format(now.getTime());
		return time;
	}
	
	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	

	public void setServletRequest(HttpServletRequest req)
	{
		this.request = req;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}


	
	public String getCity_id()
	{
		return city_id;
	}


	
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
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


	
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}


	
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	
	public ConfigFailInfoServ getConfigFailInfoSer()
	{
		return configFailInfoSer;
	}

	
	public void setConfigFailInfoSer(ConfigFailInfoServ configFailInfoSer)
	{
		this.configFailInfoSer = configFailInfoSer;
	}

	
	public List<Map> getFailInfoList()
	{
		return failInfoList;
	}

	
	public void setFailInfoList(List<Map> failInfoList)
	{
		this.failInfoList = failInfoList;
	}

	
	public List<Map> getData()
	{
		return data;
	}

	
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	
	public String[] getColumn()
	{
		return column;
	}

	
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public int getTotal()
	{
		return total;
	}

	
	public void setTotal(int total)
	{
		this.total = total;
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}


}
