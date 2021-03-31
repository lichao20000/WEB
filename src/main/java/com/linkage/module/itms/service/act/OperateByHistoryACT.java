
package com.linkage.module.itms.service.act;

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

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.OperateByHistoryBIO;

public class OperateByHistoryACT extends splitPageAction implements ServletRequestAware,
		SessionAware
{

	private static Logger logger = LoggerFactory.getLogger(OperateByHistoryACT.class);
	// 开始时间
	private String startOpenDate = "";
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// loid
	private String username;
	// 属地
	private String city_id = null;
	// 业务类型
	private String servType = null;
	// 操作类型
	private String resultType = null;
	// 开通状态
	private String resultId = null;
	// 是否过滤资料工单      1:代表否     0：代表是
	private String isGl = null;
	// 账号类型
	private String usernameType;
	// session
	private Map session = null;
	private HttpServletRequest request;
	// 1 家庭网关，2 企业网关
	private String gw_type;
	// 属地列表
	private List<Map<String, String>> cityList = null;
	// 列表数据
	private List<Map> dataList;
	private String bss_sheet_id = null;
	private String receive_date="";
	// 工单页面数据
	private Map<String, String> operateMap = null;
	private OperateByHistoryBIO bio;
	// 地区(例如江苏 ，吉林)
	//@Deprecated 页面请使用ms:inArea标签
	private String instAreaName;

	public String init()
	{
		logger.debug("init()");
		instAreaName = Global.instAreaShortName;
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}
	
	public String initStb()
	{
		logger.debug("init()");
		instAreaName = Global.instAreaShortName;
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "initStb";
	}

	public String getOperateByHistoryInfo()
	{
		logger.debug("getOperateByHandInfo()");
		// 如果在表单中提交了username这个参数，则在后台将开始时间结束时间置空
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		if (!StringUtil.IsEmpty(username))
		{
			username = username.trim();
			startOpenDate1 = "";
			endOpenDate1 = "";
		}
		instAreaName = Global.instAreaShortName;
		dataList = bio.getOperateByHistoryInfo(startOpenDate1, endOpenDate1, username,
				city_id, servType, resultType, resultId, usernameType, gw_type,isGl,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countOperateByHistoryInfo(startOpenDate1, endOpenDate1,
				username, city_id, servType, resultType, resultId, usernameType, gw_type,isGl,
				curPage_splitPage, num_splitPage);
		return "list";
	}
	
	public String getOperateByHistoryInfoStb()
	{
		logger.debug("getOperateByHandInfo()");
		// 如果在表单中提交了username这个参数，则在后台将开始时间结束时间置空
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		if (!StringUtil.IsEmpty(username))
		{
			username = username.trim();
			startOpenDate1 = "";
			endOpenDate1 = "";
		}
		instAreaName = Global.instAreaShortName;
		dataList = bio.getOperateByHistoryInfoStb(startOpenDate1, endOpenDate1, username,
				city_id, servType, resultType, resultId, usernameType, gw_type,isGl,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countOperateByHistoryInfoStb(startOpenDate1, endOpenDate1,
				username, city_id, servType, resultType, resultId, usernameType, gw_type,isGl,
				curPage_splitPage, num_splitPage);
		return "listStb";
	}

	public String getOperateMessage()
	{
		logger.debug("getOperateMessage()");
		operateMap = bio.getOperateMessage(bss_sheet_id,receive_date, gw_type);
		return "message";
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
	
	public static void main(String[] args)
	{
		System.out.println(new DateTimeUtil().getMonth());
	}

	// 当前年的1月1号
	private String getStartDate()
	{
		String time = "";
		instAreaName = Global.instAreaShortName;
		if(Global.CQDX.equals(instAreaName)){
			GregorianCalendar now = new GregorianCalendar();
			SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			time = fmtrq.format(now.getTime());
		}
		if(Global.SXLT.equals(instAreaName)){
			GregorianCalendar now = new GregorianCalendar();
			SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			now.set(GregorianCalendar.DATE, new DateTimeUtil().getDay());
			now.set(GregorianCalendar.MONTH, new DateTimeUtil().getMonth()==1?0:new DateTimeUtil().getMonth()-2);
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			time = fmtrq.format(now.getTime());
		}
		else{
			GregorianCalendar now = new GregorianCalendar();
			SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			now.set(GregorianCalendar.DATE, 1);
			now.set(GregorianCalendar.MONTH, 0);
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			time = fmtrq.format(now.getTime());
		}
		
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

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest req)
	{
		this.request = req;
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

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public String getServType()
	{
		return servType;
	}

	public void setServType(String servType)
	{
		this.servType = servType;
	}

	public String getResultType()
	{
		return resultType;
	}

	public void setResultType(String resultType)
	{
		this.resultType = resultType;
	}

	public String getResultId()
	{
		return resultId;
	}

	public void setResultId(String resultId)
	{
		this.resultId = resultId;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public String getUsernameType()
	{
		return usernameType;
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	public void setUsernameType(String usernameType)
	{
		this.usernameType = usernameType;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public OperateByHistoryBIO getBio()
	{
		return bio;
	}

	public void setBio(OperateByHistoryBIO bio)
	{
		this.bio = bio;
	}

	public String getBss_sheet_id()
	{
		return bss_sheet_id;
	}

	public void setBss_sheet_id(String bss_sheet_id)
	{
		this.bss_sheet_id = bss_sheet_id;
	}

	public Map<String, String> getOperateMap()
	{
		return operateMap;
	}

	public void setOperateMap(Map<String, String> operateMap)
	{
		this.operateMap = operateMap;
	}

	public List<Map> getDataList()
	{
		return dataList;
	}

	public void setDataList(List<Map> dataList)
	{
		this.dataList = dataList;
	}

	public String getIsGl()
	{
		return isGl;
	}

	public void setIsGl(String isGl)
	{
		this.isGl = isGl;
	}

	
	public String getInstAreaName()
	{
		return instAreaName;
	}

	
	public void setInstAreaName(String instAreaName)
	{
		this.instAreaName = instAreaName;
	}

	
	public String getReceive_date()
	{
		return receive_date;
	}

	
	public void setReceive_date(String receive_date)
	{
		this.receive_date = receive_date;
	}
}
