package com.linkage.module.gtms.stb.resource.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 *
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-12
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class btachImportAdvertisementDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory
			.getLogger(btachImportAdvertisementDAO.class);
	public int insertServAccount(List<Map<String,String>> dataList,String group_id,long taskId)
	{
		long currTime = new Date().getTime() / 1000L;
		ArrayList<String> sqlList = new ArrayList<String>();
		int index = 1;
		for(int i=0;i<dataList.size();i++)
		{
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.append("insert into stb_tab_pic_account(task_id,serv_account,status,add_time)values(?,?,?,?)");
			pSQL.setLong(1, taskId);
			pSQL.setString(2, dataList.get(i).get("serv_account"));
			pSQL.setInt(3, 0);
			pSQL.setLong(4, currTime);
			sqlList.add(pSQL.toString());
			index++;
			if(index >=500){
				int rs = 1;
				if(Global.CQDX.equals(Global.instAreaShortName))
				{
					rs= DBOperation.executeUpdate(sqlList,"xml-test");

				}
				else
				{
					rs = DBOperation.executeUpdate(sqlList, "xml-picture");
				}
				if(rs<=0){
					return rs;
				}
				index =1;
				sqlList.clear();
			}
		}
		if(Global.CQDX.equals(Global.instAreaShortName))
		{
		int rs= DBOperation.executeUpdate(sqlList,"xml-test");
		return rs;
		}
		else
		{
			int rs = DBOperation.executeUpdate(sqlList, "xml-picture");
			return rs;
		}
	}
	/**
	 * 查询所有的分组
	 */
	public List getGroupId()
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select group_id,group_name from stb_tab_usergroup");
		if(Global.CQDX.equals(Global.instAreaShortName))
		{
			return DBOperation.getRecords(pSQL.getSQL(), "xml-test");
		}else
		{
			return DBOperation.getRecords(pSQL.getSQL());
		}

	}
	public List<Map<String,String>> getServerParameter()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select server_url, access_user, access_passwd, server_url from stb_tab_picture_file_server  where 1=1 and file_type = 1 and server_name = '图片服务器' ");
		return jt.queryForList(psql.getSQL());
	}

	public int OpenDeviceShowPicConfig(long taskId,String taskName,String group_id,String file_path
			,String booturl,String starturl,String authturl,long acc_oid,String priority,long add_time,String Invalid_time,String url)
	{
		logger.warn("stbBootAdvertisementBIO(taskId={},taskName={},group_id={},file_path={},booturl={},starturl={},authturl={},acc_oid={},priority={},add_time={},Invalid_time={},url={})",
	    		new Object[]{taskId,taskName,group_id,file_path,booturl,starturl,authturl,acc_oid,priority,add_time,Invalid_time,url});
		DateTimeUtil dt = new DateTimeUtil(Invalid_time);
		String status1 = LipossGlobals.getLipossProperty("status") ;
		ArrayList<String> sqllist = new ArrayList<String>();
		PrepareSQL sql1 = new PrepareSQL("insert into  stb_tab_pic_task(");
		sql1.append("task_id,task_name,group_id,file_path,sd_qd_pic_url,");
		sql1.append(" sd_kj_pic_url,sd_rz_pic_url,acc_oid,priority,status,add_time,Invalid_time)");
		sql1.append(" values(?,?,?,?,?,?,?,?,?,?,?,?)");
		sql1.setLong(1, taskId);
		sql1.setString(2, taskName);
		if(StringUtil.IsEmpty(group_id))
		{
			sql1.setString(3, "");
		}else
		{
			sql1.setString(3, group_id);
		}
		if(StringUtil.IsEmpty(file_path))
		{
			sql1.setString(4, "");
		}else
		{
			sql1.setString(4, file_path);
		}
		if(StringUtil.IsEmpty(booturl))
		{
			sql1.setString(5, "");
		}else
		{
			sql1.setString(5, url+booturl);
		}
		if(StringUtil.IsEmpty(starturl))
		{
			sql1.setString(6, "");
		}else
		{
			sql1.setString(6, url+starturl);
		}
		if(StringUtil.IsEmpty(authturl))
		{
			sql1.setString(7, "");
		}else
		{
			sql1.setString(7, url+authturl);
		}
		sql1.setLong(8, acc_oid);
		if(StringUtil.IsEmpty(priority))
		{
			sql1.setInt(9, 1);
		}else
		{
			sql1.setInt(9, Integer.valueOf(priority));
		}

		sql1.setInt(10, Integer.valueOf(status1));
		sql1.setLong(11, add_time);
		sql1.setLong(12, dt.getLongTime());
		sqllist.add(sql1.getSQL());
		int ier=0;
		if(Global.CQDX.equals(Global.instAreaShortName))
		{
			ier= DBOperation.executeUpdate(sqllist, "xml-test");
		}else
		{
			ier = DBOperation.executeUpdate(sqllist, "xml-picture");
		}



		if (ier> 0) {
			logger.warn("任务定制：  成功");
			return 1;
		} else {
			logger.warn("任务定制：  失败");
			return 0;
		}
	}
	/**
	 * 获取设备信息
	 * @param param
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDeviceInfo(String param)
	{
		Map<String, String> taskMap = null;
		String sql = "select distinct(serv_account),customer_id from stb_tab_customer  where serv_account = ?  ";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setString(1, param);
		try
		{
			taskMap = jt.queryForMap(taskSql.getSQL());
		}
		catch (DataAccessException e)
		{
			taskMap = null;
		}
		return taskMap;
	}
}
