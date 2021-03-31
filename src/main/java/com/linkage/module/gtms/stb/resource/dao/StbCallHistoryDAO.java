package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 机顶盒apk上报信息查询DAO
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StbCallHistoryDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StbCallHistoryDAO.class);
	private int queryCount;

	/**
	 * 查询列表
	 */
	public List<Map<String,String>> query(int curPage_splitPage, int num_splitPage,
			String login_ip, String mac, String request_username,
			String result_username, String startTime, String endTime,String table)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select login_time,login_ip,event_id,mac,");
		psql.append("apk_version_name,server_host,server_port,server_version,");
		psql.append("request_username,result_username,result_code ");
		psql.append(pinSql(login_ip,mac,request_username,result_username,startTime,endTime,table));
		psql.append(" order by login_time ");

		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();

						map.put("login_time", rs.getString("login_time"));
						map.put("request_time", transDate(rs.getString("login_time")));
						map.put("login_ip", rs.getString("login_ip"));
						map.put("event_id", rs.getString("event_id"));
						map.put("mac", rs.getString("mac"));
						map.put("apk_version_name", rs.getString("apk_version_name"));
						map.put("server_host", rs.getString("server_host"));
						map.put("server_port", rs.getString("server_port"));
						map.put("server_version", rs.getString("server_version"));
						map.put("request_username", rs.getString("request_username"));
						map.put("result_username", rs.getString("result_username"));
						map.put("result_code", rs.getString("result_code"));

						return map;
					}
				});

		return list;
	}

	/**
	 * 分页
	 */
	public int count(int curPage_splitPage, int num_splitPage, String login_ip,
			String mac, String request_username, String result_username,
			String startTime, String endTime,String table)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) ");
		psql.append(pinSql(login_ip,mac,request_username,result_username,startTime,endTime,table));

		int total = jt.queryForInt(psql.getSQL());
		queryCount = total;
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;
	}

	/**
	 * 查询详细
	 */
	public List<Map<String, String>> detailDevice(String mac, long login_time,String table)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select login_time,login_ip,stbid,event_id,event_num,");
		psql.append("sn,mac,apk_version_name,server_host,server_port,");
		psql.append("server_version,request_username,result_username,result_code ");
		psql.append("from "+table);
		psql.append(" where 1=1");
		if(!StringUtil.IsEmpty(mac)){
			psql.append(" and mac='"+mac.trim()+"'");
		}
		if(login_time>0){
			psql.append(" and login_time="+login_time);
		}
		psql.append(" order by login_time ");

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 拼接sql条件
	 */
	private String pinSql(String login_ip, String mac, String request_username,
			String result_username, String startTime, String endTime,String table)
	{
		StringBuffer sb=new StringBuffer();
		sb.append(" from "+table);
		sb.append(" where 1=1");
		if(!StringUtil.IsEmpty(login_ip)){
			sb.append(" and login_ip='"+login_ip.trim()+"'");
		}
		if(!StringUtil.IsEmpty(mac)){
			sb.append(" and mac='"+mac.trim()+"'");
		}
		if(!StringUtil.IsEmpty(request_username)){
			sb.append(" and request_username='"+request_username.trim()+"'");
		}
		if(!StringUtil.IsEmpty(result_username)){
			sb.append(" and result_username='"+result_username.trim()+"'");
		}
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and login_time>="+startTime);
		}
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and login_time<="+endTime);
		}

		return sb.toString();
	}

	/**
	 * 日期格式转换
	 */
	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try{
				DateTimeUtil dt = new DateTimeUtil(Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}catch (Exception e){
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}


	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

}
