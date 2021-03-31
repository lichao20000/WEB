package com.linkage.module.gtms.config.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.LogItem;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.UserInfo;
import action.splitpage.splitPageAction;

import com.linkage.module.gtms.config.serv.UseParameterBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class UseParameterACT extends splitPageAction implements SessionAware ,ServletRequestAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(UseParameterACT.class);
	/**设备id*/
	private String device_id = "";
	/** 宽带账号 */
	private String netusername = "";
	/**密码*/
	private String netusernamepwd = "";
	
	private Map<String, Object> session;
	private UseParameterBIO bio;
	private String ajax = "";

	private HttpServletRequest request;

	public UseParameterACT() {
	}

	/**
	 * 配置
	 */
	public String doConfig()
	{
		logger.warn("[{}]-[{}] UseParameterACT.doConfig()",netusername,netusernamepwd);
		
		List<HashMap<String, String>> list=bio.queryDeviceByUsername(netusername);
		ajax=null;
		if(list==null || list.isEmpty()){
			ajax="根据宽带账号未查询到数据";
		}else if(list.size()>1){
			ajax="根据宽带账号查询到多条数据";
		}
		else
		{
			HashMap<String, String> map=list.get(0);
			String loid = StringUtil.getStringValue(map, "loid", "");
			String oui = StringUtil.getStringValue(map, "oui", "");
			String devSn = StringUtil.getStringValue(map, "device_serialnumber", "");
			String userId = StringUtil.getStringValue(map, "user_id", "");
			String city_id = StringUtil.getStringValue(map, "city_id", "");
			device_id = StringUtil.getStringValue(map, "device_id", "");
			
			if (StringUtil.IsEmpty(loid)) {
				ajax = "用户表中没有查询结果";
			}else if(StringUtil.IsEmpty(city_id)) {
				ajax = "用户表中用户属地为空";
			}else if(StringUtil.IsEmpty(device_id) 
					|| StringUtil.IsEmpty(oui) || StringUtil.IsEmpty(devSn)) {
				ajax = "用户没有绑定设备";
			}else if(StringUtil.IsEmpty(userId)) {
				ajax="用户信息为空";
			}else if(0==StringUtil.getIntValue(map,"online_status")) {
				ajax="设备["+devSn+"]不在线";
			}
			else
			{
				UserInfo userInfo=new UserInfo();
				userInfo.userId = userId;
				userInfo.deviceId = device_id;
				userInfo.deviceSn = devSn;
				userInfo.gatherId = "1";
				userInfo.oui = oui;
				userInfo.servTypeId = "10";
				userInfo.operTypeId = "1";

				ajax=bio.doConfig(Long.valueOf(userId),netusername,netusernamepwd,userInfo);
				// 山东联通新增日志记录-》SDLT-REQ-RMS-20190825-FMK-001
				if(Global.SDLT.equals(Global.instAreaShortName)){
					long  user_id=((UserRes) session.get("curUser")).getUser().getId();
					bio.addOperationLog(user_id,request.getRemoteHost(),netusername,ajax,1);
				}
			}
		}

		logger.warn("[{}] UseParameterACT.doConfigResult:[{}]",netusername,ajax);
		return "ajax";
	}

	public ArrayList getOpertionLog(HttpServletRequest request) {
		logger.debug("getOpertionLog(request)");
		String operationObject = org.apache.commons.lang3.StringUtils.trim(request.getParameter("content"));
		ArrayList list = new ArrayList();
		list.clear();
		String stroffset = request.getParameter("offset");
		int offset;
		int pagelen = 20;
		if (stroffset == null) {
			offset = 1;
		} else {
			offset = Integer.parseInt(stroffset);
		}
		String param = "";
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select a.operation_time,a.operation_name,b.acc_loginname from tab_oper_log a,tab_accounts b where a.acc_oid=b.acc_oid and a.log_sub_type = 1 ");
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(operationObject)) {
			stringBuilder.append(" and a.operation_name like '%"+operationObject+"%'");
			param = "&content="+ operationObject;
		}
		stringBuilder.append("order by a.operation_time desc");
		String sql = stringBuilder.toString();
		String strBar = null;
		QueryPage qryp = new QueryPage();
		qryp.initPage(sql, offset, pagelen);
		strBar = qryp.getPageBar(param);
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		list.add(cursor);
		return list;
	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getNetusername() {
		return netusername;
	}

	public void setNetusername(String netusername) {
		this.netusername = netusername;
	}

	public String getNetusernamepwd() {
		return netusernamepwd;
	}

	public void setNetusernamepwd(String netusernamepwd) {
		this.netusernamepwd = netusernamepwd;
	}

	public UseParameterBIO getBio() {
		return bio;
	}

	public void setBio(UseParameterBIO bio) {
		this.bio = bio;
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

}
