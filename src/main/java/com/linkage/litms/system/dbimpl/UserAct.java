package com.linkage.litms.system.dbimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserMap;
import com.linkage.litms.system.UserRes;

public class UserAct 
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(UserAct.class);
  private static String usersByAreaNameSQL ="select c.acc_oid,c.acc_loginname from tab_acc_area a,tab_area b,tab_accounts c  where a.area_id= b.area_id and a.acc_oid=c.acc_oid and b.area_name=?";
  
  private static String usersByUserPIDSQL = "select acc_oid from tab_accounts where creator=?";
  
  
  
  
  private PrepareSQL pSQL = null;
  
  /**
   * 构造函数
   *
   */
  public UserAct()
  {
	  if (pSQL == null)
	  {
		pSQL = new PrepareSQL();
	  }	  
  }
  
  /**
   * 根据userPIDList获取其创建的所有用户,包括自身
   * @param userPIDList
   * @param userIDList
   * @return
   */
  private ArrayList getUsersByUserPID(ArrayList userPIDList,ArrayList userIDList)
  {
	  Cursor cursor = null;
	  Map field = null;
	  ArrayList tempUserIDList = new ArrayList(10);
	  tempUserIDList.clear();
	  String selfUserID ="";

	  for (int i = 0; i < userPIDList.size(); i++) 
	  {
		    selfUserID = (String)userPIDList.get(i);
			pSQL.setSQL(usersByUserPIDSQL);
			pSQL.setInt(1, Integer.parseInt(selfUserID));
			cursor = DataSetBean.getCursor(pSQL.getSQL());
			field = cursor.getNext();

			// 存在子节点
			if (field != null) 
			{
				while (field != null) 
				{
					tempUserIDList.add(field.get("acc_oid"));
//					logger.debug("acc_oid:"+field.get("acc_oid"));
//					logger.debug(userIDList.toString());
//					logger.debug(userIDList.contains(field.get("acc_oid")));
					if (!userIDList.contains(field.get("acc_oid"))) {
						userIDList.add(field.get("acc_oid"));
					}

					field = cursor.getNext();
				}
				userIDList.addAll(getUsersByUserPID(tempUserIDList,
						userIDList));                
				tempUserIDList.clear();				
			} 
			userIDList.add(selfUserID);
		}
		// 剔除重复对象
		ArrayList temp_list = new ArrayList();
		temp_list.clear();

		for (int s = 0; s < userIDList.size(); s++) {
			if (!temp_list.contains(userIDList.get(s))) {
				temp_list.add(userIDList.get(s));
			}
		}
		userIDList.clear();
		userIDList = null;
		
		return temp_list;
  }
  
  /**
   * 获取UserID用户创建的所有用户，包括自身
   * @param userID  用户帐号ID
   * @return
   */
  public ArrayList getSubusersByUserID(long userID)
  {
	  ArrayList userIDlist = new ArrayList(20);
	  ArrayList userPIDlist = new ArrayList(20);
	  userPIDlist.add(String.valueOf(userID));
	  userIDlist = getUsersByUserPID(userPIDlist,userIDlist);	
	  logger.debug("getSubusersByUserID:"+userIDlist.toString());
	  return userIDlist;
  }
  
  
  /**
   * 获取某个域下的所有的用户信息
   * @param AreaName
   * @return
   */
  public HashMap getUsersByArea(String areaName)
  {
	  HashMap usersMap = new HashMap(10);
	  Cursor cursor = null;
	  Map fields = null;
	  pSQL.setSQL(usersByAreaNameSQL);
	  pSQL.setString(1,areaName);
	  cursor = DataSetBean.getCursor(pSQL.getSQL());
	  fields = cursor.getNext();
	  String acc_oid ="";
	  String acc_name="";
	  if(null!=fields)
	  {
		  while(null!=fields)
		  {
			  acc_oid = (String)fields.get("acc_oid");
			  acc_name =(String)fields.get("acc_loginname");
			  if(null!=acc_oid&&!"".equals(acc_oid)&&null!=acc_name&&!"".equals(acc_name))
			  {
				  usersMap.put(acc_oid,acc_name);
			  }
			  fields = cursor.getNext();
		  }
	  }	 
	  fields = null;
	  cursor = null;
	  return usersMap;
  }
  
  /**
   * 获取userid所创建的并属于域areaName的用户
   * @param userID
   * @param areaName
   * @return
   */
  public HashMap getUsersByAreaByUserID(long userID,String areaName)
  {
//	  logger.debug("userID:"+userID+"|areaName:"+areaName);
	  HashMap usersMap = new HashMap(10);
	  HashMap tempUsersMap = getUsersByArea(areaName);
	  String acc_oid ="";
	  String acc_name="";
	  if(0!=tempUsersMap.size())
	  {
//		  logger.debug("getUsersByArea_size:"+tempUsersMap.size());
		  ArrayList subUsers = getSubusersByUserID(userID);
//		  logger.debug("subUsers_size:"+subUsers.size());
		  for(int i=0;i<subUsers.size();i++)
		  {
			  acc_oid = (String)subUsers.get(i);			  
			  if(tempUsersMap.containsKey(acc_oid))
			  {
				  acc_name = (String)tempUsersMap.get(acc_oid);
				  usersMap.put(acc_oid,acc_name);
			  }
		  }
		  subUsers = null;
	  }
	  tempUsersMap = null;	  
	  return usersMap;
  } 
  
  /**
   * 获取系统中的在线用户的详细信息
   * @return
   */
  public Cursor getOnlineUserInfo(HttpServletRequest req)
  {
	  HashMap onlineUsers = UserMap.getInstance().getOnlineUserMap();
	  String userNameStr = "";
	  String acc_oid="";
	  String acc_oidStr=null;
	  synchronized(onlineUsers)
	  {
		  Iterator it = onlineUsers.keySet().iterator();
		  while(it.hasNext())
		  {
		  	String userName = (String)it.next();
		  	userNameStr +=",'"+userName+"'";	
		  }
	  }	  
	  if(userNameStr.length()>0)
	  {
	  	userNameStr = userNameStr.substring(1);
	  }
	  else
	  {
	  	userNameStr = null;
	  }	  
	  
	  String sql="select a.acc_oid,a.acc_loginname,b.per_mobile,b.per_email,a.acc_login_ip,b.per_name,b.per_city from tab_accounts a,tab_persons b where a.acc_oid = b.per_acc_oid and a.acc_loginname in("+userNameStr+")";
	  PrepareSQL psql = new PrepareSQL(sql);
	  psql.getSQL();
	  Cursor cursor = DataSetBean.getCursor(sql);
	  Map fields = cursor.getNext();
	  HttpSession session = req.getSession();
	  Object obj = session.getAttribute("curUser");
	  long   userID = -2;
	  if(null!=obj)
	  {
		  userID = ((UserRes)obj).getUser().getId();
	  }
	  //不是admin用户的情况，要权限控制
	  if(1!=userID)
	  {
		  ArrayList subUsers = getSubusersByUserID(userID);
		  while(null!=fields)
		  {
			  acc_oid = (String)fields.get("acc_oid");
			  if(subUsers.contains(acc_oid))
			  {
				  if(null==acc_oidStr)
				  {
					  acc_oidStr=acc_oid;
				  }
				  else
				  {
					  acc_oidStr+=","+acc_oid;
				  }
			  }
			  
			  fields = cursor.getNext();
		  }
		  
		 sql="select a.acc_oid,a.acc_loginname,b.per_mobile,b.per_email from tab_accounts a,tab_persons b where a.acc_oid = b.per_acc_oid"
			 +"  and acc_oid in("+acc_oidStr+")";
		 psql = new PrepareSQL(sql);
		 psql.getSQL();
		 cursor = DataSetBean.getCursor(sql);
	  }	  
	  
	  cursor.Reset();
	  
	  return cursor;
  }
 
}
