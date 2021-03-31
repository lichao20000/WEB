/**
 * 
 */

package com.linkage.module.gwms.service.act;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.service.dao.ServStrategyLogDAO;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category action.confTaskView
 */
public class ServStrategyLogACT extends splitPageAction implements ServletRequestAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ServStrategyLogDAO dao;
	// 定制起始时间
	private String time_start = null;
	// 定制截止时间
	private String time_end = null;
	// 定制人
	private String operatesName = null;
	// 策略状态
	private String status = null;
	// 业务名称
	private String service_id = null;
	// 策略方式
	private String type = null;
	// 厂商
	private String vendor_id = null;
	// 设备序列号
	private String device_serialnumber = null;
	// 用户账号
	private String username = null;
	// 业务名称
	private List service_idLsit = null;
	// 厂商
	private List vendor_idLsit = null;
	// 查询结果
	private List servStrategyList = null;
	// 查询类型
	private String selectType = null;
	// 策略类型
	private Integer strategyType = 0;
	// request取登陆帐号使用
	private HttpServletRequest request;
	// 数据源
	private String datasource;
	

	// 策略历史配置查询
	public String execute() throws Exception
	{
		if ("0".equals(selectType))
		{
			this.time_start = "";
			this.time_end = "";
		}
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		device_serialnumber = device_serialnumber == null?null:device_serialnumber.trim();
		servStrategyList = dao.getServStrategList(strategyType, curPage_splitPage, num_splitPage, user,
				time_start, time_end, operatesName, status, service_id, type, vendor_id,
				device_serialnumber, username, datasource);
		maxPage_splitPage = dao.getServStrategyCount(strategyType, curPage_splitPage, num_splitPage,
				user, time_start, time_end, operatesName, status, service_id, type,
				vendor_id, device_serialnumber, username, datasource);
		return "list";
	}

	public String goPage() throws Exception
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if ("0".equals(selectType))
		{
			this.time_start = "";
			this.time_end = "";
		}

		
		User user = curUser.getUser();
		
		device_serialnumber = device_serialnumber == null?null:device_serialnumber.trim();
		servStrategyList = dao.getServStrategList(strategyType,curPage_splitPage, num_splitPage, user,
				time_start, time_end, operatesName, status, service_id, type, vendor_id,
				device_serialnumber, username, datasource);
		maxPage_splitPage = dao.getServStrategyCount(strategyType,curPage_splitPage, num_splitPage,
				user, time_start, time_end, operatesName, status, service_id, type,
				vendor_id, device_serialnumber, username, datasource);
		return "list";
	}

	public String startQuery() throws Exception
	{
		this.service_idLsit = dao.getAllService_idList();
		this.vendor_idLsit = dao.getAllVendor_idList();
		// Global.CPE_FAULT_CODE_MAP = dao.initFaultCode();
		return "query";
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
	 * @return the operatesName
	 */
	public String getOperatesName()
	{
		return operatesName;
	}

	/**
	 * @param operatesName
	 *            the operatesName to set
	 */
	public void setOperatesName(String operatesName)
	{
		this.operatesName = operatesName;
	}

	/**
	 * @return the service_id
	 */
	public String getService_id()
	{
		return service_id;
	}

	/**
	 * @param service_id
	 *            the service_id to set
	 */
	public void setService_id(String service_id)
	{
		this.service_id = service_id;
	}

	/**
	 * @return the service_idLsit
	 */
	public List getService_idLsit()
	{
		return service_idLsit;
	}

	/**
	 * @param service_idLsit
	 *            the service_idLsit to set
	 */
	public void setService_idLsit(List service_idLsit)
	{
		this.service_idLsit = service_idLsit;
	}

	/**
	 * @return the servStrategyList
	 */
	public List getServStrategyList()
	{
		return servStrategyList;
	}

	/**
	 * @param servStrategyList
	 *            the servStrategyList to set
	 */
	public void setServStrategyList(List servStrategyList)
	{
		this.servStrategyList = servStrategyList;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the time_end
	 */
	public String getTime_end()
	{
		return time_end;
	}

	/**
	 * @param time_end
	 *            the time_end to set
	 */
	public void setTime_end(String time_end)
	{
		this.time_end = time_end;
	}

	/**
	 * @return the time_start
	 */
	public String getTime_start()
	{
		return time_start;
	}

	/**
	 * @param time_start
	 *            the time_start to set
	 */
	public void setTime_start(String time_start)
	{
		this.time_start = time_start;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
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
	 * @return the vendor_id
	 */
	public String getVendor_id()
	{
		return vendor_id;
	}

	/**
	 * @param vendor_id
	 *            the vendor_id to set
	 */
	public void setVendor_id(String vendor_id)
	{
		this.vendor_id = vendor_id;
	}

	/**
	 * @return the vendor_idLsit
	 */
	public List getVendor_idLsit()
	{
		return vendor_idLsit;
	}

	/**
	 * @param vendor_idLsit
	 *            the vendor_idLsit to set
	 */
	public void setVendor_idLsit(List vendor_idLsit)
	{
		this.vendor_idLsit = vendor_idLsit;
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest()
	{
		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	/**
	 * @return the selectType
	 */
	public String getSelectType()
	{
		return selectType;
	}

	/**
	 * @param selectType
	 *            the selectType to set
	 */
	public void setSelectType(String selectType)
	{
		this.selectType = selectType;
	}

	/**
	 * @return the dao
	 */
	public ServStrategyLogDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(ServStrategyLogDAO dao)
	{
		this.dao = dao;
	}
	
	public Integer getStrategyType()
	{
		return strategyType;
	}
	
	public void setStrategyType(Integer strategyType)
	{
		this.strategyType = strategyType;
	}
	
	public String getDatasource()
	{
		return datasource;
	}
	
	public void setDatasource(String datasource)
	{
		this.datasource = datasource;
	}
	
}
