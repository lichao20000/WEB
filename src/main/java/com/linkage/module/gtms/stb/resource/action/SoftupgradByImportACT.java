
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.stb.resource.serv.SoftupgradByImportBIO;

/**
 * @author yinlei3 (73167.)
 * @version 1.0
 * @since 2016年3月8日
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SoftupgradByImportACT extends splitPageAction
{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(SoftupgradByImportACT.class);
	/** 文件名 */
	private String gwShare_fileName = null;
	/** 策略方式 */
	private String softStrategy_type = null;
	/** 目标版本 */
	private String div_goal_softwareversion = null;
	/** bio */
	private SoftupgradByImportBIO bio;
	/** ajax */
	private String ajax = null;
	/** 版本列表 */
	@SuppressWarnings("rawtypes")
	private List versionpathList = null;

	/**
	 * 页面初始化
	 */
	public String init()
	{
		logger.debug("init");
		// 获取厂商集合
		versionpathList = bio.getVersionPathList();
		return "init";
	}

	/**
	 * 分析文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String anlazye() throws Exception
	{
		if (null != gwShare_fileName)
		{
			gwShare_fileName.trim();
		}
		ajax = bio.makeStrategy(gwShare_fileName, softStrategy_type,
				div_goal_softwareversion);
		return "ajax";
	}

	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	public String getSoftStrategy_type()
	{
		return softStrategy_type;
	}

	public String getDiv_goal_softwareversion()
	{
		return div_goal_softwareversion;
	}

	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	public void setSoftStrategy_type(String softStrategy_type)
	{
		this.softStrategy_type = softStrategy_type;
	}

	public void setDiv_goal_softwareversion(String div_goal_softwareversion)
	{
		this.div_goal_softwareversion = div_goal_softwareversion;
	}

	public void setBio(SoftupgradByImportBIO bio)
	{
		this.bio = bio;
	}

	public String getAjax()
	{
		return ajax;
	}

	@SuppressWarnings("rawtypes")
	public List getVersionpathList()
	{
		return versionpathList;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	@SuppressWarnings("rawtypes")
	public void setVersionpathList(List versionpathList)
	{
		this.versionpathList = versionpathList;
	}
}
