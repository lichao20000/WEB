
package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class BatchSoftRestartDAO extends SuperDAO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(BatchSoftRestartDAO.class);

	public void insertTask(long task_id,long add_time,long starttime,long accoid,String task_desc,int status)
	{
		logger.warn("insertTask start");
		String sql = "insert into gw_device_restart_task(task_id,add_time,start_time,account_id,task_desc,status) values(?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, task_id);
		psql.setLong(2, add_time);
		psql.setLong(3, starttime);
		psql.setLong(4, accoid);
		psql.setString(5, task_desc);
		psql.setLong(6, status);
		
		jt.execute(psql.getSQL());
	}
	
	
	public void insertTmp(long task_id,String[] deviceId,long  add_time)
	{
		List<String> tempList = new ArrayList<String>();
		for (int i= 0;i<deviceId.length;i++)
		{
			PrepareSQL taskSql = new PrepareSQL("insert into gw_device_restart_temp (task_id, device_id, add_time) values(?,?,?)");
	    	
	    	taskSql.setLong(1, task_id);
	    	taskSql.setString(2, deviceId[i]);
	    	taskSql.setLong(3, add_time);
			tempList.add(taskSql.getSQL());
		}
		
		int[] ier = doBatch(tempList);
		if (ier != null && ier.length > 0) {
			logger.debug("批量入库成功");
		} else {
			logger.debug("批量入库失败");
		}
	}
	
	public void deleteSameData()
	{
		logger.warn("deleteSameData");
		PrepareSQL taskSql = new PrepareSQL("delete from gw_device_restart_batch where device_id in (select device_id from gw_device_restart_temp)");
		jt.execute(taskSql.getSQL());
	}
	
	public void exportDataToBatch()
	{
		logger.warn("exportDataToBatch");
		PrepareSQL taskSql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			taskSql.append("select task_id,device_id,add_time from gw_device_restart_temp");
			List list=jt.queryForList(taskSql.getSQL());
			
			if(list!=null && !list.isEmpty())
			{
				taskSql.append("insert into gw_device_restart_batch(task_id,device_id,add_time) ");
				taskSql.append("values(?,?,?) ");
				String[] sql=new String[200];
				for(int i=0;i<list.size();i++){
					Map m=(Map)list.get(i);
					taskSql.setString(1,StringUtil.getStringValue(m,"task_id"));
					taskSql.setString(2,StringUtil.getStringValue(m,"device_id"));
					taskSql.setInt(3,StringUtil.getIntValue(m,"add_time"));
					
					sql[i%200]=taskSql.getSQL();
					if(i%200==199){
						jt.batchUpdate(sql);
						sql=null;
						sql=new String[200];
					}
				}
				
				if(sql[0]!=null){
					jt.batchUpdate(sql);
					sql=null;
				}
			}
		}else{
			taskSql.append("insert into gw_device_restart_batch select * from gw_device_restart_temp");
			jt.execute(taskSql.getSQL());
		}
	}

	public void truncateTmp()
	{
		logger.warn("truncateTmp");
		PrepareSQL taskSql = new PrepareSQL("truncate table gw_device_restart_temp");
		jt.execute(taskSql.getSQL());
	}
	
	public void updateBatchSuccess(long add_time)
	{
		logger.warn("updateBatchSuccess");
		PrepareSQL taskSql = new PrepareSQL("update gw_device_restart_task set status = 1 where task_id = ?");
		taskSql.setLong(1, add_time);
		jt.execute(taskSql.getSQL());
	}
}
