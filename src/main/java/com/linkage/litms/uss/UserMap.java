package com.linkage.litms.uss;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpSession;

/**
 * 系统中特定用户的容器
 * @author gsj
 *
 */
public class UserMap
{
	//单态实例对象
	private static UserMap instance = null;
	
	//系统中的在线用户
	private static HashMap<String, String> onlineUsers = new HashMap<String, String>(20);
	
	//放系统中存活的session
	private static HashMap<String, HttpSession> onlineSession = new HashMap<String , HttpSession>(100);
	
	private static HashMap<String, String> checkedResult = new HashMap<String, String>();
	
	private Timer onlineUser_timer = null;
	
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
		//启动在线用户的定时任务
//		onlineUser_timer = new Timer();		
//		onlineUser_timer.schedule(new OnlineTask(),10*60*1000,10*60*1000);		
	}
	
	/**
	 * 把检测结果放到内存中
	 * @param CustomerId
	 * @param type
	 */
	public void addCheckedResult(String CustomerId, String type) {
		if (null == type) {
			return;
		}
		synchronized (checkedResult) {
			checkedResult.put(CustomerId, type);
		}
	}
	
	/**
	 * 取得检测结果
	 * @return
	 */
	public void removeCheckedResult(String CustomerId) {
		
		synchronized(checkedResult) {
			checkedResult.remove(CustomerId);
		}
		
		//return checkedResult;
	}
	
	public synchronized HashMap<String, String> getCheckedResult() {
		return checkedResult;
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
//		synchronized(onlineSession)
//		{
//			onlineSession.put(userName,session);
//		}
		session.setAttribute(userName, userName);
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
		Object obj = onlineSession.get(userName);
		if(null!=obj)
		{
			HttpSession session = (HttpSession)obj;
			try
			{
				session.invalidate();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			//删除会话队列中的该用户的会话
			deleteOnlineSession(userName);
			
			//删除在线用户队列中的用户
			deleteOnlineUser(userName);
		}
	}
	/**
	 * 更新在线用户的时间
	 * @param userName
	 */
	public void updateOnlineUser(String userName)
	{
		synchronized(onlineUsers)
		{
			onlineUsers.put(userName,String.valueOf(new Date().getTime()/1000));
		}
	}
	
	/**
	 * 删除在线用户
	 * @param userName
	 */
	public void deleteOnlineUser(String userName)
	{
		synchronized(onlineUsers)
		{
			onlineUsers.remove(userName);
		}
	}
	
	/**
	 * 返回在线用户列表
	 * @return
	 */
	public HashMap<String, String> getOnlineUserMap()
	{
		return onlineUsers;
	}
	
	/**
	 * 返回系统中所有的session
	 * @return
	 */
	public HashMap<String, HttpSession> getOnlineSession() {
		return onlineSession;
	}
	
	/**
	 * 更新在线用户列表
	 * @author wp
	 *
	 */
	class OnlineTask extends TimerTask
	{
		public void run()
		{
			synchronized(onlineUsers)
			{
				Iterator it = onlineUsers.keySet().iterator();
				long now = new Date().getTime()/1000;
				while(it.hasNext())
				{
					String userName = (String)it.next();
					long visitTime = Long.parseLong((String)onlineUsers.get(userName));
					//有五分钟没有访问系统，认为已经退出
					if(visitTime+5*60<=now)
					{
						onlineUsers.remove(userName);
						synchronized(onlineSession)
						{
							onlineSession.remove(userName);
						}
						
					}
				}
			}
		}
	}	
	
}

