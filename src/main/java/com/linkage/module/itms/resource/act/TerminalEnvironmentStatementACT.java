
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
import com.linkage.module.itms.resource.bio.TerminalEnvironmentStatementBIO;

public class TerminalEnvironmentStatementACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	private static final long serialVersionUID = -7787526862261263533L;
	private static Logger logger = LoggerFactory
			.getLogger(TerminalEnvironmentStatementACT.class);
	private TerminalEnvironmentStatementBIO bio;
	@SuppressWarnings("unused")
	private HttpServletRequest request;
	@SuppressWarnings("rawtypes")
	private Map session;
	//开始时间
	private String startOpenDate="";
	//开始时间
	private String startOpenDate1="";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	//温度
	private String temperature = "";
	
	//电流
	private String bais_current;
	//电压
	private String voltage;
	// 属地
	private String city_id = null;
	// 属地列表
	private List<Map<String, String>> cityList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	@SuppressWarnings("rawtypes")
	private List<Map> deployList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> deployDevList = null;

	/**
	 * 初始化页面数据
	 */
	@Override
	public String execute() throws Exception
	{
		logger.debug("TerminalEnvironmentStatementACT=>execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}

	public String queryTerminalEnvironmentStatement()
	{
		logger.debug("TerminalEnvironmentStatementACT=>queryTerminalEnvironmentStatement()");
		this.setTime();
		deployList = bio.queryTerminalEnvironmentStatement(city_id, startOpenDate1, endOpenDate1,temperature,bais_current,voltage);
		return "list";
	}

	public String queryTerminalEnvironmentStatementExcel()
	{
		logger.debug("TerminalEnvironmentStatementACT=>queryTerminalEnvironmentStatementExcel()");
		this.setTime();
		deployList = bio.queryTerminalEnvironmentStatement(city_id, startOpenDate1, endOpenDate1,temperature,bais_current,voltage);
		String excelCol = "city_name#deploy_total";
		String excelTitle = "区域#终端数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "deployListTotal";
		data = deployList;
		return "excel";
	}

	public String queryTerminalEnvironmentStatementDev()
	{
		logger.debug("TerminalEnvironmentStatementACT=>queryTerminalEnvironmentStatementDev()");
		this.setTime();
		deployDevList = bio.queryTerminalEnvironmentStatementList(city_id, startOpenDate1, endOpenDate1,temperature,bais_current,voltage,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQueryTerminalEnvironmentStatementList(city_id, startOpenDate1, endOpenDate1,temperature, bais_current,voltage,curPage_splitPage, num_splitPage);
		return "devlist";
	}

	public String queryTerminalEnvironmentStatementDevExcel()
	{
		logger.debug("TerminalEnvironmentStatementACT=>queryTerminalEnvironmentStatementDevExcel()");
		this.setTime();
		deployDevList = bio.excelQueryTerminalEnvironmentStatementList(city_id, startOpenDate1, endOpenDate1,temperature,bais_current,voltage);
		String excelCol = "city_name#loid#device_serialnumber#device_type#temperature#bais_current#vottage#upload_time";
		String excelTitle = "区域#loid#设备序列号#终端类型#温度(度) #电流(2微安)#电压(100微伏)#上报时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "deployDevList";
		data = deployDevList;
		return "excel";
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		// now.set(Calendar.HOUR_OF_DAY, 23);
		// now.set(Calendar.MINUTE, 59);
		// now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}
	
	// 当前年的1月1号
		private String getStartDate() {
			GregorianCalendar now = new GregorianCalendar();
			SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.US);
			now.set(GregorianCalendar.DATE, 1);
			now.set(GregorianCalendar.MONTH, Calendar.MONTH);
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			String time = fmtrq.format(now.getTime());
			return time;
		}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + endOpenDate);
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


	public TerminalEnvironmentStatementBIO getBio() {
		return bio;
	}

	public void setBio(TerminalEnvironmentStatementBIO bio) {
		this.bio = bio;
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

	
	public String getBais_current()
	{
		return bais_current;
	}

	
	public void setBais_current(String bais_current)
	{
		this.bais_current = bais_current;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}


	@SuppressWarnings("rawtypes")
	public List<Map> getData()
	{
		return data;
	}

	@SuppressWarnings("rawtypes")
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

	@SuppressWarnings("rawtypes")
	public List<Map> getDeployList()
	{
		return deployList;
	}

	@SuppressWarnings("rawtypes")
	public void setDeployList(List<Map> deployList)
	{
		this.deployList = deployList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDeployDevList()
	{
		return deployDevList;
	}

	@SuppressWarnings("rawtypes")
	public void setDeployDevList(List<Map> deployDevList)
	{
		this.deployDevList = deployDevList;
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getStartOpenDate1() {
		return startOpenDate1;
	}

	public void setStartOpenDate1(String startOpenDate1) {
		this.startOpenDate1 = startOpenDate1;
	}

	public String getEndOpenDate1() {
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1) {
		this.endOpenDate1 = endOpenDate1;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	
	public String getVoltage()
	{
		return voltage;
	}

	
	public void setVoltage(String voltage)
	{
		this.voltage = voltage;
	}
	
}
