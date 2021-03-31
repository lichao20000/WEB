
package com.linkage.module.gtms.stb.resource.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.stb.resource.dto.CustomerDTO;
import com.linkage.module.gtms.stb.resource.serv.CustomerBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 用户业务查询
 * 
 * @author fangchao (Ailk No. 69934)
 * @version 1.0
 * @since 2013-8-26
 * @category com.linkage.module.lims.itv.zeroconf.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class CustomerAction extends splitPageAction implements SessionAware,
		ServletRequestAware, ServletResponseAware
{

	private static final long serialVersionUID = -3546927537244035075L;
	private static Logger logger = LoggerFactory.getLogger(CustomerAction.class);
	private CustomerBIO customerBIO;
	private CustomerDTO customerDTO;
	/**
	 * 属地集合
	 */
	private List cityList;
	private List<Map> customerList;
	// 标题（导出用）
	private String[] title;
	// 列（导出用）
	private String[] column;
	// 数据（导出用）
	private List<Map> data;
	// 文件名称（导出用）
	private String fileName;
	private Map customerMap;
	private Map session = null;
	private List<Map> zeroInfoList;
	private String status;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	// 局点名称
	private String instAreaName;
	// 吉林电信加loid
	private String loid;
	private String ajax;
	private String gw_type;

	private String instArea=Global.instAreaShortName;
	/**
	 * 跳转到查询页面，进行属地初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		customerDTO = new CustomerDTO();
		instAreaName = Global.instAreaShortName;
		// modify by chensq5 :宁夏机顶盒管理界面问题20151103_zhangyh_20151103154831
		if (Global.NXDX.equals(Global.instAreaShortName))
		{
			customerDTO.setStartTime(DateUtil
					.firstDayOfCurrentYear("yyyy-MM-dd HH:mm:ss"));
		}
		else if (Global.SDLT.equals(Global.instAreaShortName))
		{
			Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			customerDTO.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(now.getTime()));
		}
		else
		{
			customerDTO.setStartTime(DateUtil
					.firstDayOfCurrentMonth("yyyy-MM-dd HH:mm:ss"));
		}
		customerDTO.setEndTime(DateUtil.lastTimeOfCurrentDay("yyyy-MM-dd HH:mm:ss"));
		return "query";
	}

	/**
	 * 查询用户业务列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listCustomer() throws Exception
	{
		UserRes curUser = (UserRes) session.get("curUser");
		if (StringUtil.IsEmpty(customerDTO.getCityId()))
		{
			customerDTO.setCityId(curUser.getCityId());
		}
		logger.info("query customer list by {}", customerDTO);
		totalRowCount_splitPage = customerBIO.countCustomer(customerDTO);
		customerList = customerBIO.queryCustomerList(customerDTO, (curPage_splitPage - 1)
				* num_splitPage, num_splitPage);
		// 局点名称
		instAreaName = Global.instAreaShortName;
		
		logger.debug("instAreaName="+instAreaName + "   customerList="+customerList);
		return "list";
	}

	/**
	 * 查询用户业务详情信息
	 * 
	 * @return
	 */
	public String queryCustomerDetail()
	{
		String customerId = request.getParameter("customerId");
		String deviceId = request.getParameter("deviceId");
		// 宁夏电信显示MAC地址
		customerMap = customerBIO.queryCustomerDetail(customerId, deviceId, status);
		// 局点名称
		instAreaName = Global.instAreaShortName;
		// 山东联通显示工单详情
		if (Global.SDLT.equals(instAreaName))
		{
			zeroInfoList = customerBIO.queryWorkDetail(customerId);
		}
		else
		{
			zeroInfoList = customerBIO.queryZeroDetail(customerId, deviceId);
		}
		return "detail";
	}
	
	public String querySTBCustomerDetail()
	{
		String serv_account = request.getParameter("serv_account");
		String deviceId = request.getParameter("deviceId");
		// 宁夏电信显示MAC地址
		customerMap = customerBIO.querySTBCustomerDetail(serv_account, deviceId, status);
		return "detail";
	}
	public String exportCustomer()
	{
		title = new String[] { "属地", "业务账号", "接入帐号", "接入类型", "机顶盒序列号", "开户时间", "开通状态" };
		column = new String[] { "cityName", "servAccount", "pppoeUser", "addressingType",
				"deviceSN", "openUserdate", "userStatus" };
		if (Global.SDLT.equals(Global.instAreaShortName))
		{
			title = new String[] { "属地", "业务账号", "接入帐号", "接入类型", "接入方式", "MAC", "机顶盒序列号",
					"开户时间", "开通状态" };
			column = new String[] { "cityName", "servAccount", "pppoeUser",
					"addressingType", "stbuptyle", "mac", "deviceSN", "openUserdate",
					"userStatus" };
		}		
		if (Global.JXDX.equals(Global.instAreaShortName))
		{
			title = new String[] { "属地", "用户宽带账号", "业务账号", "接入帐号", "接入类型","机顶盒序列号", "开户时间", "开通状态" };
			column = new String[] { "cityName", "custAccount", "servAccount", "pppoeUser", "addressingType",
					"deviceSN", "openUserdate", "userStatus" };
		}
		if (Global.JLDX.equals(Global.instAreaShortName))
		{
			title = new String[] { "属地", "业务账号", "接入帐号", "LOID", "接入类型", "机顶盒序列号", "开户时间", "客户类型", "工单mac地址", "开通状态" };
			column = new String[] { "cityName", "servAccount", "pppoeUser", "loid", "addressingType",
					"deviceSN", "openUserdate", "custType", "mac", "userStatus" };
		}
		if (Global.HNLT.equals(Global.instAreaShortName))
		{
			title = new String[] { "属地", "业务账号", "接入帐号", "业务平台类型", "用户分组类型", "机顶盒序列号", "开户时间"};
			column = new String[] { "cityName", "servAccount", "pppoeUser","belongName","groupName",
					"deviceSN", "openUserdate"};
		}
		if (Global.SXLT.equals(Global.instAreaShortName))
		{
			title = new String[] { "属地", "业务账号", "接入帐号", "唯一标识", "工单序号", "机顶盒序列号", "开户时间",  "开通状态" };
			column = new String[] { "cityName", "servAccount", "pppoeUser", "loid", "orderNo",
					"deviceSN", "openUserdate",  "userStatus" };
		}
		fileName = "customerInfo";
		// data = customerBIO.queryCustomerList(customerDTO,(curPage_splitPage-1)*
		// num_splitPage,num_splitPage);
		UserRes curUser = (UserRes) session.get("curUser");
		/** 如果cityID不为空，或者是"请选择"的时候点击了查询 ，则就以当前用户的登陆属地查询 **/
		if (StringUtil.IsEmpty(customerDTO.getCityId()))
		{
			customerDTO.setCityId(curUser.getCityId());
		}
		data = customerBIO.exportCustomerList(customerDTO);
		return "excel";
	}
	

	public String getUserIdByLoid(){
		String userId = customerBIO.getUserIdByLoid(loid,gw_type);
		if(userId==null){
			ajax = "";
		}else{
			ajax = userId;
		}
		
		return "ajax";
	}
	
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public List getCityList()
	{
		return cityList;
	}

	public void setCustomerBIO(CustomerBIO customerBIO)
	{
		this.customerBIO = customerBIO;
	}

	public CustomerDTO getCustomerDTO()
	{
		return customerDTO;
	}

	public void setCustomerDTO(CustomerDTO customerDTO)
	{
		this.customerDTO = customerDTO;
	}

	public List<Map> getCustomerList()
	{
		return customerList;
	}

	public void setCustomerList(List<Map> customerList)
	{
		this.customerList = customerList;
	}

	public String[] getTitle()
	{
		return title;
	}

	public String[] getColumn()
	{
		return column;
	}

	public List<Map> getData()
	{
		return data;
	}

	public String getFileName()
	{
		return fileName;
	}

	public Map getCustomerMap()
	{
		return customerMap;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	public void setServletResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	public List<Map> getZeroInfoList()
	{
		return zeroInfoList;
	}

	public void setZeroInfoList(List<Map> zeroInfoList)
	{
		this.zeroInfoList = zeroInfoList;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getInstAreaName()
	{
		return instAreaName;
	}

	public void setInstAreaName(String instAreaName)
	{
		this.instAreaName = instAreaName;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
	}
	
}
