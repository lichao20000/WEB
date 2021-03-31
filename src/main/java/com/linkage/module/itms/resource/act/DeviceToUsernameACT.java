
package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.resource.bio.DeviceToUsernameBIO;

public class DeviceToUsernameACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(DeviceToUsernameACT.class);
	// session
	private Map session;
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private String deviceId;
	private String deviceSn;
	private String oui;
	private List<Map> hgwList = null;
	private DeviceToUsernameBIO bio;

	public String init()
	{
		DateTimeUtil dt = new DateTimeUtil();
//		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
//		dt = new DateTimeUtil(endtime);
//		long end_time = dt.getLongTime();
//		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
//		endtime = dt.getLongDate();
//		dt = new DateTimeUtil(starttime);
//		starttime = dt.getLongDate();
		return "init";
	}

	public String execute()
	{
		logger.debug("execute()");
		this.setTime();
		DateTimeUtil dt = new DateTimeUtil(starttime);
		String tabName = "dev_inform_user_log_"+dt.getShortDate();
		int ret = bio.isHaveTable(tabName);
		if(ret==1){
			hgwList = bio.getHgwByDeviceList(deviceId, deviceSn, oui,
					curPage_splitPage, num_splitPage,tabName);
			maxPage_splitPage = bio.getHgwByDeviceCount(deviceId,
					curPage_splitPage, num_splitPage,tabName);
		}
		return "list";
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
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the starttime
	 */
	public String getStarttime()
	{
		return starttime;
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
	 * @return the starttime1
	 */
	public String getStarttime1()
	{
		return starttime1;
	}

	/**
	 * @param starttime1
	 *            the starttime1 to set
	 */
	public void setStarttime1(String starttime1)
	{
		this.starttime1 = starttime1;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime()
	{
		return endtime;
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
	 * @return the endtime1
	 */
	public String getEndtime1()
	{
		return endtime1;
	}

	/**
	 * @param endtime1
	 *            the endtime1 to set
	 */
	public void setEndtime1(String endtime1)
	{
		this.endtime1 = endtime1;
	}

	/**
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	/**
	 * @return the hgwList
	 */
	public List<Map> getHgwList()
	{
		return hgwList;
	}

	/**
	 * @param hgwList
	 *            the hgwList to set
	 */
	public void setHgwList(List<Map> hgwList)
	{
		this.hgwList = hgwList;
	}

	/**
	 * @return the bio
	 */
	public DeviceToUsernameBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(DeviceToUsernameBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the deviceSn
	 */
	public String getDeviceSn()
	{
		return deviceSn;
	}

	/**
	 * @param deviceSn
	 *            the deviceSn to set
	 */
	public void setDeviceSn(String deviceSn)
	{
		this.deviceSn = deviceSn;
	}

	/**
	 * @return the oui
	 */
	public String getOui()
	{
		return oui;
	}

	/**
	 * @param oui
	 *            the oui to set
	 */
	public void setOui(String oui)
	{
		this.oui = oui;
	}
}
