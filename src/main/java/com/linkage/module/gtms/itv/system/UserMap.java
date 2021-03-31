package com.linkage.module.gtms.itv.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.linkage.litms.common.database.PrepareSQL;
import org.apache.log4j.Logger;

import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;

/**
 * 系统中特定用户的容器
 * @author wp
 *
 */
public class UserMap
{
	private static Logger log = Logger.getLogger(UserMap.class);
	//系统中由于登录失败而锁定的用户容器
	private static HashMap lockUsers = new HashMap(10);

	//单态实例对象
	private static UserMap instance = null;

	//放系统中存活的session
	private static HashMap onlineSession = new HashMap(20);

	/**
	 * 获取单态实例
	 * @return
	 */
	public static UserMap getInstance()
	{
		if(null==instance)
		{
			instance = new UserMap();
		}
		return instance;
	}


	private UserMap()
	{
		new LockUserThread().start();
	}

	/**
	 * 在会话队列中增加这个会话
	 * @param userName 会话对应的用户名
	 * @param session  会话
	 */
	public void addOnlineSession(String userName,HttpSession session)
	{
		if(null==userName||"".equals(userName)||null==session)
		{
			return;
		}
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@ "+new DateTimeUtil().getLongDate()+"  user logon:" + userName);
		synchronized(onlineSession)
		{
			onlineSession.put(userName,session);
		}
	}

	/**
	 * 删除会话队列中的这个会话
	 * @param userName 会话对应的用户名
	 */
	public void deleteOnlineSession(String userName)
	{
		synchronized(onlineSession)
		{
			onlineSession.remove(userName);
		}
	}

	/**
	 * 将userName用户踢出系统
	 * @param userName
	 */
	public void destroySessionByUserName(String userName)
	{
		log.info("destroySessionByUserName  "+userName);
		synchronized(onlineSession)
		{
			Object obj = onlineSession.get(userName);
			if(null!=obj)
			{
				log.info(userName+"   登录用户失效！");
				HttpSession session = (HttpSession)obj;
				try
				{
					onlineSession.remove(userName);
					session.invalidate();
					log.info("############ 删除用户   "+userName);
				}
				catch(Exception e)
				{
//					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 展现出在线用户，true，不做权限过滤，false，只展现出指定用户的子层在线用户。
	 * @param acc_oid 用户id
	 * @param isAdmin true：不要做权限过滤，false:只展示acc_oid创建的用户，展现出在线的。
	 */
	public ArrayList getOnlineUser(long acc_oid,boolean isAdmin)
	{
		ArrayList onlineUserList = new ArrayList();
		ArrayList temp = new ArrayList();
		String userName ="";
		//不是管理员，只要展示子层用户在线的就可以了
		if(!isAdmin)
		{
			PrepareSQL psql = new PrepareSQL("select distinct acc_loginname from tab_accounts where creator="+acc_oid);
			Cursor cursor =DataSetBean.getCursor(psql.getSQL());
			Map fields = cursor.getNext();
			while(null!=fields)
			{
				temp.add(fields.get("acc_loginname"));
				fields = cursor.getNext();
			}

			//遍历子用户
			for(int i=0;i<temp.size();i++)
			{
				userName =(String)temp.get(i);
				synchronized(onlineSession)
				{
					//子用户在线
					if(null!=onlineSession.get(userName))
					{
						onlineUserList.add(userName);
					}
				}
			}

			//clear
			fields = null;
			cursor=null;

		}
		else
		{
			synchronized(onlineSession)
			{
				Iterator it =onlineSession.keySet().iterator();
				while(it.hasNext())
				{
					onlineUserList.add(it.next());
				}
			}
		}

		//clear
		temp=null;

		return onlineUserList;
	}

	/**
	 * 把userName的可以登录的时间加入锁定用户队列
	 * @param userName  用户名
	 * @param allowLoginTime  该用户可以登录的时间
	 */
	public void addLockUser(String userName,long allowLoginTime)
	{
		log.info("addLockUser:"+userName);
		if(null==userName||allowLoginTime<=new Date().getTime()/1000)
		{
			return;
		}
		synchronized(lockUsers)
		{
			lockUsers.put(userName,String.valueOf(allowLoginTime));
		}
	}

	/**
	 * 获取被锁定用户信息
	 */
	public static Map getLockUser()
	{
		return lockUsers;
	}

	/**
	 * 判断userName用户是否是锁定用户
	 * @param userName
	 * @return true：锁定用户  false：可以自由登录
	 */
	public boolean checkIsLockUser(String userName)
	{
		Object obj = lockUsers.get(userName);
		//在锁用户队列中没有找到用户信息
		if(null==obj)
		{
			return false;
		}
		else
		{
			long allowLoginTime = Long.parseLong((String)obj);
			if(new Date().getTime()/1000>=allowLoginTime)
			{
				deleteLockUserMap(userName);
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
	 * @param userName
	 */
	public void deleteLockUserMap(String userName)
	{
		synchronized(lockUsers)
		{
			lockUsers.remove(userName);
		}
	}



	/**
	 * 及时清理已失效的锁定用户列表
	 * @author wp
	 *
	 */
	class LockUserThread extends Thread
	{
		@Override public void run()
		{
			ArrayList deleteUserList = new ArrayList();
			while(true)
			{
				synchronized(lockUsers)
				{
					long time = new Date().getTime()/1000;
					Iterator it =lockUsers.keySet().iterator();
					String userName ="";
					long loginTime;
					deleteUserList.clear();
					while(it.hasNext())
					{
						userName =(String)it.next();
						loginTime =Long.parseLong((String)lockUsers.get(userName));
						//已经过了用户的锁定时间，则把锁定用户的信息放入删除链表中
						if(loginTime<=time)
						{
							deleteUserList.add(userName);
						}
					}

					//删除锁定用户队列中已不被锁定用户
					for(int i=0;i<deleteUserList.size();i++)
					{
						userName=(String)deleteUserList.get(i);
						log.info(userName+"   已过了锁定时间，将从锁定用户队列中删除");
						lockUsers.remove(userName);
					}

				}

				try
				{
					Thread.sleep(10*1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

		}
	}

}
