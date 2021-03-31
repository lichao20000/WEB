
package com.linkage.module.gwms.resource.act;


import java.util.List;
import java.util.Map;




import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import action.splitpage.splitPageAction;

import com.linkage.module.gwms.resource.bio.RouteInfoBIO;
import com.linkage.module.gwms.resource.obj.RouteInfoBean;

/**
 * 软件升级
 * 
 * @author 王森博
 */
public class RouteInfoACT extends splitPageAction implements SessionAware
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(RouteInfoACT.class);

	private String ajax;
	private List<Map> routeInfoList;
	private RouteInfoBIO bio;
	private RouteInfoBean routeBean;
	private List<Map> data;
	private String fileName;
	private String[] column;
	private String[] title;
	// session  
	private Map session;

	public String queryRouteInfo(){
		routeInfoList = bio.queryRouteInfo(curPage_splitPage, num_splitPage, routeBean);
		maxPage_splitPage = bio.queryRouteInfoCount(curPage_splitPage, num_splitPage, routeBean);
		return "showList";
	}
	public String toExcel()
	{
		logger.debug("into toExcel()");
		routeInfoList = this.bio.queryRouteInfoForExcel(routeBean);
		this.column = new String[] { "cityName", "city_name", "vendor_add", "device_model", "softwareversion", "hardwareversion", "device_serialnumber", "loid", "kdname" };
		this.title = new String[] { "属地", "区县", "厂家", "型号", "软件版本", "硬件版本", "设备序列号", "逻辑ID", "宽带账号" };
		this.fileName = "RouteInfoExcel";
		this.data = routeInfoList;
		return "excel";
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public RouteInfoBIO getBio()
	{
		return bio;
	}

	public void setBio(RouteInfoBIO bio)
	{
		this.bio = bio;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}


	public RouteInfoBean getRouteBean()
	{
		return routeBean;
	}


	public void setRouteBean(RouteInfoBean routeBean)
	{
		this.routeBean = routeBean;
	}


	public List<Map> getRouteInfoList()
	{
		return routeInfoList;
	}


	public void setRouteInfoList(List<Map> routeInfoList)
	{
		this.routeInfoList = routeInfoList;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
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

}
