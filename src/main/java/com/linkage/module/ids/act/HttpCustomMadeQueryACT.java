
package com.linkage.module.ids.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.ids.bio.HttpCustomMadeQueryBIO;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年6月12日
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class HttpCustomMadeQueryACT extends splitPageAction
{

	/** 日志 */
	private static Logger logger = LoggerFactory
			.getLogger(HttpCustomMadeQueryACT.class);
	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** HTTP定制查询用service */
	private HttpCustomMadeQueryBIO bio;
	/** 开始时间 */
	private String starttime;
	private String starttime1;
	/** 结束时间 */
	private String endtime;
	private String endtime1;
	/** 定制人 */
	private String acc_loginname;
	/** 任务名称 */
	private String taskname;
	/** 任务Id */
	private String taskId;
	/** 查询结果集合 */
	@SuppressWarnings("rawtypes")
	private List<Map> list;
	/** 初始化结果 */
	private final String INIT_RESULT = "taskInfo";
	/** HTTP定制查询列表结果 */
	private final String TASKLIST_RESULT = "taskList";
	/** HTTP定制详细查询列表结果 */
	private final String DEVLIST_RESULT = "devList";

	/**
	 * 初始化查询页面
	 * 
	 * @return 初始化结果
	 */
	public String init()
	{
		logger.debug("HttpCustomMadeQueryACT=>init()");
		starttime = getStartTempTime();
		endtime = getEndTempTime();
		return INIT_RESULT;
	}

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	public String queryTask()
	{
		logger.debug("HttpCustomMadeQueryACT=>queryTask()");
		this.setTime();
		list = bio.queryTask(taskname, acc_loginname, starttime1, endtime1,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryTaskCount(taskname, acc_loginname, starttime1,
				endtime1, curPage_splitPage, num_splitPage);
		return TASKLIST_RESULT;
	}

	/**
	 * 获取设备列表
	 * 
	 * @return
	 */
	public String getTaskDevList()
	{
		logger.debug("HttpCustomMadeQueryACT=>getTaskDevList()");
		list = bio.getTaskDevList(taskId, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getTaskCount(taskId, curPage_splitPage, num_splitPage);
		return DEVLIST_RESULT;
	}

	private String getStartTempTime()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	private String getEndTempTime()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}

	public String getStarttime()
	{
		return starttime;
	}

	public String getStarttime1()
	{
		return starttime1;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public String getEndtime1()
	{
		return endtime1;
	}

	public String getAcc_loginname()
	{
		return acc_loginname;
	}

	public String getTaskname()
	{
		return taskname;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getList()
	{
		return list;
	}

	public void setBio(HttpCustomMadeQueryBIO bio)
	{
		this.bio = bio;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public void setStarttime1(String starttime1)
	{
		this.starttime1 = starttime1;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public void setEndtime1(String endtime1)
	{
		this.endtime1 = endtime1;
	}

	public void setAcc_loginname(String acc_loginname)
	{
		this.acc_loginname = acc_loginname;
	}

	public void setTaskname(String taskname)
	{
		this.taskname = taskname;
	}

	@SuppressWarnings("rawtypes")
	public void setList(List<Map> list)
	{
		this.list = list;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
}
