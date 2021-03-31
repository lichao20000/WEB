
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
import com.linkage.module.ids.bio.IdsAlarmInfoBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2014-2-18
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class IdsAlarmInfoACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	private static Logger logger = LoggerFactory.getLogger(IdsAlarmInfoACT.class);
	private IdsAlarmInfoBIO bio;
	// session
	private Map session = null;
	private HttpServletRequest request;
	private String alarm_type;
	private String alarm_code;
	private String is_send_sheet;
	private String is_pre_release;
	private String alarm_count;
	private String is_release;
	private String device_id;
	private String loid;
	// 开始时间
	private String startOpenDate = "";
	// 转换的 时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 装换的时间
	private String endOpenDate1 = "";
	private List<Map> alarmList;
	private String ajax;
	// ********Export All Data To Excel****************
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private int total;
	
	// 属地列表
	private List<Map<String, String>> cityList = null;
	// 属地
	private String city_id = null;

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

	public String getIdsarmInfoList()
	{
		this.setTime();
		alarmList = bio.getIdsarmInfoList(startOpenDate1, endOpenDate1, alarm_type,
				alarm_code, alarm_count, is_send_sheet, is_pre_release, is_release,
				device_id, loid,city_id, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countIdsarmInfoList(startOpenDate1, endOpenDate1,
				alarm_type, alarm_code, alarm_count, is_send_sheet, is_pre_release,
				is_release, device_id, loid,city_id, curPage_splitPage, num_splitPage);
		return "alarmList";
	}

	public String getIdsarmInfoListExcel()
	{
		this.setTime();
		alarmList = bio.getIdsarmInfoListExcel(startOpenDate1, endOpenDate1, alarm_type,
				alarm_code, alarm_count, is_send_sheet, is_pre_release, is_release,
				device_id, loid,city_id);
		String excelCol = "city_name#loid#device_serialnumber#alarm_type#alarm_name#alarm_code#alarm_content#first_up_time#last_up_time#release_time#duration_time#olt_name#olt_ip#pon_intf";
		String excelTitle = "区域#LOID#设备标识#告警类别#告警名称#告警代码#告警信息#告警开始时间#最近一次上报时间#告警解除时间#告警历时#OLT名称#OLTIP#PON口";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "预检预修告警信息";
		data = alarmList;
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
		// TODO Auto-generated method stub
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		this.request = request;
	}

	public String getAlarm_type()
	{
		return alarm_type;
	}

	public void setAlarm_type(String alarm_type)
	{
		this.alarm_type = alarm_type;
	}

	public String getAlarm_code()
	{
		return alarm_code;
	}

	public void setAlarm_code(String alarm_code)
	{
		this.alarm_code = alarm_code;
	}

	public String getIs_send_sheet()
	{
		return is_send_sheet;
	}

	public void setIs_send_sheet(String is_send_sheet)
	{
		this.is_send_sheet = is_send_sheet;
	}

	public String getIs_pre_release()
	{
		return is_pre_release;
	}

	public void setIs_pre_release(String is_pre_release)
	{
		this.is_pre_release = is_pre_release;
	}

	public String getIs_release()
	{
		return is_release;
	}

	public void setIs_release(String is_release)
	{
		this.is_release = is_release;
	}

	public String getDevice_id()
	{
		return device_id;
	}

	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	public String getLoid()
	{
		return loid;
	}

	public void setLoid(String loid)
	{
		this.loid = loid;
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

	public List<Map> getAlarmList()
	{
		return alarmList;
	}

	public void setAlarmList(List<Map> alarmList)
	{
		this.alarmList = alarmList;
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

	public IdsAlarmInfoBIO getBio()
	{
		return bio;
	}

	public void setBio(IdsAlarmInfoBIO bio)
	{
		this.bio = bio;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getAlarm_count()
	{
		return alarm_count;
	}

	public void setAlarm_count(String alarm_count)
	{
		this.alarm_count = alarm_count;
	}

	
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	
	public String getCity_id()
	{
		return city_id;
	}

	
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}
}
