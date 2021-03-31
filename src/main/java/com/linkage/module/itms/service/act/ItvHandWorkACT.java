
package com.linkage.module.itms.service.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.ItvHandWorkBIO;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-4-26
 * @category com.linkage.module.itms.service.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ItvHandWorkACT implements SessionAware
{

	public static Logger logger = LoggerFactory.getLogger(ItvHandWorkACT.class);
	@SuppressWarnings("rawtypes")
	private Map session;
	private List<Map<String, String>> cityList = null;
	private String ajax;
	private ItvHandWorkBIO bio;
	//处理时间
	private String starttime;
	//前台传过来的字符串
	private String itvInfo;
	public String init()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		DateTimeUtil dt = new DateTimeUtil();
		starttime = dt.getDate();
		dt = new DateTimeUtil(starttime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		starttime = dt.getLongDate();
		return "success";
	}

	public String doBusiness()
	{
		logger.warn("starttime=====>"+starttime);
		this.setTime();
		logger.warn("itvInfo====>"+itvInfo);
		logger.warn("starttime=====>"+starttime);
		ajax=bio.doBusiness(itvInfo, starttime);
		return "ajax";
	}
	
	/**
	 * 时间转化
	 * 
	 * @return
	 */
	private void setTime()
	{
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime = String.valueOf(dt.getLongDateChar());
		}
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public ItvHandWorkBIO getBio()
	{
		return bio;
	}

	public void setBio(ItvHandWorkBIO bio)
	{
		this.bio = bio;
	}

	
	public String getStarttime()
	{
		return starttime;
	}

	
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	
	public String getItvInfo()
	{
		return itvInfo;
	}

	
	public void setItvInfo(String itvInfo)
	{
		this.itvInfo = itvInfo;
	}
	
}
