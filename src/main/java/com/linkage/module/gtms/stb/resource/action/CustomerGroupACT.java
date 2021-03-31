package com.linkage.module.gtms.stb.resource.action;

import java.util.HashMap;
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
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.CustomerGroupBIO;

public class CustomerGroupACT extends splitPageAction implements  SessionAware,
	ServletRequestAware, ServletResponseAware {
	
	/**
	 *  serial
	 */
	private static final long serialVersionUID = 1L;

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(CustomerGroupACT.class);

	CustomerGroupBIO cgBIO = null;
	
	private String ajax = null;
	
	private List<HashMap<String, String>> dataList = null;

	private String groupName = null;
	
	private String groupId = null;
	
	private String remark = null;
	
	private Map session = null;
	
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	/**
	 * 查询数据
	 * @return
	 */
	public String queryDataList() {
		logger.debug("CustomerGroupACT=>queryDataList()");
		dataList = cgBIO.getDataList(groupName);
		return "list";
	}
	
	/**
	 * 查询数据
	 * @return
	 */
	public String queryData() {
		logger.debug("CustomerGroupACT=>queryData()");
		logger.warn(groupId);
		if (!StringUtil.IsEmpty(groupId)) {
			Map<String, String> map = cgBIO.getData(groupId);
			groupId = StringUtil.getStringValue(map, "group_id");
			groupName = StringUtil.getStringValue(map, "group_name");
			remark = StringUtil.getStringValue(map, "remark");
		}
		return "edit";
	}

	public String addData() {
		// 获取用户信息
		UserRes curUser = (UserRes) session.get("curUser");
		String userId = String.valueOf(curUser.getUser().getId());
		int num = cgBIO.getDataCount(groupId);
		if (num > 0) {
			ajax = "用户分组ID已经存在请重新输入！";
		}
		else {
			int res = cgBIO.insertData(groupId, groupName, remark, userId);
			if (res == 1) {
				ajax = "1";
			}
			else {
				ajax = "0";
			}
		}
		return "ajax";
	}

	public String editData() {
		// 获取用户信息
		UserRes curUser = (UserRes) session.get("curUser");
		String userId = String.valueOf(curUser.getUser().getId());
		int res = cgBIO.updateData(groupId, groupName, remark, userId);
		if (res == 1) {
			ajax = "1";
		}
		else {
			ajax = "0";
		}
		return "ajax";
	}

	public String deleteData() {
		int res = cgBIO.deleteData(groupId);
		if (res == 1) {
			ajax = "1";
		}
		else {
			ajax = "0";
		}
		return "ajax";
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public CustomerGroupBIO getCgBIO() {
		return cgBIO;
	}

	public void setCgBIO(CustomerGroupBIO cgBIO) {
		this.cgBIO = cgBIO;
	}

	public List<HashMap<String, String>> getDataList() {
		return dataList;
	}

	public void setDataList(List<HashMap<String, String>> dataList) {
		this.dataList = dataList;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
}
