
package com.linkage.module.gtms.stb.report.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-4-10
 * @category com.linkage.module.lims.stb.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class CheckFruitQueryDAO extends SuperDAO
{

	// 日志
	private static Logger logger = LoggerFactory.getLogger(CheckFruitQueryDAO.class);

	@SuppressWarnings("unchecked")
	public List<Map> Query(String starttime, String endtime, int curPage_splitPage,
			int num_splitPage, String user_id, String mac)
	{
		logger.warn("Query()=========>方法入口=====>"+starttime+""+endtime+""+user_id+""+mac);
		StringBuffer sql = new StringBuffer();
		sql.append("select  user_id,mac,conn_type,bitrate,package_lost,report_time from stb_check_data where 1=1");
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and report_time>=" + starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and report_time<=" + endtime);
		}
		if (!StringUtil.IsEmpty(user_id))
		{
			sql.append(" and user_id='"+user_id+"'");
		}
		if (!StringUtil.IsEmpty(mac))
		{
			sql.append(" and mac='"+mac+"'");
		}
		logger.warn(" Query=====查询sql>"+sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("mac", rs.getString("mac"));
				map.put("conn_type", rs.getString("conn_type"));
				map.put("bitrate", rs.getString("bitrate"));
				map.put("package_lost", rs.getString("package_lost"));
				map.put("report_time", transDate(rs.getString("report_time")));
				return map;
			}
		});
	}

	/**
	 * 分页
	 *
	 * @return
	 */
	public int getpaging(String starttime, String endtime, int curPage_splitPage,
			int num_splitPage, String user_id, String mac)
	{
		logger.warn("getpaging()=========>方法入口=====>"+starttime+""+endtime+""+user_id+""+mac);
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from stb_check_data where 1=1");
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and report_time>=" + starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and report_time<=" + endtime);
		}
		if (!StringUtil.IsEmpty(user_id))
		{
			sql.append(" and user_id='"+user_id+"'");
		}
		if (!StringUtil.IsEmpty(mac))
		{
			sql.append(" and mac='"+mac+"'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		logger.warn("getpaging()=========>方法出口=====>"+starttime+""+endtime+""+user_id+""+mac);
		return maxPage;
	}

	/**
	 * 导出
	 *
	 * @param seconds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> derive(String starttime, String endtime, String user_id, String mac)
	{
		logger.warn("derive()=========>方法入口=====>"+starttime+""+endtime+""+user_id+""+mac);
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql.append(" select ");
		}else{
			sql.append(" select top 2000 ");
		}
		sql.append(" user_id,mac,conn_type,bitrate,package_lost,report_time from stb_check_data where 1=1 ");
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and report_time>=" + starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and report_time<=" + endtime);
		}
		if (!StringUtil.IsEmpty(user_id))
		{
			sql.append(" and user_id='"+user_id+"'");
		}
		if (!StringUtil.IsEmpty(mac))
		{
			sql.append(" and mac='"+mac+"'");
		}
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql.append(" limit 2000 ");
		}

		logger.warn(" derive=====导出sql>"+sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("mac", rs.getString("mac"));
				map.put("conn_type", rs.getString("conn_type"));
				map.put("bitrate", rs.getString("bitrate"));
				map.put("package_lost", rs.getString("package_lost"));
				map.put("report_time", transDate(rs.getString("report_time")));
				return map;
			}
		});
		logger.warn("derive()=========>方法出口=====>"+starttime+""+endtime+""+user_id+""+mac);
		return list;
	}

	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(
						Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}
			catch (NumberFormatException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}
}
