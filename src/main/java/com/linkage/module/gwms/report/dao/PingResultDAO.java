package com.linkage.module.gwms.report.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.gw.PingObject;

public class PingResultDAO {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(OnlineDevStatDAO.class);

	private JdbcTemplate jt;

	
	public List<Map<String, String>> getPingResult(String deviceId) 
	{
		StringBuilder sql = new StringBuilder();
		
		String datestr="1970-01-01 00:00:00";
		if(LipossGlobals.isOracle())
		{
			sql.append("select to_char(to_date('"+datestr+"','yyyy-mm-dd hh24:mi:ss') + time/24/60/60,'yyyy-mm-dd hh24:mi:ss') as time,");
		}
		else if(DBUtil.GetDB()==Global.DB_MYSQL)
		{
			sql.append("select date_format(str_to_date('"+datestr+"','%Y-%m-%d %H:%i:%s') + time/24/60/60,'%Y-%m-%d %H:%i:%s') as time,");
		}
		else
		{
			sql.append("select convert(varchar, dateadd(ss,time,'"+datestr+"'), 111)+' '+convert(varchar,dateadd(ss,time,'"+datestr+"'),108) as time,");
		}
		
		sql.append("device_port,test_ip,package_size,package_num,time_out,succ_num,fail_num,");
		sql.append("avg_res_time, min_res_time, max_res_time, is_ping_succ,");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append(" case succ_num when 0 then 0 else cast(round(fail_num/(succ_num+fail_num),2) as decimal float)*100 end as rate ");
		}else{
			sql.append(" case succ_num when 0 then 0 else cast(round(fail_num/(succ_num+fail_num),2) as numeric(5,2))*100 end as rate ");
		}
		
		sql.append("from gw_ping where device_id='"+deviceId+"'");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 入ping操作历史记录表，返回执行结果
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-4
	 * @return List
	 */
	public int getPingRecord(String deviceId, PingObject obj)
	{
		logger.debug("getPingRecord({},{})", deviceId, obj);
		long time = new Date().getTime() / 1000;
		String strSQL = "insert into gw_ping (device_id,time,device_port,test_ip,package_size,"
				+ "package_num,time_out,succ_num,fail_num,avg_res_time,min_res_time,max_res_time,"
				+ "is_ping_succ) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		psql.setLong(2, time);
		psql.setString(3, obj.getDevInterface());
		psql.setString(4, obj.getPingAddress());
		psql.setLong(5, obj.getPackageSize());
		psql.setInt(6, obj.getNumOfRepetitions());
		psql.setLong(7, obj.getTimeOut());
		psql.setInt(8, obj.getSuccessCount());
		psql.setInt(9, obj.getFailureCount());
		psql.setLong(10, obj.getAverageResponseTime());
		psql.setLong(11, obj.getMinimumResponseTime());
		psql.setLong(12, obj.getMaximumResponseTime());
		if(obj.isSuccess()){
			psql.setInt(13, 1);
		}else{
			psql.setInt(13, 0);
		}
		return jt.update(psql.getSQL());
	}
	
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
}
