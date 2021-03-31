
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

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.UserSpecRealInfoBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-8-19
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class UserSpecRealInfoACT extends ActionSupport implements ServletRequestAware,
		SessionAware
{

	private static Logger logger = LoggerFactory
			.getLogger(UserSpecRealInfoACT.class);
	private UserSpecRealInfoBIO bio;
	private HttpServletRequest request;
	private Map session;
	// 开始时间
	private String startOpenDate = "";
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// 属地
	private String city_id;
	// 属地列表
	private List<Map<String, String>> cityList = null;
	/** 终端类型 1:e8-b 2:e8-c 0:all */
	private String devicetype;
	/**
	 * 客户类型：2：家庭用户，其他政企类型
	 */
	private String cust_type_id;
	/**
	 * 用户实际终端类型
	 */
	private String spec_id;
	// 查询数据列表
	private List<Map> userSpecList;
	
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

	public String getUserSpecRealInfo()
	{
		this.setTime();
		userSpecList = bio.getBRealInfo(startOpenDate1, endOpenDate1, city_id,
				devicetype, spec_id);
		return "list";
	}
	
	/**
	 * 获取列表数据
	 * @return
	 */
	public String getTabBssDevPort(){
		ajax = bio.getTabBssDevPort(cust_type_id);
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

	public UserSpecRealInfoBIO getBio()
	{
		return bio;
	}

	public void setBio(UserSpecRealInfoBIO bio)
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

	public String getDevicetype()
	{
		return devicetype;
	}

	public void setDevicetype(String devicetype)
	{
		this.devicetype = devicetype;
	}

	public String getCust_type_id()
	{
		return cust_type_id;
	}

	public void setCust_type_id(String cust_type_id)
	{
		this.cust_type_id = cust_type_id;
	}

	public String getSpec_id()
	{
		return spec_id;
	}

	public void setSpec_id(String spec_id)
	{
		this.spec_id = spec_id;
	}

	public List<Map> getUserSpecList()
	{
		return userSpecList;
	}

	public void setUserSpecList(List<Map> userSpecList)
	{
		this.userSpecList = userSpecList;
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
