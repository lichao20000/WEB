
package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.resource.bio.HgwByMacBIO;

public class HgwByMacACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(HgwByMacACT.class);
	// session
	private Map session;
	private HgwByMacBIO hgwByMacBio;
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	private String username;
	private String mac;
	private List hgwList = new ArrayList();
	private String queryFlag = null;

	public String execute()
	{
		logger.debug("execute()");
		this.setTime();
		hgwList = hgwByMacBio.getHgwByMacList(starttime1, endtime1, username, mac,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = hgwByMacBio.getHgwByMacCount(starttime1, endtime1, username,
				mac, curPage_splitPage, num_splitPage);
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
	 * @return the hgwByMacBio
	 */
	public HgwByMacBIO getHgwByMacBio()
	{
		return hgwByMacBio;
	}

	/**
	 * @param hgwByMacBio
	 *            the hgwByMacBio to set
	 */
	public void setHgwByMacBio(HgwByMacBIO hgwByMacBio)
	{
		this.hgwByMacBio = hgwByMacBio;
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
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the mac
	 */
	public String getMac()
	{
		return mac;
	}

	/**
	 * @param mac
	 *            the mac to set
	 */
	public void setMac(String mac)
	{
		this.mac = mac;
	}

	/**
	 * @return the hgwList
	 */
	public List getHgwList()
	{
		return hgwList;
	}

	/**
	 * @param hgwList
	 *            the hgwList to set
	 */
	public void setHgwList(List hgwList)
	{
		this.hgwList = hgwList;
	}

	
	/**
	 * @return the queryFlag
	 */
	public String getQueryFlag()
	{
		return queryFlag;
	}

	
	/**
	 * @param queryFlag the queryFlag to set
	 */
	public void setQueryFlag(String queryFlag)
	{
		this.queryFlag = queryFlag;
	}
	
}
