package com.linkage.module.gtms.config.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.config.serv.OttConfigServ;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-3-31
 * @category com.linkage.module.gtms.config.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class OttConfigActionImpl implements OttConfigAction
{
	private static Logger logger = LoggerFactory.getLogger(OttConfigActionImpl.class);
	private String userId = "";
	/**  */
	private String searchType = "";
	/** 帐号 **/
	private String username;
	private OttConfigServ bio;
	private String ajax = "";
	private String gw_type = "";
	
	// 关闭还是开通 flag 1:开通，0:关闭
	private String flag = "1";

	@Override
	public String getUserInfo()
	{
		logger.debug("getServUserInfo({})",username);
		if (null == gw_type || "".equals(gw_type)) {
			gw_type = "1";
		}
		ajax = bio.getServUserInfo(searchType,username,gw_type);
		return "ajax";
	}

	@Override
	public String doExecute()
	{
		logger.debug("doExecute({})",username);
		
		if (null == gw_type || "".equals(gw_type)) {
			gw_type = "1";
		}
		if("1".equals(flag)){
			ajax = bio.openOtt(username,userId,gw_type);
		}else{
			ajax = bio.closeOtt(username,userId,gw_type);
		}
		
		return "ajax";
	}

	
	public String getUserId()
	{
		return userId;
	}

	
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	
	public String getSearchType()
	{
		return searchType;
	}

	
	public void setSearchType(String searchType)
	{
		this.searchType = searchType;
	}

	
	public String getUsername()
	{
		return username;
	}

	
	public void setUsername(String username)
	{
		this.username = username;
	}

	
	public OttConfigServ getBio()
	{
		return bio;
	}

	
	public void setBio(OttConfigServ bio)
	{
		this.bio = bio;
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	
	public String getFlag()
	{
		return flag;
	}

	
	public void setFlag(String flag)
	{
		this.flag = flag;
	}
	
}
