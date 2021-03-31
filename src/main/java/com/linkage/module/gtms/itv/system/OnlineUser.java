
package com.linkage.module.gtms.itv.system;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserMap;
import com.linkage.litms.system.UserRes;

public class OnlineUser implements HttpSessionBindingListener
{

	private static Logger log = Logger.getLogger(OnlineUser.class);

	/**
	 * 绑定session时的操作
	 */
	public void valueBound(HttpSessionBindingEvent arg0)
	{
		// //获取用户名
		// HttpSession session = arg0.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");
		// String account = curUser.getUser().getAccount();
		//
		// //将用户和session的对应关系放入map中
		// UserMap.getInstance().addOnlineSession(account, session);
		// log.info(new DateTimeUtil().getLongDate()+"  user logon:" + account);
	}

	/**
	 * session超时或失效时的操作
	 */
	public void valueUnbound(HttpSessionBindingEvent arg0)
	{
		try
		{
			// 获取用户名
			HttpSession session = arg0.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			if (curUser == null)
			{
				return;
			}
			String account = curUser.getUser().getAccount();
			log.info(new DateTimeUtil().getLongDate() + "  user logout:" + account);
			UserMap.getInstance().deleteOnlineSession(account);
		}
		catch (IllegalStateException ignore)
		{
			// getAttribute: Session already invalidated
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
}
