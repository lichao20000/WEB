
package com.linkage.module.gtms.itv.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.linkage.litms.common.database.PrepareSQL;
import org.apache.log4j.Logger;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 系统中特定用户的容器
 *
 * @author wp
 */
public class UserIpMap
{

	private static Logger log = Logger.getLogger(UserIpMap.class);
	// 系统中由于登录失败而锁定的用户容器
	private static HashMap lockIps = new HashMap(10);
	private static HashMap lockUserMap = new HashMap(20);
	// 单态实例对象
	private static UserIpMap instance = null;
	// 放系统中存活的session
	private static HashMap onlineSession = new HashMap(20);

	/**
	 * 获取单态实例
	 *
	 * @return
	 */
	public static UserIpMap getInstance()
	{
		if (null == instance)
		{
			instance = new UserIpMap();
		}
		return instance;
	}

	private UserIpMap()
	{
		new LockIpThread().start();
	}

	/**
	 * 在会话队列中增加这个会话
	 *
	 * @param userIp
	 *            会话对应的用户名
	 * @param session
	 *            会话
	 */
	public void addOnlineSession(String userIp, HttpSession session)
	{
		if (null == userIp || "".equals(userIp) || null == session)
		{
			return;
		}
		synchronized (onlineSession)
		{
			onlineSession.put(userIp, session);
		}
	}

	/**
	 * 删除会话队列中的这个会话
	 *
	 * @param userIp
	 *            会话对应的用户名
	 */
	public void deleteOnlineSession(String userIp)
	{
		synchronized (onlineSession)
		{
			onlineSession.remove(userIp);
		}
	}

	/**
	 * 将userIp用户踢出系统
	 *
	 * @param userIp
	 */
	public void destroySessionByUserIp(String userIp)
	{
		log.info("destroySessionByUserIp  " + userIp);
		synchronized (onlineSession)
		{
			Object obj = onlineSession.get(userIp);
			if (null != obj)
			{
				log.info(userIp + "   登录用户IP失效！");
				HttpSession session = (HttpSession) obj;
				try
				{
					onlineSession.remove(userIp);
					session.invalidate();
					log.info("############ 删除用户   " + userIp);
				}
				catch (Exception e)
				{
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 展现出在线用户，true，不做权限过滤，false，只展现出指定用户的子层在线用户。
	 *
	 * @param acc_oid
	 *            用户id
	 * @param isAdmin
	 *            true：不要做权限过滤，false:只展示acc_oid创建的用户，展现出在线的。
	 */
	public ArrayList getOnlineUser(long acc_oid, boolean isAdmin)
	{
		ArrayList onlineUserList = new ArrayList();
		ArrayList temp = new ArrayList();
		String userName = "";
		// 不是管理员，只要展示子层用户在线的就可以了
		if (!isAdmin)
		{
			PrepareSQL psql = new PrepareSQL("select distinct acc_loginname from tab_accounts where creator=" + acc_oid);
			Cursor cursor = DataSetBean.getCursor(psql.getSQL());
			Map fields = cursor.getNext();
			while (null != fields)
			{
				temp.add(fields.get("acc_loginname"));
				fields = cursor.getNext();
			}
			// 遍历子用户
			for (int i = 0; i < temp.size(); i++)
			{
				userName = (String) temp.get(i);
				synchronized (onlineSession)
				{
					// 子用户在线
					if (null != onlineSession.get(userName))
					{
						onlineUserList.add(userName);
					}
				}
			}
			// clear
			fields = null;
			cursor = null;
		}
		else
		{
			synchronized (onlineSession)
			{
				Iterator it = onlineSession.keySet().iterator();
				while (it.hasNext())
				{
					onlineUserList.add(it.next());
				}
			}
		}
		// clear
		temp = null;
		return onlineUserList;
	}

	/**
	 * 把userIp的可以登录的时间加入锁定用户队列
	 *
	 * @param userIp
	 *            用户名
	 * @param allowLoginTime
	 *            该用户可以登录的时间
	 */
	public void addLockUserIp(String userIp, long allowLoginTime)
	{
		log.info("addLockUserIp:" + userIp);
		if (null == userIp || allowLoginTime <= new Date().getTime() / 1000)
		{
			return;
		}
		synchronized (lockIps)
		{
			lockIps.put(userIp, String.valueOf(allowLoginTime));
		}
	}

	/**
	 * 把账号和IP的对应关系放入Map
	 *
	 * @param userIp
	 *            用户名
	 * @param allowLoginTime
	 *            该用户可以登录的时间
	 */
	public void addlockUserMap(String account, String userip)
	{
		log.info("addlockUserMap:" + account + ",userip:" + userip);
		synchronized (lockUserMap)
		{
			lockUserMap.put(account, userip);
		}
	}

	/**
	 * 获取账号和IP的对应关系
	 */
	public static HashMap getlockUserMap()
	{
		return lockUserMap;
	}

	/**
	 * 获取被锁定用户IP信息
	 */
	public static Map getLockUserIp()
	{
		return lockIps;
	}

	/**
	 * 判断userName用户是否是锁定用户
	 *
	 * @param userName
	 * @return true：锁定用户 false：可以自由登录
	 */
	public boolean checkIsLockUserIp(String userIp)
	{
		Object obj = lockIps.get(userIp);
		// 在锁用户队列中没有找到用户信息
		if (null == obj)
		{
			return false;
		}
		else
		{
			long allowLoginTime = Long.parseLong((String) obj);
			if (new Date().getTime() / 1000 >= allowLoginTime)
			{
				deleteLockUserIpMap(userIp);
				return false;
			}
			else
			{
				return true;
			}
		}
	}

	/**
	 * 从锁用户队列中删除当前用户
	 *
	 * @param userName
	 */
	public void deleteLockUserIpMap(String userIp)
	{
		synchronized (lockIps)
		{
			lockIps.remove(userIp);
		}
	}

	/**
	 * 及时清理已失效的锁定用户列表
	 *
	 * @author wp
	 */
	class LockIpThread extends Thread
	{

		@Override
		public void run()
		{
			ArrayList deleteUserList = new ArrayList();
			while (true)
			{
				synchronized (lockIps)
				{
					long time = new Date().getTime() / 1000;
					Iterator it = lockIps.keySet().iterator();
					String userIp = "";
					long loginTime;
					deleteUserList.clear();
					while (it.hasNext())
					{
						userIp = (String) it.next();
						loginTime = Long.parseLong((String) lockIps.get(userIp));
						// 已经过了用户的锁定时间，则把锁定用户的信息放入删除链表中
						if (loginTime <= time)
						{
							deleteUserList.add(userIp);
						}
					}
					// 删除锁定用户队列中已不被锁定用户
					for (int i = 0; i < deleteUserList.size(); i++)
					{
						userIp = (String) deleteUserList.get(i);
						log.info(userIp + "   已过了锁定时间，将从锁定用户IP队列中删除");
						lockIps.remove(userIp);
					}
				}
				try
				{
					Thread.sleep(10 * 1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
