
package com.linkage.module.bbms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.bio.ServiceQueryBIO;

/**
 * 业务使用查询
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class ServiceQueryACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ServiceQueryACT.class);
	// session
	private Map session;
	/** 用户账号 */
	private String userName;
	/** 设备序列号 */
	private String device_serialnumber;
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private ServiceQueryBIO serviceQueryBio;
	/** 业务列表 */
	private List<Map<String, String>> serviceTypeList = null;
	/** 业务ID */
	private String serviceId = "-1";

	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date Mar 16, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		serviceTypeList = serviceQueryBio.getServiceTypeList();
		return "init";
	}

	/**
	 * 查询
	 * 
	 * @author wangsenbo
	 * @date Mar 16, 2010
	 * @param
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		data = serviceQueryBio.queryService(curUser.getCityId(), userName,
				device_serialnumber, serviceId);
		return "list";
	}

	/**
	 * 导出Excel
	 * 
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "业务使用情况";
		UserRes curUser = (UserRes) session.get("curUser");
		title = new String[] { "", "用户业务使用", "", "" };
		column = new String[] { "column1", "column2", "column3", "column4"};
		data = serviceQueryBio.serviceExcel(curUser.getCityId(), userName,
				device_serialnumber, serviceId);
		return "excel";
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
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	/**
	 * @return the device_serialnumber
	 */
	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	/**
	 * @param device_serialnumber
	 *            the device_serialnumber to set
	 */
	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	/**
	 * @return the serviceQueryBio
	 */
	public ServiceQueryBIO getServiceQueryBio()
	{
		return serviceQueryBio;
	}

	/**
	 * @param serviceQueryBio
	 *            the serviceQueryBio to set
	 */
	public void setServiceQueryBio(ServiceQueryBIO serviceQueryBio)
	{
		this.serviceQueryBio = serviceQueryBio;
	}

	/**
	 * @return the serviceTypeList
	 */
	public List<Map<String, String>> getServiceTypeList()
	{
		return serviceTypeList;
	}

	/**
	 * @param serviceTypeList
	 *            the serviceTypeList to set
	 */
	public void setServiceTypeList(List<Map<String, String>> serviceTypeList)
	{
		this.serviceTypeList = serviceTypeList;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId()
	{
		return serviceId;
	}

	/**
	 * @param serviceId
	 *            the serviceId to set
	 */
	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
	}
}