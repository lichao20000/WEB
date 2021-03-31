package com.linkage.module.gwms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.UserInfo;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;


public class BatchResumeThread extends Thread {

	private static Logger logger = LoggerFactory.getLogger(BatchResumeThread.class);
	private long task_id;

	public BatchResumeThread(long task_id) {
		this.task_id = task_id;
	}

	@Override
	public void run() 
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select task_id ");
		}else{
			sql.append("select * ");
		}
		sql.append("from gw_device_resume_task where task_id="+task_id+" and status=0 ");
		Map<String, String> map = DBOperation.getRecord(sql.getSQL());
		if (map==null || map.size()<=0) {
			logger.warn("任务[{}]已经执行完成或者执行中，结束",task_id);
			return;
		}
		updateTaskStatus(task_id,2);
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select c.device_id,c.gw_type,c.customer_id,c.oui,c.device_serialnumber,c.device_name ");
		pSql.append("from gw_device_resume_task_dev b,tab_gw_device c ");
		pSql.append("where b.device_id=c.device_id and b.task_id="+task_id+" and b.status=0 ");
		
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(pSql.getSQL());
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, String> devInfoMap=list.get(i);
			// 得到设备类型
			String gw_type = StringUtil.getStringValue(devInfoMap, "gw_type");
			String device_id = StringUtil.getStringValue(devInfoMap, "device_id");
			// 恢复出厂设置成功，更新用户标识
			//湖北恢复出厂设置，先更新业务用户表的状态
			updateCustStatus(gw_type,StringUtil.getStringValue(devInfoMap, "customer_id"));
			// TR069恢复出厂设置,调用配置模块业务接口
			PreProcessInterface ppc = CreateObjectFactory.createPreProcess(gw_type);
			UserInfo[] userInfo = new UserInfo[1];
			userInfo[0] = new UserInfo();
			userInfo[0].deviceId = device_id;
			userInfo[0].oui = StringUtil.getStringValue(devInfoMap, "oui");
			userInfo[0].deviceSn = StringUtil.getStringValue(devInfoMap,"device_serialnumber");
			userInfo[0].gatherId = "factory_reset";
			userInfo[0].userId = StringUtil.getStringValue(devInfoMap, "customer_id");
			userInfo[0].servTypeId = "0";
			userInfo[0].operTypeId = "1";
			int ret = ppc.processServiceInterface(userInfo);
			logger.warn("ResetOrRebootAct.reset PreProcess({})", ret);
			if (1 == ret)
			{
				logger.warn("device_id[{}]恢复出厂 success.",device_id);
				updateTaskDevStatus(task_id, device_id, 1);
			}
			else
			{
				logger.warn("device_id[{}]恢复出厂 error.",device_id);
				updateTaskDevStatus(task_id, device_id, 2);
			}
		}
		updateTaskStatus(task_id,1);
	}
	
	private void updateCustStatus(String gwType, String userId)
	{
		String tableName = null;
		if (Global.GW_TYPE_BBMS.endsWith(gwType))
		{
			tableName = "egwcust_serv_info";
		}
		else
		{
			tableName = "hgwcust_serv_info";
		}
		if (StringUtil.IsEmpty(userId))
		{
			logger.warn("user_id is empty, no need to update table[{}] set open_status = 0",tableName);
			return;
		}
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("update "+tableName);
		pSql.append(" set open_status=0,updatetime=? where user_id=? and serv_status in (1,2) and open_status!=0");
		int index = 0;
		pSql.setLong(++index, new DateTimeUtil().getLongTime());
		pSql.setInt(++index, StringUtil.getIntegerValue(userId));
		int updateRows = DataSetBean.executeUpdate(pSql.getSQL());
		logger.warn("update table[{}] rows[{}].", tableName, updateRows);
	}
	
	private void updateTaskStatus(long task_id,int status)
	{
		long time = new DateTimeUtil().getLongTime();
		PrepareSQL pSql1 = new PrepareSQL();
		pSql1.setSQL("update gw_device_resume_task set status="+status+",update_time="+time+" where task_id="+task_id+"");
		DataSetBean.executeUpdate(pSql1.getSQL());
	}
	
	private void updateTaskDevStatus(long task_id, String device_id,int status)
	{
		long time = new DateTimeUtil().getLongTime();
		PrepareSQL pSql1 = new PrepareSQL();
		pSql1.setSQL("update gw_device_resume_task_dev set status="+status+",update_time="+time+" where task_id="+task_id+" and device_id='"+device_id+"'");
		DataSetBean.executeUpdate(pSql1.getSQL());
	}
}
