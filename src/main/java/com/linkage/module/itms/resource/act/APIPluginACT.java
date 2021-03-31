
package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.resource.bio.APIPluginBIO;

import action.splitpage.splitPageAction;

/**
 * @author 岩 (Ailk No.)
 * @version 1.0
 * @since 2016-10-12
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class APIPluginACT extends splitPageAction implements SessionAware
{

	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(APIPluginACT.class);
	// /** mq集合 */
	// private List<HashMap<String, String>> mqList = new ArrayList<HashMap<String,
	// String>>();
	// /** mqMap */
	// private static Map<String, String> mqMap = new HashMap<String, String>();
	/** classifyId标识 */
	private String classifyId;
	/** 开始时间 */
	private String starttime;
	/** 结束时间 */
	private String endtime;
	/** 采集时间 */
	private String gathertime;
	/** 分类名称 */
	private String classifyName;
	/** 分类描述 */
	private String classifyDesc;
	/** 创建者 */
	private String creator;
	/** 分类状态 */
	private String status;
	private String ajax;
	/** 查询结果mqList */
	@SuppressWarnings("rawtypes")
	private List<Map> pluginList = null;
	@SuppressWarnings("rawtypes")
	private Map session;
	private APIPluginBIO bio;
	/** 查询总数 */
	private int queryCount;

	/**
	 * 页面初始化
	 * 
	 * @author 岩
	 * @date 2016-10-13
	 * @return
	 */
	public String init()
	{
		logger.debug("APIPluginACT ==> init");
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		logger.warn(starttime + "------------");
		logger.warn(endtime + "------------");
		return "init";
	}

	/**
	 * 获取mqList并分页
	 * 
	 * @author 岩
	 * @date 2016-10-13
	 * @return
	 */
	public String getApiPluginList()
	{
		logger.debug("DevVerificationDisListACT ==> queryDevVerification()");
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		pluginList = bio.getApiPluginList(classifyName, creator, starttime, endtime,
				status, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countApiPluginList(classifyName, creator, starttime,
				endtime, status, curPage_splitPage, num_splitPage);
		queryCount = bio.getQueryCount();
		logger.warn(pluginList.toString() + "xxxx");
		return "list";
	}

	public String addApiPlugin()
	{
		try
		{
			DateTimeUtil dt = new DateTimeUtil();
			long addTime = dt.getLongTime();
			classifyId = Long.toString(addTime);
			creator = "admin";
			bio.addApiPlugin(classifyId, classifyName, classifyDesc, addTime, creator,
					status);
			ajax = "1";
		}
		catch (Exception e)
		{
			ajax = "-1";
		}
		return "ajax";
	}

	public String update(){
		return "update";
	}
	
	public String updateApiPlugin()
	{
		try
		{
			logger.warn("修改1" + classifyId + classifyName);
			bio.updateApiPlugin(classifyId, classifyName, status, classifyDesc);
			logger.warn("修改2");
			ajax = "1";
		}
		catch (Exception e)
		{
			ajax = "-1";
		}
		return "ajax";
	}

	public String deleteApiPlugin()
	{
		logger.debug("deleteApiPlugin()");
		// 可以删除
		try
		{
			bio.deleteApiPlugin(classifyId);
			ajax = "1";
		}
		catch (Exception e)
		{
			ajax = "-1";
		}
		return "ajax";
	}

	/**
	 * 时间转化
	 */
	private String setTime(String time)
	{
		logger.debug("setTime()" + time);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (!StringUtil.IsEmpty(time))
		{
			dt = new DateTimeUtil(time);
			time = StringUtil.getStringValue(dt.getLongTime());
			return time;
		}
		else
		{
			return "";
		}
	}

	public APIPluginBIO getBio()
	{
		return bio;
	}

	public void setBio(APIPluginBIO bio)
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

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	@SuppressWarnings("rawtypes")
	public Map getSession()
	{
		return session;
	}

	@SuppressWarnings("rawtypes")
	public void setSession(Map session)
	{
		this.session = session;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public int getQueryCount()
	{
		return queryCount;
	}

	public void setQueryCount(int queryCount)
	{
		this.queryCount = queryCount;
	}

	public String getGathertime()
	{
		return gathertime;
	}

	public void setGathertime(String gathertime)
	{
		this.gathertime = gathertime;
	}

	public String getClassifyId()
	{
		return classifyId;
	}

	public void setClassifyId(String classifyId)
	{
		this.classifyId = classifyId;
	}

	public String getClassifyName()
	{
		return classifyName;
	}

	public void setClassifyName(String classifyName)
	{
		this.classifyName = classifyName;
	}

	public String getClassifyDesc()
	{
		return classifyDesc;
	}

	public void setClassifyDesc(String classifyDesc)
	{
		this.classifyDesc = classifyDesc;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getPluginList()
	{
		return pluginList;
	}

	public void setPluginList(@SuppressWarnings("rawtypes") List<Map> pluginList)
	{
		this.pluginList = pluginList;
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
