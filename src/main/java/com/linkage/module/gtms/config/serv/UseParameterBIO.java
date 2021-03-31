package com.linkage.module.gtms.config.serv;

import java.util.HashMap;
import java.util.List;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.UserInfo;

import com.linkage.module.gtms.config.dao.UseParameterDAO;
import com.linkage.module.gwms.util.corba.PreProcessCorba;

public class UseParameterBIO 
{
	private static Logger logger = LoggerFactory.getLogger(UseParameterBIO.class);
	private UseParameterDAO dao;
	// 回传消息
	private String msg = null;
	// 山东联通记录上网方式更改日志
	private static String writeLogSql = "insert into tab_oper_log("
			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
			+ ",operation_object"
			+ ",operation_result,result_id,log_sub_type) values(?,?,?,?,?,?,?,?,?)";

	/**
	 * 根据宽带账号获取设备信息
	 */
	public List<HashMap<String, String>> queryDeviceByUsername(String userName){
		return dao.queryDeviceByUsername(userName);
	}
	
	/**
	 * 业务下发
	 */
	public String doConfig(long uid,String netUsername,String netusernamepwd,UserInfo userInfo)
	{
		logger.debug("UseParameterBIO.doConfig()");
		
		// 入数据库
		int result = dao.doConfig(uid,netUsername,netusernamepwd);
		if(result == -1){
			msg = "上网方式更新失败";
			return msg;
		}else{
			msg = "上网方式更新成功";
			
			result=new PreProcessCorba("1").processServiceInterface(userInfo);
			if(1==result){
				msg += "；业务下发，调用配置模块(serv)成功";
				return msg;
			}else{
				msg += "；业务下发，调用配置模块(serv)失败";
				return msg;
			}
		}
	}
	/**
	 *@描述 记录山东联通上网方式修改为路由产生的日志，通过 log_sub_type = 1 来区分跟普通日志
	 *@参数  [userId, userIp, operationObject, operationResult, resultId]
	 *@返回值  void
	 *@创建人  lsr
	 *@创建时间  2019/8/26
	 *@throws
	 *@修改人和其它信息
	 */
	public void addOperationLog(Long userId, String userIp, String operationObject,String operationResult,int resultId){
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,userId);
		psql.setString(2,userIp);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,operationObject);
		psql.setString(6,"上网方式修改为路由");
		psql.setString(7,operationResult);
		psql.setInt(8,resultId);
		psql.setInt(9,1);
		DBOperation.executeUpdate(psql.getSQL());
	}

	public UseParameterDAO getDao() {
		return dao;
	}

	public void setDao(UseParameterDAO dao) {
		this.dao = dao;
	}
	
}
