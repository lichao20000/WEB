
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.servPlatformBIO;

import action.splitpage.splitPageAction;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-1-5
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class servPlatformACT extends splitPageAction implements SessionAware
{

	private Map session;
	private static Logger logger = LoggerFactory.getLogger(servPlatformACT.class);
	// id
	private String platformid;
	// 名称
	private String platformname;
	// 备注
	private String remark="	";
	// 操作人
	private String operator;
	private String ajax;
	// 返回集合
	private List<Map<String, String>> date;
	private servPlatformBIO bio;
	
	/**
	 * 查询信息
	 * @return
	 */
	public String query()
	{
		date=bio.querylist(platformname);
		return "list";
	}
	/**
	 * 修改
	 * @return
	 */
	public String updateservPlatform()
	{
		ajax=bio.updateservPlatform(platformid, platformname, remark, operator);
		return "ajax";
	}
	public String queryplatformname()
	{
		date=bio.queryplatformname(platformid);
		return "success";
	}
	/**
	 * 添加
	 * @return
	 */
	public String addservPlatform()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		operator=curUser.getUser().getAccount();
		ajax=bio.addservPlatform(platformid, platformname, remark, operator);
		return "ajax";
	}
	/**
	 * 查询id
	 * @return
	 */
	public String queryplatformid()
	{
		ajax=bio.queryplatformid(platformid);
		return "ajax";
	}
	
	/**
	 * 删除
	 * @return
	 */
	public String deleteservPlatform()
	{
		logger.warn("deleteservPlatform====platformid-==="+platformid);
		ajax=bio.deleteservPlatform(platformid);
		return "ajax";
	}
	public servPlatformBIO getBio()
	{
		return bio;
	}

	
	public void setBio(servPlatformBIO bio)
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

	public String getPlatformid()
	{
		return platformid;
	}

	public void setPlatformid(String platformid)
	{
		this.platformid = platformid;
	}

	public String getPlatformname()
	{
		return platformname;
	}

	public void setPlatformname(String platformname)
	{
		this.platformname = platformname;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getOperator()
	{
		return operator;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<Map<String, String>> getDate()
	{
		return date;
	}

	public void setDate(List<Map<String, String>> date)
	{
		this.date = date;
	}
}
