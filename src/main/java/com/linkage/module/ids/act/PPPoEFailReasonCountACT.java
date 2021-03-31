package com.linkage.module.ids.act;

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
import com.linkage.module.ids.bio.PPPoEFailReasonCountBIO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2014-2-13
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class PPPoEFailReasonCountACT extends splitPageAction implements SessionAware,ServletRequestAware
{
	
	private static Logger logger = LoggerFactory.getLogger(PPPoEFailReasonCountACT.class);
	
	// session
	private Map session = null;
	private HttpServletRequest request;
	
	private PPPoEFailReasonCountBIO bio;
	
	// 开始时间 
	private String startOpenDate = "";
	//转换的 时间
	private String startOpenDate1 = "";
	// 结束时间 
	private String endOpenDate = "";
	//装换的时间
	private String endOpenDate1 = "";
		
	// 属地
	private String city_id = null;
	
	private String failCode = null;
	
	private List<Map> pppoeCountList = null;
	
	private List<Map> pppoeInfoList = null;
	
	
	// 属地列表
	private List<Map<String, String>> cityList = null;
	
	// ********Export All Data To Excel****************
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private int total;
	
	
	@Override
	public String execute() throws Exception
	{
		logger.warn("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = this.getStartDate();
		endOpenDate = this.getEndDate();
		return "init";
	}

	public String getPppoeFailReasonCount(){
		UserRes curUser = (UserRes) session.get("curUser");
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			city_id = curUser.getCityId();
		}
		pppoeCountList = bio.getPppoeFailReasonCountList(startOpenDate1, endOpenDate1, city_id);
		return "list";
	}
	
	public String getPppoeFailReasonCountExcel(){
		UserRes curUser = (UserRes) session.get("curUser");
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			city_id = curUser.getCityId();
		}
		pppoeCountList = bio.getPppoeFailReasonCountList(startOpenDate1, endOpenDate1, city_id);
		String excelCol = "city_name#reason1#reason2#reason3";
		String excelTitle = "属地#ERROR_ISP_TIME_OUT#ERROR_AUTHENTICATION_FAILURE#ERROR_ISP_DISCONNECT";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "pppoeCountList";
		data = pppoeCountList;
		return "excel";
	}
	
	public String getPppoeFailReasonInfo(){
		UserRes curUser = (UserRes) session.get("curUser");
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			city_id = curUser.getCityId();
		}
		pppoeInfoList = bio.getPppoeFailReasonInfo(startOpenDate1, endOpenDate1, city_id, failCode, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countGetPppoeFailReasonInfo(startOpenDate1, endOpenDate1, city_id, failCode, curPage_splitPage, num_splitPage);
		return "pppoeInfo";
	}
	
	public String getPppoeFailReasonInfoExcel(){
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		pppoeInfoList = bio.getPppoeFailReasonInfoExcel(startOpenDate1, endOpenDate1, city_id, failCode);
		String excelCol = "city_name#loid#device_serialnumber#device_type#status#reason#upload_time";
		String excelTitle = "区域#LOID#设备序列号#终端类型#PPPoE拨号状态#拨号失败原因#上报时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "pppoeInfoList";
		data = pppoeInfoList;
		return "excel";
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
		now.set(GregorianCalendar.MONTH, Calendar.MONTH);
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

	
	public PPPoEFailReasonCountBIO getBio()
	{
		return bio;
	}

	
	public void setBio(PPPoEFailReasonCountBIO bio)
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

	
	public String getFailCode()
	{
		return failCode;
	}

	
	public void setFailCode(String failCode)
	{
		this.failCode = failCode;
	}

	
	public List<Map> getPppoeCountList()
	{
		return pppoeCountList;
	}

	
	public void setPppoeCountList(List<Map> pppoeCountList)
	{
		this.pppoeCountList = pppoeCountList;
	}

	
	public List<Map> getPppoeInfoList()
	{
		return pppoeInfoList;
	}

	
	public void setPppoeInfoList(List<Map> pppoeInfoList)
	{
		this.pppoeInfoList = pppoeInfoList;
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

	
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}
}
