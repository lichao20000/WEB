
package com.linkage.module.gwms.config.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.module.gwms.config.bio.BridgeToRouteBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 桥接转路由
 * 
 * @author 王森博
 */
public class BridgeToRouteACT extends ActionSupport implements SessionAware
{

	private String ajax;
	// session
	private Map session;
	private String username;
	private String devSn;
	private BridgeToRouteBIO bio;
	private List data;
	private String password;
	private String message;

	/**
	 * 桥改路由，路由下发支持情况查询
	 * 
	 * @author wangsenbo
	 * @date Jul 21, 2010
	 * @param
	 * @return String
	 */
	public String execute()
	{
		data = bio.infoQuery(username, devSn);
		if (data == null)
		{
			data = new ArrayList();
		}
		message = bio.getMessage();
		return "result";
	}

	/**
	 * @author wangsenbo
	 * @date Jul 21, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		return "success";
	}

	/**
	 * 桥改路由，桥改路由业务下发
	 * 
	 * @author wangsenbo
	 * @date Jul 21, 2010
	 * @param
	 * @return String
	 */
	public String edit()
	{
		bio.bridge2Routed(username, devSn, password);
		ajax = bio.getMessage();
		return "ajax";
	}
	
	/**
	 * 桥改路由，桥改路由下发情况查看
	 * 
	 * @author wangsenbo
	 * @date Jul 21, 2010
	 * @param
	 * @return String
	 */
	public String routeQuery()
	{
		bio.routedQuery(username, devSn);
		ajax = bio.getMessage();
		return "ajax";
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
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
	 * @return the devSn
	 */
	public String getDevSn()
	{
		return devSn;
	}

	/**
	 * @param devSn
	 *            the devSn to set
	 */
	public void setDevSn(String devSn)
	{
		this.devSn = devSn;
	}

	/**
	 * @return the bio
	 */
	public BridgeToRouteBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(BridgeToRouteBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the data
	 */
	public List getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List data)
	{
		this.data = data;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
}
