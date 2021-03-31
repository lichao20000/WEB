
package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.ErrorBssSheetBIO;

/**
 * @author zhangshimin(工号) Tel:��
 * @version 1.0
 * @since 2011-5-26 下午09:07:48
 * @category com.linkage.module.itms.service.act
 * @copyright 南京联创科技 网管科技部
 */
public class ErrorBssSheetACT extends splitPageAction implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// session
	private Map session;
	private String reciveDateStart;
	private String reciveDateEnd;
	private String cityId;
	private String username;
	private String sheetType;
	private String resultSheet;
	private ErrorBssSheetBIO bio;
	private List<Map<String, String>> errSheetList;
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;

	public String init()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		// DateTimeUtil dt = new DateTimeUtil();
		reciveDateStart = getStartDate();
		reciveDateEnd = getEndDate();
		return "init";
	}

	public String query()
	{
		DateTimeUtil dt = null;// 定义DateTimeUtil
		String startDate , endDate;
		if (reciveDateStart == null || "".equals(reciveDateStart))
		{
			startDate = null;
		}
		else
		{
			dt = new DateTimeUtil(reciveDateStart);
			startDate = String.valueOf(dt.getLongTime());
		}
		if (reciveDateEnd == null || "".equals(reciveDateEnd))
		{
			endDate = null;
		}
		else
		{
			dt = new DateTimeUtil(reciveDateEnd);
			endDate = String.valueOf(dt.getLongTime());
		}
		errSheetList = bio.queryErrSheet(startDate, endDate, cityId, username, sheetType,
				resultSheet, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "list";
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

	public String getReciveDateStart()
	{
		return reciveDateStart;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public void setReciveDateStart(String reciveDateStart)
	{
		this.reciveDateStart = reciveDateStart;
	}

	public String getReciveDateEnd()
	{
		return reciveDateEnd;
	}

	public void setReciveDateEnd(String reciveDateEnd)
	{
		this.reciveDateEnd = reciveDateEnd;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getSheetType()
	{
		return sheetType;
	}

	public void setSheetType(String sheetType)
	{
		this.sheetType = sheetType;
	}

	public String getResultSheet()
	{
		return resultSheet;
	}

	public void setResultSheet(String resultSheet)
	{
		this.resultSheet = resultSheet;
	}

	public ErrorBssSheetBIO getBio()
	{
		return bio;
	}

	public void setBio(ErrorBssSheetBIO bio)
	{
		this.bio = bio;
	}

	public List<Map<String, String>> getErrSheetList()
	{
		return errSheetList;
	}

	public void setErrSheetList(List<Map<String, String>> errSheetList)
	{
		this.errSheetList = errSheetList;
	}

	@Override
	public void setSession(Map session)
	{
		// TODO Auto-generated method stub
		this.session = session;
	}
}
