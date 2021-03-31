package com.linkage.module.ids.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

public class DeviceMonitoringDAO extends SuperDAO
{

	/**
	 * PON口状态信息
	 */
	public int queryPonstatusCount(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,int curPage_splitPage, int num_splitPage) 
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSQL.setSQL("select count(*) from tab_ponstatus_info where 1=1 ");
		}else{
			pSQL.setSQL("select count(1) from tab_ponstatus_info where 1=1 ");
		}	
		
		checkQueryParams(startTime, endTime, quertTimeType, deviceno, loid,pSQL);
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryPonstatusList(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,
			int curPage_splitPage, int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select loid,oui,device_serialnumber,status,tx_power,");
			psql.append("rx_power,temperature,vottage,bais_current,upload_time,add_time ");
			psql.append("from tab_ponstatus_info where 1=1 ");
		}else{
			psql.append("select * from tab_ponstatus_info where 1=1 ");
		}
		
		checkQueryParams(startTime, endTime, quertTimeType, deviceno, loid,psql);
		return querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet1Map(map, rs);
			}
		});
	}
	
	/**
	 * 宽带业务参数信息表
	 */
	public int queryNetparamCount(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,int curPage_splitPage, int num_splitPage) 
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSQL.setSQL("select count(*) from tab_netparam_info where 1=1 ");
		}else{
			pSQL.setSQL("select count(1) from tab_netparam_info where 1=1 ");
		}
		
		checkQueryParams(startTime, endTime, quertTimeType, deviceno, loid,pSQL);
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryNetparamList(String startTime,String endTime,String quertTimeType,
			String deviceno,String loid,int curPage_splitPage,int num_splitPage) 
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSQL.append("select loid,oui,device_serialnumber,status,reason,upload_time,add_time ");
			pSQL.append("from tab_netparam_info where 1=1 ");
		}else{
			pSQL.setSQL("select * from tab_netparam_info where 1=1 ");
		}
		
		checkQueryParams(startTime, endTime, quertTimeType, deviceno, loid,pSQL);
		return querySP(pSQL.getSQL(), (curPage_splitPage - 1)
				* num_splitPage, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}
	
	/**
	 * 语音状态数据表
	 */
	public int queryVoicestatusCount(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,int curPage_splitPage, int num_splitPage) 
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select count(1) from tab_voicestatus_info where 1=1 ");
		checkQueryParams(startTime, endTime, quertTimeType, deviceno, loid,pSQL);
		int total = jt.queryForInt(pSQL.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryVoicestatusList(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid,int curPage_splitPage, int num_splitPage) 
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSQL.append("select loid,oui,device_serialnumber,voip_id,voip_prof_id,");
			pSQL.append("line_id,enabled,status,reason,upload_time,add_time ");
			pSQL.append("from tab_voicestatus_info where 1=1 ");
		}else{
			pSQL.setSQL("select * from tab_voicestatus_info where 1=1 ");
		}
		
		checkQueryParams(startTime, endTime, quertTimeType, deviceno, loid,pSQL);
		return querySP(pSQL.getSQL(), (curPage_splitPage - 1)
				* num_splitPage, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet3Map(map, rs);
			}
		});
	}
	

	/**
	 * 拼装SQL条件
	 */
	private void checkQueryParams(String startTime, String endTime,
			String quertTimeType, String deviceno, String loid, PrepareSQL pSQL) 
	{
		if(!StringUtil.IsEmpty(quertTimeType)){
			if (quertTimeType.equals("0")) {
				if (!StringUtil.IsEmpty(deviceno)) {
					pSQL.append(" and device_serialnumber = '" + deviceno + "'");
				}
				if (!StringUtil.IsEmpty(loid)) {
					pSQL.append(" and loid = '" + loid + "'");
				}
			}
			if (quertTimeType.equals("1")) {
				if (!StringUtil.IsEmpty(startTime)) {
					pSQL.append(" and upload_time >");
					pSQL.append(String.valueOf(new DateTimeUtil(startTime).getLongTime()));
				}
				if (!StringUtil.IsEmpty(endTime)) {
					pSQL.append(" and upload_time < ");
					pSQL.append(String.valueOf(new DateTimeUtil(endTime).getLongTime()));
				}
				if (!StringUtil.IsEmpty(deviceno)) {
					pSQL.append(" and device_serialnumber = '" + deviceno + "'");
				}
				if (!StringUtil.IsEmpty(loid)) {
					pSQL.append(" and loid = '" + loid + "'");
				}
			}
			if (quertTimeType.equals("2")) {
				if (!StringUtil.IsEmpty(startTime)) {
					pSQL.append(" and add_time > ");
					pSQL.append(String.valueOf(new DateTimeUtil(startTime).getLongTime()));
				}
				if (!StringUtil.IsEmpty(endTime)) {
					pSQL.append(" and add_time < ");
					pSQL.append(String.valueOf(new DateTimeUtil(endTime).getLongTime()));
				}
				if (!StringUtil.IsEmpty(deviceno)) {
					pSQL.append(" and device_serialnumber = '" + deviceno + "'");
				}
				if (!StringUtil.IsEmpty(loid)) {
					pSQL.append(" and loid = '" + loid + "'");
				}
			}
		}
	}

	/**
	 * 将结果集放入map中
	 */
	protected Map<String, String> resultSet1Map(Map<String, String> map, ResultSet rs) 
	{
		try {
			map.put("loid", rs.getString("loid"));
			map.put("oui", rs.getString("oui"));
			map.put("device_serialnumber",rs.getString("device_serialnumber"));
			map.put("status", rs.getString("status"));
			map.put("tx_power", rs.getString("tx_power"));
			map.put("rx_power", rs.getString("rx_power"));
			map.put("temperature", rs.getString("temperature"));
			map.put("vottage", rs.getString("vottage"));
			map.put("bais_current", rs.getString("bais_current"));
			long upload_time = StringUtil.getLongValue(rs.getString("upload_time"));
			DateTimeUtil dt = new DateTimeUtil(upload_time*1000);
			map.put("upload_time", dt.getLongDate());
			long add_time = StringUtil.getLongValue(rs.getString("add_time"));
			DateTimeUtil dateTimeUtil = new DateTimeUtil(add_time*1000);
			map.put("add_time", dateTimeUtil.getLongDate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 将结果集放入map中
	 */
	protected Map<String, String> resultSet2Map(Map<String, String> map, ResultSet rs) 
	{
		try {
			map.put("loid", rs.getString("loid"));
			map.put("oui", rs.getString("oui"));
			map.put("device_serialnumber",rs.getString("device_serialnumber"));
			map.put("status", rs.getString("status"));
			map.put("reason", rs.getString("reason"));
			long upload_time = StringUtil.getLongValue(rs.getString("upload_time"));
			DateTimeUtil dt = new DateTimeUtil(upload_time*1000);
			map.put("upload_time", dt.getLongDate());
			long add_time = StringUtil.getLongValue(rs.getString("add_time"));
			DateTimeUtil dateTimeUtil = new DateTimeUtil(add_time*1000);
			map.put("add_time", dateTimeUtil.getLongDate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 将结果集放入map中
	 */
	protected Map<String, String> resultSet3Map(Map<String, String> map, ResultSet rs) 
	{
		try {
			map.put("loid", rs.getString("loid"));
			map.put("oui", rs.getString("oui"));
			map.put("device_serialnumber",rs.getString("device_serialnumber"));
			map.put("voip_id", rs.getString("voip_id"));
			map.put("voip_prof_id", rs.getString("voip_prof_id"));
			map.put("line_id", rs.getString("line_id"));
			String enabled = "";
			if ("Enabled".equals(rs.getString("enabled"))) {
				enabled="启用";
			}else {
				enabled="未启用";
			}
			map.put("enabled", enabled);
			map.put("status", rs.getString("status"));
			String reason = "";
			if ("0".equals(rs.getString("reason"))) {
				reason ="成功";
			}
			if ("1".equals(rs.getString("reason"))) {
				reason ="IAD模块错误";
			}
			if ("2".equals(rs.getString("reason"))) {
				reason ="访问路由不通";
			}
			if ("3".equals(rs.getString("reason"))) {
				reason ="访问服务器无响应";
			}
			if ("4".equals(rs.getString("reason"))) {
				reason ="帐号、密码错误";
			}
			if ("5".equals(rs.getString("reason"))) {
				reason ="未知错误";
			}
			map.put("reason", reason);
			long upload_time = StringUtil.getLongValue(rs.getString("upload_time"));
			DateTimeUtil dt = new DateTimeUtil(upload_time*1000);
			map.put("upload_time", dt.getLongDate());
			long add_time = StringUtil.getLongValue(rs.getString("add_time"));
			DateTimeUtil dateTimeUtil = new DateTimeUtil(add_time*1000);
			map.put("add_time", dateTimeUtil.getLongDate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
}
