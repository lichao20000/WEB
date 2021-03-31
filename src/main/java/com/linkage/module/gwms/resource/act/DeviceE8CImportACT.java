package com.linkage.module.gwms.resource.act;

import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.module.gwms.resource.bio.DeviceE8CImportBIO;
import com.opensymphony.xwork2.ActionSupport;


public class DeviceE8CImportACT extends ActionSupport implements SessionAware
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(DeviceE8CImportACT.class);
	
	private Map session;
	/** 文件名 */
	private String gwShare_fileName = null;
	private DeviceE8CImportBIO bio;
	private String ajax = null;

	/**
	 * 页面初始化
	 */
	public String init()
	{
		return "init";
	}

	/**
	 * 分析文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String analyse() throws Exception
	{
		logger.warn(gwShare_fileName);
		if (null != gwShare_fileName)
		{
			gwShare_fileName.trim();
		}
		ajax = bio.analyse(gwShare_fileName);
		return "ajax";
	}

	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getAjax()
	{
		return ajax;
	}


	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public Map getSession()
	{
		return session;
	}
	
	public void setSession(Map session)
	{
		this.session = session;
	}

	public DeviceE8CImportBIO getBio() {
		return bio;
	}

	public void setBio(DeviceE8CImportBIO bio) {
		this.bio = bio;
	}
}
