
package com.linkage.module.bbms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.bio.EvdoPercentBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author 王森博 统计EVDO网关占比
 */
public class EvdoPercentACT extends ActionSupport implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(EvdoPercentACT.class);
	// BIO
	private EvdoPercentBIO evdoPercentBio;
	// session
	private Map session;
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	/** 按本地网 按行业 */
	private String type = "";

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date Nov 19, 2009
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		UserRes curUser = (UserRes) session.get("curUser");
		this.setTime();
		type = "1";
		data = evdoPercentBio.getEvdoPercent(type, starttime1, endtime1, curUser);
		return "list";
	}

	/**
	 * 获取统计列表
	 * 
	 * @author wangsenbo
	 * @date Nov 19, 2009
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		this.setTime();
		data = evdoPercentBio.getEvdoPercent(type, starttime1, endtime1, curUser);
		return "list";
	}

	/**
	 * 获取统计列表,导出excel
	 * 
	 * @author wangsenbo
	 * @date Nov 19, 2009
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		UserRes curUser = (UserRes) session.get("curUser");
		fileName = "EVDO";
		this.setTime();
		title = evdoPercentBio.getTitle(type);
		column = evdoPercentBio.getColumn(type);
		data = evdoPercentBio.getEvdoPercent(type, starttime1, endtime1, curUser);
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + starttime);
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

	/**
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @return the starttime
	 */
	public String getStarttime()
	{
		return starttime;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime()
	{
		return endtime;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param evdoPercentBio
	 *            the evdoPercentBio to set
	 */
	public void setEvdoPercentBio(EvdoPercentBIO evdoPercentBio)
	{
		this.evdoPercentBio = evdoPercentBio;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	/**
	 * @param endtime
	 *            the endtime to set
	 */
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}
}
