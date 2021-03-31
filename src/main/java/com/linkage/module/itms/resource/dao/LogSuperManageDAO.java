
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

import flex.messaging.io.ArrayList;

public class LogSuperManageDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(LogSuperManageDAO.class);

	public List<Map> getLogInfo(String auth_name, String user_name, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("LogSuperManageDAO->getLogInfo");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.auth_name,c.acc_loginname oper_user,a.oper_time,a.oper_desc"
				+ " from t_log_authoper a ,t_sys_auth b,tab_accounts c"
				+ " where 1=1 and a.oper_user=c.acc_oid and a.auth_id = b.auth_id");
		if (!StringUtil.IsEmpty(auth_name))
		{
			sql.append(" and b.auth_name='").append(auth_name.trim()).append("' ");
		}
		// 用户名，关联用户表
		if (!StringUtil.IsEmpty(user_name))
		{
			sql.append(" and c.acc_loginname='").append(user_name.trim()).append("' ");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and a.oper_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and a.oper_time<=").append(endtime);
		}
		sql.append(" order by a.oper_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("authName", StringUtil.getStringValue(rs.getString("auth_name")));
				map.put("userName", StringUtil.getStringValue(rs.getString("oper_user")));
				try
				{
					long opertime = StringUtil.getLongValue(rs.getString("oper_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					map.put("operTime", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("operTime", "");
				}
				catch (Exception e)
				{
					map.put("operTime", "");
				}
				map.put("operDesc", rs.getString("oper_desc"));
				return map;
			}
		});
		return list;
	}

	public int countLogInfo(String auth_name, String user_name, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("LogSuperManageDAO->countLogInfo");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from t_log_authoper a ,t_sys_auth b,tab_accounts c"
				+ " where 1=1  and a.oper_user=c.acc_oid and a.auth_id = b.auth_id ");
		if (!StringUtil.IsEmpty(auth_name))
		{
			sql.append(" and b.auth_name='").append(auth_name.trim()).append("' ");
		}
		// 用户名，关联用户表
		if (!StringUtil.IsEmpty(user_name))
		{
			sql.append(" and c.acc_loginname='").append(user_name.trim()).append("' ");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and a.oper_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and a.oper_time<=").append(endtime);
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
		return maxPage;
	}
	
	
	public List<Map> excelLogInfo(String auth_name, String user_name, String starttime,
			String endtime)
	{
		logger.debug("LogSuperManageDAO->getLogInfo");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.auth_name,c.acc_loginname oper_user,a.oper_time,a.oper_desc"
				+ " from t_log_authoper a ,t_sys_auth b,tab_accounts c"
				+ " where 1=1 and a.oper_user=c.acc_oid and a.auth_id = b.auth_id");
		if (!StringUtil.IsEmpty(auth_name))
		{
			sql.append(" and b.auth_name='").append(auth_name.trim()).append("' ");
		}
		// 用户名，关联用户表
		if (!StringUtil.IsEmpty(user_name))
		{
			sql.append(" and c.acc_loginname='").append(user_name.trim()).append("' ");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and a.oper_time>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and a.oper_time<=").append(endtime);
		}
		sql.append(" order by a.oper_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		if(null!=list && list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
				list.get(i).put("authName",StringUtil.getStringValue(list.get(i).get("auth_name")) );
				list.get(i).put("userName", StringUtil.getStringValue(list.get(i).get("oper_user")));
				try
				{
					long opertime = StringUtil.getLongValue(list.get(i).get("oper_time")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					list.get(i).put("operTime", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					list.get(i).put("operTime", "");
				}
				catch (Exception e)
				{
					list.get(i).put("operTime", "");
				}
				
				list.get(i).put("operDesc", StringUtil.getStringValue(list.get(i).get("oper_desc")));
			}
		}
		return list;
	}


	/**
	 * 根据超级权限编码查询超级权限信息
	 * 
	 * @param authCode
	 *            超级权限简码
	 * @return 超级权限Map
	 */
	public Map<String, Object> querySuperAuth(String authCode)
	{

		String sql = "select t.* from t_sys_auth t where t.auth_code = ?";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select t.auth_name, t.auth_id, t.auth_code from t_sys_auth t where t.auth_code = ?";
		}

		PrepareSQL pSql = new PrepareSQL(sql);
		int index = 0;
		pSql.setString(++index, authCode);
		return jt.queryForMap(pSql.getSQL());
	}

	/**
	 * 记录操作日志
	 * 
	 * @param authId
	 *            超级权限ID,不能为空
	 * @param operUser
	 *            操作人，即当前登录用户ID
	 * @param operDesc
	 *            操作描述
	 */
	public void addSuperAuthLog(long authId, long operUser, String operDesc)
	{
		PrepareSQL pSql = new PrepareSQL(
				"insert into t_log_authoper(auth_id, oper_user, oper_time, oper_desc) values (?, ?, ?, ?)");
		int index = 0;
		pSql.setLong(++index, authId);
		pSql.setLong(++index, operUser);
		pSql.setLong(++index, System.currentTimeMillis() / 1000);
		pSql.setString(++index, operDesc);
		jt.update(pSql.getSQL());
	}
}
