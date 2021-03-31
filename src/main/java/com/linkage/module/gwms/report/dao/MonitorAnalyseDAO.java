package com.linkage.module.gwms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author zzd (Ailk No.)
 * @version 1.0
 * @since 2016-12-26
 * @category com.linkage.module.gwms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MonitorAnalyseDAO extends SuperDAO
{
	public List<Map> queryMonitorAnalyse(int curPage_splitPage, int num_splitPage,
			long startTime,long endTime,String indexType,String instance,String ipSelect)
	{
		StringBuffer sql = new StringBuffer();
		
		if("1".equals(indexType)){
			sql.append("select a.count_day,a.device_message,a.report_times from tab_alarm_monitor a  where 1=1" );
		}else if("4".equals(indexType)){
			sql.append("select gettime,instance,value1 from tab_performance where 1=1 ");
		}else{
		/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
				//TODO wait
			}else{
				
			} */
			sql.append("select a.count_day,a.device_message,a.report_times," +
					"d.vendor_add,b.device_serialnumber,c.device_model " +
					"from tab_alarm_monitor a " +
					"left join tab_gw_device b on a.device_message=b.device_id  " +
					"join gw_device_model c on b.device_model_id = c.device_model_id " +
					"join tab_vendor d on b.vendor_id=d.vendor_id");
		}
		if("4".equals(indexType)){
			if(0!=startTime){
				sql.append(" and gettime >="+startTime);
			}
			if(0!=endTime){
				sql.append(" and gettime <="+endTime);
			}
			if(null!=ipSelect&&!"".equals(ipSelect)){
				sql.append(" and hostip ='"+ipSelect+"'");
			}
			if(null!=instance&&!"".equals(instance)){
				sql.append(" and instance ='"+instance+"'");
			}
		}else{
			if(0!=startTime){
				sql.append(" and a.gather_time >="+startTime);
			}
			if(0!=endTime){
				sql.append(" and a.gather_time <="+endTime);
			}
			if(null!=indexType&&!"".equals(indexType)){
				sql.append(" and a.monitor_type ="+indexType);
			}
		}

		PrepareSQL pSql = new PrepareSQL(sql.toString());
		if("4".equals(indexType)){
			return querySP(pSql.toString(), (curPage_splitPage - 1) * num_splitPage + 1, 
					num_splitPage, new RowMapper()
			{
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException
						{
					Map map = new HashMap();
					long gettime = StringUtil.getLongValue(rs.getString("gettime"));
					DateTimeUtil dt = new DateTimeUtil(gettime * 1000L);
					map.put("gettime", dt.getYYYY_MM_DD_HH_mm_ss());
					map.put("instance", rs.getString("instance"));
					map.put("value1", rs.getString("value1"));
					return map;
						}
			});
		}else if("1".equals(indexType)){
			return querySP(pSql.toString(), (curPage_splitPage - 1) * num_splitPage + 1, 
					num_splitPage, new RowMapper()
			{
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException
						{
					Map map = new HashMap();
					map.put("count_day", rs.getString("count_day"));
					map.put("device_message", rs.getString("device_message"));
					map.put("report_times", rs.getString("report_times"));
					return map;
						}
			});
		}else{
			return querySP(pSql.toString(), (curPage_splitPage - 1) * num_splitPage + 1, 
					num_splitPage, new RowMapper()
			{
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException
						{
					Map map = new HashMap();
					map.put("count_day", rs.getString("count_day"));
					map.put("device_message", rs.getString("device_message"));
					map.put("report_times", rs.getString("report_times"));
					map.put("vendor_add", rs.getString("vendor_add"));
					map.put("device_model", rs.getString("device_model"));
					map.put("device_serialnumber", rs.getString("device_serialnumber"));
					return map;
						}
			});
		}
	}

	public List<Map> queryIpMsg(){
		PrepareSQL pSql = new PrepareSQL("select distinct hostip,hostname from tab_objectdef where attr_id = 5");
		return jt.queryForList(pSql.getSQL());
	}
	
	public List<Map> queryConMsg(){
		PrepareSQL pSql = new PrepareSQL("select distinct instance from tab_objectdef where attr_id = 5");
		return jt.queryForList(pSql.getSQL());
	}

	public List<Map> queryMonitorAnalyseForExcel(long startTime,long endTime,
			String indexType,String instance,String ipSelect)
	{
		StringBuffer sql = new StringBuffer();
		if("1".equals(indexType)){
			sql.append("select a.count_day,a.device_message,a.report_times from tab_alarm_monitor a  where 1=1" );
		}else if("4".equals(indexType)){
			sql.append("select gettime,instance,value1 from tab_performance where 1=1 ");
		}else{
		/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
				//TODO wait
			}else{
				
			} */
			sql.append("select a.count_day,a.device_message,a.report_times," +
					"d.vendor_add,b.device_serialnumber,c.device_model " +
					"from tab_alarm_monitor a " +
					"left join tab_gw_device b on a.device_message=b.device_id " +
					"join gw_device_model c on b.device_model_id = c.device_model_id " +
					"join tab_vendor d on b.vendor_id=d.vendor_id");
		}
		if("4".equals(indexType)){
			if(0!=startTime){
				sql.append(" and gettime >="+startTime);
			}
			if(0!=endTime){
				sql.append(" and gettime <="+endTime);
			}
			if(null!=ipSelect&&!"".equals(ipSelect)){
				sql.append(" and hostip ='"+ipSelect+"'");
			}
			if(null!=instance&&!"".equals(instance)){
				sql.append(" and instance ='"+instance+"'");
			}
		}else{
			if(0!=startTime){
				sql.append(" and a.gather_time >="+startTime);
			}
			if(0!=endTime){
				sql.append(" and a.gather_time <="+endTime);
			}
			if(null!=indexType&&!"".equals(indexType)){
				sql.append(" and a.monitor_type ="+indexType);
			}
		}

		PrepareSQL pSql = new PrepareSQL(sql.toString());
		return jt.queryForList(pSql.getSQL());
	}

	public int queryMonitorAnalyseCount(int curPage_splitPage, int num_splitPage,
			long startTime,long endTime,String indexType,String instance,String ipSelect)
	{
		StringBuffer sql = new StringBuffer();
		if("4".equals(indexType)){
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append("select count(*) from tab_performance where 1=1 ");
			}else{
				sql.append("select count(1) from tab_performance where 1=1 ");
			}
			
			if(0!=startTime){
				sql.append(" and gettime >="+startTime);
			}
			if(0!=endTime){
				sql.append(" and gettime <="+endTime);
			}
			if(null!=ipSelect&&!"".equals(ipSelect)){
				sql.append(" and hostip ='"+ipSelect+"'");
			}
			if(null!=instance&&!"".equals(instance)){
				sql.append(" and instance ='"+instance+"'");
			}
		}else{
			if("1".equals(indexType)){
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					sql.append("select count(*) from tab_alarm_monitor a  where 1=1" );
				}else{
					sql.append("select count(1) from tab_alarm_monitor a  where 1=1" );
				}
			}else{
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					//TODO wait
					sql.append("select count(*) ");
				}else{
					sql.append("select count(1) ");
				}
				sql.append("from tab_alarm_monitor a " +
						"left join tab_gw_device b on a.device_message=b.device_id " +
						"join gw_device_model c on b.device_model_id=c.device_model_id");
			}
			if(0!=startTime){
				sql.append(" and a.gather_time >="+startTime);
			}
			if(0!=endTime){
				sql.append(" and a.gather_time <="+endTime);
			}
			if(null!=indexType&&!"".equals(indexType)){
				sql.append(" and a.monitor_type ="+indexType);
			}
		}

		PrepareSQL pSql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(pSql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}


}
