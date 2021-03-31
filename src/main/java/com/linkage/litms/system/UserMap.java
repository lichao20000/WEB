package com.linkage.litms.system;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;

/**
 * 系统中特定用户的容器
 * @author wp
 *
 */
public class UserMap {
	//系统中由于登录失败而锁定的用户容器
	private static HashMap<String, String> lockUsers = new HashMap<String, String>(10);
	
	//单态实例对象
	private static UserMap instance = null;
	
	//系统中的在线用户
	private static HashMap onlineUsers = new HashMap(20);
	
	//放系统中存活的session
	private static HashMap onlineSession = new HashMap(20);
	
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
		onlineUser_timer = new Timer();		
		onlineUser_timer.schedule(new OnlineTask(),10*60*1000,10*60*1000);		
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
	 * 把userName的可以登录的时间加入锁定用户队列
	 * @param userName  用户名
	 * @param allowLoginTime  该用户可以登录的时间
	 */
	public void addLockUser(String userName,long allowLoginTime)
	{
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
	 * AHDX_ITMS-REQ-20190620YQW-001（web用户改造） 超过三月的用户加入锁定用户队列
	 *
	 * @param userName  用户名
	 * @param allowLoginTime  该用户可以登录的时间
	 */
	public void addLockOutdatedUser() {
		Long nowTime = new Date().getTime() / 1000;
		String initTime = StringUtil.getStringValue(lockUsers, "init_time");
		// 初始化时间在一天之内不用再次初始化
		if (!StringUtil.IsEmpty(initTime)  && Long.parseLong(initTime) + 86400 >= nowTime) {
			return;
		}
		
		// 将超过三月的用户锁定
		String sql = "select  acc_loginname, acc_pwd_time from tab_accounts where acc_pwd_time < ? and creationdate < ?";
		PrepareSQL pSql = new PrepareSQL(sql);
		pSql.setString(1,  new DateTimeUtil().getNextDateTime("day", -90));
		pSql.setString(2,  new DateTimeUtil().getNextDateTime("day", -90));
		List<HashMap<String, String>> result = DBOperation.getRecords(pSql.getSQL());
		for (HashMap<String, String> map : result) {
			String userName = StringUtil.getStringValue(map, "acc_loginname");
			if (StringUtil.IsEmpty(userName)) {
				continue;
			}
			synchronized(lockUsers) {
				lockUsers.put(userName, String.valueOf(nowTime + 86400 * 90));
			}
		}
		synchronized(lockUsers) {
			lockUsers.put("init_time", String.valueOf(nowTime));
		}
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
	 *  AHDX_ITMS-REQ-20190620YQW-001（web用户改造） 从锁用户队列中删除解锁用户并修改密码
	 * @param userName
	 */
	public void deleteLockOutdatedUser(String userName) {
		String sql = "update tab_accounts set acc_pwd_time = ? where acc_loginname = ?";
		
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, new DateTimeUtil().getLongDate());
		psql.setString(2, userName);
		DBOperation.executeUpdate(psql.getSQL());

		synchronized(lockUsers) {
			lockUsers.remove(userName);
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
	public HashMap getOnlineUserMap()
	{
		return onlineUsers;
	}
	
	/**
	 * 返回锁定用户列表
	 * @return
	 */
	public HashMap getLockUsers()
	{
		return lockUsers;
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
					//有二十分钟没有访问系统，认为已经退出
					if(visitTime+20*60<=now)
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
