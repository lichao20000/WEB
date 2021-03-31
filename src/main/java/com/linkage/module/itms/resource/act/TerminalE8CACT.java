package com.linkage.module.itms.resource.act;

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
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.TerminalE8CBIO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-17
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class TerminalE8CACT extends splitPageAction implements SessionAware, ServletRequestAware
{

	private static Logger logger = LoggerFactory.getLogger(TerminalE8CACT.class);
	private Map session;
	private HttpServletRequest request;
	private TerminalE8CBIO bio;
	
	// 开始时间 
	private String startOpenDate = "";
	// 开始时间 
	private String startOpenDate1 = "";
	// 结束时间 
	private String endOpenDate = "";
	// 结束时间 
	private String endOpenDate1 = "";
	// 属地
	private String city_id = null;
	
	
	// 属地列表
	private List<Map<String, String>> cityList = null;
	
	private List<Map> terminalList = null;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private String ajax;
	
	@Override
	public String execute() throws Exception
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}
	
	/**
	 * 获取报表统计信息(e8-c)
	 * @return
	 */
	public String getTerminalE8CInfo(){
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		terminalList = bio.getTerminalE8CInfo(city_id, startOpenDate1, endOpenDate1);
		return "list";
	}
	
	public String getTerminalE8CInfoExcel()
	{	
		logger.debug("getOperateByHandInfoExcel()");
		
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		terminalList = bio.getTerminalE8CInfo(city_id, startOpenDate1, endOpenDate1);
		String excelCol = "city_name#allNum#newNum#twoNum#fourNum#egwNum";
		String excelTitle = "属地#终端总数#新增终端总数#2+1规格#4+2规格#政企终端数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "e8-c终端信息";
		data = terminalList;
		return "excel";
	}



	
	public TerminalE8CBIO getBio()
	{
		return bio;
	}

	
	public void setBio(TerminalE8CBIO bio)
	{
		this.bio = bio;
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

	
	public String getCity_id()
	{
		return city_id;
	}

	
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	
	public List<Map> getTerminalList()
	{
		return terminalList;
	}

	
	public void setTerminalList(List<Map> terminalList)
	{
		this.terminalList = terminalList;
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
	
	@Override
	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	
	public String getStartOpenDate1()
	{
		return startOpenDate1;
	}

	
	public void setStartOpenDate1(String startOpenDate1)
	{
		this.startOpenDate1 = startOpenDate1;
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
