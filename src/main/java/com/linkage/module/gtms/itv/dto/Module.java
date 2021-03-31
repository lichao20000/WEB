package com.linkage.module.gtms.itv.dto;

/**
 * 模块信息
 * 
 * @author 陈仲民(5243)
 * @version 1.0
 * @since 2009-12-22
 * @category com.linkage.module.fba.menu.dto<br>
 *           版权：南京联创科技 网管科技部
 */
public class Module
{
	
	/**
	 * 模块名称
	 */
	private String moduleName = "";
	
	/**
	 * 模块编号
	 */
	private String moduleId = "";
	
	/**
	 * 模块首页
	 */
	private String moduleUrl = "";
	
	/**
	 * 参数信息
	 */
	private String urlParam = "";
	
	public String getModuleName()
	{
		return moduleName;
	}
	
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}
	
	public String getModuleId()
	{
		return moduleId;
	}
	
	public void setModuleId(String moduleId)
	{
		this.moduleId = moduleId;
	}
	
	public String getUrlParam()
	{
		return urlParam;
	}
	
	public void setUrlParam(String urlParam)
	{
		this.urlParam = urlParam;
	}
	
	public String getModuleUrl()
	{
		return moduleUrl;
	}
	
	public void setModuleUrl(String moduleUrl)
	{
		this.moduleUrl = moduleUrl;
	}
	
}
