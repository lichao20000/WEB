package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-10-11
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchResetDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BatchResetDAO.class);
	/**
	 * 设备id
	 * @param dataList
	 * @param user_Account
	 * @return
	 */
	public int insertDeviceId(List<String> dataList,String user_Account)
	{
		//获取当前时间
		long lnow = new Date().getTime() / 1000;
		String total=LipossGlobals.getLipossProperty("batchReset.fileRow");
		if(Integer.valueOf(total)>=dataList.size()+1)
		{
			//今日导入总数
			int a=total();
			//导入数加今日导入总数是否超过40万
			int b=a+Integer.valueOf(dataList.size());
			
			String resetCount=LipossGlobals.getLipossProperty("batchReset.resetCount");
			if(a>=Integer.valueOf(resetCount))
			{
				return -6;
			}
			else if(b>Integer.valueOf(resetCount))
			{
				return -7;
			}
			else
			{
				ArrayList<String> sqlList = new ArrayList<String>();
				for (int i = 0;i<dataList.size();i++)
				{
					PrepareSQL pSQL = new PrepareSQL();
					String task_id = UUID.randomUUID().toString();
					pSQL.append("insert into task_batch_reset(task_id,device_id,dev_type,");
					pSQL.append("add_time,reset_status,reset_time,operator) values(?,?,1,?,0,null,?) ");
					pSQL.setString(1, task_id);
					pSQL.setString(2, dataList.get(i));
					pSQL.setLong(3, lnow);
					pSQL.setString(4, user_Account);
					sqlList.add(pSQL.toString());
					//sqlList1.addAll(sqlList);
				}
				return DBOperation.executeUpdate(sqlList);
			}
		}
		return -5;
	}
	
	/**
	 * 查看今日剩余数
	 */
	public int TodayNumber()
	{
		//今日导入总数
		int a=total();
		String resetCount=LipossGlobals.getLipossProperty("batchReset.resetCount");
		int b=Integer.valueOf(resetCount)-a;
		return b;
	}
	/**
	 * 设备序列号
	 * @param dataList
	 * @param user_Account
	 * @return
	 */
	public int insertDevSn(List<String> dataList,String user_Account)
	{
		//获取当前时间
		long lnow = new Date().getTime() / 1000;
		String total=LipossGlobals.getLipossProperty("batchReset.fileRow");
		int show=Integer.valueOf(dataList.size());
		if(Integer.valueOf(total)>=(show+1))
		{
			int a=total();
			//导入数加今日导入总数是否超过40万
			int b=a+Integer.valueOf(dataList.size());
			String resetCount=LipossGlobals.getLipossProperty("batchReset.resetCount");
			logger.warn("batchReset.resetCount="+resetCount);
			if(a>=StringUtil.getIntegerValue(resetCount))
			{
				return -6;
			}
			else if(b>StringUtil.getIntegerValue(resetCount))
			{
				return -7;
			}
			else
			{
				ArrayList<String> sqlList = new ArrayList<String>();
				for (int i = 0;i<dataList.size();i++)
				{
					PrepareSQL pSQL = new PrepareSQL();
					
					String task_id = UUID.randomUUID().toString();
					pSQL.append("insert into task_batch_reset(task_id,dev_sn,dev_type,add_time,");
					pSQL.append("reset_status,reset_time,operator) values(?,?,2,?,0,null,?) ");
					pSQL.setString(1, task_id);
					pSQL.setString(2, dataList.get(i));
					pSQL.setLong(3, lnow);
					pSQL.setString(4, user_Account);
					sqlList.add(pSQL.toString());
				}
				int rs= DBOperation.executeUpdate(sqlList);
				if(rs>0){
					return rs;
				}else{
					return -10000;
				}
			}
		}
		return -5;
	}
	
	public int total()
	{
		//获取当前00：00:00时间
		DateTimeUtil dateTimeUtil = new DateTimeUtil();
		long starttime = new DateTimeUtil(dateTimeUtil.getYYYY_MM_DD()).getLongTime();
		//获取第二天00:00:00时间
		long endtime=new DateTimeUtil(new DateTimeUtil(dateTimeUtil.getNextLongTime()*1000).getYYYY_MM_DD()).getLongTime();
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSQL.append("select count(*)");
		}else{
			pSQL.append("select count(1) ");
		}
		pSQL.append("from task_batch_reset where add_time < ? and add_time > ? ");
		pSQL.setLong(1, endtime);
		pSQL.setLong(2, starttime);
		return jt.queryForInt(pSQL.getSQL());
	}
}
