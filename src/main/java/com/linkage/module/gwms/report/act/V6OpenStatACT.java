package com.linkage.module.gwms.report.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.report.bio.V6OpenStatBIO;

import action.splitpage.splitPageAction;



public class V6OpenStatACT extends splitPageAction implements
		ServletRequestAware {

	private static final long serialVersionUID = 1891651756165L;
	Logger logger = LoggerFactory.getLogger(V6OpenStatACT.class);

	private V6OpenStatBIO bio;
	private HttpServletRequest request;
	
	//结果
	private List<Map> statList = null;
	
	//导出
	private String fileName = "";
	private String[] title ;
	private String[] column ;
	private List<Map> data;
 
	
	 
	public String init(){
		setStatList(bio.stat(curPage_splitPage, num_splitPage));
		maxPage_splitPage = bio.count(curPage_splitPage, num_splitPage);
		return "init";
	}
	
	public String toExcel(){
		fileName = "IPV6开通情况";
		title = new String[] { "厂家", "型号","网关类型", "是否支持IPV6", "绑定数量","成功数量",  "失败数量","最近一月未上线数量"};
		column = new String[] { "vendor_name", "device_model", "gateway_type","is_v6", "bindnum", "succnum","failnum","notnum"};
		data = bio.toExcel();
		return "excel";
	}


	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		 
		
	}
	
	public V6OpenStatBIO getBio()
	{
		return bio;
	}


	
	public void setBio(V6OpenStatBIO bio)
	{
		this.bio = bio;
	}


	
	public HttpServletRequest getRequest()
	{
		return request;
	}


	
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String[] getTitle()
	{
		return title;
	}


	
	public void setTitle(String[] title)
	{
		this.title = title;
	}


	
	public String[] getColumn()
	{
		return column;
	}


	
	public void setColumn(String[] column)
	{
		this.column = column;
	}


	
	public List<Map> getData()
	{
		return data;
	}


	
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public List<Map> getStatList()
	{
		return statList;
	}

	public void setStatList(List<Map> statList)
	{
		this.statList = statList;
	}
	
}
