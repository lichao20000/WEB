
package com.linkage.module.bbms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.DateTimeUtil;

/**
 * 日志查询DAO
 * @author ZhangCong
 *
 */
public class SyslogQueryDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SyslogQueryDAO.class);

	/**
	 * 查询日志表数据
	 * @return
	 */
	public List<Map> querySysLogs(String startTime,String endTime,String oui,String sn,String logType, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("SyslogQueryDAO=>queryLogs({},{},{},{})", new Object[] { startTime, endTime, oui, sn });

		String querySql = "select dev_oui,dev_sn,receive_time,source_ip,format_version,log_type,content_desc from tv_syslog "
				+ " where receive_time >"
				+ startTime
				+ " and receive_time <"
				+ endTime
				+ " and log_type = "
				+ logType
				+ " and dev_oui = '"
				+ oui
				+ "' and dev_sn = '" + sn + "'";

		PrepareSQL psql = new PrepareSQL(querySql);

		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new SysLogRowMapper());
	}

	/**
	 * 查询日志类型数据
	 * @return
	 */
	public List<Map> querySysLogTypes()
	{
		String sqlStr = "select type_id,type_name from tv_syslog_type";
		PrepareSQL psql = new PrepareSQL(sqlStr);
		return querySP(psql.getSQL(), 1,
				1000, new SysLogTypesRowMapper());
	}


	/**
	 * 查询日志表数据
	 * @return
	 */
	public int querySysLogCount(String startTime,String endTime,String oui,String sn,String logType,
			int num_splitPage)
	{
		logger.debug("SyslogQueryDAO=>queryL" +
				"ogs({},{},{},{})", new Object[]{startTime,endTime,oui,sn});

		String sqlStr = "select count(*) from tv_syslog "
				+ " where receive_time >" + startTime + " and receive_time <"
				+ endTime + " and log_type = " + logType + " and dev_oui = '" + oui
				+ "' and dev_sn = '" + sn + "'";

		PrepareSQL psql = new PrepareSQL(sqlStr);
		psql.getSQL();
		int total = jt.queryForInt(sqlStr);
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		} else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	private class SysLogTypesRowMapper implements RowMapper
	{
		/**
		 * 组装数据
		 */
		public Object mapRow(ResultSet rs, int arg1) throws SQLException
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("type_id", StringUtil.getStringValue(rs.getString("type_id")));
			map.put("type_name", StringUtil.getStringValue(rs.getString("type_name")));
			return map;
		}
	}

	/**
	 * 自定义数据结果集处理类
	 * @author ZhangCong
	 *
	 */
	private class SysLogRowMapper implements RowMapper
	{
		/**
		 * 组装数据
		 */
		public Object mapRow(ResultSet rs, int arg1) throws SQLException
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("dev_oui", rs.getString("dev_oui"));
			map.put("dev_sn", rs.getString("dev_sn"));
			map.put("receive_time", changeTimeToDate(rs.getString("receive_time")));
			map.put("source_ip", StringUtil.getStringValue(rs.getString("source_ip")));
			map.put("format_version", StringUtil.getStringValue(rs.getString("format_version")));
			map.put("log_type", StringUtil.getStringValue(rs.getString("log_type")));
			map.put("content_desc", StringUtil.getStringValue(rs.getString("content_desc")));

			return map;
		}


//		/**
//		 * 组装数据
//		 */
//		public Object mapRow(ResultSet rs, int arg1) throws SQLException
//		{
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("deviceid", rs.getString("deviceid"));
//			map.put("targetip", rs.getString("targetip"));
//			map.put("srcip", rs.getString("srcip"));
//			map.put("targetmac", StringUtil.getStringValue(rs.getString("targetmac")));
//			map.put("srcmac", StringUtil.getStringValue(rs.getString("srcmac")));
//			map.put("stime", changeTimeToDate(rs.getString("stime")));
//			map.put("etime", changeTimeToDate(rs.getString("etime")));
//
//			//判断需要查询的日志类型
//			if ("filter".equals(logType))
//			{
//				//告警日志 过滤
//				map.put("filterid", StringUtil.getStringValue(rs.getString("filterid")));
//				map.put("content", StringUtil.getStringValue(rs.getString("content")));
//				map.put("filtertimes", getZeroIfNull(rs.getString("filtertimes")));
//			}else if("conn".equals(logType))
//			{
//				//访问日志 连接
//				map.put("targetport", rs.getString("targetport"));
//				map.put("srcport", rs.getString("srcport"));
//				map.put("protocoltype", getProtocolType(rs.getString("protocoltype")));
//				map.put("times", getZeroIfNull(rs.getString("times")));
//				map.put("flux", getZeroIfNull(rs.getString("flux")));
//			}else if("attack".equals(logType))
//			{
//				//安全审计日志  攻击
//				map.put("targetport", rs.getString("targetport"));
//				map.put("srcport", rs.getString("srcport"));
//				map.put("attacktype", rs.getString("attacktype"));
//				map.put("mark", StringUtil.getStringValue(rs.getString("mark")));
//				map.put("attacktimes", getZeroIfNull(rs.getString("attacktimes")));
//			}else
//			{
//				//应用审计日志 病毒
//				map.put("targetport", StringUtil.getStringValue(rs.getString("targetport")));
//				map.put("srcport", StringUtil.getStringValue(rs.getString("srcport")));
//				map.put("virustype", StringUtil.getStringValue(rs.getString("virustype")));
//				map.put("operation", getOperation(rs.getString("operation")));
//				map.put("remark", StringUtil.getStringValue(rs.getString("remark")));
//				map.put("virustimes", getZeroIfNull(rs.getString("virustimes")));
//			}
//
//			return map;
//		}

		/**
		 * 获取时间
		 * @param timeStr
		 * @param rs
		 * @return
		 */
		private String changeTimeToDate(String timeStr)
		{
			try
			{
				long time = Long.parseLong(timeStr);
				DateTimeUtil dateTimeUtil = new DateTimeUtil(time * 1000);
				return dateTimeUtil.getDate();
			}
			catch (NumberFormatException e)
			{
				return "";
			}
			catch (Exception e)
			{
				return "";
			}
		}

//		private String getZeroIfNull(String value)
//		{
//			if(null == value || "".equals(value))
//			{
//				return "0";
//			}
//			return value;
//		}

//		/**
//		 * 获得协议描述
//		 * @param type
//		 * @return
//		 */
//		private String getProtocolType(String type)
//		{
//			if("0".equals(type))
//			{
//				return "http";
//			}else if("1".equals(type))
//			{
//				return "ftp";
//			}else if("2".equals(type))
//			{
//				return "smtp";
//			}else
//			{
//				return "pop3";
//			}
//		}
//
//		private String getOperation(String type)
//		{
//
//			if("0".equals(type))
//			{
//				return "不处理";
//			}else if("1".equals(type))
//			{
//				return "删除";
//			}else if("2".equals(type))
//			{
//				return "隔离";
//			}else
//			{
//				return "修复";
//			}
//		}
	}
}
