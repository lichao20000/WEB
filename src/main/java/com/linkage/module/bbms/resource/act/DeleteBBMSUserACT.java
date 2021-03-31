
package com.linkage.module.bbms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.resource.bio.DeleteBBMSUserBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 云南未使用定制终端用户删除
 * 
 * @author 王森博
 * @date 2010-07-23
 */
public class DeleteBBMSUserACT extends ActionSupport implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// session
	private Map session;
	private DeleteBBMSUserBIO bio;
	// 不存在用户列表
	private List notExistUserList;
	// 绑定设备用户列表
	private List bindUserList;
	// 可以删除用户列表
	private List canDelUserList;
	private String msg;
	private String gwShare_fileName = null;
	private String userIds = "";
	private String ajax;

	/**
	 * 通过上传的文件查询用户
	 * 
	 * @author wangsenbo
	 * @date Jul 23, 2010
	 * @param
	 * @return String
	 */
	public String queryUser()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		bio.getUserList(gwShare_fileName, cityId);
		notExistUserList = bio.getNotExistUserList();
		bindUserList = bio.getBindUserList();
		canDelUserList = bio.getCanDelUserList();
		userIds = bio.getUserIds();
		msg = bio.getMsg();
		return "list";
	}

	public String deleteUser()
	{
		ajax = bio.deleteUser(userIds);
		return "ajax";
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
	 * @return the bio
	 */
	public DeleteBBMSUserBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(DeleteBBMSUserBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the notExistUserList
	 */
	public List getNotExistUserList()
	{
		return notExistUserList;
	}

	/**
	 * @param notExistUserList
	 *            the notExistUserList to set
	 */
	public void setNotExistUserList(List notExistUserList)
	{
		this.notExistUserList = notExistUserList;
	}

	/**
	 * @return the bindUserList
	 */
	public List getBindUserList()
	{
		return bindUserList;
	}

	/**
	 * @param bindUserList
	 *            the bindUserList to set
	 */
	public void setBindUserList(List bindUserList)
	{
		this.bindUserList = bindUserList;
	}

	/**
	 * @return the canDelUserList
	 */
	public List getCanDelUserList()
	{
		return canDelUserList;
	}

	/**
	 * @param canDelUserList
	 *            the canDelUserList to set
	 */
	public void setCanDelUserList(List canDelUserList)
	{
		this.canDelUserList = canDelUserList;
	}

	/**
	 * @return the msg
	 */
	public String getMsg()
	{
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	/**
	 * @return the gwShare_fileName
	 */
	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	/**
	 * @param gwShare_fileName
	 *            the gwShare_fileName to set
	 */
	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	/**
	 * @return the userIds
	 */
	public String getUserIds()
	{
		return userIds;
	}

	/**
	 * @param userIds
	 *            the userIds to set
	 */
	public void setUserIds(String userIds)
	{
		this.userIds = userIds;
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
}
