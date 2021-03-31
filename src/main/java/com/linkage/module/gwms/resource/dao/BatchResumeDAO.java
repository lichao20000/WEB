
package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

public class BatchResumeDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(BatchResumeDAO.class);

	/**
	 * 查询设备信息
	 * @param sqlSpell
	 */
	public  ArrayList<HashMap<String, String>> getDevIds(String sqlSpell){
		return DBOperation.getRecords(sqlSpell);
	}
	
	/**
	 * 增加任务信息
	 * @param name
	 * @param taskid
	 * @param accoid
	 * @param addtime
	 * @param starttime
	 * @param endtime
	 * @param enddate
	 * @return
	 */
	public int addTask(String task_name,long taskid, long accoid, String param, int type,int status){
		
		PrepareSQL psql = new PrepareSQL(
		"insert into gw_device_resume_task(task_id,task_name,acc_oid,add_time,param,type,status) values(?,?,?,?,?,?,?)");
		psql.setLong(1, taskid);
		psql.setString(2, task_name);
		psql.setLong(3, accoid);
		psql.setLong(4, taskid);
		psql.setString(5, param);
		psql.setInt(6, type);
		psql.setInt(7, status);
		
		return jt.update(psql.getSQL());
		
	}
	
	
	/**
	 * 把设备序列号插入临时表
	 * @param fileName
	 * @param dataList
	 */
	public void insertTmp(String fileName, List<String> dataList) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		for (int i = 0; i < dataList.size(); i++) {
			psql = new PrepareSQL();
			psql.append("insert into tab_temporary_device"
					+ "(filename,device_serialnumber)" + " values ('" + fileName + "','"
					+ dataList.get(i) + "')");
			sqlList.add(psql.getSQL());
		}
		int res = 0;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		} else {
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			if(sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int resTmp = DBOperation.executeUpdate(sqlListTmp);
						if(resTmp>0){
							res += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int resTmp = DBOperation.executeUpdate(sqlListTmp);
				if(resTmp>0){
					res += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			logger.info("====成功插入tab_temporary_device表"+res+"条数据====");
		}
	}
	
	
	/**
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getTaskDevList(String filename) {

		PrepareSQL psql = new PrepareSQL();
		
		psql.append(" select a.device_id, a.oui, a.device_serialnumber ");
		psql.append(" from tab_gw_device a ,tab_temporary_device b where  ");
		psql.append(" a.device_id=b.device_serialnumber  ");
		psql.append(" and b.filename='"+filename+"'");
		
		
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
		if(null==list || list.size()==0){
			return null;
		}
		
		return list;
	}
	
	public ArrayList<String> sqlList(List<HashMap<String, String>> devList,long taskid){
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < devList.size(); i++) {
			String device_serialnumber = String.valueOf(devList.get(i).get("device_serialnumber"));
			String device_id = String.valueOf(devList.get(i).get("device_id"));
			String oui = String.valueOf(devList.get(i).get("oui"));
			
			PrepareSQL pSql = new PrepareSQL();
			pSql.append(" insert into gw_device_resume_task_dev (task_id,device_id,device_serialnumber,oui,status,add_time)");
			pSql.append("  values(?,?,?,?,?,?)");
			
			pSql.setLong(1, taskid);
			pSql.setString(2, device_id);
			pSql.setString(3, device_serialnumber);
			pSql.setString(4, oui);
			pSql.setLong(5, 0);
			pSql.setLong(6, System.currentTimeMillis()/1000);
			sqlList.add(pSql.getSQL());
		}
		return sqlList;
	}
	
	
	/**
	 * 批量插入设备信息
	 * @param sqlList
	 */
	public int insertTaskDev(ArrayList<String> sqlList){
		return DBOperation.executeUpdate(sqlList);
	}

}
