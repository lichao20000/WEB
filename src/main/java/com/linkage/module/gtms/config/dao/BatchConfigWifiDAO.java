package com.linkage.module.gtms.config.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年9月27日
 * @category com.linkage.module.gtms.config.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchConfigWifiDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(BatchConfigWifiDAO.class);

	public String doConfig(String deviceIds,int wifiEnable){
	
		logger.warn("doConfig=>deviceIds:{},wifiEnable:{}",new Object[]{deviceIds,wifiEnable});
		
		if(StringUtil.IsEmpty(deviceIds)){
			return "请查询设备";
		}
		ArrayList<String> sqlList = new ArrayList<String>();
		String[] devIdArr = deviceIds.split(",");
		long taskId = System.currentTimeMillis()/1000;
		for(String devId : devIdArr){
			String sqlStr = "insert into tab_wifi_disjunctor_task(task_id,device_id,wifi_enable,status,oper_time,update_time )"
					+ " values(?,?,?,?,?, ?)";
			PrepareSQL pSQL = new PrepareSQL(sqlStr);
			pSQL.setLong(1, taskId);
			pSQL.setString(2, devId);
			pSQL.setInt(3, wifiEnable);
			pSQL.setInt(4, 0);//默认未做状态
			pSQL.setLong(5, taskId);
			pSQL.setLong(6, taskId);
			
			sqlList.add(pSQL.getSQL());
			
			//500条入一次
			if(sqlList.size() > 1000){
				logger.warn("第一次{}条批量入库",sqlList.size());
				DBOperation.executeUpdate(sqlList);
				sqlList.clear();
			}
		}
		logger.warn("{}条sql批量入库",sqlList.size());
		if(null != sqlList && !sqlList.isEmpty() && sqlList.size() > 0){
			int res = DBOperation.executeUpdate(sqlList);
			if(res > 0){
				return "入库成功,等待执行";
			}
		} 
		return "入库成功,等待执行";
	}
}
