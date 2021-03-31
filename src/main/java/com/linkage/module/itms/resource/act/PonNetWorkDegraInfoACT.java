
package com.linkage.module.itms.resource.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.PonNetWorkDegraInfoBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-8-20
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class PonNetWorkDegraInfoACT extends splitPageAction implements
		ServletRequestAware, SessionAware
{

	private static Logger logger = LoggerFactory
			.getLogger(PonNetWorkDegraInfoACT.class);
	private PonNetWorkDegraInfoBIO bio;
	private HttpServletRequest request;
	private Map<String, Object> session;
	// 属地
	private String city_id = null;
	// 属地列表
	private List<Map<String, String>> cityList = null;
	private List<Map> ponlist;
	private List<Map> devList;
	// 日期
	private String endOpenDate;
	private String start_time;
	private String end_time;
	private int month;
	// 导出excel
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	@Override
	public String execute() throws Exception
	{
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		endOpenDate = getInitDate();
		return "init";
	}

	public String getPondataInfo()
	{
		start_time = getStartDate();
		end_time = getEndDate();
		ponlist = bio.getPondata(city_id, start_time, end_time, month);
		return "list";
	}

	public String getPondataExcel()
	{
		start_time = getStartDate();
		end_time = getEndDate();
		ponlist = bio.getPondata(city_id, start_time, end_time, month);
		String excelCol = "area_name#e8cValue#ponValue#pert#lessPonValue";
		String excelTitle = "区域#E8C终端总数#E8C终端光路劣化数#劣化光路占比#光功率小于30DB记录数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "ponlist";
		data = ponlist;
		return "excel";
	}

	public String getDegraContext()
	{
		start_time = getStartDate();
		end_time = getEndDate();
		devList = bio.getDegraContext(city_id, start_time, end_time, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = bio.countDegraContext(city_id, start_time, end_time,
				curPage_splitPage, num_splitPage);
		return "ponlist";
	}

	public String getDegraContextExcel()
	{
		start_time = getStartDate();
		end_time = getEndDate();
		devList = bio.getDegraContextExcel(city_id, start_time, end_time);
		String excelCol = "area_name#subarea_name#loid#device_serialnumber#tx_power#rx_power#linkaddress#olt_name#olt_ip#pon_id#ont_id#count_num";
		String excelTitle = "区域#子区域#LOID#设备序列号#发送光功率#接收光功率#接收地址#OLT名称#OLTIP#PON端口#ONTID#出现次数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devList";
		data = devList;
		return "excel";
	}
	
	public String getDegraLessContext(){
		start_time = getStartDate();
		end_time = getEndDate();
		devList = bio.getDegraLessContext(city_id, start_time, end_time, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDegraLessContext(city_id, start_time, end_time, curPage_splitPage, num_splitPage);
		return "ponlesslist";
	}
	
	public String getDegraLessContextExcel(){
		start_time = getStartDate();
		end_time = getEndDate();
		devList = bio.getDegraLessContextExcel(city_id, start_time, end_time);
		String excelCol = "area_name#subarea_name#loid#device_serialnumber#tx_power#rx_power#linkaddress#olt_name#olt_ip#pon_id#ont_id#count_num";
		String excelTitle = "区域#子区域#LOID#设备序列号#发送光功率#接收光功率#接收地址#OLT名称#OLTIP#PON端口#ONTID#出现次数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devList";
		data = devList;
		return "excel";
	}

	private String getInitDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		String[] s = endOpenDate.split("-");
		now.set(Calendar.YEAR, Integer.parseInt(s[0]));
		now.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
		month = Integer.parseInt(s[1]);
		DateTimeUtil dt = new DateTimeUtil(now.getTime());
		return dt.getFirtDayOfMonth();
	}

	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		String[] s = endOpenDate.split("-");
		DateTimeUtil dtf = new DateTimeUtil();
		String str = "";
		if (Integer.parseInt(s[0]) == dtf.getYear()
				&& Integer.parseInt(s[1]) == dtf.getMonth())
		{
			str = dtf.getDate();
		}
		else
		{
			now.set(Calendar.YEAR, Integer.parseInt(s[0]));
			now.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
			DateTimeUtil dt = new DateTimeUtil(now.getTime());
			str = dt.getLastDayOfMonth();
		}
		return str;
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

	public PonNetWorkDegraInfoBIO getBio()
	{
		return bio;
	}

	public void setBio(PonNetWorkDegraInfoBIO bio)
	{
		this.bio = bio;
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

	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	public List<Map> getPonlist()
	{
		return ponlist;
	}

	public void setPonlist(List<Map> ponlist)
	{
		this.ponlist = ponlist;
	}

	public List<Map> getDevList()
	{
		return devList;
	}

	public void setDevList(List<Map> devList)
	{
		this.devList = devList;
	}

	public synchronized int getMonth()
	{
		return month;
	}

	public synchronized void setMonth(int month)
	{
		this.month = month;
	}

	public synchronized List<Map> getData()
	{
		return data;
	}

	public synchronized void setData(List<Map> data)
	{
		this.data = data;
	}

	public synchronized String[] getColumn()
	{
		return column;
	}

	public synchronized void setColumn(String[] column)
	{
		this.column = column;
	}

	public synchronized String[] getTitle()
	{
		return title;
	}

	public synchronized void setTitle(String[] title)
	{
		this.title = title;
	}

	public synchronized String getFileName()
	{
		return fileName;
	}

	public synchronized void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
}
