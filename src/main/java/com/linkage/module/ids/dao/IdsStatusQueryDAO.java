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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class IdsStatusQueryDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(IdsStatusQueryDAO.class);
	
	
	public List getQueryStatusList(int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1) 
	{
		PrepareSQL pSql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSql.append("select a.task_id,a.add_time,a.enable,a.timelist,");
			pSql.append("a.serverurl,a.paralist,a.tftp_port,b.acc_loginname ");
		}else{
			pSql.append("select a.*,b.acc_loginname ");
		}
		pSql.append("from tab_status_report_task a,tab_accounts b where a.acc_oid=b.acc_oid");
		
		if (false == StringUtil.IsEmpty(starttime1)){
			pSql.append(" and add_time>= "+starttime1);
		}
		
		if (false == StringUtil.IsEmpty(endtime1)){
			pSql.append(" and add_time <= "+endtime1);
		}
		
		pSql.append(" order by a.add_time desc ");
		
		List<Map> list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				String accLoginname = rs.getString("acc_loginname");
				map.put("acc_loginname", accLoginname);
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
				String enable = rs.getString("enable");
				if(StringUtil.getIntegerValue(enable)==1){
					map.put("enable", "开启");
				}else{
					map.put("enable", "关闭");
				}
				map.put("timelist", rs.getString("timelist"));
				map.put("serverurl", rs.getString("serverurl"));
				map.put("paralist", rs.getString("paralist"));
				map.put("tftp_port", rs.getString("tftp_port"));
				if(!StringUtil.IsEmpty(taskId)){
					PrepareSQL sql = new PrepareSQL();
					sql.append("select count(1) from  tab_status_report_task_dev  where task_id="+taskId);
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
	
	public int getQueryStatusCount(int num_splitPage, String starttime1,String endtime1) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append(" select count(*) from ");
		}else{
			psql.append(" select count(1) from ");
		}
		psql.append("from tab_status_report_task where 1=1 ");
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
	
	public List getDevInfo(int curPage_splitPage, int num_splitPage,String taskId) 
	{
		List<Map> list = new ArrayList<Map>();
		if(!StringUtil.IsEmpty(taskId))
		{
			PrepareSQL sql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append("select a.device_serialnumber,a.result_id,a.status,b.oui device_oui ");
			}else{
				sql.append("select a.*,b.oui device_oui ");
			}
			sql.append("from tab_status_report_task_dev a,tab_gw_device b ");
			sql.append("where a.device_serialnumber=b.device_serialnumber and a.task_id="+taskId);
			list = querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage
					+ 1, num_splitPage, new RowMapper()
					{
						public Object mapRow(ResultSet rs, int arg1) throws SQLException
						{
							Map<String, String> map = new HashMap<String, String>();
							//设备类型
							map.put("oui", rs.getString("device_oui"));
							map.put("device_serialnumber", rs.getString("device_serialnumber"));
							map.put("result_id", rs.getString("result_id"));
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
		return list;
	}
	
	public int getDevInfoCount(int num_splitPage, String taskId) 
	{
		if(StringUtil.IsEmpty(taskId)){
			return 0;
		}else{
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select count(*) ");
			}else{
				psql.append("select count(1) ");
			}
			psql.append("from tab_status_report_task_dev where task_id=? ");
			psql.setLong(1, StringUtil.getLongValue(taskId));
			int total = jt.queryForInt(psql.getSQL());
			int maxPage = 1;
			if (total % num_splitPage == 0){
				maxPage = total / num_splitPage;
			}else{
				maxPage = total / num_splitPage + 1;
			}
			return maxPage;
		}
	}
	
	public List getQueryStatusListExcel(String starttime, String endtime) 
	{
		PrepareSQL pSql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSql.append("select a.add_time,a.enable,a.timelist,a.serverurl,");
			pSql.append("a.tftp_port,a.paralist,a.task_id,b.acc_loginname ");
		}else{
			pSql.append("select a.*,b.acc_loginname ");
		}
		pSql.append("from tab_status_report_task a,tab_accounts b where a.acc_oid=b.acc_oid");
		if (false == StringUtil.IsEmpty(starttime)){
			pSql.append(" and add_time>= "+starttime);
		}
		
		if (false == StringUtil.IsEmpty( endtime)){
			pSql.append(" and add_time <= "+ endtime);
		}
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSql.getSQL());
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
				String enable=StringUtil.getStringValue(list.get(i).get("enable"));
				if(StringUtil.getIntegerValue(enable)==1){
					list.get(i).put("enable", "开启");
				}else {
					list.get(i).put("enable", "关闭");
				}
				list.get(i).put("timelist",StringUtil.getStringValue(list.get(i).get("timelist")));
				list.get(i).put("serverurl",StringUtil.getStringValue(list.get(i).get("serverurl")));
				list.get(i).put("tftp_port",StringUtil.getStringValue(list.get(i).get("tftp_port")));
				list.get(i).put("paralist",StringUtil.getStringValue(list.get(i).get("paralist")));
				String taskId = StringUtil.getStringValue(list.get(i).get("task_id"));
				list.get(i).put("taskId",taskId);
				if(!StringUtil.IsEmpty(taskId)){
					PrepareSQL sql = new PrepareSQL();
					sql.append("select count(1) from  tab_status_report_task_dev where task_id="+taskId);
					int devCount = jt.queryForInt(sql.getSQL());
					list.get(i).put("devCount", devCount+"");
				}else{
					list.get(i).put("devCount","0");
				}
			}
		}
		return list;
	}
	
	public List getDevInfoExcel(String taskId) 
	{
		List<Map> list = new ArrayList<Map>();
		if(!StringUtil.IsEmpty(taskId)){
			PrepareSQL sql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append("select a.device_serialnumber,a.result_id,a.status,b.oui device_oui ");
			}else{
				sql.append("select a.*,b.oui device_oui ");
			}
			sql.append("from tab_status_report_task_dev a,tab_gw_device b ");
			sql.append("where a.device_serialnumber=b.device_serialnumber and a.task_id ="+taskId);
			list = jt.queryForList(sql.getSQL());
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++)
				{
					list.get(i).put("oui",StringUtil.getStringValue(list.get(i).get("device_oui")));
					list.get(i).put("device_serialnumber",StringUtil.getStringValue(list.get(i).get("device_serialnumber")));
					list.get(i).put("result_id",StringUtil.getStringValue(list.get(i).get("result_id")));
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
