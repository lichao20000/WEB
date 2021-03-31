package com.linkage.module.gwms.resource.act;

import com.linkage.module.gwms.resource.bio.KdPasswordModificationBIO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-4-13
 * @category com.linkage.module.gwms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class KdPasswordModificationACT
{
	/** * loid */
	private String loid;
	/** * 宽带账号 */
	private String username;
	/** * 宽带密码 */
	private String netPasswd;
	private String ajax;
	private KdPasswordModificationBIO bio;
	public String updateKdPassword()
	{
		ajax=bio.updateKdPassword(loid, username, netPasswd);
		return "ajax";
	}
	
	public String getLoid()
	{
		return loid;
	}
	
	public void setLoid(String loid)
	{
		this.loid = loid;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getNetPasswd()
	{
		return netPasswd;
	}
	
	public void setNetPasswd(String netPasswd)
	{
		this.netPasswd = netPasswd;
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
	public KdPasswordModificationBIO getBio()
	{
		return bio;
	}
	
	public void setBio(KdPasswordModificationBIO bio)
	{
		this.bio = bio;
	}
	
}
