package com.linkage.module.liposs.system.basesupport;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.liposs.system.exception.UserResNotFoundException;
import com.linkage.system.extend.struts.splitpage.SplitPageAction;
import com.linkage.system.systemlog.core.SystemLogBean;

/**
 * 分页功能的action,包括分页和获取会话信息的功能
 *
 * @author 陈仲民(5243)
 * @version 1.0
 * @since 2008-8-14
 * @category com.linkage.module.bcms.system<br>
 *           版权：南京联创科技 网管科技部
 *
 */
public class BaseSupportSplitAction extends SplitPageAction implements SessionAware,ServletRequestAware, ServletResponseAware
{
	/**
	 *
	 */
	private static final long serialVersionUID = 2560923258564000295L;
	protected Map session;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	/**
	 * 注入session信息
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}
	/**
	 * 从session中回去UserRes资源类
	 *
	 * @return
	 * @throws Exception
	 */
	protected UserRes getUserRes() throws Exception
	{
		Object ur = session.get("curUser");
		if (ur == null)
		{
			throw new UserResNotFoundException(
					"业务平台BaseSupportAction--getUserRes,无法从session获取资源信息类UserRes，可能是session已经失效或者没有登录");
		}
		else
		{
			return (UserRes) ur;
		}
	}
	/**
	 * 获取user类
	 *
	 * @return
	 * @throws Exception
	 */
	protected User getUser() throws Exception
	{
		return getUserRes().getUser();
	}
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	/**
	 * 获取系统日志的systemlogbean信息
	 *
	 * @return SystemLogBean对象
	 * @throws Exception
	 */
	public SystemLogBean getSysBean() throws Exception
	{
		SystemLogBean slb = new SystemLogBean();
		slb.setAccountName(getUser().getAccount());
		slb.setAreaId(getUser().getAreaId());
		slb.setIpAddr(request.getRemoteAddr());
		return slb;
	}
}
