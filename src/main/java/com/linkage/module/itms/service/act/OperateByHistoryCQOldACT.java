
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
import com.linkage.module.itms.service.bio.OperateByHistoryCQOldBIO;

public class OperateByHistoryCQOldACT extends splitPageAction implements ServletRequestAware,
		SessionAware
{

	private static Logger logger = LoggerFactory.getLogger(OperateByHistoryCQOldACT.class);
	private String content = "";
	private String ajax = "";
	private String ACCOUNT_NAME = "";
	
	private String isHistory = "";
	
	private String SERIAL_NUMBER = "";
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
	private OperateByHistoryCQOldBIO bio;
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
		endOpenDate = getEndDate();
		startOpenDate = getStartDate();
		
		return "init";
	}
	
	public String send(){
		logger.warn("send begin........");
		ajax = bio.send(content,bss_sheet_id,isHistory);
		return "ajax";
	}

	public String getOperateByHistoryInfo()
	{
		logger.debug("getOperateByHandInfo()");
		if (!StringUtil.IsEmpty(username))
		{
			username = username.trim();
		}
		if (!StringUtil.IsEmpty(ACCOUNT_NAME))
		{
			ACCOUNT_NAME = ACCOUNT_NAME.trim();
		}
		if (!StringUtil.IsEmpty(SERIAL_NUMBER))
		{
			SERIAL_NUMBER = SERIAL_NUMBER.trim();
		}
		instAreaName = Global.instAreaShortName;
		curPage_splitPage=1;
		num_splitPage=1000;//当成不分页去做
		dataList = bio.getOperateByHistoryInfo(startOpenDate, endOpenDate, username,
				ACCOUNT_NAME, SERIAL_NUMBER, curPage_splitPage, num_splitPage);
		/*maxPage_splitPage = bio.countOperateByHistoryInfo(startOpenDate, endOpenDate,
				username, ACCOUNT_NAME, SERIAL_NUMBER,curPage_splitPage, num_splitPage);*/
		return "list";
	}

	public String getOperateMessage()
	{
		logger.debug("getOperateMessage()");
		operateMap = bio.getOperateMessage(bss_sheet_id,isHistory);
		operateMap.put("isHistory", isHistory);
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

	// 当前年的1月1号
		private String getStartDate()
		{
			DateTimeUtil now = new DateTimeUtil(endOpenDate);
			int month = now.getMonth()-1;
			if(month==0){
				month = 12;
			}
			String monthStr = StringUtil.getStringValue(month);
			if(monthStr.length()==1){
				monthStr = "0"+monthStr;
			}
			String before = endOpenDate.substring(0, 5);
			String end = endOpenDate.substring(7);
			logger.warn("startdate:"+before +monthStr+ end);
			return before +monthStr+ end;
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

	public OperateByHistoryCQOldBIO getBio()
	{
		return bio;
	}

	public void setBio(OperateByHistoryCQOldBIO bio)
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

	
	public String getACCOUNT_NAME()
	{
		return ACCOUNT_NAME;
	}

	
	public void setACCOUNT_NAME(String aCCOUNT_NAME)
	{
		ACCOUNT_NAME = aCCOUNT_NAME;
	}

	
	public String getSERIAL_NUMBER()
	{
		return SERIAL_NUMBER;
	}

	
	public void setSERIAL_NUMBER(String sERIAL_NUMBER)
	{
		SERIAL_NUMBER = sERIAL_NUMBER;
	}

	
	public String getContent()
	{
		return content;
	}

	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String getAjax()
	  {
	    return this.ajax;
	  }

	  public void setAjax(String ajax)
	  {
	    this.ajax = ajax;
	  }

	
	public String getIsHistory()
	{
		return isHistory;
	}

	
	public void setIsHistory(String isHistory)
	{
		this.isHistory = isHistory;
	}
}
