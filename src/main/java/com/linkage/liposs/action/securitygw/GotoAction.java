package com.linkage.liposs.action.securitygw;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.action.cst;
import com.linkage.liposs.buss.dao.securitygw.SGWListDAO;
import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 该action主要负责url的定位，不做任何处理，适应需要带参数传递的情况
 * 
 * @author 王志猛(5194) tel：5194
 * @version 1.0
 * @since 2008-4-1
 * @category com.linkage.liposs.action.securitygw 版权：南京联创科技 网管科技部
 * 
 */
public class GotoAction extends ActionSupport implements SessionAware
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2211550746133501032L;
	private String device_id;// 设备id
	private String custom_name;// 客户名称
	private String area_id;// 域id
	private Map session;// 用户会话
	private List<Map> sgwList;//
	private UserRes res;// 用户资源
	private SGWListDAO sgwDao;// 后台获取性能设备列表的dao
	private String toUrl = null;// 跳转的url
	private static Logger log = LoggerFactory.getLogger(GotoAction.class);
	/**
	 * 点击域的时候使用该方法
	 */
	@Override
	public String execute() throws Exception
	{
		res = (UserRes) session.get("curUser");
		sgwList = sgwDao.getSGWFirst(String.valueOf((area_id == null) ? res.getAreaId()
				: area_id));
		
		return SUCCESS;
	}
	/**
	 * 点击客户的时候使用该方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public String goSubMenu() throws Exception
	{
		return cst.OK;
	}
	public String getDevice_id()
	{
		return device_id;
	}
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}
	public String getCustom_name()
	{
		return custom_name;
	}
	public void setCustom_name(String custom_name)
	{
		this.custom_name = custom_name;
	}
	public void setArea_id(String area_id)
	{
		this.area_id = area_id;
	}
	public String getArea_id()
	{
		return area_id;
	}
	public void setSession(Map arg0)
	{
		this.session = arg0;
	}
	public List<Map> getSgwList()
	{
		return sgwList;
	}
	public void setSgwDao(SGWListDAO sgwDao)
	{
		this.sgwDao = sgwDao;
	}
	public void setToUrl(String toUrl)
	{
		if ("cpu".equals(toUrl))
			{
				this.toUrl = "/securitygw/SgwPerformance.action?device_id=" + device_id
						+ "&class1=1&desc=" + custom_name;
			}
		else if ("mem".equals(toUrl))
			{
				this.toUrl = "/securitygw/SgwPerformance.action?device_id=" + device_id
						+ "&class1=2&desc=" + custom_name;
			}
		else if ("cons".equals(toUrl))
			{
				this.toUrl = "/securitygw/SgwPerformance.action?device_id=" + device_id
						+ "&class1=8&desc=" + custom_name;
			}
		else if ("virus".equals(toUrl))
			{
				this.toUrl = "/securitygw/VirusReport.action?deviceid=" + device_id
						+ "&remark=" + custom_name;
			}
		else if ("ashmail".equals(toUrl))
			{
				this.toUrl = "/securitygw/TrashMailReport.action?deviceid=" + device_id
						+ "&remark=" + custom_name;
			}
		else if ("attack".equals(toUrl))
			{
				this.toUrl = "/securitygw/AttackReport.action?deviceid=" + device_id
						+ "&remark=" + custom_name;
			}
		else if ("filter".equals(toUrl))
			{
				this.toUrl = "/securitygw/FilterReport.action?deviceid=" + device_id
						+ "&remark=" + custom_name;
			}
		else if ("ping".equals(toUrl))
			{
				this.toUrl = "/securitygw/SgwPerformance.action?device_id=" + device_id
						+ "&desc=" + custom_name;
			}
	}
	public String getToUrl()
	{
		return ((toUrl == null) ? ("/securitygw/entSecStat!redirect.action?deviceId="
				+ device_id + "&customerName=" + custom_name) : (toUrl));
	}
}
