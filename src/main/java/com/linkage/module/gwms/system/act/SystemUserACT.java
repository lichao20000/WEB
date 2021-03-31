
package com.linkage.module.gwms.system.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.system.bio.SystemUserBIO;

/**
 * 系统用户列表
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class SystemUserACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SystemUserACT.class);
	// session
	private Map session;
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private SystemUserBIO bio;

	/**
	 * 导出系统列表
	 * 登录用户创建的系统用户
	 *
	 * @author wangsenbo
	 * @date Jul 12, 2010
	 * @param 
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "SYSTEMUSER";
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		if(Global.CQDX.equals(Global.instAreaShortName)){
		title = new String[] { "登录名", "部门/地市", "名称", "分公司" };
		column = new String[] { "acc_loginname", "city_name", "per_name", "per_jobtitle" };
		}else
		{
		title = new String[] { "登陆帐号", "部门/地市", "姓名", "权限描述" };
		column = new String[] { "acc_loginname", "city_name", "per_name", "role_desc" };
		}
		
		data = bio.getSystemUserExcel(user);
		return "excel";
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
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the bio
	 */
	public SystemUserBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(SystemUserBIO bio)
	{
		this.bio = bio;
	}
}
