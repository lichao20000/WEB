package com.linkage.module.itms.report.act;

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


import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.ZeroAutoConfigInfoBIO;

import action.splitpage.splitPageAction;



public class ZeroAutoConfigInfoACT extends splitPageAction implements
		ServletRequestAware, SessionAware {

private static Logger logger = LoggerFactory.getLogger(ZeroAutoConfigInfoACT.class);
	
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
	
	// 业务类型
	private String servType = null;
	// 开通状态
	private String resultType = null;
	// session
	private Map session = null;
	private HttpServletRequest request;
	// 属地列表
	private List<Map<String, String>> cityList = null;
	// 列表数据
	private List<Map> zeroAutoList = null;
	// ********Export All Data To Excel****************
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private int total;
	private String ajax;
	
	private ZeroAutoConfigInfoBIO zeroAutoConfigBio;

	/**
	 * 页面初始化
	 * 
	 * @author gaoyi
	 * @date 6 17, 2013
	 * @param
	 * @return String
	 */
	public String init()
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
	 * 页面数据列表
	 * 
	 * @author gaoyi
	 * @date 5 14, 2013
	 * @param
	 * @return String
	 */
	public String getZeroAutoConfigInfo()
	{
		logger.debug("getZeroAutoConfigInfo()");
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		zeroAutoList = zeroAutoConfigBio.getZeroConfigInfo(startOpenDate1, endOpenDate1, city_id, servType, resultType, curPage_splitPage, num_splitPage);
		maxPage_splitPage = zeroAutoConfigBio.countZeroConfigInfo(startOpenDate1, endOpenDate1, city_id, servType,resultType, curPage_splitPage, num_splitPage);
		return "list";
	}

	public String getZeroAutoConfigInfoExcel()
	{	
		logger.debug("getZeroAutoConfigInfoExcel()");
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		zeroAutoList = zeroAutoConfigBio.getZeroConfigInfoExcel(startOpenDate1, endOpenDate1,
				city_id, servType,  resultType);
		String excelCol = "username#city_name#dealdate#serv_type#device_serialnumber#open_status#zdType#vendor_name#device_model#access_style_relay_id#linkaddress";
		String excelTitle = "逻辑SN#属地#工单受理时间#业务类型#设备序列号#开通状态#终端类型#终端厂家#终端型号#接入方式#用户装机地址";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "zeroAutoConfig";
		data = zeroAutoList;
		return "excel";
	}

	public String getZeroAutoConfigInfoCount()
	{	
		logger.debug("getZeroAutoConfigInfoCount");
		total = zeroAutoConfigBio.countZeroConfigInfoExcel(startOpenDate1, endOpenDate1,
				city_id, servType, resultType);
		ajax = total + "";
		return "ajax";
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


	public String getResultType()
	{
		return resultType;
	}

	public void setResultType(String resultType)
	{
		this.resultType = resultType;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
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


	public ZeroAutoConfigInfoBIO getZeroAutoConfigBio() {
		return zeroAutoConfigBio;
	}

	public void setZeroAutoConfigBio(ZeroAutoConfigInfoBIO zeroAutoConfigBio) {
		this.zeroAutoConfigBio = zeroAutoConfigBio;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	
	public String getStartOpenDate()
	{
		return startOpenDate;
	}

	
	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}

	
	public String getStartOpenDate1()
	{
		return startOpenDate1;
	}

	
	public void setStartOpenDate1(String startOpenDate1)
	{
		this.startOpenDate1 = startOpenDate1;
	}

	
	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	
	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	
	public String getEndOpenDate1()
	{
		return endOpenDate1;
	}

	
	public void setEndOpenDate1(String endOpenDate1)
	{
		this.endOpenDate1 = endOpenDate1;
	}
	
	public String getServType()
	{
		return servType;
	}

	public void setServType(String servType)
	{
		this.servType = servType;
	}
	public List<Map> getZeroAutoList() {
		return zeroAutoList;
	}

	public void setZeroAutoList(List<Map> zeroAutoList) {
		this.zeroAutoList = zeroAutoList;
	}
}
