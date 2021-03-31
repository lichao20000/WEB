package com.linkage.module.ids.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReportPeroidUpateDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(ReportPeroidUpateDAO.class);

	/**
	 * order task
	 * @param acc_oid
	 * @param currTime
	 * @param reporttimelist
	 * @param targettimelist 
	 */
	public void importReportTask(String acc_oid,long currTime,String reporttimelist,String targettimelist) 
	{
		logger.debug("importReportTask");
		String sql = "insert into tab_report_peroid_task (task_id, acc_oid , add_time , report_timelist,target_timelist) values(?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setLong(1, currTime);
		psql.setLong(2, StringUtil.getLongValue((acc_oid)));
		psql.setLong(3, currTime);
		psql.setInt(4, StringUtil.getIntegerValue((reporttimelist)));
		psql.setInt(5, StringUtil.getIntegerValue((targettimelist)));
		int ier = jt.update(psql.getSQL());
		if(ier>0) {
			logger.warn("任务入库成功");
		} else {
			logger.warn("任务入库失败");
		}
	}
	
	/**
	 * device info 
	 * @param currTime
	 * @param deviceList
	 */
	public void insertTaskDev(long currTime, List<String> deviceList) 
	{
		ArrayList<String> tempList = new ArrayList<String>();
		for (String dev : deviceList)
		{
			String sql = "insert into tab_report_peroid_task_dev(task_id, device_serialnumber,status) values(?,?,?)";
			PrepareSQL psql = new PrepareSQL(sql.toString());
			psql.setLong(1,currTime);
			psql.setString(2,dev);
			psql.setInt(3,0);
			tempList.add(psql.getSQL());
		}
		int[] ier = doBatch(tempList);
		if (ier != null && ier.length > 0) {
			logger.warn("批量入库成功");
		} else {
			logger.warn("批量入库失败");
		}
	}
	
	/**
	 * update the status of device
	 * @param taskid
	 * @param deviceSn
	 * @param result
	 */
	public void updateTaskDev(String taskid, String deviceSn, String result) 
	{
		String sql = "update tab_report_peroid_task_dev set status = ? where task_id = ? and device_serialnumber = ?";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1,StringUtil.getIntegerValue(result));
		psql.setLong(2,StringUtil.getLongValue(taskid));
		psql.setString(3,deviceSn);
		int i = jt.update(psql.getSQL());
		if (i > 0) {
			logger.warn("更新任务设备表成功！");
		} else {
			logger.warn("更新任务设备表失败！");
		}
	}
	
	/**
	 * get the information of task
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List getTaskInfo(int curPage_splitPage, int num_splitPage,String starttime1, String endtime1) 
	{
		logger.warn("ReportPeroidUpateDAO->getTaskInfo()");
		PrepareSQL pSql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSql.append("select a.task_id,a.add_time,a.report_timelist,a.target_timelist,b.acc_loginname ");
		}else{
			pSql.append("select a.*,b.acc_loginname ");
		}
		pSql.append("from tab_report_peroid_task a,tab_accounts b where a.acc_oid=b.acc_oid");
		if (false == StringUtil.IsEmpty(starttime1)){
			pSql.append(" and add_time>= "+starttime1);
		}
		
		if (false == StringUtil.IsEmpty(endtime1)){
			pSql.append(" and add_time <= "+endtime1);
		}
		List<Map> list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("acc_loginname", rs.getString("acc_loginname"));
				String taskId = StringUtil.getStringValue(rs.getString("task_id"));
				map.put("taskId", taskId);
				//下发时间转格式yyyy-mmdd hh:mm:ss
				try
				{
					long addtime = StringUtil.getLongValue(rs.getString("add_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(addtime);
					map.put("add_time", dt.getLongDate());
				}
				catch (Exception e)
				{
					map.put("add_time", "");
				}
				map.put("reporttimelist", rs.getString("report_timelist"));
				map.put("targettimelist", rs.getString("target_timelist"));
				logger.warn("taskId=="+taskId);
				if(!StringUtil.IsEmpty(taskId)){
					PrepareSQL sql = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						sql.append("select count(*) ");
					}else{
						sql.append("select count(1) ");
					}
					sql.append("from tab_report_peroid_task_dev where task_id="+taskId);
					int devCount = jt.queryForInt(sql.getSQL());
					map.put("devCount", devCount+"");
				}else{
					map.put("devCount","0");
				}
				return map;
			}
		});
		return list;
	}
	
	/**
	 * get total page
	 * @param num_splitPage
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public int getTaskInfoCount(int num_splitPage, String starttime1,String endtime1) 
	{
		logger.debug("ReportPeroidUpateDAO->getTaskInfoCount()");
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) from tab_report_peroid_task where 1=1 ");
		}else{
			psql.append("select count(1) from tab_report_peroid_task where 1=1 ");
		}
		
		if (false == StringUtil.IsEmpty(starttime1)){
			psql.append(" and add_time>= "+starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			psql.append(" and add_time <= "+endtime1);
		}
		
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * export data by excel
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List getTaskInfoExl(String starttime1, String endtime1) 
	{
		logger.debug("ReportPeroidUpateDAO->getTaskInfoExl()");
		PrepareSQL pSql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSql.append("select a.add_time,a.report_timelist,a.target_timelist,a.task_id,b.acc_loginname ");
		}else{
			pSql.append("select a.*,b.acc_loginname ");
		}
		pSql.append("from tab_report_peroid_task a,tab_accounts b where a.acc_oid=b.acc_oid");
		if (false == StringUtil.IsEmpty(starttime1)){
			pSql.append(" and add_time>= "+starttime1);
		}
		
		if (false == StringUtil.IsEmpty( endtime1)){
			pSql.append(" and add_time <= "+ endtime1);
		}
		List<Map> list  = jt.queryForList(pSql.getSQL());
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
				list.get(i).put("acc_loginname",StringUtil.getStringValue(list.get(i).get("acc_loginname")));
				try
				{
					long addtime = StringUtil.getLongValue(list.get(i).get("add_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(addtime);
					list.get(i).put("add_time", dt.getLongDate());
				}
				catch (Exception e)
				{
					list.get(i).put("add_time", "");
				}
				list.get(i).put("reporttimelist",StringUtil.getStringValue(list.get(i).get("report_timelist")));
				list.get(i).put("targettimelist",StringUtil.getStringValue(list.get(i).get("target_timelist")));
				String taskId = StringUtil.getStringValue(list.get(i).get("task_id"));
				list.get(i).put("taskId",taskId);
				logger.warn("taskId=="+taskId);
				if(!StringUtil.IsEmpty(taskId)){
					PrepareSQL sql = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						sql.append("select count(*) from tab_report_peroid_task_dev where task_id="+taskId);
					}else{
						sql.append("select count(1) from tab_report_peroid_task_dev where task_id="+taskId);
					}
					
					int devCount = jt.queryForInt(sql.getSQL());
					list.get(i).put("devCount", devCount+"");
				}else{
					list.get(i).put("devCount","0");
				}
			}
		}
		return list;
	}
	
	/**
	 * get information of device
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param taskId
	 * @return
	 */
	public List getDevInfo(int curPage_splitPage, int num_splitPage,String taskId) 
	{
		logger.debug("getDevInfo({},{},{})", new Object[] {curPage_splitPage, num_splitPage, taskId});
		List<Map> list = new ArrayList<Map>();
		if(!StringUtil.IsEmpty(taskId)){
			PrepareSQL sql = new PrepareSQL("");
			sql.append("select task_id,device_serialnumber,status from tab_report_peroid_task_dev where task_id ="+taskId);
			list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
					+ 1, num_splitPage, new RowMapper()
					{
						public Object mapRow(ResultSet rs, int arg1) throws SQLException
						{
							Map<String, String> map = new HashMap<String, String>();
							//设备类型
							map.put("device_serialnumber", rs.getString("device_serialnumber"));
							int st = StringUtil.getIntegerValue(rs.getString("status"));
							String status ="";
							switch (st) {
							case 0: status = "未定制";
								   break;
							case 1:  status = "设备不存在";
								   break;
							case 2:  status = "等待下发";
								   break;
							case 3:  status = "已下发";
							   break;
							default:  
								break;
							}
							map.put("status", status);
							return map;
						}
					});
		}
		logger.debug("获取列表大小："+list.size());
		return list;
	}
	
	/**
	 * total page of detail dev
	 * @param num_splitPage
	 * @param taskId
	 * @return
	 */
	public int getDevInfoCount(int num_splitPage, String taskId) 
	{
		if(StringUtil.IsEmpty(taskId)){
			return 0;
		}else{
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psql.append("select count(*) from tab_report_peroid_task_dev where task_id=? ");
			}else{
				psql.append("select count(1) from tab_report_peroid_task_dev where task_id=? ");
			}
			
			psql.setLong(1, StringUtil.getLongValue(taskId));
			int total = jt.queryForInt(psql.getSQL());
			int maxPage = 1;
			if (total % num_splitPage == 0){
				maxPage = total / num_splitPage;
			}else{
				maxPage = total / num_splitPage + 1;
			}
			logger.warn("总页数："+total);
			return maxPage;
		}
	}
	
	/**
	 * export device information
	 * @param taskId
	 * @return
	 */
	public List getDevInfoExcel(String taskId) 
	{
		logger.debug("getDevInfoExcel({})", new Object[] {taskId});
		List<Map> list = new ArrayList<Map>();
		if(!StringUtil.IsEmpty(taskId))
		{
			PrepareSQL sql = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				sql.append("select device_serialnumber,status ");
			}else{
				sql.append("select * ");
			}
			sql.append("from tab_report_peroid_task_dev where task_id ="+taskId);
			list = jt.queryForList(sql.getSQL());
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++)
				{
					list.get(i).put("device_serialnumber",
							StringUtil.getStringValue(list.get(i).get("device_serialnumber")));
					int st = Integer.valueOf(String.valueOf(list.get(i).get("status")));
					String status ="";
					switch (st) {
					case 0: status = "未定制";
						   break;
					case 1:  status = "设备不存在";
						   break;
					case 2:  status = "等待下发";
						   break;
					case 3:  status = "已下发";
					   break;
					default:  
						break;
					}
					list.get(i).put("status", status);
				}
			}
		}
		return list;
	}
}
