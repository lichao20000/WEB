package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.Global;
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
public class BatchRestartDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BatchRestartDAO.class);
	
	/**
	 * 设备id
	 * @param dataList
	 * @param user_Account
	 * @return
	 */
	public int insertDeviceId(List<String> dataList,String user_Account,String batchType)
	{
		long lnow = new Date().getTime() / 1000;
		String total=LipossGlobals.getLipossProperty("batchRestart.fileRow");
		if(Integer.valueOf(total)>=dataList.size()+1)
		{
			//今日导入总数
			int a=total(batchType);
			//导入数加今日导入总数是否超过40万
			int b=a+Integer.valueOf(dataList.size());
			
			String restartCount=LipossGlobals.getLipossProperty("batchRestart.restartCount");
			if(a>=Integer.valueOf(restartCount))
			{
				return -6;
			}else if(b>Integer.valueOf(restartCount))
			{
				return -7;
			}
			else
			{
				ArrayList<String> sqlList = new ArrayList<String>();
				for (int i = 0;i<dataList.size();i++)
				{
					PrepareSQL pSQL = new PrepareSQL();
					//ArrayList<String> sqlList = new ArrayList<String>();
					String task_id = UUID.randomUUID().toString();
					String table = "task_batch_restart";
					if("4".equals(batchType)){
						table = "stb_task_batch_restart";
					}
					String queryDevIdByLoid = "";
					String device_id = "";
					if(Global.NXDX.equals(Global.instAreaShortName))
					{
						if("1".equals(batchType))
						{
							queryDevIdByLoid = "select a.device_id from tab_gw_device a,tab_hgwcustomer b "
									+"where a.device_serialnumber = b.device_serialnumber " +
									"and a.customer_id is not null and b.username='"+dataList.get(i)+"'";
							List rs = DBOperation.getRecords(queryDevIdByLoid);
							if(null != rs && rs.size() != 0){
								logger.warn("get device_id by loid success !");
								Map one = (Map)rs.get(0);
								device_id = String.valueOf(one.get("device_id"));
							}else {
								logger.warn("get device_id by loid fail !");
								continue;
							}
						}
					}
					pSQL.append("insert into " + table + "(task_id,device_id,dev_type,add_time,restart_status,");
					pSQL.append("restart_time,operator) values (?,?,1,?,0,null,?) ");
					pSQL.setString(1, task_id);
					if(Global.NXDX.equals(Global.instAreaShortName)){
						pSQL.setString(2, device_id);
					}else{
						pSQL.setString(2, dataList.get(i));
					}
					pSQL.setLong(3, lnow);
					pSQL.setString(4, user_Account);
					sqlList.add(pSQL.toString());
					//sqlList1.addAll(sqlList);
				}
				return DBOperation.executeUpdate(sqlList,"proxool.xml-report");
			}
		}
			
		return -5;
	}
	
	/**
	 * 导入业务账号，入表后再关联更新重启设备结果表，同时更新导入的业务账号状态
	 */
	public int insertStbDeviceId(List<String> dataList,String user_Account)
	{
		logger.debug("insertStbDeviceId(导入账号量：{},定制账号：{})",dataList.size(),user_Account);
		if(dataList==null || dataList.isEmpty()){
			return 1;
		}
		
		long lnow = new Date().getTime() / 1000;
		ArrayList<String> sqlList = new ArrayList<String>();
		
		for (String serv_account:dataList)
		{
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.append("insert into stb_batch_restart_serv(task_id,serv_account,add_time,status,operator) ");
			pSQL.append("values(?,?,?,?,?) ");
			pSQL.setString(1, UUID.randomUUID().toString());
			pSQL.setString(2,serv_account);	
			pSQL.setLong(3,lnow);
			pSQL.setInt(4,1);//账号状态，1：正常用户，-1:异常用户
			pSQL.setString(5,user_Account);	
			sqlList.add(pSQL.toString());
			
			if(sqlList.size()==200)
			{
				DBOperation.executeUpdate(sqlList,"proxool.xml-stb");
				sqlList.clear();
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(!sqlList.isEmpty()){
			DBOperation.executeUpdate(sqlList,"proxool.xml-stb");
			sqlList.clear();
		}
		
		logger.warn("批量重启账号入表stb_batch_restart_serv完成，继续执行更新设备数据sql");
		PrepareSQL pSQL = new PrepareSQL();
		
		if(DBUtil.GetDB()==3){
			pSQL.append("select a.task_id,c.device_id,c.device_serialnumber,a.add_time,a.operator ");
			pSQL.append("from stb_batch_restart_serv a,stb_tab_customer b,stb_tab_gw_device c ");
			pSQL.append("where a.add_time=? and a.serv_account=b.serv_account ");
			pSQL.append("and b.customer_id=c.customer_id and c.customer_id is not null ");
			pSQL.setLong(1,lnow);
			
			ArrayList<HashMap<String,String>> list=DBOperation.getRecords(pSQL.getSQL(),"proxool.xml-stb");
			if(list!=null && !list.isEmpty())
			{
				ArrayList<String> sql=new ArrayList<String>();
				pSQL = new PrepareSQL();
				pSQL.append("insert into stb_task_batch_restart(task_id,device_id,dev_sn,dev_type,add_time,restart_status,operator) ");
				pSQL.append("values(?,?,?,?,?,?,?) ");
				for(HashMap<String,String> m:list)
				{
					pSQL.setString(1,StringUtil.getStringValue(m,"task_id"));
					pSQL.setString(2,StringUtil.getStringValue(m,"device_id"));
					pSQL.setString(3,StringUtil.getStringValue(m,"device_serialnumber"));
					pSQL.setInt(4,4);
					pSQL.setInt(5,StringUtil.getIntValue(m,"add_time"));
					pSQL.setInt(6,0);
					pSQL.setString(7,StringUtil.getStringValue(m,"operator"));
					
					sql.add(pSQL.getSQL());
					if(sql.size()==200){
						DBOperation.executeUpdate(sql);
						sql.clear();
					}
				}
				
				if(!sql.isEmpty()){
					DBOperation.executeUpdate(sql);
					sql.clear();
				}
			}
		}
		else
		{
			pSQL.append("insert into stb_task_batch_restart(task_id,device_id,dev_sn,dev_type,add_time,restart_status,operator) ");
			pSQL.append("select a.task_id,c.device_id,c.device_serialnumber,4,a.add_time,0,a.operator ");
			pSQL.append("from stb_batch_restart_serv a,stb_tab_customer b,stb_tab_gw_device c ");
			pSQL.append("where a.add_time=? and a.serv_account=b.serv_account ");
			pSQL.append("and b.customer_id=c.customer_id and c.customer_id is not null ");
			pSQL.setLong(1,lnow);
			
			DBOperation.executeUpdate(pSQL.getSQL(),"proxool.xml-stb");
		}
		
		logger.warn("继续执行更新异常账号数据sql");
		pSQL = new PrepareSQL();
		pSQL.append("update stb_batch_restart_serv set status=-1 where task_id not in ");
		pSQL.append("(select task_id from stb_task_batch_restart where add_time=? )");
		pSQL.setLong(1,lnow);
		
		return DBOperation.executeUpdate(pSQL.getSQL(),"proxool.xml-stb");
	}
	
	/**
	 * 查看今日剩余数
	 */
	public int TodayNumber(String batchType)
	{
		//今日导入总数
		int a=total(batchType);
		String restartCount=LipossGlobals.getLipossProperty("batchRestart.restartCount");
		int b=Integer.valueOf(restartCount)-a;
		return b;
	}
	
	/**
	 * 设备序列号
	 * @param dataList
	 * @param user_Account
	 * @return
	 */
	public int insertDevSn(List<String> dataList,String user_Account,String batchType)
	{
		//获取当前时间
		long lnow = new Date().getTime() / 1000;
		String total=LipossGlobals.getLipossProperty("batchRestart.fileRow");
		int show=Integer.valueOf(dataList.size());
		
		if(Integer.valueOf(total)>=(show+1))
		{
			int a=total(batchType);
			//导入数加今日导入总数是否超过40万
			int b=a+Integer.valueOf(dataList.size());
			String restartCount=LipossGlobals.getLipossProperty("batchRestart.restartCount");
			if(a>=Integer.valueOf(restartCount))
			{
				return -6;
			}else if(b>Integer.valueOf(restartCount))
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
					
					String table = "task_batch_restart";
					if("4".equals(batchType)){
						table = "stb_task_batch_restart";
					}
					pSQL.append( "insert into " + table + "(task_id,dev_sn,dev_type,add_time,restart_status,restart_time,operator) values (?,?,2,?,0,null,?)");
					pSQL.setString(1, task_id);
					pSQL.setString(2, dataList.get(i));
					pSQL.setLong(3, lnow);
					pSQL.setString(4, user_Account);
					sqlList.add(pSQL.toString());
				}
				int rs= DBOperation.executeUpdate(sqlList,"proxool.xml-report");
				if(rs>0){
					return rs;
				}else{
					return -10000;
				}
			}
		}
			
		return -5;
	}
	
	public int total(String batchType)
	{
		//获取当前00：00:00时间
		DateTimeUtil dateTimeUtil = new DateTimeUtil();
		long starttime = new DateTimeUtil(dateTimeUtil.getYYYY_MM_DD()).getLongTime();
		//获取第二天00:00:00时间
		long endtime=new DateTimeUtil(new DateTimeUtil(dateTimeUtil.getNextLongTime()*1000).getYYYY_MM_DD()).getLongTime();
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSQL.append("select count(*) ");
		}else{
			pSQL.append("select count(1) ");
		}
		String table = "task_batch_restart";
		if("4".equals(batchType)){
			table = "stb_task_batch_restart";
		}
		pSQL.append("from " + table + " where add_time < ? and add_time > ? ");  
		pSQL.setLong(1, endtime);
		pSQL.setLong(2, starttime);
		return jt.queryForInt(pSQL.getSQL());
	}
}
