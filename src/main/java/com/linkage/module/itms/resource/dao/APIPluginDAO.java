
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

public class APIPluginDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(APIPluginDAO.class);
	private int queryCount;

	/**
	 * @author 岩
	 * @date 2016-10-13
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getApiPluginList(String classifyName, String creator,String starttime, String endtime,
			String status, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getMqListByMq()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.id,a.classify_name,a.classify_desc,a.create_time,a.creator,a.status ");
		psql.append(" from  tab_plugin_power_classify a ");
		psql.append(" where 1=1 and status ="+status);
		if ((!StringUtil.IsEmpty(classifyName)))
		{
			psql.append(" and  a.classify_name like '%" + classifyName+ "%'");
		}
		if ((!StringUtil.IsEmpty(creator)))
		{
			psql.append("   and a.creator  like '%");
			psql.append(creator + "%'");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			psql.append("   and a.create_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			psql.append("   and a.create_time <= ");
			psql.append(endtime);
		}
		psql.append(" order by a.create_time desc");
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 主题名称
				map.put("classify_id",
						StringUtil.getStringValue(rs.getString("id")));
				// 主题名称
				map.put("classify_name",
						StringUtil.getStringValue(rs.getString("classify_name")));
				// 消费者订阅数量
				map.put("classify_desc",
						StringUtil.getStringValue(rs.getString("classify_desc")));
				DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
						.getString("create_time")) * 1000l);
				// 消息入队
				map.put("create_time",dt.getLongDate());
				// 消息出队
				map.put("creator",
						StringUtil.getStringValue(rs.getString("creator")));
				// 消息出队
				String status = StringUtil.getStringValue(rs.getString("status"));
				if ("1".equals(status)){
					map.put("status","生效" );
				}else{
					map.put("status","失效" );
				}
				
				return map;
			}
		});
		return list;
	}

	/**
	 * @author 岩
	 * @date 2016-10-13
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countApiPluginList(String classifyName, String creator,String starttime, String endtime,
			String status, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("countMqListByMq()");
		PrepareSQL psql = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == 3) {
			psql.append("select count(*) ");
		}
		else {
			psql.append("select count(1) ");
		}
		psql.append(" from  tab_plugin_power_classify a ");
		psql.append(" where 1=1 and status ="+status);
		if ((!StringUtil.IsEmpty(classifyName)))
		{
			psql.append(" and  a.classify_name like '%" + classifyName+ "%'");
		}
		if ((!StringUtil.IsEmpty(creator)))
		{
			psql.append("   and a.creator  like '%");
			psql.append(creator + "%'");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			psql.append("   and a.create_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			psql.append("   and a.create_time <= ");
			psql.append(endtime);
		}
		
		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0)
		{
			maxPage = queryCount / num_splitPage;
		}
		else
		{
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * @author 岩
	 * @date 2016-10-13
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getMqIpPort()
	{
		String strSQL = "select id, mq_address, mq_port from tab_mq_list  ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List<HashMap<String, String>> list = jt.queryForList(psql.getSQL());
		if (list == null)
		{
			list = new ArrayList<HashMap<String, String>>();
		}
		return list;
	}

	public int getQueryCount()
	{
		return queryCount;
	}

	public int addApiPlugin(String classifyId, String classifyName, String classifyDesc,
			long addTime, String creator, String status)
	{
		String sql = null;
		sql = "insert into tab_plugin_power_classify (id,classify_name,classify_desc,create_time,creator,status) values(?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		int id = Integer.parseInt(classifyId);
		int ustatus = Integer.parseInt(status);
		int time1 = (int)addTime;
		psql.setInt(1, id);
		psql.setString(2, classifyName);
		psql.setString(3, classifyDesc);
		psql.setInt(4, time1);
		psql.setString(5, creator);
		psql.setInt(6, ustatus);
		return jt.update(psql.getSQL());
	}

	public int updateApiPlugin(String classifyId,String classifyName, String status, String classifyDesc)
	{
		PrepareSQL psql = new PrepareSQL(
				"update tab_plugin_power_classify set status = ? ,classify_desc = ?, classify_name = ? where id = ?");
		int ustatus = Integer.parseInt(status);
		int id = Integer.parseInt(classifyId);
		psql.setInt(1, ustatus);
		psql.setString(2, classifyDesc);
		psql.setString(3, classifyName);
		psql.setInt(4, id);
		return jt.update(psql.getSQL());
	}

	public void deleteApiPlugin(String classifyId)
	{
		logger.debug("deleteDevice({})", new Object[] { classifyId });
		String sql = null;
		sql = "delete from tab_plugin_power_classify where id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		int id = Integer.parseInt(classifyId);
		psql.setInt(1, id);
		jt.update(psql.getSQL());
	}
}
