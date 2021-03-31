
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
import com.linkage.module.itms.resource.bio.LogSuperManageBIO;

public class LogSuperManageACT extends splitPageAction implements ServletRequestAware,
		SessionAware
{

	private static final long serialVersionUID = 723576970339799538L;
	private static Logger logger = LoggerFactory.getLogger(LogSuperManageACT.class);
	private HttpServletRequest request;
	private Map session;
	private String auth_name;
	private String user_name;
	// 操作开始时间
	private String startOpenDate = "";
	// 操作开始时间
	private String startOpenDate1 = "";
	// 操作结束时间
	private String endOpenDate = "";
	// 操作结束时间
	private String endOpenDate1 = "";
	private List<Map> logList;
	private LogSuperManageBIO bio;
	
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	/**
	 * 超级权限简码
	 */
	private String authCode;
	/**
	 * 超级权限操作描述
	 */
	private String operDesc;
	private String ajax;

	public String execute()
	{
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}

	public String getLogInfo()
	{
		logger.debug("LogSuperManageACT->getLogInfo");
		this.setTime();
		logList = bio.getLogInfo(auth_name, user_name, startOpenDate1, endOpenDate1,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countLogInfo(auth_name, user_name, startOpenDate1,
				endOpenDate1, curPage_splitPage, num_splitPage);
		return "list";
	}
	
	public String excelLogInfo(){
		
		logger.debug("LogSuperManageACT->excelLogInfo()");
		this.setTime();
		logList = bio.excelLogInfo(auth_name, user_name, startOpenDate1, endOpenDate1);
		String excelCol = "authName#userName#operDesc#operTime";
		String excelTitle = "权限名称#操作人#操作内容#操作时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "logList";
		data = logList;
		return "excel";
	}

	/**
	 * 记录超级权限操作日志
	 * 
	 * @return
	 */
	public String addSuperAuthLog()
	{
		bio.addSuperAuthLog(authCode, operDesc);
		// 该方法不需要ajax返回参数，但是在AJAXResult类中做了null校验，所以该处赋空字符串
		ajax = "";
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

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public String getAuth_name()
	{
		return auth_name;
	}

	public LogSuperManageBIO getBio()
	{
		return bio;
	}

	public void setBio(LogSuperManageBIO bio)
	{
		this.bio = bio;
	}

	public void setAuth_name(String auth_name)
	{
		this.auth_name = auth_name;
	}

	public String getUser_name()
	{
		return user_name;
	}

	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
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

	public List<Map> getLogList()
	{
		return logList;
	}

	public void setLogList(List<Map> logList)
	{
		this.logList = logList;
	}

	public String getAuthCode()
	{
		return authCode;
	}

	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}

	public String getOperDesc()
	{
		return operDesc;
	}

	public void setOperDesc(String operDesc)
	{
		this.operDesc = operDesc;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
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
}
