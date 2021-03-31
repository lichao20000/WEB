package com.linkage.module.gtms.config.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class DelWanConnToolsDAO extends SuperDAO {
	
	/**
	 * 插入数据
	 * @param jsonToObject
	 */
	@SuppressWarnings("rawtypes")
	public long insertTabDelWanConn(long userId,List<Map> jsonToObject) {
		int executeUpdate = -1;
		long taskid = new DateTimeUtil().getLongTime();
		ArrayList<String> sqlList = new ArrayList<String>();
		// 入库任务表
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_delwanconn_task(task_id,acc_oid,add_time,status,update_time) values(?,?,?,?,?)");
		psql.setLong(1, taskid);
		psql.setLong(2, userId);
		psql.setLong(3, taskid);
		// --0 未执行
		psql.setInt(4, 0);
		psql.setLong(5, taskid);
		sqlList.add(psql.getSQL());
		// 入库明细表
		if (null!=jsonToObject && jsonToObject.size()>0) {
			for (Map<String,String> infoStr : jsonToObject) {
					String loid = infoStr.get("loid");
					String serv_type = infoStr.get("serv_type");
					String vlanid = infoStr.get("vlanid");
					// task_id add_time  status  update_time
					psql = new PrepareSQL(" insert into tab_delwanconn_detail (task_id,loid,vlanid,serv_type,add_time,status,update_time ");
				    psql.append(") values(?,?,?,?,?, ?,?)");
					psql.setLong(1, taskid);
					psql.setString(2, loid);
					psql.setString(3, vlanid);
					psql.setString(4, serv_type);
					psql.setLong(5, taskid);
					// --- 状态为0
					psql.setInt(6, 0);
					psql.setLong(7, taskid);
					sqlList.add(psql.getSQL());
				}
		}
		if(sqlList.size()>0){
			executeUpdate = DBOperation.executeUpdate(sqlList);
		}
		return executeUpdate;
	}

}
